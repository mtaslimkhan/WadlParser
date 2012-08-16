package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.XSSchemaSet;

import edu.sjtu.ist.bjggzxb.WadlParser.core.MethodNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourceTypeNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.WadlParser;
import edu.sjtu.ist.bjggzxb.WadlParser.core.WadlXML;
public class SimpleParser extends WadlParser {

	public static String SUCCESS = "success";

	protected ApplicationNodeImpl application;
	protected XSSchemaSet grammarSet;
	protected Document doc;
	protected SAXBuilder builder;

	public ApplicationNodeImpl getApplication() {
		return application;
	}

	public XSSchemaSet getGrammarSet() {
		return grammarSet;
	}

	public SimpleParser() {
		this.application = new ApplicationNodeImpl();
		this.builder = new SAXBuilder();
	}

	public boolean buildDoc(InputStream instream) {
		try {
			doc = builder.build(instream);
			return true;
		} catch (Exception e) {
			doc = null;
			return false;
		}
	}

	public boolean buildDoc(String str) {
		InputStream ins = new ByteArrayInputStream(str.getBytes());
		return buildDoc(ins);
	}

	public String parse() {
		if (doc == null)
			return "Document is not initialized.";
		Element root = doc.getRootElement();
		application = new ApplicationNodeImpl();
		grammarSet = null;
		if (!root.getName().equals(WadlXML.applicationNode))
			return "Document can't be parsed";
		application.setNamespace(root.getAdditionalNamespaces());

		List<Element> elements = root.getChildren();
		System.out.println("Get " + elements.size() + " Elements");
		if (elements.size() == 0)
			return "Document context is empty";

		/*
		 * It is quite important that we parse method, resource_type,
		 * representation, param elements in the beginning, because resource may
		 * contain such element reference, and reference will not be referencing
		 * to a reference node
		 */
		Iterator<Element> eleIter = elements.iterator();
		while (eleIter.hasNext()) {
			Element element = eleIter.next();
			String name = element.getName();
			if (name.equals(WadlXML.grammarsNode)) {
				this.parseGrammars(element);
			} else if (name.equals(WadlXML.docNode)) {
				this.parseDoc(element, application);
			} else if (name.equals(WadlXML.methodNode)) {
				this.parseMethod(element, application);
			} else if (name.equals(WadlXML.resourceTypeNode)) {
				this.parseResourceType(element);
			} else if (name.equals(WadlXML.paramNode)) {
				this.parseParam(element, application);
			} else if (name.equals(WadlXML.representationNode)) {
				this.parseRepresentation(element, application);
			}
		}
		eleIter = elements.iterator();
		while (eleIter.hasNext()) {
			Element element = eleIter.next();
			if (element.getName().equals(WadlXML.resourcesNode)) {
				this.parseResources(element);
			}
		}
		return null;
	}

	protected boolean parseGrammars(Element grammars) {
		XMLOutputter out = new XMLOutputter();
		XSOMParser parser = new XSOMParser();
		List<Element> grammarList = grammars.getChildren();
		Iterator<Element> iter = grammarList.iterator();
		InputStream is;
		while (iter.hasNext()) {
			Element grammar = iter.next();
			String xml = out.outputString(grammar);
			try {
				is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				parser.parse(is);
				is.close();
			} catch (Exception e) {
				System.out.println("Error parsing a grammar element.");
			}
		}
		try {
			grammarSet = parser.getResult();
		} catch (SAXException e) {
			return false;
		}
		return true;
	}

	protected boolean parseDoc(Element doc, GenericNodeImpl parent) {
		String xmlLang = doc.getAttributeValue(WadlXML.doc_xml_lang);
		String title = doc.getAttributeValue(WadlXML.doc_title);
		String text = doc.getText();
		DocNodeImpl docNode = new DocNodeImpl(xmlLang, title, text);
		parent.addDoc(docNode);
		return true;
	}

	protected boolean parseResources(Element resources) {
		String base = resources.getAttributeValue(WadlXML.resources_base);
		if (base == null)
			base = "";
		ResourcesNodeImpl resourcesNode = new ResourcesNodeImpl(base,
				application);
		application.addResources(resourcesNode);

		Iterator<Element> iter = resources.getChildren().iterator();
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.resourceNode)) {
				this.parseResource(child, resourcesNode);
			}
		}
		return true;
	}

	protected ResourceNodeImpl parseResource(Element resource, GenericNodeImpl parent) {
		String path = resource.getAttributeValue(WadlXML.resource_path);
		String id = resource.getAttributeValue(WadlXML.resource_id);
		String type = resource.getAttributeValue(WadlXML.resource_type);
		String queryType = resource
				.getAttributeValue(WadlXML.resource_queryType);
		if (path == null)
			path = "";
		ResourceNodeImpl resourceNode = new ResourceNodeImpl(path, parent);
		resourceNode.setId(id);
		resourceNode.setType(type);
		resourceNode.setQueryType(queryType);
		if (type != null) {
			String[] hrefs = type.split(" ");
			for (String href : hrefs) {
				if (href.startsWith("#"))
					href = href.substring(1);
				ResourceTypeNode resourceType = application
						.getResourceTypeById(href);
				if (resourceType != null)
					resourceNode.addResourceType(resourceType);
			}
		}
		parent.addResource(resourceNode);

		Iterator<Element> iter = resource.getChildren().iterator();
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.resourceNode)) {
				this.parseResource(child, resourceNode);
			} else if (child.getName().equals(WadlXML.paramNode)) {
				this.parseParam(child, resourceNode);
			} else if (child.getName().equals(WadlXML.methodNode)) {
				this.parseMethod(child, resourceNode);
			} else if (child.getName().equals(WadlXML.docNode)) {
				this.parseDoc(child, resourceNode);
			}
		}
		return resourceNode;
	}

	protected MethodNodeImpl parseMethod(Element method, GenericNodeImpl parent) {
		String href = method.getAttributeValue(WadlXML.method_href);
		MethodNodeImpl methodNode;
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			methodNode = (MethodNodeImpl) application
					.getMethodById(href);
			if (methodNode != null) {
				MethodNode newNode = new MethodNodeImpl(methodNode, parent);
				parent.addMethod(newNode);
			} else
				System.out.println("No method " + href + " found.");
		} else {
			String id = method.getAttributeValue(WadlXML.method_id);
			methodNode = new MethodNodeImpl(id, parent);
			methodNode.setName(method.getAttributeValue(WadlXML.method_name));

			parent.addMethod(methodNode);
			Iterator<Element> iter = method.getChildren().iterator();
			while (iter.hasNext()) {
				Element child = iter.next();
				if (child.getName().equals(WadlXML.docNode)) {
					this.parseDoc(child, methodNode);
				} else if (child.getName().equals(WadlXML.requestNode)) {
					this.parseRequest(child, methodNode);
				} else if (child.getName().equals(WadlXML.responseNode)) {
					this.parseResponse(child, methodNode);
				}
			}
		}
		return methodNode;
	}

	protected boolean parseRequest(Element request, MethodNodeImpl parent) {
		RequestNodeImpl requestNode = new RequestNodeImpl(parent);
		parent.setRequest(requestNode);
		Iterator<Element> iter = request.getChildren().iterator();
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.docNode)) {
				this.parseDoc(child, requestNode);
			} else if (child.getName().equals(WadlXML.paramNode)) {
				this.parseParam(child, requestNode);
			} else if (child.getName().equals(WadlXML.representationNode)) {
				this.parseRepresentation(child, requestNode);
			}
		}
		return true;
	}

	protected boolean parseResponse(Element response, MethodNodeImpl parent) {
		ResponseNodeImpl responseNode = new ResponseNodeImpl(parent);
		parent.setResponse(responseNode);
		Iterator<Element> iter = response.getChildren().iterator();
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.docNode)) {
				this.parseDoc(child, responseNode);
			} else if (child.getName().equals(WadlXML.paramNode)) {
				this.parseParam(child, responseNode);
			} else if (child.getName().equals(WadlXML.representationNode)) {
				this.parseRepresentation(child, responseNode);
			}
		}
		return true;
	}

	protected boolean parseParam(Element param, GenericNodeImpl parent) {
		String href = param.getAttributeValue(WadlXML.param_href);
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			ParamNodeImpl paramNode = (ParamNodeImpl) application
					.getParamById(href);
			if (paramNode != null) {
				ParamNodeImpl newNode = new ParamNodeImpl(paramNode, parent);
				parent.addParam(newNode);
			}
		} else {
			String id = param.getAttributeValue(WadlXML.param_id);
			ParamNodeImpl newNode;
			if (id == null) {
				newNode = new ParamNodeImpl(parent);
			} else {
				newNode = new ParamNodeImpl(id, parent);
			}
			newNode.setName(param.getAttributeValue(WadlXML.param_name));
			newNode.setStyle(param.getAttributeValue(WadlXML.param_style));
			newNode.setType(param.getAttributeValue(WadlXML.param_type));
			newNode.setPath(param.getAttributeValue(WadlXML.param_path));
			newNode.setDefaultValue(param
					.getAttributeValue(WadlXML.param_default));
			newNode.setFixedValue(param.getAttributeValue(WadlXML.param_fixed));
			newNode.setRequired(param.getAttributeValue(WadlXML.param_required));
			newNode.setRepeating(param
					.getAttributeValue(WadlXML.param_repeating));
			parent.addParam(newNode);

			Iterator<Element> iter = param.getChildren().iterator();
			while (iter.hasNext()) {
				Element child = iter.next();
				if (child.getName().equals(WadlXML.linkNode)) {
					this.parseLink(child, newNode);
				} else if (child.getName().equals(WadlXML.optionNode)) {
					this.parseOption(child, newNode);
				}
			}
		}
		return true;
	}

	protected boolean parseLink(Element link, ParamNodeImpl parent) {
		String resourceType = link
				.getAttributeValue(WadlXML.link_resource_type);
		String rel = link.getAttributeValue(WadlXML.link_rel);
		String rev = link.getAttributeValue(WadlXML.link_rev);
		LinkNodeImpl linkNode = new LinkNodeImpl();
		linkNode.setResourceType(resourceType);
		linkNode.setRel(rel);
		linkNode.setRev(rev);
		parent.setLink(linkNode);

		Iterator<Element> iter = link.getChildren().iterator();
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.docNode)) {
				this.parseDoc(child, linkNode);
			}
		}
		return true;
	}

	protected boolean parseOption(Element option, ParamNodeImpl parent) {
		String value = option.getAttributeValue(WadlXML.option_value);
		String mediaType = option.getAttributeValue(WadlXML.option_mediaType);
		OptionNodeImpl optionNode = new OptionNodeImpl();
		optionNode.setValue(value);
		optionNode.setMediaType(mediaType);
		parent.addOption(optionNode);
		return true;
	}

	protected boolean parseRepresentation(Element representation,
			GenericNodeImpl parent) {
		String href = representation
				.getAttributeValue(WadlXML.representation_href);
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			RepresentationNodeImpl representationNode = (RepresentationNodeImpl) application
					.getRepresentationById(href);
			if (representationNode != null) {
				RepresentationNodeImpl newNode = new RepresentationNodeImpl(
						representationNode, parent);
				parent.addRepresentation(newNode);
			}
		} else {
			String id = representation.getAttributeValue(WadlXML.param_id);
			RepresentationNodeImpl newNode;
			if (id == null) {
				newNode = new RepresentationNodeImpl(parent);
			} else {
				newNode = new RepresentationNodeImpl(id, parent);
			}
			newNode.setElement(representation
					.getAttributeValue(WadlXML.representation_element));
			newNode.setMediaType(representation
					.getAttributeValue(WadlXML.representation_mediaType));
			newNode.setProfile(representation
					.getAttributeValue(WadlXML.representation_profile));
			parent.addRepresentation(newNode);

			Iterator<Element> iter = representation.getChildren().iterator();
			while (iter.hasNext()) {
				Element child = iter.next();
				if (child.getName().equals(WadlXML.docNode)) {
					this.parseDoc(child, newNode);
				} else if (child.getName().equals(WadlXML.paramNode)) {
					this.parseParam(child, newNode);
				}
			}
		}
		return true;
	}

	protected boolean parseResourceType(Element resourceType) {
		String id = resourceType.getAttributeValue(WadlXML.resource_type_id);
		ResourceTypeNodeImpl rtNode = new ResourceTypeNodeImpl(id, application);
		Iterator<Element> iter = resourceType.getChildren().iterator();
		application.addResourceType(rtNode);
		while (iter.hasNext()) {
			Element child = iter.next();
			if (child.getName().equals(WadlXML.docNode)) {
				this.parseDoc(child, rtNode);
			} else if (child.getName().equals(WadlXML.paramNode)) {
				this.parseParam(child, rtNode);
			} else if (child.getName().equals(WadlXML.methodNode)) {
				this.parseMethod(child, rtNode);
			} else if (child.getName().equals(WadlXML.resourceNode)) {
				this.parseMethod(child, rtNode);
			}
		}
		return true;
	}
}
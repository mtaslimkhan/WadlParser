package edu.sjtu.ist.bjggzxb.WadlParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.XSSchemaSet;

public class WadlParser {

	public static String SUCCESS = "success";

	private ApplicationNode application;
	private XSSchemaSet grammarSet;
	private Document doc;
	private SAXBuilder builder;

	public ApplicationNode getApplication() {
		return application;
	}

	public XSSchemaSet getGrammarSet() {
		return grammarSet;
	}

	public WadlParser() {
		this.application = new ApplicationNode();
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
	
	public boolean buildDoc(String str){
		InputStream ins = new ByteArrayInputStream(str.getBytes());
		return buildDoc(ins);
	}

	public String parse() {
		if (doc == null)
			return "Document is not initialized.";
		Element root = doc.getRootElement();
		application = new ApplicationNode();
		grammarSet = null;
		if (!root.getName().equals(WadlXML.applicationNode))
			return "Document can't be parsed";
		List<Attribute> appAttributes = root.getAttributes();
		if (appAttributes.size() == 0) {
			System.out.println("Document namespace not defined.");
			// return "Document namespace not defined.";
		}
		application.setNamespace(root.getAdditionalNamespaces());
		
		/*
		 * This part is wrong code
		 */
		
		// Iterator<Attribute> attIter = appAttributes.iterator();
		// while (attIter.hasNext()) {
		// Attribute attri = attIter.next();
		// String name = attri.getName();
		// String value = attri.getValue();
		// if (name.equals(WadlXML.application_xmlns)
		// || name.equals(WadlXML.application_xmlns_xsd)
		// || name.equals(WadlXML.application_xmlns_xsi)
		// || name.equals(WadlXML.application_xsi_schemaLocation)
		// || name.startsWith(WadlXML.application_xmlns_extNs)) {
		// application.addNamespace(new NamespaceAttribute(name, value));
		// }
		// }
		
		
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

	private boolean parseGrammars(Element grammars) {
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

	private boolean parseDoc(Element doc, GenericNode parent) {
		String xmlLang = doc.getAttributeValue(WadlXML.doc_xml_lang);
		String title = doc.getAttributeValue(WadlXML.doc_title);
		String text = doc.getText();
		DocNode docNode = new DocNode(xmlLang, title, text);
		parent.addDoc(docNode);
		return true;
	}

	private boolean parseResources(Element resources) {
		String base = resources.getAttributeValue(WadlXML.resources_base);
		if (base == null)
			base = "";
		ResourcesNode resourcesNode = new ResourcesNode(base, application);
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

	private boolean parseResource(Element resource, GenericNode parent) {
		String path = resource.getAttributeValue(WadlXML.resource_path);
		String id = resource.getAttributeValue(WadlXML.resource_id);
		String type = resource.getAttributeValue(WadlXML.resource_type);
		String queryType = resource
				.getAttributeValue(WadlXML.resource_queryType);
		if (path == null)
			path = "";
		ResourceNode resourceNode = new ResourceNode(path, parent);
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
		return true;
	}

	private boolean parseMethod(Element method, GenericNode parent) {
		String href = method.getAttributeValue(WadlXML.method_href);
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			MethodNode methodNode = application.getMethodById(href);
			if (methodNode != null) {
				MethodNode newNode = new MethodNode(methodNode,
						parent);
				parent.addMethod(newNode);
			} else
				System.out.println("No method " + href + " found.");
		} else {
			String id = method.getAttributeValue(WadlXML.method_id);
			MethodNode newNode = new MethodNode(id, parent);
			newNode.setName(method.getAttributeValue(WadlXML.method_name));

			parent.addMethod(newNode);
			Iterator<Element> iter = method.getChildren().iterator();
			while (iter.hasNext()) {
				Element child = iter.next();
				if (child.getName().equals(WadlXML.docNode)) {
					this.parseDoc(child, newNode);
				} else if (child.getName().equals(WadlXML.requestNode)) {
					this.parseRequest(child, newNode);
				} else if (child.getName().equals(WadlXML.responseNode)) {
					this.parseResponse(child, newNode);
				}
			}
		}
		return true;
	}

	private boolean parseRequest(Element request, MethodNode parent) {
		RequestNode requestNode = new RequestNode(parent);
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

	private boolean parseResponse(Element response, MethodNode parent) {
		ResponseNode responseNode = new ResponseNode(parent);
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

	private boolean parseParam(Element param, GenericNode parent) {
		String href = param.getAttributeValue(WadlXML.param_href);
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			ParamNode paramNode = application.getParamById(href);
			if (paramNode != null) {
				ParamNode newNode = new ParamNode(paramNode,
						parent);
				parent.addParam(newNode);
			}
		} else {
			String id = param.getAttributeValue(WadlXML.param_id);
			ParamNode newNode;
			if (id == null) {
				newNode = new ParamNode(parent);
			} else {
				newNode = new ParamNode(id, parent);
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

	private boolean parseLink(Element link, ParamNode parent) {
		String resourceType = link
				.getAttributeValue(WadlXML.link_resource_type);
		String rel = link.getAttributeValue(WadlXML.link_rel);
		String rev = link.getAttributeValue(WadlXML.link_rev);
		LinkNode linkNode = new LinkNode();
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

	private boolean parseOption(Element option, ParamNode parent) {
		String value = option.getAttributeValue(WadlXML.option_value);
		String mediaType = option.getAttributeValue(WadlXML.option_mediaType);
		OptionNode optionNode = new OptionNode();
		optionNode.setValue(value);
		optionNode.setMediaType(mediaType);
		parent.addOption(optionNode);
		return true;
	}

	private boolean parseRepresentation(Element representation,
			GenericNode parent) {
		String href = representation
				.getAttributeValue(WadlXML.representation_href);
		if (href != null) {
			if (href.startsWith("#"))
				href = href.substring(1);
			RepresentationNode representationNode = application
					.getRepresentationById(href);
			if (representationNode != null) {
				RepresentationNode newNode = new RepresentationNode(
						representationNode, parent);
				parent.addRepresentation(newNode);
			}
		} else {
			String id = representation.getAttributeValue(WadlXML.param_id);
			RepresentationNode newNode;
			if (id == null) {
				newNode = new RepresentationNode(parent);
			} else {
				newNode = new RepresentationNode(id, parent);
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

	private boolean parseResourceType(Element resourceType) {
		String id = resourceType.getAttributeValue(WadlXML.resource_type_id);
		ResourceTypeNode rtNode = new ResourceTypeNode(id, application);
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
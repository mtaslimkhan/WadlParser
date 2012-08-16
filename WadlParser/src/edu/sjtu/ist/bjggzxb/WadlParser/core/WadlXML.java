package edu.sjtu.ist.bjggzxb.WadlParser.core;

public class WadlXML {
	public static final String applicationNode = "application";
	public static final String resourcesNode = "resources";
	public static final String resourceNode = "resource";
	public static final String resourceTypeNode = "resource_type";
	public static final String requestNode = "request";
	public static final String responseNode = "response";
	public static final String methodNode = "method";
	public static final String paramNode = "param";
	public static final String docNode = "doc";
	public static final String grammarsNode = "grammars";
	public static final String includeNode = "include";
	public static final String representationNode = "representation";
	public static final String faultNode = "fault";
	public static final String optionNode = "option";
	public static final String linkNode = "link";

	// allowed child elements
	public static final String[] applicationChildren = { resourcesNode,
			grammarsNode, resourceTypeNode, methodNode, representationNode,
			paramNode, faultNode, docNode };
	public static final String[] resourcesChildren = { resourceNode, docNode };
	public static final String[] grammarsChildren = { includeNode, docNode };
	public static final String[] includeChildren = { docNode };
	public static final String[] optionChildren = { docNode };
	public static final String[] linkChildren = { docNode };
	public static final String[] resourceChildren = { paramNode, methodNode,
			resourceNode, docNode };
	public static final String[] resource_typeChildren = { paramNode,
			methodNode, docNode };
	public static final String[] methodChildren = { requestNode, responseNode,
			docNode };
	public static final String[] requestChildren = { representationNode,
			paramNode, docNode };
	public static final String[] responseChildren = { representationNode,
			faultNode, paramNode, docNode };
	public static final String[] representationChildren = { paramNode, docNode };
	public static final String[] paramChildren = { optionNode, linkNode,
			docNode };
	public static final String[] faultChildren = { paramNode, docNode };

	// list of attributes
	public static final String application_xmlns_xsi = "xmlns:xsi";
	public static final String application_xmlns_xsd = "xmlns:xsd";
	public static final String application_xsi_schemaLocation = "xsi:schemaLocation";
	public static final String application_xmlns = "xmlns";
	public static final String application_xmlns_extNs = "xmlns:"; 
	public static final String doc_xml_lang = "xml:lang";
	public static final String doc_title = "title";
	public static final String include_href = "href";
	public static final String resources_base = "base";
	public static final String resource_id = "id";
	public static final String resource_path = "path";
	public static final String resource_type = "type";
	public static final String resource_queryType = "queryType";
	public static final String resource_type_id = "id";
	public static final String method_href = "href";
	public static final String method_id = "id";
	public static final String method_name = "name";
	public static final String representation_href = "href";
	public static final String representation_id = "id";
	public static final String representation_mediaType = "mediaType";
	public static final String representation_element = "element";
	public static final String representation_profile = "profile";
	public static final String representation_status = "status";
	public static final String fault_href = "href";
	public static final String fault_id = "id";
	public static final String fault_mediaType = "mediaType";
	public static final String fault_element = "element";
	public static final String fault_profile = "profile";
	public static final String fault_status = "status";
	public static final String param_id = "id";
	public static final String param_href = "href";
	public static final String param_name = "name";
	public static final String param_style = "style";
	public static final String param_type = "type";
	public static final String param_default = "default";
	public static final String param_path = "path";
	public static final String param_required = "required";
	public static final String param_repeating = "repeating";
	public static final String param_fixed = "fixed";
	public static final String option_value = "value";
	public static final String option_mediaType = "mediaType";
	public static final String link_resource_type = "resource_type";
	public static final String link_rel = "rel";
	public static final String link_rev = "rev";

	// allowed attributes
	public static final String[] applicationAttributes = {
			application_xmlns_xsi, application_xmlns_xsd,
			application_xsi_schemaLocation, application_xmlns };
	public static final String[] docAttributes = { doc_title, doc_xml_lang };
	public static final String[] includeAttributes = { include_href };
	public static final String[] resourcesAttributes = { resources_base };
	public static final String[] resourceAttributes = { resource_id,
			resource_path, resource_type, resource_queryType };
	public static final String[] resource_typeAttributes = { resource_type_id };
	public static final String[] methodAttributes = { method_href, method_id,
			method_name };
	public static final String[] representationAttributes = {
			representation_element, representation_href, representation_id,
			representation_mediaType, representation_profile,
			representation_status };
	public static final String[] faultAttributes = { fault_element, fault_href,
			fault_id, fault_mediaType, fault_profile, fault_status };
	public static final String[] paramAttributes = { param_id, param_href,
			param_name, param_style, param_type, param_default, param_path,
			param_required, param_repeating, param_fixed };
	public static final String[] optionAttributes = { option_value };
	public static final String[] linkAttributes = { link_rel,
			link_resource_type, link_rev };

	// default wadl namespace and schema stuff
	public static final String xmlns_xsi = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String xmlns_xsd = "http://www.w3.org/2001/XMLSchema";
	public static final String xsi_schemaLocation = "http://wadl.dev.java.net/wadl/2009/02 wadl.xsd";
	public static final String xmlns = "http://research.sun.com/wadl/2006/10";
}

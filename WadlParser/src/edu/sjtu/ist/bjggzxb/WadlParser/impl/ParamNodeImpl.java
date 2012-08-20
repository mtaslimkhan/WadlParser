/*
	A param element describes a parameterized component of its parent element. 
	A param element can either be a parameter definition or a reference to a 
	parameter defined elsewhere.
	
	Parameter Reference
	
	A param reference element is a param element that has an href attribute 
	whose type is xsd:anyURI. The value of the href attribute is a cross 
	reference to a param definition element. A param reference element MUST 
	NOT have any other WADL-defined attributes or contain any WADL-defined 
	child elements.

	This form of param element may be used to reduce duplication when the same 
	parameter applies to more than one parent.

	Parameter Definition

	A param definition element describes a parameterized component of its 
	parent element and may be a child of a resource, application, request, 
	response, or a representation element. A param definition element has zero 
	or more doc child elements, zero or more option child elements, an optional 
	link child element and has the following attributes:

		id
		    An optional identifier that may be used to refer to a parameter 
		    definition using a URI reference.
		name
		    The name of the parameter as an xsd:NMTOKEN. Required.
		style
		    Indicates the parameter style, following table lists the allowed 
		    values and shows the context(s) in which each value may be used.
		type
		    Optionally indicates the type of the parameter as an XML qualified 
		    name, defaults to xsd:string.
		default
		    Optionally provides a value that is considered identical to an 
		    unspecified parameter value.
		path
		    When the parent element is a representation element, this attribute 
		    optionally provides a path to the value of the parameter within the 
		    representation. For XML representations, use of XPath 1.0 is recommended.
		required
		    Optionally indicates whether the parameter is required to be present 
		    or not, defaults to false (parameter not required).
		repeating
		    Optionally indicates whether the parameter is single valued or may 
		    have multiple values, defaults to false (parameter is single valued).
		fixed
		    Optionally provides a fixed value for the parameter. 
		
		Note that some combinations of the above attributes might not make sense 
		in all cases. E.g. matrix URI parameters are normally optional so a param 
		element with a style value of 'matrix' and a required value of 'true' 
		might be unwise.

Value 		Parent Element(s) of param 						Usage
matrix 		resource 										Specifies a matrix URI component.
header 		resource, resource_type, request or response 	Specifies a HTTP header that pertains to the HTTP request (resource or request) 
																or HTTP response (response)
query 		resource, resource_type or request 				Specifies a URI query parameter represented according to the rules for the query 
																component media type specified by the queryType attribute.
query 		representation 									Specifies a component of the representation as a name value pair formatted 
																according to the rules of the media type. Typically used with media type 
																'application/x-www-form-urlencoded' or 'multipart/form-data'.
template 	resource 										The parameter is represented as a string encoding of the parameter value and is
																substituted into the value of the path attribute of the resource element.
plain 		representation 									Specifies a component of the representation formatted as a string encoding of 
																the parameter value according to the rules of the media type.
 
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.List;
import java.util.ArrayList;

import edu.sjtu.ist.bjggzxb.WadlParser.core.LinkNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.OptionNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ParamNode;

public class ParamNodeImpl extends GenericNodeImpl implements ParamNode{

	public static String MATRIX_STYLE = "matrix";
	public static String HEADER_STYLE = "header";
	public static String QUERY_STYLE = "query";
	public static String TEMPLATE_STYLE = "template";
	public static String PLAIN_STYLE = "plain";

	private final boolean href;
	private final String id;
	private final ParamNodeImpl hrefNode;

	private String name;
	private String style;
	private String type;
	private String defaultValue;
	private String fixedValue;
	private String path;
	private boolean required;
	private boolean repeating;

	private LinkNode linkNode;
	private List<OptionNode> optionNodes = new ArrayList<OptionNode>();

	/**
	 * tells whether two ParamNode objects have the same "name" attribute
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ParamNodeImpl) {
			ParamNodeImpl param = (ParamNodeImpl) other;
			if (id != null && param.getId() != null) {
				if (id.equals(param.getId()))
					return true;
			} else if (getName() != null && getName().equals(param.getName()))
				return true;
		}
		return false;
	}

	public ParamNodeImpl(GenericNodeImpl parent) {
		this.href = false;
		this.id = null;
		this.hrefNode = null;
		super.parentNode = parent;
	}

	public ParamNodeImpl(String id, GenericNodeImpl parent) {
		this.href = false;
		this.id = id;
		this.hrefNode = null;
		super.parentNode = parent;
	}

	public ParamNodeImpl(ParamNodeImpl other, GenericNodeImpl parent) {
		this.href = true;
		this.id = null;
		this.hrefNode = other;
		super.parentNode = parent;
	}

	protected boolean isHref() {
		return href;
	}

	public String getId() {
		return id;
	}

	public ParamNodeImpl getHrefNode() {
		return hrefNode;
	}

	public String getName() {
		if (href)
			return hrefNode.getName();
		else
			return name;
	}

	protected void setName(String name) {
		if (!href)
			this.name = name;
	}

	protected boolean addOption(OptionNode option) {
		if (href)
			return false;
		else if (!optionNodes.contains(option)) {
			optionNodes.add(option);
			return true;
		} else
			return false;
	}

	public List<OptionNode> getAllOptions() {
		if (href)
			return hrefNode.getAllOptions();
		else
			return optionNodes;
	}

	protected void setLink(LinkNode link) {
		if (!href)
			this.linkNode = link;
	}

	public LinkNode getLink() {
		if (href)
			return hrefNode.getLink();
		else
			return linkNode;
	}

	public String getStyle() {
		if (href)
			return hrefNode.getStyle();
		else
			return style;
	}

	protected void setStyle(String style) {
		if (!href)
			this.style = style;
	}

	public String getType() {
		if (href)
			return hrefNode.getType();
		else
			return type;
	}

	protected void setType(String type) {
		if (!href)
			this.type = type;
	}

	public String getDefaultValue() {
		if (href)
			return hrefNode.getDefaultValue();
		else
			return defaultValue;
	}

	protected void setDefaultValue(String defaultValue) {
		if (!href)
			this.defaultValue = defaultValue;
	}

	public String getFixedValue() {
		if (href)
			return hrefNode.getFixedValue();
		else
			return fixedValue;
	}

	protected void setFixedValue(String fixedValue) {
		if (!href)
			this.fixedValue = fixedValue;
	}

	public String getPath() {
		if (href)
			return hrefNode.getPath();
		else
			return path;
	}

	protected void setPath(String path) {
		if (!href)
			this.path = path;
	}

	public boolean isRequired() {
		if (href)
			return hrefNode.isRequired();
		else
			return required;
	}

	protected void setRequired(boolean required) {
		if (!href)
			this.required = required;
	}

	protected void setRequired(String required) {
		if (!href) {
			if (required == null)
				return;
			if (required.equals("true"))
				this.required = true;
			else
				this.required = false;
		}
	}

	public boolean isRepeating() {
		if (!href)
			return hrefNode.isRepeating();
		else
			return repeating;
	}

	protected void setRepeating(boolean repeating) {
		if (href)
			this.repeating = repeating;
	}

	protected void setRepeating(String repeating) {
		if (href) {
			if (repeating == null)
				return;
			if (repeating.equals("true"))
				this.required = true;
			else
				this.required = false;
		}
	}
}
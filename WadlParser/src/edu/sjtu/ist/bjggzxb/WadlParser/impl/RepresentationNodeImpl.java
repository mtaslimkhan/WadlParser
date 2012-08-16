/*
	A representation element describes a representation of a resource's state. 
	A representation element can either be a representation definition or a 
	reference to a representation defined elsewhere.

	Representation Reference
		A representation reference element can be a child of a request or response 
		element. It has a href attribute of type xsd:anyURI. The value of the href 
		attribute is a cross reference to a representation definition element. A 
		representation reference element MUST NOT have any other WADL-defined 
		attributes or contain any WADL-defined child elements.

		This form of representation element may be used to reduce duplication when 
		the same representation is used in multiple locations.

	Representation Definition
		A representation definition element can be a child of a request, response 
		or application element. It has the following attributes:

		id
		    An identifier for the representation, required for globally defined 
		    representations, not allowed on locally embedded representations. 
		    Representations are identified by an XML ID and are referred to using 
		    a URI reference.
		mediaType
		    Indicates the media type of the representation. Media ranges (e.g. text/*) 
		    are acceptable and indicate that any media type in the specified 
		    range is supported.
		element
		    For XML-based representations, specifies the qualified name of the 
		    root element as described within the grammars section.
		profile
		    Similar to the HTML profile attribute, gives the location of one or 
		    more meta data profiles, separated by white space. The meta-data 
		    profiles define the meaning of the rel and rev attributes of descendent 
		    link elements.

		In addition to the attributes listed above, a representation definition element 
		can have zero or more child doc elements and param elements.
 */
package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.List;
import java.util.ArrayList;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ParamNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.RepresentationNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.WadlException;

public class RepresentationNodeImpl extends GenericNodeImpl implements RepresentationNode{

	private final boolean href;
	private final String id;
	private final RepresentationNodeImpl hrefNode;

	private String mediaType;
	private String element;
	private String profile;
	private List<DocNode> docNodes = new ArrayList<DocNode>();
	private List<ParamNode> paramNodes = new ArrayList<ParamNode>();

	public RepresentationNodeImpl(GenericNodeImpl parent) {
		this.href = false;
		this.id = null;
		this.hrefNode = null;
		super.parentNode = parent;
	}

	public RepresentationNodeImpl(String id, GenericNodeImpl parent) {
		if (id == null)
			throw new WadlException("Representation id is null.");
		this.href = false;
		this.id = id;
		this.hrefNode = null;
		super.parentNode = parent;
	}

	public RepresentationNodeImpl(RepresentationNodeImpl other, GenericNodeImpl parent) {
		if (other == null)
			throw new WadlException("Representation href node is null.");
		this.href = true;
		this.id = null;
		this.hrefNode = other;
		super.parentNode = parent;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof RepresentationNodeImpl) {
			RepresentationNodeImpl rep = (RepresentationNodeImpl) other;
			if (id != null && rep.id != null) {
				if (id.equals(rep.id))
					return true;
			} else {
				try {
					if (rep.getElement().equals(element)
							&& rep.getMediaType().equals(mediaType))
						return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}

	public String getId() {
		if (href)
			return hrefNode.getId();
		else
			return id;
	}

	public String getMediaType() {
		if (href)
			return hrefNode.getMediaType();
		else
			return mediaType;
	}

	protected void setMediaType(String mediaType) {
		if (!href)
			this.mediaType = mediaType;
	}

	public String getElement() {
		if (href)
			return hrefNode.getElement();
		else
			return element;
	}

	protected void setElement(String element) {
		if (!href)
			this.element = element;
	}

	public String getProfile() {
		if (href)
			return hrefNode.getProfile();
		else
			return profile;
	}

	protected void setProfile(String profile) {
		if (!href)
			this.profile = profile;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (!href) {
			docNodes.add(doc);
			return true;
		}
		return false;
	}

	public List<DocNode> getAllDocs() {
		if (href)
			return hrefNode.getAllDocs();
		else
			return docNodes;
	}

	@Override
	protected boolean addParam(ParamNode param) {
		if (!href || paramNodes.contains(param))
			return false;
		paramNodes.add(param);
		return true;
	}

	public List<ParamNode> getAllParams() {
		if (href)
			return hrefNode.getAllParams();
		else
			return paramNodes;
	}
}

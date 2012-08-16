/*
	A request element describes the input to be included when applying an HTTP method to a 
	resource. A request element has no attributes and may contain the following child elements:

    Zero or more doc elements.

    Zero or more representation elements. Note that use of representation elements is confined 
    to HTTP methods that accept an entity body in the request (e.g., PUT or POST). Sibling 
    representation elements represent logically equivalent alternatives, e.g., a particular 
    resource might support multiple XML grammars for a particular request.

    Zero or more param elements with one of the following values for their style attribute:

	    query
	        Specifies a URI query parameter for all methods that apply to this resource
	    header
	        Specifies a HTTP header for use in the request
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ParamNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.RepresentationNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.RequestNode;

public class RequestNodeImpl extends GenericNodeImpl implements RequestNode{

	private List<DocNode> docNodes = new ArrayList<DocNode>();
	private List<ParamNode> paramNodes = new ArrayList<ParamNode>();
	private List<RepresentationNode> representationNodes = new ArrayList<RepresentationNode>();

	public RequestNodeImpl(MethodNodeImpl method) {
		super.parentNode = method;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (doc != null) {
			this.docNodes.add(doc);
			return true;
		}
		return false;
	}

	@Override
	public List<DocNode> getAllDocs() {
		return this.docNodes;
	}

	@Override
	protected boolean addParam(ParamNode param) {
		if (!paramNodes.contains(param)) {
			paramNodes.add(param);
			return true;
		}
		return false;
	}

	@Override
	public List<ParamNode> getAllParams() {
		return paramNodes;
	}

	@Override
	protected boolean addRepresentation(RepresentationNode representation) {
		if (!representationNodes.contains(representation)) {
			representationNodes.add(representation);
			return true;
		}
		return false;
	}

	@Override
	public List<RepresentationNode> getAllRepresentations() {
		return representationNodes;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof RequestNodeImpl) {
			RequestNodeImpl request = (RequestNodeImpl) other;
			if (paramNodes.size() != request.getAllParams().size()
					|| representationNodes.size() != request
							.getAllRepresentations().size())
				return false;
			Iterator<ParamNode> piter = request.getAllParams().iterator();
			while (piter.hasNext()) {
				ParamNode param = piter.next();
				if (!paramNodes.contains(param))
					return false;
			}
			Iterator<RepresentationNode> riter = request
					.getAllRepresentations().iterator();
			while (riter.hasNext()) {
				RepresentationNode representation = riter.next();
				if (!representationNodes.contains(representation))
					return false;
			}
			return true;
		}
		return false;
	}
}
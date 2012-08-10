/*
	A response element describes the output that results from performing an HTTP method on a 
	resource. It has the following attributes:

	status
	    Optionally present on responses, provides a list of HTTP status codes associated with 
	    a particular response.

	A response element may contain the following child elements:

	    Zero or more doc elements.
	    Zero or more representation elements, each of which describes a resource representation 
	    that may result from performing the method. Sibling representation elements indicate 
	    logically equivalent alternatives; normal HTTP content negotiation mechanisms may be 
	    used to select a particular alternative.

	    Zero or more param elements with a value of 'header' for their style attribute, each 
	    of which specifies the details of a HTTP header for the response

 */

package edu.sjtu.ist.bjggzxb.WadlParser;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ResponseNode extends GenericNode {

	private List<DocNode> docNodes = new ArrayList<DocNode>();
	private List<ParamNode> paramNodes = new ArrayList<ParamNode>();
	private List<RepresentationNode> representationNodes = new ArrayList<RepresentationNode>();
	private int status;

	public ResponseNode(MethodNode method) {
		this.paramNodes = new ArrayList<ParamNode>();
		this.representationNodes = new ArrayList<RepresentationNode>();
		super.parentNode = method;
	}

	protected void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (doc != null) {
			this.docNodes.add(doc);
			return true;
		}
		return false;
	}

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

	public List<RepresentationNode> getAllRepresentations() {
		return representationNodes;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof RequestNode) {
			ResponseNode response = (ResponseNode) other;
			if (paramNodes.size() != response.getAllParams().size()
					|| representationNodes.size() != response
							.getAllRepresentations().size())
				return false;
			Iterator<ParamNode> piter = response.getAllParams().iterator();
			while (piter.hasNext()) {
				ParamNode param = piter.next();
				if (!paramNodes.contains(param))
					return false;
			}
			Iterator<RepresentationNode> riter = response
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
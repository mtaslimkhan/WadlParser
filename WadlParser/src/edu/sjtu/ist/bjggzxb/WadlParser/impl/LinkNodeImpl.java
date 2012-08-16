/*	A link element is used to identify links to resources within representations. 
 *	A link element is a child of a param element whose path attribute identifies 
 *	the portion of its parent representation that contains a link URI.
 *
 *	A link element contains zero or more doc elements (see section 2.3) and has 
 *	the following attributes:
 *
 *	resource_type
 *   	An optional cross reference (see section 2.1) to a resource_type element 
 *		that defines the capabilities of the resource that the link identifies.
 *	rel
 *   	An optional token that identifies the relationship of the resource 
 *		identified by the link to the resource whose representation the link is 
 *		embedded in. The value is scoped by the value of the ancestor representation 
 *		element's profile attribute.
 *	rev
 *   	An optional token that identifies the relationship of the resource whose 
 *		representation the link is embedded in to the resource identified by the link. 
 *		This is the reverse relationship to that identified by the rel attribute. 
 *		The value is scoped by the value of the ancestor representation element's 
 *		profile attribute. 
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.ArrayList;
import java.util.List;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.LinkNode;

public class LinkNodeImpl extends GenericNodeImpl implements LinkNode{

	private List<DocNode> docNodes = new ArrayList<DocNode>();
	private String resourceType;
	private String rel;
	private String rev;

	protected void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceType() {
		return resourceType;
	}

	protected void setRel(String rel) {
		this.rel = rel;
	}

	public String getRel() {
		return rel;
	}

	protected void setRev(String rev) {
		this.rev = rev;
	}

	public String getRev() {
		return rev;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (doc != null){
			this.docNodes.add(doc);
			return true;
		}
		return false;
	}

	public List<DocNode> getAllDocs() {
		return this.docNodes;
	}
}
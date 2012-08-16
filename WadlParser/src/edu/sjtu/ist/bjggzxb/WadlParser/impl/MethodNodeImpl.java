/*
	A method element describes the input to and output from an HTTP protocol method that 
	may be applied to a resource. A method element can either be a method definition or 
	a reference to a method defined elsewhere.
	
	Method Reference
		A method reference element is a child of a resource element that has an href 
		attribute whose type is xsd:anyURI. The value of the href attribute is a cross 
		reference to a method definition element. A method reference element MUST NOT 
		have any other WADL-defined attributes or contain any WADL-defined child elements.

	Method Definition
		A method definition element is a child of a resource or application element and 
		has the following attributes:

		name
		    Indicates the HTTP method used.
		id
		    An identifier for the method, required for globally defined methods, not 
		    allowed on locally embedded methods. Methods are identified by an XML ID and 
		    are referred to using a URI reference.

		It is permissible to have multiple child method elements that have the same value 
		of the name attribute for a given resource; such siblings represent distinct 
		variations of the same HTTP method and will typically have different input data.

		A method element has the following child elements:

		doc
		    Zero or more doc elements.
		request
		    Describes the input to the method as a collection of parameters and an optional 
		    resource representation - see section 2.9 .
		response
		    Zero or more response elements that describe the possible outputs of the method 
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.List;
import java.util.ArrayList;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.MethodNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.RequestNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResponseNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.WadlException;
import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.BaseElement;

public class MethodNodeImpl extends GenericNodeImpl implements MethodNode{

	private final boolean href;
	private final String id;
	private final MethodNodeImpl hrefNode;

	private String name;
	private RequestNode requestNode;
	private ResponseNode responseNode;
	private List<DocNode> docNodes = new ArrayList<DocNode>();

	/*
	 * xml schema defined element
	 */
	protected BaseElement elementDecl;
	
	/*
	 * from doc
	 */
	private String miniDes = "none";
	private String des = "none";
	private String docName = "known";
	
	public String getMiniDes() {
		return miniDes;
	}

	public String getDes() {
		return des;
	}

	public String getDocName() {
		return docName;
	}

	public MethodNodeImpl(GenericNodeImpl parent) {
		href = false;
		id = null;
		hrefNode = null;
		parentNode = parent;
	}

	public MethodNodeImpl(String id, GenericNodeImpl parent) {
		href = false;
		this.id = id;
		hrefNode = null;
		parentNode = parent;
	}

	public MethodNodeImpl(MethodNodeImpl other, GenericNodeImpl parent)
			throws WadlException {
		if (other == null)
			throw new WadlException("Method reference is null pointer.");
		href = true;
		id = null;
		hrefNode = other;
		parentNode = parent;
	}

	protected void iniDoc() {
		
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof MethodNodeImpl) {
			MethodNodeImpl method = (MethodNodeImpl) other;
			if (href && method.isHref() && hrefNode == method.getHrefNode()) {
				return true;
			} else if (id != null && method.getId() != null) {
				if (id.equals(method.getId()))
					return true;
			} else {
				return this.hasSameContext(method);
			}
		}
		return false;
	}

	private boolean hasSameContext(MethodNodeImpl method) {
		try {
			if (!this.getName().equals(method.getName()))
				return false;
			if (!this.getRequest().equals(method.getRequest()))
				return false;
			if (!this.getResponse().equals(method.getResponse()))
				return false;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@Override
	public BaseElement getElementDecl(){
		return elementDecl;
	}

	protected boolean isHref() {
		return href;
	}

	protected MethodNodeImpl getHrefNode() {
		return hrefNode;
	}

	protected void setName(String name) {
		if (!href)
			this.name = name;
	}

	@Override
	public String getName() {
		if (href)
			return hrefNode.getName();
		else
			return name;
	}

	@Override
	public String getId() {
		return id;
	}

	protected void setRequest(RequestNodeImpl request) {
		if (!href)
			requestNode = request;
	}

	@Override
	public RequestNode getRequest() {
		if (href)
			return hrefNode.getRequest();
		else
			return requestNode;
	}

	protected void setResponse(ResponseNodeImpl response) {
		if (!href)
			responseNode = response;
	}

	@Override
	public ResponseNode getResponse() {
		if (href)
			return hrefNode.getResponse();
		else
			return responseNode;
	}

	protected boolean addDoc(DocNode doc) {
		if (!href && doc != null) {
			this.docNodes.add(doc);
			return true;
		}
		return false;
	}

	@Override
	public List<DocNode> getAllDocs() {
		if (href)
			return hrefNode.getAllDocs();
		else
			return this.docNodes;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
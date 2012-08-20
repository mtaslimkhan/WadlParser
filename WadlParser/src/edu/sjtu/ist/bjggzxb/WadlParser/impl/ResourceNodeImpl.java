/*
	A resource element describes a set of resources, each identified by a URI that follows 
	a common pattern. A resource element has the following attributes:

	id
    	An optional attribute of type xsd:ID that identifies the resource element.
	path
    	An optional attribute of type xsd:string. If present, it provides a relative URI 
    	template for the identifier of the resource. The resource's base URI is given by 
    	the resource element's parent resource or resources element.
	type
    	An optional attribute whose type is a space-separated list of of xsd:anyURI. Each 
    	value in the list is a cross reference that identifies a resource_type element that 
    	defines a set of methods supported by the resource.
	queryType
    	Defines the media type for the query component of the resource URI. Defaults to 
    	'application/x-www-form-urlencoded' if not specified which results in query strings 
    	being formatted.

	A resource element contains the following child elements:

    	Zero or more doc elements.
    	Zero or more param elements with one of the following values for its style attribute:

    	template
        	Provides additional information about an embedded template parameter, see above. 
        	Child param elements whose name attribute value does not match the name of an 
        	embedded template parameter are ignored.
    	matrix
        	Specifies a matrix URI parameter
    	query
        	Specifies a global URI query parameter for all child method elements of the 
        	resource. Does not apply to methods inherited from a resource_type specified 
        	using the type attribute.
    	header
        	Specifies a global HTTP header for use in the request part of all child method 
        	elements of the resource. Does not apply to methods inherited from a resource_type 
        	specified using the type attribute.

    	Zero or more method(see section 2.8 ) elements, each of which describes the input to 
    	and output from an HTTP protocol method that can be applied to the resource. Such 
    	locally-defined methods are added to any methods included in resource_type elements 
    	referred to using the type attribute.
    	
    	Zero or more resource elements that describe sub-resources. Such sub-resources inherit 
    	matrix and template parameters from the parent resource since their URI is relative to 
    	that of the parent resource but they do not inherit query or header parameters specified 
    	globally for the parent resource.
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.MethodNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ParamNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourceNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourceTypeNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.BaseElement;
import edu.sjtu.ist.bjggzxb.doc.ResourceDoc;

public class ResourceNodeImpl extends GenericNodeImpl implements ResourceNode {

	/*
	 * attributes from defination of wadl
	 */
	protected List<DocNode> docNodes = new ArrayList<DocNode>();
	protected List<ParamNode> paramNodes = new ArrayList<ParamNode>();
	protected List<MethodNode> methodNodes = new ArrayList<MethodNode>();
	protected List<ResourceNode> resourceNodes = new ArrayList<ResourceNode>();
	protected List<ResourceTypeNode> resourceTypeNodes = new ArrayList<ResourceTypeNode>();
	protected String path;
	protected String id;
	protected String type;
	protected String queryType;

	/*
	 * xml schema defined element
	 */
	protected BaseElement elementDecl;

	/*
	 * these two attributes are info about full path resource
	 */
	protected String absolutePath;
	protected List<ParamNode> parentParams = new ArrayList<ParamNode>();

	/*
	 * all doc info
	 */
	protected ResourceDoc docInfo;

	/*
	 * other
	 */
	private int hashCode;

	public ResourceNodeImpl(String resourcePath, GenericNodeImpl parentNode) {
		// every path should start with "/" and end whithout "/", empty path
		// should be ""
		// out rule is differnet from w3c documentation
		if (!resourcePath.startsWith("/"))
			resourcePath = "/" + resourcePath;
		if (resourcePath.endsWith("/"))
			resourcePath = resourcePath.substring(0, resourcePath.length() - 1);
		this.path = resourcePath;
		super.parentNode = parentNode;

		// take care of the real node type of parent <resource> and <resources>
		absolutePath = path;
		ResourceNodeImpl iter = this;
		while (iter.getParentNode() instanceof ResourceNodeImpl) {
			ResourceNodeImpl resource = (ResourceNodeImpl) iter.getParentNode();
			String parentPath = resource.getPath();
			absolutePath = parentPath + absolutePath;
			parentParams.addAll(0, resource.getAllParams());
			iter = (ResourceNodeImpl) iter.getParentNode();
		}
		if (iter.getParentNode() instanceof ResourcesNodeImpl) {
			String parentPath = ((ResourcesNodeImpl) iter.getParentNode())
					.getBase();
			absolutePath = parentPath + absolutePath;
		} else if (iter.getParentNode() instanceof ResourceTypeNodeImpl) {
			String parentPath = "";
			absolutePath = parentPath + absolutePath;
		}
		hashCode = hashCode();
	}

	protected void iniDoc() {
		int size = docNodes.size();
		for (int index = 0; index < size; index++) {
			DocNode doc = docNodes.get(index);
			if (doc.getTitle().equals("istdoc")) {
				Gson gson = new Gson();
				docInfo = gson.fromJson(doc.getText(), ResourceDoc.class);
			}
		}
	}

	public BaseElement getElementDecl() {
		return elementDecl;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public int getHashCode() {
		return hashCode;
	}

	protected boolean updatePath(String oldPath, String newPath) {
		if (path.equals(oldPath)) {
			path = newPath;
			return true;
		}
		return false;
	}

	protected boolean removeParam(ParamNode param) {
		if (paramNodes.contains(param)) {
			paramNodes.remove(param);
			return false;
		}
		return false;
	}

	protected void directlyAddMethod(MethodNode method) {
		methodNodes.add(method);
	}

	protected boolean removeMethod(MethodNode method) {
		if (methodNodes.contains(method)) {
			methodNodes.remove(method);
			return true;
		}
		return false;
	}

	public List<MethodNode> getAllMethods() {
		return methodNodes;
	}

	@Override
	protected boolean addMethod(MethodNode method) {
		if (!methodNodes.contains(method)) {
			methodNodes.add(method);
			return true;
		}
		return false;
	}

	protected void addResourceType(ResourceTypeNode resourceType) {
		resourceTypeNodes.add(resourceType);
	}

	protected void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	protected void setId(String newId) {
		id = newId;
	}

	protected void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getPath();
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
	protected boolean addParam(ParamNode param) {
		if (!paramNodes.contains(param)) {
			paramNodes.add(param);
			parentParams.add(param);
			return true;
		}
		return false;
	}

	private boolean containsResource(String path) {
		Iterator<ResourceNode> iter = resourceNodes.iterator();
		while (iter.hasNext()) {
			ResourceNode resource = iter.next();
			if (resource.getPath().equals(path))
				return true;
		}
		return false;
	}

	@Override
	protected boolean addResource(ResourceNode resource) {
		if (!containsResource(resource.getPath())) {
			resourceNodes.add(resource);
			return true;
		}
		return false;
	}

	public ResourceNode getResourceByPath(String path) {
		Iterator<ResourceNode> iter = resourceNodes.iterator();
		while (iter.hasNext()) {
			ResourceNode resource = iter.next();
			if (resource.getPath().equals(path))
				return resource;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((absolutePath == null) ? 0 : absolutePath.hashCode());
		return result;
	}

	@Override
	public List<ResourceNode> getAllResources() {
		return resourceNodes;
	}

	@Override
	public List<DocNode> getAllDocs() {
		return this.docNodes;
	}

	@Override
	public List<ParamNode> getAllParams() {
		return paramNodes;
	}

	@Override
	public List<ParamNode> getParentParams() {
		return parentParams;
	}

	@Override
	public List<ResourceTypeNode> getAllResourceTypes() {
		return resourceTypeNodes;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getQueryType() {
		return queryType;
	}

	@Override
	public String getProvider() {
		if (docInfo == null)
			return "uknown";
		else
			return docInfo.provider;
	}

	@Override
	public String getName() {
		if (docInfo == null) {
			String[] strs = path.split("/");
			return strs[strs.length - 1].toUpperCase();
		} else
			return docInfo.name;
	}

	@Override
	public String getMiniDes() {
		if (docInfo == null)
			return "none";
		else
			return docInfo.minides;
	}

	@Override
	public String getDes() {
		if (docInfo == null)
			return "none";
		else
			return docInfo.description;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ResourceDoc getDoc() {
		return docInfo;
	}
}
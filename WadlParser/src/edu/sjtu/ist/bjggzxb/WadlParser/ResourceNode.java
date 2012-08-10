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

package edu.sjtu.ist.bjggzxb.WadlParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ResourceNode extends GenericNode {

	private List<DocNode> docNodes = new ArrayList<DocNode>();
	private List<ParamNode> paramNodes = new ArrayList<ParamNode>();
	private List<MethodNode> methodNodes = new ArrayList<MethodNode>();
	private List<ResourceNode> resourceNodes = new ArrayList<ResourceNode>();
	private List<ResourceTypeNode> resourceTypeNodes = new ArrayList<ResourceTypeNode>();
	private String path;
	private String id;
	private String type;
	private String queryType;
	private String absolutePath;
	
	private String miniDes;
	private String des;
	private String name;
	private int hashCode;
	private String provider;

	public ResourceNode(String resourcePath, GenericNode parentNode) {
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
		ResourceNode iter = this;
		while (iter.getParentNode() instanceof ResourceNode) {
			String parentPath = ((ResourceNode) iter.getParentNode()).getPath();
			absolutePath = parentPath + absolutePath;
			iter = (ResourceNode) iter.getParentNode();
		}
		if (iter.getParentNode() instanceof ResourcesNode) {
			String parentPath = ((ResourcesNode) iter.getParentNode())
					.getBase();
			absolutePath = parentPath + absolutePath;
		} else if (iter.getParentNode() instanceof ResourceTypeNode) {
			String parentPath = "";
			absolutePath = parentPath + absolutePath;
		}
		hashCode = hashCode();
	}

	public void iniDoc() {
		miniDes = null;
		des = "No Description.";
		name = "Noname";
		if (docNodes.size() == 0) {
			try {
				name = path.substring(path.lastIndexOf("/") + 1);
			} catch (Exception e) {
			}
		}else{
			Iterator<DocNode> iter = docNodes.iterator();
			while(iter.hasNext()){
				DocNode doc = iter.next();
				if(doc.getTitle().equalsIgnoreCase("name"))
					name = doc.getText();
				else if(doc.getTitle().equalsIgnoreCase("minidescription"))
					miniDes = doc.getText();
				else if(doc.getTitle().equalsIgnoreCase("description"))
					des = doc.getText();
			}
		}
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getName() {
		return name;
	}
	
	public String getMiniDes(){
		return miniDes;
	}
	
	public String getDes(){
		return des;
	}

	public String getPath() {
		return path;
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

	public List<ResourceTypeNode> getAllResourceTypes() {
		return resourceTypeNodes;
	}
	
	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	public String getQueryType() {
		return queryType;
	}

	protected void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getId() {
		return id;
	}

	protected void setId(String newId) {
		id = newId;
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

	public List<DocNode> getAllDocs() {
		return this.docNodes;
	}

	public List<ParamNode> getAllParams() {
		return paramNodes;
	}

	@Override
	protected boolean addParam(ParamNode param) {
		if (!paramNodes.contains(param)) {
			paramNodes.add(param);
			return true;
		}
		return false;
	}

	public List<ResourceNode> getAllResources() {
		return resourceNodes;
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
}
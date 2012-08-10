/*
 * 	A resource_type element describes a set of methods that, 
 *	together, define the behavior of a type of resource. 
 *	A resource_type may be used to define resource behavior 
 *	that is expected to be supported by multiple resources.
 *  	
 *  A resource_type element has the following attributes:
 *		id
 *   		A required attribute of type xsd:ID that identifies 
 *			the resource_type element.
 *
 *  A resource_type element contains the following child elements:
 *   	Zero or more doc elements
 *   	Zero or more param elements  
 *			with one of the following values for its style attribute:
 *		    query
 *				Specifies a URI query parameter for all child 
 *				method elements of the resource type.
 *		    header
 *		        Specifies a HTTP header for use in the request 
 *				part of all child method elements of the resource type.
 *  	Zero or more method elements, each of which describes an 
 *		HTTP protocol method that can be applied to a resource of this type.
 *   	
 *		Zero or more resource elements that describe sub-resources of 
 *		resources of this type. The URI of such sub-resources is provided 
 *		by the path attribute of the resource element and is relative to 
 *		that of the parent resource.
 *
 */

package edu.sjtu.ist.bjggzxb.WadlParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ResourceTypeNode extends GenericNode {

	private final String id;
	private List<DocNode> docNodes;
	private List<ParamNode> paramNodes;
	private List<MethodNode> methodNodes;
	private List<ResourceNode> resourceNodes;

	public ResourceTypeNode(String resourceTypeId, ApplicationNode application) {
		if (resourceTypeId == null)
			throw new WadlException("Resource_Type Id is null.");
		this.id = resourceTypeId;
		this.docNodes = new ArrayList<DocNode>();
		this.paramNodes = new ArrayList<ParamNode>();
		this.methodNodes = new ArrayList<MethodNode>();
		this.resourceNodes = new ArrayList<ResourceNode>();
		super.parentNode = application;
	}

	public boolean equals(Object other) {
		if (other instanceof ResourceTypeNode) {
			ResourceTypeNode re = (ResourceTypeNode) other;
			if (id.equals(re.getId()))
				return true;
		}
		return false;
	}

	public String getId() {
		return id;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (doc != null && doc != null) {
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
		if (param != null && !paramNodes.contains(param)) {
			paramNodes.add(param);
			return true;
		}
		return false;
	}

	public List<ParamNode> getAllParams() {
		return paramNodes;
	}

	@Override
	protected boolean addMethod(MethodNode method) {
		if (method != null && !methodNodes.contains(method)) {
			methodNodes.add(method);
			return true;
		}
		return false;
	}

	public List<MethodNode> getAllMethods() {
		return methodNodes;
	}

	private boolean containsResource(String resourcePath) {
		Iterator<ResourceNode> iter = resourceNodes.iterator();
		while (iter.hasNext()) {
			ResourceNode resource = iter.next();
			if (resource.getPath().equals(resourcePath))
				return true;
		}
		return false;
	}

	public List<ResourceNode> getAllResources() {
		return resourceNodes;
	}

	public ResourceNode getResourceByPath(String resourcePath) {
		if (resourcePath == null)
			return null;
		Iterator<ResourceNode> iter = resourceNodes.iterator();
		while (iter.hasNext()) {
			ResourceNode resource = iter.next();
			if (resource.getPath().equals(resourcePath))
				return resource;
		}
		return null;
	}

	@Override
	protected boolean addResource(ResourceNode resource) {
		if (resource != null && resource.getPath() != null
				&& !this.containsResource(resource.getPath())) {
			resourceNodes.add(resource);
			return true;
		}
		return false;
	}
}
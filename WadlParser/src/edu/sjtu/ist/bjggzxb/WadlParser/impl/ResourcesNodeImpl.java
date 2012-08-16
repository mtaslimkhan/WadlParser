/*
 *	The resources element acts as a container for the resources provided by the 
 * 	application. A resources element has a base attribute of type xsd:anyURI that 
 * 	provides the base URI for each child resource identifier. Descendent resource 
 * 	elements describe the resources provided by the application.
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourceNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourcesNode;

public class ResourcesNodeImpl extends GenericNodeImpl implements ResourcesNode{

	/*
	 * For each <resources> node, it should contains base attribute and some
	 * <resource> node
	 */
	private String base;

	private List<ResourceNode> resourceNodes;

	public ResourcesNodeImpl(String resourcesBase, ApplicationNodeImpl application) {
		// resource base should start with "http://" and end without "/"
		// out rule is differnet from w3c documentation
		if (resourcesBase.endsWith("/"))
			resourcesBase = resourcesBase.substring(0,
					resourcesBase.length() - 1);
		resourceNodes = new ArrayList<ResourceNode>();
		this.base = resourcesBase;
		super.parentNode = application;
	}

	public String getBase() {
		return base;
	}

	protected boolean updateBase(String oldBase, String newBase) {
		if (oldBase.equals(base)) {
			base = newBase;
			return true;
		}
		return false;
	}

	public List<ResourceNode> getAllResources() {
		return resourceNodes;
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

	public ResourceNode getResourceByPath(String resourcePath) {
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
		if (!this.containsResource(resource.getPath())) {
			resourceNodes.add(resource);
			return true;
		}
		return false;
	}
}
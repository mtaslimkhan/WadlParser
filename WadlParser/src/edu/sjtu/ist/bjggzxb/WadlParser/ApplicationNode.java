/*
 * The application element forms the root of a WADL description and contains the following:
 *
 *   Zero or more doc elements
 *   An optional grammars element
 *   Zero or more resources elements
 *   Zero or more of the following:
 *       resource_type elements
 *       method elements
 *       representation elements
 *       param elements
 */

package edu.sjtu.ist.bjggzxb.WadlParser;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Namespace;

/*
 * Here we don't parse grammars node
 */
public class ApplicationNode extends GenericNode {

	private List<DocNode> docNodes;
	private List<ResourcesNode> resourcesNodes;
	private List<ResourceTypeNode> resourceTypeNodes;
	private List<MethodNode> methodNodes;
	private List<RepresentationNode> representationNodes;
	private List<ParamNode> paramNodes;
	private List<Namespace> namespaceAttributes;

	public ApplicationNode() {
		docNodes = new ArrayList<DocNode>();
		resourcesNodes = new ArrayList<ResourcesNode>();
		resourceTypeNodes = new ArrayList<ResourceTypeNode>();
		methodNodes = new ArrayList<MethodNode>();
		representationNodes = new ArrayList<RepresentationNode>();
		paramNodes = new ArrayList<ParamNode>();
		namespaceAttributes = new ArrayList<Namespace>();
	}

	protected boolean addResources(ResourcesNode resources) {
		if (resources != null) {
			resourcesNodes.add(resources);
			return true;
		}
		return false;
	}

	public List<ResourcesNode> getAllResources() {
		return resourcesNodes;
	}

	protected boolean setNamespace(List<Namespace> namespaces) {
		this.namespaceAttributes = namespaces;
		return true;
	}

	public String getNamespaceByName(String name) {
		if (name == null)
			return null;
		Iterator<Namespace> iter = namespaceAttributes.iterator();
		while (iter.hasNext()) {
			Namespace nm = iter.next();
			if (nm.getPrefix().equals(name)) {
				return nm.getURI();
			}
		}
		return null;
	}

	public List<Namespace> getAllNamespaces() {
		return namespaceAttributes;
	}

	protected boolean addResourceType(ResourceTypeNode resourceType) {
		if (resourceType != null && !resourceTypeNodes.contains(resourceType)) {
			resourceTypeNodes.add(resourceType);
			return true;
		}
		return false;
	}

	public ResourceTypeNode getResourceTypeById(String id) {
		if (id == null)
			return null;
		Iterator<ResourceTypeNode> iter = resourceTypeNodes.iterator();
		while (iter.hasNext()) {
			ResourceTypeNode resourceType = iter.next();
			if (id.equals(resourceType.getId()))
				return resourceType;
		}
		return null;
	}

	public List<ResourceTypeNode> getAllResourceTypes() {
		return resourceTypeNodes;
	}

	@Override
	public String toString() {
		String res = " Application ";
		for (ResourcesNode resourcesNode : getAllResources()) {
			res += " [ ";
			res += "(" + resourcesNode.getBase() + ")";
			for (ResourceNode resourceNode : resourcesNode.getAllResources()) {
				res += resourceNode.getPath() + " ( ";
				for (ResourceNode resourceNodeNested : resourceNode
						.getAllResources()) {
					res += resourceNodeNested.getPath();
					res += " ( ";
					for (ResourceNode resourceNodeNestedNested : resourceNodeNested
							.getAllResources()) {
						res += resourceNodeNestedNested.getPath() + " + ";
					}
					res += ") + ";
				}
				res += ") , ";
			}
			res = res.substring(0, res.length() - 2);
			res += "]\n";
		}
		return res;
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
		if (param == null)
			return false;
		Iterator<ParamNode> iter = paramNodes.iterator();
		while (iter.hasNext()) {
			ParamNode exist = iter.next();
			if (exist.getId() != null && exist.getId().equals(param.getId()))
				return false;
		}
		paramNodes.add(param);
		return true;
	}

	public List<ParamNode> getAllParams() {
		return paramNodes;
	}

	public ParamNode getParamById(String id) {
		if (id == null)
			return null;
		Iterator<ParamNode> iter = paramNodes.iterator();
		while (iter.hasNext()) {
			ParamNode param = iter.next();
			if (id.equals(param.getId()))
				return param;
		}
		return null;
	}

	public List<MethodNode> getAllMethods() {
		return methodNodes;
	}

	@Override
	protected boolean addMethod(MethodNode method) {
		if (method == null)
			return false;
		Iterator<MethodNode> iter = methodNodes.iterator();
		while (iter.hasNext()) {
			MethodNode exist = iter.next();
			if (exist.getId() != null && exist.getId().equals(method.getId()))
				return false;
		}
		methodNodes.add(method);
		return true;
	}

	public MethodNode getMethodById(String id) {
		if (id == null)
			return null;
		Iterator<MethodNode> iter = methodNodes.iterator();
		while (iter.hasNext()) {
			MethodNode method = iter.next();
			if (id.equals(method.getId()))
				return method;
		}
		return null;
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

	public RepresentationNode getRepresentationById(String id) {
		Iterator<RepresentationNode> iter = representationNodes.iterator();
		while (iter.hasNext()) {
			RepresentationNode rep = iter.next();
			if (id.equals(rep.getId()))
				return rep;
		}
		return null;
	}
}
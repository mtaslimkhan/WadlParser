package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

import org.jdom2.Namespace;

public interface ApplicationNode {

	public List<RepresentationNode> getAllRepresentations();
	
	public RepresentationNode getRepresentationById(String id);
	
	public MethodNode getMethodById(String id);
	
	public ParamNode getParamById(String id);
	
	public List<DocNode> getAllDocs();
	
	public List<ResourceTypeNode> getAllResourceTypes();
	
	public ResourceTypeNode getResourceTypeById(String id);
	
	public List<Namespace> getAllNamespaces();
	
	public String getNamespaceByName(String name);
	
	public List<ResourcesNode> getAllResources();
}

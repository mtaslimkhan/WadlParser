package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface ResourceTypeNode {
	public String getId();

	public List<DocNode> getAllDocs();

	public List<ParamNode> getAllParams();

	public List<MethodNode> getAllMethods();

	public List<ResourceNode> getAllResources();

	public ResourceNode getResourceByPath(String resourcePath);
}

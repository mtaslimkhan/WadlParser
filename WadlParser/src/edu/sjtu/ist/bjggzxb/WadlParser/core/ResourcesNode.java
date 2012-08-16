package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface ResourcesNode {

	public String getBase();

	public List<ResourceNode> getAllResources();

	public ResourceNode getResourceByPath(String resourcePath);
}

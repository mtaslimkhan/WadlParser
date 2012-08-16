package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.BaseElement;

public interface ResourceNode {

	public String getProvider();

	public String getName();

	public String getMiniDes();

	public String getDes();

	public String getAbsolutePath();

	public String getPath();

	public int getHashCode();

	public List<MethodNode> getAllMethods();

	public List<ResourceTypeNode> getAllResourceTypes();

	public String getType();

	public String getQueryType();

	public String getId();

	public List<DocNode> getAllDocs();

	public List<ParamNode> getParentParams();

	public List<ParamNode> getAllParams();

	public List<ResourceNode> getAllResources();
	
	public BaseElement getElementDecl();
}

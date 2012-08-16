package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.BaseElement;

public interface MethodNode extends GenericNode{

	public String getMiniDes();

	public String getDes();

	public String getDocName();

	public String getName();

	public String getId();

	public RequestNode getRequest();

	public ResponseNode getResponse();

	public List<DocNode> getAllDocs();

	public BaseElement getElementDecl();

}

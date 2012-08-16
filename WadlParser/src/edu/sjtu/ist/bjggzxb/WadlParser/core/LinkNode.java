package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface LinkNode {

	public List<DocNode> getAllDocs();
	
	public String getRev();
	
	public String getRel();
	
	public String getResourceType();
}

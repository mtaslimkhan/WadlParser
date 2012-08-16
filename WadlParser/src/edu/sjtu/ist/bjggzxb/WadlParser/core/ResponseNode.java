package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface ResponseNode {

	public int getStatus();
	
	public List<DocNode> getAllDocs();
	
	public List<ParamNode> getAllParams();
	
	public List<RepresentationNode> getAllRepresentations();
}

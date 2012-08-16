package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface RequestNode {
	public List<DocNode> getAllDocs();

	public List<ParamNode> getAllParams();

	public List<RepresentationNode> getAllRepresentations();
}

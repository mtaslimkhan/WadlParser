package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface RepresentationNode {

	public String getId();

	public String getMediaType();

	public String getElement();

	public String getProfile();

	public List<DocNode> getAllDocs();

	public List<ParamNode> getAllParams();
}

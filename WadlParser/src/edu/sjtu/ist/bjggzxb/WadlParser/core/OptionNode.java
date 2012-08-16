package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface OptionNode {
	
	public List<DocNode> getAllDocs();

	public String getMediaType();

	public String getValue();
}

package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.util.List;

public interface ParamNode {

	public String getId();
	
	public String getName();
	
	public List<OptionNode> getAllOptions();
	
	public LinkNode getLink();
	
	public String getStyle();
	
	public String getType();
	
	public String getDefaultValue();
	
	public String getFixedValue();
	
	public String getPath();
	
	public boolean isRequired();
	
	public boolean isRepeating();
}

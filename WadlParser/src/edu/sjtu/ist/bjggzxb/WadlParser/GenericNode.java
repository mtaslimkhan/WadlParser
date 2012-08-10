package edu.sjtu.ist.bjggzxb.WadlParser;

public class GenericNode{
	
	protected GenericNode parentNode;

	public GenericNode getParentNode(){
		return parentNode;
	}

	public String toString(){
		return this.getClass().getName();
	}
	
	protected boolean addDoc(DocNode doc){
		return false;	
	}
	
	protected boolean addParam(ParamNode param){
		return false;
	}
	
	protected boolean addMethod(MethodNode method){
		return false;
	}
	
	protected boolean addRepresentation(RepresentationNode rep){
		return false;
	}
	
	protected boolean addResource(ResourceNode resource){
		return false;
	}
}
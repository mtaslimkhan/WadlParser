package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.GenericNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.MethodNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ParamNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.RepresentationNode;
import edu.sjtu.ist.bjggzxb.WadlParser.core.ResourceNode;

public class GenericNodeImpl implements GenericNode {

	protected GenericNodeImpl parentNode;

	@Override
	public GenericNode getParentNode() {
		return parentNode;
	}

	protected boolean addDoc(DocNode doc) {
		return false;
	}

	protected boolean addParam(ParamNode param) {
		return false;
	}

	protected boolean addMethod(MethodNode method) {
		return false;
	}

	protected boolean addRepresentation(RepresentationNode rep) {
		return false;
	}

	protected boolean addResource(ResourceNode resource) {
		return false;
	}

}

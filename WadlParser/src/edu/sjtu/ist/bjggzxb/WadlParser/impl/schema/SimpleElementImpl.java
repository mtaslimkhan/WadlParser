package edu.sjtu.ist.bjggzxb.WadlParser.impl.schema;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;

import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.ComplexElement;
import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.SimpleElement;

public class SimpleElementImpl implements SimpleElement {

	private String name;

	private int max;

	private int min;

	private String type;

	public SimpleElementImpl() {
		name = "no name";
		max = 1;
		min = 1;
		type = "string";
	}

	public SimpleElementImpl(XSElementDecl element, int max, int min) {
		this.name = element.getName();
		this.max = max;
		this.min = min;
		XSType t = element.getType();
		if (!t.isSimpleType())
			return;
		XSSimpleType simple = t.asSimpleType();
		if (simple.isRestriction()) {
			type = t.getName();
		} else if (simple.isList()) {

		} else if (simple.isUnion()) {

		}
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public boolean isSimple() {
		return true;
	}

	@Override
	public boolean isComplex() {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SimpleElement asSimple() {
		return this;
	}

	@Override
	public ComplexElement asComplex() {
		return null;
	}

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public int getMin() {
		return min;
	}

	public String toString(String prefix) {
		return prefix + name + " " + type + "\n";
	}

}

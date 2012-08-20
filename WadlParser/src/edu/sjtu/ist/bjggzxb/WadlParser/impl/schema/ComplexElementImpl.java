package edu.sjtu.ist.bjggzxb.WadlParser.impl.schema;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSType;

import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.BaseElement;
import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.ComplexElement;
import edu.sjtu.ist.bjggzxb.WadlParser.core.schema.SimpleElement;

public class ComplexElementImpl implements ComplexElement {

	private String name;

	private int max;

	private int min;

	private ArrayList<BaseElement> children;

	public ComplexElementImpl() {
		name = "no name";
		max = 1;
		min = 1;
		children = new ArrayList<BaseElement>();
	}

	public ComplexElementImpl(XSElementDecl element, int max, int min) {
		this.name = element.getName();
		this.max = max;
		this.min = min;
		children = new ArrayList<BaseElement>();
		XSType t = element.getType();
		if (!t.isComplexType())
			return;
		XSParticle[] particles = t.asComplexType().getContentType()
				.asParticle().getTerm().asModelGroup().getChildren();
		int size = particles.length;
		for (int index = 0; index < size; index++) {
			XSParticle particle = particles[index];
			if (!particle.getTerm().isElementDecl())
				continue;
			XSElementDecl subelement = particle.getTerm().asElementDecl();
			if (subelement.getType().isSimpleType()) {
				SimpleElementImpl simple = new SimpleElementImpl(subelement,
						particle.getMaxOccurs(), particle.getMinOccurs());
				children.add(simple);
			} else {
				ComplexElementImpl complex = new ComplexElementImpl(subelement,
						particle.getMaxOccurs(), particle.getMinOccurs());

				children.add(complex);
			}
		}
	}

	public List<BaseElement> getChildElements() {
		return children;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean isComplex() {
		return true;
	}

	@Override
	public SimpleElement asSimple() {
		return null;
	}

	@Override
	public ComplexElement asComplex() {
		return this;
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
		String str = prefix + name + "\n";
		int size = children.size();
		for (int index = 0; index < size; index++) {
			str += children.get(index).toString(prefix + "   ");
		}
		return str;
	}
}

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import org.jdom2.Element;

import com.sun.xml.xsom.XSElementDecl;

import edu.sjtu.ist.bjggzxb.WadlParser.core.MethodNode;
import edu.sjtu.ist.bjggzxb.WadlParser.impl.schema.ComplexElementImpl;
import edu.sjtu.ist.bjggzxb.WadlParser.impl.schema.SimpleElementImpl;

public class DocParser extends SimpleParser {

	protected ResourceNodeImpl parseResource(Element resource,
			GenericNodeImpl parent) {

		ResourceNodeImpl resourceNode = super.parseResource(resource, parent);
		/*
		 * initial document
		 */
		resourceNode.iniDoc();

		/*
		 * this part parse the method representation
		 */
		int size = resourceNode.getAllMethods().size();
		for (int index = 0; index < size; index++) {
			MethodNode method = resourceNode.getAllMethods().get(index);
			if (method.getName().equals("GET")) {
				String[] elestrs = method.getResponse().getAllRepresentations()
						.get(0).getElement().split(":");
				String eledomain = elestrs[0];
				String elename = elestrs[1];
				String elenamespace = application.getNamespaceByName(eledomain);
				System.out.println(elenamespace + " " + eledomain + " "
						+ elename);
				XSElementDecl decl = grammarSet.getElementDecl(elenamespace,
						elename);
				if (decl.getType().isSimpleType()) {
					SimpleElementImpl ele = new SimpleElementImpl(decl, 1, 1);
					resourceNode.elementDecl = ele;
				} else {
					ComplexElementImpl ele = new ComplexElementImpl(decl, 1, 1);
					resourceNode.elementDecl = ele;
				}
				System.out.println(resourceNode.elementDecl.toString(""));
			}
		}
		return resourceNode;
	}

	protected MethodNodeImpl parseMethod(Element method, GenericNodeImpl parent) {
		MethodNodeImpl methodNode = super.parseMethod(method, parent);
		methodNode.iniDoc();
		return methodNode;
	}
}

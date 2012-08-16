/*
 *	Each WADL-defined element can have one or more child doc elements that can 
 *	be used to document that element. The doc element has the following attributes:
 *
 *	xml:lang
 *    	Defines the language for the title attribute value and the contents of the 
 *    	doc element. If an element contains more than one doc element then they MUST 
 *    	have distinct values for their xml:lang attribute.
 *	title
 *    	A short plain text description of the element being documented, the value 
 *    	SHOULD be suitable for use as a title for the contained documentation. 
 */

package edu.sjtu.ist.bjggzxb.WadlParser.impl;

import edu.sjtu.ist.bjggzxb.WadlParser.core.DocNode;

public class DocNodeImpl implements DocNode {
	private String xmlLang;
	private String title;
	private String text;

	public DocNodeImpl(String docLang, String docTitle, String docText) {
		this.xmlLang = docLang;
		this.title = docTitle;
		this.text = docText;
	}

	@Override
	public String getXmlLang() {
		return xmlLang;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getText() {
		return text;
	}
}
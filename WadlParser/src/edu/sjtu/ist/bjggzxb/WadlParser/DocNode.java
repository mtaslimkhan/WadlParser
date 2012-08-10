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

package edu.sjtu.ist.bjggzxb.WadlParser;

public class DocNode{
	private String xmlLang;
	private String title;
	private String text;

	public DocNode(String docLang, String docTitle, String docText){
		this.xmlLang = docLang;
		this.title = docTitle;
		this.text = docText;
	}
	
	public String getXmlLang(){
		return xmlLang;
	}

	public String getTitle(){
		return title;
	}

	public String getText(){
		return text;
	}
}
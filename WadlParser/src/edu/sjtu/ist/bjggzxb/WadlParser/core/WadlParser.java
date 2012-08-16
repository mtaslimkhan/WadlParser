package edu.sjtu.ist.bjggzxb.WadlParser.core;

import java.io.InputStream;

import com.sun.xml.xsom.XSSchemaSet;

import edu.sjtu.ist.bjggzxb.WadlParser.impl.ApplicationNodeImpl;
import edu.sjtu.ist.bjggzxb.WadlParser.impl.DocParser;
import edu.sjtu.ist.bjggzxb.WadlParser.impl.SimpleParser;

public abstract class WadlParser {
	
	public static WadlParser newSimpleParser(){
		return new SimpleParser();
	}
	
	public static WadlParser newDocParser(){
		return new DocParser();
	}
	
	public abstract ApplicationNodeImpl getApplication();

	public abstract XSSchemaSet getGrammarSet();

	public abstract boolean buildDoc(InputStream instream);

	public abstract boolean buildDoc(String str);

	public abstract String parse();
}

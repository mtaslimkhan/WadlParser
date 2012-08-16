package edu.sjtu.ist.bjggzxb.WadlParser.core.schema;

public interface BaseElement {
	
	public String getName();
	
	public int getMax();
	
	public int getMin();
	
	public boolean isSimple();
	
	public boolean isComplex();
	
	public SimpleElement asSimple();
	
	public ComplexElement asComplex();
	
	public String toString(String prefix);
}

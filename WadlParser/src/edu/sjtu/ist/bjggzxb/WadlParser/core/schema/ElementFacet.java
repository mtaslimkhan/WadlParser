package edu.sjtu.ist.bjggzxb.WadlParser.core.schema;

import com.sun.xml.xsom.XSFacet;

public interface ElementFacet {

	public final static String FACET_ENUMERATION = XSFacet.FACET_ENUMERATION;

	public final static String FACET_FRACTIONDIGITS = XSFacet.FACET_FRACTIONDIGITS;

	public final static String FACET_LENGTH = XSFacet.FACET_LENGTH;

	public final static String FACET_MAXEXCLUSIVE = XSFacet.FACET_MAXEXCLUSIVE;

	public final static String FACET_MAXINCLUSIVE = XSFacet.FACET_MAXINCLUSIVE;

	public final static String FACET_MAXLENGTH = XSFacet.FACET_MAXLENGTH;

	public final static String FACET_MINEXCLUSIVE = XSFacet.FACET_MINEXCLUSIVE;

	public final static String FACET_MININCLUSIVE = XSFacet.FACET_MININCLUSIVE;

	public final static String FACET_MINLENGTH = XSFacet.FACET_MINLENGTH;

	public final static String FACET_PATTERN = XSFacet.FACET_PATTERN;

	public final static String FACET_TOTALDIGITS = XSFacet.FACET_TOTALDIGITS;

	public final static String FACET_WHITESPACE = XSFacet.FACET_WHITESPACE;

	public String getName();

	public String getValue();

	public boolean isFixed();

}

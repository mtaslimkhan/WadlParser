/*
 * 	An option element defines one of a set of possible values for the parameter represented 
 *	by its parent param element. An option element has the following attributes:
 *	
 *	value
 *   	A required attribute that defines one of the possible values of the parent parameter.
 *	mediaType
 *   	When present this indicates that the parent parameter acts as a media type selector 
 *		for responses. The value of the attribute is the media type that is expected when the 
 *		parameter has the value given in the value attribute.
 *
 *	An option element may have zero or more doc elements that document the meaning of the value.
 */
package edu.sjtu.ist.bjggzxb.WadlParser;

import java.util.List;
import java.util.ArrayList;

public class OptionNode extends GenericNode {

	private String value;
	private String mediaType;
	private List<DocNode> docNodes;

	public OptionNode() {
		value = null;
		this.docNodes = new ArrayList<DocNode>();
	}

	public String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	public String getMediaType() {
		return mediaType;
	}

	protected void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Override
	protected boolean addDoc(DocNode doc) {
		if (doc != null){
			docNodes.add(doc);
			return true;
		}
		return false;
	}

	public List<DocNode> getAllDocs() {
		return docNodes;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof OptionNode) {
			OptionNode option = (OptionNode) other;
			if (option.getValue().equals(this.value))
				return true;
		}
		return false;
	}
}
package edu.sjtu.ist.bjggzxb.WadlParser.nouse;

public class NamespaceAttribute {

	private String name;
	private String value;

	public NamespaceAttribute(String attributeName, String attributeValue) {
		this.name = attributeName;
		this.value = attributeValue;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	protected boolean isValid() {
		if (name != null && value != null)
			return true;
		else
			return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof NamespaceAttribute) {
			NamespaceAttribute att = (NamespaceAttribute) other;
			if (name != null && value != null && name.equals(att.getName())
					&& value.equals(att.getValue()))
				return true;
		}
		return false;
	}
}
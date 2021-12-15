package getobjects;

public class GlobalAttribute extends JobMetadataData {
	String globalAttrName;
	String globalAttrValue;
	
	GlobalAttribute(String globalAttrName, String globalAttrValue) {
		this.globalAttrName = globalAttrName;
		this.globalAttrValue = globalAttrValue;
		setColList(" ( global_attr_name, global_attr_value) ");
	}
	
	public String toString() {
		String returnString = "( '" + this.globalAttrName + "', '" + this.globalAttrValue + "') ";
		
		return returnString;
		
	}
}

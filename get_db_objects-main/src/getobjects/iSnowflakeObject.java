package getobjects;

public interface iSnowflakeObject {
	
	public String getDDL(String objectName);
	
	static public String writeObject() {
		
		return "S";
	};

}

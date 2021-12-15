package getobjects;

public class InventoryItem {
	private String project;
	private String sourceDatabase;
	private String targetDatabase;
	private String schema;
	private String objectType;
	private String objectName;
	private String signature;
	private String path;
	private String functionalArea;
	
	
	//for creating an inventory item from the database
	InventoryItem(String project, String functionalArea, String sourceDatabase, String targetDatabase, String schema, String objectType, String objectName, String signature) {
		this.sourceDatabase = sourceDatabase;
		this.targetDatabase = targetDatabase;
		this.schema = schema;
		this.objectType = objectType;
		this.objectName = objectName;
		this.signature =  signature ;
		setPath();
		this.project = project;
		this.functionalArea = functionalArea;
	}
	
	/*
	//for creating inventory from a file
	InventoryItem(String project, String functionalArea, String targetDatabase, String schema, String objectType, String objectName, String signature) {
		this.sourceDatabase = "";
		this.targetDatabase = targetDatabase;
		this.schema = schema;
		this.objectType = objectType;
		this.objectName = objectName;
		this.signature =  signature ;
		setPath();
		this.project = project;
		this.functionalArea = functionalArea;
	}
	*/
	

	public void serFunctionalArea(String functionalArea) {
		this.functionalArea = functionalArea;
	}
	
	public void setProject(String project ) {
		this.project = project;
	}
	
	public void setPath() {
		String path = "resources/" + this.targetDatabase + "/" + this.schema + "/" + this.objectType + "/" + this.objectName + "." + this.objectType;
		this.path = path;
	}
	
	/*
	public void setSourceDatabase(String db) {
		TranslateEnv.translateTargetDbToSourceDb(
				this.getProject(), 
				this.getTargetEnvironment(), 
				this.getFunctionalArea(),
				this.getTargetDb());
	}
	*/
	
	public void setTargetDatabase(String db) {
		this.targetDatabase = db;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getFuncationalArea() {
		return this.functionalArea;
	}
	
	public String getProject() {
		return this.project;
	}
	
	public String getSourceDatabase() {
		return this.sourceDatabase ;
	}
	
	public String getTargetDatabase() {
		return this.targetDatabase ;
	}
	
	public String getSchema() {
		return this.schema ;
	}
	
	public String getObjectType() {
		return this.objectType ;
	}
	
	public String getObjectName() {
		return this.objectName ;
	}
	
	public String getSignature() {
		return this.signature ;
	}
	
	public String toString() {
		String string;
		string = "Project: " + 		this.project;
		string += "\nSource Database: " + 	this.sourceDatabase;
		string += "\nTarget Database: " + 	this.targetDatabase;
		string += "\nSchema: " + 		this.schema;
		string += "\nObject type: " + this.objectType;
		string += "\nObject name: " + this.objectName;
		string += "\nSignature: " + 	this.signature;
		string += "\nPath: " + this.path;
		return string;
	}
	
	public String toFile() {
		String string;
		string = this.project + "\t";
		string += this.functionalArea + "\t";
		string += this.targetDatabase + "\t";
		string += this.schema + "\t";
		string += this.objectType + "\t";
		string += this.objectName + "\t";
		string += this.signature + "\t";
		string += this.path;
		string += "\n";
		
		return string;
	}
	
}

package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import common.DbConnect;

public abstract class SnowflakeObject {


	private String name;
	private String ddl;
	private String parentSchema;
	private String parentDb;
	private SnowflakeObjectType objectType;
	private String fileName;
	private String signature;
	private String targetEnvironment;
	private String project;
	private String functionalArea;
	private String targetDb;
	
	/*
	SnowflakeObject() {
		TranslateEnv.translateSourceDbToTargetDb(getProject(), getTargetEnvironment(), getFunctionalArea(), getParentDb());
	}
	*/
	
	//getters and setters
	

	
	public void setFunctionalArea(String functionalArea) {
		this.functionalArea = functionalArea;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public void setTargetEnvironment(String targetEnvironmnet) {
		this.targetEnvironment = targetEnvironmnet;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDdl(String ddl) throws Exception {
		this.ddl = ddl;
	}
	
	public void setParentSchema(String parentSchema) {
		this.parentSchema = parentSchema;
	}
	
	public void setParentDb(String parentDb) {
		this.parentDb = parentDb;
	}
	
	public void setObjectType(SnowflakeObjectType objectType) {
		this.objectType = objectType;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public void setSignature(String signature) {
		this.signature = "(" + signature + ")";
	}
	
	public String getFunctionalArea() {
		return this.functionalArea;
	}
	
	public String getProject() {
		return this.project;
	}
	
	public String getTargetEnvironment() {
		return this.targetEnvironment;
	}
	
	public String getSignature() {
		return this.signature;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDdl() {
		return this.ddl;
	}
	
	public String getParentSchema() {
		return this.parentSchema;
	}

	public String getParentDb() {
		return this.parentDb;
	}
	
	public SnowflakeObjectType getObjectType() {
		return this.objectType;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	//worker methods
	
	public void translateEnvInDdl(String targetEnvironment, String ddl ) throws Exception {
		String sourceEnv = TranslateEnv.translate(targetEnvironment);
		
		setDdl(getDdl().replaceAll(sourceEnv.toUpperCase(), targetEnvironment.toUpperCase()));
	}
	
	// if get_ddl does not work for the given object type 
	// then this method should be overwritten in the object class
	public String extractDdl(Connection connection) throws Exception {
		
		String getDdlStatement = null;
		
		//for objects which get_ddl works
		// TODO Auto-generated method stub
		
		//Connection connection = DbConnect.getConnection();
		Statement statement = connection.createStatement();
		
		
	   getDdlStatement = "select get_ddl('" + getObjectType() + "' , '" + getParentDb() + "." + getParentSchema() + "." + getName() + "');";
		
		
		ResultSet resultSet = statement.executeQuery(getDdlStatement);
		
		//String dbObjects = null; 
		
		resultSet.next();
		String ddl = resultSet.getString(1);
		
		//debugging
		//System.out.println(ddl);
		
		//System.out.println(dbObjects); //for debugging purposes
		return ddl;
	}
	
	//optional version of extract which takes a snowflake object
	//used for jobmetadata stuff since the table is actually a child
	public String extractDdl(Connection connection, SnowflakeObject theObject) throws Exception {
		
		String getDdlStatement = null;
		
		//for objects which get_ddl works
		// TODO Auto-generated method stub
		
		//Connection connection = DbConnect.getConnection();
		Statement statement = connection.createStatement();
		
		
	   getDdlStatement = "select get_ddl('" + theObject.getObjectType() + "' , '" + theObject.getParentDb() + "." + theObject.getParentSchema() + "." + theObject.getName() + "');";
		
		
		ResultSet resultSet = statement.executeQuery(getDdlStatement);
		
		//String dbObjects = null; 
		
		resultSet.next();
		String ddl = resultSet.getString(1);
		
		//debugging
		//System.out.println(ddl);
		
		//System.out.println(dbObjects); //for debugging purposes
		return ddl;
	}

	public String getTargetDb() {
		return targetDb;
	}

	public void setTargetDb(String targetDb) {
		this.targetDb = targetDb;
	}
	
}


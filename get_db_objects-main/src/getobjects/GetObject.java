package getobjects;

import java.sql.Connection;

public class GetObject {

	
	public static SnowflakeObject getObjectDDL(String project, String functionalArea, String targetEnvironmnet, String objectTypeInput, String parentDb, String targetDb, String parentSchema, String objectName, String signature, Connection connection) throws Exception {
		
		SnowflakeObject myObject = SnowflakeObjectFactory.createObject(connection, project, functionalArea, targetEnvironmnet, objectTypeInput, parentDb, targetDb, parentSchema, objectName, signature);
		
		
		return myObject;
	}
	
	
	/*
	 * purpose: extract the DDL from Snowflake
	 * 
	 * input needed: current parent DB & schema, object name
	 * 
	 * steps:
	 * 	1. instantiate the object
	 *  2. DDL will be printed when the object is instantiated (hopefully?)
	 */
	
	/*
	 * removing this as its moved to a different class
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//arg[0] should be objectType
		String objectTypeInput = args[0];
		
		//arg[1] should be parentDb
		String parentDb = args[1];
		//arg[2] should be parentSchema
		String parentSchema = args[2];
		//arg[3] should be the object name
		String objectName = args[3];
		
		//TO DO: add check here is object type is involved
		SnowflakeObjectType objectType = SnowflakeObjectType.valueOf(objectTypeInput.toUpperCase());
		
		if (objectType == SnowflakeObjectType.TABLE ) {
		Table myFirstTable = new Table(parentDb, parentSchema, objectName);
		}


	}
	*/

}

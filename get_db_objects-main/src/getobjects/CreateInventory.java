package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DbConnect;

public class CreateInventory {

	
	public static ArrayList<InventoryItem>  createInventory(String targetEnv, String projectName, String functionalArea) throws Exception {
		
		//create inventory list to hold all objects
		ArrayList<InventoryItem> inventory = new ArrayList<InventoryItem>();
		
		//translate targetEnv to soruceEnv
		
		String sourceEnv = TranslateEnv.translate(targetEnv);
		
		//assemble database names for functionalDB and jobmetadata
		//if targetenv is PRODFINAL, then just extract from PROD
		
		String sourceFunctionalDb = "";
		String sourceJobmetadataDb = "" ;
		
		if (sourceEnv == "PROD") {
			sourceFunctionalDb = sourceEnv + "_" + functionalArea;
			sourceJobmetadataDb =  sourceEnv + "_JOBMETADATA";
			
		} else {
		
		sourceFunctionalDb = projectName + "_" + sourceEnv + "_" + functionalArea;
		sourceJobmetadataDb = projectName + "_" + sourceEnv + "_JOBMETADATA";
		}
		
		extractObjects(targetEnv, inventory, sourceFunctionalDb, projectName, functionalArea);
		extractObjects(targetEnv, inventory, sourceJobmetadataDb, projectName, functionalArea);
		
		return inventory;
		
	}
	
	
	private static void extractObjects(String targetEnv, ArrayList<InventoryItem> inventory, String dbName, String projectName, String functionalArea) throws SQLException {
	
	Connection connection = DbConnect.getConnection(targetEnv);
	Statement statement = connection.createStatement();
	
	String getFunctionalSchemaList = "show schemas in database " + dbName + ";";
			
	ResultSet resultSetSchemas = statement.executeQuery(getFunctionalSchemaList);
	
	while (resultSetSchemas.next()) {
		String schema = resultSetSchemas.getString("name");
		System.out.println("Schema Name: " + schema);
		
		//skip info schema
		if (schema.equals("INFORMATION_SCHEMA")) {
			continue;
		}
		
		//loop through each SnowflakeObjectType and instantiate an object, then add to the list
		
		ResultSet resultSetObjects = null;
		for (SnowflakeObjectType objectType : SnowflakeObjectType.values()) {
			//need to get rid of jobmetadata and data types
			String getObjectsStatement = null;
			
			if (objectType.toString() == "JOBMETADATA" || objectType.toString() == "DATA") {
				//ignore jobmetadata and data types as these are special for extracting DDL only
				continue;
			}
			else if (objectType.toString() == "FILE_FORMAT" ) {
				getObjectsStatement = "show FILE FORMATS in schema " + dbName + "." + schema + ";";
			}
			//get the list of objects
			else if (objectType.toString() == "FUNCTION") {
				getObjectsStatement = "show USER " + objectType.toString() + "S in schema " + dbName + "." + schema + ";";	
			}
			else {
				getObjectsStatement = "show " + objectType.toString() + "S in schema " + dbName + "." + schema + ";";
				
			}
			
			System.out.println("Running " + getObjectsStatement);
			
			resultSetObjects = statement.executeQuery(getObjectsStatement);
			
			while (resultSetObjects.next()) {
				//pass all arguments to the constructor for that object type
				//get object name out of result
				//if function or procedure, get arguments
				String objectName = resultSetObjects.getString("name");
				String signature = "NA";
				try {
					signature = resultSetObjects.getString("arguments");
					//since show output contains name and returns, just get between the parenthesis
					signature = signature.substring(signature.indexOf("(")+1,signature.indexOf(")"));
					if (signature.equals("")) {
						signature = " ";
					}
				} catch (Exception e) {
					//if no signature field, just ignore it
					
				}
				

				String destDb = TranslateEnv.translateSourceDbToTargetDb(projectName, targetEnv, functionalArea, dbName);

				InventoryItem item = new InventoryItem(projectName, functionalArea, dbName, destDb, schema, objectType.toString(), objectName, signature);
				
				System.out.println(item.toString());
				
				inventory.add(item);
			}
			
			
		}
	}
	}
	

}

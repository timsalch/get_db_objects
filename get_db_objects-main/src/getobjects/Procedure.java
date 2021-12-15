package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;

import common.DbConnect;

public class Procedure extends SnowflakeObject {
	
	Procedure() {
		setObjectType(SnowflakeObjectType.PROCEDURE);
	}
	
	Procedure(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name, String signature) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.PROCEDURE);
		setSignature(signature);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
		
	}
	
	@Override
	public String extractDdl(Connection connection) throws Exception {
		String getDdlStatement = null;
		
		//for objects which get_ddl works
		// TODO Auto-generated method stub
		
		//Connection connection = DbConnect.getConnection();
		Statement statement = connection.createStatement();
		
		getDdlStatement = "desc " ;
		getDdlStatement += this.getObjectType()  + " " ;
		getDdlStatement += this.getParentDb() + ".";
		getDdlStatement += this.getParentSchema() + "." ;
		getDdlStatement += this.getName();
		getDdlStatement += this.getSignature() ;
		
		ResultSet resultSet = statement.executeQuery(getDdlStatement);
		
		//reconstruct the DDL from the result set
		
		//get all the fields and put in a dictionary
		Dictionary<String, String> procMetadata = new Hashtable<String, String>();
		
		while (resultSet.next()) {
			//for each row, extract the property
			String property = resultSet.getString(1);
			String value = resultSet.getString(2);
			
			procMetadata.put(property, value);
			
		}
		
		//construct the DDL in the correct order
		String procDdl;
		
		procDdl = "CREATE PROCEDURE " + this.getName() + procMetadata.get("signature") + "\n";				
	    procDdl += " RETURNS " + 	procMetadata.get("returns") + 						"\n";
	    procDdl += " LANGUAGE " + 	procMetadata.get("language") + 						"\n";
	    procDdl += " " + 			procMetadata.get("null handling") + 				"\n";
	    procDdl += " " + 			procMetadata.get("volatility") + 					"\n";
	    procDdl += " EXECUTE AS " + procMetadata.get("execute as") +  					"\n";
	    procDdl += " as $$ " + 		procMetadata.get("body") + 							"$$;";
	    
	    
	    //System.out.println(procDdl);
		
		return procDdl;
	}


}

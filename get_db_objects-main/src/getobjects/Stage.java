package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;

import common.DbConnect;

public class Stage extends SnowflakeObject {
	
	Stage(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.STAGE);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
		//need to be able to translate storage locations before i can do this
		//translateEnvInDdl(targetEnvironment, getDdl());
		
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
		
		ResultSet resultSet = statement.executeQuery(getDdlStatement);
		
		//reconstruct the DDL from the result set
		
		//get all the fields and put in a dictionary
		
		String copyOptions = "";
		String fileFormatOptions = "";
		String externalStageParams = "";
		String comment = "''";
		
		while (resultSet.next()) {
		
			if (resultSet.getString("property_value") == null || resultSet.getString("property_value").equals("") || resultSet.getString("property_value").equals(resultSet.getString("property_default"))) {
				continue;
			}
			
			//for each row, extract the property
			String property = resultSet.getString("parent_property");
			String value = resultSet.getString("property") + " = " + resultSet.getString("property_value");
			
		
			
			switch (property) {
			case "STAGE_FILE_FORMAT":
				fileFormatOptions += " " + value + ",";
				break;
			case "STAGE_COPY_OPTIONS":
				if (resultSet.getString("property").equals("SIZE_LIMIT")) {
					value = resultSet.getString("property") + " = NULL " ;
				}
				copyOptions += " " + value + ",";
				break;
			case "STAGE_LOCATION":
				//externalStageParams += " " + resultSet.getString("property") + " \n";
				//the URL has brackets so need to remove them
				String url = resultSet.getString("property_value");
				externalStageParams += resultSet.getString("property") + " = '" + url.substring(2, url.length() - 2 ) + "' \n";
				break;
			case "STAGE_INTEGRATION":
				externalStageParams += " " + value + " \n";
				break;
			case "COMMENT":
				comment += " '" + value + " ";
			}
	
			
		}
		
		//construct the DDL in the correct order
		String stageDdl;
		
		
		stageDdl = "CREATE STAGE " + this.getName() + "\n";
		stageDdl += externalStageParams;
		
		//remove the last comma from the next two
		if (!fileFormatOptions.equals("")) {
		stageDdl += " FILE_FORMAT = (" + fileFormatOptions.substring(0, fileFormatOptions.length() -1) + ")\n";
		}
		if (!copyOptions.equals("")) {
			stageDdl += " COPY_OPTIONS = (" + copyOptions.substring(0, copyOptions.length() -1 ) + ")";
		}
		stageDdl += " COMMENT = " + comment;
		stageDdl += ";";
		
	    
	    //System.out.println(procDdl);
		
		return stageDdl;
	}

}

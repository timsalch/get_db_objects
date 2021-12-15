package getobjects;

import java.sql.Connection;

public class Data extends SnowflakeObject {
	
	
	
	Data(Connection connection, String project,  String functionalArea, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.DATA);
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setFunctionalArea(functionalArea);
		setDdl(extractDdl(connection));
		setTargetDb(targetDb);
		
		//need to determine source and destination tables
		//build clone statement
		//add to ddl attribute

}
	
	@Override
	public String extractDdl(Connection connection) {
		String ddlString;
		String fullTarget = null;
		if (getTargetEnvironment().toUpperCase().startsWith("PROD")) {
			fullTarget = getTargetEnvironment() + "_" + getFunctionalArea();
		} else {
			fullTarget = getProject() +"_" + getTargetEnvironment() + "_" + getFunctionalArea();
		}
		
		ddlString = " CREATE TABLE " + fullTarget + ".";
		ddlString += getParentSchema() + ".";
		ddlString += getName() + " \n";
		ddlString += " CLONE ";
		ddlString += getParentDb() + ".";
		ddlString += getParentSchema() + ".";
		ddlString += getName() + ";";
		
		return ddlString;
	}
}

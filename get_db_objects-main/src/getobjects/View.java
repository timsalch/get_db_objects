package getobjects;

import java.sql.Connection;

public class View extends SnowflakeObject {
	
	//default constructor
	View() {
		setObjectType(SnowflakeObjectType.VIEW);
	}
	
	//parameterized constructor
	View(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.VIEW);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
	}


}

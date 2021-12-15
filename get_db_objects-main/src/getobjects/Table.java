package getobjects;

import java.sql.Connection;

public class Table extends SnowflakeObject {

	//default constructor
	Table() {

		setObjectType(SnowflakeObjectType.TABLE);
	}
	
	//parameterized constructor
	Table(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.TABLE);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
	}

	


}

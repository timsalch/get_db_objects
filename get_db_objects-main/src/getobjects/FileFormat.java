package getobjects;

import java.sql.Connection;

public class FileFormat extends SnowflakeObject {

	//parameterized constructor
	FileFormat(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.FILE_FORMAT);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
	}

	
}

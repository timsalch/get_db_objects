package getobjects;

import java.sql.Connection;

public class Pipe extends SnowflakeObject{

	//parameterized constructor
	Pipe(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.PIPE);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		translateEnvInDdl(targetEnvironment, getDdl());
		setTargetDb(targetDb);
	}
	

}

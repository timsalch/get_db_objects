package getobjects;

import java.sql.Connection;

public class Task extends SnowflakeObject{

	Task(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.TASK);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		translateEnvInDdl(targetEnvironment, getDdl());
		setTargetDb(targetDb);
	}
}

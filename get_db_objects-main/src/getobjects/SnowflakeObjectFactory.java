package getobjects;

import java.sql.Connection;

public class SnowflakeObjectFactory {
	public static SnowflakeObject createObject(Connection connection , String project, String functionalArea, String targetEnvironmnet, String objectTypeInput, String parentDb, String targetDb, String parentSchema, String objectName, String signature) throws Exception {
		
		SnowflakeObject myObject = null;
		
		SnowflakeObjectType objectType = SnowflakeObjectType.valueOf(objectTypeInput.toUpperCase());
		
		switch (objectType) {
		case TABLE:
			myObject = new Table(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case PROCEDURE:
			myObject =  new Procedure(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName, signature);
			break;
		case VIEW:
			myObject = new View(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case FUNCTION:
			myObject = new Function(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName, signature);
			break;
		case JOBMETADATA:
			myObject = new JobMetadata(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case FILE_FORMAT:
			myObject = new FileFormat(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case STAGE:
			myObject = new Stage(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case PIPE:
			myObject = new Pipe(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case TASK:
			myObject = new Task(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case DATA:
			myObject = new Data(connection, project, functionalArea, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
			break;
		case SEQUENCE:
			myObject = new Sequence(connection, project, targetEnvironmnet, parentDb, targetDb, parentSchema, objectName);
		default:
			break;
		}
		
		return myObject;
	}
}

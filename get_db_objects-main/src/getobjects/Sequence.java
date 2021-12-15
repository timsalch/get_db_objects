package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.DbConnect;

public class Sequence extends SnowflakeObject {

	Sequence(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.SEQUENCE);
		setDdl(extractDdl(connection));
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
	}
	
	@Override
	public String extractDdl(Connection connection) throws SQLException {
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
		
		String ddl = "";
		
		while (resultSet.next() ) {
			ddl += "CREATE SEQUENCE " + this.getName();
			ddl += "\n START WITH = " + resultSet.getString("next_value");
			ddl += "\n INCREMENT BY =  " + resultSet.getString("interval");
			ddl += ";";
			
			
		}
		
		return ddl;
		
	}
}

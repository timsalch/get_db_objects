package getobjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DbConnect;

public class JobMetadata extends SnowflakeObject {
	
	Table sourceTable;
	
	
	//default constructor make sure we set the object type
	JobMetadata() {

		setObjectType(SnowflakeObjectType.JOBMETADATA);
	}
	
	//parameterized constructor set all the things
	JobMetadata(Connection connection, String project, String targetEnvironment, String parentDb, String targetDb, String parentSchema, String name) throws Exception {
		
		setName(name);
		setParentDb(parentDb);
		setParentSchema(parentSchema);
		setObjectType(SnowflakeObjectType.JOBMETADATA);
		setTargetEnvironment(targetEnvironment);
		setProject(project);
		setTargetDb(targetDb);
		
		//set all other attributes before running this
		this.sourceTable = new Table(connection, this.getProject(), this.getTargetEnvironment(), this.getParentDb(), this.getTargetDb(), this.getParentSchema(), this.getName());
		setDdl(extractDdl(connection, sourceTable));

		
		ArrayList<JobMetadataData> JobMetadataContents = new ArrayList<JobMetadataData>();
		
		//need to add each attribute to the DDL
		//connect to snowflake
		//select * from the table
		//find the env name
		//replace with a generic name
		
		//Connection connection = DbConnect.getConnection();
		Statement statement = connection.createStatement();
		
		JobMetadataType metadataType = JobMetadataType.valueOf(getName().toLowerCase());
		
		switch (metadataType) {
			case job_attribute:
			
				String 	selectStatement = "select job_id, job_attribute_name, replace(job_attribute_value, '\\'', '$$' ) as job_attribute_value from " + sourceTable.getParentDb();
						selectStatement += "." + sourceTable.getParentSchema();
						selectStatement += "." + sourceTable.getName();
						
				ResultSet resultSet = statement.executeQuery(selectStatement);
				
		
				
				
				while (resultSet.next()) {
					
						JobAttribute jobAttribute = new JobAttribute(	
																		resultSet.getString("JOB_ID"), 
																		resultSet.getString("JOB_ATTRIBUTE_NAME"),
																		resultSet.getString("JOB_ATTRIBUTE_VALUE").toString()
																	);
						//translate the environment in the attribute values
						//if targetEnv = prodfinal, dont trasnlate anything
						
						if (!targetEnvironment.contentEquals("PRODFINAL")) {
						jobAttribute.setJobAttributeValue(jobAttribute.translateEnvValue(jobAttribute.getJobAttributeValue(), targetEnvironment.toUpperCase()));
						}
						JobMetadataContents.add(jobAttribute);
						
			}
					break;
			case job_control:
				//construct query to extract
				String getJobControlData = "select * from " + sourceTable.getParentDb();
					   getJobControlData += "." + sourceTable.getParentSchema();
					   getJobControlData += "." + sourceTable.getName();
					   
			    ResultSet jobControlResultSet = statement.executeQuery(getJobControlData);
			    
			  //loop through query results
			    while (jobControlResultSet.next()) {
			    	JobControl jobControl = new JobControl(
			    			jobControlResultSet.getString("JOB_ID"),
			    			jobControlResultSet.getString("JOB_NAME"),
			    			jobControlResultSet.getString("JOB_DESC"),
			    			jobControlResultSet.getString("GROUP_JOB_ID"),
			    			jobControlResultSet.getString("JOB_ACTIVE_STATUS_CD"),
			    			jobControlResultSet.getString("GROUP_IND"),
			    			jobControlResultSet.getString("JOB_SEQUENCE"),
			    			jobControlResultSet.getString("TECHNOLOGY"),
			    			jobControlResultSet.getString("ALERTING_GROUP"),
			    			jobControlResultSet.getString("SOURCE_TYPE"),
			    			jobControlResultSet.getString("SUCCESS_ALERT"),
			    			jobControlResultSet.getString("FAILURE_ALERT"),
			    			jobControlResultSet.getString("SNOWPIPE_PATTERN")
			    			);
			    	
			    	//add contents to JobMetadataContents
				    JobMetadataContents.add(jobControl);
			    }
				
				//if needed translate the values
			    //not needed
			    break;
			    
			case global_attribute:
				String getGlobalAttrData = "select * from " + sourceTable.getParentDb();
					   getGlobalAttrData += "." + sourceTable.getParentSchema();
					   getGlobalAttrData += "." + sourceTable.getName();
				
				ResultSet globalAttrResultSet = statement.executeQuery(getGlobalAttrData);
				
				while (globalAttrResultSet.next()) {
					GlobalAttribute globalAttribute = new GlobalAttribute(
							globalAttrResultSet.getString("GLOBAL_ATTR_NAME"),
							globalAttrResultSet.getString("GLOBAL_ATTR_VALUE")
							);
					
					JobMetadataContents.add(globalAttribute);
				}
					   
				break;
			case job_control_extraction_date:
				String getControlExtractDate = "select * from " + sourceTable.getParentDb();
				       getControlExtractDate += "." + sourceTable.getParentSchema();
				       getControlExtractDate += "." + sourceTable.getName();
			
			ResultSet getControlExtractDateResultSet = statement.executeQuery(getControlExtractDate);
			
			while (getControlExtractDateResultSet.next()) {
				JobControlExtractionDate jobControlExtractionDate = new JobControlExtractionDate(
						getControlExtractDateResultSet.getInt("JOB_ID"),
						getControlExtractDateResultSet.getString("LAST_EXTRACTED_DATETIME"),
						getControlExtractDateResultSet.getString("OVERRIDE_START_DATETIME"),
						getControlExtractDateResultSet.getString("OVERRIDE_END_DATETIME"));
				
				JobMetadataContents.add(jobControlExtractionDate);
			}
				
		
		}
		
		String ddl = getDdl();
		
		ddl += " INSERT INTO " + getParentSchema() + "." + getName() + JobMetadataContents.get(0).getColList() + " VALUES " ;
		
		for (JobMetadataData data : JobMetadataContents) {
			ddl +=  data.toString() + ", \n";
		}
		
		ddl = ddl.substring(0, ddl.length() -3);
		
		ddl += ";";
		
		//if this is job_attribute add a command to convert the $$ back to single quote
		if (metadataType == JobMetadataType.job_attribute ) {
		String  convertQuotedStringBack = " update " ;
				convertQuotedStringBack +=  getName();
				convertQuotedStringBack += " set job_attribute_value = replace(job_attribute_value, '$$', '\\'' ); ";
		
		ddl += convertQuotedStringBack;
		}
		
		setDdl(ddl);
		

		
	}
	


}

package getobjects;

public class JobControl extends JobMetadataData {
	String jobId;
	String jobName;
	String jobDesc;
	String groupJobId;
	String jobActiveStatusCd;
	String groupInd;
	String jobSequence;
	String technology;
	String alertingGroup;
	String sourceType;
	String successAlert;
	String failureAlert;
	String snowpipePattern;
	
	JobControl(	String jobId,
				String jobName,
				String jobDesc,
				String groupJobId,
				String jobActiveStatusCd,
				String groupInd,
				String jobSequence,
				String technology,
				String alertingGroup,
				String sourceType,
				String successAlert,
				String failureAlert,
				String snowpipePattern) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobDesc = jobDesc;
		this.groupJobId = groupJobId;
		this.jobActiveStatusCd = jobActiveStatusCd;
		this.groupInd = groupInd;
		this.jobSequence = jobSequence;
		this.technology = technology;
		this.alertingGroup = alertingGroup;
		this.sourceType = sourceType;
		this.successAlert = successAlert;
		this.failureAlert = failureAlert;
		this.snowpipePattern = snowpipePattern;
		setColList(" (job_id, job_name, job_desc, group_job_id, job_Active_Status_Cd, group_ind, job_sequence, technology, alerting_group, source_type, SUCCESS_ALERT, FAILURE_ALERT, SNOWPIPE_PATTERN)");
	}
	
	public String toString() {
		String returnString;
		returnString = " ( " + jobId;
		returnString +=  ",'" + jobName;
		returnString +=  "','" + jobDesc;
		returnString +=  "'," + groupJobId;
		returnString +=  ",'" + jobActiveStatusCd;
		returnString +=  "','" + groupInd;
		returnString +=  "'," + jobSequence;
		returnString +=  ",'" + technology;
		returnString +=  "','" + alertingGroup;
		returnString +=  "','" + sourceType;
		returnString +=  "','" + successAlert;
		returnString +=  "','" + failureAlert;
		returnString +=  "','" + snowpipePattern;
		returnString +=		"' )";
		
		return returnString;
	}
	
	
}

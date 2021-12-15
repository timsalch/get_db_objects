package getobjects;

public class JobAttribute extends JobMetadataData {
	String jobId;
	String jobAttributeName;
	String jobAttributeValue;
	String modifiedDate;
	String createdDate;
	String createdBy;
	
	
	JobAttribute(
					String jobId, 
					String jobAttributeName, 
					String jobAttributeValue
					//String modifiedDate, 
					//String modifiedBy, 
					//String createdDate, 
					//String createdBy
					) {
		this.jobId = jobId;
		this.jobAttributeName = jobAttributeName;
		this.jobAttributeValue = jobAttributeValue;
		//this.modifiedDate = modifiedDate;
		//this.createdDate = createdDate;
		//this.createdBy = createdBy;
		setColList(" (job_id, job_attribute_name, job_attribute_value) ");
		
	}
	
	public String toString() {
		String returnString = "(" + jobId + ", '" + jobAttributeName + "','" + jobAttributeValue + "')";
		return returnString;
	}
	
	public void setJobAttributeValue(String newValue) {
		this.jobAttributeValue = newValue;
	}
	
	public String getJobAttributeValue() {
		return this.jobAttributeValue;
	}
	
	
	//need a method to parse the attribute value and get the environment name
}

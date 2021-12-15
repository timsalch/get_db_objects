package getobjects;

public class JobControlExtractionDate extends JobMetadataData {

	Number jobId;
	String lastExtaractedDateTime;
	String overrideStartDateTime;
	String overrideEndDateTime;
	
	JobControlExtractionDate(Number jobId, String lastExtractedDateTime, String overrideStartDateTime, String overrideEndDateTime) {
		this.jobId = jobId;
		this.lastExtaractedDateTime = lastExtractedDateTime;
		this.overrideStartDateTime = overrideStartDateTime;
		this.overrideEndDateTime = overrideEndDateTime;
		setColList(" (job_id, last_extracted_datetime, override_start_datetime, override_end_datetime ) ");
	}
	
	public String toString() {
		String returnString = "( " + this.jobId + " , '" + this.lastExtaractedDateTime + "', " + this.overrideStartDateTime + ", " + this.overrideEndDateTime + ") ";
		
		return returnString;
	}
}

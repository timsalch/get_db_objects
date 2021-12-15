package getobjects;

public class TranslateEnv {
	public static String translate(String destEnv) {
		String sourceEnv = null;
		
		//get the previous environment, only allow promoting one environment at a time
		switch (destEnv.toUpperCase()) {
		//only doing prod for now which can only be upgraded from QA
		case "PROD":
			sourceEnv = "PT";
			break;
		case "PRODSTAGE":
			sourceEnv = "PT";
			break;
		case "PRODFINAL":
			sourceEnv = "PROD";
			break;
		case "QA":
			sourceEnv = "QA";
			break;
		case "PT":
			sourceEnv = "QA";
			break;
		default:
			sourceEnv = "QA";
		}
		
		return sourceEnv;
	}
	
	public static String translateSourceDbToTargetDb(String project, String targetEnvironment, String functionalArea, String parentDb) {
		String targetDb = "";
		
		if (!targetEnvironment.startsWith("PROD") && !parentDb.endsWith("JOBMETADATA")) {
			targetDb = project + "_" + targetEnvironment + "_" + functionalArea;
		}
		else if (targetEnvironment.startsWith("PROD") && !parentDb.endsWith("JOBMETADATA")) {
			
			//if this is for prodfinal, then change targetEnv to PROD
			if (targetEnvironment.equals("PRODFINAL") ) {
				targetEnvironment = "PROD";
			}
			
			targetDb = targetEnvironment + "_" + functionalArea;
		}
		else if (!targetEnvironment.startsWith("PROD") && parentDb.endsWith("JOBMETADATA")) {
			targetDb = project + "_" + targetEnvironment + "_JOBMETADATA";
			
		}
		else if (targetEnvironment.startsWith("PROD") && parentDb.endsWith("JOBMETADATA")) {
			
			//if this is for prodfinal, then change targetEnv to PROD
			if (targetEnvironment.equals("PRODFINAL") ) {
				targetEnvironment = "PROD";
			}
			
			targetDb = targetEnvironment + "_JOBMETADATA";
		}
		
		return targetDb;
		
	}
	
	public static String translateTargetDbToSourceDb(String project, String targetEnvironment, String functionalArea, String targetDb) {
		String sourceDb = "";
		String sourceEnv = translate(targetEnvironment);
		
		System.out.println(targetDb.endsWith("JOBMETADATA"));
		
		if (!sourceEnv.startsWith("PROD") && !targetDb.endsWith("JOBMETADATA")) {
			sourceDb = project + "_" + sourceEnv + "_" + functionalArea;
		}
		else if (targetEnvironment.startsWith("PROD") && !targetDb.endsWith("JOBMETADATA")) {
			sourceDb = sourceEnv + "_" + functionalArea;
		}
		else if (!targetEnvironment.startsWith("PROD") && targetDb.endsWith("JOBMETADATA")) {
			sourceDb = project + "_" + sourceEnv + "_JOBMETADATA";
			
		}
		else if (targetEnvironment.startsWith("PROD") && targetDb.endsWith("JOBMETADATA")) {
			//sourceDb = project + "_" + sourceEnv + "_JOBMETADATA";
			sourceDb =  sourceEnv + "_JOBMETADATA";
		}
		
		
		return sourceDb;
		
	}
}

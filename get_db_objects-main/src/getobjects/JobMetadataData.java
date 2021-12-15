package getobjects;

public abstract class JobMetadataData {
	private String colList;
	private String sourceEnv;
	
	public void setColList(String colList) {
		this.colList = colList;
	}
	
	public String getColList() {
		return this.colList;
	}
	
	public String translateEnvValue(String fieldToChange, String destEnv) {
		
		
		sourceEnv = TranslateEnv.translate(destEnv);
		
		//debugging - prints all values before and after
		//System.out.println(fieldToChange);
		//System.out.println("New Value: " + fieldToChange.replaceAll(sourceEnv, destEnv));
		
		
		//need to check if the attr is a value name, if so drop the project name
		
		//check to see if value contains environment
		String translatedValue;
		
		if (fieldToChange.toUpperCase().contains(sourceEnv)) {
			//if destEnv is prod, then remove the project name
			

			
			if (destEnv.contentEquals("PROD") && fieldToChange.split("_").length == 3 ) {
				translatedValue = destEnv + "_" + fieldToChange.split("_")[2];
			} else {
			translatedValue = fieldToChange.toUpperCase().replaceAll(sourceEnv, destEnv);
			}
		} else {
			//if it doesnt, just return the same value back
			translatedValue = fieldToChange;
		}
		
		return translatedValue;
	}
}

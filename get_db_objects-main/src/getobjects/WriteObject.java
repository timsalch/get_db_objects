package getobjects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteObject {

	public static String writeToFile(SnowflakeObject myObject, String rootPath) throws IOException {
		//determine what parent directory the object should be written to
		
		String thePath = rootPath + "resources/" + myObject.getTargetDb() + "/" + myObject.getParentSchema() + "/" + myObject.getObjectType().toString().toLowerCase() + "/";
		
		String fileName = thePath + myObject.getName() + "." + myObject.getObjectType().toString().toLowerCase(); 
		
		File fileChecker = new File(fileName);
		
		if (fileChecker.exists()) {
			fileName = thePath + myObject.getName() + "2." + myObject.getObjectType().toString().toLowerCase();
		}
		
		System.out.println("Creating file: " + fileName);
		
		
		//create the directory it it does not exist
		
		File objectPath = new File(thePath);
		objectPath.mkdirs();
		
			
		//get the db objects from the database
		//String dbObjectString = getDbObjects(database);
		String objectDDL = myObject.getDdl();
		
		//debug
		//System.out.println(objectDDL);
		
		//write the get_ddl output to a file
	    try {
			FileOutputStream outputStream = new FileOutputStream(fileName);
		    byte[] strToBytes = objectDDL.getBytes();
		    outputStream.write(strToBytes);
		    outputStream.close();
		}
	    catch (IOException e) {
	    	System.out.println(e);
	    }
	  
	    
	    
	    myObject.setFileName(fileName);
	    
	    System.out.println("File Created Successfully");
	    
	    return fileName;
		

	};
}


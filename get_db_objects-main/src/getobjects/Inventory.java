package getobjects;

import java.io.*;
import java.util.ArrayList;

public class Inventory {
	//need to read the inventory from the file system
	//parse to get all objcet
	//how to instantiate anF object for each?  a method to do so?
	
	ArrayList<InventoryItem> inventory = new ArrayList<InventoryItem>();
	
	//create the inventory from a database
	Inventory(ArrayList<InventoryItem> itemList) {
		inventory = itemList;
	}
	
	//create the inventory from a file
	Inventory(String inventoryPath, String targetEnvironment) {
		
		try {
			
			File inventoryFile = new File(inventoryPath);
		
			BufferedReader br = new BufferedReader(new FileReader(inventoryFile));
			
			String line = "";
			
			//iterator to identify the first row
			int i = 0;
			while ((line = br.readLine()) != null) {
				
				//if the first row is a header we want to discard it
				if (i > 0) {
				
				String[] item = line.split("\t");
								
				//derive the sourceDb
				String sourceDatabase = TranslateEnv.translateTargetDbToSourceDb(item[0], targetEnvironment, item[1], item[2]);
				
				System.out.println(item[0]);
				System.out.println(targetEnvironment);
				System.out.println(item[1]);
				System.out.println(item[2]);
				
				System.out.println(sourceDatabase);
				
				InventoryItem inventoryItem = new InventoryItem(item[0], item[1], sourceDatabase, item[2], item[3], item[4], item[5], item[6]);
				
				inventory.add(inventoryItem);
				
				}
				i++;

			}
			
			//for debugging
			System.out.println("\n INVENTORY CONTENTS \n");
			for ( InventoryItem item : inventory) {
				System.out.println(item.toString());
			}
			System.out.println("\n END INVENORY \n");
			
			}
		catch (Exception e) {
			System.out.println(e);
			}
	
		
	
	}
	
	public String writeInventoryFile(String inventoryPath) throws IOException {
		
		//create the file
		File inventoryFile = new File(inventoryPath);
		
		//write all objects to the file
		FileWriter fw = new FileWriter(inventoryFile);
		
		fw.write("project\tfunctional_area\tSnowflake_Db\tSnowflake_Schema\tobject_type\tobject_name\tsignature\tpath\n");
		
		//iterate through all items in the list and write each one
		for (InventoryItem item : this.inventory ) {
			fw.write(item.toFile());
		}
		
		fw.close();
		
		return inventoryPath;
	}
}


package getobjects;

import java.sql.Connection;
import java.sql.Statement;

import common.DbConnect;

public class SyncObjectsToFile {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		//gettignt hese from the inventory file now
		/*
		
		//arg[0] should be objectType
		String objectTypeInput = args[0];
		
		//arg[1] should be parentDb
		String parentDb = args[1];
		
		//arg[2] should be parentSchema
		String parentSchema = args[2];
		
		//arg[3] should be the object name
		String objectName = args[3];
		
		//arg[4] should be the signature for applicable types
		String signature = args[4];
		
		*/
		
		//arg[0] should be createinventory or filesync
		String actionInput = args[0];
		
		//arg[1] should eb the home dir to use
		String homeDir = args[1];
		
		//arg[2] should be the path to the local inventory file
		String inventoryPath = args[2];
		
		//arg[3] should be the target environment
		String targetEnvironment = args[3].toUpperCase();
		
		
		MainActionType action = MainActionType.valueOf(actionInput.toLowerCase());
		
		
		Inventory inventory = null;
		
		
		Connection connection = DbConnect.getConnection(targetEnvironment);
		
		switch (action) {

		case filesync:
			inventory = new Inventory(inventoryPath, targetEnvironment);
			
			for (InventoryItem item : inventory.inventory) {
			
			SnowflakeObject mySnowflakeObject = GetObject.getObjectDDL(
																		item.getProject(),
																		item.getFuncationalArea(),
																		targetEnvironment,
																		item.getObjectType(),
																		item.getSourceDatabase(),
																		item.getTargetDatabase(),
																		item.getSchema(),
																		item.getObjectName(),
																		item.getSignature(),
																		connection);
			
			
			WriteObject.writeToFile(mySnowflakeObject, homeDir);
			}
			System.out.println("\n!!!DONE!!!\n");
			
			break;
		case createinventory:
			inventory = new Inventory((CreateInventory.createInventory(targetEnvironment, "EDC", "EPM")));
			inventory.writeInventoryFile(inventoryPath);
			
			System.out.println("\n!!!DONE!!!\n");
			
			break;
			
		}
		


	}

}

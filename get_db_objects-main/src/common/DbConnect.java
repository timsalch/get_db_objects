package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnect {
	public static Connection getConnection(String targetEnvironment)
	          throws SQLException
	  {
	    try
	    {
	      Class.forName("com.snowflake.client.jdbc.SnowflakeDriver");
	    }
	    catch (ClassNotFoundException ex)
	    {
	     System.err.println("Driver not found");
	    }
	    
	    /*
	    // build connection properties
	    Properties properties = new Properties();
	    properties.put("user", "tsalch");     // replace "" with your username
	    properties.put("password", "u[hJqYfbcCQ{68nvZMcF"); // replace "" with your password
	    properties.put("account", "aws_cas2");  // replace "" with your account name
	    properties.put("warehouse", "adhoc");
	    //properties.put("db", "");       // replace "" with target database name
	    //properties.put("schema", "");   // replace "" with target schema name
	    //properties.put("tracing", "on");
	      */
	     
	    
	    Properties properties = new Properties();
	    properties.put("user", "salchta");     // replace "" with your username
	    properties.put("password", "Ts$#701913"); // replace "" with your password
	    properties.put("account", "edwsce");  // replace "" with your account name
	    properties.put("warehouse", "admin_wh");
	    properties.put("authenticator", "externalbrowser");
	    
	    //determine if it is prod or another environment to know what role to use
	    
	    switch (targetEnvironment) {

		    case ("PROD"):
		    	properties.put("role", "EDWSF_DEVOPS_SVC_PROD_ROLE");
		    	break;
		    case ("PRODSTAGE"):
		    	properties.put("role", "EDWSF_DEVOPS_SVC_PROD_ROLE");
		    	break;
		    case ("PRODFINAL"):
		    	properties.put("role", "sysadmin");
		    	break;
		    default:
		    	//for now if not prod it all comes from QA
		    	properties.put("role", "edwsf_devops_pt_pt_role");
		    break;
		    
		    	}
	    //debugging
	    //System.out.println(properties.get("role"));
	    
	    // create a new connection
	    String connectStr = System.getenv("SF_JDBC_CONNECT_STRING");
	    // use the default connection string if it is not set in environment
	    if(connectStr == null)
	    {
	     connectStr = "jdbc:snowflake://edwsce.west-us-2.azure.snowflakecomputing.com"; // replace accountName with your account name
	    }
	    return DriverManager.getConnection(connectStr, properties);
	  }
	}


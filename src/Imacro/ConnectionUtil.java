package Imacro;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


//For database connection
public class ConnectionUtil { 
	// String connectionUrl1="", database1="", user1="",password1="";
	  static Properties properties = new PropertyLoader().getPropertyFile();
	
	public static final String connectionUrl =
			 properties.getProperty("connectionUrl")
	                    + "database="+properties.getProperty("database")
	                    + "user="+properties.getProperty("user")
	                    + "password="+properties.getProperty("password")
	                    + "encrypt="+properties.getProperty("encrypt")
	                    + "trustServerCertificate="+properties.getProperty("trustServerCertificate")
	                    + "loginTimeout="+properties.getProperty("loginTimeout");


/*	public static final String connectionUrl =
            "jdbc:sqlserver://172.29.58.41:1433;"
                    + "database=MIDAS;"
                    + "user=MIDAS;"
                    + "password=Midas@1234;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "loginTimeout=30;";*/
	public static final String DRIVER_CLASS="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static Connection con = null;
	public static Connection getConnection() {
		//System.out.println( properties.getProperty("connectionUrl")+"url for connection");
		if (con==null) {
        try {
        	Class.forName(DRIVER_CLASS);
    		con = DriverManager.getConnection(connectionUrl);
    		
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
		}
        return con;
}
}

package Imacro;

import java.io.FileInputStream;
import java.util.Properties;


public class PropertyLoader {
	public Properties getPropertyFile() {
		Properties properties = null;
		String strPropertyPath = "";
		FileInputStream propfile = null;
		try {

			strPropertyPath = new String(this.getClass().getResource(
					"/imacro/config.properties").getPath());
			System.out.println(strPropertyPath+"path of project and properties file");

			//if (strPropertyPath != null) {
			//	strPropertyPath = strPropertyPath.replaceAll("%20", " ");

				propfile = new FileInputStream(strPropertyPath);

				if (propfile != null) {
					properties = new Properties();
					properties.load(propfile);
					System.out.println(properties.getProperty("database")+"cnwdjbbchdcvgjvdcgvjdgvcidg");
					
					String s=
					properties.getProperty("connectionUrl")
					+ "database="+properties.getProperty("database")
					+ "user="+properties.getProperty("user")
					+ "password="+properties.getProperty("password")
					+ "encrypt="+properties.getProperty("encrypt")
					+ "trustServerCertificate="+properties.getProperty("trustServerCertificate")
					+ "loginTimeout="+properties.getProperty("loginTimeout");
					System.out.println(s);
				}
		//	}

		} catch (Exception e) {
			System.out.println("Error while loading property file :: ");
			e.printStackTrace();
		} finally {
			try {
				propfile.close();
			} catch (Exception e) {
				System.out.println("Error while closing input stream :: " + e);
			}
		}

		return properties;
	}
}

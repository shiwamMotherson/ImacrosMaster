package Imacro;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileLoader {
	public Properties getPropertyFile() {
		Properties properties = null;
		String strPropertyPath = "";
		FileInputStream propfile = null;
		try {

			strPropertyPath = new String(this.getClass().getResource(
					"/iMacroSfReadUpload/db.properties").getPath());
			

			if (strPropertyPath != null) {
				strPropertyPath = strPropertyPath.replaceAll("%20", " ");

				propfile = new FileInputStream(strPropertyPath);

				if (propfile != null) {
					properties = new Properties();
					properties.load(propfile);
				}
			}

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

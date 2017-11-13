package com.weekendesk.anki;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class AnkiProperties {
	
	public static Properties properties;
	
	public static void loadProperties(String fileName) {
		
		properties = new Properties();
		try {
			
		  URL url = ClassLoader.getSystemResource(fileName);
		  properties.load(url.openStream());
		  
		} catch (IOException e) {}
		
	}
	
	
	public static String getProperty(String propName) {
		
		return properties.getProperty(propName);
		
	}
}

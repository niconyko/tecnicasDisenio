package ar.com.grupo1.tecnicas.Logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


public class ParserFileProperties {

	private String filename;
	private ArrayList<String> listConf;
	private String datePattern = null;
	private String delimeter = "-";
	private String level = "0";
	
	public ParserFileProperties(String fileProperties){
		filename = fileProperties;
	}
	
	private void processDatePattern() {
		for (int i = 0; i < listConf.size(); i++) {
			String field = listConf.get(i);
			char caracter = field.charAt(1);
    		if( caracter == 'd' ){
    			String shortField = field.substring(0,2);
    			datePattern = field.substring(3, (field.length() - 1) );
            	listConf.set(i, shortField);
    		}
		}
	}
	
	private void processFormat(Properties prop) {
		String format = prop.getProperty("format");
		listConf = new ArrayList<String>(Arrays.asList(format.split(" ")));
		processDatePattern();
	}
	
	private void processDelimeter(Properties prop) {
		String readDelimeter = prop.getProperty("delimeter");
		if (readDelimeter == null) return;
		this.delimeter = readDelimeter;
	}
	
	private void processLevel(Properties prop) {
		String readLevel = prop.getProperty("level");
		if (readLevel == null) return;
		this.level = readLevel;
	}
	
	public ArrayList<String> parser() {
	
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			input = new FileInputStream(filename);
			prop.load(input);
			processFormat(prop);
			processDelimeter(prop);
			processLevel(prop);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return listConf;
	}
	
	public String getDatePattern(){
		return this.datePattern;
	}

	public String getDelimeter() {
		return this.delimeter;
	}
	
	public String getFilterLevel() {
		return this.level;
	}
}
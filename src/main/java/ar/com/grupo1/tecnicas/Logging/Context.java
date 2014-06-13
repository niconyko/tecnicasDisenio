package ar.com.grupo1.tecnicas.Logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


public class Context {

	private String name, nameMethod, fileName, threadName, level, message, datePattern, delimeter, lineNumber;
	private Hashtable<String, String> data;
	
	
	public Context(String name, String level, String message, String datePattern, String delimeter) {
		this.nameMethod = Thread.currentThread().getStackTrace()[3].getMethodName();
		this.lineNumber = String.valueOf(Thread.currentThread().getStackTrace()[3].getLineNumber());
		this.fileName = Thread.currentThread().getStackTrace()[3].getFileName();
		this.threadName = Thread.currentThread().getName();
		this.level = level;
		this.message = message;
		this.datePattern = processDate(datePattern);
		this.delimeter = delimeter;
		this.name = name;
		
		data = new Hashtable<String, String>();
		loadConfigHash();
	}

	private String processDate(String datePattern) {
		if(datePattern == null){ 
			return "";
		}
		SimpleDateFormat date = new SimpleDateFormat(datePattern);
		return date.format(new Date());
	}
	
	private void loadConfigHash() {
		data.put("%d", this.datePattern);
		data.put("%p", this.level);
		data.put("%t", this.threadName);
		data.put("%m", this.message);
		data.put("%%", "%");
		data.put("%n", this.delimeter);
		data.put("%L", this.lineNumber);
		data.put("%F", this.fileName);
		data.put("%M", this.nameMethod);
		data.put("%g", this.name);
	}
	
	public String getData(String key) {
		return data.get(key);
	}
	
	public String getDelimeter() {
		return this.delimeter;
	}
	
	private String generateJson(){
		String date =  processDate("yyyy-MM-ddzhh:mm:ss"); 
		return "{\"datetime\": \""+date+"\",\"level\": \""+this.level+"\", \"logger\": \""+this.name+"\" ,\"message\": \""+this.message +"\"}";
	}

	protected String generateMessage(Configuration configuration) {
		ArrayList<String> dataList = configuration.getConfiguration();
		String finalString = "";
		if( dataList.get(0).equals("json") ){
			finalString += generateJson();
		}
		else{
			
			for (String key : dataList) {
				finalString += this.getData(key);
				finalString += this.getDelimeter();
			}
			
			// Elimina el ultimo delimeter
			finalString = finalString.substring(0, finalString.length() - 1);
		}
		return finalString;
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Context)){
			return false;
		}
		Context context = (Context) object;
		return (this.nameMethod == context.getData("%F") && this.lineNumber == getData("%L")
				&& this.threadName == context.getData("%t") && this.level == context.getData("%p") );	
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(this.getData("%L")) % 7;
	}
	 
}

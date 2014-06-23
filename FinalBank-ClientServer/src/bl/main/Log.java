package bl.main;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Log implements Filter {

	private FileHandler handler;
	private String fileName;
	public static Object locker = new Object();
	
	//Constructor
	public Log(String name) throws SecurityException, IOException {
		
		setFileName(name);
		setHandler(name);
		BankManager.logger.addHandler(handler);
	}
	
	public void addToLog(String text) {
		
		synchronized (locker) {
			BankManager.logger.log(Level.INFO, text);
		}
		
	}
	
	@Override
	public boolean isLoggable(LogRecord record) {

		if(record.getMessage().contains(fileName)) 
			return true;
		
		return false;
	}
	
	
	//Getters & Setters
	public void setHandler(String nameOfFile) throws SecurityException, IOException	{
		
		handler = new FileHandler(nameOfFile + ".txt");
		handler.setFilter(this);
		handler.setFormatter(new myFormattor());
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String Name) {
		this.fileName = Name;
	}

	
}

package bl.bank_services;

import java.io.IOException;

import org.w3c.dom.Element;

import bl.main.BankManager;
import bl.main.XmlHandler;


public class Atm extends ClientService implements ServiceActions  {

	
	String location;

	//Constructor
	public Atm(String location, int id) throws SecurityException, IOException  {
		super(id);
		setLocation(location);
	}
	
	@Override
	public void cashDeposit(double amount)
	{
		super.cashDeposit(amount);
		logHandler.addToLog(currentClient.getClientName() + " ( " + currentClient.getBankName() + " )" + 
				" deposit amount of " + (int)amount + " using " + getLogFileName());
		
	}

	@Override
	public  void cashWithdraw(double amount){
		
		super.cashWithdraw(amount);
		logHandler.addToLog(currentClient.getClientName() +  " ( " + currentClient.getBankName() + " )" + 
				" withraw amount of " + (int)amount + " using " + getLogFileName());
	}

	@Override
	public void getReport() {

		logHandler.addToLog(currentClient.getClientName() +  " ( " + currentClient.getBankName() + " )" +
							" get Report using " + getLogFileName());
		super.getReport();
	}
	
	@Override
	public void addAuthorization(String organiztion)
	{
		super.addAuthorization(organiztion);
		logHandler.addToLog(currentClient.getClientName() +  " ( " + currentClient.getBankName() + " )" +
							" add authorization to " + organiztion + " using " + getLogFileName());
	}
	
	
	public static Atm getAtmInstance(int indexOfAtm) throws SecurityException, IOException 
	{
		Element atmElement = (Element)XmlHandler.getElement("atm", BankManager.XML_FILE_NAME, indexOfAtm);
		String location = atmElement.getAttribute("location");
		int id = Integer.parseInt(atmElement.getAttribute("id"));
		
		return new Atm(location, id);
	}

	//Getters & Setters

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}

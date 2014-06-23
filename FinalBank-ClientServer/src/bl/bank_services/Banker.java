package bl.bank_services;

import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Banker extends ClientService implements ServiceActions {

	String bankerName;
	double commission;

	//Constructor
	public Banker(int id, String name, double commission) throws SecurityException, IOException{
		super(id);
		setBankerName(name);
		this.commission = commission;
	}

	@Override
	public void cashDeposit(double amount) {
	
		super.cashDeposit(amount - commission);
		logHandler.addToLog(currentClient.getClientName() + " ( " + currentClient.getBankName() + " )" + 
							" deposit amount of " + (int)amount + " using " + getLogFileName() + ", the commission for this is:" +  commission);
		
	}
	
	@Override
	public void cashWithdraw(double amount){
		
		super.cashWithdraw(amount + commission);
		logHandler.addToLog(currentClient.getClientName() + " ( " + currentClient.getBankName() + " )" + 
							" withraw amount of " + (int)amount + " using " + getLogFileName() + ", the commission for this is: " +  commission);
	}

	@Override
	public void getReport() {
		
		super.cashWithdraw(commission);
		logHandler.addToLog(currentClient.getClientName() + " ( " + currentClient.getBankName() + " )" + 
							" get Report using " + getLogFileName() + ", the commission for this is: " +  commission);
		super.getReport();
	}
	
	@Override
	public void addAuthorization(String organiztion) {
	
		super.addAuthorization(organiztion);
		super.cashWithdraw(commission);
		logHandler.addToLog(currentClient.getClientName() + " ( " + currentClient.getBankName() + " )" +
							" add authorization to " + organiztion + " using " + getLogFileName() + ", the commission for this is: " +  commission);
		
	}
	
	//function that gets a Branch node and creates an array of bankers that belongs to the branch 
	public static Banker[] getBankersList(Node node) throws SecurityException, IOException 
	{
		Banker[] bankers;
		Element bankerElement = (Element)node;
		NodeList bankersList = bankerElement.getElementsByTagName("banker");
		int listLen = bankersList.getLength();
		bankers = new Banker[listLen];
		for(int j = 0; j < listLen; j++)
		{
			bankers[j] = getBankerInstance(bankersList.item(j));
			bankers[j].setName(bankers[j].getName());
		}
	
		return bankers;
	}
	
	//function that gets Banker node and creates a Banker instance with name and id
	public static Banker getBankerInstance(Node node) throws SecurityException, IOException
	{
		Element bankerElement = (Element)node;
		String name = bankerElement.getAttribute("name");
		int id = Integer.parseInt(bankerElement.getAttribute("id"));
		
		return new Banker(id, name, 0);
	}
	
	
	//Getters & Setters
	public void setBankerName(String name) {
		this.bankerName = name;
	}

	public String getBankerName() {
		return bankerName;
	}
	
	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getCommission() {
		return commission;
	}
	
}

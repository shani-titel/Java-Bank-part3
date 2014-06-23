package bl.main;

import java.io.IOException;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bl.bank_services.Atm;
import bl.bank_services.Banker;
import bl.bank_services.ClientService;
import bl.bank_services.ServiceActions;


public class Client extends Thread implements ServiceActions{

	public static final int INPUT = 0;
	public static final int OUTPUT = 1;
	public static final int MAX_DEPOSIT_AMOUNT = 10000;
	public static final int MAX_WITHROW_AMOUNT = 2500;

	public static boolean isAlive = true;
	
	private boolean inProcess = false;  
	private String cname;
	private String bankName;
	private int id;
	private int counter = 0;
	private int ioSelection;
	private double balance;
	private Log logHandler;
	private Vector<String> inputAuthorization;
	private Vector<String> outputAuthorization;
	private ClientService clientService; 
	
	//Constructor
	public Client(int id, String name, double balance,
			Vector<String> inputAuthorization,
			Vector<String> outputAuthorization) throws SecurityException, IOException {
		
		super();
		setClientId(id);
		setClientName(name);
		setBalance(balance);
		setLogHandler(new Log(getClientName()));
		setInputAuthorization(inputAuthorization);
		setOutputAuthorization(outputAuthorization);	
	}
	public Client(int id, String name, double balance) throws SecurityException, IOException{
		setClientId(id);
		setClientName(name);
		setBalance(balance);
		setLogHandler(new Log(getClientName()));
		inputAuthorization = new Vector<>();
		outputAuthorization = new Vector<>();
	}
	
	
	public static void goHome() {
		Client.isAlive = false;
	}

	
	@Override
	public void cashDeposit(double amount) {
		balance += amount;
	}

	@Override
	public void cashWithdraw(double amount) {
		balance -= amount;
	}
	
	@Override
	public void getReport() {
		
		BankManager.printReport(XmlHandler.readTxtfile(logHandler.getFileName()), cname);
	}

	@Override
	public void addAuthorization(String organiztion) {
		
		if(ioSelection == INPUT)
			inputAuthorization.add(organiztion);
		else
			outputAuthorization.add(organiztion);
		
	}
	
	
	@Override
	public synchronized void run() {
		
		while(true)
		{
			
			try {
				wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			//if the branch closes before the client "decides" what action he wants
			//he should leave the branch and go home
			if(isAlive == false && isInProcess() == false) {
				if(clientService != null)
					clientService.releaseService();
				setService(null);
				setInProcess(false);
				return;
			}
				
			//sleep till the service will be set (after notifying the client)
			while(clientService == null && isAlive == true) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			//sleep till the chosen action will be set (after notifying the client)
			if(clientService.getAction() == -1) {
				
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			}
			
			
			switch(clientService.getAction()) {
			
				case BankManager.CASH_DEPOSIT: {
					
					if(clientService instanceof Banker) 
						((Banker) clientService).cashDeposit(NumberSelector.selectInteger(MAX_DEPOSIT_AMOUNT) + 1);
					else
						((Atm) clientService).cashDeposit(NumberSelector.selectInteger(MAX_DEPOSIT_AMOUNT) + 1);
				}
				
				break;
				
				case BankManager.CASH_WITHDRAW: {
					
					if(clientService instanceof Banker)
						((Banker) clientService).cashWithdraw(NumberSelector.selectInteger(MAX_WITHROW_AMOUNT) + 1);
					else
						((Atm) clientService).cashWithdraw(NumberSelector.selectInteger(MAX_WITHROW_AMOUNT) + 1);
				}
				
				break;
				
				case BankManager.GET_REPORT: {
					
					if(clientService instanceof Banker)
						((Banker) clientService).getReport();
					else
						((Atm) clientService).getReport();
				}
				
				break;
				
				case BankManager.ADD_AUTHORIZATION:	{
					
					if(clientService instanceof Banker)
						((Banker) clientService).addAuthorization("MoneyCharger" + ++counter);	
					else
						((Atm) clientService).addAuthorization("MoneyCharger" + ++counter);		
				}
				
				break;
				
			}
			
			try {
				Thread.sleep(500);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			//after the client finishes his action he will release the service
			//also set his current service to null and "inProcess" to false
			clientService.releaseService();
			setService(null);
			setInProcess(false);
		}
		
	}
	
	//function that gets a Branch node and creates an array of clients that belongs to the branch
	public static Client[] getClientsList(Node node) throws SecurityException, IOException {
		
		Client[] clients;
		Element clientElement = (Element)node;
		NodeList clientsList = clientElement.getElementsByTagName("client");
		int listLen = clientsList.getLength();
		clients = new Client[listLen];
		for(int j = 0; j < listLen; j++)
		{
			clients[j] = getClientInstance(clientsList.item(j));
			clients[j].setName(clients[j].getClientName());
		}
	
		return clients;
	}
	
	//function that gets Client node and creates a Client instance with name, cash ,id and authorizations
	public static Client getClientInstance(Node clientNode) throws SecurityException, IOException 
	{
		
		Element clientElement = (Element)clientNode;
		int balance = Integer.parseInt(clientElement.getAttribute("cash"));
		String name = clientElement.getAttribute("name");
		int id = Integer.parseInt(clientElement.getAttribute("id"));
		Vector<String> inputAuthorization = new Vector<String>();
		Vector<String> outputAuthorization = new Vector<String>();
		
		NodeList authorizationList = clientElement.getElementsByTagName("authorization");
		
		for(int i = 0; i < authorizationList.getLength(); i++) {
			
			Node authorizationNode = authorizationList.item(i);
			
			if (authorizationNode.getNodeType() == Node.ELEMENT_NODE) {
				 
				Element authorizationElement = (Element) authorizationNode;
				
				if(authorizationElement.getAttribute("type").equals("input"))
					inputAuthorization.add(authorizationElement.getAttribute("from"));
				
				else
					outputAuthorization.add(authorizationElement.getAttribute("from"));

			}
		}
	
		return new Client(id, name, balance, inputAuthorization, outputAuthorization);
	}
	
	
	//Getters & Setters
	public String getClientName() {
		return cname;
	}

	public void setClientName(String name) {
		this.cname = name;
	}

	public int getClientId() {
		return id;
	}

	public void setClientId(int id) {
		this.id = id;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public Log getLogHandler() {
		return logHandler;
	}

	public void setLogHandler(Log logHandler) {
		this.logHandler = logHandler;
	}

	public Vector<String> getInputAuthorization() {
		return inputAuthorization;
	}

	public void setInputAuthorization(Vector<String> inputAuthorization) {
		this.inputAuthorization = inputAuthorization;
	}

	public Vector<String> getOutputAuthorization() {
		return outputAuthorization;
	}

	public void setOutputAuthorization(Vector<String> outputAuthorization) {
		this.outputAuthorization = outputAuthorization;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public void setIoSelection(int ioSelection) {
		this.ioSelection = ioSelection;
	}

	public void setService(ClientService service) {
		
		clientService = service;
	}
	
	public boolean isInProcess() {
		return inProcess;
	}


	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}



}

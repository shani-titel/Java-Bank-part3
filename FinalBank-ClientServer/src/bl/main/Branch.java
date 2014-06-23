package bl.main;


import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;



import bl.bank_services.Banker;
import bl.bank_services.ClientService;

public class Branch{

	private String name;
	private int id;
	private double commission;
	private Log handler;
	private Banker bankers[];
	private Client clients[];
	
	
	//Constructor
	public Branch(String name, int id, double commission, Client[] clients, Banker[] bankers) throws SecurityException, IOException  {
		
		setName(name);
		setId(id);
		setCommission(commission);
		setHandler(new Log(getName()));
		setBankers(bankers);
		setClients(clients);
		
		//start bankers threads when branch "opened"
		for (int i = 0; i < bankers.length; i++) {
			bankers[i].setCommission(commission);
			bankers[i].setName(bankers[i].getLogFileName());
			bankers[i].start();
		}
		
	}
	
	//close branch
	public void close() {
		
		//static function that sets isAlive static variable of class Client to false
		Client.goHome();
		
		//notify all clients if they are alive and wait them to finish their running (by join)
		for(Client c : clients) {
			try {		 
				if(c.isAlive() == true) {
					synchronized (c) {
						c.notifyAll();
					}
					
					Thread.sleep(1000);
					c.join();
				}
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
		}
			 
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		//function that sets static variable isWorking to false (at clientService)
		ClientService.closeService();	
		
		//notify all bankers
		for (int i = 0; i < bankers.length; i++) {
			
			synchronized (bankers[i]) {
				bankers[i].notifyAll();
			
			}
		}
		
	}
	
	//function that gets an index and creates branch instance from the xml file 
	//the branch constructor also received client and bankers arrays by calling static functions "getClientsList" and "getBankersList"
	//and passing them the current branch node
	public static Branch getBranchInstance(int indexOfBank) throws SecurityException, IOException {
		Node bankNode =  XmlHandler.getElement("bank", BankManager.XML_FILE_NAME, indexOfBank);
		Element bankElement = (Element)bankNode;
		
		String name = bankElement.getAttribute("name");
		int id = Integer.parseInt(bankElement.getAttribute("id"));
		double commission = Double.parseDouble(bankElement.getAttribute("commission"));
		
		return new Branch(name, id, commission, Client.getClientsList(bankNode), Banker.getBankersList(bankNode));
	}
	
	
	//Getters & Setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public Log getHandler() {
		return handler;
	}

	public void setHandler(Log handler) {
		this.handler = handler;
	}

	public Banker[] getBankers() { 
		return bankers;
	}
	
	public void setBankers(Banker[] bankers) {
		this.bankers = bankers;
	}

	public Client[] getClients() {
		return clients;
	}

	public void setClients(Client[] clients) {
		this.clients = clients;
	}
		
}

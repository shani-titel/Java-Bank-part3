package bl.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import utils.BankActions;
import dal.DBWriter;
import listeners.BLEventsListener;
import bl.bank_services.Atm;
import bl.bank_services.Banker;
import bl.bank_services.ClientService;


public class BankManager {

	public static Logger logger = Logger.getLogger("logger");
	
	public static final int ATM = 0;
	public static final int BANKER = 1;
	public static final int CASH_DEPOSIT = 0;
	public static final int CASH_WITHDRAW = 1;
	public static final int GET_REPORT = 2;
	public static final int ADD_AUTHORIZATION = 3;
	public static final String XML_FILE_NAME = "bank.xml";

	public static Branch branch;
	public static Atm atms[];
	public static Banker bankers[];
	public static Client clients[];
	public static boolean isWorking = true;
	
	private DBWriter dbWriter = new DBWriter();
	private Vector<BLEventsListener> listeners = new Vector<>();
	

	public void main() 
	{
		//load the data from XML file
		initComponentes();	
		
		System.out.println("*****Welcome to Bank System*****\n");
		
		while(isWorking) {
			
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			 
			 //FOR DEBUGGING - print the clients state
			 /* for(int i = 0 ; i < clients.length; i++)
			 {
				 System.out.println((i+1) + "." + clients[i].getClientName() + "\n" + clients[i].getState());
			 }*/
		
			 
			 int select;
			 
			 //choose random client that is not in process
			 do {
				 select = NumberSelector.selectInteger(clients.length);
				 
			 }while(clients[select].isInProcess() == true);
			 
			 clients[select].setInProcess(true);
			 
			 System.out.println(clients[select].getClientName() + ", choose: \n1.Atm\n2.Banker");
			 
			 //random choose between ATM and banker
			 switch(NumberSelector.selectInteger(2)) {
			 	case ATM: {
			 		System.out.println("\nATM Selected \nWhich ATM?:\n");
			 		int index = 0;
			 		for (Atm atm : atms) 
			 			System.out.println(++index + "." + atm.getLocation());
					
			 		int res = NumberSelector.selectInteger(atms.length);
			 		System.out.println("\nATM" + (res + 1) + " Selected");
			 		registerClient(atms[res],clients[select]);
		 			
			 	}
			 	break;
			 	
			 	case BANKER: {
			 		
			 		bankers = branch.getBankers();
			 		System.out.println("\nBanker Selected \nWitch Banker?\n");
					int index = 0;
					for (Banker Banker : bankers) 
						System.out.println(++index + "." + Banker.getName());
						
					int res = NumberSelector.selectInteger(bankers.length);
					System.out.println("\nBanker" + (res + 1) + " Selected");
			 		registerClient(bankers[res], clients[select]);
			 		
			 	}
			 	break;
	
			 }
			 
		}
		
	}
	
	//function that gets service and client that randomly chosen, register the client to the service if the service is available,
	//if not - lock the thread and print that the service is busy.
	public void registerClient(final ClientService tempService, final Client currentClient)
	{
		 
		new Thread( new Runnable() {
			
              public void run() {
				if(tempService.lockService() == false)
					try {
						System.out.println("\n" + tempService.getLogFileName() + " is busy, "+ currentClient.getClientName() + " waiting\n");
						tempService.getClientSemaphore().acquire();
						System.out.println("\n" + tempService.getLogFileName() + " is released, " + currentClient.getClientName() + " notified\n");
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
				
				tempService.setCurrentClient(currentClient);
				synchronized (tempService) {
					//wake up Service
					tempService.notifyAll();	 
				}
				
				tempService.notifyCurrentClient();
				serveClient(tempService, currentClient);     
              }
          }).start();
	  
	  
		
	}
	
	public synchronized void serveClient(ClientService service, Client currentClient) {
		
		System.out.println("\nChoose Action:");
		System.out.println("1.Cash Deposit\n" +
							"2.Cash Withdraw\n" +
							"3.Get Report\n" +
							"4.Add Authorization");
		
		int select = NumberSelector.selectInteger(4);
		System.out.println("\nAction " + (select + 1) + " Selected\n");
		service.setAction(select);
	}

	public void initComponentes() {
		
		//gets branch instance from XML
		try {
			branch = Branch.getBranchInstance(0);
		} catch (SecurityException e) {
			e.getMessage();
			
		} catch (IOException e) {
			e.getMessage();
			
		}
		
		bankers = branch.getBankers();
		
		clients = branch.getClients();
		
		//gets ATMs list from XML
		atms = new Atm[XmlHandler.getNumberOfElements("atm", XML_FILE_NAME)];
		
		//initialize the Atms threads
		 for(int i = 0; i < atms.length; i++) {
			 try {
				atms[i]= Atm.getAtmInstance(i);
			} catch (SecurityException e) {
				e.getMessage();
			} catch (IOException e) {
				e.getMessage();
			}
			 
			 atms[i].setName(atms[i].getLogFileName());
			 atms[i].start();	 
		 }
		 
		 //initialize the clients threads
		 for(Client c : clients){	 
			 c.start();
			 c.setBankName(branch.getName());
		 }
 
	}

	//function that gets buffer reader from file and prints his content
	public static void printReport(BufferedReader reader, String rptName){
		
		String line = null;
		System.out.println("\n***********************************");
		System.out.println("      Report for: " + rptName);
		try {
			
			while ((line = reader.readLine()) != null) 
				System.out.println(line);
			System.out.println("***********************************\n");
			reader.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void closeBank()	{
			
		branch.close();
		
		//nofify all atms
		for(int i=0; i < atms.length; i++) {
			if(atms[i] != null) {
				synchronized (atms[i]) {
					atms[i].notifyAll();
				}
			}
		}
		
		System.out.println("\nbank is closing...");
		
		String bankName = branch.getName();
		
		//print bank report
		printReport(XmlHandler.readTxtfile(bankName), bankName);
		
	}

	public void addCustomerToSystem(String name) throws SecurityException, IOException {
		int id=1; //TEMPORARY
		Client temp = new Client(id, name, 0);
		List<Client> clientsList = new ArrayList<Client>(Arrays.asList(clients));
		clientsList.add(temp);
		//fireAddCustomerToSystem(temp);
		System.out.println(name);
		dbWriter.writeNewClient(temp);
	}

	public void addATMToSystem(String location) throws SecurityException, IOException {
		int id=1; //TEMPORARY
		Atm temp = new Atm(location, id);
		List<Atm> atmsList = new ArrayList<Atm>(Arrays.asList(atms));
		atmsList.add(temp);
		//fireAddATMToSystem(temp);
		// save to db
		dbWriter.writeNewAtm(temp);
		
	}

	public void addBankerToSystem(String name, double commission) throws SecurityException, IOException {
		int id=1; //TEMPORARY
		Banker temp = new Banker(id, name, commission);
		List<Banker> bankersList = new ArrayList<Banker>(Arrays.asList(bankers));
		bankersList.add(temp);
		//fireAddBankerToSystem(temp);
		//  save to db
		dbWriter.writeNewBanker(temp);
		
	}
	
	public void registerListener(BLEventsListener listener) {
		listeners.add(listener);
	}
	
	private void fireAddCustomerToSystem(Client client) {
		for (BLEventsListener l : listeners) {
			l.customerAddedToBLEvent(client.getName());
		}
	}
	private void fireAddATMToSystem(Atm atm) {
		for (BLEventsListener l : listeners) {
			l.customerAddedToBLEvent(atm.getName());
		}
	}
	private void fireAddBankerToSystem(Banker banker) {
		for (BLEventsListener l : listeners) {
			l.customerAddedToBLEvent(banker.getName());
		}
	}

	public void makeAction(BankActions action, int customerId, ClientService serviceGiver, double amount) {
		Client currClient = null;
		
		for(Client c : clients){
			if(c.getClientId() == customerId){
				currClient = c;
				if(action != BankActions.BankCharge && action != BankActions.BankCredit){
					serviceGiver.setCurrentClient(c);
				}
			}
		}		
		
		switch(action){
		case Deposit:
			serviceGiver.cashDeposit(amount);
			break;
		case Withdraw:
			serviceGiver.cashWithdraw(amount);
			break;
		case BankCharge:
			currClient.setBalance(currClient.getBalance()-amount);
			dbWriter.updateClientBalance(currClient);
			break;
		case BankCredit:
			currClient.setBalance(currClient.getBalance()+amount);
			dbWriter.updateClientBalance(currClient);
			break;
		
		}
		
	}

	public void makeAuthorizationAction(BankActions authorization,
			int customerId, ClientService serviceGiver, String organization) {
		for(Client c : clients){
			if(c.getClientId() == customerId){
				serviceGiver.setCurrentClient(c);
			}
		}	
		serviceGiver.addAuthorization(organization);
	}
	
	
	//getters & setters
	public String getClientName(int i) {
		return clients[i].getClientName();
	}

	public Client[] getClients() {
		return clients;
	}
	
	public static Atm[] getAtms() {
		return atms;
	}
	
	public static Banker[] getBankers() {
		return bankers;
	}

	public static void setBankers(Banker[] bankers) {
		BankManager.bankers = bankers;
	}

	public void setComponents(Client[] clientsArray, Atm[] atmsArray,
			Banker[] bankersArray) {
		this.clients = clientsArray;
		this.bankers = bankersArray;
		this.atms = atmsArray;
		
	}
	
}

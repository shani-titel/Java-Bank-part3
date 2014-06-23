package bl.bank_services;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import dal.DBWriter;

import bl.main.Client;
import bl.main.Log;
import bl.main.NumberSelector;


public class ClientService extends Thread implements ServiceActions {
	
	public static boolean isWorking = true;	
	protected DBWriter dbwriter = new DBWriter();
	protected Client currentClient = null;
	protected Log logHandler;
	protected Semaphore clientSemaphore;
	protected int id;
	protected int action;
	
	//Constructor
	public ClientService(int id) throws SecurityException, IOException  {
		
		setId(id);
		setAction(-1);
		setLogHandler(new Log(getLogFileName()));
		setClientSemaphore(new Semaphore(1));
		
	}

	public static void closeService() {
		isWorking = false;
	}
	
	public boolean lockService() {

		 return clientSemaphore.tryAcquire();
	}
	
	public void releaseService() {
		setAction(-1);
		clientSemaphore.release();
	}
	
	
	public void notifyCurrentClient() {
		
		if(currentClient.getState() == Thread.State.WAITING)
			synchronized (currentClient) {
				currentClient.notifyAll();
		}
			
	}

	@Override
	public synchronized void run() {
		
		while(isWorking){
			try {
				
				//wait until will be a client
				wait();		
			} 
			
			catch (InterruptedException e) {	
				e.printStackTrace();	
			}
			
			//set the service to the client service instance
			if(isWorking == true)
				currentClient.setService(this);
			
		}
		
		//release the service after closing
		releaseService();
	}

	public String getLogFileName()
	{
		if(this instanceof Atm)
			return "Atm" + id;
		
		return "Banker" + id;
	}
	
	
	@Override	
	public  void cashDeposit(double amount){
		//delay of 2 seconds
		//serveTime(2);
		currentClient.cashDeposit(amount);
		dbwriter.updateClientBalance(currentClient);
	}
	
	@Override
	public  void cashWithdraw(double amount){
		//serveTime(2);
		currentClient.cashWithdraw(amount);
		dbwriter.updateClientBalance(currentClient);
	}

	@Override
	public void getReport() {
		serveTime(2);
		currentClient.getReport();
	}
	
	@Override
	public void addAuthorization(String organiztion){
		//serveTime(2);
		
		currentClient.setIoSelection(NumberSelector.selectInteger(1));
		currentClient.addAuthorization(organiztion);
		dbwriter.addAuthorizationToClient(currentClient, organiztion);
	}
	
	public void serveTime(int seconds) {
		
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	//Getters & Setters
	public Semaphore getClientSemaphore() {
		return clientSemaphore;
	}

	public void setClientSemaphore(Semaphore clientSemaphore) {
		this.clientSemaphore = clientSemaphore;
	}
	
	public int getServiceId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Log getLogHandler() {
		return logHandler;
	}

	public void setLogHandler(Log logHandler) {
		this.logHandler = logHandler;
	}

	public void setCurrentClient(Client client)
	{
		this.currentClient = client;
	}
	

	
}

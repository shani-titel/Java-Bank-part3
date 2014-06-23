package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dal.DBReader;
import dal.DBWriter;
import bl.bank_services.Atm;
import bl.bank_services.Banker;
import bl.main.BankManager;
import bl.main.Client;
import bl.main.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestProgram extends Application {
	
	private BankManager model; 
	private List<Client> clients;
	private List<Atm> atms;
	private List<Banker> bankers;
	
	
	public static void main(String[] args) {

		launch(args);
		
	}

	@Override
	public void start(Stage arg0) throws Exception {
		clients = new ArrayList<Client>();
		atms = new ArrayList<Atm>();
		bankers = new ArrayList<Banker>();
		 
		model = new BankManager();
		
		ServiceAndCustomerWindow app = new ServiceAndCustomerWindow();
		app.setServerIp("localhost");
		app.setServerPort(7000);
		Controller controller1 = new Controller(model, app);
		
        try {
        	initComponents();
        	
        	Atm atmsArray[] = atms.toArray(new Atm[atms.size()]); //THE WAY convert list to array
        	Banker bankersArray[] = bankers.toArray(new Banker[bankers.size()]);
        	Client clientsArray[] = clients.toArray(new Client[clients.size()]);
        	
			model.setComponents(clientsArray, atmsArray, bankersArray);
        	
        	app.start(new Stage(),clientsArray, atmsArray, bankersArray,  model);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//read clients bankers and atms from db 
	private void initComponents(){
		DBReader reader = new DBReader();
		try {
			clients = reader.readAllClients();
			atms = reader.readAllAtms();
			bankers = reader.readAllBankers();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void fromXMLtoDB(){
		DBWriter dbWriter = new DBWriter();
		
		//insert clients to db
		Client[] clients = model.getClients();
		for(Client c : clients){
			dbWriter.writeNewClient(c);
		}
		
		//insert atms to db
		Atm[] atms = model.getAtms();
		for(Atm a : atms){
			dbWriter.writeNewAtm(a);
		}
		
		//insert bankers to db
		Banker[] bankers = model.getBankers();
		for(Banker b : bankers){
			dbWriter.writeNewBanker(b);
		}
	}

}

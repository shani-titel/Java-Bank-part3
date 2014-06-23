package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.Driver;

import bl.bank_services.*;
import bl.main.Client;

public class DBWriter {

	public static ArrayList<Client> clients;
	public static ArrayList<Atm> atms;
	public static ArrayList<Banker> bankers;
	
	private Connection connection = null;
	
	public DBWriter(){
		clients = new ArrayList<Client>();
		atms = new ArrayList<Atm>();
		bankers = new ArrayList<Banker>();
	}
	
	public void writeNewClient(Client client){
		if(client != null){
			clients.add(client);
			//write to db
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "INSERT INTO `bank_db`.`customers` (`name`,`balance`) VALUES ('"
						+ client.getClientName() + "','0')";
				//execute the query
				int numOfAffectedRows = statment.executeUpdate(s);
				//for debug:
				System.out.println("new client inserted to db. num of rows affected = " + numOfAffectedRows);
				
			} catch (InstantiationException  e) {e.printStackTrace();
			} catch (IllegalAccessException e)  {e.printStackTrace();
			} catch (ClassNotFoundException e)  {e.printStackTrace();
			} catch (SQLException e)            {e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public void writeNewAtm(Atm atm){
		if(atm != null){
			atms.add(atm);
			//write to db
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "INSERT INTO `bank_db`.`atms` (`location`) VALUES ('"
						+ atm.getLocation() + "')";
				//execute the query
				int numOfAffectedRows = statment.executeUpdate(s);
				//for debug:
				System.out.println("new ATM inserted to db. num of rows affected = =" + numOfAffectedRows);
				
			} catch (InstantiationException  e) {e.printStackTrace();
			} catch (IllegalAccessException e)  {e.printStackTrace();
			} catch (ClassNotFoundException e)  {e.printStackTrace();
			} catch (SQLException e)            {e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}

	}
	
	public void writeNewBanker(Banker banker){
		if(banker != null){
			bankers.add(banker);
			//write to db
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "INSERT INTO `bank_db`.`bankers` (`name`,`commission`) VALUES ('"
						+ banker.getBankerName() + "','"+ banker.getCommission() + "')";
				//execute the query
				int numOfAffectedRows = statment.executeUpdate(s);
				//for debug:
				System.out.println("new banker inserted to db. num of rows affected =" + numOfAffectedRows);
				
			} catch (InstantiationException  e) {e.printStackTrace();
			} catch (IllegalAccessException e)  {e.printStackTrace();
			} catch (ClassNotFoundException e)  {e.printStackTrace();
			} catch (SQLException e)            {e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void openConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch(Exception e){
			Driver driver = new com.mysql.jdbc.Driver();
			DriverManager.registerDriver(driver);
		}
		String dbUrl = "jdbc:mysql://localhost/bank_db";
		connection = DriverManager.getConnection(dbUrl, "root", "");
		
		//System.out.println("Database connection established");
	}
	
	public void updateClientBalance(Client client){
		if(client != null){
			//write to db
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "UPDATE `bank_db`.`customers` SET `balance` = "
						+ client.getBalance() + " WHERE name = '" + client.getClientName()+"'";
				//execute the query
				int numOfAffectedRows = statment.executeUpdate(s);
				//for debug:
				System.out.println("client balance updated to =" + client.getBalance());
				
			} catch (InstantiationException  e) {e.printStackTrace();
			} catch (IllegalAccessException e)  {e.printStackTrace();
			} catch (ClassNotFoundException e)  {e.printStackTrace();
			} catch (SQLException e)            {e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void addAuthorizationToClient(Client client, String organizition){
		if(client != null && organizition != ""){
			//write to db
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "INSERT INTO `bank_db`.`autorizations_per_customer` (`customer_id`,`auth_name`) VALUES ('"
						+ client.getClientId() + "','"+ organizition + "')";
				//execute the query
				int numOfAffectedRows = statment.executeUpdate(s);
				//for debug:
				System.out.println("new auth inserted to db");
				
			} catch (InstantiationException  e) {e.printStackTrace();
			} catch (IllegalAccessException e)  {e.printStackTrace();
			} catch (ClassNotFoundException e)  {e.printStackTrace();
			} catch (SQLException e)            {e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

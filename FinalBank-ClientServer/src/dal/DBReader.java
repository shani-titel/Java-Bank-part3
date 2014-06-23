package dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bl.bank_services.Atm;
import bl.bank_services.Banker;
import bl.main.Client;

import com.mysql.jdbc.Driver;

public class DBReader {
	
	private Connection connection = null;
	
	public Client findClientByName(String clientName) throws SecurityException, IOException{
		Client clientResult = new Client(0, "", 0);
		if(clientName != null){ //check
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "SELECT * FROM customers WHERE name ='" + clientName + "'";
				//execute the query
				ResultSet result = statment.executeQuery(s);
				result.next(); //IMPORTANT! in order to get the first row...
				clientResult = new Client(result.getInt("id"),result.getString("name") , result.getDouble("balance"), null, null);
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
		return clientResult;
	}
	
	public Atm findATMByLocation(String location) throws SecurityException, IOException{
		Atm atmResult = new Atm("", 0);
		if(location != null){ //check
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "SELECT * FROM atms WHERE Location ='" + location + "'";;
				//execute the query
				ResultSet result = statment.executeQuery(s);
				result.next();
				atmResult = new Atm(result.getString("Location"), result.getInt("id"));
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
		return atmResult;
	}
	
	public Banker findBankerByName(String bankerName) throws SecurityException, IOException{
		Banker bankerResult = new Banker(0, "", 0);
		if(bankerName != null){ //check
			try {
				openConnection();

				Statement statment = connection.createStatement();
				//create sql query string
				String s = "SELECT * FROM bankers WHERE name ='" + bankerName +"'";
				//execute the query
				ResultSet result = statment.executeQuery(s);
				result.next();
				bankerResult = new Banker(result.getInt("id"), result.getString("name"), result.getDouble("commission"));
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
		return bankerResult;
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
	
	public List<Client> readAllClients()throws SecurityException, IOException{
		List<Client> allClients = new ArrayList<Client>();
		try {
			openConnection();
			Statement statment = connection.createStatement();
			//create sql query string
			String s = "SELECT * FROM customers";
			//execute the query
			ResultSet result = statment.executeQuery(s);
			while(result.next()){
				Client c = new Client(result.getInt("id"),result.getString("name"), 
						result.getDouble("balance"));
				allClients.add(c);
			}
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
		return allClients;
	}
	
	public List<Atm> readAllAtms()throws SecurityException, IOException{
		List<Atm> allAtms = new ArrayList<Atm>();
		try {
			openConnection();
			Statement statment = connection.createStatement();
			//create sql query string
			String s = "SELECT * FROM atms";
			//execute the query
			ResultSet result = statment.executeQuery(s);
			while(result.next()){
				Atm a = new Atm(result.getString("Location"), result.getInt("id"));
				allAtms.add(a);
			}
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
		return allAtms;
	}
	
	public List<Banker> readAllBankers()throws SecurityException, IOException{
		List<Banker> allBankers = new ArrayList<Banker>();
		try {
			openConnection();
			Statement statment = connection.createStatement();
			//create sql query string
			String s = "SELECT * FROM bankers";
			//execute the query
			ResultSet result = statment.executeQuery(s);
			while(result.next()){
				String name = result.getString("name");
				Banker b = new Banker(result.getInt("id"),name , result.getDouble("commission"));
				allBankers.add(b);
			}
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
		return allBankers;
	}
}

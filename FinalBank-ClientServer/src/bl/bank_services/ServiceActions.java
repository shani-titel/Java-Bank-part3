package bl.bank_services;

public interface ServiceActions {

	public void cashDeposit(double amount);
	public void cashWithdraw(double amount);
	public void getReport();
	public void addAuthorization(String organiztion);
}

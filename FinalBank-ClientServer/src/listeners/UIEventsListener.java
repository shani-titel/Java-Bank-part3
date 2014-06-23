package listeners;

import bl.bank_services.ClientService;

public interface UIEventsListener {
	void customerAddedToUIEvent(String name);
	void ATMAddedToUIEvent(String location);
	void bankerAddedToUIEvent(String name, double commission);
	void depositInUIEvent(int customerId, double amount,ClientService serviceGiver);
	void withdrawInUIEvent(int customerId, double amount,ClientService serviceGiver);
	void authorizationInUIEvent(int customerId, String organization,ClientService serviceGiver);
	void bankChargeInUIEvent(int customerId, double amount);
	void bankCreditInUIEvent(int customerId, double amount);
}

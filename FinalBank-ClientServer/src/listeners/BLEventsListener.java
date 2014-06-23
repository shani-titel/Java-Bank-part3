package listeners;

public interface BLEventsListener {
	void customerAddedToBLEvent(String name);
	void ATMAddedToBLEvent(String location);
	void bankerAddedToBLEvent(String name, double commission);
	
}

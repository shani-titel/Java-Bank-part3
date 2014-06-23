package bl.main;

public class MainProgram {
	
	//10 seconds of running
	public static final int PROGRAM_RUNTIME = 10;
	
	
	public static void main(String[] args)  {
		
		final BankManager manager = new BankManager(); 
		
		new Thread( new Runnable(){
            public void run(){
			
				try {
					Thread.sleep(PROGRAM_RUNTIME * 1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				manager.isWorking = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				manager.closeBank();
			}
			
		}).start();
		
		manager.main();
		
			 
	}
	
}

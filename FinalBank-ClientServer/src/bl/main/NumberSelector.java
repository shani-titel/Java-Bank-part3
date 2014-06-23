package bl.main;

import java.util.Random;


public class NumberSelector {

	public static Random rand = new Random();
	
	public synchronized static int selectInteger(int limit)
	{
		int i = rand.nextInt();
		return Math.abs(i % limit);
	}
	
}

package utils;

public class General {	
	
	public double StringToDouble(String s){
		double d= 0;
	    try {
	       d = Double.valueOf(s.trim()).doubleValue();
	    } catch (NumberFormatException nfe) {
	       System.out.println("NumberFormatException: " + nfe.getMessage());
	    }
	    return d;
	}
	

}

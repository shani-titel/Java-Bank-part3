package bl.main;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class myFormattor extends Formatter {

	@Override
	public String format(LogRecord arg0) {
		StringBuffer buf = new StringBuffer();
		
		buf.append(new java.util.Date().toLocaleString());
		buf.append(System.getProperty("line.separator").toString());
		buf.append(formatMessage(arg0));
		buf.append(System.getProperty("line.separator").toString());
		buf.append(System.getProperty("line.separator").toString());
		
		return buf.toString();
	}

}

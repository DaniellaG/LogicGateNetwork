import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public final class Logger {
	//static String m_path = "/home/daniella/worksapce/LogicGatesNetwork/log";
	static PrintWriter m_writer;
	private Logger()
	{
		return;
	}
	
	public static void init()
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    
	    try{
			m_writer = new PrintWriter("log" +  strDate, "UTF-8");
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Cannot create log file:" + e.toString());
	    }
	}
	
	public static void write (String msg)
	{
		//m_writer.println(msg);
		return;
	}
	
	public static void close()
	{
		m_writer.close();
	}
	
}

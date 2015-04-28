package utilities;

public class UniqueID
{
	static long current= System.currentTimeMillis();
	 public static synchronized long get()
	  {
	    return current++;
	  }
	
	
}

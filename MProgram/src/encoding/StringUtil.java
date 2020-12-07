package encoding;

public class StringUtil {
	
	private StringUtil() {
		new AssertionError();
	}
	
	public static String fixNull(Object data) {
		if(data instanceof String) {			
			return (String)data;
		}
		
		return "";
	}

}

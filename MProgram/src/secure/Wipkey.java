package secure;

public class Wipkey {
	
	private static String wipkey;
	
	private Wipkey(String wipkey) {
		Wipkey.wipkey = wipkey;
	}
	
	static Wipkey of(String wipkey) {
		return new Wipkey(wipkey);
	}

	public static String getKey() {
		return wipkey;
	}

	
}

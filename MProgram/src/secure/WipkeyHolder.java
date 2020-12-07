package secure;

import java.util.function.Supplier;

public class WipkeyHolder {
	
	private String key;
	private Supplier<Wipkey> heavy = () -> createAndCache();
	
	public WipkeyHolder(String key) {
		this.key = key;
	}
	
	public Wipkey getWipkey() { 
		return heavy.get();
	}
	
	private synchronized Wipkey createAndCache() {
		
		class WipkeyFactory implements Supplier<Wipkey> {

			private final Wipkey INSTANCE = Wipkey.of(key);
			
			public Wipkey get() {
				return INSTANCE;
			}			
		}
		
		if(!WipkeyFactory.class.isInstance(heavy)) {
			heavy = new WipkeyFactory();
		}
		
		return heavy.get();
	}
}

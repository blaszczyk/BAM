package bam.core;

import java.util.Iterator;

public interface BAMListable {
	
	public abstract Object getValue( String key );
	public abstract void setValue( String key, Object value);
	public abstract Class<?> getClass( String key );
	public abstract boolean containsKey( String key);
	public abstract Iterator<String> getKeyIterator();
	public abstract boolean isList( String key );

}
package bam.core;

import java.util.*;

public abstract class BAMAbstractListable implements BAMListable {


	protected Map<String,Object> valueMap;	
	
	public BAMAbstractListable() {
		createClassMap();
		valueMap = new HashMap<>();
	}

	protected abstract Map<String,Class<?>> getClassMap();
	protected abstract void createClassMap();
	
	@Override
	public Object getValue(String key) {
		return valueMap.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		valueMap.put(key, value);
	}
	
	@Override
	public Class<?> getClass(String key) {
		return getClassMap().get(key);
	}

	@Override
	public boolean containsKey(String key) {
		return getClassMap().containsKey(key);
	}

	@Override
	public Iterator<String> getKeyIterator() {
		return getClassMap().keySet().iterator();
	}
}

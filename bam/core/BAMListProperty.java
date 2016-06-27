package bam.core;

import java.util.ArrayList;
import java.util.List;

public class BAMListProperty<T extends BAMProperty<?>> implements BAMProperty<List<T>> {

	private List<T> list;
	
	public BAMListProperty() {
		list = new ArrayList<>();
	}

	@Override
	public List<T> getValue() {
		return list;
	}

	@Override
	public void setValue(List<T> value) {
		this.list = value;
	}	

}

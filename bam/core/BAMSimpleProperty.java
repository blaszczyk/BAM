package bam.core;

import java.math.BigDecimal;
import java.util.Date;

import bam.tools.BAMUtils;

public class BAMSimpleProperty<T> implements BAMProperty<T> {

	private T value;
	
	public BAMSimpleProperty() {
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		if( value instanceof Date )
			return BAMUtils.DateDDMMYYYY( (Date) value );
		return value.toString();
	}
	
	@SuppressWarnings("unchecked")
	public boolean fromString( String in ) {
		if(value instanceof Date)
		{
			if( ! BAMUtils.isDateFormatDDMMYYYY(in) )
				return false;
			value = (T) BAMUtils.toDateFormatDDMMYYYY(in);
			return true;
		}
		if(value instanceof BigDecimal)
		{
			if( ! BAMUtils.isBigDec(in) )
				return false;
			value = (T) BAMUtils.toBigDec(in);
			return true;
		}
		if(value instanceof String)
		{
			value = (T) in;
			return true;
		}
		return false;
	}

}

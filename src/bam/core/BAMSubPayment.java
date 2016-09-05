package bam.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BAMSubPayment extends BAMAbstractListable {

	public static final String DATE = "DATE";
	public static final String AMOUNT = "AMOUNT";
	public static final String TRANSACTION_ID = "TRANSACTION_ID";
	
	private static Map<String,Class<?>> subpaymentClassMap;
	
	/*
	 * Constructors
	 */
	
	public BAMSubPayment() {}
	
	public BAMSubPayment(BigDecimal amount, Date date, String transaction_id) {
		setValue( AMOUNT, amount );
		setValue( DATE, date );
		setValue( TRANSACTION_ID, transaction_id );
	}

	/*
	 * Additional Getters
	 */
	public BigDecimal getAmount() {
		return (BigDecimal) getValue( AMOUNT );
	}
	
	public Date getDate() {
		return (Date) getValue( DATE );
	}
	
	public String getTransaction_id() {
		return (String) getValue( TRANSACTION_ID );
	}
	
	/*
	 * Listable Methods
	 */

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return subpaymentClassMap;
	}

	@Override
	protected void createClassMap() {
		if( subpaymentClassMap == null )
		{
			subpaymentClassMap = new HashMap<>();
			subpaymentClassMap.put(AMOUNT, BigDecimal.class );
			subpaymentClassMap.put(DATE, Date.class );
			subpaymentClassMap.put(TRANSACTION_ID, String.class );
		}
		
	}

	@Override
	public boolean isList(String key) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
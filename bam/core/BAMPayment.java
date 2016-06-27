package bam.core;

import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("unchecked")
public class BAMPayment extends BAMAbstractListable{
	
	public static final String NAME = "NAME";
	public static final String AMOUNT = "AMOUNT";
	public static final String PURPOSE = "PURPOSE";
	public static final String BILL_NR = "BILL_NR";
	public static final String DATE = "DATE";
	public static final String  TRANSACTIONS = "TRANSACTIONS";
	
	private BAMSubAccount parent;
	
    private static Map<String,Class<?>> paymentClassMap;

    /*
     * Constructor
     */

    public BAMPayment() { }
	
	public BAMPayment( BAMSubAccount parent, String name, BigDecimal amount, String purpose, String billNr, Date date, BAMTransaction transaction )
	{
		this.parent = parent;
		setValue( NAME, name);
		setValue( AMOUNT, amount );
		setValue( PURPOSE, purpose );
		setValue( BILL_NR, billNr );
		setValue( DATE, date );
		setValue( TRANSACTIONS, new ArrayList<String>() );
		addTransaction(transaction);
	}
	
    /*
     * Getters Setters
     */

	public String getName() {
		return (String) getValue( NAME );
	}

	public BigDecimal getAmount() {
		return (BigDecimal) getValue( AMOUNT );
	}

	public String getPurpose() {
		return (String) getValue( PURPOSE );
	}

	public String getBillNr() {
		return (String) getValue( BILL_NR );
	}

	public Date getDate() {
		return (Date) getValue( DATE );
	}

	public BAMSubAccount getParent() {
		return parent;
	}

	void setParent(BAMSubAccount parent) {
		this.parent = parent;
	}

	public List<String> getTransactions() {
		return (List<String>)getValue( TRANSACTIONS );
	}
		
	public void addTransaction( BAMTransaction t ) {
		getTransactions().add( t.getTransaction_id() );
	}

	public void clearTransactions() {
		setValue( TRANSACTIONS, new ArrayList<String>() );
	}

	public int getTransactionCount() {
		return getTransactions().size();
	}

	/*
	 * Specific Methods
	 */
	
	public boolean containsTransaction( BAMTransaction t)
	{
		return containsTransaction( t.getTransaction_id() );
	}
	
	public boolean containsTransaction( String transaction_id )
	{
		for( String tid : getTransactions() )
			if( tid.equals( transaction_id ) )
				return true;
		return false;
	}

	/*
	 * Listable Methods
	 */
	
	@Override
	protected void createClassMap()
	{
		if(paymentClassMap == null)
		{
			paymentClassMap = new HashMap<>();
			paymentClassMap.put("NAME", String.class);
			paymentClassMap.put("AMOUNT", BigDecimal.class);
			paymentClassMap.put("DATE", Date.class);
			paymentClassMap.put("PURPOSE", String.class);
			paymentClassMap.put("BILL_NR", String.class);
			paymentClassMap.put("TRANSACTIONS", String.class);
		}
	}

	@Override
	public boolean isList(String key) {
		return key.equals("TRANSACTIONS");
	}

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return paymentClassMap;
	}

}

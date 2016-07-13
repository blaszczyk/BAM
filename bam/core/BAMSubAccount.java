package bam.core;

import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("unchecked")
public class BAMSubAccount extends BAMModifyableListable implements BAMListable{

	public static final String NAME = "NAME";
	public static final String BALANCE = "BALANCE";
	public static final String PAYMENTS = "PAYMENTS";
	public static final String MULTIPAYMENTS = "MULTIPAYMENTS";
	
	private static Map<String,Class<?>> subAccountClassMap; 
	
	/*
	 * Constructor
	 */
	
	public BAMSubAccount() {}
	/*
	 * Getters Setters
	 */
	
	public BigDecimal getBalance() {
		return (BigDecimal) getValue(BALANCE);
	}


	public void setBalance(BigDecimal balance) {
		setValue( BALANCE, balance);
		fireModifiedEvents();
	}


	public List<BAMPayment> getPayments() {
		return (List<BAMPayment>) getValue(PAYMENTS);
	}
	
	public BAMPayment getPayment( int index ) {
		return getPayments().get(index);
	}
	
	public int getPaymentCount(){
		return getPayments().size();
	}

	public List<BAMMultiPayment> getMultiPayments() {
		return (List<BAMMultiPayment>) getValue(MULTIPAYMENTS);
	}
	
	public BAMMultiPayment getMultiPayment( int index ){
		return getMultiPayments().get(index);
	}

	public int getMultiPaymentCount(){
		return getMultiPayments().size();
	}
	
	public void addMultiPayment( BAMMultiPayment multipayment )
	{
		getMultiPayments().add(multipayment);
	}

	public String toString()
	{
		return (String) getValue(NAME);
	}
	
	public void addPayment( BAMPayment payment )
	{
		getPayments().add( payment );
	}

	/*
	 * Specific Methods
	 */
	
	public boolean containsTransaction( BAMTransaction t )
	{
		for( BAMPayment p : getPayments() )
			if( p.containsTransaction( t ) )
				return true;
		for( BAMMultiPayment mp : getMultiPayments() )
			if( mp.containsTransaction( t ) )
				return true;
		return false;
	}


	public void addBalance(BigDecimal augend) {
		setBalance( getBalance().add(augend) );
	}


	public void removePayment(BAMPayment payment) {
		int i = getPayments().indexOf( payment );
		if(i < 0)
			return;
		getPayments().remove( i );
	}
	
	public void removeMultiPayment(BAMMultiPayment multipayment) {
		int i = getMultiPayments().indexOf( multipayment );
		if(i < 0)
			return;
		getMultiPayments().remove( i );
	}

	void setParents()
	{
		for( BAMPayment p : getPayments())
			p.setParent(this);
		for( BAMMultiPayment m : getMultiPayments())
			m.setParent(this);
	}
	
	/*
	 * Listable Methods
	 */
	
	@Override
	public boolean isList(String key) {
		return key.equals(PAYMENTS) || key.equals(MULTIPAYMENTS);
	}

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return subAccountClassMap;
	}

	@Override
	protected void createClassMap() {
		if( subAccountClassMap == null )
		{
			subAccountClassMap = new HashMap<>();
			subAccountClassMap.put("NAME", String.class);
			subAccountClassMap.put("BALANCE", BigDecimal.class);
			subAccountClassMap.put("PAYMENTS", BAMPayment.class);
			subAccountClassMap.put("MULTIPAYMENTS", BAMMultiPayment.class);
		}
	}
	
}
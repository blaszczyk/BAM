package bam.core;

import java.math.BigDecimal;
import java.util.*;


@SuppressWarnings("unchecked")
public class BAMMultiPayment extends BAMAbstractListable implements BAMGenericPayment, Iterable<BAMSubPayment> {
	
	public static final String NAME = "NAME";
	public static final String PURPOSE = "PURPOSE";
	public static final String PAYMENTS = "PAYMENTS";
	public static final String SEARCH_NAME = "SEARCH_NAME";
	public static final String SEARCH_PURPOSE = "SEARCH_PURPOSE";
	public static final String LAST_DATE = "LAST_DATE";
	public static final String LAST_AMOUNT = "LAST_AMOUNT";
	public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
	public static final String NR_TRANSACTIONS = "NR_TRANSACTIONS";
	
	private BAMSubAccount parent;
	
	private static Map<String,Class<?>> multipaymentClassMap;

	/*
	 * Contstructor
	 */
	
	public BAMMultiPayment(){ }
	
	public BAMMultiPayment( BAMSubAccount sa)
	{
		this.parent = sa;
	}
	
	public BAMMultiPayment( BAMSubAccount parent, String name, String purpose, String searchName, String searchPurpose) {
		this.parent = parent;
		setValue( NAME, name);
		setValue( PURPOSE, purpose);
		setValue( SEARCH_NAME, searchName);
		setValue( SEARCH_PURPOSE, searchPurpose);
		setValue( PAYMENTS, new ArrayList<BAMSubPayment>());
		computeInternalValues();
	}

	/*
	 * Getters and Setters
	 */
	public String getName() {
		return (String) getValue( NAME );
	}


	public String getPurpose() {
		return (String) getValue( PURPOSE );
	}
	public BAMSubAccount getParent() {
		return parent;
	}

	private List<BAMSubPayment> getPayments() {
		return (List<BAMSubPayment>) getValue( PAYMENTS );
	}

	public BAMSubPayment getPayment( int index) {
		return getPayments().get(index);
	}
	
	public int getPaymentCount(){
		return getPayments().size();
	}
	
	private String getSearchName() {
		return (String) getValue(SEARCH_NAME);
	}

	private String getSearchPurpose() {
		return (String) getValue(SEARCH_PURPOSE);
	}

	void setParent(BAMSubAccount parent) {
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		return getName() + " - " + getPurpose();
	}

	/*
	 * Specific Methods
	 */

	private BAMSubPayment getLastPayment()
	{
		if(getPayments().isEmpty())
			return new BAMSubPayment(BigDecimal.ZERO, new Date( 0 ), "");
		return getPayments().get(0);
	}
	
	public void computeInternalValues()
	{
		BigDecimal sum = BigDecimal.ZERO;
		for( BAMSubPayment p : getPayments() )
			sum = sum.add(p.getAmount());
		setValue( NR_TRANSACTIONS, getPaymentCount()  );
		setValue( TOTAL_AMOUNT, sum );
		setValue( LAST_DATE, getLastPayment().getDate() );
		setValue( LAST_AMOUNT, getLastPayment().getAmount() );
	}
	
	public void addTransaction( BAMTransaction t )
	{
		BAMSubPayment p = new BAMSubPayment( t.getAmount(), t.getDate(), t.getTransaction_id() );
		getPayments().add(p);
		getPayments().sort( (p1,p2)->p2.getDate().compareTo(p1.getDate()) );
		computeInternalValues();
	}

	public void removeTransaction(BAMSubPayment payment) {
		int i = getPayments().indexOf(payment);
		if( i < 0)
			return;
		getPayments().remove( i );
		computeInternalValues();
	}
	
	public void setTransactionAmount( BAMSubPayment payment, BigDecimal amount )
	{
		if( !getPayments().contains(payment) )
			return;
		payment.setValue( BAMSubPayment.AMOUNT, amount);
		computeInternalValues();
	}
	
	public boolean containsTransaction( BAMTransaction t)
	{
		return containsTransaction( t.getTransaction_id() );
	}
	
	public boolean containsTransaction( String transaction_id )
	{
		for( BAMSubPayment p : getPayments() )
			if( p.getTransaction_id().compareTo(transaction_id) == 0 )
				return true;
		return false;
	}

	public boolean suggestForTransaction( BAMTransaction t)
	{
		return t.getName().toLowerCase().contains( getSearchName().toLowerCase() ) &&
				t.getPurpose().toLowerCase().contains( getSearchPurpose().toLowerCase() );
	}

	/*
	 * Iterable Method
	 */
	
	@Override
	public Iterator<BAMSubPayment> iterator()
	{
		return getPayments().iterator();
	}
	/*
	 * Listable Methods
	 */
	
	@Override
	protected void createClassMap()
	{
		if(multipaymentClassMap == null)
		{
			multipaymentClassMap = new HashMap<>();
			multipaymentClassMap.put(NAME, String.class);
			multipaymentClassMap.put(LAST_AMOUNT, BigDecimal.class);
			multipaymentClassMap.put(LAST_DATE, Date.class);
			multipaymentClassMap.put(PURPOSE, String.class);
			multipaymentClassMap.put(NR_TRANSACTIONS, String.class);
			multipaymentClassMap.put(TOTAL_AMOUNT, BigDecimal.class);
			multipaymentClassMap.put(SEARCH_NAME, String.class);
			multipaymentClassMap.put(SEARCH_PURPOSE, String.class);
			multipaymentClassMap.put(PAYMENTS, BAMSubPayment.class);
		}
	}

	@Override
	public Iterator<String> getKeyIterator()
	{
		String[] keys = {NAME,PURPOSE,SEARCH_NAME,SEARCH_PURPOSE,PAYMENTS};
		return Arrays.asList(keys).iterator();
	}
	
	@Override
	public boolean isList(String key) {
		return key.equals(PAYMENTS);
	}

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return multipaymentClassMap;
	}


}
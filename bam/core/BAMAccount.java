package bam.core;

import java.math.BigDecimal;
import java.util.*;


@SuppressWarnings("unchecked")
public class BAMAccount extends BAMModifyableListable implements BAMListable{

	public static final String NAME = "NAME";
	public static final String IBAN = "IBAN";
	public static final String BALANCE = "BALANCE";
	public static final String SUBACCOUNTS = "SUBACCOUNTS";
	public static final String TRANSACTIONS = "TRANSACTIONS";
	
	private static Map<String,Class<?>> accountClassMap;

	
	/*
	 * Constructor
	 */
	public BAMAccount(){ }

	
	/*
	 * Additional Getters and Setters
	 */
	
	@Override
	public String toString()
	{
		return (String) getValue(NAME);
	}
	

	public BigDecimal getBalance()
	{
		return (BigDecimal) getValue(BALANCE);
	}

	public void setBalance( BigDecimal balance)
	{
		setValue(BALANCE, balance);
		fireModifiedEvents();
	}

	private List<BAMSubAccount> getSubAccountsList() {
		return (List<BAMSubAccount>) getValue(SUBACCOUNTS);
	}
	
	public List<BAMSubAccount> getSubAccounts() {
		return Collections.unmodifiableList( getSubAccountsList() );
	}


	private List<BAMTransaction> getTransactionsList() {
		return (List<BAMTransaction>) getValue(TRANSACTIONS);
	}
	public List<BAMTransaction> getTransactions() {
		return Collections.unmodifiableList( getTransactionsList() );
	}

	public BAMTransaction getTransaction( int index) {
		return getTransactionsList().get(index);
	}

	public BAMSubAccount getSubAccount(int index) {
		return getSubAccountsList().get(index);
	}
	
	public int getSubAccountCount( ) {
		return getSubAccountsList().size();
	}

	public String getIban() {
		return (String) getValue(IBAN);
	}

	public void setIban(String iban) {
		setValue(IBAN, iban);
		fireModifiedEvents();
	}

	/*
	 * Specific Methods
	 */
	
	public boolean containsTransaction(BAMTransaction transaction)
	{
		for( BAMTransaction t : getTransactionsList() )
			if(  t.equalsById(transaction) )
				return true;
		return false;
	}

	public boolean containsTransaction ( String transaction_id )
	{
		for( BAMTransaction t : getTransactionsList() )
			if(  t.equalsId(transaction_id) )
				return true;
		return false;
		
	}
	public BAMTransaction getTransactionById( String transaction_id )
	{
		for( BAMTransaction t : getTransactionsList() )
			if(  t.equalsId(transaction_id) )
				return t;
		return null;
	}

	public void addTransaction(BAMTransaction t) {
		getTransactionsList().add(t);
		setBalance( getBalance().add( t.getAmount() ) );
	}
	
	public void sortTransactions( Comparator<BAMTransaction> c )
	{
		getTransactionsList().sort(c);
		fireModifiedEvents();
	}

	public BigDecimal getMisBalance(){
		BigDecimal sum = getBalance();
		for( BAMSubAccount sa : getSubAccountsList() )
			sum = sum.subtract( sa.getBalance() );
		return sum;
	}
	
	void setParents()
	{
		for( BAMSubAccount sa : getSubAccountsList())
			sa.setParents();
	}

	/*
	 * Listable Methods
	 */

	@Override
	public boolean isList(String key) {
		return key.equals(SUBACCOUNTS) || key.equals(TRANSACTIONS);
	}


	@Override
	protected Map<String, Class<?>> getClassMap() {
		return accountClassMap;
	}


	@Override
	protected void createClassMap()
	{
		if(accountClassMap == null)
		{
			accountClassMap = new HashMap<>();
			accountClassMap.put(NAME, String.class);
			accountClassMap.put(BALANCE, BigDecimal.class);
			accountClassMap.put(IBAN, String.class);
			accountClassMap.put(SUBACCOUNTS, BAMSubAccount.class);
			accountClassMap.put(TRANSACTIONS, BAMTransaction.class);
		}
	}
}
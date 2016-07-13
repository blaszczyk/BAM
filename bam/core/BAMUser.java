package bam.core;

import java.util.*;

@SuppressWarnings("unchecked")
public class BAMUser extends BAMModifyableListable implements BAMListable{

	public static final String NAME = "NAME";
	public static final String FIGO_USER = "FIGO_USER";
	public static final String FIGO_PW = "FIGO_PW";
	public static final String BANK_PIN = "BANK_PIN";
	public static final String ACCOUNTS = "ACCOUNTS";
	
	private static Map<String,Class<?>> userClassMap;
		
	private boolean modified;

	/*
	 * Constructor
	 */
	
	public BAMUser( String name)
	{
		setValue(NAME,name);
		modified = false;
	}

	/*
	 * Getters Setters
	 */
	
	public List<BAMAccount> getAccounts()
	{
		return (List<BAMAccount>) getValue(ACCOUNTS);
	}
	
	public BAMAccount getAccount( int index )
	{
		return getAccounts().get(index);
	}
	
	public int getAccountCount()
	{
		return getAccounts().size();
	}
	
	public String getFigoAccount() {
		return (String) getValue(FIGO_USER);
	}

	public String getFigoPW() {
		return (String) getValue(FIGO_PW);
	}

	public String getBankPin() {
		return (String) getValue(BANK_PIN);
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		fireModifiedEvents();
	}

	@Override 
	public String toString(){
		return (String) getValue(NAME);
	}

	/*
	 * Specific Methods
	 */
	
	public boolean containsTransaction( String transaction_id)
	{
		for( BAMAccount a : getAccounts())
			if(a.containsTransaction(transaction_id) )
				return true;
		return false;
	}
	
	public BAMTransaction getTransactionById( String transaction_id)
	{
		for( BAMAccount a : getAccounts())
			if(a.containsTransaction(transaction_id) )
				return a.getTransactionById(transaction_id);
		return null;
	}
	
	public boolean isTransactionStored( BAMTransaction t)
	{
		for( BAMAccount a : getAccounts() )
			for( BAMSubAccount sa : a.getSubAccounts() )
				if( sa.containsTransaction(t) )
					return true;
		return false;
	}

	public void setParents()
	{
		for( BAMAccount a : getAccounts())
			a.setParents();
	}
	/*
	 * Listable Methods
	 */

	@Override
	public boolean isList(String key) {
		return key.equals(ACCOUNTS);
	}

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return userClassMap;
	}

	@Override
	protected void createClassMap() {
		if( userClassMap == null )
		{
			userClassMap = new HashMap<>();
			userClassMap.put(NAME, String.class);
			userClassMap.put(ACCOUNTS, BAMAccount.class);
			userClassMap.put(FIGO_USER, String.class);
			userClassMap.put(FIGO_PW, String.class);
			userClassMap.put(BANK_PIN, String.class);
		}
	}
	
}
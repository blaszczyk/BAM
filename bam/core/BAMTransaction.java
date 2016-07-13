package bam.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.figo.models.Transaction;

public class BAMTransaction extends BAMAbstractListable implements BAMGenericPayment {


	public static final String NAME = "NAME";
	public static final String TRANSACTION_ID = "TRANSACTION_ID";
	public static final String ACC_NR = "ACC_NR";
	public static final String BANK_NAME = "BANK_NAME";
	public static final String BANK_CODE = "BANK_CODE";
	public static final String AMOUNT = "AMOUNT";
	public static final String CURRENCY = "CURRENCY";
	public static final String DATE = "DATE";
	public static final String PURPOSE = "PURPOSE";
	public static final String TYPE = "TYPE";
	
	
	private static Map<String,Class<?>> transactionClassMap;
    
	public BAMTransaction() {}

	public BAMTransaction ( Transaction transaction)
	{
		this();
		setValue( TRANSACTION_ID, transaction.getTransactionId() );
		setValue( NAME, transaction.getName() );
		setValue( ACC_NR, defaultIfNull( transaction.getAccountNumber(), "n/a" ) );
		setValue( BANK_CODE, defaultIfNull( transaction.getBankCode(), "n/a" ) );
		setValue( BANK_NAME, defaultIfNull( transaction.getBankName(), "n/a" ) );
		setValue( AMOUNT, transaction.getAmount() );
		setValue( CURRENCY, transaction.getCurrency() );
		setValue( DATE, transaction.getValueDate() );
		setValue( PURPOSE, convertPurpose ( transaction.getPurposeText() ) );
		setValue( TYPE, convertType( transaction.getPurposeText()) );
	}

	
	/*
	 * Additional Getters
	 */
	
	public String getTransaction_id() {
		return (String) getValue( TRANSACTION_ID );
	}

	public String getName() {
		return (String) getValue( NAME );
	}

	public String getAccount_number() {
		return (String) getValue( ACC_NR );
	}

	public String getBank_code() {
		return (String) getValue( BANK_CODE );
	}

	public String getBank_name() {
		return (String) getValue( BANK_NAME );
	}

	public BigDecimal getAmount() {
		return (BigDecimal) getValue( AMOUNT );
	}

	public String getCurrency() {
		return (String) getValue( CURRENCY );
	}

	public Date getDate() {
		return (Date) getValue( DATE );
	}

	public String getPurpose() {
		return (String) getValue( PURPOSE );
	}

	public String getType() {
		return (String) getValue( TYPE );
	}

	
	/*
	 * Specific Methods
	 */

	public boolean equalsById( BAMTransaction that )
	{
		return getTransaction_id().equals( that.getTransaction_id() );
	}
		
	public boolean equalsId( String transaction_id )
	{
		return getTransaction_id().equals(transaction_id);
	}

	public void identifyTransfer( BAMUser user )
	{
		for(BAMAccount a : user.getAccounts() )
			if( a.getIban().equals( getAccount_number() ) )
				setValue(NAME, a.toString());
	}

	private static String convertPurpose( String purpose )
	{
		return purpose.contains( "End-To-End") ? purpose.substring( purpose.indexOf(' ') + 1 , purpose.indexOf("End-To-End") ) : purpose;
	}

	private static String convertType( String purpose )
	{
		return purpose.substring(0, purpose.indexOf(' '));
	}
	
	private static String defaultIfNull( String in, String def )
	{
		if( in == null || in.equals("null") )
			return def;
		return in;
	}

	/*
	 * Listable Methods
	 */

	@Override
	public boolean isList(String key) {
		return false;
	}
	
	@Override
	protected Map<String, Class<?>> getClassMap() {
		return transactionClassMap;
	}

	@Override
	protected void createClassMap()
	{
		if(transactionClassMap == null)
		{
			transactionClassMap = new HashMap<>();
			transactionClassMap.put(TRANSACTION_ID, String.class);
			transactionClassMap.put(NAME, String.class);
			transactionClassMap.put(ACC_NR, String.class);
			transactionClassMap.put(BANK_CODE, String.class);
			transactionClassMap.put(BANK_NAME, String.class);
			transactionClassMap.put(AMOUNT, BigDecimal.class);
			transactionClassMap.put(CURRENCY, String.class);
			transactionClassMap.put(DATE, Date.class);
			transactionClassMap.put(PURPOSE, String.class);
			transactionClassMap.put(TYPE, String.class);
		}
	}

}
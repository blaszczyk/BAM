package bam.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import bam.core.*;


public interface BAMCoreController {

	public boolean loadUser();
	public boolean saveUser();
	public boolean exit() ;
	
	public boolean deletePayment( BAMPayment payment);
	public boolean editPayment( BAMPayment payment, String name, String amount, Date date, String purpose, String billNr, List<BAMTransaction> tList );
	public boolean addPayment( BAMSubAccount subaccount, String name, String amount, Date date, String purpose, String billNr, BAMTransaction transaction );
	public boolean addTransactionToPayment( BAMPayment payment , BAMTransaction transaction );
	
	public boolean addTransactionToMultiPayment( BAMMultiPayment multipayment , BAMTransaction transaction );
	public boolean removeTransactionfromMultiPayment ( BAMMultiPayment multipayment, BAMSubPayment payment );
	public boolean setAmountInMultiPayment( BAMMultiPayment multipayment, BAMSubPayment payment, String amount );
	public boolean deleteMultiPayment( BAMMultiPayment multipayment);
	
	public boolean setIBAN( BAMAccount account, String iban);
	public boolean setBalance( BAMAccount account, BigDecimal balance);
	public boolean addTransaction( BAMAccount account, BAMTransaction transaction );
	public boolean sortTransactions(BAMAccount account);
	
	public boolean setUserData( String figo_Account, String figo_PW, String bank_pin );
	public boolean addMultiPayment(BAMSubAccount subaccount, String name, String purpose, String searchName, String searchPurpose);
	
}

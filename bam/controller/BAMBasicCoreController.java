package bam.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import bam.core.*;
import bam.gui.settings.BAMGUISettings;
import bam.tools.BAMException;
import bam.tools.BAMListableJson;
import bam.tools.BAMFormats;

public class BAMBasicCoreController implements BAMCoreController {

	BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	BAMUser user;
	
	public BAMBasicCoreController( BAMUser user ) {
		this.user = user;
	}


	@Override
	public boolean loadUser(){
		try {
			BAMListableJson.loadListable(user, "users/" + user + ".bam");
			user.setParents();
		} catch (BAMException be) {
			be.printStackTrace();
			JOptionPane.showMessageDialog( null, 
					be.getErrorMessage(), 
					guiSettings.getPhrase("LOAD_USER_ERROR"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean saveUser()
	{
		try{
			BAMListableJson.saveListable(user, "users/" + user + ".bam");
			JOptionPane.showMessageDialog(null, 
					guiSettings.getPhrase("ACC_SAVED"), 
					guiSettings.getPhrase("ACC_SAVED"), 
					JOptionPane.INFORMATION_MESSAGE);
		} catch ( BAMException be ) {
			be.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					be.getErrorMessage() , 
					guiSettings.getPhrase("ACC_SAVED_ERR"), 
					JOptionPane.ERROR_MESSAGE );
			return false;
		}
		return true;
	}

	@Override
	public boolean exit() 
	{
		System.exit(0);		
		return true;
	}

	@Override
	public boolean deletePayment( BAMPayment payment)
	{
		BAMSubAccount sa = 	payment.getParent();
		sa.removePayment( payment );
		sa.addBalance( payment.getAmount().negate() );		
		return true;
	}

	@Override
	public boolean editPayment( BAMPayment payment, String name, String amount, Date date, String purpose, String billNr, List<BAMTransaction> tList )
	{
		if( ! BAMFormats.isBigDec( amount ) )
		{
			JOptionPane.showMessageDialog(null, 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_MSG"), 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_TTL"), 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		BigDecimal amountBD = BAMFormats.parseBigDec( amount );
		payment.getParent().addBalance( amountBD.subtract( payment.getAmount() ) );
		payment.setValue( BAMPayment.NAME, name );
		payment.setValue( BAMPayment.PURPOSE, purpose );
		payment.setValue( BAMPayment.BILL_NR, billNr );
		payment.setValue( BAMPayment.DATE, date);
		payment.setValue( BAMPayment.AMOUNT, amountBD );
		payment.clearTransactions();
		for(BAMTransaction t : tList)
			payment.addTransaction(t);
		return true;
	}

	@Override
	public boolean addPayment( BAMSubAccount subaccount, String name, String amount, Date date, String purpose, String billNr, BAMTransaction transaction )
	{	
		if( ! BAMFormats.isBigDec( amount ) )
		{
			JOptionPane.showMessageDialog(null, 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_MSG"), 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_TTL"), 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		BigDecimal amountBD = BAMFormats.parseBigDec( amount );
		BAMPayment payment = new BAMPayment(subaccount, name, amountBD, purpose, billNr, date, transaction );
		subaccount.addPayment(payment);
		subaccount.addBalance(amountBD);
		return true;
	}


	@Override
	public boolean removeTransactionfromMultiPayment ( BAMMultiPayment multipayment, BAMSubPayment payment )
	{
		multipayment.removeTransaction(payment);
		multipayment.getParent().addBalance( payment.getAmount().negate() );
		return true;
	}       

	@Override
	public boolean addTransactionToMultiPayment( BAMMultiPayment multipayment , BAMTransaction transaction ) 
	{
		multipayment.addTransaction(transaction);
		multipayment.getParent().addBalance( transaction.getAmount() );
		return true;
	}
	
	@Override
	public boolean setAmountInMultiPayment(BAMMultiPayment multipayment, BAMSubPayment payment, String amount) {
		if( ! BAMFormats.isBigDec( amount ) )
			return false;
		BigDecimal amountBD = BAMFormats.parseBigDec(amount);
		multipayment.getParent().addBalance( amountBD.subtract( payment.getAmount() ) );
		multipayment.setTransactionAmount(payment, amountBD);
		return true;
	}

	@Override
	public boolean deleteMultiPayment(BAMMultiPayment multipayment) {
		for( BAMSubPayment p : multipayment.getPayments() )
			removeTransactionfromMultiPayment(multipayment, p);
		multipayment.getParent().removeMultiPayment(multipayment);
		return true;
	}
	
	@Override
	public boolean addTransactionToPayment( BAMPayment payment , BAMTransaction transaction ) 
	{
		payment.addTransaction(transaction);
		return true;

	}

	@Override
	public boolean setIBAN( BAMAccount account, String iban)
	{
		account.setIban(iban);
		return true;
	}

	@Override
	public boolean setBalance( BAMAccount account, BigDecimal balance)
	{
		account.setBalance(balance);
		return true;
	}

	@Override
	public boolean addTransaction( BAMAccount account, BAMTransaction transaction )
	{
		account.addTransaction(transaction);
		return true;
	}

	@Override 
	public boolean sortTransactions( BAMAccount account )
	{
		account.sortTransactions( (t1,t2) -> t2.getDate().compareTo(t1.getDate()) );
		return true;
	}

	@Override
	public boolean setUserData(String figo_Account, String figo_PW, String bank_pin) 
	{
		user.setValue( BAMUser.FIGO_USER, figo_Account);
		user.setValue( BAMUser.FIGO_PW, figo_PW);
		user.setValue( BAMUser.BANK_PIN, bank_pin);
		return true;
	}

	@Override
	public boolean addMultiPayment(BAMSubAccount subaccount, String name, String purpose, String searchName, String searchPurpose) {
		BAMMultiPayment mp = new BAMMultiPayment( subaccount, name, purpose, searchName, searchPurpose );
		subaccount.addMultiPayment( mp );
		return true;
	}

}
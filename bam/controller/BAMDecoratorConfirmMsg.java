package bam.controller;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMSwingFrame;
import bam.tools.BAMFormats;
import bam.core.*;

public class BAMDecoratorConfirmMsg extends BAMAbstractDecorator {

	BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	BAMUser user;
	BAMSwingFrame mainFrame = null;

	public BAMDecoratorConfirmMsg( BAMUser user , BAMController controller ) {
		super(controller);
		this.user = user;
	}

	@Override	
	public boolean exit() 
	{
		if( user.isModified() )
		{
			int option = JOptionPane.showConfirmDialog( mainFrame, 
					guiSettings.getPhrase("EXIT_CONFIRM_MSG"),
					guiSettings.getPhrase("EXIT_CONFIRM_TTL"),
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			if( option == JOptionPane.CANCEL_OPTION)
				return false;
			if( option == JOptionPane.YES_OPTION)
				controller.saveUser();
			controller.exit();
		}
		else
			controller.exit();		
		return true;
	}

	@Override
	public boolean deletePayment(BAMPayment payment)
	{
		if( JOptionPane.showConfirmDialog( mainFrame, 
				guiSettings.getPhrase("DEL_PAYMENT")+ " " + payment.getName() + " - " + payment.getPurpose() + " ?", 
				guiSettings.getPhrase("DEL_PAYMENT"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			return controller.deletePayment(payment);
		return false;
	}

	@Override
	public boolean editPayment( BAMPayment payment, String name, String amount, Date date, String purpose, String billNr, List<BAMTransaction> tList )
	{
		if( JOptionPane.showConfirmDialog( mainFrame, 
				guiSettings.getPhrase("EDIT_PAYMENT")+ " " + payment.getName() + " - " + payment.getPurpose() + " ?", 
				guiSettings.getPhrase("EDIT_PAYMENT"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			return controller.editPayment(payment, name, amount, date, purpose, billNr, tList);
		return false;
	}

	@Override
	public boolean addPayment( BAMSubAccount subaccount, String name, String amount, Date date, String purpose, String billNr, BAMTransaction transaction )
	{
		return controller.addPayment(subaccount, name, amount, date, purpose, billNr, transaction);
	}


	@Override
	public boolean removeTransactionfromMultiPayment ( BAMMultiPayment multipayment, BAMSubPayment payment )
	{
		if( JOptionPane.showConfirmDialog( mainFrame, 
				guiSettings.getPhrase("REM_TRANSACT_MSG"), 
				guiSettings.getPhrase("REM_TRANSACT_TTL"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
		{
			return controller.removeTransactionfromMultiPayment(multipayment, payment);
		}
		return false;
	}

	@Override
	public boolean setAmountInMultiPayment( BAMMultiPayment multipayment, BAMSubPayment payment, String amount ) 
	{
		if( ! BAMFormats.isBigDec( amount ) )
		{
			JOptionPane.showMessageDialog(mainFrame, 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_MSG"), 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_TTL"), 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return controller.setAmountInMultiPayment(multipayment, payment, amount);
	}
	

	@Override
	public boolean deleteMultiPayment(BAMMultiPayment multipayment) {
		if( !multipayment.getPayments().isEmpty() )
		{
			JOptionPane.showMessageDialog(mainFrame, 
					guiSettings.getPhrase("MP_NONEMPTY_MSG"),
					guiSettings.getPhrase("MP_NONEMPTY_TTL"), 
					JOptionPane.ERROR_MESSAGE );
			return false;
		}
		if( JOptionPane.showConfirmDialog(mainFrame,
				guiSettings.getPhrase("CONF_DEL_MP_MSG"), 
				guiSettings.getPhrase("CONF_DEL_MP_TTL"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
		{
			return controller.deleteMultiPayment(multipayment);
		}
		return false;
	}

	@Override
	public boolean setUserData(String figo_Account, String figo_PW, String bank_pin) {
		if( JOptionPane.showConfirmDialog(mainFrame, 
				guiSettings.getPhrase("SET_USER_MSG"), 
				guiSettings.getPhrase("SET_USER_TTL"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
		{
			return controller.setUserData(figo_Account, figo_PW, bank_pin);
		}
		return false;
	}
	

	@Override
	public boolean addMultiPayment(BAMSubAccount subaccount, String name, String purpose, String searchName, String searchPurpose) {
		if( JOptionPane.showConfirmDialog(mainFrame, 
				guiSettings.getPhrase("ADD_MP_MSG_1") + name + " - " + purpose + guiSettings.getPhrase("ADD_MP_MSG_2") + subaccount + "?" , 
				guiSettings.getPhrase("ADD_MP_TTL"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
		{
			return controller.addMultiPayment(subaccount, name, purpose, searchName, searchPurpose);
		}
		return false;
	}
}
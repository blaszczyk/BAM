package bam.controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMSwingFrame;
import bam.tools.BAMFormats;
import bam.core.*;

public class BAMDecoratorConfirmMsg extends BAMAbstractGUIDecorator {

	
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	private BAMUser user;
	private List<BAMSwingFrame> frameList = new ArrayList<BAMSwingFrame>();

	public BAMDecoratorConfirmMsg( BAMUser user , BAMController controller ) {
		super(controller);
		this.user = user;
	}

	@Override	
	public boolean exit() 
	{
		if( user.isModified() )
		{
			int option = JOptionPane.showConfirmDialog( getActiveFrame(), 
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
		if( JOptionPane.showConfirmDialog( getActiveFrame(), 
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
		if( JOptionPane.showConfirmDialog( getActiveFrame(), 
				guiSettings.getPhrase("EDIT_PAYMENT")+ " " + payment.getName() + " - " + payment.getPurpose() + " ?", 
				guiSettings.getPhrase("EDIT_PAYMENT"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			return controller.editPayment(payment, name, amount, date, purpose, billNr, tList);
		return false;
	}



	@Override
	public boolean editMultiPayment(BAMMultiPayment multipayment, String name, String purpose, String searchName, String searchPurpose)
	{
		if( JOptionPane.showConfirmDialog( getActiveFrame(), 
				guiSettings.getPhrase("EDIT_MULTIPAYMENT")+ " " + multipayment.getName() + " - " + multipayment.getPurpose() + " ?", 
				guiSettings.getPhrase("EDIT_MULTIPAYMENT"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			return controller.editMultiPayment(multipayment, name, purpose, searchName, searchPurpose);
		return false;
	}
	@Override
	public boolean removeTransactionfromMultiPayment ( BAMMultiPayment multipayment, BAMSubPayment payment )
	{
		if( JOptionPane.showConfirmDialog( getActiveFrame(), 
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
			JOptionPane.showMessageDialog(getActiveFrame(), 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_MSG"), 
					guiSettings.getPhrase("BIGDEC_PARSE_ERR_TTL"), 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return controller.setAmountInMultiPayment(multipayment, payment, amount);
	}
	

	@Override
	public boolean deleteMultiPayment(BAMMultiPayment multipayment) {
		if( multipayment.getPaymentCount() != 0 )
		{
			JOptionPane.showMessageDialog(getActiveFrame(), 
					guiSettings.getPhrase("MP_NONEMPTY_MSG"),
					guiSettings.getPhrase("MP_NONEMPTY_TTL"), 
					JOptionPane.ERROR_MESSAGE );
			return false;
		}
		if( JOptionPane.showConfirmDialog(getActiveFrame(),
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
		if( JOptionPane.showConfirmDialog(getActiveFrame(), 
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
		if( JOptionPane.showConfirmDialog(getActiveFrame(), 
				guiSettings.getPhrase("ADD_MP_MSG_1") + name + " - " + purpose + guiSettings.getPhrase("ADD_MP_MSG_2") + subaccount + "?" , 
				guiSettings.getPhrase("ADD_MP_TTL"), 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
		{
			return controller.addMultiPayment(subaccount, name, purpose, searchName, searchPurpose);
		}
		return false;
	}

	@Override
	protected void onOpen(BAMSwingFrame frame)
	{
		frameList.add(frame);
	}

	@Override
	protected void onClose(BAMSwingFrame frame)
	{
		frameList.remove(frame);
	}
	
	private Component getActiveFrame()
	{
		for(BAMSwingFrame frame : frameList)
			if(frame.isActive())
				return frame;
		return null;
	}
}
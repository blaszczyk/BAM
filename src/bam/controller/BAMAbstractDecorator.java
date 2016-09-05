package bam.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bam.core.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.BAMSwingFrame;

public abstract class BAMAbstractDecorator implements BAMController
{

	protected BAMController controller;
	
	public BAMAbstractDecorator( BAMController controller)
	{
		this.controller = controller;
	}

	@Override
	public boolean loadUser()
	{
		return controller.loadUser();
	}

	@Override
	public boolean saveUser()
	{
		return controller.saveUser();
	}

	@Override
	public boolean exit()
	{
		return controller.exit();
	}

	@Override
	public boolean deletePayment(BAMPayment payment)
	{
		return controller.deletePayment(payment);
	}

	@Override
	public boolean editPayment(BAMPayment payment, String name, String amount, Date date, String purpose, String billNr, List<BAMTransaction> tList)
	{
		return controller.editPayment(payment, name, amount, date, purpose, billNr, tList);
	}

	@Override
	public boolean addPayment(BAMSubAccount subaccount, String name, String amount, Date date, String purpose, String billNr, BAMTransaction transaction)
	{
		return controller.addPayment(subaccount, name, amount, date, purpose, billNr, transaction);
	}

	@Override
	public boolean addTransactionToPayment(BAMPayment payment, BAMTransaction transaction)
	{
		return controller.addTransactionToPayment(payment, transaction);
	}

	@Override
	public boolean addTransactionToMultiPayment(BAMMultiPayment multipayment, BAMTransaction transaction)
	{
		return controller.addTransactionToMultiPayment(multipayment, transaction);
	}

	@Override
	public boolean editMultiPayment(BAMMultiPayment multipayment, String name, String purpose, String searchName, String searchPurpose)
	{
		return controller.editMultiPayment(multipayment, name, purpose, searchName, searchPurpose);
	}
	@Override
	public boolean removeTransactionfromMultiPayment(BAMMultiPayment multipayment, BAMSubPayment payment)
	{
		return controller.removeTransactionfromMultiPayment(multipayment, payment);
	}

	@Override
	public boolean setAmountInMultiPayment(BAMMultiPayment multipayment, BAMSubPayment payment, String amount)
	{
		return controller.setAmountInMultiPayment(multipayment, payment, amount);
	}

	@Override
	public boolean deleteMultiPayment(BAMMultiPayment multipayment)
	{
		return controller.deleteMultiPayment(multipayment);
	}

	@Override
	public boolean setIBAN(BAMAccount account, String iban)
	{
		return controller.setIBAN(account, iban);
	}

	@Override
	public boolean setBalance(BAMAccount account, BigDecimal balance)
	{
		return controller.setBalance(account, balance);
	}

	@Override
	public boolean addTransaction(BAMAccount account, BAMTransaction transaction)
	{
		return controller.addTransaction(account, transaction);
	}

	@Override
	public boolean sortTransactions(BAMAccount account)
	{
		return controller.sortTransactions(account);
	}

	@Override
	public boolean setUserData(String figo_Account, String figo_PW, String bank_pin)
	{
		return controller.setUserData(figo_Account, figo_PW, bank_pin);
	}

	@Override
	public boolean addMultiPayment(BAMSubAccount subaccount, String name, String purpose, String searchName, String searchPurpose)
	{
		return controller.addMultiPayment(subaccount, name, purpose, searchName, searchPurpose);
	}

	@Override
	public BAMSwingFrame openMainFrame()
	{
		return controller.openMainFrame();
	}

	@Override
	public BAMSwingFrame openPopup(BAMGenericPayment gPayment)
	{
		return controller.openPopup(gPayment);
	}

	@Override
	public BAMSwingFrame openPopup(String transaction_id)
	{
		return controller.openPopup(transaction_id);
	}

	@Override
	public BAMSwingFrame openUpdateFrame()
	{
		return controller.openUpdateFrame();
	}

	@Override
	public BAMSwingFrame openStoreFrame(BAMTransaction t)
	{
		return controller.openStoreFrame(t);
	}

	@Override
	public BAMSwingFrame openStoreFrame(List<BAMTransaction> t)
	{
		return controller.openStoreFrame(t);
	}

	@Override
	public BAMSwingFrame openEditFrame(BAMGenericPayment gPayment)
	{
		return controller.openEditFrame(gPayment);
	}

	@Override
	public BAMSwingFrame openSettings()
	{
		return controller.openSettings();
	}

	@Override
	public BAMSwingFrame openSetupUser(BAMUser user)
	{
		return controller.openSetupUser(user);
	}

	@Override
	public void closeFrame(BAMSwingFrame frame)
	{
		controller.closeFrame(frame);
	}

	@Override
	public boolean setGUISettings(Locale locale, BAMFontSet fontSet, String iconFile)
	{
		return controller.setGUISettings(locale, fontSet, iconFile);
	}

	@Override
	public boolean loadGUISettings()
	{
		return controller.loadGUISettings();
	}

	@Override
	public boolean saveGUISettings()
	{
		return controller.saveGUISettings();
	}
}

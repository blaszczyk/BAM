package bam.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import bam.core.BAMAccount;
import bam.core.BAMMultiPayment;
import bam.core.BAMPayment;
import bam.core.BAMSubAccount;
import bam.core.BAMSubPayment;
import bam.core.BAMTransaction;

public abstract class BAMAbstractCoreDecorator implements BAMCoreController {

	public BAMCoreController controller;
	
	public BAMAbstractCoreDecorator( BAMCoreController controller) {
		this.controller = controller;
	}

	@Override
	public boolean loadUser() {
		return doOnLoadSave( controller.loadUser() );
	}

	@Override
	public boolean saveUser() {
		return doOnLoadSave( controller.saveUser() );
	}

	@Override
	public boolean exit() {
		return controller.exit();
	}

	@Override
	public boolean deletePayment(BAMPayment payment) {
		return doAction( controller.deletePayment(payment) );
	}

	@Override
	public boolean editPayment(BAMPayment payment, String name, String amount, Date date, String purpose,
			String billNr, List<BAMTransaction> tList) {
		return doAction( controller.editPayment(payment, name, amount, date, purpose, billNr, tList) );
	}

	@Override
	public boolean addPayment(BAMSubAccount subaccount, String name, String amount, Date date, String purpose,
			String billNr, BAMTransaction transaction) {
		return doAction( controller.addPayment(subaccount, name, amount, date, purpose, billNr, transaction) );
	}

	@Override
	public boolean addTransactionToPayment(BAMPayment payment, BAMTransaction transaction) {
		return doAction( controller.addTransactionToPayment(payment, transaction) );
	}

	@Override
	public boolean addTransactionToMultiPayment(BAMMultiPayment multipayment, BAMTransaction transaction) {
		return doAction( controller.addTransactionToMultiPayment(multipayment, transaction) );
	}

	@Override
	public boolean removeTransactionfromMultiPayment(BAMMultiPayment multipayment, BAMSubPayment payment) {
		return doAction( controller.removeTransactionfromMultiPayment(multipayment, payment) );
	}

	@Override
	public boolean setAmountInMultiPayment(BAMMultiPayment multipayment, BAMSubPayment payment, String amount) {
		return doAction( controller.setAmountInMultiPayment(multipayment, payment, amount) );
	}

	@Override
	public boolean deleteMultiPayment( BAMMultiPayment multipayment) {
		return doAction( controller.deleteMultiPayment( multipayment) );
	}
	
	@Override
	public boolean setIBAN(BAMAccount account, String iban) {
		return doAction( controller.setIBAN(account, iban) );
	}

	@Override
	public boolean setBalance(BAMAccount account, BigDecimal balance) {
		return doAction( controller.setBalance(account, balance) );
	}

	@Override
	public boolean addTransaction(BAMAccount account, BAMTransaction transaction) {
		return doAction( controller.addTransaction(account, transaction) );
	}

	@Override
	public boolean sortTransactions(BAMAccount account) {
		return doAction( controller.sortTransactions(account) );
	}


	@Override
	public boolean setUserData(String figo_Account, String figo_PW, String bank_pin) {
		return doAction( controller.setUserData(figo_Account, figo_PW, bank_pin) );
	}
	
	@Override
	public boolean addMultiPayment(BAMSubAccount subaccount, String name, String purpose, String searchName, String searchPurpose) {
		return doAction( controller.addMultiPayment(subaccount, name, purpose, searchName, searchPurpose) );
	}

	protected abstract void ifChanged();
	protected abstract void ifNotChanged();
	protected abstract void onLoadSaveSucess();
	protected abstract void onLoadSaveFail();
	
	private boolean doAction( boolean hasChanged )
	{
		if( hasChanged)
			ifChanged();
		else
			ifNotChanged();
		return hasChanged;
	}

	private boolean doOnLoadSave( boolean sucess )
	{
		if( sucess )
			onLoadSaveSucess();
		else
			onLoadSaveFail();
		return sucess;
	}
}

package bam.gui;


import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.settings.BAMGUISettings;

@SuppressWarnings("serial")
public class BAMSwingPopupMenu extends JPopupMenu
{
	private BAMController controller;
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	public BAMSwingPopupMenu( BAMListable listable, BAMController controller)
	{
		this.controller = controller;
		if( listable instanceof BAMPayment )
			showPaymentMenu((BAMPayment)listable);
		else if( listable instanceof BAMMultiPayment)
			showMultiPaymentMenu((BAMMultiPayment) listable);
		else if( listable instanceof BAMTransaction)
			showTransactionMenu((BAMTransaction) listable);
		else if( listable instanceof BAMSubPayment)
			showSubPaymentMenu((BAMSubPayment) listable);
	}
	
	private void showPaymentMenu( BAMPayment payment )
	{
		addMenuItem("VIEW").addActionListener( e -> {
			controller.openPopup(payment);
		});
		addMenuItem("EDIT").addActionListener( e -> {
			controller.openEditFrame(payment);
		});
		addMenuItem("DELETE").addActionListener( e -> {
			controller.deletePayment(payment);
		});
	}

	private void showMultiPaymentMenu( BAMMultiPayment multipayment)
	{
		addMenuItem("VIEW").addActionListener( e -> {
			controller.openPopup(multipayment);
		});
		addMenuItem("EDIT").addActionListener( e -> {
			controller.openEditFrame(multipayment);
		});
	}

	private void showTransactionMenu( BAMTransaction transaction)
	{
		addMenuItem("VIEW").addActionListener( e -> {
			controller.openPopup(transaction);
		});
		addMenuItem("STORE").addActionListener( e -> {
			controller.openStoreFrame(transaction);
		});
	}
	
	private void showSubPaymentMenu( BAMSubPayment subpayment )
	{
		addMenuItem("VIEW").addActionListener( e -> {
			controller.openPopup( subpayment.getTransaction_id() );
		});
	}
	
	private JMenuItem addMenuItem(String name)
	{
		JMenuItem item = new JMenuItem( guiSettings.getPhrase(name));
		this.add(item);
		return item;
	}
	
}

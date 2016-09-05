package bam.gui;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.tools.BAMListableTable;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMSwingTwoTable;

@SuppressWarnings("serial")
public class BAMSwingPopupFrame extends BAMSwingFrame{

	private JButton buttonClose;
	private JButton buttonEdit;
	private BAMGenericPayment gPayment;
	
	public BAMSwingPopupFrame( BAMGenericPayment gPayment, BAMController controller)
	{
		super(controller);
		this.gPayment = gPayment;
		buttonClose = new JButton( guiSettings.getPhrase("CLOSE") );
		buttonClose.addActionListener( e->{
			controller.closeFrame(this);
		});
		buttonEdit = new JButton( guiSettings.getPhrase("EDIT") );
		buttonEdit.addActionListener( e->{
			controller.openEditFrame( gPayment );
			controller.closeFrame(this);
		});
		drawAll();		
	}
	

	
	private void setShowTransaction(BAMTransaction transaction )
	{
		
		BAMSwingTwoTable center = new BAMSwingTwoTable(8);
		center.setLeftAppend("     ");
		center.setMiddleAppend( " : ");
		center.addValues(transaction, 
				BAMTransaction.NAME, 
				BAMTransaction.ACC_NR, 
				BAMTransaction.BANK_CODE, 
				BAMTransaction.BANK_NAME, 
				BAMTransaction.AMOUNT, 
				BAMTransaction.TYPE, 
				BAMTransaction.PURPOSE, 
				BAMTransaction.DATE);
		center.setBorder( guiSettings.getBorder() );
		

		JButton buttonStore = new JButton( guiSettings.getPhrase("STORE") );
		buttonStore.addActionListener(e -> {
			controller.openStoreFrame( transaction );
			controller.closeFrame(this);
		});

		JPanel south = new JPanel( new GridLayout(1,2) );
		south.add(buttonStore);
		south.add(buttonClose);

		setTitle( guiSettings.getPhrase("TRANSACTION")+ " - id: " + transaction.getTransaction_id());
		setComponents(null,null,center,null,south);
	}
	
	private void setShowPayment( BAMPayment payment )
	{
		
		BAMSwingTwoTable center = new BAMSwingTwoTable(5);
		center.setLeftAppend("     ");
		center.setMiddleAppend( " : ");
		center.addValues(payment, 
				BAMPayment.NAME, 
				BAMPayment.PURPOSE, 
				BAMPayment.AMOUNT, 
				BAMPayment.DATE, 
				BAMPayment.BILL_NR);
		center.setBorder( guiSettings.getBorder() );
		

		JPanel south = new JPanel( new GridLayout(1,2) );
		south.add(buttonEdit);
		south.add(buttonClose);

		setTitle( guiSettings.getPhrase("PAYMENT") + " - " + payment.getName() + " - " + payment.getPurpose() );
		setComponents(null,null,center,null,south);
	}
	
	private void setShowMultiPayment( BAMMultiPayment multipayment)
	{
		
		BAMSwingTwoTable north = new BAMSwingTwoTable(3);
		north.setLeftAppend("     ");
		north.setMiddleAppend( " : ");
		north.addValues(multipayment, BAMMultiPayment.NAME, BAMMultiPayment.PURPOSE, BAMMultiPayment.TOTAL_AMOUNT);
		north.setBorder( guiSettings.getBorder() );

		Action view = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openPopup( ((BAMSubPayment) e.getSource()).getTransaction_id() );
			}
		};
		
		BAMListableTable<BAMSubPayment> table = new BAMListableTable<>( multipayment, controller, BAMSubPayment.DATE, BAMSubPayment.AMOUNT, "VIEW" );
		table.setColumnWidths( 100, 100, 133);
		table.setMinimumSize( new Dimension(600,600) );
		table.setDoubleClickAction(view);
		table.setButtonColumn(2, view);
		table.draw();
		

		JScrollPane scrollPane = new JScrollPane( table );
		scrollPane.setPreferredSize( new Dimension(600,300) );
		

		JPanel south = new JPanel( new GridLayout(1,2) );
		south.add(buttonEdit);
		south.add(buttonClose);
		
		setTitle(guiSettings.getPhrase("MULTIPAYMENT")+ " - " + multipayment.getName() +" - " + multipayment.getPurpose() );
		setComponents(north,null,scrollPane,null,south);
	}	

	@Override 
	public void drawAll()
	{
		if( gPayment instanceof BAMTransaction )
			setShowTransaction( (BAMTransaction)gPayment );
		if( gPayment instanceof BAMPayment )
			setShowPayment( (BAMPayment) gPayment );
		if( gPayment instanceof BAMMultiPayment )
			setShowMultiPayment( (BAMMultiPayment) gPayment );
		setMinimumSize( new Dimension(600,100));
		super.draw();
	}
	
	@Override
	public void draw()
	{ }
}

package bam.gui;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.BAMListable;
import bam.core.BAMMultiPayment;
import bam.core.BAMPayment;
import bam.core.BAMSubPayment;
import bam.core.BAMTransaction;
import bam.gui.tools.BAMListableTable;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMSwingTwoTable;

@SuppressWarnings("serial")
public class BAMSwingPopup extends BAMSwingFrame{


	JButton buttonClose;
	BAMListable shownObject;
	
	public BAMSwingPopup( BAMListable shownObject, BAMController controller)
	{
		super(controller);
		this.shownObject = shownObject;
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
		
		JButton buttonEdit = new JButton( guiSettings.getPhrase("EDIT_PAYMENT") );
		buttonEdit.addActionListener( e->{
			controller.openEditPayment( payment );
			controller.closeFrame(this);
		});

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
		Action setAmount = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				BAMSubPayment payment = (BAMSubPayment) e.getSource();
				String amount = JOptionPane.showInputDialog( guiSettings.getPhrase("ENTER_AMOUNT"), payment.getAmount().toString() );
				if( amount != null && controller.setAmountInMultiPayment(multipayment, payment, amount) )
				{
					controller.closeFrame( BAMSwingPopup.this );
					controller.openPopup( multipayment );
				}
			}
		};
		Action remove = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if( controller.removeTransactionfromMultiPayment(multipayment, (BAMSubPayment) e.getSource() ) )
				{
					controller.closeFrame( BAMSwingPopup.this );
					controller.openPopup( multipayment );
				}
			}
		};
		
		BAMListableTable table = new BAMListableTable( multipayment.getPayments(), BAMSubPayment.DATE, BAMSubPayment.AMOUNT, "VIEW", "SET_AMOUNT", "REMOVE" );
		table.setColumnWidths( 100, 100, 133, 133, 133);
		table.setMinimumSize( new Dimension(600,600) );
		table.setButtonColumnMouse(2, view);
		table.setButtonColumn(3, setAmount);
		table.setButtonColumn(4, remove);
		table.draw();
		

		JScrollPane scrollPane = new JScrollPane( table );
		scrollPane.setPreferredSize( new Dimension(600,300) );
		
		JButton buttonDelete = new JButton( guiSettings.getPhrase("DELETE") );
		buttonDelete.addActionListener( e -> {
			if ( controller.deleteMultiPayment(multipayment) )
				controller.closeFrame(this);			
		});

		JPanel south = new JPanel( new GridLayout(1,2) );
		south.add(buttonDelete);
		south.add(buttonClose);
		
		setTitle(guiSettings.getPhrase("MULTIPAYMENT")+ " - " + multipayment.getName() +" - " + multipayment.getPurpose() );
		setComponents(north,null,scrollPane,null,south);
	}	

	@Override 
	public void drawAll()
	{
		buttonClose = new JButton( guiSettings.getPhrase("CLOSE") );
		buttonClose.addActionListener( e->{
			controller.closeFrame(this);
		});
		if( shownObject instanceof BAMTransaction )
			setShowTransaction( (BAMTransaction)shownObject );
		if( shownObject instanceof BAMPayment )
			setShowPayment( (BAMPayment) shownObject );
		if( shownObject instanceof BAMMultiPayment )
			setShowMultiPayment( (BAMMultiPayment) shownObject );
		setMinimumSize( new Dimension(600,100));
		super.draw();
	}
	
	@Override
	protected void draw()
	{ }
}

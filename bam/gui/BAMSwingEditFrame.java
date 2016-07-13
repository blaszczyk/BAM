package bam.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.tools.*;

@SuppressWarnings("serial")
public class BAMSwingEditFrame extends BAMSwingFrame {

	private BAMGenericPayment gPayment;
	private BAMUser user;
	
	private List<BAMTransaction> tList;
	
	private BAMSwingTwoTable north;
	private JPanel center = new JPanel();
	private JPanel south = new JPanel();
	
	private JTextField tfName;
	private JTextField tfPurpose;
	private JTextField tfSearchName;
	private JTextField tfSearchPurpose;
	private BAMDateSelect dsDate;
	private JTextField tfAmount;
	private JTextField tfBillNr;
	

	Action viewTransaction = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.openPopup( (BAMGenericPayment)e.getSource() );
		}
	};
	Action removeTransaction = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			tList.remove( e.getSource() );
			drawCenter();
		}
	};
	
	Action setAmount = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			BAMSubPayment payment = (BAMSubPayment) e.getSource();
			String amount = JOptionPane.showInputDialog( guiSettings.getPhrase("ENTER_AMOUNT"), payment.getAmount().toString() );
			if( amount != null && controller.setAmountInMultiPayment((BAMMultiPayment)gPayment, payment, amount) )
			{
				controller.closeFrame( BAMSwingEditFrame.this );
				controller.openPopup( gPayment );
			}
		}
	};

	Action remove = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			if( controller.removeTransactionfromMultiPayment((BAMMultiPayment) gPayment, (BAMSubPayment) e.getSource() ) )
			{
				controller.closeFrame( BAMSwingEditFrame.this );
				controller.openPopup( gPayment );
			}
		}
	};
	public BAMSwingEditFrame( BAMGenericPayment gPayment, BAMUser user, BAMController controller){
		super("EDIT", controller);
		this.gPayment = gPayment;
		this.user = user;
		setMinimumSize( new Dimension(800,300));
		drawAll();
		setComponents(north,null,center,null,south);
		draw();
	}

	private void paymentView(BAMPayment payment)
	{
		tfName = new JTextField(payment.getName());
		tfPurpose = new JTextField(payment.getPurpose());
		dsDate = new BAMDateSelect(payment.getDate());
		tfAmount = new JTextField(payment.getAmount().toString());
		tfBillNr = new JTextField(payment.getBillNr());

		
		north =  new BAMSwingTwoTable(5);
		north.setLeftAppend("     ");
		north.setMiddleAppend( " : ");
		north.addRow( BAMPayment.NAME , tfName );
		north.addRow( BAMPayment.PURPOSE , tfPurpose );
		north.addRow( BAMPayment.DATE, dsDate );
		north.addRow( BAMPayment.AMOUNT , tfAmount );
		north.addRow( BAMPayment.BILL_NR , tfBillNr );
		north.setBorder( guiSettings.getBorder() );
		north.setVisible(true);

		tList = new ArrayList<BAMTransaction>();
		for( String t : payment.getTransactions() )
			tList.add( user.getTransactionById( t ) );
		
		BAMListableTable<BAMTransaction> table = new BAMListableTable<>(tList, controller, BAMTransaction.NAME, BAMTransaction.AMOUNT, BAMTransaction.PURPOSE, "VIEW", "REMOVE" );
		table.setColumnWidths(150,70,350,100,100);
		table.setButtonColumn(3, viewTransaction);
		table.setDoubleClickAction(viewTransaction);
		table.setButtonColumn(4, removeTransaction);
		table.draw();
		center.setVisible(false);
		center.removeAll();	
		center.add(table);
		center.setBorder( guiSettings.getBorder() );
		center.setVisible(true);
	}
	
	private void multipaymentView( BAMMultiPayment multipayment)
	{
		tfName = new JTextField(multipayment.getName());
		tfPurpose = new JTextField(multipayment.getPurpose());
		tfSearchName = new JTextField((String) multipayment.getValue( BAMMultiPayment.SEARCH_NAME ));
		tfSearchPurpose = new JTextField((String) multipayment.getValue( BAMMultiPayment.SEARCH_PURPOSE ));
		
		north =  new BAMSwingTwoTable(4);
		north.setLeftAppend("     ");
		north.setMiddleAppend( " : ");
		north.addRow( BAMMultiPayment.NAME , tfName );
		north.addRow( BAMMultiPayment.PURPOSE , tfPurpose );
		north.addRow( BAMMultiPayment.SEARCH_NAME , tfSearchName );
		north.addRow( BAMMultiPayment.SEARCH_PURPOSE , tfSearchPurpose );
		north.setBorder( guiSettings.getBorder() );
		north.setVisible(true);

		tList = new ArrayList<BAMTransaction>();
		for( BAMSubPayment p : multipayment )
			tList.add( user.getTransactionById( p.getTransaction_id() ) );

		BAMListableTable<BAMSubPayment> table = new BAMListableTable<>( multipayment , controller, BAMSubPayment.DATE, BAMSubPayment.AMOUNT,  "SET_AMOUNT", "REMOVE" );
		table.setColumnWidths( 100, 100,  133, 133);
		table.setMinimumSize( new Dimension(600,600) );
		table.setDoubleClickAction( viewTransaction);
		table.setButtonColumn(2, setAmount);
		table.setButtonColumn(3, removeTransaction);
		table.draw();
	
		center.setVisible(false);
		center.removeAll();	
		center.add(table);
		center.setBorder( guiSettings.getBorder() );
		center.setVisible(true);
	}
	
	@Override
	protected void drawSouth()
	{
		JButton bSave = new JButton( guiSettings.getPhrase("SAVE") );
		bSave.addActionListener( e -> {
			if(gPayment instanceof BAMMultiPayment)
				if(controller.editMultiPayment((BAMMultiPayment)gPayment, tfName.getText(), tfPurpose.getText(), tfSearchName.getText(), tfSearchPurpose.getText()))
					controller.closeFrame(this);						
			if(gPayment instanceof BAMPayment)
				if( controller.editPayment((BAMPayment)gPayment, tfName.getText(), tfAmount.getText(), dsDate.getDate(), tfPurpose.getText(), tfBillNr.getText(), tList))
					controller.closeFrame( this );
		});
		JButton bDelete = new JButton( guiSettings.getPhrase("DELETE") );
		bDelete.addActionListener( e -> {
			if(gPayment instanceof BAMMultiPayment)
				if ( controller.deleteMultiPayment((BAMMultiPayment) gPayment) )
					controller.closeFrame(this);
			if(gPayment instanceof BAMPayment)
				if ( controller.deletePayment((BAMPayment) gPayment) )
					controller.closeFrame(this);
		});

		JButton bCancel = new JButton(guiSettings.getPhrase("CANCEL") );
		bCancel.addActionListener( e -> {
			controller.closeFrame( this );
		});
		south.setLayout( new GridLayout(1,3));
		south.add(bSave);
		south.add(bDelete);
		south.add(bCancel);
	}

	@Override 
	public void drawAll()
	{
		if( gPayment instanceof BAMPayment )
			paymentView( (BAMPayment) gPayment);
		else if( gPayment instanceof BAMMultiPayment )
			multipaymentView( (BAMMultiPayment) gPayment);
		setMinimumSize( new Dimension(600,100));
		drawSouth();
		super.draw();
	}
	
	
}
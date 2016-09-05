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
public class BAMSwingEditPayment extends BAMSwingFrame {

	private BAMPayment payment;
	
	private List<BAMTransaction> tList;
	
	private BAMSwingTwoTable north;
	private JPanel center = new JPanel();
	private JPanel south = new JPanel();
	
	private JTextField tfName;
	private JTextField tfPurpose;
	private BAMDateSelect dsDate;
	private JTextField tfAmount;
	private JTextField tfBillNr;
	
	public BAMSwingEditPayment( BAMPayment payment, BAMUser user, BAMController controller){
		super("Edit Payment", controller);
		this.payment = payment;
		
		setMinimumSize( new Dimension(800,300));

		tfName = new JTextField(payment.getName());
		tfPurpose = new JTextField(payment.getPurpose());
		dsDate = new BAMDateSelect(payment.getDate());
		tfAmount = new JTextField(payment.getAmount().toString());
		tfBillNr = new JTextField(payment.getBillNr());

		tList = new ArrayList<BAMTransaction>();
		for( String t : payment.getTransactions() )
			tList.add( user.getTransactionById( t ) );
		
		drawAll();
		setComponents(north,null,center,null,south);
		draw();
	}
	
	@Override
	protected void drawNorth()
	{		
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
	}

	@Override
	protected void drawCenter()
	{
		
		Action viewTransaction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openPopup( (BAMListable)e.getSource() );
			}
		};
		Action removeTransaction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				tList.remove( e.getSource() );
				drawCenter();
			}
		};

		BAMListableTable table = new BAMListableTable(tList, BAMTransaction.NAME, BAMTransaction.AMOUNT, BAMTransaction.PURPOSE, "VIEW", "REMOVE" );
		table.setColumnWidths(150,70,350,100,100);
		table.setButtonColumnMouse(3, viewTransaction);
		table.setButtonColumn(4, removeTransaction);
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
			controller.editPayment(payment, tfName.getText(), tfAmount.getText(), dsDate.getDate(), tfPurpose.getText(), tfBillNr.getText(), tList);
			controller.closeFrame( this );
		});

		JButton bCancel = new JButton(guiSettings.getPhrase("CANCEL") );
		bCancel.addActionListener( e -> {
			controller.closeFrame( this );
		});
		south.setLayout( new GridLayout(1,2));
		south.add(bSave);
		south.add(bCancel);
	}
}
package bam.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMListableTable;
import bam.gui.tools.BAMSwingTwoTable;

@SuppressWarnings("serial")
public class BAMSwingSubAccountPanel extends JPanel implements BAMModifiedListener{
	
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();

	private BAMSubAccount subaccount;
	private BAMController controller;

	private BAMSwingTwoTable north = new BAMSwingTwoTable( 2 );
	private JTabbedPane center = new JTabbedPane();
	private JPanel paymentsPanel = new JPanel();
	private JPanel multipaymentsPanel = new JPanel();
	private JSplitPane tablePanel = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
	
	private Action view = new AbstractAction(){
		@Override 
		public void actionPerformed(ActionEvent e) {
			controller.openPopup( (BAMListable) e.getSource() );
		}
	};		
	
	private Action delete = new AbstractAction(){
		@Override 
		public void actionPerformed(ActionEvent e) {
			controller.deletePayment((BAMPayment) e.getSource() );
		}
	};
	
	public BAMSwingSubAccountPanel(  BAMSubAccount subaccount, BAMController controller) {
		this.subaccount = subaccount;
		this.controller = controller;
		drawNorth();
		drawCenter();
		setLayout( guiSettings.getBorderLayout() );
		add(north, BorderLayout.NORTH );
		add(center, BorderLayout.CENTER );
		setVisible(true);
	}


	private void drawNorth(){
		north.setVisible(false);
		north.removeAll();;
		north.setLeftAppend("     ");
		north.setMiddleAppend(" : ");
		north.addRow( "SUBACCOUNT" , subaccount.toString() );
		north.addRow( BAMSubAccount.BALANCE , subaccount.getBalance() );		
		north.setVisible(true);
	}
	
	
	private void drawCenter(){
		drawTableView();
		drawPaymentsView();
		drawMultiPaymentsView();
		center.addTab(guiSettings.getPhrase("TABLE_VIEW"), tablePanel);
		center.addTab(guiSettings.getPhrase("PAYMENTS"), paymentsPanel);
		center.addTab(guiSettings.getPhrase("MULTIPAYMENTS"), multipaymentsPanel);
	}
		
	private void drawMultiPaymentsView()
	{
		multipaymentsPanel.setVisible(false);
		multipaymentsPanel.removeAll();
		multipaymentsPanel.add( new JLabel("MPS") );
		// TODO: add some more
		multipaymentsPanel.setVisible(true);
	}


	private void drawPaymentsView()
	{
		paymentsPanel.setVisible(false);
		paymentsPanel.removeAll();
		paymentsPanel.add( new JLabel("PS") );
		// TODO: add some more
		paymentsPanel.setVisible(true);
	}


	private void drawTableView(){
		BAMListableTable mptable = new BAMListableTable( subaccount.getMultiPayments(), 
				BAMMultiPayment.NAME, BAMMultiPayment.PURPOSE, BAMMultiPayment.TOTAL_AMOUNT, 
				BAMMultiPayment.LAST_AMOUNT, BAMMultiPayment.LAST_DATE, BAMMultiPayment.NR_TRANSACTIONS, "VIEW");
		mptable.setColumnWidths( 200,150,100,100,100,150,100);
		mptable.setButtonColumnMouse(6, view);
		mptable.draw();
		
		BAMListableTable ptable = new BAMListableTable( subaccount.getPayments(), 
				BAMPayment.NAME, BAMPayment.DATE, BAMPayment.AMOUNT, 
				BAMPayment.PURPOSE, BAMPayment.BILL_NR, "VIEW", "DELETE" );
		ptable.setColumnWidths(150,100,100,250,100,100,100);
		ptable.setButtonColumnMouse(5, view);
		ptable.setButtonColumn(6, delete);
		ptable.draw();
		
		tablePanel.setVisible(false);
		tablePanel.setLeftComponent(new JScrollPane( mptable ));
		tablePanel.setRightComponent(new JScrollPane( ptable));
		int divLoc = (int) mptable.getPreferredSize().getHeight() + 50;
//		int divLoc = (int) ((center.getPreferredSize().getHeight() + mptable.getPreferredSize().getHeight() - ptable.getPreferredSize().getHeight() ) / 2);
//		System.out.println("" + center.getPreferredSize().getHeight() + " " + mptable.getPreferredSize().getHeight() + " " + ptable.getPreferredSize().getHeight() );
//		System.out.println(divLoc);
		tablePanel.setDividerLocation(divLoc);
		tablePanel.setOneTouchExpandable(true);
		tablePanel.setContinuousLayout(true);
		tablePanel.setVisible( true );
	}

	public BAMSwingSubAccountPanel setPaymentView( )
	{
		center.setSelectedIndex( 1 );
		return this;
	}

	public BAMSwingSubAccountPanel setMultiPaymentView( )
	{
		center.setSelectedIndex( 2 );
		return this;
	}

	@Override
	public void modified(BAMModifiedEvent e) {
		if( e.getModfiedInstance() == subaccount)
		{
			drawNorth();
			drawCenter();
		}
	}

	@Override
	public void setVisible( boolean aFlag )
	{
		super.setVisible( aFlag );
		if( aFlag )
			subaccount.addModifiedListener( this );
		else
			subaccount.removeModifiedListener( this );
	}
}
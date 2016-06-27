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
	private JSplitPane center = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
	
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
		north.addRow( BAMSubAccount.BALANCE , subaccount.getBalance().toString() );		
		north.setVisible(true);
	}
	
	
	private void drawCenter(){
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
		
		center.setVisible(false);
		center.setLeftComponent(new JScrollPane( mptable ));
		center.setRightComponent(new JScrollPane( ptable));
		int divLoc = (int) mptable.getPreferredSize().getHeight() + 50;
//		int divLoc = (int) ((center.getPreferredSize().getHeight() + mptable.getPreferredSize().getHeight() - ptable.getPreferredSize().getHeight() ) / 2);
//		System.out.println("" + center.getPreferredSize().getHeight() + " " + mptable.getPreferredSize().getHeight() + " " + ptable.getPreferredSize().getHeight() );
//		System.out.println(divLoc);
		center.setDividerLocation(divLoc);
		center.setOneTouchExpandable(true);
		center.setContinuousLayout(true);
		center.setVisible( true );
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
package bam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.*;
import bam.tools.*;

@SuppressWarnings("serial")
public class BAMSwingStoreTransactionFrame extends BAMSwingFrame{

	private BAMUser user;
	
	private BAMSwingTwoTable north = new BAMSwingTwoTable(4);
	private JPanel west = new JPanel();
	private JPanel center = new JPanel();
	private JPanel south = new JPanel();
	
	private JTextField tfName = new JTextField();
	private JTextField tfAmount = new JTextField();
	private JTextField tfPurpose = new JTextField();
	private JTextField tfBillNr = new JTextField();
	
	private BAMSuggestor suggestor;
	private BAMTransaction transaction;
	private List<BAMTransaction> tList;
	private BAMSubAccount subAccount;
	private BAMListable targetPayment;
	
	private static final int TYPE_SUGGESTION = 1;
	private static final int TYPE_MULTIPAYMENT = 2;
	private static final int TYPE_NEW_PAYMENT = 3;
	private static final int TYPE_OLD_PAYMENT = 4;
	private int type;
	
	private int tCount = 0;
	

	Action select = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			targetPayment =  (BAMListable) e.getSource();
			selectSubAccount();
			drawSouth();
		}
	};

	//Three Constructors
	private BAMSwingStoreTransactionFrame( BAMUser user, BAMController controller )
	{
		super(controller);
		this.user = user;
		setComponents(north,west,center,null,south);
	}
	
	public BAMSwingStoreTransactionFrame( BAMUser user, BAMController controller, BAMTransaction transaction)
	{
		this(user,controller);
		loadTransaction(transaction);
		draw();
	}
	
	public BAMSwingStoreTransactionFrame( BAMUser user, BAMController controller, List<BAMTransaction> tList)
	{
		this(user,controller);
		if(tList == null || tList.isEmpty() )
			controller.closeFrame(this);
		else
		{
			this.tList = tList;
			loadTransaction(tList.get(0));
		}
		draw();
	}
	
	// Draw Methods ( North, West, Center, South)
	@Override
	protected void drawNorth( )
	{
		north.setVisible( false);
		north.removeAll();
		north.setLeftAppend("     ");
		north.setMiddleAppend(" : ");
		north.addValues(transaction, BAMTransaction.NAME, BAMTransaction.DATE, BAMTransaction.PURPOSE, BAMTransaction.AMOUNT);
		north.setBorder( guiSettings.getBorder() );
		north.setVisible(true);
	}

	@Override
	protected void drawWest( )
	{
		west.setVisible(false);
		west.removeAll();
		west.setBorder( guiSettings.getBorder() );

		JRadioButton newPayment = new JRadioButton( guiSettings.getPhrase("CREATE_NEW") );
		newPayment.addActionListener( e -> {
			type = TYPE_NEW_PAYMENT;
			drawComponents(false,false,true,false,true);
		});
		
		JRadioButton toPayment = new JRadioButton( guiSettings.getPhrase("ADD_TO_PAYMENT") );
		toPayment.addActionListener( e -> {
			type = TYPE_OLD_PAYMENT;
			drawComponents(false,false,true,false,true);
		});
		
		JRadioButton toMultiPayment = new JRadioButton( guiSettings.getPhrase("ADD_TO_MULTI") );
		toMultiPayment.addActionListener( e -> {
			type = TYPE_MULTIPAYMENT;
			drawComponents(false,false,true,false,true);
		});
		
		JRadioButton toSuggestion = new JRadioButton( guiSettings.getPhrase("ADD_TO_SUGGESTION") );
		toSuggestion.addActionListener( e -> {
			type = TYPE_SUGGESTION;
			if( suggestor.getSuggestionCount() == 1 )
				targetPayment = suggestor.getSuggestion(0);
			drawComponents(false,false,true,false,true);
		});
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(newPayment);
		buttonGroup.add(toPayment);
		buttonGroup.add(toMultiPayment);
		
		JPanel selector = new JPanel( new GridLayout(4,1) );
		selector.add(newPayment);
		selector.add(toPayment);
		selector.add(toMultiPayment);

		if(suggestor.getSuggestionCount() > 0)
		{
			buttonGroup.add(toSuggestion);
			selector.add(toSuggestion);
			toSuggestion.setSelected(true);
		}
		else
			newPayment.setSelected(true);	

				
		JTree tree = new JTree( new BAMUserTreeModel ( user ).setTerminateAtSubaccounts(true) );
		for (int row = 0; row < tree.getRowCount(); row++ )
			tree.expandPath( tree.getPathForRow(row));
		tree.addTreeSelectionListener( e -> {
			Object o = e.getNewLeadSelectionPath().getLastPathComponent();
			if(o instanceof BAMSubAccount)
			{
				subAccount = (BAMSubAccount)o ;
				drawComponents(false,false,true,false,true);
			}
		});
		
		west.setLayout( guiSettings.getBorderLayout() );		
		west.add(selector,BorderLayout.NORTH);
//		west.add( new JScrollPane(tree), BorderLayout.CENTER);
		west.add( tree , BorderLayout.CENTER);
		west.setVisible(true);
	}
	
	@Override
	protected void drawCenter( )
	{
		center.setVisible(false);
		center.removeAll();
		center.setBorder( guiSettings.getBorder() );
		center.setPreferredSize( new Dimension(600,200) );

		switch( type )
		{
		case TYPE_SUGGESTION:
			if(suggestor.getSuggestionCount() == 1)
			{
				JLabel label = new JLabel(guiSettings.getPhrase("SUGG_MULTI"), SwingConstants.CENTER);
				label.setFont( guiSettings.getFont( BAMFontSet.BIG ) );
				center.add(label);
				label = new JLabel("" + suggestor.getSuggestion(0).getValue("NAME") + " - " + suggestor.getSuggestion(0).getValue("PURPOSE") , SwingConstants.CENTER);
				label.setFont( guiSettings.getFont( BAMFontSet.BIG ) );
				center.add(label);
			}
//				center.add( new JLabel( guiSettings.getPhrase("SUGG_MULTI") + suggestor.getSuggestion(0).getName() + " - " + suggestor.getSuggestion(0).getPurpose() ), SwingConstants.CENTER);
			else
			{
				BAMListableTable<BAMGenericPayment> table = new BAMListableTable<>( suggestor.getSuggestions(), controller,  "NAME", "PURPOSE", "SELECT");
				table.setColumnWidths(250,300,150);
				table.setDoubleClickAction(select);
				table.setButtonColumn(2, select);
				table.setPopupMenuEnabled(false);
				table.draw();
				center.add(new JScrollPane( table ));
			}
			break;
		case TYPE_NEW_PAYMENT:
			tfName.setText( transaction.getName() );
			tfAmount.setText( transaction.getAmount().toString() );
			tfPurpose.setText( transaction.getPurpose() );

			BAMSwingTwoTable form = new BAMSwingTwoTable(5);
			form.setLeftAppend("     ");
			form.setMiddleAppend( " : ");
			form.addRow( BAMPayment.NAME , tfName);
			form.addRow( BAMPayment.AMOUNT , tfAmount);
			form.addRow( BAMPayment.PURPOSE , tfPurpose);
			form.addRow( BAMPayment.BILL_NR , tfBillNr);
			form.addRow( BAMPayment.DATE , BAMFormats.dateFormat( transaction.getDate() ) );
			center.add(form) ;
			break;
		case TYPE_OLD_PAYMENT:
			BAMListableTable<BAMPayment> ptable = new BAMListableTable<>(subAccount.getPayments(), controller,
					BAMPayment.NAME, BAMPayment.PURPOSE, BAMPayment.AMOUNT, "SELECT");
			ptable.setColumnWidths(200,250,100,150);
			ptable.setDoubleClickAction(select);
			ptable.setButtonColumn(3, select);
			ptable.setPopupMenuEnabled(false);
			ptable.draw();
			center.add(new JScrollPane( ptable));
			break;
		case TYPE_MULTIPAYMENT:
			BAMListableTable<BAMMultiPayment> mptable = new BAMListableTable<>(subAccount.getMultiPayments(), controller,
					BAMMultiPayment.NAME, BAMMultiPayment.PURPOSE, "SELECT");
			mptable.setColumnWidths(250,300,150);
			mptable.setDoubleClickAction(select);
			mptable.setButtonColumn(2, select);
			mptable.setPopupMenuEnabled(false);
			mptable.draw();
			center.add(new JScrollPane( mptable ));
		}		
		center.setVisible(true);
	}


	@Override
	protected void drawSouth()
	{				
		JButton save = new JButton();
		
		JLabel infoText = new JLabel();
		infoText.setFont( guiSettings.getFont( BAMFontSet.BIG ) );
		infoText.setHorizontalAlignment( SwingConstants.CENTER );
		
		if( type == TYPE_NEW_PAYMENT )
		{
			infoText.setText( guiSettings.getPhrase("NEW_PAYMENT_IN") + subAccount );
			save.setText( guiSettings.getPhrase("SAVE_NEW") );
			save.addActionListener( e -> {
				controller.addPayment(subAccount, 
						tfName.getText(), 
						tfAmount.getText(), 
						transaction.getDate(), 
						tfPurpose.getText(), 
						tfBillNr.getText(), 
						transaction );
				nextTransaction();					
			});
		} 
		else if( targetPayment instanceof BAMPayment )
		{
			BAMPayment p = (BAMPayment) targetPayment;
			infoText.setText( guiSettings.getPhrase("SELECTED_PAYMENT") + p.getName() + " - " + p.getAmount() + " - " + p.getPurpose() + " in "  + subAccount );
			save.setText( guiSettings.getPhrase("STORE_TO_PAYMENT") );
			save.addActionListener( e -> {
				controller.addTransactionToPayment(p, transaction);
				nextTransaction();
			});
		}
		else if( targetPayment instanceof BAMMultiPayment )
		{
			BAMMultiPayment mp = (BAMMultiPayment) targetPayment;
			infoText.setText( guiSettings.getPhrase("SELECTED_MULTI") + mp.getName() + " - " + mp.getPurpose() + " in "  + subAccount );
			save.setText( guiSettings.getPhrase("STORE_TO_MULTI") );
			save.addActionListener( e -> {
				controller.addTransactionToMultiPayment(mp, transaction);
				nextTransaction();
			});
		}
		else
		{			
			save.setText( guiSettings.getPhrase("SAVE") );
			save.setEnabled( false );
			if( type == TYPE_OLD_PAYMENT)
				infoText.setText( guiSettings.getPhrase("SEL_PAYMENT") );
			else
				infoText.setText( guiSettings.getPhrase("SEL_MULTI") );
		}
		save.addActionListener( e -> {
			user.setModified( true );
		});
		
		JButton skip = new JButton ( guiSettings.getPhrase("SKIP") );
		if( tList == null || tCount >= tList.size() )
			skip.setText( guiSettings.getPhrase("CANCEL") );
		skip.addActionListener( e->{nextTransaction();} );

		JPanel buttonRow = new JPanel();
		buttonRow.setLayout( new GridLayout(1,2));
		buttonRow.add( save );
		buttonRow.add( skip );

		south.setVisible(false);
		south.removeAll();
		south.setLayout( new GridLayout(2,1));		
		south.add(infoText);
		south.add(buttonRow);
		south.setVisible(true);
	}
	
	// Transaction methods
	private void loadTransaction( BAMTransaction transaction )
	{		
		this.transaction = transaction;
		setTitle( guiSettings.getPhrase("STORE_TTL") + " - " + transaction.getTransaction_id()) ;
		suggestor = new BAMSuggestor(transaction, user).findSuggestion();
		if( suggestor.getSuggestionCount() > 0)
		{
			type = TYPE_SUGGESTION;
			targetPayment = suggestor.getSuggestion(0);
			selectSubAccount();
		}
		else
		{
			type = TYPE_NEW_PAYMENT;
			subAccount = user.getAccount(0).getSubAccount(0);
		}
		drawAll();
	}

	private void nextTransaction()
	{
		tCount++;
		if( tList == null || tCount >= tList.size() )
			controller.closeFrame(this);
		else
			loadTransaction(tList.get(tCount));
	}
	
	private void selectSubAccount()
	{
		if( targetPayment instanceof BAMMultiPayment )
			subAccount = ((BAMMultiPayment)targetPayment).getParent();
		if( targetPayment instanceof BAMPayment )
			subAccount = ((BAMPayment)targetPayment).getParent();
	}
	
	public void addTransaction( BAMTransaction transaction)
	{
		if(tList.contains(transaction))
			return;
		tList.add(transaction);
	}
	
}
package bam.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import bam.controller.BAMController;
import bam.core.BAMAccount;
import bam.core.BAMSubAccount;
import bam.core.BAMUser;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMSwingTwoTable;

@SuppressWarnings("serial")
public class BAMSwingSetupUser extends BAMSwingFrame {

	private BAMUser user;
	private JTextField figoAccount;
	private JPasswordField figoPW;
	private JPasswordField bankPin;

	private JComboBox<String> accounts;
	private JComboBox<String> subaccounts;
	private JButton addMultiPayment;
	
	private JList<String> west;
	private JPanel center = new JPanel();
	private JPanel south = new JPanel();

	private int selection = 0;
	private BAMSubAccount subaccount;


	private class TextColorBlack implements DocumentListener{
		private Component c;
		public TextColorBlack( Component c ) {
			this.c = c ;
		}
		@Override
		public void insertUpdate(DocumentEvent e) {
			c.setForeground( Color.BLACK );
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			c.setForeground( Color.BLACK );
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			c.setForeground( Color.BLACK );
		}
	};
	
	
	public BAMSwingSetupUser(BAMUser user, BAMController controller) {
		super(controller);
		this.user = user;

		figoAccount = new JTextField( user.getFigoAccount() );
		figoPW = new JPasswordField( user.getFigoPW() );
		bankPin = new JPasswordField( user.getBankPin() );
		setTitle( "USER_SETUP" );

		center.setPreferredSize( new Dimension( 500,300));
		drawAll();
		setComponents(null,west,center,null,south);
		draw();
	}
	
	@Override
	protected void drawWest()
	{
		String[] options = { guiSettings.getPhrase("CHANGE_USER_DATA"), guiSettings.getPhrase("NEW_MULTIPAYMENT")  };
		west = new JList<>( options );
		west.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		west.addListSelectionListener( e -> {
			selection = west.getSelectedIndex();
			drawCenter();
		});
	}

	
	
	@Override
	protected void drawCenter()
	{
		center.setVisible(false);
		center.removeAll();
		switch(selection)
		{
		case 0:
			drawChangeUserData();
			break;
		case 1:
			drawNewMultiPayment();
			break;
		}
		center.setVisible(true);
	}

	private void drawChangeUserData()
	{
		BAMSwingTwoTable userDataPanel = new BAMSwingTwoTable(3);
		userDataPanel.setLeftAppend("     ");
		userDataPanel.addRow(guiSettings.getPhrase("FIGO_ACCOUNT") + ":", figoAccount);
		userDataPanel.addRow(guiSettings.getPhrase("FIGO_PW") + ":", figoPW);
		userDataPanel.addRow(guiSettings.getPhrase("BANK_PIN") + ":", bankPin);
		center.add(userDataPanel);
	}
	
	private void drawNewMultiPayment()
	{		
		/*
		 * ComboBoxes to Select SubAccount
		 */
		accounts = new JComboBox<String>( );
		accounts.addItem( guiSettings.getPhrase("SEL_ACC"));
		for( BAMAccount a : user.getAccounts() )
			accounts.addItem( a.toString() );
		accounts.addItemListener( e -> {
			addMultiPayment.setEnabled( false );
			int index = accounts.getSelectedIndex() - 1;
			if( index < 0 )
			{
				subaccounts.setEnabled( false );
				return;
			}
			BAMAccount a = user.getAccount( index );
			subaccounts.removeAllItems();
			subaccounts.addItem( guiSettings.getPhrase("SEL_SUBACC"));
			for( BAMSubAccount sa : a.getSubAccounts() )
				subaccounts.addItem( sa.toString() );
			subaccounts.setEnabled(true);
		});
		
		subaccounts = new JComboBox<String>();
		subaccounts.addItem( guiSettings.getPhrase("SEL_SUBACC"));
		subaccounts.setEnabled(false);
		subaccounts.addItemListener( e -> {
			int index = subaccounts.getSelectedIndex() - 1;
			if( index < 0 )
			{
				subaccount = null;
				addMultiPayment.setEnabled( false );
				return;
			}
			subaccount = user.getAccount( accounts.getSelectedIndex() - 1 ).getSubAccount( subaccounts.getSelectedIndex() - 1 );
			addMultiPayment.setEnabled( true );
		});
		
		JPanel selectSubAccount = new JPanel( new GridLayout( 1,2 ) );
		selectSubAccount.add(accounts);
		selectSubAccount.add(subaccounts);
		
		/*
		 * TextFields for MultiPayment Data
		 */
		JTextField mpName = new JTextField( guiSettings.getPhrase("REQUIRED") );
		JTextField mpPurpose = new JTextField( guiSettings.getPhrase("REQUIRED") );
		JTextField mpSearchName = new JTextField( guiSettings.getPhrase("OPTIONAL") );
		JTextField mpSearchPurpose = new JTextField( guiSettings.getPhrase("OPTIONAL") );

		mpName.getDocument().addDocumentListener( new TextColorBlack( mpName ) );
		mpPurpose.getDocument().addDocumentListener( new TextColorBlack( mpPurpose ) );
		
		BAMSwingTwoTable mpProperties = new BAMSwingTwoTable(4);
		mpProperties.setLeftAppend("   ");
		mpProperties.setMiddleAppend(" : ");
		mpProperties.addRow("NAME", mpName);
		mpProperties.addRow("PURPOSE", mpPurpose);
		mpProperties.addRow("SEARCH_NAME", mpSearchName);
		mpProperties.addRow("SEARCH_PURPOSE", mpSearchPurpose);
				

		/*
		 * Confirm Button
		 */
		
		addMultiPayment = new JButton( guiSettings.getPhrase("ADD_MULTIPAYMENT") );
		addMultiPayment.setEnabled( false );
		addMultiPayment.addActionListener( e -> {
			if( mpName.getText().equals( guiSettings.getPhrase("REQUIRED") ) )
			{
				mpName.setForeground( Color.RED );
				return;
			}
			if( mpPurpose.getText().equals( guiSettings.getPhrase("REQUIRED") ) )
			{
				mpPurpose.setForeground( Color.RED );
				return;
			}
			if( mpSearchName.getText().equals( guiSettings.getPhrase("OPTIONAL") ) )
				mpSearchName.setText("");
			if( mpSearchPurpose.getText().equals( guiSettings.getPhrase("OPTIONAL") ) )
				mpSearchPurpose.setText("")
				;
			controller.addMultiPayment( subaccount, mpName.getText(), mpPurpose.getText(), mpSearchName.getText(), mpSearchPurpose.getText() );			
		});

		
		center.setLayout( guiSettings.getBorderLayout() );
		center.add(selectSubAccount, BorderLayout.NORTH);
		center.add(mpProperties, BorderLayout.CENTER);
		center.add(addMultiPayment,  BorderLayout.SOUTH);
	}
	
	@Override
	protected void drawSouth()
	{
		JButton save = new JButton( guiSettings.getPhrase("SAVE") );
		save.addActionListener( e -> {
			controller.closeFrame(this);
		});
		JButton cancel = new JButton( guiSettings.getPhrase("CANCEL") );
		cancel.addActionListener( e -> {
			controller.closeFrame(this);
		});
		
		south.setLayout( new GridLayout(1,2) );
		south.add(save);
		south.add(cancel);
	}
}

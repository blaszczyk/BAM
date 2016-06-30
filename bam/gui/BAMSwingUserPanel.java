package bam.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;

import bam.controller.BAMController;
import bam.core.BAMAccount;
import bam.core.BAMModifiedEvent;
import bam.core.BAMModifiedListener;
import bam.core.BAMSubAccount;
import bam.core.BAMUser;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMSwingMsgPanel;
import bam.tools.BAMUtils;

@SuppressWarnings("serial")
public class BAMSwingUserPanel extends JPanel implements BAMModifiedListener {

	private BAMUser user;
	private BAMController controller;
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private BAMSwingMsgPanel center = new BAMSwingMsgPanel();
	private JPanel south = new JPanel();
	

	public BAMSwingUserPanel( BAMUser user, BAMController controller ) {
		this.user = user;
		this.controller = controller;
		drawCenter();
		drawSouth();
		setLayout( guiSettings.getBorderLayout() );
		add( center, BorderLayout.CENTER);
		add( south, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	private void drawCenter()
	{
		center.clear();
		center.setFont( guiSettings.getFont( BAMFontSet.BIG) );
		center.drawMsg( "BAM - BankAccountManager 0.10" , "", 
				guiSettings.getPhrase("USER") + ": " + user, 
				guiSettings.getPhrase("FIGO_USER") + ": " + user.getFigoAccount() );
		for( BAMAccount a : user.getAccounts() )
		{
			center.drawMsg("" , 
					guiSettings.getPhrase("ACCOUNT") + ": " + a + " ( " + a.getIban() + " ) " , 
					"     " + guiSettings.getPhrase("BALANCE") + ": " + BAMUtils.toString( a.getBalance() ) + " " + guiSettings.getPhrase("EUR"));
			for( BAMSubAccount sa : a.getSubAccounts() )
				center.drawMsg("     " + guiSettings.getPhrase("SUBACCOUNT") + ": " + sa + " ( " + BAMUtils.toString( sa.getBalance() ) + " " + guiSettings.getPhrase("EUR") + " )");
			if( a.getMisBalance().compareTo( BigDecimal.ZERO) != 0 )
				center.drawMsg( Color.RED, guiSettings.getPhrase("BALANCE_MIS1") + BAMUtils.toString( a.getMisBalance() ) + guiSettings.getPhrase("BALANCE_MIS2") + a );
		}		
	}
	
	private void drawSouth()
	{
		JButton buttonOnlineRefresh = new JButton(guiSettings.getPhrase("ONLINE_ACC_UPD"));
		buttonOnlineRefresh.addActionListener( e -> { 
			controller.openUpdateFrame();
		} );
		
		JButton buttonSave = new JButton( guiSettings.getPhrase("SAVE_ACC") );
		buttonSave.setEnabled( user.isModified() );
		buttonSave.addActionListener( e -> { 
			controller.saveUser();	
		} );
		
		JButton buttonExit = new JButton(guiSettings.getPhrase("EXIT"));
		buttonExit.addActionListener( e -> { 
			controller.exit();
		} );
		
		south.setVisible(false);
		south.removeAll();
		south.setLayout( new GridLayout(1,3));
		south.add( buttonOnlineRefresh );
		south.add( buttonSave  );
		south.add( buttonExit  );		
		south.setVisible(true);
	}

	@Override
	public void modified(BAMModifiedEvent e) {
		drawCenter();
		drawSouth();
	}

	@Override
	public void setVisible( boolean aFlag )
	{
		if( aFlag )
			user.addModifiedListener( this );
		else
			user.removeModifiedListener( this );
		super.setVisible( aFlag );
	}
}
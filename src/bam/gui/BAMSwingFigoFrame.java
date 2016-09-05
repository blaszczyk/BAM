package bam.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.*;

//import java.util.*;
import javax.swing.*;

import bam.controller.BAMController;
import bam.core.BAMAccount;
import bam.core.BAMTransaction;
import bam.core.BAMUser;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMSwingMsgPanel;
import bam.tools.BAMException;
import bam.tools.BAMFigoRequest;

@SuppressWarnings("serial")
public class BAMSwingFigoFrame extends BAMSwingFrame{
	
	private BAMFigoRequest figo;
	private BAMUser user;
	
	private BAMSwingMsgPanel center = new BAMSwingMsgPanel();
	private JCheckBox sync;
	private JButton update;
	private JButton cancel;
	
	private Thread updateThread;
	private ActionListener actionListener;
	
	public BAMSwingFigoFrame( BAMUser user, BAMController controller )
	{
		super( controller ); 
		this.user = user;
		figo = new BAMFigoRequest(user);

		updateThread = new Thread( ()->{ 
			updateAccounts( );
			});
		
		sync = new JCheckBox( guiSettings.getPhrase("DO_SYNC") );
		sync.setSelected(true);

		cancel = new JButton( guiSettings.getPhrase("CANCEL"));
		cancel.addActionListener( e -> {
			controller.closeFrame( BAMSwingFigoFrame.this );
		});
		
		update = new JButton( guiSettings.getPhrase("START_UPDATE") );
		update.addActionListener( actionListener = e -> {
				update.setEnabled( false );
				sync.setEnabled( false );
				updateThread.start();
		});

		JPanel south = new JPanel();	
		south.setLayout( new GridLayout(1,3));
		south.add(sync);
		south.add(update);
		south.add(cancel);

		setTitle( "FIGO_TTL" );
		setMinimumSize( new Dimension(700,600));
		setComponents( null, null, center, null, south);
		draw();
	}

	public void updateAccounts( )
	{
		List<BAMTransaction> tList = new ArrayList<>();
		try{
			center.addMsg( guiSettings.getPhrase("CONNECT_START") + user.getFigoAccount() );
			figo.startSession();
			center.addMsg( guiSettings.getPhrase("CONNECT_END") );
			if( sync.isSelected() )
			{
				center.addMsg( guiSettings.getPhrase("SYNC_START") );
				figo.synchronizeAccounts();
				center.addMsg( guiSettings.getPhrase("SYNC_END") );
				
			}
			List<BAMTransaction> temp;
			for( BAMAccount account : user.getAccounts() )
			{
				center.addMsg( guiSettings.getPhrase("UPD_ACC_START") + account );
//				controller.setIBAN(account, figo.getAccount(account.toString()).getIBAN() );
				BigDecimal newBalance =  figo.getBalance( account );
				center.addMsg( account + guiSettings.getPhrase("UPD_NEW_BALANCE") + newBalance + " " + guiSettings.getPhrase("EUR") );
				temp = figo.getNewTransactions( account );
				center.addMsg( account + guiSettings.getPhrase("UPD_NEW_TA1") + temp.size() + guiSettings.getPhrase("UPD_NEW_TA2") );
				if(!temp.isEmpty())
				{
					for( BAMTransaction transaction : temp)
					{
						transaction.identifyTransfer(user);
						center.addMsg( guiSettings.getPhrase("UPD_NEW_TRANSACTION") + transaction.getName() + " - " + transaction.getPurpose() );
						controller.addTransaction(account, transaction);
					}
					controller.sortTransactions( account );
					if(newBalance.compareTo( account.getBalance())!= 0 )
							center.addMsg( guiSettings.getPhrase("BALANCE_MIS1") + newBalance.subtract(account.getBalance()) + guiSettings.getPhrase("BALANCE_MIS2") + account );
					controller.setBalance(account, newBalance);
					center.addMsg( guiSettings.getPhrase("UPD_END1") + account + guiSettings.getPhrase("UPD_END2") );
					tList.addAll(temp);
				}
			}
			tList.sort( (t1,t2) -> t1.getPurpose().compareToIgnoreCase( t2.getPurpose() ) );
		} catch (BAMException e) {
			JOptionPane.showMessageDialog(center, 
					e.getErrorMessage() , 
					guiSettings.getPhrase("ERROR"), 
					JOptionPane.ERROR_MESSAGE);
		}
		SwingUtilities.invokeLater( () -> {
			update.setText( guiSettings.getPhrase("STORE_NEW") );
			update.removeActionListener(actionListener);
			update.addActionListener( e -> {
				if(!tList.isEmpty())
					controller.openStoreFrame(tList);
				controller.closeFrame(this);
			});
			update.setEnabled(true);
		});
	}

}

package bam.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import bam.controller.BAMController;
import bam.core.BAMAccount;
import bam.core.BAMModifyableListable;
import bam.core.BAMSubAccount;
import bam.core.BAMUser;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMUserTreeModel;

@SuppressWarnings("serial")
public class BAMSwingMainFrame extends BAMSwingFrame implements TreeSelectionListener{

	private BAMUser user;
	private BAMModifyableListable shownObject;
	
	private JPanel center = new JPanel( new GridLayout(1,1));
	private JPanel centerContent;
	private JTree west;
	
	public BAMSwingMainFrame( BAMUser user, BAMController controller ) {
		super( "BankAccountManager - " + user, controller);
		this.user = user;
		shownObject = user;
		addWindowListener( new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				controller.exit();
			}
		});
		drawAll();
		setComponents(null,west,center,null,null);
		draw();
	}

	@Override
	public void draw()
	{
		setJMenuBar( new BAMSwingMenuBar(controller,user) );
		super.draw();
	}
	
	@Override
	protected void drawWest()
	{
		west = new JTree( new BAMUserTreeModel(user) );
		for (int row = 0; row < west.getRowCount(); row++ )
			west.expandPath( west.getPathForRow(row));
		west.setMinimumSize( new Dimension(400, 600) );
		west.setBorder( BorderFactory.createLineBorder( Color.WHITE  ,10 ) );
		west.addTreeSelectionListener( this );
	}
	
	@Override
	protected void drawCenter( )
	{
		drawCenter( shownObject );
	}
	
	private void drawCenter( Object o )
	{
		if( centerContent != null )
			centerContent.setVisible( false );
		if(o instanceof BAMAccount)
			centerContent =  new BAMSwingAccountPanel( (BAMAccount)o, user, controller) ;	
		if(o instanceof BAMSubAccount)
			centerContent = new BAMSwingSubAccountPanel( (BAMSubAccount)o, controller) ;	
		if(o instanceof BAMUser)
			centerContent = new BAMSwingUserPanel( user, controller ) ;
		centerContent.setPreferredSize( new Dimension(900,600) );
		
		center.setVisible(false);
		center.removeAll();		
		center.add(centerContent);
		center.setBorder( guiSettings.getBorder() );
		center.setVisible(true);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		shownObject = (BAMModifyableListable) e.getNewLeadSelectionPath().getLastPathComponent();
		drawCenter( );
	}
	
}

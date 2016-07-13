package bam.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.settings.BAMGraphics;
import bam.gui.tools.BAMSwingFrame;
import bam.gui.tools.BAMUserTreeModel;
import bam.gui.tools.BAMUserTreeModel.BAMListableList;

@SuppressWarnings("serial")
public class BAMSwingMainFrame extends BAMSwingFrame {

	private BAMUser user;
	private Object shownObject;
	
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
		draw();
	}

	@Override
	public void draw()
	{
		setJMenuBar( new BAMSwingMenuBar(controller,user) );
		setComponents(null,west,center,null,null);
		super.draw();
	}
	
	@Override
	protected void drawWest()
	{
		west = new JTree( new BAMUserTreeModel(user) );
		for (int row = 0; row < west.getRowCount(); row++ )
			if( west.getPathForRow(row).getPathCount() < 3  )
				west.expandPath( west.getPathForRow(row));
		west.setMinimumSize( new Dimension(400, 600) );
		west.setBorder( BorderFactory.createLineBorder( Color.WHITE  ,10 ) );
		west.setCellRenderer( new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) 
			{
				Component renderer = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				if( value instanceof BAMUser )
					setIcon( BAMGraphics.getImageIcon( BAMGraphics.USER ) );
				if( value instanceof BAMAccount)
					setIcon( BAMGraphics.getImageIcon( BAMGraphics.ACCOUNT ) );
				if( value instanceof BAMSubAccount)
					setIcon( BAMGraphics.getImageIcon( BAMGraphics.SUBACCOUNT ) );
				if( value instanceof BAMListableList )
				{
					BAMListableList<?> list = (BAMListableList<?>) value;
					if( list.getType() == BAMListableList.PAYMENT )
						setIcon( BAMGraphics.getImageIcon( BAMGraphics.PAYMENT_LIST ) );
					else // if( list.getType() == BAMListableList.MULTIPAYMENT )
						setIcon( BAMGraphics.getImageIcon( BAMGraphics.MULTIPAYMENT_LIST ) );
				}
				if( value instanceof BAMPayment )
					setIcon( BAMGraphics.getImageIcon( BAMGraphics.PAYMENT ) );
				if( value instanceof BAMMultiPayment )
					setIcon( BAMGraphics.getImageIcon( BAMGraphics.MULTIPAYMENT ) );
				return renderer;
			}
		});
		west.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e) {
				TreePath path = west.getSelectionPath();
				if( path == null )
					return;
				Object o = path.getLastPathComponent();
				if( BAMGenericPayment.class.isAssignableFrom( o.getClass() ) )
				{
					if( e.getClickCount() > 1)
						controller.openPopup( (BAMGenericPayment) o );
				}
				else
				{
					shownObject = o;
					drawCenter( o );
				}
			}
		});
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
		if(o instanceof BAMListableList<?> )
		{
			BAMListableList<?> list = (BAMListableList<?>) o;
			if( list.getType() == BAMListableList.PAYMENT )
				centerContent = new BAMSwingSubAccountPanel( list.getParent(), controller ).setPaymentView();
			else // if( list.getType() == BAMListableList.MULTIPAYMENT )
				centerContent = new BAMSwingSubAccountPanel( list.getParent(), controller  ).setMultiPaymentView();
		}
		centerContent.setPreferredSize( new Dimension(900,600) );
		
		center.setVisible(false);
		center.removeAll();		
		center.add(centerContent);
		center.setBorder( guiSettings.getBorder() );
		center.setVisible(true);
	}
	
}

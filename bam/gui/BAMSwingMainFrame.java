package bam.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import bam.controller.BAMController;
import bam.core.*;
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
					setIcon( new ImageIcon( guiSettings.getIcon() ) );
				if( value instanceof BAMAccount)
					setIcon( guiSettings.getAccountIcon() );
				if( value instanceof BAMSubAccount)
					setIcon( guiSettings.getSubaccountIcon() );
				if( value instanceof BAMListableList )
				{
					BAMListableList<?> list = (BAMListableList<?>) value;
					if( list.getType() == BAMListableList.PAYMENT )
						setIcon( guiSettings.getPaymentListIcon() );
					else // if( list.getType() == BAMListableList.MULTIPAYMENT )
						setIcon( guiSettings.getMultipaymentListIcon() );
				}
				if( value instanceof BAMPayment )
					setIcon( guiSettings.getPaymentIcon() );
				if( value instanceof BAMMultiPayment )
					setIcon( guiSettings.getMultipaymentIcon() );
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
						controller.openPopup( (BAMListable) o );
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

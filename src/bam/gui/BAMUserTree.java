//package bam.gui;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.BorderFactory;
//import javax.swing.JTree;
//import javax.swing.tree.DefaultTreeCellRenderer;
//import javax.swing.tree.TreePath;
//
//import bam.core.BAMAccount;
//import bam.core.BAMGenericPayment;
//import bam.core.BAMMultiPayment;
//import bam.core.BAMPayment;
//import bam.core.BAMSubAccount;
//import bam.core.BAMUser;
//import bam.gui.settings.BAMGraphics;
//import bam.gui.tools.BAMUserTreeModel;
//import bam.gui.tools.BAMUserTreeModel.BAMListableList;
//
//public class BAMUserTree extends JTree
//{
//	public BAMUserTree(BAMUser user)
//	{
//		super( new BAMUserTreeModel(user) );
//		for (int row = 0; row < getRowCount(); row++ )
//			if( getPathForRow(row).getPathCount() < 3  )
//				expandPath( getPathForRow(row));
//		setMinimumSize( new Dimension(400, 600) );
//		setBorder( BorderFactory.createLineBorder( Color.WHITE  ,10 ) );
//		setCellRenderer( new DefaultTreeCellRenderer() {
//			@Override
//			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) 
//			{
//				Component renderer = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
//				if( value instanceof BAMUser )
//					setIcon( BAMGraphics.getImageIcon( BAMGraphics.USER ) );
//				if( value instanceof BAMAccount)
//					setIcon( BAMGraphics.getImageIcon( BAMGraphics.ACCOUNT ) );
//				if( value instanceof BAMSubAccount)
//					setIcon( BAMGraphics.getImageIcon( BAMGraphics.SUBACCOUNT ) );
//				if( value instanceof BAMListableList )
//				{
//					BAMListableList<?> list = (BAMListableList<?>) value;
//					if( list.getType() == BAMListableList.PAYMENT )
//						setIcon( BAMGraphics.getImageIcon( BAMGraphics.PAYMENT_LIST ) );
//					else // if( list.getType() == BAMListableList.MULTIPAYMENT )
//						setIcon( BAMGraphics.getImageIcon( BAMGraphics.MULTIPAYMENT_LIST ) );
//				}
//				if( value instanceof BAMPayment )
//					setIcon( BAMGraphics.getImageIcon( BAMGraphics.PAYMENT ) );
//				if( value instanceof BAMMultiPayment )
//					setIcon( BAMGraphics.getImageIcon( BAMGraphics.MULTIPAYMENT ) );
//				return renderer;
//			}
//		});
//		addMouseListener( new MouseAdapter() {
//			@Override
//			public void mouseClicked( MouseEvent e) {
//				TreePath path = getSelectionPath();
//				if( path == null )
//					return;
//				Object o = path.getLastPathComponent();
//				if( BAMGenericPayment.class.isAssignableFrom( o.getClass() ) )
//				{
//					if( e.getClickCount() > 1)
//						controller.openPopup( (BAMGenericPayment) o );
//				}
//				else
//				{
//					shownObject = o;
//					drawCenter( o );
//				}
//			}
//		});
//	}
//	
//}

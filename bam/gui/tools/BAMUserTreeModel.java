package bam.gui.tools;

import bam.core.*;
import bam.gui.settings.BAMGUISettings;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class BAMUserTreeModel implements TreeModel {
	
	public class BAMListableList <T extends BAMListable> {
		
		public static final int PAYMENT = 1;
		public static final int MULTIPAYMENT = 2;
		private String name;
		private BAMSubAccount parent;
		private List<T> tList;		
		private int type;
		
		@SuppressWarnings("unchecked")
		public BAMListableList( BAMSubAccount parent, int type ) {
			this.parent = parent;
			this.type = type;
			if( type == PAYMENT )
			{
				name = guiSettings.getPhrase("PAYMENTS");
				tList = (List<T>) parent.getPayments();
			}
			else // if( type == MULTIPAYMENT )
			{
				name = guiSettings.getPhrase("MULTIPAYMENTS");
				tList = (List<T>) parent.getMultiPayments();
			}
		}		
		
		@Override 
		public String toString() { return name; }
		public BAMSubAccount getParent() { return parent; }
		public int size() { return tList.size(); }
		public T get( int index ) { return tList.get(index); }
		public int getType() { return  type; }
	}
	
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	private BAMUser u;
	private boolean isTerminateAtSubaccounts = false;
	
	public BAMUserTreeModel( BAMUser u)
	{
		this.u = u;
	}

	@Override
	public Object getRoot() {
		return u;
	}
	
	public BAMUserTreeModel setTerminateAtSubaccounts( boolean isTerminateAtSubaccounts)
	{
		this.isTerminateAtSubaccounts = isTerminateAtSubaccounts;
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getChild(Object parent, int index) {
		if( parent == u )
			return u.getAccount(index);
		if( parent instanceof BAMAccount )
			return ((BAMAccount)parent).getSubAccount(index);
		if( parent instanceof BAMSubAccount )
		{
			BAMSubAccount sa = (BAMSubAccount) parent;
			if( index == 0 )
				return new BAMListableList<BAMPayment>( sa, BAMListableList.PAYMENT );
			else // if( index == 1 )
				return new BAMListableList<BAMMultiPayment>( sa, BAMListableList.MULTIPAYMENT );
		}
		if( parent instanceof BAMListableList )
			return ((BAMListableList) parent).get(index);
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if( parent == u )
			return u.getAccountCount();
		if( parent instanceof BAMAccount )
			return ((BAMAccount)parent).getSubAccountCount();
		if( parent instanceof BAMSubAccount && ! isTerminateAtSubaccounts )
			return 2;
		if( parent instanceof BAMListableList )
			return ((BAMListableList<?>) parent).size();
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if( isTerminateAtSubaccounts )
			return node instanceof BAMSubAccount;
		return BAMGenericPayment.class.isAssignableFrom( node.getClass() );
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {

	}

}

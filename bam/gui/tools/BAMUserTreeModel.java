package bam.gui.tools;

import bam.core.BAMAccount;
import bam.core.BAMSubAccount;
import bam.core.BAMUser;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class BAMUserTreeModel implements TreeModel {
	
	private BAMUser user;
	
	public BAMUserTreeModel( BAMUser user)
	{
		this.user = user;
	}

	@Override
	public Object getRoot() {
		return user;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if( parent == user )
			return user.getAccount(index);
		if( parent instanceof BAMAccount )
			return ((BAMAccount)parent).getSubAccount(index);
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if( parent == user )
			return user.getAccountCount();
		if( parent instanceof BAMAccount )
			return ((BAMAccount)parent).getSubAccountCount();
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof BAMSubAccount;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) { }

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) { }

}

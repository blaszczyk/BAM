package bam.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import bam.controller.BAMController;
import bam.core.BAMListable;
import bam.gui.BAMSwingPopupMenu;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;
import bam.tools.ButtonColumn;

@SuppressWarnings({"serial","unchecked"})
public class BAMListableTable<T extends BAMListable> extends JTable implements MouseListener{

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	private BAMController controller;
	
	
	@SuppressWarnings("hiding")
	private class Column<T>{
		private String key;
		private Action action;
		private int width = -1;
		
		public Column( String key ) 
		{
			this.key = key;
		}
		public Action getAction(){return action;}
		public void setAction(Action action){this.action = action;}
		public int getWidth(){return width;}
		public void setWidth(int width){this.width = width;}
		@SuppressWarnings("unused")
		public String getKey(){return key;}
	}

	private List<BAMListable> list;
	private String[] keys;
	private BAMListableTableModel tableModel;
	
	private Comparator<BAMListable> comparator;
	private boolean sortBackwards = false;
	
	private Column<T>[] columns;
	private boolean popupMenuEnabled = true;
	private Action doubleClickAction;

	public BAMListableTable( Iterable<T> container, BAMController controller, String... keys ) {
		this.keys = keys;
		this.controller = controller;
		
		columns = new Column[keys.length];
		for(int i = 0; i < keys.length; i++)
			columns[i] = new Column<T>(keys[i]);	
		
		list = new ArrayList<>();
		Iterator<T> iterator = container.iterator();
		while(iterator.hasNext())
			list.add(iterator.next());
		
		setModel( tableModel = new BAMListableTableModel(list,keys) );
		setRowHeight( guiSettings.getFont( BAMFontSet.TABLE).getSize() + 5 );
		getTableHeader().addMouseListener( this);		
		addMouseListener(this);
	}
	
	public void setButtonColumn( int columnIndex, Action action)
	{
		columns[columnIndex].setAction(action);
	}
	
	public void setDoubleClickAction( Action action )
	{
		doubleClickAction = action;
	}
	
	public void setPopupMenuEnabled( boolean popupMenuEnabled)
	{
		this.popupMenuEnabled = popupMenuEnabled;
	}


	public void setColumnWidths( int ...widths)
	{
		int maxIndex = Math.min( widths.length, columns.length);
		for(int i = 0; i < maxIndex; i++)
			columns[i].setWidth(widths[i]);
	}
	

	private void makeTableNumbersRed( int columnIndex )
	{
		getColumnModel().getColumn(columnIndex).setCellRenderer( new DefaultTableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if( ! ( tableModel.getTrueValueAt(row, column) instanceof BigDecimal) )
					return c;
				if( ( (BigDecimal) tableModel.getTrueValueAt(row, column) ).signum() < 0 )
					c.setForeground( Color.RED );
				else
					c.setForeground( Color.BLACK );
				return c;
			}
			
			@Override
			public void setHorizontalAlignment( int alignment )
			{
				super.setHorizontalAlignment( JLabel.RIGHT );
			}
		});
	}
	
	public void draw()
	{
		for( int i = 0; i < columns.length; i++)
		{
			Column<T> c = columns[i];
			if( c.getAction() != null )
			{
				Action click = new AbstractAction(){
					@Override
					public void actionPerformed(ActionEvent e) {
						ActionEvent ee = new ActionEvent( list.get( Integer.valueOf( e.getActionCommand() ) ), 1337, "openPopup");				
						c.getAction().actionPerformed( ee );
					}
				};
				new ButtonColumn( this, click, i ,guiSettings.getFont( BAMFontSet.SMALL ));
			}
			if( c.getWidth() >= 0 )
				getColumnModel().getColumn(i).setPreferredWidth(c.getWidth());
			if( tableModel.getTrueColumnClass(i).equals( BigDecimal.class ) )
				makeTableNumbersRed(i);
		}
	}

	/*
	 * Mouse Listener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getComponent() == this )
		{
			if( e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1  )
				if( getSelectedRow() >= 0 )
				{
					ActionEvent ee = new ActionEvent( list.get(  getSelectedRow() ), 1337, "openPopup");	
					doubleClickAction.actionPerformed(ee);
				}
		}
		else if( e.getComponent() == getTableHeader() )
		{
			int column = columnAtPoint(e.getPoint());
			Class<?> type =  tableModel.getColumnClass(column);
			if( Comparable.class.isAssignableFrom( type ) )
				if( ! tableModel.isCellEditable(0, column) )
				{
					sortBackwards = ! sortBackwards;
					String key = tableModel.getColumnKey(column);
					comparator = (l1, l2) -> 
							((Comparable<Object>) l1.getValue(key)).compareTo(l2.getValue(key)) * (sortBackwards ? -1 : 1);
					BAMListableTable.this.list.sort(comparator);
					BAMListableTable.this.setModel( tableModel = new BAMListableTableModel(list,keys) );
					draw();
				}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}
	private void doPopup(MouseEvent e)
	{
		if(!popupMenuEnabled)
			return;
		int row = rowAtPoint( e.getPoint() );
		JPopupMenu menu = new BAMSwingPopupMenu(list.get(row), controller);
		menu.show(this, e.getX(), e.getY());
	}
}

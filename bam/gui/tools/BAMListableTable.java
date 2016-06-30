package bam.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import bam.core.BAMListable;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;
import bam.tools.ButtonColumn;

@SuppressWarnings("serial")
public class BAMListableTable extends JTable {

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private List<? extends BAMListable> list;
	private BAMListableTableModel tableModel;
	private Comparator<BAMListable> comparator;
	private boolean sortBackwards = false;
	
	private class ColumnButton{
			private Action action;
			private int column;
			public ColumnButton( Action action, int column) {
				this.action = action;
				this.column = column;
			}		
			public Action getAction() { return action;	}
			public int getColumn() { return column; }
	}
	
	private int[] columnWidths;
	private List<ColumnButton> buttons = new ArrayList<>();
	private Action mouseAction;
	
	public BAMListableTable( List<? extends BAMListable> list, String... keys ) {
		super( new BAMListableTableModel(list,keys) );
		this.setModel( tableModel = new BAMListableTableModel(list,keys) );
		this.list = list;
		this.setRowHeight( guiSettings.getFont( BAMFontSet.TABLE).getSize() + 2 );
		this.getTableHeader().addMouseListener( new MouseAdapter(){
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
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
		}; }); 
	}
	
	public void setButtonColumn( int columnIndex, Action action)
	{
		buttons.add( new ColumnButton( action, columnIndex ) );
	}
	
	public void setButtonColumnMouse( int columnIndex, Action action)
	{
		setButtonColumn(columnIndex, action);
		mouseAction = action;
	}


	public void setColumnWidths( int ...widths)
	{
		columnWidths = widths;
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
		addMouseListener( new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1  )
					if( getSelectedRow() >= 0 )
					{
						ActionEvent ee = new ActionEvent( list.get(  getSelectedRow() ), 1337, "openPopup");	
						mouseAction.actionPerformed(ee);
					}
			}
		});
		for( ColumnButton b : buttons)
		{
			Action click = new AbstractAction(){
				@Override
				public void actionPerformed(ActionEvent e) {
					ActionEvent ee = new ActionEvent( list.get( Integer.valueOf( e.getActionCommand() ) ), 1337, "openPopup");				
					b.getAction().actionPerformed( ee );
				}
			};
			new ButtonColumn( this, click, b.getColumn() ,guiSettings.getFont( BAMFontSet.SMALL ));
		}
		for( int i = 0; i < columnWidths.length; i++)
			getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);	
		for( int i = 0; i < tableModel.getColumnCount(); i++)
			if( tableModel.getTrueColumnClass(i).equals( BigDecimal.class ) )
				makeTableNumbersRed(i);
	}
}

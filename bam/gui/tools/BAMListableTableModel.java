package bam.gui.tools;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bam.core.BAMListable;
import bam.gui.settings.BAMGUISettings;
import bam.tools.BAMUtils;

public class BAMListableTableModel implements TableModel {

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private List<? extends BAMListable> list;
	private List<String> keys;
	private BAMListable firstItem;
	private boolean isEmpty;
	
	public BAMListableTableModel(List<? extends BAMListable> list, String... keys) {
		this.list = list;
		isEmpty = list.isEmpty();
		if( !isEmpty )
			firstItem = list.get(0);
		this.keys = Arrays.asList(keys);
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return  keys.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		String key = keys.get( columnIndex );
		if( isEmpty || firstItem.containsKey( key ) )
			return guiSettings.getPhrase(key);
		return "";
	}

	public String getColumnKey(int columnIndex) {
		return keys.get(columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		String key = keys.get( columnIndex );
		if( isEmpty || !firstItem.containsKey(key) )
			return String.class;
		if( firstItem.getClass(key) == Date.class )
			return String.class;
		return firstItem.getClass(key);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(isEmpty)
			return false;
		return ! firstItem.containsKey( keys.get(columnIndex) );
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String key = keys.get(columnIndex);
		BAMListable item = list.get(rowIndex);
		if( !item.containsKey(key) )
			return guiSettings.getPhrase(key);
		if( item.getClass(key) == Date.class )
		{
			Date date = (Date) item.getValue(key);
			if( date.getTime() < 1 )
				return "";
			return " " + BAMUtils.DateDDMMYYYY( date ) + " ";
		}
		return item.getValue(key);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {	}

	@Override
	public void addTableModelListener(TableModelListener l) {	}

	@Override
	public void removeTableModelListener(TableModelListener l) {	}

}
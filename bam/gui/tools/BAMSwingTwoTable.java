package bam.gui.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import bam.core.BAMListable;
import bam.gui.settings.BAMGUISettings;
import bam.tools.BAMUtils;

@SuppressWarnings("serial")
public class BAMSwingTwoTable extends JPanel {

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private JPanel west;
	private JPanel center;
	private String leftAppend = "";
	private String middleAppend = "";
	private Font font = UIManager.getFont("Label.font");
	
	
	
	public BAMSwingTwoTable( int rowCount ) {
		west = new JPanel( new GridLayout( rowCount, 1) );
		center = new JPanel( new GridLayout( rowCount, 1) );
		setLayout( guiSettings.getBorderLayout() );
		add( west, BorderLayout.WEST );
		add( center, BorderLayout.CENTER );
	}
	
	public BAMSwingTwoTable( int rowCount, String north )
	{
		this(rowCount);
		add( new JLabel(north), BorderLayout.NORTH );
	}

	public void addRow( String left, String right )
	{
		JLabel tmp = new JLabel( right );
		tmp.setFont(font);
		addRow( left, tmp );
	}


	public void addRow( String left, Component right )
	{
		if( guiSettings.hasPhrase( left ) )
			left = guiSettings.getPhrase( left );
		JLabel tmp = new JLabel( leftAppend + left + middleAppend , SwingConstants.RIGHT );
		tmp.setFont(font);
		west.add(tmp);
		center.add( right );
	}
	
	public void addRow( String left, BigDecimal right, String append)
	{
		JLabel number = new JLabel( BAMUtils.toString(right) + append );
		number.setFont(font);
		if( right.signum() < 0 )
			number.setForeground( Color.RED );
		else
			number.setForeground( Color.BLACK );
		addRow( left, number );
	}
	
	public void addRow( String left, BigDecimal right)
	{
		this.addRow(left, right, "");
	}

	public void addValue( BAMListable listable, String key, String append )
	{
		if( !listable.containsKey(key) )
			return;
		if(listable.getClass(key).equals(BigDecimal.class) )
			addRow( key, (BigDecimal) listable.getValue(key), append );
		else
			addRow( key , listable.getValue(key).toString() + append );
	}
	
	public void addValue( BAMListable listable, String key )
	{
		addValue(listable, key, "");
	}
	
	public void addValues( BAMListable listable, String... keys)
	{
		for(int i = 0; i < keys.length; i++ )
			addValue( listable, keys[i]);
	}
	 
	@Override
	public void removeAll()
	{
		west.removeAll();
		center.removeAll();
	}


	public void setLeftAppend(String leftAppend) {
		this.leftAppend = leftAppend;
	}
	
	public void setMiddleAppend(String middleAppend) {
		this.middleAppend = middleAppend;
	}
	
	@Override
	public void setFont( Font font) {
		this.font = font;
	}
}

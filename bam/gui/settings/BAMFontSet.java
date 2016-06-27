package bam.gui.settings;

import java.awt.Font;

import javax.swing.UIManager;

public class BAMFontSet {
	
	public static final int BIG = 1;
	public static final int MEDIUM = 2; 
	public static final int SMALL = 3;
	public static final int TABLE = 4;
	
	
	private Font big;
	private Font medium;
	private Font small;
	private Font table;

	private BAMFontSet( Font big, Font medium, Font small, Font table)
	{
		this.big = big;
		this.medium = medium;
		this.small = small;
		this.table = table;
	}
	
	public Font getFont( int font )
	{
		switch( font )
		{
		case BIG:
			return big;
		case MEDIUM:
			return medium;
		case SMALL:
			return small;
		case TABLE:
			return table;
		}
		return null;
	}

	public final static BAMFontSet DEFAULT_SET = new BAMFontSet(
			UIManager.getDefaults().getFont( "JPanel.font"),
			UIManager.getDefaults().getFont( "JPanel.font"),
			UIManager.getDefaults().getFont( "JPanel.font"),
			UIManager.getDefaults().getFont( "JTable.font") );
	
	public final static BAMFontSet BIG_SET = new BAMFontSet( 
			new Font("Arial",Font.PLAIN,24), 
			new Font("Arial",Font.PLAIN,20), 
			new Font("Arial",Font.PLAIN,15),
			new Font("Arial", Font.PLAIN, 19) );
	
	public final static BAMFontSet MEDIUM_SET = new BAMFontSet( 
			new Font("Arial",Font.PLAIN,20), 
			new Font("Arial",Font.PLAIN,16), 
			new Font("Arial",Font.PLAIN,12),
			new Font("Arial", Font.PLAIN, 15) );
	
	public final static BAMFontSet SMALL_SET = new BAMFontSet( 
			new Font("Arial",Font.PLAIN,16), 
			new Font("Arial",Font.PLAIN,13), 
			new Font("Arial",Font.PLAIN,11),
			new Font("Arial", Font.PLAIN, 12) );
}

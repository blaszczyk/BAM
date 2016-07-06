package bam.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;

import bam.core.BAMModifyableListable;
import bam.tools.BAMException;

public class BAMGUISettings extends BAMModifyableListable {

	public static final String ICON = "ICON";
	public static final String FONT = "FONT";
	public static final String LOCALE = "LOCALE";
	
	private static Map<String,Class<?>> guiSettingsClassMap;
	
	public static BufferedImage ICON1;	
	public static BufferedImage ICON2;
	public static BufferedImage ICON3;
	public static List<BufferedImage> icons;
	private ImageIcon paymentIcon;
	private ImageIcon multipaymentIcon;
	private ImageIcon paymentListIcon;
	private ImageIcon multipaymentListIcon;
	private ImageIcon accountIcon;
	private ImageIcon subaccountIcon;
	
	public static List<BAMFontSet> fontsets;

	public static List<Locale> locales;
	
	private BufferedImage icon;		
	private BAMLanguage language;
	private BAMFontSet fontSet;
	private Locale locale;
	private static BAMGUISettings instance;
	private Border border;
	
	public static BAMGUISettings getInstance()
	{
		if( instance == null)
			instance = new BAMGUISettings();
		return instance;
	}
	
	private BAMGUISettings () {
		try {
			ICON1 = ImageIO.read(new File("data/icon1.bmp"));
			ICON2 = ImageIO.read(new File("data/icon2.bmp"));
			ICON3 = ImageIO.read(new File("data/icon3.bmp"));
			paymentIcon = new ImageIcon( ImageIO.read(new File("data/payment.png")) );
			multipaymentIcon = new ImageIcon( ImageIO.read(new File("data/multipayment.png")) );
			paymentListIcon = new ImageIcon( ImageIO.read(new File("data/paymentlist.png")) );
			multipaymentListIcon = new ImageIcon( ImageIO.read(new File("data/multipaymentlist.png")) );
			accountIcon = new ImageIcon( ImageIO.read(new File("data/account.png")) );
			subaccountIcon = new ImageIcon( ImageIO.read(new File("data/subaccount.png")) );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedImage[] iconArray = {ICON1,ICON2,ICON3}; 
		icons = Arrays.asList( iconArray );

		BAMFontSet[] fontsetsArray = {BAMFontSet.SMALL_SET, BAMFontSet.MEDIUM_SET, BAMFontSet.BIG_SET};
		fontsets = Arrays.asList( fontsetsArray );

		Locale[] localesArray = { Locale.GERMAN, Locale.ENGLISH };
		locales = Arrays.asList( localesArray );
		
		border = BorderFactory.createLineBorder( new Color(0,0,0,0) , 5 );

//		icon = ICON2;
//		setFontSet( BAMFontSet.MEDIUM_SET );
//		setLocale( Locale.GERMAN );
	}
	
	public BufferedImage getIcon()
	{
		return icon;
	}
	
	public ImageIcon getPaymentIcon() {
		return paymentIcon;
	}

	public ImageIcon getMultipaymentIcon() {
		return multipaymentIcon;
	}

	public ImageIcon getPaymentListIcon() {
		return paymentListIcon;
	}

	public ImageIcon getMultipaymentListIcon() {
		return multipaymentListIcon;
	}

	public ImageIcon getAccountIcon() {
		return accountIcon;
	}

	public ImageIcon getSubaccountIcon() {
		return subaccountIcon;
	}

	public Border getBorder()
	{
		return border;
	}

	public LayoutManager getBorderLayout() {
		return new BorderLayout(5,5);
	}

	public String getPhrase( String key )
	{
		return language.getPhrase(key);
	}

	public boolean hasPhrase(String key) {
		return language.hasPhrase(key);
	}
	
	public void loadLanguage( Locale locale )
	{
		try {
			language = new BAMLanguage( locale );
		} catch (BAMException e) {			
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					e.getErrorMessage(), 
					"Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setFontSet( BAMFontSet fontSet )
	{
		this.fontSet = fontSet;
		UIManager.put( "Table.font", fontSet.getFont( BAMFontSet.TABLE ) );
		UIManager.put( "TableHeader.font", fontSet.getFont( BAMFontSet.TABLE ) );
		UIManager.put( "PopupMenu.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "Label.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "Button.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "Tree.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "RadioButton.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "RadioButtonMenuItem.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "TextField.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "TextPane.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "OptionPane.messageFont", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "ObtionPane.buttonFont", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "JTree.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "CheckBox.font", fontSet.getFont(BAMFontSet.MEDIUM) );
		UIManager.put( "MenuBar.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put( "Menu.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put( "MenuItem.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put( "List.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put( "ComboBox.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put( "TabbedPane.font", fontSet.getFont(BAMFontSet.MEDIUM));
		
	}
	
	public Font getFont( int font )
	{
		return fontSet.getFont(font);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		if( this.locale == locale )
			return;
		this.locale = locale;
		loadLanguage(locale);
	}

	public BAMFontSet getFontSet() {
		return fontSet;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
	public void modifyAll()
	{
		fireModifiedEvents();
	}
	
	@Override
	public Object getValue( String key ) {
		switch( key )
		{
		case ICON:
			return "" + icons.indexOf(icon);
		case FONT:
			return "" + fontsets.indexOf(fontSet);
		case LOCALE:
			return "" + locales.indexOf(locale);
		}
		return null;
	}
	
	@Override
	public void setValue( String key, Object o )
	{
		int index = Integer.parseInt( (String) o );
		switch( key )
		{
		case ICON:
			setIcon( icons.get(index) );
			return;
		case FONT:
			setFontSet( fontsets.get(index) );
			return;
		case LOCALE:
			setLocale( locales.get(index) );
			return;
		}
	}

	@Override
	public boolean isList(String key) {
		return false;
	}

	@Override
	protected Map<String, Class<?>> getClassMap() {
		return guiSettingsClassMap;
	}

	@Override
	protected void createClassMap() {
		if(guiSettingsClassMap == null)
		{
			guiSettingsClassMap = new HashMap<>();
			guiSettingsClassMap.put(ICON, String.class);
			guiSettingsClassMap.put(FONT, String.class);
			guiSettingsClassMap.put(LOCALE, String.class);
		}
	}

}
package bam.gui.settings;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import bam.core.BAMModifyableListable;
import bam.tools.BAMException;

public class BAMGUISettings extends BAMModifyableListable
{

	public static final String ICON = "ICON";
	public static final String FONT = "FONT";
	public static final String LOCALE = "LOCALE";

	private static Map<String, Class<?>> guiSettingsClassMap;

	public static List<BAMFontSet> fontsets;
	public static List<Locale> locales;

	private BAMLanguage language;
	private String icon;
	private BAMFontSet fontSet;
	private Locale locale;
	private Border border;

	private static BAMGUISettings instance;

	public static BAMGUISettings getInstance()
	{
		if (instance == null)
			instance = new BAMGUISettings();
		return instance;
	}

	private BAMGUISettings()
	{
		try
		{
			UIManager.setLookAndFeel( new NimbusLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		BAMFontSet[] fontsetsArray = { BAMFontSet.SMALL_SET, BAMFontSet.MEDIUM_SET, BAMFontSet.BIG_SET };
		fontsets = Arrays.asList(fontsetsArray);

		Locale[] localesArray = { Locale.GERMAN, Locale.ENGLISH };
		locales = Arrays.asList(localesArray);

		border = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5);

	}

	public BufferedImage getIcon()
	{
		return BAMGraphics.getImage((String) getValue(ICON));
	}

	public Border getBorder()
	{
		return border;
	}

	public LayoutManager getBorderLayout()
	{
		return new BorderLayout(5, 5);
	}

	public String getPhrase(String key)
	{
		return language.getPhrase(key);
	}

	public boolean hasPhrase(String key)
	{
		return language.hasPhrase(key);
	}

	public void loadLanguage(Locale locale)
	{
		try
		{
			language = new BAMLanguage(locale);
		}
		catch (BAMException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getErrorMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setFontSet(BAMFontSet fontSet)
	{
		this.fontSet = fontSet;
		UIManager.put("Table.font", fontSet.getFont(BAMFontSet.TABLE));
		UIManager.put("TableHeader.font", fontSet.getFont(BAMFontSet.TABLE));
		UIManager.put("PopupMenu.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("Label.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("Button.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("Tree.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("RadioButton.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("RadioButtonMenuItem.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("TextField.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("TextPane.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("OptionPane.messageFont", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("ObtionPane.buttonFont", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("JTree.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("CheckBox.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("MenuBar.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("Menu.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("MenuItem.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("List.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("ComboBox.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("TabbedPane.font", fontSet.getFont(BAMFontSet.MEDIUM));
		UIManager.put("PopupMenu.font", fontSet.getFont(BAMFontSet.MEDIUM));
	}

	public Font getFont(int font)
	{
		return fontSet.getFont(font);
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		if (this.locale == locale)
			return;
		this.locale = locale;
		loadLanguage(locale);
	}

	public BAMFontSet getFontSet()
	{
		return fontSet;
	}

	public String getIconFile()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public void modifyAll()
	{
		fireModifiedEvents();
	}

	@Override
	public Object getValue(String key)
	{
		switch (key)
		{
		case ICON:
			return icon;
		case FONT:
			return "" + fontsets.indexOf(fontSet);
		case LOCALE:
			return "" + locales.indexOf(locale);
		}
		return null;
	}

	@Override
	public void setValue(String key, Object o)
	{
		String value = (String) o;
		switch (key)
		{
		case ICON:
			setIcon(value);
			return;
		case FONT:
			setFontSet(fontsets.get(Integer.parseInt(value)));
			return;
		case LOCALE:
			setLocale(locales.get(Integer.parseInt(value)));
			return;
		}
	}

	@Override
	public boolean isList(String key)
	{
		return false;
	}

	@Override
	protected Map<String, Class<?>> getClassMap()
	{
		return guiSettingsClassMap;
	}

	@Override
	protected void createClassMap()
	{
		if (guiSettingsClassMap == null)
		{
			guiSettingsClassMap = new HashMap<>();
			guiSettingsClassMap.put(ICON, String.class);
			guiSettingsClassMap.put(FONT, String.class);
			guiSettingsClassMap.put(LOCALE, String.class);
		}
	}

}
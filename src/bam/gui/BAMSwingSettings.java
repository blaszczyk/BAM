package bam.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.*;

import bam.controller.BAMController;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGraphics;
import bam.gui.tools.BAMIconRadioButton;
import bam.gui.tools.BAMSwingFrame;

@SuppressWarnings("serial")
public class BAMSwingSettings extends BAMSwingFrame {

	private JPanel center = new JPanel();
	private JPanel south = new JPanel(); 
	
	private Locale locale = guiSettings.getLocale();
	private String iconFile = guiSettings.getIconFile();
	private BAMFontSet fontSet = guiSettings.getFontSet();
	
	ButtonGroup languageGroup = new ButtonGroup();
	ButtonGroup iconGroup = new ButtonGroup();
	ButtonGroup fontGroup = new ButtonGroup();
	
	JPanel languagePanel = new JPanel( new GridLayout(3,1) );
	JPanel iconPanel = new JPanel( new GridLayout(4,1) );
	JPanel fontPanel = new JPanel( new GridLayout(4,1) );
	
	public BAMSwingSettings(BAMController controller) {
		super("SETTINGS", controller);
		setMinimumSize( new Dimension(400,100 ));
		drawAll();
		setComponents(null,null,center,null,south);
		draw();
		
	}

	@Override
	protected void drawCenter()
	{
		/*
		 *  Language Selection
		 */
		languagePanel.add( new JLabel( guiSettings.getPhrase("LANGUAGE") + ":" ) );
		createLanguageButton("United-Kingdom-icon.png", Locale.ENGLISH );
		createLanguageButton("Germany-icon.png", Locale.GERMAN);
		
		/*
		 *  Font Size Selection
		 */
		fontPanel.add( new JLabel( guiSettings.getPhrase("FONT_SIZE") + ":" ) );
		createFontSetButton("SMALL", BAMFontSet.SMALL_SET);
		createFontSetButton("MEDIUM", BAMFontSet.MEDIUM_SET);
		createFontSetButton("BIG", BAMFontSet.BIG_SET);
		
		/*
		 * Icon Selection
		 */
		iconPanel.add( new JLabel( " " + guiSettings.getPhrase("ICON") + ": " ) ); //, SwingConstants.RIGHT ) );
		createIconButton( BAMGraphics.ICON1 );
		createIconButton( BAMGraphics.ICON2 );
		createIconButton( BAMGraphics.ICON3 );
		
		center.setVisible(false);
		center.removeAll();
		center.setLayout( new GridLayout(1,3));
		center.add(languagePanel);
		center.add(fontPanel);
		center.add(iconPanel);
		center.setVisible(true);
	}
	
	@Override
	protected void drawSouth()
	{
		JButton apply = new JButton( guiSettings.getPhrase("APPLY") );
		apply.addActionListener( e -> {
			controller.setGUISettings(locale, fontSet, iconFile);
		});
		
		JButton save = new JButton( guiSettings.getPhrase("SAVE") );
		save.addActionListener( e -> {
			controller.setGUISettings( locale, fontSet, iconFile );
			controller.saveGUISettings();
			controller.closeFrame( this );
		});
		
		JButton cancel = new JButton( guiSettings.getPhrase("CANCEL") );
		cancel.addActionListener( e -> {
			controller.closeFrame( this );
		});
		
		south.setVisible(false);
		south.removeAll();
		south.setLayout( new GridLayout(1,3) );
		south.add(save);
		south.add(apply);
		south.add(cancel);
		south.setVisible(true);
	}
	
	private JRadioButton createFontSetButton( String label, BAMFontSet fontSet)
	{
		JRadioButton button = new JRadioButton( guiSettings.getPhrase(label), this.fontSet == fontSet );
		button.setFont(fontSet.getFont( BAMFontSet.MEDIUM ));
		fontGroup.add(button);
		fontPanel.add(button);
		button.addActionListener( e -> {
			this.fontSet = fontSet;
		});
		
		return button;
	}
	
	private BAMIconRadioButton createLanguageButton( String iconFile, Locale locale)
	{
		BAMIconRadioButton button = new BAMIconRadioButton( iconFile, this.locale == locale );
		languageGroup.add(button.getRadioButton());
		languagePanel.add(button);
		button.addActionListener( e -> {
			this.locale = locale;
		});
		return button;
	}
	
	private BAMIconRadioButton createIconButton( String iconFile )
	{
		BAMIconRadioButton button = new BAMIconRadioButton( iconFile, this.iconFile.equals(iconFile ) );
		iconGroup.add(button.getRadioButton());
		iconPanel.add(button);
		button.addActionListener( e -> {
			this.iconFile  = iconFile;		
		});
		return button;
	}
}


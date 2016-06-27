package bam.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Locale;

import javax.swing.*;

import bam.controller.BAMController;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMIconRadioButton;
import bam.gui.tools.BAMSwingFrame;

@SuppressWarnings("serial")
public class BAMSwingSettings extends BAMSwingFrame {

	private JPanel center = new JPanel();
	private JPanel south = new JPanel(); 
	
	private Locale locale;
	private BAMFontSet fontSet;
	private BufferedImage icon;
	
	public BAMSwingSettings(BAMController controller) {
		super("SETTINGS", controller);
		locale = guiSettings.getLocale();
		fontSet = guiSettings.getFontSet();
		icon = guiSettings.getIcon();
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
		JRadioButton english = new JRadioButton("english", locale == Locale.ENGLISH );
		english.addActionListener( e -> { 
			locale = Locale.ENGLISH;
		});
		JRadioButton german = new JRadioButton("deutsch", locale == Locale.GERMAN );
		german.addActionListener( e -> {
			locale = Locale.GERMAN;
		});
		ButtonGroup languages = new ButtonGroup();
		languages.add(english);
		languages.add(german);
		
		JPanel languagePanel = new JPanel( new GridLayout(3,1) );
		languagePanel.add( new JLabel( guiSettings.getPhrase("LANGUAGE") + ":" ) );
		languagePanel.add(english);
		languagePanel.add(german);

		/*
		 *  Font Size Selection
		 */
		JRadioButton smallFont = new JRadioButton( guiSettings.getPhrase("SMALL"), fontSet == BAMFontSet.SMALL_SET );
		smallFont.addActionListener( e -> {
			fontSet = BAMFontSet.SMALL_SET;
		});
		JRadioButton mediumFont = new JRadioButton( guiSettings.getPhrase("MEDIUM"), fontSet == BAMFontSet.MEDIUM_SET );
		mediumFont.addActionListener( e -> {
			fontSet = BAMFontSet.MEDIUM_SET;
		});
		JRadioButton bigFont = new JRadioButton( guiSettings.getPhrase("BIG"), fontSet == BAMFontSet.BIG_SET );
		bigFont.addActionListener( e -> {
			fontSet = BAMFontSet.BIG_SET;
		});
		
		ButtonGroup fonts = new ButtonGroup();
		fonts.add(smallFont);
		fonts.add(mediumFont);
		fonts.add(bigFont);
		
		JPanel fontPanel = new JPanel( new GridLayout(4,1) );
		fontPanel.add( new JLabel( guiSettings.getPhrase("FONT_SIZE") + ":" ) );
		fontPanel.add(smallFont);
		fontPanel.add(mediumFont);
		fontPanel.add(bigFont);
		
		/*
		 * Icon Selection
		 */

		BAMIconRadioButton icon1 = new BAMIconRadioButton( BAMGUISettings.ICON1 , icon );
		icon1.addActionListener( e -> {
			icon = BAMGUISettings.ICON1; 			
		});
		BAMIconRadioButton icon2 = new BAMIconRadioButton( BAMGUISettings.ICON2 , icon );
		icon2.addActionListener( e -> {
			icon = BAMGUISettings.ICON2;
		});
		BAMIconRadioButton icon3 = new BAMIconRadioButton( BAMGUISettings.ICON3 , icon );
		icon3.addActionListener( e -> {
			icon = BAMGUISettings.ICON3;
		});
		
		ButtonGroup icons = new ButtonGroup();
		icons.add(icon1.getRadioButton());
		icons.add(icon2.getRadioButton());
		icons.add(icon3.getRadioButton());

		JPanel iconPanel = new JPanel( new GridLayout(4,1) );
		iconPanel.add( new JLabel( " " + guiSettings.getPhrase("ICON") + ": " ) ); //, SwingConstants.RIGHT ) );
		iconPanel.add(icon1);
		iconPanel.add(icon2);
		iconPanel.add(icon3);
		
		
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
			controller.setGUISettings(locale, fontSet, icon);
		});
		
		JButton save = new JButton( guiSettings.getPhrase("SAVE") );
		save.addActionListener( e -> {
			controller.setGUISettings( locale, fontSet, icon );
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
}


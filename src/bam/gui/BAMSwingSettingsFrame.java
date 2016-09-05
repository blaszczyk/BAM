package bam.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.*;

import bam.controller.BAMController;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGraphics;
import bam.gui.tools.BAMButtonGroupSelector;
import bam.gui.tools.BAMSwingFrame;

@SuppressWarnings("serial")
public class BAMSwingSettingsFrame extends BAMSwingFrame {

	private JPanel center = new JPanel();
	private JPanel south = new JPanel(); 
	
	private BAMButtonGroupSelector<Locale> languageSelector;
	private BAMButtonGroupSelector<BAMFontSet> fontSelector;
	private BAMButtonGroupSelector<String> iconSelector;
	
	public BAMSwingSettingsFrame(BAMController controller) {
		super("SETTINGS", controller);
		
		languageSelector = new BAMButtonGroupSelector<>("LANGUAGE", guiSettings.getLocale() );
		languageSelector.addIconButton("english.png", Locale.ENGLISH );
		languageSelector.addIconButton("deutsch.png", Locale.GERMAN);

		
		fontSelector = new BAMButtonGroupSelector<>("FONT_SIZE", guiSettings.getFontSet());
		fontSelector.addFontButton("SMALL", BAMFontSet.SMALL_SET );
		fontSelector.addFontButton("MEDIUM", BAMFontSet.MEDIUM_SET);
		fontSelector.addFontButton("BIG", BAMFontSet.BIG_SET);
		
		iconSelector = new BAMButtonGroupSelector<>("ICON", guiSettings.getIconFile());
		iconSelector.addIconButton( BAMGraphics.ICON1 );
		iconSelector.addIconButton( BAMGraphics.ICON2 );
		iconSelector.addIconButton( BAMGraphics.ICON3 );
		
		setMinimumSize( new Dimension(400,100 ));
		drawAll();
		setComponents(null,null,center,null,south);
		draw();
		
	}

	@Override
	protected void drawCenter()
	{	
		languageSelector.redraw();
		fontSelector.redraw();
		iconSelector.redraw();
		
		center.setVisible(false);
		center.removeAll();
		center.setLayout( new GridLayout(3,1));
//		center.setLayout( new BoxLayout(center,BoxLayout.PAGE_AXIS) );
		center.add(languageSelector);
		center.add(fontSelector);
		center.add(iconSelector);
		center.setVisible(true);
	}
	
	@Override
	protected void drawSouth()
	{
		JButton apply = new JButton( guiSettings.getPhrase("APPLY") );
		apply.addActionListener( e -> {
			Locale locale = languageSelector.getSelectedObject();
			BAMFontSet fontSet =  fontSelector.getSelectedObject();
			String iconFile = iconSelector.getSelectedObject();
			controller.setGUISettings(locale, fontSet, iconFile);
		});
		
		JButton save = new JButton( guiSettings.getPhrase("SAVE") );
		save.addActionListener( e -> {
			Locale locale = languageSelector.getSelectedObject();
			BAMFontSet fontSet = fontSelector.getSelectedObject();
			String iconFile = iconSelector.getSelectedObject();
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
}


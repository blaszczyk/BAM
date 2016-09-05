package bam.gui.tools;

import java.awt.Font;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;

@SuppressWarnings({"serial","unchecked"})
public class BAMButtonGroupSelector<Type> extends JPanel
{
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	/*
	 * Encodes Type(Icon/Text) and Properties of Button
	 */
	private class ButtonProperties{
		private String text;
		private boolean isIcon;
		private Type representedObject;
		private Font font;
		
		public ButtonProperties(String text, Type representedObject, Font font){
			this.text = text;
			this.representedObject = representedObject;
			this.font = font;
			isIcon = false;
		}
		public ButtonProperties(String iconFile, Type representedObject){
			this.text = iconFile;
			this.representedObject = representedObject;
			isIcon = true;
		}
		
		public String getText(){return text;}
		public boolean isIcon(){return isIcon;}
		public Type getRepresentedObject(){return representedObject;}
		public Font getFont(){return font;}
	}
	
	/*
	 * ButtonGroup Properties
	 */
	private String name;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private Type selectedObject;
	private List<ButtonProperties> buttonProperties = new ArrayList<>();
	
	/*
	 * Constructor
	 */
	public BAMButtonGroupSelector(String name,  Type selectedObject)
	{
		this.name = name;
		this.selectedObject = selectedObject;
	}

	/*
	 * Getter for selected Object
	 */
	public Type getSelectedObject()
	{
		return selectedObject;
	}
	
	/*
	 * Adders for different types of Buttons
	 */
	public void addTextButton(String text, Type representedObject, Font font)
	{
		buttonProperties.add( new ButtonProperties(text, representedObject, font));
	}

	public void addIconButton(String iconFile, Type representedObject)
	{
		buttonProperties.add( new ButtonProperties(iconFile, representedObject));
	}
	
	public void addIconButton(String iconFile)
	{
		buttonProperties.add( new ButtonProperties(iconFile, (Type)iconFile));
	}
	
	public void addFontButton(String text, BAMFontSet fontSet)
	{
		buttonProperties.add( new ButtonProperties(text,(Type)fontSet,fontSet.getFont( BAMFontSet.MEDIUM)));
	}

	/*
	 * draw Methods
	 */
	private void drawButton( ButtonProperties properties )
	{
		if(properties.isIcon())
		{
			BAMIconRadioButton iconButton = new BAMIconRadioButton( properties.getText(), properties.getRepresentedObject().equals(selectedObject) );
			buttonGroup.add(iconButton.getRadioButton());
			add(iconButton);
			iconButton.addActionListener( e -> {
				selectedObject  = properties.getRepresentedObject();		
			});
		}
		else
		{
			JRadioButton radioButton = new JRadioButton( guiSettings.getPhrase(properties.getText()),  properties.getRepresentedObject().equals(selectedObject) );
			radioButton.setFont(properties.getFont());
			add(radioButton);
			buttonGroup.add(radioButton);
			radioButton.addActionListener( e -> {
				selectedObject  = properties.getRepresentedObject();	
			});
		}
	}
	
	public void redraw()
	{
		setVisible(false);
		removeAll();
		buttonGroup = new ButtonGroup();
		setLayout( new BoxLayout(this, BoxLayout.X_AXIS));
		JLabel label = new JLabel(guiSettings.getPhrase(name) + ": ");
		label.setFont(guiSettings.getFont(BAMFontSet.MEDIUM));
		add(label);
		for( ButtonProperties p : buttonProperties)
			drawButton(p);
		setVisible(true);
	}
}

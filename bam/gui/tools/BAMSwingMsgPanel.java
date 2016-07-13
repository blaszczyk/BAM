package bam.gui.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;

import javax.swing.*;

import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;

@SuppressWarnings("serial")
public class BAMSwingMsgPanel extends JPanel{

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private JPanel gridPanel;
	private List<JLabel> labels;
	private Font font = guiSettings.getFont( BAMFontSet.MEDIUM );

	public BAMSwingMsgPanel()
	{
		setLayout( guiSettings.getBorderLayout() );
		gridPanel = new JPanel();
		add(gridPanel, BorderLayout.NORTH);
		labels = new ArrayList<JLabel>();
		gridPanel.setVisible(true);
		setBorder( guiSettings.getBorder() );
		setVisible(true);
	}
	
	public BAMSwingMsgPanel(String ...strings)
	{
		this();
		drawMsg( strings );
	}

	public void addMsg( String ...msg )
	{
		SwingUtilities.invokeLater( ()-> {drawMsg( msg ); } );
	}
	
	public void addMsg( Color textcolor, String ...msg)
	{
		SwingUtilities.invokeLater( ()-> {drawMsg( textcolor, msg ); } );
	}
	

	public void drawMsg( String ...msg )
	{
		drawMsg( Color.BLACK, msg);
	}
	
	public void drawMsg( Color textcolor, String ...msg )
	{
		for( String s : msg )
		{
			JLabel jl = new JLabel(s);
			jl.setForeground(textcolor);
			jl.setFont(font);
			labels.add(jl);
		}
		gridPanel.setVisible(false);
		gridPanel.removeAll();
		gridPanel.setLayout( new GridLayout( labels.size() ,1 ) );
		for( JLabel l : labels)
			gridPanel.add( l );		
		gridPanel.setVisible(true);
	}
	
	public void clear()
	{
		gridPanel.setVisible(false);
		labels.clear();
		gridPanel.removeAll();
		gridPanel.setVisible(true);
	}

	@Override
	public void setFont(Font font)
	{
		this.font = font;
	}
}

package bam.gui.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import bam.controller.BAMController;
import bam.core.BAMModifiedEvent;
import bam.core.BAMModifiedListener;
import bam.gui.settings.BAMGUISettings;

@SuppressWarnings("serial")
public abstract class BAMSwingFrame extends JFrame implements BAMModifiedListener {

	protected BAMController controller;
	protected static BAMGUISettings guiSettings = BAMGUISettings.getInstance();

	protected Component north;
	protected Component tree;
	protected Component center;
	protected Component east;
	protected Component south;

	private boolean firstDraw = true;
	
	private WindowListener closeMe = new WindowAdapter(){
		@Override
		public void windowClosing(WindowEvent e) {
			controller.closeFrame( BAMSwingFrame.this );
		}
	};
	
	public BAMSwingFrame( String title, BAMController controller)
	{
		this.controller = controller;
		super.addWindowListener( closeMe ); 
		setTitle( title );
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setIconImage( guiSettings.getIcon() );
		getRootPane().setBorder( guiSettings.getBorder() );
		setLayout( guiSettings.getBorderLayout() );
	}
	
	
	public BAMSwingFrame( BAMController controller )  
	{
		this("", controller);
	}

	@Override
	public void remove( Component comp )
	{
		if( comp != null)
			super.remove(comp);
	}
	
	@Override 
	public void setTitle( String title )
	{
		if( guiSettings.hasPhrase(title) )
			super.setTitle( "BAM - " + guiSettings.getPhrase(title) );
		else
			super.setTitle( "BAM - " + title);
	}
	
	protected void setComponents( Component north, Component west, Component center, Component east, Component south)
	{		
		remove(this.north);
		remove(this.tree);
		remove(this.center);
		remove(this.east);
		remove(this.south);
		
		this.north = north;
		this.tree= west;
		this.center = center;
		this.east = east;
		this.south = south;
		
		if( north != null )
			add( north , BorderLayout.NORTH );
		if( west != null )
			add( west , BorderLayout.WEST);
		if( center != null )
			add( center , BorderLayout.CENTER );
		if( east != null )
			add( east , BorderLayout.EAST );
		if( south != null )
			add( south , BorderLayout.SOUTH);
	}
	
	@Override
	public void addWindowListener( WindowListener l ){
		removeWindowListener(closeMe);
		super.addWindowListener(l);
	}
	
	@Override
	public void modified( BAMModifiedEvent e ){
		if(e.getModfiedInstance() == guiSettings)
		{
			setVisible(false);
			setIconImage( guiSettings.getIcon() );
			drawAll();
			draw();
			
		}
	}
	
	protected void drawNorth() {};
	protected void drawWest() {};
	protected void drawCenter() {};
	protected void drawEast() {};
	protected void drawSouth() {};
	
	protected void drawComponents( boolean drawNorth, boolean drawWest, boolean drawCenter, boolean drawEast,boolean drawSouth )
	{
		if( drawNorth )	
			drawNorth();
		if( drawWest )				
			drawWest();
		if( drawCenter )
			drawCenter();
		if( drawEast )
			drawEast();
		if( drawSouth )
			drawSouth();
		
	}
	
	protected void drawAll()
	{
		drawComponents(true,true,true,true,true);
	}

	protected void draw()
	{
		pack();
		if( firstDraw ) {
			setLocationRelativeTo(null);
			firstDraw = false;
		}
		setVisible(true);
	}
	
}
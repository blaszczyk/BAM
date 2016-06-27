package bam.controller;

import java.util.ArrayList;
import java.util.List;

import bam.gui.tools.BAMSwingFrame;

public class BAMDecoratorLogFrames extends BAMAbstractGUIDecorator{
	

	private List<BAMSwingFrame> frames = new ArrayList<>();
	
	public BAMDecoratorLogFrames(BAMGUIController controller) {
		super(controller);
	}

	@Override
	protected void onOpen(BAMSwingFrame frame) {
		frames.add(frame);	
	}

	@Override
	protected void onClose(BAMSwingFrame frame) {
		printFrames();
		int i = frames.indexOf(frame);
		if( i > -1 )
			frames.remove(i);
	}
	
	public void printFrames()
	{
		System.out.println( "Threre were " + frames.size() + " open Frames:");
		for(BAMSwingFrame frame : frames)
			System.out.println( frame.getClass().getName() + " - " + frame.getTitle() );
	}

}

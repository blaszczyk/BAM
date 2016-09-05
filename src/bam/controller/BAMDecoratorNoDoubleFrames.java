package bam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bam.core.BAMGenericPayment;
import bam.core.BAMTransaction;
import bam.core.BAMUser;
import bam.gui.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.BAMSwingFrame;

public class BAMDecoratorNoDoubleFrames extends BAMAbstractDecorator {

	private List<BAMSwingFrame> frames = new ArrayList<>();
		
	public BAMDecoratorNoDoubleFrames( BAMController controller ) {
		super(controller);
	}

	@Override
	public BAMSwingFrame openMainFrame() {
		BAMSwingFrame frame = findFrame( BAMSwingMainFrame.class);
		if( frame == null)
		{
			frame = controller.openMainFrame();
			frames.add(frame);
		}
		else
			frame.requestFocus();
		return frame;
	}

	@Override
	public BAMSwingFrame openPopup( BAMGenericPayment gPayment ) {
		BAMSwingFrame frame = controller.openPopup( gPayment ) ;
		frames.add(frame);
		return frame;
	}

	@Override
	public BAMSwingFrame openPopup(String transaction_id) {
		BAMSwingFrame frame = controller.openPopup( transaction_id ) ;
		frames.add(frame);
		return frame;
	}
		
	@Override
	public BAMSwingFrame openUpdateFrame() {
		BAMSwingFrame frame = findFrame( BAMSwingFigoFrame.class);
		if( frame == null)
		{
			frame = controller.openUpdateFrame();
			frames.add(frame);
		}
		else
			frame.requestFocus();
		return frame;
	}

	@Override
	public BAMSwingFrame openStoreFrame(BAMTransaction t) {
		BAMSwingFrame frame = findFrame( BAMSwingStoreTransactionFrame.class);
		if( frame == null)
		{
			frame = controller.openStoreFrame(t);
			frames.add(frame);
		}
		else
		{
			frame.requestFocus();
			((BAMSwingStoreTransactionFrame)frame).addTransaction(t);
		}
		return frame;
	}

	@Override
	public BAMSwingFrame openStoreFrame(List<BAMTransaction> tList) {
		BAMSwingFrame frame = findFrame( BAMSwingStoreTransactionFrame.class);
		if( frame == null)
		{
			frame = controller.openStoreFrame(tList);
			frames.add(frame);
		}
		else
		{
			frame.requestFocus();
			for( BAMTransaction t : tList)
				((BAMSwingStoreTransactionFrame)frame).addTransaction(t);
		}
		return frame;
	}

	@Override
	public BAMSwingFrame openEditFrame(BAMGenericPayment gPayment ) {
		BAMSwingFrame frame = controller.openEditFrame(gPayment);
		frames.add(frame);
		return frame;
	}
	
	@Override
	public BAMSwingFrame openSettings() {
		BAMSwingFrame frame = findFrame( BAMSwingSettingsFrame.class);
		if( frame == null)
		{
			frame = controller.openSettings();
			frames.add(frame);
		}
		else
			frame.requestFocus();
		return frame;
	}

	@Override
	public BAMSwingFrame openSetupUser(BAMUser user)  {
		BAMSwingFrame frame = findFrame( BAMSwingSetupUserFrame.class);
		if( frame == null)
		{
			frame = controller.openSetupUser(user);
			frames.add(frame);
		}
		else
			frame.requestFocus();
		return frame;
	}
	@Override
	public void closeFrame(BAMSwingFrame frame) {
		frames.remove(frame);
		controller.closeFrame(frame);
	}

	@Override
	public boolean setGUISettings(Locale locale, BAMFontSet fontSet, String iconFile) {
		return controller.setGUISettings(locale, fontSet, iconFile);
	}

	@Override
	public boolean loadGUISettings() {
		return controller.loadGUISettings();
	}

	@Override
	public boolean saveGUISettings() {
		return controller.saveGUISettings();
	}
	
	private BAMSwingFrame findFrame( Class<?> type ) {
		for( BAMSwingFrame f : frames )
			if( type.isInstance(f) )
				return f;
		return null;
	}

}

package bam.controller;

import java.util.List;
import java.util.Locale;

import bam.core.BAMListable;
import bam.core.BAMPayment;
import bam.core.BAMTransaction;
import bam.core.BAMUser;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.BAMSwingFrame;

public abstract class BAMAbstractGUIDecorator extends BAMAbstractDecorator {

		
	public BAMAbstractGUIDecorator( BAMController controller ) {
		super(controller);
	}

	@Override
	public BAMSwingFrame openMainFrame() {
		return doOnOpen( controller.openMainFrame() );
	}

	@Override
	public BAMSwingFrame openPopup( BAMListable listable ) {
		return doOnOpen( controller.openPopup( listable ) );
	}

	@Override
	public BAMSwingFrame openPopup(String transaction_id) {
		return doOnOpen( controller.openPopup(transaction_id) );
	}
		
	@Override
	public BAMSwingFrame openUpdateFrame() {
		return doOnOpen( controller.openUpdateFrame() );
	}

	@Override
	public BAMSwingFrame openStoreFrame(BAMTransaction t) {
		return doOnOpen( controller.openStoreFrame(t) );
	}

	@Override
	public BAMSwingFrame openStoreFrame(List<BAMTransaction> t) {
		return doOnOpen( controller.openStoreFrame(t) );
	}

	@Override
	public BAMSwingFrame openEditPayment(BAMPayment payment) {
		return doOnOpen( controller.openEditPayment(payment) );
	}
	
	@Override
	public BAMSwingFrame openSettings() {
		return doOnOpen( controller.openSettings() );
	}

	@Override
	public BAMSwingFrame openSetupUser(BAMUser user) {
		return doOnOpen( controller.openSetupUser(user) );
	}

	@Override
	public void closeFrame(BAMSwingFrame frame) {
		onClose( frame );
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
		
	private BAMSwingFrame doOnOpen( BAMSwingFrame frame)
	{
		onOpen(frame);
		return frame;
	}

	protected abstract void onOpen( BAMSwingFrame frame );
	protected abstract void onClose( BAMSwingFrame frame );
}

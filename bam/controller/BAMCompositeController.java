package bam.controller;

import java.util.List;
import java.util.Locale;

import bam.core.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.BAMSwingFrame;

public class BAMCompositeController extends BAMAbstractCoreDecorator implements BAMController {


	private BAMGUIController guiController;
	
	public BAMCompositeController( BAMCoreController coreController) {
		super( coreController );
	}
	
	
	public void setGUIController( BAMGUIController guiController ) {
		this.guiController = guiController;
	}	

// GUI Methods	

	@Override
	public BAMSwingFrame openMainFrame() {
		return guiController.openMainFrame();
	}
	@Override
	public BAMSwingFrame openUpdateFrame() {
		return guiController.openUpdateFrame();
	}

	@Override
	public BAMSwingFrame openPopup( BAMListable listable) {
		return guiController.openPopup( listable );
	}

	@Override
	public BAMSwingFrame openPopup(String transaction_id) {
		return guiController.openPopup(transaction_id);
	}

	@Override
	public BAMSwingFrame openStoreFrame(BAMTransaction t) {
		return guiController.openStoreFrame(t);
	}

	@Override
	public BAMSwingFrame openStoreFrame(List<BAMTransaction> t) {
		return guiController.openStoreFrame(t);
	}

	@Override
	public BAMSwingFrame openEditPayment(BAMPayment payment) {
		return guiController.openEditPayment(payment);
	}

	@Override
	public BAMSwingFrame openSettings() {
		return guiController.openSettings();
	}

	@Override
	public BAMSwingFrame openSetupUser(BAMUser user) {
		return guiController.openSetupUser(user);
	}
	
	@Override
	public void closeFrame(BAMSwingFrame frame) {
		guiController.closeFrame(frame);
	}

	@Override
	public boolean setGUISettings(Locale locale, BAMFontSet fontSet, String icon) {
		return guiController.setGUISettings(locale, fontSet, icon);		
	}

	@Override
	public boolean loadGUISettings() {
		return guiController.loadGUISettings();
	}


	@Override
	public boolean saveGUISettings() {
		return guiController.saveGUISettings();
	}
	
	
	@Override
	protected void ifChanged() { }

	@Override
	protected void ifNotChanged() {	}

	@Override
	protected void onLoadSaveSucess() {	}

	@Override
	protected void onLoadSaveFail() {}


}

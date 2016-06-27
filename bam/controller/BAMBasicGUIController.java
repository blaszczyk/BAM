package bam.controller;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;

import bam.core.*;
import bam.gui.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMSwingFrame;
import bam.tools.BAMException;
import bam.tools.BAMListableJson;

public class BAMBasicGUIController implements BAMGUIController {
	
	BAMUser user;
	BAMController controller;
	BAMGUISettings guiSettings = BAMGUISettings.getInstance();

	public BAMBasicGUIController( BAMUser user, BAMController controller) {
		this.user = user;
		this.controller = controller;
	}

	@Override
	public BAMSwingFrame openMainFrame() {
		return new BAMSwingMainFrame( user, controller );
	}

	@Override
	public BAMSwingFrame openUpdateFrame() {
		return new BAMSwingFigo( user, controller );
	}

	@Override
	public BAMSwingFrame openPopup( BAMListable listable ) {
		return new BAMSwingPopup( listable, controller );
	}

	@Override
	public BAMSwingFrame openPopup( String transaction_id )
	{
		if(user.containsTransaction( transaction_id) )
			return openPopup( user.getTransactionById(transaction_id) );
		return null;
	}

	@Override
	public BAMSwingFrame openStoreFrame(BAMTransaction t) {
		return new BAMSwingStoreTransaction( user, controller, t );		
	}

	@Override
	public BAMSwingFrame openStoreFrame(List<BAMTransaction> t) {
		return new BAMSwingStoreTransaction( user, controller, t );		
	}

	@Override
	public BAMSwingFrame openEditPayment( BAMPayment payment) {
		return new BAMSwingEditPayment(payment, user, controller);
	}

	@Override
	public BAMSwingFrame openSettings() {
		return new BAMSwingSettings( controller );
	}

	@Override
	public BAMSwingFrame openSetupUser(BAMUser user) {
		return new BAMSwingSetupUser( user, controller );
	}

	@Override
	public void closeFrame( BAMSwingFrame frame )
	{
		frame.setVisible( false );
		frame.dispose();
	}

	@Override
	public boolean setGUISettings(Locale locale, BAMFontSet fontSet, BufferedImage icon) {
		guiSettings.setLocale(locale);
		guiSettings.setFontSet(fontSet);
		guiSettings.setIcon(icon);
		guiSettings.modifyAll();
		return true;
	}

	@Override
	public boolean loadGUISettings() {
		try {
			BAMListableJson.loadListable(guiSettings, "data/settings.bam" );
		} catch (BAMException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveGUISettings() {
		try {
			BAMListableJson.saveListable(guiSettings, "data/settings.bam");
		} catch (BAMException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

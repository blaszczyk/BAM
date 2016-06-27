package bam.controller;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;

import bam.core.*;
import bam.gui.settings.BAMFontSet;
import bam.gui.tools.BAMSwingFrame;

public interface BAMGUIController {
	
	public BAMSwingFrame openMainFrame( );
	public BAMSwingFrame openPopup( BAMListable listable );
	public BAMSwingFrame openPopup( String transaction_id );
	public BAMSwingFrame openUpdateFrame();
	public BAMSwingFrame openStoreFrame( BAMTransaction t);
	public BAMSwingFrame openStoreFrame( List<BAMTransaction> t);
	public BAMSwingFrame openEditPayment( BAMPayment payment );
	public BAMSwingFrame openSettings();
	public BAMSwingFrame openSetupUser( BAMUser user );

	public void closeFrame( BAMSwingFrame frame );

	public boolean setGUISettings(Locale locale, BAMFontSet fontSet, BufferedImage icon);
	public boolean loadGUISettings();
	public boolean saveGUISettings();

}

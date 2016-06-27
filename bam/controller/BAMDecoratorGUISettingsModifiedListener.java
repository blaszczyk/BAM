package bam.controller;

import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.BAMSwingFrame;

public class BAMDecoratorGUISettingsModifiedListener extends BAMAbstractGUIDecorator {

	private BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	public BAMDecoratorGUISettingsModifiedListener(BAMGUIController controller) {
		super(controller);
	}

	@Override
	protected void onOpen(BAMSwingFrame frame) {
		guiSettings.addModifiedListener(frame);
	}

	@Override
	protected void onClose(BAMSwingFrame frame) {
		guiSettings.removeModifiedListener(frame);
	}
	
};
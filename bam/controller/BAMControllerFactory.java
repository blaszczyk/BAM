package bam.controller;

import bam.core.BAMUser;

public class BAMControllerFactory {

	public BAMControllerFactory() {
	}

	public static BAMController createController( BAMUser user, boolean hasConfirmMessages, boolean hasModifyUser, boolean hasGUISettingsModifiedListener )
	{
		BAMCoreController coreController = new BAMBasicCoreController( user );
		if( hasConfirmMessages )
			coreController = new BAMDecoratorConfirmMsg( user, coreController );
		if( hasModifyUser )
			coreController = new BAMDecoratorModifyUser( user, coreController );
		
		BAMCompositeController controller = new BAMCompositeController( coreController );
		
		BAMGUIController guiController = new BAMBasicGUIController(user, controller);
//		guiController = new BAMDecoratorLogFrames( guiController );
		if(hasGUISettingsModifiedListener)
			guiController = new BAMDecoratorGUISettingsModifiedListener( guiController );
			
		controller.setGUIController( guiController );
		return controller;
	}
}
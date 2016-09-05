package bam.controller;

import bam.core.BAMUser;

public class BAMControllerFactory {

	public BAMControllerFactory() {
	}

	public static BAMController createController( BAMUser user, boolean hasConfirmMessages, boolean hasModifyUser, boolean hasGUISettingsModifiedListener )
	{
		BAMBasicController basicController = new BAMBasicController( user );
		BAMController controller = basicController;
		if( hasConfirmMessages )
			controller = new BAMDecoratorConfirmMsg( user, controller );
		if( hasModifyUser )
			controller = new BAMDecoratorModifyUser( user, controller );
		if(hasGUISettingsModifiedListener)
			controller = new BAMDecoratorGUISettingsModifiedListener( controller );
		controller = new BAMDecoratorNoDoubleFrames(controller);
//		controller = new BAMDecoratorLogFrames( controller );
		basicController.setController( controller );
		return controller;
	}
}
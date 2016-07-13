package bam.controller;

import bam.core.*;

public class BAMDecoratorModifyUser extends BAMAbstractCoreDecorator {

	BAMUser user;
	
	public BAMDecoratorModifyUser( BAMUser user, BAMController controller) {
		super(controller);
		this.user = user;
	}

	@Override
	protected void ifChanged() {
		user.setModified(true);
	}

	@Override
	protected void ifNotChanged() {
	}

	@Override
	protected void onLoadSaveSucess() {
		user.setModified(false);
	}

	@Override
	protected void onLoadSaveFail() {
	}
	
}
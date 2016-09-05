package bam;

import bam.controller.*;
import bam.core.BAMUser;

public class BAM {
	public static void main(String[] args) {
		BAMUser user = new BAMUser("Müttinghoven");
		BAMController controller = BAMControllerFactory.createController(user, true, true, true);
		controller.loadGUISettings();
		controller.loadUser();
		controller.openMainFrame();
	}
}
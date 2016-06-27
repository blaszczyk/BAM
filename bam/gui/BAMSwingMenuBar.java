package bam.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import bam.controller.BAMController;
import bam.core.BAMUser;
import bam.gui.settings.BAMGUISettings;

@SuppressWarnings("serial")
public class BAMSwingMenuBar extends JMenuBar {

	BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	public BAMSwingMenuBar( BAMController controller, BAMUser user ) {
		
		JMenu mainMenu = new JMenu( guiSettings.getPhrase("FILE") );
		addMenuItem( mainMenu, "USER_SETUP", e -> {
			controller.openSetupUser(user);
		});
		addMenuItem( mainMenu, "SAVE_ACC", e -> {
			controller.saveUser();
		});
		addMenuItem( mainMenu, "ONLINE_ACC_UPD", e -> {
			controller.openUpdateFrame();
		});
		addMenuItem( mainMenu, "SETTINGS", e -> {
			controller.openSettings();
		});
		addMenuItem( mainMenu, "EXIT", e -> {
			controller.exit();
		});
		add( mainMenu );
	}
	
	private void addMenuItem( JMenu menu, String phrase, ActionListener actionListener )
	{
		JMenuItem menuItem = new JMenuItem( guiSettings.getPhrase(phrase) );
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
	}


}

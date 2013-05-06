package org.jboss.reddeer.eclipse.jface.preference;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.lookup.impl.ShellLookup;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * Represents a general preference page in the Preferences dialog. Subclasses
 * should represent the concrete preference page.
 * 
 * @author Lucia Jelinkova
 * 
 */
public abstract class PreferencePage {

	public static final String DIALOG_TITLE = "Preferences";

	private String[] path;
	protected final Logger log = Logger.getLogger(this.getClass());

	public PreferencePage(String... path) {
		this.path = path;
	}

	public void open() {

		// if preferences dialog is not open, open it
		log.info("Open Preferences dialog");
		
		boolean openedShell = false;
		for (Shell s: new ShellLookup().getShells()) {
			final Shell shell = s;
			String text = Display.syncExec(new ResultRunnable<String>() {
				@Override
				public String run() {
					return shell.getText();
				}
			});
			if (text.equals(DIALOG_TITLE)) {
				log.debug("Preferences dialog was already opened.");
				openedShell = true;
			}
		}
		if (!openedShell) {
			log.debug("Preferences dialog was not already opened. Opening via menu.");
			Menu menu = new ShellMenu("Window", "Preferences");
			menu.select();
		}
		
		new DefaultShell(DIALOG_TITLE);
		TreeItem t = new DefaultTreeItem(path);
		t.select();
		
	}

	public String getName() {
		DefaultCLabel cl = new DefaultCLabel();
		return cl.getText();
	}

	public void ok() {
		Button b = new PushButton("OK");
		log.info("Close Preferences dialog");
		b.click();
	}

	public void cancel() {
		Button b = new PushButton("Cancel");
		log.info("Cancel Preferences dialog");
		b.click();
	}

	public void apply() {
		Button b = new PushButton("Apply");
		log.info("Apply changes in Preferences dialog");
		b.click();
	}

	public void restoreDefaults() {
		Button b = new PushButton("Restore Defaults");
		log.info("Restore default values in Preferences dialog");
		b.click();
	}
	
}

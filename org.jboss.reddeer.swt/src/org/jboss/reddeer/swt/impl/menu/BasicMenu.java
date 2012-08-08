package org.jboss.reddeer.swt.impl.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.exception.WidgetNotAvailableException;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Abstract class for all Menu implementations
 * @author Jiri Peterka
 *
 */
public abstract class BasicMenu implements Menu {
	
	protected final Logger log = Logger.getLogger(this.getClass());
	
	SWTBotMenu menu;
	
	@Override
	public void select(String... items) {
		
		String current = "";
		try {
			log.info("Menu selection:");
			
			current = items[0];
			menu = Bot.get().menu(current);
			
			List<String> list = new ArrayList<String>(Arrays.asList(items));
			list.remove(0);
			String[] items2 = new String[items.length-1]; 
			list.toArray(items2);
			
			for (String item : items2) {
				current = item;
<<<<<<< HEAD
				menu = Bot.get().menu(item);
				log.debug(item + " -> ");
=======
				menu = menu.menu(item);
				log.info(item + " -> ");
>>>>>>> 7382172d6093b9d36af3c8bde9b4ac0e96a2f768
			}
			menu.click();		
			log.info("Last item clicked ");
		}
		catch (WidgetNotFoundException e) {
			
			String message =  "Menuitem " + current + " cannot be found,  " + e.getMessage(); 
			log.error(message);
			throw new WidgetNotAvailableException(message);
		}
	}
	
	@Override
	public void select() {
		if (menu != null) {
			menu.click();
		}		
	}
	
}

package org.jboss.reddeer.swt.impl.text;

import org.apache.log4j.Logger;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.handler.WidgetHandler;

/**
 * Abstract class for all Text implementations
 * @author Jiri Peterka
 *
 */
public abstract class AbstractText implements Text {
	
	protected org.eclipse.swt.widgets.Text w;
	protected final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void setText(String str) {
		log.info("Text set to: " + str);
		WidgetHandler.getInstance().setText(w, str);
	}
	
	
	@Override
	public String getText() {
		String text = WidgetHandler.getInstance().getText(w);
		return text;
	}
	
	
	@Override
	public String getToolTipText() {
		String tooltipText = WidgetHandler.getInstance().getToolTipText(w);
		return tooltipText;
	}
}

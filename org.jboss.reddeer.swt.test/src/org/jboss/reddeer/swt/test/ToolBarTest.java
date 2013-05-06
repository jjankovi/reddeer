package org.jboss.reddeer.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.ToolBar;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.ShellToolBar;
import org.jboss.reddeer.swt.impl.toolbar.ShellToolItem;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolBar;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.swt.impl.toolbar.WorkbenchToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.reddeer.swt.test.model.TestModel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for various toolbar implementations
 * @author Jiri Peterka
 *
 */
public class ToolBarTest extends RedDeerTest {

	@BeforeClass
	public static void prepare() {
			
		new WorkbenchShell();
		
		RegexMatchers m = new RegexMatchers("Window.*", "Show View.*",
				"Other...*");
		Menu menu = new ShellMenu(m.getMatchers());
		menu.select();
				
		TreeItem item = new DefaultTreeItem("RedDeer SWT","RedDeer SWT");
		item.select();
		new PushButton("OK").click();
		
		new WorkbenchShell();
		SWTBotView viewObject = Bot.getNew().viewByTitle("RedDeer SWT");
		viewObject.show();
		viewObject.setFocus();
	}
	
	@Test 
	public void workbenchToolBarTest() {
		
		ToolItem i = new WorkbenchToolItem("RedDeer SWT WorkbenchToolItem");
		i.click();
		assertTrue("ToolItem should be clicked", TestModel.getClickedAndReset());
	}
	
	@Test 
	public void workbenchToolBarRegexTest() {
		
		RegexMatcher rm = new RegexMatcher("RedDeer SWT Workbench.*");
		ToolItem i = new WorkbenchToolItem(rm);
		i.click();
		assertTrue("ToolItem should be clicked", TestModel.getClickedAndReset());
	}

	@Test
	public void testToolBar() {
		new WorkbenchShell();
		ToolBar t = new ViewToolBar();
		assertNotNull(t);
	}

	@Test
	public void testToolItemInViewToolBarFound() {
		ToolItem i = new ViewToolItem("RedDeer SWT ViewToolItem");
		assertEquals("RedDeer SWT ViewToolItem", i.getToolTipText());
	}

	@Test
	public void testToolItemInViewToolBarClicked() {
		ToolItem i = new ViewToolItem("RedDeer SWT ViewToolItem");
		i.click();		
		assertTrue("ToolItem should be clicked", TestModel.getClickedAndReset());		
	}

	@Test
	public void testToolItemInViewToolBarRegexClicked() {
		RegexMatcher rm = new RegexMatcher("RedDeer SWT View.*");
		ToolItem i = new ViewToolItem(rm);
		i.click();
		assertTrue("ToolItem should be clicked", TestModel.getClickedAndReset());		
	}
	
	@Test
	public void testShellToolBar() {
		openPreferences();
		ToolBar t = new ShellToolBar();
		assertNotNull(t);
		closePreferences();	
	}
	
	@Test
	public void testToolItemInShellToolBarFound() {
		openPreferences();
		ToolItem ti = new ShellToolItem();
		assertNotNull(ti);
		closePreferences();	
	}
	
	@Test
	public void testToolItemInShellToolBarClicked() {
		openPreferences();
		assertThat(new DefaultCLabel().getText(), Is.is("General"));
		new DefaultTreeItem(1).select();
		assertThat(new DefaultLabel().getText(), IsNot.not("General"));
		ToolItem ti = new ShellToolItem();
		ti.click();
		assertThat(new DefaultCLabel().getText(), Is.is("General"));
		closePreferences();		
	}

	@Test
	public void testToolItemInShellToolBarRegexClicked() {
		openPreferences();
		new DefaultTreeItem(1).select();
		ToolItem ti = new ShellToolItem(new RegexMatcher(".*ack.*"));
		assertNotNull(ti);
		closePreferences();			
	}
	
	private void openPreferences() {
		RegexMatchers m = new RegexMatchers("Window.*", "Preferences.*");
		Menu menu = new ShellMenu(m.getMatchers());
		menu.select();
		new DefaultShell("Preferences");
		TreeItem item = new DefaultTreeItem("General");
		item.select();
	}
	
	private void closePreferences() {
		new DefaultTreeItem(0).select();
		new PushButton("Cancel").click();
	}
}

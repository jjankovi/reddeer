package org.jboss.reddeer.eclipse.wst.server.ui.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Represents a server on {@link ServersView}. Contains both, the server data
 * (name, state, status) and operations that can be invoked on server (Start,
 * Stop, Delete etc.). For operations that can be invoked on project added to
 * server see {@link ServerProject}
 * 
 * @author Lucia Jelinkova
 * 
 */
public class Server {

	private static final TimePeriod TIMEOUT = TimePeriod.VERY_LONG;

	private static final Logger log = Logger.getLogger(Server.class);
	
	private TreeItem treeItem;

	public Server(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public ServerLabel getLabel(){
		return new ServerLabel(treeItem);
	}

	public List<ServerProject> getProjects() {
		throw new UnsupportedOperationException();
	}

	public ServerProject getProject() {
		throw new UnsupportedOperationException();
	}

	public void open() {
		throw new UnsupportedOperationException();
	}

	public void start() {
		log.info("Starting server " + getLabel().getName());
		if (!ServerState.STOPPED.equals(getLabel().getState())){
			throw new ServersViewException("Cannot start server because it is not stopped");
		}
		operateServerState("Start", ServerState.STARTED);
	}

	public void debug() {
		log.info("Starting server in debug" + getLabel().getName());
		if (!ServerState.STOPPED.equals(getLabel().getState())){
			throw new ServersViewException("Cannot debug server because it is not stopped");
		}
		operateServerState("Debug", ServerState.DEBUGGING);
	}

	public void profile() {
		log.info("Starting server in profiling mode" + getLabel().getName());
		if (!ServerState.STOPPED.equals(getLabel().getState())){
			throw new ServersViewException("Cannot profile server because it is not stopped");
		}
		operateServerState("Profile", ServerState.PROFILING);
	}

	public void restart() {
		log.info("Restarting server " + getLabel().getName());
		if (!getLabel().getState().isRunningState()){
			throw new ServersViewException("Cannot restart server because it is not running");
		}
		operateServerState("Restart", ServerState.STARTED);
	}

	public void restartInDebug() {
		log.info("Restarting server in debug" + getLabel().getName());
		if (!getLabel().getState().isRunningState()){
			throw new ServersViewException("Cannot restart server in debug because it is not running");
		}
		operateServerState("Restart in Debug", ServerState.DEBUGGING);
	}
	
	public void restartInProfile() {
		log.info("Restarting server in profile" + getLabel().getName());
		if (!getLabel().getState().isRunningState()){
			throw new ServersViewException("Cannot restart server in profile because it is not running");
		}
		operateServerState("Restart in Profile", ServerState.PROFILING);
	}

	public void stop() {
		log.info("Stopping server " + getLabel().getName());
		ServerState state = getLabel().getState();
		if (!ServerState.STARTING.equals(state) && !state.isRunningState()){
			throw new ServersViewException("Cannot stop server because it not running");
		}
		operateServerState("Stop", ServerState.STOPPED);
	}

	public void publish() {
		log.info("Publishing server " + getLabel().getName());
		select();
		new ContextMenu("Publish").select();
		waitForPublish();
	}

	public void clean() {
		log.info("Cleaning server " + getLabel().getName());
		select();
		new ContextMenu("Clean...").select();
		new DefaultShell("Server");
		new PushButton("OK").click();
		waitForPublish();
	}

	public void delete() {
		delete(false);
	}

	public void delete(boolean stopFirst) {
		log.info("Deleting server " + getLabel().getName() + ". Stopping server first: " + stopFirst);
		select();
		ServerState state = getLabel().getState();
		
		new ContextMenu("Delete").select();	
		new WaitUntil(new ShellWithTextIsActive("Delete Server"),TimePeriod.NORMAL);
		if (!ServerState.STOPPED.equals(state) && !ServerState.NONE.equals(state)){
			new CheckBox().toggle(stopFirst);
		}
		new PushButton("OK").click();
		new WaitUntil(new TreeItemIsDisposed(treeItem), TIMEOUT);
		new WaitWhile(new JobIsRunning(), TIMEOUT);
	}

	public void addAndRemoveProject() {
		throw new UnsupportedOperationException();
	}

	protected void select() {
		treeItem.select();
	}

	protected void operateServerState(String menuItem, ServerState resultState){
		select();
		new ContextMenu(menuItem).select();
		new WaitUntil(new JobIsRunning(), TIMEOUT);
		new WaitUntil(new ServerStateCondition(resultState), TIMEOUT);
		new WaitWhile(new JobIsRunning(), TIMEOUT);
	}
	
	protected void waitForPublish(){
		new WaitUntil(new JobIsRunning(), TIMEOUT);
		new WaitWhile(new ServerPublishStateCondition(ServerPublishState.PUBLISHING), TIMEOUT);
		new WaitUntil(new ServerPublishStateCondition(ServerPublishState.SYNCHRONIZED), TIMEOUT);
	}

	private class ServerStateCondition implements WaitCondition {

		private ServerState expectedState;

		private ServerStateCondition(ServerState expectedState) {
			this.expectedState = expectedState;
		}

		@Override
		public boolean test() {
			return expectedState.equals(getLabel().getState());
		}

		@Override
		public String description() {
			return "Server's state is: " + expectedState.getText();
		}
	}
	
	private class ServerPublishStateCondition implements WaitCondition {

		private ServerPublishState expectedState;

		private ServerPublishStateCondition(ServerPublishState expectedState) {
			this.expectedState = expectedState;
		}

		@Override
		public boolean test() {
			return expectedState.equals(getLabel().getPublishState());
		}

		@Override
		public String description() {
			return "Server's publish state is " + expectedState.getText();
		}
	}

	private class TreeItemIsDisposed implements WaitCondition {

		private TreeItem treeItem;

		public TreeItemIsDisposed(TreeItem treeItem) {
			this.treeItem = treeItem;
		}

		@Override
		public boolean test() {
			return treeItem.isDisposed();
		}

		@Override
		public String description() {
			return "Server tree item is disposed";
		}
	}
}


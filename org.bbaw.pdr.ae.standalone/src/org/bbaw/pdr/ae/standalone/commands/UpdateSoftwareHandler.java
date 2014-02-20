package org.bbaw.pdr.ae.standalone.commands;

import java.util.ArrayList;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class UpdateSoftwareHandler implements IHandler {

	private Status _log;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ArrayList<Parameterization> parameters = new ArrayList<Parameterization>(0);

		// get the command from plugin.xml
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
		Command cmd = cmdService.getCommand("org.eclipselabs.p2.rcpupdate.update");


		// build the parameterized command
		ParameterizedCommand pc = new ParameterizedCommand(cmd,
				parameters.toArray(new Parameterization[parameters.size()]));

		// execute the command
		try
		{
			IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
			handlerService.executeCommand(pc, null);
		}
		catch (ExecutionException e)
		{
			_log = new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"execute UpdateSoftwareHandler ", e);
			iLogger.log(_log);
			e.printStackTrace();
		}
		catch (NotDefinedException e)
		{
			_log = new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"execute UpdateSoftwareHandler ", e);
			iLogger.log(_log);
			e.printStackTrace();
		}
		catch (NotEnabledException e)
		{
			_log = new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"execute UpdateSoftwareHandler ", e);
			iLogger.log(_log);
			e.printStackTrace();
		}
		catch (NotHandledException e)
		{
			_log = new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"execute UpdateSoftwareHandler ", e);
			iLogger.log(_log);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}

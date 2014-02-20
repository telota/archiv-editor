package org.eclipselabs.p2.rcpupdate.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipselabs.p2.rcpupdate.utils.plugin.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


/**
 * @see http://wiki.eclipse.org/Equinox/p2/Adding_Self-Update_to_an_RCP_Application
 * @see http://bugs.eclipse.org/281226
 */
public class P2Util {

	public static void checkForUpdates() {
		try {
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(null);
			progressDialog.run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					doCheckForUpdates(new AccumulatingProgressMonitor(monitor, Display.getDefault()));
				}

			});
		} catch (InvocationTargetException e) {
			Activator.log(e);
		} catch (InterruptedException e) {
			Activator.log(e);
		}

	}

	private static void doCheckForUpdates(IProgressMonitor monitor) {
		BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
		ServiceReference reference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		if (reference == null) {
			Activator.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"No provisioning agent found.  This application is not set up for updates."));
			return;
		}

		final IProvisioningAgent agent = (IProvisioningAgent) bundleContext.getService(reference);
		try {
			IStatus updateStatus = P2Util.checkForUpdates(agent, monitor);
			Activator.log(updateStatus);
			if (updateStatus.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
				return;
			}
			if (updateStatus.getSeverity() != IStatus.ERROR)
				PlatformUI.getWorkbench().restart();
		} finally {
			bundleContext.ungetService(reference);
		}
	}

	static IStatus checkForUpdates(IProvisioningAgent agent, IProgressMonitor monitor)
			throws OperationCanceledException {
		ProvisioningSession session = new ProvisioningSession(agent);
		// the default update operation looks for updates to the currently
		// running profile, using the default profile root marker. To change
		// which installable units are being updated, use the more detailed
		// constructors.
		UpdateOperation operation = new UpdateOperation(session);
		SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		IStatus status = operation.resolveModal(sub.newChild(100));
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			return status;
		}
		if (status.getSeverity() == IStatus.CANCEL)
			throw new OperationCanceledException();

		if (status.getSeverity() != IStatus.ERROR) {
			// More complex status handling might include showing the user what
			// updates are available if there are multiples, differentiating
			// patches vs. updates, etc. In this example, we simply update as
			// suggested by the operation.
			ProvisioningJob job = operation.getProvisioningJob(monitor);
			if (job == null) {
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"ProvisioningJob could not be created - does this application support p2 software installation?");
			}
			status = job.runModal(sub.newChild(100));
			if (status.getSeverity() == IStatus.CANCEL)
				throw new OperationCanceledException();
		}
		return status;
	}
}

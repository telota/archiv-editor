package org.eclipselabs.p2.rcpupdate.utils.plugin;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipselabs.p2.rcpupdate.utils"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public static void log(IStatus status) {
		ILog log = getDefault().getLog();
		if (log != null) {
			log.log(status);
		} else {
//			System.out.println(status.getMessage());
			if (status.getException() != null)
				status.getException().printStackTrace();
		}
	}
	
	public static void log(Exception e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}

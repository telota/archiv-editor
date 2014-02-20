/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package net.sf.vex.editor;


import net.sf.vex.core.DisplayDevice;
import net.sf.vex.editor.config.ConfigLoaderJob;
import net.sf.vex.swt.SwtDisplayDevice;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class VexPlugin extends AbstractUIPlugin {
    
    // The plugin's id
    public static final String ID = "net.sf.vex.editor"; //$NON-NLS-1$
    
    /**
     * The constructor.
     */
    public VexPlugin() {

        instance = this;
        
    }
    
    /**
     * Asserts that this method is called from the display thread. If not,
     * an IllegalStateException is thrown.
     */
    public static void assertIsDisplayThread() {
        if (Thread.currentThread() != Display.getDefault().getThread()) {
            throw new IllegalStateException("This method must be called from the display thread."); //$NON-NLS-1$
        }
    }
    
    /**
     * Returns the shared instance.
     */
    public static VexPlugin getInstance() {
        return instance;
    }

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Log an error message without an exception.
     * @param severity One of the IStatus severity levels, e.g. IStatus.ERROR.
     * @param message Message describing the error.
     */
    public void log(int severity, String message) {
        this.getLog().log(new Status(severity, ID, 0, message, null));
    }
    
    /**
     * Log an error message.
     * @param severity One of the IStatus severity levels, e.g. IStatus.ERROR.
     * @param message Message describing the error.
     * @param exception Exception related to the error, or null of none.
     */
    public void log(int severity, String message, Throwable exception) {
        this.getLog().log(new Status(severity, "net.sf.vex.editor", 0, message, exception)); //$NON-NLS-1$
    }
    
    /**
     * Override the plugin startup to intialize the resource tracker.
     */
    public void start(BundleContext bundleContext) throws Exception {

        super.start(bundleContext);

        // TODO Remove DisplayDevice.setCurrent from VexPlugin.start
        // This has been added to the VexWidget ctor, but the problem is that
        // when loading an editor, we load the document before creating the
        // widget, and to do that we need to load the stylesheet, and *this*
        // needs the DisplayDevice to be set properly.
        //
        // One solution might be to do a simplified stylesheet load that only
        // looks at the display property, which is enough to do space
        // normalization but doesn't need to look at the display device.
    
        DisplayDevice.setCurrent(new SwtDisplayDevice());

//        boolean configDebug = this.isDebugging() &&
//            "true".equalsIgnoreCase(Platform.getDebugOption(ID + "/debug/config"));
    
        this.initJob.schedule();

    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }
    
    //========================================================= PRIVATE
    
    private static VexPlugin instance;
    
    private ConfigLoaderJob initJob = new ConfigLoaderJob();
    
}

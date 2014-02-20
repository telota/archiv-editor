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
package net.sf.vex;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ToolkitPlugin extends AbstractUIPlugin {

    private static ToolkitPlugin instance;
    
    public ToolkitPlugin() {
        instance = this;  
    }
    
    /**
     * Returns the shared instance.
     */
    public static ToolkitPlugin getInstance() {
        return instance;
    }
    
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }
}

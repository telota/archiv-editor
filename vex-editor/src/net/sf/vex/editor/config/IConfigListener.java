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
package net.sf.vex.editor.config;

import java.util.EventListener;

/**
 * Interface through which Vex notifies UI components that configuration
 * items such as doctypes and styles have been added, removed, or changed. 
 * Implementations of this 
 * interface should be registered with the VexPlugin instance. All calls to
 * implementations occur on the UI thread.
 */
public interface IConfigListener extends EventListener {
	
    /**
     * Called when one or more configuration items are added, removed, or
     * changed. 
     * @param e ConfigEvent containing details of the change.
     */
    public void configChanged(ConfigEvent e);
    
    /**
     * Called when the Vex configuration is first loaded by the ConfigLoaderJob.
     * This method is guaranteed to be called before the first call to
     * configChanged.
     * @param e ConfigEvent containing details of the change.
     */
    public void configLoaded(ConfigEvent e);
}

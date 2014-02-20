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

import java.net.URL;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

/**
 * Decorates Vex projects with the Vex logo.
 */
public class PluginProjectDecorator implements ILightweightLabelDecorator {

    public void decorate(Object element, IDecoration decoration) {
        
        if (this.vexIcon == null) {
            this.loadImageDescriptors();
        }
        
        if (element instanceof IProject) {
            try {
                IProject project = (IProject) element;
                if (project.hasNature(PluginProjectNature.ID)) {
                    decoration.addOverlay(this.vexIcon, IDecoration.TOP_RIGHT);
                }
            } catch (CoreException e) {
            }
        }
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }
    
    //======================================================== PRIVATE
    
    private ImageDescriptor vexIcon;
    
    private void loadImageDescriptors() {
    	URL url = FileLocator.find(
    			VexPlugin.getInstance().getBundle(),
    			new Path("icons/vex8.gif"), //$NON-NLS-1$
    			null); 
        this.vexIcon = ImageDescriptor.createFromURL(url);
    }

}

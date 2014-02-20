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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

/**
 * Decorates Vex resources that build problems.
 */
public class BuildProblemDecorator extends LabelProvider implements ILightweightLabelDecorator {

    public static final String ID = "net.sf.vex.editor.config.buildProblemDecorator"; //$NON-NLS-1$
    
    public void decorate(Object element, IDecoration decoration) {
        
        if (this.errorIcon == null) {
            this.loadImageDescriptors();
        }
        
        if (element instanceof IResource) {
            try {
                IResource resource = (IResource) element;
                IMarker[] markers = resource.findMarkers(IMarker.PROBLEM, true, 0);
                if (markers.length > 0) {
                    decoration.addOverlay(this.errorIcon, IDecoration.BOTTOM_LEFT);
                }
            } catch (CoreException e) {
            }
        }
    }

    /**
     * Fire a change notification that the markers on the given resource has changed.
     * @param resources Array of resources whose markers have changed.
     */
    public void update(IResource resource) {
        this.fireLabelProviderChanged(new LabelProviderChangedEvent(this, resource));
    }
    
    /**
     * Fire a change notification that the markers on the given resources has changed.
     * @param resources Array of resources whose markers have changed.
     */
    public void update(IResource[] resources) {
        this.fireLabelProviderChanged(new LabelProviderChangedEvent(this, resources));
    }
    
    //======================================================== PRIVATE
    
    private ImageDescriptor errorIcon;
    
    private void loadImageDescriptors() {
    	URL url = FileLocator.find(
    			VexPlugin.getInstance().getBundle(),
    			new Path("icons/error_co.gif"), //$NON-NLS-1$
    			null); 
        this.errorIcon = ImageDescriptor.createFromURL(url);
    }

}

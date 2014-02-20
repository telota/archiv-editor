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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXParseException;

/**
 * Parses and registers Vex configuration objects in a Vex Plug-in project.
 */
public class PluginProjectBuilder extends IncrementalProjectBuilder {

    public static final String ID = "net.sf.vex.editor.pluginBuilder"; //$NON-NLS-1$

    public PluginProjectBuilder() {
    }
    
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
            throws CoreException {

        IProject project = this.getProject();
        
        final PluginProject pluginProject = PluginProject.get(project);

        if (pluginProject == null) {
            String message = MessageFormat.format(
                    Messages.getString("PluginProjectBuilder.notConfigSource"), //$NON-NLS-1$
                    new Object[] { project.getName() });
            VexPlugin.getInstance().log(IStatus.ERROR, message);
            return null;
        }
        
        boolean parseConfigXml;

        IResourceDelta delta = this.getDelta(project);

        if (kind == FULL_BUILD || delta == null) {
            
            //System.out.println("PluginProjectBuilder.build (full) starts for project " + project.getName());
            
            this.clean(null);
            parseConfigXml = true;
            
        } else { // incremental or auto build

            //System.out.println("PluginProjectBuilder.build (incremental) starts for project " + project.getName());
            
            parseConfigXml = (delta.findMember(new Path(PluginProject.PLUGIN_XML)) != null);

            // If a resource is deleted, renamed, or moved, we'll update the
            // config, but only if we're not going to parse it.
            final boolean canUpdateConfig = !parseConfigXml;
            
            IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
                public boolean visit(IResourceDelta delta) throws CoreException {
                    IResource resource = delta.getResource();
                    String path = resource.getProjectRelativePath().toString();
                    pluginProject.removeResource(path);

                    if (delta.getKind() == IResourceDelta.REMOVED) {
                        
                        ConfigItem item = pluginProject.getItemForResource(path);
                        
                        if (item == null) {
                            return true;
                        }
                        
                        if (canUpdateConfig && (delta.getFlags() & IResourceDelta.MOVED_TO) > 0) {
                            // Resource was moved.
                            String newPath = delta.getMovedToPath().removeFirstSegments(1).toString();
                            item.setResourcePath(newPath);
                        } else {
                            // Resource deleted, so let's nuke the item from the config
                            pluginProject.remove(item);
                        }
                        
                        try {
                            pluginProject.writeConfigXml();
                        } catch (Exception ex) {
                            String message = MessageFormat.format(
                                    Messages.getString("PluginProjectBuilder.cantSaveFile"), //$NON-NLS-1$
                                    new Object[] { PluginProject.PLUGIN_XML });

                            VexPlugin.getInstance().log(IStatus.ERROR, message, ex);
                        }
                        
                        try {
                            // If auto-build is on this is unnecessary since
                            // another build will be triggered by us saving
                            // vex-plugin.xml above. This is just here in case 
                            // we're not auto-building
                            pluginProject.saveState();
                        } catch (Exception ex) {
                            String message = MessageFormat.format(
                                    Messages.getString("PluginProjectBuilder.cantSaveFile"), //$NON-NLS-1$
                                    new Object[] { PluginProject.PROJECT_CONFIG_SER });
                            VexPlugin.getInstance().log(IStatus.WARNING, message, ex);
                        }
                    }
                    
                    return true;
                }
            };
            
            delta.accept(visitor);
        }

        IMarker[] oldMarkers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        IResource[] markedResources = new IResource[oldMarkers.length];
        for (int i = 0; i < markedResources.length; i++) {
            markedResources[i] = oldMarkers[i].getResource();
        }
        
        project.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        this.getBuildProblemDecorator().update(markedResources);

        ConfigRegistry registry = ConfigRegistry.getInstance();
        
        try {
            registry.lock();

            if (parseConfigXml) {
                IResource pluginXmlResource = this.getProject().getFile(PluginProject.PLUGIN_XML);
                try {
                    if (pluginXmlResource.exists()) {
                        pluginProject.parseConfigXml();
                    } else {
                        pluginProject.removeAllItems();
                        String message = MessageFormat.format(
                                Messages.getString("PluginProjectBuilder.missingFile"), //$NON-NLS-1$
                                new Object[] { PluginProject.PLUGIN_XML });
                        this.flagError(this.getProject(), message);
                    }
                } catch (SAXParseException ex) {
                    this.flagError(pluginXmlResource, ex.getLocalizedMessage(), ex.getLineNumber());
                } catch (Exception ex) {
                    String message = MessageFormat.format(
                            Messages.getString("PluginProjectBuilder.parseError"), //$NON-NLS-1$
                            new Object[] { PluginProject.PLUGIN_XML });
                    VexPlugin.getInstance().log(IStatus.ERROR, message, ex); //$NON-NLS-1$
                    this.flagError(pluginXmlResource, ex.getLocalizedMessage());
                }
            }
            
            IBuildProblemHandler problemHandler = new IBuildProblemHandler() {
                public void foundProblem(BuildProblem problem) {
                    try {
                        IResource resource = getProject().getFile(problem.getResourcePath());
                        flagError(resource, problem.getMessage(), problem.getLineNumber());
                    } catch (CoreException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
            
            pluginProject.parseResources(problemHandler);

            // Write new config to SER file.
            try {
                pluginProject.saveState();
            } catch (IOException ex) {
                String message = MessageFormat.format(
                        Messages.getString("PluginProjectBuilder.cantSaveConfig"), //$NON-NLS-1$
                        new Object[] { project.getName() });
                VexPlugin.getInstance().log(IStatus.WARNING, message, ex);
            }
        } finally {
            registry.unlock();
        }

        registry.fireConfigChanged(new ConfigEvent(this));
        
        return null;
    }

    
    protected void clean(IProgressMonitor monitor) throws CoreException {
        ConfigRegistry registry = ConfigRegistry.getInstance();
        try {
            registry.lock();
            PluginProject pluginProject = PluginProject.get(this.getProject());
            if (pluginProject != null) {
                pluginProject.cleanState();
                pluginProject.removeAllItems();
                pluginProject.removeAllResources();
                registry.fireConfigChanged(new ConfigEvent(this));
            }
        } finally {
            registry.unlock();
        }
    }
    
    //======================================================== PRIVATE

    private BuildProblemDecorator buildProblemDecorator;

    private void flagError(IResource resource, String message) throws CoreException {
        flagError(resource, message, -1);
    }
    
    private void flagError(IResource resource, String message, int lineNumber) throws CoreException {
        IMarker marker = resource.createMarker(IMarker.PROBLEM);
        if (marker.exists()) {
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            if (lineNumber > 0) {
                marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            }
            this.getBuildProblemDecorator().update(resource);
        }
    }
    
    private BuildProblemDecorator getBuildProblemDecorator() {
        if (this.buildProblemDecorator == null) {
            IDecoratorManager dm = PlatformUI.getWorkbench().getDecoratorManager();
            this.buildProblemDecorator = (BuildProblemDecorator) dm.getBaseLabelProvider(BuildProblemDecorator.ID);
        }
        return this.buildProblemDecorator;
    }
    
}

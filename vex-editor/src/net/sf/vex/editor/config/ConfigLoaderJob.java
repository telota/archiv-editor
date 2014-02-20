/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *     Ed Burnette - 7/23/2006 -  Changes needed to build on 3.2.
 *******************************************************************************/
package net.sf.vex.editor.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * Job that loads Vex configuration objects from plugins and plugin projects.
 */
public class ConfigLoaderJob extends Job {

    public static final String PLUGIN_CONFIG_SER_PREFIX = ".vexConfig-"; //$NON-NLS-1$
    public static final String PLUGIN_CONFIG_SER_SUFFIX = ".ser"; //$NON-NLS-1$


    /**
     * Class constructor.
     */
    public ConfigLoaderJob() {
        super(Messages.getString("ConfigLoaderJob.loadingConfig")); //$NON-NLS-1$
    }

    protected IStatus run(IProgressMonitor monitor) {
        
        //System.out.println("ConfigLoaderJob starts");
        
        int pluginCount = Platform.getExtensionRegistry().getNamespaces().length;
        int projectCount = ResourcesPlugin.getWorkspace().getRoot().getProjects().length;
        
        monitor.beginTask(Messages.getString("ConfigLoaderJob.loadingConfig"), pluginCount + projectCount); //$NON-NLS-1$

        this.loadPlugins(monitor);
        this.loadPluginProjects(monitor);
        ConfigRegistry.getInstance().fireConfigLoaded(new ConfigEvent(this));

        monitor.done();
        
        //System.out.println("ConfigLoaderJob ends");
        
        return Status.OK_STATUS;
    }

    //======================================================= PRIVATE
    
    /**
     * Load configurations from all registered plugins.
     */
    private void loadPlugins(IProgressMonitor monitor) {
        
        ConfigRegistry configRegistry = ConfigRegistry.getInstance();
        IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
        String[] namespaces = extRegistry.getNamespaces();
        for (int i = 0; i < namespaces.length; i++) {
            
            String ns = namespaces[i];
            Bundle bundle = Platform.getBundle(ns);
            if (bundle == null)
            	continue;
            
            String name = (String) bundle.getHeaders().get(Constants.BUNDLE_NAME);
            monitor.subTask(Messages.getString("ConfigLoaderJob.loading") + name); //$NON-NLS-1$
            
            File stateDir = Platform.getStateLocation(bundle).toFile();
            String version = (String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
            String serFile = PLUGIN_CONFIG_SER_PREFIX + version + PLUGIN_CONFIG_SER_SUFFIX;
            File configSerFile = new File(stateDir, serFile);

            ConfigSource source = null;
            if (configSerFile.exists()) {
                try {
                    //long start = System.currentTimeMillis();
                    source = loadConfigSourceFromFile(configSerFile);
                    //long end = System.currentTimeMillis();
                    //System.out.println("  load from ser file took " + (end-start) + "ms");
                } catch (IOException ex) {
                    String message = MessageFormat.format(
                            Messages.getString("ConfigLoaderJob.loadingError"), //$NON-NLS-1$
                            new Object[] { configSerFile });
                    this.log(IStatus.WARNING, message, ex);
                }
            }
            
            if (source == null) {
                
                source = ConfigPlugin.load(ns);

                if (source != null) {
                    try {
                        saveConfigSourceToFile(source, configSerFile);
                    } catch (IOException ex) {
                        String message = MessageFormat.format(
                                Messages.getString("ConfigLoaderJob.cacheError"), //$NON-NLS-1$
                                new Object[] { configSerFile });
                        this.log(IStatus.WARNING, message, ex);
                    }
                } else {
                    if (configSerFile.exists()) {
                        configSerFile.delete(); // Used to have a config, but now we don't
                    }
                }

            }
            
            if (source != null) {
                configRegistry.addConfigSource(source);
            }
            
            monitor.worked(1);
        }
    }
    
    private static ConfigSource loadConfigSourceFromFile(File file) throws IOException {
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (ConfigSource) ois.readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    private static void saveConfigSourceToFile(ConfigSource config, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(config);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /**
     * Load configurations from all Vex Plugin Projects in the workspace.
     */
    private void loadPluginProjects(IProgressMonitor monitor) {
        
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = root.getProjects();
        
        for (int i = 0; i < projects.length; i++) {
            try {
                if (projects[i].isOpen() &&
                        projects[i].hasNature(PluginProjectNature.ID)) {
                    monitor.subTask(Messages.getString("ConfigLoaderJob.loadingProject") + projects[i].getName()); //$NON-NLS-1$
                    PluginProject.load(projects[i]);
                    monitor.worked(1);
                }
            } catch (CoreException e) {
                String message = MessageFormat.format(
                        Messages.getString("ConfigLoaderJob.natureError"), //$NON-NLS-1$
                        new Object[] { projects[i].getName() });
                VexPlugin.getInstance().log(IStatus.ERROR, message, e);
            }
        }
    }


    private void log(int severity, String message, Throwable exception) {
        VexPlugin.getInstance().log(severity, message, exception);
    }
}

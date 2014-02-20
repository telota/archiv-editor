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
import java.net.URL;
import java.text.MessageFormat;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

/**
 * A regular Eclipse plugin that provides Vex configuration items.
 */
public class ConfigPlugin extends ConfigSource {

    /**
     * Filename prefix used when serializing the config from an installed
     * plugin. Since all versions of a plugin share the same persistence
     * area, we incorporate the plugin version number into the filename.
     */
    public static final String SER_FILE_PREFIX = ".vexConfig-"; //$NON-NLS-1$
    
    /** Filename suffix used when serializing an installed plugin */
    public static final String SER_FILE_SUFFIX = ".ser"; //$NON-NLS-1$

    protected ConfigPlugin(String namespace) {
        this.namespace = namespace;
    }
    
    public static ConfigPlugin load(String namespace) {
            
        ConfigPlugin configPlugin = new ConfigPlugin(namespace);
        configPlugin.setUniqueIdentifer(namespace);
        
        IExtension[] exts = Platform.getExtensionRegistry().getExtensions(namespace);
        for (int i = 0; i < exts.length; i++) {
            IExtension ext = exts[i];
            try {
                configPlugin.addItem(
                        ext.getExtensionPointUniqueIdentifier(), 
                        ext.getSimpleIdentifier(),
                        ext.getLabel(),
                        ConfigurationElementWrapper.convertArray(ext.getConfigurationElements()));
            } catch (IOException e) {
                String message = MessageFormat.format(
                        Messages.getString("ConfigPlugin.loadError"), //$NON-NLS-1$
                        new Object[] { ext.getSimpleIdentifier(), namespace });
                VexPlugin.getInstance().log(IStatus.ERROR, message, e);
                return null;
            }
        }
        
        configPlugin.parseResources(null);
        
        return configPlugin.isEmpty() ? null : configPlugin;
    }
    
    
    public URL getBaseUrl() {
        return Platform.getBundle(namespace).getEntry("plugin.xml"); //$NON-NLS-1$
    }
    
    //======================================================= PRIVATE
    
    private String namespace;
}

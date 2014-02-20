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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Base class of all configurtion items such as document types and styles.
 */
public abstract class ConfigItem implements Comparable, Serializable {
    
    /**
     * Class constructor.
     * @param config VexConfiguration to which this item belongs.
     */
    public ConfigItem(ConfigSource config) {
        this.config = config;
    }

    public int compareTo(Object o) {
        return this.getName().compareTo(((ConfigItem) o).getName());
    }
    
    /**
     * Generate a simple identifier for the item that is unique with its
     * configuration.
     */
    public String generateSimpleId() {
        String base = "id"; //$NON-NLS-1$
        int i = 1;
        for (;;) {
            String id = base + i;
            if (this.getConfig().getItem(id) == null) {
                return id;
            }
            i++;
        }
    }
  
    /**
     * Returns the VexConfiguration to which this item belongs.
     */
    public ConfigSource getConfig() {
        return config;
    }

    /**
     * Returns the extension point ID of configuration item. This will be 
     * the same for all config items of a given type.
     */
    public abstract String getExtensionPointId();

    /**
     * Returns the simple ID of this item. This is a short string containing
     * no periods.
     */
    public String getSimpleId() {
        return this.id;
    }

    /**
     * Returns the unique ID of this item. The unique ID is formed by concatenating
     * the ID of the associated VexConfiguration, a period, and the simple ID
     * of this item.
     */
    public String getUniqueId() {
        return this.id == null ? null : this.getConfig().getUniqueIdentifer() + "." + this.id; //$NON-NLS-1$
    }

    /**
     * Returns the human-readable name for this item.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the path of the associated resource, if any, relative to 
     * the base directory of the associated VexConfiguration. Returns null
     * if no resource is associated with this item.
     */
    public String getResourcePath() {
        return this.resourcePath;
    }

    /**
     * Returns the URL of the associated resource, if any. This is formed
     * by appending the resource path to the URL of the associated
     * VexConfiguration.
     */
    public URL getResourceUrl() throws MalformedURLException {
        if (this.resourcePath == null) {
            return null;
        } else {
            return new URL(this.getConfig().getBaseUrl(), this.getResourcePath());
        }
    }

    /**
     * Returns true if s is null or empty. Convenience method to be used
     * in isValid().
     * @param s String to check.
     */
    protected boolean isBlank(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Returns true if this item has sufficient information to be used as
     * a configuration item. By default, the item must have a simple ID 
     * and a name. Subclasses should override and provide additional checks
     * as necessary, after calling this base implementation.
     */
    public boolean isValid() {
        return !isBlank(id) && !isBlank(name);
    }
    
    /**
     * Sets the name for this item.
     * @param name New name for this item.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Sets the resource path for this item.
     * @param resourcePath New resource path for this item.
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    /**
     * Sets the simple ID for this item. The simple ID should only contain
     * letters and numbers, and must not contain a period.
     * @param id New simple ID for this item.
     */
    public void setSimpleId(String id) {
        this.id = id;
    }
    
    //==================================================== PRIVATE
    
    private String id;
    private String name;
    private String resourcePath;
    private ConfigSource config;
    
}

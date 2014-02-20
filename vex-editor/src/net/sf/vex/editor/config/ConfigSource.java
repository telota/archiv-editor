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
import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.runtime.IStatus;


/**
 * A plugin or plugin project that contributes ConfigItems. This class and all
 * configuration items added to it must be serializable, since it is persisted
 * across Vex invocations due to the expense of reparsing the configuration
 * items.
 */
public abstract class ConfigSource implements Serializable {
    
    /**
     * Adds the given item to the configuration.
     * @param item ConfigItem to be added.
     */
    public void addItem(ConfigItem item) {
        this.items.add(item);
    }

    /**
     * Creates a configuration item and adds it to this configuration. 
     * If the given extension point does not have a factory registered
     * in VexPlugin, no action is taken and null is returned.
     * 
     * @param extensionPoint Extension point of the item to be added.
     * @param simpleIdentifier Simple (i.e. no dots) identifier of the item.
     * @param name Name of the item.
     * @param configElements Array of IConfigElement objects representing the item's settings.
     * @return The newly created ConfigItem, or null if extensionPoint is
     * not recognized.
     * @throws IOException
     */
    public ConfigItem addItem(
            String extensionPoint, 
            String simpleIdentifier, 
            String name, 
            IConfigElement[] configElements) throws IOException {
        
        IConfigItemFactory factory = ConfigRegistry.getInstance().getConfigItemFactory(extensionPoint);
        if (factory != null) {
            ConfigItem item = factory.createItem(this, configElements);
            item.setSimpleId(simpleIdentifier);
            item.setName(name);
            this.addItem(item);
            return item;
        } else {
            return null;
        }
        
    }
    
    /**
     * Removes the given item from the configuration.
     * @param item ConfigItem to be removed.
     */
    public void remove(ConfigItem item) {
        items.remove(item);
    }
    
    /**
     * Remove all items from this configuration.
     */
    public void removeAllItems() {
        this.items.clear();
    }
    
    /**
     * Remove all parsed resources from this configuration.
     */
    public void removeAllResources() {
        this.parsedResources.clear();
    }
    
    /**
     * Remove the resource associated with the given URI from the resource
     * cache. The factory must handle any of the following scenarios.
     * 
     * <ul>
     * <li>The URI represents the primary resource associated with a
     * configuration item.</li>
     * <li>The URI is a secondary resource associated with a primary
     * resource. In this case the primary resource is removed.</li>
     * <li>The URI has nothing to do with a configuration item,
     * in which case no action is taken.</li>
     * </ul>
     * 
     * To fully implement this method, the factory must interact with the
     * parser and track which secondary resources are associated with
     * which primaries.
     * 
     * @param uri Relative URI of the resource to remove.
     */
    public void removeResource(String uri) {
        this.parsedResources.remove(uri); // TODO Respect secondary resources
    }


    /**
     * Returns a list of all items in this configuration.
     */
    public List getAllItems() {
        return items;
    }

    /**
     * Returns all ConfigItems of the given type registered with this 
     * configuration.
     * @param type The type of ConfigItem to return.
     */
    public Collection getAllItems(String type) {
        List items = new ArrayList();
        for (Iterator it = this.items.iterator(); it.hasNext();) {
            ConfigItem item = (ConfigItem) it.next();
            if (item.getExtensionPointId().equals(type)) {
                items.add(item);
            }
        }
        return items;
    }
    
    /**
     * Returns the base URL of this factory. This is used to resolve 
     * relative URLs in config items
     */
    public abstract URL getBaseUrl();
    

    /**
     * Returns a particular item from the configuration. Returns null if no
     * matching item could be found.  
     * @param simpleId Simple ID of the item to return.
     */
    public ConfigItem getItem(String simpleId) {
        for (Iterator it = this.items.iterator(); it.hasNext();) {
            ConfigItem item = (ConfigItem) it.next();
            if (item.getSimpleId() != null && item.getSimpleId().equals(simpleId)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Returns the item for the resource with the given path relative
     * to the plugin or project. May return null if no such item exists.
     * @param resourcePath Path of the resource.
     */
    public ConfigItem getItemForResource(String resourcePath) {
        for (Iterator it = this.items.iterator(); it.hasNext();) {
            ConfigItem item = (ConfigItem) it.next();
            if (item.getResourcePath().equals(resourcePath)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Returns the parsed resource object for the given URI, or null of
     * none exists.
     * @param uri URI of the resource, relative to the base URL of this configuration.
     */
	public Object getParsedResource(String uri)
	{
        return this.parsedResources.get(uri);
    }

    /**
     * Returns the unique identifier of this configuration. This is the same
     * as the ID of the plugin that defines the configuration.
     */
    public String getUniqueIdentifer() {
        return this.id;
    }
    
    /**
     * Returns all ConfigItems of the given type for which isValid returns
     * true.
     * @param type The type of ConfigItem to return.
     */
    public Collection getValidItems(String type) {
        Collection allItems = this.getAllItems(type);
        List validItems = new ArrayList();
        for (Iterator it = allItems.iterator(); it.hasNext();) {
            ConfigItem item = (ConfigItem) it.next();

			// FIXME auskommentier cp
			// if (item.isValid()) {
                validItems.add(item);
			// }
        }
        return validItems;
    }
    
    /**
     * Returns true if there are no items in this configuration.
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
    
    /**
     * Parses all resources required by the registered items.
     * @param problemHandler Handler for build problems. May be null.
     */
    public void parseResources(IBuildProblemHandler problemHandler) {
        for (Iterator it = this.items.iterator(); it.hasNext();) {
            ConfigItem item = (ConfigItem) it.next();
            String uri = item.getResourcePath();
            if (!this.parsedResources.containsKey(uri)) {
                IConfigItemFactory factory = ConfigRegistry.getInstance().getConfigItemFactory(item.getExtensionPointId());
                Object parsedResource;
                try {
                    parsedResource = factory.parseResource(this.getBaseUrl(), uri, problemHandler);
                    this.parsedResources.put(uri, parsedResource);
                } catch (IOException ex) {
                    String message = MessageFormat.format(
                            Messages.getString("ConfigSource.errorParsingUri"),
                            new Object[] { uri });
                    VexPlugin.getInstance().log(IStatus.ERROR, message, ex); //$NON-NLS-1$
                }
            }
        }
    }
    
    /**
     * Sets the unique identifier of this configuration.
     * @param id New identifier for this configuration.
     */
    public void setUniqueIdentifer(String id) {
        this.id = id;
    }
    
    //==================================================== PRIVATE
    
    // Globally-unique identifier of this configuration
    // == the plugin id.
    private String id;
    
    // all config items in this configuration
    private List items = new ArrayList();
    
    // map String URI => parsed resource
    private Map parsedResources = new HashMap();
    
}

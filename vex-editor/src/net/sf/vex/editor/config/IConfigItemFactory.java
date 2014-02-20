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

/**
 * Manager of a set of configuration items and their associated resources.
 * One concrete implementation of this interface will exist for each kind
 * of configuration item, e.g. a DoctypeManager, StyleManager, etc.
 * Further, each Vex plugin and Vex plugin project will have one of
 * each kind of manager. All managers are registered with the VexPlugin
 * class.
 */
public interface IConfigItemFactory {

    /**
     * Returns an array of configuration elements needed to re-create the given
     * item. If no configuration elements are necessary, the method may return
     * null instead of an empty array. This is essentially the inverse of
     * the createItem method.
     * 
     * @param item ConfigItem for which to create configuration elements.
     */
    public IConfigElement[] createConfigurationElements(ConfigItem item);

    /**
     * Creates an item and adds it to the given configuration. 
     * @param config Configuration that owns the item.
     * @param configElements Details of the configuration item from the
     * plugin manifest.
     */
    public ConfigItem createItem(ConfigSource config, IConfigElement[] configElements) throws IOException;
    
    /**
     * Returns the ID of the extension point that defines this type
     * of configuration item.
     */
    public String getExtensionPointId();
    
    /**
     * Returns an array of file extension for resources that apply to this type
     * of configuration item. The returned strings should <i>not</i> have 
     * leading dots.
     */
    public String[] getFileExtensions();
    
    /**
     * Returns the pluralized name of the type of configuration item managed
     * by this factory. For example, "Document Types".
     * @return
     */
    public String getPluralName();
    
    /**
     * Parse a resource for this type of configuration item. Implementations
     * must not fail if passed a null problem handler.
     * 
     * @param baseUrl Base URL of the project or plugin containing the resource.
     * @param resourcePath Path of the resource relative to the base URL.
     * @param problemHandler Problem handler, or null if the caller does
     * not require build problem reporting.
     */
    public Object parseResource(URL baseUrl, String resourcePath, IBuildProblemHandler problemHandler) throws IOException;

}

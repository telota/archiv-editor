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

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Wrapper for IConfigurationElement that implements IConfigElement.
 */
public class ConfigurationElementWrapper implements IConfigElement {

    /**
     * Class constructor.
     * @param element Element to be wrapped.
     */
    public ConfigurationElementWrapper(IConfigurationElement element) {
        this.element = element;
    }
    
    public String getAttribute(String name) {
        return this.element.getAttribute(name);
    }

    public String[] getAttributeNames() {
        return this.element.getAttributeNames();
    }

    public IConfigElement[] getChildren() {
        return convertArray(this.element.getChildren());
    }

    public IConfigElement[] getChildren(String name) {
        return convertArray(this.element.getChildren(name));
    }

    public String getName() {
        return this.element.getName();
    }

    public String getValue() {
        return this.element.getValue();
    }

    /**
     * Wraps each element in an array of IConfigurationElement objects with
     * a ConfigurationElementWrapper and returns the result.
     * @param elements Array of elements to be wrapped.
     */
    public static IConfigElement[] convertArray(IConfigurationElement[] elements) {
        IConfigElement[] ret = new IConfigElement[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ret[i] = new ConfigurationElementWrapper(elements[i]);
        }
        return ret;
    }

    //=================================================== PRIVATE
    
    private IConfigurationElement element;
    
}

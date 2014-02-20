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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lightweight implementation of the IConfigurationElement interface. This
 * class is used by config item factories when re-creating the configuration
 * elements corresponding to a given config item.
 */
public class ConfigurationElement implements IConfigElement {

    /**
     * Class constructor.
     */
    public ConfigurationElement() {
    }

    /**
     * Class constructor.
     * @param name Name of the element.
     */
    public ConfigurationElement(String name) {
        this.name = name;
    }
    
    /**
     * Adds a new child to this element.
     * @param child child to be added.
     */
    public void addChild(IConfigElement child) {
        this.children.add(child);
    }
    
    public String getAttribute(String name) {
        return (String) this.attributes.get(name);
    }

    public String[] getAttributeNames() {
        Set keys = this.attributes.keySet();
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    public IConfigElement[] getChildren() {
        return (IConfigElement[]) this.children.toArray(new IConfigElement[this.children.size()]);
    }

    public IConfigElement[] getChildren(String name) {
        List kids = new ArrayList();
        for (Iterator it = this.children.iterator(); it.hasNext();) {
            IConfigElement child = (IConfigElement) it.next();
            if (child.getName().equals(name)) {
                kids.add(child);
            }
        }
        return (IConfigElement[]) kids.toArray(new IConfigElement[kids.size()]);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * Sets the given attribute. If value is null, the attribute is removed
     * from the element.
     * 
     * @param name Name of the attribute.
     * @param value Value of the attribute.
     */
    public void setAttribute(String name, String value) {
        if (value == null) {
            this.attributes.remove(name);
        } else {
            this.attributes.put(name, value);
        }
    }
    
    /**
     * Sets the children of this element given an array of IConfigElement
     * objects.
     * @param children Children of this element.
     */
    public void setChildren(IConfigElement[] children) {
        this.children.clear();
        this.children.addAll(Arrays.asList(children));
    }
    
    /**
     * Sets the name of the element.
     * @param name Name of the element.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Sets the value of the element.
     * @param value Value of the element.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    //==================================================== PRIVATE

    private String name;
    private String value;
    private Map attributes = new HashMap();
    private List children = new ArrayList();
}

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


/**
 * Represents the XML element for a Vex config item in plugin.xml.
 * Vex-specific replacement for the Eclipse IConfigurationElement class. 
 * We need this because we are not supposed to implement IConfigurationElement,
 * and in fact it changed and broke us from Eclipse 3.0 -> 3.1.
 */
public interface IConfigElement {

    /**
     * Returns the value of the given attribute.
     * @param name Name of the attribute for which to return a name.
     * @return
     */
    public String getAttribute(String name);

    /**
     * Returns an array of all the attributes defined by this element.
     */
    public String[] getAttributeNames();

    /**
     * Returns an array of the children of this element.
     */
    public IConfigElement[] getChildren();

    /**
     * Returns an array of the children of this element with the given name.
     * @param name Name of children to search for.
     */
    public IConfigElement[] getChildren(String name);

    /**
     * Returns the name of this element.
     */
    public String getName();

    /**
     * Returns the value of this element.
     */
    public String getValue();
}
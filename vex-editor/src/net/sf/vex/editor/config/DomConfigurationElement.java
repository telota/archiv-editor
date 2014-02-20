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
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Implements IConfigurationElement against a W3C DOM Element object.
 */
public class DomConfigurationElement implements IConfigElement {

    public DomConfigurationElement(Element element) {
        this.element = element;
    }
    
    public String getAttribute(String name) {
        return this.element.getAttribute(name); // TODO translate from resource bundle
    }

    public String[] getAttributeNames() {
        int n = this.element.getAttributes().getLength();
        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            Node node = this.element.getAttributes().item(i);
            names[i] = node.getLocalName();
        }
        return names;
    }

    public IConfigElement[] getChildren() {
        return this.getChildren(null);
    }

    public IConfigElement[] getChildren(String name) {
        List children = new ArrayList();
        NodeList list = this.element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                if (name == null || name.equals(node.getNodeName())) {
                    children.add(node);
                }
            }
        }
        
        int n = children.size();
        IConfigElement[] childArray = new IConfigElement[n];
        for (int i = 0; i < n; i++) {
            childArray[i] = new DomConfigurationElement((Element) children.get(i));
        }
        
        return childArray;
    }

    public String getName() {
        return element.getLocalName();
    }

    public String getValue() {
        StringBuffer sb = new StringBuffer();
        
        NodeList list = this.element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Text) {
                sb.append(node.getNodeValue());
            }
        }
        return sb.toString();
    }

    //===================================================== PRIVATE
    
    private Element element;
    
}

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
package net.sf.vex.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.vex.undo.CannotRedoException;
import net.sf.vex.undo.CannotUndoException;
import net.sf.vex.undo.IUndoableEdit;

/**
 * <code>Element</code> represents a tag in an XML document. Methods
 * are available for managing the element's attributes and children.
 */
public class Element extends Node implements Cloneable {

    private String name;
    private Element parent = null;
    private List children = new ArrayList();
    private Map attributes = new HashMap();



    /**
     * Class constructor.
     * @param name element name
     */
    public Element(String name) {
	this.name = name;
    }

    /**
     * Adds the given child to the end of the child list.
     * Sets the parent attribute of the given element to this element.
     */
    public void addChild(Element child) {
        this.children.add(child);
        child.parent = this;
    }
    

    /**
     * Clones the element and its attributes. The returned element has
     * no parent or children.
     */
    public Object clone() {
        try {
            Element element = new Element(this.getName());
            for (Iterator it = this.attributes.keySet().iterator(); it
                    .hasNext();) {
                String attrName = (String) it.next();
                element.setAttribute(attrName, (String) this.attributes
                        .get(attrName));
            }
            return element;
            
        } catch (DocumentValidationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns the value of an attribute given its name. If no such
     * attribute exists, returns null.
     *
     * @param name Name of the attribute.
     */
    public String getAttribute(String name) {
	return (String) attributes.get(name);
    }

    /**
     * Returns an array of names of the attributes in the element.
     */
    public String[] getAttributeNames() {
	Collection names = this.attributes.keySet();
	return (String[]) names.toArray(new String[names.size()]);
    }

    /**
     * Returns an iterator over the children. Used by
     * <code>Document.delete</code> to safely delete children.
     */
    public Iterator getChildIterator() {
	return this.children.iterator();
    }

    /**
     * Returns an array of the elements children.
     */
    public Element[] getChildElements() {
	int size = this.children.size();
	return (Element[]) this.children.toArray(new Element[size]);
    }

    /**
     * Returns an array of nodes representing the content of this element. 
     * The array includes child elements and runs of text returned as 
     * <code>Text</code> objects.
     */
    public Node[] getChildNodes() {
        return Document.createNodeArray(
            this.getContent(),
            this.getStartOffset() + 1,
            this.getEndOffset(),
            this.getChildElements());
    }

    /**
     * @return The document to which this element belongs.
     * Returns null if this element is part of a document
     * fragment.
     */
    public Document getDocument() {
        Element root = this;
        while (root.getParent() != null) {
            root = root.getParent(); 
        }
        if (root instanceof RootElement) {
            return ((RootElement) root).getDocument();
        } else {
            return null;
        }
    }
    
    /**
     * Returns the name of the element.
     */
    public String getName() {
	return this.name;
    }

    /**
     * Returns the parent of this element, or null if this is the root element.
     */
    public Element getParent() {
	return this.parent;
    }

    
    public String getText() {
        String s = super.getText();
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * Inserts the given element as a child at the given child index.
     * Sets the parent attribute of the given element to this element.
     */
    void insertChild(int index, Element child) {
	this.children.add(index, child);
	child.parent = this;
    }
    
    /**
     * Returns true if the element has no content.
     */
    public boolean isEmpty() {
        return this.getStartOffset() + 1 == this.getEndOffset();
    }

    /**
     * Removes the given attribute from the array.
     *
     * @param name name of the attribute to remove.
     */
    public void removeAttribute(String name) 
        throws DocumentValidationException {

        String oldValue = this.getAttribute(name);
        String newValue = null;            
	if (oldValue != null) {
	    this.attributes.remove(name);
	}
        Document doc = this.getDocument();
        if (doc != null) { // doc may be null, e.g. when we're cloning an element
                           // to produce a document fragment
            
            IUndoableEdit edit = doc.isUndoEnabled() ?
                    new AttributeChangeEdit(name, oldValue, newValue) : null;
                    
            doc.fireAttributeChanged(new DocumentEvent(
                    doc, this, name, oldValue, newValue, edit));
        }
    }

    /**
     * Sets the value of an attribute for this element.
     *
     * @param name Name of the attribute to be set.
     * @param value New value for the attribute. If null, this call
     * has the same effect as removeAttribute(name).
     */
    public void setAttribute(String name, String value) 
        throws DocumentValidationException {
            
        String oldValue = this.getAttribute(name);
        
        if (value == null && oldValue == null) {
            return;
        } else if (value == null) {
            this.removeAttribute(name);
        } else if (value.equals(oldValue)) {
            return;
        } else {
            this.attributes.put(name, value);
            Document doc = this.getDocument();
            if (doc != null) { // doc may be null, e.g. when we're cloning an element
                               // to produce a document fragment
                
                IUndoableEdit edit = doc.isUndoEnabled() ?
                        new AttributeChangeEdit(name, oldValue, value) : null;
                        
                doc.fireAttributeChanged(new DocumentEvent(
                        doc, this, name, oldValue, value, edit));
            }
        }
        

    }

    /**
     * Sets the parent of this element.
     *
     * @param parent Parent element.
     */
    public void setParent(Element parent) {
	this.parent = parent;
    }

    public String toString() {
        
	StringBuffer sb = new StringBuffer();
	sb.append("<");
	sb.append(this.getName());
	String[] attrs = this.getAttributeNames();
	
	for (int i = 0; i < attrs.length; i++) {
	    if (i > 0) {
	    	sb.append(",");
	    }
	    sb.append(" ");
	    sb.append(attrs[i]);
	    sb.append("=\"");
	    sb.append(this.getAttribute(attrs[i]));
	    sb.append("\"");
	}
    
	sb.append("> (");
	sb.append(this.getStartPosition());
	sb.append(",");
	sb.append(this.getEndPosition());
	sb.append(")");
    
	return sb.toString();
    }

    //========================================================= PRIVATE

    private class AttributeChangeEdit implements IUndoableEdit {

        private String name;
        private String oldValue;
        private String newValue;
    
        public AttributeChangeEdit(String name, String oldValue, String newValue) {
            this.name = name;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public boolean combine(IUndoableEdit edit) {
            return false;
        }

        public void undo() throws CannotUndoException {
            Document doc = getDocument();
            try {
                doc.setUndoEnabled(false);
                setAttribute(name, oldValue);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                doc.setUndoEnabled(true);
            }
        }

        public void redo() throws CannotRedoException {
            Document doc = getDocument();
            try {
                doc.setUndoEnabled(false);
                setAttribute(name, newValue);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                doc.setUndoEnabled(true);
            }
        }
    }

}


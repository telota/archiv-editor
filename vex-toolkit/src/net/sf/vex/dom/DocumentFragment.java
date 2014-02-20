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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a fragment of an XML document.
 */
public class DocumentFragment implements Serializable {

    /** 
     * Mime type representing document fragments: 
     * "text/x-vex-document-fragment" 
     */
    public static final String MIME_TYPE = "application/x-vex-document-fragment";
    
    private Content content;
    private Element[] elements;

    /**
     * Class constructor.
     *
     * @param content Content holding the fragment's content.
     * @param elements Elements that make up this fragment.
     */
    public DocumentFragment(Content content, Element[] elements) {
        this.content = content;
	this.elements = elements;
    }

    /**
     * Returns the Content object holding this fragment's content.
     */
    public Content getContent() {
        return this.content;
    }
    
    /**
     * Returns the number of characters, including sentinels, represented
     * by the fragment.
     */
    public int getLength() {
        return this.content.getLength();
    }
    
    /**
     * Returns the elements that make up this fragment.
     */
    public Element[] getElements() {
        return this.elements;
    }
    
    /**
     * Returns an array of element names and Validator.PCDATA representing
     * the content of the fragment.
     */
    public String[] getNodeNames() {
        
        Node[] nodes = this.getNodes();
        String[] names = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Text) {
                names[i] = Validator.PCDATA;
            } else {
                names[i] = ((Element) nodes[i]).getName();
            }
        }
        
        return names;
    }

    /**
     * Returns the nodes that make up this fragment, including 
     * elements and <code>Text</code> objects.
     */
    public Node[] getNodes() {
        return Document.createNodeArray(
            this.getContent(),
            0,
            this.getContent().getLength(),
            this.getElements());
    }
 
 
    //======================================================= PRIVATE
    
    /*
     * Custom Serialization Methods
     */

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(this.content.getString(0, this.content.getLength()));
        out.writeInt(this.elements.length);
        for (int i = 0; i < this.elements.length; i++) {
            this.writeElement(this.elements[i], out);
        }
    }
    
    private void writeElement(Element element, ObjectOutputStream out) 
        throws IOException {
            
        out.writeObject(element.getName());
        out.writeInt(element.getStartOffset());
        out.writeInt(element.getEndOffset());
        String[] attrNames = element.getAttributeNames();
        out.writeInt(attrNames.length);
        for (int i = 0; i < attrNames.length; i++) {
            out.writeObject(attrNames[i]);
            out.writeObject(element.getAttribute(attrNames[i]));
        }
        Element[] children = element.getChildElements();
        out.writeInt(children.length);
        for (int i = 0; i < children.length; i++) {
            this.writeElement(children[i], out);
        }
    }
    
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
            
        String s = in.readUTF();
        this.content = new GapContent(s.length());
        content.insertString(0, s);
        int n = in.readInt();
        this.elements = new Element[n];
        for (int i = 0; i < n; i++) {
            this.elements[i] = this.readElement(in);
        }
    }
    
    private Element readElement(ObjectInputStream in) 
        throws IOException, ClassNotFoundException {
            
        String name = (String) in.readObject();
        int startOffset = in.readInt();
        int endOffset = in.readInt();
        Element element = new Element(name);
        element.setContent(this.content, startOffset, endOffset);
        
        int attrCount = in.readInt();
        for (int i = 0; i < attrCount; i++) {
            String key = (String) in.readObject();
            String value = (String) in.readObject();
            try {
                element.setAttribute(key, value);
            } catch (DocumentValidationException e) {
                // Should never happen; there ain't no document
                e.printStackTrace();
            }
        }
        
        int childCount = in.readInt();
        for (int i = 0; i < childCount; i++) {
            Element child = this.readElement(in);
            child.setParent(element);
            element.insertChild(i, child);
        }
        
        return element;
    }
}

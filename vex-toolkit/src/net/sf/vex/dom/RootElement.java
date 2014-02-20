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

/**
 * The root element of a document. Keeps track of the document to which
 * it is associated. Any element can find the document to which it is
 * associated by following its parents to this root. This would be done,
 * for example, to notify document listeners that the document has changed
 * when the element changes.
 */
public class RootElement extends Element {

    private Document document;
    
    /**
     * Class constructor
     * @param name Name of the element.
     */
    public RootElement(String name) {
        super(name);
    }

    /**
     * @return The document associated with this element.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Sets the document to which this element is associated.
     * This is called by the document constructor, so it need not
     * be called by client code.
     * @param document Document to which this root element is
     * associated.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

}

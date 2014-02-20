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
 * Receives notifications of document changes.
 */
public interface DocumentListener extends java.util.EventListener {

    /**
     * Called when an attribute is changed in one of the document's
     * elements.
     * 
     * @param e the document event.
     */
    public void attributeChanged(DocumentEvent e);
    
    /**
     * Called before content is deleted from a document.
     * 
     * @param e the document event
     */
    public void beforeContentDeleted(DocumentEvent e);
    
    /**
     * Called before content is inserted into a document.
     * 
     * @param e the document event
     */
    public void beforeContentInserted(DocumentEvent e);
    
    /**
     * Called when content is deleted from a document.
     *
     * @param e the document event
     */
    public void contentDeleted(DocumentEvent e);

    /**
     * Called when content is inserted into a document.
     *
     * @param e the document event
     */
    public void contentInserted(DocumentEvent e);

}

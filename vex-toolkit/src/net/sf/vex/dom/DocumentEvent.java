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

import java.util.EventObject;

import net.sf.vex.undo.IUndoableEdit;

/**
 * Encapsulation of the details of a document change
 */
public class DocumentEvent extends EventObject {

    private Document document;
    private Element parentElement;
    private int offset;
    private int length;
    private String attributeName;
    private String oldAttributeValue;
    private String newAttributeValue;
    private IUndoableEdit undoableEdit;

    /**
     * Class constructor.
     *
     * @param document Document that changed.
     * @param parentElement Element containing the change.
     * @param offset offset at which the change occurred.
     * @param length length of the change.
     * @param undoableEdit IUndoableEdit that can be used to undo the change.
     */
    public DocumentEvent(Document document,
                         Element parentElement,
			 int offset,
			 int length,
                         IUndoableEdit undoableEdit) {

        super(document);
        this.document = document;
	this.parentElement = parentElement;
	this.offset = offset;
	this.length = length;
	this.undoableEdit = undoableEdit;
    }


    /**
     * Class constructor used when firing an attributeChanged event.
     * 
     * @param document Document that changed.
     * @param parentElement element containing the attribute that
     * changed
     * @param attributeName name of the attribute that changed
     * @param oldAttributeValue value of the attribute before the
     * change.
     * @param newAttributeValue value of the attribute after the change.
     * @param undoableEdit IUndoableEdit that can be used to undo the change.
     */
    public DocumentEvent(Document document,
                         Element parentElement,
                         String attributeName,
                         String oldAttributeValue,
                         String newAttributeValue,
                         IUndoableEdit undoableEdit) {
    
                             super(document);
        this.document = document;
        this.parentElement = parentElement;
        this.attributeName = attributeName;
        this.oldAttributeValue = oldAttributeValue;
        this.newAttributeValue = newAttributeValue;                         
        this.undoableEdit = undoableEdit;
    }
    
    /**
     * Returns the length of the change.
     */
    public int getLength() {
	return this.length;
    }

    /**
     * Returns the offset at which the change occurred.
     */
    public int getOffset() {
	return this.offset;
    }

    /**
     * Returns the element containing the change.
     */
    public Element getParentElement() {
	return this.parentElement;
    }
    /**
     * @return the value of the attribute before the change.
     * If null, indicates that the attribute was removed.
     */
    public String getNewAttributeValue() {
        return newAttributeValue;
    }

    /**
     * @return the value of the attribute after the change.
     * If null, indicates the attribute did not exist before
     * the change.
     */
    public String getOldAttributeValue() {
        return oldAttributeValue;
    }

    /**
     * @return the name of the attribute that was changed.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * @return the document for which this event was generated
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns the undoable edit that can be used to undo the action.
     * May be null, in which case the action cannot be undone.
     */
    public IUndoableEdit getUndoableEdit() {
        return undoableEdit;
    }
}

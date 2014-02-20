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
package net.sf.vex.layout;

import net.sf.vex.core.Graphics;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.Element;

/**
 * Encapsulation of all the resources needed to create a box
 * tree. Most operations on a box tree, such as creating the tree,
 * painting the tree, and converting between spatial and model
 * coordinates, require the context.
 */
public class LayoutContext {

    private BoxFactory boxFactory;
    private Document document;
    private Graphics graphics;
    private StyleSheet styleSheet;
    private int selectionStart;
    private int selectionEnd;
    private long startTime = System.currentTimeMillis();
    
    /**
     * Class constructor.
     */
    public LayoutContext() {
    }

    /**
     * Returns the BoxFactory used to generate boxes for the layout.
     */
    public BoxFactory getBoxFactory() {
        return boxFactory;
    }

    /**
     * Returns the document being layed out.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns the <code>Graphics</code> object used for layout. Box paint
     * methods use this graphics for painting.
     */
    public Graphics getGraphics() {
	return this.graphics;
    }

    /**
     * Returns the time the layout was started. Actually, it's the time since
     * this context was created, as returned by System.currentTimeMills().
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Returns the <code>StyleSheet</code> used for this layout.
     */
    public StyleSheet getStyleSheet() {
        return this.styleSheet;
    }
    
    
    /**
     * Helper method that returns true if the given element is in the
     * selected range.
     * @param element Element to test. May be null, in which case this method
     * returns false.
     */
    public boolean isElementSelected(Element element) {
        return element != null
            && element.getStartOffset() >= this.getSelectionStart()
            && element.getEndOffset() + 1 <= this.getSelectionEnd();
    }
    
    /**
     * Resets the start time to currentTimeMillis.
     */
    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Sets the BoxFactory used to generate boxes for this layout.
     */
    public void setBoxFactory(BoxFactory factory) {
        boxFactory = factory;
    }

    /**
     * Sets the document being layed out.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Sets the Graphics object used for this layout.
     */
    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    /**
     * Sets the stylesheet used for this layout.
     */
    public void setStyleSheet(StyleSheet sheet) {
        styleSheet = sheet;
    }

    /**
     * Returns the offset where the current selection ends.
     */
    public int getSelectionEnd() {
        return selectionEnd;
    }

    /**
     * Returns the offset where the current selection starts.
     */
    public int getSelectionStart() {
        return selectionStart;
    }

    /**
     * Sets the offset where the current selection ends.
     * @param i the new value for selectionEnd
     */
    public void setSelectionEnd(int i) {
        selectionEnd = i;
    }

    /**
     * Sets the offset where the current selection starts.
     * @param i the new value for selectionStart
     */
    public void setSelectionStart(int i) {
        selectionStart = i;
    }

}


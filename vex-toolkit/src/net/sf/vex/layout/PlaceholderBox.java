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

import net.sf.vex.core.Caret;
import net.sf.vex.core.FontMetrics;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;

/**
 * A zero-width box that represents a single offset in the document.
 */
public class PlaceholderBox extends AbstractBox implements InlineBox {

    private Element element;
    private int relOffset;
    private int textTop;
    private int baseline;
        
    /**
     * Class constructor.
     * @param context LayoutContext in effect.
     * @param element Element containing this placeholder. the element is used
     * both to determine the size of the box and its caret, but also as a base
     * point for relOffset.
     * @param relOffset Offset of the placeholder, relative to the start of
     * the element.
     */
    public PlaceholderBox(LayoutContext context, Element element, int relOffset) {
        
        this.element = element;
        this.relOffset = relOffset;
        
        this.setWidth(0);
        
        Graphics g = context.getGraphics();
        Styles styles = context.getStyleSheet().getStyles(element);
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent();

        int lineHeight = styles.getLineHeight();
        this.textTop = (lineHeight - height) / 2;

        this.baseline = this.textTop + fm.getAscent();
        this.setHeight(lineHeight);
        g.setFont(oldFont);
        font.dispose();
    }
    
    /**
     * @see net.sf.vex.layout.InlineBox#getBaseline()
     */
    public int getBaseline() {
        return this.baseline;
    }

    /**
     * @see net.sf.vex.layout.InlineBox#split(net.sf.vex.layout.LayoutContext, int, boolean)
     */
    public Pair split(LayoutContext context, int maxWidth, boolean force) {
        return new Pair(null, this);
    }

    /**
     * @see net.sf.vex.layout.Box#getCaret(net.sf.vex.layout.LayoutContext, int)
     */
    public Caret getCaret(LayoutContext context, int offset) {
        return new TextCaret(0, this.textTop, this.baseline - this.textTop);
    }

    /**
     * @see net.sf.vex.layout.Box#getElement()
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * @see net.sf.vex.layout.Box#getEndOffset()
     */
    public int getEndOffset() {
        return this.element.getStartOffset() + this.relOffset;
    }

    /**
     * @see net.sf.vex.layout.Box#getStartOffset()
     */
    public int getStartOffset() {
        return this.element.getStartOffset() + this.relOffset;
    }
    
    /**
     * @see net.sf.vex.layout.Box#hasContent()
     */
    public boolean hasContent() {
        return true;
    }

    public boolean isEOL() {
        return false;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[placeholder(" + this.getStartOffset() + ")]";
    }
    /**
     * @see net.sf.vex.layout.Box#viewToModel(net.sf.vex.layout.LayoutContext, int, int)
     */
    public int viewToModel(LayoutContext context, int x, int y) {
        return this.getStartOffset();
    }


}

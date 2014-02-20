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

import java.util.List;

import net.sf.vex.core.IntRange;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;

/**
 * Implements a Block
 */
public class BlockPseudoElementBox extends AbstractBox implements BlockBox {

    private Element pseudoElement;
    private BlockBox parent;
    private ParagraphBox para;
    
    private int marginTop;
    private int marginBottom;
    
    public BlockPseudoElementBox(LayoutContext context, Element pseudoElement, BlockBox parent, int width) {
        
        this.pseudoElement = pseudoElement;
        this.parent = parent;
        
        Styles styles = context.getStyleSheet().getStyles(pseudoElement);
        
        this.marginTop = styles.getMarginTop().get(width);
        this.marginBottom = styles.getMarginBottom().get(width);

        int leftInset = styles.getMarginLeft().get(width)
            + styles.getBorderLeftWidth()
            + styles.getPaddingLeft().get(width);
        int rightInset = styles.getMarginRight().get(width)
            + styles.getBorderRightWidth()
            + styles.getPaddingRight().get(width);

        int childWidth = width - leftInset - rightInset;
        List inlines = LayoutUtils.createGeneratedInlines(context, pseudoElement);
        this.para = ParagraphBox.create(context, pseudoElement, inlines, childWidth);

        this.para.setX(0);
        this.para.setY(0);
        this.setWidth(width - leftInset - rightInset);
        this.setHeight(this.para.getHeight());
    }


    /**
     * Provide children for {@link AbstractBox#paint}.
     * @see net.sf.vex.layout.Box#getChildren()
     */
    public Box[] getChildren() {
        return new Box[] { this.para };    
    }

    /**
     * @see net.sf.vex.layout.Box#getElement()
     */
    public Element getElement() {
        return this.pseudoElement;
    }

    /**
     * @see net.sf.vex.layout.BlockBox#getFirstLine()
     */
    public LineBox getFirstLine() {
        throw new IllegalStateException();
    }

    /**
     * @see net.sf.vex.layout.BlockBox#getLastLine()
     */
    public LineBox getLastLine() {
        throw new IllegalStateException();
    }

    /**
     * @see net.sf.vex.layout.BlockBox#getLineEndOffset(int)
     */
    public int getLineEndOffset(int offset) {
        throw new IllegalStateException();
    }

    /**
     * @see net.sf.vex.layout.BlockBox#getLineStartOffset(int)
     */
    public int getLineStartOffset(int offset) {
        throw new IllegalStateException();
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }
    
    public int getMarginTop() {
        return this.marginTop;
    }
    
    /**
     * @see net.sf.vex.layout.BlockBox#getNextLineOffset(net.sf.vex.layout.LayoutContext, int, int)
     */
    public int getNextLineOffset(LayoutContext context, int offset, int x) {
        throw new IllegalStateException();
    }

    /**
     * Returns this box's parent.
     */
    public BlockBox getParent() {
        return this.parent;
    }
    
    /**
     * @see net.sf.vex.layout.BlockBox#getPreviousLineOffset(net.sf.vex.layout.LayoutContext, int, int)
     */
    public int getPreviousLineOffset(
        LayoutContext context,
        int offset,
        int x) {
            throw new IllegalStateException();
    }

    public IntRange layout(LayoutContext context, int top, int bottom) {
        return null;
    }
    
    public void invalidate(boolean direct) {
        throw new IllegalStateException("invalidate called on a non-element BlockBox");
    }


    /**
     * Draw boxes before painting our child.
     * @see net.sf.vex.layout.Box#paint(net.sf.vex.layout.LayoutContext, int, int)
     */
    public void paint(LayoutContext context, int x, int y) {
        this.drawBox(context, x, y, this.getParent().getWidth(), true);
        super.paint(context, x, y);
    }

    public void setInitialSize(LayoutContext context) {
        // NOP - size calculated in the ctor
    }

}

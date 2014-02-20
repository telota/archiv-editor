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

import net.sf.vex.dom.Element;

/**
 * Represents a line of text and inline images.
 */
public class LineBox extends CompositeInlineBox {

    private Element element;    
    private InlineBox[] children;
    private InlineBox firstContentChild = null;
    private InlineBox lastContentChild = null;
    private int baseline;
                
    /**
     * Class constructor.
     *
     * @param context LayoutContext for this layout. 
     * @param children InlineBoxes that make up this line.
     */
    public LineBox(LayoutContext context, Element element, InlineBox[] children) {

        this.element = element;
        this.children = children;
        
        int height = 0;
        int x = 0;
        this.baseline = 0;
        for (int i = 0; i < children.length; i++) {
            InlineBox child = children[i];
            child.setX(x);
            child.setY(0); // TODO: do proper vertical alignment
            this.baseline = Math.max(this.baseline, child.getBaseline());
            x += child.getWidth();
            height = Math.max(height, child.getHeight());
            if (child.hasContent()) {
                if (this.firstContentChild == null) {
                    this.firstContentChild = child;           
                }
                this.lastContentChild = child;
            }
        }
        
        this.setHeight(height);
        this.setWidth(x);
    }
    
    /**
     * @see net.sf.vex.layout.InlineBox#getBaseline()
     */
    public int getBaseline() {
        return this.baseline;
    }
    
    public Box[] getChildren() {
        return this.children;
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
        return this.lastContentChild.getEndOffset();
    }

    /**
     * @see net.sf.vex.layout.Box#getStartOffset()
     */
    public int getStartOffset() {
        return this.firstContentChild.getStartOffset();
    }

    /**
     * @see net.sf.vex.layout.Box#hasContent()
     */
    public boolean hasContent() {
        return this.firstContentChild != null;
    }
    
    /**
     * @see net.sf.vex.layout.CompositeInlineBox#split(net.sf.vex.layout.LayoutContext, net.sf.vex.layout.InlineBox[], net.sf.vex.layout.InlineBox[])
     */
    public Pair split(LayoutContext context, InlineBox[] lefts, InlineBox[] rights) {
        
        LineBox left = null;
        LineBox right = null;
        
        if (lefts.length > 0) {
            left = new LineBox(context, this.getElement(), lefts);
        }
        
        if (rights.length > 0) {
            right = new LineBox(context, this.getElement(), rights);
        }
        
        return new Pair(left, right);
    }
    
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        Box[] children = this.getChildren();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < children.length; i++) {
            sb.append(children[i]);
        }
        return sb.toString();
    }

    //========================================================== PRIVATE
    

}

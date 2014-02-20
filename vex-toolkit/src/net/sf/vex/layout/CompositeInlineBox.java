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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.sf.vex.core.Caret;
import net.sf.vex.core.FontMetrics;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.css.Styles;

/**
 * InlineBox consisting of several children. This is the parent class 
 * of InlineElementBox and LineBox, and implements the split method.
 */
public abstract class CompositeInlineBox extends AbstractBox implements InlineBox {

    /**
     * Returns true if any of the children have content.
     */
    public boolean hasContent() {
        Box[] children = this.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i].hasContent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isEOL() {
        Box[] children = this.getChildren();
        return children.length > 0 && ((InlineBox) children[children.length-1]).isEOL();
    }

    /**
     * @see net.sf.vex.layout.Box#getCaret(net.sf.vex.layout.LayoutContext, int)
     */
    public Caret getCaret(LayoutContext context, int offset) {

        int x = 0;
        Box[] children = this.getChildren();
        
        // we want the caret to be to the right of any leading static boxes... 
        int start = 0; 
        while (start < children.length && !children[start].hasContent()) {
            x += children[start].getWidth();
            start++;
        }
        
        // ...and to the left of any trailing static boxes
        int end = children.length;
        while (end < 0 && !children[end - 1].hasContent()) {
            end--;
        }
        
        for (int i = start; i < end; i++) {
            Box child = children[i];
            if (child.hasContent()) {
                if (offset < child.getStartOffset()) {
                    break;
                } else if (offset <= child.getEndOffset()) {
                    Caret caret = child.getCaret(context, offset);
                    caret.translate(child.getX(), child.getY());
                    return caret;
                }                
            }
            x += child.getWidth();
        }
        
        Graphics g = context.getGraphics();
        Styles styles = context.getStyleSheet().getStyles(this.getElement());
        
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent();
        g.setFont(oldFont);
        font.dispose();
        
        int lineHeight = styles.getLineHeight();
        int y = (lineHeight - height) / 2;
        return new TextCaret(x, y, height);
    }


    /**
     * @see net.sf.vex.layout.InlineBox#split(net.sf.vex.layout.LayoutContext, int, boolean)
     */
    public Pair split(LayoutContext context, int maxWidth, boolean force) {
        
        // list of children that have yet to be added to the left side
        LinkedList rights = new LinkedList(Arrays.asList(this.getChildren()));

        // pending is a list of inlines we are trying to add to the left side
        // but which cannot end at a split
        List pending = new ArrayList();
        
        // list of inlines that make up the left side
        List lefts = new ArrayList();
        
        int remaining = maxWidth;
        boolean eol = false;
        
        while (!rights.isEmpty() && remaining >= 0) {
            InlineBox inline = (InlineBox) rights.removeFirst();
            InlineBox.Pair pair = inline.split(context, remaining, force && lefts.isEmpty());

            if (pair.getLeft() != null) {
                lefts.addAll(pending);
                pending.clear();
                lefts.add(pair.getLeft());
                remaining -= pair.getLeft().getWidth();
            }
            
            if (pair.getRight() != null) {
                pending.add(pair.getRight());
                remaining -= pair.getRight().getWidth();
            }
            
            if (pair.getLeft() != null && pair.getLeft().isEOL()) { 
                eol = true;
                break;
            }
            
        }
        
        if (((force && lefts.isEmpty()) || remaining >= 0) && !eol) {
            lefts.addAll(pending);
        } else {
            rights.addAll(0, pending);
        }
        
        InlineBox[] leftKids = (InlineBox[]) lefts.toArray(new InlineBox[lefts.size()]);
        InlineBox[] rightKids = (InlineBox[]) rights.toArray(new InlineBox[rights.size()]);
        
        return this.split(context, leftKids, rightKids);
    }

    
    /**
     * Creates a Pair of InlineBoxes, each with its own set of children. 
     * @param context LayoutContext used for this layout.
     * @param lefts Child boxes to be given to the left box.
     * @param rights Child boxes to be given to the right box.
     * @return
     */
    protected abstract Pair split(LayoutContext context, InlineBox[] lefts, InlineBox[] rights);

    /**
     * @see net.sf.vex.layout.Box#viewToModel(net.sf.vex.layout.LayoutContext, int, int)
     */
    public int viewToModel(LayoutContext context, int x, int y) {
        
        if (!this.hasContent()) {
            throw new RuntimeException("Oops. Calling viewToModel on a line with no content");
        }
        
        Box closestContentChild = null;
        int delta = Integer.MAX_VALUE;
        Box[] children = this.getChildren();
        for (int i = 0; i < children.length; i++) {
            Box child = children[i];
            if (child.hasContent()) {
                int newDelta = 0;
                if (x < child.getX()) {
                    newDelta = child.getX() - x;
                } else if (x > child.getX() + child.getWidth()) {
                    newDelta = x - (child.getX() + child.getWidth());
                }
                if (newDelta < delta) {
                    delta = newDelta;
                    closestContentChild = child;
                }
            }
        }
        
        return closestContentChild.viewToModel(
            context, 
            x - closestContentChild.getX(),
            y - closestContentChild.getY());
    }


}

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
import java.util.List;

import net.sf.vex.core.Drawable;
import net.sf.vex.core.FontMetrics;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.core.Rectangle;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.Node;
import net.sf.vex.dom.Text;

/**
 * An inline box that represents an inline element. This box is responsible
 * for creating and laying out its child boxes.
 */
public class InlineElementBox extends CompositeInlineBox {
    
    private Element element;
    private InlineBox[] children;
    private InlineBox firstContentChild = null;
    private InlineBox lastContentChild = null;
    private int baseline;
    private int halfLeading;
    
    

    /**
     * Class constructor, called by the createInlineBoxes static factory method.
     * @param context LayoutContext to use.
     * @param element Element that generated this box
     * @param startOffset Start offset of the range being rendered, which may be arbitrarily
     * before or inside the element.
     * @param endOffset End offset of the range being rendered, which may be arbitrarily
     * after or inside the element.
     */
    private InlineElementBox(LayoutContext context, Element element, int startOffset, int endOffset) {
        
        this.element = element;

        List childList = new ArrayList();

        Styles styles = context.getStyleSheet().getStyles(element);
        
        if (startOffset <= element.getStartOffset()) {

            // space for the left margin/border/padding
            int space = styles.getMarginLeft().get(0) 
                + styles.getBorderLeftWidth() 
                + styles.getPaddingLeft().get(0);
            
            if (space > 0) {
                childList.add(new SpaceBox(space, 1));
            }
            
            // :before content
            Element beforeElement = context.getStyleSheet().getBeforeElement(element);
            if (beforeElement != null) {
                childList.addAll(LayoutUtils.createGeneratedInlines(context, beforeElement));
            }
            
            // left marker
            childList.add(createLeftMarker(element, styles));
        }
        
        InlineBoxes inlines = createInlineBoxes(context, element, startOffset, endOffset);
        childList.addAll(inlines.boxes);
        this.firstContentChild = inlines.firstContentBox;
        this.lastContentChild = inlines.lastContentBox;

        if (endOffset > element.getEndOffset()) {
            
            childList.add(new PlaceholderBox(context, element, element.getEndOffset() - element.getStartOffset()));
            
            // trailing marker        
            childList.add(createRightMarker(element, styles));
            
            // :after content
            Element afterElement = context.getStyleSheet().getAfterElement(element);
            if (afterElement != null) {
                childList.addAll(LayoutUtils.createGeneratedInlines(context, afterElement));
            }
            
            // space for the right margin/border/padding
            int space = styles.getMarginRight().get(0) 
                    + styles.getBorderRightWidth() 
                    + styles.getPaddingRight().get(0);

            if (space > 0) {
                childList.add(new SpaceBox(space, 1));
            }
        }
        
        this.children = (InlineBox[]) childList.toArray(new InlineBox[childList.size()]);
        this.layout(context);
    }
    

    
    
    
    /**
     * Class constructor. This constructor is called by the split method.
     * @param context LayoutContext used for the layout.
     * @param element Element to which this box applies.
     * @param children Child boxes.
     */
    private InlineElementBox(LayoutContext context, Element element, InlineBox[] children) {
        this.element = element;
        this.children = children;
        this.layout(context);
        for (int i = 0; i < children.length; i++) {
            InlineBox child = children[i];        
            if (child.hasContent()) {
                if (this.firstContentChild == null) {
                    this.firstContentChild = child;           
                }
                this.lastContentChild = child;
            }
        }
    }
    
    /**
     * @see net.sf.vex.layout.InlineBox#getBaseline()
     */
    public int getBaseline() {
        return this.baseline;
    }

    /**
     * @see net.sf.vex.layout.Box#getChildren()
     */
    public Box[] getChildren() {
        return this.children;
    }
    
    /**
     * Returns the element associated with this box.
     */
    public Element getElement() {
        return this.element;
    }
    
    /**
     * @see net.sf.vex.layout.Box#getEndOffset()
     */
    public int getEndOffset() {
        if (this.lastContentChild == null) {
            return this.getElement().getEndOffset();
        } else {
            return this.lastContentChild.getEndOffset();
        }
    }

    /**
     * @see net.sf.vex.layout.Box#getStartOffset()
     */
    public int getStartOffset() {
        if (this.firstContentChild == null) {
            return this.getElement().getStartOffset();
        } else {
            return this.firstContentChild.getStartOffset();
        }
    }
    
    /**
     * Override to paint background and borders.
     * @see net.sf.vex.layout.AbstractBox#paint(net.sf.vex.layout.LayoutContext, int, int)
     */
    public void paint(LayoutContext context, int x, int y) {
        this.drawBox(context, x, y, 0, true); // TODO CSS violation
        super.paint(context, x, y);
    }
    


    public Pair split(LayoutContext context, InlineBox[] lefts, InlineBox[] rights) {
        
        InlineElementBox left = null;
        InlineElementBox right = null;
        
        if (lefts.length > 0 || rights.length == 0) {
            left = new InlineElementBox(context, this.getElement(), lefts);
        }
        
        if (rights.length > 0) {
            right = new InlineElementBox(context, this.getElement(), rights);
        }
        
        return new Pair(left, right);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.getStartOffset() == this.getElement().getStartOffset() + 1) {
            sb.append("<");
            sb.append(this.getElement().getName());
            sb.append(">");    
        }
        Box[] children = this.getChildren();
        for (int i = 0; i < children.length; i++) {
            sb.append(children[i]);
        }
        if (this.getEndOffset() == this.getElement().getEndOffset()) {
            sb.append("</");
            sb.append(this.getElement().getName());
            sb.append(">");    
        }
        return sb.toString();
    }

    /**
     * Holds the results of the createInlineBoxes method. 
     */
    static class InlineBoxes {

        /** List of generated boxes */
        public List boxes = new ArrayList();
        
        /** First generated box that has content */
        public InlineBox firstContentBox;
        
        /** Last generated box that has content */
        public InlineBox lastContentBox;
    }
    
    /**
     * Creates a list of inline boxes given a range of offsets. This method is
     * used when creating both ParagraphBoxes and InlineElementBoxes.
     * @param context LayoutContext to be used.
     * @param containingElement Element containing both offsets
     * @param startOffset The start of the range to convert to inline boxes.
     * @param endOffset The end of the range to convert to inline boxes.
     * @return
     */
    static InlineBoxes createInlineBoxes(LayoutContext context, Element containingElement, int startOffset, int endOffset) {
    
        InlineBoxes result = new InlineBoxes();
        
        Node[] nodes = containingElement.getChildNodes();
        for (int i = 0; i < nodes.length; i++) {

            Node node = nodes[i];
            InlineBox child;
            
            if (node.getStartOffset() >= endOffset) {
                break;
            } else if (node instanceof Text) {
                
                // This check is different for Text and Element, so we have to
                // do it here and below, too.
                if (node.getEndOffset() <= startOffset) {
                    continue;
                }
                
                int start = Math.max(startOffset, node.getStartOffset());
                int end = Math.min(endOffset, node.getEndOffset());
                child = new DocumentTextBox(context, containingElement, start, end);
                
            } else {

                if (node.getEndOffset() < startOffset) {
                    continue;
                }
                
                Element childElement = (Element) node;
                InlineBox placeholder = new PlaceholderBox(context, containingElement, childElement.getStartOffset() - containingElement.getStartOffset());
                result.boxes.add(placeholder);
                if (result.firstContentBox == null) {
                    result.firstContentBox = placeholder;
                }
                child = new InlineElementBox(context, childElement, startOffset, endOffset);
            }

            if (result.firstContentBox == null) {
                result.firstContentBox = child;           
            }
            
            result.lastContentBox = child;
            
            result.boxes.add(child);
        }

        return result;
    }

    //========================================================== PRIVATE
    

    private static InlineBox createLeftMarker(Element element, Styles styles) {
        final int size = Math.round(0.5f * styles.getFontSize());
        final int lift = Math.round(0.1f * styles.getFontSize());
        Drawable drawable = new Drawable() {
            public void draw(Graphics g, int x, int y) {
                g.setLineStyle(Graphics.LINE_SOLID);
                g.setLineWidth(1);
                y -= lift;
                g.drawLine(x, y - size, x, y);
                g.drawLine(x, y, x + size - 1, y - size/2);
                g.drawLine(x + size - 1, y - size/2, x, y - size);
            }
            public Rectangle getBounds() {
                return new Rectangle(0, -size, size, size);
            }
        };
        return new DrawableBox(drawable, element, DrawableBox.START_MARKER);
    }
    
    private static InlineBox createRightMarker(Element element, Styles styles) {
        final int size = Math.round(0.5f * styles.getFontSize());
        final int lift = Math.round(0.1f * styles.getFontSize());
        Drawable drawable = new Drawable() {
            public void draw(Graphics g, int x, int y) {
                g.setLineStyle(Graphics.LINE_SOLID);
                g.setLineWidth(1);
                y -= lift;
                g.drawLine(x + size - 1, y - size, x + size - 1, y);
                g.drawLine(x + size - 1, y, x, y - size/2);
                g.drawLine(x, y - size/2, x + size - 1, y - size);
            }
            public Rectangle getBounds() {
                return new Rectangle(0, -size, size, size);
            }
        };
        return new DrawableBox(drawable, element, DrawableBox.END_MARKER);
    }
    
    
    private void layout(LayoutContext context) {
        Graphics g = context.getGraphics();
        Styles styles = context.getStyleSheet().getStyles(element);
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        this.setHeight(styles.getLineHeight());
        this.halfLeading = (styles.getLineHeight() - fm.getAscent() - fm.getDescent()) / 2;
        this.baseline = this.halfLeading + fm.getAscent();
        g.setFont(oldFont);
        font.dispose();
        
        int x = 0;
        for (int i = 0; i < this.children.length; i++) {
             InlineBox child = this.children[i];
             // TODO: honour the child's vertical-align property
             child.setX(x);
             child.setY(this.baseline - child.getBaseline());
             x += child.getWidth();
        }

        this.setWidth(x);
    }
    


}

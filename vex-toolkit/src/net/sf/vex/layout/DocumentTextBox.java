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

import net.sf.vex.core.ColorResource;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.Text;

/**
 * A TextBox that gets its text from the document.
 * Represents text which is editable within the VexWidget. 
 */
public class DocumentTextBox extends TextBox {

    private int startRelative;
    private int endRelative;

    /**
     * Class constructor, accepting a Text object.
     * @param context LayoutContext in use
     * @param element Element being used 
     * @param text
     */
    public DocumentTextBox(LayoutContext context, Element element, Text text) {
        this(context, element, text.getStartOffset(), text.getEndOffset());
    }
    
    /**
     * Class constructor.
     * 
     * @param context LayoutContext used to calculate the box's size.
     * @param element Element that directly contains the text.
     * @param startOffset start offset of the text
     * @param endOffset end offset of the text
     */
    public DocumentTextBox(LayoutContext context, Element element, int startOffset, int endOffset) {
        super(element);
        
        if (startOffset >= endOffset) {
            throw new IllegalStateException("DocumentTextBox: startOffset (" + startOffset + ") >= endOffset (" + endOffset + ")");
        }
        
        this.startRelative = startOffset - element.getStartOffset();
        this.endRelative = endOffset - element.getStartOffset();
        this.calculateSize(context);
        
        if (this.getText().length() < (endOffset - startOffset)) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * @see net.sf.vex.layout.Box#getEndOffset()
     */
    public int getEndOffset() {
        if (this.endRelative == -1) {
            return -1;
        } else {
            return this.getElement().getStartOffset() + this.endRelative - 1;
        }
    }

    /**
     * @see net.sf.vex.layout.Box#getStartOffset()
     */
    public int getStartOffset() {
        if (this.startRelative == -1) {
            return -1;
        } else {
            return this.getElement().getStartOffset() + this.startRelative;
        }
    }
    
    /**
     * @see net.sf.vex.layout.TextBox#getText()
     */
    public String getText() {
        Document doc = this.getElement().getDocument(); 
        return doc.getText(this.getStartOffset(), this.getEndOffset() + 1);
    }

    /**
     * @see net.sf.vex.layout.Box#hasContent()
     */
    public boolean hasContent() {
        return true;
    }


    /**
     * @see net.sf.vex.layout.Box#paint(net.sf.vex.layout.LayoutContext, int, int)
     */
    public void paint(LayoutContext context, int x, int y) {
        
        Styles styles = context.getStyleSheet().getStyles(this.getElement());
        Graphics g = context.getGraphics();
        
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        ColorResource foreground = g.createColor(styles.getColor());
        ColorResource oldForeground = g.setColor(foreground);
//        ColorResource background = g.createColor(styles.getBackgroundColor());
//        ColorResource oldBackground = g.setBackgroundColor(background);
        
        char[] chars = this.getText().toCharArray();
        
        if (chars.length < this.getEndOffset() - this.getStartOffset()) {
            throw new IllegalStateException();
        }
        
        if (chars.length == 0) {
            throw new IllegalStateException();
        }
        
        int start = 0;
        int end = chars.length;
        if (chars[end - 1] == NEWLINE_CHAR) {
            end--;
        }
        int selStart = context.getSelectionStart() - this.getStartOffset();
        selStart = Math.min(Math.max(selStart, start), end);
        int selEnd = context.getSelectionEnd() - this.getStartOffset();
        selEnd = Math.min(Math.max(selEnd, start), end);
        
        // text before selection
        if (start < selStart) {
            g.drawChars(chars, start, selStart - start, x, y);
            String s = new String(chars, start, selStart - start);
            paintTextDecoration(context, styles, s, x, y);
        }
        
        // text after selection
        if (selEnd < end) {
            int x1 = x + g.charsWidth(chars, 0, selEnd);
            g.drawChars(chars, selEnd, end - selEnd, x1, y);
            String s = new String(chars, selEnd, end - selEnd);
            paintTextDecoration(context, styles, s, x1, y);
        }
        
        // text within selection
        if (selStart < selEnd) {
            String s = new String(chars, selStart, selEnd - selStart);
            int x1 = x + g.charsWidth(chars, 0, selStart);
            this.paintSelectedText(context, s, x1, y);
            paintTextDecoration(context, styles, s, x1, y);
        }

        g.setFont(oldFont);
        g.setColor(oldForeground);
//        g.setBackgroundColor(oldBackground);
        font.dispose();
        foreground.dispose();
//        background.dispose();
    }
    
    
    /**
     * @see net.sf.vex.layout.TextBox#splitAt(int)
     */
    public Pair splitAt(LayoutContext context, int offset) {

        if (offset < 0 || offset > (this.endRelative - this.startRelative)) {
            throw new IllegalStateException();
        }
        
        int split = this.getStartOffset() + offset;
                
        DocumentTextBox left;
        if (offset == 0) {
            left = null; 
        } else {
            left = new DocumentTextBox(context, this.getElement(), this.getStartOffset(), split);
        }
        
        InlineBox right;
        if (split == this.getEndOffset() + 1) {
            right = null; 
        } else {
            right = new DocumentTextBox(context, this.getElement(), split, this.getEndOffset() + 1);
        }
        return new Pair(left, right);
    }

    /**
     * @see net.sf.vex.layout.Box#viewToModel(net.sf.vex.layout.LayoutContext, int, int)
     */
    public int viewToModel(LayoutContext context, int x, int y) {
        
        Graphics g = context.getGraphics();
        Styles styles = context.getStyleSheet().getStyles(this.getElement());
        FontResource font = g.createFont(styles.getFont());
        FontResource oldFont = g.setFont(font);
        char[] chars = this.getText().toCharArray();
        
        if (this.getWidth() <= 0) {
            return this.getStartOffset();
        }
        
        // first, get an estimate based on x / width
        int offset = (x / this.getWidth()) * chars.length;
        offset = Math.max(0, offset);
        offset = Math.min(chars.length, offset);
        
        int delta = Math.abs(x - g.charsWidth(chars, 0, offset));

        // Search backwards
        while (offset > 0) {
            int newDelta = Math.abs(x - g.charsWidth(chars, 0, offset-1));
            if (newDelta > delta) {
                break;
            }
            delta = newDelta;
            offset--;
        }
        
        // Search forwards
        while (offset < chars.length-1) {
            int newDelta = Math.abs(x - g.charsWidth(chars, 0, offset+1));
            if (newDelta > delta) {
                break;
            }
            delta = newDelta;
            offset++;
        }

        g.setFont(oldFont);
        font.dispose();
        return this.getStartOffset() + offset;
    }

    
}

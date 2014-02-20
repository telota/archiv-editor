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

import net.sf.vex.core.IntRange;


/**
 * Represents a block box. Block boxes are stacked one on top of another.
 */
public interface BlockBox extends Box {
    
    /**
     * Returns the first LineBox contained by this block, or null if the block
     * contains no lines.
     */
    public LineBox getFirstLine();
    
    /**
     * Returns the last LineBox contained by this block, or null if the block
     * contains no lines.
     */
    public LineBox getLastLine();
    
    /**
     * Returns the offset of the end of the line containing the given offset.
     * @param offset offset identifying the current line.
     */
    public int getLineEndOffset(int offset);
    
    /**
     * Returns the offset of the start of the line containing the given offset.
     * @param offset offset identifying the current line.
     */
    public int getLineStartOffset(int offset);

    /**
     * Returns the bottom margin of this box.
     */
    public int getMarginBottom();
    
    /**
     * Returns the top margin of this box.
     */
    public int getMarginTop();

    /**
     * Returns the offset on the next line that is closest to the given 
     * x coordinate. The given offset may be before the start of this box
     * in which case this method should return the offset of the first line
     * in this box.
     * @param context LayoutContext used for the layout
     * @param offset the current offset
     * @param x the x coordinate
     */
    public int getNextLineOffset(LayoutContext context, int offset, int x);
    
    /**
     * Returns the offset on the previous line that is closest to the given 
     * x coordinate. The given offset may be after the end of this box
     * in which case this method should return the offset of the last line
     * in this box.
     * @param context LayoutContext used for the layout
     * @param offset the current offset
     * @param x the x coordinate
     */
    public int getPreviousLineOffset(LayoutContext context, int offset, int x);
    
    /**
     * Returns the parent box of this box.
     */
    public BlockBox getParent();
    
    /**
     * Informs this box that its layout has become invalid, and that it should
     * re-do it the next time layout is called.
     * @param direct If true, the box's content has changed and it must re-create
     * it's children on the next call to layout. Otherwise, it should just 
     * propagate the next layout call to its children.
     */
    public void invalidate(boolean direct);

    /**
     * Layout this box. This method is responsible for the following.
     * 
     * <ul>
     * <li>Creating any child boxes.</li>
     * <li>Calling layout on the child boxes.</li>
     * <li>Positioning the child boxes (i.e. calling child.setX() and child.setY())</li>
     * <li>Determining this box's height and width.</li>
     * </ul>
     * 
     * <p>Boxes with no children should simply calculate their width and height
     * here</p>
     * 
     * <p>This method is passed a vertical range to be layed out. Children
     * falling outside this range need not be layed out.</p>
     * 
     * <p>This method returns an IntRange object representing the vertical
     * range to re-draw due to layout change. Null may be returned if there
     * were no changes that need to be re-drawn.</p>
     * 
     * @param context The layout context to be used.
     * @param top Top of the range to lay out.
     * @param bottom Bottom of the range to lay out.
     */    
    public IntRange layout(LayoutContext context, int top, int bottom);
    
    /**
     * Sets the initial size of the box.
     * 
     * @param context LayoutContext to use.
     */
    public void setInitialSize(LayoutContext context);
}



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
package net.sf.vex.core;

/**
 * Toolkit-independent insets.
 */
public class Insets {
    
    private int top;
    private int left;
    private int bottom;
    private int right;

    /** Zero insets */
    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
    
    /**
     * Class constructor.
     * 
     * @param top Top inset.
     * @param left Left inset.
     * @param bottom Bottom inset.
     * @param right Right inset.
     */
    public Insets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * @return Returns the top.
     */
    public int getTop() {
        return top;
    }

    /**
     * @return Returns the left.
     */
    public int getLeft() {
        return left;
    }

    /**
     * @return Returns the bottom.
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * Returns the right inset.
     */
    public int getRight() {
        return right;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(80);
        sb.append(Insets.class.getName());
        sb.append("[top=");
        sb.append(this.getTop());
        sb.append(",left=");
        sb.append(this.getLeft());
        sb.append(",bottom=");
        sb.append(this.getBottom());
        sb.append(",right=");
        sb.append(this.getRight());
        sb.append("]");
        return sb.toString();
    }

}

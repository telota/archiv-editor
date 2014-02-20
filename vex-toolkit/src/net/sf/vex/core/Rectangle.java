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
 * Toolkit-independent rectangle.
 */
public class Rectangle {
    
    private int x;
    private int y;
    private int width;
    private int height;
    
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public boolean intersects(Rectangle rect) {
        return rect.x < this.x + this.width
            && rect.x + rect.width > this.x
            && rect.y < this.y + this.height
            && rect.y + rect.height > this.y;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(80);
        sb.append(Rectangle.class.getName());
        sb.append("[x=");
        sb.append(this.getX());
        sb.append(",y=");
        sb.append(this.getY());
        sb.append(",width=");
        sb.append(this.getWidth());
        sb.append(",height=");
        sb.append(this.getHeight());
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return Returns the x.
     */
    public int getX() {
        return x;
    }

    /**
     * @return Returns the y.
     */
    public int getY() {
        return y;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Returns the height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns a Rectangle that is the union of this rectangle with another.
     * @param rect Rectangle with which to union this one.
     */
    public Rectangle union(Rectangle rect) {
        int left = Math.min(this.x, rect.x);
        int top = Math.min(this.y, rect.y);
        int right = Math.max(this.x + this.width, rect.x + rect.width);
        int bottom = Math.max(this.y + this.height, rect.y + rect.height);
        return new Rectangle(left, top, right - left, bottom - top);
    }
}

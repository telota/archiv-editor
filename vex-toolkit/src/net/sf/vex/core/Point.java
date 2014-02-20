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
 * Toolkit-independent point.
 */
public class Point {
    
    private int x;
    private int y;

    /**
     * Class constructor.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(80);
        sb.append(Point.class.getName());
        sb.append("[x=");
        sb.append(this.getX());
        sb.append(",y=");
        sb.append(this.getY());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     */
    public int getY() {
        return y;
    }

}

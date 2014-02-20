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
 * Toolkit-independent representation of a color. Colors consist of three
 * integers in the range 0..255 representing red, green, and blue components.
 * Objects of this class are immutable.
 */
public class Color {

    public static final Color BLACK = new Color(0, 0, 0);
    
    private int red;
    private int green;
    private int blue;
    
    /**
     * Class constructor.
     *
     * @param red red value, 0..255
     * @param green green value, 0..255
     * @param blue blue value, 0..255
     */
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Returns the blue component of the color, in the range 0..255
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Returns the green component of the color, in the range 0..255
     */
    public int getGreen() {
        return green;
    }

    /**
     * Returns the red component of the color, in the range 0..255
     */
    public int getRed() {
        return red;
    }
    
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Color c = (Color) o;
        return this.red == c.red && this.green == c.green && this.blue == c.blue; 
    }

    public int hashCode() {
        return this.red + this.green << 16 + this.blue << 24; 
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(20);
        sb.append("Color[r=");
        sb.append(this.red);
        sb.append(",g=");
        sb.append(this.green);
        sb.append(",b=");
        sb.append(this.blue);
        sb.append("]");
        return sb.toString();
    }

}

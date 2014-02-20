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
 * Represents the caret, a line that indicates an insertion point in the 
 * document.
 */
public abstract class Caret {

    private int x;
    private int y;
    
    /**
     * Class constructor
     * @param x x-coordinate of the top left corner of the caret
     * @param y y-coordinate of the top left corner of the caret
     */
    public Caret(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Draws the caret in the given Graphics context.
     * @param g Graphics within which the caret should be drawn.
     * @param color Color with which the caret should be drawn.
     */
    public abstract void draw(Graphics g, Color color);
    
    /**
     * Returns the smallest rectangle that completely encloses the caret.
     */
    public abstract Rectangle getBounds();
    
    /**
     * Returns the x-coordinate of the top left corner of the caret
     */
    public int getX() {
        return this.x;
    }
   
    /**
     * Returns the y-coordinate of the top left corner of the caret
     */
    public int getY() {
        return this.y;
    }
   
    /**
     * Moves the caret by the given x and y distance.
     * @param x amount by which to move the caret to the right
     * @param y amount by which to move the caret down
     */
    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }
}

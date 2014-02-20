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
 * An object that can be drawn into a Graphics.
 */
public interface Drawable {
    
    /**
     * Draw the object.
     * @param g Graphics into which to draw the object.
     * @param x x-coordinate where the object should be drawn
     * @param y y-coordinate where the object should be drawn
     */
    public void draw(Graphics g, int x, int y);

    /**
     * Returns the smallest rectangle that completely encloses the 
     * drawn shape.
     */
    public Rectangle getBounds();
    
}

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

import net.sf.vex.core.Caret;
import net.sf.vex.core.Color;
import net.sf.vex.core.ColorResource;
import net.sf.vex.core.Graphics;
import net.sf.vex.core.Rectangle;

/**
 * A horizontal caret representing the insertion point between two block boxes.
 */
public class HCaret extends Caret {

    private static final int LINE_WIDTH = 2;
    
    /**
     * Class constructor.
     * @param x x-coordinate of the top left corner of the caret
     * @param y y-coordinate of the top left corner of the caret
     * @param length Horizontal length of the caret.
     */
    public HCaret(int x, int y, int length) {
        super(x, y);
        this.length = length;
    }
    
    public void draw(Graphics g, Color color) {
		// XXX RAP extra
        ColorResource newColor = g.createColor(color);
        ColorResource oldColor = g.setColor(newColor);
        g.fillRect(this.getX(), this.getY(), this.length, LINE_WIDTH);
        g.setColor(oldColor);
        newColor.dispose();
    }    

    /**
     * @see net.sf.vex.core.Caret#getBounds()
     */
    public Rectangle getBounds() {
        return new Rectangle(this.getX(), this.getY(), this.length, LINE_WIDTH);
    }
    
    
    //====================================================== PRIVATE
    
    private int length;
}

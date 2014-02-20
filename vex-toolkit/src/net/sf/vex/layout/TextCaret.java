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
 * A caret drawn as a vertical line between characters.
 */
public class TextCaret extends Caret {

    private static final int LINE_WIDTH = 2;

    private int height;
    
    /**
     * Class constructor
     * @param x x-coordinate of the caret
     * @param y y-coordinate of the top of the caret
     * @param height height of the caret
     */
    public TextCaret(int x, int y, int height) {
        super(x, y);
        this.height = height;
    }
    
    public void draw(Graphics g, Color color) {
        ColorResource newColor = g.createColor(color);
        ColorResource oldColor = g.setColor(newColor);
        g.fillRect(this.getX(), this.getY(), LINE_WIDTH, height);
        g.setColor(oldColor);
        newColor.dispose();
    }
    
    public Rectangle getBounds() {
        return new Rectangle(this.getX(), this.getY(), LINE_WIDTH, height);    
    }
}

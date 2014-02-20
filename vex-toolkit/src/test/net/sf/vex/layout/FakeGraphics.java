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
package test.net.sf.vex.layout;

import net.sf.vex.core.Color;
import net.sf.vex.core.ColorResource;
import net.sf.vex.core.DisplayDevice;
import net.sf.vex.core.FontMetrics;
import net.sf.vex.core.FontResource;
import net.sf.vex.core.FontSpec;
import net.sf.vex.core.Graphics;
import net.sf.vex.core.Rectangle;

/**
 * A pseudo-Graphics class that returns a known set of font metrics.
 */
public class FakeGraphics implements Graphics {

    private int charWidth = 6;
    
    public FakeGraphics() {
        DisplayDevice.setCurrent(new DisplayDevice() {
            public int getHorizontalPPI() {
                return 72;
            }
            public int getVerticalPPI() {
                return 72;
            }
        });
    }

    private FontMetrics fontMetrics = new FontMetrics() {
        public int getAscent() {
            return 10;
        }
        public int getDescent() {
            return 3;
        }
        public int getHeight() {
            return 13;
        }
        public int getLeading() {
            return 2;
        }
    };
    
    public int charsWidth(char[] data, int offset, int length) {
        return length * charWidth;
    }

    public ColorResource createColor(Color rgb) {
        return new ColorResource() {
            public void dispose() {
            }
        };
    }

    public FontResource createFont(FontSpec fontSpec) {
        return new FontResource() {
            public void dispose() {
            }
        };
    }

    public void dispose() {
    }

    public void drawChars(char[] chars, int offset, int length, int x, int y) {
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
    }

    public void drawString(String s, int x, int y) {
    }

    public void drawOval(int x, int y, int width, int height) {
    }

    public void drawRect(int x, int y, int width, int height) {
    }

    public void fillOval(int x, int y, int width, int height) {
    }

    public void fillRect(int x, int y, int width, int height) {
    }

    public Rectangle getClipBounds() {
        return null;
    }

    public ColorResource getBackgroundColor() {
        return null;
    }

    public ColorResource getColor() {
        return null;
    }

    public FontResource getFont() {
        return null;
    }

    public int getLineStyle() {
        return 0;
    }

    public int getLineWidth() {
        return 0;
    }

    public ColorResource getSystemColor(int id) {
        return null;
    }

    public FontMetrics getFontMetrics() {
        return this.fontMetrics;
    }

    public boolean isAntiAliased() {
        return false;
    }

    public void setAntiAliased(boolean antiAliased) {
    }

    public ColorResource setBackgroundColor(ColorResource color) {
        return null;
    }

    public ColorResource setColor(ColorResource color) {
        return null;
    }

    public FontResource setFont(FontResource font) {
        return null;
    }

    public void setLineStyle(int style) {
    }

    public void setLineWidth(int width) {
    }

    public int stringWidth(String s) {
        return charWidth * s.length();
    }

    public int getCharWidth() {
        return this.charWidth;
    }

    public void setXORMode(boolean xorMode) {
        // TODO Auto-generated method stub
        
    }
}

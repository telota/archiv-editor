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
 * Interface through which Vex performs graphics operations. Implemented
 * by adapters to the java.awt.Graphics and org.eclipse.swt.graphics.GC
 * classes.
 */
public interface Graphics {
    
    public static final int LINE_SOLID = 0;
    public static final int LINE_DASH = 1;
    public static final int LINE_DOT = 2;
    
    public int charsWidth(char[] data, int offset, int length);
    public ColorResource createColor(Color rgb);
    public FontResource createFont(FontSpec fontSpec);
    public void dispose();
    public void drawChars(char[] chars, int offset, int length, int x, int y);
    public void drawLine(int x1, int y1, int x2, int y2);
    
    /**
     * Draw the given string at the given point using the current font.
     * @param s string to draw
     * @param x x-coordinate of the top left corner of the text box
     * @param y y-coordinate of the top left corner of the text box
     */
    public void drawString(String s, int x, int y);
    public void drawOval(int x, int y, int width, int height);
    public void drawRect(int x, int y, int width, int height);
    public void fillOval(int x, int y, int width, int height);
    public void fillRect(int x, int y, int width, int height);
    public Rectangle getClipBounds();
    public ColorResource getColor();
    public FontResource getFont();
    public int getLineStyle();
    public int getLineWidth();
    public ColorResource getSystemColor(int id);
    public FontMetrics getFontMetrics();
    public boolean isAntiAliased();
    public void setAntiAliased(boolean antiAliased);
    public ColorResource setColor(ColorResource color);
    public FontResource setFont(FontResource font);

	public void setLineStyle(int style);
    public void setLineWidth(int width);
    
    public int stringWidth(String s);
    
}

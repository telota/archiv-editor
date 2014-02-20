/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *     Dave Holroyd - Implement text decoration
 *******************************************************************************/
package net.sf.vex.core;

/**
 * Toolkit-independent specifier of a font. This class does not encapsulate
 * an actual font, but simply the information needed
 * for the toolkit to find an actual font.
 * 
 * <p>An array of font family names may be specified. If more than one name
 * is specified, the toolkit should select the first name that matches an
 * actual font on the platform.</p>
 */
public class FontSpec {
    
    public static final int PLAIN         = 0x0;
    public static final int BOLD          = 1<<0;
    public static final int ITALIC        = 1<<1;
    public static final int UNDERLINE     = 1<<2;
    public static final int OVERLINE      = 1<<3;
    public static final int LINE_THROUGH  = 1<<4;

    private String[] names;
    private float size;
    private int style;

    /**
     * Class constructor.
     * @param names Array of names of the font family.
     * @param style Bitwise-OR of the applicable style flages, e.g. BOLD | ITALIC
     * @param size Size of the font, in points.
     */
    public FontSpec(String[] names, int style, float size) {
        this.names = names;
        this.style = style;
        this.size = size;
    }
    
    /**
     * Returns the names of the font families that match the font.
     */
    public String[] getNames() {
        return names;
    }

    /**
     * Returns the size of the font in points.
     */
    public float getSize() {
        return size;
    }

    /**
     * Returns a bitwise-OR of the style flags. The following sample checks if
     * the font is bold.
     * 
     * <pre>
     * if (font.getStyle | VexFont.BOLD) {
     *     // do something bold...
     * }
     * </pre>
     */
    public int getStyle() {
        return style;
    }

}

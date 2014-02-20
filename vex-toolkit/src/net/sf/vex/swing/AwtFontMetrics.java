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
package net.sf.vex.swing;

import net.sf.vex.core.FontMetrics;

/**
 * Wrapper for the AWT FontMetrics class.
 */
public class AwtFontMetrics implements FontMetrics {
    
    private java.awt.FontMetrics awtFontMetrics;

    public AwtFontMetrics(java.awt.FontMetrics awtFontMetrics) {
        this.awtFontMetrics = awtFontMetrics;
    }
    
    /**
     * @see net.sf.vex.core.FontMetrics#getAscent()
     */
    public int getAscent() {
        return this.awtFontMetrics.getAscent();
    }

    /**
     * @see net.sf.vex.core.FontMetrics#getDescent()
     */
    public int getDescent() {
        return this.awtFontMetrics.getDescent();
    }

    /**
     * @see net.sf.vex.core.FontMetrics#getHeight()
     */
    public int getHeight() {
        return this.awtFontMetrics.getHeight();
    }

    /**
     * @see net.sf.vex.core.FontMetrics#getLeading()
     */
    public int getLeading() {
        return this.awtFontMetrics.getLeading();
    }

}

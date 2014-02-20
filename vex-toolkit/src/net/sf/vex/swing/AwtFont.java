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

import net.sf.vex.core.FontResource;

/**
 * Wrapper for the AWT Font class.
 */
public class AwtFont implements FontResource {

    private java.awt.Font awtFont;
    
    public AwtFont(java.awt.Font awtFont) {
        this.awtFont = awtFont;
    }
    
    java.awt.Font getAwtFont() {
        return this.awtFont;
    }
    
    public void dispose() {
    }
}

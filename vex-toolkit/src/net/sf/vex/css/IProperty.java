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
package net.sf.vex.css;

import org.w3c.css.sac.LexicalUnit;

/**
 * Represents a CSS property.
 */
public interface IProperty {
    
    /** Constant indicating the length is along the horizontal axis. */
    public static final byte AXIS_HORIZONTAL = 0;

    /** Constant indicating the length is along the vertical axis. */
    public static final byte AXIS_VERTICAL = 1;


    /**
     * Returns the name of the property.
     */
    public String getName();

    /**
     * Calculates the value of a property given a LexicalUnit.
     * @param lu LexicalUnit to interpret.
     * @param parentStyles Styles of the parent element. These are used
     * when the property inherits a value.
     * @param styles Styles currently in effect. Often, the calculated
     * value depends on previously calculated styles such as font size 
     * and color.
     */
    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles);

}

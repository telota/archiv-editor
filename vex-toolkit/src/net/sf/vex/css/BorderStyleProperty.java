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
 * The border-XXX-style CSS property.
 */
public class BorderStyleProperty extends AbstractProperty {

    /**
     * Class constructor.
     * @param name Name of the property.
     */
    public BorderStyleProperty(String name) {
        super(name);
    }

    /**
     * Returns true if the given lexical unit represents a border style.
     *
     * @param lu LexicalUnit to check.
     */
    public static boolean isBorderStyle(LexicalUnit lu) {
        if (lu == null) {
            return false;
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            return s.equals(CSS.NONE)
            || s.equals(CSS.HIDDEN)
            || s.equals(CSS.DOTTED)
            || s.equals(CSS.DASHED)
            || s.equals(CSS.SOLID)
            || s.equals(CSS.DOUBLE)
            || s.equals(CSS.GROOVE)
            || s.equals(CSS.RIDGE)
            || s.equals(CSS.INSET)
            || s.equals(CSS.OUTSET);
        }
        
        return false;
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        if (isBorderStyle(lu)) {
            return lu.getStringValue();
        } else if (isInherit(lu) && parentStyles != null) {
            return parentStyles.get(this.getName());
        } else {
            return CSS.NONE;
        }
    }

}

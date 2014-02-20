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
 * The CSS text-decoration property.
 */
public class TextDecorationProperty extends AbstractProperty {

    /**
     * Class constructor.
     */
    public TextDecorationProperty() {
        super(CSS.TEXT_DECORATION);
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        if (isTextDecoration(lu)) {
            return lu.getStringValue();
        } else {
            // not specified, "inherit", or some other value
            if (parentStyles != null) {
                return parentStyles.getTextDecoration();
            } else {
                return CSS.NONE;
            }
        }
    }

    //=================================================== PRIVATE
    
    /**
     * Returns true if the given lexical unit represents a text decoration.
     *
     * @param lu LexicalUnit to check.
     */
    private static boolean isTextDecoration(LexicalUnit lu) {
        if (lu == null) {
            return false;
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            return s.equals(CSS.NONE)
                || s.equals(CSS.UNDERLINE)
                || s.equals(CSS.OVERLINE)
                || s.equals(CSS.LINE_THROUGH)
                || s.equals(CSS.BLINK);
        } else {
            return false;
        }
    }

}

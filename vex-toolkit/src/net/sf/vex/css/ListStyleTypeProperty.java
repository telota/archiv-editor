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
 * The CSS list-style-type property.
 */
public class ListStyleTypeProperty extends AbstractProperty {

    public ListStyleTypeProperty() {
        super(CSS.LIST_STYLE_TYPE);
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        if (isListStyleType(lu)) {
            return lu.getStringValue();
        } else {
            if (parentStyles == null) {
                return CSS.DISC;
            } else {
                return parentStyles.getListStyleType();
            }
        }
        
    }

    private static boolean isListStyleType(LexicalUnit lu) {
        
        if (lu == null || lu.getLexicalUnitType() != LexicalUnit.SAC_IDENT) {
            return false;
        }
        
        String s = lu.getStringValue();
        return s.equals(CSS.ARMENIAN)
            || s.equals(CSS.CIRCLE)
            || s.equals(CSS.CJK_IDEOGRAPHIC)
            || s.equals(CSS.DECIMAL)
            || s.equals(CSS.DECIMAL_LEADING_ZERO)
            || s.equals(CSS.DISC)
            || s.equals(CSS.GEORGIAN)
            || s.equals(CSS.HEBREW)
            || s.equals(CSS.HIRAGANA)
            || s.equals(CSS.HIRAGANA_IROHA)
            || s.equals(CSS.KATAKANA)
            || s.equals(CSS.KATAKANA_IROHA)
            || s.equals(CSS.LOWER_ALPHA)
            || s.equals(CSS.LOWER_GREEK)
            || s.equals(CSS.LOWER_LATIN)
            || s.equals(CSS.LOWER_ROMAN)
            || s.equals(CSS.NONE)
            || s.equals(CSS.SQUARE)
            || s.equals(CSS.UPPER_ALPHA)
            || s.equals(CSS.UPPER_LATIN)
            || s.equals(CSS.UPPER_ROMAN);
    }

}

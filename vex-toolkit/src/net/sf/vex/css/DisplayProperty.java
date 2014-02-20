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
 * The CSS 'display' property.
 */
public class DisplayProperty extends AbstractProperty {

    /**
     * Class constructor.
     */
    public DisplayProperty() {
        super(CSS.DISPLAY);
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles,
            Styles styles) {
        
        if (isDisplay(lu)) {
            return lu.getStringValue();
        } else if (isInherit(lu) && parentStyles != null) {
            return parentStyles.getDisplay();
        } else {
            // not specified or other unknown value
            return CSS.INLINE;
        }
    }

    //======================================================== PRIVATE
    
    /**
     * Returns true if the value of the given LexicalUnit represents 
     * a valid value for this property.
     * @param lu LexicalUnit to inspect.
     */
    private static boolean isDisplay(LexicalUnit lu) {
        if (lu == null) {
            return false; 
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            return s.equals(CSS.BLOCK)
            || s.equals(CSS.INLINE)
            || s.equals(CSS.INLINE_BLOCK)
            || s.equals(CSS.INLINE_TABLE)
            || s.equals(CSS.LIST_ITEM)
            || s.equals(CSS.NONE)
            || s.equals(CSS.RUN_IN)
            || s.equals(CSS.TABLE)
            || s.equals(CSS.TABLE_CAPTION)
            || s.equals(CSS.TABLE_CELL)
            || s.equals(CSS.TABLE_COLUMN)
            || s.equals(CSS.TABLE_COLUMN_GROUP)
            || s.equals(CSS.TABLE_FOOTER_GROUP)
            || s.equals(CSS.TABLE_HEADER_GROUP)
            || s.equals(CSS.TABLE_ROW)
            || s.equals(CSS.TABLE_ROW_GROUP);
        } else {
            return false;
        }
    }
    
}

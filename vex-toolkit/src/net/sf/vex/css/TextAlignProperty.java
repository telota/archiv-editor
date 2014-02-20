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
 * The CSS text-align property.
 */
public class TextAlignProperty extends AbstractProperty {

    /**
     * Class constructor
     */
    public TextAlignProperty() {
        super(CSS.TEXT_ALIGN);
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        if (TextAlignProperty.isTextAlign(lu)) {
            return lu.getStringValue();
        } else {
            // not specified, "inherit", or some other value
            if (parentStyles != null) {
                return parentStyles.getTextAlign();
            } else {
                return CSS.LEFT;
            }
        }

    }

    //=================================================== PRIVATE
    
    private static boolean isTextAlign(LexicalUnit lu) {
        if (lu == null) {
            return false; 
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            return true;
        } else {
            return false;
        }
    }

}

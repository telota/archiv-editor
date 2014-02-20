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

import net.sf.vex.core.DisplayDevice;

import org.w3c.css.sac.LexicalUnit;

/**
 * The CSS line-height property.
 */
public class LineHeightProperty extends AbstractProperty {

    private static final float LINE_HEIGHT_NORMAL = 1.2f;

    /**
     * Class constructor.
     */
    public LineHeightProperty() {
        super(CSS.LINE_HEIGHT);
    }

    /**
     * Calculates the value of the property given a LexicalUnit. Returns
     * a RelativeLength that is relative to the current font size.
     */
    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        
        int ppi = DisplayDevice.getCurrent().getVerticalPPI();
        
        if (isLength(lu)) {
            return RelativeLength.createAbsolute(Math.round(getIntLength(lu, styles.getFontSize(), ppi) / styles.getFontSize()));
        } else if (isNumber(lu)) {
            if (getNumber(lu) <= 0) {
                return RelativeLength.createRelative(LINE_HEIGHT_NORMAL);
            } else {
                return RelativeLength.createRelative(getNumber(lu));
            }
        } else if (isPercentage(lu)) {
            if (lu.getFloatValue() <= 0) {
                return RelativeLength.createRelative(LINE_HEIGHT_NORMAL);
            } else {
                return RelativeLength.createRelative(lu.getFloatValue() / 100);
            }
        } else {
            // not specified, "inherit", or other unknown value
            if (parentStyles == null) {
                return RelativeLength.createRelative(LINE_HEIGHT_NORMAL);
            } else {
                return (RelativeLength) parentStyles.get(CSS.LINE_HEIGHT);
            }
        }
    }

}

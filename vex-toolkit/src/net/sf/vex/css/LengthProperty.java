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
 * A property that represents lengths, such as a margin or padding.
 */
public class LengthProperty extends AbstractProperty {

    public LengthProperty(String name, byte axis) {
        super(name);
        this.axis = axis;
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {

        DisplayDevice device = DisplayDevice.getCurrent();
        int ppi = this.axis == AXIS_HORIZONTAL ? device.getHorizontalPPI() : device.getVerticalPPI();
        
        if (isLength(lu)) {
            int length = getIntLength(lu, styles.getFontSize(), ppi);
            return RelativeLength.createAbsolute(length);
        } else if (isPercentage(lu)) {
            return RelativeLength.createRelative(lu.getFloatValue() / 100);
        } else if (isInherit(lu) && parentStyles != null) {
            return parentStyles.get(this.getName());
        } else {
            // not specified, "auto", or other unknown value
            return RelativeLength.createAbsolute(0);
        }
    }


    //============================================================ PRIVATE

    private int axis;
}

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
 * The CSS font-size property. Note that other lengths depend on the
 * computed value of this property, so this should be evaluated early
 * on in the stylesheet, before any other lengths.
 */
public class FontSizeProperty extends AbstractProperty {

    /**
     * Class constructor,
     */
    public FontSizeProperty() {
        super(CSS.FONT_SIZE);
    }

    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles) {
        return new Float(this.calculateInternal(lu, parentStyles, styles));
    }

    /**
     * Returns true if the given lexical unit represents a font size.
     *
     * @param lu LexicalUnit to check.
     */
    public static boolean isFontSize(LexicalUnit lu) {
        if (lu == null) {
            return false;
        } else if (isLength(lu)) {
            return true;
        } else if (isPercentage(lu)) {
            return true;
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            return s.equals(CSS.XX_SMALL)
            || s.equals(CSS.X_SMALL)
            || s.equals(CSS.SMALL)
            || s.equals(CSS.MEDIUM)
            || s.equals(CSS.LARGE)
            || s.equals(CSS.X_LARGE)
            || s.equals(CSS.XX_LARGE)
            || s.equals(CSS.SMALLER)
            || s.equals(CSS.LARGER);
        } else {
            return false;
        }
    }
    
    //======================================================== PRIVATE
    
    private float calculateInternal(LexicalUnit lu, Styles parentStyles, Styles styles) {

        DisplayDevice device = DisplayDevice.getCurrent();
        float baseFontSize = DEFAULT_FONT_SIZE_POINTS * device.getVerticalPPI() / 72;
        
        if (parentStyles != null) {
            baseFontSize = parentStyles.getFontSize();
        }
        
        if (lu == null) {
            return baseFontSize;
        } else if (isLength(lu)) {
            return getFloatLength(lu, baseFontSize, device.getVerticalPPI());
        } else if (isPercentage(lu)) {
            return baseFontSize * lu.getFloatValue() / 100;
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
            String s = lu.getStringValue();
            
            if (s.equals(CSS.XX_SMALL)) {
                return baseFontSize * FONT_FACTOR_XX_SMALL;
            
            } else if (s.equals(CSS.X_SMALL)) {
                return baseFontSize * FONT_FACTOR_X_SMALL;
            
            } else if (s.equals(CSS.SMALL)) {
                return baseFontSize * FONT_FACTOR_SMALL;
            
            } else if (s.equals(CSS.MEDIUM)) {
                return baseFontSize * FONT_FACTOR_MEDIUM;
            
            } else if (s.equals(CSS.LARGE)) {
                return baseFontSize * FONT_FACTOR_LARGE;
            
            } else if (s.equals(CSS.X_LARGE)) {
                return baseFontSize * FONT_FACTOR_X_LARGE;
            
            } else if (s.equals(CSS.XX_LARGE)) {
                return baseFontSize * FONT_FACTOR_XX_LARGE;
            
            } else if (s.equals(CSS.SMALLER)) {
                return baseFontSize / FONT_SIZE_FACTOR;
            
            } else if (s.equals(CSS.LARGER)) {
                return baseFontSize * FONT_SIZE_FACTOR;
            
            } else {
                return baseFontSize;
            }
        } else {
            return baseFontSize;
        }
        
        
    }

    private static final float DEFAULT_FONT_SIZE_POINTS = 12;

    // relative size of adjacent font size names
    private static final float FONT_SIZE_FACTOR = 1.2f;

    // Sizes of named font sizes, relative to "medium"
    private static final float FONT_FACTOR_MEDIUM = 1.0f;
    
    private static final float FONT_FACTOR_SMALL = 
        FONT_FACTOR_MEDIUM / FONT_SIZE_FACTOR;
    
    private static final float FONT_FACTOR_X_SMALL = 
        FONT_FACTOR_SMALL / FONT_SIZE_FACTOR;
    
    private static final float FONT_FACTOR_XX_SMALL = 
        FONT_FACTOR_X_SMALL / FONT_SIZE_FACTOR;
    
    private static final float FONT_FACTOR_LARGE = 
        FONT_FACTOR_MEDIUM * FONT_SIZE_FACTOR;
    
    private static final float FONT_FACTOR_X_LARGE = 
        FONT_FACTOR_LARGE * FONT_SIZE_FACTOR;
    
    private static final float FONT_FACTOR_XX_LARGE = 
        FONT_FACTOR_X_LARGE * FONT_SIZE_FACTOR;


}

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
 * Abstract base class for property classes. Implements the <code>name</code>
 * property but leaves the implementation of <code>calculate</code> to the
 * subclass.
 */
public abstract class AbstractProperty implements IProperty {

    /**
     * Class constructor.
     * @param name Name of the property.
     */
    public AbstractProperty(String name) {
        this.name = name;
    }

    /**
     * Returns true if the given lexical unit represents the token "inherit".
     */
    public static boolean isInherit(LexicalUnit lu) {
        return lu != null
        && lu.getLexicalUnitType() == LexicalUnit.SAC_INHERIT;
    }

    public String getName() {
        return this.name;
    }

    public static boolean isPercentage(LexicalUnit lu) {
        return lu != null &&
            lu.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE;
    }



    public static boolean isLength(LexicalUnit lu) {
        if (lu == null) {
            return false;
        }
        
        short type = lu.getLexicalUnitType();
        
        if (type == LexicalUnit.SAC_INTEGER &&
                lu.getIntegerValue() == 0) {
            return true;
        }
        
        return type == LexicalUnit.SAC_CENTIMETER
        || type == LexicalUnit.SAC_DIMENSION
        || type == LexicalUnit.SAC_EM
        || type == LexicalUnit.SAC_EX
        || type == LexicalUnit.SAC_INCH
        || type == LexicalUnit.SAC_MILLIMETER
        || type == LexicalUnit.SAC_PICA
        || type == LexicalUnit.SAC_PIXEL
        || type == LexicalUnit.SAC_POINT;
    }

    public static int getIntLength(LexicalUnit lu, float fontSize, int ppi) {
        return Math.round(getFloatLength(lu, fontSize, ppi));
    }

    public static float getFloatLength(LexicalUnit lu, float fontSize, int ppi) {
    
        float value = 0f;
        
        switch (lu.getLexicalUnitType()) {
        
        case LexicalUnit.SAC_CENTIMETER:
            value = lu.getFloatValue() * ppi / 2.54f;
        break;
        case LexicalUnit.SAC_EM:
            value = lu.getFloatValue() * fontSize;
        break;
        case LexicalUnit.SAC_EX:
            value = lu.getFloatValue() * fontSize * EX_FACTOR;
        break;
        case LexicalUnit.SAC_INCH:
            value = lu.getFloatValue() * ppi;
        break;
        case LexicalUnit.SAC_INTEGER:
            value = 0; // 0 is the only valid length w/o a dimension
        break;
        case LexicalUnit.SAC_MILLIMETER:
            value = lu.getFloatValue() * ppi / 25.4f;
        break;
        case LexicalUnit.SAC_PICA:
            value = lu.getFloatValue() * ppi / 6;
        break;
        case LexicalUnit.SAC_PIXEL:
            value = lu.getFloatValue();
        break;
        case LexicalUnit.SAC_POINT:
            value = lu.getFloatValue() * ppi / 72;
        break;
        }
        return value;
    }


    //============================================================== PRIVATE
    
    public static boolean isNumber(LexicalUnit lu) {
        return lu != null &&
            (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER
            || lu.getLexicalUnitType() == LexicalUnit.SAC_REAL);
    }


    public static float getNumber(LexicalUnit lu) {
        if (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
            return lu.getIntegerValue();
        } else if (lu.getLexicalUnitType() == LexicalUnit.SAC_REAL) {
            return lu.getFloatValue();
        } else {
            throw new RuntimeException("LexicalUnit type " + lu.getLexicalUnitType() + " is not a numeric type.");
        }
    }


    private String name;

    private static final float EX_FACTOR = 0.6f;

}

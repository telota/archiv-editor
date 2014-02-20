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
package test.net.sf.vex.css;

import org.w3c.css.sac.LexicalUnit;

/**
 * Dummy LexicalUnit implementation.
 */
public class TestLU implements LexicalUnit {

    public TestLU(short type) {
        this.lexicalUnitType = type;
    }

    public static LexicalUnit INHERIT = new TestLU(LexicalUnit.SAC_INHERIT);
    
    public static LexicalUnit createFloat(short units, float value) {
        TestLU lu = new TestLU(units);
        lu.setFloatValue(value);
        return lu;
    }
    
    public static LexicalUnit createIdent(String s) {
        TestLU lu = new TestLU(LexicalUnit.SAC_IDENT);
        lu.setStringValue(s);
        return lu;
    }
    
    public static LexicalUnit createString(String s) {
        TestLU lu = new TestLU(LexicalUnit.SAC_STRING_VALUE);
        lu.setStringValue(s);
        return lu;
    }
    
    public String getDimensionUnitText() {
        return this.dimensionUnitText;
    }
    public float getFloatValue() {
        return this.floatValue;
    }
    public String getFunctionName() {
        return this.functionName;
    }
    public int getIntegerValue() {
        return this.integerValue;
    }
    public short getLexicalUnitType() {
        return this.lexicalUnitType;
    }
    public LexicalUnit getNextLexicalUnit() {
        return this.nextLexicalUnit;
    }
    public LexicalUnit getParameters() {
        return this.parameters;
    }
    public LexicalUnit getPreviousLexicalUnit() {
        return this.previousLexicalUnit;
    }
    public String getStringValue() {
        return this.stringValue;
    }
    public LexicalUnit getSubValues() {
        return this.subValues;
    }
    public void setDimensionUnitText(String dimensionUnitText) {
        this.dimensionUnitText = dimensionUnitText;
    }
    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }
    public void setLexicalUnitType(short lexicalUnitType) {
        this.lexicalUnitType = lexicalUnitType;
    }
    public void setNextLexicalUnit(LexicalUnit nextLexicalUnit) {
        this.nextLexicalUnit = nextLexicalUnit;
    }
    public void setParameters(LexicalUnit parameters) {
        this.parameters = parameters;
    }
    public void setPreviousLexicalUnit(LexicalUnit previousLexicalUnit) {
        this.previousLexicalUnit = previousLexicalUnit;
    }
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    public void setSubValues(LexicalUnit subValues) {
        this.subValues = subValues;
    }
    private short lexicalUnitType;
    private LexicalUnit nextLexicalUnit;
    private LexicalUnit previousLexicalUnit;
    private int integerValue;
    private float floatValue;
    private String dimensionUnitText;
    private String functionName;
    private LexicalUnit parameters;
    private String stringValue;
    private LexicalUnit subValues;
    
}

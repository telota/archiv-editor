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

import java.net.URL;

import junit.framework.TestCase;
import net.sf.vex.core.Color;
import net.sf.vex.core.DisplayDevice;
import net.sf.vex.css.CSS;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.RootElement;

/**
 * Test the <code>net.sf.vex.css</code> package.
 */
public class CssTest extends TestCase {
    
    protected void setUp() throws Exception {
        super.setUp();
        DisplayDevice.setCurrent(new TestDisplayDevice(90, 90));
    }
    
    /*
    public void testAll() throws Exception {
	Element aElement = new Element("A");
	Element bElement = new Element("B");
	Element cElement = new Element("C");
	Document doc = new Document(aElement);
	doc.insertElement(1, bElement);
	doc.insertElement(2, cElement);

	StyleSheet ss = parseStyleSheetResource("test1.css");
	Styles styles = ss.get(aElement);

	assertProperty(styles, "name", "A", LexicalUnit.SAC_IDENT);
	
    }
    */


    public void testBorderColor() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles;
	Color red = new Color(255, 0, 0);
	Color green = new Color(0, 128, 0);
	Color blue = new Color(0, 0, 255);
	Color white = new Color(255, 255, 255);

	styles = ss.getStyles(new Element("borderColor1"));
	assertEquals(red, styles.getBorderTopColor());
	assertEquals(red, styles.getBorderLeftColor());
	assertEquals(red, styles.getBorderRightColor());
	assertEquals(red, styles.getBorderBottomColor());

	styles = ss.getStyles(new Element("borderColor2"));
	assertEquals(red, styles.getBorderTopColor());
	assertEquals(green, styles.getBorderLeftColor());
	assertEquals(green, styles.getBorderRightColor());
	assertEquals(red, styles.getBorderBottomColor());

	styles = ss.getStyles(new Element("borderColor3"));
	assertEquals(red, styles.getBorderTopColor());
	assertEquals(green, styles.getBorderLeftColor());
	assertEquals(green, styles.getBorderRightColor());
	assertEquals(blue, styles.getBorderBottomColor());

	styles = ss.getStyles(new Element("borderColor4"));
	assertEquals(red, styles.getBorderTopColor());
	assertEquals(green, styles.getBorderRightColor());
	assertEquals(blue, styles.getBorderBottomColor());
	assertEquals(white, styles.getBorderLeftColor());

    }
    
    public void testBorderStyle() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles;

	styles = ss.getStyles(new Element("borderStyle1"));
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(CSS.SOLID, styles.getBorderLeftStyle());
	assertEquals(CSS.SOLID, styles.getBorderRightStyle());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());

	styles = ss.getStyles(new Element("borderStyle2"));
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(CSS.DOTTED, styles.getBorderLeftStyle());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());

	styles = ss.getStyles(new Element("borderStyle3"));
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(CSS.DOTTED, styles.getBorderLeftStyle());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());
	assertEquals(CSS.DASHED, styles.getBorderBottomStyle());

	styles = ss.getStyles(new Element("borderStyle4"));
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());
	assertEquals(CSS.DASHED, styles.getBorderBottomStyle());
	assertEquals(CSS.OUTSET, styles.getBorderLeftStyle());

    }
    
    public void testBorderWidth() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles;

	styles = ss.getStyles(new Element("borderWidth1"));
	assertEquals(1, styles.getBorderTopWidth());
	assertEquals(1, styles.getBorderLeftWidth());
	assertEquals(1, styles.getBorderRightWidth());
	assertEquals(1, styles.getBorderBottomWidth());

	styles = ss.getStyles(new Element("borderWidth2"));
	assertEquals(1, styles.getBorderTopWidth());
	assertEquals(2, styles.getBorderLeftWidth());
	assertEquals(2, styles.getBorderRightWidth());
	assertEquals(1, styles.getBorderBottomWidth());

	styles = ss.getStyles(new Element("borderWidth3"));
	assertEquals(1, styles.getBorderTopWidth());
	assertEquals(2, styles.getBorderLeftWidth());
	assertEquals(2, styles.getBorderRightWidth());
	assertEquals(3, styles.getBorderBottomWidth());

	styles = ss.getStyles(new Element("borderWidth4"));
	assertEquals(1, styles.getBorderTopWidth());
	assertEquals(2, styles.getBorderRightWidth());
	assertEquals(3, styles.getBorderBottomWidth());
	assertEquals(4, styles.getBorderLeftWidth());

    }
    
    public void testDefaults() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(new Element("defaults"));

	assertEquals(15.0f, styles.getFontSize(), 0.1);

	assertNull(styles.getBackgroundColor());

	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(0, styles.getBorderBottomWidth());

	assertEquals(new Color(0, 0, 0), styles.getBorderLeftColor());
	assertEquals(CSS.NONE, styles.getBorderLeftStyle());
	assertEquals(0, styles.getBorderLeftWidth());

	assertEquals(new Color(0, 0, 0), styles.getBorderRightColor());
	assertEquals(CSS.NONE, styles.getBorderRightStyle());
	assertEquals(0, styles.getBorderRightWidth());

	assertEquals(new Color(0, 0, 0), styles.getBorderTopColor());
	assertEquals(CSS.NONE, styles.getBorderTopStyle());
	assertEquals(0, styles.getBorderTopWidth());

	assertEquals(new Color(0, 0, 0), styles.getColor());
	assertEquals(CSS.INLINE, styles.getDisplay());

	assertEquals(0, styles.getMarginBottom().get(10));
	assertEquals(0, styles.getMarginLeft().get(10));
	assertEquals(0, styles.getMarginRight().get(10));
	assertEquals(0, styles.getMarginTop().get(10));

	assertEquals(0, styles.getPaddingBottom().get(10));
	assertEquals(0, styles.getPaddingLeft().get(10));
	assertEquals(0, styles.getPaddingRight().get(10));
	assertEquals(0, styles.getPaddingTop().get(10));
    }

    /**
     * Check the correct properties are inherited by default.
     */
    public void testDefaultInheritance() throws Exception {
	RootElement simple = new RootElement("simple");
	Element defaults = new Element("defaults");
	Document doc = new Document(simple);
	doc.insertElement(1, defaults);

	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(defaults);

	assertEquals(12.5f, styles.getFontSize(), 0.1);

	assertNull(styles.getBackgroundColor());

	assertEquals(new Color(0, 128, 0), styles.getBorderBottomColor());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(0, styles.getBorderBottomWidth());

	assertEquals(new Color(0, 128, 0), styles.getBorderLeftColor());
	assertEquals(CSS.NONE, styles.getBorderLeftStyle());
	assertEquals(0, styles.getBorderLeftWidth());

	assertEquals(new Color(0, 128, 0), styles.getBorderRightColor());
	assertEquals(CSS.NONE, styles.getBorderRightStyle());
	assertEquals(0, styles.getBorderRightWidth());

	assertEquals(new Color(0, 128, 0), styles.getBorderTopColor());
	assertEquals(CSS.NONE, styles.getBorderTopStyle());
	assertEquals(0, styles.getBorderTopWidth());

	assertEquals(new Color(0, 128, 0), styles.getColor());
	assertEquals(CSS.INLINE, styles.getDisplay());

	assertEquals(0, styles.getMarginBottom().get(10));
	assertEquals(0, styles.getMarginLeft().get(10));
	assertEquals(0, styles.getMarginRight().get(10));
	assertEquals(0, styles.getMarginTop().get(10));

	assertEquals(0, styles.getPaddingBottom().get(10));
	assertEquals(0, styles.getPaddingLeft().get(10));
	assertEquals(0, styles.getPaddingRight().get(10));
	assertEquals(0, styles.getPaddingTop().get(10));
    }

    public void testExpandBorder() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles;

	styles = ss.getStyles(new Element("expandBorder"));
	assertEquals(2, styles.getBorderBottomWidth());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderBottomColor());
	assertEquals(2, styles.getBorderLeftWidth());
	assertEquals(CSS.SOLID, styles.getBorderLeftStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderLeftColor());
	assertEquals(2, styles.getBorderRightWidth());
	assertEquals(CSS.SOLID, styles.getBorderRightStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderRightColor());
	assertEquals(2, styles.getBorderTopWidth());
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderTopColor());
	
	styles = ss.getStyles(new Element("expandBorderBottom"));
	assertEquals(2, styles.getBorderBottomWidth());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderBottomColor());
	assertEquals(0, styles.getBorderLeftWidth());
	assertEquals(CSS.NONE, styles.getBorderLeftStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderLeftColor());
	assertEquals(0, styles.getBorderRightWidth());
	assertEquals(CSS.NONE, styles.getBorderRightStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderRightColor());
	assertEquals(0, styles.getBorderTopWidth());
	assertEquals(CSS.NONE, styles.getBorderTopStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderTopColor());
	
	styles = ss.getStyles(new Element("expandBorderLeft"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(2, styles.getBorderLeftWidth());
	assertEquals(CSS.SOLID, styles.getBorderLeftStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderLeftColor());
	assertEquals(0, styles.getBorderRightWidth());
	assertEquals(CSS.NONE, styles.getBorderRightStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderRightColor());
	assertEquals(0, styles.getBorderTopWidth());
	assertEquals(CSS.NONE, styles.getBorderTopStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderTopColor());
	
	styles = ss.getStyles(new Element("expandBorderRight"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(0, styles.getBorderLeftWidth());
	assertEquals(CSS.NONE, styles.getBorderLeftStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderLeftColor());
	assertEquals(2, styles.getBorderRightWidth());
	assertEquals(CSS.SOLID, styles.getBorderRightStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderRightColor());
	assertEquals(0, styles.getBorderTopWidth());
	assertEquals(CSS.NONE, styles.getBorderTopStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderTopColor());
	
	styles = ss.getStyles(new Element("expandBorderTop"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(0, styles.getBorderLeftWidth());
	assertEquals(CSS.NONE, styles.getBorderLeftStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderLeftColor());
	assertEquals(0, styles.getBorderRightWidth());
	assertEquals(CSS.NONE, styles.getBorderRightStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderRightColor());
	assertEquals(2, styles.getBorderTopWidth());
	assertEquals(CSS.SOLID, styles.getBorderTopStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderTopColor());

	styles = ss.getStyles(new Element("expandBorder1"));
	assertEquals(2, styles.getBorderBottomWidth());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	
	styles = ss.getStyles(new Element("expandBorder2"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderBottomColor());
	
	styles = ss.getStyles(new Element("expandBorder3"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	
	styles = ss.getStyles(new Element("expandBorder4"));
	assertEquals(3, styles.getBorderBottomWidth());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderBottomColor());
	
	styles = ss.getStyles(new Element("expandBorder5"));
	assertEquals(3, styles.getBorderBottomWidth());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	
	styles = ss.getStyles(new Element("expandBorder6"));
	assertEquals(0, styles.getBorderBottomWidth());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(new Color(255, 0, 0), styles.getBorderBottomColor());
	
    }
    
    public void testExpandMargins() throws Exception {
        StyleSheet ss = parseStyleSheetResource("test2.css");
        
        Styles styles = ss.getStyles(new Element("margin1"));
        assertEquals(10, styles.getMarginTop().get(67));
        assertEquals(10, styles.getMarginLeft().get(67));
        assertEquals(10, styles.getMarginRight().get(67));
        assertEquals(10, styles.getMarginBottom().get(67));
        
        styles = ss.getStyles(new Element("margin2"));
        assertEquals(10, styles.getMarginTop().get(67));
        assertEquals(20, styles.getMarginLeft().get(67));
        assertEquals(20, styles.getMarginRight().get(67));
        assertEquals(10, styles.getMarginBottom().get(67));
        
        styles = ss.getStyles(new Element("margin3"));
        assertEquals(10, styles.getMarginTop().get(67));
        assertEquals(20, styles.getMarginLeft().get(67));
        assertEquals(20, styles.getMarginRight().get(67));
        assertEquals(30, styles.getMarginBottom().get(67));
        
        styles = ss.getStyles(new Element("margin4"));
        assertEquals(10, styles.getMarginTop().get(67));
        assertEquals(20, styles.getMarginRight().get(67));
        assertEquals(30, styles.getMarginBottom().get(67));
        assertEquals(40, styles.getMarginLeft().get(67));
    }

    public void testExtras() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(new Element("extras"));

	assertEquals(new Color(0, 255, 0), styles.getBackgroundColor());

	assertEquals(new Color(128, 0, 0), styles.getBorderBottomColor());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());

	assertEquals(new Color(0, 0, 128), styles.getBorderLeftColor());
	assertEquals(CSS.DASHED, styles.getBorderLeftStyle());

	assertEquals(new Color(128, 128, 0), styles.getBorderRightColor());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());

	assertEquals(new Color(128, 0, 128), styles.getBorderTopColor());
	assertEquals(CSS.DOUBLE, styles.getBorderTopStyle());

	assertEquals(new Color(255, 0, 0), styles.getColor());
	assertEquals(CSS.INLINE, styles.getDisplay());
    }

    public void testExtras2() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(new Element("extras2"));

	assertEquals(new Color(192, 192, 192), styles.getBackgroundColor());

	assertEquals(new Color(0, 128, 128), styles.getBorderBottomColor());
	assertEquals(CSS.NONE, styles.getBorderBottomStyle());
	assertEquals(0, styles.getBorderBottomWidth());

	assertEquals(new Color(255, 255, 255), styles.getBorderLeftColor());
	assertEquals(CSS.GROOVE, styles.getBorderLeftStyle());

	assertEquals(new Color(255, 255, 0), styles.getBorderRightColor());
	assertEquals(CSS.RIDGE, styles.getBorderRightStyle());

	assertEquals(CSS.INSET, styles.getBorderTopStyle());
    }

    /**
     * Test the symbolic font sizes.
     */
    public void testFontSize() throws Exception {

	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles;

	styles = ss.getStyles(new Element("medium"));
	assertEquals(15.0f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("small"));
	assertEquals(12.5f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("xsmall"));
	assertEquals(10.4f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("xxsmall"));
	assertEquals(8.7f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("large"));
	assertEquals(18.0f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("xlarge"));
	assertEquals(21.6f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("xxlarge"));
	assertEquals(25.9, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("smaller"));
	assertEquals(12.5f, styles.getFontSize(), 0.1);

	styles = ss.getStyles(new Element("font100pct"));
	assertEquals(15.0f, styles.getFontSize(), 0.1);
	
	styles = ss.getStyles(new Element("font80pct"));
	assertEquals(12.0f, styles.getFontSize(), 0.1);
	
	styles = ss.getStyles(new Element("font120pct"));
	assertEquals(18.0f, styles.getFontSize(), 0.1);

    }

    public void testForcedInheritance() throws Exception {
	RootElement simple = new RootElement("simple");
	Element inherit = new Element("inherit");
	Document doc = new Document(simple);
	doc.insertElement(1, inherit);

	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(inherit);

	assertEquals(12.5f, styles.getFontSize(), 0.1);

	assertEquals(new Color(0, 255, 255), styles.getBackgroundColor());

	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(1, styles.getBorderBottomWidth());

	assertEquals(new Color(0, 0, 255), styles.getBorderLeftColor());
	assertEquals(CSS.DASHED, styles.getBorderLeftStyle());
	assertEquals(3, styles.getBorderLeftWidth());

	assertEquals(new Color(255, 0, 255), styles.getBorderRightColor());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());
	assertEquals(5, styles.getBorderRightWidth());

	assertEquals(new Color(128, 128, 128), styles.getBorderTopColor());
	assertEquals(CSS.DOUBLE, styles.getBorderTopStyle());
	assertEquals(1, styles.getBorderTopWidth());

	assertEquals(new Color(0, 128, 0), styles.getColor());
	assertEquals(CSS.BLOCK, styles.getDisplay());

	assertEquals(3543, styles.getMarginBottom().get(10));
	assertEquals(0, styles.getMarginLeft().get(10));
	assertEquals(125, styles.getMarginRight().get(10));
	assertEquals(75, styles.getMarginTop().get(10));

	assertEquals(450, styles.getPaddingBottom().get(10));
	assertEquals(4252, styles.getPaddingLeft().get(10));
	assertEquals(120, styles.getPaddingRight().get(10));
	assertEquals(19, styles.getPaddingTop().get(10));
    }

    public void testImportant() throws Exception {
        StyleSheet ss = parseStyleSheetResource("testImportant.css");
        Element a = new Element("a");
        Styles styles = ss.getStyles(a);
        
        Color black = new Color(0, 0, 0);
        //Color white = new Color(255, 255, 255);
        //Color red = new Color(255, 0, 0);
        Color blue = new Color(0, 0, 255);
        
        assertEquals(black, styles.getBackgroundColor());
        assertEquals(black, styles.getColor());
        assertEquals(blue, styles.getBorderTopColor());
        
    }
    
    public void testMarginInheritance() throws Exception {
        StyleSheet ss = parseStyleSheetResource("test2.css");
        Element root = new Element("margin1");
        Element child = new Element("defaults");
        child.setParent(root);
        Styles styles = ss.getStyles(child);
        
        assertEquals(0, styles.getMarginTop().get(67));
        assertEquals(0, styles.getMarginLeft().get(67));
        assertEquals(0, styles.getMarginRight().get(67));
        assertEquals(0, styles.getMarginBottom().get(67));
    }

    public void testSimple() throws Exception {
	StyleSheet ss = parseStyleSheetResource("test2.css");
	Styles styles = ss.getStyles(new Element("simple"));

	assertEquals(12.5f, styles.getFontSize(), 0.1);

	assertEquals(new Color(0, 255, 255), styles.getBackgroundColor());

	assertEquals(new Color(0, 0, 0), styles.getBorderBottomColor());
	assertEquals(CSS.SOLID, styles.getBorderBottomStyle());
	assertEquals(1, styles.getBorderBottomWidth());

	assertEquals(new Color(0, 0, 255), styles.getBorderLeftColor());
	assertEquals(CSS.DASHED, styles.getBorderLeftStyle());
	assertEquals(3, styles.getBorderLeftWidth());

	assertEquals(new Color(255, 0, 255), styles.getBorderRightColor());
	assertEquals(CSS.DOTTED, styles.getBorderRightStyle());
	assertEquals(5, styles.getBorderRightWidth());

	assertEquals(new Color(128, 128, 128), styles.getBorderTopColor());
	assertEquals(CSS.DOUBLE, styles.getBorderTopStyle());
	assertEquals(1, styles.getBorderTopWidth());

	assertEquals(new Color(0, 128, 0), styles.getColor());
	assertEquals(CSS.BLOCK, styles.getDisplay());

	assertEquals(3543, styles.getMarginBottom().get(10));
	assertEquals(0, styles.getMarginLeft().get(10));
	assertEquals(125, styles.getMarginRight().get(10));
	assertEquals(75, styles.getMarginTop().get(10));

	assertEquals(450, styles.getPaddingBottom().get(10));
	assertEquals(4252, styles.getPaddingLeft().get(10));
	assertEquals(120, styles.getPaddingRight().get(10));
	assertEquals(19, styles.getPaddingTop().get(10));

    }


    /**
     * Confirm our assumptions about the structure of lexical units.
     */
    /*
    public void testLexicalUnits() throws Exception {
	Element aElement = new Element("A");
	StyleSheet ss = parseStyleSheetResource("testLexicalUnits.css");
	Styles styles = ss.get(aElement);

	LexicalUnit lu;
	LexicalUnit lu2;

	System.out.println("DEBUG: styles for element A");
	dumpStyles(styles);

	// TEST: how to access color specified in "rgb(R,G,B)" format
	lu = styles.get(CSS.COLOR);
	assertEquals(LexicalUnit.SAC_RGBCOLOR, lu.getLexicalUnitType());
	assertEquals("color", lu.getFunctionName());
	lu2 = lu.getParameters();
	assertNotNull(lu2);
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(255, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(255, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(255, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertNull(lu2);

	// TEST: color specified in "#RGB" format is accessed the same way
	lu = styles.get(CSS.BACKGROUND_COLOR);
	assertEquals(LexicalUnit.SAC_RGBCOLOR, lu.getLexicalUnitType());
	assertEquals("color", lu.getFunctionName());
	lu2 = lu.getParameters();
	assertNotNull(lu2);
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(0, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(0, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_INTEGER, lu2.getLexicalUnitType());
	assertEquals(0, lu2.getIntegerValue());
	lu2 = lu2.getNextLexicalUnit();
	assertNull(lu2);

	// TEST: color specified in "rgb(R%,G%,B%)" is accessed as SAC_PERCENTAGE
	lu = styles.get(CSS.BORDER_BOTTOM);
	assertEquals(LexicalUnit.SAC_RGBCOLOR, lu.getLexicalUnitType());
	assertEquals("color", lu.getFunctionName());
	lu2 = lu.getParameters();
	assertNotNull(lu2);
	assertEquals(LexicalUnit.SAC_PERCENTAGE, lu2.getLexicalUnitType());
	assertEquals(10f, lu2.getFloatValue(), 0.001);
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_PERCENTAGE, lu2.getLexicalUnitType());
	assertEquals(20f, lu2.getFloatValue(), 0.001);
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, lu2.getLexicalUnitType());
	lu2 = lu2.getNextLexicalUnit();
	assertEquals(LexicalUnit.SAC_PERCENTAGE, lu2.getLexicalUnitType());
	assertEquals(30f, lu2.getFloatValue(), 0.001);
	lu2 = lu2.getNextLexicalUnit();
	assertNull(lu2);

	// TEST: color incompletely specified, e.g. "rgb(10,20)" is rejected
	// by the parser
	lu = styles.get(CSS.BORDER_LEFT);
	assertNull(lu);

	// TEST: color incorrectly specified, e.g. "10", "foo", or 
	// "rgb(larry,curly,moe)" is passed by the parser
	lu = styles.get(CSS.BORDER_RIGHT); // "10" is not a valid lexical unit
	assertNull(lu);
	
	lu = styles.get(CSS.BORDER_TOP); // "rgb(larry, curly, moe)"
	assertNull(lu); 

	styles = ss.get(new Element("B"));
	lu = styles.get(CSS.BACKGROUND_COLOR); // "foo"
	assertEquals(LexicalUnit.SAC_IDENT, lu.getLexicalUnitType());
	assertEquals("foo", lu.getStringValue());

	lu = styles.get(CSS.COLOR); // "10px"
	assertEquals(LexicalUnit.SAC_PIXEL, lu.getLexicalUnitType());
	//assertEquals(10, lu.getIntegerValue()); // NOTE: not an int!
	assertEquals(10f, lu.getFloatValue(), 0.001);
    }


    public void testExpandMargins() throws Exception {
	Element aElement = new Element("A");
	Element bElement = new Element("B");
	Element cElement = new Element("C");
	Element dElement = new Element("D");
	Element eElement = new Element("E");
	Element fElement = new Element("F");
	Element gElement = new Element("G");
	Document doc = new Document(new Element("root"));
	doc.insertElement(1, aElement);
	doc.insertElement(3, cElement);
	doc.insertElement(5, cElement);
	doc.insertElement(7, cElement);

	StyleSheet ss = parseStyleSheetResource("expansion.css");
	Styles styles;

	// single margin call expands
	styles = ss.get(aElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 1.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 1.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 1.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 1.0f, LexicalUnit.SAC_INCH);

	// more-specific overrides; shorthand comes first
	styles = ss.get(bElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 0.5f, LexicalUnit.SAC_INCH);
	
	// more-specific overrides; shorthand comes last
	styles = ss.get(cElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 0.5f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 0.5f, LexicalUnit.SAC_INCH);

	// second shorthand overrides first
	styles = ss.get(dElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 0.25f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 0.25f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 0.25f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 0.25f, LexicalUnit.SAC_INCH);
	
	// expanding two values for margins
	styles = ss.get(eElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 1.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 2.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 2.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 1.0f, LexicalUnit.SAC_INCH);
	
	// expanding three values for margins
	styles = ss.get(fElement);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 3.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 2.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 2.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_TOP, 1.0f, LexicalUnit.SAC_INCH);
	
	// expanding four values for margins
	styles = ss.get(gElement);
	assertProperty(styles, CSS.MARGIN_TOP, 1.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_RIGHT, 2.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_BOTTOM, 3.0f, LexicalUnit.SAC_INCH);
	assertProperty(styles, CSS.MARGIN_LEFT, 4.0f, LexicalUnit.SAC_INCH);
	
    }


    public static void assertProperty(Styles styles, 
				      String name, 
				      float value,
				      int lexicalUnitType) {

	LexicalUnit lu = styles.get(name);
	assertEquals(lexicalUnitType, lu.getLexicalUnitType());
	assertEquals(value, lu.getFloatValue(), 0.001f);
    }

    public static void assertProperty(Styles styles, 
				      String name, 
				      String value,
				      int lexicalUnitType) {

	LexicalUnit lu = styles.get(name);
	assertEquals(lexicalUnitType, lu.getLexicalUnitType());
	assertEquals(value, lu.getStringValue());
    }

    public static void dumpStyles(Styles styles) {
	java.util.Iterator iter = styles.getPropertyNames().iterator();
	while (iter.hasNext()) {
	    String name = (String) iter.next();
	    System.out.println(name + ": " + styles.get(name));
	}
    }
    */ 

    private StyleSheet parseStyleSheetResource(String resource) 
	throws java.io.IOException {
        
        URL url = this.getClass().getResource(resource);
        StyleSheetReader reader = new StyleSheetReader();
        return reader.read(url);

    }
}


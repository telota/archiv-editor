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

import junit.framework.TestCase;
import net.sf.vex.core.DisplayDevice;
import net.sf.vex.css.BorderStyleProperty;
import net.sf.vex.css.BorderWidthProperty;
import net.sf.vex.css.CSS;
import net.sf.vex.css.IProperty;
import net.sf.vex.css.Styles;

public class PropertyTest extends TestCase {

    /**
     * From CSS2.1 section 8.5.3
     */
    public void testBorderStyleProperty() throws Exception {
        Styles styles = new Styles();
        Styles parentStyles = new Styles();
        IProperty prop = new BorderStyleProperty(CSS.BORDER_TOP_STYLE);
        
        // Inheritance
        parentStyles.put(CSS.BORDER_TOP_STYLE, CSS.DASHED);
        assertEquals(CSS.NONE, prop.calculate(null, parentStyles, styles));
        assertEquals(CSS.DASHED, prop.calculate(TestLU.INHERIT, parentStyles, styles)); // not inherited

        // Regular values
        assertEquals(CSS.NONE, prop.calculate(TestLU.createIdent(CSS.NONE), parentStyles, styles));
        assertEquals(CSS.HIDDEN, prop.calculate(TestLU.createIdent(CSS.HIDDEN), parentStyles, styles));
        assertEquals(CSS.DOTTED, prop.calculate(TestLU.createIdent(CSS.DOTTED), parentStyles, styles));
        assertEquals(CSS.DASHED, prop.calculate(TestLU.createIdent(CSS.DASHED), parentStyles, styles));
        assertEquals(CSS.SOLID, prop.calculate(TestLU.createIdent(CSS.SOLID), parentStyles, styles));
        assertEquals(CSS.DOUBLE, prop.calculate(TestLU.createIdent(CSS.DOUBLE), parentStyles, styles));
        assertEquals(CSS.GROOVE, prop.calculate(TestLU.createIdent(CSS.GROOVE), parentStyles, styles));
        assertEquals(CSS.RIDGE, prop.calculate(TestLU.createIdent(CSS.RIDGE), parentStyles, styles));
        assertEquals(CSS.INSET, prop.calculate(TestLU.createIdent(CSS.INSET), parentStyles, styles));
        assertEquals(CSS.OUTSET, prop.calculate(TestLU.createIdent(CSS.OUTSET), parentStyles, styles));
        
        // Invalid token
        assertEquals(CSS.NONE, prop.calculate(TestLU.createIdent(CSS.BOLD), parentStyles, styles));
        
        // Wrong type
        assertEquals(CSS.NONE, prop.calculate(TestLU.createString(CSS.HIDDEN), parentStyles, styles));
    }


    /**
     * From CSS2.1 section 8.5.1
     */
    public void testBorderWidthProperty() throws Exception {
        
        Styles styles = new Styles();
        Styles parentStyles = new Styles();
        DisplayDevice.setCurrent(new DummyDisplayDevice(50, 100));
        IProperty prop = new BorderWidthProperty(CSS.BORDER_TOP_WIDTH, CSS.BORDER_TOP_STYLE, IProperty.AXIS_VERTICAL);

        styles.put(CSS.FONT_SIZE, new Float(12));
        styles.put(CSS.BORDER_TOP_STYLE, CSS.SOLID);
        
        // Inheritance
        parentStyles.put(CSS.BORDER_TOP_WIDTH, new Integer(27));
        assertEquals(new Integer(3), prop.calculate(null, parentStyles, styles));
        assertEquals(new Integer(27), prop.calculate(TestLU.INHERIT, parentStyles, styles)); // not inherited

        // Regular values
        assertEquals(new Integer(20), prop.calculate(TestLU.createFloat(LexicalUnit.SAC_INCH, 0.2f), parentStyles, styles));
        
        // Invalid token
        assertEquals(new Integer(3), prop.calculate(TestLU.createIdent(CSS.BOLD), parentStyles, styles));
        
        // Wrong type
        assertEquals(new Integer(3), prop.calculate(TestLU.createString(CSS.HIDDEN), parentStyles, styles));
        
        // Corresponding style is "none" or "hidden"
        styles.put(CSS.BORDER_TOP_STYLE, CSS.NONE);
        assertEquals(new Integer(0), prop.calculate(TestLU.createFloat(LexicalUnit.SAC_INCH, 0.2f), parentStyles, styles));
        styles.put(CSS.BORDER_TOP_STYLE, CSS.HIDDEN);
        assertEquals(new Integer(0), prop.calculate(TestLU.createFloat(LexicalUnit.SAC_INCH, 0.2f), parentStyles, styles));
        
        // check that we use the proper PPI 
        styles.put(CSS.BORDER_LEFT_STYLE, CSS.SOLID);
        prop = new BorderWidthProperty(CSS.BORDER_LEFT_WIDTH, CSS.BORDER_LEFT_STYLE, IProperty.AXIS_HORIZONTAL);
        assertEquals(new Integer(10), prop.calculate(TestLU.createFloat(LexicalUnit.SAC_INCH, 0.2f), parentStyles, styles));
    }
    

    /**
     * From CSS2.1 section 8.5.2 (border-XXX-color),
     * section 14.1 (color), and section 14.2.1 (background-color)
     */
    public void testColorProperty() throws Exception {
    }
    
    
    private class DummyDisplayDevice extends DisplayDevice {

        public DummyDisplayDevice(int horizontalPPI, int verticalPPI) {
            this.horizontalPPI = horizontalPPI;
            this.verticalPPI = verticalPPI;
        }
        
        public int getHorizontalPPI() {
            return this.horizontalPPI;
        }

        public int getVerticalPPI() {
            return this.verticalPPI;
        }
        
        
        private int horizontalPPI;
        private int verticalPPI;
    }
}

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
package test.net.sf.vex.layout;

import java.net.URL;
import junit.framework.TestCase;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.RootElement;
import net.sf.vex.layout.CssBoxFactory;
import net.sf.vex.layout.InlineBox;
import net.sf.vex.layout.LayoutContext;
import net.sf.vex.layout.StaticTextBox;

public class TestStaticTextBox extends TestCase {

    FakeGraphics g;
    LayoutContext context;
    
    public TestStaticTextBox() throws Exception {
        
        URL url = this.getClass().getResource("test.css");
        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(url);
        
        this.g = new FakeGraphics();
        
        this.context = new LayoutContext();
        this.context.setBoxFactory(new CssBoxFactory());
        this.context.setGraphics(this.g);
        this.context.setStyleSheet(ss);
    }
    
    public void testSplit() throws Exception {
        RootElement root = new RootElement("root");
        
        Styles styles = this.context.getStyleSheet().getStyles(root);
        
        int width = g.getCharWidth();
        
        //  0     6     13      21
        // /     /      /       /
        // baggy orange trousers
        
        StaticTextBox box = new StaticTextBox(this.context, root, "baggy orange trousers");
        assertEquals(box.getText().length() * width, box.getWidth());
        assertEquals(styles.getLineHeight(), box.getHeight());
        assertSplit(box, 22, false, "baggy orange trousers", null);
        assertSplit(box, 21, false, "baggy orange trousers", null);
        assertSplit(box, 20, false, "baggy orange ", "trousers");
        assertSplit(box, 13, false, "baggy orange ", "trousers");
        assertSplit(box, 12, false, "baggy ", "orange trousers");
        assertSplit(box, 6, false, "baggy ", "orange trousers");
        assertSplit(box, 5, false, null, "baggy orange trousers");
        assertSplit(box, 1, false, null, "baggy orange trousers");
        assertSplit(box, 0, false, null, "baggy orange trousers");
        assertSplit(box, -1, false, null, "baggy orange trousers");

        assertSplit(box, 22, true, "baggy orange trousers", null);
        assertSplit(box, 21, true, "baggy orange trousers", null);
        assertSplit(box, 20, true, "baggy orange ", "trousers");
        assertSplit(box, 13, true, "baggy orange ", "trousers");
        assertSplit(box, 12, true, "baggy ", "orange trousers");
        assertSplit(box, 6, true, "baggy ", "orange trousers");
        assertSplit(box, 5, true, "baggy", " orange trousers");
        assertSplit(box, 4, true, "bagg", "y orange trousers");
        assertSplit(box, 3, true, "bag", "gy orange trousers");
        assertSplit(box, 2, true, "ba", "ggy orange trousers");
        assertSplit(box, 1, true, "b", "aggy orange trousers");
        assertSplit(box, 0, true, "b", "aggy orange trousers");
        assertSplit(box, -1, true, "b", "aggy orange trousers");
        
        //                                            0    5   10
        //                                           /    /    /
        box = new StaticTextBox(this.context, root, "red  green");
        assertSplit(box, 11, false, "red  green", null);
        assertSplit(box, 10, false, "red  green", null);
        assertSplit(box, 9, false, "red  ", "green");
        assertSplit(box, 5, false, "red  ", "green");
        
        //
        // This is the way it should work from a formatting point-of-view, but
        // it could be problematic when it gets to positioning the caret, e.g.
        // if we had lots of spaces to the right of a word it would format
        // properly, but the caret would get carried out of the formatted area.
        // 
//        assertSplit(box, 4, false, null, "red  green");
//        assertSplit(box, 1, false, null, "red  green");
//        assertSplit(box, 0, false, null, "red  green");
//        assertSplit(box, -1, false, null, "red  green");

        //
        // This solves the caret problem at the expense of the formatting
        // problem. It also happens to be how my initial implementation works!
        // In the end it doesn't much matter, since Vex should collapse
        // contiguous space into a single space character.
        //
        assertSplit(box, 4, false, "red ", " green");
        assertSplit(box, 3, false, null, "red  green");
        assertSplit(box, 1, false, null, "red  green");
        assertSplit(box, 0, false, null, "red  green");
        assertSplit(box, -1, false, null, "red  green");

        assertSplit(box, 4, true, "red ", " green");
        assertSplit(box, 3, true, "red", "  green");
        assertSplit(box, 1, true, "r", "ed  green");
        assertSplit(box, 0, true, "r", "ed  green");
        assertSplit(box, -1, true, "r", "ed  green");

    }
    
    private void assertSplit(StaticTextBox box, int splitPos, boolean force, String left, String right) {
        
        Styles styles = this.context.getStyleSheet().getStyles(box.getElement());
        int width = g.getCharWidth();
        
        InlineBox.Pair pair = box.split(context, splitPos * width, force);

        StaticTextBox leftBox = (StaticTextBox) pair.getLeft();
        StaticTextBox rightBox = (StaticTextBox) pair.getRight();
        
        if (left == null) {
            assertNull(leftBox); 
        } else {
            assertNotNull(leftBox);
            assertEquals(left, leftBox.getText());
            assertEquals(left.length() * width, leftBox.getWidth());
            assertEquals(styles.getLineHeight(), leftBox.getHeight());
        }
        
        if (right == null) {
            assertNull(rightBox); 
        } else {
            assertNotNull(rightBox);
            assertEquals(right, rightBox.getText());
            assertEquals(right.length() * width, rightBox.getWidth());
            assertEquals(styles.getLineHeight(), rightBox.getHeight());
        }
        
    }
}



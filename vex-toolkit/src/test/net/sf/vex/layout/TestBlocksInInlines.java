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
import net.sf.vex.dom.Document;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.RootElement;
import net.sf.vex.layout.LayoutContext;
import net.sf.vex.layout.RootBox;

/**
 * Tests proper function of a block-level element within an inline element.
 * These must be layed out as a block child of the containing block element.
 */
public class TestBlocksInInlines extends TestCase {

    FakeGraphics g;
    LayoutContext context;

    public TestBlocksInInlines() throws Exception {
        URL url = this.getClass().getResource("test.css");
        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(url);
        
        this.g = new FakeGraphics();
        
        this.context = new LayoutContext();
        this.context.setBoxFactory(new TestBoxFactory());
        this.context.setGraphics(this.g);
        this.context.setStyleSheet(ss);
    }

    public void testBlockInInline() throws Exception {
        RootElement root = new RootElement("root");
        Document doc = new Document(root);

        doc.insertText(1, "one  five");
        doc.insertElement(5, new Element("b"));
        doc.insertText(6, "two  four");
        doc.insertElement(10, new Element("p"));
        doc.insertText(11, "three");
        
        RootBox rootBox = new RootBox(this.context, root, 500);
        rootBox.layout(this.context, 0, Integer.MAX_VALUE);

        
    }
}

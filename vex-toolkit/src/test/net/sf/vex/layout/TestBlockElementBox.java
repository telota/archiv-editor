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
import net.sf.vex.layout.BlockElementBox;
import net.sf.vex.layout.BlockPseudoElementBox;
import net.sf.vex.layout.Box;
import net.sf.vex.layout.LayoutContext;
import net.sf.vex.layout.RootBox;


public class TestBlockElementBox extends TestCase {
    
    FakeGraphics g;
    LayoutContext context;

    public TestBlockElementBox() throws Exception {
        URL url = this.getClass().getResource("test.css");
        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(url);
        
        this.g = new FakeGraphics();
        
        this.context = new LayoutContext();
        this.context.setBoxFactory(new TestBoxFactory());
        this.context.setGraphics(this.g);
        this.context.setStyleSheet(ss);
    }

    public void testBeforeAfter() throws Exception {
        RootElement root = new RootElement("root");
        Document doc = new Document(root);
        doc.insertElement(1, new Element("beforeBlock"));
        
        RootBox rootBox = new RootBox(this.context, root, 500);
        rootBox.layout(this.context, 0, Integer.MAX_VALUE);

        Box[] children;
        BlockElementBox beb;
        
        children = rootBox.getChildren();
        assertEquals(1, children.length);
        assertEquals(BlockElementBox.class, children[0].getClass());
        beb = (BlockElementBox) children[0];
        assertEquals(root, beb.getElement());
        
        children = beb.getChildren();
        assertEquals(2, children.length);
        assertEquals(BlockPseudoElementBox.class, children[0].getClass());
        assertEquals(BlockElementBox.class, children[1].getClass());
        beb = (BlockElementBox) children[1];
        assertEquals("beforeBlock", beb.getElement().getName());
        
    }
    
}

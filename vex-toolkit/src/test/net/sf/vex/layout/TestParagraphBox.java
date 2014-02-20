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
import net.sf.vex.layout.CssBoxFactory;
import net.sf.vex.layout.LayoutContext;

public class TestParagraphBox extends TestCase {

    FakeGraphics g;
    LayoutContext context;
    
    public TestParagraphBox() throws Exception {
        
        URL url = this.getClass().getResource("test.css");
        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(url);
        
        this.g = new FakeGraphics();
        
        this.context = new LayoutContext();
        this.context.setBoxFactory(new CssBoxFactory());
        this.context.setGraphics(this.g);
        this.context.setStyleSheet(ss);
    }

    /*
    public void testWordWrap() throws Exception {
        RootElement root = new RootElement("root");
        Document doc = new Document(root);
        
        Styles styles = this.context.getStyleSheet().getStyles(root);
        
        FontMetrics fm = this.g.getFontMetrics();
        
        // Test Case 1: check the offsets 
        //
        // UPPER CASE indicates static text
        // lower case indicates document text
        // [ ] represent element start and end         
        //
        // BLACK WHITE GRAY
        // RED [orange] YELLOW   (line is 1:8, last=false)
        // BLACK WHITE GRAY
        // [blue] GREEN [pink]     (line is 9:20 last=true)
        // BLACK WHITE GRAY
        //
        // Document looks like this (# chars are element sentinels
        //    2     8      16  20
        //   /     /       /   /    
        // ##orange##blue##pink##
        //           \   \ 
        //            10  14
        //
        
    }
    */
}



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
package test.net.sf.vex.dom;

import junit.framework.TestCase;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.DocumentFragment;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.Node;
import net.sf.vex.dom.RootElement;
import net.sf.vex.dom.Text;

/**
 * Test the <code>net.sf.vex.dom</code> package.
 */
public class DomTest extends TestCase {

    public void testDom() throws Exception {

	//
	// Document initialisation
	//
	RootElement root = new RootElement("article");
	Document doc = new Document(root);
	Node[] content;
	Element[] children;

	//              root
	//              | |
	//               * *

	assertEquals(2, doc.getLength());
	assertEquals(root, doc.getRootElement());
	assertEquals(0, root.getStartOffset());
	assertEquals(1, root.getEndOffset());

	content = root.getChildNodes();
	assertEquals(0, content.length);
	children = root.getChildElements();
	assertEquals(0, children.length);

	//               root
	//              |     |
	//               * a c *

	try {
	    doc.insertText(0, "ac");
	    fail("Expected IllegalArgumentException");
	} catch (IllegalArgumentException ex) {
	}

	
	try {
	    doc.insertText(2, "ac");
	    fail("Expected IllegalArgumentException");
	} catch (IllegalArgumentException ex) {
	}

	doc.insertText(1, "ac");
	assertEquals(4, doc.getLength());
	content = root.getChildNodes();
	assertEquals(1, content.length);
	assertIsText(content[0], "ac", 1, 3);
	assertEquals(1, content[0].getStartPosition().getOffset());
	assertEquals(3, content[0].getEndPosition().getOffset());
	assertEquals(0, root.getStartOffset());
	assertEquals(3, root.getEndOffset());

	//
	// Try inserting at illegal offset
	//
	Element element = new Element("b");
	
	try {
	    doc.insertElement(0, element);
	    fail("Expected IllegalArgumentException");
	} catch (IllegalArgumentException ex) {
	}

	
	try {
	    doc.insertElement(4, element);
	    fail("Expected IllegalArgumentException");
	} catch (IllegalArgumentException ex) {
	}

	//                 root
	//              |         |
	//              |    z    |
	//              |   | |   |
	//               * a * * c *
	//              0 1 2 3 4 5 6
	//
	doc.insertElement(2, element);
	assertEquals(root, element.getParent());
	assertEquals(6, doc.getLength());

	Element element2 = new Element("x");
	doc.insertElement(2, element2);

	content = root.getChildNodes();
	assertEquals(4, content.length);
	assertIsText(content[0], "a", 1, 2);
	assertIsElement(content[1], "x", root, 2, 3);
	assertIsElement(content[2], "b", root, 4, 5);
	assertIsText(content[3], "c", 6, 7);

    }
    
    public void testFragments() throws Exception {
        
        Document doc;
        DocumentFragment frag;
        Element[] elements;
        Node[] nodes;
        Element root;
        Element x;
        Element y;
        Element z;
        
        // Case 1: just text
        //
        //          root
        //        * a b c *
        //       0 1 2 3 4 5
        doc = new Document(new RootElement("root"));
        doc.insertText(1, "abc");
        
        try {
            frag = doc.getFragment(2, 2);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            frag = doc.getFragment(-1, 0);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            frag = doc.getFragment(4, 5);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        frag = doc.getFragment(2, 3);
        assertEquals(1, frag.getContent().getLength());
        assertEquals(0, frag.getElements().length);
        nodes = frag.getNodes();
        assertEquals(1, nodes.length);
        this.assertIsText(nodes[0], "b", 0, 1);

        // Case 2: single element, no children
        //        
        //                   root
        //              |           |
        //              |     z     |
        //              |   |   |   |
        //               * a * b * c *
        //              0 1 2 3 4 5 6 7

        //                  z  
        //                |   |
        //               a * b * c
        //              0 1 2 3 4 5

        doc = new Document(new RootElement("root"));
        doc.insertText(1, "ac");
        doc.insertElement(2, new Element("z"));
        doc.insertText(3, "b");

        frag = doc.getFragment(1, 6);
        elements = frag.getElements();
        assertEquals(1, elements.length);
        this.assertIsElement(elements[0], "z", null, 1, 3);
        nodes = frag.getNodes();
        assertEquals(3, nodes.length);
        assertIsText(nodes[0], "a", 0, 1);
        assertIsElement(nodes[1], "z", null, 1, 3);
        assertIsText(nodes[2], "c", 4, 5);
        nodes = elements[0].getChildNodes();
        assertEquals(1, nodes.length);
        assertIsText(nodes[0], "b", 2, 3);
        
        // Case 3: complex with child elements
        //        
        //                            root
        //              |                               |
        //              |               z               |
        //              |   |                       |   |
        //              |   |     | x |   | y |     |   | 
        //               * a * b c * d * e * f * g h * i *
        //              0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7
        //  3a:                 |<------frag----->|
        //  3b:             |<--------frag----------->|

        

        doc = new Document(new RootElement("root"));
        doc.insertText(1, "ai");
        doc.insertElement(2, new Element("z"));
        doc.insertText(3, "bcgh");
        doc.insertElement(5, new Element("x"));
        doc.insertText(6, "d");
        doc.insertText(8, "e");
        doc.insertElement(9, new Element("y"));
        doc.insertText(10, "f");
        
        //  3a:
        //                | x |   | y | 
        //               c * d * e * f * g 
        //              0 1 2 3 4 5 6 7 8 9
        frag = doc.getFragment(4, 13);
        assertEquals(9, frag.getContent().getLength());
        
        elements = frag.getElements();
        assertEquals(2, elements.length);
        assertIsElement(elements[0], "x", null, 1, 3);
        assertIsElement(elements[1], "y", null, 5, 7);

        nodes = frag.getNodes();
        assertEquals(5, nodes.length);
        assertIsText(nodes[0], "c", 0, 1);
        assertIsElement(nodes[1], "x", null, 1, 3);
        assertIsText(nodes[2], "e", 4, 5);
        assertIsElement(nodes[3], "y", null, 5, 7);
        assertIsText(nodes[4], "g", 8, 9);
        
        //  3b:
        //                          z
        //              |                       |
        //              |     | x |   | y |     | 
        //               * b c * d * e * f * g h * 
        //              0 1 2 3 4 5 6 7 8 9 0 1 2 3
        frag = doc.getFragment(2, 15);
        assertEquals(13, frag.getContent().getLength());
        
        elements = frag.getElements();
        assertEquals(1, elements.length);
        assertIsElement(elements[0], "z", null, 0, 12);
        
        nodes = frag.getNodes();
        assertEquals(1, nodes.length);
        assertIsElement(nodes[0], "z", null, 0, 12);
        
        z = elements[0];
        nodes = z.getChildNodes();
        assertEquals(5, nodes.length);
        assertIsText(nodes[0], "bc", 1, 3);
        assertIsElement(nodes[1], "x", z, 3, 5);
        assertIsText(nodes[2], "e", 6, 7);
        assertIsElement(nodes[3], "y", z, 7, 9);
        assertIsText(nodes[4], "gh", 10, 12);
        
        // 3c: remove and re-insert the same frag as in 3a
        frag = doc.getFragment(4, 13);
        doc.delete(4, 13);
        doc.insertFragment(4, frag);
        
        root = doc.getRootElement();
        assertIsElement(root, "root", null, 0, 16);
        nodes = root.getChildNodes();
        assertEquals(3, nodes.length);
        assertIsText(nodes[0], "a", 1, 2);
        assertIsElement(nodes[1], "z", doc.getRootElement(), 2, 14);
        assertIsText(nodes[2], "i", 15, 16);
        z = (Element) nodes[1];
        nodes = z.getChildNodes();
        assertEquals(5, nodes.length);
        assertIsText(nodes[0], "bc", 3, 5);
        assertIsElement(nodes[1], "x", z, 5, 7);
        assertIsText(nodes[2], "e", 8, 9);
        assertIsElement(nodes[3], "y", z, 9, 11);
        assertIsText(nodes[4], "gh", 12, 14);
        x = (Element) nodes[1];
        y = (Element) nodes[3];
        nodes = x.getChildNodes();
        assertEquals(1, nodes.length);
        assertIsText(nodes[0], "d", 6, 7);
        nodes = y.getChildNodes();
        assertEquals(1, nodes.length);
        assertIsText(nodes[0], "f", 10, 11);
        
        // 3d: remove and re-insert the same frag as in 3b
        frag = doc.getFragment(2, 15);
        doc.delete(2, 15);
        doc.insertFragment(2, frag);
        
        root = doc.getRootElement();
        assertIsElement(root, "root", null, 0, 16);
        nodes = root.getChildNodes();
        assertEquals(3, nodes.length);
        assertIsText(nodes[0], "a", 1, 2);
        assertIsElement(nodes[1], "z", doc.getRootElement(), 2, 14);
        assertIsText(nodes[2], "i", 15, 16);
        z = (Element) nodes[1];
        nodes = z.getChildNodes();
        assertEquals(5, nodes.length);
        assertIsText(nodes[0], "bc", 3, 5);
        assertIsElement(nodes[1], "x", z, 5, 7);
        assertIsText(nodes[2], "e", 8, 9);
        assertIsElement(nodes[3], "y", z, 9, 11);
        assertIsText(nodes[4], "gh", 12, 14);
        x = (Element) nodes[1];
        y = (Element) nodes[3];
        nodes = x.getChildNodes();
        assertEquals(1, nodes.length);
        assertIsText(nodes[0], "d", 6, 7);
        nodes = y.getChildNodes();
        assertEquals(1, nodes.length);
        assertIsText(nodes[0], "f", 10, 11);
        
    }

    public void assertIsElement(Node node, 
				String name, 
				Element parent,
				int startOffset, 
				int endOffset) {

	assertTrue(node instanceof Element);
	assertEquals(name, ((Element)node).getName());
	assertEquals(parent, ((Element)node).getParent());
	assertEquals(startOffset, node.getStartOffset());
	assertEquals(endOffset, node.getEndOffset());
    }

    public void assertIsText(Node node, 
			     String text, 
			     int startOffset, 
			     int endOffset) {

	assertTrue(node instanceof Text);
	assertEquals(text, node.getText());
	assertEquals(startOffset, node.getStartOffset());
	assertEquals(endOffset, node.getEndOffset());
    }
}


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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.DocumentBuilder;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.IWhitespacePolicy;
import net.sf.vex.dom.IWhitespacePolicyFactory;
import net.sf.vex.dom.Node;
import net.sf.vex.dom.Text;
import net.sf.vex.widget.CssWhitespacePolicy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Test the SpaceNormalizer class.
 */
public class SpaceNormalizerTest extends TestCase {

    /**
     * Test the normalize method. Test cases are as follows.
     *
     * <ul>
     * <li>leading w/s trimmed</li>
     * <li>trailing w/s trimmed</li>
     * <li>internal w/s collapsed to a single space</li>
     * <li>internal w/s before and after an inline child element collapsed 
     *     to a single space.</li>
     * <li>internal w/s before and after a block child element removed.</li>
     * <li>spaces between blocks eliminated.</li>
     * <li>no extraneous spaces before or after elements added</li>
     * </ul>
     */
    public void testNormalize() throws Exception {
        
	String input = "<doc>\n\t  " +
	    "<block>\n\t foo\n\t <inline>foo\n\t bar</inline>\n\t baz\n\t </block>\n\t " +
	    "<block>\n\t foo\n\t <block>bar</block>\n\t baz</block>" +
	    "<block>\n\t foo<inline> foo bar </inline>baz \n\t </block>" +
	    "<block>\n\t foo<block>bar</block>baz \n\t</block>" +
	    "\n\t </doc>";

        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(this.getClass().getResource("test.css"));

	Document doc = createDocument(input, ss);

	//SpaceNormalizer norm = new SpaceNormalizer(ss);
	//norm.normalize(doc);

	Element element;

	element = doc.getRootElement();
	assertContent(element, new String[] { "<block>", 
					      "<block>", 
					      "<block>", 
					      "<block>" });

	Element[] children = element.getChildElements();

	//--- Block 0 ---

	assertContent(children[0], new String[] { "foo ",
						  "<inline>",
						  " baz" });
	Element[] c2 = children[0].getChildElements();
	assertContent(c2[0], new String[] { "foo bar" });

	//--- Block 1 ---

	assertContent(children[1], new String[] { "foo",
						  "<block>",
						  "baz" });
	c2 = children[1].getChildElements();
	assertContent(c2[0], new String[] { "bar" });

	//--- Block 2 ---

	assertContent(children[2], new String[] { "foo",
						  "<inline>",
						  "baz" });
	c2 = children[2].getChildElements();
	assertContent(c2[0], new String[] { "foo bar" });

	//--- Block 3 ---

	assertContent(children[3], new String[] { "foo",
						  "<block>",
						  "baz" });
	c2 = children[3].getChildElements();
	assertContent(c2[0], new String[] { "bar" });

    
	//========= Now test with a PRE element =========
    
	input = "<doc>\n\t  " +
	"<pre>\n\t foo\n\t <inline>\n\t foo\n\t bar\n \t</inline>\n\t baz\n\t </pre>\n\t " +
	"\n\t </doc>";

	doc = createDocument(input, ss);

	element = doc.getRootElement();
	assertContent(element, new String[] { "<pre>" });
    
	Element pre = element.getChildElements()[0];
	assertContent(pre, new String[] {
	        "\n\t foo\n\t ", "<inline>", "\n\t baz\n\t "
	});
    
	Element inline = pre.getChildElements()[0];
	assertContent(inline, new String[] { "\n\t foo\n\t bar\n \t" });
    
    }


    //========================================================= PRIVATE

//    private static final String DTD = "<!ELEMENT doc ANY>";

    /**
     * Asserts the content of the given element matches the given
     * list. If a string in content is enclosed in angle brackets,
     * it's assume to refer to the name of an element; otherwise, it
     * represents text content.
     */
    private void assertContent(Element element, String[] strings) {
	Node[] content = element.getChildNodes();
	assertEquals(strings.length, content.length);
	for (int i = 0; i < strings.length; i++) {
	    if (strings[i].startsWith("<")) {
		String name = strings[i].substring(1, strings[i].length() - 1);
		assertTrue(content[i] instanceof Element);
		assertEquals(name, ((Element)content[i]).getName());
	    } else {
		assertTrue(content[i] instanceof Text);
		assertEquals(strings[i], content[i].getText());
	    }
	}
    }

    private Document createDocument(String s, StyleSheet ss) 
	throws ParserConfigurationException, SAXException, IOException {

	SAXParserFactory factory = SAXParserFactory.newInstance();
	XMLReader xmlReader = factory.newSAXParser().getXMLReader();
	final StyleSheet mySS = ss;
	DocumentBuilder builder = new DocumentBuilder(new IWhitespacePolicyFactory() {
	    
	    public IWhitespacePolicy getPolicy(String publicId) {
	        return new CssWhitespacePolicy(mySS);
	    }
	    
	});

	InputSource is = new InputSource(new ByteArrayInputStream(s.getBytes()));
	xmlReader.setContentHandler(builder);
	//xmlReader.setDTDHandler(defaultHandler);
//	xmlReader.setEntityResolver(new EntityResolver() {
//	    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//	        System.out.println("resolveEntity called");
//	        return new InputSource(new ByteArrayInputStream(DTD.getBytes()));
//	    }
//        });
	//xmlReader.setErrorHandler(defaultHandler);
	xmlReader.parse(is);
	return builder.getDocument();
    }

}


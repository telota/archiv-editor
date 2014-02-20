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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.DocumentWriter;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.IWhitespacePolicy;
import net.sf.vex.dom.IWhitespacePolicyFactory;
import net.sf.vex.dom.Node;
import net.sf.vex.widget.CssWhitespacePolicy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Test the DocumentWriterImpl class.
 */
public class DocumentWriterTest extends TestCase {

    public void testWriteDocument() throws Exception {

        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(this.getClass().getResource("test.css"));

        URL docUrl = this.getClass().getResource("DocumentWriterTest1.xml");

        Document docOrig = readDocument(new InputSource(docUrl.toString()), ss);

        DocumentWriter dw = new DocumentWriter();
        dw.setWhitespacePolicy(new CssWhitespacePolicy(ss));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dw.write(docOrig, os);

        InputStream is = new ByteArrayInputStream(os.toByteArray());

        // Dump document to console
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        while (true) {
//            String s = br.readLine();
//            if (s == null)
//                break;
//            System.out.println(s);
//        }
//        is.reset();
        
        Document docNew = readDocument(new InputSource(is), ss);

        assertEquals(docOrig, docNew);
    }

    private void assertEquals(Document expected, Document actual)
        throws Exception {

        assertEquals(expected.getRootElement(), actual.getRootElement());
    }

    private void assertEquals(Element expected, Element actual)
        throws Exception {

        System.out.println("Checking " + actual.getName());
        assertEquals(expected.getName(), actual.getName());

        String[] expectedAttrs = expected.getAttributeNames();
        Arrays.sort(expectedAttrs);

        String[] actualAttrs = actual.getAttributeNames();
        Arrays.sort(actualAttrs);

        assertEquals(expectedAttrs.length, actualAttrs.length);
        for (int i = 0; i < expectedAttrs.length; i++) {
            assertEquals(expectedAttrs[i], actualAttrs[i]);
        }

        Node[] expectedContent = expected.getChildNodes();
        Node[] actualContent = actual.getChildNodes();
        assertEquals(expectedContent.length, actualContent.length);
        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(
                expectedContent[i].getClass(),
                actualContent[i].getClass());
            if (expectedContent[i] instanceof Element) {
                assertEquals(
                    (Element) expectedContent[i],
                    (Element) actualContent[i]);
            } else {
                assertEquals(
                    expectedContent[i].getText(),
                    actualContent[i].getText());
            }
        }
    }

    private static Document readDocument(InputSource is, StyleSheet ss)
        throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        DefaultHandler defaultHandler = new DefaultHandler();
        
        final IWhitespacePolicy policy = new CssWhitespacePolicy(ss);
            
        IWhitespacePolicyFactory wsFactory = new IWhitespacePolicyFactory() {
            public IWhitespacePolicy getPolicy(String publicId) {
                return policy; 
            }
        };
        
        net.sf.vex.dom.DocumentBuilder builder =
            new net.sf.vex.dom.DocumentBuilder(wsFactory);

        xmlReader.setContentHandler(builder);
        xmlReader.setDTDHandler(defaultHandler);
        xmlReader.setEntityResolver(defaultHandler);
        xmlReader.setErrorHandler(defaultHandler);
        xmlReader.parse(is);
        return builder.getDocument();
    }

    /**
     * Which elements are block elements for the purposes of our test cases.
     */
    /*
    private static boolean isBlockElement(Element element) {
        return element.getName().equals("html")
            || element.getName().equals("body")
            || element.getName().equals("p");
    }
*/
    /**
     * Parse the given document and pass them through to stdout to confirm
     * their goodness.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java DocumentWriterTest filename width");
            System.exit(1);
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[0]);
            int width = Integer.parseInt(args[1]);
            StyleSheetReader reader = new StyleSheetReader();
            StyleSheet ss =
                reader.read(DocumentWriterTest.class.getResource("test.css"));
            Document doc = readDocument(new InputSource(fis), ss);

            DocumentWriter writer = new DocumentWriter();
            writer.setWhitespacePolicy(new CssWhitespacePolicy(ss));
            writer.setWrapColumn(width);

            writer.write(doc, System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
                fis = null;
            }
        }
    }
}

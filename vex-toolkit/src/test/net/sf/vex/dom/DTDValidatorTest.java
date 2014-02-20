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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import net.sf.vex.dom.AttributeDefinition;
import net.sf.vex.dom.DTDValidator;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.RootElement;
import net.sf.vex.dom.Validator;

public class DTDValidatorTest extends TestCase {

    public void testAll() throws Exception {
	URL url = DTDValidatorTest.class.getResource("test1.dtd");
	Validator validator = DTDValidator.create(url);

	AttributeDefinition.Type adType = validator.getAttributeDefinitions("section")[0].getType();
    
	// Test serialization while we're at it
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(baos);
	oos.writeObject(validator);
    
	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	ObjectInputStream ois = new ObjectInputStream(bais);
	validator = (Validator) ois.readObject();

	AttributeDefinition.Type adType2 = validator.getAttributeDefinitions("section")[0].getType();
    
	assertSame(adType, adType2);
    
	Document doc;
	Set expected;

	doc = new Document(new RootElement("empty"));
        doc.setValidator(validator);
	assertEquals(Collections.EMPTY_SET, getValidItemsAt(doc, 1));

	doc = new Document(new RootElement("any"));
        doc.setValidator(validator);
	Set anySet = new HashSet();
	anySet.add(Validator.PCDATA);
	anySet.add("any");
	anySet.add("empty");
	anySet.add("section");
	anySet.add("title");
	anySet.add("para");
	anySet.add("emphasis");
	assertEquals(anySet, getValidItemsAt(doc, 1));

	// <section> <title> a b </title> <para> </para> </section>
	//          1       2 3 4        5      6       7
	doc = new Document(new RootElement("section"));
        doc.setValidator(validator);
	doc.insertElement(1, new Element("title"));
	doc.insertText(2, "ab");
	doc.insertElement(5, new Element("para"));

	assertEquals(Collections.EMPTY_SET, getValidItemsAt(doc, 1));
	expected = Collections.singleton(Validator.PCDATA);
	assertEquals(expected, getValidItemsAt(doc, 2));
	assertEquals(expected, getValidItemsAt(doc, 3));
	assertEquals(expected, getValidItemsAt(doc, 4));
	expected = Collections.singleton("para");
	assertEquals(expected, getValidItemsAt(doc, 5));
	assertEquals(expected, getValidItemsAt(doc, 7));
	expected = new HashSet();
	expected.add(Validator.PCDATA);
	expected.add("emphasis");
	assertEquals(expected, getValidItemsAt(doc, 6));

    }

    private Set getValidItemsAt(Document doc, int offset) {
        Element element = doc.getElementAt(offset);
        String[] prefix = doc.getNodeNames(element.getStartOffset() + 1, offset);
        String[] suffix = doc.getNodeNames(offset, element.getEndOffset());
        return doc.getValidator().getValidItems(element.getName(), prefix, suffix);
    }
    /*
    private void dump(Validator validator, Document doc, int offset) {
	Set set = getValidItemsAt(doc, offset);
	
	Iterator iter = set.iterator();
	while (iter.hasNext()) {
	    System.out.println("  " + iter.next());
	}
    }
    */
}


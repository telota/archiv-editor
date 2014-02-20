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
import net.sf.vex.dom.TextWrapper;

/**
 * Test the TextWrapper class.
 */
public class TextWrapperTest extends TestCase {

    public void testWrap() {
	String[] results;
	String[] inputs;
	TextWrapper wrapper = new TextWrapper();

	results = wrapper.wrap(40);
	assertEquals(0, results.length);

	inputs = new String[] {
	    "Here ",
	    "are ", 
	    "some ",
	    "short ",
	    "words ",
	    "and here are some long ones. We make sure we have some short stuff and some long stuff, just to make sure it all wraps." };

	for (int i = 0; i < inputs.length; i++) {
	    wrapper.add(inputs[i]);
	}
	results = wrapper.wrap(40);
	assertWidth(results, 40);
	assertPreserved(inputs, results);

	wrapper.clear();
	results = wrapper.wrap(40);
	assertEquals(0, results.length);

	String s1 = "yabba ";
	String s3 = "yabba yabba yabba ";
	wrapper.add(s1);
	wrapper.addNoSplit(s3);
	wrapper.addNoSplit(s3);
	wrapper.add(s1);
	results = wrapper.wrap(18);
	assertEquals(4, results.length);
	assertEquals(s1, results[0]);
	assertEquals(s3, results[1]);
	assertEquals(s3, results[2]);
	assertEquals(s1, results[3]);
    }

    /**
     * Ensure the two string arrays represent the same run of text
     * after all elements are concatenated.
     */
    private void assertPreserved(String[] inputs, String[] results) {
	StringBuffer inputSB = new StringBuffer();
	StringBuffer resultSB = new StringBuffer();
	for (int i = 0; i < inputs.length; i++) {
	    inputSB.append(inputs[i]);
	}
	for (int i = 0; i < results.length; i++) {
	    resultSB.append(results[i]);
	}
	assertEquals(inputSB.toString(), resultSB.toString());
    }

    /**
     * Ensure all lines fit within the given width, and that adding an
     * extra token from the next line would blow it.
     */
    private void assertWidth(String[] results, int width) {
	for (int i = 0; i < results.length; i++) {
	    assertTrue(results[i].length() > 0);
	    assertTrue(results[i].length() <= width);
	    if (i < results.length-1) {
		assertTrue(results[i].length() 
			   + getToken(results[i+1]).length() > width);
	    }
	}
    }

    /**
     * Get a token from a string.
     */
    private String getToken(String s) {
	int i = 0;
	while (i < s.length() && !Character.isWhitespace(s.charAt(i))) {
	    i++;
	}
	while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
	    i++;
	}
	return s.substring(0, i);
    }

}


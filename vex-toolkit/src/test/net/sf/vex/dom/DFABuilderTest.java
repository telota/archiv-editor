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

import net.sf.vex.dom.*;
import junit.framework.*;

/**
 * Test the <code>net.sf.vex.dom</code> package.
 */
public class DFABuilderTest extends TestCase {

    public void testAll() {
	DFAState dfa;

	dfa = parseRegex("a");
	assertTrue(matches(dfa, "a"));
	assertFalse(matches(dfa, ""));
	assertFalse(matches(dfa, "aa"));

	dfa = parseRegex("a?");
	assertTrue(matches(dfa, ""));
	assertTrue(matches(dfa, "a"));
	assertFalse(matches(dfa, "aa"));

	dfa = parseRegex("a*");
	assertTrue(matches(dfa, ""));
	assertTrue(matches(dfa, "a"));
	assertTrue(matches(dfa, "aa"));
	assertTrue(matches(dfa, "aaa"));

	dfa = parseRegex("a+");
	assertFalse(matches(dfa, ""));
	assertTrue(matches(dfa, "a"));
	assertTrue(matches(dfa, "aa"));
	assertTrue(matches(dfa, "aaa"));

	dfa = parseRegex("ab");
	assertFalse(matches(dfa, ""));
	assertFalse(matches(dfa, "a"));
	assertFalse(matches(dfa, "b"));
	assertTrue(matches(dfa, "ab"));
	assertFalse(matches(dfa, "aab"));
	assertFalse(matches(dfa, "abb"));

	dfa = parseRegex("a|b");
	assertFalse(matches(dfa, ""));
	assertTrue(matches(dfa, "a"));
	assertTrue(matches(dfa, "b"));
	assertFalse(matches(dfa, "ab"));
	assertFalse(matches(dfa, "ba"));

	dfa = parseRegex("a?b");
	assertFalse(matches(dfa, ""));
	assertFalse(matches(dfa, "a"));
	assertTrue(matches(dfa, "b"));
	assertTrue(matches(dfa, "ab"));
	assertFalse(matches(dfa, "aa"));
	assertFalse(matches(dfa, "bb"));
	assertFalse(matches(dfa, "aab"));

	dfa = parseRegex("a*b");
	assertFalse(matches(dfa, ""));
	assertFalse(matches(dfa, "a"));
	assertTrue(matches(dfa, "b"));
	assertTrue(matches(dfa, "ab"));
	assertFalse(matches(dfa, "aa"));
	assertFalse(matches(dfa, "bb"));
	assertTrue(matches(dfa, "aab"));
	assertTrue(matches(dfa, "aaab"));
	assertFalse(matches(dfa, "aabb"));

	dfa = parseRegex("a+b");
	assertFalse(matches(dfa, ""));
	assertFalse(matches(dfa, "a"));
	assertFalse(matches(dfa, "b"));
	assertTrue(matches(dfa, "ab"));
	assertFalse(matches(dfa, "aa"));
	assertFalse(matches(dfa, "bb"));
	assertTrue(matches(dfa, "aab"));
	assertTrue(matches(dfa, "aaab"));
	assertFalse(matches(dfa, "aabb"));

	dfa = parseRegex("(ts?)?p*");
	assertTrue(matches(dfa, ""));
	assertTrue(matches(dfa, "t"));
	assertTrue(matches(dfa, "ts"));
	assertTrue(matches(dfa, "tsppp"));
	assertTrue(matches(dfa, "ppp"));
	assertFalse(matches(dfa, "s"));
	assertFalse(matches(dfa, "sp"));
    }

    /**
     * Returns true if the given DFA matches the given string.
     */
    private boolean matches(DFAState state, String s) {
	for (int i = 0; i < s.length(); i++) {
	    String symbol = String.valueOf(s.charAt(i));
	    state = state.getNextState(symbol);
	    if (state == null) {
		return false;
	    }
	}
	return state.isAccepting();
    }

    /**
     * Parses simple regex's into nodes.
     */
    private DFAState parseRegex(String s) {
	ParseState ps = new ParseState();
	ps.s = s; 
	ps.pos = 0;
	return DFABuilder.createDFA(parseRegex(ps));
    }

    private static class ParseState {
	public String s;
	public int pos;
	public char getChar() {
	    if (pos == s.length()) {
		return 0;
	    } else {
		return s.charAt(pos++);
	    }
	}
	public char peekChar() {
	    if (pos == s.length()) {
		return 0;
	    } else {
		return s.charAt(pos);
	    }
	}
    }

    //
    // regex ::= null
    //        |  part regex*
    //        |  ')'
    //
    // part ::= repeating [ '|' part ]
    //
    // repeating ::= simplepart [ '?' | '*' | '+' ]
    //
    // simplepart ::= char
    //             |  '(' regex
    //
    private DFABuilder.Node parseRegex(ParseState ps) {

	if (ps.peekChar() == ')') {
	    ps.getChar();
	    return null;
	}

	DFABuilder.Node node = parsePart(ps);
	if (node == null) {
	    return node;
	}

	DFABuilder.Node node2 = parseRegex(ps);
	if (node2 != null) {
	    node =  DFABuilder.createSequenceNode(node, node2);
	}

	return node;
    }

    private DFABuilder.Node parsePart(ParseState ps) {
	DFABuilder.Node node = parseRepeating(ps);
	if (node == null) {
	    return node;
	}

	if (ps.peekChar() == '|') {
	    ps.getChar();
	    DFABuilder.Node node2 = parsePart(ps);
	    if (node2 != null) {
		node = DFABuilder.createChoiceNode(node, node2);
	    }
	}

	return node;
    }


    private DFABuilder.Node parseRepeating(ParseState ps) {

	DFABuilder.Node node = parseSimplePart(ps);
	if (node == null) {
	    return node;
	}

	if (ps.peekChar() == '?') {
	    ps.getChar();
	    node = DFABuilder.createOptionalNode(node);
	} else if (ps.peekChar() == '*') {
	    ps.getChar();
	    node = DFABuilder.createRepeatingNode(node, 0);
	} else if (ps.peekChar() == '+') {
	    ps.getChar();
	    node = DFABuilder.createRepeatingNode(node, 1);
	}

	return node;
    }

    private DFABuilder.Node parseSimplePart(ParseState ps) {

	DFABuilder.Node node;

	char c = ps.getChar();

	if (c == 0) {
	    return null;
	} else if (c == '(') {
	    node = parseRegex(ps);
	} else {
	    node = DFABuilder.createSymbolNode(String.valueOf(c));
	}

	return node;
    }

    /*
    private DFABuilder.Node parseRegexToNode(ParseState ps) {
	DFABuilder.Node node = null;
	while (ps.pos < ps.s.length()) {
	    DFABuilder.Node nextNode;
	    char c = ps.s.charAt(ps.pos);
	    ps.pos++;
	    if (c == ')') {
		return node;
	    } else if (c == '(') {
		nextNode = parseRegexToNode(ps);
	    } else if (c == '?' || c == '*' || c == '+') {
		throw new RuntimeException("Misplaced '?': " + ps.s);
	    } else {
		nextNode = DFABuilder.createSymbolNode(String.valueOf(c));
	    }

	    if (ps.pos < ps.s.length()) {
		char c2 = ps.s.charAt(ps.pos);
		if (c2 == '?') {
		    nextNode = DFABuilder.createOptionalNode(nextNode);
		    ps.pos++;
		} else if (c2 == '*') {
		    nextNode = DFABuilder.createRepeatingNode(nextNode, 0);
		    ps.pos++;
		} else if (c2 == '+') {
		    nextNode = DFABuilder.createRepeatingNode(nextNode, 1);
		    ps.pos++;
		}
	    }

	    if (node == null) {
		node = nextNode;
	    } else {
		node = DFABuilder.createSequenceNode(node, nextNode);
	    }
	}

	return node;
    }
    */
}


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
package net.sf.vex.dom;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * Tools for building a deterministic finite automaton (DFA)
 * recognizer for regular expression-like languages.
 */
public class DFABuilder {

    /**
     * Node represents a node in an abstract syntax tree. The first
     * step to creating a DFA is to build an AST using the given
     * createXxx methods.
     */
    public interface Node {
	public void accept(NodeVisitor visitor);
	public Object clone();
	public Set getFirstPos();
	public Set getLastPos();
	public boolean isNullable();
    }

    /**
     * Create a node that represents a choice between two nodes.
     *
     * @param child1 first choice
     * @param child2 second choice
     */
    public static Node createChoiceNode(Node child1, Node child2) {
	return new OrNode(child1, child2);
    }

    /**
     * Create a DFA given the root node of the syntax tree.
     *
     * @return Initial state of the resulting DFA.
     * @param root Root node of the syntax tree.
     */
    public static DFAState createDFA(Node root) {

	// Append a sentinel to indicate accepting states.
	SymbolNode sentinelNode = new SymbolNode(Sentinel.getInstance());
	Node fakeRoot = new CatNode(root, sentinelNode);

	// map symbol node set => state in the new DFA
	Map stateMap = new HashMap();

	// symbol node sets we have considered
	Set marked = new HashSet();

	// stack of symbol node sets we have yet to consider
	Stack unmarked = new Stack();

	// calculate followPos and symbolMap
	FollowPosBuilder fpb = new FollowPosBuilder();
	fakeRoot.accept(fpb);

	// map symbol node => set of symbol nodes that follow it
	Map followPos = fpb.getFollowPos();

	// map symbol => set of symbol nodes that represent it
	Map symbolMap = fpb.getSymbolMap();

	Set nodeSet = fakeRoot.getFirstPos();
	DFAState startState = new DFAState();
	if (nodeSet.contains(sentinelNode)) {
	    startState.setAccepting(true);
	}

	stateMap.put(nodeSet, startState);

	unmarked.push(nodeSet);

	while (unmarked.size() > 0) {
	    nodeSet = (Set) unmarked.pop();
	    marked.add(nodeSet);
	    DFAState state = (DFAState) stateMap.get(nodeSet);
	    if (state == null) {
		state = new DFAState();
		stateMap.put(nodeSet, state);
	    }

	    Iterator iterSymbols = symbolMap.keySet().iterator();
	    while (iterSymbols.hasNext()) {
		Object symbol = iterSymbols.next();
		Set targetSet = new HashSet();
		Iterator iterNodes = nodeSet.iterator();
		while (iterNodes.hasNext()) {
		    SymbolNode node = (SymbolNode) iterNodes.next();
		    if (node.getSymbol().equals(symbol)) {
			targetSet.addAll((Set)followPos.get(node));
		    }
		}

		if (!targetSet.isEmpty()) { 
		    if (!unmarked.contains(targetSet) 
			&& !marked.contains(targetSet)) {

			unmarked.push(targetSet);
		    }

		    DFAState targetState = (DFAState) 
			stateMap.get(targetSet);

		    if (targetState == null) {
			targetState = new DFAState();
			if (targetSet.contains(sentinelNode)) {
			    targetState.setAccepting(true);
			}
			stateMap.put(targetSet, targetState);
		    }

		    state.addTransition(symbol, targetState);
		}
	    }

	    
	}

	return startState;
    }

    /**
     * Create optional node.
     *
     * @param child Node that is optional.
     */
    public static Node createOptionalNode(Node child) {
	return new OrNode(child, new NullNode());
    }

    /**
     * Create a repeating node.
     *
     * @param child Node that can be repeated.
     * @param minRepeat minimum number of times the node can be repeated.
     */
    public static Node createRepeatingNode(Node child, int minRepeat) {
	Node node = new StarNode(child);
	for (int i = 0; i < minRepeat; i++) {
	    node = new CatNode(node, (Node) child.clone());
	}
	return node;
    }

    /**
     * Creates a node representing a sequence of two other nodes.
     *
     * @param child1 first node in the sequence.
     * @param child2 second node in the sequence.
     */
    public static Node createSequenceNode(Node child1, Node child2) {
	return new CatNode(child1, child2);
    }

    /**
     * Create a node for a symbol.
     *
     * @param symbol Symbol contained by the node.
     */
    public static Node createSymbolNode(Object symbol) {
	return new SymbolNode(symbol);
    }

    //============================================================ PRIVATE

    /**
     * Implementation of node that keeps firstPos, lastPos, and nullable
     * as instance variables. The accept method is undefined.
     */
    private abstract static class AbstractNode implements Node {
	protected Set firstPos;
	protected Set lastPos;
	protected boolean nullable;

	public abstract Object clone();

	public Set getFirstPos() {
	    return this.firstPos;
	}
	public Set getLastPos() {
	    return this.lastPos;
	}
	public boolean isNullable() {
	    return this.nullable;
	}

	protected Set union(Set set1, Set set2) {
	    Set retval = new HashSet();
	    retval.addAll(set1);
	    retval.addAll(set2);
	    return retval;
	}
    }

    /**
     * Node representing a sequence of two nodes.
     */
    private static class CatNode extends AbstractNode {
	private Node leftChild;
	private Node rightChild;

	public CatNode(Node leftChild, Node rightChild) {
	    this.leftChild = leftChild;
	    this.rightChild = rightChild;

	    if (leftChild.isNullable()) {
		this.firstPos = union(leftChild.getFirstPos(), 
				      rightChild.getFirstPos());
	    } else {
		this.firstPos = leftChild.getFirstPos();
	    }

	    if (rightChild.isNullable()) {
		this.lastPos = union(leftChild.getLastPos(), 
				     rightChild.getLastPos());
	    } else {
		this.lastPos = rightChild.getLastPos();
	    }

	    this.nullable = leftChild.isNullable() && rightChild.isNullable();
	}

	public void accept(NodeVisitor visitor) { 
	    leftChild.accept(visitor);
	    rightChild.accept(visitor);
	    visitor.visitCatNode(this);
	}

	public Object clone() {
	    return new CatNode((Node) this.leftChild.clone(),
			       (Node) this.rightChild.clone());
	}

	public Node getLeftChild() {
	    return this.leftChild;
	}

	public Node getRightChild() {
	    return this.rightChild;
	}
    }

    /**
     * Builds the followPos function. The function is represented by a
     * map from symbol nodes to sets of symbol nodes that can follow
     * them.  Also generates a map of symbols to sets of symbol nodes
     * that represent them.
     */
    private static class FollowPosBuilder implements NodeVisitor {
	private Map followPos = new HashMap();
	private Map symbolMap = new HashMap();

	public Map getFollowPos() {
	    return this.followPos;
	}

	public Map getSymbolMap() {
	    return this.symbolMap;
	}

	public void visitCatNode(CatNode node) {
	    Iterator iter = node.getLeftChild().getLastPos().iterator();
	    while (iter.hasNext()) {
		SymbolNode symbolNode = (SymbolNode) iter.next();
		Set set = this.getFollowPos(symbolNode);
		set.addAll(node.getRightChild().getFirstPos());
	    }
	}

	public void visitNullNode(NullNode node) {
	}

	public void visitOrNode(OrNode node) {
	}

	public void visitStarNode(StarNode node) {
	    Iterator iter = node.getChild().getLastPos().iterator();
	    while (iter.hasNext()) {
		SymbolNode symbolNode = (SymbolNode) iter.next();
		Set set = this.getFollowPos(symbolNode);
		set.addAll(node.getChild().getFirstPos());
	    }
	}

	public void visitSymbolNode(SymbolNode node) {

	    // Done by getFollowPos(SymbolNode)
	    //this.followPos.put(node, new HashSet());

	    // Ensure we have an entry for this symbol
	    this.getFollowPos(node);

	    Object symbol = node.getSymbol();
	    Set symbolNodeSet = (Set) this.symbolMap.get(symbol);
	    if (symbolNodeSet == null) {
		symbolNodeSet = new HashSet();
		this.symbolMap.put(symbol, symbolNodeSet);
	    }
	    symbolNodeSet.add(node);
	}

	private Set getFollowPos(SymbolNode node) {
	    Set ret = (Set) this.followPos.get(node);
	    if (ret == null) {
		ret = new HashSet();
		this.followPos.put(node, ret);
	    }
	    return ret;
	}

    }

    /**
     * Describes a visitor that can walk an AST.
     */
    private interface NodeVisitor {
	public void visitCatNode(CatNode node);
	public void visitNullNode(NullNode node);
	public void visitOrNode(OrNode node);
	public void visitStarNode(StarNode node);
	public void visitSymbolNode(SymbolNode node);
    }

    /**
     * Node representing nothing. It is used with OrNode to construct an
     * optional entry.
     */
    private static class NullNode extends AbstractNode {
	public NullNode() {
	    this.firstPos = Collections.EMPTY_SET;
	    this.lastPos = Collections.EMPTY_SET;
	    this.nullable = true;
	}

	public void accept(NodeVisitor visitor) { 
	    visitor.visitNullNode(this);
	}

	public Object clone() {
	    return new NullNode();
	}
    }

    /**
     * Node representing a choice between two alternatives.
     */
    private static class OrNode extends AbstractNode {
	private Node leftChild;
	private Node rightChild;

	public OrNode(Node leftChild, Node rightChild) {
	    this.leftChild = leftChild;
	    this.rightChild = rightChild;

	    this.firstPos = union(leftChild.getFirstPos(), 
				  rightChild.getFirstPos());
	    this.lastPos = union(leftChild.getLastPos(),
				 rightChild.getLastPos());

	    this.nullable = leftChild.isNullable() || rightChild.isNullable();
	}

	public void accept(NodeVisitor visitor) { 
	    leftChild.accept(visitor);
	    rightChild.accept(visitor);
	    visitor.visitOrNode(this);
	}

	public Object clone() {
	    return new OrNode((Node) this.leftChild.clone(),
					 (Node) this.rightChild.clone());
	}

	public Node getLeftChild() {
	    return this.leftChild;
	}

	public Node getRightChild() {
	    return this.rightChild;
	}
    }

    /**
     * Symbol appended to the AST to mark accepting states.
     */
    private static class Sentinel {
	private static final Sentinel instance = new Sentinel();

	private Sentinel() {
	}

	public static Sentinel getInstance() {
	    return instance;
	}

	public String toString() {
	    return "#";
	}
    }

    /**
     * Node representing zero or more repetitions of its child.
     */
    private static class StarNode extends AbstractNode {
	private Node child;

	public StarNode(Node child) {
	    this.child = child;
	    this.firstPos = child.getFirstPos();
	    this.lastPos = child.getLastPos();
	    this.nullable = true;
	}
	
	public void accept(NodeVisitor visitor) { 
	    child.accept(visitor);
	    visitor.visitStarNode(this);
	}

	public Object clone() {
	    return new StarNode((Node) this.child.clone());
	}

	public Node getChild() {
	    return this.child;
	}
    }

    /**
     * Node representing a symbol.
     */
    private static class SymbolNode extends AbstractNode {

	private static int pos = 1;
	private int myPos;
	private Object symbol;

	public SymbolNode (Object symbol) {
	    this.symbol = symbol;
	    this.firstPos = Collections.singleton(this);
	    this.lastPos = Collections.singleton(this);
	    this.nullable = false;
	    this.myPos = pos++;
	}

	public void accept(NodeVisitor visitor) { 
	    visitor.visitSymbolNode(this);
	}

	public Object clone() {
	    return new SymbolNode(this.symbol);
	}

	public int getMyPos() {
	    return this.myPos;
	}

	public Object getSymbol() {
	    return this.symbol;
	}
    }


    /*
    private static String snSetToString(Set set) {
	StringBuffer sb = new StringBuffer();
	sb.append("{ ");
	Iterator i2 = set.iterator();
	while (i2.hasNext()) {
	    SymbolNode sn2 = (SymbolNode) i2.next();
	    sb.append(sn2.getMyPos());
	    sb.append(" ");
	}
	sb.append("}");
	return sb.toString();

    }
    */
}


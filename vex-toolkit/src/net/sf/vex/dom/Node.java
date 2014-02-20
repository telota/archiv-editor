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

/**
 * <code>Node</code> represents a component of an XML document. .
 */
public class Node {

    private Content content = null;
    private Position start = null;
    private Position end = null;

    /**
     * Class constructor.
     */
    public Node() {
    }


    /**
     * Returns the document associated with this node. Null if the node
     * has not yet been inserted into a document.
     */
    public Content getContent() {
	return this.content;
    }
    
    /**
     * Returns the character offset corresponding to the end of the
     * node.
     */
    public int getEndOffset() {
	return this.end.getOffset();
    }

    /**
     * Returns the <code>Position</code> corresponding to the end of
     * the node.
     */
    public Position getEndPosition() {
	return this.end;
    }

    /**
     * Returns the character offset corresponding to the start of the
     * node.
     */
    public int getStartOffset() {
	return this.start.getOffset();
    }

    /**
     * Returns the <code>Position</code> corresponding to the start of
     * the node.
     */
    public Position getStartPosition() {
	return this.start;
    }

    /**
     * Returns the text contained by this node. If this node is an element,
     * the text in all child nodes is included.
     */
    public String getText() {
	return this.content.getString(this.getStartOffset(),
				      this.getEndOffset() - this.getStartOffset());
    }

    /**
     * Sets the content of the node
     * 
     * @param content Content object holding the node's content
     * @param startOffset offset at which the node's content starts
     * @param endOffset offset at which the node's content ends
     */
    void setContent(Content content, 
		    int startOffset, 
		    int endOffset) {

	this.content = content;
	this.start = content.createPosition(startOffset);
	this.end = content.createPosition(endOffset);
    }

}


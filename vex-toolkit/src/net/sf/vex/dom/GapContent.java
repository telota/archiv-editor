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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of the <code>Content</code> interface that manages
 * changes efficiently. Implements a buffer that keeps its free space
 * (the "gap") at the location of the last change. Insertions at the
 * start of the gap require no other chars to be moved so long as the
 * insertion is smaller than the gap. Deletions that end of the gap
 * are also very efficent. Furthermore, changes near the gap require
 * relatively few characters to be moved.
 */
public class GapContent implements Content {

    private char[] content;
    private int gapStart;
    private int gapEnd;
    private final Map positions = new HashMap();

    /**
     * Class constructor.
     *
     * @param initialCapacity initial capacity of the content.
     */
    public GapContent(int initialCapacity) {

	assertPositive(initialCapacity);

	this.content = new char[initialCapacity];
	this.gapStart = 0;
	this.gapEnd = initialCapacity;
    }

    /**
     * Creates a new Position object at the given initial offset.
     *
     * @param offset initial offset of the position
     */
    public Position createPosition(int offset) {

	assertOffset(offset, 0, this.getLength());

	Position pos = new GapContentPosition(offset);
	this.positions.put(pos, pos);
	
	return pos;
    }

    /**
     * Insert a string into the content.
     *
     * @param offset Offset at which to insert the string.
     * @param s String to insert.
     */
    public void insertString(int offset, String s) {

	assertOffset(offset, 0, this.getLength());

	if (s.length() > (this.gapEnd - this.gapStart)) {
	    this.expandContent(this.getLength() + s.length());
	}
    
	//
	// Optimization: no need to update positions if we're inserting
	// after existing content (offset == this.getLength()) and if
	// we don't have to move the gap to do it (offset == gapStart).
	//
	// This significantly improves document load speed.
	//
	boolean atEnd = (offset == this.getLength() && offset == gapStart);
    
	this.moveGap(offset);
	s.getChars(0, s.length(), this.content, offset);
	this.gapStart += s.length();

	if (!atEnd) {
	    //
	    // Update positions
	    //
	    for (Iterator i = this.positions.keySet().iterator(); i.hasNext(); ) {
	        GapContentPosition pos = (GapContentPosition) i.next();
	        if (pos.getOffset() >= offset) {
	            pos.setOffset(pos.getOffset() + s.length());
	        }
	    }
	}
    }
	

    /**
     * Deletes the given range of characters.
     *
     * @param offset Offset from which characters should be deleted.
     * @param length Number of characters to delete.
     */
    public void remove(int offset, int length) {

	assertOffset(offset, 0, this.getLength() - length);
	assertPositive(length);
	
	this.moveGap(offset + length);
	this.gapStart -= length;

	for (Iterator i = this.positions.keySet().iterator(); i.hasNext(); ) {
	    GapContentPosition pos = (GapContentPosition) i.next();
	    if (pos.getOffset() >= offset + length) {
	        pos.setOffset(pos.getOffset() - length);
	    } else if (pos.getOffset() >= offset) {
	        pos.setOffset(offset);
	    }
	}
    }

    /**
     * Gets a substring of the content.
     *
     * @param offset Offset at which the string begins.
     * @param length Number of characters to return.
     */
    public String getString(int offset, int length) {

	assertOffset(offset, 0, this.getLength() - length);
	assertPositive(length);

	if (offset + length <= this.gapStart) {
	    return new String(this.content,
			      offset,
			      length);
	} else if (offset >= this.gapStart) {
	    return new String(this.content,
			      offset - this.gapStart + this.gapEnd,
			      length);
	} else {
	    StringBuffer sb = new StringBuffer(length);
	    sb.append(this.content, 
		      offset, 
		      this.gapStart - offset);
	    sb.append(this.content,
		      this.gapEnd,
		      offset + length - this.gapStart);
	    return sb.toString();
	}
    }

    /**
     * Return the length of the content.
     */
    public int getLength() {
	return this.content.length - (this.gapEnd - this.gapStart);
    }

    //====================================================== PRIVATE

    private static final int GROWTH_SLOWDOWN_SIZE = 100000;
    private static final int GROWTH_RATE_FAST = 2;
    private static final float GROWTH_RATE_SLOW = 1.1f;
    
    /**
     * Implementation of the Position interface.
     */
    private static class GapContentPosition implements Position {

	private int offset;

	public GapContentPosition(int offset) {
	    this.offset = offset;
	}

	public int getOffset() {
	    return this.offset;
	}

	public void setOffset(int offset) {
	    this.offset = offset;
	}
    
	public String toString() {
	    return Integer.toString(this.offset);
	}
    }

    /**
     * Assert that the given offset is within the given range,
     * throwing IllegalArgumentException if not.
     */
    private static void assertOffset(int offset, int min, int max) {
	if (offset < min || offset > max) {
	    throw new IllegalArgumentException("Bad offset " + offset +
					       "must be between " + min +
					       " and " + max);
	}
    }

    /**
     * Assert that the given value is zero or positive.
     * throwing IllegalArgumentException if not.
     */
    private static void assertPositive(int value) {
	if (value < 0) {
	    throw new IllegalArgumentException("Value should be zero or positive, but it was " + value);
	}
    }

    /**
     * Expand the content array to fit at least the given length.
     */
    private void expandContent(int newLength) {

        // grow quickly when small, slower when large  	
    	
	int newCapacity;
	
	if (newLength < GROWTH_SLOWDOWN_SIZE) {
	    newCapacity = Math.max((int) (newLength * GROWTH_RATE_FAST), 32);
	} else {
	    newCapacity = (int)(newLength * GROWTH_RATE_SLOW);
	}
	
	char[] newContent = new char[newCapacity];

	System.arraycopy(this.content, 0,
			 newContent, 0,
			 this.gapStart);

	int tailLength = this.content.length - this.gapEnd;
	System.arraycopy(this.content, this.gapEnd,
			 newContent, newCapacity - tailLength,
			 tailLength);

	this.content = newContent;
	this.gapEnd = newCapacity - tailLength;
    }

    /**
     * Move the gap to the given offset.
     */
    private void moveGap(int offset) {

	assertOffset(offset, 0, this.getLength());

	if (offset <= this.gapStart) {
	    int length = this.gapStart - offset;
	    System.arraycopy(this.content, offset,
			     this.content, this.gapEnd - length,
			     length);
	    this.gapStart -= length;
	    this.gapEnd -= length;
	} else {
	    int length = offset - this.gapStart;
	    System.arraycopy(this.content, this.gapEnd,
			     this.content, this.gapStart,
			     length);
	    this.gapStart += length;
	    this.gapEnd += length;
	}
    }
}

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
package net.sf.vex.core;

/**
 * Represents a range of integers. Zero-length ranges (i.e. ranges where
 * start == end) are permitted. This class is immutable.
 */
public class IntRange {

    /**
     * Class constuctor.
     * @param start Start of the range.
     * @param end End of the range. Must be >= start.
     */
    public IntRange(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("start (" + start + ") is greater than end (" + end + ")");
        }
        this.start = start;
        this.end = end;
    }
    
    /**
     * Returns the start of the range.
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Returns the end of the range.
     */
    public int getEnd() {
        return this.end;
    }
    
    /**
     * Returns the range that represents the intersection of this range
     * and the given range. If the ranges do not intersect, returns null.
     * May return an empty range.
     * @param range Range with which to perform an intersection.
     */
    public IntRange intersection(IntRange range) {
        if (this.intersects(range)) {
            return new IntRange(Math.max(this.start, range.start), Math.min(this.end, range.end));
        } else {
            return null;
        }
        
    }

    /**
     * Returns true if this range intersects the given range, even if the 
     * result would be an empty range.
     * @param range Range with which to intersect.
     */
    public boolean intersects(IntRange range) {
        return this.start <= range.end && this.end >= range.start;
    }

    /**
     * Returns true if start and end are equal.
     */
    public boolean isEmpty() {
        return start == end;
    }

    /**
     * Returns a range that is the union of this range and the given range.
     * If the ranges are disjoint, the gap between the ranges is included
     * in the result.
     * @param range Rnage with which to perform the union
     */
    public IntRange union(IntRange range) {
        return new IntRange(Math.min(this.start, range.start), Math.min(this.end, range.end));
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("IntRange(");
        sb.append(start);
        sb.append(",");
        sb.append(end);
        sb.append(")");
        return sb.toString();
    }
    //============================================================= PRIVATE
    
    private int start;
    private int end;
}

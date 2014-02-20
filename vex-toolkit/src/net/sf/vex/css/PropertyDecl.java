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
package net.sf.vex.css;

import java.io.Serializable;

import org.w3c.css.sac.LexicalUnit;

/**
 * Represents a particular CSS property declaration.
 */
public class PropertyDecl implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final byte SOURCE_DEFAULT = 0;
    public static final byte SOURCE_AUTHOR = 1;
    public static final byte SOURCE_USER = 2;

    private Rule rule;
    private String property;
    private LexicalUnit value;
    private boolean important;

    /**
     * Class constructor.
     */
    public PropertyDecl(Rule rule,
			String property,
			LexicalUnit value,
			boolean important) {
	this.rule = rule;
	this.property = property;
	this.value = value;
	this.important = important;
    }

    /**
     * Implementation of <code>Comparable.compareTo(Object)</code>
     * that implements CSS cascade ordering.
     */
    public int compareTo(Object o) {
	PropertyDecl other = (PropertyDecl) o;
	int thisWeight = this.getWeight();
	int otherWeight = other.getWeight();
	if (thisWeight != otherWeight) {
	    return thisWeight - otherWeight;
	}

	int thisSpec = this.getRule().getSpecificity();
	int otherSpec = other.getRule().getSpecificity();

	return thisSpec - otherSpec;
    }

    /**
     * Return the value of the <code>important</code> property.
     */
    public boolean isImportant() {
        return this.important;
    }

    /**
     * Return the value of the <code>property</code> property.
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * Return the value of the <code>rule</code> property.
     */
    public Rule getRule() {
        return this.rule;
    }

    /**
     * Return the value of the <code>value</code> property.
     */
    public LexicalUnit getValue() {
        return this.value;
    }


    //===================================================== PRIVATE

    /**
     * Returns the weight of this declaration, as follows...
     *
     * <pre>
     * 4 => user stylesheet, important decl
     * 3 => author stylesheet, important decl
     * 2 => author stylesheet, not important
     * 1 => user stylesheet, not important
     * 0 => default stylesheet
     * </pre>
     */
    private int getWeight() {
        int source = this.getRule().getSource();
        if (this.isImportant() && source == StyleSheet.SOURCE_USER) {
            return 4;
        } else if (this.isImportant() && source == StyleSheet.SOURCE_AUTHOR) {
            return 3;
        } else if (!this.isImportant() && source == StyleSheet.SOURCE_AUTHOR) {
            return 2;
        } else if (!this.isImportant() && source == StyleSheet.SOURCE_USER) {
            return 1;
        } else {
            return 0;
        }
    }
}


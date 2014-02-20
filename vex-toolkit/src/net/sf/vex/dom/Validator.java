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

import java.io.Serializable;
import java.util.Set;

/**
 * Represents an object that can validate the structure of a document.
 * Validators must be serializable.
 */
public interface Validator extends Serializable {

    /**
     * String indicating that character data is allowed at the given
     * point in the document.
     */
    public static final String PCDATA = "#PCDATA";

    /**
     * Returns the AttributeDefinition for a particular attribute.
     * @param element Name of the element.
     * @param attribute Name of the attribute.
     */
    public AttributeDefinition getAttributeDefinition(String element, String attribute);

    /**
     * Returns the attribute definitions that apply to the given element.
     * @param element Name of the element to check.
     */
    public AttributeDefinition[] getAttributeDefinitions(String element);

    /**
     * Returns a set of Strings representing valid root elements for the
     * given document type.
     */
    public Set getValidRootElements();
    
    /**
     * Returns a set of Strings representing items that are valid at
     * point in the child nodes of a given element. Each string is either
     * an element name or Validator.PCDATA.
     *
     * @param element Name of the parent element.
     * @param prefix Array of strings representing nodes coming before the 
     * insertion point
     * @param suffix Array of strings representing nodes coming after the
     * insertion point
     */
    public Set getValidItems(String element, String[] prefix, String[] suffix);

    /**
     * Returns true if the given sequence is valid for the given element.
     * Accepts three sequences, which will be concatenated before doing
     * the check. 
     * 
     * @param element Name of the element being tested.
     * @param nodes Array of element names and Validator.PCDATA.
     * @param partial If true, an valid but incomplete sequence is acceptable.
     */
    public boolean isValidSequence(
        String element,
        String[] nodes,
        boolean partial);
                                 
    /**
     * Returns true if the given sequence is valid for the given element.
     * Accepts three sequences, which will be concatenated before doing
     * the check. 
     * 
     * @param element Name of the element being tested.
     * @param seq1 Array of element names and Validator.PCDATA.
     * @param seq2 Array of element names and Validator.PCDATA. May be null or empty.
     * @param seq3 Array of element names and Validator.PCDATA. May be null or empty.
     * @param partial If true, an valid but incomplete sequence is acceptable.
     */
    public boolean isValidSequence(
        String element,
        String[] seq1,
        String[] seq2,
        String[] seq3,
        boolean partial);

}


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

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAny;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDCardinal;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDContainer;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDNotationList;
import com.wutka.dtd.DTDPCData;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

/**
 * A validator driven by a DTD.
 */
public class DTDValidator extends AbstractValidator {

    // DFA representing an EMPTY element; just a single non-accepting state
    // with no transitions.
    private static final DFAState emptyDFA = new DFAState();

    // map element names to DFAs
    private Map elementDFAs = new HashMap();

    // list of all element names plus PCDATA
    private Set anySet;
    
    // map element names to arrays of AttributeDefinition objects
    private Map attributeArrays = new HashMap();
    
    // map element names to maps of attribute name to attribute def
    private Map attributeMaps = new HashMap();

    /**
     * Creates a instance of DtdValidator given a URL.
     *
     * @param url URL of the DTD file to use.
     */
    public static DTDValidator create(URL url) throws IOException {

	// Compute the DFAs for each element in the DTD

	DTDParser parser = new DTDParser(url);
	DTD dtd = parser.parse();

	DTDValidator validator = new DTDValidator();
	Iterator iter = dtd.elements.values().iterator();
	while (iter.hasNext()) {
	    DTDElement element = (DTDElement) iter.next();
	    DFAState dfa;
	    if (element.getContent() instanceof DTDEmpty) {
		dfa = emptyDFA;
	    } else if (element.getContent() instanceof DTDAny) {
		dfa = null;
	    } else {
		DFABuilder.Node node = createDFANode(element.getContent());
		dfa = DFABuilder.createDFA(node);
	    }
	    validator.elementDFAs.put(element.getName(), dfa);

	    Map defMap = new HashMap();
            AttributeDefinition[] defArray = new AttributeDefinition[element.attributes.size()];
            int i = 0;        
            Iterator iter2 = element.attributes.values().iterator();
            while (iter2.hasNext()) {
                DTDAttribute attr = (DTDAttribute) iter2.next();
                AttributeDefinition.Type type;
                String[] values = null;
                if (attr.getType() instanceof DTDEnumeration) {
                    type = AttributeDefinition.Type.ENUMERATION;
                    values = ((DTDEnumeration)attr.getType()).getItems();
                } else if (attr.getType() instanceof DTDNotationList) {
                    type = AttributeDefinition.Type.ENUMERATION;
                    values = ((DTDNotationList)attr.getType()).getItems();
                } else if (attr.getType() instanceof String) {
                    type = AttributeDefinition.Type.get((String) attr.getType());
                } else {
                    throw new RuntimeException("Unrecognized attribute type for element "
                        + element.getName() + " attribute " + attr.getName()
                        + " type " + attr.getType().getClass().getName());
                }
                
                AttributeDefinition ad = new AttributeDefinition(attr.getName(),
                    type,
                    attr.getDefaultValue(),
                    values,
                    attr.getDecl() == DTDDecl.REQUIRED,
                    attr.getDecl() == DTDDecl.FIXED);

                defMap.put(attr.getName(), ad);
                defArray[i] = ad;
                
                i++;
            }
            validator.attributeMaps.put(element.getName(), defMap);
            Arrays.sort(defArray);
            validator.attributeArrays.put(element.getName(), defArray);
	}

	// Calculate anySet

	validator.anySet = new HashSet();
	validator.anySet.addAll(validator.elementDFAs.keySet());
	validator.anySet.add(Validator.PCDATA);

	return validator;
    }

    public AttributeDefinition getAttributeDefinition(String element, String attribute) {
        Map attrMap = (Map) this.attributeMaps.get(element);
        return attrMap == null ? null : (AttributeDefinition) attrMap.get(attribute);
    }

    public AttributeDefinition[] getAttributeDefinitions(String element) {
        if (this.attributeArrays.containsKey(element)) {
            return (AttributeDefinition[]) this.attributeArrays.get(element);
        } else {
            return new AttributeDefinition[0];
        }
    }
    
    public Set getValidRootElements() {
        return this.elementDFAs.keySet();
    }
    
    /** @see Validator#getValidItems */
    public Set getValidItems(String element, String[] prefix, String[] suffix) {

	// First, get a set of candidates. We'll later test to see if each is
	// valid to insert here.

	Set candidates = null;
	DFAState dfa = (DFAState) elementDFAs.get(element);
	if (dfa == null) {
	    // Anything goes!
	    return this.anySet;
	}
	    
        DFAState target = dfa.getState(Arrays.asList(prefix));
        if (target == null) {
            return Collections.EMPTY_SET;
        } else {
            // If the last transition was due to PCDATA, adding more PCDATA
            // is also valid
            if (prefix.length > 0
                && prefix[prefix.length - 1].equals(Validator.PCDATA)) {
                candidates = new HashSet();
                candidates.addAll(target.getValidSymbols());
                candidates.add(Validator.PCDATA);
            } else {
                candidates = target.getValidSymbols();
            }
        }

	// Now, see if each candidate can be inserted at the given
	// offset. This second test is necessary in some simple
	// cases. Consider a <section> with an optional <title>; if
	// we're at the first offset of the <section> and a <title>
	// already exists, we should not allow another <title>.

	Set results = new HashSet();
        String[] middle = new String[1];
	for (Iterator iter = candidates.iterator(); iter.hasNext(); ) {
	    middle[0] = (String) iter.next();
	    if (this.isValidSequence(element, prefix, middle, suffix, true)) {
		results.add(middle[0]);
	    }
	}

	return Collections.unmodifiableSet(results);
    }

    /**
     * @see Validator#isValidSequence
     */
    public boolean isValidSequence(
        String element,
        String[] nodes,
        boolean partial) {
            
        DFAState dfa = (DFAState) this.elementDFAs.get(element);
        if (dfa == null) {
            // Unrecognized element. Give the user the benefit of the doubt.
            return true;
        }
        
        DFAState target = dfa.getState(Arrays.asList(nodes));

        return target != null && (partial || target.isAccepting());
    }
    
    //==================================================== PRIVATE

    /**
     * Homeys must call create()
     */
    private DTDValidator() {
    }

    /**
     * Create a DFABuilder.Node corresponding to the given DTDItem.
     */
    private static DFABuilder.Node createDFANode(DTDItem item) {
	DFABuilder.Node node = null;

	if (item instanceof DTDName) {
	    String name = ((DTDName) item).getValue();
	    node = DFABuilder.createSymbolNode(name);

	} else if (item instanceof DTDPCData) {
	    node = DFABuilder.createSymbolNode(Validator.PCDATA);

	} else if (item instanceof DTDChoice) {
	    Iterator iter = ((DTDContainer)item).getItemsVec().iterator();
	    while (iter.hasNext()) {
		DTDItem child = (DTDItem) iter.next();
		DFABuilder.Node newNode = createDFANode(child);
		if (node == null) {
		    node = newNode;
		} else {
		    node = DFABuilder.createChoiceNode(node, newNode);
		}
	    }

	} else if (item instanceof DTDMixed) {
	    Iterator iter = ((DTDContainer)item).getItemsVec().iterator();
	    while (iter.hasNext()) {
		DTDItem child = (DTDItem) iter.next();
		DFABuilder.Node newNode = createDFANode(child);
		if (node == null) {
		    node = newNode;
		} else {
		    node = DFABuilder.createChoiceNode(node, newNode);
		}
	    }
	    DFABuilder.Node pcdata = 
		DFABuilder.createSymbolNode(Validator.PCDATA);
	    node = DFABuilder.createChoiceNode(node, pcdata);
					       
	} else if (item instanceof DTDSequence) {
	    Iterator iter = ((DTDContainer)item).getItemsVec().iterator();
	    while (iter.hasNext()) {
		DTDItem child = (DTDItem) iter.next();
		DFABuilder.Node newNode = createDFANode(child);
		if (node == null) {
		    node = newNode;
		} else {
		    node = DFABuilder.createSequenceNode(node, newNode);
		}
	    }
	} else {
	    throw new RuntimeException("Unexpected DTDItem subclass: " +
				       item.getClass().getName());
	}

	// Cardinality is moot if it's a null node
	if (node == null) {
	    return node;
	}

	if (item.cardinal == DTDCardinal.OPTIONAL) {
	    node = DFABuilder.createOptionalNode(node);
	} else if (item.cardinal == DTDCardinal.ZEROMANY) {
	    node = DFABuilder.createRepeatingNode(node, 0);
	} else if (item.cardinal == DTDCardinal.ONEMANY) {
	    node = DFABuilder.createRepeatingNode(node, 1);
	} 

	return node;
    }

}


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

import java.util.ArrayList;
import java.util.List;

/**
 * Partial implementation of the Validator interface.
 */
public abstract class AbstractValidator implements Validator {

    
    /**
     * @see Validator#isValidSequence
     */
    public boolean isValidSequence(
        String element,
        String[] seq1,
        String[] seq2,
        String[] seq3,
        boolean partial) {
            
        List list = new ArrayList();
        for (int i = 0; i < seq1.length; i++) {
            list.add(seq1[i]);
        }
        if (seq2 != null) {
            for (int i = 0; i < seq2.length; i++) {
                if (i == 0 && seq2[i].equals(Validator.PCDATA) && list.size() > 0
                    && list.get(list.size() - 1).equals(Validator.PCDATA)) {
                    // Avoid consecutive PCDATA's
                    continue;
                }
                list.add(seq2[i]);
            }
        }
        if (seq3 != null) {
            for (int i = 0; i < seq3.length; i++) {
                if (i == 0 && seq3[i].equals(Validator.PCDATA) && list.size() > 0
                    && list.get(list.size() - 1).equals(Validator.PCDATA)) {
                    // Avoid consecutive PCDATA's
                    continue;
                }
                list.add(seq3[i]);
            }
        }

        String[] nodes = (String[]) list.toArray(new String[list.size()]);
        return this.isValidSequence(element, nodes, partial);    
    }


}


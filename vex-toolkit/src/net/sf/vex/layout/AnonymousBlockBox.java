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
package net.sf.vex.layout;

import java.util.List;

/**
 * A block box that is not associated with a particular element.
 */
public class AnonymousBlockBox extends AbstractBlockBox {

    public AnonymousBlockBox(
            LayoutContext context, 
            BlockBox parent,
            int startOffset,
            int endOffset) {
        
        super(context, parent, startOffset, endOffset);
    }

    protected List createChildren(LayoutContext context) {
        return createBlockBoxes(context, this.getStartOffset(), this.getEndOffset(), this.getWidth(), null, null);
    }


}

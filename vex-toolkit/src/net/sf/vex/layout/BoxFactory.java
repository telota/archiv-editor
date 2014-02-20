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

import java.io.Serializable;

import net.sf.vex.dom.Element;


/**
 * Interface to an object that creates boxes from elements. Implementations
 * of this interface must be serializable.
 */
public interface BoxFactory extends Serializable {

    /**
     * Creates a box given an element. 
     * @param context CSS styles for the new element
     * @param element Element for which the box should be created.
     * @param parent Parent box for the new box.
     * @param containerWidth Width of the box to be created.
     */
    public Box createBox(LayoutContext context, Element element, BlockBox parent, int containerWidth);

}


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
package net.sf.vex.widget;

import net.sf.vex.layout.Box;

/**
 * Interface implemented by classes that determine whether a Box
 * matches certain criteria. 
 * 
 * @see IVexWidget#
 */
public interface IBoxFilter {

    /**
     * Returns <code>true</code> if the given box matches the criteria.
     * 
     * @param box Box to be tested.
     */
    public boolean matches(Box box);
}

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
package net.sf.vex.editor.config;

/**
 * Interface through which a resource parser notifies an interested party
 * of build problems.
 */
public interface IBuildProblemHandler {

    /**
     * Called by the parser when a problem is found while parsing a resource.
     * @param problem Details of the problem.
     */
    public void foundProblem(BuildProblem problem);
}

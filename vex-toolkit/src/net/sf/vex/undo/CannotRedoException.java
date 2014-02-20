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
package net.sf.vex.undo;

/**
 * Thrown when an IUndoableEdit cannot be undone.
 */
public class CannotRedoException extends RuntimeException {

    /**
     * Class constructor. 
     */
    public CannotRedoException() {
    }

    /**
     * Class constructor.
     * @param message Message indicating the reason for the failure.
     */
    public CannotRedoException(String message) {
        super(message);
    }

    /**
     * Class constructor.
     * @param cause Root cause of the failure.
     */
    public CannotRedoException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor.
     * @param message Message indicating the reason for the failure.
     * @param cause Root cause of the failure.
     */
    public CannotRedoException(String message, Throwable cause) {
        super(message, cause);
    }

}

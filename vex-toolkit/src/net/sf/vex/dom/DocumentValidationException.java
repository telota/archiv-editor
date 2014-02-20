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


/**
 * Exception thrown when an change would have compromised document validity.
 */
public class DocumentValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor.
     *
     * @param message Message indicating the nature of the exception.
     */
    public DocumentValidationException(String message) {
        super(message);
    }
}


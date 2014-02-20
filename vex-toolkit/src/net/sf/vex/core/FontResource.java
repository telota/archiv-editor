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
package net.sf.vex.core;

/**
 * Wrapper for a toolkit-defined font. Fonts are system-defined resources.
 * They must be retrieved from the Graphics.createFont method, and must be
 * disposed when no longer needed.
 */
public interface FontResource {

    public void dispose();
}

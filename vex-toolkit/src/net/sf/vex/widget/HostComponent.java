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

import net.sf.vex.core.Graphics;
import net.sf.vex.core.Rectangle;

/**
 * Callback interface through which VexComponentImpl accesses its host
 * component.
 */
public interface HostComponent {

    /**
     * Creates a Graphics object for the default system display. The returned
     * object must be disposed after use.
     */
    public Graphics createDefaultGraphics();
    
    /**
     * If the component is scrollable, return the height of the viewport;
     * otherwise, return the size of the widget. 
     */
    public Rectangle getViewport();
    
    /**
     * Called when the selection in the widget has changed. This method
     * should reset the caret timer to the full interval.
     */
    public void fireSelectionChanged();
    
    public void invokeLater(Runnable runnable);
    
    /**
     * Flag the entire component for a repaint.
     */
    public void repaint();

    
    /**
     * Flag a rectangular area of the component to be repainted.
     * @param x X-coordinate of the region to be repainted.
     * @param y Y-coordinate of the region to be repainted.
     * @param width Width of the region to be repainted.
     * @param height Height of the region to be repainted.
     */
    public void repaint(int x, int y, int width, int height);
    
    /**
     * Move the viewport to a new location
     * @param left New left-side of the viewport
     * @param top New top-side of the viewport
     */
    public void scrollTo(int left, int top);
    
    /**
     * Sets the preferred size of the component.
     * @param width Preferred width of the component.
     * @param height Preferred height of the component.
     */
    public void setPreferredSize(int width, int height);

	boolean isDisposed();
}

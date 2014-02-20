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
 * Represents a device that can display graphics. This class is subclassed
 * for each target system.
 */
public abstract class DisplayDevice {

    /**
     * Class constructor. 
     */
    public DisplayDevice() {
    }

    /**
     * Returns the current display device.
     */
    public static DisplayDevice getCurrent() {
        return current;
    }
    /**
     * Returns the horizontal resolution of the device, in pixels-per-inch.
     */
    public abstract int getHorizontalPPI();
    

    /**
     * Returns the horizontal resolution of the device, in pixels-per-inch.
     */
    public abstract int getVerticalPPI();

    
    /**
     * Sets the current display device. This is typically called by the 
     * platform-specific widget; 
     * @param current The device to use as the current device.
     */
    public static void setCurrent(DisplayDevice current) {
        DisplayDevice.current = current;
    }
    
    //======================================================= PRIVATE
    
    private static DisplayDevice current;
}

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A collection of listener objects. The main point of this class is the 
 * fireEvent method, which takes care of the
 * tedium of iterating over the collection and catching exceptions generated
 * by listeners.
 */
public class ListenerList {

    /**
     * Class constructor.
     * @param listenerClass Class of the listener interface.
     * @param eventClass Class of the event objects passed to methods in the
     * listener interface.
     */
    public ListenerList(Class listenerClass, Class eventClass) {
        this.listenerClass = listenerClass;
        this.methodParams = new Class[] { eventClass };
    }
    
    /**
     * Adds a listener to the list. Rejects listeners that are not subclasses
     * of the listener class passed to the constructor.
     * @param listener Listener to be added.
     */
    public void add(Object listener) {
        if (!listenerClass.isInstance(listener)) {
            this.handleException(new IllegalArgumentException("" + listener + " is not an instance of " + listenerClass));
        }
        this.listeners.add(listener);
    }
    
    /**
     * Calls the given method on each registered listener. Any exception
     * thrown from one of the called methods is passed to handleException, as
     * is any introspection error, e.g. if the given method doesn't exist.
     * 
     * @param methodName Listener method to call.
     * @param event Event to be passed to each call.
     */
    public void fireEvent(String methodName, EventObject event) {
        
        Method method = (Method) this.methods.get(methodName);
        if (method == null) {
            try {
                method = listenerClass.getMethod(methodName, methodParams);
                this.methods.put(methodName, method);
            } catch (Exception e) {
                this.handleException(e);
                return;
            }
        }
        
        Object[] args = new Object[] { event };
        for (Iterator it = this.listeners.iterator(); it.hasNext();) {
            Object listener = it.next();
            try {
                method.invoke(listener, args);
            } catch (Exception ex) {
                this.handleException(ex);
            }
        }
        
    }
    
    /**
     * Called from fireEvent whenever a called listener method throws an 
     * exception, or if there is a problem looking up the listener method
     * by reflection. By default, simply prints the stack trace to stdout.
     * Clients may override this method to provide a more suitable 
     * implementation. 
     * @param ex Exception thrown by the listener method.
     */
    public void handleException(Exception ex) {
        ex.printStackTrace();
    }
    
    /**
     * Removes a listener from the list.
     * @param listener Listener to remove.
     */
    public void remove(Object listener) {
        this.listeners.remove(listener);
    }
    
    //====================================================== PRIVATE
    
    private Class listenerClass;
    private Class[] methodParams;
    private Collection listeners = new ArrayList();
    
    // map methodName => Method object
    private Map methods = new HashMap();
}

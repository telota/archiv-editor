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
package net.sf.vex.swing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the {@link SelectionProvider} interface. Also acts as
 * a selection event multiplexor: any events received on its 
 * {@link SelectionListener} interface are relayed to any registered listeners. 
 */
public class SelectionProviderImpl 
    implements SelectionProvider, SelectionListener {

    private List listeners = new ArrayList();
    
    /**
     * @see net.sf.vex.core.SelectionProvider#addSelectionChangeListener(net.sf.vex.SelectionListener)
     */
    public void addSelectionListener(SelectionListener listener) {
        this.listeners.add(listener);

    }

    /**
     * Call <code>selectionChanged</code> on all registered listeners.
     * @param selection Selection that has changed.
     */
    public void fireSelectionChanged(Selection selection) {
        for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
            SelectionListener listener = (SelectionListener) iter.next();
            //long start = System.currentTimeMillis();
            listener.selectionChanged(selection);
            //long end = System.currentTimeMillis();
            //System.out.println("" + (end-start) + ": " + listener);
        }
    }
    
    /**
     * @see net.sf.vex.core.SelectionProvider#removeSelectionChangeListener(net.sf.vex.SelectionListener)
     */
    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * @see net.sf.vex.core.SelectionListener#selectionChanged(net.sf.vex.Selection)
     */
    public void selectionChanged(Selection selection) {
        this.fireSelectionChanged(selection);
    }

}

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
package net.sf.vex.editor;

import net.sf.vex.core.ListenerList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * Implementation of ISelectionProvider. This class is also an
 * ISelectionChangedListener; any events received by selectionChanged are
 * relayed to registered listeners.
 */
public class SelectionProvider implements ISelectionProvider,
        ISelectionChangedListener {

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Fire a SelectionChangedEvent to all registered listeners.
     * @param e Event to be passed to the listeners' selectionChanged method.
     */
    public void fireSelectionChanged(SelectionChangedEvent e) {
        this.selection = e.getSelection();
        this.listeners.fireEvent("selectionChanged", e); //$NON-NLS-1$
    }
    
    public ISelection getSelection() {
        return this.selection;
    }

    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        this.listeners.remove(listener);
    }

    public void setSelection(ISelection selection) {
        this.selection = selection;
    }

    public void selectionChanged(SelectionChangedEvent event) {
        this.fireSelectionChanged(event);
    }

    //===================================================== PRIVATE

    ISelection selection;
    private ListenerList listeners = new ListenerList(ISelectionChangedListener.class, SelectionChangedEvent.class);
}

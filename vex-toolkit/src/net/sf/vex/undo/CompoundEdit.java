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

import java.util.ArrayList;
import java.util.List;

/**
 * An undoable edit that is a composite of others.
 */
public class CompoundEdit implements IUndoableEdit {

    /**
     * Class constructor.
     */
    public CompoundEdit() {
    }

    /**
     * Adds an edit to the list.
     * @param edit Edit to be undone/redone as part of the compound group.
     */
    public void addEdit(IUndoableEdit edit) {
        edits.add(edit);
    }
    
    public boolean combine(IUndoableEdit edit) {
        return false;
    }

    /**
     * Calls redo() on each contained edit, in the order that they were added.
     */
    public void redo() {
        for (int i = 0; i < this.edits.size(); i++) {
            IUndoableEdit edit = (IUndoableEdit) this.edits.get(i);
            edit.redo();
        }
    }

    /**
     * Calls undo() on each contained edit, in reverse order from which they
     * were added.
     */
    public void undo() {
        for (int i = this.edits.size() - 1; i >= 0; i--) {
            IUndoableEdit edit = (IUndoableEdit) this.edits.get(i);
            edit.undo();
        }
    }

    //===================================================== PRIVATE
    
    private List edits = new ArrayList();
}

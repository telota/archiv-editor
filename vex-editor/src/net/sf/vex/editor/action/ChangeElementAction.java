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
package net.sf.vex.editor.action;

import net.sf.vex.action.IVexAction;
import net.sf.vex.editor.MorphAssistant;
import net.sf.vex.swt.VexWidget;
import net.sf.vex.widget.IVexWidget;

/**
 * Displays the Change Element dialog.
 */
public class ChangeElementAction implements IVexAction {

    public void run(IVexWidget vexWidget) {
        new MorphAssistant().show((VexWidget) vexWidget);
    }

    public boolean isEnabled(IVexWidget vexWidget) {
        return true;
    }

}

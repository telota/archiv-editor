/**
 * This file is part of Archiv-Editor.
 * 
 * The software Archiv-Editor serves as a client user interface for working with
 * the Person Data Repository. See: pdr.bbaw.de
 * 
 * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
 * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
 * www.bbaw.de
 * 
 * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
 * of Sciences and Humanities
 * 
 * The software Archiv-Editor was developed by @author: Christoph Plutte.
 * 
 * Archiv-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Archiv-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Archiv-Editor.  
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package org.bbaw.pdr.ae.view.control.interfaces;

import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public interface IEasyAspectEditor extends IAEBasicEditor
{
	public void addSelectionChangedListener(ISelectionChangedListener listener);

	public void addSelectionListener(final SelectionListener selectionListener);

	public Aspect getAspect();

	public ISelection getSelection();

	public void removeSelectionChangedListener(ISelectionChangedListener listener);

	public void setBackground(Color color);

	public void setForeground(Color color);

	public void setSelection(ISelection selection);

	public void addCustomPaintListener(PaintListener _paintListener);

	public void removeCustomPaintListener(PaintListener _paintListener);

	public boolean setFocus();

	public void setLayoutData(Object layoutData);

	Point getSize();

	PdrObject getOwningObject();

	void layout();

	public void resize();


}

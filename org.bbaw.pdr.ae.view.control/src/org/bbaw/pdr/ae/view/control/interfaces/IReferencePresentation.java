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

import org.bbaw.pdr.ae.model.ReferenceMods;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

/**
 * The Interface IReferencePresentation.
 * @author Christoph Plutte
 */
public interface IReferencePresentation
{

	/**
	 * Adds the double click listener.
	 * @param listener the listener
	 */
	void addDoubleClickListener(Listener listener);

	/**
	 * Adds the selection listener.
	 * @param listener the listener
	 */
	void addSelectionListener(Listener listener);

	/**
	 * Creates the presentation.
	 */
	void createPresentation();

	/** get the Control of this ReferencePresentation for setting context menu.
	 * @return upper control of this ReferencePresentation.
	 */
	Control getControl();

	/**
	 * Gets the reference.
	 * @return the reference
	 */
	ReferenceMods getReference();

	/**
	 * Sets the composite.
	 * @param textComposite the new composite
	 */
	void setComposite(Composite textComposite);

	/**
	 * Sets the grayed out.
	 * @param grayedout the new grayed out
	 */
	void setGrayedOut(boolean grayedout);

	/**
	 * Sets the reference.
	 * @param reference the new reference
	 */
	void setReference(ReferenceMods reference);

	/**
	 * Sets the selected.
	 * @param seleted the new selected
	 */
	void setSelected(boolean seleted);
}

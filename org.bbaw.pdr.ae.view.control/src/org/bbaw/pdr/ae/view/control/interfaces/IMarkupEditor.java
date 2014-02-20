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
import org.bbaw.pdr.ae.model.TaggingRange;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

/**
 * The Interface IMarkupEditor.
 * @author Christoph Plutte
 */
public interface IMarkupEditor
{

	/**
	 * Adds the extended modify listener.
	 * @param listener the listener
	 */
	void addExtendedModifyListener(Listener listener);

	/**
	 * Adds the focus listener.
	 * @param listener the listener
	 */
	void addFocusListener(Listener listener);

	/**
	 * Adds the key listener.
	 * @param listener the listener
	 */
	void addKeyListener(Listener listener);

	/**
	 * Adds the markup selection listener.
	 * @param listener the listener
	 */
	void addMarkupSelectionListener(Listener listener);

	/**
	 * Adds the text selection listener.
	 * @param listener the listener
	 */
	void addTextSelectionListener(Listener listener);

	/**
	 * Creates the editor.
	 */
	void createEditor();

	/**
	 * Delete markup.
	 * @param taggingRange the tagging range
	 */
	void deleteMarkup(TaggingRange[] taggingRanges);

	/**
	 * Gets the aspect.
	 * @return the aspect
	 */
	Aspect getAspect();

	/**
	 * Gets the control.
	 * @return the control
	 */
	Control getControl();

	/**
	 * Gets the selected markup.
	 * @return the selected markup
	 */
	TaggingRange[] getSelectedMarkups();

	/**
	 * Gets the title.
	 * @return the title
	 */
	String getTitle();

	/**
	 * Insert content set markup.
	 * @param taggingRange the tagging range
	 */
	void insertContentSetMarkup(TaggingRange taggingRange);


	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	boolean isValid();

	/**
	 * Sets the aspect.
	 * @param currentAspect the new aspect
	 */
	void setAspect(Aspect currentAspect);

	/**
	 * Sets the composite.
	 * @param frontComposite the new composite
	 */
	void setComposite(Composite frontComposite);

	/**
	 * Sets the editable.
	 * @param editable the new editable
	 */
	void setEditable(boolean editable);

	/**
	 * Sets the font.
	 * @param fontDescriptor the new font
	 */
	void setFont(FontDescriptor fontDescriptor);

	/**
	 * Sets the markup.
	 * @param taggingRange the new markup
	 */
	void setMarkup(TaggingRange taggingRange);

	/**
	 * Sets the title.
	 * @param string the new title
	 */
	void setTitle(String string);
	

	void refresh();
	
	void saveChanges();

	void insert(String string);

	void setSelected(boolean selected);

	void setFocus();

	String getSelectionText();

}

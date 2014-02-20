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
package org.bbaw.pdr.ae.model;

import java.util.HashMap;

import org.bbaw.pdr.ae.metamodel.IAEPresentable;

/**
 * The Class ReferenceModsTemplate.
 * @author Christoph Plutte
 */
public class ReferenceModsTemplate implements IAEPresentable
{

	/** The label. */
	private String _label;

	/** The value. */
	private String _value;

	/** The ignore. */
	private boolean _ignore;

	/** The ref template. */
	private ReferenceMods _refTemplate;

	/** The image string. */
	private String _imageString;

	/** The priority. */
	private int _priority;

	/** The documentation. */
	private HashMap<String, String> _documentation = new HashMap<String, String>();

	/**
	 * @param o aepresentable
	 * @return comparision of both labels
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final IAEPresentable o)
	{
		return this.getLabel().compareToIgnoreCase(o.getLabel());
	}

	@Override
	public final String getContent()
	{
		return this._label;
	}

	@Override
	public final int getCursorPosition()
	{
		return 0;
	}

	@Override
	public final String getDescription()
	{
		return null;
	}

	/**
	 * Gets the documentation.
	 * @return the documentation
	 */
	public final HashMap<String, String> getDocumentation()
	{
		return _documentation;
	}

	@Override
	public final String getImageString()
	{
		return _imageString;
	}

	@Override
	public final String getLabel()
	{
		return _label;
	}

	@Override
	public final int getPriority()
	{
		return _priority;
	}

	/**
	 * Gets the ref template.
	 * @return the ref template
	 */
	public final ReferenceMods getRefTemplate()
	{
		return _refTemplate;
	}

	@Override
	public final String getValue()
	{
		return _value;
	}

	/**
	 * Checks if is ignore.
	 * @return true, if is ignore
	 */
	public final boolean isIgnore()
	{
		return _ignore;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		boolean valid = true;
		if (this._value == null)
		{
			valid = false;
		}
		if (this._label == null)
		{
			valid = false;
		}
		return valid;
	}

	/**
	 * Sets the documentation.
	 * @param documentation the documentation
	 */
	public final void setDocumentation(final HashMap<String, String> documentation)
	{
		this._documentation = documentation;
	}

	/**
	 * Sets the ignore.
	 * @param ignore the new ignore
	 */
	public final void setIgnore(final boolean ignore)
	{
		this._ignore = ignore;
	}

	/**
	 * Sets the image string.
	 * @param imageString the new image string
	 */
	public final void setImageString(final String imageString)
	{
		this._imageString = imageString;
	}

	/**
	 * Sets the label.
	 * @param label the new label
	 */
	public final void setLabel(final String label)
	{
		this._label = label;
	}

	/**
	 * Sets the priority.
	 * @param priority the new priority
	 */
	public final void setPriority(final int priority)
	{
		this._priority = priority;
	}

	/**
	 * Sets the ref template.
	 * @param refTemplate the new ref template
	 */
	public final void setRefTemplate(final ReferenceMods refTemplate)
	{
		this._refTemplate = refTemplate;
	}

	@Override
	public final void setValue(final String value)
	{
		this._value = value;
	}

}

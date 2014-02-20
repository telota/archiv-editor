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
package org.bbaw.pdr.ae.model.view;

import org.bbaw.pdr.ae.model.PdrObject;

/**
 * The Class PDRObjectsConflict.
 * @author Christoph Plutte
 */
public class PDRObjectsConflict
{

	/** The local object. */
	private PdrObject _localObject;

	/** The repository object. */
	private PdrObject _repositoryObject;

	/** The keep local. */
	private boolean _keepLocal = false;

	/** The override local. */
	private boolean _overrideLocal = false;

	/**
	 * Gets the local object.
	 * @return the local object
	 */
	public final PdrObject getLocalObject()
	{
		return _localObject;
	}

	/**
	 * Gets the repository object.
	 * @return the repository object
	 */
	public final PdrObject getRepositoryObject()
	{
		return _repositoryObject;
	}

	/**
	 * Checks if is keep local.
	 * @return true, if is keep local
	 */
	public final boolean isKeepLocal()
	{
		return _keepLocal;
	}

	/**
	 * Checks if is override local.
	 * @return true, if is override local
	 */
	public final boolean isOverrideLocal()
	{
		return _overrideLocal;
	}

	/**
	 * Sets the keep local.
	 * @param keepLocal the new keep local
	 */
	public final void setKeepLocal(final boolean keepLocal)
	{
		this._keepLocal = keepLocal;
	}

	/**
	 * Sets the local object.
	 * @param localObject the new local object
	 */
	public final void setLocalObject(final PdrObject localObject)
	{
		this._localObject = localObject;
	}

	/**
	 * Sets the override local.
	 * @param overrideLocal the new override local
	 */
	public final void setOverrideLocal(final boolean overrideLocal)
	{
		this._overrideLocal = overrideLocal;
	}

	/**
	 * Sets the repository object.
	 * @param repositoryObject the new repository object
	 */
	public final void setRepositoryObject(final PdrObject repositoryObject)
	{
		this._repositoryObject = repositoryObject;
	}
}

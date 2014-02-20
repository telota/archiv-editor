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
package org.bbaw.pdr.ae.control.datahandling.xqj.internal;

import org.bbaw.pdr.ae.metamodel.PdrId;

public class IDSingleton
{

	/** Singleton for standalone RCP. */
	private static volatile IDSingleton singletonIDSingletonInstance;

	/**
	 * Gets the instanz.
	 * @return the instanz
	 */
	public static IDSingleton getInstance()
	{
		if (singletonIDSingletonInstance == null)
		{
			synchronized (IDSingleton.class)
			{
				if (singletonIDSingletonInstance == null)
				{
					singletonIDSingletonInstance = new IDSingleton();
				}
			}
		}
		return singletonIDSingletonInstance;
	}

	public PdrId getLastPersonID()
	{
		return lastPersonID;
	}

	public void setLastPersonID(PdrId lastPersonID)
	{
		this.lastPersonID = lastPersonID;
	}

	public PdrId getLastAspectId()
	{
		return lastAspectId;
	}

	public void setLastAspectId(PdrId lastAspectId)
	{
		this.lastAspectId = lastAspectId;
	}

	public PdrId getLastReferenceID()
	{
		return lastReferenceID;
	}

	public void setLastReferenceID(PdrId lastReferenceID)
	{
		this.lastReferenceID = lastReferenceID;
	}

	public PdrId getLastUserID()
	{
		return lastUserID;
	}

	public void setLastUserID(PdrId lastUserID)
	{
		this.lastUserID = lastUserID;
	}

	private PdrId lastPersonID;

	private PdrId lastAspectId;

	private PdrId lastReferenceID;

	private PdrId lastUserID;


}

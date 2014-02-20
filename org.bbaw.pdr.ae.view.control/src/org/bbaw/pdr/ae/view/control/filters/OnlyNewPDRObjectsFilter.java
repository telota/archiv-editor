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
package org.bbaw.pdr.ae.view.control.filters;

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;

/**
 * The Class OnlyNewPDRObjectsFilter.
 * @author Christoph Plutte
 */
public class OnlyNewPDRObjectsFilter implements AEFilter
{

	@Override
	public final boolean select(final Object element)
	{
		PdrObject obj = (PdrObject) element;
		if (obj instanceof Aspect)
		{
			Integer state;
			try
			{
				state = Facade.getInstanz().getAspectsUpdateState().get(obj.getPdrId().toString());
			}
			catch (Exception e)
			{
				return false;
			}
			if (state != null)
			{
				return state == 2;
			}
			else
			{
				return false;
			}

		}
		if (obj instanceof Person)
		{
			Integer state;

			try
			{
				state = Facade.getInstanz().getPersonsUpdateState().get(obj.getPdrId().toString());
			}
			catch (Exception e)
			{
				return false;
			}
			if (state != null)
			{
				return state == 2;
			}
			else
			{
				return false;
			}
		}
		if (obj instanceof ReferenceMods)
		{
			Integer state;

			try
			{
				state = Facade.getInstanz().getReferencesUpdateState().get(obj.getPdrId().toString());
			}
			catch (Exception e)
			{
				return false;
			}
			if (state != null)
			{
				return state == 2;
			}
			else
			{
				return false;
			}

		}
		return false;
	}

}

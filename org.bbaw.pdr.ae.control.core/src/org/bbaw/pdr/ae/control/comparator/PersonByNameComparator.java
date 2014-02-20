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
package org.bbaw.pdr.ae.control.comparator;

import java.util.Comparator;

import org.bbaw.pdr.ae.model.Person;

/**
 * Comparator for Person objects according to names (case insensitive). First,
 * it compares the SurNames given in the BasicPersonData, secondly, if SurNames
 * are equal it compares ForeNames.
 * @author Christoph Plutte
 */
public class PersonByNameComparator implements Comparator<Person>
{

	@Override
	public final int compare(final Person p1, final Person p2)
	{
		int c = 0;
		if (!p1.getDisplayName().startsWith("pdrPo") && !p2.getDisplayName().startsWith("pdrPo"))
		{
			c = p1.getDisplayName().compareToIgnoreCase(p2.getDisplayName());
		}
		else if (p1.getDisplayName().startsWith("pdrPo") && !p2.getDisplayName().startsWith("pdrPo"))
		{
			// System.out.println("p1 hat keinen namen");
			c = 1;
		}
		else if (!p1.getDisplayName().startsWith("pdrPo") && p2.getDisplayName().startsWith("pdrPo"))
		{
			// System.out.println("p2 hat keinen namen");
			c = -1;
		}
		else
		{

			c = p1.getPdrId().getId() - p2.getPdrId().getId();
			// System.out.println("weder p1 noch p2 haben einen namen c " + c);
		}
		// System.out.println("comparator " + c + " p1 " + p1.getDisplayName() +
		// " p2 " + p2.getDisplayName());
		return c;
	}

}

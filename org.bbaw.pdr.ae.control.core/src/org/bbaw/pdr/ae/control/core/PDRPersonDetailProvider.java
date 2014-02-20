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
package org.bbaw.pdr.ae.control.core;

import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;

public class PDRPersonDetailProvider
{

	private Facade _facade = Facade.getInstanz();
	/** The _main searcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();

	public String getMarkupedText(PdrId personId, String semantic, String element, String type, String subtype,
			String role)
	{
		Person p = _facade.getPerson(personId);
		if (p != null)
		{
			if (!p.isAspectsLoaded())
			{
				try
				{
					_mainSearcher.searchAspectsByRelatedObject(p, null);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (p.getAspectIds() != null && !p.getAspectIds().isEmpty())
			{
				Aspect a;
				boolean semanticFound = false;
				for (PdrId id : p.getAspectIds())
				{
					a = _facade.getAspect(id);
					if (a != null && a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
					{
						for (SemanticStm sStm : a.getSemanticDim().getSemanticStms())
						{
							if (sStm.getLabel() != null && sStm.getLabel().equals(semantic))
							{
								semanticFound = true;
								break;
							}
						}
					}
					if (semanticFound)
					{
						if (a.getRangeList() != null)
						{
							for (TaggingRange tr : a.getRangeList())
							{
								if (element != null && tr.getName() != null && tr.getName().equals(element))
								{
									if (type != null && tr.getType() != null && tr.getType().equals(type))
									{
										if (subtype != null && tr.getSubtype() != null
												&& tr.getSubtype().equals(subtype))
										{
											if (role != null && tr.getRole() != null && tr.getRole().equals(role))
											{
												return tr.getTextValue();
											}
											return tr.getTextValue();
										}
										return tr.getTextValue();
									}
									return tr.getTextValue();
								}
							}
						}
					}
				}
			}
		}

		return null;
	}
}

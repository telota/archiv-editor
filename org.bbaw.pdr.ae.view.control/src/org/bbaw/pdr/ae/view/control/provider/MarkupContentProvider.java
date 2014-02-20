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
package org.bbaw.pdr.ae.view.control.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.bbaw.pdr.ae.config.core.ConfigDataComparator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class MarkupContentProvider.
 * @author Christoph Plutte
 */
public class MarkupContentProvider implements IStructuredContentProvider
{

	/** The add all. */
	private boolean _addAll = false;

	/**
	 * Instantiates a new markup content provider.
	 */
	public MarkupContentProvider()
	{
		this._addAll = false;
	}

	/**
	 * Instantiates a new markup content provider.
	 * @param addAll the add all
	 */
	public MarkupContentProvider(final boolean addAll)
	{
		this._addAll = addAll;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		if (inputElement != null && inputElement instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> children = (HashMap<String, ConfigData>) inputElement;
			for (String k : children.keySet())
			{
				if (children.get(k) instanceof DataType)
				{
					if (_addAll)
					{
						DataType[] datatypes = new DataType[children.size() - 1];
						DataType dtAll = new DataType();
						dtAll.setValue("ALL"); //$NON-NLS-1$
						dtAll.setLabel("ALL"); //$NON-NLS-1$
						datatypes[0] = dtAll;
						int i = 1;
						for (String key : children.keySet())
						{
							DataType dt = (DataType) children.get(key);
							if (!(dt.getValue().equals("aodl:relation") || dt.getValue().equals("aodl:semanticStm")))
							{
								datatypes[i] = dt;
								i++;
							}

						}

						return datatypes;
					}
					else
					{
						DataType[] datatypes = new DataType[children.size() - 2];
						int i = 0;
						for (String key : children.keySet())
						{
							DataType dt = (DataType) children.get(key);
							if (!(dt.getValue().equals("aodl:relation") || dt.getValue().equals("aodl:semanticStm")))
							{
								datatypes[i] = dt;
								i++;
							}

						}
						return datatypes;
					}
				}
				else if (children.get(k) instanceof ConfigData)
				{
					ArrayList<ConfigData> configItems = new ArrayList<ConfigData>();
					boolean samePosition = true;
					String lastPosition = null;
					for (String key : children.keySet())
					{
						ConfigData ci = (ConfigData) children.get(key);
						if (!ci.isIgnore())
						{
							configItems.add(ci);
						}
						else if ((ci instanceof ConfigItem) && ((ConfigItem) ci).isReadAlthoughIgnored())
						{
							((ConfigItem) ci).setReadAlthoughIgnored(false);
							configItems.add(ci);
						}
						if (lastPosition != null && ci.getPos() != null && !ci.getPos().equals(lastPosition))
						{
							samePosition = false;
						}
						lastPosition = ci.getPos();
					}
					if (samePosition)
					{
						ConfigData[] itms = configItems.toArray(new ConfigData[configItems.size()]);
						Arrays.sort(itms);
						return itms;
					}
					else
					{
						Collections.sort(configItems, new ConfigDataComparator(false));
						ConfigData[] itms = configItems.toArray(new ConfigData[configItems.size()]);
						return itms;
					}

				}
				break;
			}
		}
		// System.out.println("input null");
		DataType[] datatypes = new DataType[1];
		datatypes[0] = new DataType();
		datatypes[0].setValue("");
		datatypes[0].setLabel("");
		return datatypes;
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{
		// TODO Auto-generated method stub

	}

}

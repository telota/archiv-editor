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

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * The Class ListLabelProvider.
 * @author Christoph Plutte
 */
public class ListLabelProvider implements ILabelProvider
{

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	@Override
	public void addListener(final ILabelProviderListener listener)
	{

	}

	@Override
	public void dispose()
	{

	}

	@Override
	public final Image getImage(final Object element)
	{

		if (element instanceof PdrObject)
		{
			PdrObject o = (PdrObject) element;
			if (o instanceof Aspect)
			{
				return _imageReg.get(IconsInternal.ASPECT);
			}
			else if (o instanceof Person)
			{
				return _imageReg.get(IconsInternal.PERSON);
			}
			else if (o instanceof ReferenceMods)
			{
				return _imageReg.get(IconsInternal.REFERENCE);
			}
		}
		return null;
	}

	@Override
	public final String getText(final Object element)
	{

		if (element instanceof PdrObject)
		{
			return ((PdrObject) element).getDisplayNameWithID();
		}
		return null;
	}

	@Override
	public final boolean isLabelProperty(final Object element, final String property)
	{
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener)
	{

	}

}

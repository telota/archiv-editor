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
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * The Class AEConfigPresentableLabelProvider.
 * @author Christoph Plutte
 */
public class AEConfigPresentableLabelProvider implements ILabelProvider
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
		if (element instanceof ConfigData)
		{
			ConfigData cd = (ConfigData) element;
			if (cd.getValue().equals("aodl:relation"))
			{
				return _imageReg.get(IconsInternal.RELATION);
			}
			else if (cd.getValue().equals("aodl:semanticStm"))
			{
				return _imageReg.get(IconsInternal.CLASSIFICATION);
			}
			else
			{
				return _imageReg.get(IconsInternal.MARKUP);
			}
		}
		return null;
	}

	@Override
	public final String getText(final Object element)
	{
		if (element instanceof IAEPresentable)
		{
			IAEPresentable cp = (IAEPresentable) element;
			if (cp.getLabel() != null && cp.getLabel().trim().length() > 0)
			{
				return cp.getLabel();
			}
			else
			{
				return cp.getValue();
			}
		}
		return "ERROR";
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

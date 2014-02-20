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
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * The Class RefTemplateLabelProvider.
 * @author Christoph Plutte
 */
public class RefTemplateLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider
{

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	@Override
	public final Color getBackground(final Object element, final int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Image getColumnImage(final Object element, final int columnIndex)
	{
		if (columnIndex == 0)
		{
			ReferenceModsTemplate template = (ReferenceModsTemplate) element;
			if (template.getImageString() != null)
			{
				return _imageReg.get(template.getImageString());
			}
			return _imageReg.get(IconsInternal.REFERENCE);
		}
		else
		{
			return null;
		}
	}

	@Override
	public final String getColumnText(final Object element, final int columnIndex)
	{
		ReferenceModsTemplate template = (ReferenceModsTemplate) element;
		switch (columnIndex)
		{
			case 0:
				return template.getLabel();

			default:
				throw new RuntimeException("Error");
		}

	}

	@Override
	public final Color getForeground(final Object element, final int columnIndex)
	{
		ReferenceModsTemplate template = (ReferenceModsTemplate) element;
		if (template.isIgnore())
		{
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}
		else
		{
			return null;
		}
	}

}

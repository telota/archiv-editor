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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * The Class TableLabelProvider.
 * @author Christoph Plutte
 */
public class TableLabelProvider extends LabelProvider implements ITableLabelProvider
{

	/** The _provider. */
	private String _provider = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
			"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null);
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	@Override
	public final Image getColumnImage(final Object element, final int columnIndex)
	{
		if (columnIndex == 0)
		{
			if (element instanceof Person)
			{
				return _imageReg.get(IconsInternal.PERSON);
			}
			else if (element instanceof Aspect)
			{
				return _imageReg.get(IconsInternal.ASPECT);
			}
			else if (element instanceof ReferenceMods)
			{
				return _imageReg.get(IconsInternal.REFERENCE);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public final String getColumnText(final Object element, final int columnIndex)
	{
		PdrObject pdrobj = (PdrObject) element;

		if (pdrobj instanceof Aspect)
		{
			Aspect a = (Aspect) pdrobj;
			switch (columnIndex)
			{
				case 0:
					return a.getDisplayName();
				case 1:
					if (a.getSemanticDim() != null
							&& !a.getSemanticDim().getSemanticLabelByProvider(_provider).isEmpty())
					{
						String semantic = a.getSemanticDim().getSemanticLabelByProvider(_provider).firstElement();
						return PDRConfigProvider.getSemanticLabel(_provider, semantic);
					}
					else
					{
						return "????";
					}
				case 2:
					return a.getPdrId().toString();
				default:
					throw new RuntimeException("Error");
			}
		}
		else if (pdrobj instanceof ReferenceMods)
		{
			ReferenceMods r = (ReferenceMods) pdrobj;
			switch (columnIndex)
			{
				case 0:
					return r.getDisplayName();
				case 1:
					if (r.getTitleInfo() != null && r.getTitleInfo().getTitle() != null)
					{
						return r.getTitleInfo().getTitle();
					}
					else
					{
						return "";
					}
				case 2:
					return r.getPdrId().toString();

				default:
					throw new RuntimeException("Error");
			}
		}
		else
		{
			switch (columnIndex)
			{
				case 0:
					return pdrobj.getDisplayName();
				case 1:
					return pdrobj.getPdrId().toString();

				default:
					throw new RuntimeException("Error");
			}
		}

	}

}

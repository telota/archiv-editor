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
package org.bbaw.pdr.ae.config.editor.view;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.icons.PdrObjectDecorator;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * label provider for config tree in config editor.
 * @author Christoph Plutte
 */
public class ConfigTreeLabelProvider extends LabelProvider implements ITableLabelProvider, ITableFontProvider,
		ITableColorProvider
{
	/** instance of config facade. */
	private IConfigFacade _configFacade = new ConfigFactory().getConfigFacade();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** font registry. */
	private FontRegistry _registry = new FontRegistry();
	/** decorator. */
	private PdrObjectDecorator _decorator = new PdrObjectDecorator();

	@Override
	public void addListener(final ILabelProviderListener listener)
	{
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public final Color getBackground(final Object element, final int columnIndex)
	{

		// ConfigData cd = ((TreeNode) element).getConfigData();
		// if (cd instanceof ConfigItem)
		// {
		// ConfigItem ci = (ConfigItem) cd;
		//
		// if (ci.isIgnore())
		// {
		// return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		// }
		// return null;
		// }
		return null;

	}

	@Override
	public final Image getColumnImage(final Object element, final int columnIndex)
	{
		if (element instanceof ConfigTreeNode)
		{
			ConfigTreeNode tn = (ConfigTreeNode) element;
			ConfigData cd = tn.getConfigData();
			if (tn.getValue().equals("aodl:semanticStm"))
			{
				if (cd.isMandatory())
				{
					return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.CLASSIFICATIONS), new String[]
					{IconsInternal.DECORATION_LOCK});
				}
				else
				{
					return _imageReg.get(IconsInternal.CLASSIFICATIONS);
				}
			}
			else if (tn.getValue().equals("aodl:relation"))
			{
				if (cd.isMandatory())
				{
					return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.RELATION), new String[]
					{IconsInternal.DECORATION_LOCK});
				}
				else
				{
					return _imageReg.get(IconsInternal.RELATION);
				}
			}
			else if (tn.getValue().startsWith("aodl:"))
			{
				Image im = null;
				if (cd.isMandatory())
				{
					im = _decorator.decorateImageKeys(_imageReg.get(IconsInternal.MARKUP), new String[]
					{IconsInternal.DECORATION_LOCK});
				}
				else
				{
					im = _imageReg.get(IconsInternal.MARKUP);
				}
				return im;
			}
			else if (tn.getValue().equals("aspectTemplates") || tn.getParent().getValue().equals("aspectTemplates"))
			{
				return _imageReg.get(IconsInternal.TEMPLATES);
			}
			else if (tn.getParent().getValue().equals("complexTemplates")
					|| (tn.getParent().getParent() != null && tn.getParent().getParent().getValue() != null && tn
							.getParent().getParent().getValue().equals("complexTemplates"))
					|| tn.getParent().getValue().equals("semanticTemplates"))
			{
				return _imageReg.get(IconsInternal.TEMPLATE);
			}
			else if (tn.getParent().getParent() != null
					&& tn.getParent().getParent().getValue() != null
					&& (tn.getParent().getParent().getValue().equals("semanticTemplates") || (tn.getParent()
							.getParent().getParent() != null && tn.getParent().getParent().getParent().getValue()
							.equals("complexTemplates"))))
			{
				return _imageReg.get(IconsInternal.TEMPLATE_MARKUP);
			}
			else if (tn.getValue().equals("personIdentifiers"))
			{
				return _imageReg.get(IconsInternal.IDENTIFIERS);
			}
			else if (tn.getParent().getValue().equals("personIdentifiers"))
			{
				return _imageReg.get(IconsInternal.IDENTIFIER);
			}
			else
			{
				if (tn.getParent().getValue().equals("aodl:semanticStm"))
				{
					for (String s : _configFacade.getPersonDisplayNameTags(null))
					{
						if (tn.getValue().equals(s))
						{
							if (cd.isMandatory())
							{
								return _decorator.decorateImageKeys(
										_imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM), new String[]
										{IconsInternal.DECORATION_LOCK});
							}
							else
							{
								return _imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM);
							}
						}
					}
					for (String s : _configFacade.getPersonNameTags(null))
					{
						if (tn.getValue().equals(s))
						{
							if (cd.isMandatory())
							{
								return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.CLASSIFICATION_NAME),
										new String[]
										{IconsInternal.DECORATION_LOCK});
							}
							else
							{
								return _imageReg.get(IconsInternal.CLASSIFICATION_NAME);
							}
						}
					}
					if (cd.isMandatory())
					{
						return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.CLASSIFICATION), new String[]
						{IconsInternal.DECORATION_LOCK});
					}
					else
					{
						return _imageReg.get(IconsInternal.CLASSIFICATION);
					}
				}
				else if (tn.getParent().getValue().equals("aodl:relation")
						|| (tn.getParent().getParent() != null && tn.getParent().getParent().getValue()
								.equals("aodl:relation"))
						|| (tn.getParent().getParent() != null && tn.getParent().getParent().getParent() != null && tn
								.getParent().getParent().getParent().getValue().equals("aodl:relation")))
				{
					if (cd.isMandatory())
					{
						return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.RELATION), new String[]
						{IconsInternal.DECORATION_LOCK});
					}
					else
					{
						return _imageReg.get(IconsInternal.RELATION);
					}
				}
				else
				{
					if (cd.isMandatory())
					{
						return _decorator.decorateImageKeys(_imageReg.get(IconsInternal.MARKUP), new String[]
						{IconsInternal.DECORATION_LOCK});
					}
					else
					{
						return _imageReg.get(IconsInternal.MARKUP);
					}
				}
			}
		}
		return _imageReg.get(IconsInternal.ERROR);
	}

	@Override
	public final String getColumnText(final Object element, final int columnIndex)
	{
		return ((ConfigTreeNode) element).getLabel();
	}

	@Override
	public final Font getFont(final Object element, final int columnIndex)
	{

		ConfigData cd = ((ConfigTreeNode) element).getConfigData();

		if (!cd.isIgnore() && ((ConfigTreeNode) element).isSelected())
		{
			return _registry.getBold(Display.getCurrent().getSystemFont().getFontData()[0].getName());
		}
		return null;
	}

	@Override
	public final Color getForeground(final Object element, final int columnIndex)
	{
		ConfigData cd = ((ConfigTreeNode) element).getConfigData();
		if (cd.isIgnore())
		{
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}
		return null;
	}

	@Override
	public final Image getImage(final Object element)
	{
		return getColumnImage(element, 0);
	}

	// System.out.println("config tree image provision");
	// if (element instanceof TreeNode)
	// {
	// TreeNode tn = (TreeNode) element;
	// ConfigData cd = tn.getConfigData();
	// if (tn.getValue().equals("aodl:semanticStm"))
	// {
	// return
	// decorator.decorateImage(imageReg.get(IconsInternal.CLASSIFICATIONS), cd);
	// }
	// else if (tn.getValue().equals("aodl:relation"))
	// {
	// return decorator.decorateImage(imageReg.get(IconsInternal.RELATION), cd);
	// }
	// else if (tn.getValue().startsWith("aodl:"))
	// {
	// return decorator.decorateImage(imageReg.get(IconsInternal.MARKUP), cd);
	// }
	// else
	// {
	// if (tn.getParent().getValue().equals("aodl:semanticStm"))
	// {
	// for (String s :_configFacade.getPersonDisplayNameTags(null))
	// {
	// if (tn.getValue().equals(s))
	// {
	// return
	// decorator.decorateImage(imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM),
	// cd);
	// }
	// }
	// for (String s :_configFacade.getPersonNameTags(null))
	// {
	// if (tn.getValue().equals(s))
	// {
	// return
	// decorator.decorateImage(imageReg.get(IconsInternal.CLASSIFICATION_NAME),
	// cd);
	// }
	// }
	// return
	// decorator.decorateImage(imageReg.get(IconsInternal.CLASSIFICATION), cd);
	// }
	// else if (tn.getParent().getValue().equals("aodl:relation")
	// || tn.getParent().getParent().getValue().equals("aodl:relation")
	// || (tn.getParent().getParent().getParent() != null
	// &&
	// tn.getParent().getParent().getParent().getValue().equals("aodl:relation")))
	// {
	// return decorator.decorateImage(imageReg.get(IconsInternal.RELATION), cd);
	// }
	// else
	// {
	// return decorator.decorateImage(imageReg.get(IconsInternal.MARKUP), cd);
	// }
	// }
	// }
	// return null;
	// }
	//
	//
	@Override
	public final String getText(final Object element)
	{
		return ((ConfigTreeNode) element).getLabel();
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

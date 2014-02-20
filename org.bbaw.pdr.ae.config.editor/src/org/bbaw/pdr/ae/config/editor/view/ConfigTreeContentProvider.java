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

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * content provider for config tree in config editor and preference pages.
 * @author Christoph Plutte
 */
public class ConfigTreeContentProvider implements ITreeContentProvider
{

	/** show ignored. */
	private boolean _showIgnored;

	private boolean _showTemplates = false;

	private boolean _showIdentifiers;

	/**
	 * constructor with show ignored flag.
	 * @param showIgnored flag whether ignored configs should be shown
	 */
	public ConfigTreeContentProvider(final boolean showIgnored, final boolean showTemplates, final boolean showIdentifiers)
	{
		this._showIgnored = showIgnored;
		this._showTemplates = showTemplates;
		this._showIdentifiers = showIdentifiers;
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public final Object[] getChildren(final Object parentElement)
	{
		if (parentElement instanceof ConfigTreeNode)
		{
			return ((ConfigTreeNode) parentElement).getChildren();
		}
		else if (parentElement instanceof DatatypeDesc)
		{
			return getElements(parentElement);
		}
		return null;
	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		// System.out.println("show ignored " + showIgnored);
		ConfigTreeNode root = new ConfigTreeNode("root", "root", null);

		if (inputElement instanceof ConfigData)
		{
			ConfigData cfd = (ConfigData) inputElement;

			cfd.processPositionSettings();

			processChildren(cfd, root);
			if (_showTemplates && inputElement instanceof DatatypeDesc)
			{
				DatatypeDesc dtd = (DatatypeDesc) cfd;
				ConfigData templates = dtd.getUsage().getTemplates();

				// cfd.processPositionSettings();

				processChildren(templates, root);
			}

			if (_showIdentifiers && inputElement instanceof DatatypeDesc)
			{
				DatatypeDesc dtd = (DatatypeDesc) cfd;
				ConfigData identifiers = dtd.getUsage().getIdentifiers();
				ConfigData helper = new ConfigData();
				helper.getChildren().put("personIdentifiers", identifiers);

				// cfd.processPositionSettings();

				processChildren(helper, root);
			}
		}

		return root.getChildren();
	}

	private void processChildren(ConfigData cfd, ConfigTreeNode root)
	{
		ConfigTreeNode tn1;
		ConfigTreeNode tn2;
		ConfigTreeNode tn3;
		ConfigTreeNode tn4;
		ConfigTreeNode tn5;

		ConfigData c1;
		ConfigData c2;
		ConfigData c3;
		ConfigData c4;
		ConfigData c5;
		for (String k1 : cfd.getChildren().keySet())
		{
			// System.out.println("k1" + k1);
			c1 = cfd.getChildren().get(k1);
			tn1 = new ConfigTreeNode(k1, c1.getLabel(), c1);
			root.addChild(tn1);

			for (String k2 : c1.getChildren().keySet())
			{
				// System.out.println("k2" + k2);

				c2 = c1.getChildren().get(k2);
				tn2 = new ConfigTreeNode(k2, c2.getLabel(), c2);
				tn1.addChild(tn2);

				for (String k3 : c2.getChildren().keySet())
				{
					// System.out.println("k3" + k3);

					c3 = c2.getChildren().get(k3);
					if (_showIgnored || !c3.isIgnore())
					{

						tn3 = new ConfigTreeNode(k3, c3.getLabel(), c3);
						tn2.addChild(tn3);
						for (String k4 : c3.getChildren().keySet())
						{
							c4 = c3.getChildren().get(k4);
							if (_showIgnored || !c4.isIgnore())
							{
								tn4 = new ConfigTreeNode(k4, c4.getLabel(), c4);
								tn3.addChild(tn4);
								for (String k5 : c4.getChildren().keySet())
								{
									c5 = c4.getChildren().get(k5);
									if (_showIgnored || !c5.isIgnore())
									{
										tn5 = new ConfigTreeNode(k5, c5.getLabel(), c5);
										tn4.addChild(tn5);

									}
								}
							}
						}
					}
				}
			}
		}

	}

	// private ConfigData processPositionSettings(ConfigData cfd)
	// {
	// for (String s : cfd.getChildren().keySet())
	// {
	// DataType dt = (DataType) cfd.getChildren().get(s);
	// if (dt.getChildren().size() > 0)
	// {
	// for (String s2 : dt.getChildren().keySet())
	// {
	// processPositionConfigItem(dt.getChildren().get(s2));
	// }
	// }
	// }
	// return cfd;
	// }
	//
	//
	// private void processPositionConfigItem(ConfigData cd)
	// {
	// if (cd.getChildren() != null)
	// {
	// if (cd.getChildren().size() > 0)
	// {
	// for (String s2 : cd.getChildren().keySet())
	// {
	// ConfigItem c2 = (ConfigItem) cd.getChildren().get(s2);
	// for (String s3 : cd.getChildren().keySet())
	// {
	// ConfigItem c3 = (ConfigItem) cd.getChildren().get(s3);
	// if (!c2.equals(c3) && c2.compareTo(c3) == 0)
	// {
	// System.out.println("c3 change base");
	// c3.setBase(c3.getBase() + 1);
	// }
	// }
	// if (c2.getChildren() != null)
	// processPositionConfigItem(cd.getChildren().get(s2));
	// }
	// }
	// }
	//
	//
	// }

	@Override
	public final Object getParent(final Object element)
	{
		return ((ConfigTreeNode) element).getParent();
	}

	@Override
	public final boolean hasChildren(final Object element)
	{
		return ((ConfigTreeNode) element).hasChildren();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{

	}

}

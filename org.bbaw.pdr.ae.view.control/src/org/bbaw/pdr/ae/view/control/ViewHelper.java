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
package org.bbaw.pdr.ae.view.control;

import java.util.HashMap;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.view.control.customSWTWidges.CustomTooltip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.interfaces.ISWTnotRWTHelper;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

public class ViewHelper
{

	private ViewHelper()
	{

	}
	private static Facade _facade = Facade.getInstanz();

	/**
	 * Sets the radio by string.
	 * @param radios the radios
	 * @param quality the quality
	 */
	public static void setRadioByString(final Button[] radios, final String quality)
	{
		if (quality.equals("certain")) //$NON-NLS-1$
		{
			radios[0].setSelection(true);
		}
		else if (quality.equals("probable")) //$NON-NLS-1$
		{
			radios[1].setSelection(true);
		}
		else if (quality.equals("unsure")) //$NON-NLS-1$
		{
			radios[2].setSelection(true);
		}

	}

	/**
	 * Sets the combo viewer by string.
	 * @param cv the cv
	 * @param s the s
	 */
	public static void setComboViewerByString(ComboViewer cv, String s, boolean reveal)
	{
		if (cv.getInput() == null || !(cv.getInput() instanceof HashMap<?, ?>))
		{
			cv.setInput(new HashMap<String, ConfigData>());
		}
		if (cv.getInput() instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
			if (!inputs.containsKey(s))
			{
				boolean isConfigItem = true;
				ConfigItem sibling = null;
				String pos = null;
				for (String key : inputs.keySet())
				{
					if (inputs.get(key) instanceof ConfigItem)
					{
						isConfigItem = true;
						sibling = (ConfigItem) inputs.get(key);
					}
					else
					{
						isConfigItem = false;
					}
					pos = inputs.get(key).getPos();
					break;
				}
				if (isConfigItem)
				{
					HashMap<String, ConfigItem> clones = new HashMap<String, ConfigItem>(inputs.size());
					for (String key : inputs.keySet())
					{
						ConfigItem clone = ((ConfigItem) inputs.get(key)).clone();
						clones.put(new String(key), clone);
					}
					ConfigItem ci = new ConfigItem();
					ci.setValue(s);
					ci.setLabel(s);
					ci.setPos(pos);
					if (sibling != null)
					{
						ci.setParent(sibling.getParent());
					}
					clones.put(s, ci);

					// blanc
					ConfigItem b = new ConfigItem();
					b.setValue("");
					b.setLabel("");
					b.setPos(pos);
					if (sibling != null)
					{
						b.setParent(sibling.getParent());
					}
					clones.put("", b);

					cv.setInput(clones);
					cv.refresh();
					StructuredSelection sel = new StructuredSelection(ci);
					cv.setSelection(sel, reveal);
				}
				else
				{
					HashMap<String, ConfigData> clones = new HashMap<String, ConfigData>(inputs.size());
					for (String key : inputs.keySet())
					{
						ConfigData clone = inputs.get(key).clone();
						clones.put(new String(key), clone);
					}
					ConfigData ci = new ConfigData();
					ci.setValue(s);
					ci.setLabel(s);
					ci.setPos(pos);
					clones.put(s, ci);

					// blanc
					ConfigData b = new ConfigData();
					b.setValue("");
					b.setLabel("");
					b.setPos(pos);
					clones.put("", b);

					cv.setInput(clones);
					cv.refresh();
					StructuredSelection sel = new StructuredSelection(ci);
					cv.setSelection(sel, reveal);
				}
			}
			if (inputs.containsKey(s))
			{
				for (String key : inputs.keySet())
				{
					if (key.equals(s))
					{
						boolean isConfigItem = true;
						ConfigItem sibling = null;
						String pos = null;
						for (String k : inputs.keySet())
						{
							if (inputs.get(k) instanceof ConfigItem)
							{
								isConfigItem = true;
								sibling = (ConfigItem) inputs.get(k);
							}
							else
							{
								isConfigItem = false;
							}
							pos = inputs.get(k).getPos();
							break;
						}
						if (isConfigItem)
						{
							ConfigData cd = inputs.get(key);
							if (cd instanceof ConfigItem && ((ConfigItem) cd).isIgnore())
							{
								((ConfigItem) cd).setReadAlthoughIgnored(true);
								cv.setInput(inputs);
							}
							StructuredSelection sel = new StructuredSelection(cd);
							cv.setSelection(sel, reveal);
						}
						else
						{
							ConfigData cd = inputs.get(key);
							StructuredSelection sel = new StructuredSelection(cd);
							cv.setSelection(sel, reveal);
						}

						break;
					}
				}
			}
		}

	}

	/**
	 * Sets the deco info.
	 * @param deco the deco
	 * @param id the id
	 */
	public static void setDecoInfo(final ControlDecoration deco, final PdrId id)
	{
		//		System.out.println("deco " + deco.toString()); //$NON-NLS-1$
		PdrObject o = null;
		if (id.getType().equals("pdrPo") && _facade.getPerson(id) != null) //$NON-NLS-1$
		{
			o = _facade.getPerson(id);
		}
		else if (id.getType().equals("pdrRo")) //$NON-NLS-1$
		{
			o = _facade.getReference(id);
		}
		else if (id.getType().equals("pdrAo")) //$NON-NLS-1$
		{
			o = _facade.getAspect(id);
		}
		if (o != null)
		{
			deco.setDescriptionText(o.getDisplayName());
			deco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		}
		else
		{
			deco.setDescriptionText(NLMessages.getString("Editor_missing_data_object"));
			deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
					.getImage());
		}
	}

	/**
	 * Set the selection of a combo to a given string s, if string s is not
	 * contained among items of combo it is added and selected.
	 * @param c the combo.
	 * @param s the string.
	 */
	public static void setComboByString(final Combo c, final String s)
	{
		boolean found = false;
		for (int i = 0; i < c.getItems().length; i++)
		{
			if (c.getItem(i).toString().equals(s))
			{
				c.select(i);
				found = true;
				break;
			}
		}
		if (!found)
		{
			c.add(s);
			c.select(c.getItems().length - 1);
		}

	}



	public static void accelerateScrollbar(ScrolledComposite composite, int times)
	{
		ISWTnotRWTHelper swtHelper = ControlExtensions.getSWTnotRWTHelper();
		if (swtHelper != null)
		{
			swtHelper.accelerateScrollbar(composite, times);
		}
	}

	public static void setTabfolderSimple(CTabFolder tabFolder, boolean b)
	{
		ISWTnotRWTHelper swtHelper = ControlExtensions.getSWTnotRWTHelper();
		if (swtHelper != null)
		{
			swtHelper.setTabfolderSimple(tabFolder, b);
		}

	}

	public static void equipWithMouseExitListener(Control control, MarkupTooltip tooltip)
	{
		ISWTnotRWTHelper swtHelper = ControlExtensions.getSWTnotRWTHelper();
		if (swtHelper != null)
		{
			swtHelper.equipWithMouseExitListener(control, tooltip);
		}
	}

	public static void equipeTabFolderToolTip(CTabFolder tabFolder, MarkupTooltip tooltip)
	{
		ISWTnotRWTHelper swtHelper = ControlExtensions.getSWTnotRWTHelper();
		if (swtHelper != null)
		{
			swtHelper.equipeTabFolderToolTip(tabFolder, tooltip);
		}

	}

	public static void comboViewerInsertElement(ComboViewer cv, ConfigData item, int position)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
		boolean isConfigItem = true;
		ConfigItem sibling = null;
		String pos = null;
		if (item instanceof ConfigItem)
		{
			HashMap<String, ConfigItem> clones = new HashMap<String, ConfigItem>(inputs.size());
			for (String key : inputs.keySet())
			{
				ConfigItem clone = ((ConfigItem) inputs.get(key)).clone();
				clones.put(new String(key), clone);
			}

			item.setPos(pos);
			item.setPriority(position);
			if (inputs.keySet().iterator().hasNext())
			{
				sibling = (ConfigItem) inputs.get(inputs.keySet().iterator().next());
			}
			if (sibling != null)
			{
				item.setParent(sibling.getParent());
			}
			clones.put(item.getValue(), (ConfigItem) item);

			// blanc
			ConfigItem b = new ConfigItem();
			b.setValue("");
			b.setLabel("");
			b.setPos(pos);
			if (sibling != null)
			{
				b.setParent(sibling.getParent());
			}
			clones.put("", b);

			cv.setInput(clones);
			cv.refresh();
		}
		else
		{
			HashMap<String, ConfigData> clones = new HashMap<String, ConfigData>(inputs.size());
			for (String key : inputs.keySet())
			{
				ConfigData clone = inputs.get(key).clone();
				clones.put(new String(key), clone);
			}
			
			item.setPos(pos);
			item.setPriority(position);
			clones.put(item.getValue(), item);

			// blanc
			ConfigData b = new ConfigData();
			b.setValue("");
			b.setLabel("");
			b.setPos(pos);
			clones.put("", b);

			cv.setInput(clones);
			cv.refresh();
		}

	}

	// public static String getSemanticLabel(String prov, final String semantic)
	// {
	// if (prov == null)
	// {
	// prov = Platform
	// .getPreferencesService()
	// .getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
	//							AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$;
	// }
	// if (_facade.getConfigs().get(prov) != null
	// && _facade.getConfigs().get(prov).getChildren() != null
	// && _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm")
	// != null
	// &&
	// _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren()
	// .containsKey(semantic))
	// {
	//
	// return
	// _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren().get(semantic)
	// .getLabel();
	// }
	//
	// return semantic;
	// }

	public static void equipeControlWithToolTip(Control control, CustomTooltip tooltip)
	{
		ISWTnotRWTHelper swtHelper = ControlExtensions.getSWTnotRWTHelper();
		if (swtHelper != null)
		{
			swtHelper.equipeControlWithToolTip(control, tooltip);
			swtHelper.equipWithMouseExitListener(control, tooltip);
		}

	}

}

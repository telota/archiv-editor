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
package org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.config.model.Usage;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.control.interfaces.IAEAspectSemanticEditorTemplateController;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IComplexAspectTemplateEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.internal.ComplexAspectTemplateBuilder;
import org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.internal.EasyAspectTemplateBuilder;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;

public class AspectSemanticTemplateController implements IAEAspectSemanticEditorTemplateController
{
	
	private EasyAspectTemplateBuilder _easyAspectTemplateBuilder = new EasyAspectTemplateBuilder();
	private Facade _facade = Facade.getInstanz();
	
	private ComplexAspectTemplateBuilder _complexAspectTemplateBuilder = new ComplexAspectTemplateBuilder();
	
	public AspectSemanticTemplateController()
	{
		String configProvider = getConfigProvider();
		String standard = "PDR";
		if (!_facade.getConfigs().containsKey(standard))
		{
			for (String s : _facade.getConfigs().keySet())
			{
				standard = s;
				break;
			}
		}
		if (!_facade.getConfigs().containsKey(configProvider))
		{
			configProvider = standard;
		}
	}

	private String getConfigProvider()
	{
		return Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY,
						null).toUpperCase();
	}

	@Override
	public String[] getSemanticsOfTemplates()
	{
		String configProvider = getConfigProvider();
		List<String> templates = new ArrayList<String>();
		DatatypeDesc dtd = _facade.getConfigs().get(configProvider);
		if (dtd != null && dtd.getUsage().getTemplates() != null && dtd.getUsage().getTemplates().getChildren() != null)
		{
			ConfigData cd = dtd.getUsage().getTemplates().getChildren().get("aspectTemplates");
			if (cd != null && cd.getChildren() != null && cd.getChildren().containsKey("semanticTemplates")
					&& cd.getChildren().get("semanticTemplates").getChildren() != null)
			{
				for (String key : cd.getChildren().get("semanticTemplates").getChildren().keySet())
				{
					SemanticTemplate semTempalte = (SemanticTemplate) cd.getChildren().get("semanticTemplates")
							.getChildren().get(key);
					if (!semTempalte.isIgnore() && !semTempalte.getChildren().isEmpty())
					{
						templates.add(key);
					}
				}

			}
		}
		return (String[]) (templates.toArray(new String[templates.size()]));

	}

	@Override
	public Object getTemplate(String value)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEasyAspectEditor getEasyAspectEditor(IAEBasicEditor parentEditor, SemanticTemplate semanticTemplate,
			Person currentPerson, Aspect currentAspect,
			Composite control, int style)
	{
		return _easyAspectTemplateBuilder.buildEasyAspectEditor(parentEditor, semanticTemplate, currentPerson,
				currentAspect,
				control, style);
	}

	@Override
	public IComplexAspectTemplateEditor getComplexAspectTemplateEditor(IAEBasicEditor parentEditor, String label,
			Person currentPerson, Composite parentComposite,
			int style)
	{
		String configProvider = getConfigProvider();
		Usage u = Facade.getInstanz().getConfigs().get(configProvider).getUsage();
		if (u.getTemplates() != null && u.getTemplates().getChildren() != null
				&& u.getTemplates().getChildren().containsKey("aspectTemplates")
				&& u.getTemplates().getChildren().get("aspectTemplates").getChildren().containsKey("complexTemplates")
				&& !u.getTemplates().getChildren().get("aspectTemplates").getChildren().get("complexTemplates")
						.isIgnore())
		{
			ConfigData cd = u.getTemplates().getChildren().get("aspectTemplates").getChildren().get("complexTemplates");
			if (cd.getChildren().get(label) != null && !cd.getChildren().get(label).isIgnore())
			{
				ComplexSemanticTemplate cst = (ComplexSemanticTemplate) cd.getChildren().get(label);
				return _complexAspectTemplateBuilder.buildComplexAspectEditor(parentEditor, cst, currentPerson,
						parentComposite, style);
			}
			else if (!cd.getChildren().isEmpty())
			{
				for (ConfigData c : cd.getChildren().values())
				{
					if ((c.getValue() != null && c.getValue().equals(label)) || (c.getLabel() != null && c.getLabel().equals(label)) && c instanceof ComplexSemanticTemplate)
					{
						ComplexSemanticTemplate cst = (ComplexSemanticTemplate) c;
						return _complexAspectTemplateBuilder.buildComplexAspectEditor(parentEditor, cst, currentPerson,
								parentComposite, style);
					}
				}
			}
			
		}
		return null;
	}

	@Override
	public String[] getComplexAspectTemplateEditorLabels()
	{
		String configProvider = getConfigProvider();
		if (_facade.getConfigs().containsKey(configProvider))
	{
			Usage u = Facade.getInstanz().getConfigs().get(configProvider).getUsage();
		if (u.getTemplates() != null && u.getTemplates().getChildren() != null
				&& u.getTemplates().getChildren().containsKey("aspectTemplates")
				&& u.getTemplates().getChildren().get("aspectTemplates").getChildren().containsKey("complexTemplates")
				&& !u.getTemplates().getChildren().get("aspectTemplates").getChildren().get("complexTemplates")
						.isIgnore())
		{
			ConfigData cd = u.getTemplates().getChildren().get("aspectTemplates").getChildren().get("complexTemplates");
			List<ConfigData> children = new ArrayList<ConfigData>(cd.getChildren().values());
			Collections.sort(children);
			ArrayList<String> list = new ArrayList<String>();
			for (ConfigData cdd : children)
			{
				if (!cdd.isIgnore())
				{
					list.add(cdd.getLabel());
				}
			}
			String[] array = list.toArray(new String[list.size()]);
			return array;

		}
		}
		return null;
	}

	@Override
	public String[] getComplexAspectTemplateSemantics()
	{
		String configProvider = getConfigProvider();
		if (_facade.getConfigs().containsKey(configProvider))
		{
			Usage u = Facade.getInstanz().getConfigs().get(configProvider).getUsage();
		if (u.getTemplates() != null
				&& u.getTemplates().getChildren() != null
				&& u.getTemplates().getChildren().containsKey("aspectTemplates")
				&& u.getTemplates().getChildren().get("aspectTemplates").getChildren().containsKey("complexTemplates")
				&& !u.getTemplates().getChildren().get("aspectTemplates").getChildren().get("complexTemplates")
						.isIgnore())
		{
			ConfigData cd1 = u.getTemplates().getChildren().get("aspectTemplates").getChildren()
					.get("complexTemplates");
			ArrayList<String> list = new ArrayList<String>();
			ConfigData cd2; // complexTemplate
			ConfigData cd3;
			String semantic;
			for (String s : cd1.getChildren().keySet())
			{
				cd2 = cd1.getChildren().get(s);
				if (!cd2.isIgnore())
				{
					for (String ss : cd2.getChildren().keySet())
					{
						cd3 = cd2.getChildren().get(ss);
						if (cd3 != null && !cd3.isIgnore())
						{
							semantic = cd3.getValue();
							if (!list.contains(semantic))
							{
								list.add(semantic);
							}
						}
					}
				}
			}
			String[] array = list.toArray(new String[list.size()]);
//			for (String s : array)
//			{
//				System.out.println("complex template semantics:" + s);
//			}
			return array;
		}
		}
		return null;
	}

}

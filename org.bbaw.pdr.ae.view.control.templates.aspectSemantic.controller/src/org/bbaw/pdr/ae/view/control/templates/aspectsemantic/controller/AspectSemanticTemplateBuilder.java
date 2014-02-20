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

import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.view.control.customSWTWidges.AEAspectWidgetCustomizable;
import org.bbaw.pdr.ae.view.control.customSWTWidges.AspectMarkupTemplate;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AspectSemanticTemplateBuilder
{
	private boolean _maywrite = true;


	public AspectMarkupTemplate buildTemplate(IEasyAspectEditor editor, SemanticTemplate semanticTemplate, Composite control)
	{
		if (editor.getAspect() != null)
		{
			_maywrite = new UserRichtsChecker().mayWrite(editor.getAspect());
		}
//		loadAspectTemplates();
//		
//		if (_aspectTemplates != null)
//		{
//			ConfigData semanticTemplates = _aspectTemplates.getChildren().get("semanticTemplates");
//			if (semanticTemplates != null)
//			{
//				ConfigData template = semanticTemplates.getChildren().get(value);
//				if (template != null)
//				{
//					configTemplates = new ArrayList<ConfigData>(template.getChildren().values());
//				}
//
//			}
//		}
		List<ConfigData> configTemplates = null;
		
		if (semanticTemplate != null)
		{
			configTemplates = new ArrayList<ConfigData>(semanticTemplate.getChildren().values());
		}

		control.setLayoutData(new GridData());
		((GridData) control.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) control.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) control.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) control.getLayoutData()).grabExcessVerticalSpace = true;
		control.setLayout(new GridLayout());
		((GridLayout) control.getLayout()).marginHeight = 0;
		((GridLayout) control.getLayout()).verticalSpacing = 0;
		((GridLayout) control.getLayout()).marginWidth = 0;

		AspectMarkupTemplate aspectTemplate = new AspectMarkupTemplate(editor, editor.getOwningObject(), control, SWT.NONE);
		aspectTemplate.setLayoutData(new GridData());
		((GridData) aspectTemplate.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) aspectTemplate.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) aspectTemplate.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) aspectTemplate.getLayoutData()).grabExcessVerticalSpace = true;
		aspectTemplate.setLayout(new GridLayout(4, true));
		((GridLayout) aspectTemplate.getLayout()).marginHeight = 2;
		((GridLayout) aspectTemplate.getLayout()).verticalSpacing = 3;
		((GridLayout) aspectTemplate.getLayout()).marginWidth = 0;

		if (configTemplates != null)
		{
			Collections.sort(configTemplates);
			for (ConfigData configD : configTemplates)
			{
				if (!configD.isIgnore())
				{
					AspectConfigTemplate configTemplate = (AspectConfigTemplate) configD;
					AEAspectWidgetCustomizable widget = new AEAspectWidgetCustomizable(editor, aspectTemplate,
							configTemplate, _maywrite, SWT.NONE);
					widget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, configTemplate
							.getHorizontalSpan(), 1));
					// widget.layout();
					// widget.pack();
					aspectTemplate.addWidget(widget);
				}
			}
			Point p = aspectTemplate.computeSize(editor.getSize().x, SWT.DEFAULT);
			aspectTemplate.setSize(p);
			// aspectTemplate.layout();

			control.layout();

		}
		return aspectTemplate;
	}

//	private ConfigData loadAspectTemplates()
//	{
//		if (_aspectTemplates == null)
//		{
//			_aspectTemplates = _facade.getConfigs().get(_markupProvider).getUsage().getTemplates().getChildren()
//					.get("aspectTemplates");
//		}
//		return _aspectTemplates;
//	}

}

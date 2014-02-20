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
package org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.internal;

import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticDim;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.SpatialDim;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.interfaces.IComplexAspectTemplateEditor;
import org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.AspectSemanticTemplateController;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class AspectTemplatePreviewDialog extends TitleAreaDialog
{

	private ConfigData _template;

	private EasyAspectTemplateBuilder _easyAspectTemplateBuilder = new EasyAspectTemplateBuilder();
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AspectTemplatePreviewDialog(Shell parentShell, ConfigData template)
	{
		super(parentShell);
		this._template = template;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		final Composite area = (Composite) super.createDialogArea(parent);

		ScrolledComposite scrollComp = new ScrolledComposite(area, SWT.V_SCROLL);
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setAlwaysShowScrollBars(true);
		scrollComp.setLayoutData(new GridData());
		((GridData) scrollComp.getLayoutData()).heightHint = 375;
		((GridData) scrollComp.getLayoutData()).widthHint = 860;
		((GridData) scrollComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) scrollComp.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) scrollComp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) scrollComp.getLayoutData()).grabExcessVerticalSpace = true;

		GridLayout layout;
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 0;
		scrollComp.setLayout(layout);

		Composite contentComp = new Composite(scrollComp, SWT.NONE);
		contentComp.setLayout(layout);
		contentComp.setLayoutData(new GridData());
		((GridData) contentComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) contentComp.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) contentComp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) contentComp.getLayoutData()).grabExcessVerticalSpace = true;
		scrollComp.setContent(contentComp);
		scrollComp.setMinHeight(1);
		scrollComp.setMinWidth(1);
		scrollComp.setFocus();

		if (_template instanceof SemanticTemplate)
		{
			SemanticTemplate semanticTempalte = (SemanticTemplate) _template;
			_easyAspectTemplateBuilder
					.buildEasyAspectEditor(null, semanticTempalte, null, null, contentComp,
					SWT.NONE);

		}
		else if (_template instanceof ComplexSemanticTemplate)
		{

			IComplexAspectTemplateEditor editor = new AspectSemanticTemplateController()
					.getComplexAspectTemplateEditor(null, _template.getValue(), null, contentComp, SWT.NONE);
			for (String value : editor.getHandledSemantics())
			{
				OrderingHead oh = new OrderingHead();
				oh.setValue(value);
				Vector<Aspect> as = new Vector<Aspect>(1);
				Aspect a = new Aspect("pdrXX.000.000.000000000");
				a.setSemanticDim(new SemanticDim());
				a.getSemanticDim().getSemanticStms().add(new SemanticStm(value));
				a.setRelationDim(new RelationDim());
				RelationStm rStm = new RelationStm();
				rStm.setSubject(new PdrId("pdrXX.000.000.000000000"));
				rStm.setRelations(new Vector<Relation>());
				Relation rel = new Relation();
				rel.setRelation("aspect_of");
				rel.setObject(new PdrId("pdrXX.000.000.000000000"));
				rStm.getRelations().add(rel);
				a.getRelationDim().getRelationStms().add(rStm);
				if (a.getTimeDim() == null)
				{
					a.setTimeDim(new TimeDim());
					a.getTimeDim().setTimeStms(new Vector<TimeStm>());
				}
				if (a.getTimeDim().getTimeStms().size() == 0)
				{
					TimeStm st = new TimeStm();
					st.setType("undefined"); //$NON-NLS-1$
					st.setTimes(new Vector<Time>());
					a.getTimeDim().getTimeStms().add(st);
				}
				if (a.getSpatialDim() == null)
				{
					a.setSpatialDim(new SpatialDim());
					a.getSpatialDim().setSpatialStms(new Vector<SpatialStm>());
				}
				if (a.getSpatialDim().getSpatialStms().size() == 0)
				{
					SpatialStm spS = new SpatialStm();
					spS.setType("undefined"); //$NON-NLS-1$
					a.getSpatialDim().getSpatialStms().add(spS);
				}

				ValidationStm vStm = new ValidationStm();
				Reference ref = new Reference();
				ref.setSourceId(new PdrId("pdrRo.000.000.000000000"));
				ref.setQuality("mock");
				ref.setAuthority(new PdrId("pdrXX.000.000.000000000"));
				vStm.setReference(ref);
				a.setValidation(new Validation());
				a.getValidation().getValidationStms().add(vStm);
				a.setNotification("?");
				// System.out.println("preview aspect valid: " + a.isValid());
				as.add(a);

				oh.setAspects(as);
				editor.setInput(oh);
			}

		}
		else
		{
			// System.out.println("NO Template");
		}

		contentComp.layout();
		scrollComp.setMinSize(contentComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		area.layout();
		area.pack();
		// Point point = area.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		//
		// area.setSize(point.x, point.y);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, NLMessages.getString("BrowserDialog_close"), true);
	}

	@Override
	protected boolean isResizable()
	{
		return true;
	}
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(1010, 600);
	}
}

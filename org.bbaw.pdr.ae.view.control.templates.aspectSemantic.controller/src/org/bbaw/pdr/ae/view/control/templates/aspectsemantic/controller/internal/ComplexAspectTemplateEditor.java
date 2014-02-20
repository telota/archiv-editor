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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.control.core.PDRObjectBuilder;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.facade.RightsChecker;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IComplexAspectTemplateEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.bbaw.pdr.ae.view.editorlite.view.PersonAspectEditor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.progress.UIJob;

public class ComplexAspectTemplateEditor extends Composite implements
		IComplexAspectTemplateEditor {


	private Facade _facade = Facade.getInstanz();
	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;
	private ComplexSemanticTemplate _complexSemanticTemplate;
	private Composite _composite;
	private EasyAspectTemplateBuilder _easyAspectTemplateBuilder = new EasyAspectTemplateBuilder();
	private HashMap<String, ComplexAspectEditor> _complexAspectEditors = new HashMap<String, ComplexAspectEditor>(3);
	private Person _currentPerson;
	private Vector<OrderingHead> _inputs = new Vector<OrderingHead>();
	private PaintListener _paintListener;
	private ArrayList<PaintListener> _paintListeners = new ArrayList<PaintListener>();
	private PDRObjectBuilder _pdrObjectBuilder = new PDRObjectBuilder();
	private SelectionAdapter _selectionListener;
	private ComplexAspectEditor _selectedEditor;
	private IAEBasicEditor _parentEditor;
	private ArrayList<SelectionListener> _easyEditorSelectionListener = new ArrayList<SelectionListener>();
	
	private RightsChecker _rightsChecker = new RightsChecker();
	private boolean _selected;


	public ComplexAspectTemplateEditor(IAEBasicEditor parentEditor, ComplexSemanticTemplate complexSemanticTemplate,
			Person currentPerson,
			Composite parentComposite, int style)
	{
		super(parentComposite, SWT.BORDER);

		this._parentEditor = parentEditor;
		this._complexSemanticTemplate = complexSemanticTemplate;
		this._currentPerson = currentPerson;
		
		if (_complexSemanticTemplate != null)
		{
			createEditor();
		}
	}

	private void createEditor() {
		if (_facade .getConfigs() != null)
		{
			if (!_facade.getConfigs().containsKey(_markupProvider))
			{
				if (_facade.getConfigs().containsKey("PDR"))
				{
					_markupProvider = "PDR";
				}
				else if (!_facade.getConfigs().isEmpty())
				{
					for (String key : _facade.getConfigs().keySet())
					{
						_markupProvider = key;
						break;
					}
				}
			}
		}
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginWidth = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		this.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

		this.setLayoutData(new GridData());
		((GridData) this.getLayoutData()).widthHint = 1000;
		_composite = new Composite(this, SWT.NONE);
		_composite.setLayout(new GridLayout(1, false));
		((GridLayout) _composite.getLayout()).marginWidth = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 25;
		_composite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		
		createListener();

		List<ConfigData> semantics = new  ArrayList<ConfigData>( _complexSemanticTemplate.getChildren().values());
		Collections.sort(semantics);
		for (ConfigData cd : semantics)
		{
			cd.getValue();
			SemanticTemplate semanticTemplate = (SemanticTemplate) cd;
			if (!semanticTemplate.isIgnore())
			{
				ComplexAspectEditor editor = new ComplexAspectEditor(ComplexAspectTemplateEditor.this,
						semanticTemplate, _composite, SWT.BORDER);
				editor.setLayoutData(new GridData());
				editor.layout();
				editor.addCustomPaintListener(_paintListener);
				editor.addSelectionListener(_selectionListener);
				_complexAspectEditors.put(semanticTemplate.getValue(), editor);
			}
		}
		_composite.layout();
		this.layout();
		
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
		 {
			doExtraLayoutRefresh();

		 }
		
	}
	public void doExtraLayoutRefresh() {
		Job job = new Job("Refresh")
		{
			@Override
			protected IStatus run(final IProgressMonitor monitor)
			{

				UIJob job = new UIJob("Refresh layout...")
				{
					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{

						Point p = ComplexAspectTemplateEditor.this.getShell().getSize();
						ComplexAspectTemplateEditor.this.getShell().setSize(p.x + 1, p.y +1);
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule(100);
		
	}

	private void createListener()
	{
		_paintListener = new PaintListener()
		{

			@Override
			public void paintControl(PaintEvent e)
			{
				// System.out.println("paint event aus complexaspecttemplateteditor");
//				_composite.layout();

				ComplexAspectTemplateEditor.this.resize();
			}
		};

		_selectionListener = new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// System.out.println("SelectionEvent in ComplexAspectTemplateEditor");
				if (!_selected && e.data != null && e.data instanceof ComplexAspectEditor)
				{
					if (_selectedEditor != null)
					{
						_selectedEditor.setSelected(false, _selectedEditor.isValid());
					}
					_selectedEditor = (ComplexAspectEditor) e.data;
					_selectedEditor.setSelected(true, _selectedEditor.isValid());
				}
			}
		};
	}
	private void resize() {
	
		// System.out.println("complexaspecttemplateeditor resize");
		Point point = ComplexAspectTemplateEditor.this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		ComplexAspectTemplateEditor.this.setSize(point.x, point.y);
		for (PaintListener l : _paintListeners)
		{
			Event ee = new Event();
			ee.widget = ComplexAspectTemplateEditor.this;
			l.paintControl(new PaintEvent(ee));
		}
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDirty(boolean isDirty)
	{

		if (isDirty && _parentEditor != null)
		{
			_parentEditor.setDirty(true);
		}
	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid) {
		this._selected = isSelected;
		if (contextIsValid && isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		else if (isSelected)
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}

	}

	
	private void setValid(boolean isValid) {

	}

	@Override
	public void validate() {
		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}

	}

	@Override
	public void saveInput()
	{

	}

	public Vector<Aspect> getDirtyAspects()
	{
//		System.out.println("ComplexAspectTemplateEditor: getDirtyAspects");
		Vector<Aspect> secureAspects = new Vector<Aspect>();
		if (_inputs != null && !_inputs.isEmpty())
		{
			for (OrderingHead oh : _inputs)
			{
				for (Aspect a : oh.getAspects())
				{
					if (a.isDirty() && !secureAspects.contains(a))
					{
						if (!_currentPerson.getAspectIds().contains(a.getPdrId()))
						{
							_currentPerson.getAspectIds().add(a.getPdrId());
						}
						secureAspects.add(a);
					}
				}
			}
		}
		return secureAspects;
	}

	@Override
	public void addSelectionListener(SelectionListener sl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(Object input) 
	{
		// System.out.println("complex editor set input");
		if (input != null && input instanceof OrderingHead)
		{
			OrderingHead oh = (OrderingHead) input;
			if (!_inputs.contains(oh))
			{
				ComplexAspectEditor editor = _complexAspectEditors.get(oh.getValue());
				if (editor != null)
				{
					editor.setInput(oh);
				}
				_composite.layout();
				_inputs.add(oh);
			}
			
		}
		this.getParent().layout(true, true);


	}

	@Override
	public Vector<String> getHandledSemantics() {
		Vector<String> handledSemantics = new Vector<String>();
		for (ConfigData cd : _complexSemanticTemplate.getChildren().values())
		{
			
			if (!cd.isIgnore() && !handledSemantics.contains(cd.getValue()))
			{
				handledSemantics.add(cd.getValue());
			}
		}
		// for (String s : handledSemantics)
		// {
		// // System.out.println("handledsem " + s);
		// }
		return handledSemantics;
	}
	public final void addCustomPaintListener(final PaintListener paintListener)
	{
		if (paintListener != null && !_paintListeners.contains(paintListener))
		{
			_paintListeners.add(paintListener);
		}

	}
	
	private class ComplexAspectEditor extends Composite implements IAEBasicEditor
	{
		private SemanticTemplate semanticTemplate;
		private ArrayList<PaintListener> paintListeners = new ArrayList<PaintListener>();
		private PaintListener paintListener;
		private Vector<IEasyAspectEditor> eds = new Vector<IEasyAspectEditor>(1);
		private Vector<Button> deleteButtons = new Vector<Button>(3);
		private OrderingHead orderingHead;
		private Composite editorComposite;
		private SelectionAdapter selectionListener;
		private List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

		private IEasyAspectEditor selectedEditor;
		private Label titleLabel;
		private SelectionListener deleteListener;
		private boolean editable = true;
		private boolean selected;
		private Button multiButton;
		public ComplexAspectEditor(IAEBasicEditor parentEditor, SemanticTemplate semanticTemplate, Composite parent,
				int style)
		{
			super(parent, style);
			this.semanticTemplate = semanticTemplate;
			this.setLayout(new GridLayout(2, true));
			((GridLayout) this.getLayout()).marginWidth = 0;
			((GridLayout) this.getLayout()).marginHeight = 0;
			((GridLayout) this.getLayout()).verticalSpacing = 0;
			this.setLayoutData(new GridData());
			((GridData) this.getLayoutData()).widthHint = 1005;
			this.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			
			createComplexEditor();
		}

		public final void addCustomPaintListener(final PaintListener paintListener)
		{
			if (paintListener != null)
			{
				paintListeners.add(paintListener);
			}

		}

		@Override
		public final void addSelectionListener(final SelectionListener selectionListener)
		{
			if (selectionListener != null)
			{
				selectionListeners.add(selectionListener);
			}

		}

		private void createComplexEditor() {
			
			paintListener = new PaintListener()
			{

				@Override
				public void paintControl(PaintEvent e)
				{
					// System.out.println("paint event aus complexaspeceditor");
//					_composite.layout();

					ComplexAspectEditor.this.resize();
					for (PaintListener l : ComplexAspectTemplateEditor.this._paintListeners)
					{
						Event ee = new Event();
						ee.widget = ComplexAspectTemplateEditor.this;
						l.paintControl(new PaintEvent(ee));
					}
				}
			};
			
			selectionListener = new SelectionAdapter()
			{

				@Override
				public void widgetSelected(SelectionEvent e)
				{
					// System.out.println("SelectionEvent in ComplexAspectEditor selected "
					// + selected);
					if (e.data != null && e.data instanceof IEasyAspectEditor)
					{
						if (selectedEditor != null && !((Composite) selectedEditor).isDisposed())
						{
							selectedEditor.setSelected(false, _selectedEditor.isValid());
						}
						selectedEditor = (IEasyAspectEditor) e.data;
						selectedEditor.setSelected(true, selectedEditor.isValid());
						if (true || !selected)
						{
							for (SelectionListener s : selectionListeners)
							{
								e.data = ComplexAspectEditor.this;
								s.widgetSelected(e);
							}

						}
						for (SelectionListener s : _easyEditorSelectionListener)
						{
							e.data = selectedEditor;
							s.widgetSelected(e);
						}
						validate();
					}
				}
			};

			titleLabel = new Label(this, SWT.NONE);
			titleLabel.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			titleLabel.setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);

			titleLabel.setLayoutData(new GridData());
			((GridData) titleLabel.getLayoutData()).horizontalSpan = 1;
			((GridData) titleLabel.getLayoutData()).horizontalAlignment = SWT.RIGHT;
			
			ControlDecoration descriptionDeco = new ControlDecoration(titleLabel,  SWT.RIGHT | SWT.TOP);
			titleLabel.setText(semanticTemplate.getLabel());
			String desc = semanticTemplate.getDescription();
			if (desc != null && desc.trim().length() > 0)
			{
				descriptionDeco.setDescriptionText(desc);
				descriptionDeco.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			}
			
			if (semanticTemplate != null
					&& (semanticTemplate.isAllowMultiple() || orderingHead == null || orderingHead.getAspects()
							.isEmpty()))
			{
				multiButton = new Button(this, SWT.PUSH);
				multiButton.setText("+");
				// _multiButton.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
				multiButton.setLayoutData(new GridData());
				((GridData) multiButton.getLayoutData()).horizontalSpan = 1;
				((GridData) multiButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;

				multiButton.addSelectionListener(new SelectionListener()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						SemanticStm sStm = new SemanticStm();
						sStm.setProvider(_markupProvider);
						sStm.setLabel(semanticTemplate.getValue());
						Aspect a;
						if (_currentPerson != null)
						{
							a = _pdrObjectBuilder.buildNewAspect(_currentPerson.getPdrId(), sStm);
							_currentPerson.getAspectIds().add(a.getPdrId());
						}
						else
						{
							a = _pdrObjectBuilder.buildNewAspect(null, sStm);
						}
						Vector<Aspect> aspects = orderingHead.getAspects();
						if (aspects == null)
						{
							aspects = new Vector<Aspect>(3);
							orderingHead.setAspects(aspects);
						}
						aspects.insertElementAt(a, 0);
						ComplexAspectEditor.this.loadOrderingHead(orderingHead);

						ComplexAspectEditor.this.resize();
						setSelected(true, false);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e)
					{
						// TODO Auto-generated method stub

					}
				});
			}
			if (orderingHead == null)
			{
				orderingHead = new OrderingHead();
			}
			createEditorComposite();
			// createDefaultInput();
			this.layout();
			
		}
		

		private void createDefaultInput()
		{
			if (orderingHead == null)
			{
				IEasyAspectEditor editor = _easyAspectTemplateBuilder.buildEasyAspectEditor(ComplexAspectEditor.this,
						semanticTemplate, _currentPerson, null, editorComposite, SWT.NONE);
				eds.add(editor);
				editor.addCustomPaintListener(_paintListener); //changed to parent paintlistener
				editor.addSelectionListener(selectionListener);
				SemanticStm sStm = new SemanticStm();
				sStm.setProvider(_markupProvider);
				sStm.setLabel(semanticTemplate.getValue());
				Aspect a;
				if (_currentPerson != null)
				{
					a = _pdrObjectBuilder.buildNewAspect(_currentPerson.getPdrId(), sStm);
				}
				else
				{
					a = _pdrObjectBuilder.buildNewAspect(null, sStm);
				}
				orderingHead = new OrderingHead();
				Vector<Aspect> aspects = new Vector<Aspect>(1);
				aspects.add(a);
				orderingHead.setAspects(aspects);
				editor.setInput(a);
				// editor.layout();
				editor.setEditable(_rightsChecker.mayWrite(a));
				editor.setSelected(false, true);
				if (semanticTemplate != null && semanticTemplate.isAllowMultiple())
				{
					((GridData) ((Control) editor).getLayoutData()).horizontalSpan = 1;

					Button multiButton = new Button(editorComposite, SWT.PUSH);
					multiButton.setText("-");
					multiButton.setData(a);
					// _multiButton.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
					multiButton.setLayoutData(new GridData());
					((GridData) multiButton.getLayoutData()).horizontalSpan = 1;
					((GridData) multiButton.getLayoutData()).horizontalAlignment = SWT.LEFT;

					multiButton.addSelectionListener(deleteListener);
					deleteButtons.add(multiButton);

				}
				else
				{
					((GridData) ((Control) editor).getLayoutData()).horizontalSpan = 2;
				}
			}

		}

		protected void resize() {
			Point point = ComplexAspectTemplateEditor.this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

			ComplexAspectTemplateEditor.this.setSize(point.x, point.y);
			for (PaintListener l : _paintListeners)
			{
				Event ee = new Event();
				ee.widget = ComplexAspectTemplateEditor.this;
				l.paintControl(new PaintEvent(ee));
			}
			
		}

		@Override
		public boolean isDirty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isValid() {
			boolean valid = true;
			for (IEasyAspectEditor e : eds)
			{
				if (!e.isValid())
				{
					valid = false;
					break;
				}
			}
			setValid(valid);
			return valid;
		}

		@Override
		public void setDirty(boolean isDirty) {
			// System.out.println("ComplexAspectEditor setDirty and add oh to parentEditor");
			if (!ComplexAspectTemplateEditor.this._inputs.contains(orderingHead))
			{
				ComplexAspectTemplateEditor.this._inputs.add(orderingHead);
			}
			if (isDirty && _parentEditor != null)
			{
				_parentEditor.setDirty(true);
			}
		}

		@Override
		public void setSelected(boolean isSelected, boolean contextIsValid)
		{
			this.selected = isSelected;

			if (!this.isDisposed())
			{
				for (IEasyAspectEditor e : eds)
				{
					if (!((Composite) e).isDisposed())
					{
						if (e.equals(selectedEditor))
						{
							e.setSelected(isSelected && editable, e.isValid());
						}
						else
						{
							e.setSelected(false, e.isValid());
						}
					}
				}
				for (Button b : deleteButtons)
				{
					if (!b.isDisposed())
					{
						b.setEnabled(isSelected && editable);
					}
				}
				if (contextIsValid && isSelected)
				{
					setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
					setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
					editorComposite.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
				}
				else if (contextIsValid)
				{
					setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
					setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
					editorComposite.setForeground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
				}
				else if (isSelected)
				{
					setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
					setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
					editorComposite.setForeground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
				}
				else
				{
					setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
					setForeground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
					editorComposite.setForeground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
				}
			}
		}

		@Override
		public void setBackground(Color color)
		{
			super.setBackground(color);
			if (titleLabel != null)
			{
				titleLabel.setBackground(color);
			}
		}

		@Override
		public void setForeground(Color color)
		{
			super.setForeground(color);
			if (titleLabel != null)
			{
				titleLabel.setForeground(color);
			}
		}


		@Override
		public void validate() {
			if (_parentEditor != null)
			{
				_parentEditor.validate();
			}
			
		}

		@Override
		public void saveInput() {

		}

		@Override
		public void setInput(Object input) {
			

			if (input != null && input instanceof OrderingHead)
			{
				eds.clear();
				orderingHead = (OrderingHead) input;
				loadOrderingHead(orderingHead);

			}
			
		}

		private void loadOrderingHead(OrderingHead orderingHead2)
		{
			if (editorComposite != null)
			{
				editorComposite.dispose();
				editorComposite = null;
			}
			createEditorComposite();

			IEasyAspectEditor editor;
			Aspect a;
			// System.out.println("oh.aspects.size " +
			// orderingHead.getAspects().size());
			for (int i = 0; i < orderingHead.getAspects().size(); i++)
			{
				// System.out.println("for index " + i);
				a = orderingHead.getAspects().get(i);
				editor = _easyAspectTemplateBuilder.buildEasyAspectEditor(ComplexAspectEditor.this, semanticTemplate,
						_currentPerson, null,
						editorComposite, SWT.NONE);
				eds.add(editor);
				editor.addCustomPaintListener(paintListener);
				editor.addSelectionListener(selectionListener);
				editor.setInput(a);
				// editor.layout();
				editor.setSelected(false, editor.isValid());
				editor.setEditable(_rightsChecker.mayWrite(a));
				if (_rightsChecker.mayWrite(a) && semanticTemplate != null)
				{
					((GridData) ((Control) editor).getLayoutData()).horizontalSpan = 1;

					Button multiButton = new Button(editorComposite, SWT.PUSH);
					multiButton.setText("-");
					multiButton.setData(a);
					// _multiButton.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
					multiButton.setLayoutData(new GridData());
					((GridData) multiButton.getLayoutData()).horizontalSpan = 1;
					((GridData) multiButton.getLayoutData()).horizontalAlignment = SWT.LEFT;

					multiButton.addSelectionListener(deleteListener);
					deleteButtons.add(multiButton);

				}
				else
				{
					((GridData) ((Control) editor).getLayoutData()).horizontalSpan = 2;
				}

			}
			multiButton.setEnabled(orderingHead.getAspects().isEmpty() || semanticTemplate.isAllowMultiple());
			editorComposite.layout();
			editorComposite.pack();
			this.layout();
			this.pack();
			ComplexAspectEditor.this.resize();
		}

		private void createEditorComposite()
		{
			editorComposite = new Composite(ComplexAspectEditor.this, SWT.NONE);
			editorComposite.setLayout(new GridLayout(2, false));
			((GridLayout) editorComposite.getLayout()).marginWidth = 0;
			((GridLayout) editorComposite.getLayout()).marginHeight = 0;
			((GridLayout) editorComposite.getLayout()).verticalSpacing = 5;
			editorComposite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			editorComposite.setLayoutData(new GridData());
			((GridData) editorComposite.getLayoutData()).horizontalSpan =2;
			deleteListener = new SelectionListener()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					Aspect a = (Aspect) ((Button)e.getSource()).getData();
					if (a != null && _currentPerson != null)
					{
						if (_parentEditor instanceof PersonAspectEditor)
						{
							if (((PersonAspectEditor) _parentEditor).deleteAspect(a))
							{
								orderingHead.getAspects().remove(a);
								ComplexAspectEditor.this.loadOrderingHead(orderingHead);
							}
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub

				}
			};

		}

		@Override
		public void setEditable(boolean editable) {
			this.editable = editable;
			if (!this.isDisposed())
			{
				for (IEasyAspectEditor e : eds)
				{
					if (!((Composite) e).isDisposed())
					{
						e.setEditable(editable);
						if (e.equals(selectedEditor))
						{
							e.setSelected(selected && editable, e.isValid());
						}
						else
						{
							e.setSelected(false, e.isValid());
						}
					}
				}
				for (Button b : deleteButtons)
				{
					if (!b.isDisposed())
					{
						b.setEnabled(selected && editable);
					}
				}
			}
			
		}
		
	}

	@Override
	public void createDefaultInput()
	{
		for (String key : _complexAspectEditors.keySet())
		{
			_complexAspectEditors.get(key).createDefaultInput();
		}
//		_composite.layout();
		_composite.layout();
		// System.out.println("complexaspecteditor createDefaultInput");
		for (PaintListener l : _paintListeners)
		{
			Event ee = new Event();
			ee.widget = ComplexAspectTemplateEditor.this;
			l.paintControl(new PaintEvent(ee));
		}
	}

	@Override
	public void setEditable(boolean editable) {
		for (ComplexAspectEditor ed : _complexAspectEditors.values())
		{
			if (ed != null)
			{
				ed.setEditable(editable);
			}
		}
		
	}

	@Override
	public void addEasyEditorSelectionListener(SelectionListener selectionListener)
	{
		if (selectionListener != null && !_easyEditorSelectionListener.contains(selectionListener))
		{
			_easyEditorSelectionListener.add(selectionListener);
		}

	}

}

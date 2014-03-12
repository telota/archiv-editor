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
import java.util.Stack;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.view.UndoInformation;
import org.bbaw.pdr.ae.view.control.customSWTWidges.AspectMarkupTemplate;
import org.bbaw.pdr.ae.view.control.customSWTWidges.RelationEditorLine;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.bbaw.pdr.ae.view.editorlite.view.EasyAspectEditor;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class EasyAspectTemplateEditor extends Composite implements IAEBasicEditor, ISelectionProvider,
		IEasyAspectEditor
{

	private Facade _facade = Facade.getInstanz();

	private Aspect _aspect;

	private Person _currentPerson;
	/** The _selected taggingRange. */
	private TaggingRange _selectedTR;

	private Group _group;

	private Composite _plainEditorControl;

	private Composite _relationComposite;

	private boolean _pointOfTime;

	private boolean _showTemplate = true;


	private PdrDate _dateFrom;

	private PdrDate _dateTo;

	private boolean _isValid;

	private boolean _isDirty;

	private String _selectedText;

	private List<RelationEditorLine> _relationEditors;
	private SelectionListener _relationSelectionListener;
	private Listener _relationDeleteListener;
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListener = new ArrayList<SelectionListener>();

	private ArrayList<PaintListener> _paintListeners = new ArrayList<PaintListener>();

	/* Undo/Redo */
	/** The UND o_ stacksize. */
	private static final int UNDO_STACKSIZE = 50;

	/** The _stack undo. */
	private Stack<UndoInformation> _stackUndo;

	/** The _stack redo. */
	private Stack<UndoInformation> _stackRedo;

	/** The _protect redo stack. */
	private boolean _protectRedoStack;

	/** The _customize favorite markup. */
	private Action _undoAction, _redoAction, _markupDelete, _personFromStringAction;

	private Composite _markupControl;

	private AspectMarkupTemplate _template;

	private StackLayout _editorStackLayout;

	private Composite _stackComposite;

	private EasyAspectEditor _easyEditor;

	private PaintListener _paintListener;

	private Button _editorSwitch;

	private IAEBasicEditor _parentEditor;

	private boolean _editable = true;

	private boolean _selected;

	public EasyAspectTemplateEditor(IAEBasicEditor parentEditor, Person currentPerson, Aspect aspect, Composite parent,
			int style)
	{
		super(parent, style);
		// super.setLayoutData(new GridData());
		// ((GridData) super.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData) super.getLayoutData()).grabExcessHorizontalSpace = true;
		this._parentEditor = parentEditor;
		this._aspect = aspect;
		this._currentPerson = currentPerson;
		createEditor();

		if (_aspect != null)
		{
			_isValid = _aspect.isValid();
			_group.setText(_aspect.getDisplayNameWithID());
		}

		// EasyAspectTemplateEditor.this.layout();


	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{

	}


	/**
	 * @param selectionListener
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#addSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	@Override
	public final void addSelectionListener(final SelectionListener selectionListener)
	{
		if (selectionListener != null)
		{
			_selectionListener.add(selectionListener);
		}
		if (_template != null)
		for (SelectionListener l : _selectionListener)
		{
			_template.addSelectionListener(l);
		}

	}

	public final void addCustomPaintListener(final PaintListener paintListener)
	{
		if (paintListener != null && !_paintListeners.contains(paintListener))
		{
			_paintListeners.add(paintListener);
		}

	}

	private void createActionFromConfigData(IMenuManager menuManager, final ConfigData cd)
	{
		boolean ignore = false;
		if (cd instanceof ConfigItem)
		{
			ignore = ((ConfigItem) cd).isIgnore();
		}

		if (!ignore && cd instanceof ConfigItem)
		{
			Action markup = new Action(cd.getLabel())
			{
				@Override
				public void run()
				{
					// System.out.println("setMarkup-action " + cd.getValue());
					setMarkup(cd);
				}
			};
			menuManager.add(markup);

		}
		if (_selectedTR == null && !ignore && cd.getChildren() != null)
		{
			IMenuManager mm2 = new MenuManager(cd.getLabel());
			menuManager.add(mm2);
			List<String> keys = new ArrayList<String>(cd.getChildren().keySet());
			Collections.sort(keys);
			for (String key : keys)
			{
				final ConfigData cd2 = cd.getChildren().get(key);
				createActionFromConfigData(mm2, cd2);
			}
		}

	}

	private void createActions()
	{

		_undoAction = new Action(NLMessages.getString("Editor_action_undo"))
		{
			@Override
			public void run()
			{
				/* Undo -> Redo */
				if (_stackRedo.size() == UNDO_STACKSIZE)
				{
					_stackRedo.removeElementAt(0);
				}
				_stackRedo.push(_stackUndo.pop());

				UndoInformation currentUndoInformation;
				currentUndoInformation = _stackUndo.pop();

				/* Redo-Stack vorm Loeschen bewahren */
				_protectRedoStack = true;

				/* Aenderung ausfuehren */
				if (currentUndoInformation.isModifiedText())
				{
					_aspect.setNotification(currentUndoInformation.getReplacedText());
					_aspect.setRangeList(currentUndoInformation.getReplacedRanges());
					loadTextAndMarkup();

				}
				else
				{
					// System.out.println("Ungueltige Undo-Information: " +
					// currentUndoInformation.toString());
				}
				/*
				 * Bei der naechsten Aenderung, die auf den Undo-Stack kommt,
				 * kann der Redo-Stack wieder geloescht werden
				 */
				_protectRedoStack = false;

			}
		};
		_undoAction.setEnabled(false);
		_undoAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.UNDO));

		_redoAction = new Action(NLMessages.getString("Editor_action_redo"))
		{
			@Override
			public void run()
			{
				UndoInformation currentUndoInformation;
				currentUndoInformation = _stackRedo.pop();

				/* Redo-Stack vorm Loeschen bewahren */
				_protectRedoStack = true;

				/* Aenderung ausfuehren */
				if (currentUndoInformation.isModifiedText())
				{
					_aspect.setNotification(currentUndoInformation.getReplacedText());
					_aspect.setRangeList(currentUndoInformation.getReplacedRanges());
					loadTextAndMarkup();
				}
				else
				{
					// System.out.println("Ungueltige Undo-Information: " +
					// currentUndoInformation.toString());
				}
				/*
				 * Bei der naechsten Aenderung, die auf den Undo-Stack kommt,
				 * kann der Redo-Stack wieder geloescht werden
				 */
				_protectRedoStack = false;

			}
		};
		_redoAction.setEnabled(false);
		_redoAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REDO));

		_personFromStringAction = new Action(NLMessages.getString("Editor_create_person_fromString"))
		{
			@Override
			public void run()
			{
				ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
				IParameter iparam = null;

				// get the command from plugin.xml
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
				Command cmd = cmdService.getCommand("org.bbaw.pdr.ae.view.main.commands.CreatePersonFromNameString");

				// get the parameter
				try
				{
					iparam = cmd.getParameter("org.bbaw.pdr.ae.view.main.param.personNameString");
				}
				catch (NotDefinedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Parameterization params = new Parameterization(iparam, _selectedText);
				parameters.add(params);

				try
				{
					iparam = cmd.getParameter("org.bbaw.pdr.ae.view.main.param.originalAspectID");
				}
				catch (NotDefinedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				params = new Parameterization(iparam, _aspect.getPdrId().toString());
				// build the parameterized command
				ParameterizedCommand pc = new ParameterizedCommand(cmd,
						parameters.toArray(new Parameterization[parameters.size()]));

				// execute the command
				try
				{
					IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
					handlerService.executeCommand(pc, null);
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}

			}
		};
		_personFromStringAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.PERSON_QUICK));
	}

	private void createEditor()
	{
		if (_facade.getConfigs() != null)
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

		/* Undo/Redo */
		_stackUndo = new Stack<UndoInformation>();
		_stackUndo.ensureCapacity(UNDO_STACKSIZE);
		_stackRedo = new Stack<UndoInformation>();
		_stackRedo.ensureCapacity(UNDO_STACKSIZE);
		_protectRedoStack = false;

		this.setLayout(new GridLayout(2, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		this.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		this.setLayoutData(new GridData());
		((GridData) this.getLayoutData()).widthHint = 970;

		_editorSwitch = new Button(this, SWT.TOGGLE);
		_editorSwitch.setImage(_imageReg.get(IconsInternal.TEMPLATE));
		_editorSwitch.setLayoutData(new GridData());
		((GridData) _editorSwitch.getLayoutData()).verticalAlignment = SWT.TOP;
		_editorSwitch.setSelection(_showTemplate);
		_editorSwitch.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_showTemplate = !_showTemplate;
				if (_showTemplate)
				{
					//rap auskommentiert cp
					_easyEditor.saveInput();
					_easyEditor.removeCustomPaintListener(_paintListener);
					_template.setInput(null);
					_template.setInput(_aspect);
					_template.layout();
					_editorStackLayout.topControl = _group;
					_markupControl.setVisible(true);
					_plainEditorControl.setVisible(false);
					_stackComposite.layout();
					
				}
				else
				{
					_template.saveInput();
					if (!(Boolean) _plainEditorControl.getData("loaded"))
					{
						loadPlainEditor();
						//rap auskommentiert cp
						_easyEditor.addCustomPaintListener(_paintListener);
						_plainEditorControl.setData("loaded", true);
						
					}
					else
					{
						//rap auskommentiert cp
						_easyEditor.setInput(_aspect);
						_easyEditor.addCustomPaintListener(_paintListener);

					}
					_editorStackLayout.topControl = _plainEditorControl;
					_easyEditor.layout();
					_template.layout();
					_plainEditorControl.setVisible(true);
					_markupControl.setVisible(false);
					_stackComposite.layout();
				}
				validateInternal();
				setSelected(true, isValid());
//				Event ee = new Event();
//				ee.widget = EasyAspectTemplateEditor.this;
//				SelectionEvent se = new SelectionEvent(ee);
//				se.data = EasyAspectTemplateEditor.this;
//				for (SelectionListener s : _selectionListener)
//				{
//					s.widgetSelected(se);
//				}
				resize();
//				validate();
//				contentChanged();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		_editorStackLayout = new StackLayout();
		_stackComposite = new Composite(this, SWT.None);
		_stackComposite.setLayout(new GridLayout(1, false));
		((GridLayout) _stackComposite.getLayout()).marginHeight = 0;
		((GridLayout) _stackComposite.getLayout()).verticalSpacing = 0;
		((GridLayout) _stackComposite.getLayout()).marginWidth = 0;
		_stackComposite.setLayoutData(new GridData());
		((GridData) _stackComposite.getLayoutData()).horizontalSpan = 1;
		_stackComposite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		_stackComposite.setLayout(_editorStackLayout);

		_group = new Group(_stackComposite, SWT.NONE);
		_group.setLayoutData(new GridData());
		((GridData) _group.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _group.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _group.getLayoutData()).widthHint = 930;
		_group.setLayout(new GridLayout(1, false));
		((GridLayout) _group.getLayout()).marginHeight = 0;
		((GridLayout) _group.getLayout()).verticalSpacing = 0;

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;

		// gd.heightHint = 50;
		 gd.widthHint = 850;

		_markupControl = new Composite(_group, SWT.NONE);
		_markupControl.setLayoutData(gd);

		_editorStackLayout.topControl = _group;

		_plainEditorControl = new Composite(_stackComposite, SWT.NONE);
		_plainEditorControl.setLayoutData(gd);
		_plainEditorControl.setData("loaded", false);
		_plainEditorControl.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);



		createActions();
		createMenus(_markupControl);

	}



	private void loadPlainEditor()
	{
		_easyEditor = new EasyAspectEditor(_currentPerson, _aspect, EasyAspectTemplateEditor.this, _plainEditorControl,
				SWT.NONE);
		_easyEditor.setLayoutData(new GridData());
		_easyEditor.setEditable(_editable);
		//rap auskommentiert cp
//		Listener[] ls = EasyAspectTemplateEditor.this.getListeners(SWT.Paint);
//		for (Listener l : ls)
//		{
//			if (true || l instanceof PaintListener)
//			{
//				_easyEditor.addListener(SWT.Paint, l);
//
//			}
//		}
		// rap alternativ
		ArrayList<PaintListener> ls = EasyAspectTemplateEditor.this._paintListeners;
		for (PaintListener l : ls)
		{
			_easyEditor.addCustomPaintListener(l);
		}
		_easyEditor.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = EasyAspectTemplateEditor.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = EasyAspectTemplateEditor.this;
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});



	}

	protected void createMenus(final Composite st)
	{
		MenuManager menuMgr = new MenuManager();
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			@Override
			public void menuAboutToShow(IMenuManager mgr)
			{
				if (_editable)
				{
					fillMenu(mgr);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(st);
		fillMenu(menuMgr);

		st.setMenu(menu);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite()
				.registerContextMenu("org.bbaw.pdr.view.editorlite.menu", menuMgr, EasyAspectTemplateEditor.this);
	}

	private void deleteRelation(Relation rel)
	{
		boolean found = false;
		boolean removeStm = false;
		RelationStm relationStm = null;
		if (_aspect.getRelationDim() != null && _aspect.getRelationDim().getRelationStms() != null)
		{
			for (RelationStm rStm : _aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject() != null && rStm.getSubject().equals(_currentPerson.getPdrId())
						&& rStm.getRelations() != null)
				{
					for (Relation r : rStm.getRelations())
					{
						if (r.equals(rel))
						{
							found = true;
							removeStm = (rStm.getRelations().size() == 1);
							break;
						}
					}
				}
				if (found && removeStm)
				{
					relationStm = rStm;
					break;
				}
				else if (found)
				{
					rStm.getRelations().remove(rel);
				}
			}
			if (found && removeStm && relationStm != null)
			{
				_aspect.getRelationDim().getRelationStms().remove(relationStm);
			}
		}
		_relationEditors = null;
		for (Control c : _relationComposite.getChildren())
		{
			c.dispose();
		}
		if (_aspect.getRelationDim() != null && _aspect.getRelationDim().getRelationStms() != null)
		{
			for (RelationStm rStm : _aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject() != null && rStm.getSubject().equals(_currentPerson.getPdrId())
						&& rStm.getRelations() != null)
				{
					for (Relation r : rStm.getRelations())
					{
						loadRelation(r, false);
					}
				}
			}
			_relationComposite.layout();
		}
		if (_relationEditors != null)
		{
			for (RelationEditorLine relEd : _relationEditors)
			{
				relEd.setSelected(true, isValid());
			}
		}
		_group.layout();

		_relationComposite.layout();
		setDirty(true);

	}

	protected void fillMenu(IMenuManager rootMenuManager)
	{

		if (_selectedTR == null && _facade.getConfigs().containsKey(_markupProvider))
		{
			final HashMap<String, ConfigData> configs = _facade.getConfigs().get(_markupProvider).getChildren();

			for (String key : configs.keySet())
			{
				ConfigData cd = configs.get(key);
				if (!(cd.getValue().contains("relation") || cd.getValue().contains("semantic")))
				{
					createActionFromConfigData(rootMenuManager, cd);
				}
			}
		}


		rootMenuManager.add(new Separator());
		if (_selectedTR != null)
		{
			rootMenuManager.add(_markupDelete);
		}
		rootMenuManager.add(new Separator());

		rootMenuManager.add(new Separator());

		rootMenuManager.add(_undoAction);
		rootMenuManager.add(_redoAction);
	}

	/**
	 * @return
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#getAspect()
	 */
	@Override
	public Aspect getAspect()
	{
		return _aspect;
	}

	/**
	 * @return
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#getSelection()
	 */
	@Override
	public ISelection getSelection()
	{
		StructuredSelection selection = new StructuredSelection(_aspect);
		return selection;
	}

	/**
	 * @return
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#isDirty()
	 */
	@Override
	public boolean isDirty()
	{
		return _isDirty;
	}

	/**
	 * @return
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#isValid()
	 */
	@Override
	public boolean isValid()
	{
		return _isValid;
	}

	private void loadAspect()
	{
		_group.setText(_aspect.getDisplayName() + " - ID: " + _aspect.getPdrId().toString());

		_template.setInput(_aspect);
		validateInternal();
		setSelected(_selected, isValid());
		
	}

	private void loadTextAndMarkup()
	{



	}

	private void loadRelation(Relation rel, boolean selected)
	{
		if (_relationEditors == null)
		{
			_relationEditors = new ArrayList<RelationEditorLine>(3);
			_relationSelectionListener = new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					Event ee = new Event();
					ee.widget = EasyAspectTemplateEditor.this;
					SelectionEvent se = new SelectionEvent(ee);
					se.data = EasyAspectTemplateEditor.this;
					for (SelectionListener s : _selectionListener)
					{
						s.widgetSelected(se);
					}
				}

			};
			_relationDeleteListener = new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					Relation rel = (Relation) event.data;
					deleteRelation(rel);

				}

			};
		}
		RelationEditorLine relEditor = new RelationEditorLine(EasyAspectTemplateEditor.this, rel, _relationComposite,
				false, SWT.NONE);
		relEditor.setLayoutData(new GridData());
		((GridData) relEditor.getLayoutData()).horizontalSpan = 1;
		((GridData) relEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) relEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		relEditor.setSelected(selected, isValid());
		relEditor.addSelectionListener(_relationSelectionListener);
		relEditor.addDeleteListener(_relationDeleteListener);

		_relationEditors.add(relEditor);
	}

	/**
	 * @param listener
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param color
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#setBackground(org.eclipse.swt.graphics.Color)
	 */
	@Override
	public void setBackground(Color color)
	{
		if (_group != null && !super.isDisposed())
		{
			super.setBackground(color);
			_group.setBackground(color);
			// _titleLabel.setBackground(color);
			// _buttonComposite.setBackground(color);
			// _relationComposite.setBackground(color);
			// for (Control c : _relationComposite.getChildren())
			// {
			// c.setBackground(color);
			// //
			// c.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			//
			// }
		}
		if (_plainEditorControl != null && !_plainEditorControl.isDisposed())
		{
			_plainEditorControl.setBackground(color);
		}
	}

	/**
	 * @param isDirty
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#setDirty(boolean)
	 */
	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_isDirty && _aspect != null && !_aspect.isDirty())
		{
			_aspect.setDirty(true);
			if (_parentEditor != null)
			{
				_parentEditor.setDirty(isDirty);
			}
		}

	}

	/**
	 * @param color
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#setForeground(org.eclipse.swt.graphics.Color)
	 */
	@Override
	public void setForeground(Color color)
	{
		if (_group != null && !super.isDisposed())
		{
			super.setForeground(color);
			_group.setForeground(color);
			// _buttonComposite.setForeground(color);
			// _relationComposite.setForeground(color);
			// for (Control c : _relationComposite.getChildren())
			// {
			// c.setForeground(color);
			//
			// }
		}
	}

	/**
	 * Sets the markup.
	 * @param cd
	 */
	private final void setMarkup(ConfigData cd)
	{
		if (_stackUndo.size() == UNDO_STACKSIZE)
		{
			_stackUndo.removeElementAt(0);
		}
		_stackUndo.push(new UndoInformation(_aspect.getNotification(), _aspect.getRangeList()));

		if (!_protectRedoStack)
		{
			_stackRedo.clear();
		}
		if (_undoAction != null)
		{
			_undoAction.setEnabled(_stackUndo.size() > 0);
		}
		if (_redoAction != null)
		{
			_redoAction.setEnabled(_stackRedo.size() > 0);
		}
		ConfigItem ci = (ConfigItem) cd;
		ConfigItem ciParent;
		ConfigItem ciGrandParent;
		ConfigItem ciGreatGrandParent;
		String name;
		String type = null;
		String subtype = null;
		String role = null;
		String ana = _currentPerson.getPdrId().toString();
		if (ci.getParent() != null)
		{
			if (ci.getParent() instanceof ConfigItem)
			{
				ciParent = (ConfigItem) ci.getParent();
				if (ciParent.getParent() != null)
				{
					if (ciParent.getParent() instanceof ConfigItem)
					{
						ciGrandParent = (ConfigItem) ciParent.getParent();
						if (ciGrandParent.getParent() != null)
						{
							if (ciGrandParent.getParent() instanceof ConfigItem)
							{
								ciGreatGrandParent = (ConfigItem) ciGrandParent.getParent();
								name = ciGreatGrandParent.getValue();
								type = ciGrandParent.getValue();
								subtype = ciParent.getValue();
								role = ci.getValue();
							}
							else
							{
								name = ciGrandParent.getParent().getValue();
								type = ciGrandParent.getValue();
								subtype = ciParent.getValue();
								role = ci.getValue();
							}

						}
						else
						{
							name = ciGrandParent.getValue();
							type = ciParent.getValue();
							subtype = ci.getValue();
						}
					}
					else
					{
						name = ciParent.getParent().getValue();
						type = ciParent.getValue();
						subtype = ci.getValue();
					}
				}
				else
				{
					name = ciParent.getValue();
					type = ci.getValue();
				}
			}
			else
			{
				name = ci.getParent().getValue();
				type = ci.getValue();
			}
		}
		else
		{
			name = ci.getValue();
		}

		if (name.startsWith("aodl:"))
		{
			name = name.substring(5);
		}
		// FIXME testselection!!!???
		if (name.length() > 0 && type.length() > 0)
		{
			// StyleRange sr = new StyleRange();
			// sr.start = styledTextAspect.getSelection().x;
			// sr.length = styledTextAspect.getSelectionCount();
			// sr.background = chooseColor(name);
			TaggingRange tr = new TaggingRange(name, type, subtype, role, ana, null);
			if (name.equals("date"))
			{
				if (_pointOfTime)
				{
					tr.setWhen(_dateFrom);
				}
				else
				{
					tr.setFrom(_dateFrom);
					tr.setTo(_dateTo);
				}
			}


			// styledTextAspect.setStyleRange(sr);
			// styledTextAspect.setSelection(sr.start,sr.start);

			setDirty(true);

		}
	}

	/**
	 * @param b
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;

		if (_editorSwitch != null && !_editorSwitch.isDisposed())
		{
			_editorSwitch.setEnabled(isSelected);
		}
		if (_template != null && !_template.isDisposed())
		{
			_template.setSelected(isSelected, contextIsValid);
		}
		if (contextIsValid && isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			if (!_markupControl.isDisposed())
			{
				_markupControl.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			}
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			if (!_markupControl.isDisposed())
			{
				_markupControl.setForeground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			}
		}
		else if (isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			if (!_markupControl.isDisposed())
			{
				_markupControl.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			}
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			if (!_markupControl.isDisposed())
			{
				_markupControl.setForeground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			}
		}
		if (_easyEditor != null)
		{
			_easyEditor.setSelected(isSelected, contextIsValid);
		}
		

	}


	@Override
	public void setSelection(ISelection selection)
	{
		// TODO Auto-generated method stub

	}


	
	private void setValid(boolean isValid)
	{
		this._isValid = isValid;
	}
	private void contentChanged()
	{
		setDirty(true);
		saveInput();
		validateInternal();
		validate();
	}
	private void validateInternal() {
		if (_aspect != null)
		{
			if (_aspect.getValidation() == null)
			{
				_aspect.setValidation(new Validation());
			}
			if (_aspect.getTimeDim() == null)
			{
				_aspect.setTimeDim(new TimeDim());
				_aspect.getTimeDim().setTimeStms(new Vector<TimeStm>());

			}
			// FIXME relations
			if (_template != null)
			{
				setValid(_aspect.isValid() && _template.isValid());
			}

		}
		else
		{
			setValid(true);
		}
		
	}

	@Override
	public void validate()
	{
		
		validateInternal();

		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}

	}

	public void setLayoutData(LayoutData layoutData)
	{
		super.setLayoutData(layoutData);
	}

	public Composite getMarkupControl()
	{
		// TODO Auto-generated method stub
		return _markupControl;
	}

	public void setMarkupTemplate(AspectMarkupTemplate template)
	{
		_template = template;
		_template.setInput(_aspect);
		_template.setOwningObject(_currentPerson);
		((GridData) _markupControl.getLayoutData()).widthHint = 900; // this.getParent().getSize().x

		//rap auskommentiert cp
		// for (Listener l :
		// EasyAspectTemplateEditor.this.getListeners(SWT.Paint))
		// {
		// if (l instanceof PaintListener)
		// {
		// PaintListener p = (PaintListener) l;
		// Event ee = new Event();
		// ee.widget = EasyAspectTemplateEditor.this;
		// p.paintControl(new PaintEvent(ee));
		// }
		// }
		// rap alternativ
		// System.out.println("easyaspectemplateditor settemplate");
		for (PaintListener l : EasyAspectTemplateEditor.this._paintListeners)
		{
			if (l instanceof PaintListener)
			{
				PaintListener p = (PaintListener) l;
				Event ee = new Event();
				ee.widget = EasyAspectTemplateEditor.this;
				p.paintControl(new PaintEvent(ee));
			}
		}
		
		_template.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = EasyAspectTemplateEditor.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = EasyAspectTemplateEditor.this;
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		_template.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(MouseEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = EasyAspectTemplateEditor.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = EasyAspectTemplateEditor.this;
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}

			}
		});
		// EasyAspectTemplateEditor.this.layout();
		contentChanged();
//		setSelected(false, isValid());
	}

	@Override
	public void saveInput()
	{
		if (_template != null && _showTemplate)
		{
			_template.saveInput();
		}

	}

	@Override
	public PdrObject getOwningObject() {
		
		return _currentPerson;
	}

	@Override
	public void setInput(Object input) {
		if (input instanceof Aspect)
		{
			_aspect = (Aspect) input;
			loadAspect();
			_group.setText(_aspect.getDisplayNameWithID());
			resize();
			validateInternal();
			validate();
//			for (PaintListener l : EasyAspectTemplateEditor.this._paintListeners)
//			{
//				Event ee = new Event();
//				ee.widget = EasyAspectTemplateEditor.this;
//				l.paintControl(new PaintEvent(ee));
//			}
		}
		
	}

	@Override
	public void layout()
	{
		super.layout();
	}

	public void resize()
	{
		// System.out.println("resize template editor");

		Point point;
//		if (_template != null)
//		{
//			point = _template.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
//
//			_template.setSize(point.x, point.y);
//			_template.layout();
//			_template.redraw();
//			_template.update();
//		}
//		_group.layout();
//		_group.redraw();
//		_group.update();
//		Point point = _group.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
//
//		_group.setSize(point.x, point.y);
//		_markupControl.layout();
		_markupControl.redraw();
		_markupControl.update();
		// _markupControl.pack();
//		_markupControl.getParent().redraw();
		_markupControl.getParent().update();
		_markupControl.getParent().layout();
		point = EasyAspectTemplateEditor.this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		int x = point.x > 970 ? 970 : point.x;
		
		EasyAspectTemplateEditor.this.setSize(970, point.y);
		// EasyAspectTemplateEditor.this.redraw();
		for (PaintListener l : _paintListeners)
		{
			Event ee = new Event();
			ee.widget = EasyAspectTemplateEditor.this;
			l.paintControl(new PaintEvent(ee));
		}

	}

	@Override
	public void dispose()
	{
		super.dispose();
		_selectionListener.clear();
	}

	@Override
	public void removeCustomPaintListener(PaintListener paintListener)
	{
		if (_paintListeners != null)
		{
			_paintListeners.remove(paintListener);
		}

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable = editable;
		if (_template != null && !_template.isDisposed())
		{
			_template.setEditable(_editable);
		}
	}

}

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
package org.bbaw.pdr.ae.view.editorlite.view;

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
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.UndoInformation;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.RelationEditorLine;
import org.bbaw.pdr.ae.view.control.customSWTWidges.TimeStmEditorLine;
import org.bbaw.pdr.ae.view.control.customSWTWidges.ValidationEditorLine;
import org.bbaw.pdr.ae.view.control.dialogs.CharMapDialog;
import org.bbaw.pdr.ae.view.control.dialogs.SelectOwnerAndKeyDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IDateParser;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupEditor;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class EasyAspectEditor extends Composite implements IAEBasicEditor, ISelectionProvider, IEasyAspectEditor
{

	private Facade _facade = Facade.getInstanz();

	private Aspect _aspect;

	private Person _currentPerson;
	/** The _selected taggingRange. */

	private Group _group;

	private Composite _buttonComposite;

	private Composite _relationComposite;

	// private StyledText _sText;

	private ControlDecoration _decoSText;

	private Label _textLabel;

	private Button _addRelation;

	private boolean _pointOfTime;

	private boolean _customizedTime = false;

	private boolean _helperStarted = false;

	private PdrDate _dateFrom;

	private PdrDate _dateTo;

	private boolean _isValid;

	private boolean _isDirty;

	private TimeStmEditorLine _timeStmEditor;

	private MarkupTooltip _markupTooltip;

	private Vector<ValidationEditorLine> _validationEditors = new Vector<ValidationEditorLine>(1);

	private List<RelationEditorLine> _relationEditors;
	private SelectionListener _relationSelectionListener;
	private Listener _relationDeleteListener;

	private ArrayList<PaintListener> _paintListeners = new ArrayList<PaintListener>();

	/** The _date parser. */
	private IDateParser _dateParser = new ControlExtensions().getDateParser();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	private boolean _editable = true;

	private boolean _markupShownOnAction;

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListener = new ArrayList<SelectionListener>();
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

	private Composite _valEdComposite;

	private Vector<Button> _validationButtons;

	private Action _insertSpecialCharAction;

	private Button _symbolButton;

	private Composite _labelComp;

	/** The _markup editor. */
	private IMarkupEditor _markupEditor = ControlExtensions.createMarkupEditor();

	protected TaggingRange[] _selectedTaggingRanges;

	private boolean _selected;

	private IAEBasicEditor _parentEditor;

	public EasyAspectEditor(Person currentPerson, Aspect aspect, IAEBasicEditor parentEditor, Composite parent,
			int style)
	{
		super(parent, style);
		this._aspect = aspect;
		this._currentPerson = currentPerson;
		this._parentEditor = parentEditor;
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		((GridLayout) this.getLayout()).marginWidth = 0;
		this.setLayoutData(new GridData());
		((GridData) this.getLayoutData()).widthHint = 850;
		this.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		createEditor();

		if (_aspect != null)
		{
			_isValid = _aspect.isValid();
			loadAspect();
			setSelected(false, true);
		}
//		_group.pack();
		_group.layout();
//		this.pack();
		this.layout();
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
			if (_markupEditor.getSelectionText().length() > 0)
			{
				markup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP_ADD));
			}
			else
			{
				markup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP_LIGHTNING));
			}
		}
		if ((_selectedTaggingRanges == null || _selectedTaggingRanges.length == 0) && !ignore
				&& cd.getChildren() != null)
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
		_markupDelete = new Action(NLMessages.getString("Editor_delete"))
		{
			@Override
			public void run()
			{
				if (_selectedTaggingRanges != null)
				{
					if (_stackUndo.size() == UNDO_STACKSIZE) 
					{
						_stackUndo.removeElementAt(0);
					}
					saveMarkup();
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
					_markupEditor.deleteMarkup(_selectedTaggingRanges);
					// _sText.setStyleRange(new
					// StyleRange(_selectedTaggingRange.getStart(),
					// _selectedTaggingRange
					// .getLength(), null, null));
					_aspect.getRangeList().remove(_selectedTaggingRanges);
					EasyAspectEditor.this.setDirty(true);

				}
			}
		};
		_markupDelete.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP_REMOVE));

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
				if (!_stackUndo.isEmpty())
				{
					currentUndoInformation = _stackUndo.pop();

				/* Redo-Stack vorm Loeschen bewahren */
				_protectRedoStack = true;

				/* Aenderung ausfuehren */
				 if (currentUndoInformation.isModifiedText())
				 {
					 _aspect.setNotification(currentUndoInformation.getReplacedText());
					 _aspect.setRangeList(currentUndoInformation.getReplacedRanges());
					_markupEditor.refresh();

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
					_markupEditor.refresh();
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
				Parameterization params = new Parameterization(iparam, _markupEditor.getSelectionText());
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

		_insertSpecialCharAction = new Action(NLMessages.getString("Editor_insert_special_char"))
		{
			@Override
			public void run()
			{
				CharMapDialog dialog = new CharMapDialog(new Shell());
				int c = dialog.open();
				if (c != Window.CANCEL)
				{
					_markupEditor.insert(new String(new char[]
					{(char) c}));
					// _sText.setSelection(_sText.getSelection().x + 1);
				}
			}
		};
		_insertSpecialCharAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.EDIT_SYMBOL));
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

		_group = new Group(this, SWT.NONE);
		_group.setLayoutData(new GridData());
		((GridData) _group.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _group.getLayoutData()).grabExcessHorizontalSpace = true;
		_group.setLayout(new GridLayout(3, false));
		((GridLayout) _group.getLayout()).marginHeight = 0;
		((GridLayout) _group.getLayout()).verticalSpacing = 0;
		_group.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(MouseEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = EasyAspectEditor.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = EasyAspectEditor.this;
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}

			}
		});

		_labelComp = new Composite(_group, SWT.NONE);
		_labelComp.setLayoutData(new GridData());
		((GridData) _labelComp.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		_labelComp.setLayout(new GridLayout(1, false));
		((GridLayout) _labelComp.getLayout()).marginHeight = 0;
		((GridLayout) _labelComp.getLayout()).verticalSpacing = 0;
		
		_textLabel = new Label(_labelComp, SWT.NONE);
		_textLabel.setText(NLMessages.getString("Editor_content"));
		_textLabel.setLayoutData(new GridData());
		((GridData) _textLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) _textLabel.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		
		_symbolButton = new Button(_labelComp, SWT.PUSH);
		_symbolButton.setImage(_imageReg.get(IconsInternal.EDIT_SYMBOL));
		_symbolButton.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_insertSpecialCharAction.run();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});

		_markupEditor.setComposite(_group);
		_markupEditor.setTitle(NLMessages.getString("Editor_notificationOfAspect"));
		_markupEditor.createEditor();

		((GridData) _markupEditor.getControl().getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _markupEditor.getControl().getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _markupEditor.getControl().getLayoutData()).horizontalSpan = 2;

		_markupEditor.addKeyListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				_selectedTaggingRanges = _markupEditor.getSelectedMarkups();
				if (_aspect.getNotification() == null || _aspect.getNotification().trim().length() == 0)
				{
					_markupEditor.saveChanges();
					_selectedTaggingRanges = null;
					_decoSText.setImage(null);
					validate();

				}
				else
				{
					_decoSText.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					validate();
				}
			}

		});
		_markupEditor.addFocusListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				_selectedTaggingRanges = _markupEditor.getSelectedMarkups();
				if (event.doit)
				{
					Event ee = new Event();
					 // ee.data = EasyAspectEditor.this;
					 ee.widget = EasyAspectEditor.this;
					 SelectionEvent se = new SelectionEvent(ee);
					 se.data = EasyAspectEditor.this;
					 for (SelectionListener s : _selectionListener)
					 {
					 s.widgetSelected(se);
					 }
					

				}
				else
				{
					saveMarkup();
				}
			}

		});

		_markupEditor.addExtendedModifyListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				if (_stackUndo.size() == UNDO_STACKSIZE)
				{
					_stackUndo.removeElementAt(0);
				}
				saveMarkup();
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
				validate();
			}
		});

		_markupEditor.addMarkupSelectionListener(new Listener()
		{

			@Override
			public void handleEvent(final Event e)
			{
				TaggingRange[] tr = (TaggingRange[]) e.data;

				if ((tr == null || tr.length == 0 || tr[0] == null || _selectedTaggingRanges == null
						|| _selectedTaggingRanges.length == 0 || !tr[0].equals(_selectedTaggingRanges[0]))
						&& (!_markupTooltip.isVisible() || !_markupShownOnAction))
				{
					_selectedTaggingRanges = tr;
					if (_selectedTaggingRanges != null && _selectedTaggingRanges.length > 0
							&& _selectedTaggingRanges[0] != null)
					{
						showMarkupInfo(_selectedTaggingRanges[0], new Point(e.x, e.y + 40));
						_markupShownOnAction = false;
					}
					else
					{
						_markupTooltip.hide();
					}
				}

			}
		});
		ViewHelper.equipWithMouseExitListener(_markupEditor.getControl(), _markupTooltip);
		_markupEditor.addTextSelectionListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				_selectedTaggingRanges = _markupEditor.getSelectedMarkups();
				Vector<String> dates = null;
				if (_dateParser != null)
				{
					dates = _dateParser.getParsedDates(_markupEditor.getSelectionText());
				}

				if (dates != null && dates.size() == 1)
				{
					String dateString = dates.firstElement();
					if (dateString.split("\"").length == 2)
					{
						// _pointOfTime = true;
						_dateFrom = new PdrDate(dateString.split("\"")[1]);
						if (!_customizedTime)
						{
							TimeStm tStm = new TimeStm();
							tStm.setType("defined");
							tStm.getTimes().add(new Time("when", "exact", _dateFrom));
							_timeStmEditor.setInput(tStm);
						}
					}
					else if (dateString.split("\"").length > 3)
					{
						// _pointOfTime = false;
						_dateFrom = new PdrDate(dateString.split("\"")[1]);

						_dateTo = new PdrDate(dateString.split("\"")[3]);
						if (!_customizedTime)
						{
							TimeStm tStm = new TimeStm();
							tStm.setType("defined");
							tStm.getTimes().add(new Time("from", "exact", _dateFrom));
							tStm.getTimes().add(new Time("to", "exact", _dateTo));
							_timeStmEditor.setInput(tStm);
						}
					}
				}

			}
		});
		_markupTooltip = new MarkupTooltip(_markupEditor.getControl());
		_markupTooltip.setShift(new Point(0, 10));
		_markupTooltip.setPopupDelay(0);
		_markupTooltip.setHideOnMouseDown(true);
		_markupTooltip.deactivate();
		createActions();
		createMenus(_markupEditor.getControl());


		_decoSText = new ControlDecoration(_markupEditor.getControl(), SWT.LEFT | SWT.TOP);

		_valEdComposite = new Composite(_group, SWT.None);
		_valEdComposite.setLayoutData(new GridData());
		((GridData) _valEdComposite.getLayoutData()).horizontalSpan = 3;

		((GridData) _valEdComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _valEdComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_valEdComposite.setLayout(new GridLayout(2, false));
		((GridLayout) _valEdComposite.getLayout()).marginHeight = 0;
		((GridLayout) _valEdComposite.getLayout()).verticalSpacing = 0;


		_timeStmEditor = new TimeStmEditorLine(EasyAspectEditor.this, null, _group, SWT.NONE);
		_timeStmEditor.setLayoutData(new GridData());
		((GridData) _timeStmEditor.getLayoutData()).horizontalAlignment = SWT.LEFT;
		((GridData) _timeStmEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _timeStmEditor.getLayoutData()).horizontalSpan = 2;

		// FIXME


		_buttonComposite = new Composite(_group, SWT.NONE);
		_buttonComposite.setLayoutData(new GridData());
		((GridData) _buttonComposite.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		((GridData) _buttonComposite.getLayoutData()).grabExcessHorizontalSpace = false;
		_buttonComposite.setLayout(new GridLayout(1, false));
		((GridLayout) _buttonComposite.getLayout()).marginHeight = 0;
		((GridLayout) _buttonComposite.getLayout()).verticalSpacing = 0;

		_addRelation = new Button(_buttonComposite, SWT.PUSH);
		_addRelation.setText(NLMessages.getString("Editor_addRelation"));
		_addRelation.setToolTipText(NLMessages.getString("Editor_add_relation_tooltip"));
		_addRelation.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
		_addRelation.setLayoutData(new GridData());
		((GridData) _addRelation.getLayoutData()).horizontalSpan = 1;
		((GridData) _addRelation.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_addRelation.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				RelationStm rStm = new RelationStm();
				rStm.setSubject(_currentPerson.getPdrId());
				Relation rel = new Relation();
				rel.setProvider(_markupProvider);
				rStm.getRelations().add(rel);
				if (_aspect.getRelationDim() == null)
				{
					_aspect.setRelationDim(new RelationDim());
					_aspect.getRelationDim().setRelationStms(new Vector<RelationStm>());
					_aspect.getRelationDim().getRelationStms().add(rStm);
				}
				else
				{
					_aspect.getRelationDim().getRelationStms().add(rStm);
				}
				loadRelation(rel, true);
				
				// rap auskommentiert alternativ
				// EasyAspectEditor.this.layout();

				resize();
				validate();
//				for (PaintListener l : EasyAspectEditor.this._paintListeners)
//				{
//					Event ee = new Event();
//					ee.widget = EasyAspectEditor.this;
//					l.paintControl(new PaintEvent(ee));
//				}

			}

		});



		_relationComposite = new Composite(_group, SWT.NONE);
		_relationComposite.setLayoutData(new GridData());
		((GridData) _relationComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _relationComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _relationComposite.getLayoutData()).horizontalSpan = 3;
		_relationComposite.setLayout(new GridLayout(1, false));
		((GridLayout) _relationComposite.getLayout()).marginHeight = 0;
		((GridLayout) _relationComposite.getLayout()).verticalSpacing = 0;

		// createActions();
		// createMenus(_sText);
		setSelected(false, true);

	}

	protected void saveMarkup()
	{
		// System.out.println("saveMarkup aspect dirty " + _aspect.isDirty());
		// if (_aspect.getRangeList() != null)
		// {
		// processStyleRanges();
		// }
		//		        System.out.println("before hex removal " + _stext.getText()); //$NON-NLS-1$
		_markupEditor.saveChanges();
		_aspect.setDirty(true);
		// _aspect.setNotification(removeInvalidHexChar(_stext.getText()));
		
	}


	protected void createMenus(final Control control)
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
		Menu menu = menuMgr.createContextMenu(control);
		fillMenu(menuMgr);

		control.setMenu(menu);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().
		getSite().registerContextMenu("org.bbaw.pdr.view.editorlite.menu", menuMgr, EasyAspectEditor.this);
	}

	private void deleteRelation(Relation rel)
	{
		_relationEditors = new ArrayList<RelationEditorLine>(3);
		for (Control c : _relationComposite.getChildren())
		{
			c.dispose();
		}
		boolean found = false;
		boolean removeStm = false;
		RelationStm relationStm =
		null;
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
		_relationEditors = new ArrayList<RelationEditorLine>(3);
		for (Control c :_relationComposite.getChildren())
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
				relEd.setSelected(true, relEd.isValid());
			}
		}
		_relationComposite.layout();
		contentChanged();


	}

	protected void fillMenu(IMenuManager rootMenuManager)
	{

		if ((_selectedTaggingRanges == null || _selectedTaggingRanges.length == 0)
				&& _facade.getConfigs().containsKey(_markupProvider))
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
		else if (_selectedTaggingRanges != null && _selectedTaggingRanges[0] != null)
		{
			Action markup = new Action(NLMessages.getString("EditorLite_show_Markup"))
			{

				@Override
				public void run()
				{
					Point p = new Point(_markupEditor.getControl().getSize().x,
							_markupEditor.getControl().getSize().y - 200);
					_markupShownOnAction = true;
					showMarkupInfo(_selectedTaggingRanges[0], p);
				}
			};
			rootMenuManager.add(markup);
			markup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP));
		}

		rootMenuManager.add(new Separator());
		if (_selectedTaggingRanges != null && _selectedTaggingRanges.length > 0)
		{
			rootMenuManager.add(_markupDelete);
		}
		rootMenuManager.add(new Separator());
		if (_markupEditor.getSelectionText() != null && _markupEditor.getSelectionText().trim().length() > 0)
		{
			rootMenuManager.add(_personFromStringAction);
		}
		rootMenuManager.add(new Separator());

		rootMenuManager.add(_undoAction);
		rootMenuManager.add(_redoAction);
		rootMenuManager.add(_insertSpecialCharAction);
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

	// /**
	// * Gets the label of config data.
	// * @param element the element
	// * @param type the type
	// * @param subtype the subtype
	// * @param role the role
	// * @return the label of config data
	// */
	// private String getLabelOfConfigData(String element, final String type,
	// final String subtype, final String role)
	// {
	// if (!element.startsWith("aodl:"))
	// {
	//			element = "aodl:" + element; //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// String label = null;
	// Vector<String> providers = new Vector<String>();
	// for (String s : _facade.getConfigs().keySet())
	// {
	// if (!s.equals(_markupProvider))
	// {
	// providers.add(s);
	// }
	// }
	// HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
	// if (element != null && type == null)
	// {
	//
	// if (_facade.getConfigs().containsKey(_markupProvider)
	// && _facade.getConfigs().get(_markupProvider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren());
	// }
	// if (configs.get(element) != null)
	// {
	// label = configs.get(element).getLabel();
	// }
	// else
	// {
	// label = element;
	// }
	// }
	// else if (element != null && type != null && subtype == null)
	// {
	//			//			System.out.println("get label for type " + type); //$NON-NLS-1$
	// for (String p : providers)
	// {
	// if (_facade.getConfigs().get(p).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren());
	// }
	// }
	//			//			System.out.println("markupprovider " + _provider); //$NON-NLS-1$
	//			//			System.out.println("config size1 " + configs.size()); //$NON-NLS-1$
	//
	// if (_facade.getConfigs().containsKey(_markupProvider)
	// && _facade.getConfigs().get(_markupProvider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren());
	// }
	//			//			System.out.println("config size2 " + configs.size()); //$NON-NLS-1$
	//
	// if (configs.get(type) != null)
	// {
	// label = configs.get(type).getLabel();
	// }
	// else
	// {
	// label = type;
	//				//			System.out.println("get label for label " + label); //$NON-NLS-1$
	// }
	//
	// }
	// else if (element != null && type != null && subtype != null && role ==
	// null)
	// {
	// for (String provider : providers)
	// {
	// if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .containsKey(type))
	// {
	// configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .get(type).getChildren());
	// }
	// }
	// if (_facade.getConfigs().containsKey(_markupProvider)
	// && _facade.getConfigs().get(_markupProvider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().containsKey(type))
	// {
	// configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
	// .getChildren());
	// }
	// if (configs.get(subtype) != null)
	// {
	// label = configs.get(subtype).getLabel();
	// }
	// else
	// {
	// label = subtype;
	// }
	// }
	// else if (element != null && type != null && subtype != null && role !=
	// null)
	// {
	// for (String provider : providers)
	// {
	// if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .containsKey(type)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
	// .getChildren() != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
	// .getChildren().containsKey(subtype))
	// {
	// configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .get(type).getChildren().get(subtype).getChildren());
	// }
	// }
	// if (_facade.getConfigs().containsKey(_markupProvider)
	// && _facade.getConfigs().get(_markupProvider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().containsKey(type)
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
	// .getChildren() != null
	// &&
	// _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
	// .getChildren().containsKey(subtype))
	// {
	// configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
	// .getChildren().get(subtype).getChildren());
	// }
	//
	// if (configs.get(role) != null)
	// {
	// label = configs.get(role).getLabel();
	// }
	// else
	// {
	// label = role;
	// }
	// }
	// return label;
	// }

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
		return _aspect.isValid();
	}

	private void loadAspect()
	{
		_relationSelectionListener = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				ee.widget = EasyAspectEditor.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = EasyAspectEditor.this;
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
		
		_group.setText(_aspect.getDisplayNameWithID());

		loadTextAndMarkup();

		if (_aspect.getValidation() == null)
		{
			_aspect.setValidation(new Validation());
		}
		if (_aspect.getValidation().getValidationStms() == null
				|| _aspect.getValidation().getValidationStms().isEmpty())
		{
			ValidationStm valStm = new ValidationStm();
			valStm.setAuthority(_facade.getCurrentUser().getPdrId());
			valStm.setReference(new Reference("certain"));

			_aspect.getValidation().getValidationStms().add(valStm);
		}
		if (_aspect.getValidation() != null && _aspect.getValidation().getValidationStms() != null
				&& !_aspect.getValidation().getValidationStms().isEmpty())
		{
			// _validationEditor.setValidationStm(_aspect.getValidation().getValidationStms().firstElement());
			loadValidations(false);
		}
		if (_aspect.getTimeDim() != null && _aspect.getTimeDim().getTimeStms() != null)
		{
			boolean definedTimefound = false;
			for (TimeStm tStm : _aspect.getTimeDim().getTimeStms())
			{
				if (tStm.getType() != null && !tStm.getType().equals("undefined") && tStm.getTimes() != null
						&& !tStm.getTimes().isEmpty())
				{
					loadTimes(tStm);
					definedTimefound = true;
					break;
				}
			}
			if (!definedTimefound)
			{
				for (TimeStm tStm : _aspect.getTimeDim().getTimeStms())
				{
					if (tStm.getType() != null && tStm.getTimes() != null && !tStm.getTimes().isEmpty())
					{
						loadTimes(tStm);
						break;
					}
				}
			}
		}
		_relationEditors = new ArrayList<RelationEditorLine>(3);
		for (Control c : _relationComposite.getChildren())
		{
			c.dispose();
		}
		if (_aspect.getRelationDim() != null && _aspect.getRelationDim().getRelationStms() != null)
		{
			for (RelationStm rStm : _aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject() != null && !rStm.getSubject().equals(_aspect.getPdrId())
						&& rStm.getRelations() != null)
				{
					for (Relation rel : rStm.getRelations())
					{
						loadRelation(rel, false);
					}
				}
			}
			_relationComposite.layout();
		}
		validate();
		resize();
		// _group.layout();
	}

	private void loadValidations(boolean selected)
	{
		boolean first = true;
		_validationEditors = new Vector<ValidationEditorLine>(2);
		for (Control c : _valEdComposite.getChildren())
		{
			c.dispose();
		}
		_validationButtons = new Vector<Button>(2);
		for (ValidationStm vStm : _aspect.getValidation().getValidationStms())
		{
			ValidationEditorLine validationEditor = new ValidationEditorLine(EasyAspectEditor.this, vStm,
					_valEdComposite, true, SWT.NONE);
			_validationEditors.add(validationEditor);
			validationEditor.setLayoutData(new GridData());
			((GridData) validationEditor.getLayoutData()).horizontalSpan = 1;
			((GridData) validationEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) validationEditor.getLayoutData()).grabExcessHorizontalSpace = true;
			validationEditor.setSelected(selected, validationEditor.isValid());
			validationEditor.setEditable(_editable);
			validationEditor.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					Event ee = new Event();
					ee.widget = EasyAspectEditor.this;
					SelectionEvent se = new SelectionEvent(ee);
					se.data = EasyAspectEditor.this;
					for (SelectionListener s : _selectionListener)
					{
						s.widgetSelected(se);
					}
				}
			});

			if (first)
			{
				first = false;
				Button addValStm = new Button(_valEdComposite, SWT.PUSH);
				addValStm.setText("+");
				addValStm.setToolTipText(NLMessages.getString("Editor_addReference"));
				addValStm.setImage(_imageReg.get(IconsInternal.REFERENCE_ADD));
				addValStm.setLayoutData(new GridData());
				((GridData) addValStm.getLayoutData()).horizontalSpan = 1;
				((GridData) addValStm.getLayoutData()).horizontalAlignment = SWT.RIGHT;

				addValStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						ValidationStm valStm = new ValidationStm();
						valStm.setAuthority(_facade.getCurrentUser().getPdrId());
						Reference ref = new Reference("certain");
						ref.setAuthority(_facade.getCurrentUser().getPdrId());
						valStm.setReference(ref);
						_aspect.getValidation().getValidationStms().add(valStm);
						loadValidations(true);
						resize();
						validate();

						// rap auskommentiert alternativ
						// EasyAspectEditor.this.layout();

//						for (PaintListener l : EasyAspectEditor.this._paintListeners)
//						{
//							Event ee = new Event();
//							ee.widget = EasyAspectEditor.this;
//							l.paintControl(new PaintEvent(ee));
//						}
					}

				});
				_validationButtons.add(addValStm);
			}
			else
			{
				final Button removeValStm = new Button(_valEdComposite, SWT.PUSH);
				removeValStm.setText("-");
				removeValStm.setToolTipText(NLMessages.getString("Editor_deleteRef"));
				removeValStm.setImage(_imageReg.get(IconsInternal.REFERENCE_REMOVE));
				removeValStm.setLayoutData(new GridData());
				((GridData) removeValStm.getLayoutData()).horizontalSpan = 1;
				((GridData) removeValStm.getLayoutData()).horizontalAlignment = SWT.RIGHT;
				removeValStm.setData(vStm);

				removeValStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						_aspect.getValidation().getValidationStms().remove((ValidationStm) removeValStm.getData());
						loadValidations(true);
						// rap auskommentiert alternativ
						// EasyAspectEditor.this.layout();
						resize();
						validate();
						
					}

				});
				_validationButtons.add(removeValStm);
			}

		}

//		_group.layout();
		_valEdComposite.layout();


		//rap auskommentiert cp
		// for (Listener l : EasyAspectEditor.this.getListeners(SWT.Paint))
		// {
		// if (l instanceof PaintListener)
		// {
		// PaintListener p = (PaintListener) l;
		// Event ee = new Event();
		// ee.widget = EasyAspectEditor.this;
		// p.paintControl(new PaintEvent(ee));
		// }
		// }
		// rap alternativ
//		for (PaintListener l : EasyAspectEditor.this._paintListeners)
//		{
//			PaintListener p = (PaintListener) l;
//			Event ee = new Event();
//			ee.widget = EasyAspectEditor.this;
//			p.paintControl(new PaintEvent(ee));
//		}
	}

	private void loadTextAndMarkup()
	{
		_markupEditor.setAspect(_aspect);
	}

	private void loadRelation(Relation rel, boolean selected)
	{
		
			
		
		RelationEditorLine relEditor = new RelationEditorLine(EasyAspectEditor.this, rel, _relationComposite, true,
				SWT.NONE);
		relEditor.setLayoutData(new GridData());
		((GridData) relEditor.getLayoutData()).horizontalSpan = 1;
		((GridData) relEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) relEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		relEditor.setSelected(selected, false);
		relEditor.setEditable(_editable);
		relEditor.addSelectionListener(_relationSelectionListener);
		relEditor.addDeleteListener(_relationDeleteListener);

		_relationEditors.add(relEditor);
//		Point point = EasyAspectEditor.this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
//		EasyAspectEditor.this.setSize(point.x, point.y);
//		EasyAspectEditor.this.layout();
	}

	private void loadTimes(TimeStm tStm)
	{
		_timeStmEditor.setInput(tStm);
		_timeStmEditor.setEditable(_editable);
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
		if (_group != null)
		{
			super.setBackground(color);
			_group.setBackground(color);
			_labelComp.setBackground(color);
			_buttonComposite.setBackground(color);
			_valEdComposite.setBackground(color);
			_relationComposite.setBackground(color);
			for (Control c : _relationComposite.getChildren())
			{
				c.setBackground(color);
				// c.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);

			}
			_textLabel.setBackground(color);
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
		if (_isDirty)
		{
			_aspect.setDirty(true);
			if (_timeStmEditor.isDirty() && !_customizedTime && !_helperStarted)
			{
				_helperStarted = true;
				Job job = new Job("timer")
				{

					@Override
					protected IStatus run(IProgressMonitor monitor)
					{
						_customizedTime = true;
						return Status.OK_STATUS;
					}
				};
				job.schedule(5000);
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
		if (_group != null)
		{
			super.setForeground(color);
			_group.setForeground(color);
			_buttonComposite.setForeground(color);
			_valEdComposite.setForeground(color);
			_relationComposite.setForeground(color);
			for (Control c : _relationComposite.getChildren())
			{
				c.setForeground(color);

			}
			_textLabel.setForeground(color);
		}
	}

	/**
	 * Sets the markup.
	 * @param cd
	 */
	protected final void setMarkup(ConfigData cd)
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
			// TODO anakeydialog einfügen

			int returnCode = 0;
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_LITE_EDIT_ANA_KEY",
					AEConstants.ASPECT_LITE_EDIT_ANA_KEY, null))
			{
				Dialog dialog = new SelectOwnerAndKeyDialog(this.getShell(), tr);
				returnCode = dialog.open();
			}
			if (returnCode < 2)
			{
				setDirty(true);
				if (_markupEditor.getSelectionText() == null || _markupEditor.getSelectionText().trim().length() == 0)
				{
					tr.setTextValue(PDRConfigProvider.getLabelOfMarkup(name, type, subtype, role));
					_markupEditor.insertContentSetMarkup(tr);
				}
				else
				{
					_markupEditor.setMarkup(tr);
				}
				_aspect.getRangeList().add(tr);

				//            System.out.println("vor sort "+ sr.start + " - " + sr.length); //$NON-NLS-1$ //$NON-NLS-2$
				// _sText.setSelection(tr.getStart() + tr.getLength());
				saveMarkup();
			}
		}
	}

	/**
	 * @param b
	 * @see org.bbaw.pdr.ae.view.editorlite.view.IAEEasyAspectEditor#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean b, boolean contextIsValid)
	{

		this._selected = b;
		_markupEditor.setSelected(b);
		if (_addRelation.isDisposed())
		{
			return;
		}
		_addRelation.setEnabled(b && _editable);
		if (!b)
		{
			saveInput();
		}


		for (ValidationEditorLine ed : _validationEditors)
		{
			ed.setSelected(b, contextIsValid);
		}
		if (_validationButtons != null)
		{
			for (Button button : _validationButtons)
			{
				button.setEnabled(b && _editable);
			}
		}
		_timeStmEditor.setSelected(b, contextIsValid);
		_symbolButton.setEnabled(b && _editable);
		if (_relationEditors != null)
		{
			for (RelationEditorLine relEd : _relationEditors)
			{
				relEd.setSelected(b, contextIsValid);
			}
		}
		if (contextIsValid && b)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		else if (b)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
		
		if (_aspect != null && !_aspect.isValid())
		{
			setValid(false);
		}

	}


	private void setValid(boolean isValid) {
		this._isValid = isValid;
		
	}

	@Override
	public void setSelection(ISelection selection)
	{
		// TODO Auto-generated method stub

	}


	private void validateInternal()
	{
		boolean isValid = false;
		
		if (!_markupEditor.isValid())
		{
			_decoSText.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
		}
		else
		{
				_decoSText.setImage(null);
		}


	}

	private void showMarkupInfo(TaggingRange tr, Point point)
	{
		String message;
		if (!tr.getName().equals("date")) //$NON-NLS-1$
		{
			message = NLMessages.getString("View_markupName")
					+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null)
					+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
			if (tr.getType() != null)
			{
				message = message + NLMessages.getString("View_type")
						+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
			if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
			{
				message = message + NLMessages.getString("View_subtype")
						+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
			if (tr.getRole() != null && tr.getRole().trim().length() > 0)
			{
				message = message + NLMessages.getString("View_role")
						+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), tr.getRole())
						+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
			if (tr.getKey() != null && tr.getKey().trim().length() > 0)
			{
				message = message + NLMessages.getString("View_key") + tr.getKey(); //$NON-NLS-1$
				PdrObject o = _facade.getPdrObject(new PdrId(tr.getKey()));
				if (o != null)
				{
					message = message + " " + o.getDisplayName(); //$NON-NLS-1$
				}
				else
				{
					message = message + NLMessages.getString("View_message_missing_dataObject");
				}
			}
			if (tr.getAna() != null && tr.getAna().trim().length() > 0)
			{
				message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
				PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
				if (o != null)
				{
					message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
				}
				else
				{
					message = message + NLMessages.getString("View_message_missing_dataObject");
				}
			}
			message = message + "\n" + NLMessages.getString("View_lb_content") + tr.getTextValue();
		}
		else if (tr.getName().equals("date")) //$NON-NLS-1$
		{
			message = NLMessages.getString("View_MarkupDate")
					+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null)
					+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
			if (tr.getType() != null)
			{
				message = message + NLMessages.getString("View_type")
						+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
			if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
			{
				message = message + NLMessages.getString("View_subtype")
						+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
			}
			if (tr.getWhen() != null)
			{
				message = message + NLMessages.getString("View_when") + tr.getWhen().toString(); //$NON-NLS-1$

			}
			if (tr.getFrom() != null)
			{
				message = message + NLMessages.getString("View_from") + tr.getFrom().toString(); //$NON-NLS-1$

			}
			if (tr.getTo() != null)
			{
				message = message + "\n" + NLMessages.getString("View_to") + tr.getTo().toString(); //$NON-NLS-1$

			}
			if (tr.getNotBefore() != null)
			{
				message = message + NLMessages.getString("View_notBefore") + tr.getNotBefore().toString(); //$NON-NLS-1$

			}
			if (tr.getNotAfter() != null)
			{
				message = message + "\n" + NLMessages.getString("View_NotAfter") + tr.getNotAfter().toString(); //$NON-NLS-1$

			}
			if (tr.getAna() != null && tr.getAna().trim().length() > 0)
			{
				message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
				PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
				if (o != null)
				{
					message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
				}
				else
				{
					message = message + NLMessages.getString("View_message_missing_dataObject");
				}
			}
			message = message + "\n" + NLMessages.getString("View_lb_content") + tr.getTextValue();
		}
		else
		{
			message = NLMessages.getString("View_errorMarkupInfo"); //$NON-NLS-1$
		}
		//	    							MessageDialog.openInformation(parentShell, "", message); //$NON-NLS-1$
		// break;
		_markupTooltip.setToolTipText(message);
		// FIXME erneuern.

		_markupTooltip.activate();
		_markupTooltip.show(new Point(point.x + 5, point.y + 10));

	}
	private void contentChanged()
	{
		setDirty(true);
		saveInput();
		validateInternal();
		validate();
	}

	@Override
	public void validate()
	{
		if (_aspect.getValidation() == null)
		{
			_aspect.setValidation(new Validation());
		}
		for (ValidationEditorLine ed : _validationEditors)
		{
			if (_aspect.getValidation().getValidationStms().contains(ed.getValidationStm()))
			{
				_aspect.getValidation().getValidationStms().remove(ed.getValidationStm());
			}
			_aspect.getValidation().getValidationStms().add(ed.getValidationStm());
		}
		if (_aspect.getTimeDim() == null)
		{
			_aspect.setTimeDim(new TimeDim());
			_aspect.getTimeDim().setTimeStms(new Vector<TimeStm>());

		}
		if (_aspect.getTimeDim().getTimeStms().contains(_timeStmEditor.getTimeStm()))
		{
			if (_timeStmEditor.isDirty())
			{
				_aspect.getTimeDim().getTimeStms().remove(_timeStmEditor.getTimeStm());
			}
		}
		if (_timeStmEditor.isDirty())
		{
			_aspect.getTimeDim().getTimeStms().add(_timeStmEditor.getTimeStm());
		}

		// FIXME relations
		setValid(_aspect.isValid());
		// System.out.println("validate easyaspecteditor " + _aspect.isValid());

		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}

	}

	public void setLayoutData(LayoutData layoutData)
	{
		super.setLayoutData(layoutData);
	}

	@Override
	public void saveInput()
	{
		if (_aspect != null && _markupEditor != null)
		{
			saveMarkup();
		}

	}

	public void setInput(Object input)
	{
		if (input instanceof Aspect)
		{
		this._aspect = (Aspect) input;
		loadAspect();
		}
	}

	@Override
	public PdrObject getOwningObject() {
		return _currentPerson;
	}

	@Override
	public void addCustomPaintListener(PaintListener paintListener)
	{
		//rap auskommentiert cp
		// addPaintListener(_paintListener);
		// rap alternativ
		if (paintListener != null && !_paintListeners.contains(paintListener))
		{
			_paintListeners.add(paintListener);
		}

	}

	@Override
	public void resize()
	{
		Point point = EasyAspectEditor.this.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		EasyAspectEditor.this.setSize(850, point.y);
		for (PaintListener l : EasyAspectEditor.this._paintListeners)
		{
			Event ee = new Event();
			ee.widget = EasyAspectEditor.this;
			l.paintControl(new PaintEvent(ee));
		}
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
		_markupEditor.setEditable(_editable);
		_addRelation.setEnabled(_selected && _editable);

		for (ValidationEditorLine ed : _validationEditors)
		{
			ed.setEnabled(_editable);
		}
		if (_validationButtons != null)
		{
			for (Button button : _validationButtons)
			{
				button.setEnabled(_selected && _editable);
			}
		}
		_timeStmEditor.setEnabled(_editable);
		_symbolButton.setEnabled(_selected && _editable);
		if (_relationEditors != null)
		{
			for (RelationEditorLine relEd : _relationEditors)
			{
				relEd.setEnabled(_editable);
			}
		}
	}
}

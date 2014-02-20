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
package org.bbaw.pdr.ae.view.control.customSWTWidges;

import java.util.ArrayList;

import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

public class ValidationEditorLine extends Composite implements IAEBasicEditor
{
	private ValidationStm _validationStm;

	private Composite _composite;

	private Button _searchButton;
	private Button _newButton;

	private Text _sourceText;

	private Text _sourcePageText;

	private Label _labelSource;

	private Label _labelInternal;

	private boolean _isValid = true;
	private boolean _isDirty = false;

	private ControlDecoration _decoValId;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	private Facade _facade = Facade.getInstanz();

	private IAEBasicEditor _parentEditor;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListener = new ArrayList<SelectionListener>();

	private boolean _showLabel;

	private Button _editRefButton;

	private boolean _selected;

	private boolean _editable = true;

	public ValidationEditorLine(IAEBasicEditor parentEditor, ValidationStm validationStm, Composite parent,
			boolean showLabel, int style)
	{
		super(parent, style);
		_parentEditor = parentEditor;
		_validationStm = validationStm;
		_showLabel = showLabel;
		createEditor();
		if (_validationStm == null)
		{
			_validationStm = new ValidationStm();
			_validationStm.setReference(new Reference());
			_validationStm.setAuthority(_facade.getCurrentUser().getPdrId());
			setValidationQuality();
		}
		else
		{
			loadValidation();
		}
		validateInternal();
		// _composite.pack();
		_composite.layout();
		
		// this.setSize(SWT.DEFAULT, 18);
		// this.pack();
		this.layout();
	}

	public final void addSelectionListener(final SelectionListener listener)
	{
		if (listener != null)
		{
			_selectionListener.add(listener);
		}

	}

	private void createEditor()
	{
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;

		_composite = new Composite(this, SWT.NONE);
		_composite.setLayoutData(new GridData());
		((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
		_composite.setLayout(new GridLayout(12, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		if (_showLabel)
		{
			((GridLayout) _composite.getLayout()).numColumns = 13;

			_labelSource = new Label(_composite, SWT.NONE);
			_labelSource.setText(NLMessages.getString("Editor_reference") + "*");
			_labelSource.setLayoutData(new GridData());
			((GridData) _labelSource.getLayoutData()).horizontalSpan = 1;
			((GridData) _labelSource.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		}
		_sourceText = new Text(_composite, SWT.BORDER);
		_sourceText.setBackground(WHITE_COLOR);
		_sourceText.setLayoutData(new GridData());
		((GridData) _sourceText.getLayoutData()).horizontalSpan = 5;
		((GridData) _sourceText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _sourceText.getLayoutData()).grabExcessHorizontalSpace = true;

		_sourceText.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = ValidationEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
					ContentProposalAdapter adapter = new ContentProposalAdapter(_sourceText, new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getAllReferenceFacets()), null,
 null);
					adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
					adapter.addContentProposalListener(new IContentProposalListener()
					{

						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_sourceText.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
							if (_validationStm.getReference() == null)
							{
								_validationStm.setReference(new Reference());
							}
								_validationStm.getReference().setSourceId(new PdrId(((Facet) proposal).getKey()));
								_decoValId.setImage(null);
								contentChanged();

							}
						}
					});
				// }
				// catch (org.eclipse.jface.bindings.keys.ParseException e1)
				// {
				//
				// e1.printStackTrace();
				// }

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_sourceText.getText() != null && _sourceText.getText().trim().length() == 23)
				{
					PdrId id = new PdrId(_sourceText.getText());
					PdrObject o = _facade.getPdrObject(id);
					if (o != null)
					{
						_validationStm.getReference().setSourceId(id);
						_decoValId.setImage(null);
					}

				}
				if (_validationStm.getReference().getSourceId() != null)

				{
					if (_facade.getReference(_validationStm.getReference().getSourceId()) != null)
					{
						_decoValId.setDescriptionText("");
						_decoValId.setImage(null);
					}
					else
					{
						_decoValId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						_decoValId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
				if (_validationStm.getReference().getQuality() == null)
				{
					_validationStm.getReference().setQuality("certain");
				}
				contentChanged();
			}
		});
		_sourceText.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				if (_sourceText.getText().length() == 23)
				{
					PdrObject o = _facade.getReference(new PdrId(_sourceText.getText()));
					if (o != null)
					{
						_decoValId.setImage(null);
						_validationStm.getReference().setSourceId(new PdrId(_sourceText.getText()));
						_sourceText.setText(o.getDisplayNameWithID());
					}
					else
					{
						_validationStm.getReference().setSourceId(null);
						_decoValId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					}
				}
				else if (_sourceText.getText().trim().length() == 0)
				{
					_validationStm.getReference().setSourceId(null);
					_decoValId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();
			}
		});

		_decoValId = new ControlDecoration(_sourceText, SWT.LEFT | SWT.TOP);

		_searchButton = new Button(_composite, SWT.NONE);
		_searchButton.setToolTipText(NLMessages.getString("Editor_linkWithSource"));
		_searchButton.setImage(_imageReg.get(IconsInternal.SEARCH));
		_searchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{

				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				SelectObjectDialog dialog = new SelectObjectDialog(shell, 2);
				dialog.open();
				if (_facade.getRequestedId() != null)
				{
					_validationStm.getReference().setSourceId(_facade.getRequestedId());
					if (_validationStm.getReference().isValidId())
					{
						_decoValId.setImage(null);
						PdrObject o = _facade.getReference(_validationStm.getReference().getSourceId());
						if (o != null)
						{
							_sourceText.setText(o.getDisplayNameWithID()); //$NON-NLS-1$
						}
					}
				}
				else
				{
					if (!_validationStm.getReference().isValidId())
					{
						_decoValId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						_sourceText.setText("");
					}
				}
				contentChanged();
			}
		});
		_editRefButton = new Button(_composite, SWT.PUSH);
		_editRefButton.setImage(_imageReg.get(IconsInternal.REFERENCE_EDIT));
		_editRefButton.setLayoutData(new GridData());
		_editRefButton.setToolTipText(NLMessages.getString("Editor_edit_reference_tooltip")); //$NON-NLS-1$

		_editRefButton.pack();
		
		_editRefButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_validationStm.getReference() != null && _validationStm.getReference().getSourceId() != null)
				{
					ReferenceMods ref = _facade.getReference(_validationStm.getReference().getSourceId());
					if (ref != null)
					{
						_facade.setCurrentReference(ref);

						IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
								.getService(IHandlerService.class);
						try
						{
							handlerService.executeCommand(
									"org.bbaw.pdr.ae.view.main.commands.OpenSourceEditorDialog", null); //$NON-NLS-1$
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
				}

			}
		});
		_newButton = new Button(_composite, SWT.NONE);
		_newButton.setToolTipText(NLMessages.getString("Editor_create_new_ref_tip"));
		_newButton.setImage(_imageReg.get(IconsInternal.REFERENCE_NEW));
		_newButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				Event ee = new Event();
				ee.widget = ValidationEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.NewReference", null); //$NON-NLS-1$
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
				if (_facade.getCurrentReference() != null)
				{
					_validationStm.getReference().setSourceId(_facade.getCurrentReference().getPdrId());
					_sourceText.setText(_facade.getReference(_validationStm.getReference().getSourceId())
							.getDisplayNameWithID()); //$NON-NLS-1$
				}
				if (_validationStm.getReference().isValidId())
				{
					_decoValId.setImage(null);
				}
				else
				{
					_decoValId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();

			}
		});

		_labelInternal = new Label(_composite, SWT.NONE);
		_labelInternal.setText(NLMessages.getString("Editor_internal"));
		_labelInternal.setLayoutData(new GridData());
		((GridData) _labelInternal.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelInternal.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_sourcePageText = new Text(_composite, SWT.BORDER);
		_sourcePageText.setLayoutData(new GridData());
		((GridData) _sourcePageText.getLayoutData()).horizontalSpan = 3;
		((GridData) _sourcePageText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _sourcePageText.getLayoutData()).grabExcessHorizontalSpace = true;
		_sourcePageText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = ValidationEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
				String[] vals = new String[]
				{"test"};
				try
				{
					vals = _facade.getMainSearcher().getFacets("validation", "internal", null, null, //$NON-NLS-1$
							null);
				}
				catch (Exception e1)
				{

					e1.printStackTrace();
				}
				new AutoCompleteField(_sourcePageText, new TextContentAdapter(), vals);
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if (_sourcePageText.getText() != null && _sourcePageText.getText().trim().length() > 0)
				{
					_validationStm.getReference().setInternal(_sourcePageText.getText());
				}
				contentChanged();

			}
		});


	}

	public ValidationStm getValidationStm()
	{
		return _validationStm;
	}

	@Override
	public boolean isDirty()
	{
		return _isDirty;
	}

	@Override
	public boolean isValid()
	{
		return (_validationStm.isValid());
	}
	private void loadValidation()
	{
		if (_validationStm.getReference() != null)
		{
			if (_validationStm.getReference().getSourceId() != null)
			{
				PdrObject o = _facade.getPdrObject(_validationStm.getReference().getSourceId());
				if (o != null)
				{
					_sourceText.setText(o.getDisplayName());
					_decoValId.setImage(null);
				}
				else
				{
					_sourceText.setText(_validationStm.getReference().getSourceId().toString());
					_decoValId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					_decoValId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
				}
			}
			if (_validationStm.getReference().getInternal() != null)
			{
				_sourcePageText.setText(_validationStm.getReference().getInternal());
			}
		}
		else
		{
			_sourceText.setText("");
			_decoValId.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
		}

	}

	@Override
	public void setBackground(Color color)
	{
		if (!super.isDisposed())
		{
			super.setBackground(color);
			_composite.setBackground(color);
			if (_showLabel)
			{
				_labelSource.setBackground(color);
			}
			_labelInternal.setBackground(color);
		}
	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_isDirty && _parentEditor != null)
		{
			_parentEditor.setDirty(isDirty);
		}
	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		if (_showLabel)
		{
			_labelSource.setForeground(color);
		}
		_labelInternal.setForeground(color);
		_sourceText.setForeground(color);
		_sourcePageText.setForeground(color);
	}

	@Override
	public void setLayoutData(Object layoutData)
	{
		super.setLayoutData(layoutData);
		// if (_composite != null) {
		// _composite.setLayoutData(layoutData);
		// }

	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		_sourceText.setEditable(_selected && _editable);
		_sourcePageText.setEditable(_selected && _editable);
		_newButton.setEnabled(isSelected && _editable);
		_searchButton.setEnabled(isSelected && _editable);
		_editRefButton.setEnabled(isSelected && _editable);
		if (isSelected && contextIsValid)
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
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}

	}
	private void contentChanged()
	{
		setDirty(true);
		saveInput();
		validateInternal();
		validate();
	}
	private void validateInternal() {
		setValid(_validationStm.isValid());
		
	}

	private void setValid(boolean isValid)
	{
		this._isValid = isValid;

	}

	private void setValidationQuality()
	{
		if (_validationStm != null
				&& _validationStm.getReference() != null
				&& (_validationStm.getReference().getQuality() == null
				|| !(_validationStm.getReference().getQuality().equals("certain")
						|| _validationStm.getReference().getQuality().equals("probable") || _validationStm
						.getReference().getQuality().equals("unsure"))))
		{
			_validationStm.getReference().setQuality("certain");
		}

	}

	public void setInput(Object input)
	{
		if (input instanceof ValidationStm)
		{
		this._validationStm = (ValidationStm) input;
		setValidationQuality();
		loadValidation();

		}

	}

	@Override
	public void validate()
	{
		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}

	}

	@Override
	public void saveInput()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable = editable;
		_sourceText.setEditable(_selected && _editable);
		_sourcePageText.setEditable(_selected && _editable);
		_newButton.setEnabled(_selected && editable);
		_searchButton.setEnabled(_selected && editable);
		_editRefButton.setEnabled(_selected && editable);
		
	}
}

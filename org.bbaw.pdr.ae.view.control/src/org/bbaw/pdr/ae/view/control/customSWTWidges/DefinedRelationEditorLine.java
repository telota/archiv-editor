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
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class DefinedRelationEditorLine extends Composite implements IAEBasicEditor
{
	private Relation _relation;

	private Composite _composite;

	private Button _searchButton;

	private Text _personText;

	private Label _labelRelation;

	private Label _labelType;

	private ControlDecoration _decoPersonId;

	private boolean _isDirty = false;

	private boolean _isValid = true;

	private IAEBasicEditor _parentEditor;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	private Facade _facade = Facade.getInstanz();

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

	/** The _selection listener. */
	private ArrayList<Listener> _deleteListener = new ArrayList<Listener>();

	private boolean _showLabel;

	private boolean _editable = true;

	private boolean _selected;

	public DefinedRelationEditorLine(IAEBasicEditor parentEditor, Relation relation, Composite parent,
			boolean showLabel, int style)
	{
		super(parent, style);
		_relation = relation;
		_parentEditor = parentEditor;
		_showLabel = showLabel;
		createEditor();
		if (_relation == null)
		{
			_relation = new Relation();
		}
		loadRelation();
		// _composite.pack();
		_composite.layout();
		// this.setSize(SWT.DEFAULT, 18);
		// this.pack();
		this.layout();
	}

	public final void addDeleteListener(final Listener listener)
	{
		if (listener != null)
		{
			_deleteListener.add(listener);
		}

	}

	public final void addSelectionListener(final SelectionListener listener)
	{
		if (listener != null)
		{
			_selectionListeners.add(listener);
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
		_composite.setLayout(new GridLayout(4, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		if (_showLabel)
		{
			((GridLayout) _composite.getLayout()).numColumns = 5;
			_labelType = new Label(_composite, SWT.NONE);
			_labelType.setText(NLMessages.getString("Dialog_relation"));
			_labelType.setLayoutData(new GridData());
			((GridData) _labelType.getLayoutData()).horizontalSpan = 1;
			((GridData) _labelType.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		}

		_labelRelation = new Label(_composite, SWT.NONE);
		_labelRelation.setText(NLMessages.getString("Dialog_person"));
		_labelRelation.setLayoutData(new GridData());
		((GridData) _labelRelation.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelRelation.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_personText = new Text(_composite, SWT.BORDER);
		_personText.setBackground(WHITE_COLOR);
		_personText.setLayoutData(new GridData());
		((GridData) _personText.getLayoutData()).horizontalSpan = 1;
		((GridData) _personText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _personText.getLayoutData()).grabExcessHorizontalSpace = true;

		_decoPersonId = new ControlDecoration(_personText, SWT.LEFT | SWT.TOP);

		_personText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

				Event ee = new Event();
				ee.widget = DefinedRelationEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}

				ContentProposalAdapter adapter = new ContentProposalAdapter(_personText, new TextContentAdapter(),
						new FacetContentProposalProvider(_facade.getAllPersonsFacets()), null, null);
				adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
				adapter.addContentProposalListener(new IContentProposalListener()
				{
					@Override
					public void proposalAccepted(final IContentProposal proposal)
					{
						_personText.setText(proposal.getContent());
						if (((Facet) proposal).getKey() != null)
						{
							_relation.setObject(new PdrId(((Facet) proposal).getKey()));
							_decoPersonId.setImage(null);
							contentChanged();
						}
					}
				});

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_relation != null && _relation.getObject() == null)
				{
					if (_personText.getText() != null && _facade.getPdrObject(new PdrId(_personText.getText())) != null)
					{
						_relation.setObject(new PdrId(_personText.getText()));
						_decoPersonId.setDescriptionText("");
						_decoPersonId.setImage(null);
					}
					else
					{
						_relation.setObject(null);
						_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						_decoPersonId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
				else if (_personText.getText().trim().length() == 0)
				{
					_relation.setObject(null);
					_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();

			}
		});

		_personText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				if (_personText.getText().length() == 23)
				{
					PdrObject o = _facade.getPdrObject(new PdrId(_personText.getText()));
					if (o != null)
					{
						_decoPersonId.setImage(null);
						_relation.setObject(new PdrId(_personText.getText()));
						_personText.setText(o.getDisplayNameWithID());
					}
				}
				else if (_personText.getText().trim().length() == 0)
				{
					_relation.setObject(null);
					_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();
			}
		});

		_searchButton = new Button(_composite, SWT.NONE);
		_searchButton.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_object_tip"));
		_searchButton.setImage(_imageReg.get(IconsInternal.SEARCH));
		_searchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				SelectObjectDialog dialog = new SelectObjectDialog(shell, 1);
				dialog.open();
				if (_facade.getRequestedId() != null)
				{
					_relation.setObject(_facade.getRequestedId());
					PdrObject o = _facade.getPdrObject(_relation.getObject());
					if (o != null)
					{
						_personText.setText(o.getDisplayNameWithID());
					}
					else
					{
						_personText.setText(_facade.getRequestedId().toString());
					}
					if (_relation.getObject().isValid())
					{
						_decoPersonId.setImage(null);
					}
				}
				else
				{
					if (!_relation.getObject().isValid())
					{
						_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					}
				}
				contentChanged();

			}
		});
	}

	public Relation getRelation()
	{
		if (_personText.getText() != null && _personText.getText().trim().length() > 0)
		{
			PdrId id = new PdrId(_personText.getText());
			PdrObject o = _facade.getPdrObject(id);
			if (o != null)
			{
				_relation.setObject(id);
			}
			// context, class
		}
		return _relation;
	}

	@Override
	public boolean isDirty()
	{
		return _isDirty;
	}

	@Override
	public boolean isValid()
	{
		return _isValid;
	}

	private void loadRelation()
	{

		if (_relation != null && _relation.getObject() != null)
		{
			PdrObject o = _facade.getPdrObject(_relation.getObject());
			if (o != null)
			{
				_personText.setText(o.getDisplayName());
			}
			else
			{
				_personText.setText(_relation.getObject().toString());
				_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				_decoPersonId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
			}
		}

		else
		{
			_personText.setText("");
			_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
		}

		// context, class

		_composite.redraw();
		_composite.layout();
		_composite.update();
	}

	@Override
	public void setBackground(Color color)
	{
		if (!super.isDisposed())
		{
			super.setBackground(color);
		}
		if (!_composite.isDisposed())
		{
			_composite.setBackground(color);
			if (_showLabel)
			{
				_labelType.setBackground(color);
			}
			_labelRelation.setBackground(color);
		}

	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_parentEditor != null && _isDirty)
		{
			_parentEditor.setDirty(true);
		}

	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		if (_showLabel)
		{
			_labelType.setForeground(color);
		}
		_labelRelation.setForeground(color);
		_personText.setForeground(color);
	}

	@Override
	public void setLayoutData(Object layoutData)
	{
		super.setLayoutData(layoutData);
		// if (_composite != null) {
		// _composite.setLayoutData(layoutData);
		// }

	}

	public void setInput(Object input)
	{
		if (input instanceof Relation)
		{
			this._relation = (Relation) input;
			loadRelation();
		}
	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		_searchButton.setEnabled(isSelected && _editable);
		if (isSelected && contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			if (!_composite.isDisposed())
			{
				_decoPersonId.setImage(null);
				_decoPersonId.setDescriptionText(null);
			}
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			if (!_composite.isDisposed())
			{
				_decoPersonId.setImage(null);
				_decoPersonId.setDescriptionText(null);
			}
		}
		else if (isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			if (!_composite.isDisposed())
			{
				_decoPersonId.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				_decoPersonId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
			}
		}
	}

	
	private void setValid(boolean isValid)
	{
		this._isValid = isValid;
	}
	private void validateInternal()
	{
		setValid(_relation.isValid());
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
		this._editable  = editable;
		_searchButton.setEnabled(_selected && _editable);
		_personText.setEditable(_editable);

	}
}

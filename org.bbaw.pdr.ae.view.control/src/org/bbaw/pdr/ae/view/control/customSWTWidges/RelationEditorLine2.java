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
import java.util.HashMap;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class RelationEditorLine2 extends Composite implements IAEBasicEditor
{
	private Relation _relation;

	private Composite _composite;

	private Button _searchButton;

	private Text _personText;

	private Label _labelRelation;

	private Label _labelType;

	private Combo _relationContextCombo;

	private Combo _relationClassCombo;

	private Combo _relValueCombo;

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

	private ComboViewer _relationContextComboViewer;

	private ComboViewer _relationClassComboViewer;

	private ComboViewer _relValueComboViewer;

	private boolean _editable = true;

	private boolean _selected;

	public RelationEditorLine2(IAEBasicEditor parentEditor, Relation relation, Composite parent, boolean showLabel,
			int style)
	{
		super(parent, style);
		_relation = relation;
		_parentEditor = parentEditor;
		_showLabel = showLabel;
		if (_relation == null)
		{
			_relation = new Relation();
			_relation.setProvider(Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
							AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase());
		}
		createEditor();
		
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
		_composite.setLayout(new GridLayout(10, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;
		new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = RelationEditorLine2.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = RelationEditorLine2.this;
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				contentChanged();

			}

		};
		if (_showLabel)
		{
			((GridLayout) _composite.getLayout()).numColumns = 11;
			_labelType = new Label(_composite, SWT.NONE);
			_labelType.setText(NLMessages.getString("Dialog_relation"));
			_labelType.setLayoutData(new GridData());
			((GridData) _labelType.getLayoutData()).horizontalSpan = 1;
			((GridData) _labelType.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		}

		_relationContextCombo = new Combo(_composite, SWT.READ_ONLY);
		_relationContextCombo.setBackground(WHITE_COLOR);
		_relationContextCombo.setLayoutData(new GridData());
		((GridData) _relationContextCombo.getLayoutData()).horizontalSpan = 2;
		((GridData) _relationContextCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _relationContextCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		_relationContextComboViewer = new ComboViewer(_relationContextCombo);
		_relationContextComboViewer.setContentProvider(new MarkupContentProvider());
		_relationContextComboViewer.setLabelProvider(new MarkupLabelProvider());
		
		final ControlDecoration relContextDeco = new ControlDecoration(_relationContextCombo, SWT.RIGHT | SWT.TOP);
//		_relationContextCombo.addFocusListener(focusListener);
		_relationClassCombo = new Combo(_composite, SWT.READ_ONLY);
		_relationClassCombo.setBackground(WHITE_COLOR);
		_relationClassCombo.setLayoutData(new GridData());
		((GridData) _relationClassCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) _relationClassCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _relationClassCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		_relationClassComboViewer = new ComboViewer(_relationClassCombo);
		_relationClassComboViewer.setContentProvider(new MarkupContentProvider());
		_relationClassComboViewer.setLabelProvider(new MarkupLabelProvider());
		final ControlDecoration relClassDeco = new ControlDecoration(_relationClassCombo, SWT.RIGHT | SWT.TOP);
//		_relationClassCombo.addFocusListener(focusListener);

		_relValueCombo = new Combo(_composite, SWT.BORDER | SWT.READ_ONLY);
		_relValueCombo.setBackground(WHITE_COLOR);
		_relValueCombo.setLayoutData(new GridData());
		((GridData) _relValueCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _relValueCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _relValueCombo.getLayoutData()).horizontalSpan = 1;
		_relValueComboViewer = new ComboViewer(_relValueCombo);
		_relValueComboViewer.setContentProvider(new MarkupContentProvider());
		_relValueComboViewer.setLabelProvider(new MarkupLabelProvider());
		final ControlDecoration relValueDeco = new ControlDecoration(_relValueCombo, SWT.RIGHT | SWT.TOP);
//		_relValueCombo.addFocusListener(focusListener);

		
		_relationContextComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_relation.setContext(cd.getValue());
				_relationClassCombo.removeAll();
				_relValueCombo.removeAll();
				relContextDeco.setDescriptionText(PDRConfigProvider.readDocu(_relation.getProvider(), "relation",
						"context", _relation.getContext(), null, null, null));
				if (relContextDeco.getDescriptionText() != null)
				{
					relContextDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					relContextDeco.setImage(null);
				}
				_relationClassComboViewer.setInput(_facade.getConfigs().get(_relation.getProvider()).getChildren()
						.get("aodl:relation").getChildren().get(_relation.getContext()).getChildren());
				contentChanged();
			}

		});
		_relationContextCombo.layout();

		// control for relationContextCombo
		
		_relationClassComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_relation.setRClass(cd.getValue());
				_relValueComboViewer.setInput(_facade.getConfigs().get(_relation.getProvider()).getChildren()
						.get("aodl:relation").getChildren().get(_relation.getContext()).getChildren()
						.get(_relation.getRClass()).getChildren());
				relClassDeco.setDescriptionText(PDRConfigProvider.readDocu(_relation.getProvider(), "relation",
						"class", _relation.getContext(), _relation.getRClass(), null, null));
				if (relClassDeco.getDescriptionText() != null)
				{
					relClassDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					relClassDeco.setImage(null);
				}
				contentChanged();
			}
		});
		
		
		_relValueComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_relation.setRelation(cd.getValue());
				relValueDeco.setDescriptionText(PDRConfigProvider.readDocu(_relation.getProvider(), "relation",
						"value", _relation.getContext(), _relation.getRClass(), _relation.getRelation(), null));
				if (relValueDeco.getDescriptionText() != null)
				{
					relValueDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					relValueDeco.setImage(null);
				}
				contentChanged();
			}
		});
		

		_labelRelation = new Label(_composite, SWT.NONE);
		_labelRelation.setText(NLMessages.getString("Editor_space_withPerson"));
		_labelRelation.setLayoutData(new GridData());
		((GridData) _labelRelation.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelRelation.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_personText = new Text(_composite, SWT.BORDER);
		_personText.setBackground(WHITE_COLOR);
		_personText.setLayoutData(new GridData());
		((GridData) _personText.getLayoutData()).horizontalSpan = 4;
		((GridData) _personText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _personText.getLayoutData()).grabExcessHorizontalSpace = true;

		_decoPersonId = new ControlDecoration(_personText, SWT.LEFT | SWT.TOP);

		_personText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

				Event ee = new Event();
				ee.widget = RelationEditorLine2.this;
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
				validate();

			}
		});
		
		// _composite.layout();
		// _composite.pack();
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

		if (_relation != null && _facade.getConfigs().get(_relation.getProvider()) != null)
		{
			_relationContextComboViewer.setInput(_facade.getConfigs().get(_relation.getProvider()).getChildren()
					.get("aodl:relation").getChildren());
		}
		if (_relation != null && _relation.getContext() != null)
		{
			setComboViewerByString(_relationContextComboViewer, _relation.getContext());
		}
		if (_relation != null && _relation.getRClass() != null)
		{
			if (_relation.getContext() != null
					&& _facade.getConfigs().get(_relation.getProvider()) != null
					&& _facade.getConfigs().get(_relation.getProvider()).getChildren().get("aodl:relation") != null
					&& _facade.getConfigs().get(_relation.getProvider()).getChildren().get("aodl:relation")
							.getChildren().get(_relation.getContext()) != null)
			{
				_relationClassComboViewer.setInput(_facade.getConfigs().get(_relation.getProvider()).getChildren()
						.get("aodl:relation").getChildren().get(_relation.getContext()).getChildren());
			}
			setComboViewerByString(_relationClassComboViewer, _relation.getRClass());
		}
		else
		{
			_relationClassComboViewer.setInput(null);
			_relationClassComboViewer.refresh();
		}
		_relationClassCombo.layout();
		if (_relation != null && _relation.getRelation() != null)
		{
			if (_relation.getRClass() != null
					&& _facade.getConfigs().get(_relation.getProvider()) != null
					&& _facade.getConfigs().get(_relation.getProvider()).getChildren().get("aodl:relation") != null
					&& _facade.getConfigs().get(_relation.getProvider()).getChildren().get("aodl:relation")
							.getChildren().get(_relation.getContext()) != null
					&& _facade.getConfigs().get(_relation.getProvider()).getChildren().get("aodl:relation")
							.getChildren().get(_relation.getContext()).getChildren().get(_relation.getRClass()) != null)
			{
				_relValueComboViewer.setInput(_facade.getConfigs().get(_relation.getProvider()).getChildren()
						.get("aodl:relation").getChildren().get(_relation.getContext()).getChildren()
						.get(_relation.getRClass()).getChildren());
			}
			setComboViewerByString(_relValueComboViewer, _relation.getRelation());
		}
		else
		{
			_relValueComboViewer.setInput(null);
			_relValueComboViewer.refresh();
		}
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
				_labelType.setForeground(color);
			}
			_labelRelation.setBackground(color);
		}

	}

	/**
	 * Sets the combo viewer by string.
	 * @param cv the cv
	 * @param s the s
	 */
	private void setComboViewerByString(final ComboViewer cv, final String s)
	{
		if (cv.getInput() instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
			if (inputs.containsKey(s))
			{
				// System.out.println("contains key s " + s);
				for (String key : inputs.keySet())
				{
					if (key.equals(s))
					{
						ConfigData cd = inputs.get(key);
						if (cd instanceof ConfigItem && ((ConfigItem) cd).isIgnore())
						{
							((ConfigItem) cd).setReadAlthoughIgnored(true);
							cv.setInput(inputs);
						}
						cv.setSelection(new StructuredSelection(cd));
						return;
					}
				}
			}
		}
		ConfigItem ci = new ConfigItem();
		ci.setValue(s);
		ci.setLabel(s);
		cv.add(ci);
		StructuredSelection selection = new StructuredSelection(ci);
		cv.setSelection(selection);

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
		_searchButton.setEnabled(_selected && _editable);
		_relationContextCombo.setEnabled(_selected && _editable);
		_relationClassCombo.setEnabled(_selected && _editable);
		_relValueCombo.setEnabled(_selected && _editable);
		_personText.setEnabled(_selected && _editable);
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
		setValid(_relation.isValid());
		if (_isValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			if (!_composite.isDisposed())
			{
				_decoPersonId.setImage(null);
				_decoPersonId.setDescriptionText(null);
			}
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
		_relationContextCombo.setEnabled(_selected && _editable);
		_relationClassCombo.setEnabled(_selected && _editable);
		_relValueCombo.setEnabled(_selected && _editable);
		_personText.setEnabled(_selected && _editable);
	}
}

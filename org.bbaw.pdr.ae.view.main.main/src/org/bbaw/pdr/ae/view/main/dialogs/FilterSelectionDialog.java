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
package org.bbaw.pdr.ae.view.main.dialogs;

import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.filters.AspectPersonFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectSemanticFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectYearFilter;
import org.bbaw.pdr.ae.view.control.filters.PdrObjectUserFilter;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Class creates login dialog for entering the repository settings.
 * @author cplutte
 */
public class FilterSelectionDialog extends TitleAreaDialog
{
	/** singleton instace of facade. */
	private Facade _facade = Facade.getInstanz();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The pdr objects provider. */
	private PDRObjectsProvider _pdrObjectsProvider;

	/** The type. */
	private String _type;

	/** The main composite. */
	private Composite _mainComposite;

	/** The scroll comp. */
	private ScrolledComposite _scrollComp;

	/** The ref filter. */
	private AspectReferenceFilter _refFilter;

	/** The sem filter. */
	private AspectSemanticFilter _semFilter;

	/** The user filter. */
	private PdrObjectUserFilter _userFilter;

	/** The person filter. */
	private AspectPersonFilter _personFilter;

	/** The year filter. */
	private AspectYearFilter _yearFilter;

	/** The filter strings. */
	private Vector<String> _filterStrings;

	/** The filter year max. */
	private int _filterYearMax;

	/** The filter year min. */
	private int _filterYearMin;
	// private boolean newFilter;

	/** The semantic provider. */
	private String _semanticProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	// /** Logger */
	// private static ILog iLogger =
	// org.bbaw.pdr.ae.view.main.Activator.getILogger();

	/**
	 * Instantiates a new filter selection dialog.
	 * @param parentShell the parent shell
	 * @param pdrObjectsProvider the pdr objects provider
	 * @param type the type
	 */
	public FilterSelectionDialog(final Shell parentShell, final PDRObjectsProvider pdrObjectsProvider, final String type)
	{
		super(parentShell);
		this._pdrObjectsProvider = pdrObjectsProvider;
		this._type = type;
		//		System.out.println("constructur filterselection dialog type " + type); //$NON-NLS-1$
	}

	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(NLMessages.getString("Dialog_filter_selection_dialog_title"));
		// Set the message
		if (_type.equals("reference"))
		{
			setMessage(NLMessages.getString("Dialog_message_select_references_filter"), IMessageProvider.INFORMATION); //$NON-NLS-1$
		}
		else if (_type.equals("semantic"))
		{
			setMessage(NLMessages.getString("Dialog_message_select_semantic_filter"), IMessageProvider.INFORMATION); //$NON-NLS-1$
		}
		else if (_type.startsWith("user"))
		{
			setMessage(NLMessages.getString("Dialog_message_select_user_filter"), IMessageProvider.INFORMATION); //$NON-NLS-1$
		}
		else if (_type.equals("person"))
		{
			setMessage(NLMessages.getString("Dialog_message_select_persons_filter"), IMessageProvider.INFORMATION); //$NON-NLS-1$
		}
		else if (_type.equals("year"))
		{
			setMessage(NLMessages.getString("Dialog_message_select_time_filter"), IMessageProvider.INFORMATION); //$NON-NLS-1$
		}

	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, NLMessages.getString("Dialog_save"), true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Dialog_cancel"), false); //$NON-NLS-1$
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	@Override
	protected final Control createDialogArea(final Composite parent)
	{
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		parent.setLayout(layout);
		_mainComposite = new Composite(parent, SWT.FILL);
		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).minimumHeight = 250;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		_mainComposite.setLayout(new GridLayout());
		// ((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth =
		// true;
		// ((GridLayout) mainComposite.getLayout()).numColumns = 1 ;

		Label listTitle = new Label(_mainComposite, SWT.NONE);
		listTitle.setLayoutData(new GridData());
		((GridData) listTitle.getLayoutData()).horizontalSpan = 1;

		Composite buttonComp = new Composite(parent, SWT.NONE);
		buttonComp.setLayoutData(new GridData());
		buttonComp.setLayout(new RowLayout());
		Button selectAllButton = new Button(buttonComp, SWT.PUSH);
		selectAllButton.setText(NLMessages.getString("Dialog_filter_select_all"));
		selectAllButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_type.equals("reference")) //$NON-NLS-1$
				{
					_filterStrings.removeAllElements();
					for (String s : _pdrObjectsProvider.getAspectsReferences())
					{
						_filterStrings.add(new String(s));
					}
				}
				else if (_type.equals("semantic")) //$NON-NLS-1$
				{
					_filterStrings.removeAllElements();
					for (String s : _pdrObjectsProvider.getAspectsSemantics())
					{
						_filterStrings.add(new String(s));
					}
				}
				else if (_type.equals("user")) //$NON-NLS-1$
				{
					_filterStrings.removeAllElements();
					for (String s : _pdrObjectsProvider.getAspectsUsers())
					{
						_filterStrings.add(new String(s));
					}
				}
				else if (_type.equals("userRef")) //$NON-NLS-1$
				{
					_filterStrings.removeAllElements();
					for (String s : _pdrObjectsProvider.getReferencesUsers())
					{
						_filterStrings.add(new String(s));
					}
				}
				else if (_type.equals("person")) //$NON-NLS-1$
				{
					_filterStrings.removeAllElements();
					for (String s : _pdrObjectsProvider.getAspectsRelatedObjects())
					{
						_filterStrings.add(new String(s));
					}
				}
				else if (_type.equals("year")) //$NON-NLS-1$
				{
					// filterStrings.removeAllElements();
					// for (String s :
					// pdrObjectsProvider.getAspectsRelatedObjects())
					// {
					// filterStrings.add(new String(s));
					// }
				}
				loadValues();
			}
		});
		selectAllButton.pack();

		Button deselectAllButton = new Button(buttonComp, SWT.PUSH);
		deselectAllButton.setText(NLMessages.getString("Dialog_filter_deselect_all"));
		deselectAllButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_filterStrings.removeAllElements();
				loadValues();

			}
		});
		deselectAllButton.pack();
		if (_type.equals("year")) //$NON-NLS-1$
		{
			selectAllButton.setEnabled(false);
			deselectAllButton.setEnabled(false);
		}
		if (_pdrObjectsProvider != null)
		{
			loadFilter();
		}

		parent.pack();

		parent.layout();
		return parent;
	}

	/**
	 * creates OKButton.
	 * @param parent parent composite
	 * @param id id
	 * @param label label of button
	 * @param defaultButton is default
	 * @return okButton
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (isValidInput())
				{
					okPressed();
					saveInput();
					close();

				}
			}
		});
		if (defaultButton)
		{
			Shell shell = parent.getShell();
			if (shell != null)
			{
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Gets the semantic label.
	 * @param semantic the semantic
	 * @return the semantic label
	 */
	private String getSemanticLabel(final String semantic)
	{
		if (_facade.getConfigs().get(_semanticProvider) != null
				&& _facade.getConfigs().get(_semanticProvider).getChildren() != null
				&& _facade.getConfigs().get(_semanticProvider).getChildren().get("aodl:semanticStm") != null
				&& _facade.getConfigs().get(_semanticProvider).getChildren().get("aodl:semanticStm").getChildren() != null
				&& _facade.getConfigs().get(_semanticProvider).getChildren().get("aodl:semanticStm").getChildren()
						.containsKey(semantic))
		{

			return _facade.getConfigs().get(_semanticProvider).getChildren().get("aodl:semanticStm").getChildren()
					.get(semantic).getLabel();
		}

		return semantic;
	}

	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * checks if input is valid.
	 * @return true
	 */
	private boolean isValidInput()
	{
		return true;
	}

	/**
	 * Load filter.
	 */
	private void loadFilter()
	{

		if (_type.equals("reference")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getFilters())
				{
					if (f instanceof AspectReferenceFilter)
					{
						_refFilter = (AspectReferenceFilter) f;
						_filterStrings = _refFilter.getReferenceIds();

						break;
					}
				}
			}
			if (_filterStrings == null)
			{
				_filterStrings = new Vector<String>();
			}
			if (_refFilter == null)
			{
				_refFilter = new AspectReferenceFilter(null);
				// newFilter = true;
				for (String s : _pdrObjectsProvider.getAspectsReferences())
				{
					_filterStrings.add(new String(s));
				}
			}
		}
		else if (_type.equals("person")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getFilters())
				{
					if (f instanceof AspectPersonFilter)
					{
						_personFilter = (AspectPersonFilter) f;
						_filterStrings = _personFilter.getObjectIds();

						break;
					}
				}
			}
			if (_filterStrings == null)
			{
				_filterStrings = new Vector<String>();
			}
			if (_personFilter == null)
			{
				_personFilter = new AspectPersonFilter(null);
				// newFilter = true;
				for (String s : _pdrObjectsProvider.getAspectsRelatedObjects())
				{
					_filterStrings.add(new String(s));
				}
			}
		}
		else if (_type.equals("semantic")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getFilters())
				{
					if (f instanceof AspectSemanticFilter)
					{
						_semFilter = (AspectSemanticFilter) f;
						_filterStrings = _semFilter.getSemantics();

						break;
					}
				}
			}
			if (_filterStrings == null)
			{
				_filterStrings = new Vector<String>();
			}
			if (_semFilter == null)
			{
				_semFilter = new AspectSemanticFilter(null);
				// newFilter = true;
				for (String s : _pdrObjectsProvider.getAspectsSemantics())
				{
					_filterStrings.add(new String(s));
				}
			}
		}
		else if (_type.equals("user")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getFilters())
				{
					if (f instanceof PdrObjectUserFilter)
					{
						_userFilter = (PdrObjectUserFilter) f;
						_filterStrings = _userFilter.getUserIds();

						break;
					}
				}
			}
			if (_filterStrings == null)
			{
				_filterStrings = new Vector<String>();
			}
			if (_userFilter == null)
			{
				_userFilter = new PdrObjectUserFilter(null);
				// newFilter = true;
				for (String s : _pdrObjectsProvider.getAspectsUsers())
				{
					_filterStrings.add(new String(s));
				}
			}
		}
		else if (_type.equals("userRef")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getRefFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getRefFilters())
				{
					if (f instanceof PdrObjectUserFilter)
					{
						_userFilter = (PdrObjectUserFilter) f;
						_filterStrings = _userFilter.getUserIds();

						break;
					}
				}
			}
			if (_filterStrings == null)
			{
				_filterStrings = new Vector<String>();
			}
			if (_userFilter == null)
			{
				_userFilter = new PdrObjectUserFilter(null);
				// newFilter = true;
				for (String s : _pdrObjectsProvider.getReferencesUsers())
				{
					_filterStrings.add(new String(s));
				}
			}
		}
		else if (_type.equals("year")) //$NON-NLS-1$
		{
			if (_pdrObjectsProvider.getFilters() != null)
			{
				for (AEFilter f : _pdrObjectsProvider.getFilters())
				{
					if (f instanceof AspectYearFilter)
					{
						_yearFilter = (AspectYearFilter) f;
						_filterYearMax = _yearFilter.getAspectMaxYear();
						_filterYearMin = _yearFilter.getAspectMinYear();
						break;
					}
				}
			}
			if (_yearFilter == null)
			{
				_filterYearMax = _pdrObjectsProvider.getAspectMaxYear();
				_filterYearMin = _pdrObjectsProvider.getAspectMinYear();
				_yearFilter = new AspectYearFilter(_filterYearMin, _filterYearMax);
				// newFilter = true;
			}
		}

		loadValues();

	}

	/**
	 * Load values.
	 */
	private void loadValues()
	{
		if (_scrollComp != null)
		{
			_scrollComp.dispose();
		}
		_scrollComp = new ScrolledComposite(_mainComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollComp.setExpandHorizontal(true);
		_scrollComp.setExpandVertical(true);
		_scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		_scrollComp.setLayoutData(new GridData());
		((GridData) _scrollComp.getLayoutData()).heightHint = 240;
		((GridData) _scrollComp.getLayoutData()).widthHint = 200;
		((GridData) _scrollComp.getLayoutData()).horizontalSpan = 1;

		((GridData) _scrollComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollComp.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollComp.setMinHeight(1);
		_scrollComp.setMinWidth(1);

		_scrollComp.setLayout(new GridLayout());

		Composite contentComp = new Composite(_scrollComp, SWT.NONE);
		contentComp.setLayoutData(new GridData());
		((GridData) contentComp.getLayoutData()).horizontalSpan = 1;
		contentComp.setLayout(new GridLayout());
		((GridLayout) contentComp.getLayout()).numColumns = 3;
		((GridLayout) contentComp.getLayout()).makeColumnsEqualWidth = false;
		contentComp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		_scrollComp.setContent(contentComp);

		if (_type.equals("reference")) //$NON-NLS-1$
		{
			for (final String refId : _pdrObjectsProvider.getAspectsReferences())
			{
				ReferenceMods reference = _facade.getReference(new PdrId(refId));
				Button checkButton = new Button(contentComp, SWT.CHECK);
				checkButton.setToolTipText(NLMessages.getString("Dialog_filter_button_tooltip"));
				checkButton.setLayoutData(new GridData());
				checkButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_filterStrings != null && _filterStrings.contains(refId))
						{
							_filterStrings.remove(refId);
						}
						else
						{
							_filterStrings.add(refId);
						}
					}
				});
				checkButton.pack();
				if (_filterStrings != null && _filterStrings.contains(refId))
				{
					checkButton.setSelection(true);
				}
				else
				{
					checkButton.setSelection(false);
				}

				Label imageLabel = new Label(contentComp, SWT.NO_BACKGROUND);
				imageLabel.setText(""); //$NON-NLS-1$
				imageLabel.setImage(_imageReg.get(IconsInternal.REFERENCE));
				imageLabel.setLayoutData(new GridData());

				Text refText = new Text(contentComp, SWT.MULTI | SWT.WRAP);
				refText.setLayoutData(new GridData());
				// ((GridData) refText.getLayoutData()).horizontalAlignment =
				// SWT.FILL;
				// ((GridData) refText.getLayoutData()).verticalAlignment =
				// SWT.FILL;
				((GridData) refText.getLayoutData()).grabExcessHorizontalSpace = true;
				refText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

				if (reference != null)
				{
					refText.setText(reference.getDisplayName());
				}
				else
				{
					refText.setText(refId);
				}
			}
		}
		else if (_type.equals("person")) //$NON-NLS-1$
		{

			for (final String persId : _pdrObjectsProvider.getAspectsRelatedObjects())
			{

				Button checkButton = new Button(contentComp, SWT.CHECK);
				checkButton.setToolTipText(NLMessages.getString("Dialog_filter_button_tooltip"));
				checkButton.setLayoutData(new GridData());
				checkButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_filterStrings != null && _filterStrings.contains(persId))
						{
							_filterStrings.remove(persId);
						}
						else
						{
							_filterStrings.add(persId);
						}
					}
				});
				checkButton.pack();
				if (_filterStrings != null && _filterStrings.contains(persId))
				{
					checkButton.setSelection(true);
				}
				else
				{
					checkButton.setSelection(false);
				}

				Label imageLabel = new Label(contentComp, SWT.NO_BACKGROUND);
				imageLabel.setText(""); //$NON-NLS-1$
				imageLabel.setLayoutData(new GridData());

				Text refText = new Text(contentComp, SWT.MULTI | SWT.WRAP);
				refText.setLayoutData(new GridData());
				// ((GridData) refText.getLayoutData()).horizontalAlignment =
				// SWT.FILL;
				// ((GridData) refText.getLayoutData()).verticalAlignment =
				// SWT.FILL;
				((GridData) refText.getLayoutData()).grabExcessHorizontalSpace = true;
				refText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				PdrObject obj = _facade.getPdrObject(new PdrId(persId));
				if (obj != null)
				{
					refText.setText(obj.getDisplayName());
					if (persId.startsWith("pdrPo")) //$NON-NLS-1$
					{
						imageLabel.setImage(_imageReg.get(IconsInternal.PERSON));
					}
					else if (persId.startsWith("pdrAo")) //$NON-NLS-1$
					{
						imageLabel.setImage(_imageReg.get(IconsInternal.ASPECT));
					}
				}
			}
		}
		else if (_type.equals("semantic")) //$NON-NLS-1$
		{

			for (final String semantic : _pdrObjectsProvider.getAspectsSemantics())
			{

				Button checkButton = new Button(contentComp, SWT.CHECK);
				checkButton.setToolTipText("Check to select"); //$NON-NLS-1$
				checkButton.setLayoutData(new GridData());
				checkButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_filterStrings != null && _filterStrings.contains(semantic))
						{
							_filterStrings.remove(semantic);
						}
						else
						{
							_filterStrings.add(semantic);
						}
					}
				});
				checkButton.pack();
				if (_filterStrings != null && _filterStrings.contains(semantic))
				{
					checkButton.setSelection(true);
				}
				else
				{
					checkButton.setSelection(false);
				}

				Label imageLabel = new Label(contentComp, SWT.NO_BACKGROUND);
				imageLabel.setText(""); //$NON-NLS-1$
				if (semantic.startsWith("NormName")
						|| _facade.getPersonDisplayNameTags(_semanticProvider).contains(semantic))
				{
					imageLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM));
				}
				else if (semantic.equals("Name") || _facade.getPersonNameTags(_semanticProvider).contains(semantic))
				{
					imageLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME));
				}
				else
				{
					imageLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION));
				}
				imageLabel.setLayoutData(new GridData());

				Text refText = new Text(contentComp, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
				refText.setLayoutData(new GridData());
				((GridData) refText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).verticalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).grabExcessHorizontalSpace = true;
				refText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

				refText.setText(getSemanticLabel(semantic));
			}
		}
		else if (_type.equals("user")) //$NON-NLS-1$
		{

			IUserManager userManager = _facade.getUserManager();
			for (final String userId : _pdrObjectsProvider.getAspectsUsers())
			{

				User user = null;
				try
				{
					user = userManager.getUserById(userId);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				Button checkButton = new Button(contentComp, SWT.CHECK);
				checkButton.setToolTipText(NLMessages.getString("Dialog_filter_button_tooltip"));
				checkButton.setLayoutData(new GridData());
				checkButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_filterStrings != null && _filterStrings.contains(userId))
						{
							_filterStrings.remove(userId);
						}
						else
						{
							_filterStrings.add(userId);
						}
					}
				});
				checkButton.pack();
				if (_filterStrings != null && _filterStrings.contains(userId))
				{
					checkButton.setSelection(true);
				}
				else
				{
					checkButton.setSelection(false);
				}

				Label imageLabel = new Label(contentComp, SWT.NO_BACKGROUND);
				imageLabel.setText(""); //$NON-NLS-1$
				imageLabel.setImage(_imageReg.get(IconsInternal.USER));
				imageLabel.setLayoutData(new GridData());

				Text refText = new Text(contentComp, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
				refText.setLayoutData(new GridData());
				((GridData) refText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).verticalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).grabExcessHorizontalSpace = true;
				refText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				if (user != null)
				{
					refText.setText(user.getDisplayName());
				}
				else
				{
					refText.setText(NLMessages.getString("Dialog_filter_user_name_notFound") + ": " + userId + ")"); //$NON-NLS-2$
				}
			}

		}
		else if (_type.equals("userRef")) //$NON-NLS-1$
		{

			IUserManager userManager = _facade.getUserManager();
			for (final String userId : _pdrObjectsProvider.getReferencesUsers())
			{
				//				System.out.println("build userref"); //$NON-NLS-1$
				User user = null;
				try
				{
					user = userManager.getUserById(userId);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				Button checkButton = new Button(contentComp, SWT.CHECK);
				checkButton.setToolTipText(NLMessages.getString("Dialog_filter_button_tooltip"));
				checkButton.setLayoutData(new GridData());
				checkButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_filterStrings != null && _filterStrings.contains(userId))
						{
							_filterStrings.remove(userId);
						}
						else
						{
							_filterStrings.add(userId);
						}
					}
				});
				checkButton.pack();
				if (_filterStrings != null && _filterStrings.contains(userId))
				{
					checkButton.setSelection(true);
				}
				else
				{
					checkButton.setSelection(false);
				}

				Label imageLabel = new Label(contentComp, SWT.NO_BACKGROUND);
				imageLabel.setText(""); //$NON-NLS-1$
				imageLabel.setImage(_imageReg.get(IconsInternal.USER));
				imageLabel.setLayoutData(new GridData());

				Text refText = new Text(contentComp, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
				refText.setLayoutData(new GridData());
				((GridData) refText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).verticalAlignment = SWT.FILL;
				((GridData) refText.getLayoutData()).grabExcessHorizontalSpace = true;
				refText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				if (user != null)
				{
					refText.setText(user.getDisplayName());
				}
				else
				{
					refText.setText(NLMessages.getString("Dialog_filter_user_name_notFound") + userId + ")"); //$NON-NLS-2$
				}
			}

		}
		else if (_type.equals("year")) //$NON-NLS-1$
		{
			final int min = _pdrObjectsProvider.getAspectMinYear();
			int max = _pdrObjectsProvider.getAspectMaxYear();
			int diff = max - min;

			// System.out.println("min " + min + " max " + max + " diff " +
			// diff);
			// System.out.println("filterYearMin " + filterYearMin +
			// " filterYearMax " + filterYearMax + " diff " + diff);

			Text minText = new Text(contentComp, SWT.BORDER | SWT.READ_ONLY);
			minText.setText(NLMessages.getString("Dialog_filter_time_min") + min);
			minText.setBackground(WHITE_COLOR);

			final Scale minScale = new Scale(contentComp, SWT.HORIZONTAL);
			minScale.setMinimum(0);
			minScale.setMaximum(diff);
			minScale.setIncrement(1);
			minScale.setPageIncrement(5);
			minScale.setSelection(_filterYearMin - min);
			minScale.setBackground(WHITE_COLOR);
			minScale.setLayoutData(new GridData());
			((GridData) minScale.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) minScale.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) minScale.getLayoutData()).grabExcessHorizontalSpace = true;

			final Text minfilterText = new Text(contentComp, SWT.BORDER | SWT.READ_ONLY);
			minfilterText.setText("" + _filterYearMin); //$NON-NLS-1$
			minfilterText.setLayoutData(new GridData());
			minfilterText.setSize(35, 16);
			minfilterText.setBackground(WHITE_COLOR);

			((GridData) minfilterText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) minfilterText.getLayoutData()).minimumWidth = 50;
			minfilterText.pack();

			Text maxText = new Text(contentComp, SWT.BORDER | SWT.READ_ONLY);
			maxText.setText(NLMessages.getString("Dialog_filter_time_max") + max);
			maxText.setBackground(WHITE_COLOR);

			final Scale maxScale = new Scale(contentComp, SWT.HORIZONTAL);
			maxScale.setMinimum(0);
			maxScale.setMaximum(diff);
			maxScale.setIncrement(1);
			maxScale.setPageIncrement(5);
			maxScale.setSelection(_filterYearMax - min);
			maxScale.setBackground(WHITE_COLOR);
			maxScale.setLayoutData(new GridData());
			((GridData) maxScale.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) maxScale.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) maxScale.getLayoutData()).grabExcessHorizontalSpace = true;
			final Text maxfilterText = new Text(contentComp, SWT.BORDER | SWT.READ_ONLY);
			maxfilterText.setText("" + _filterYearMax); //$NON-NLS-1$
			maxfilterText.setBackground(WHITE_COLOR);
			maxfilterText.setLayoutData(new GridData());
			((GridData) maxfilterText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) maxfilterText.getLayoutData()).minimumWidth = 50;

			minScale.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					_filterYearMin = min + minScale.getSelection();
					minfilterText.setText("" + _filterYearMin); //$NON-NLS-1$
				}
			});

			// minScale.addMouseWheelListener(new MouseWheelListener() {
			//
			// @Override
			// public void mouseScrolled(MouseEvent e) {
			// Spinner src = (Spinner)e.getSource();
			// src.setSelection( src.getSelection() - e.count );
			//
			// }
			// });
			maxScale.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					_filterYearMax = min + maxScale.getSelection();
					maxfilterText.setText("" + _filterYearMax); //$NON-NLS-1$
				}
			});

			// maxScale.addMouseWheelListener(new MouseWheelListener() {
			//
			// @Override
			// public void mouseScrolled(MouseEvent e) {
			// Scale src = (Scale)e.getSource();
			// src.setSelection( src.getSelection() - e.count );
			//
			// }
			// });

		}
		contentComp.layout();

		_scrollComp.setContent(contentComp);
		Point point = contentComp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// Point mp = mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// if (point.x > mp.x - 20) point.x = mp.x - 20;
		_scrollComp.setMinSize(point);
		// scrollComp.pack();
		_scrollComp.layout();
		_mainComposite.redraw();
		// mainComposite.pack();
		_mainComposite.layout();
		_mainComposite.getParent().layout();
		// mainComposite.getParent().pack();
	}

	@Override
	protected final void okPressed()
	{
		saveInput();
		// super.okPressed();
	}

	/**
	 * if user name and password are correct the identified current user is
	 * saved as currentUser in facade.
	 */
	private void saveInput()
	{
		if (_type.equals("reference")) //$NON-NLS-1$
		{
			_refFilter.setReferenceIds(_filterStrings);
			if (_pdrObjectsProvider.getAspectsReferences().size() == _filterStrings.size())
			{
				_pdrObjectsProvider.removeFilter(_refFilter);
			}
			else
			{
				_pdrObjectsProvider.addFilter(_refFilter);
			}
		}
		else if (_type.equals("semantic")) //$NON-NLS-1$
		{
			_semFilter.setSemantics(_filterStrings);
			if (_pdrObjectsProvider.getAspectsSemantics().size() == _filterStrings.size())
			{
				_pdrObjectsProvider.removeFilter(_semFilter);
			}
			else
			{
				_pdrObjectsProvider.addFilter(_semFilter);
			}
		}
		else if (_type.equals("user")) //$NON-NLS-1$
		{
			_userFilter.setUserIds(_filterStrings);
			if (_pdrObjectsProvider.getAspectsUsers().size() == _filterStrings.size())
			{
				_pdrObjectsProvider.removeFilter(_userFilter);
			}
			else
			{
				_pdrObjectsProvider.addFilter(_userFilter);
			}
		}
		else if (_type.equals("userRef")) //$NON-NLS-1$
		{
			_userFilter.setUserIds(_filterStrings);
			if (_pdrObjectsProvider.getReferencesUsers().size() == _filterStrings.size())
			{
				_pdrObjectsProvider.removeRefFilter(_userFilter);
			}
			else
			{
				_pdrObjectsProvider.addRefFilter(_userFilter);
			}
		}
		else if (_type.equals("person")) //$NON-NLS-1$
		{
			_personFilter.setObjectIds(_filterStrings);
			if (_pdrObjectsProvider.getAspectsRelatedObjects().size() == _filterStrings.size())
			{
				_pdrObjectsProvider.removeFilter(_personFilter);
			}
			else
			{
				_pdrObjectsProvider.addFilter(_personFilter);
			}
		}
		else if (_type.equals("year")) //$NON-NLS-1$
		{
			if (_filterYearMin > _filterYearMax)
			{
				int help = _filterYearMin;
				_filterYearMin = _filterYearMax;
				_filterYearMax = help;
			}
			_yearFilter.setAspectMinYear(_filterYearMin);
			_yearFilter.setAspectMaxYear(_filterYearMax);
			if (_pdrObjectsProvider.getAspectMaxYear() == _filterYearMax
					&& _pdrObjectsProvider.getAspectMinYear() == _filterYearMin)
			{
				_pdrObjectsProvider.removeFilter(_yearFilter);
			}
			else
			{
				_pdrObjectsProvider.addFilter(_yearFilter);
			}
		}

	}
}

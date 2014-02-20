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
package org.bbaw.pdr.ae.view.control.dialogs;

import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class SelectOwnerAndKeyDialog extends Dialog
{

	private TaggingRange _taggingRange;
	protected Facade _facade = Facade.getInstanz();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	private Text _textTaggingAna;
	private Text _textTaggingKey;

	private String _ana;
	private String _key;
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param tr
	 */
	public SelectOwnerAndKeyDialog(Shell parentShell, TaggingRange taggingRange)
	{
		super(parentShell);
		this._taggingRange = taggingRange;
		this._ana = taggingRange.getAna();
		this._key = taggingRange.getKey();
	}


	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;

		GridLayout gl = new GridLayout(3, false);
		composite.setLayout(gl);
		composite.setLayoutData(gd);
		Label taggingAna = new Label(composite, SWT.NONE);
		taggingAna.setText(NLMessages.getString("Editor_markup_ana"));
		taggingAna.setLayoutData(new GridData());

		_textTaggingAna = new Text(composite, SWT.BORDER);
		_textTaggingAna.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		_textTaggingAna.setLayoutData(new GridData());
		((GridData) _textTaggingAna.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _textTaggingAna.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _textTaggingAna.getLayoutData()).horizontalSpan = 1;
		ControlDecoration decoAna = new ControlDecoration(_textTaggingAna, SWT.LEFT);
		decoAna.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_aspects_persons_last"));
		decoAna.setImage(FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		decoAna.setShowOnlyOnFocus(false);
		_textTaggingAna.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				char[] autoActivationCharacters = new char[]
				{'.', '#'};
				KeyStroke keyStrokeP;
				KeyStroke keyStrokeA;
				KeyStroke keyStrokeL;

				try
				{
					keyStrokeA = KeyStroke.getInstance("Ctrl+e");
					keyStrokeP = KeyStroke.getInstance("Ctrl+p");
					keyStrokeL = KeyStroke.getInstance("Ctrl+l");

					ContentProposalAdapter adapter = new ContentProposalAdapter(_textTaggingAna,
							new TextContentAdapter(), new FacetContentProposalProvider(_facade.getAllPersonsFacets()),
							keyStrokeP, autoActivationCharacters);
					adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
					// System.out.println("innerhalb des try");
					adapter.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingAna.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_ana = ((Facet) proposal).getKey();
							}
						}
					});

					ContentProposalAdapter adapter2 = new ContentProposalAdapter(_textTaggingAna,
							new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getLoadedAspectsFacets()), keyStrokeA,
							autoActivationCharacters);
					adapter2.setLabelProvider(new AutoCompleteNameLabelProvider());
					// System.out.println("innerhalb des try");
					adapter2.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingAna.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_ana = ((Facet) proposal).getKey();

							}
						}
					});
					if (_facade.getLastObjectsFacets() != null)
					{

						ContentProposalAdapter adapter3 = new ContentProposalAdapter(_textTaggingAna,
								new TextContentAdapter(), new FacetContentProposalProvider(_facade
										.getLastObjectsFacets()), keyStrokeL, autoActivationCharacters);
						adapter3.setLabelProvider(new AutoCompleteNameLabelProvider());
						// System.out.println("innerhalb des try");
						adapter3.addContentProposalListener(new IContentProposalListener()
						{
							@Override
							public void proposalAccepted(final IContentProposal proposal)
							{
								_textTaggingAna.setText(proposal.getContent());
								if (((Facet) proposal).getKey() != null)
								{
									_ana = ((Facet) proposal).getKey();

								}
							}
						});
					}
				}
				catch (org.eclipse.jface.bindings.keys.ParseException e1)
				{

					e1.printStackTrace();
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_textTaggingAna.getText().trim().length() == 0)
				{
					_ana = null;

				}
				else
				{
					_facade.addIDStringToLastObjects(_ana);
				}
			}
		});

		Button findAna = new Button(composite, SWT.PUSH);
		findAna.setText(NLMessages.getString("Editor_select_dots"));
		findAna.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_ana_tip"));
		findAna.setImage(_imageReg.get(IconsInternal.SEARCH));
		// findAna.setLayoutData(new GridData());
		// ((GridData) findAna.getLayoutData()).horizontalSpan = 1;
		// ((GridData) findAna.getLayoutData()).horizontalAlignment =
		// SWT.RIGHT;
		findAna.addSelectionListener(new SelectionAdapter()
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
					PdrObject o = _facade.getPdrObject(new PdrId(_facade.getRequestedId().toString()));
					_ana = o.getPdrId().toString();
					_textTaggingAna.setText(o.getDisplayNameWithID());
				}
			}
		});

		// end Ana

		Label taggingKey = new Label(composite, SWT.NONE);
		taggingKey.setText(NLMessages.getString("Editor_key"));
		taggingKey.setLayoutData(new GridData());

		_textTaggingKey = new Text(composite, SWT.BORDER);
		_textTaggingKey.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		_textTaggingKey.setLayoutData(new GridData());
		((GridData) _textTaggingKey.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _textTaggingKey.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _textTaggingKey.getLayoutData()).horizontalSpan = 1;
		ControlDecoration decoKey = new ControlDecoration(_textTaggingKey, SWT.LEFT);
		decoKey.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_all_ref"));
		decoKey.setImage(FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		decoKey.setShowOnlyOnFocus(false);
		_textTaggingKey.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				char[] autoActivationCharacters = new char[]
				{'.', '#'};
				KeyStroke keyStroke;

				try
				{
					keyStroke = KeyStroke.getInstance("Ctrl+Space");

					ContentProposalAdapter adapter = new ContentProposalAdapter(_textTaggingKey,
							new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getAllReferenceFacets()), keyStroke,
							autoActivationCharacters);
					adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
					adapter.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingKey.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_key = ((Facet) proposal).getKey();
							}
						}
					});

				}
				catch (org.eclipse.jface.bindings.keys.ParseException e1)
				{

					e1.printStackTrace();
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_textTaggingKey.getText().trim().length() == 0)
				{
					_key = null;
				}
			}
		});

		// Label bk = new Label(tagging1Coposite, SWT.NONE);
		// bk.setText("");
		// bk.setLayoutData(new GridData());

		Button findKey = new Button(composite, SWT.PUSH);
		findKey.setText(NLMessages.getString("Editor_select_dots"));
		findKey.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_key_tip"));
		findKey.setImage(_imageReg.get(IconsInternal.SEARCH));
		findKey.setLayoutData(new GridData());
		((GridData) findKey.getLayoutData()).horizontalSpan = 1;
		findKey.addSelectionListener(new SelectionAdapter()
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
					PdrObject o = _facade.getPdrObject(_facade.getRequestedId());
					_textTaggingKey.setText(o.getDisplayNameWithID());
					_key = o.getPdrId().toString();
				}

			}
		});

		if (_ana != null && _ana.trim().length() > 0)
		{
			PdrObject o = _facade.getPdrObject(new PdrId(_ana));
			if (o != null)
			{
				_textTaggingAna.setText(o.getDisplayNameWithID());
			}

		}
		else
		{
			_textTaggingAna.setText("");
			_ana = null;
		}

		if (_key != null && _key.trim().length() > 0)
		{
			PdrObject o = _facade.getPdrObject(new PdrId(_key));
			if (o != null)
			{
				_textTaggingKey.setText(o.getDisplayNameWithID());
			}

		}
		else
		{
			_textTaggingKey.setText("");
			_key = null;
		}
		composite.layout();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		Button setButton = createButton(parent, IDialogConstants.OK_ID, "Set Markup", true);
		setButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (_taggingRange != null)
				{
					if (_ana != null && _ana.trim().length() > 0)
					{
						_taggingRange.setAna(_ana);
					}
					if (_key != null && _key.trim().length() > 0)
					{
						_taggingRange.setKey(_key);
					}
				}
				setReturnCode(OK);
				close();
			}
		});
		Button setWithoutButton = createButton(parent, 1, "Set Markup without key and owner", false);
		setWithoutButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				if (_taggingRange != null)
				{
					_taggingRange.setAna(null);
					_taggingRange.setKey(null);

				}

				setReturnCode(1);
				close();
			}
		});
		Button cancelButton = createButton(parent, 2, NLMessages.getString("Dialog_cancel"), false);
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

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(620, 180);
	}

}

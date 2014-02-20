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
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.AccessCondition;
import org.bbaw.pdr.ae.model.DetailMods;
import org.bbaw.pdr.ae.model.ExtendMods;
import org.bbaw.pdr.ae.model.IdentifierMods;
import org.bbaw.pdr.ae.model.LocationMods;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.Note;
import org.bbaw.pdr.ae.model.OriginInfo;
import org.bbaw.pdr.ae.model.PartMods;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.RoleMods;
import org.bbaw.pdr.ae.model.TimeSpan;
import org.bbaw.pdr.ae.model.TitleInfo;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class ReferenceEditorAddFieldToolTip.
 * @author Christoph Plutte
 */
public class ReferenceEditorAddFieldToolTip extends CustomTooltip
{

	/** The observers. */
	private ArrayList<Observer> _observers = new ArrayList<Observer>();

	/** The parent shell. */
	private Shell _parentShell;

	/** The add button comp. */
	private Composite _addButtonComp;

	/** The main comp. */
	private Composite _mainComp;

	/** The tool tip text. */
	private String _toolTipText;

	/** The link. */
	private Link _link;

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The header text. */
	private String _headerText = "Markup Information";

	/** The Constant HEADER_BG_COLOR. */
	public static final String HEADER_BG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_BG_COLOR";

	/** The Constant HEADER_FG_COLOR. */
	public static final String HEADER_FG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_FG_COLOR";

	/** The Constant HEADER_FONT. */
	public static final String HEADER_FONT = Policy.JFACE + ".TOOLTIP_HEAD_FONT";

	/** The Constant HEADER_CLOSE_ICON. */
	public static final String HEADER_CLOSE_ICON = Policy.JFACE + ".TOOLTIP_CLOSE_ICON";

	/** The Constant HEADER_HELP_ICON. */
	public static final String HEADER_HELP_ICON = Policy.JFACE + ".TOOLTIP_HELP_ICON";

	/** The _current reference. */
	private ReferenceMods _currentReference;

	/**
	 * Instantiates a new reference editor add field tool tip.
	 * @param control the control
	 * @param reference the reference
	 */
	public ReferenceEditorAddFieldToolTip(final Control control, final ReferenceMods reference)
	{
		super(control);
		this._currentReference = reference;
		this._parentShell = control.getShell();
	}

	/**
	 * Adds the observer.
	 * @param o the o
	 */
	public final void addObserver(final Observer o)
	{
		_observers.add(o);
	}

	/**
	 * Creates the content area.
	 * @return the composite
	 */
	protected final Composite createContentArea()
	{
		if (_addButtonComp != null)
		{
			_addButtonComp.dispose();
		}
		_addButtonComp = new Composite(_mainComp, SWT.COLOR_INFO_BACKGROUND);
		_addButtonComp.setLayoutData(new GridData());
		((GridData) _addButtonComp.getLayoutData()).widthHint = 370;
		((GridData) _addButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
		_addButtonComp.setLayout(new GridLayout());
		((GridLayout) _addButtonComp.getLayout()).numColumns = 1;
		((GridLayout) _addButtonComp.getLayout()).makeColumnsEqualWidth = false;

		loadAddButtons();
		return _addButtonComp;
	}

	@Override
	protected final Composite createToolTipContentArea(final Event event, final Composite parent)
	{
		_mainComp = new Composite(parent, SWT.NONE);

		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.verticalSpacing = 1;
		_mainComp.setLayout(gl);

		Composite topArea = new Composite(_mainComp, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.widthHint = 370;
		topArea.setLayoutData(data);
		topArea.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));

		gl = new GridLayout(2, false);
		gl.marginBottom = 2;
		gl.marginTop = 2;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 5;
		gl.marginRight = 2;

		topArea.setLayout(gl);

		Label l = new Label(topArea, SWT.NONE);
		l.setText(_headerText);
		l.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		l.setFont(JFaceResources.getFontRegistry().get(HEADER_FONT));
		l.setForeground(JFaceResources.getColorRegistry().get(HEADER_FG_COLOR));
		l.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite iconComp = new Composite(topArea, SWT.NONE);
		iconComp.setLayoutData(new GridData());
		iconComp.setLayout(new GridLayout(2, false));
		iconComp.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));

		gl = new GridLayout(2, false);
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		iconComp.setLayout(gl);

		Label helpIcon = new Label(iconComp, SWT.NONE);
		helpIcon.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		helpIcon.setImage(_imageReg.get(IconsInternal.CANCEL));
		helpIcon.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(final MouseEvent e)
			{
				hide();
				// openHelp();
			}
		});

		Label closeIcon = new Label(iconComp, SWT.NONE);
		closeIcon.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		closeIcon.setImage(JFaceResources.getImage(HEADER_CLOSE_ICON));
		closeIcon.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(final MouseEvent e)
			{
				_parentShell.setFocus();
				hide();
			}
		});

		createContentArea().setLayoutData(new GridData(GridData.FILL_BOTH));

		return _mainComp;
	}

	/**
	 * Gets the tool tip text.
	 * @return the tool tip text
	 */
	public final String getToolTipText()
	{
		return _toolTipText;
	}

	/**
	 * Load add buttons.
	 */
	private final void loadAddButtons()
	{
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			// if (_currentReference.getNameMods() == null ||
			// _currentReference.getNameMods().isEmpty())
			// {
			final Button addNames = new Button(rowButtonComp, SWT.PUSH);
			addNames.setText(NLMessages.getString("Editor_add_names"));
			addNames.setToolTipText(NLMessages.getString("Editor_add_names_tooltip"));
			addNames.setImage(_imageReg.get(IconsInternal.ADD));

			addNames.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					if (_currentReference.getNameMods() == null)
					{
						_currentReference.setNameMods(new Vector<NameMods>(1));
						_currentReference.getNameMods().add(new NameMods(2));
					}
					else
					{
						if (!_currentReference.getNameMods().isEmpty()
								&& _currentReference.getNameMods().lastElement() != null
								&& _currentReference.getNameMods().lastElement().getRoleMods() != null
								&& _currentReference.getNameMods().lastElement().getRoleMods().getRoleTerm() != null)
						{
							NameMods lastn = _currentReference.getNameMods().lastElement();
							NameMods newN = new NameMods(2);
							newN.setRoleMods(new RoleMods());
							newN.getRoleMods().setRoleTerm(new String(lastn.getRoleMods().getRoleTerm()));
							_currentReference.getNameMods().add(newN);
						}
						else
						{
							_currentReference.getNameMods().add(new NameMods(2));
						}
					}
					notifyObservers();
				}
			});
			// }
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getTitleInfo() == null || _currentReference.getTitleInfo().getTitle() == null)
			{
				final Button addTitleInfo = new Button(rowButtonComp, SWT.PUSH);
				addTitleInfo.setText(NLMessages.getString("Editor_add_title"));
				addTitleInfo.setToolTipText(NLMessages.getString("Editor_add_title_tooltip"));
				addTitleInfo.setImage(_imageReg.get(IconsInternal.ADD));
				addTitleInfo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getTitleInfo() == null)
						{
							_currentReference.setTitleInfo(new TitleInfo());
							_currentReference.getTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						else
						{
							_currentReference.getTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						notifyObservers();
					}
				});
			}
			if (_currentReference.getTitleInfo() == null || _currentReference.getTitleInfo().getSubTitle() == null)
			{
				final Button addExtra = new Button(rowButtonComp, SWT.PUSH);
				addExtra.setText(NLMessages.getString("Editor_subtitle")); //$NON-NLS-1$
				addExtra.setToolTipText(NLMessages.getString("Editor_add_subtible"));
				addExtra.setImage(_imageReg.get(IconsInternal.ADD));
				addExtra.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getTitleInfo() == null)
						{
							_currentReference.setTitleInfo(new TitleInfo());
							_currentReference.getTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						_currentReference.getTitleInfo().setSubTitle(" "); //$NON-NLS-1$
						notifyObservers();
						// addExtra.setEnabled(false);

					}
				});

			}
			if (_currentReference.getTitleInfo() == null || _currentReference.getTitleInfo().getPartName() == null
					|| _currentReference.getTitleInfo().getPartNumber() == null)
			{
				final Button addExtra2 = new Button(rowButtonComp, SWT.PUSH);
				addExtra2.setText(NLMessages.getString("Editor_partName")); //$NON-NLS-1$
				addExtra2.setToolTipText(NLMessages.getString("Editor_add_title_extra_tooltip"));
				addExtra2.setImage(_imageReg.get(IconsInternal.ADD));

				addExtra2.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getTitleInfo() == null)
						{
							_currentReference.setTitleInfo(new TitleInfo());
							_currentReference.getTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						_currentReference.getTitleInfo().setPartName(" "); //$NON-NLS-1$
						_currentReference.getTitleInfo().setPartNumber(" "); //$NON-NLS-1$
						notifyObservers();

					}
				});
			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getSeriesTitleInfo() == null
					|| _currentReference.getSeriesTitleInfo().getTitle() == null)
			{
				final Button addTitleInfo = new Button(rowButtonComp, SWT.PUSH);
				addTitleInfo.setText(NLMessages.getString("Editor_add_series"));
				addTitleInfo.setImage(_imageReg.get(IconsInternal.ADD));
				addTitleInfo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getSeriesTitleInfo() == null)
						{
							_currentReference.setSeriesTitleInfo(new TitleInfo());
							_currentReference.getSeriesTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						else
						{
							_currentReference.getSeriesTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						notifyObservers();
					}
				});
			}
			if (_currentReference.getSeriesTitleInfo() == null
					|| _currentReference.getSeriesTitleInfo().getSubTitle() == null)
			{
				final Button addExtra = new Button(rowButtonComp, SWT.PUSH);
				addExtra.setText(NLMessages.getString("Editor_add_series") + " " + NLMessages.getString("Editor_subtitle")); //$NON-NLS-1$
				addExtra.setImage(_imageReg.get(IconsInternal.ADD));
				addExtra.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getSeriesTitleInfo() == null)
						{
							_currentReference.setSeriesTitleInfo(new TitleInfo());
							_currentReference.getSeriesTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						_currentReference.getSeriesTitleInfo().setSubTitle(" "); //$NON-NLS-1$
						notifyObservers();
						// addExtra.setEnabled(false);

					}
				});

			}
			if (_currentReference.getSeriesTitleInfo() == null
					|| _currentReference.getSeriesTitleInfo().getPartName() == null
					|| _currentReference.getSeriesTitleInfo().getPartNumber() == null)
			{
				final Button addExtra2 = new Button(rowButtonComp, SWT.PUSH);
				addExtra2
						.setText(NLMessages.getString("Editor_add_series") + " " + NLMessages.getString("Editor_partName")); //$NON-NLS-1$
				addExtra2.setImage(_imageReg.get(IconsInternal.ADD));

				addExtra2.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getSeriesTitleInfo() == null)
						{
							_currentReference.setSeriesTitleInfo(new TitleInfo());
							_currentReference.getSeriesTitleInfo().setTitle(" "); //$NON-NLS-1$
						}
						_currentReference.getSeriesTitleInfo().setPartName(" "); //$NON-NLS-1$
						_currentReference.getSeriesTitleInfo().setPartNumber(" "); //$NON-NLS-1$
						notifyObservers();

					}
				});
			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null || _currentReference.getOriginInfo().getPlaceTerm() == null)
			{
				final Button addOrigin = new Button(rowButtonComp, SWT.PUSH);
				addOrigin.setText(NLMessages.getString("Editor_place"));
				addOrigin.setToolTipText(NLMessages.getString("Editor_add_origin_info_tooltip"));
				addOrigin.setImage(_imageReg.get(IconsInternal.ADD));
				addOrigin.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setPlaceTerm(" "); //$NON-NLS-1$
						_currentReference.getOriginInfo().setPlaceType("text"); //$NON-NLS-1$

						notifyObservers();
					}
				});
			}
			if (_currentReference.getOriginInfo() == null || _currentReference.getOriginInfo().getPublisher() == null)
			{
				final Button addOrigin = new Button(rowButtonComp, SWT.PUSH);
				addOrigin.setText(NLMessages.getString("Editor_publisher"));
				addOrigin.setToolTipText(NLMessages.getString("Editor_add_origin_info_tooltip"));
				addOrigin.setImage(_imageReg.get(IconsInternal.ADD));
				addOrigin.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setPublisher(" "); //$NON-NLS-1$
						notifyObservers();
					}
				});
			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null || _currentReference.getOriginInfo().getDateCreated() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_dateCreated"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateCreated(new PdrDate(0, 0, 0));
						notifyObservers();
					}
				});

			}
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getDateCreatedTimespan() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_add_date_created_timespan"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateCreatedTimespan(new TimeSpan());
						_currentReference.getOriginInfo().getDateCreatedTimespan().setDateFrom(new PdrDate(0, 0, 0));
						_currentReference.getOriginInfo().getDateCreatedTimespan().setDateTo(new PdrDate(0, 0, 0));

						notifyObservers();
					}
				});

			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null || _currentReference.getOriginInfo().getDateIssued() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_dateIssued"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateIssued(new PdrDate(0, 0, 0));
						notifyObservers();
					}
				});

			}
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getDateIssuedTimespan() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_add_date_issued_timespan"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateIssuedTimespan(new TimeSpan());
						_currentReference.getOriginInfo().getDateIssuedTimespan().setDateFrom(new PdrDate(0, 0, 0));
						_currentReference.getOriginInfo().getDateIssuedTimespan().setDateTo(new PdrDate(0, 0, 0));

						notifyObservers();
					}
				});

			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getCopyrightDate() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_copyrightDate"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setCopyrightDate(new PdrDate(0, 0, 0));
						notifyObservers();
					}
				});

			}
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getCopyrightDateTimespan() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_add_date_copyright_timespan"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setCopyrightDateTimespan(new TimeSpan());
						_currentReference.getOriginInfo().getCopyrightDateTimespan().setDateFrom(new PdrDate(0, 0, 0));
						_currentReference.getOriginInfo().getCopyrightDateTimespan().setDateTo(new PdrDate(0, 0, 0));

						notifyObservers();
					}
				});

			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getDateCaptured() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_dateCaptured"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateCaptured(new PdrDate(0, 0, 0));
						notifyObservers();
					}
				});

			}
			if (_currentReference.getOriginInfo() == null
					|| _currentReference.getOriginInfo().getDateCapturedTimespan() == null)
			{
				final Button addDate = new Button(rowButtonComp, SWT.PUSH);
				addDate.setText(NLMessages.getString("Editor_add_date_captured_timespan"));
				addDate.setToolTipText(NLMessages.getString("Editor_add_dates_tooltip"));
				addDate.setImage(_imageReg.get(IconsInternal.ADD));

				addDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setDateCapturedTimespan(new TimeSpan());
						_currentReference.getOriginInfo().getDateCapturedTimespan().setDateFrom(new PdrDate(0, 0, 0));
						_currentReference.getOriginInfo().getDateCapturedTimespan().setDateTo(new PdrDate(0, 0, 0));

						notifyObservers();
					}
				});

			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getOriginInfo() == null || _currentReference.getOriginInfo().getEdition() == null)
			{
				final Button addOrigin = new Button(rowButtonComp, SWT.PUSH);
				addOrigin.setText(NLMessages.getString("Editor_edition"));
				addOrigin.setToolTipText(NLMessages.getString("Editor_add_origin_info_tooltip"));
				addOrigin.setImage(_imageReg.get(IconsInternal.ADD));
				addOrigin.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getOriginInfo() == null)
						{
							_currentReference.setOriginInfo(new OriginInfo());
						}
						_currentReference.getOriginInfo().setEdition(" "); //$NON-NLS-1$
						notifyObservers();
					}
				});
			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getLocation() == null || _currentReference.getLocation().getUrl() == null)
			{
				final Button addLocation = new Button(rowButtonComp, SWT.PUSH);
				addLocation.setText(NLMessages.getString("Editor_url"));
				addLocation.setToolTipText(NLMessages.getString("Editor_add_location_tooltip"));
				addLocation.setImage(_imageReg.get(IconsInternal.ADD));
				addLocation.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getLocation() == null)
						{
							_currentReference.setLocation(new LocationMods());
						}
						if (_currentReference.getLocation().getUrl() == null)
						{
							_currentReference.getLocation().setUrl(" "); //$NON-NLS-1$
						}
						notifyObservers();
					}
				});
			}
			if (_currentReference.getLocation() == null
					|| _currentReference.getLocation().getPhysicalLocation() == null)
			{
				final Button addLocation = new Button(rowButtonComp, SWT.PUSH);
				addLocation.setText(NLMessages.getString("Editor_physicalLocation"));
				addLocation.setToolTipText(NLMessages.getString("Editor_add_location_tooltip"));
				addLocation.setImage(_imageReg.get(IconsInternal.ADD));
				addLocation.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getLocation() == null)
						{
							_currentReference.setLocation(new LocationMods());
						}
						if (_currentReference.getLocation().getPhysicalLocation() == null)
						{
							_currentReference.getLocation().setPhysicalLocation(" "); //$NON-NLS-1$
						}
						notifyObservers();
					}
				});
			}
			if (_currentReference.getLocation() == null || _currentReference.getLocation().getShelfLocator() == null)
			{
				final Button addLocation = new Button(rowButtonComp, SWT.PUSH);
				addLocation.setText(NLMessages.getString("Editor_shelfLocator"));
				addLocation.setToolTipText(NLMessages.getString("Editor_add_location_tooltip"));
				addLocation.setImage(_imageReg.get(IconsInternal.ADD));
				addLocation.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getLocation() == null)
						{
							_currentReference.setLocation(new LocationMods());
						}
						if (_currentReference.getLocation().getShelfLocator() == null)
						{
							_currentReference.getLocation().setShelfLocator(" "); //$NON-NLS-1$
						}
						notifyObservers();
					}
				});
			}
		}
		// if (_currentReference.getIdentifiersMods() == null ||
		// _currentReference.getIdentifiersMods().isEmpty())
		// {
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());

			final Button addIdentis = new Button(rowButtonComp, SWT.PUSH);
			addIdentis.setText(NLMessages.getString("Editor_add_identifiers"));
			addIdentis.setToolTipText(NLMessages.getString("Editor_add_identifiers_tooltip"));
			addIdentis.setImage(_imageReg.get(IconsInternal.ADD));
			addIdentis.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					if (_currentReference.getIdentifiersMods() == null)
					{
						_currentReference.setIdentifiersMods(new Vector<IdentifierMods>(1));
					}
					IdentifierMods i = new IdentifierMods();
					i.setIdentifier(" "); //$NON-NLS-1$
					i.setType("ISBN-13"); //$NON-NLS-1$
					_currentReference.getIdentifiersMods().add(i);

					notifyObservers();
				}
			});
			// }

			if (_currentReference.getNote() == null || _currentReference.getNote().getNote() == null)
			{

				final Button addNote = new Button(rowButtonComp, SWT.PUSH);
				addNote.setText(NLMessages.getString("Editor_add_note"));
				addNote.setToolTipText(NLMessages.getString("Editor_add_note_tooltip"));
				addNote.setImage(_imageReg.get(IconsInternal.ADD));
				addNote.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getNote() == null)
						{
							_currentReference.setNote(new Note());
						}
						_currentReference.getNote().setNote(" "); //$NON-NLS-1$
						_currentReference.getNote().setType(" ");
						notifyObservers();
					}
				});
			}
			if (_currentReference.getAccessCondition() == null)
			{

				final Button addAccessCon = new Button(rowButtonComp, SWT.PUSH);
				addAccessCon.setText(NLMessages.getString("Editor_add_access"));
				addAccessCon.setToolTipText(NLMessages.getString("Editor_add_access_tooltip"));
				addAccessCon.setImage(_imageReg.get(IconsInternal.ADD));
				addAccessCon.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (_currentReference.getAccessCondition() == null)
						{
							_currentReference.setAccessCondition(new AccessCondition());
						}
						_currentReference.getAccessCondition().setAccessCondition(" "); //$NON-NLS-1$
						_currentReference.getAccessCondition().setType("restrictions"); //$NON-NLS-1$

						notifyObservers();
					}
				});
			}
		}
		{
			Composite rowButtonComp = new Composite(_addButtonComp, SWT.NONE);
			rowButtonComp.setLayoutData(new GridData());
			((GridData) rowButtonComp.getLayoutData()).widthHint = 370;
			((GridData) rowButtonComp.getLayoutData()).grabExcessHorizontalSpace = false;
			rowButtonComp.setLayout(new RowLayout());
			if (_currentReference.getRelatedItems() == null || _currentReference.getRelatedItems().size() == 0
					|| _currentReference.getRelatedItems().firstElement() == null)
			{

				final Button addAccessCon = new Button(rowButtonComp, SWT.PUSH);
				addAccessCon.setText(NLMessages.getString("Editor_add_related_item"));
				addAccessCon.setToolTipText(NLMessages.getString("Editor_add_related_item_tooltip"));
				addAccessCon.setImage(_imageReg.get(IconsInternal.ADD));
				addAccessCon.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						RelatedItem relItem = new RelatedItem();
						relItem.setType("host"); //$NON-NLS-1$
						if (_currentReference.getRelatedItems() == null)
						{
							_currentReference.setRelatedItems(new Vector<RelatedItem>(1));
						}
						_currentReference.getRelatedItems().add(relItem); //$NON-NLS-1$
						notifyObservers();
					}
				});
			}
			if (_currentReference.getRelatedItems() == null
					|| _currentReference.getRelatedItems().isEmpty()
					|| _currentReference.getRelatedItems().firstElement() == null
					|| _currentReference.getRelatedItems().firstElement().getPart() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDetailVolume() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDetailVolume().getNumber() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDetailIssue() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDetailIssue().getNumber() == null)

			{

				final Button addVolumeIssue = new Button(rowButtonComp, SWT.PUSH);
				addVolumeIssue.setText(NLMessages.getString("Editor_add_related_item") + " "
						+ NLMessages.getString("Editor_add_volume"));
				addVolumeIssue
						.setToolTipText("Add Information about the Volume, Issue and Pages where this reference appeared in.");
				addVolumeIssue.setImage(_imageReg.get(IconsInternal.ADD));
				addVolumeIssue.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						RelatedItem relItem;
						if (_currentReference.getRelatedItems() == null)
						{
							_currentReference.setRelatedItems(new Vector<RelatedItem>(1));
						}
						if (_currentReference.getRelatedItems().isEmpty()
								|| _currentReference.getRelatedItems().firstElement() == null)
						{
							relItem = new RelatedItem();
							relItem.setType("host"); //$NON-NLS-1$)
							_currentReference.getRelatedItems().add(relItem); //$NON-NLS-1$
						}
						if (_currentReference.getRelatedItems().firstElement().getPart() == null)
						{
							_currentReference.getRelatedItems().firstElement().setPart(new PartMods());
						}
						relItem = _currentReference.getRelatedItems().firstElement();
						if (relItem.getPart().getDetailVolume() == null)
						{
							final DetailMods detailVolume = new DetailMods();
							detailVolume.setType("volume");
							detailVolume.setNumber("");
							relItem.getPart().setDetails(new Vector<DetailMods>(2));
							relItem.getPart().getDetails().add(detailVolume);
						}
						else
						{
							relItem.getPart().getDetailVolume().setNumber("");
						}
						if (relItem.getPart().getDetailIssue() == null)
						{
							DetailMods detailIssue = new DetailMods();
							detailIssue.setType("issue");
							detailIssue.setNumber("");
							relItem.getPart().getDetails().add(detailIssue);
						}
						else
						{
							relItem.getPart().getDetailIssue().setNumber("");
						}
						if (relItem.getPart().getExtendPages() == null)
						{
							ExtendMods extendPages = new ExtendMods();
							extendPages.setUnit("pages");
							extendPages.setStart("");
							extendPages.setEnd("");
							relItem.getPart().setExtendsMods(new Vector<ExtendMods>(2));
							relItem.getPart().getExtendsMods().add(extendPages);
						}
						else
						{
							relItem.getPart().getExtendPages().setStart("");
							relItem.getPart().getExtendPages().setEnd("");
						}
						notifyObservers();
					}
				});
			}
			if (_currentReference.getRelatedItems() == null || _currentReference.getRelatedItems().isEmpty()
					|| _currentReference.getRelatedItems().firstElement() == null
					|| _currentReference.getRelatedItems().firstElement().getPart() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDates() == null
					|| _currentReference.getRelatedItems().firstElement().getPart().getDates().isEmpty()
					|| _currentReference.getRelatedItems().firstElement().getPart().getDates().firstElement() == null)
			// ||
			// _currentReference.getRelatedItems().firstElement().getPart().getDates().firstElement().getYear()
			// == 0)
			{

				final Button addrelItemDate = new Button(rowButtonComp, SWT.PUSH);
				addrelItemDate.setText(NLMessages.getString("Editor_add_related_item") + " "
						+ NLMessages.getString("Editor_add_date_part"));
				addrelItemDate.setToolTipText(NLMessages.getString("Editor_add_related_item_tooltip"));
				addrelItemDate.setImage(_imageReg.get(IconsInternal.ADD));
				addrelItemDate.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{

						if (_currentReference.getRelatedItems() == null)
						{
							_currentReference.setRelatedItems(new Vector<RelatedItem>(1));
						}
						if (_currentReference.getRelatedItems().isEmpty()
								|| _currentReference.getRelatedItems().firstElement() == null)
						{
							RelatedItem relItem = new RelatedItem();
							relItem.setType("host"); //$NON-NLS-1$)
							_currentReference.getRelatedItems().add(relItem); //$NON-NLS-1$
						}
						if (_currentReference.getRelatedItems().firstElement().getPart() == null)
						{
							_currentReference.getRelatedItems().firstElement().setPart(new PartMods());
						}
						if (_currentReference.getRelatedItems().firstElement().getPart().getDates() == null)
						{
							_currentReference.getRelatedItems().firstElement().getPart()
									.setDates(new Vector<PdrDate>(1));
						}
						if (_currentReference.getRelatedItems().firstElement().getPart().getDates().isEmpty()
								|| _currentReference.getRelatedItems().firstElement().getPart().getDates()
										.firstElement() == null)
						{
							_currentReference.getRelatedItems().firstElement().getPart().getDates()
									.add(new PdrDate(0, 0, 0));
						}

						notifyObservers();
					}
				});
			}
			rowButtonComp.layout();
			rowButtonComp.pack();
		}

		_addButtonComp.layout();
		_addButtonComp.pack();
		_mainComp.layout();
		_mainComp.pack();

	}

	/**
	 * Notify observers.
	 */
	private void notifyObservers()
	{
		createContentArea();
		for (Observer o : _observers)
		{
			o.update(null, null);
		}
	}

	/**
	 * Open help.
	 */
	protected final void openHelp()
	{
		_parentShell.setFocus();

		MessageBox box = new MessageBox(_parentShell, SWT.ICON_INFORMATION);
		box.setText("Info");
		box.setMessage("Here is where we'd show some information.");
		box.open();
	}

	/**
	 * Open url.
	 */
	protected final void openURL()
	{
		MessageBox box = new MessageBox(_parentShell, SWT.ICON_INFORMATION);
		box.setText("Eclipse.org");
		box.setMessage("Here is where we'd open the URL.");
		box.open();
	}

	/**
	 * Sets the reference.
	 * @param reference the new reference
	 */
	public final void setReference(final ReferenceMods reference)
	{
		_currentReference = reference;
	}

	/**
	 * Sets the tool tip text.
	 * @param toolTipText the new tool tip text
	 */
	public final void setToolTipText(final String toolTipText)
	{
		this._toolTipText = toolTipText;
		if (_link != null && !_link.isDisposed())
		{
			_link.setText(toolTipText);
		}
	}

}

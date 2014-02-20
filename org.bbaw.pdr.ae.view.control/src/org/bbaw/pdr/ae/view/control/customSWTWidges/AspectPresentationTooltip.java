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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.facade.RightsChecker;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupPresentation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class MarkupTooltip.
 * @author Christoph Plutte
 */
public class AspectPresentationTooltip extends CustomTooltipMouseListener
{

	/** The parent shell. */
	private Shell _parentShell;

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** The Constant HEADER_BG_COLOR. */
	public static final String HEADER_BG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_BG_COLOR";

	/** The Constant HEADER_FG_COLOR. */
	public static final String HEADER_FG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_FG_COLOR";

	/** The Constant HEADER_FONT. */
	public static final String HEADER_FONT = Policy.JFACE + ".TOOLTIP_HEAD_FONT";

	/** The Constant HEADER_CLOSE_ICON. */
	public static final String HEADER_CLOSE_ICON = Policy.JFACE + ".TOOLTIP_CLOSE_ICON";

	private Composite _parentComposite;
	private RightsChecker _richtsChecker = new RightsChecker();
	private Aspect _aspect;
	/** The _provider. */
	private String _provider;
	private Facade _facade = Facade.getInstanz();

	private SelectionAdapter _aspectsSelectionAdapter;


	public AspectPresentationTooltip(final Control control, Aspect aspect)
	{
		super(control);
		this._parentShell = control.getShell();
		this._aspect = aspect;
		_provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
		createAdditionalAspectsSelectionAdapter();
	}

	/**
	 * Creates the content area.
	 * @param parent the parent
	 * @return the composite
	 */
	protected final Composite createContentArea(final Composite parent)
	{
		this._parentComposite = parent;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;
		comp.setLayout(layout);
		final IMarkupPresentation markupPresentation = ControlExtensions.createMarkupPresentation();

		if (markupPresentation != null)
		{
			Composite textComp = new Composite(comp, SWT.LEFT | SWT.BORDER);
			textComp.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			textComp.setLayoutData(new GridData());
			((GridData) textComp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) textComp.getLayoutData()).horizontalAlignment = SWT.FILL;

			textComp.setLayout(new GridLayout());
			((GridLayout) textComp.getLayout()).numColumns = 2;
			((GridLayout) textComp.getLayout()).marginHeight = 0;
			((GridLayout) textComp.getLayout()).marginWidth = 0;

			markupPresentation.setAspect(_aspect);
			markupPresentation.setComposite(textComp);
			
			markupPresentation.createPresentation();

			Composite rightcomp = new Composite(textComp, SWT.RIGHT | SWT.TOP);
			rightcomp.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			rightcomp.setLayout(new GridLayout());
			((GridLayout) rightcomp.getLayout()).numColumns = 2;
			rightcomp.setLayoutData(new GridData());
			((GridData) rightcomp.getLayoutData()).verticalAlignment = SWT.TOP;
			((GridData) rightcomp.getLayoutData()).horizontalAlignment = SWT.RIGHT;

			Label blancLabel = new Label(rightcomp, SWT.NONE);
			blancLabel.setText("");
			blancLabel.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			blancLabel.setLayoutData(new GridData());
			blancLabel.pack();

			final Button editButton = new Button(rightcomp, SWT.PUSH);
			editButton.setImage(_imageReg.get(IconsInternal.EDIT));
			editButton.setToolTipText("Aspekt in Editor bearbeiten");
			editButton.setData(markupPresentation);
			editButton.setEnabled((_richtsChecker.mayWrite(_aspect)));
			editButton.setLayoutData(new GridData());
			((GridData) editButton.getLayoutData()).verticalAlignment = SWT.TOP;
			((GridData) editButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
			editButton.addSelectionListener(new SelectionAdapter()
			{

				@Override
				public void widgetSelected(SelectionEvent se)
				{
					IMarkupPresentation current = (IMarkupPresentation) editButton.getData(); //$NON-NLS-1$
					_facade.setCurrentAspect(current.getAspect());

					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
							.getService(IHandlerService.class);
					try
					{
						AspectPresentationTooltip.this.hide();

						handlerService.executeCommand(
								"org.bbaw.pdr.ae.view.main.commands.CallAspectEditor", null); //$NON-NLS-1$

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

			});
			editButton.pack();

			for (SemanticStm sStm : _aspect.getSemanticDim().getSemanticStms())
			{
				Label imLabel = new Label(rightcomp, SWT.NONE);
				if (sStm.getLabel().startsWith("NormName")
						|| _facade.getPersonDisplayNameTags(_provider).contains(sStm.getLabel()))
				{
					imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM));
				}
				else if (sStm.getLabel().equals("Name")
						|| _facade.getPersonNameTags(_provider).contains(sStm.getLabel()))
				{
					imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME));
				}
				else
				{
					imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION));
				}
				imLabel.setLayoutData(new GridData());
				imLabel.pack();
				Label semantic = new Label(rightcomp, SWT.NONE);
				semantic.setText(PDRConfigProvider.getSemanticLabel(sStm.getProvider(), sStm.getLabel()));
				semantic.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
				semantic.setLayoutData(new GridData());
				semantic.pack();
			}

			for (RelationStm rStm : _aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject().equals(_aspect.getPdrId()) && rStm.getRelations() != null
						&& rStm.getRelations().firstElement() != null
						&& rStm.getRelations().firstElement().getObject() != null)
				{
					Label imLabel = new Label(rightcomp, SWT.NONE);
					imLabel.setToolTipText(NLMessages.getString("View_message_aspect_belongsto"));
					final String id = rStm.getRelations().firstElement().getObject().toString();
					String name = id;
					PdrObject obj = _facade.getPdrObject(new PdrId(id));
					if (obj != null)
					{
						name = obj.getDisplayName();
						if (id.startsWith("pdrPo")) //$NON-NLS-1$
						{
							imLabel.setImage(_imageReg.get(IconsInternal.PERSON));
						}
						if (id.startsWith("pdrAo")) //$NON-NLS-1$
						{
							imLabel.setImage(_imageReg.get(IconsInternal.ASPECT));
						}
						if (id.startsWith("pdrRo")) //$NON-NLS-1$
						{
							imLabel.setImage(_imageReg.get(IconsInternal.REFERENCE));
						}
					}
					imLabel.setLayoutData(new GridData());
					imLabel.pack();
					Link person = new Link(rightcomp, SWT.NONE);
					person.addSelectionListener(_aspectsSelectionAdapter);
					person.setText("<a href=\"native\">" + name + "</a>");
					person.setData(id);
					person.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
					person.setLayoutData(new GridData());
					person.pack();

				}

					
				}
			final MarkupTooltip markupTooltipLabel = new MarkupTooltip(textComp);
			markupTooltipLabel.setPopupDelay(0);
			markupTooltipLabel.setHideOnMouseDown(true);
			markupTooltipLabel.deactivate();

			markupPresentation.addMarkupSelectionListener(new Listener()
			{

				@Override
				public void handleEvent(final Event event)
				{
					TaggingRange tr = (TaggingRange) event.data;
					if (tr != null)
					{
						String message;
						if (!tr.getName().equals("date")) //$NON-NLS-1$
						{
							message = NLMessages.getString("View_markupName")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
							if (tr.getType() != null)
							{
								message = message + NLMessages.getString("View_type")
										+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null)
										+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
							}
							if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
							{
								message = message
										+ NLMessages.getString("View_subtype")
										+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(),
												tr.getSubtype(),
												null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
							}
							if (tr.getRole() != null && tr.getRole().trim().length() > 0)
							{
								message = message
										+ NLMessages.getString("View_role")
										+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(),
												tr.getSubtype(),
												tr.getRole()) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
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
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
							if (tr.getType() != null)
							{
								message = message + NLMessages.getString("View_type")
										+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null)
										+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
							}
							if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
							{
								message = message
										+ NLMessages.getString("View_subtype")
										+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(),
												tr.getSubtype(),
												null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
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
								message = message
										+ NLMessages.getString("View_notBefore") + tr.getNotBefore().toString(); //$NON-NLS-1$

							}
							if (tr.getNotAfter() != null)
							{
								message = message
										+ "\n" + NLMessages.getString("View_NotAfter") + tr.getNotAfter().toString(); //$NON-NLS-1$

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
						// System.out.println("open message " + message);
						markupTooltipLabel.setToolTipText(message);
						// FIXME erneuern.

						markupTooltipLabel.activate();
						markupTooltipLabel.show(new Point(event.x + 5, event.y + 10));
					}
					else
					{
						markupTooltipLabel.hide();
					}

				}
			});
			rightcomp.layout();
			rightcomp.pack();
			// Point p = _text.computeSize(250, SWT.DEFAULT);
			// _text.setSize(p);
			// _text.setLayoutData(new GridData(p.x, p.y));
			// // _text.redraw();
			// // _text.update();
			// _text.pack(true);

		}
		return comp;
	}

	/**
	 * Creates the additional aspects selection adapter.
	 */
	private void createAdditionalAspectsSelectionAdapter()
	{
		_aspectsSelectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent ev)
			{
				//			 System.out.println("Selection: " + ev.text); //$NON-NLS-1$
				Link button = (Link) ev.getSource();
				Event event = new Event();
				event.data = button.getData();
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService
							.executeCommand("org.bbaw.pdr.ae.view.main.commands.OpenAdditionalAspectsView", event); //$NON-NLS-1$
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
	}
	@Override
	protected final Composite createToolTipContentArea(final Event event, final Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);

		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.verticalSpacing = 1;
		comp.setLayout(gl);

		Composite topArea = new Composite(comp, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.widthHint = 200;
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
		l.setText(_aspect.getDisplayNameWithID());
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
				// MarkupTooltip.this.dispose();
				openHelp();
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
				// MarkupTooltip.this.dispose();
			}
		});

		createContentArea(comp).setLayoutData(new GridData(GridData.FILL_BOTH));

		return comp;
	}

	/**
	 * Open help.
	 */
	protected void openHelp()
	{
		// parentShell.setFocus();
		//
		// MessageBox box = new MessageBox(parentShell,SWT.ICON_INFORMATION);
		// box.setText("Info");
		// box.setMessage("Here is where we'd show some information.");
		// box.open();
	}

	/**
	 * Open url.
	 */
	protected void openURL()
	{
		// MessageBox box = new MessageBox(parentShell,SWT.ICON_INFORMATION);
		// box.setText("Eclipse.org");
		// box.setMessage("Here is where we'd open the URL.");
		// box.open();
	}

}

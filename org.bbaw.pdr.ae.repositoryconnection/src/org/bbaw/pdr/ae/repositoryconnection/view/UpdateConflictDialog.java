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
package org.bbaw.pdr.ae.repositoryconnection.view;

import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.PDRObjectsConflict;
import org.bbaw.pdr.ae.repositoryconnection.internal.Activator;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.RevisionHistoryToolTip;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupPresentation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author cplutte Class creates login dialog for entering the repository
 *         settings.
 */
public class UpdateConflictDialog extends TitleAreaDialog
{

	/** The conflicting aspects. */
	private Vector<PDRObjectsConflict> _conflictingAspects;

	/** The conflicting persons. */
	private Vector<PDRObjectsConflict> _conflictingPersons;

	/** The conflicting references. */
	private Vector<PDRObjectsConflict> _conflictingReferences;

	/** The parent shell. */
	private Shell _parentShell;

	/** The main r composite. */
	private Composite _mainRComposite;

	/** The main p composite. */
	private Composite _mainPComposite;

	/** The main a composite. */
	private Composite _mainAComposite;

	/** The scroll r composite. */
	private ScrolledComposite _scrollRComposite;

	/** The scroll p composite. */
	private ScrolledComposite _scrollPComposite;

	/** The scroll a composite. */
	private ScrolledComposite _scrollAComposite;

	/** The save button. */
	private Button _saveButton;

	/** The resources. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** __facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	/** The GRA y_ color. */
	private static final Color GRAY_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	/** The GREE n_ color. */
	private static final Color GREEN_COLOR = resources.createColor(new RGB(186, 255, 183));

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/** The provider. */
	private String _provider;

	/**
	 * Instantiates a new update conflict dialog.
	 * @param parentShell the parent shell
	 * @param conflictingAspects the conflicting aspects
	 * @param conflictingPersons the conflicting persons
	 * @param conflictingReferences the conflicting references
	 */
	public UpdateConflictDialog(final Shell parentShell, final Vector<PDRObjectsConflict> conflictingAspects,
			final Vector<PDRObjectsConflict> conflictingPersons, final Vector<PDRObjectsConflict> conflictingReferences)
	{
		super(parentShell);
		this._conflictingAspects = conflictingAspects;
		this._conflictingPersons = conflictingPersons;
		this._conflictingReferences = conflictingReferences;
	}

	/**
	 * Builds the aspects comp.
	 */
	private void buildAspectsComp()
	{
		if (_scrollAComposite != null)
		{
			_scrollAComposite.dispose();
		}
		_scrollAComposite = new ScrolledComposite(_mainAComposite, SWT.BORDER | SWT.V_SCROLL);
		_scrollAComposite.setExpandHorizontal(true);
		_scrollAComposite.setExpandVertical(true);
		_scrollAComposite.setMinHeight(1);
		_scrollAComposite.setMinWidth(1);

		_scrollAComposite.setLayout(new GridLayout());
		_scrollAComposite.setLayoutData(new GridData());
		((GridData) _scrollAComposite.getLayoutData()).heightHint = 490;
		((GridData) _scrollAComposite.getLayoutData()).widthHint = 740;
		((GridData) _scrollAComposite.getLayoutData()).horizontalSpan = 1;

		((GridData) _scrollAComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollAComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollAComposite.pack();

		Composite contentCompA = new Composite(_scrollAComposite, SWT.NONE);
		contentCompA.setLayout(new GridLayout());
		_scrollAComposite.setContent(contentCompA);

		// System.out.println("build asp comp");
		loadObjects(_scrollAComposite, _conflictingAspects, 0, 10);
		_scrollAComposite.layout();
		_scrollAComposite.pack();
		_mainAComposite.layout();
		_mainAComposite.pack();
	}

	/**
	 * Builds the person comp.
	 */
	private void buildPersonComp()
	{
		if (_scrollPComposite != null)
		{
			_scrollPComposite.dispose();
		}
		_scrollPComposite = new ScrolledComposite(_mainPComposite, SWT.BORDER | SWT.V_SCROLL);
		_scrollPComposite.setExpandHorizontal(true);
		_scrollPComposite.setExpandVertical(true);
		_scrollPComposite.setMinHeight(1);
		_scrollPComposite.setMinWidth(1);

		_scrollPComposite.setLayout(new GridLayout());
		_scrollPComposite.setLayoutData(new GridData());
		((GridData) _scrollPComposite.getLayoutData()).heightHint = 490;
		((GridData) _scrollPComposite.getLayoutData()).widthHint = 740;
		((GridData) _scrollPComposite.getLayoutData()).horizontalSpan = 1;

		((GridData) _scrollPComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollPComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollPComposite.pack();

		Composite contentCompP = new Composite(_scrollPComposite, SWT.NONE);
		contentCompP.setLayout(new GridLayout());
		_scrollPComposite.setContent(contentCompP);

		loadObjects(_scrollPComposite, _conflictingPersons, 0, 10);

	}

	/**
	 * Builds the reference comp.
	 */
	private void buildReferenceComp()
	{
		if (_scrollRComposite != null)
		{
			_scrollRComposite.dispose();
		}
		_scrollRComposite = new ScrolledComposite(_mainRComposite, SWT.BORDER | SWT.V_SCROLL);
		_scrollRComposite.setExpandHorizontal(true);
		_scrollRComposite.setExpandVertical(true);
		_scrollRComposite.setMinHeight(1);
		_scrollRComposite.setMinWidth(1);

		_scrollRComposite.setLayout(new GridLayout());
		_scrollRComposite.setLayoutData(new GridData());
		((GridData) _scrollRComposite.getLayoutData()).heightHint = 490;
		((GridData) _scrollRComposite.getLayoutData()).widthHint = 740;
		((GridData) _scrollRComposite.getLayoutData()).horizontalSpan = 1;

		((GridData) _scrollRComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollRComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollRComposite.pack();

		Composite contentCompR = new Composite(_scrollRComposite, SWT.NONE);
		contentCompR.setLayout(new GridLayout());
		_scrollRComposite.setContent(contentCompR);

		loadObjects(_scrollRComposite, _conflictingReferences, 0, 10);

	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle("Update Conflict Dialog");
		// Set the message
		setMessage("Please select the version - either local or form repository - you want to keep",
				IMessageProvider.WARNING);

	}

	/**
	 * Creates the aspect tap item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createAspectTapItem(final TabFolder mainTabFolder)
	{

		TabItem aspectTabItem = new TabItem(mainTabFolder, SWT.NONE);
		aspectTabItem.setText(NLMessages.getString("Editor_aspect")); //$NON-NLS-1$
		aspectTabItem.setImage(_imageReg.get(IconsInternal.ASPECTS));
		_mainAComposite = new Composite(mainTabFolder, SWT.NONE);
		_mainAComposite.setLayoutData(new GridData());
		((GridData) _mainAComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainAComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		_mainAComposite.setLayout(new GridLayout());
		((GridLayout) _mainAComposite.getLayout()).numColumns = 1;
		((GridLayout) _mainAComposite.getLayout()).makeColumnsEqualWidth = false;

		aspectTabItem.setControl(_mainAComposite);

		buildAspectsComp();
		_mainAComposite.layout();
		_mainAComposite.pack();

	}

	// private void writeAspect2Text(Aspect ca, Composite comp, final StyledText
	// text,
	// boolean fromRepository) {
	// GridData gd = new GridData();
	// gd.grabExcessHorizontalSpace=true;
	// gd.horizontalAlignment = GridData.FILL;
	// gd.widthHint = 200;
	//
	//
	// text.setEditable(false);
	////	    	text.setData("category", categorieID); //$NON-NLS-1$
	//	    	text.setData("key", ca.getPdrId().toString()); //$NON-NLS-1$
	//	    	text.setData(NLMessages.getString("View_37"), ca.getPdrId().toString()); //$NON-NLS-1$
	//	    	text.setData("textOffset", 0); //$NON-NLS-1$
	//
	//
	// IStatus slaCaret = new Status(IStatus.INFO,Activator.PLUGIN_ID,
	// "CategoryView load aspect - text caret offset: " + text.getCaretOffset()); //$NON-NLS-1$
	// iLogger.log(slaCaret);
	//
	// final MarkupTooltip markupTooltipLabel = new MarkupTooltip(text);
	// markupTooltipLabel.setShift(new Point(0, 10));
	// markupTooltipLabel.setPopupDelay(0);
	// markupTooltipLabel.setHideOnMouseDown(false);
	// markupTooltipLabel.deactivate();
	//
	// text.addMouseMoveListener(new MouseMoveListener(){
	//
	// @Override
	// public void mouseMove(MouseEvent e) {
	// // Status sca = new Status(IStatus.INFO,Activator.PLUGIN_ID,
	//	"CategoryView current aspect: " + _facade.getCurrentAspect().getPdrId().toString()); //$NON-NLS-1$
	// // iLogger.log(sca);
	// // System.out.println("x " + e.x + " y " + e.y);
	// Point p = new Point(e.x, e.y);
	// if (p != null)
	// {
	// try
	// {
	// int offset = text.getOffsetAtLocation(p);
	// // System.out.println("offset " + offset);
	////							System.out.println(" offset " + offset + " zwischen 0 und der text.länge " + text.getText().length()); //$NON-NLS-1$ //$NON-NLS-2$
	// if (offset >= 1 && offset <= text.getText().length())
	// {
	//			    				int index = (Integer)text.getData("textOffset"); //$NON-NLS-1$
	// // int modifiedOffset = offset - index;
	////			    				System.out.println("index " + index + " modifiedOffset " + modifiedOffset); //$NON-NLS-1$ //$NON-NLS-2$
	// //FIXME hier ist der
	// if (text.getStyleRangeAtOffset(offset) != null ||
	// text.getStyleRangeAtOffset(offset - 1) != null)
	// {
	// markupTooltipLabel.activate();
	//
	//			//    					System.out.println(" es gibt eine SR bei " + modifiedOffset); //$NON-NLS-1$
	//			       			        IStatus sindex = new Status(IStatus.INFO,Activator.PLUGIN_ID, "CategoryView index: " + index); //$NON-NLS-1$
	// iLogger.log(sindex);
	// StyleRange sr;
	// @SuppressWarnings("unchecked")
	//									LinkedList<TaggingRange> rangeList = (LinkedList<TaggingRange>) text.getData("rangeList"); //$NON-NLS-1$
	// for (TaggingRange tr : rangeList)
	// {
	// sr = tr.getStyleRange();
	//
	////			    						System.out.println("TR " + tr.getName() + " index " + index + " sr.start " + sr.start + " length " + sr.length); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	////			    						System.out.println("offset " + offset); //$NON-NLS-1$
	//
	//			           			        IStatus ssr2 = new Status(IStatus.INFO,Activator.PLUGIN_ID, "CategoryView StyleRange: " + sr); //$NON-NLS-1$
	// iLogger.log(ssr2);
	//
	// int modifiedStart = sr.start + index;
	// int modifiedEnd = sr.start + index + sr.length;
	//
	// IStatus sms = new Status(IStatus.INFO, Activator.PLUGIN_ID,
	//			           			        		"CategoryView modifiedStart " + modifiedStart  //$NON-NLS-1$
	//			           			        		+ " length: " + sr.length + " modifiedEnd: " + modifiedEnd); //$NON-NLS-1$ //$NON-NLS-2$
	// iLogger.log(sms);
	//
	// if (modifiedStart <= offset && offset <= modifiedEnd)
	// {
	//
	// String message;
	//			    							if( !tr.getName().equals("date")) //$NON-NLS-1$
	// {
	//				    							message = NLMessages.getString("View_markupName") 	+ configProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n";   //$NON-NLS-1$//$NON-NLS-2$
	// if (tr.getType() != null)
	// {
	//				    								message = message + NLMessages.getString("View_type") + configProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n";  //$NON-NLS-1$//$NON-NLS-2$
	// }
	// if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
	// {
	//				    								message = message + NLMessages.getString("View_subtype") + configProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null) + "\n";  //$NON-NLS-1$//$NON-NLS-2$
	// }
	// if (tr.getRole() != null && tr.getRole().trim().length() > 0)
	// {
	//				    								message = message + NLMessages.getString("View_role") + configProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), tr.getRole()) + "\n";  //$NON-NLS-1$//$NON-NLS-2$
	// }
	// if (tr.getKey() != null && tr.getKey().trim().length() > 0)
	// {
	//				    								message = message + NLMessages.getString("View_key") + tr.getKey(); //$NON-NLS-1$
	// PdrObject o = _facade.getPdrObject(new PdrId(tr.getKey()));
	// if (o != null)
	// {
	//					    								message = message + " " + o.getDisplayName(); //$NON-NLS-1$
	// }
	// else
	// {
	// message = message +
	// NLMessages.getString("View_message_missing_dataObject");
	// }
	// }
	// if (tr.getAna() != null && tr.getAna().trim().length() > 0)
	// {
	// message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
	// PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
	// if (o != null)
	// {
	//					    								message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
	// }
	// else
	// {
	// message = message +
	// NLMessages.getString("View_message_missing_dataObject");
	// }
	// }
	// message = message + "\n" + NLMessages.getString("View_lb_content") +
	// text.getText(modifiedStart, modifiedEnd);
	// }
	//			    							else if (tr.getName().equals("date")) //$NON-NLS-1$
	// {
	//				    							message = NLMessages.getString("View_MarkupDate") + configProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n";   //$NON-NLS-1$//$NON-NLS-2$
	// if (tr.getType() != null)
	// {
	//				    								message = message + NLMessages.getString("View_type") + configProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n";  //$NON-NLS-1$//$NON-NLS-2$
	// }
	// if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
	// {
	//				    								message = message + NLMessages.getString("View_subtype") + configProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null) + "\n";  //$NON-NLS-1$//$NON-NLS-2$
	// }
	// if (tr.getWhen()!= null)
	// {
	//					    							message = message + NLMessages.getString("View_when") + tr.getWhen().toString(); //$NON-NLS-1$
	//
	// }
	// if (tr.getFrom() != null)
	// {
	//				    								message = message + NLMessages.getString("View_from") + tr.getFrom().toString(); //$NON-NLS-1$
	//
	// }
	// if (tr.getTo() != null)
	// {
	//					    							message = message + "\n" + NLMessages.getString("View_to") + tr.getTo().toString(); //$NON-NLS-1$
	//
	// }
	// if (tr.getNotBefore() != null)
	// {
	//				    								message = message + NLMessages.getString("View_notBefore") + tr.getNotBefore().toString(); //$NON-NLS-1$
	//
	// }
	// if (tr.getNotAfter()!= null)
	// {
	//					    							message = message + "\n" + NLMessages.getString("View_NotAfter") + tr.getNotAfter().toString(); //$NON-NLS-1$
	//
	// }
	// if (tr.getAna() != null && tr.getAna().trim().length() > 0)
	// {
	// message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
	// PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
	// if (o != null)
	// {
	//					    								message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
	// }
	// else
	// {
	// message = message +
	// NLMessages.getString("View_message_missing_dataObject");
	// }
	// }
	// message = message + "\n" + NLMessages.getString("View_lb_content") +
	// text.getText(modifiedStart, modifiedEnd);
	//
	// }
	// else
	// {
	//			    								message = NLMessages.getString("View_errorMarkupInfo"); //$NON-NLS-1$
	// }
	//			//	    							MessageDialog.openInformation(parentShell, "", message); //$NON-NLS-1$
	// // break;
	// markupTooltipLabel.setToolTipText(message);
	// }
	// }
	//
	// }
	// }
	// else
	// {
	// markupTooltipLabel.deactivate();
	//
	// }
	// }
	//
	// catch (IllegalArgumentException ex)
	// {
	// markupTooltipLabel.deactivate();
	// }
	//
	// }
	//
	// }
	// });
	//
	//
	//
	// String aContent = ca.getNotification();
	// String tempText;
	//
	// if ((tempText = aContent) != null)
	// {
	//	        	text.append("\n"); //$NON-NLS-1$
	// int index = text.getText().length();
	//	        	text.append(tempText); //$NON-NLS-1$
	//
	// /* PropertyRanges */
	// LinkedList<TaggingRange> rangeList = ca.getRangeList();
	// StyleRange sr;
	// if (rangeList != null) {
	// for (TaggingRange tr : rangeList) {
	// sr = (StyleRange)tr.getStyleRange().clone();
	// sr.start = sr.start + index;
	// text.setStyleRange(sr);
	// }
	// }
	//	            text.setData("rangeList", rangeList); //$NON-NLS-1$
	//	            text.setData("textOffset", index); //$NON-NLS-1$
	//		        IStatus soffset = new Status(IStatus.INFO,Activator.PLUGIN_ID, "CategoryView offset: "  + text.getData("textOffset")); //$NON-NLS-1$ //$NON-NLS-2$
	// iLogger.log(soffset);
	// }
	//	        text.append("\n"); //$NON-NLS-1$
	//	        text.append(" \n" + NLMessages.getString("CategoryView_id") + ca.getPdrId().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	//
	//
	// text.append(NLMessages.getString("View_lb_user") +
	// _facade.getObjectDisplayName(ca.getRecord().getRevisions().firstElement().getAuthority()));
	//
	// if (ca.getRelationDim().getRelationStms().size() > 1)
	// {
	//	        	text.append("\n" + NLMessages.getString("View_other_relations_dot")); //$NON-NLS-1$
	// }
	// for (RelationStm rStm : ca.getRelationDim().getRelationStms())
	// {
	// if (rStm.getSubject().equals(ca.getPdrId()))
	// {
	// if (_facade.getPdrObject(rStm.getRelations().firstElement().getObject())
	// != null)
	// {
	//        	        	text.append("\n"  + NLMessages.getString("View_aspect_of") + _facade.getPdrObject(rStm.getRelations().firstElement().getObject()).getDisplayName() + " (" + rStm.getRelations().firstElement().getObject().toString() + ")" ); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
	// }
	// else
	// {
	//    	        		text.append("\n"  + NLMessages.getString("View_object_dot") + NLMessages.getString("View_missing_object")+ " (" + rStm.getRelations().firstElement().getObject().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-4$ //$NON-NLS-5$
	// }
	// }
	// else
	// {
	// if (_facade.getPdrObject(rStm.getSubject()) != null)
	// {
	//    					text.append("\n"  + NLMessages.getString("View_relation_subject") + _facade.getPdrObject(rStm.getSubject()).getDisplayName()+ " (" + rStm.getSubject().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
	// }
	// else
	// {
	//    					text.append("\n" + NLMessages.getString("View_object_dot") + NLMessages.getString("View_missing_object") + " (" + rStm.getSubject().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-4$ //$NON-NLS-5$
	//    					text.append("\n" ); //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// for (Relation r : rStm.getRelations())
	// {
	// if (r.getContext() != null)
	// {
	//	        	        	text.append("\n" + NLMessages.getString("View_relation_context_dot") + r.getContext() + "   "); //$NON-NLS-1$ //$NON-NLS-3$
	//
	// }
	// if (r.getRClass() != null)
	// {
	//	        	        	text.append(NLMessages.getString("View_relation_class_dot") + r.getRClass() + "   "); //$NON-NLS-2$
	//
	// }
	// if (r.getRelation() != null)
	// {
	// text.append(NLMessages.getString("View_relation_value_dot") +
	// r.getRelation());
	//
	// }
	// if (r.getObject() != null)
	// {
	//	        	        	text.append("\n"  + NLMessages.getString("View_object_dot_delete") + _facade.getPdrObject(r.getObject()).getDisplayName()); //$NON-NLS-1$
	//
	// }
	// else
	// {
	//        	        		text.append("\n" + NLMessages.getString("View_object_dot_delete") + NLMessages.getString("View_missing_object")); //$NON-NLS-1$
	// }
	//        	        	text.append("\n"  ); //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// }
	//
	// }
	// for (ValidationStm vs : ca.getValidation().getValidationStms())
	// {
	// if (vs.getReference() != null && vs.getReference().getSourceId() != null)
	// {
	// if (_facade.getReference(vs.getReference().getSourceId()) != null)
	// {
	//	        			text.append("\n"  + NLMessages.getString("View_lbReference") + _facade.getReference(vs.getReference().getSourceId()).getDisplayNameLong()); //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// else
	// {
	//	        			text.append("\n" + NLMessages.getString("View_missing_reference") + " (" + vs.getReference().getSourceId().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
	// }
	//	        		text.append("\n"  ); //$NON-NLS-1$ //$NON-NLS-2$
	// }
	//
	// }
	//		text.append("\n"); //$NON-NLS-1$
	//	    text.setData("id", ca.getPdrId().toString()); //$NON-NLS-1$
	// text.setLayoutData(gd);
	//
	// // text.setBounds(0,0,100,100);
	// text.layout();
	// }
	//

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
		_saveButton = createOkButton(parent, OK, NLMessages.getString("Dialog_save"), true); //$NON-NLS-1$
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
		_provider = CommonActivator.getDefault().getPreferenceStore()
				.getString("PRIMARY_SEMANTIC_PROVIDER").toUpperCase(); //$NON-NLS-1$

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).minimumHeight = 90;
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) mainComposite.getLayout()).numColumns = 1;

		TabFolder mainTabFolder = new TabFolder(mainComposite, SWT.TOP | SWT.FILL);
		GridData gridData2 = new GridData();
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 1;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalAlignment = SWT.FILL;
		mainTabFolder.setLayoutData(gridData2);

		// System.out.println("createDialogArea");
		// System.out.println("conflictingAspects " +
		// _conflictingAspects.size());
		if (_conflictingAspects != null && !_conflictingAspects.isEmpty())
		{
			createAspectTapItem(mainTabFolder);
		}

		if (_conflictingPersons != null && !_conflictingPersons.isEmpty())
		{
			createPersonTapItem(mainTabFolder);
		}

		if (_conflictingReferences != null && !_conflictingReferences.isEmpty())
		{
			createReferenceTapItem(mainTabFolder);
		}

		mainTabFolder.layout();
		mainTabFolder.pack();

		mainComposite.layout();
		mainComposite.pack();
		parent.pack();

		return parent;
	}

	/**
	 * creates OKButton.
	 * @param parent parent composite
	 * @param id id
	 * @param label label
	 * @param defaultButton button default.
	 * @return button
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
	 * Creates the person tap item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createPersonTapItem(final TabFolder mainTabFolder)
	{
		TabItem personTabItem = new TabItem(mainTabFolder, SWT.NONE);
		personTabItem.setText(NLMessages.getString("Editor_person")); //$NON-NLS-1$
		personTabItem.setImage(_imageReg.get(IconsInternal.PERSONS));
		_mainPComposite = new Composite(mainTabFolder, SWT.NONE);
		_mainPComposite.setLayoutData(new GridData());
		((GridData) _mainPComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainPComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		_mainPComposite.setLayout(new GridLayout());
		((GridLayout) _mainPComposite.getLayout()).numColumns = 1;
		((GridLayout) _mainPComposite.getLayout()).makeColumnsEqualWidth = false;

		personTabItem.setControl(_mainPComposite);

		buildPersonComp();

	}

	/**
	 * Creates the reference tap item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createReferenceTapItem(final TabFolder mainTabFolder)
	{
		TabItem referenceTabItem = new TabItem(mainTabFolder, SWT.NONE);
		referenceTabItem.setText(NLMessages.getString("Editor_reference")); //$NON-NLS-1$
		referenceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCES));
		_mainRComposite = new Composite(mainTabFolder, SWT.NONE);
		_mainRComposite.setLayoutData(new GridData());
		((GridData) _mainRComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainRComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		_mainRComposite.setLayout(new GridLayout());
		((GridLayout) _mainRComposite.getLayout()).numColumns = 1;
		((GridLayout) _mainRComposite.getLayout()).makeColumnsEqualWidth = false;

		referenceTabItem.setControl(_mainRComposite);

		buildReferenceComp();

	}

	// We do not allow the user to resize this dialog
	/**
	 * @return true
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected final boolean isResizable()
	{
		return true;
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
	 * Load aspect.
	 * @param object the object
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void loadAspect(final PdrObject object, final Composite comp, final Text text,
			final boolean fromRepository)
	{
		Composite aspcomp = new Composite(comp, SWT.RIGHT | SWT.TOP);
		aspcomp.setLayout(new GridLayout());
		((GridLayout) aspcomp.getLayout()).numColumns = 1;
		((GridLayout) aspcomp.getLayout()).marginHeight = 0;
		((GridLayout) aspcomp.getLayout()).marginWidth = 0;
		aspcomp.setLayoutData(new GridData());
		((GridData) aspcomp.getLayoutData()).verticalAlignment = SWT.TOP;
		((GridData) aspcomp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) aspcomp.getLayoutData()).horizontalSpan = 2;
		
		Aspect ca = (Aspect) object;
		final IMarkupPresentation markupPresentation = ControlExtensions.createMarkupPresentation();
		markupPresentation.setAspect(ca);
		markupPresentation.setComposite(aspcomp);
		markupPresentation.createPresentation();
		if (fromRepository)
		{
			aspcomp.setBackground(GREEN_COLOR);
			markupPresentation.setBackground(GREEN_COLOR);
		}

		final MarkupTooltip markupTooltipLabel = new MarkupTooltip(comp);
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
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_subtype")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
											null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getRole() != null && tr.getRole().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_role")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
											tr.getRole())
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
								+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						if (tr.getType() != null)
						{
							message = message + NLMessages.getString("View_type")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_subtype")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
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
							message = message + NLMessages.getString("View_notBefore") + tr.getNotBefore().toString(); //$NON-NLS-1$

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
					// markupTooltipLabel.activate();
					markupTooltipLabel.show(new Point(event.x + 5, event.y + 10));
				}
				else
				{
					markupTooltipLabel.hide();
				}

			}
		});

		Composite rightcomp = new Composite(comp, SWT.RIGHT | SWT.TOP);
		rightcomp.setBackground(WHITE_COLOR);
		rightcomp.setLayout(new GridLayout());
		((GridLayout) rightcomp.getLayout()).numColumns = 2;
		rightcomp.setBackground(rightcomp.getParent().getBackground());
		rightcomp.setLayoutData(new GridData());
		((GridData) rightcomp.getLayoutData()).verticalAlignment = SWT.TOP;
		((GridData) rightcomp.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		for (SemanticStm sStm : ca.getSemanticDim().getSemanticStms())
		{
			Label imLabel = new Label(rightcomp, SWT.NONE);
			if (sStm.getLabel().startsWith("NormName")
					|| _facade.getPersonDisplayNameTags(_provider).contains(sStm.getLabel()))
			{
				imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM));
			}
			else if (sStm.getLabel().equals("Name") || _facade.getPersonNameTags(_provider).contains(sStm.getLabel()))
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
			semantic.setBackground(rightcomp.getParent().getBackground());
			semantic.setLayoutData(new GridData());
			semantic.pack();
		}
		for (RelationStm rStm : ca.getRelationDim().getRelationStms())
		{
			if (rStm.getSubject().equals(ca.getPdrId()) && rStm.getRelations() != null
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
				}
				imLabel.setLayoutData(new GridData());
				imLabel.pack();
				Label person = new Label(rightcomp, SWT.NONE);
				person.setText(name);
				person.setBackground(WHITE_COLOR);
				person.setBackground(rightcomp.getParent().getBackground());

				person.setLayoutData(new GridData());
				person.pack();

				rightcomp.layout();
				rightcomp.pack();
			}
		}
		Label historyLabel = new Label(rightcomp, SWT.SHADOW_IN);
		historyLabel.setText(NLMessages.getString("Editor_revision_history")); //$NON-NLS-1$
		historyLabel.setLayoutData(new GridData());
		final RevisionHistoryToolTip historyToolTip = new RevisionHistoryToolTip(historyLabel, ca.getRecord());
		historyToolTip.setShift(new Point(-25, -25));
		historyToolTip.setPopupDelay(0);
		historyToolTip.setHideOnMouseDown(true);
		historyToolTip.activate();
		historyLabel.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseDoubleClick(final MouseEvent e)
			{
			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				historyToolTip.show(new Point(e.x, e.y));
			}

			@Override
			public void mouseUp(final MouseEvent e)
			{
				historyToolTip.show(new Point(e.x, e.y));
			}
		});

	}

	/**
	 * Load object.
	 * @param object the object
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void loadObject(final PdrObject object, final Composite comp, final Text text,
			final boolean fromRepository)
	{

		if (fromRepository)
		{
			text.setBackground(GREEN_COLOR);
		}
		if (object instanceof Aspect)
		{
			loadAspect(object, comp, text, fromRepository);
		}
		if (object instanceof Person)
		{
			loadPerson(object, comp, text, fromRepository);
		}
		if (object instanceof ReferenceMods)
		{
			loadReference(object, comp, text, fromRepository);
		}

		text.pack();
	}

	/**
	 * meth. loads all aspects that belong to the same currently selected
	 * category.
	 * @param scrollComposite composite
	 * @param conflictingObjects conflicting objects
	 * @param startIndex start index
	 * @param number number
	 */
	private void loadObjects(final ScrolledComposite scrollComposite,
			final Vector<PDRObjectsConflict> conflictingObjects, final int startIndex, final int number)
	{
		final int start;
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComposite.setFocus();

		Composite comp = (Composite) scrollComposite.getContent();
		Control[] children = comp.getChildren();
		for (Control c : children)
		{
			c.dispose();
		}
		// Collections.sort(aspects, new AspectsByCronComparator());
		IStatus sla = new Status(IStatus.INFO, Activator.PLUGIN_ID,
				"UpdateConflictDialog load objects - number of objects: " + conflictingObjects.size()); //$NON-NLS-1$ //$NON-NLS-2$
		iLogger.log(sla);

		if (conflictingObjects != null)
		{
			final int size = conflictingObjects.size(); //$NON-NLS-1$
			Label eventNumber = new Label(comp, SWT.NONE);
			int endIndex = startIndex + 10;
			if (endIndex > size)
			{
				endIndex = size;
			}
			eventNumber.setText(NLMessages.getString("View_allTogether") + size + "Objects" + //$NON-NLS-1$ //$NON-NLS-2$
					(startIndex + 1) + " - " + endIndex); //$NON-NLS-1$
			if (size > 10)
			{

				start = startIndex;

				Group eventNavBar = new Group(comp, SWT.NONE);
				eventNavBar.setText(NLMessages.getString("View_scroll")); //$NON-NLS-1$
				eventNavBar.setLayout(new RowLayout());

				Button toStart = new Button(eventNavBar, SWT.PUSH);
				toStart.setText(" |< "); //$NON-NLS-1$
				toStart.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						loadObjects(scrollComposite, conflictingObjects, 0, number);
					}

				});
				final Button minusFifty = new Button(eventNavBar, SWT.PUSH);
				minusFifty.setText(" -50 "); //$NON-NLS-1$
				if (start - 50 < 0)
				{
					minusFifty.setEnabled(false);
				}
				else
				{
					minusFifty.setEnabled(true);
				}
				minusFifty.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 50;

						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
				final Button minusTen = new Button(eventNavBar, SWT.PUSH);
				minusTen.setText(" -10 "); //$NON-NLS-1$
				if (start - 10 < 0)
				{
					minusTen.setEnabled(false);
				}
				else
				{
					minusTen.setEnabled(true);
				}
				minusTen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 10;

						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
				final Button minusOne = new Button(eventNavBar, SWT.PUSH);
				minusOne.setText(" -1 "); //$NON-NLS-1$
				if (start - 1 < 0)
				{
					minusOne.setEnabled(false);
				}
				else
				{
					minusOne.setEnabled(true);
				}
				minusOne.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 1;
						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
				final Text jumpTo = new Text(eventNavBar, SWT.BORDER);
				jumpTo.setSize(15, 20);

				Button okButton = new Button(eventNavBar, SWT.PUSH);
				okButton.setText(NLMessages.getString("View_ok")); //$NON-NLS-1$
				okButton.setToolTipText(NLMessages.getString("View_jump_to_aspect_tooltip"));
				okButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						try
						{
							if (jumpTo.getText().length() > 0)
							{
								int n = Integer.parseInt(jumpTo.getText());

								if (n > 0 && n <= size)
								{
									loadObjects(scrollComposite, conflictingObjects, --n, number);
								}
							}
						}
						catch (NumberFormatException ex)
						{
							String message = NLMessages.getString("View_pleaseEnterNumber"); //$NON-NLS-1$
							MessageDialog.openInformation(_parentShell, NLMessages.getString("View_error"), message); //$NON-NLS-1$
							//
						}
					}
				});

				final Button plusOne = new Button(eventNavBar, SWT.PUSH);
				plusOne.setText(" +1 "); //$NON-NLS-1$
				if (start + 1 >= size)
				{
					plusOne.setEnabled(false);
				}
				else
				{
					plusOne.setEnabled(true);
				}
				plusOne.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 1;

						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
				final Button plusTen = new Button(eventNavBar, SWT.PUSH);
				plusTen.setText(" +10 "); //$NON-NLS-1$
				if (start + 10 >= size)
				{
					plusTen.setEnabled(false);
				}
				else
				{
					plusTen.setEnabled(true);
				}
				plusTen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 10;

						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
				final Button plusFifty = new Button(eventNavBar, SWT.PUSH);
				plusFifty.setText(" +50 "); //$NON-NLS-1$
				if (start + 50 >= size)
				{
					plusFifty.setEnabled(false);
				}
				else
				{
					plusFifty.setEnabled(true);
				}
				plusFifty.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 50;

						loadObjects(scrollComposite, conflictingObjects, nextStart, number);
					}

				});
			}
			else
			{
				start = 0;
			}

			int i = start;
			// int end = i + number;

			while (i < start + number)
			{
				if (size > i)
				{
					final PDRObjectsConflict oc = conflictingObjects.get(i); //$NON-NLS-1$
					if (oc != null)
					{

						Composite outerComp = new Composite(comp, SWT.LEFT | SWT.BORDER);
						outerComp.setBackground(WHITE_COLOR);
						outerComp.setLayoutData(new GridData());
						((GridData) outerComp.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) outerComp.getLayoutData()).horizontalAlignment = SWT.FILL;

						outerComp.setLayout(new GridLayout());
						((GridLayout) outerComp.getLayout()).numColumns = 2;
						((GridLayout) outerComp.getLayout()).marginHeight = 0;
						((GridLayout) outerComp.getLayout()).marginWidth = 0;

						Composite leftComp = new Composite(outerComp, SWT.LEFT | SWT.NONE);
						leftComp.setBackground(WHITE_COLOR);
						leftComp.setLayoutData(new GridData());
						((GridData) leftComp.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) leftComp.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) leftComp.getLayoutData()).verticalIndent = 4;

						leftComp.setLayout(new GridLayout());
						((GridLayout) leftComp.getLayout()).numColumns = 1;
						((GridLayout) leftComp.getLayout()).marginHeight = 0;
						((GridLayout) leftComp.getLayout()).marginWidth = 0;

						Composite buttonComp = new Composite(outerComp, SWT.LEFT | SWT.NONE);
						buttonComp.setBackground(WHITE_COLOR);
						buttonComp.setLayoutData(new GridData());
						// ((GridData)
						// buttonComp.getLayoutData()).grabExcessHorizontalSpace
						// = true;
						// ((GridData)
						// buttonComp.getLayoutData()).horizontalAlignment =
						// SWT.FILL;

						buttonComp.setLayout(new GridLayout());
						((GridLayout) buttonComp.getLayout()).numColumns = 1;
						((GridLayout) buttonComp.getLayout()).marginHeight = 0;
						((GridLayout) buttonComp.getLayout()).marginWidth = 0;

						Composite repoComp = new Composite(leftComp, SWT.LEFT | SWT.BORDER);
						repoComp.setBackground(GREEN_COLOR);
						repoComp.setLayoutData(new GridData());
						((GridData) repoComp.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) repoComp.getLayoutData()).horizontalAlignment = SWT.FILL;

						repoComp.setLayout(new GridLayout());
						((GridLayout) repoComp.getLayout()).numColumns = 2;
						((GridLayout) repoComp.getLayout()).marginHeight = 0;
						((GridLayout) repoComp.getLayout()).marginWidth = 0;

						Composite repLComp = new Composite(repoComp, SWT.LEFT | SWT.NONE);
						repLComp.setBackground(GREEN_COLOR);
						repLComp.setLayoutData(new GridData());
						((GridData) repLComp.getLayoutData()).horizontalSpan = 2;

						repLComp.setLayout(new GridLayout());
						((GridLayout) repLComp.getLayout()).numColumns = 3;
						((GridLayout) repLComp.getLayout()).marginHeight = 0;
						((GridLayout) repLComp.getLayout()).marginWidth = 0;

						new Label(repLComp, SWT.NONE).setImage(_imageReg.get(IconsInternal.REPOSITORY));
						Label rep = new Label(repLComp, SWT.NONE);
						rep.setBackground(rep.getParent().getBackground());
						rep.setText("Version from Repository");
						rep.pack();
						Label historyLabelRep = new Label(repLComp, SWT.NONE);
						historyLabelRep.setText(NLMessages.getString("Editor_revision_history")); //$NON-NLS-1$
						historyLabelRep.setBackground(historyLabelRep.getParent().getBackground());
						historyLabelRep.setLayoutData(new GridData());
						final RevisionHistoryToolTip historyToolTipRep = new RevisionHistoryToolTip(historyLabelRep, oc
								.getRepositoryObject().getRecord());
						historyToolTipRep.setShift(new Point(-25, -25));
						historyToolTipRep.setPopupDelay(0);
						historyToolTipRep.setHideOnMouseDown(true);
						historyToolTipRep.activate();
						historyLabelRep.addMouseListener(new MouseListener()
						{

							@Override
							public void mouseDoubleClick(final MouseEvent e)
							{
							}

							@Override
							public void mouseDown(final MouseEvent e)
							{
								historyToolTipRep.show(new Point(e.x, e.y));
							}

							@Override
							public void mouseUp(final MouseEvent e)
							{
								historyToolTipRep.show(new Point(e.x, e.y));
							}
						});
						historyLabelRep.pack();
						repLComp.layout();
						repLComp.pack();

						rep.setLayoutData(new GridData());
						((GridData) rep.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) rep.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) rep.getLayoutData()).horizontalSpan = 1;

						final Text textRepo = new Text(repoComp, SWT.WRAP | SWT.NO_BACKGROUND | SWT.NO_FOCUS
								| SWT.CURSOR_ARROW);
						if (oc.getRepositoryObject() != null)
						{
							loadObject(oc.getRepositoryObject(), repoComp, textRepo, true);
						}

						Composite localComp = new Composite(leftComp, SWT.LEFT | SWT.BORDER);
						localComp.setBackground(WHITE_COLOR);
						localComp.setLayoutData(new GridData());
						((GridData) localComp.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) localComp.getLayoutData()).horizontalAlignment = SWT.FILL;

						localComp.setLayout(new GridLayout());
						((GridLayout) localComp.getLayout()).numColumns = 2;
						((GridLayout) localComp.getLayout()).marginHeight = 0;
						((GridLayout) localComp.getLayout()).marginWidth = 0;

						Composite locLComp = new Composite(localComp, SWT.LEFT | SWT.NONE);
						locLComp.setBackground(WHITE_COLOR);
						locLComp.setLayoutData(new GridData());
						((GridData) locLComp.getLayoutData()).horizontalSpan = 2;

						locLComp.setLayout(new GridLayout());
						((GridLayout) locLComp.getLayout()).numColumns = 3;
						((GridLayout) locLComp.getLayout()).marginHeight = 0;
						((GridLayout) locLComp.getLayout()).marginWidth = 0;

						new Label(locLComp, SWT.NONE).setImage(_imageReg.get(IconsInternal.BACKUP));
						Label loc = new Label(locLComp, SWT.NONE);
						loc.setText("Local Version");
						loc.setBackground(loc.getParent().getBackground());
						loc.setLayoutData(new GridData());
						((GridData) loc.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) loc.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) loc.getLayoutData()).horizontalSpan = 1;

						Label historyLabelLoc = new Label(locLComp, SWT.NONE);
						historyLabelLoc.setText(NLMessages.getString("Editor_revision_history")); //$NON-NLS-1$
						historyLabelLoc.setBackground(historyLabelLoc.getParent().getBackground());
						historyLabelLoc.setLayoutData(new GridData());
						final RevisionHistoryToolTip historyToolTip = new RevisionHistoryToolTip(historyLabelLoc, oc
								.getLocalObject().getRecord());
						historyToolTip.setShift(new Point(-25, -25));
						historyToolTip.setPopupDelay(0);
						historyToolTip.setHideOnMouseDown(true);
						historyToolTip.activate();
						historyLabelLoc.addMouseListener(new MouseListener()
						{

							@Override
							public void mouseDoubleClick(final MouseEvent e)
							{
							}

							@Override
							public void mouseDown(final MouseEvent e)
							{
								historyToolTip.show(new Point(e.x, e.y));
							}

							@Override
							public void mouseUp(final MouseEvent e)
							{
								historyToolTip.show(new Point(e.x, e.y));
							}
						});
						historyLabelLoc.pack();
						loc.pack();
						locLComp.layout();
						locLComp.pack();

						final Text textLocal = new Text(localComp, SWT.WRAP | SWT.NO_BACKGROUND | SWT.NO_FOCUS
								| SWT.CURSOR_ARROW);

						if (oc.getLocalObject() != null)
						{
							loadObject(oc.getLocalObject(), localComp, textLocal, false);
						}

						final Button[] radios = new Button[3];
						SelectionListener refListener = new SelectionAdapter()
						{
							@Override
							public void widgetDefaultSelected(final SelectionEvent e)
							{
							}

							@Override
							public void widgetSelected(final SelectionEvent e)
							{
								final int obj = (Integer) ((Button) e.getSource()).getData("obj");
								Text stextRepo = (Text) ((Button) e.getSource()).getData("repo");
								Text stextLocal = (Text) ((Button) e.getSource()).getData("local");

								if (obj == 0)
								{
									oc.setOverrideLocal(true);
									oc.setKeepLocal(false);
									if (stextLocal != null)
									{
										textLocal.setForeground(GRAY_COLOR);
										// text.setForeground(GRAY_COLOR);

										textLocal.getParent().layout();
									}
									if (textRepo != null)
									{
										stextRepo.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
									}
								}

								else if (obj == 2)
								{
									oc.setOverrideLocal(false);
									oc.setKeepLocal(true);
									if (stextRepo != null)
									{
										stextRepo.setForeground(GRAY_COLOR);
										stextRepo.getParent().layout();
									}
									if (stextLocal != null)
									{
										stextLocal.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
									}
								}
								else
								{
									oc.setOverrideLocal(false);
									oc.setKeepLocal(false);
									if (stextLocal != null)
									{
										stextLocal.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
									}
									if (stextRepo != null)
									{
										stextRepo.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
									}
								}

							}
						};
						radios[0] = new Button(buttonComp, SWT.RADIO | SWT.TOP);
						radios[0].setText("Override Local Copy");
						radios[0].setData("obj", 0);
						radios[0].setData("repo", textRepo);
						radios[0].setData("local", textLocal);
						radios[0].setBackground(radios[0].getParent().getBackground());
						radios[0].addSelectionListener(refListener);
						radios[0].setLayoutData(new GridData());
						((GridData) radios[0].getLayoutData()).verticalIndent = 18;

						new Label(buttonComp, SWT.NONE).setText("");

						radios[1] = new Button(buttonComp, SWT.RADIO | SWT.BOTTOM);
						radios[1].setText("Resolve later");
						radios[1].setData("obj", 1);
						radios[1].setData("repo", textRepo);
						radios[1].setData("local", textLocal);
						radios[1].setBackground(radios[1].getParent().getBackground());

						radios[1].addSelectionListener(refListener);
						radios[1].setLayoutData(new GridData());
						((GridData) radios[1].getLayoutData()).verticalAlignment = SWT.END;

						new Label(buttonComp, SWT.NONE).setText("");

						radios[2] = new Button(buttonComp, SWT.RADIO | SWT.BOTTOM);
						radios[2].setText("Keep Local Copy");
						radios[2].setData("obj", 2);
						radios[2].setData("repo", textRepo);
						radios[2].setData("local", textLocal);
						radios[2].setBackground(radios[2].getParent().getBackground());

						radios[2].addSelectionListener(refListener);
						radios[2].setLayoutData(new GridData());
						((GridData) radios[2].getLayoutData()).verticalAlignment = SWT.END;

						new Label(buttonComp, SWT.NONE).setText("");

						if (oc.isOverrideLocal())
						{
							radios[0].setSelection(true);
							radios[1].setSelection(false);
							radios[2].setSelection(false);
							oc.setOverrideLocal(true);
							oc.setKeepLocal(false);
							if (textLocal != null)
							{
								textLocal.setForeground(GRAY_COLOR);
								textLocal.getParent().layout();
							}
							if (textRepo != null)
							{
								textRepo.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
							}
						}

						else if (oc.isKeepLocal())
						{
							radios[0].setSelection(false);
							radios[1].setSelection(false);
							radios[2].setSelection(true);
							oc.setOverrideLocal(false);
							oc.setKeepLocal(true);
							if (textRepo != null)
							{
								textRepo.setForeground(GRAY_COLOR);
								textRepo.getParent().layout();
							}
							if (textLocal != null)
							{
								textLocal.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
							}
						}
						else
						{
							radios[0].setSelection(false);
							radios[1].setSelection(true);
							radios[2].setSelection(false);
							oc.setOverrideLocal(false);
							oc.setKeepLocal(false);
							if (textLocal != null)
							{
								textLocal.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
							}
							if (textRepo != null)
							{
								textRepo.setForeground(AEVIEWConstants.VIEW_TEXT_SELECTED_COLOR);
							}
						}
						radios[0].pack();
						radios[1].pack();

						radios[2].pack();

						repoComp.layout();
						repoComp.pack();
						localComp.layout();
						localComp.pack();

						leftComp.layout();
						leftComp.pack();

						buttonComp.layout();
						buttonComp.pack();

						outerComp.layout();
						outerComp.pack();
						i++;
					}
				}
				else
				{
					break;
				}
			}
			scrollComposite.setContent(comp);
			scrollComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			comp.layout();
			scrollComposite.layout();
			scrollComposite.pack();
			scrollComposite.getParent().layout();
			scrollComposite.getParent().pack();

		}

	}

	/**
	 * Load person.
	 * @param object the object
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void loadPerson(final PdrObject object, final Composite comp, final Text text,
			final boolean fromRepository)
	{
		Person cp = (Person) object;

		writePerson2Text(cp, comp, text, fromRepository);

	}

	/**
	 * Load reference.
	 * @param object the object
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void loadReference(final PdrObject object, final Composite comp, final Text text,
			final boolean fromRepository)
	{
		ReferenceMods cr = (ReferenceMods) object;

		writeReference2Text(cr, comp, text, fromRepository);

	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
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

	}

	/**
	 * Validate.
	 */
	protected final void validate()
	{
		boolean valid = true;
		if (_conflictingAspects != null && !_conflictingAspects.isEmpty())
		{
			if (!validConflicts(_conflictingAspects))
			{
				valid = false;
			}
		}

		if (_conflictingPersons != null && !_conflictingPersons.isEmpty())
		{
			if (!validConflicts(_conflictingPersons))
			{
				valid = false;
			}
		}

		if (_conflictingReferences != null && !_conflictingReferences.isEmpty())
		{
			if (!validConflicts(_conflictingReferences))
			{
				valid = false;
			}
		}
		_saveButton.setEnabled(valid);
	}

	/**
	 * Valid conflicts.
	 * @param conflictingObjs the conflicting objs
	 * @return true, if successful
	 */
	private boolean validConflicts(final Vector<PDRObjectsConflict> conflictingObjs)
	{
		for (PDRObjectsConflict oc : conflictingObjs)
		{
			if (!(oc.isKeepLocal() || oc.isOverrideLocal()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Write person2 text.
	 * @param cp the cp
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void writePerson2Text(final Person cp, final Composite comp, final Text text,
			final boolean fromRepository)
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		text.setEditable(false);
		text.setData("key", cp.getPdrId().toString()); //$NON-NLS-1$
		text.setData("id", cp.getPdrId().toString()); //$NON-NLS-1$


		text.append("\n"); //$NON-NLS-1$
		text.append(cp.getDisplayName());
		text.append("\n" + cp.getPdrId().toString()); //$NON-NLS-1$ //$NON-NLS-2$

		if (cp.getConcurrences() != null && cp.getConcurrences().getConcurrences() != null
				&& !cp.getConcurrences().getConcurrences().isEmpty())
		{
			text.append("\n" + "Concurrences:\n");
			for (Concurrence con : cp.getConcurrences().getConcurrences())
			{
				if (con.getPersonId() != null)
				{
					Person p = _facade.getPerson(con.getPersonId());
					if (p != null)
					{
						text.append("\n" + p.getDisplayNameWithID());
					}
					else
					{
						text.append("\n" + con.getPersonId().toString());
					}
				}
				if (con.getReferences() != null && !con.getReferences().isEmpty())
				{
					text.append("\n" + "References:\n");
					for (ValidationStm valS : con.getReferences())
					{
						if (valS.getReference() != null && valS.getReference().getSourceId() != null)
						{
							ReferenceMods r = _facade.getReference(valS.getReference().getSourceId());
							if (r != null)
							{
								text.append("\n" + r.getDisplayNameWithID());
							}
							else
							{
								text.append("\n" + valS.getReference().getSourceId());
							}
							text.append(" Internal: " + valS.getReference().getInternal());
							text.append(" Quality: " + valS.getReference().getQuality());
						}
						if (valS.getAuthority() != null)
						{
							text.append(NLMessages.getString("View_lb_user")
									+ _facade.getObjectDisplayName(valS.getAuthority()));

						}
						if (valS.getInterpretation() != null)
						{
							text.append("\nInterpretation " + valS.getInterpretation());
						}

					}

				}
			}
		}
		if (cp.getIdentifiers() != null && cp.getIdentifiers().getIdentifiers() != null
				&& !cp.getIdentifiers().getIdentifiers().isEmpty())
		{
			text.append("\n\n" + "External Identifiers:");
			for (Identifier ident : cp.getIdentifiers().getIdentifiers())
			{
				if (ident.getIdentifier() != null)
				{
					text.append("\n" + ident.getProvider() + ": " + ident.getIdentifier());
				}
				if (ident.getQuality() != null)
				{
					text.append("\nQuality: " + ident.getQuality());
				}
				if (ident.getAuthority() != null)
				{

					text.append(NLMessages.getString("View_lb_user")
							+ _facade.getObjectDisplayName(ident.getAuthority()));

				}
			}
		}
	}

	/**
	 * Write reference2 text.
	 * @param cr the cr
	 * @param comp the comp
	 * @param text the text
	 * @param fromRepository the from repository
	 */
	private void writeReference2Text(final ReferenceMods cr, final Composite comp, final Text text,
			final boolean fromRepository)
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		text.setEditable(false);
		text.setData("key", cr.getPdrId().toString()); //$NON-NLS-1$
		text.setData("id", cr.getPdrId().toString()); //$NON-NLS-1$

		text.append("\n"); //$NON-NLS-1$
		text.append(cr.getDisplayNameLong());
		text.append("\n" + cr.getPdrId().toString()); //$NON-NLS-1$ //$NON-NLS-2$
		text.append("\n"); //$NON-NLS-1$
		if (cr.getRecord() != null && cr.getRecord().getRevisions() != null
				&& cr.getRecord().getRevisions().firstElement() != null
				&& cr.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			text.append(NLMessages.getString("View_lb_user")
					+ _facade.getObjectDisplayName(cr.getRecord().getRevisions().firstElement().getAuthority()));
		}
		// use a verify listener to keep the offsets up to date

		text.append("\n"); //$NON-NLS-1$
		text.setLayoutData(gd);

		text.pack();

	}
}

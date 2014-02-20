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
package org.bbaw.pdr.ae.view.main.views;

import java.util.Observable;
import java.util.Observer;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.main.internal.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * @author cplutte This ViewClass creates the CategoryView of the Aspects of the
 *         current Person. There has yet to be implemented a method to load the
 *         Aspects. The List of Categories is still static and based upon the
 *         List given in the properties file. CategoryView .
 */
public class CommentView extends ViewPart implements ISelectionListener, Observer
{

	/** The Constant ID. */
	public static final String ID = "org.bbaw.pdr.ae.view.main.views.CategoryView"; //$NON-NLS-1$

	/** facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** Instance of shared image registry. */
	@SuppressWarnings("unused")
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** Resource Manager for colors and fonts. */
	private LocalResourceManager _resources = new LocalResourceManager(JFaceResources.getResources());

	/** The comment text. */
	private Text _commentText;

	/** The show comments. */
	private Button _showComments;

	/** The edit comment. */
	private Button _editComment;

	/** The delete comment. */
	private Button _deleteComment;

	/** The insert comment. */
	private Button _insertComment;

	/** The button composite. */
	private Composite _buttonComposite;

	/** The scroll comp. */
	private ScrolledComposite _scrollComp;

	/** The content comp. */
	private Composite _contentComp;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/**
	 * Instantiates a new comment view.
	 */
	public CommentView()
	{
	}

	@Override
	public final void createPartControl(final Composite parent)
	{

		getViewSite().getPage().addSelectionListener(this);
		// facade.addObserver(this);

		SashForm sashFormMain = new SashForm(parent, SWT.VERTICAL);
		sashFormMain.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group commentHeadGroup = new Group(sashFormMain, SWT.SHADOW_IN);
		commentHeadGroup.setText("Kommentare zum aktuellen Objekt");
		commentHeadGroup.setLayout(new GridLayout());
		commentHeadGroup.setLayoutData(new GridData());

		_buttonComposite = new Composite(commentHeadGroup, SWT.NONE);
		_buttonComposite.setLayout(new GridLayout());
		_buttonComposite.setLayoutData(new GridData());
		((GridData) _buttonComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _buttonComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _buttonComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _buttonComposite.getLayoutData()).grabExcessVerticalSpace = false;
		((GridLayout) _buttonComposite.getLayout()).numColumns = 4;
		((GridLayout) _buttonComposite.getLayout()).makeColumnsEqualWidth = false;

		_showComments = new Button(_buttonComposite, SWT.PUSH);
		_showComments.setText("Kommentare zeigen");
		_showComments.setLayoutData(new GridData());
		_showComments.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				loadComments();
			}
		});
		_showComments.pack();

		_editComment = new Button(_buttonComposite, SWT.PUSH);
		_editComment.setText("Bearbeiten");
		_editComment.setLayoutData(new GridData());

		_editComment.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
			}
		});
		_editComment.setEnabled(false);
		_editComment.pack();

		_deleteComment = new Button(_buttonComposite, SWT.PUSH);
		_deleteComment.setText("Löschen");
		_deleteComment.setLayoutData(new GridData());

		_deleteComment.setEnabled(false);
		_editComment.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
			}
		});
		_deleteComment.pack();

		_insertComment = new Button(_buttonComposite, SWT.PUSH);
		_insertComment.setText("Einfügen");
		_insertComment.setLayoutData(new GridData());
		_insertComment.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// TODO meth schreiben, updater einklinken.
			}
		});
		_insertComment.pack();

		_commentText = new Text(commentHeadGroup, SWT.MULTI | SWT.V_SCROLL);
		_commentText.setLayoutData(new GridData());
		((GridData) _commentText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _commentText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _commentText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _commentText.getLayoutData()).grabExcessVerticalSpace = true;

		// _commentHeadGroup
		// } // upper Part of sashFormMain
		//
		// {
		_scrollComp = new ScrolledComposite(sashFormMain, SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollComp.setExpandHorizontal(true);
		_scrollComp.setExpandVertical(true);
		_scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		_scrollComp.setLayout(layout);
		_contentComp = new Composite(_scrollComp, SWT.NONE);
		_contentComp.setLayout(layout);

		_scrollComp.setContent(_contentComp);
		// lower part of sashFormMain
		sashFormMain.setWeights(new int[]
		{2, 5});
		// sashFormMain
	}

	/**
	 * load the comment.
	 * @param cp current person
	 * @param text comment text
	 * @param i index
	 */
	@SuppressWarnings("unused")
	private void loadComment(final Person cp, final Text text, final int i)
	{
		// Comment com = cp.getComments().get(i);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		// System.out.println( event + category + position);

		text.setEditable(false);

		text.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseDoubleClick(final MouseEvent e)
			{

			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				Text last = (Text) _scrollComp.getData("lastSelected"); //$NON-NLS-1$
				Text current = (Text) e.widget;
				(current).setBackground(_resources.createColor(new RGB(255, 255, 200)));

				IStatus sca = new Status(IStatus.INFO, Activator.PLUGIN_ID, "CategoryView current aspect: "
						+ _facade.getCurrentAspect());
				iLogger.log(sca);
				if (last != null && !last.equals(current) && !last.isDisposed())
				{
					last.setBackground(_resources.createColor(new RGB(255, 255, 255)));
				}
				_scrollComp.setData("lastSelected", current); //$NON-NLS-1$

			}

			@Override
			public void mouseUp(final MouseEvent e)
			{
				//
			}
		});

		// String cString = "Erstellt von: " + com.getCommentCreator() + "\n";
		// cString = cString + "Erstellt am: " + com.getCommentDate() + "\n\n";
		// cString = cString + com.getCommentText();

		// text.setText(cString);
		text.setLayoutData(gd);
		text.pack();
	}

	/**
	 * Load comments.
	 */
	public void loadComments()
	{
		// Person cp = facade.getCurrentPerson();
		// Aspect ca = facade.getCurrentAspect();
		//
		// // IStatus sla = new Status(IStatus.INFO,Activator.PLUGIN_ID,
		// "CommentView load Comments - number of aspects: " +
		// cp.getComments().size());
		// // iLogger.log(sla);
		//
		// Composite comp = (Composite)scrollComp.getContent();
		//
		// Control[] children = contentComp.getChildren();
		// for (Control c : children) {
		// c.dispose();
		// }
		//
		// // int i;
		// // for (i=0; i<cp.getComments().size(); i++){
		// // if (cp != null){
		// // Comment com = cp.getComments().get(i);
		// // Text text = new Text(comp, SWT.WRAP | SWT.READ_ONLY | SWT.NO_FOCUS
		// | SWT.CURSOR_ARROW);
		// // text.setBackground(SWTResourceManager.getColor(255, 255, 255));
		// // loadComment(cp, text, i);
		// // }
		// //
		// // }
		//
		// scrollComp.setContent(comp);
		// scrollComp.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// comp.layout();

	}

	// /////////////////////Update - Observer ///////////////////////////////

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
	{

	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus()
	{

	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus supdate = new Status(IStatus.INFO, Activator.PLUGIN_ID, "CategoryView update: " + arg);
		iLogger.log(supdate);

		if (arg.equals("newAspect")) //$NON-NLS-1$
		{
			if (_facade.getCurrentAspect() != null)
			{
				// loadEvent(event, container.getCurrentCategory());
				loadComments();
			}
		}
		else if (arg.equals("newPerson")) //$NON-NLS-1$
		{
			if (_facade.getCurrentAspect() != null)
			{
				// loadEvent(event, container.getCurrentCategory());

				loadComments();
			}
		}
		// } else if (arg.equals("newNewPerson")) {
		// buildTreePersonen();
		// String id = facade.getCurrentPerson().getId();
		// /* Zweig der hinzugefuegten Person oeffnen (andere werden
		// geschlossen) */
		// char firstChar =
		// Character.toUpperCase(facade.getCurrentPerson().getLastName().trim().charAt(0));
		// for(TreeItem t : treePersonen.getItems()) {
		// //_l.fatal("firstChar=" + firstChar + ", t.getText()=" + t.getText()
		// + ", " + (t.getText().charAt(0) == firstChar));
		// t.setExpanded(t.getText().charAt(0) == firstChar);
		// for(TreeItem ti : t.getItems()) {
		// if (ti.getData("key").equals(id)){
		// ti.setBackground(SWTResourceManager.getColor(255, 255, 200));
		// break;
		// }
		// }
		// }
	}

}

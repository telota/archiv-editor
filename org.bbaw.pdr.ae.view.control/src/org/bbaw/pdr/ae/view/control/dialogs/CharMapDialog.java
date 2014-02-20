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

import java.util.Vector;

import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
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
import org.eclipse.ui.PlatformUI;

public class CharMapDialog extends TitleAreaDialog 
{
static //	private static final LocalResourceManager _resources = new LocalResourceManager(JFaceResources.getResources());

	private Vector<Character> chars = new Vector<Character>(773);
	private Text selectedText;
	private ScrolledComposite _scrollComp;
	private Text _charText;
	FontRegistry fontRegistry = new FontRegistry(PlatformUI.getWorkbench().getDisplay());

	
	public CharMapDialog(Shell parentShell)
	{
		super(parentShell);
	}
	
	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle("Char map"); //$NON-NLS-1$
		// Set the message
		setMessage("select char", IMessageProvider.INFORMATION); //$NON-NLS-1$

	}

	@Override
	public final void createButtonsForButtonBar(final Composite parent)
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
		createOkButton(parent, OK, "insert", true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "cancel", false); //$NON-NLS-1$
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
	public final Control createDialogArea(final Composite parent)
	{
		int i;
		char ch;
		Character c;
		for (i = 0; i <= 0xffff; i++)
		{
			ch = (char) i;
			// if (i % 0x10 == 0)
			// {
			// System.out.println();
			// }
			// if (i % 0x100 == 0)
			// {
			// System.out.println("****" + Integer.toHexString(i / 0x100) +
			// "****");
			// }
			if (Integer.toHexString(i / 0x100).equals("8"))

			{
				break;
			}
			if ((i > 31 && i < 127) || (i > 160 && i < 564) || (i > 591 && i < 686) || (i > 912 && i < 1015)
					|| (i > 1023 && i < 1270) || (i > 1487 && i < 1515) || (i > 1519 && i < 1525))
			{
				c = new Character(ch);
				chars.add(c);
				// System.out.println(ch + " " + i);
			}

		}
		// System.out.println();

		fontRegistry.put("font2", new FontData[]
		{new FontData("Arial", 30, SWT.BOLD)});
		// fontRegistry.put("font1", new FontData[]
		// {new FontData("Arial", 12, SWT.NONE)});

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2, false));
		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).heightHint = 320;
		((GridData) mainComposite.getLayoutData()).widthHint = 220;
		((GridData) mainComposite.getLayoutData()).horizontalSpan = 2;

		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollComp = new ScrolledComposite(mainComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollComp.setExpandHorizontal(true);
		_scrollComp.setExpandVertical(true);
		_scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		_scrollComp.setLayoutData(new GridData());
		((GridData) _scrollComp.getLayoutData()).heightHint = 240;
		((GridData) _scrollComp.getLayoutData()).widthHint = 200;
		((GridData) _scrollComp.getLayoutData()).horizontalSpan = 2;

		((GridData) _scrollComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollComp.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollComp.setMinHeight(1);
		_scrollComp.setMinWidth(1);

		_scrollComp.setLayout(new GridLayout());


		Composite contentComp = new Composite(_scrollComp, SWT.NONE);
		contentComp.setLayoutData(new GridData());
		((GridData) contentComp.getLayoutData()).horizontalSpan = 2;
		contentComp.setLayout(new GridLayout());
		((GridLayout) contentComp.getLayout()).numColumns = 25;
		((GridLayout) contentComp.getLayout()).makeColumnsEqualWidth = false;
		contentComp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		_scrollComp.setContent(contentComp);
		ViewHelper.accelerateScrollbar(_scrollComp, 10);
		_scrollComp.setShowFocusedControl(true);
		// RAP auskommentiert
		// MouseWheelListener mwl = new MouseWheelListener() {
		//
		// @Override
		// public void mouseScrolled(MouseEvent e) {
		// _scrollComp.setOrigin(_scrollComp.getOrigin().x,
		// _scrollComp.getOrigin().y - e.count * 10);
		//
		// }
		// };
		FocusListener sl = new FocusListener() {
			
			
			@Override
			public void focusGained(FocusEvent e) {
				if (selectedText != null)
				{
					selectedText.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
				}
				Text l = (Text) e.widget;
				l.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				selectedText = l;
				_charText.setText(l.getText());
				_charText.pack();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		MouseListener ml = new MouseListener()
		{

			@Override
			public void mouseUp(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				okPressed();

			}
		};

		GridData gd = new GridData();
		
		
		// int i;
		for (Character cc : chars)
		{
			Text b = new Text(contentComp, SWT.READ_ONLY);
			b.setText(new String(cc.toString()));
			b.addFocusListener(sl);
			b.setLayoutData(gd);
			// RAP auskommentiert
			// b.addMouseWheelListener(mwl);
			b.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			b.addMouseListener(ml);
			// b.setFont(fontRegistry.get("font1"));
		}
		   
		// for(i=0;i<=0xffff;i++)
		// {
		// ch=(char)i;
		// if (i % 0x10 == 0)
		// {
		// System.out.println();
		// }
		// if (i % 0x100 == 0)
		// {
		// System.out.println("****" + Integer.toHexString(i / 0x100) + "****");
		// }
		// if (Integer.toHexString(i/0x100).equals("8")) break;
		// System.out.print(ch);
		// Text b = new Text(contentComp, SWT.READ_ONLY);
		// b.setText(new String (new char[]{ch}));
		// b.addFocusListener(sl);
		// b.setLayoutData(gd);
		// b.addMouseWheelListener(mwl);
		// b.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		// b.addMouseListener(ml);
		// b.setFont(fontRegistry.get("font1"));
		// }
//		System.out.println();  
		   
		contentComp.layout();

		_scrollComp.setContent(contentComp);
		Point point = contentComp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// Point mp = mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// if (point.x > mp.x - 20) point.x = mp.x - 20;
		_scrollComp.setMinSize(point);
		// scrollComp.pack();
		_scrollComp.layout();
		
		_scrollComp.setFocus();
		
		Label charLabel = new Label(mainComposite, SWT.NONE);
		charLabel.setText("Character");
		
		_charText = new Text(mainComposite, SWT.READ_ONLY);
		_charText.setLayoutData(new GridData());
		((GridData) _charText.getLayoutData()).horizontalSpan = 1;
		_charText.setFont(fontRegistry.get("font2"));
		

		mainComposite.layout();
		parent.pack();

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
	
	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * checks if input is valid.
	 * @return boolean valid TODO die Überprüfung des Benutzernamens und
	 *         Passwortes soll nicht hier, sondern in der Controller-Schicht
	 *         oder sogar in der DAtenhaltung aus geführt werden. TODO
	 *         User-datenbank einbauen.
	 */
	private boolean isValidInput()
	{
		boolean valid = true;
		
		return valid;
	}

	

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected final void okPressed()
	{
		setReturnCode(_charText.getText().charAt(0));
		close();
	}


}

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
package org.bbaw.pdr.ae.view.filesaveandload.local;

import org.bbaw.pdr.ae.common.AEConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * The Class FileSaveDialog.
 * @author Christoph Plutte
 */
public class FileSaveDialog extends TitleAreaDialog
{

	/** The text. */
	private Text _text;

	/** The title. */
	private String _title;

	/** The message. */
	private String _message;

	/** The file name. */
	private String _fileName;

	/** The directory. */
	private String _directory;

	/**
	 * Instantiates a new file save dialog.
	 * @param parentShell the parent shell
	 * @param fileName the file name
	 * @param directory the directory
	 * @param title the title
	 * @param message the message
	 */
	public FileSaveDialog(final Shell parentShell, final String fileName, final String directory, final String title,
			final String message)
	{
		super(parentShell);
		this._fileName = fileName;
		this._directory = directory;
		this._title = title;
		this._message = message;
	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected final Control createDialogArea(final Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		setTitle(_title);
		setMessage(_message);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 49, 13);
		lblNewLabel.setText("Save to");

		_text = new Text(container, SWT.BORDER);
		_text.setBounds(10, 29, 352, 19);
		if (_directory != null)
		{
			_text.setText(_directory + AEConstants.FS + _fileName);
		}

		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(368, 27, 68, 23);
		btnNewButton.setText("Select...");
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setFilterPath(_directory); //$NON-NLS-1$
				directoryDialog.setMessage(_message);
				directoryDialog.setText(_title);
				_directory = directoryDialog.open();
				_text.setText(_directory + AEConstants.FS + _fileName);
			}
		});

		return area;
	}

	/**
	 * Gets the directory.
	 * @return the directory
	 */
	public final String getDirectory()
	{
		return _directory;
	}

	@Override
	protected final Point getInitialSize()
	{
		return new Point(450, 300);
	}
}

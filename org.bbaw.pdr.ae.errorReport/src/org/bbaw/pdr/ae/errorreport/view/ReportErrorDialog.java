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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.errorreport.view;

import java.io.File;

import javax.mail.MessagingException;

import org.bbaw.pdr.ae.view.errorReport.mail.SendMailUsingAuthentication;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author cplutte Class creates a Dialog for choosing/creating a new Identifier
 *         for the current Person. TODO it has to be adapted to dynamic data
 *         TODO dialog wird derzeit nicht benötigt.
 */
public class ReportErrorDialog extends TitleAreaDialog
{

	/** The error title. */
	private Text _errorTitle;

	/** The error what. */
	private Text _errorWhat;

	/** The error where. */
	private Text _errorWhere;

	/** The error desc. */
	private Text _errorDesc;

	/**
	 * Instantiates a new report error dialog.
	 * @param parentShell the parent shell
	 */
	public ReportErrorDialog(final Shell parentShell)
	{
		super(parentShell);
	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		createOkButton(parent, IDialogConstants.OK_ID, Messages.getString("ErrorDialog_send_report"), true);
		createButton(parent, IDialogConstants.CANCEL_ID,

		// TODO externalize string
				"Cancel", false);

	}

	@Override
	protected final Control createContents(final Composite parent)
	{
		Control contents = super.createContents(parent);
		// Set the title
		setTitle(Messages.getString("ErrorDialog_error_dialog_title"));
		// Set the message
		setMessage(Messages.getString("ErrorDialog_error_dialog_message"), IMessageProvider.INFORMATION);
		return contents;
	}

	@Override
	protected final Control createDialogArea(final Composite parent)
	{
		// return super.createDialogArea(parent);
		parent.setSize(300, 400);

		Composite mainComposite = new Composite(parent, SWT.None);
		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).minimumHeight = 350;
		((GridData) mainComposite.getLayoutData()).minimumWidth = 250;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).numColumns = 1;
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = false;

		Label eTitle = new Label(mainComposite, SWT.NONE);
		eTitle.setLayoutData(new GridData());
		eTitle.setText(Messages.getString("ErrorDialog_what_malfunction"));

		_errorTitle = new Text(mainComposite, SWT.BORDER);
		_errorTitle.setLayoutData(new GridData());
		((GridData) _errorTitle.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _errorTitle.getLayoutData()).horizontalAlignment = SWT.FILL;

		Label eWhat = new Label(mainComposite, SWT.NONE);
		eWhat.setLayoutData(new GridData());
		eWhat.setText(Messages.getString("ErrorDialog_whant_wrong"));

		_errorWhat = new Text(mainComposite, SWT.BORDER);
		_errorWhat.setLayoutData(new GridData());
		((GridData) _errorWhat.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _errorWhat.getLayoutData()).horizontalAlignment = SWT.FILL;

		Label eWhere = new Label(mainComposite, SWT.NONE);
		eWhere.setLayoutData(new GridData());
		eWhere.setText(Messages.getString("ErrorDialog_where_malfunction"));

		_errorWhere = new Text(mainComposite, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_errorWhere.setLayoutData(new GridData());
		((GridData) _errorWhere.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _errorWhere.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData) errorWhere.getLayoutData()).grabExcessVerticalSpace =
		// true ;
		// ((GridData) errorWhere.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _errorWhere.getLayoutData()).heightHint = 60;

		Label eDesc = new Label(mainComposite, SWT.NONE);
		eDesc.setLayoutData(new GridData());
		eDesc.setText(Messages.getString("ErrorDialog_please_give_details"));

		_errorDesc = new Text(mainComposite, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_errorDesc.setLayoutData(new GridData());
		((GridData) _errorDesc.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _errorDesc.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _errorDesc.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _errorDesc.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _errorDesc.getLayoutData()).minimumHeight = 140;

		mainComposite.layout();
		parent.layout();
		parent.pack();
		return parent;
	}

	/**
	 * Creates the ok button.
	 * @param parent the parent
	 * @param id the id
	 * @param label the label
	 * @param defaultButton the default button
	 * @return the button
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			boolean defaultButton)
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
	 * Checks if is valid input.
	 * @return true, if is valid input
	 */
	private boolean isValidInput()
	{
		boolean valid = true;
		//		if (Activator.getDefault().getPreferenceStore().getString("MAIL_ADRESS_SENDER") == null //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_ADRESS_SENDER").trim().length() == 0 //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_HOST_NAME") == null //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_HOST_NAME").trim().length() == 0 //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_AUTH_USER") == null //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_AUTH_USER").trim().length() == 0 //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_AUTH_PWD") == null //$NON-NLS-1$
		//				|| Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_AUTH_PWD").trim().length() == 0) //$NON-NLS-1$
		// {
		// IWorkbench workbench = PlatformUI.getWorkbench();
		// Display display = workbench.getDisplay();
		// Shell shell = new Shell(display);
		// MailSettingsDialog dialog = new MailSettingsDialog(shell);
		// dialog.open();
		// valid = (dialog.getReturnCode() == 0);
		// }
		return valid;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected final void okPressed()
	{
		saveInput();
		super.okPressed();
	}

	/**
	 * Save input.
	 */
	private void saveInput()
	{
		String userId = Platform.getPreferencesService().getString("ArchivEditor", "USER_SAVE_ID", "unkown", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String message = "Error Report Message\n\nUser: " + userId; //$NON-NLS-1$
		message += "\n\nWhat kind malfunction has occured?\n" + _errorTitle.getText(); //$NON-NLS-1$
		message += "\n\nWhat went wrong?\n" + _errorWhat.getText(); //$NON-NLS-1$
		message += "\n\nWhen and where did this malfunction occur?\n" + _errorWhere.getText(); //$NON-NLS-1$
		message += "\n\nDescription:\n" + _errorDesc.getText(); //$NON-NLS-1$
		String aeHome = Platform.getPreferencesService().getString("ArchivEditor", "AE_HOME", "unkown", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String fs = System.getProperty("file.separator"); //$NON-NLS-1$
		File f = new File(aeHome + fs + "workspace" + fs + ".metadata"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!f.exists())
		{
			f.mkdir();
		}
		String attachment = aeHome + fs + "workspace" + fs + ".metadata" + fs + ".log"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try
		{
			new SendMailUsingAuthentication().postMail(new String[]
			{"plutte@bbaw.de"}, "[ArchivEditor - Error Report Message]", message, "plutte@bbaw.de", attachment); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
	}

}

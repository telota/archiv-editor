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
package org.bbaw.pdr.ae.view.filesaveandload.web;

import org.bbaw.pdr.ae.view.filesaveandload.web.internal.DownloadServiceHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;
import org.eclipse.rwt.service.IServiceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class FileDownloadDialog extends TitleAreaDialog {

	private String fileName;
	private String linkName;
	private Composite area;
	private String title;
	private String message;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FileDownloadDialog(Shell parentShell, String fileName, String linkName, String title, String message) {
		super(parentShell);
		this.fileName = fileName;
		this.linkName = linkName;
		this.title = title;
		this.message = message;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());
		area = (Composite) super.createDialogArea(parent);
		IServiceManager manager = RWT.getServiceManager();
		IServiceHandler handler = new DownloadServiceHandler();
		manager.registerServiceHandler( "downloadServiceHandler", handler );

		setTitle(title);
		setMessage(message);
		
		createDownloadLink(area);
		createDownloadHtml(fileName, fileName);
		createDownloadUrl(fileName);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, "OK",
//				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				"Cancel", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	private void createDownloadLink( Composite parent ) {
		  Browser browser = new Browser( parent, SWT.NONE );
		  browser.setText( createDownloadHtml( fileName, linkName ) );
		  GridData gd_browser = new GridData();
		  gd_browser.heightHint = 140;
		  gd_browser.verticalAlignment = SWT.TOP;
		  gd_browser.widthHint = 433;
		  browser.setLayoutData(gd_browser);
		  ((GridData) browser.getLayoutData()).grabExcessHorizontalSpace = true;
		  ((GridData) browser.getLayoutData()).grabExcessVerticalSpace = true;
		  ((GridData) browser.getLayoutData()).horizontalAlignment = SWT.FILL;
		  ((GridData) browser.getLayoutData()).verticalAlignment = SWT.FILL;

		}
		 
		// constructs something like <a href="url-to-download-handler">Download file</a>
		private String createDownloadHtml( String filename, String text ) {
		  StringBuilder html = new StringBuilder();
		  html.append( "<a href=\"" );
		  html.append( createDownloadUrl( filename ) );
		  html.append( "\">" );
		  html.append( text );
		  html.append( "</a>" );
		  return html.toString();
		}
		 
		private String createDownloadUrl( String filename ) {
//			return filename;
		  StringBuilder url = new StringBuilder();
		  url.append( RWT.getRequest().getContextPath() );
		  url.append( RWT.getRequest().getServletPath() );
		  url.append( "?" );
		  url.append( IServiceHandler.REQUEST_PARAM );
		  url.append( "=downloadServiceHandler" );
		  url.append( "&filename=" );
		  url.append( filename );
		  String encodedURL = RWT.getResponse().encodeURL( url.toString() );
		  return encodedURL;
		}
}

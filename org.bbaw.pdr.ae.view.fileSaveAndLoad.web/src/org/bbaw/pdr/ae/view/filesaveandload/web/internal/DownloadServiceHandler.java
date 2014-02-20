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
package org.bbaw.pdr.ae.view.filesaveandload.web.internal;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;

public class DownloadServiceHandler implements IServiceHandler {
	 
	  public void service() throws IOException, ServletException {
	    // Which file to download?
	    String fileName = RWT.getRequest().getParameter( "filename" );
	    // Get the file content
	    File f = new File(fileName);
	    byte[] download = f.toString().getBytes();                 //MyDataStore.getByteArrayData( fileName );
	    // Send the file in the response
	    HttpServletResponse response = RWT.getResponse();
	    response.setContentType( "application/octet-stream" );
	    response.setContentLength( download.length );
	    String contentDisposition = "attachment; filename=\"" + fileName + "\"";
	    response.setHeader( "Content-Disposition", contentDisposition );
	    try {
	      response.getOutputStream().write( download );
	    } catch( IOException e1 ) {
	      e1.printStackTrace();
	    }
	  }
	}
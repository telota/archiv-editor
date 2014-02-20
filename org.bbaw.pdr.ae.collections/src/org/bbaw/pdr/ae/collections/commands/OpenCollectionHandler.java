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
package org.bbaw.pdr.ae.collections.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bbaw.pdr.ae.collections.control.CollectionSaxHandler;
import org.bbaw.pdr.ae.collections.control.CollectionsFacade;
import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * @author cplutte
 * this handlerClass opens the AspectEditor with AspectEditorInput.
 */
public class OpenCollectionHandler extends AbstractHandler implements
		IHandler
{

	/** execute method.
	 * @param event to be executed.
	 * @throws ExecutionException ee.
	 * @return null. */
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		FileDialog fileDialog = new FileDialog(HandlerUtil.getActiveWorkbenchWindow(
				event).getShell());
		fileDialog.setFilterPath("/"); //$NON-NLS-1$
		fileDialog.setText(NLMessages.getString("WriteLocalBackupHandler_title2")); //$NON-NLS-1$
		fileDialog.setFilterExtensions(new String[]{"*.xml"});
		final String selectedfile = fileDialog.open();
		if (selectedfile != null)
		{
			File file = new File(selectedfile);
//   		 System.out.println("file path " + filePathString); //$NON-NLS-1$
   		 SAXParserFactory factory = SAXParserFactory.newInstance();
   		 try
   		 {

   		     InputStream    xmlInput  = new FileInputStream(file);
   		     SAXParser      saxParser = factory.newSAXParser();

   		     CollectionSaxHandler handler   = new CollectionSaxHandler();
   		     XMLReader reader = saxParser.getXMLReader();
   		     try
   		     {
   		    	  // Turn on validation
   		    	 reader.setFeature("http://xml.org/sax/features/validation", true); //$NON-NLS-1$
   		    	  // Ensure namespace processing is on (the default)
   		    	 reader.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
   		    	}
   		     catch (SAXNotRecognizedException e)
   		     {
					//   		    	  System.err.println("Unknown feature specified: " + e.getMessage()); //$NON-NLS-1$
   		    }
   		     catch (SAXNotSupportedException e)
   		     {
					//   		    	  System.err.println("Unsupported feature specified: " + e.getMessage()); //$NON-NLS-1$
   		    }

   		     saxParser.parse(xmlInput, handler);
   		  PDRCollection coll = null;
   		coll = handler.getResultObject();
   		CollectionsFacade.getInstance().getLoadedCollections().put(coll.getName(), coll);
   		TreeNode root = new TreeNode(coll.getName(), "collection");
		if (coll != null && coll.getObjects() != null)
		{
			for (TreeNode item : coll.getObjects())
			{
				root.addChild(item);
			}
			Event ev = new Event();
			ev.data = root;
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
		    .getService(IHandlerService.class);
			try
			{
				handlerService.executeCommand(
						"org.bbaw.pdr.ae.view.main.commands.ShowObjectsInTreeView", ev); //$NON-NLS-1$
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
			ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
			IParameter iparam = null;
			//get the command from plugin.xml
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			ICommandService cmdService = (ICommandService)window.getService(ICommandService.class);
			Command cmd = cmdService.getCommand("org.bbaw.pdr.ae.view.main.commands.SelectTree");
			//get the parameter
			try
			{
				iparam = cmd.getParameter("org.bbaw.pdr.ae.view.tree");
			}
			catch (NotDefinedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Parameterization params = new Parameterization(iparam, coll.getName());
			parameters.add(params);
			//build the parameterized command
			ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters.toArray(new Parameterization[parameters.size()]));
			//execute the command
			try
			{
				handlerService = (IHandlerService)window.getService(IHandlerService.class);
				handlerService.executeCommand(pc, null);
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
		}

   		  catch (Throwable err)
   		  {
   		     err.printStackTrace();
   		 }


		}
		return null;
	}

}

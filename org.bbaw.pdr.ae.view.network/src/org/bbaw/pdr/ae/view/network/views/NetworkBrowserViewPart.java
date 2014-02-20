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
package org.bbaw.pdr.ae.view.network.views;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.utils.OpenExternalBrowser;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * View of _browser for seeking exterenal person identifier such as pnd, lccn.
 * @author cplutte
 */
public class NetworkBrowserViewPart extends ViewPart implements Observer
{

	/** Browser. */
	private Browser _browser;

	private Facade _facade = Facade.getInstanz();

	private PDRObjectsProvider _pdrObjectsProvider = new PDRObjectsProvider();
	/** The _orderer factory. */
	private PDROrdererFactory _ordererFactory = new PDROrdererFactory();
	/**
	 * creates control for view.
	 * @param parent main composite.
	 */
	@Override
	public final void createPartControl(final Composite parent)
	{

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);
		Label labelAddress = new Label(parent, SWT.NONE);
		labelAddress.setText(NLMessages.getString("BrowserViewPart_address"));

		final Text location = new Text(parent, SWT.BORDER);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 1;
		data.grabExcessHorizontalSpace = true;
		location.setLayoutData(data);

		_browser = new Browser(parent, SWT.NONE);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.horizontalSpan = 2;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		_browser.setLayoutData(layoutData);

		// System.out.println("_browser menu items: " +
		// _browser.getMenu().getItemCount());

		final Label status = new Label(parent, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		status.setLayoutData(data);

		final ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);

		/* event handling */
		Listener listener = new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				ToolItem item = (ToolItem) event.widget;
				String string = item.getText();

				// FIXME einkommentieren browser singlesourcen
				// if
				// (string.equals(NLMessages.getString("BrowserViewPart_back")))
				// {
				// _browser.back();
				// }
				// else if
				// (string.equals(NLMessages.getString("BrowserViewPart_forward")))
				// {
				// _browser.forward();
				// }
				// else if
				// (string.equals(NLMessages.getString("BrowserViewPart_stop")))
				// {
				// _browser.stop();
				// }
				// else if
				// (string.equals(NLMessages.getString("BrowserViewPart_refresh")))
				// {
				// _browser.refresh();
				// }
				// else
				if (string.equals(NLMessages.getString("BrowserViewPart_go")))
				{
					_browser.setUrl(location.getText());
				}

			}
		};
		_browser.addProgressListener(new ProgressListener()
		{
			@Override
			public void changed(final ProgressEvent event)
			{
				if (event.total == 0)
				{
					return;
				}
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
			}

			@Override
			public void completed(final ProgressEvent event)
			{
				progressBar.setSelection(0);
			}
		});

		// FIXME einkommentieren und singlesourcen
		// _browser.addStatusTextListener(new StatusTextListener() {
		// public void changed(StatusTextEvent event) {
		// status.setText(event.text);
		// }
		// });
		_browser.addLocationListener(new LocationListener()
		{
			@Override
			public void changed(final LocationEvent event)
			{
				if (event.top)
				{
					location.setText(event.location);
				}
			}

			@Override
			public void changing(final LocationEvent event)
			{
			}
		});

		location.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(final Event e)
			{
				_browser.setUrl(location.getText());
			}
		});
		setUrlFromSelection(selection);
		createSelectionListener();
		_facade.addObserver(this);
	}

	/** creates selection listener in view. */
	private void createSelectionListener()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		ISelectionService selectionService = window.getSelectionService();
		selectionService.addSelectionListener(new ISelectionListener()
		{

			@Override
			public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
			{
				setUrlFromSelection(selection);
			}
		});
	}

	/** sets focus in view. */
	@Override
	public final void setFocus()
	{
		_browser.setFocus();
	}

	/**
	 * sets url according to selection.
	 * @param selection selected url.
	 */
	private void setUrlFromSelection(final ISelection selection)
	{

	}

	public void openURL(String url)
	{
		if (url != null)
		{
			_browser.setUrl(url);
		}

	}

	@Override
	public final void update(final Observable o, final Object arg)
	{

		_pdrObjectsProvider.removeAllFilters();

		 {
		 if (_facade.getCurrentTreeObjects() != null)
		 {
		
		 }
					if (arg.equals("newTreeObjects")) //$NON-NLS-1$ //$NON-NLS-2$
		 {
		 loadPdrObjects(_facade.getCurrentTreeObjects());
		
		 }
					else if (arg.equals("newNewAspect")) //$NON-NLS-1$ //$NON-NLS-2$
		 {
		 loadPdrObjects(_facade.getCurrentTreeObjects());
		 }
					else if (arg.equals("refreshAll")) //$NON-NLS-1$
		 {
		 }
		
		 }
	}

	private void loadPdrObjects(PdrObject[] currentTreeObjects)
	{
		_pdrObjectsProvider.setInput(currentTreeObjects);
		_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.person"));
		Vector<OrderingHead> arrangedAspects = _pdrObjectsProvider.getArrangedAspects();
		Vector<String> nodeValues = new Vector<String>(arrangedAspects.size());
		StringBuffer nodeNames = new StringBuffer();
		StringBuffer links = new StringBuffer();
		HashMap<String, Integer> bucketList = new HashMap<String, Integer>();

		for (int i = 0; i < arrangedAspects.size(); i++)
		{
			OrderingHead oh = arrangedAspects.get(i);
			nodeValues.add(oh.getValue());
			nodeNames.append("\n{nodeName:\"" + oh.getLabel() + "\", group:" + i + "},");
			bucketList.put(oh.getValue(), i);
		}
		if (nodeNames.length() > 1)
		{
			nodeNames.deleteCharAt(nodeNames.length() - 1);
		}

		for (int i = 0; i < arrangedAspects.size(); i++)
		{

			OrderingHead oh = arrangedAspects.get(i);
			for (Aspect a : oh.getAspects())
			{
				if (a.getRelationDim() != null && a.getRelationDim().getRelationStms() != null
						&& a.getRelationDim().getRelationStms().size() > 0)
				{
					for (RelationStm rStm : a.getRelationDim().getRelationStms())
					{
						if (!rStm.getSubject().toString().equals(oh.getValue()))
						{
							if (rStm.getRelations() != null && rStm.getRelations().size() > 0)
							{
								for (Relation rel : rStm.getRelations())
								{
									if (!rel.getObject().toString().equals(oh.getValue()))
									{
										links.append("\n{source:" + i + ", target: "
												+ bucketList.get(rel.getObject().toString()) + ", value:2},");
									}
								}
							}
						}
						
					}

				}
			}

		}
		if (links.length() > 1)
		{
			links.deleteCharAt(links.length() - 1);
		}
		String output = "var data = { \nnodes:[" + nodeNames + "\n], \nlinks:[" + links.toString() + "]\n};";
		String fileName = AEConstants.AE_HOME + AEConstants.FS + "workspace_ae-2.1" + AEConstants.FS
				+ "org.bbaw.pdr.ae.view.network" + AEConstants.FS + "web_files" + AEConstants.FS + "protovis"
				+ AEConstants.FS + "data" + AEConstants.FS + "data.js";
		File f = new File(fileName);
		if (!f.exists())
		{
			try
			{
				f.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			FileWriter fw = new FileWriter(f);
			fw.write(output);
			fw.flush();
			fw.close();
			OpenExternalBrowser.openURL("http://localhost:8080/files/protovis/data/data.html");
			// _browser.setUrl("http://localhost:8080/files/protovis/data/data.html");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void dispose()
	{
		_facade.deleteObserver(this);
		super.dispose();
	}

}

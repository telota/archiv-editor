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
package org.bbaw.pdr.ae.view.identifiers.view;

import org.bbaw.pdr.ae.common.NLMessages;
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
import org.eclipse.swt.widgets.ToolBar;
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
public class BrowserViewPart extends ViewPart
{

	/** Browser. */
	private Browser _browser;
	/** urls for searching external person identifier. */
	private static final String PND_SEARCH = "http://tools.wmflabs.org/persondata/"; //$NON-NLS-1$

	/** The LCC n_ search. */
	private static final String LCCN_SEARCH = "http://catalog.loc.gov/webvoy.htm"; //$NON-NLS-1$

	/** The ICC u_ search. */
	private static final String ICCU_SEARCH = "http://www.sbn.it/opacsbn/opac/iccu/informazioni.jsp"; //$NON-NLS-1$

	/** The VIA f_ search. */
	private static final String VIAF_SEARCH = "http://www.viaf.org"; //$NON-NLS-1$

	/**
	 * creates control for view.
	 * @param parent main composite.
	 */
	@Override
	public final void createPartControl(final Composite parent)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		parent.setLayout(gridLayout);
		ToolBar toolbar = new ToolBar(parent, SWT.NONE);
		ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
		itemBack.setText(NLMessages.getString("BrowserViewPart_back"));
		ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
		itemForward.setText(NLMessages.getString("BrowserViewPart_forward"));
		ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
		itemStop.setText(NLMessages.getString("BrowserViewPart_stop"));
		ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
		itemRefresh.setText(NLMessages.getString("BrowserViewPart_refresh"));
		ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
		itemGo.setText(NLMessages.getString("BrowserViewPart_go"));
		new ToolItem(toolbar, SWT.SEPARATOR);
		ToolItem itemPND = new ToolItem(toolbar, SWT.PUSH);
		itemPND.setText("PND"); //$NON-NLS-1$
		ToolItem itemLCCN = new ToolItem(toolbar, SWT.PUSH);
		itemLCCN.setText("LCCN"); //$NON-NLS-1$
		ToolItem itemICCU = new ToolItem(toolbar, SWT.PUSH);
		itemICCU.setText("ICCU"); //$NON-NLS-1$
		ToolItem itemVIAF = new ToolItem(toolbar, SWT.PUSH);
		itemVIAF.setText("VIAF"); //$NON-NLS-1$

		new ToolItem(toolbar, SWT.SEPARATOR);
		ToolItem itemSPND = new ToolItem(toolbar, SWT.PUSH);
		itemSPND.setText("SPND"); //$NON-NLS-1$

		GridData data = new GridData();
		data.horizontalSpan = 3;
		toolbar.setLayoutData(data);

		Label labelAddress = new Label(parent, SWT.NONE);
		labelAddress.setText(NLMessages.getString("BrowserViewPart_address"));

		final Text location = new Text(parent, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		location.setLayoutData(data);

		_browser = new Browser(parent, SWT.NONE);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.horizontalSpan = 3;
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
				else if (string.equals("PND")) //$NON-NLS-1$
				{
					_browser.setUrl(PND_SEARCH);
				}
				else if (string.equals("LCCN")) //$NON-NLS-1$
				{
					_browser.setUrl(LCCN_SEARCH);
				}
				else if (string.equals("ICCU")) //$NON-NLS-1$
				{
					_browser.setUrl(ICCU_SEARCH);
				}
				else if (string.equals("VIAF")) //$NON-NLS-1$
				{
					_browser.setUrl(VIAF_SEARCH);
				}
				else if (string.equals("SPND")) //$NON-NLS-1$
				{
					// TODO javascript für pnd etc. identifier und import in ae
					// entwickeln.
					// _browser.execute("window.scrollTo(0,100000);");
					_browser.execute("a = window.find('Personennamendatei (PND)', true)"); //$NON-NLS-1$
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
		itemBack.addListener(SWT.Selection, listener);
		itemForward.addListener(SWT.Selection, listener);
		itemStop.addListener(SWT.Selection, listener);
		itemRefresh.addListener(SWT.Selection, listener);
		itemGo.addListener(SWT.Selection, listener);
		itemPND.addListener(SWT.Selection, listener);
		itemLCCN.addListener(SWT.Selection, listener);
		itemICCU.addListener(SWT.Selection, listener);
		itemVIAF.addListener(SWT.Selection, listener);
		itemSPND.addListener(SWT.Selection, listener);
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
		if (!_browser.isDisposed())
		{
			_browser.setUrl(PND_SEARCH);

		}
	}

	public void openURL(String url)
	{
		if (url != null)
		{
			_browser.setUrl(url);
		}

	}
}

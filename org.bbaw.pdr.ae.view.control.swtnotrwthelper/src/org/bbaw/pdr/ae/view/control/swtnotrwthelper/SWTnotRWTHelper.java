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
package org.bbaw.pdr.ae.view.control.swtnotrwthelper;

import org.bbaw.pdr.ae.view.control.customSWTWidges.CustomTooltip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.interfaces.ISWTnotRWTHelper;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.progress.UIJob;

public class SWTnotRWTHelper implements ISWTnotRWTHelper
{

	public SWTnotRWTHelper()
	{
	}

	@Override
	public void accelerateScrollbar(ScrolledComposite composite, int times)
	{
		composite.getVerticalBar().setIncrement(composite.getVerticalBar().getIncrement() * times);
	}

	@Override
	public void setTabfolderSimple(CTabFolder tabFolder, boolean b)
	{
		tabFolder.setSimple(b);

	}

	@Override
	public void equipWithMouseExitListener(Control control, final CustomTooltip tooltip)
	{
		if (tooltip != null && control != null)
		{
			control.addListener(SWT.MouseExit, new Listener()
			{
				public void handleEvent(Event event)
				{
					switch (event.type)
					{
						case SWT.MouseEnter:
						case SWT.MouseMove:
							tooltip.hide();
						case SWT.MouseExit:
							tooltip.hide();
							break;
					}
				}
			});
		}

	}

	@Override
	public void equipeTabFolderToolTip(final CTabFolder tabFolder, final MarkupTooltip tooltip)
	{
		tabFolder.addMouseMoveListener(new MouseMoveListener()
		{
			private boolean first = false;
			private boolean hide = false;

			private UIJob job;
			public void mouseMove(MouseEvent arg0)
			{
				// System.out.println("event x y " + arg0.x + " " + arg0.y +
				// " visible " + tooltip.isVisible());
				boolean mouseOver = tooltip.isVisible();
				Point point = new Point(arg0.x, arg0.y);
				CTabItem[] items = tabFolder.getItems();
				String tip = null;
				if (arg0.y > 14)
				{
					hide = true;
					if (job != null)
					{
						// System.out.println("cancel job");
						job.cancel();
						job = null;
					}
					tooltip.hide();
				}
				else
				{
					for (int i = 0; i < items.length; i++)
					{
						int y = items[i].getBounds().y;
						if (items[i].getBounds().contains(point))
						{
							if (!mouseOver)
							{
								first = true;
							}
							mouseOver = true;

							tip = (String) items[i].getData("tip");
							if (tip != null)
							{
								tooltip.setToolTipText(tip);
								if (first && job == null)
								{
									first = false;
									hide = false;
									final Point p = new Point(point.x, 30);
									job = new UIJob("timer")
									{
										@Override
										public IStatus runInUIThread(IProgressMonitor monitor)
										{
											// System.out.println("hide " +
											// hide);
											if (!tooltip.isVisible() && !hide)
											{
												tooltip.show(p);
											}
											return Status.OK_STATUS;
										}
									};
									// System.out.println("new job schedule");
									job.schedule(1800);
								}
							}
						}
					}
				}
			}
		});
		
	}

	@Override
	public void equipeControlWithToolTip(final Control control, final CustomTooltip tooltip)
	{
		control.addMouseMoveListener(new MouseMoveListener()
		{
			private UIJob job;

			public void mouseMove(MouseEvent arg0)
			{
				Point point = new Point(arg0.x, arg0.y);
				if (control.getBounds().contains(point))
				{
					if (tooltip.isVisible())
					{
						if (job != null)
						{
							// System.out.println("cancel job");
							job.cancel();
							job = null;
						}
						tooltip.hide();
					}
					else
					{
						if (job == null)
						{
							final Point p = new Point(point.x, 30);
							job = new UIJob("timer")
							{
								@Override
								public IStatus runInUIThread(IProgressMonitor monitor)
								{
									// System.out.println("hide " +
									// hide);
									if (!tooltip.isVisible())
									{
										tooltip.show(p);
									}
									return Status.OK_STATUS;
								}
							};
							// System.out.println("new job schedule");
							job.schedule(10);
						}
					}
				}

			}
		});

	}

}

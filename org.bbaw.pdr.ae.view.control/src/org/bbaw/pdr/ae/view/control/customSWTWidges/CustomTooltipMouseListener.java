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
package org.bbaw.pdr.ae.view.control.customSWTWidges;

/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/

import java.util.HashMap;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * 32 * This class gives implementors to provide customized tooltips for any
 * control. 33 * 34 * @since 3.3 35
 */
public abstract class CustomTooltipMouseListener
{

	/**
	 * The listener interface for receiving tooltipHide events. The class that
	 * is interested in processing a tooltipHide event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's <code>addTooltipHideListener</code>
	 * method. When the tooltipHide event occurs, that object's appropriate
	 * method is invoked.
	 * @see TooltipHideEvent
	 */
	private class TooltipHideListener implements Listener
	{

		/**
		 * @param event
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		@Override
		public void handleEvent(final Event event)
		{
			if (event.widget instanceof Control)
			{

				if (!event.widget.isDisposed())
				{
					Control c = (Control) event.widget;
					Shell shell = c.getShell();

					switch (event.type)
					{
						case SWT.MouseDown:
							if (isHideOnMouseDown())
							{
								toolTipHide(shell, event);
							}
							break;
						case SWT.MouseExit:
							/*
							 * Give some insets to ensure we get exit
							 * informations from a wider area ;-)
							 */
							Rectangle rect = shell.getBounds();
							rect.x += 5;
							rect.y += 5;
							rect.width -= 10;
							rect.height -= 10;

							if (!rect.contains(c.getDisplay().getCursorLocation()))
							{
								toolTipHide(shell, event);
							}
						default:
							break;

					// break;
					}
				}
			}
		}
	}

	/**
	 * The listener interface for receiving toolTipOwnerControl events. The
	 * class that is interested in processing a toolTipOwnerControl event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addToolTipOwnerControlListener</code> method. When the
	 * toolTipOwnerControl event occurs, that object's appropriate method is
	 * invoked.
	 * @see ToolTipOwnerControlEvent
	 */
	private class ToolTipOwnerControlListener implements Listener
	{

		/**
		 * @param event
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		@Override
		public void handleEvent(final Event event)
		{
			switch (event.type)
			{
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseDown:
				case SWT.MouseMove:
					toolTipHide(currentTooltip, event);
					break;
				case SWT.MouseHover:
					toolTipCreate(event);
					break;
				case SWT.MouseExit:
					/*
					 * Check if the mouse exit happend because we move over the
					 * tooltip
					 */
					if (currentTooltip != null && !currentTooltip.isDisposed())
					{
						if (currentTooltip.getBounds().contains(_control.toDisplay(event.x, event.y)))
						{
							break;
						}
					}

					toolTipHide(currentTooltip, event);
					break;
				default:
					break;
			}
		}
	}

	/** The control. */
	private Control _control;

	/** The x shift. */
	private int _xShift = 3;

	/** The y shift. */
	private int _yShift = 0;

	/** The popup delay. */
	private int _popupDelay = 0;

	/** The hide delay. */
	private int _hideDelay = 0;
	/** The listener. */
	private ToolTipOwnerControlListener _listener;
	/** The data. */
	private HashMap _data;
	// Ensure that only one tooltip is active in time
	/** The CURREN t_ tooltip. */
	private static Shell currentTooltip;

	/**
	 * Recreate the tooltip on every mouse move.
	 */
	public static final int RECREATE = 1;

	/**
	 * Don't recreate the tooltip as long the mouse doesn't leave the area
	 * triggering the Tooltip creation.
	 */
	public static final int NO_RECREATE = 1 << 1;

	/** The hide listener. */
	private TooltipHideListener _hideListener = new TooltipHideListener();

	/** The hide on mouse down. */
	private boolean _hideOnMouseDown = true;

	/** The respect display bounds. */
	private boolean _respectDisplayBounds = true;

	/** The respect monitor bounds. */
	private boolean _respectMonitorBounds = true;
	/** The style. */
	private int _style;
	/** The current area. */
	private Object _currentArea;

	/**
	 * Create new instance which add TooltipSupport to the widget.
	 * @param control the control on whose action the tooltip is shown
	 */
	public CustomTooltipMouseListener(final Control control)
	{
		this(control, RECREATE, false);
	}

	/**
	 * @param control the control to which the tooltip is bound
	 * @param style style passed to control tooltip behaviour
	 * @param manualActivation <code>true</code> if the activation is done
	 *            manually using {@link #show(Point)}
	 * @see #RECREATE
	 * @see #NO_RECREATE
	 */
	public CustomTooltipMouseListener(final Control control, final int style, final boolean manualActivation)
	{
		this._control = control;
		this._style = style;
		this._control.addDisposeListener(new DisposeListener()
		{

			@Override
			public void widgetDisposed(final DisposeEvent e)
			{
				deactivate();
			}

		});

		this._listener = new ToolTipOwnerControlListener();

		if (!manualActivation)
		{
			activate();
		}
	}

	/**
	 * Activate tooltip support for this control.
	 */
	public void activate()
	{
		deactivate();
		_control.addListener(SWT.Dispose, _listener);
		// RAP auskommentiewrt
		_control.addListener(SWT.MouseHover, _listener);
		_control.addListener(SWT.MouseMove, _listener);
		_control.addListener(SWT.MouseExit, _listener);
		_control.addListener(SWT.MouseDown, _listener);
	}

	/**
	 * This method is called after a Tooltip is hidden.
	 * <p>
	 * <b>Subclasses may override to clean up requested system resources</b>
	 * </p>
	 * @param event event triggered the hiding action (may be <code>null</code>
	 *            if event wasn't triggered by user actions directly)
	 */
	protected void afterHideToolTip(final Event event)
	{

	}

	/**
	 * Creates the content area of the the tooltip.
	 * @param event the event that triggered the activation of the tooltip
	 * @param parent the parent of the content area
	 * @return the content area created
	 */
	protected abstract Composite createToolTipContentArea(Event event, Composite parent);

	/**
	 * Deactivate tooltip support for the underlying control.
	 */
	public void deactivate()
	{
		_control.removeListener(SWT.Dispose, _listener);
		// RAP auskommentiewrt
		_control.removeListener(SWT.MouseHover, _listener);
		_control.removeListener(SWT.MouseMove, _listener);
		_control.removeListener(SWT.MouseExit, _listener);
		_control.removeListener(SWT.MouseDown, _listener);
	}

	/**
	 * Fixup display bounds.
	 * @param tipSize the tip size
	 * @param location the location
	 * @return the point
	 */
	private Point fixupDisplayBounds(final Point tipSize, final Point location)
	{
		if (_respectDisplayBounds || _respectMonitorBounds)
		{
			Rectangle bounds;
			Point rightBounds = new Point(tipSize.x + location.x, tipSize.y + location.y);

			Monitor[] ms = _control.getDisplay().getMonitors();
			if (_respectMonitorBounds && ms.length > 1)
			{
				// By default present in the monitor of the control
				bounds = _control.getMonitor().getBounds();
				Point p = new Point(location.x, location.y);

				// Search on which monitor the event occurred
				Rectangle tmp;
				for (int i = 0; i < ms.length; i++)
				{
					tmp = ms[i].getBounds();
					if (tmp.contains(p))
					{
						bounds = tmp;
						break;
					}
				}

			}
			else
			{
				bounds = _control.getDisplay().getBounds();
			}

			if (!(bounds.contains(location) && bounds.contains(rightBounds)))
			{
				if (rightBounds.x > bounds.width)
				{
					location.x -= rightBounds.x - bounds.width;
				}

				if (rightBounds.y > bounds.height)
				{
					location.y -= rightBounds.y - bounds.height;
				}

				if (location.x < bounds.x)
				{
					location.x = bounds.x;
				}

				if (location.y < bounds.y)
				{
					location.y = bounds.y;
				}
			}
		}

		return location;
	}

	/**
	 * Get the data restored under the key.
	 * @param key the key
	 * @return data or <code>null</code> if no entry is restored under the key
	 */
	public Object getData(final String key)
	{
		if (_data != null)
		{
			return _data.get(key);
		}
		return null;
	}

	/**
	 * Get the display relative location where the tooltip is displayed.
	 * Subclasses may overwrite to implement custom positioning.
	 * @param tipSize the size of the tooltip to be shown
	 * @param event the event triggered showing the tooltip
	 * @return the absolute position on the display
	 */
	public Point getLocation(final Point tipSize, final Event event)
	{
		return _control.toDisplay(event.x + _xShift, event.y + _yShift);
	}

	/**
	 * This method is called to check for which area the tooltip is
	 * created/hidden for. In case of {@link #NO_RECREATE} this is used to
	 * decide if the tooltip is hidden recreated.
	 * <code>By the default it is the widget the tooltip is created for but could be any object. To decide if
	 * the area changed the {@link Object#equals(Object)} method is used.</code>
	 * @param event the event
	 * @return the area responsible for the tooltip creation or
	 *         <code>null</code> this could be any object describing the area
	 *         (e.g. the {@link Control} onto which the tooltip is bound to, a
	 *         part of this area e.g. for {@link ColumnViewer} this could be a
	 *         {@link ViewerCell})
	 */
	protected Object getToolTipArea(final Event event)
	{
		return _control;
	}

	/**
	 * Hide the currently active tool tip.
	 */
	public void hide()
	{
		toolTipHide(currentTooltip, null);
	}

	/**
	 * Return if hiding on mouse down is set.
	 * @return <code>true</code> if hiding on mouse down in the tool tip is on
	 */
	public boolean isHideOnMouseDown()
	{
		return _hideOnMouseDown;
	}

	/**
	 * Return whther the tooltip respects bounds of the display.
	 * @return <code>true</code> if the tooltip respects bounds of the display
	 */
	public boolean isRespectDisplayBounds()
	{
		return _respectDisplayBounds;
	}

	/**
	 * Return whther the tooltip respects bounds of the monitor.
	 * @return <code>true</code> if tooltip respects the bounds of the monitor
	 */
	public boolean isRespectMonitorBounds()
	{
		return _respectMonitorBounds;
	}

	/**
	 * Restore arbitary data under the given key.
	 * @param key the key
	 * @param value the value
	 */
	@SuppressWarnings(
	{"unchecked", "rawtypes"})
	public void setData(final String key, final Object value)
	{
		if (_data == null)
		{
			_data = new HashMap();
		}
		_data.put(key, value);
	}

	/**
	 * Set the hide delay.
	 * @param hideDelay the delay before the tooltip is hidden. If
	 *            <code>0</code> the tooltip is shown until user moves to other
	 *            item
	 */
	public void setHideDelay(final int hideDelay)
	{
		this._hideDelay = hideDelay;
	}

	/**
	 * If you don't want the tool tip to be hidden when the user clicks inside
	 * the tool tip set this to <code>false</code>. You maybe also need to hide
	 * the tool tip yourself depending on what you do after clicking in the
	 * tooltip (e.g. if you open a new {@link Shell})
	 * @param hideOnMouseDown flag to indicate of tooltip is hidden
	 *            automatically on mouse down inside the tool tip
	 */
	public void setHideOnMouseDown(final boolean hideOnMouseDown)
	{
		// Only needed if there's currently a tooltip active
		if (currentTooltip != null && !currentTooltip.isDisposed())
		{
			// Only change if value really changed
			if (hideOnMouseDown != this._hideOnMouseDown)
			{
				_control.getDisplay().syncExec(new Runnable()
				{
					@Override
					public void run()
					{
						if (currentTooltip != null && currentTooltip.isDisposed())
						{
							toolTipHookByTypeRecursively(currentTooltip, hideOnMouseDown, SWT.MouseDown);
						}
					}
				});
			}
		}

		this._hideOnMouseDown = hideOnMouseDown;
	}

	/**
	 * Set the popup delay.
	 * @param popupDelay the delay before the tooltip is shown to the user. If
	 *            <code>0</code> the tooltip is shown immediately
	 */
	public void setPopupDelay(final int popupDelay)
	{
		this._popupDelay = popupDelay;
	}

	/**
	 * Set to <code>false</code> if display bounds should not be respected or to
	 * <code>true</code> if the tooltip is should repositioned to not overlap
	 * the display bounds.
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 * @param respectDisplayBounds
	 */
	public void setRespectDisplayBounds(final boolean respectDisplayBounds)
	{
		this._respectDisplayBounds = respectDisplayBounds;
	}

	/**
	 * Set to <code>false</code> if monitor bounds should not be respected or to
	 * <code>true</code> if the tooltip is should repositioned to not overlap
	 * the monitors bounds. The monitor the tooltip belongs to is the same is
	 * control's monitor the tooltip is shown for.
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 * @param respectMonitorBounds
	 */
	public void setRespectMonitorBounds(final boolean respectMonitorBounds)
	{
		this._respectMonitorBounds = respectMonitorBounds;
	}

	/**
	 * Set the shift (from the mouse position triggered the event) used to
	 * display the tooltip. By default the tooltip is shifted 3 pixels to the
	 * left
	 * @param p the new shift 153
	 */
	public void setShift(final Point p)
	{
		_xShift = p.x;
		_yShift = p.y;
	}

	/**
	 * Should the tooltip displayed because of the given event.
	 * <p>
	 * <b>Subclasses may overwrite this to get custom behaviour</b>
	 * </p>
	 * @param event the event
	 * @return <code>true</code> if tooltip should be displayed
	 */
	protected boolean shouldCreateToolTip(final Event event)
	{
		if ((_style & NO_RECREATE) != 0)
		{
			Object tmp = getToolTipArea(event);

			// No new area close the current tooltip
			if (tmp == null)
			{
				hide();
				return false;
			}

			boolean rv = !tmp.equals(_currentArea);
			return rv;
		}

		return true;
	}

	/**
	 * This method is called before the tooltip is hidden.
	 * @param event the event trying to hide the tooltip
	 * @return <code>true</code> if the tooltip should be hidden
	 */
	private boolean shouldHideToolTip(final Event event)
	{
		if (event != null && event.type == SWT.MouseMove && (NO_RECREATE) != 0)
		{
			Object tmp = getToolTipArea(event);

			// No new area close the current tooltip
			if (tmp == null)
			{
				hide();
				return false;
			}

			boolean rv = !tmp.equals(_currentArea);
			return rv;
		}

		return true;
	}

	/**
	 * Start up the tooltip programmatically.
	 * @param location the location relative to the control the tooltip is shown
	 */
	public void show(final Point location)
	{
		Event event = new Event();
		event.x = location.x;
		event.y = location.y;
		event.widget = _control;
		toolTipCreate(event);
	}

	/**
	 * Tool tip create.
	 * @param event the event
	 * @return the shell
	 */
	private Shell toolTipCreate(final Event event)
	{
		if (_control != null && !_control.isDisposed() && shouldCreateToolTip(event))
		{
			Shell shell = new Shell(_control.getShell(), SWT.ON_TOP | SWT.TOOL | SWT.NO_FOCUS);
			shell.setLayout(new GridLayout());
			toolTipOpen(shell, event);

			return shell;
		}
		return null;
	}

	/**
	 * Tool tip hide.
	 * @param tip the tip
	 * @param event the event
	 */
	private void toolTipHide(final Shell tip, final Event event)
	{
		if (tip != null && !tip.isDisposed() && shouldHideToolTip(event))
		{
			_currentArea = null;
			tip.dispose();
			currentTooltip = null;
			afterHideToolTip(event);
		}
	}

	/**
	 * Tool tip hook both recursively.
	 * @param c the c
	 */
	private void toolTipHookBothRecursively(final Control c)
	{
		c.addListener(SWT.MouseDown, _hideListener);
		c.addListener(SWT.MouseExit, _hideListener);

		if (c instanceof Composite)
		{
			Control[] children = ((Composite) c).getChildren();
			for (int i = 0; i < children.length; i++)
			{
				toolTipHookBothRecursively(children[i]);
			}
		}
	}

	/**
	 * Tool tip hook by type recursively.
	 * @param c the c
	 * @param add the add
	 * @param type the type
	 */
	private void toolTipHookByTypeRecursively(final Control c, final boolean add, final int type)
	{
		if (add)
		{
			c.addListener(type, _hideListener);
		}
		else
		{
			c.removeListener(type, _hideListener);
		}

		if (c instanceof Composite)
		{
			Control[] children = ((Composite) c).getChildren();
			for (int i = 0; i < children.length; i++)
			{
				toolTipHookByTypeRecursively(children[i], add, type);
			}
		}
	}

	/**
	 * Tool tip open.
	 * @param shell the shell
	 * @param event the event
	 */
	private void toolTipOpen(final Shell shell, final Event event)
	{
		// Ensure that only one Tooltip is shown in time
		if (currentTooltip != null)
		{
			toolTipHide(currentTooltip, null);
		}

		currentTooltip = shell;

		if (_popupDelay > 0)
		{
			_control.getDisplay().timerExec(_popupDelay, new Runnable()
			{
				@Override
				public void run()
				{
					toolTipShow(shell, event);
				}
			});
		}
		else
		{
			toolTipShow(currentTooltip, event);
		}
		if (_hideDelay > 0)
		{
			_control.getDisplay().timerExec(_popupDelay + _hideDelay, new Runnable()
			{

				@Override
				public void run()
				{
					toolTipHide(shell, null);
				}
			});
		}
	}

	/**
	 * Tool tip show.
	 * @param tip the tip
	 * @param event the event
	 */
	private void toolTipShow(final Shell tip, final Event event)
	{
		if (!tip.isDisposed())
		{
			_currentArea = getToolTipArea(event);
			createToolTipContentArea(event, tip);
			if (isHideOnMouseDown())
			{
				toolTipHookBothRecursively(tip);
			}
			else
			{
				toolTipHookByTypeRecursively(tip, true, SWT.MouseExit);
			}
			tip.pack();
			tip.setLocation(fixupDisplayBounds(tip.getSize(), getLocation(tip.getSize(), event)));
			tip.setVisible(true);
		}
	}

	public boolean isVisible()
	{
		return (currentTooltip != null && !currentTooltip.isDisposed());
	}
}

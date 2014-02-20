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
/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.bbaw.pdr.ae.view.control.swtnotrwthelper;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter2;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * An {@link IControlContentAdapter} for SWT Text controls. This is a
 * convenience class for easily creating a {@link ContentProposalAdapter} for
 * text fields.
 * @since 3.2
 */
public class StyledTextContentAdapter implements IControlContentAdapter, IControlContentAdapter2
{

	private String controlContent;

	public String getControlContents(Control control)
	{

		int caret = ((StyledText) control).getCaretOffset();
		String content = ((StyledText) control).getText().substring(0, caret);
		if (content.endsWith(" "))
		{
			controlContent = "";
		}
		else
		{
			String[] splits = content.split(" ");
			controlContent = splits[splits.length - 1];
		}
		return controlContent;
	}


	public void setControlContents(Control control, String text, int cursorPosition)
	{
		((StyledText) control).setText(text);
		((StyledText) control).setSelection(cursorPosition, cursorPosition);
	}


	public void insertControlContents(Control control, String text, int cursorPosition)
	{
		Point selection = ((StyledText) control).getSelection();
		// System.out.println("StyledTextcontent adapter " + selection.x + " " +
		// selection.y);

		if (selection.x == selection.y)
		{
			((StyledText) control).replaceTextRange(selection.x - controlContent.length(), controlContent.length(), "");
			// ((StyledText) control).insert(text);
			// Insert will leave the cursor at the end of the inserted text. If
			// this
			// is not what we wanted, reset the selection.
			if (cursorPosition < text.length())
			{
				// ((StyledText) control).setSelection(selection.x +
				// cursorPosition,
				// selection.x + cursorPosition);
				((StyledText) control).setSelection(selection.x - controlContent.length(),
						selection.x - controlContent.length());

			}
		}
		else
		{
			int len = selection.y - selection.x - 1;
			if (len < 0)
				len = 0;
			// System.out.println("StyledTextcontent adapter " + selection.x +
			// " " + len);

			((StyledText) control).replaceTextRange(selection.x, len, "");
			// ((StyledText) control).insert(text);
			// Insert will leave the cursor at the end of the inserted text. If
			// this
			// is not what we wanted, reset the selection.
			if (cursorPosition < text.length())
			{
				// ((StyledText) control).setSelection(selection.x +
				// cursorPosition,
				// selection.x + cursorPosition);
				((StyledText) control).setSelection(selection.x, selection.x);

			}
		}
	}


	public int getCursorPosition(Control control)
	{
		int caret = ((StyledText) control).getCaretOffset();
		return caret;
	}


	public Rectangle getInsertionBounds(Control control)
	{
		StyledText text = (StyledText) control;
		Point caretOrigin = text.getLocationAtOffset(text.getCaretOffset());
		// We fudge the y pixels due to problems with getCaretLocation
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=52520
		return new Rectangle(caretOrigin.x + text.getClientArea().x, caretOrigin.y + text.getClientArea().y + 3, 1,
				text.getLineHeight());
	}


	public void setCursorPosition(Control control, int position)
	{
		((StyledText) control).setSelection(new Point(position, position));
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#getSelection(org.eclipse.swt.widgets.Control)
	 * @since 3.4
	 */
	public Point getSelection(Control control)
	{
		return ((StyledText) control).getSelection();
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#setSelection(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Point)
	 * @since 3.4
	 */
	public void setSelection(Control control, Point range)
	{
		// workaround the exception
		int y = range.y - range.x;
		String t = "";
		if (range.y < ((StyledText) control).getText().length())
		{
			t = ((StyledText) control).getText(range.x, range.y);
		}
		else
		{
			t = ((StyledText) control).getText(range.x, ((StyledText) control).getText().length() - 1);

		}
		if (t.contains("\n") || t.contains("\r"))
		{
			if (t.contains("\n"))
			{
				y = t.indexOf("\n");
			}
			if (t.contains("\r") && t.indexOf("\r") < y)
			{
				y = t.indexOf("\r");
			}
			((StyledText) control).setSelection(range.x, y + range.x);
		}
		else
		{
			((StyledText) control).setSelection(range);
		}
	}
}

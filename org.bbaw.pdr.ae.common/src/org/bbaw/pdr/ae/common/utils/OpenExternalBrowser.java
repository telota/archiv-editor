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
package org.bbaw.pdr.ae.common.utils;

import java.util.Arrays;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * <b>Bare Bones Browser Launch for Java</b><br>
 * Utility class to open a web page from a Swing application in the user's
 * default browser.<br>
 * Supports: Mac OS X, GNU/Linux, Unix, Windows XP/Vista/7<br>
 * Example Usage:<code><br> &nbsp; &nbsp;
 *    String url = "http://www.google.com/";<br> &nbsp; &nbsp;
 *    BareBonesBrowserLaunch.openURL(url);<br></code> Latest Version: <a
 * href="http://www.centerkey.com/java/browser/"
 * >www.centerkey.com/java/browser</a><br>
 * Author: Dem Pilafian<br>
 * Public Domain Software -- Free to Use as You Like
 * @version 3.1, June 6, 2010
 */
public class OpenExternalBrowser
{

	static final String[] browsers =
	{"google-chrome", "firefox", "opera", "epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla"};
	static final String errMsg = "Error attempting to launch web browser";

	/**
	 * Opens the specified web page in the user's default browser
	 * @param url A web address (URL) of a web page (ex:
	 *            "http://www.google.com/")
	 */
	public static void openURL(String url)
	{
		try
		{ // attempt to use Desktop library from JDK 1.6+
			Class<?> d = Class.forName("java.awt.Desktop");
			d.getDeclaredMethod("browse", new Class[]
			{java.net.URI.class}).invoke(d.getDeclaredMethod("getDesktop").invoke(null), new Object[]
			{java.net.URI.create(url)});
			// above code mimicks: java.awt.Desktop.getDesktop().browse()
		}
		catch (Exception ignore)
		{ // library not available or failed
			String osName = System.getProperty("os.name");
			try
			{
				if (osName.startsWith("Mac OS"))
				{
					Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[]
					{String.class}).invoke(null, new Object[]
					{url});
				}
				else if (osName.startsWith("Windows"))
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				else
				{ // assume Unix or Linux
					String browser = null;
					for (String b : browsers)
						if (browser == null && Runtime.getRuntime().exec(new String[]
						{"which", b}).getInputStream().read() != -1)
							Runtime.getRuntime().exec(new String[]
							{browser = b, url});
					if (browser == null)
						throw new Exception(Arrays.toString(browsers));
				}
			}
			catch (Exception e)
			{
				MessageDialog md = new MessageDialog(null, "Error", null, errMsg + "\n" + e.toString(),
						MessageDialog.ERROR, new String[]
						{"OK"}, 0);
				md.open();
			}
		}
	}

}

/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.db.basex711;

import java.io.File;
import java.io.IOException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.utils.CopyDirectory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The Class AccessPreferences.
 * @author Christoph Plutte
 */
public class AccessPreferences extends AbstractHandler
{

	/** The i logger. */
	private static ILog iLogger = Activator.getILogger();

	/** The Constant AE_HOME. */
	private static final String AE_HOME; // =
											// Platform.getPreferencesService().getString(
	// "ArchivEditor", "AE_HOME", "hello", null);
	/** The Constant FS. */
	private static final String FS = System.getProperty("file.separator");

	/** The Constant BASEX_HOME. */
	public static final String BASEX_HOME;// = AE_HOME + FS + "baseXHOME" + FS;

	static
	{
		AE_HOME = Platform.getPreferencesService().getString("org.bbaw.pdr.ae.common", "AE_HOME",
 AEConstants.AE_HOME,
				null);

		if (Platform.getPreferencesService().getBoolean("org.bbaw.pdr.ae.common",
				"SAVE_DB_IN_INSTALLATION_DIR",
				AEConstants.SAVE_DB_IN_INSTALLATION_DIR, null))
		{
			BASEX_HOME = AE_HOME + FS  + "baseXHOME" + FS;

		}
		else
		{
			String dbUserHome = System.getProperty("user.home");
			dbUserHome = dbUserHome + FS + ".ae";
			File f = new File(dbUserHome);
			String baseXUserHome = dbUserHome + FS  + "baseXHOME";

			if (!f.exists())
			{
				f.mkdir();
				f = new File(baseXUserHome);
				f.mkdir();
				File ff = new File(AE_HOME + FS  + "baseXHOME");
				if (ff.exists())
				{
					try
					{
						CopyDirectory.copyDirectory(ff, f);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			BASEX_HOME = baseXUserHome + FS;
		}
		
		// FIXME dynamisieren!!!!!!!!!!!!!!!!!1
		
		IStatus ae = new Status(IStatus.INFO, Activator.PLUGIN_ID, "BASEX_HOME1: " + BASEX_HOME);
		iLogger.log(ae);
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException
	{
		IStatus ae = new Status(IStatus.INFO, Activator.PLUGIN_ID, "BASEX_HOME2: " + BASEX_HOME);
		iLogger.log(ae);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		String text = Platform.getPreferencesService().getString("ArchivEditor", "AE_HOME", "BaseXData", null);
		MessageDialog.openInformation(window.getShell(), "Access for preferences in different plugin", text);
		return null;
	}

}

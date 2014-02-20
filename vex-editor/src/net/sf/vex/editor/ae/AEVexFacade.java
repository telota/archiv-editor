package net.sf.vex.editor.ae;



public class AEVexFacade
{

	private AEVexFacade()
	{
	}

	/** Singleton for standalone RCP. */
	private static volatile AEVexFacade singletonAEVexFacadeInstance;

	public static AEVexFacade getInstanz()
	{
		if (singletonAEVexFacadeInstance == null)
		{
			synchronized (AEVexFacade.class)
			{
				if (singletonAEVexFacadeInstance == null)
				{
					singletonAEVexFacadeInstance = new AEVexFacade();
				}
			}
		}
		return singletonAEVexFacadeInstance;
	}

	public IAEVexDialog getIAEVexDialog()
	{
		return _aeVexDialog;
	}

	public void setIAEVexDialog(IAEVexDialog aeVexDialog)
	{
		this._aeVexDialog = aeVexDialog;
	}

	private IAEVexDialog _aeVexDialog;
}

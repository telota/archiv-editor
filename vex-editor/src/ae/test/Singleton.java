package ae.test;

import net.sf.vex.editor.ae.IAEVexDialog;


public class Singleton
{

	private Singleton()
	{
	}

	/** Singleton for standalone RCP. */
	private static volatile Singleton singletonFacadeInstance;

	public static Singleton getInstanz()
	{
		if (singletonFacadeInstance == null)
		{
			synchronized (Singleton.class)
			{
				if (singletonFacadeInstance == null)
				{
					singletonFacadeInstance = new Singleton();
				}
			}
		}
		return singletonFacadeInstance;
	}

	public IAEVexDialog getIvextest()
	{
		return ivextest;
	}

	public void setIvextest(IAEVexDialog ivextest)
	{
		this.ivextest = ivextest;
	}

	private IAEVexDialog ivextest;
}

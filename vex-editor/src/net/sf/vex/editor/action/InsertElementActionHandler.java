package net.sf.vex.editor.action;

import net.sf.vex.editor.InsertAssistant;
import net.sf.vex.swt.VexWidget;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import ae.test.Singleton;

public class InsertElementActionHandler implements IHandler
{

	public void addHandlerListener(IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		VexWidget vexWidget = Singleton.getInstanz().getIvextest().getVexWidget();
		new InsertAssistant().show((VexWidget) vexWidget);
		return null;
	}

	public boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHandled()
	{
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

}

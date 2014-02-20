package org.bbaw.pdr.ae.base.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

public class RemoveDuplicateAspectsHandler implements IHandler {

	/** user rights checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	private Facade _facade = Facade.getInstanz();

	private PDRObjectsProvider _objectsProvider = new PDRObjectsProvider();

	private Comparator<Aspect> aspectIDComparator;

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!_urChecker.isUserGuest()) {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(
					HandlerUtil.getActiveShell(event).getShell());
			dialog.setCancelable(true);

			try {
				dialog.run(true, true, new IRunnableWithProgress() {
					private Object _updateStatus;

					@Override
					public void run(final IProgressMonitor monitor) {
						// Activator.getDefault().getPreferenceStore().getString("REPOSITORY_URL"));
						if (monitor.isCanceled()) {
							monitor.setCanceled(true);
						}
						removeDuplicates(monitor);
					}
				}

				);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}

			return null;
		}
		return null;
	}

	private void removeDuplicates(IProgressMonitor monitor) {
		Set<Aspect> toDelete = new HashSet<Aspect>();
		
		monitor.beginTask("Check Person and Aspects",  _facade.getAllPersons().values().size());
		for (Person p : _facade.getAllPersons().values()) {
			System.out.println(p.getDisplayName());
			if (monitor.isCanceled()) {
				monitor.setCanceled(true);
			}
			_objectsProvider.setInput(p);
			Vector<Aspect> aspects = _objectsProvider.getAspects();
			Collections.sort(aspects, getAspectIDComparator());
			System.out.println(aspects.size());
			if (aspects != null && !aspects.isEmpty()) {
				Set<Aspect> toCompare = new HashSet<Aspect>();
				Set<Aspect> tempToDelete = new HashSet<Aspect>();
				// fill set to compare all
				for (Aspect a : aspects) {
					toCompare.add(a);
				}
				Aspect leftAspect = null;
				int index = 0;
				// compare
				while (!toCompare.isEmpty() && index < aspects.size()) {
					tempToDelete.clear();
					leftAspect = aspects.get(index);
					toCompare.remove(leftAspect);

					for (Aspect a : toCompare) {
						if (equalsContentRelation(leftAspect, a)) {
							tempToDelete.add(a);
						}
					}

					index++;
					toCompare.removeAll(tempToDelete);
					toDelete.addAll(tempToDelete);
					if (monitor.isCanceled()) {
						monitor.setCanceled(true);
					}
				}

			}
			monitor.worked(1);
		}
		System.out.println("Aspects to remove: " + toDelete.size());
		IDBManager dbm = _facade.getDBManager();
		monitor.beginTask("Delete Duplicate Aspects. Number of Aspects to delete: " + toDelete.size(), toDelete.size());

		for (Aspect a : toDelete) {
			try {
				dbm.delete(a.getPdrId(), "aspect");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (monitor.isCanceled()) {
				monitor.setCanceled(true);
			}
			monitor.worked(1);
		}
		UIJob job = new UIJob("Feedbackup")
		{
			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor)
			{		
				_facade.refreshAllData();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	private boolean equalsContentRelation(Aspect leftAspect, Aspect aspect) {
		if (!leftAspect.equalsContent(aspect))
		{
			return false;
		}
		if (leftAspect.getRelationDim() != null && aspect.getRelationDim() != null)
		{
			if (leftAspect.getRelationDim().getRelationStms()!= null && aspect.getRelationDim().getRelationStms() != null)
			{
				//test
				if (leftAspect.getRelationDim().getRelationStms().size() > 3)
				{
					System.out.println("jetzt");
				}
				if (!(leftAspect.getRelationDim().getRelationStms().size() == aspect.getRelationDim().getRelationStms().size()))
				{
					return false;
				}
				for (int i = 0; i < leftAspect.getRelationDim().getRelationStms().size(); i++)
				{
					if (!(leftAspect.getRelationDim().getRelationStms().get(i).equals(aspect.getRelationDim().getRelationStms().get(i)) || leftAspect.getRelationDim().getRelationStms().get(i).similarRelations(aspect.getRelationDim().getRelationStms().get(i), leftAspect.getPdrId(), aspect.getPdrId(), leftAspect.getOwningObjectId(), leftAspect.getOwningObjectId())))
					{
						return false;
					}
				}
			}
			else if ((leftAspect.getRelationDim().getRelationStms() == null && aspect.getRelationDim().getRelationStms() != null)
					|| (leftAspect.getRelationDim().getRelationStms() != null && aspect.getRelationDim().getRelationStms() == null))
			{
				return false;
			}
		}
		else if ((leftAspect.getRelationDim() != null && aspect.getRelationDim() == null)
				|| (leftAspect.getRelationDim() == null && aspect.getRelationDim() != null))
		{
			return false;
		}
		return true;
	}

	private Comparator<Aspect> getAspectIDComparator() {
		if (aspectIDComparator == null)
		{
			aspectIDComparator = new Comparator<Aspect>() {

				@Override
				public int compare(Aspect a1, Aspect a2) {
					if (a1 != null && a2 != null)
					{
						if (a1.getPdrId() != null && a2.getPdrId() != null)
						{
							return a1.getPdrId().getId() - a2.getPdrId().getId();
						}
					}
					return 0;
				}
			};
		}
		return aspectIDComparator;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {

	}

}

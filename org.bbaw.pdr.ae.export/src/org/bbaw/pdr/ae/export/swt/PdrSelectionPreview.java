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
package org.bbaw.pdr.ae.export.swt;

import java.util.Vector;

import org.bbaw.pdr.ae.export.internal.PdrObjectSelectionStructure;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.osgi.framework.FrameworkUtil;

//TODO: doc
/**
 * 
 * @author jhoeper
 *
 */
@Deprecated
public class PdrSelectionPreview extends CheckboxTreeViewer 
								implements IPdrWidgetStructure{
	

	/**
	 * Content provider for {@link PdrSelectionPreview}.
	 * 
	 * <p>Relies heavily on {@link PdrObjectSelectionStructure}, where
	 * {@link #getChildren(Object)} and {@link #inputChanged(Viewer, Object, Object)}
	 * are handled with the help of a {@link PDRObjectsProvider} overall structure.
	 * </p>
	 * 
	 * @author jhoeper
	 * @see #getContent()
	 * @see PdrObject
	 */
	@Deprecated
	private class PdrPreviewContentProvider implements ITreeContentProvider{
		protected PdrObjectSelectionStructure structure;
		
		public PdrPreviewContentProvider() {
			System.out.println(" setting up tree viewer content provider");
			structure = new PdrObjectSelectionStructure();
		}
		public PdrObjectSelectionStructure getContent() {
			return structure;
		}
		@Override
		public void dispose() {
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			try {
				PdrObject[] pdrObjects = (PdrObject[])newInput;
				if (pdrObjects != null) {
					this.structure.setInput(pdrObjects);
				}
			} catch (Exception e) {
				System.out.println("    Tree Content Provider input update failed");
				System.out.println("     "+e);
			}
		}
		@Override
		public Object[] getElements(Object inputElement) {
			return this.structure.getObjects();
		}
		@Override
		public Object[] getChildren(Object parentElement) {
			PdrObject parentPdrObject = ((PdrObject)parentElement);
//			System.out.println(" initiating search for child elements of: "+parentElement);
			return this.structure.getChildren(parentPdrObject);
		}
		@Override
		public Object getParent(Object element) {
			return structure.getParent(element);
		}
		@Override
		public boolean hasChildren(Object element) {
			return ((PdrObject)element).getAspectIds().size() > 0;
		}
	}
	/**
	 * Basic {@link LabelProvider} extension decorating {@link CheckboxTreeViewer}
	 * node elements with the display String of the represented {@link PdrObject}
	 * object and an Icon classifying it as either {@link Person} or {@link Aspect}.
	 * @author jhoeper
	 *
	 */
	@Deprecated
	private class PdrPreviewLabelProvider extends LabelProvider {
		//TODO: http://javawiki.sowas.com/doku.php?id=swt-jface:treetableviewer
		@Override
		public String getText(Object element) {
			PdrObject pdrObject = (PdrObject)element;
			return pdrObject.getDisplayNameWithID();
		}
		@Override
		public Image getImage(Object element) {
			PdrId pdrId = ((PdrObject)element).getPdrId();
			if (pdrId.isValid()) {
				String type = pdrId.getType();
				if (type.equals("pdrPo"))
					return AeExportCoreProvider.ICON_PDR_PERSON;
				else if (type.endsWith("pdrAo"))
					return AeExportCoreProvider.ICON_PDR_ASPECT;
			}
			return null;
		}
	}
	/**
	 * This Listener basically just obverses which tree items are being
	 * selected and in what way a new selection is different from the one
	 * before. 
	 * @author jhoeper
	 * @see PdrPreviewCheckStateListener
	 */
	@Deprecated
	private class PdrPreviewSelectionListener implements 
				ISelectionChangedListener {
		private StructuredSelection previous, current;
		public PdrPreviewSelectionListener() {
			addSelectionChangedListener(this);
			previous = null;
			current = new StructuredSelection();
		}
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			StructuredSelection arg = (StructuredSelection)event.getSelection();
			previous = current;
			current = arg; 
		}
		/**
		 * Returns a presumption on weather it seems like a multi-item selection
		 * put together recently has collapsed even more recently due to a user
		 * interaction that probably was performed with the intention of keeping
		 * said selection.
		 * <p>The most likely use case to require this is that a user has 
		 * selected multiple tree items and wants to change the <code>check 
		 * state</code> value of every selected item simultaneously. Any action
		 * leading to an updated item check state will also result in a new
		 * single item selection, so {@link PdrPreviewCheckStateListener}
		 * probably will {@link #rewind()} the selection history in order to
		 * operate on every of the intentionally selected items.</p>
		 * @return <code>true</code> if the current selection seems to have
		 * replaced a larger one
		 */
		public boolean rewindable() {
			return 	(current.size() == 1) &&
					(previous.size() > 1) && 
					(previous.toList().contains(current.getFirstElement()));
		}
		/**
		 * Backs up one step in selection history by restoring the very 
		 * selection that has been replaced the most recently.  
		 */
		public void rewind() {
			setSelection(previous);
		}
	}
	/**
	 * This listener keeps the hierarchic checking logic of the preview tree
	 * up to date. Whenever the check state of an item changes, the check states
	 * of related items are updated according to the following rules:
	 * <ul><li>If the item whose check state was changed is expandable, assign
	 * its check state to all its direct children</li>
	 * <li>If the item is representing an {@link Aspect}, and its check state 
	 * has been set to <code>true</code>, make sure the parent gets checked
	 * as well in order to prevent <code>Aspect</code> items from ending up
	 * being selected for export with no {@link Person} alongside itself</li>
	 * <li>If multiple items are selected at once, have them all being of the
	 * check state that was changed</li></ul>
	 * <p>The last rule is enforced by asking {@link 
	 * PdrPreviewSelectionListener} if the user's interaction that resulted in 
	 * an updated check state might have destroyed a recently set up multi-item 
	 * selection. If so, we assume that the user did actually intend to apply
	 * the outcome of their interaction to each item formerly assembled by the
	 * multi-item selection. To make this implicitly expected result come true,
	 * we {@link PdrPreviewSelectionListener#rewind()} the most recent selection
	 * history and proceed as specified above.</p>
	 * <p>Both <code>PdrPreviewCheckStateListener</code> and {@link 
	 * PdrPreviewSelectionListener} auto-activate themselves as soon as they
	 * are being instantiated by {@link PdrSelectionPreview}</p> 
	 *    
	 * @author jhoeper
	 * @see PdrPreviewSelectionListener
	 */
	@Deprecated
	private class PdrPreviewCheckStateListener implements ICheckStateListener {
		public PdrPreviewCheckStateListener() {
			addCheckStateListener(this);
		}
	    public void checkStateChanged(CheckStateChangedEvent event) {
			// if there is more than one item selected, assign the same
			// checked status to all of them
			if (selectionListener.rewindable()) {
				selectionListener.rewind();
				StructuredSelection selectedItems = 
						(StructuredSelection)getSelection();
				for (Object obj : selectedItems.toArray()) 
					setChecked(obj, event.getChecked());
			}
	    	  // if object is expandable, meaning likely a person,
	    	  // check all related aspects according to check state
			if (isExpandable(event.getElement())) {
				  expandToLevel(event.getElement(), 
						  CheckboxTreeViewer.ALL_LEVELS);
				  setSubtreeChecked(event.getElement(), 
						  event.getChecked());
			}
			if (event.getChecked()) {
				  // if checked object is an Aspect, check the person as well  
				  PdrObject node = (PdrObject)event.getElement();
				  if (node instanceof Aspect) {
					  setChecked(((PdrPreviewContentProvider) getContentProvider()).
					  	getParent(node), true);
					  setValid();
				  } else if (node instanceof Person)
					  setValid();
			} else {
				// check if at least one Pdr Person is checked in preview
				// otherwise, preview contents are marked invalid
				Object[] checkedElements = getCheckedElements(); 
				if (checkedElements.length>0) {
					for (Object chckObj : checkedElements)
						if (chckObj instanceof Person) {
							setValid();
							return;
						}
					setInvalid("Please select at least one Person.");
				} else {
					setInvalid("Please select some content to export.");
				}
			}
	    }
	}

	//TODO Maybe extend widget in a way that export to multiple files at once is possible
	/* In order to allow for choosing to export multiple pdr persons in multiple files
	 * at once, this class would then have to inform the core export plugin, which crafts
	 * the PdrObject selection representation used by extending plugins, about
	 * that choice, possibly via the local export provider.  
	 */
	
	// PdrSelectionPreview private fields
	private Tree tree;
	ITreeContentProvider contentProvider;
	LabelProvider labelProvider;
	
	PdrPreviewSelectionListener selectionListener;
	PdrPreviewCheckStateListener checkStateListener;
	
	private boolean isvalid;
	private String message;
	
	private WizardPage wizardPage;
	
	/**
	 * Creates a PDR Selection Preview on a newly-created tree under the given
	 * parent.
	 * <p>Explicitly intended to be used in a {@link GridLayout} environment.
	 * That means that the <code>parent</code> composite must provide such a 
	 * layout.</p> 
	 * <p>Standard layout data is applied using {@link SWT#FILL} and 
	 * grabbing excess space flags both horizontally and vertically. <br/>
	 * Layout data may however be customized by using {@link #setLayoutData(int, 
	 * int, boolean, boolean, int, int)} if specification row or column span 
	 * is desired.</p> 
	 * @param parent the parent Control with {@link GridLayout}
	 */
	public PdrSelectionPreview(WizardPage page, Composite parent) {
		super(parent, SWT.BORDER | SWT.CHECK | SWT.MULTI | SWT.V_SCROLL);
		this.wizardPage = page;
		tree = getTree();
		
		// bypass to prevent eclipse window builder from crashing
		if (FrameworkUtil.getBundle(getClass()) != null) {
			contentProvider = new PdrPreviewContentProvider();
			labelProvider = new PdrPreviewLabelProvider();

			setContentProvider(contentProvider);
			setLabelProvider(labelProvider);		
		}
		
		setLayoutData(SWT.FILL, SWT.FILL, true, true);
		isvalid = true;
	}

	/**
	 * Sets a custom layout data for this tree viewer. Calling this method 
	 * does the same as calling {@link Control#setLayoutData(Object)}.
	 * <p>In addition, minimum width and height are set as 150 and 100 pixels
	 * respectively, {@link Tree#setLinesVisible(boolean)} is set <code>true
	 * </code> and an {@link ICheckStateListener} is initialized for triggering
	 * shallow recursive checking of subtrees whenever a expandable node is
	 * checked and checking of the nodes of which at least one child node
	 * has been checked.</p>
	 * <p>Span numbers are optional. {@link #setLayoutData(int, int, boolean, 
	 * boolean)} may be used as well.</p> 
	 * @param horizontalAlignment {@link SWT} style for horizontal alignment 
	 * @param verticalAlignment {@link SWT} style for vertical alignment
	 * @param grabExcessHorizontalSpace boolean space grabbing flag horizontal
	 * @param grabExcessVerticalSpace space grabbing flag vertical
	 * @param horizontalSpan number of columns to be used in gridlayout
	 * @param verticalSpan number of rows to be used in gridlayout
	 */
	public void setLayoutData(int horizontalAlignment, int verticalAlignment, 
			boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace, 
			int horizontalSpan, int verticalSpan) {
		GridData gd = new GridData(horizontalAlignment, verticalAlignment, 
				grabExcessHorizontalSpace, grabExcessVerticalSpace, 
				horizontalSpan, verticalSpan);
		gd.minimumHeight = 100;
		gd.minimumWidth = 150;
		tree.setLayoutData(gd);
		tree.setLinesVisible(true);
		
		checkStateListener = new PdrPreviewCheckStateListener();
		selectionListener = new PdrPreviewSelectionListener();
	}
	/**
	 * The same thing as {@link #setLayoutData(int, int, boolean, boolean, 
	 * int, int)} except for that no column and row spans are used.
	 * @param horizontalAlignment {@link SWT} style for horizontal alignment 
	 * @param verticalAlignment {@link SWT} style for vertical alignment
	 * @param grabExcessHorizontalSpace boolean space grabbing flag horizontal
	 * @param grabExcessVerticalSpace space grabbing flag vertical
	 * @see #setLayoutData(int, int, boolean, boolean, int, int)
	 */
	public void setLayoutData(int horizontalAlignment, int verticalAlignment, 
			boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace) {
		setLayoutData(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace,
				grabExcessVerticalSpace, 1, 1);
	}
	
	/**
	 * Sets the input data for this tree viewer as an array of {@link PdrObject}
	 * elements. Each and every object is being 'checked' by default.
	 * @param input An array of {@link PdrObject}s
	 */
	public void setInput(PdrObject[] input) {
		super.setInput(input);
		for (PdrObject pdro : input) {
			setChecked(pdro, true);
			for (Object child : 
				((PdrPreviewContentProvider)getContentProvider()).
					getChildren(pdro))
				//TODO: represent aspect view selections?
				setChecked(child, true);
		}
	}
	
	/**
	 * returns an array of all objects represented by tree items whose check
	 * state is true, no matter if they are persons or aspects
	 * @return
	 */
	public PdrObject[] getSelectedObjects() {
		Vector<PdrObject> checked = new Vector<PdrObject>();
		PdrObject[] persons = ((PdrPreviewContentProvider)contentProvider).
				getContent().getObjects();
		for (PdrObject person : persons)
			if (getChecked(person)) {
				checked.add(person);
				for (Object child : contentProvider.getChildren(person))
					if (getChecked(child))
						checked.add((PdrObject)child);
			}
		return checked.toArray(new PdrObject[checked.size()]);
	}
	
	/**
	 * Returns wheather the widget's contents are valid in a way that for 
	 * instance export wizards can continue given its state.
	 * @return
	 */
	@Override
	public boolean isValid() {
		return isvalid;
	}
	
	/**
	 * Mark this widget invalid.
	 * @param message Complementary error message.
	 */
	private void setInvalid(String message) {
		if (isvalid) if (message != null && !message.equals(this.message)){
			System.out.println("Mark previewer as invalid");
			isvalid = false;
			this.message = message;
			IWizardContainer wizardContainer = this.wizardPage.getWizard().getContainer(); 
			wizardContainer.updateButtons();
			wizardContainer.updateMessage();
		}
	}
	
	/**
	 * Mark this widget valid
	 */
	private void setValid() {
		if (!isvalid) {
			isvalid = true;
			this.message = null;
			IWizardContainer wizardContainer = this.wizardPage.getWizard().getContainer(); 
			wizardContainer.updateButtons();
			wizardContainer.updateMessage();
		}
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public void performFinish() {
	}
}


/*		setCheckStateProvider(new ICheckStateProvider(){
@Override
public boolean isChecked(Object element) {
	return getChecked(element);
}
@Override
public boolean isGrayed(Object element) {
	PdrObject pdrO = (PdrObject) element; 
	if (pdrO instanceof Person) {
		Object[] children = 
		((PdrPreviewContentProvider) getContentProvider()).
			getChildren(pdrO);
		int checked = 0;
		for (Object child : children)
			if (getChecked(child))
				checked++;
		System.out.println("children checked out of: "+checked+"/"+children.length);
		return (checked < children.length) |
				(checked > 0);
	}
	return false;
}
});*/

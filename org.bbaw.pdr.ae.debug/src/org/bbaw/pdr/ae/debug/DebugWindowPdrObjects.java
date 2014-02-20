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
package org.bbaw.pdr.ae.debug;

import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.main.views.AspectsView;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.bbaw.pdr.ae.view.control.orderer.AspectByYearOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByMarkupOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPersonOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPlaceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByReferenceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByRelationOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsBySemanticOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByUserOrderer;
import org.bbaw.pdr.ae.control.facade.Facade;

public class DebugWindowPdrObjects extends TitleAreaDialog {

	private Vector<Text> output;
	private Composite container;
	
	public DebugWindowPdrObjects(Shell parentShell) {
		super(parentShell);
		output = new Vector<Text>();
	}
	
	@Override
	public void create() {
		super.create();
		setTitle("PDRObjectProvider contents");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
	    parent.setLayout(new GridLayout());
	    parent.setSize(500, 700);
	    
	    ScrolledComposite scroll = new ScrolledComposite(parent, SWT.V_SCROLL);
	    scroll.setLayout(new GridLayout());
	    scroll.setLayoutData(new GridData(GridData.FILL_BOTH));

	    container = new Composite(scroll, SWT.BORDER);
	    container.setLayout(new GridLayout());
	    container.setLayoutData(GridData.FILL_BOTH);
	    // create output in text widget
	    print();
	    
	    //scroll.setMinSize(container.getSize().x,600);
	    container.pack();
	    scroll.setContent(container);
	    scroll.setRedraw(true);
		return parent;
	}
	
	  @Override
	  protected void createButtonsForButtonBar(Composite parent) {
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.horizontalAlignment = SWT.CENTER;
	    parent.setLayoutData(gridData);
	    Button cancelButton = 
	        createButton(parent, CANCEL, "Cancel", false);
	    cancelButton.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	        setReturnCode(CANCEL);
	        close();
	      }
	    });
	  }
	  
	  protected Button createOkButton(Composite parent, int id, 
	      String label,
	      boolean defaultButton) {
	    ((GridLayout) parent.getLayout()).numColumns++;
	    Button button = new Button(parent, SWT.PUSH);
	    button.setText(label);
	    button.setFont(JFaceResources.getDialogFont());
	    button.setData(new Integer(id));
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        if (isValidInput()) {
	          okPressed();
	        }
	      }
		private boolean isValidInput() {
			return true;
		}
	    });
	    if (defaultButton) {
	      Shell shell = parent.getShell();
	      if (shell != null) {
	        shell.setDefaultButton(button);
	      }
	    }
	    button.addListener(SWT.ABORT, new Listener() {
			public void handleEvent(Event e) {
				close();
			}
		});
	    setButtonLayoutData(button);
	    return button;
	  }
	
	  
	private void println(String line) {
		if (this.output.size() < 1)
			newBox();
		Text box = this.output.lastElement();
		try {
			if (line != null) {
				box.append(line);
			} else 
				box.append("NULL");
			box.append("\n");
	    } catch (Exception e) {
	    	newBox();
			println(e.toString());
			for (StackTraceElement te: e.getStackTrace()) {
				String stck = te.toString();
				if (stck != null) {
					println(stck);
					if (stck.contains(this.getClass().getName()))
						break;
				}
			}
			newBox();
	    }
	}
	
	private void println(int indentation, String line) {
		if (this.output.size() < 1)
			newBox();
		Text box = this.output.lastElement();
		for (int i=0; i<indentation; i++)
			box.append(" ");
		println(line);
	}
	
	private void newBox(){
		Text text = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		text.setEditable(false);
		this.output.add(text);
	}

	/**
	 * Print {@link PDRObjectsProvider} contents
	 * @param output
	 */
	private void print() {
		Facade facade = Facade.getInstanz();
	    PDRObjectsProvider provider = AspectsView.instance.getPdrObjectsProvider();
	    
	    Vector<OrderingHead> groups = provider.getArrangedAspects();
	    if (groups == null) {
	    	println("No aspects selected");
	    	return;
	    }
	    
	    PDRObjectsOrderer agg = provider.getOrderer();
	    println("Aggregated by:");
	    println(1, "Person: "+(agg instanceof AspectsByPersonOrderer));
	    println(1, "Year: "+(agg instanceof AspectByYearOrderer));
	    println(1, "Markup: "+(agg instanceof AspectsByMarkupOrderer));
	    println(1, "Place: "+(agg instanceof AspectsByPlaceOrderer));
	    println(1, "Reference: "+(agg instanceof AspectsByReferenceOrderer));
	    println(1, "Relation: "+(agg instanceof AspectsByRelationOrderer));
	    println(1, "Semantic: "+(agg instanceof AspectsBySemanticOrderer));
	    println(1, "User: "+(agg instanceof AspectsByUserOrderer));
	    
	    for (OrderingHead group : groups) {
	    	newBox();
	    	// Group label
	    	println(group.getLabel());
	    	// content
	    	//println(1,group.getContent());
	    	//println(1,group.getDescription());
	    	println(1,group.getValue());
	    	//println(1,""+group.getPriority());
	    	Vector<Aspect> aspects = group.getAspects();
	    	// Aspects
	    	for (Aspect a : aspects) {
	    		PdrId owner = a.getOwningObjectId();
	    		println(2, facade.getPerson(owner).getDisplayName()+" "+owner.getId());
	    		println(3, a.getDisplayNameWithID());
	    		
	    		Vector<String> providers = a.getSemanticDim().getSemanticProviders();
	    		String prvds = "";
	    		for (String prvd : providers)
	    			prvds += prvd+" ";
	    		println(4, prvds);
	    		Vector<SemanticStm> stmnts = a.getSemanticDim().getSemanticStms();
	    		for (SemanticStm stm : stmnts)
	    			println(5, stm.getLabel()+" "+stm.getProvider());
	    	}
	    }
	    
	    newBox();
	    Vector<String> semantix = provider.getAspectsSemantics();
	    for (String sem : semantix)
	    	println(sem);
	    
	    newBox();
	    println("Shown by person: "+provider.isShownByPerson());
	    println("Ordered by semantics: "+provider.isOrderedBySemantic());
	    
	}

}

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
package org.bbaw.pdr.ae.export.swt.preview;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.export.logic.StructNode;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class PdrSelectionPreviewColumns {

	static ImageRegistry imgReg = CommonActivator.getDefault().getImageRegistry();
	
	final static HashMap<String, Image> icons = new HashMap<String, Image>();
	static {
		icons.put("semantic", imgReg.get(IconsInternal.CLASSIFICATION));
		icons.put("reference", imgReg.get(IconsInternal.REFERENCES));
		icons.put("person", imgReg.get(IconsInternal.PERSON));
		icons.put("year", imgReg.get(IconsInternal.TIME));
		icons.put("place", imgReg.get(IconsInternal.PLACE));
		icons.put("relation", imgReg.get(IconsInternal.RELATION));
		icons.put("user", imgReg.get(IconsInternal.USER));
		icons.put("markup", imgReg.get(IconsInternal.MARKUP));
	}
	
	private class PdrObjectsLblProv extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			return node.getLabel();
		}
		@Override
		public Image getImage(Object element) {
			StructNode node = ((StructNode)element);
			if (node.getType().startsWith("pdr")) {			
				if (node.getContent() instanceof Person)
					return imgReg.get(IconsInternal.PERSON);
				else if (node.getContent() instanceof Aspect)  {
					if (node.getType().endsWith("normname"))
						return imgReg.get(IconsInternal.CLASSIFICATION_NAME_NORM);
					return imgReg.get(IconsInternal.ASPECT);
				}
				else if (node.getContent() instanceof ReferenceMods)
					return imgReg.get(IconsInternal.REFERENCE);
			} else if (node.getType().startsWith("grouped.")){
				for (Entry<String, Image> icnd : icons.entrySet())
					if (node.getType().contains(icnd.getKey()))
						return icnd.getValue();
			}
			return null;
		}
		/*public Image getColumnImage(Object element, int index) {
			return getImage(element);
		}*/
		@Override
		public void update(ViewerCell cell) {
			super.update(cell);			
		}
	}
	private class ChronoLblProv extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			if (node.getContent() instanceof Aspect) {
				if (((Aspect)node.getContent()).getTimeDim().isValid()) {
					Vector<TimeStm> stms = ((Aspect)node.getContent()).getTimeDim().getTimeStms();
					if (stms.size()>0) {
						Vector<Integer> years = new Vector<Integer>();
						for (TimeStm stm : stms) {
							Vector<Time> times = stm.getTimes();
							for (Time t : times)
								years.add(t.getTimeStamp().getYear());
						}
						if (years.size()>0){
							Collections.sort(years);
							if (years.lastElement()>years.firstElement()) 
								return years.firstElement()+"-"+years.lastElement();
							return ""+years.firstElement();
						}
					}
				}
			}
			return "";
		}
	}
	private class CategoryLblProv extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			if (node.getContent() instanceof Aspect){
				String categories = "";
				for (SemanticStm stm : ((Aspect)node.getContent()).getSemanticDim().getSemanticStms())
					categories+=" "+stm.getLabel();
				return categories;
			}
			return "";
		}
	}
	private class RecentChgLblProv extends ColumnLabelProvider {
		private Date time;
		private DateFormat df;
		public RecentChgLblProv(){
			super();
			time = new Date();
			df = DateFormat.getDateInstance();
		}
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			if (node.getContent() instanceof Aspect) {
				Vector<Revision> revisions = ((Aspect)node.getContent()).getRecord().getRevisions();
				if (revisions.size()>0){
					Vector<Date> timestamps = new Vector<Date>();
					for (Revision rev : revisions)
						timestamps.add(rev.getTimeStamp());
					Collections.sort(timestamps);
					long changed = (time.getTime()-timestamps.lastElement().getTime())/60000;
					if (changed < 60) {
						return changed+" min ago";
					} else if (changed < 24*60) {
						return changed/60+" hours ago";
					} else if (changed < 24*60*7) {
						return changed/24/60+" days ago";
					} else if (changed < 24*60*4) {
						return changed/24/60/7+" weeks ago";
					}
					return df.format(timestamps.lastElement());
				}
			}
			return "";
		}
	}
	private class ReferenceLblProv extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			if (node.getContent() instanceof Aspect){
				Vector<ValidationStm> sources = ((Aspect)node.getContent()).getValidation().getValidationStms();
				for (ValidationStm src : sources){
					//FIXME: macht keinen sinn
					return src.getReference().getInternal();
				}
			}
			return "";
		};
	}
	private class CreatorLblProv extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			StructNode node = (StructNode)element;
			if (node.getContent() instanceof Aspect){
				Revision firstRev = ((Aspect)node.getContent()).getRecord().getRevisions().firstElement();
				return firstRev.getRevisor();
			}
			return "";
		};
	}
	
	private class ColumnSelectionListener extends SelectionAdapter {
		private int index;
		public ColumnSelectionListener(int i){
			index = i;
		}
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			columnSort = preview.sortBy(index);
		}
		
	}
	
	private TreeViewerColumn pdrObjectsCol;
	private TreeViewerColumn chronoCol;
	private TreeViewerColumn categoryCol;
	private TreeViewerColumn recentChgCol;
	private TreeViewerColumn referenceCol;
	private TreeViewerColumn creatorCol;
	
	private PdrSelectionFilterPreview preview;
	private int columnCount = 0;
	private int columnSort = 0;
	
	private static PdrSelectionPreviewColumns instance;
	
	public static PdrSelectionPreviewColumns createColumns(PdrSelectionFilterPreview preview, 
			Composite parent) {
		instance = new PdrSelectionPreviewColumns(preview, parent);
		return instance;
	}
	
	private PdrSelectionPreviewColumns(PdrSelectionFilterPreview preview, Composite parent){
	
		this.preview = preview;
		
		pdrObjectsCol = createColumn("", SWT.CENTER, true, false);
		chronoCol = createColumn(NLMessages.getString("View_sort_cronologically"), SWT.CENTER, true, true);
		categoryCol = createColumn(NLMessages.getString("View_sort_semantic"), SWT.CENTER, true, true);
		recentChgCol = createColumn(NLMessages.getString("View_sort_revision"), SWT.CENTER, true, true);
		referenceCol = createColumn(NLMessages.getString("View_sort_reference"), SWT.CENTER, true, true);
		creatorCol = createColumn(NLMessages.getString("View_sort_creator"), SWT.CENTER, true, true);
		
		pdrObjectsCol.setLabelProvider(new PdrObjectsLblProv());
		chronoCol.setLabelProvider(new ChronoLblProv());
		categoryCol.setLabelProvider(new CategoryLblProv());
		recentChgCol.setLabelProvider(new RecentChgLblProv());
		referenceCol.setLabelProvider(new ReferenceLblProv());
		creatorCol.setLabelProvider(new CreatorLblProv());		

		TreeColumnLayout columnLayout = new TreeColumnLayout();
		parent.setLayout(columnLayout);

		columnLayout.setColumnData(pdrObjectsCol.getColumn(), new ColumnWeightData(40));
		columnLayout.setColumnData(chronoCol.getColumn(), new ColumnWeightData(10));
		columnLayout.setColumnData(categoryCol.getColumn(), new ColumnWeightData(20));
		columnLayout.setColumnData(recentChgCol.getColumn(), new ColumnWeightData(10));
		columnLayout.setColumnData(referenceCol.getColumn(), new ColumnWeightData(15));
		columnLayout.setColumnData(creatorCol.getColumn(), new ColumnWeightData(10));	
	}
	
	private TreeViewerColumn createColumn(String caption, int style, 
			boolean resizable, boolean moveable){
			TreeViewerColumn col = new TreeViewerColumn(preview, style, columnCount);
			col.getColumn().setText(caption);
			col.getColumn().setResizable(resizable);
			col.getColumn().setMoveable(moveable);
			// TODO: sortieren reparieren, listener wieder rein
			// col.getColumn().addSelectionListener(new ColumnSelectionListener(columnCount));
			columnCount++;
			return col;
	}
	
	public static void setColumnLabel(String label) {
		instance.pdrObjectsCol.getColumn().setText(label);
	}
	
	/**
	 * Returns the index number of the column that caused the most recent re-sorting of
	 * the {@link PdrSelectionFilterPreview} content, for the sake of which this 
	 * {@link TreeViewerColumn} ensemble has been created, plus an indicator of the
	 * contents being sorted in reverse order. Ids range from 0 to 5, with 8 being added
	 * if reverse order has been applied. 
	 * @return
	 */
	public int getSortingColumn() {
		return instance.columnSort;
	}

}

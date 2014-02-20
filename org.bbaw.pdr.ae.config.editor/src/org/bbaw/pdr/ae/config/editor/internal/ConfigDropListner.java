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
package org.bbaw.pdr.ae.config.editor.internal;

import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

/** drop listener for drag-n-drop support in config editor tree viewer.
 * @author Christoph Plutte
 *
 */
public class ConfigDropListner extends ViewerDropAdapter
{

	/** treeviewer.*/
	private final TreeViewer _viewer;

	/** drop target.*/
	private ConfigData _target;
	/** location.*/
	private int _location;
	/** constructor with treeviewer.
	 * @param viewer viewer
	 */
	public ConfigDropListner(final TreeViewer viewer)
	{
		super(viewer);
		this._viewer = viewer;
	}

	@Override
	public final void drop(final DropTargetEvent event)
	{
		_location = this.determineLocation(event);
		_target = ((ConfigTreeNode) determineTarget(event)).getConfigData();
//		String translatedLocation ="";
//		switch (location){
//		case 1 :
//			translatedLocation = "Dropped before the target ";
//			break;
//		case 2 :
//			translatedLocation = "Dropped after the target ";
//			break;
//		case 3 :
//			translatedLocation = "Dropped on the target ";
//			break;
//		case 4 :
//			translatedLocation = "Dropped into nothing ";
//			break;
//		}
//
//		System.out.println(translatedLocation);
//		System.out.println("The drop was done on the element: " + target.getValue() );
		super.drop(event);
	}

	// This method performs the actual drop
	// We simply add the String we receive to the model and trigger a refresh of the
	// viewer by calling its setInput method.
	@Override
	public final boolean performDrop(Object dataObject)
	{
		IStructuredSelection selection = (IStructuredSelection) _viewer.getSelection();
        Object obj =  selection.getFirstElement();
        ConfigTreeNode tn = (ConfigTreeNode) obj;
        ConfigData data = tn.getConfigData();
		
        
        
        switch (_location) {
		case 1: // before target
		{
//			if (data instanceof ConfigItem && !(_target instanceof ComplexSemanticTemplate || _target instanceof SemanticTemplate || _target instanceof AspectConfigTemplate))
//            {
//				ConfigItem dataItem = (ConfigItem) data;
				if (_target.getParent().equals(data.getParent()))
				{
					data.setPriority(_target.getPriority());
					_target.setPriority(_target.getPriority() + 1);
				}
				else
				{
					data.getParent().getChildren().remove(data.getValue());
					data.setParent(_target.getParent());
					resetItemType(data.getParent(), data);
					data.getParent().getChildren().put(data.getValue(), data);
					data.setPriority(_target.getPriority());
					_target.setPriority(_target.getPriority() + 1);
//					System.out.println("inserting a other level");
					if (data.getChildren() != null)
					{
						for (String key : data.getChildren().keySet())
						{
							resetItemType(data, data.getChildren().get(key));
						}
					}
				}
            	
//            }
//			else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && _target.getParent() instanceof ComplexSemanticTemplate)
//			{
//				return true;
//			}
//			else if (data instanceof AspectConfigTemplate  && _target instanceof AspectConfigTemplate)
//			{
//				return true;
//			}
		}
			break;
		case 2: // after _targetget
		{
//			if (data instanceof ConfigItem && !(_target instanceof ComplexSemanticTemplate || _target instanceof SemanticTemplate || _target instanceof AspectConfigTemplate))
//            {
//				ConfigItem dataItem = (ConfigItem) data;
				if (_target.getParent().equals(data.getParent()))
				{
					data.setPriority(_target.getPriority() + 1);
				}
				else
				{
					data.getParent().getChildren().remove(data.getValue());
					data.setParent(_target.getParent());
					resetItemType(data.getParent(), data);
					data.getParent().getChildren().put(data.getValue(), data);
					data.setPriority(_target.getPriority() + 1);
//					System.out.println("inserting a other level");
					if (data.getChildren() != null)
					{
						for (String key : data.getChildren().keySet())
						{
							resetItemType(data, data.getChildren().get(key));
						}
					}
				}
            	
//            }
//			else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && _target.getParent() instanceof ComplexSemanticTemplate)
//			{
//				return true;
//			}
//			else if (data instanceof AspectConfigTemplate  && _target instanceof AspectConfigTemplate)
//			{
//				return true;
//			}
		}
			break;
		case 3: // on _targetget
		{
//			if (data instanceof ConfigItem && !(_target instanceof ComplexSemanticTemplate || _target instanceof SemanticTemplate || _target instanceof AspectConfigTemplate))
//            {
//				ConfigItem dataItem = (ConfigItem) data;
				if (_target.equals(data) || (_target instanceof ConfigItem && !_target.isMyHaveChildren()))
				{
					return false;
				}
				_target.getChildren().put(data.getValue(), data);
				data.getParent().getChildren().remove(data.getValue());
				data.setParent(_target);
				resetItemType(_target, data);
				if (data.getChildren() != null)
				{
					for (String key : data.getChildren().keySet())
					{
						resetItemType(data, data.getChildren().get(key));
					}
				}
				data.setPriority(0);
            	
//            }
//			else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && _target instanceof ComplexSemanticTemplate)
//			{
//				return true;
//			}
//			else if (data instanceof AspectConfigTemplate  && _target instanceof SemanticTemplate)
//			{
//				return true;
//			}
		}	
			break;
		case 4: // into _targetget
		{
				if (_target.equals(data) || (_target instanceof ConfigItem && !_target.isMyHaveChildren()))
			{
				return false;
			}
			_target.getChildren().put(data.getValue(), data);
			data.getParent().getChildren().remove(data.getValue());
			data.setParent(_target);
			resetItemType(_target, data);
			if (data.getChildren() != null)
			{
				for (String key : data.getChildren().keySet())
				{
					resetItemType(data, data.getChildren().get(key));
				}
			}
			data.setPriority(0);
		}	
			break;
		default:
			break;
		}
        
        
        
        
        
        

		if (_target instanceof ConfigItem)
		{
			_target.getParent().processPositionSettings();

		}
		else
		{
			_target.processPositionSettings();
		}

		Object[] objects = _viewer.getExpandedElements();
		Object input = _viewer.getInput();
		_viewer.setInput(input);
		for (Object o : objects)
		{
			_viewer.setExpandedState(o, true);
		}
		_viewer.setSelection(_viewer.getSelection());
		return false;
	}

	/** ajusts the type of the droped item to its new type if it has been put up to another
	 * level in the tree.
	 * @param c new mother item
	 * @param ci moved item
	 */
	private void resetItemType(final ConfigData parent, final ConfigData child)
	{
		if (parent.getValue().equals("aodl:semanticStm"))
		{
			child.setPos(("_TEXTNODE"));
			child.setMyHaveChildren(false);
			child.getChildren().clear();
		}
		// else if (parent.getValue().equals("aodl:date"))
//			{
		// child.setPos(("type"));
		// child.setMyHaveChildren(false);
		// child.getChildren().clear();
//			}
		else if (parent.getValue().equals("aodl:relation"))
			{
			child.setPos(("context"));
			child.setMyHaveChildren(true);
			}
		else if (parent.getValue().startsWith("aodl"))
			{
			child.setPos(("type"));
			child.setMyHaveChildren(true);
			}
		else if (parent.getPos() != null && parent.getPos().equals("type"))
			{
			child.setPos(("subtype"));
			}
		else if (parent.getPos() != null && parent.getPos().equals("subtype"))
			{
			child.setPos(("role"));
			child.setMyHaveChildren(false);
			child.getChildren().clear();

			}
		else if (parent.getPos() != null && parent.getPos().equals("context"))
			{
			child.setPos(("class"));
			child.setMyHaveChildren(true);
			}
		else if (parent.getPos() != null && parent.getPos().equals("class"))
			{
			child.setPos(("_TEXTNODE"));
			child.setMyHaveChildren(false);
			child.getChildren().clear();


			}
		else if (parent.getPos() != null && parent.getPos().equals("_TEXTNODE"))
			{
			child.setPos(("_TEXTNODE"));
			child.setMyHaveChildren(false);
			child.getChildren().clear();


			}
	}

	@Override
	public final boolean validateDrop(final Object target, final int operation,
			final TransferData transferType)
	{
		_location = getCurrentLocation();
		
		IStructuredSelection selection = (IStructuredSelection) _viewer.getSelection();
        Object obj =  selection.getFirstElement();
        ConfigTreeNode tn = (ConfigTreeNode) obj;
        ConfigData data = tn.getConfigData();
        ConfigTreeNode tnTarget = (ConfigTreeNode) target;
		if (tnTarget == null)
		{
			return false;
		}
        ConfigData tar = (ConfigData) tnTarget.getConfigData();
        if (data.getParent() != null && data instanceof ConfigData && data.getParent().equals(tar.getParent())) //same parent
        {
        	switch (_location) {
			case 1: // before target
			{
				return true;	           
			}
			case 2: // after target
			{
				return true;
			}
			case 3: // on target
			{
				return tar.isMyHaveChildren();
			}	
			case 4: // into target
			{
				return tar.isMyHaveChildren();
			}	
			default:
				break;
			}
        }
        else if (data.getParent() != null && data instanceof ConfigData && data.getParent().equals(tar)) //same parent
        {
        	switch (_location) {
			case 1: // before target
			{
				return true;	           
			}
			case 2: // after target
			{
				return true;
			}
			case 3: // on target
			{
				return false;
			}	
			case 4: // into target
			{
				return false;
			}	
			default:
				break;
			}
        }
        else
        {
        	switch (_location) {
			case 1: // before target
			{
				if (data instanceof ConfigItem && !(tar instanceof ComplexSemanticTemplate || tar instanceof SemanticTemplate || tar instanceof AspectConfigTemplate))
	            {
					if ((data.getParent().getValue().equals("aodl:relation")
							|| (data.getParent().getParent() != null && data.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (data.getParent().getParent() != null && data.getParent().getParent().getParent() != null && data
									.getParent().getParent().getParent().getValue().equals("aodl:relation")))
							&& tar.getParent() != null && (tar.getParent().getValue() != null && tar.getParent().getValue().equals("aodl:relation")
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getParent() != null && tar
									.getParent().getParent().getParent().getValue().equals("aodl:relation"))))
					{
						return true;
					}
					else if (!data.getParent().getValue().equals("aodl:semanticStm")
							&& tar.getParent() != null && tar.getParent().getValue() != null && !tar.getParent().getValue().equals("aodl:semanticStm"))
	            	{
	            		return true;
	            	}
	            	
	            	
	            }
				else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && tar.getParent() instanceof ComplexSemanticTemplate)
				{
					return true;
				}
				else if (data instanceof AspectConfigTemplate  && tar instanceof AspectConfigTemplate)
				{
					return true;
				}
			}
				break;
			case 2: // after target
			{
				if (data instanceof ConfigItem && !(tar instanceof ComplexSemanticTemplate || tar instanceof SemanticTemplate || tar instanceof AspectConfigTemplate))
	            {
					if ((data.getParent().getValue().equals("aodl:relation")
							|| (data.getParent().getParent() != null && data.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (data.getParent().getParent() != null && data.getParent().getParent().getParent() != null && data
									.getParent().getParent().getParent().getValue().equals("aodl:relation")))
							&& tar.getParent() != null && (tar.getParent().getValue() != null && tar.getParent().getValue().equals("aodl:relation")
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getParent() != null && tar
									.getParent().getParent().getParent().getValue().equals("aodl:relation"))))
					{
						return true;
					}
					else if (!data.getParent().getValue().equals("aodl:semanticStm")
							&& tar.getParent() != null && tar.getParent().getValue() != null && !tar.getParent().getValue().equals("aodl:semanticStm"))
	            	{
	            		return true;
	            	}
	            	
	            }
				else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && tar.getParent() instanceof ComplexSemanticTemplate)
				{
					return true;
				}
				else if (data instanceof AspectConfigTemplate  && tar instanceof AspectConfigTemplate)
				{
					return true;
				}
			}
				break;
			case 3: // on target
			{
				if (data instanceof ConfigItem && !(tar instanceof ComplexSemanticTemplate || tar instanceof SemanticTemplate || tar instanceof AspectConfigTemplate))
	            {
					if ((data.getParent().getValue().equals("aodl:relation")
							|| (data.getParent().getParent() != null && data.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (data.getParent().getParent() != null && data.getParent().getParent().getParent() != null && data
									.getParent().getParent().getParent().getValue().equals("aodl:relation")))
							&& tar.getParent() != null && (tar.getParent().getValue() != null && tar.getParent().getValue().equals("aodl:relation")
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getParent() != null && tar
									.getParent().getParent().getParent().getValue().equals("aodl:relation"))))
					{
						return tar.isMyHaveChildren();
					}
					else if (!data.getParent().getValue().equals("aodl:semanticStm")
	            			&& tar.getParent() != null && tar.getParent().getValue() != null && !tar.getParent().getValue().equals("aodl:semanticStm"))
	            	{
						return tar.isMyHaveChildren();
	            	}
	            	
	            }
				else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && tar instanceof ComplexSemanticTemplate)
				{
					return true;
				}
				else if (data instanceof AspectConfigTemplate  && tar instanceof SemanticTemplate)
				{
					return true;
				}
			}	
				break;
			case 4: // into target
			{
				if (data instanceof ConfigItem && !(tar instanceof ComplexSemanticTemplate || tar instanceof SemanticTemplate || tar instanceof AspectConfigTemplate))
	            {
					if ((data.getParent().getValue().equals("aodl:relation")
							|| (data.getParent().getParent() != null && data.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (data.getParent().getParent() != null && data.getParent().getParent().getParent() != null && data
									.getParent().getParent().getParent().getValue().equals("aodl:relation")))
							&& tar.getParent() != null && (tar.getParent().getValue() != null && tar.getParent().getValue().equals("aodl:relation")
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getValue()
									.equals("aodl:relation"))
							|| (tar.getParent().getParent() != null && tar.getParent().getParent().getParent() != null && tar
									.getParent().getParent().getParent().getValue().equals("aodl:relation"))))
					{
						return tar.isMyHaveChildren();
					}
					else if (!data.getParent().getValue().equals("aodl:semanticStm")
							&& tar.getParent() != null && tar.getParent().getValue() != null && !tar.getParent().getValue().equals("aodl:semanticStm"))
	            	{
						return tar.isMyHaveChildren();
	            	}
	            }
				else if (data instanceof SemanticTemplate && data.getParent() instanceof ComplexSemanticTemplate && tar instanceof ComplexSemanticTemplate)
				{
					return true;
				}
				else if (data instanceof AspectConfigTemplate  && tar instanceof SemanticTemplate)
				{
					return true;
				}
			}	
				break;
			default:
				break;
			}
        }
        
        
		return false;
	}

}

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

import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

 /** config transer for drag-n-drop support in config editor.
  * hardly implemented!
 * @author Christoph Plutte
 *
 */
public final class ConfigTransfer extends ByteArrayTransfer
 {
	/** type.*/
	private static final String MYTYPENAME = "my_type_name";
	/** type int.*/
	private static final int MYTYPEID = registerType(MYTYPENAME);
	/** instance of config transfer.*/
	private static ConfigTransfer instance = new ConfigTransfer();

 /** get the instance of config Transfer.
 * @return instance of config transfer
 */
public static ConfigTransfer getInstance()
 {
 	return instance;
 }

 /**
 * private constructor.
 */
private ConfigTransfer()
 {
 }
 /** get type ids.
 * @return type ids.
 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
 */
@Override
protected int[] getTypeIds()
 {
 	return new int[] {MYTYPEID};
 }
 /** get type names.
 * @return type names.
 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
 */
@Override
protected String[] getTypeNames()
 {
 	return new String[]{MYTYPENAME};
 }
 /** java to Native.
 * @param object obj
 * @param transferData transfer data.
 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
 */
@Override
public void javaToNative(final Object object, final TransferData transferData)
 {
 	if (object == null || !(object instanceof ConfigTreeNode[]))
	{
		return;
	}
// 	if (isSupportedType(transferData)) {
// 		TreeNode[] myTypes = (TreeNode[]) object;
//
// 	}
 }
 /** native to Java.
 * @param transferData transferData
 * @return null
 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
 */
@Override
public Object nativeToJava(final TransferData transferData)
 {

 	if (isSupportedType(transferData))
 	{
  		byte[] buffer = (byte[])super.nativeToJava(transferData);
 		if (buffer == null)
		{
			return null;
		}
 		ConfigTreeNode[] myData = new ConfigTreeNode[0];
 		return myData;
 	}

 	return null;
 }
 }

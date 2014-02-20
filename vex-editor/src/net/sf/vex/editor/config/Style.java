/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package net.sf.vex.editor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.vex.css.StyleSheet;
import net.sf.vex.layout.BoxFactory;

/**
 * Represents the combination of a style sheet and a box factory that defines
 * the styling of an XML document during editing.
 */
public class Style extends ConfigItem {
    
    public static final String EXTENSION_POINT = "net.sf.vex.editor.styles"; //$NON-NLS-1$
    
    public Style(ConfigSource config) {
        super(config);
    }
    
    /**
     * Adds the public ID of a document type to which the style applies. 
     * @param publicId public ID of the document type
     */
    public void addDocumentType(String publicId) {
        this.publicIds.add(publicId);
    }
    
    /**
     * Returns true if this style applies to the documents with the given type.
     * @param publicId public ID of the document type being sought
     */
    public boolean appliesTo(String publicId) {
        return this.publicIds.contains(publicId);
    }
    
    /**
     * Returns the box factory used to generate boxes for document elements.
     */
    public BoxFactory getBoxFactory() {
        return boxFactory;
    }

    /**
     * Returns a set of public IDs of all document types supported by
     * this style.
     */
    public Set getDocumentTypes() {
        return Collections.unmodifiableSet(this.publicIds);
    }

    /**
     * Returns an array of all styles applicable to the given public Id.
     * @param publicId Public ID for which to find styles.
     */
    public static Style[] getStylesForDoctype(String publicId) {
        ConfigRegistry registry = ConfigRegistry.getInstance();
        List styles = new ArrayList();
        List allStyles = registry.getAllConfigItems(Style.EXTENSION_POINT);
        for (Iterator it = allStyles.iterator(); it.hasNext(); ) {
            Style style = (Style) it.next();
            if (style.appliesTo(publicId)) {
                styles.add(style);
            }
        }
        return (Style[]) styles.toArray(new Style[styles.size()]);
    }
    
    /**
     * Returns the style sheet from which element styles are taken.
     */
    public StyleSheet getStyleSheet() {

		// XXX hardcorded, cp
		StyleSheet ss = null;
		// try
		// {
		// ss = new
		// StyleSheetReader().read("file:C:/IT/workspace_ae-2.1/vex-xhtml/1.0/xhtml1-plain.css");
		// }
		// catch (CSSException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return ss;
		return (StyleSheet) this.getConfig().getParsedResource(this.getResourcePath());
    }

    public String getExtensionPointId() {
        return EXTENSION_POINT;
    }
    
    /**
     * Disassociates this style from all document types.
     */
    public void removeAllDocumentTypes() {
        this.publicIds.clear();
    }
    
    /**
     * Removes the public ID of a document type to which the style 
     * no longer applies. 
     * @param publicId public ID of the document type
     */
    public void removeDocumentType(String publicId) {
        this.publicIds.remove(publicId);
    }
    
    /**
     * Sets the box factory used to generate boxes for document elements.
     * @param factory the new box factory.
     */
    public void setBoxFactory(BoxFactory factory) {
        boxFactory = factory;
    }

    //===================================================== PRIVATE
    

    private BoxFactory boxFactory;
    private Set publicIds = new HashSet();
    
}

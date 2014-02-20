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

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.vex.css.StyleSheetReader;

import org.w3c.css.sac.CSSParseException;

/**
 * Factory for style objects.
 */
public class StyleFactory implements IConfigItemFactory {

    /**
     * Returns all styles for a particular doctype.
     * @param publicId Public ID of the desired doctype.
     * @return List of Style objects.
     */
    public static List getStylesForDoctype(String publicId) {
//        List result = new ArrayList();
//        Iterator it = this.getAll().iterator();
//        while (it.hasNext()) {
//            Style style = (Style) it.next();
//            if (style.getDocumentTypes().contains(publicId)) {
//                result.add(style);
//            }
//        }
//            
//        return result;
        return null;
    }
    
    public IConfigElement[] createConfigurationElements(ConfigItem item) {
        Style style = (Style) item;
        ConfigurationElement element = new ConfigurationElement("style"); //$NON-NLS-1$
        element.setAttribute("css", style.getResourcePath()); //$NON-NLS-1$
        Set doctypes = style.getDocumentTypes();
        for (Iterator it = doctypes.iterator(); it.hasNext();) {
            String publicId = (String) it.next();
            ConfigurationElement child = new ConfigurationElement("doctypeRef"); //$NON-NLS-1$
            child.setAttribute("publicId", publicId); //$NON-NLS-1$
            element.addChild(child);
        }
        return new IConfigElement[] { element };
    }



    public ConfigItem createItem(ConfigSource config, IConfigElement[] configElements) throws IOException {
        
        if (configElements.length < 1) {
            return null;
        }
        IConfigElement configElement = configElements[0];
        
        Style style = new Style(config);
        style.setResourcePath(configElement.getAttribute("css")); //$NON-NLS-1$
        
        IConfigElement[] doctypeRefs = configElement.getChildren();
        
        for (int j = 0; j < doctypeRefs.length; j++) {
            style.addDocumentType(doctypeRefs[j].getAttribute("publicId")); //$NON-NLS-1$
        }
        
        return style;
    }

    
    public String getExtensionPointId() {
        return Style.EXTENSION_POINT;
    }
    

    public String[] getFileExtensions() {
        return EXTS;
    }

    
    public String getPluralName() {
        return Messages.getString("StyleFactory.pluralName"); //$NON-NLS-1$
    }

    public Object parseResource(URL baseUrl, String resourcePath, IBuildProblemHandler problemHandler) throws IOException {
        try {
            return new StyleSheetReader().read(new URL(baseUrl, resourcePath));
        } catch (CSSParseException ex) {
            if (problemHandler != null) {
                BuildProblem problem = new BuildProblem();
                problem.setSeverity(BuildProblem.SEVERITY_ERROR);
                problem.setResourcePath(ex.getURI());
                problem.setMessage(ex.getMessage());
                problem.setLineNumber(ex.getLineNumber());
                problemHandler.foundProblem(problem);
            }
            throw ex;
            
        }
    }

    //=================================================== PRIVATE
    
    private static final String[] EXTS = new String[] { "css" }; //$NON-NLS-1$

}

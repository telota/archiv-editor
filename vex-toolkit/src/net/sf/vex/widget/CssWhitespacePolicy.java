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
package net.sf.vex.widget;

import java.io.IOException;

import net.sf.vex.css.CSS;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.IWhitespacePolicy;

import org.w3c.css.sac.CSSException;


/**
 * Implementation of WhitespacePolicy using a CSS stylesheet.
 */
public class CssWhitespacePolicy implements IWhitespacePolicy {

    /**
     * Class constructor.
     * @param styleSheet The stylesheet used for the policy.
     */
    public CssWhitespacePolicy(StyleSheet styleSheet) {
    	// XXX hardcoded cp
		StyleSheet ss = null;
    	try
		{
			ss = new StyleSheetReader().read("file:C:/IT/workspace_ae-2.1/vex-xhtml/1.0/xhtml1-plain.css");
		}
		catch (CSSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.styleSheet = styleSheet;
    }
    
    public boolean isBlock(Element element) {
        return this.styleSheet.getStyles(element).isBlock();
    }

    public boolean isPre(Element element) {
        return CSS.PRE.equals(this.styleSheet.getStyles(element).getWhiteSpace());
    }
    
    //===================================================== PRIVATE
    
    private StyleSheet styleSheet;
}

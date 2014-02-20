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
package test.net.sf.vex.layout;

import net.sf.vex.dom.Element;
import net.sf.vex.layout.BlockElementBox;
import net.sf.vex.layout.Box;
import net.sf.vex.layout.CssBoxFactory;
import net.sf.vex.layout.LayoutContext;
import net.sf.vex.layout.SpaceBox;


/**
 * A box factory that, for an element named &lt;space&gt;, returns a SpaceBox
 * with height and width given by attributes of those names, e.g.
 * &lt;space height="100" width="200"/&gt;
 */
public class TestBoxFactory extends CssBoxFactory {
    
    public Box createBox(LayoutContext context, Element element,
            BlockElementBox parent, int width) {
        
        if (element.getName().equals("space")) {
            int w = 0;
            int h = 0;
            try {
                w = Integer.parseInt(element.getAttribute("width"));
            } catch (NumberFormatException ex) {
            }
            try {
                h = Integer.parseInt(element.getAttribute("height"));
            } catch (NumberFormatException ex) {
            }
            return new SpaceBox(w, h);
        }
        // TODO Auto-generated method stub
        return super.createBox(context, element, parent, width);
    }
}

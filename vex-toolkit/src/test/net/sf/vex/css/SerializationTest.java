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
package test.net.sf.vex.css;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import junit.framework.TestCase;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;

public class SerializationTest extends TestCase {

    public void testSerialization() throws Exception {
//        serialize("test1.css");
//        serialize("test2.css");
        serialize("testLexicalUnits.css");
//        serialize("testRules.css");
    }
    
    private void serialize(String resource) throws Exception {
        
        URL url = this.getClass().getResource(resource);
        StyleSheetReader reader = new StyleSheetReader();
        StyleSheet ss = reader.read(url);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(ss);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
    }
}

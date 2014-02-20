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
package test.net.sf.vex.eclipse;

import java.util.Collection;

import junit.framework.TestCase;
import net.sf.vex.editor.Association;

public class AssociationTest extends TestCase {

    public void testAddRemove() throws Exception {
        
        Association assoc = new Association();
        
        Collection c;
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        assoc.add("l0", "r0");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("r0", c.toArray()[0]);
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertEquals("l0", c.toArray()[0]);
        
        assoc.add("l0", "r1");
        assoc.add("l1", "r0");
        assoc.add("l1", "r1");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        
        assoc.remove("l0", "r0");

        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("r1"));
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        
        assoc.remove("l0", "r1");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        assoc.remove("l1", "r0");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        assoc.remove("l1", "r1");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(0, c.size());
    }
    
    public void testRemoveLeft() throws Exception {

        Association assoc = new Association();
        
        Collection c;
        
        assoc.add("l0", "r0");
        assoc.add("l0", "r1");
        assoc.add("l1", "r0");
        assoc.add("l1", "r1");

        
        // These should be no-ops
        assoc.removeLeft("r0");
        assoc.removeLeft("r1");
        assoc.removeRight("l0");
        assoc.removeRight("l1");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        

        assoc.removeLeft("l0");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("r0"));
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("l1"));
        
        
        assoc.removeLeft("l1");

        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(0, c.size());
    }


    public void testRemoveRight() throws Exception {

        Association assoc = new Association();
        
        Collection c;
        
        assoc.add("l0", "r0");
        assoc.add("l0", "r1");
        assoc.add("l1", "r0");
        assoc.add("l1", "r1");

        assoc.removeRight("r0");
        
        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("r1"));
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(1, c.size());
        assertTrue(c.contains("r1"));
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(2, c.size());
        assertTrue(c.contains("l0"));
        assertTrue(c.contains("l1"));
        
        
        assoc.removeRight("r1");

        c = assoc.getRightsForLeft("l0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getRightsForLeft("l1");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r0");
        assertNotNull(c);
        assertEquals(0, c.size());
        
        c = assoc.getLeftsForRight("r1");
        assertNotNull(c);
        assertEquals(0, c.size());
    }
}

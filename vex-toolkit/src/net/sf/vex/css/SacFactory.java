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
package net.sf.vex.css;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.PositionalCondition;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * Factory for producing serializable Conditions, LexicalUnits, and 
 * Selectors. The SAC parser creates instances of these that may not be
 * serializable (and in fact, those from Flute aren't). To serialize
 * StyleSheets, which contain references to these SAC objects, we must
 * clone the objects into our own serializable ones.
 */
public class SacFactory {

    /**
     * Clone the given condition, returning one that is serializable.
     * @param condition Condition to clone.
     */
    public Condition cloneCondition(Condition condition) {
        Condition clone = null;
        if (condition == null) {
            return null;
        } else if (condition instanceof AttributeCondition) {
            clone = new AttributeConditionImpl((AttributeCondition) condition);
        } else if (condition instanceof CombinatorCondition) {
            clone = new CombinatorConditionImpl((CombinatorCondition) condition, this);
        } else if (condition instanceof ContentCondition) {
            clone = new ContentConditionImpl((ContentCondition) condition);
        } else if (condition instanceof LangCondition) {
            clone = new LangConditionImpl((LangCondition) condition);
        } else if (condition instanceof NegativeCondition) {
            clone = new NegativeConditionImpl((NegativeCondition) condition, this);
        } else if (condition instanceof PositionalCondition) {
            clone = new PositionalConditionImpl((PositionalCondition) condition);
        } else {
            throw new IllegalArgumentException("Unsupported condition type: " + condition.getClass());
        }
        return clone;
    }

    /**
     * Clone the given lexical unit, returning one that is serializable.
     * @param lu LexicalUnit to clone.
     */
    public LexicalUnit cloneLexicalUnit(LexicalUnit lu) {
        if (lu == null) {
            return null;
        } else if (this.cache.containsKey(lu)) {
            return (LexicalUnit) this.cache.get(lu);
        } else {
            return new LexicalUnitImpl(lu, this);
        }
    }
    
    /**
     * Clone the given selector, returning one that is serializable.
     */
    public Selector cloneSelector(Selector selector) {
        Selector clone = null;
        if (selector == null) {
            return null;
        } else if (selector instanceof CharacterDataSelector) {
            clone = new CharacterDataSelectorImpl((CharacterDataSelector) selector);
        } else if (selector instanceof ConditionalSelector) {
            clone = new ConditionalSelectorImpl((ConditionalSelector) selector, this);
        } else if (selector instanceof DescendantSelector) {
            clone = new DescendantSelectorImpl((DescendantSelector) selector, this);
        } else if (selector instanceof ElementSelector) {
            clone = new ElementSelectorImpl((ElementSelector) selector);
        } else if (selector instanceof NegativeSelector) {
            clone = new NegativeSelectorImpl((NegativeSelector) selector, this);
        } else if (selector instanceof ProcessingInstructionSelector) {
            clone = new ProcessingInstructionSelectorImpl((ProcessingInstructionSelector) selector);
        } else if (selector instanceof SiblingSelector) {
            clone = new SiblingSelectorImpl((SiblingSelector) selector, this);
        } else {
            throw new IllegalArgumentException("Unsupported selector type: " + selector.getClass());
        }
        return clone;
    }

    //===================================================== PRIVATE
    
    private Map cache = new HashMap();
    
    private static class ConditionImpl implements Condition, Serializable {
        public ConditionImpl(Condition condition) {
            this.type = condition.getConditionType();
        }
        public short getConditionType() {
            return this.type;
        }
        private short type;
    }
    
    private static class AttributeConditionImpl extends ConditionImpl
    implements AttributeCondition {
        public AttributeConditionImpl(AttributeCondition condition) {
            super(condition);
            this.namespaceURI = condition.getNamespaceURI();
            this.localName = condition.getLocalName();
            this.specified = condition.getSpecified();
            this.value = condition.getValue();
        }
        public String getNamespaceURI() {
            return this.namespaceURI;
        }
        public String getLocalName() {
            return this.localName;
        }
        public boolean getSpecified() {
            return this.specified;
        }
        public String getValue() {
            return this.value;
        }
        private String namespaceURI;
        private String localName;
        private boolean specified;
        private String value;
    }
    
    private static class CombinatorConditionImpl extends ConditionImpl
    implements CombinatorCondition {
        public CombinatorConditionImpl(CombinatorCondition condition, SacFactory factory) {
            super(condition);
            this.firstCondition = factory.cloneCondition(condition.getFirstCondition());
            this.secondCondition = factory.cloneCondition(condition.getSecondCondition());
        }
        public Condition getFirstCondition() {
            return this.firstCondition;
        }
        public Condition getSecondCondition() {
            return this.secondCondition;
        }
        private Condition firstCondition;
        private Condition secondCondition;
    }
    
    private static class ContentConditionImpl extends ConditionImpl
    implements ContentCondition {
        public ContentConditionImpl(ContentCondition condition) {
            super(condition);
            this.data = condition.getData();
        }
        public String getData() {
            return this.data;
        }
        private String data;
    }
    
    private static class LangConditionImpl extends ConditionImpl
    implements LangCondition {
        public LangConditionImpl(LangCondition condition) {
            super(condition);
            this.lang = condition.getLang();
        }
        public String getLang() {
            return this.lang;
        }
        private String lang;
    }
    
    private static class NegativeConditionImpl extends ConditionImpl
    implements NegativeCondition {
        public NegativeConditionImpl(NegativeCondition condition, SacFactory factory) {
            super(condition);
            this.condition = factory.cloneCondition(condition.getCondition());
        }
        public Condition getCondition() {
            return this.condition;
        }
        private Condition condition;
    }
    
    private static class PositionalConditionImpl extends ConditionImpl
    implements PositionalCondition {
        public PositionalConditionImpl(PositionalCondition condition) {
            super(condition);
            this.position = condition.getPosition();
            this.typeNode = condition.getTypeNode();
            this.type = condition.getType();
        }
        public int getPosition() {
            return this.position;
        }
        public boolean getTypeNode() {
            return this.typeNode;
        }
        public boolean getType() {
            return this.type;
        }
        private int position;
        private boolean typeNode;
        private boolean type;
    }
    
    
    private static class LexicalUnitImpl implements LexicalUnit, Serializable {

        public LexicalUnitImpl(LexicalUnit lu, SacFactory factory) {
            factory.cache.put(lu, this);
            this.type = lu.getLexicalUnitType();
            this.s = lu.getStringValue();
            this.i = lu.getIntegerValue();
            this.f = lu.getFloatValue();
            this.functionName = lu.getFunctionName();
            this.next = factory.cloneLexicalUnit(lu.getNextLexicalUnit());
            this.prev = factory.cloneLexicalUnit(lu.getPreviousLexicalUnit());
            this.parameters = factory.cloneLexicalUnit(lu.getParameters());
            if (this.type == SAC_PERCENTAGE
                    || this.type == SAC_EM
                    || this.type == SAC_EX
                    || this.type == SAC_PIXEL
                    || this.type == SAC_CENTIMETER
                    || this.type == SAC_MILLIMETER
                    || this.type == SAC_INCH
                    || this.type == SAC_POINT
                    || this.type == SAC_PICA
                    || this.type == SAC_DEGREE
                    || this.type == SAC_RADIAN
                    || this.type == SAC_GRADIAN
                    || this.type == SAC_MILLISECOND
                    || this.type == SAC_SECOND
                    || this.type == SAC_HERTZ
                    || this.type == SAC_KILOHERTZ
                    || this.type == SAC_DIMENSION) {
                this.dimensionText = lu.getDimensionUnitText();
            }
            this.stringRepresentation = lu.toString();
        }

        public short getLexicalUnitType() {
            return this.type;
        }

        public LexicalUnit getNextLexicalUnit() {
            return this.next;
        }

        public LexicalUnit getPreviousLexicalUnit() {
            return this.prev;
        }

        public int getIntegerValue() {
            return this.i;
        }

        public float getFloatValue() {
            return this.f;
        }

        public String getDimensionUnitText() {
            return this.dimensionText;
        }

        public String getFunctionName() {
            return this.functionName;
        }

        public LexicalUnit getParameters() {
            return this.parameters;
        }

        public String getStringValue() {
            return this.s;
        }

        public String getStringRepresentation() {
            return this.stringRepresentation;
        }
        
        public LexicalUnit getSubValues() {
            return this.parameters;
        }

        //================================================= PRIVATE
        
        private LexicalUnitImpl() {
        }

        private short type;
        private LexicalUnit next;
        private LexicalUnit prev;
        private LexicalUnit parameters;
        private String s;
        private int i;
        private float f;
        private String functionName;
        private String dimensionText;
        private String stringRepresentation;
    }

    private static class SelectorImpl implements Selector, Serializable {
        public SelectorImpl(Selector selector) {
            this.type = selector.getSelectorType();
        }
        public short getSelectorType() {
            return this.type;
        }
        private short type;
    }
    
    private static class CharacterDataSelectorImpl extends SelectorImpl
    implements CharacterDataSelector {
        public CharacterDataSelectorImpl(CharacterDataSelector selector) {
            super(selector);
            this.data = selector.getData();
        }
        public String getData() {
            return this.data;
        }
        private String data;
    }
    
    private static class ConditionalSelectorImpl extends SelectorImpl 
    implements ConditionalSelector {
        public ConditionalSelectorImpl(ConditionalSelector selector, SacFactory factory) {
            super(selector);
            this.condition = factory.cloneCondition(selector.getCondition());
            this.simpleSelector = 
                (SimpleSelector) factory.cloneSelector(selector.getSimpleSelector());
        }
        public SimpleSelector getSimpleSelector() {
            return this.simpleSelector;
        }
        public Condition getCondition() {
            return this.condition;
        }
        private Condition condition;
        private SimpleSelector simpleSelector;
    }
    
    private static class DescendantSelectorImpl extends SelectorImpl
    implements DescendantSelector {
        public DescendantSelectorImpl(DescendantSelector selector, SacFactory factory) {
            super(selector);
            this.ancestor = factory.cloneSelector(selector.getAncestorSelector());
            this.simpleSelector = 
                (SimpleSelector) factory.cloneSelector(selector.getSimpleSelector());
        }
        public Selector getAncestorSelector() {
            return this.ancestor;
        }
        public SimpleSelector getSimpleSelector() {
            return this.simpleSelector;
        }
        private Selector ancestor;
        private SimpleSelector simpleSelector;
    }
    
    private static class ElementSelectorImpl extends SelectorImpl
    implements ElementSelector {
        public ElementSelectorImpl(ElementSelector selector) {
            super(selector);
            this.namespaceURI = selector.getNamespaceURI();
            this.localName = selector.getLocalName();
        }
        public String getNamespaceURI() {
            return this.namespaceURI;
        }
        public String getLocalName() {
            return this.localName;
        }
        private String namespaceURI;
        private String localName;
    }

    private static class NegativeSelectorImpl extends SelectorImpl
    implements NegativeSelector {
        public NegativeSelectorImpl(NegativeSelector selector, SacFactory factory) {
            super(selector);
            this.simpleSelector = (SimpleSelector) factory.cloneSelector(selector.getSimpleSelector());
        }
        public SimpleSelector getSimpleSelector() {
            return this.simpleSelector;
        }
        private SimpleSelector simpleSelector;
    }
    
    private static class ProcessingInstructionSelectorImpl extends SelectorImpl
    implements ProcessingInstructionSelector {
        public ProcessingInstructionSelectorImpl(ProcessingInstructionSelector selector) {
            super(selector);
            this.target = selector.getTarget();
            this.data = selector.getData();
        }
        public String getTarget() {
            return this.target;
        }
        public String getData() {
            return this.data;
        }
        private String target;
        private String data;
    }
    
    private static class SiblingSelectorImpl extends SelectorImpl
    implements SiblingSelector {
        private SiblingSelectorImpl(SiblingSelector selector, SacFactory factory) {
            super(selector);
            this.nodeType = selector.getNodeType();
            this.selector = factory.cloneSelector(selector.getSelector());
            this.siblingSelector = (SimpleSelector) factory.cloneSelector(selector.getSiblingSelector());
        }
        public short getNodeType() {
            return this.nodeType;
        }
        public Selector getSelector() {
            return this.selector;
        }
        public SimpleSelector getSiblingSelector() {
            return this.siblingSelector;
        }
        private short nodeType;
        private Selector selector;
        private SimpleSelector siblingSelector;
    }
}

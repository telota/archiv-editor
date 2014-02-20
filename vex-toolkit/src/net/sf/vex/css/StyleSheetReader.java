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

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

/**
 * Driver for the creation of StyleSheet objects.
 */
public class StyleSheetReader {

    /**
     * Class constructor.
     */
    public StyleSheetReader() {
        
    }
    
    protected static Parser createParser() {
        //return new org.apache.batik.css.parser.Parser();
        return new org.w3c.flute.parser.Parser();
    }
    
    /**
     * Creates a new StyleSheet object from a URL.
     * @param url URL from which to read the style sheet.
     */
    public StyleSheet read(URL url) throws IOException {
        return this.read(new InputSource(url.toString()), url);
    }

    /**
     * Creates a style sheet from a string. This is mainly used for small
     * style sheets within unit tests.
     * @param s String containing the style sheet.
     */
    public StyleSheet read(String s) throws CSSException, IOException {
        Reader reader = new CharArrayReader(s.toCharArray());
        return this.read(new InputSource(reader), null);
    }
    
    /**
     * Creates a new Stylesheet from an input source.
     * @param inputSource InputSource from which to read the stylesheet.
     * @param url URL representing the input source, used to resolve @import
     * rules with relative URIs. May be null, in which case @import rules
     * are ignored.
     */
    public StyleSheet read(InputSource inputSource, URL url) 
            throws CSSException, IOException {
		// FIXME hardcoded cp
        Parser parser = createParser();
        List rules = new ArrayList();
		StyleSheetBuilder ssBuilder = new StyleSheetBuilder(this.source, rules, url);
        parser.setDocumentHandler(ssBuilder);
		parser.parseStyleSheet(new InputSource(
				url.toString()));
        Rule[] ruleArray = (Rule[]) rules.toArray(new Rule[rules.size()]);
        return new StyleSheet(ruleArray);
    	
    }
    
    /**
     * Returns the source of the stylesheet.
     */
    public byte getSource() {
        return source;
    }

    /**
     * Sets the source of the stylesheet. Must be one of StyleSheet.SOURCE_USER,
     * StyleSheet.SOURCE_AUTHOR, or StyleSheet.SOURCE_DEFAULT.
     * @param source The source to set.
     */
    public void setSource(byte source) {
        this.source = source;
    }

    //======================================================== PRIVATE

    private byte source = StyleSheet.SOURCE_DEFAULT;
    
    private static class StyleSheetBuilder implements DocumentHandler {

        private byte source;
        
        // The rules that will be added to the stylesheet
        private List rules;
        
        // The rules to which decls are currently being added
        private List currentRules;
        
        // URL from which @import rules relative URIs are resolved.
        // May be null!
        private URL url;
        
        // Factory for creating serializable clones of SAC objects
        SacFactory factory = new SacFactory();

        public StyleSheetBuilder(byte source, List rules, URL url) {
            this.source = source;
            this.rules = rules;
            this.url = url;
        }
        
        //-------------------------------------------- DocumentHandler methods

        public void comment(java.lang.String text) {
        }

        public void endDocument(InputSource source) {
        }

        public void endFontFace() {
        }

        public void endMedia(SACMediaList media) {
        }

        public void endPage(String name, String pseudo_page) {
        }

        public void endSelector(SelectorList selectors) {
            this.rules.addAll(this.currentRules);
            this.currentRules = null;
        }

        public void ignorableAtRule(String atRule) {
        }

        public void importStyle(
            String uri,
            SACMediaList media,
            String defaultNamespaceURI) {
            
            if (this.url == null) {
                return;
            }

            try {
                Parser parser = createParser();
                URL importUrl = new URL(this.url, uri);
                StyleSheetBuilder ssBuilder = new StyleSheetBuilder(this.source, rules, importUrl);
                parser.setDocumentHandler(ssBuilder);
                parser.parseStyleSheet(new InputSource(importUrl.toString()));
            } catch (CSSException e) {
            } catch (IOException e) {
            }
            
        }

        public void namespaceDeclaration(String prefix, String uri) {
        }

        public void property(
            String name,
            LexicalUnit value,
            boolean important) {

            // Create a serializable clone of the value for storage in our
            // stylesheet.
            LexicalUnit val = factory.cloneLexicalUnit(value);
            
            if (name.equals(CSS.BORDER)) {
                this.expandBorder(val, important);
            } else if (name.equals(CSS.BORDER_BOTTOM)) {
                this.expandBorder(val, CSS.BORDER_BOTTOM, important);
            } else if (name.equals(CSS.BORDER_LEFT)) {
                this.expandBorder(val, CSS.BORDER_LEFT, important);
            } else if (name.equals(CSS.BORDER_RIGHT)) {
                this.expandBorder(val, CSS.BORDER_RIGHT, important);
            } else if (name.equals(CSS.BORDER_TOP)) {
                this.expandBorder(val, CSS.BORDER_TOP, important);
            } else if (name.equals(CSS.BORDER_COLOR)) {
                this.expandBorderColor(val, important);
            } else if (name.equals(CSS.BORDER_STYLE)) {
                this.expandBorderStyle(val, important);
            } else if (name.equals(CSS.BORDER_WIDTH)) {
                this.expandBorderWidth(val, important);
            } else if (name.equals(CSS.FONT)) {
                this.expandFont(val, important);
            } else if (name.equals(CSS.MARGIN)) {
                this.expandMargin(val, important);
            } else if (name.equals(CSS.PADDING)) {
                this.expandPadding(val, important);
            } else {
                this.addDecl(name, val, important);
            }
        }

        public void startDocument(InputSource source) {
        }

        public void startFontFace() {
        }

        public void startMedia(SACMediaList media) {
        }

        public void startPage(String name, String pseudo_page) {
        }

        public void startSelector(SelectorList selectors) {
            this.currentRules = new ArrayList();
            for (int i = 0; i < selectors.getLength(); i++) {
                Selector selector = factory.cloneSelector(selectors.item(i));
                Rule rule = new Rule(this.source, selector);
                this.currentRules.add(rule);
            }
        }

        //----------------------------------------- DocumentHandler methods end

        //======================================================= PRIVATE

        /**
         * Adds a PropertyDecl to the current set of rules.
         */
        private void addDecl(String name, LexicalUnit value, boolean important) {
            Iterator iter = this.currentRules.iterator();
            while (iter.hasNext()) {
                Rule rule = (Rule) iter.next();
                rule.add(new PropertyDecl(rule, name, value, important));
            }
        }

        /**
         * Expand the "border" shorthand property.
         */
        private void expandBorder(LexicalUnit value, boolean important) {
            this.expandBorder(value, CSS.BORDER_BOTTOM, important);
            this.expandBorder(value, CSS.BORDER_LEFT, important);
            this.expandBorder(value, CSS.BORDER_RIGHT, important);
            this.expandBorder(value, CSS.BORDER_TOP, important);
        }

        /**
         * Expand one of the the "border-xxx" shorthand
         * properties. whichBorder must be one of CSS.BORDER_BOTTOM,
         * CSS.BORDER_LEFT, CSS.BORDER_RIGHT, CSS.BORDER_TOP.
         */
        private void expandBorder(
            LexicalUnit value,
            String whichBorder,
            boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(whichBorder + CSS.COLOR_SUFFIX, value, important);
                this.addDecl(whichBorder + CSS.STYLE_SUFFIX, value, important);
                this.addDecl(whichBorder + CSS.WIDTH_SUFFIX, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            int i = 0;
            if (BorderWidthProperty.isBorderWidth(lus[i])) {
                this.addDecl(whichBorder + CSS.WIDTH_SUFFIX, lus[i], important);
                i++;
            }

            if (i < lus.length && BorderStyleProperty.isBorderStyle(lus[i])) {
                this.addDecl(whichBorder + CSS.STYLE_SUFFIX, lus[i], important);
                i++;
            }

            if (i < lus.length && ColorProperty.isColor(lus[i])) {
                this.addDecl(whichBorder + CSS.COLOR_SUFFIX, lus[i], important);
                i++;
            }

        }

        /**
         * Expand the "border-color" shorthand property.
         */
        private void expandBorderColor(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.BORDER_TOP_COLOR, value, important);
                this.addDecl(CSS.BORDER_LEFT_COLOR, value, important);
                this.addDecl(CSS.BORDER_RIGHT_COLOR, value, important);
                this.addDecl(CSS.BORDER_BOTTOM_COLOR, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            if (lus.length >= 4) {
                this.addDecl(CSS.BORDER_TOP_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_COLOR, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_COLOR, lus[2], important);
                this.addDecl(CSS.BORDER_LEFT_COLOR, lus[3], important);
            } else if (lus.length == 3) {
                this.addDecl(CSS.BORDER_TOP_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_COLOR, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_COLOR, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_COLOR, lus[2], important);
            } else if (lus.length == 2) {
                this.addDecl(CSS.BORDER_TOP_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_COLOR, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_COLOR, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_COLOR, lus[0], important);
            } else if (lus.length == 1) {
                this.addDecl(CSS.BORDER_TOP_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_COLOR, lus[0], important);
                this.addDecl(CSS.BORDER_BOTTOM_COLOR, lus[0], important);
            }
        }

        /**
         * Expand the "border-style" shorthand property.
         */
        private void expandBorderStyle(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.BORDER_TOP_STYLE, value, important);
                this.addDecl(CSS.BORDER_LEFT_STYLE, value, important);
                this.addDecl(CSS.BORDER_RIGHT_STYLE, value, important);
                this.addDecl(CSS.BORDER_BOTTOM_STYLE, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            if (lus.length >= 4) {
                this.addDecl(CSS.BORDER_TOP_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_STYLE, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_STYLE, lus[2], important);
                this.addDecl(CSS.BORDER_LEFT_STYLE, lus[3], important);
            } else if (lus.length == 3) {
                this.addDecl(CSS.BORDER_TOP_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_STYLE, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_STYLE, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_STYLE, lus[2], important);
            } else if (lus.length == 2) {
                this.addDecl(CSS.BORDER_TOP_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_STYLE, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_STYLE, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_STYLE, lus[0], important);
            } else if (lus.length == 1) {
                this.addDecl(CSS.BORDER_TOP_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_STYLE, lus[0], important);
                this.addDecl(CSS.BORDER_BOTTOM_STYLE, lus[0], important);
            }
        }

        /**
         * Expand the "border-width" shorthand property.
         */
        private void expandBorderWidth(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.BORDER_TOP_WIDTH, value, important);
                this.addDecl(CSS.BORDER_LEFT_WIDTH, value, important);
                this.addDecl(CSS.BORDER_RIGHT_WIDTH, value, important);
                this.addDecl(CSS.BORDER_BOTTOM_WIDTH, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            if (lus.length >= 4) {
                this.addDecl(CSS.BORDER_TOP_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_WIDTH, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_WIDTH, lus[2], important);
                this.addDecl(CSS.BORDER_LEFT_WIDTH, lus[3], important);
            } else if (lus.length == 3) {
                this.addDecl(CSS.BORDER_TOP_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_WIDTH, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_WIDTH, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_WIDTH, lus[2], important);
            } else if (lus.length == 2) {
                this.addDecl(CSS.BORDER_TOP_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_WIDTH, lus[1], important);
                this.addDecl(CSS.BORDER_RIGHT_WIDTH, lus[1], important);
                this.addDecl(CSS.BORDER_BOTTOM_WIDTH, lus[0], important);
            } else if (lus.length == 1) {
                this.addDecl(CSS.BORDER_TOP_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_LEFT_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_RIGHT_WIDTH, lus[0], important);
                this.addDecl(CSS.BORDER_BOTTOM_WIDTH, lus[0], important);
            }
        }

        /**
         * Expand the "font" shorthand property.
         */
        private void expandFont(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.FONT_STYLE, value, important);
                this.addDecl(CSS.FONT_VARIANT, value, important);
                this.addDecl(CSS.FONT_WEIGHT, value, important);
                this.addDecl(CSS.FONT_SIZE, value, important);
                this.addDecl(CSS.FONT_FAMILY, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            int n = lus.length;
            int i = 0;
            if (i < n && FontStyleProperty.isFontStyle(lus[i])) {
                this.addDecl(CSS.FONT_STYLE, lus[i], important);
                i++;
            }

            if (i < n && FontVariantProperty.isFontVariant(lus[i])) {
                this.addDecl(CSS.FONT_VARIANT, lus[i], important);
                i++;
            }

            if (i < n && FontWeightProperty.isFontWeight(lus[i])) {
                this.addDecl(CSS.FONT_WEIGHT, lus[i], important);
                i++;
            }

            if (i < n && FontSizeProperty.isFontSize(lus[i])) {
                this.addDecl(CSS.FONT_SIZE, lus[i], important);
                i++;
            }

            if (i < n
                && lus[i].getLexicalUnitType()
                    == LexicalUnit.SAC_OPERATOR_SLASH) {
                i++; // gobble slash
                if (i < n) {
                    this.addDecl(CSS.LINE_HEIGHT, lus[i], important);
                }
                i++;
            }

            if (i < n) {
                this.addDecl(CSS.FONT_FAMILY, lus[i], important);
            }
        }

        /**
         * Expand the "margin" shorthand property.
         */
        private void expandMargin(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.MARGIN_TOP, value, important);
                this.addDecl(CSS.MARGIN_RIGHT, value, important);
                this.addDecl(CSS.MARGIN_BOTTOM, value, important);
                this.addDecl(CSS.MARGIN_LEFT, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            if (lus.length >= 4) {
                this.addDecl(CSS.MARGIN_TOP, lus[0], important);
                this.addDecl(CSS.MARGIN_RIGHT, lus[1], important);
                this.addDecl(CSS.MARGIN_BOTTOM, lus[2], important);
                this.addDecl(CSS.MARGIN_LEFT, lus[3], important);
            } else if (lus.length == 3) {
                this.addDecl(CSS.MARGIN_TOP, lus[0], important);
                this.addDecl(CSS.MARGIN_LEFT, lus[1], important);
                this.addDecl(CSS.MARGIN_RIGHT, lus[1], important);
                this.addDecl(CSS.MARGIN_BOTTOM, lus[2], important);
            } else if (lus.length == 2) {
                this.addDecl(CSS.MARGIN_TOP, lus[0], important);
                this.addDecl(CSS.MARGIN_LEFT, lus[1], important);
                this.addDecl(CSS.MARGIN_RIGHT, lus[1], important);
                this.addDecl(CSS.MARGIN_BOTTOM, lus[0], important);
            } else if (lus.length == 1) {
                this.addDecl(CSS.MARGIN_TOP, lus[0], important);
                this.addDecl(CSS.MARGIN_LEFT, lus[0], important);
                this.addDecl(CSS.MARGIN_RIGHT, lus[0], important);
                this.addDecl(CSS.MARGIN_BOTTOM, lus[0], important);
            }
        }

        /**
         * Expand the "padding" shorthand property.
         */
        private void expandPadding(LexicalUnit value, boolean important) {

            if (AbstractProperty.isInherit(value)) {
                this.addDecl(CSS.PADDING_TOP, value, important);
                this.addDecl(CSS.PADDING_LEFT, value, important);
                this.addDecl(CSS.PADDING_RIGHT, value, important);
                this.addDecl(CSS.PADDING_BOTTOM, value, important);
                return;
            }

            LexicalUnit[] lus = getLexicalUnitList(value);
            if (lus.length >= 4) {
                this.addDecl(CSS.PADDING_TOP, lus[0], important);
                this.addDecl(CSS.PADDING_RIGHT, lus[1], important);
                this.addDecl(CSS.PADDING_BOTTOM, lus[2], important);
                this.addDecl(CSS.PADDING_LEFT, lus[3], important);
            } else if (lus.length == 3) {
                this.addDecl(CSS.PADDING_TOP, lus[0], important);
                this.addDecl(CSS.PADDING_LEFT, lus[1], important);
                this.addDecl(CSS.PADDING_RIGHT, lus[1], important);
                this.addDecl(CSS.PADDING_BOTTOM, lus[2], important);
            } else if (lus.length == 2) {
                this.addDecl(CSS.PADDING_TOP, lus[0], important);
                this.addDecl(CSS.PADDING_LEFT, lus[1], important);
                this.addDecl(CSS.PADDING_RIGHT, lus[1], important);
                this.addDecl(CSS.PADDING_BOTTOM, lus[0], important);
            } else if (lus.length == 1) {
                this.addDecl(CSS.PADDING_TOP, lus[0], important);
                this.addDecl(CSS.PADDING_LEFT, lus[0], important);
                this.addDecl(CSS.PADDING_RIGHT, lus[0], important);
                this.addDecl(CSS.PADDING_BOTTOM, lus[0], important);
            }
        }

        /**
         * Returns an array of <code>LexicalUnit</code> objects, the first
         * of which is given.
         */
        private static LexicalUnit[] getLexicalUnitList(LexicalUnit lu) {
            List lus = new ArrayList();
            while (lu != null) {
                lus.add(lu);
                lu = lu.getNextLexicalUnit();
            }
            return (LexicalUnit[]) lus.toArray(new LexicalUnit[lus.size()]);
        }

    }
}

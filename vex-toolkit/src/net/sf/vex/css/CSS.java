/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *     Dave Holroyd - Implement text decoration
 *     John Austin - More complete CSS constants.  Add the colour "orange".
 *******************************************************************************/
package net.sf.vex.css;

/**
 * CSS constants.
 */
public interface CSS {

    // property names
    public static final String AZIMUTH = "azimuth";
    public static final String BACKGROUND = "background";
    public static final String BACKGROUND_ATTACHMENT = "background-attachment";
    public static final String BACKGROUND_COLOR = "background-color";
    public static final String BACKGROUND_IMAGE = "background-image";
    public static final String BACKGROUND_POSITION = "background-position";
    public static final String BACKGROUND_REPEAT = "background-repeat";   
    public static final String BORDER = "border";
    public static final String BORDER_BOTTOM = "border-bottom";
    public static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
    public static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
    public static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
    public static final String BORDER_COLOR = "border-color";
    public static final String BORDER_LEFT = "border-left";
    public static final String BORDER_LEFT_COLOR = "border-left-color";
    public static final String BORDER_LEFT_STYLE = "border-left-style";
    public static final String BORDER_LEFT_WIDTH = "border-left-width";
    public static final String BORDER_RIGHT = "border-right";
    public static final String BORDER_RIGHT_COLOR = "border-right-color";
    public static final String BORDER_RIGHT_STYLE = "border-right-style";
    public static final String BORDER_RIGHT_WIDTH = "border-right-width";
    public static final String BORDER_SPACING = "border-spacing";
    public static final String BORDER_STYLE = "border-style";
    public static final String BORDER_TOP = "border-top";
    public static final String BORDER_TOP_COLOR = "border-top-color";
    public static final String BORDER_TOP_STYLE = "border-top-style";
    public static final String BORDER_TOP_WIDTH = "border-top-width";
    public static final String BORDER_WIDTH = "border-width";
    public static final String BOTTOM = "bottom";   
    public static final String CAPTION_SIDE = "caption-side";
    public static final String CLEAR = "clear";
    public static final String CLIP= "clip";
    public static final String COLOR = "color";
    public static final String CONTENT = "content";
    public static final String COUNTER_INCREMENT = "counter-increment";
    public static final String COUNTER_RESET= "counter-reset";
    public static final String CUE = "cue";
    public static final String CUE_AFTER = "cue-after";
    public static final String CUE_BEFORE = "cue-before";
    public static final String CURSOR = "cursor";
    public static final String DIRECTION = "direction";
    public static final String DISPLAY = "display";
    public static final String ELEVATION = "elevation";
    public static final String EMPTY_CELLS = "empty-cells";
    public static final String FLOAT = "float";
    public static final String FONT = "font";
    public static final String FONT_FAMILY = "font-family";
    public static final String FONT_SIZE = "font-size";
    public static final String FONT_STYLE = "font-style";
    public static final String FONT_VARIANT = "font-variant";
    public static final String FONT_WEIGHT = "font-weight";
    public static final String HEIGHT = "height";
    public static final String HIDDEN = "hidden";
    public static final String LETTER_SPACING = "letter-spacing";
    public static final String LINE_HEIGHT = "line-height";
    public static final String LIST_STYLE = "list-style";
    public static final String LIST_STYLE_IMAGE = "list-style-image";
    public static final String LIST_STYLE_POSITION = "list-style-position";
    public static final String LIST_STYLE_TYPE = "list-style-type";
    public static final String MARGIN = "margin";
    public static final String MARGIN_BOTTOM = "margin-bottom";
    public static final String MARGIN_LEFT = "margin-left";
    public static final String MARGIN_RIGHT = "margin-right";
    public static final String MARGIN_TOP = "margin-top";
    public static final String MAX_HEIGHT = "max-height";
    public static final String MAX_WIDTH = "max-width";
    public static final String MIN_HEIGHT = "min-height";
    public static final String MIN_WIDTH = "min-width";
    public static final String ORPHANS = "orphans";
    public static final String OUTLINE = "outline";
    public static final String OVERFLOW = "overflow";
    public static final String OUTLINE_COLOR = "outline-color";
    public static final String OUTLINE_STYLE = "outline-style";
    public static final String OUTLINE_WIDTH = "outline-width";
    public static final String PADDING = "padding";
    public static final String PADDING_BOTTOM = "padding-bottom";
    public static final String PADDING_LEFT = "padding-left";
    public static final String PADDING_RIGHT = "padding-right";
    public static final String PADDING_TOP = "padding-top";
    public static final String PAUSE = "pause";
    public static final String PAUSE_AFTER = "pause-after";
    public static final String PAUSE_BEFORE = "pause-before";
    public static final String PITCH = "pitch";
    public static final String PITCH_RANGE = "pitch-range";
    public static final String PLAY_DURING = "play-during";
    public static final String POSITION = "position";
    public static final String QUOTES = "quotes";
    public static final String RICHNESS = "richness";
    public static final String SPEAK = "speak";
    public static final String SPEAK_HEADER = "speak-header";
    public static final String SPEAK_NUMERAL = "speak-numeral";
    public static final String SPEAK_PUNCTUATION= "speak-punctuation";
    public static final String SPEECH_RATE = "speech-rate";
    public static final String STRESS = "stress";
    public static final String TABLE_LAYOUT = "table-layout";
    public static final String TEXT_ALIGN = "text-align";
    public static final String TEXT_DECORATION = "text-decoration";
    public static final String TEXT_INDENT = "text-indent";
    public static final String TEXT_TRANSFORM = "text-transform";
    public static final String UNICODE_BIDI = "unicode-bidi";
    public static final String VERTICAL_ALIGN= "vertical-align";
    public static final String VISIBILITY = "visibility";
    public static final String VOICE_FAMILY = "voice-family";
    public static final String VOLUME = "volume";
    public static final String WHITE_SPACE = "white-space";
    public static final String WIDOWS = "widows";
    public static final String WIDTH = "width";
    public static final String WORD_SPACING = "word-spacing";
    public static final String Z_SPACING = "z-spacing";

    // suffixes to BORDER_XXX
    public static final String COLOR_SUFFIX = "-color";
    public static final String STYLE_SUFFIX = "-style";
    public static final String WIDTH_SUFFIX = "-width";

    // color values
    public static final String AQUA = "aqua";
    public static final String BLACK = "black";
    public static final String BLUE = "blue";
    public static final String FUCHSIA = "fuchsia";
    public static final String GRAY = "gray";
    public static final String GREEN = "green";
    public static final String LIME = "lime";
    public static final String MAROON = "maroon";
    public static final String NAVY = "navy";
    public static final String OLIVE = "olive";
    public static final String ORANGE = "orange";
    public static final String PURPLE = "purple";
    public static final String RED = "red";
    public static final String SILVER = "silver";
    public static final String TEAL = "teal";
    public static final String WHITE = "white";
    public static final String YELLOW = "yellow";

    // list-style values
    public static final String ARMENIAN = "armenian";
    public static final String CIRCLE = "circle";
    public static final String CJK_IDEOGRAPHIC = "cjk-ideographic";
    public static final String DECIMAL = "decimal";
    public static final String DECIMAL_LEADING_ZERO = "decimal-leading-zero";
    public static final String DISC = "disc";
    public static final String GEORGIAN = "georgian";
    public static final String HEBREW = "hebrew";
    public static final String HIRAGANA = "hiragana";
    public static final String HIRAGANA_IROHA = "hiragana-iroha";
    public static final String KATAKANA = "katakana";
    public static final String KATAKANA_IROHA = "katakana-iroha";
    public static final String LOWER_ALPHA = "lower-alpha";
    public static final String LOWER_GREEK = "lower-greek";
    public static final String LOWER_LATIN = "lower-latin";
    public static final String LOWER_ROMAN = "lower-roman";
    public static final String SQUARE = "square";
    public static final String UPPER_ALPHA = "upper-alpha";
    public static final String UPPER_LATIN = "upper-latin";
    public static final String UPPER_ROMAN = "upper-roman";
    
    
    // other values
    public static final String BLINK = "blink";
    public static final String BLOCK = "block";
    public static final String BOLD = "bold";
    public static final String BOLDER = "bolder";
    public static final String CENTER = "center";
    public static final String DASHED = "dashed";
    public static final String DOTTED = "dotted";
    public static final String DOUBLE = "double";
    public static final String GROOVE = "groove";
    public static final String INLINE = "inline";
    public static final String INLINE_BLOCK = "inline-block";
    public static final String INLINE_TABLE = "inline-table";
    public static final String INSET = "inset";
    public static final String ITALIC = "italic";
    public static final String JUSTIFY = "justify";
    public static final String LARGE = "large";
    public static final String LARGER = "larger";
    public static final String LEFT = "left";
    public static final String LIGHTER = "lighter";
    public static final String LINE_THROUGH = "line-through";
    public static final String LIST_ITEM = "list-item";
    public static final String MEDIUM = "medium";
    public static final String NONE = "none";
    public static final String NORMAL = "normal";
    public static final String NOWRAP = "nowrap";
    public static final String OBLIQUE = "oblique";
    public static final String OUTSET = "outset";
    public static final String OVERLINE = "overline";
    public static final String PRE = "pre";
    public static final String RIDGE = "ridge";
    public static final String RIGHT = "right";
    public static final String RUN_IN = "run-in";
    public static final String SOLID = "solid";
    public static final String SMALL = "small";
    public static final String SMALL_CAPS = "small-caps";
    public static final String SMALLER = "smaller";
    public static final String TABLE = "table";
    public static final String TABLE_CAPTION = "table-caption";
    public static final String TABLE_CELL = "table-cell";
    public static final String TABLE_COLUMN = "table-column";
    public static final String TABLE_COLUMN_GROUP = "table-column-group";
    public static final String TABLE_FOOTER_GROUP = "table-footer-group";
    public static final String TABLE_HEADER_GROUP = "table-header-group";
    public static final String TABLE_ROW = "table-row";
    public static final String TABLE_ROW_GROUP = "table-row-group";
    public static final String THICK = "thick";
    public static final String THIN = "thin";
    public static final String UNDERLINE = "underline";
    public static final String X_LARGE = "x-large";
    public static final String X_SMALL = "x-small";
    public static final String XX_LARGE = "xx-large";
    public static final String XX_SMALL = "xx-small";
    
}


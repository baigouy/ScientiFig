/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2013 Benoit Aigouy 

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are
 met:

 (1) Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer. 

 (2) Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in
 the documentation and/or other materials provided with the
 distribution.  
    
 (3)The name of the author may not be used to
 endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */
package MyShapes;

import Commons.CommonClassesLight;
import Dialogs.ColoredTextPane;
import Tools.StyledDocTools;
import static Tools.StyledDocTools.getText;
import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * StyledDoc2Html converts an sttributed string to an html string --> best
 * choice for serialisation TODO faire un styledDoc 2 html
 *
 * @since <B>Packing Analyzer 8.1</B>
 * @author Benoit Aigouy
 */
public class StyledDoc2Html extends StyledDocTools {

    /*
     * see http://www.w3schools.com/html/html_formatting.asp
     */
    /*
     * see http://www.w3schools.com/tags/tag_u.asp
     */
    public static final String ITALIC_ON = "<i>";
    public static final String ITALIC_OFF = "</i>";
    public static final String BOLD_ON = "<b>";
    public static final String BOLD_OFF = "</b>";
    public static final String SUPERSCRIPT_ON = "<sup>";
    public static final String SUPERSCRIPT_OFF = "</sup>";
    public static final String SUBSCRIPT_ON = "<sub>";
    public static final String SUBSCRIPT_OFF = "</sub>";
    public static final String FONT_OFF = "</font>";
    public static final String NEW_LINE = "<BR>";
    public static final String UNDERLINE_ON = "<u>";
    public static final String UNDERLINE_OFF = "</u>";

    /*
     * fake tags only useful by me
     */
    public static final String FG_COLOR_OFF = "</txtFgcolor>";
    public static final String LOWERCASE_ON = "<low>";//volatile TAG
    public static final String LOWERCASE_OFF = "</low>";//volatile TAG
    public static final String UPPERCASE_ON = "<cap>";//volatile TAG
    public static final String UPPERCASE_OFF = "</cap>";//volatile TAG
    int begin_italic = -1;
    int begin_bold = -1;
    int begin_uppercase = -1;
    int begin_lowercase = -1;
    int begin_FgColor = -1;
    int begin_underline = -1;
    int begin_superscript = -1;
    int begin_color = -1;
    int begin_subscript = -1;
    int begin_font = -1;
//    boolean forceUpperCase = false;
//    boolean forceLowerCase = false;
    Color fg_color = null;
    Font ft = null;
    Color bg_color = null;
    /*
     * these are my forbidden characters
     */
    String superior = "&#xsup"; //>
    String inferior = "&#xinf"; //<
    String ET = "&#x_et"; //&

    /**
     * Empty constructor
     */
    public StyledDoc2Html() {
    }

    /**
     * Closes the font TAG
     *
     * @param ft font
     * @return html string equivalent
     */
    public static String FONT_ON(Font ft) {
        return FONT_ON(ft, -1);
    }

    /**
     * Opens a font TAG see http://www.w3schools.com/tags/tag_font.asp
     *
     * @param ft font
     * @param color font color
     * @return html string equivalent
     */
    public static String FONT_ON(Font ft, int color) {
        if (color != -1) {
            return "<font face=\"" + ft.getFamily() + "\" size=\"" + ft.getSize() + "\" color=\"" + CommonClassesLight.getHtmlColor(color) + "\">";
        } else {
            return "<font face=\"" + ft.getFamily() + "\" size=\"" + ft.getSize() + "\">";
        }
    }

    /**
     * Opens a text FG color TAG
     *
     * @param color
     * @return html string equivalent
     */
    public static String FG_COLOR_ON(int color) {
        return "<txtFgcolor color=\"" + CommonClassesLight.getHtmlColor(color) + "\">";
    }

    /**
     *
     * @param color
     * @return html string equivalent
     */
    public static String BG_COLOR_ON(int color) {
        return "<txtBgcolor color=\"" + CommonClassesLight.getHtmlColor(color) + "\">";
    }

    /**
     * see http://www.htmlhelp.com/reference/html40/entities/symbols.html
     *
     * @param name
     * @return html string equivalent
     */
    public static String GREEK(String name) {
        return "&#x" + Integer.toHexString(CommonClassesLight.greek.get(name).charAt(0));
    }

    /**
     * Recovers the text from html (strips/ignores all the tags)
     *
     * @param string html string
     * @return text
     */
    public String getTextFromHtml(String string) {
        String untagged_text = "";
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '<') {
                /*
                 * a tag was opened --> take action according to the tag
                 */
                i++;
                do {
                    c = string.charAt(i++);
                } while (c != '>');
                i--;
            } else if (c == '&') {
                char next = string.charAt(i + 1);
                if (next == '#') {
                    /*
                     * we found a greek letter
                     */
                    String greek = "";
                    i++;
                    i++;
                    i++;
                    for (int j = 0; j < 3; j++) {
                        greek += string.charAt(i++);
                    }
                    if (greek.equals("sup")) {
                        untagged_text += ">";
                    } else if (greek.equals("inf")) {
                        untagged_text += "<";
                    } else if (greek.equals("_et")) {
                        untagged_text += "&";
                    } /*
                     * we reconvert greek to hexa and hexa to char
                     */ else {
                        int hexa = Integer.parseInt(greek, 16);
                        c = (char) hexa;
                        untagged_text += c;
                    }
                    i--;
                } else {
                    untagged_text += c;
                }
            } else {
                untagged_text += c;
            }
        }
        return untagged_text;
    }

    /**
     * Sets the selection to bold
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     */
    public void bold(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("bold", null);
        StyleConstants.setBold(style, true);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("bold"), false);
    }

    private void replaceLetter(StyledDocument doc, int pos, char c) {
        AttributeSet as = doc.getCharacterElement(pos).getAttributes();
        try {
            doc.insertString(pos, c + "", as);
            doc.remove(pos + 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uppercase(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        String txt = getText(doc);
        for (int i = begin; i < end; i++) {
            char c = txt.charAt(i);
            c = Character.toUpperCase(c);
            AttributeSet as = doc.getCharacterElement(i).getAttributes();
            /**
             * dirty hack to momentaneously transfer upper/lower case to
             * variable text
             */
            MutableAttributeSet attrs = new SimpleAttributeSet(as);
            attrs.addAttribute("forceUpperCase", "true");
            doc.setCharacterAttributes(i, 1, attrs, false);
            replaceLetter(doc, i, c);
        }
    }

    private void lowercase(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        String txt = getText(doc);
        for (int i = begin; i < end; i++) {
            char c = txt.charAt(i);
            AttributeSet as = doc.getCharacterElement(i).getAttributes();
            /**
             * dirty hack to momentaneously transfer upper/lower case to
             * variable text
             */
            MutableAttributeSet attrs = new SimpleAttributeSet(as);
            attrs.addAttribute("forceLowerCase", "true");
            doc.setCharacterAttributes(i, 1, attrs, false);
            c = Character.toLowerCase(c);
            replaceLetter(doc, i, c);
        }
    }

    /**
     * Sets the selection to italic
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     */
    public void italic(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("italic", null);
        StyleConstants.setItalic(style, true);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("italic"), false);
    }

    /**
     * Sets the selection to superScript
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     */
    public void superScript(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("superscript", null);
        StyleConstants.setSuperscript(style, true);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("superscript"), false);
    }

    /**
     * Sets the selection to subScript
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     */
    public void subScript(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("subscript", null);
        StyleConstants.setSubscript(style, true);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("subscript"), false);
    }

    /**
     * underlines the selection
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     */
    public void underline(StyledDocument doc, int begin, int end, int full_length) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("underline", null);
        StyleConstants.setUnderline(style, true);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("underline"), false);
    }

    /**
     * Sets the text foreground color
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     * @param col color
     */
    public void fgColor(StyledDocument doc, int begin, int end, int full_length, Color col) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("fgColor", null);
        StyleConstants.setForeground(style, col);
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("fgColor"), false);
    }

    /**
     * Sets the text font
     *
     * @param doc the source styledDoc
     * @param begin the beginning of the selection
     * @param end the end of the selection
     * @param full_length
     * @param ft Font
     */
    public void font(StyledDocument doc, int begin, int end, int full_length, Font ft) {
        if (begin == -1) {
            return;
        }
        Style style = doc.addStyle("font", null);
        StyleConstants.setFontFamily(style, ft.getFamily());
        StyleConstants.setFontSize(style, ft.getSize());
        doc.setCharacterAttributes(begin, end - begin, doc.getStyle("font"), false);
        doc.setLogicalStyle(full_length, style);
    }

    /**
     *
     * @param tag
     * @param cur_string
     * @param doc
     * @param full_text_length
     */
    public void interpretTag(String tag, String cur_string, StyledDocument doc, int full_text_length) {
        boolean endtag = false;
        if (tag.startsWith("</")) {
            endtag = true;
        } else {
            /*
             * opening tag
             */
            if (tag.toLowerCase().equals(BOLD_ON)) {
                begin_bold = cur_string.length();
            }
            if (tag.toLowerCase().equals(ITALIC_ON)) {
                begin_italic = cur_string.length();
            }
            if (tag.toLowerCase().equals(UNDERLINE_ON)) {
                begin_underline = cur_string.length();
            }
            if (tag.toLowerCase().equals(SUBSCRIPT_ON)) {
                begin_subscript = cur_string.length();
            }
            if (tag.toLowerCase().equals(SUPERSCRIPT_ON)) {
                begin_superscript = cur_string.length();
            }
            if (tag.toLowerCase().equals(UPPERCASE_ON)) {
                begin_uppercase = cur_string.length();
            }
            if (tag.toLowerCase().equals(LOWERCASE_ON)) {
                begin_lowercase = cur_string.length();
            }
            if (tag.toLowerCase().startsWith("<txtfgcolor")) {//write it lower case to ensure compat
                begin_FgColor = cur_string.length();
                String color = CommonClassesLight.strcutl_first(CommonClassesLight.strcutr_fisrt(tag, "=\""), "\"").replace("#", "");
                if (color.length() == 6) {
                    /*
                     * todo parse sting directly all at once 
                     * better parse all at once
                     */
                    int red = Integer.parseInt(color.substring(0, 2), 16);
                    int green = Integer.parseInt(color.substring(2, 4), 16);
                    int blue = Integer.parseInt(color.substring(4, 6), 16);
                    int RGB = (red << 16) + (green << 8) + blue;
                    fg_color = new Color(RGB & 0x00FFFFFF);
                }
            }
            if (tag.toLowerCase().startsWith("<font")) {
                begin_font = cur_string.length();
                String family = CommonClassesLight.strcutl_first(CommonClassesLight.strcutr_fisrt(tag, "ace=\""), "\"");
                String size = CommonClassesLight.strcutl_first(CommonClassesLight.strcutr_fisrt(tag, "ize=\""), "\"");
                int s = (Integer.parseInt(size));
                ft = new Font(family, Font.PLAIN, s);
                if (!ft.getFamily().equals(family)) {
                    System.err.println("Font " + family + " not found --> substituted by Arial");
                    ft = new Font("Arial", Font.PLAIN, s);
                }
            }
        }
        if (endtag) {
            int end_pos = cur_string.length();
            if (tag.toLowerCase().equals(UPPERCASE_OFF)) {
                uppercase(doc, begin_uppercase, end_pos, full_text_length);
                begin_uppercase = -1;
            }
            if (tag.toLowerCase().equals(LOWERCASE_OFF)) {
                lowercase(doc, begin_lowercase, end_pos, full_text_length);
                begin_lowercase = -1;
            }
            if (tag.toLowerCase().equals(BOLD_OFF)) {
                bold(doc, begin_bold, end_pos, full_text_length);
                /*
                 * We reset the tag
                 */
                begin_bold = -1;
            }
            if (tag.toLowerCase().equals(ITALIC_OFF)) {
                italic(doc, begin_italic, end_pos, full_text_length);
                /*
                 * We reset the tag
                 */
                begin_italic = -1;
            }
            if (tag.toLowerCase().equals(SUPERSCRIPT_OFF)) {
                superScript(doc, begin_superscript, end_pos, full_text_length);
                /*
                 * We reset the tag
                 */
                begin_superscript = -1;
            }
            if (tag.toLowerCase().equals(SUBSCRIPT_OFF)) {
                subScript(doc, begin_subscript, end_pos, full_text_length);
                /*
                 * We reset the tag
                 */
                begin_subscript = -1;
            }
            if (tag.toLowerCase().equals(UNDERLINE_OFF)) {
                underline(doc, begin_underline, end_pos, full_text_length);
                /*
                 * We reset the tag
                 */
                begin_underline = -1;
            }
            if (tag.toLowerCase().equals(FG_COLOR_OFF.toLowerCase())) {//bug fix since addition of tolowercase
                fgColor(doc, begin_FgColor, end_pos, full_text_length, fg_color);
                /*
                 * We reset the tag
                 */
                begin_FgColor = -1;
            }
            if (tag.toLowerCase().equals(FONT_OFF)) {
                font(doc, begin_font, end_pos, full_text_length, ft);
                /*
                 * We reset the tag
                 */
                begin_font = -1;
            }
        }
    }

    /**
     * Converts an html String to a styled document
     *
     * @param string html like string
     * @return StyledDoc
     */
    public StyledDocument reparse(String string) {
        StyledDocument doc = new DefaultStyledDocument();
        try {
            /*
             * we reconvert the string back to a styledoc
             */
            ColoredTextPaneSerializable.initDoc(doc);
            String text = getTextFromHtml(string);
            int full_length_text = text.length();
            doc.insertString(0, text, null);
            String untagged_text = "";
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (c == '<') {
                    /*
                     * a tag was opened --> take action according to the tag
                     */
                    String full_tag = "<";
                    i++;
                    do {
                        c = string.charAt(i++);
                        full_tag += c;
                    } while (c != '>');
                    i--;
                    interpretTag(full_tag, untagged_text, doc, full_length_text);
                } else if (c == '&') {
                    char next = string.charAt(i + 1);
                    if (next == '#') {
                        /*
                         * We found a greek letter
                         */
                        String greek = "";
                        i++;
                        i++;
                        i++;
                        for (int j = 0; j < 3; j++) {
                            greek += string.charAt(i++);
                        }

                        if (greek.equals("sup")) {
                            untagged_text += ">";
                        } else if (greek.equals("inf")) {
                            untagged_text += "<";
                        } else if (greek.equals("_et")) {
                            untagged_text += "&";
                        } /*
                         * we reconvert greek to hexa and hexa to char
                         */ else {
                            int hexa = Integer.parseInt(greek, 16);
                            c = (char) hexa;
                            untagged_text += c;
                        }
                        i--;
                    } else {
                        untagged_text += c;
                    }
                } else {
                    untagged_text += c;
                }
            }
        } catch (BadLocationException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return doc;
    }

    /**
     * Adds an italic TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    public String italic(String htmlCode, boolean on) {
        if (on) {
            htmlCode += ITALIC_ON;
        } else {
            htmlCode += ITALIC_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a bold TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    public String bold(String htmlCode, boolean on) {
        if (on) {
            htmlCode += BOLD_ON;
        } else {
            htmlCode += BOLD_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a underline TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    public String underline(String htmlCode, boolean on) {
        if (on) {
            htmlCode += UNDERLINE_ON;
        } else {
            htmlCode += UNDERLINE_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a superscript TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    public String superscript(String htmlCode, boolean on) {
        if (on) {
            htmlCode += SUPERSCRIPT_ON;
        } else {
            htmlCode += SUPERSCRIPT_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a subscript TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    public String subscript(String htmlCode, boolean on) {
        if (on) {
            htmlCode += SUBSCRIPT_ON;
        } else {
            htmlCode += SUBSCRIPT_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a new line TAG to the html like code
     *
     * @param htmlCode
     * @return html string equivalent
     */
    public String newLine(String htmlCode) {
        htmlCode += NEW_LINE;
        return htmlCode;
    }

    /**
     * Closes the font TAG
     *
     * @param htmlCode
     * @return html string equivalent
     */
    public String fontOff(String htmlCode) {
        return font(htmlCode, null, false);
    }

    /**
     * Adds a Font TAG to the html code
     *
     * @param htmlCode
     * @param ft
     * @return html string equivalent
     */
    public String font(String htmlCode, Font ft) {
        if (ft == null) {
            return font(htmlCode, null, false);
        } else {
            return font(htmlCode, ft, true);
        }
    }

    /**
     * Adds a Font TAG to the html code
     *
     * @param htmlCode
     * @return html string equivalent
     */
    public String font(String htmlCode) {
        return htmlCode + FONT_OFF;
    }

    /**
     * Adds a Font TAG to the html code
     *
     * @param htmlCode
     * @param ft
     * @param on
     * @return html string equivalent
     */
    public String font(String htmlCode, Font ft, boolean on) {
        if (on) {
            htmlCode += FONT_ON(ft);
        } else {
            htmlCode += FONT_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a foreground color TAG to the html-like code
     *
     * @param htmlCode
     * @return html string equivalent
     */
    public String fgColor(String htmlCode) {
        return htmlCode + FG_COLOR_OFF;
    }

    /**
     * Adds a foreground color tag to the html-like code
     *
     * @param htmlCode
     * @param color
     * @return html string equivalent
     */
    public String fgColor(String htmlCode, int color) {
        if (color != -1) {
            return htmlCode + FG_COLOR_ON(color);
        }
        return htmlCode + FG_COLOR_OFF;
    }

    /**
     * Converts a StyleDocument to some html-like code
     *
     * @param doc
     * @return html-like code
     */
    public String convertStyledDocToHtml(StyledDocument doc) {
        String html_text = "";
        if (doc == null) {
            return html_text;
        }


        String text = getText(doc);

        text = text.replace(">", ((char) 2) + "");
        text = text.replace("<", ((char) 3) + "");
        text = text.replace("&", ((char) 4) + "");

        boolean isUnderlined = false;
        boolean isSuperScript = false;
        boolean isSubScript = false;
        boolean isBold = false;
        boolean isItalic = false;
        boolean isColored = false;
        boolean isFontDefined = false;
        Color FgColor = null;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Element el = doc.getCharacterElement(i);
            AttributeSet as = el.getAttributes();
            if (!isFontDefined) {
                String font_family = getFontFamily(as);
                int font_size = getFontSize(as);
                html_text = font(html_text, new Font(font_family, Font.PLAIN, font_size));
                isFontDefined = true;
            }
            if (isSubscript(as) || isSuperscript(as)) {
                if (isSubscript(as)) {
                    if (isSuperScript) {
                        isSuperScript = false;
                        html_text = superscript(html_text, false);
                    }
                    if (!isSubScript) {
                        html_text = subscript(html_text, true);
                    }
                    isSubScript = true;
                } else {
                    if (isSubScript) {
                        isSubScript = false;
                        html_text = subscript(html_text, false);
                    }
                    if (!isSuperScript) {
                        html_text = superscript(html_text, true);
                    }
                    isSuperScript = true;
                }
            } else {
                if (isSubScript) {
                    html_text = subscript(html_text, false);
                    isSubScript = false;
                }
                if (isSuperScript) {
                    html_text = superscript(html_text, false);
                    isSuperScript = false;
                }
            }
            if (isBold(as)) {
                if (!isBold) {
                    html_text = bold(html_text, true);
                }
                isBold = true;
            } else {
                if (isBold) {
                    html_text = bold(html_text, false);
                    isBold = false;
                }
            }
            if (isItalic(as)) {
                if (!isItalic) {
                    html_text = italic(html_text, true);
                }
                isItalic = true;
            } else {
                if (isItalic) {
                    html_text = italic(html_text, false);
                    isItalic = false;
                }
            }
            if (isUnderline(as)) {
                if (!isUnderlined) {
                    html_text = underline(html_text, true);
                }
                isUnderlined = true;
            } else {
                if (isUnderlined) {
                    html_text = underline(html_text, false);
                    isUnderlined = false;
                }
            }
            Color fgCol = getFgColor(as);
            if (fgCol != FgColor) {
                FgColor = fgCol;
                if (isColored) {
                    html_text = fgColor(html_text);
                    isColored = false;
                }
            }

            if (fgCol != null) {
                if (!isColored) {
                    html_text = fgColor(html_text, (fgCol.getRGB() & 0x00FFFFFF));
                }
                isColored = true;
            } else {
                if (isColored) {
                    html_text = fgColor(html_text);
                    isColored = false;
                }
            }
            html_text += c;
        }
        if (html_text.equals("")) {
            return html_text;
        }
        if (isBold) {
            html_text = bold(html_text, false);
        }
        if (isItalic) {
            html_text = italic(html_text, false);
        }
        if (isUnderlined) {
            html_text = underline(html_text, false);
        }
        if (isSubScript) {
            html_text = subscript(html_text, false);
        }
        if (isSuperScript) {
            html_text = superscript(html_text, false);
        }
        if (isColored) {
            html_text = fgColor(html_text);
        }
        html_text = font(html_text);
        html_text = html_text.replace(((char) 2) + "", superior);
        html_text = html_text.replace(((char) 3) + "", inferior);
        html_text = html_text.replace(((char) 4) + "", ET);
        return html_text;
    }

    /**
     *
     * @param fontTag
     * @return a font from a font TAG
     */
    public Font getFontFromHtmlTag(String fontTag) {
        String family = "Arial";
        String size = "12";
        if (fontTag.toLowerCase().contains("face=")) {
            family = CommonClassesLight.strcutl_first(CommonClassesLight.strcutr_fisrt(fontTag, "face="), "\"");
        }
        if (fontTag.toLowerCase().contains("size=")) {
            size = CommonClassesLight.strcutl_first(CommonClassesLight.strcutr_fisrt(fontTag, "size="), "\"");
        }
        return new Font(family, 0, Integer.parseInt(size));
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {

        String txt = " <font face=\"Arial Black\" size=\"20\"><txtFgcolor color=\"#ff0000\">Zeommba</txtFgcolor><txtFgcolor color=\"#ffffff\"> <i>sdsqdsqdqsd</i></txtFgcolor></font>";


        if (true) {
            StyledDocument doc = new StyledDoc2Html().reparse(txt);
            System.out.println(new StyledDoc2Html().convertStyledDocToHtml(doc));
            return;
        }


//        try {
//            ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable();
//            ColoredTextPane iopane = new ColoredTextPane(ctps);
//            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "StyledDoc2Html", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
//            if (result == JOptionPane.OK_OPTION) {
//                System.out.println(new StyledDoc2Html().convertStyledDocToHtml(ctps.doc));
//            }
//            System.exit(0);
//        } catch (Exception e) {
//        }

        try {
//            System.out.println(StyledDoc2Html.test());
            //ca marche tres bien --> tester en grandeur nature et remplacer les styled doc et les attributed string par ca --> bcp plus puissant et tres portable
            // new StyledDoc2Html().reparse("<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">Aqsdsqdq</txtFgcolor><txtFgcolor color=\"#ff0000\">sdqsdqsdqsdqs</txtFgcolor><txtFgcolor color=\"#ffffff\">dqsdqsdqsdqsd<txtBgcolor></txtFgcolor></font>");
            //new StyledDoc2Html().reparse("<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">sqd<sub></txtFgcolor><txtFgcolor color=\"#ff0000\">sqdqsd</sub></txtFgcolor><txtFgcolor color=\"#ffffff\">qsdq<sup></txtFgcolor><txtFgcolor color=\"#00ff02\">dqsd</sup></txtFgcolor></font></font>");

            StyledDocument doc = new StyledDoc2Html().reparse("<font face=\"Monospaced\" size=\"14\"><txtFgcolor color=\"#ffffff\">   toto  <low>BLA</low><cap>Bla</cap><low>BLA</low> <cap>d<i>sw<b>sdf</b>sd</cap></i> <b><i></txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</b></i></txtFgcolor><txtFgcolor color=\"#ffffff\"> <i>sdfdsfsdf</i> <sup>sdfdsf</sup> sdf <sub>sdf</sub> dsf</txtFgcolor></font>");
            ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable(doc, "ouba");
            ColoredTextPane iopane = new ColoredTextPane(ctps);
            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "StyledDoc2Html", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                System.out.println(new StyledDoc2Html().convertStyledDocToHtml(ctps.doc));
            }
            //<font face="Arial" size="12"><txtFgcolor color="#ffffff">sqd<sub></txtFgcolor><txtFgcolor color="#ff0000">sqdqsd</sub></txtFgcolor><txtFgcolor color="#ffffff">qsdq<sup></txtFgcolor><txtFgcolor color="#00ff02">dqsd</sup></txtFgcolor></font></font>
            //        long start_time = System.currentTimeMillis();
            //        StyledDoc2Html test = new StyledDoc2Html();
            ////           test.apply(list); 
            //        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        } catch (Exception e) {
            // Logger.getLogger(StyledDoc2Html.class.getName()).log(Level.SEVERE, null, ex);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        System.exit(0);
    }
}



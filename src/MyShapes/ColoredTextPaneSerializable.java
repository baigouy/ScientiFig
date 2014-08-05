/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2014 Benoit Aigouy 

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

import Checks.CheckFont;
import Checks.CheckLetterCase;
import Checks.CheckStyle;
import Checks.SFTextController;
import Checks.CheckText;
import Checks.ChooseASolution;
import Commons.CommonClassesLight;
import Tools.StyledDocTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Map;
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
 * ColoredTextPaneSerializable is a serializable object that contains text and
 * various text attributes such as text color, bold, ...
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */

/*
 * quick fix to set the bg of jtextpane
 */
public class ColoredTextPaneSerializable implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 4953163942076632816L;
    /**
     * styleddoc are serializable but not long term according to the java doc
     * --> we don't want that --> better save everything as pseudo html in
     * serializedStyledDocumentContent
     */
    public transient StyledDocument doc;
    private String serializedStyledDocumentContent = "";
    private String title;
    public Font ft = new Font("Arial", Font.PLAIN, 12);
    public static final String BOLD = "Bold";
    public static final String NOT = "Not ";
    public static final String NOTBOLD = NOT + BOLD;
    public static final String ITALIC = "Italic";
    public static final String NOTITALIC = NOT + ITALIC;
    public static final String SUBSCRIPT = "Subscript";
    public static final String NOTSUBSCRIPT = NOT + SUBSCRIPT;
    public static final String SUPERSCRIPT = "Superscript";
    public static final String NOTSUPERSCRIPT = NOT + SUPERSCRIPT;
    public static final String UNDERLINED = "Underlined";
    public static final String NOTUNDERLINED = NOT + UNDERLINED;
    public Color textBgColor;
    public boolean isFrame = false;

    /**
     * Empty constructor
     */
    public ColoredTextPaneSerializable() {
        this.doc = new DefaultStyledDocument();
        initDoc(doc, ft);
    }

    /**
     * Constructor
     *
     * @param doc
     * @param title
     * @param ft
     * @param bgColor
     * @param isFrame defines if the ColoredTextPaneSerializable should be in
     * frame mode (bg color would then be applied to the frame around the text,
     * if false the color will be applied to the inside of the rectangle
     * bounding the text)
     */
    public ColoredTextPaneSerializable(StyledDocument doc, String title, Font ft, Color bgColor, boolean isFrame) {
        this(doc, title);
        this.textBgColor = bgColor;
        this.isFrame = isFrame;
        this.ft = ft;
    }

    /**
     * Constructor
     *
     * @param doc
     * @param title
     */
    public ColoredTextPaneSerializable(StyledDocument doc, String title) {
        this();
        this.doc = doc;
        this.title = title;
    }

    /**
     * Constructor
     *
     * @param text
     */
    public ColoredTextPaneSerializable(String text) {
        this();
        try {
            if (text != null) {
                doc.insertString(0, text, null);
            }
        } catch (BadLocationException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    public StyledDocument getDoc() {
        if (doc == null) {
            recreateStyledDoc();
        }
        return doc;
    }

    public void setDoc(StyledDocument doc) {
        this.doc = doc;
    }

    public ColoredTextPaneSerializable(String text, Color bgColor) {
        this(text);
        this.textBgColor = bgColor;
    }

    public ColoredTextPaneSerializable(String text, Color bgColor, Font ft) {
        this(text);
        this.textBgColor = bgColor;
        this.ft = ft;
        setFontToAllText(ft, true, true);
    }

    /**
     * Constructor
     *
     * @param text
     * @param title
     */
    public ColoredTextPaneSerializable(String text, String title) {
        this(text);
        this.title = title;
    }

    /**
     * initialize the StyleDocument
     *
     * @param doc
     */
    public static void initDoc(StyledDocument doc) {
        initDoc(doc, new Font("Arial", Font.PLAIN, 12));
    }

    /**
     * Creates various styles for the styleDocument and applies them
     *
     * @param doc
     * @param ft
     */
    public static void initDoc(StyledDocument doc, Font ft) {
        Style style = doc.addStyle("WHITE", null);
        StyleConstants.setForeground(style, new Color(0xFFFFFF));
        StyleConstants.setFontFamily(style, ft.getFamily());
        StyleConstants.setFontSize(style, ft.getSize());

        doc.setLogicalStyle(doc.getLength(), style);
        doc.setCharacterAttributes(0, doc.getLength(), style, false);
        doc.setParagraphAttributes(0, doc.getLength(), style, false);

        Style style3 = doc.addStyle(BOLD, null);
        StyleConstants.setBold(style3, true);

        Style style2 = doc.addStyle(NOTBOLD, null);
        StyleConstants.setBold(style2, false);

        Style style4 = doc.addStyle(ITALIC, null);
        StyleConstants.setItalic(style4, true);

        Style style5 = doc.addStyle(NOTITALIC, null);
        StyleConstants.setItalic(style5, false);

        Style style6 = doc.addStyle(SUBSCRIPT, null);
        StyleConstants.setSubscript(style6, true);

        Style style7 = doc.addStyle(NOTSUBSCRIPT, null);
        StyleConstants.setSubscript(style7, false);

        Style style8 = doc.addStyle(SUPERSCRIPT, null);
        StyleConstants.setSuperscript(style8, true);

        Style style9 = doc.addStyle(NOTSUPERSCRIPT, null);
        StyleConstants.setSuperscript(style9, false);
    }

    /**
     * Sets or changes the text, its color and the text background color
     *
     * @param color text color
     * @param bgColor text background color
     * @param ft text font
     * @param change_color override text color
     * @param change_bg_color override text background color
     * @param change_ft override text font
     */
    public void setTextAndBgColor(int color, Color bgColor, Font ft, boolean change_color, boolean change_bg_color, boolean change_ft, boolean isOverrideItalic, boolean isOverRideBold) {
        if (change_ft) {
            if (ft == null) {
                return;
            }
            Style style = doc.addStyle("textFont", null);
            StyleConstants.setFontFamily(style, ft.getFamily());
            StyleConstants.setFontSize(style, ft.getSize());
            if (ft.isItalic()) {
                StyleConstants.setItalic(style, true);
            }
            if (ft.isBold()) {
                StyleConstants.setBold(style, true);
            }
            setFontToAllText(ft, isOverrideItalic, isOverRideBold);
            /*
             *  apply the font to all the text
             */
            doc.setLogicalStyle(0, null);
            doc.setParagraphAttributes(0, doc.getLength(), doc.getStyle("textFont"), false);
        }
        if (change_bg_color) {
            Style style = doc.addStyle("textBgColor", null);
            setTextBgColor(bgColor);
            if (bgColor != null) {
                StyleConstants.setBackground(style, bgColor);
            }
            doc.setLogicalStyle(doc.getLength(), style);
            doc.setParagraphAttributes(0, doc.getLength(), doc.getStyle("textBgColor"), false);
            textBgColor = bgColor;
        }
        if (change_color) {
            Style style = doc.addStyle("textColor", null);
            StyleConstants.setForeground(style, new Color(color));
            doc.setLogicalStyle(doc.getLength(), style);
            doc.setParagraphAttributes(0, doc.getLength(), doc.getStyle("textColor"), false);
            doc.setCharacterAttributes(0, doc.getLength(), style, false);
        }
    }

    /**
     *
     * @return true if ColoredTextPaneSerializable is in frame mode
     */
    public boolean isFrame() {
        return isFrame;
    }

    /**
     * Sets the current ColoredTextPaneSerializable to frame mode
     *
     * @param isFrame
     */
    public void setFrame(boolean isFrame) {
        this.isFrame = isFrame;
    }

    /**
     * Applies the 'jp' journal style to the text
     *
     * @param jp
     */
    public void setJournalStyle(JournalParameters jp, boolean isOverrideItalic, boolean isOverRideBold) {
        setFontToAllText(jp.getOutterTextFont(), isOverrideItalic, isOverRideBold);
    }

    /**
     *
     * @param serializedStyledDocumentContent is the serializable string
     * representation of a ColoredTextPaneSerializable
     */
    public void setSerializedStyledDocumentContent(String serializedStyledDocumentContent) {
        this.serializedStyledDocumentContent = serializedStyledDocumentContent;
    }

    /**
     *
     * @return the unfromatted text contained in the ColoredTextPaneSerializable
     */
    public String getText() {
        try {
            return doc.getText(0, doc.getLength());
        } catch (Exception e) {
        }
        return "";
    }

    /**
     *
     * @return the title of the ColoredTextPaneSerializable
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the ColoredTextPaneSerializable
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;

    }

    /**
     *
     * @return true if the styledDoc contains text
     */
    public boolean hasText() {
        if (!getText().equals("")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param as
     * @return true if the AttributeSet is bold
     */
    public boolean isBold(AttributeSet as) {
        return StyleConstants.isBold(as);
    }

    /**
     *
     * @param el
     * @return true if the Element is bold
     */
    public boolean isBold(Element el) {
        AttributeSet as = el.getAttributes();
        return isBold(as);
    }

    /**
     *
     * @param pos
     * @return true if the character at postion 'pos' is bold
     */
    public boolean isBold(int pos) {
        Element el = doc.getCharacterElement(pos);
        return isBold(el);
    }

    /**
     *
     * @param as
     * @return true if the AttributeSet is italic
     */
    public boolean isItalic(AttributeSet as) {
        return StyleConstants.isItalic(as);
    }

    /**
     *
     * @param el
     * @return true if the Element is italic
     */
    public boolean isItalic(Element el) {
        AttributeSet as = el.getAttributes();
        return isItalic(as);
    }

    /**
     *
     * @param pos
     * @return true if the character at postion 'pos' is italic
     */
    public boolean isItalic(int pos) {
        Element el = doc.getCharacterElement(pos);
        return isItalic(el);
    }

    /**
     *
     * @param as
     * @return true if the AttributeSet is subscript
     */
    public boolean isSubscript(AttributeSet as) {
        return StyleConstants.isSubscript(as);
    }

    /**
     *
     * @param el
     * @return true if the Element is subscript
     */
    public boolean isSubscript(Element el) {
        AttributeSet as = el.getAttributes();
        return isSubscript(as);
    }

    /**
     *
     * @param pos
     * @return true if the character at postion 'pos' is subscript
     */
    public boolean isSubscript(int pos) {
        Element el = doc.getCharacterElement(pos);
        return isSubscript(el);
    }

    /**
     *
     * @param as
     * @return true if the AttributeSet is superscript
     */
    public boolean isSuperscript(AttributeSet as) {
        return StyleConstants.isSuperscript(as);
    }

    /**
     *
     * @param el
     * @return true if the Element is superscript
     */
    public boolean isSuperscript(Element el) {
        AttributeSet as = el.getAttributes();
        return isSuperscript(as);
    }

    /**
     *
     * @param pos
     * @return true if the character at postion 'pos' is superscript
     */
    public boolean isSuperscript(int pos) {
        Element el = doc.getCharacterElement(pos);
        return isSuperscript(el);
    }

    /**
     *
     * @param as
     * @return true if the AttributeSet is underlined
     */
    public boolean isUnderline(AttributeSet as) {
        return StyleConstants.isUnderline(as);
    }

    /**
     *
     * @param el
     * @return true if the Element is underlined
     */
    public boolean isUnderline(Element el) {
        AttributeSet as = el.getAttributes();
        return isUnderline(as);
    }

    /**
     *
     * @param pos
     * @return true if the character at postion 'pos' is underlined
     */
    public boolean isUnderline(int pos) {
        Element el = doc.getCharacterElement(pos);
        return isUnderline(el);
    }

    /**
     *
     * @param as
     * @return the font size used in the current AttributeSet
     */
    public int getFontSize(AttributeSet as) {
        return StyleConstants.getFontSize(as);
    }

    /**
     *
     * @param el
     * @return the font size used in the current element
     */
    public int getFontSize(Element el) {
        AttributeSet as = el.getAttributes();
        return getFontSize(as);
    }

    /**
     *
     * @param pos
     * @return the font size used for the character at position 'pos'
     */
    public int getFontSize(int pos) {
        Element el = doc.getCharacterElement(pos);
        return getFontSize(el);
    }

    /**
     *
     * @param as
     * @return the font family used in the current AttributeSet
     */
    public String getFontFamily(AttributeSet as) {
        return StyleConstants.getFontFamily(as);
    }

    /**
     *
     * @param el
     * @return the font family used in the current element
     */
    public String getFontFamily(Element el) {
        AttributeSet as = el.getAttributes();
        return getFontFamily(as);
    }

    /**
     *
     * @param pos
     * @return the font family used for the character at position 'pos'
     */
    public String getFontFamily(int pos) {
        Element el = doc.getCharacterElement(pos);
        return getFontFamily(el);
    }

    /**
     *
     * @param as
     * @return the foreground color used in the current AttributeSet
     */
    public Color getFgColor(AttributeSet as) {
        return StyleConstants.getForeground(as);
    }

    /**
     *
     * @param el
     * @return the foreground color used in the current element
     */
    public Color getFgColor(Element el) {
        AttributeSet as = el.getAttributes();
        return getFgColor(as);
    }

    /**
     *
     * @param pos
     * @return the foreground color used for the character at position 'pos'
     */
    public Color getFgColor(int pos) {
        Element el = doc.getCharacterElement(pos);
        return getFgColor(el);
    }

    /**
     * Rebuilds the styled doc from its html-like description
     */
    public void recreateStyledDoc() {
        StyledDoc2Html test = new StyledDoc2Html();
        this.doc = test.reparse(serializedStyledDocumentContent);
    }

    /**
     * Converts the current styleDocument to an equivalent html-like code
     */
    public void getReadyForSerialization() {
        StyledDoc2Html test = new StyledDoc2Html();
        serializedStyledDocumentContent = test.convertStyledDocToHtml(doc/*, textBgColor*/);
    }

    /**
     *
     * @return an html-like code equivalent to the content of the styleDoc
     */
    public String getFormattedText() {
        StyledDoc2Html test = new StyledDoc2Html();
        serializedStyledDocumentContent = test.convertStyledDocToHtml(doc/*, textBgColor*/);
        return serializedStyledDocumentContent;
    }

    /**
     * this function can convert the first letter to upper case, to lower case
     * or ignore its case depending on the passed parameters
     *
     * @param capitalisationOfLetter
     */
    public void treatWiselyFirstLetterCapitalization(String capitalisationOfLetter) {
        if (capitalisationOfLetter == null || capitalisationOfLetter.toLowerCase().contains("as it")) {
            return;
        }
        if (capitalisationOfLetter.toLowerCase().contains("upper")) {
            if (doc != null && doc.getLength() >= 1) {
                try {
                    String letter = doc.getText(0, 1);
                    letter = letter.toUpperCase();
                    Element attributes = doc.getCharacterElement(0);
                    doc.remove(0, 1);
                    doc.insertString(0, letter, attributes.getAttributes());
                } catch (Exception e) {
                }
            }
        } else {
            if (doc != null && doc.getLength() >= 1) {

                try {
                    String letter = doc.getText(0, 1);
                    letter = letter.toLowerCase();
                    Element attributes = doc.getCharacterElement(0);
                    doc.remove(0, 1);
                    doc.insertString(0, letter, attributes.getAttributes());
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     *
     * @return an attributed string that is equivalent to the content of the
     * styledDoc
     */
    public ArrayList<AttributedString> createAttributedString() {

        ArrayList<AttributedString> multiline_text = new ArrayList<AttributedString>();
        String[] splitted_text = getText().split("\n");
        int max = splitted_text.length;
        int curpos = 0;
        int counter = 0;
        for (String string : splitted_text) {
            counter++;
            /*
             * bug fix for \n lost due to split
             */
            if (counter != max) {
                string += "\n";
            }
            AttributedString curString = new AttributedString(string);
            int begin = curpos;
            int end = curpos + string.length();
            for (int i = begin, j = 0; i < end; i++, j++) {
                Element el = doc.getCharacterElement(i);
                AttributeSet as = el.getAttributes();
                String font_family = getFontFamily(as);
                curString.addAttribute(TextAttribute.FAMILY, font_family, j, j + 1);
                int font_size = getFontSize(as);
                curString.addAttribute(TextAttribute.SIZE, (float) font_size, j, j + 1);
                if (isSubscript(as) || isSuperscript(as)) {
                    if (isSubscript(as)) {
                        curString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, j, j + 1);
                    } else {
                        curString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, j, j + 1);
                    }
                }
                if (isBold(as)) {
                    curString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, j, j + 1);
                }
                if (isItalic(as)) {
                    curString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, j, j + 1);
                }
                if (isUnderline(as)) {
                    curString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, j, j + 1);
                }
                Color fgCol = getFgColor(as);
                if (fgCol != null) {
                    curString.addAttribute(TextAttribute.FOREGROUND, fgCol, j, j + 1);
                }
            }
            curpos += string.length();
            multiline_text.add(curString);
        }
        return multiline_text;
    }

    /**
     *
     * @return the color of the rectangle bounding the text
     */
    public Color getTextBgColor() {
        return textBgColor;
    }

    /**
     * Change the font the text
     *
     * @param ft font
     */
    public final void setFontToAllText(Font ft, boolean isOverrideItalic, boolean isOverRideBold) {
        this.ft = ft;
        MutableAttributeSet attrs = new SimpleAttributeSet();
        attrs.addAttribute(TextAttribute.FAMILY, ft.getFamily());
        attrs.addAttribute(TextAttribute.SIZE, ft.getSize());
        if (isOverrideItalic) {
            if (ft.isItalic()) {
                attrs.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
            } else {
                attrs.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
            }
        }
        if (isOverRideBold) {
            if (ft.isBold()) {
                attrs.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            } else {
                attrs.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
            }
        }
        StyleConstants.setFontFamily(attrs, ft.getFamily());
        StyleConstants.setFontSize(attrs, ft.getSize());
        doc.setCharacterAttributes(0, getText().length(), attrs, false);
        /*
         * even if there is no text
         * we set the default parameters of the doc
         */
        Style default_font = doc.addStyle("default_style", null);
        StyleConstants.setFontFamily(default_font, ft.getFamily());
        StyleConstants.setFontSize(default_font, ft.getSize());

        if (isOverrideItalic) {
            if (ft.isItalic()) {
                StyleConstants.setItalic(default_font, true);
            } else {
                StyleConstants.setItalic(default_font, false);
            }
        }
        if (isOverRideBold) {
            if (ft.isBold()) {
                StyleConstants.setBold(default_font, true);
            } else {
                StyleConstants.setBold(default_font, false);
            }
        }
        doc.setLogicalStyle(doc.getLength(), default_font);
        doc.setParagraphAttributes(0, doc.getLength(), default_font, false);
        doc.setCharacterAttributes(0, doc.getLength(), default_font, false);
    }

    public void setBold() {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        Style default_font = doc.addStyle("default_style", null);
        StyleConstants.setBold(default_font, true);
        attrs.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        doc.setCharacterAttributes(0, getText().length(), attrs, false);
        doc.setLogicalStyle(doc.getLength(), default_font);
        doc.setParagraphAttributes(0, doc.getLength(), default_font, false);
        doc.setCharacterAttributes(0, doc.getLength(), default_font, false);
    }

    public void setItalic() {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        Style default_font = doc.addStyle("default_style", null);
        StyleConstants.setItalic(default_font, true);
        attrs.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        doc.setCharacterAttributes(0, getText().length(), attrs, false);
        doc.setLogicalStyle(doc.getLength(), default_font);
        doc.setParagraphAttributes(0, doc.getLength(), default_font, false);
        doc.setCharacterAttributes(0, doc.getLength(), default_font, false);
    }

    /**
     *
     * @return a list of available fonts on the system
     */
    public String[] getAvailableFonts() {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] avialable_fonts = g.getAvailableFontFamilyNames();
        return avialable_fonts;
    }

    /**
     * Sets the text backround color to 'bgcolor'
     *
     * @param bgColor
     */
    public void setTextBgColor(Color bgColor) {
        this.textBgColor = bgColor;
    }

    /**
     *
     * @return an array of simplified texts
     */
    public ArrayList<ArrayList<MyFormattedString>> simplify_text() {
        ArrayList<AttributedString> as = createAttributedString();
        return simplify_text(as);
    }

    /**
     * Converts AttributedString to a simpler version
     *
     * @param as
     * @return a set of simple formatted texts
     */
    public ArrayList<ArrayList<MyFormattedString>> simplify_text(ArrayList<AttributedString> as) {

        ArrayList<ArrayList<MyFormattedString>> final_text = new ArrayList<ArrayList<MyFormattedString>>();
        for (AttributedString attributedString : as) {
            ArrayList<MyFormattedString> simplified_text2 = simplify_text(attributedString);
            final_text.add(simplified_text2);
        }
        return final_text;
    }

    /**
     *
     * @param g2d
     * @return the bounding rectangle for the current text
     */
    public Rectangle2D getTextBounds(Graphics2D g2d) {
        float y = 0;
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        int max_x = Integer.MIN_VALUE;
        int max_y = Integer.MIN_VALUE;
        double width = 0;
        double height = 0;
        boolean no_graphics = g2d == null ? true : false;
        if (no_graphics) {
            g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
        }
        ArrayList<AttributedString> as = createAttributedString();
        for (AttributedString attributedString : as) {
            AttributedCharacterIterator aci = attributedString.getIterator();
            if (aci == null || aci.getRunLimit() == 0) {
                continue;
            }
            TextLayout tl = new TextLayout(aci, g2d.getFontRenderContext());
            y += tl.getAscent();
            Rectangle r4 = tl.getPixelBounds(g2d.getFontRenderContext(), 0, y);
            y += tl.getDescent() + tl.getLeading();
            height = y;
            width = Math.max(width, tl.getBounds().getWidth());
            max_x = Math.max(max_x, r4.x + r4.width);
            max_y = Math.max(max_y, r4.y + r4.height);
            min_x = Math.min(min_x, r4.x);
            min_y = Math.min(min_y, r4.y);
        }
        Rectangle2D.Double r = new Rectangle2D.Double(width, height, max_x - min_x, max_y - min_y);
        if (no_graphics) {
            g2d.dispose();
        }
        return r;
    }

    /**
     * Split the text into simpler (more consitant) chunks, necessary for proper
     * drawing of the text especially when using different colors or when it
     * uses things such as subscript, ...
     *
     * @param as1
     * @return simplified text chunks
     */
    public ArrayList<MyFormattedString> simplify_text(AttributedString as1) {
        ArrayList<MyFormattedString> textAndParameters = new ArrayList<MyFormattedString>();
        AttributedCharacterIterator iter = as1.getIterator();
        Map<AttributedCharacterIterator.Attribute, Object> lastAttributes = null;
        String currentString = "";
        while (iter.getIndex() < iter.getEndIndex()) {
            Map<AttributedCharacterIterator.Attribute, Object> as = iter.getAttributes();
            if (lastAttributes == null) {
                lastAttributes = as;
            }
            if (!as.equals(lastAttributes)) {
                MyFormattedString mfs = new MyFormattedString(currentString, lastAttributes);
                textAndParameters.add(mfs);
                currentString = "";
            }
            currentString += iter.current();
            iter.next();
            lastAttributes = as;
        }
        MyFormattedString mfs = new MyFormattedString(currentString, lastAttributes);
        textAndParameters.add(mfs);
        return textAndParameters;
    }

    /**
     * Colors the text contained between pos 'start'
     *
     * @param start
     * @param length length of the text to color
     * @param color new color
     */
    public void colorText(int start, int length, int color) {
        Style style = doc.addStyle(CommonClassesLight.getHtmlColor(color), null);
        StyleConstants.setForeground(style, new Color(color));
        doc.setParagraphAttributes(start, length, style, false);
        doc.setCharacterAttributes(start, length, style, false);
        /*
         * to fix the color bug upon reletterring
         */
        if (start == 0 && length == doc.getLength()) {
            doc.setLogicalStyle(0, style);
        }
    }

    /**
     * Replace current text by 'string'
     *
     * @param string
     */
    public void setText(String string) {
        try {
            if (doc == null) {
                recreateStyledDoc();
            }
            doc.remove(0, doc.getLength());
            doc.insertString(0, string, null);
        } catch (BadLocationException ex) {
        }
    }

    public boolean checkCase(BufferedImage img, String caseOfTheFirstLetter) {
        boolean changed = false;
        CheckLetterCase iopane = new CheckLetterCase(img, this, caseOfTheFirstLetter);
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Check first character case", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                setDoc(iopane.getColoredTextPaneSerializable().getDoc());
                changed = true;
            }
        }
        return changed;
    }

    public boolean checkFont(BufferedImage img, Font font, String fontName) {
        CheckFont iopane = new CheckFont(img, this, font, fontName);
        boolean changed = false;
        if (iopane.force) {
            /*
             * No text no need to warn the user, let's just apply the font directly
             */
            setDoc(iopane.getColoredTextPaneSerializable().getDoc());
            ft = iopane.getColoredTextPaneSerializable().ft;
            changed = true;
        } else if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Font Check", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                setDoc(iopane.getColoredTextPaneSerializable().getDoc());
                ft = iopane.getColoredTextPaneSerializable().ft;
                changed = true;
            }
        }
        return changed;
    }

    public boolean checkStyle(BufferedImage snap) {
        if (doc == null) {
            recreateStyledDoc();
        }
        boolean changed = false;
        for (int i = 0; i < StyledDocTools.MAX_STYLES; i++) {
            CheckStyle iopane = new CheckStyle(snap, doc, i);
            if (!iopane.noError) {
                int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Checking Style", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                if (result == JOptionPane.OK_OPTION) {
                    this.doc = iopane.getStyleCorrectedDoc();
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean checkText(JournalParameters jp, BufferedImage preview) {
        ArrayList<SFTextController> controls = jp.getAdvancedtextFormattingRules();
        if (controls == null || controls.isEmpty()) {
            return false;
        }
        if (doc == null) {
            recreateStyledDoc();
        }
        boolean changed = false;
        for (SFTextController test : controls) {
            CheckText iopane = new CheckText(preview, doc, test);
            if (!iopane.noError) {
                int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Check text", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                if (result == JOptionPane.OK_OPTION) {
                    if (iopane.getCount() > 2) {
                        ChooseASolution iopane2 = new ChooseASolution(iopane.getCount());
                        result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane2}, "Choose a solution", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            int pos = iopane2.getPos();
                            StyledDocument finalDoc = iopane.getDoc(pos);
                            if (finalDoc != null) {
                                this.doc = finalDoc;
                                changed = true;
                            }
                        }
                    } else {
                        StyledDocument finalDoc = iopane.getDoc(0);
                        if (finalDoc != null) {
                            this.doc = finalDoc;
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    private void replaceCharacter(int pos, String character) {
        try {
            Element el = doc.getCharacterElement(pos);
            AttributeSet as = el.getAttributes();
            doc.remove(pos, 1);
            doc.insertString(pos, character, as);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

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
package Dialogs;

import MyShapes.ColoredTextPaneSerializable;
import MyShapes.MyFontTool;
import Commons.CommonClassesLight;
import Commons.PaintedButton;
import Commons.SaverLight;
import MyShapes.StyledDoc2Html;
import Tools.StyledDocTools;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * ColoredTextPane is a dialog designed to add text to images, ...
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ColoredTextPane extends JPanel {

    /*
     * Variables
     */
    private boolean loading = false;
    ColoredTextPaneSerializable ctps;
    HashSet<Object> styles = new HashSet<Object>();

    /**
     * Constructor that creates a coloredTextPaneSerializable from another one
     *
     * @param coloredTextPaneSerializable
     */
    public ColoredTextPane(ColoredTextPaneSerializable coloredTextPaneSerializable) {
        if (coloredTextPaneSerializable.doc == null) {
            coloredTextPaneSerializable.recreateStyledDoc();
        }
        ctps = new ColoredTextPaneSerializable(coloredTextPaneSerializable.doc, coloredTextPaneSerializable.getTitle(), coloredTextPaneSerializable.ft, coloredTextPaneSerializable.textBgColor, false);
        initComponents();
        Color col = coloredTextPaneSerializable.getTextBgColor();
        if (col != null) {
            if (col.equals(new Color(0, 0, 0))) {
                jRadioButton2.setSelected(true);
            } else {
                jRadioButton3.setSelected(true);
            }
        }
        /**
         * we set the caret color to the negative of that of the background so
         * that we can still see it on a white bg and on a black bg
         */
        Color bgColor = ctps.getTextBgColor();
        if (bgColor == null) {
            jTextPane1.setCaretColor(Color.BLACK);
        } else if ((bgColor.getRGB() & 0x00FFFFFF) == 0x000000) {
            jTextPane1.setCaretColor(Color.WHITE);
        } else {
            jTextPane1.setCaretColor(Color.BLACK);
        }
        loading = true;
        loadFonts();
        loading = false;
        /**
         * the following line was causing a big error in the font upon loading
         * of a styledDoc --> a tester
         */
        enableFrameSelection(false);
    }

    /**
     * Constructor that creates a coloredTextPaneSerializable from another one.
     * This version is optimized for R text for which the fg/bg colors and font
     * should not be changed.
     *
     * @param coloredTextPaneSerializable
     */
    public ColoredTextPane(ColoredTextPaneSerializable coloredTextPaneSerializable, boolean showFont, boolean showBgColor, boolean showFgColor) {
        if (coloredTextPaneSerializable == null) {
            coloredTextPaneSerializable = new ColoredTextPaneSerializable();
        }
        if (coloredTextPaneSerializable.doc == null) {
            coloredTextPaneSerializable.recreateStyledDoc();
        }
        ctps = new ColoredTextPaneSerializable(coloredTextPaneSerializable.doc, coloredTextPaneSerializable.getTitle(), coloredTextPaneSerializable.ft, coloredTextPaneSerializable.textBgColor, false);
        initComponents();
        Color col = coloredTextPaneSerializable.getTextBgColor();
        if (col != null) {
            if (col.equals(new Color(0, 0, 0))) {
                jRadioButton2.setSelected(true);
            } else {
                jRadioButton3.setSelected(true);
            }
        }
        /**
         * we set the caret color to the negative of that of the background so
         * that we can still see it on a white bg and on a black bg
         */
        Color bgColor = ctps.getTextBgColor();
        if (bgColor == null) {
            jTextPane1.setCaretColor(Color.BLACK);
        } else if ((bgColor.getRGB() & 0x00FFFFFF) == 0x000000) {
            jTextPane1.setCaretColor(Color.WHITE);
        } else {
            jTextPane1.setCaretColor(Color.BLACK);
        }
        showFont(showFont);
        if (!showFgColor) {
            paintedButton1.setVisible(false);
        }
        loading = true;
        loadFonts();
        loading = false;
        fontChanged(null);
        revalidate();
        enableFrameSelection(false);
    }

    public void showFont(boolean showFont) {
        if (!showFont) {
            jComboBox1.setVisible(false);
            jSpinner1.setVisible(false);
        }

    }

    /**
     * Constructore that creates a ColoredTextPane from a html-like string
     *
     * @param text
     */
    public ColoredTextPane(String text) {
        ctps = new ColoredTextPaneSerializable(text);
        initComponents();
        loadFonts();
        ctps.createAttributedString();
        /**
         * we set the caret color to the negative of that of the background so
         * that we can still see it on a white bg and on a black bg
         */
        Color bgColor = ctps.getTextBgColor();
        if (bgColor == null) {
            jTextPane1.setCaretColor(Color.BLACK);
        } else if ((bgColor.getRGB() & 0x00FFFFFF) == 0x000000) {
            jTextPane1.setCaretColor(Color.WHITE);
        } else {
            jTextPane1.setCaretColor(Color.BLACK);
        }
        enableFrameSelection(false);
    }

    /**
     * Constructor that creates a ColoredTextPane from a title and a styledDoc
     *
     * @param doc
     * @param title
     */
    public ColoredTextPane(StyledDocument doc, String title) {
        this(new ColoredTextPaneSerializable(doc, title));
        /**
         * we set the caret color to the negative of that of the background so
         * that we can still see it on a white bg and on a black bg
         */
        Color bgColor = ctps.getTextBgColor();
        if (bgColor == null) {
            jTextPane1.setCaretColor(Color.BLACK);
        } else if ((bgColor.getRGB() & 0x00FFFFFF) == 0x000000) {
            jTextPane1.setCaretColor(Color.WHITE);
        } else {
            jTextPane1.setCaretColor(Color.BLACK);
        }
        loadFonts();
        enableFrameSelection(false);
    }

    /**
     * Constructor that creates a ColoredTextPane from a text and a title
     *
     * @param text
     * @param title
     */
    public ColoredTextPane(String text, String title) {
        this(text);
        setTitle(title);
        initComponents();
        enableFrameSelection(false);
    }

    /**
     * Constructor that creates a ColoredTextPane where the color of the text is
     * defined by textcolor
     *
     * @param textColor
     */
    public ColoredTextPane(int textColor) {
        this();
        createNewStyle(textColor);
        loadFonts();
        enableFrameSelection(false);
    }

    /**
     * Sets the coloredTextPaneSerializable
     *
     * @param coloredTextPaneSerializable
     */
    public void setColoredTextPaneSerializable(ColoredTextPaneSerializable coloredTextPaneSerializable) {
        loading = true;
        if (coloredTextPaneSerializable.doc == null) {
            coloredTextPaneSerializable.recreateStyledDoc();
        }
        this.ctps = coloredTextPaneSerializable;

        //   ctps = new ColoredTextPaneSerializable(coloredTextPaneSerializable.doc, coloredTextPaneSerializable.getTitle(), coloredTextPaneSerializable.ft, coloredTextPaneSerializable.textBgColor, coloredTextPaneSerializable.isFrame());
        //   jTextPane1.setDocument(ctps.doc);
        jTextPane1.setDocument(ctps.doc);
        if (ctps.isFrame()) {
            jRadioButton5.setSelected(true);
        }
        Color col = coloredTextPaneSerializable.getTextBgColor();
        if (col != null) {
            if (col.equals(new Color(0, 0, 0))) {
                jRadioButton2.setSelected(true);
            } else {
                jRadioButton3.setSelected(true);
            }
        } else {
            jRadioButton1.setSelected(true);
        }
        if (ctps.getTextBgColor() != null) {
            jTextPane1.setBackground(ctps.getTextBgColor());
        } else {
            jTextPane1.setBackground(new Color(128, 128, 128));
        }
        /*
         * we set the caret color to the negative of that of the background so that we can still see it on a white bg and on a black bg
         */
        Color bgColor = ctps.getTextBgColor();
        if (bgColor == null) {
            jTextPane1.setCaretColor(Color.BLACK);
        } else if ((bgColor.getRGB() & 0x00FFFFFF) == 0x000000) {
            jTextPane1.setCaretColor(Color.WHITE);
        } else {
            jTextPane1.setCaretColor(Color.BLACK);
        }
        loadFonts();
        fontChanged(null);
        loading = false;
    }

    /**
     * load system font to the font combo
     */
    private void loadFonts() {
        jComboBox1.setModel(new DefaultComboBoxModel(ctps.getAvailableFonts()));
        if (ctps.ft != null) {
            jComboBox1.setSelectedItem(ctps.ft.getFamily());
            jSpinner1.setValue(ctps.ft.getSize());
        }
    }

    /**
     * TODO find a better fix for this or remove completely
     */
    private boolean hasStyle(int color) {
        /*
         * dirty fix since I recycle the coloredTextPaneserializable in imageEditorFrame
         */
        return false;
        //return styles.contains(color);
    }

    /**
     * Creates a new color for the styledDoc
     *
     * @param color
     */
    public final void createNewStyle(int color) {
        Style style = jTextPane1.addStyle(createStyleNameFromColor(color), null);
        StyleConstants.setForeground(style, new Color(color));
        styles.add(style.getName());
        styles.add(color);
    }

    /**
     * Creates new form ColoredTextPane
     */
    public ColoredTextPane() {
        ctps = new ColoredTextPaneSerializable();
        initComponents();
        loadFonts();
        enableFrameSelection(false);
    }

    /**
     * Recereate interface
     */
    public void reload() {
        initComponents();
    }

    /**
     *
     * @return true if the background color should be applied to the frame
     * border (otherwise is applied to the inside of the frame)
     */
    public boolean isFrame() {
        return jRadioButton5.isSelected();
    }

    /**
     * Defines if the Frame selection mode should be enabled or not
     *
     * @param value
     */
    public final void enableFrameSelection(boolean value) {
        jLabel1.setVisible(value);
        jRadioButton4.setVisible(value);
        jRadioButton5.setVisible(value);
    }

    /**
     * Sets the title of the ColoredTextPane (the title may be displayed in some
     * case)
     *
     * @param title
     */
    public void setTitle(String title) {
        ctps.setTitle(title);
        if (title != null) {
            Border lineBorder = BorderFactory.createTitledBorder(title);
            this.setBorder(lineBorder);
        } else {
            this.setBorder(null);
        }

    }

    /**
     * The software knows that something hs changed but the styled doc may not
     * have detected it, we therefore force the update by faking that we moved
     * the selection
     */
    public void FakeMoveSelection() {
        int pos = jTextPane1.getCaretPosition();
        int begin = jTextPane1.getSelectionStart();
        int end = jTextPane1.getSelectionEnd();
        jTextPane1.setCaretPosition(0);
        try {
            jTextPane1.setCaretPosition(1);
            jTextPane1.setCaretPosition(getText().length() - 1);
        } catch (Exception e) {
        }
        updateString(null);
        jTextPane1.setCaretPosition(pos);
        jTextPane1.setSelectionStart(begin);
        jTextPane1.setSelectionEnd(end);
    }

    /**
     * Applies various attributes to the current CharacterAttributes
     *
     * @param pos initial position
     * @param parameterName could be bold, ....
     * @param onOff turn the attribut on or off
     */
    public void setCurrentCharacterAttribute(int pos, String parameterName, boolean onOff) {
        if (!onOff) {
            if (pos != -1) {
                ctps.doc.setCharacterAttributes(pos, 1, jTextPane1.getStyle(parameterName), false);
            } else {
                ctps.doc.setParagraphAttributes(pos, 1, jTextPane1.getStyle(parameterName), false);
                FakeMoveSelection();
            }
        } else {
            if (pos != -1) {
                ctps.doc.setCharacterAttributes(pos, 1, jTextPane1.getStyle(ctps.NOT + parameterName), false);
            } else {
                ctps.doc.setParagraphAttributes(pos, 1, jTextPane1.getStyle(ctps.NOT + parameterName), false);
                FakeMoveSelection();
            }
        }
    }

    /**
     *
     * @return the current styledDoc
     */
    public StyledDocument getDoc() {
        return ctps.doc;
    }

    public void setDoc(StyledDocument doc) {
        /*
         * TODO maybe format the doc maybe remove listeners
         */
        doc = doc == null ? new DefaultStyledDocument() : doc;
        ctps.setDoc(doc);
        ctps.getReadyForSerialization();
        jTextPane1.setDocument(doc);
    }

    public ColoredTextPaneSerializable getColoredTextPaneSerializable() {
        return ctps;
    }

    /**
     *
     * @return the current text (without formatting)
     */
    public String getText() {
        return jTextPane1.getText();
    }

    /**
     *
     * @param g2d current graphics2D context
     * @return the 2D bounds of the text in order to be able to position it
     * properly
     */
    public Rectangle2D getTextBounds2D(Graphics2D g2d) {
        Rectangle2D masterrect;
        float x = 0;
        float y = 0;
        ArrayList<AttributedString> multiline_text = ctps.createAttributedString();
        if (multiline_text.isEmpty()) {
            return new Rectangle2D.Double();
        }
        double min_x = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_y = Integer.MIN_VALUE;
        for (Object attributedString : multiline_text) {
            AttributedCharacterIterator aci = ((AttributedString) attributedString).getIterator();
            if (aci == null || aci.getRunLimit() == 0) {
                continue;
            }
            TextLayout tl = new TextLayout(aci, g2d.getFontRenderContext());
            y += tl.getAscent();
            Rectangle test = tl.getPixelBounds(g2d.getFontRenderContext(), x, y);
            min_x = Math.min(min_x, test.x);
            max_x = Math.max(max_x, test.x + test.width);
            min_y = Math.min(min_y, test.y);
            max_y = Math.max(max_y, test.y + test.height);
            y += tl.getDescent() + tl.getLeading();
        }
        if (min_x == Integer.MAX_VALUE) {
            min_x = 0;
        }
        if (max_x == Integer.MIN_VALUE) {
            max_x = 0;
        }
        if (min_y == Integer.MAX_VALUE) {
            min_y = 0;
        }
        if (max_y == Integer.MIN_VALUE) {
            max_y = 0;
        }
        masterrect = new Rectangle2D.Double(min_x, min_y, max_x, max_y);
        return masterrect;
    }

    /**
     *
     * @return the font if font settings are the same for all letters, null
     * otherwise
     */
    public Font isTextSimple() {
        Font outputFont;
        int begin = 0;
        int end = jTextPane1.getText().length();
        MyFontTool fontTool = new MyFontTool();
        for (int i = begin; i < end; i++) {
            Element el = ctps.doc.getCharacterElement(i);
            AttributeSet as = el.getAttributes();
            if (ctps.isSubscript(as) || ctps.isSuperscript(as)) {
                return null;
            }
            if (ctps.isBold(as)) {
                if (fontTool.hasFontStyle()) {
                    if (!fontTool.isSameStyle(Font.BOLD)) {
                        return null;
                    }
                } else {
                    fontTool.setFontStyle(Font.BOLD);
                }
            }
            if (ctps.isItalic(as)) {
                if (fontTool.hasFontStyle()) {
                    if (!fontTool.isSameStyle(Font.ITALIC)) {
                        return null;
                    }
                } else {
                    fontTool.setFontStyle(Font.ITALIC);
                }
            }
            if (ctps.isUnderline(as)) {
                return null;
            }
            Color fgCol = ctps.getFgColor(as);
            if (fontTool.hasFontColor()) {
                if (!fontTool.isSameColor(fgCol)) {
                    return null;
                }
            } else {
                fontTool.setFontColor(fgCol);
            }
            int font_size = ctps.getFontSize(as);
            if (fontTool.hasFontSize()) {
                if (!fontTool.isSameSize(font_size)) {
                    return null;
                }
            } else {
                fontTool.setFontSize(font_size);
            }
            String font_family = ctps.getFontFamily(as);
            if (fontTool.hasFontFamily()) {
                if (!fontTool.isSameFamily(font_family)) {
                    return null;
                }
            } else {
                fontTool.setFontFamily(font_family);
            }
        }
        outputFont = fontTool.generateFont();
        return outputFont;
    }

    /**
     * Applies attribute to the text
     *
     * @param MODE attribute to apply
     * @param begin first character we must change the attributes
     * @param end last character we must change the attributes
     * @param shouldForce if true the new attributes must replace existing
     * attributes
     * @param forcedMode if true replaces the attributes
     */
    public void setSelection(String MODE, int begin, int end, boolean shouldForce, boolean forcedMode) {
        if (end - begin == 0) {
            setCurrentCharacterAttribute(-1, MODE, false);
        }
        for (int i = begin; i < end; i++) {
            boolean value;
            Element el = ctps.doc.getCharacterElement(i);
            AttributeSet as = el.getAttributes();
            if (MODE.equals(ColoredTextPaneSerializable.BOLD)) {
                if (shouldForce) {
                    value = forcedMode;
                } else {
                    value = ctps.isBold(as);
                }
                setCurrentCharacterAttribute(i, MODE, value);
            } else if (MODE.equals(ColoredTextPaneSerializable.ITALIC)) {
                if (shouldForce) {
                    value = forcedMode;
                } else {
                    value = ctps.isItalic(as);
                }
                setCurrentCharacterAttribute(i, MODE, value);
            } else if (MODE.equals(ColoredTextPaneSerializable.SUBSCRIPT)) {
                if (shouldForce) {
                    value = forcedMode;
                } else {
                    value = ctps.isSubscript(as);
                }
                setCurrentCharacterAttribute(i, MODE, value);
            } else if (MODE.equals(ColoredTextPaneSerializable.SUPERSCRIPT)) {
                if (shouldForce) {
                    value = forcedMode;
                } else {
                    value = ctps.isSuperscript(as);
                }
                setCurrentCharacterAttribute(i, MODE, value);
            } else if (MODE.equals(ColoredTextPaneSerializable.UNDERLINED)) {
                if (shouldForce) {
                    value = forcedMode;
                } else {
                    value = ctps.isUnderline(as);
                }
                setCurrentCharacterAttribute(i, MODE, value);
            }
        }
        ctps.createAttributedString();
        jTextPane1.setCaretPosition(jTextPane1.getCaretPosition());
    }

    /**
     * Applies attributes to the current selection
     *
     * @param MODE
     */
    public void setSelection(String MODE) {
        int begin = jTextPane1.getSelectionStart();
        int end = jTextPane1.getSelectionEnd();
        setSelection(MODE, begin, end, false, false);
        jTextPane1.setSelectionStart(begin);
        jTextPane1.setSelectionEnd(end);
    }

    /**
     * Applies a font to the text
     *
     * @param ft font
     */
    public void setFontToAllText(Font ft) {
        if (ft != null) {
            jTextPane1.setFont(ft);
            ctps.ft = ft;
            int begin = 0;
            int end = getText().length();
            if (ft.isBold()) {
                setSelection(ColoredTextPaneSerializable.BOLD, begin, end, true, false);
            } else {
                setSelection(ColoredTextPaneSerializable.BOLD, begin, end, true, true);
            }
            if (ft.isItalic()) {
                setSelection(ColoredTextPaneSerializable.ITALIC, begin, end, true, false);
            } else {
                setSelection(ColoredTextPaneSerializable.ITALIC, begin, end, true, true);
            }
        }
    }

    /**
     * Italicizes the selection
     */
    public void setSelectionItalic() {
        setSelection(ColoredTextPaneSerializable.ITALIC);
    }

    /**
     * Makes the selection bold
     */
    public void setSelectionBold() {
        setSelection(ColoredTextPaneSerializable.BOLD);
    }

    /**
     * Subscripts the selection
     */
    public void setSelectionSubscript() {
        setSelection(ColoredTextPaneSerializable.SUBSCRIPT);
    }

    /**
     * Supercripts the selection
     */
    public void setSelectionSupercript() {
        setSelection(ColoredTextPaneSerializable.SUPERSCRIPT);
                    }

    /**
     * underlines the selection
     */
    public void setSelectionUnderlined() {
        setSelection(ColoredTextPaneSerializable.UNDERLINED);
    }

    /**
     *
     * @param color
     * @return a name for the style containing the color (the name of the style
     * is directly derived from the color used to generate it)
     */
    public String createStyleNameFromColor(int color) {
        return CommonClassesLight.getHtmlColor(color);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jComboBox1 = new javax.swing.JComboBox();
        jSpinner1 = new javax.swing.JSpinner();
        paintedButton1 = new Commons.PaintedButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        if (jTextPane1 == null)
        jTextPane1 = new javax.swing.JTextPane(ctps.doc){
            @Override
            public void paste() {
                try {
                    getStyledDocument().remove(getSelectionStart(), getSelectionEnd() - getSelectionStart());
                } catch (Exception e) {
                }
                String serializedStyledDocumentContent = CommonClassesLight.getTextFromClipBoard();
                if (serializedStyledDocumentContent != null) {
                    StyledDoc2Html test = new StyledDoc2Html();
                    setStyledDocument(StyledDocTools.insert(test.reparse(serializedStyledDocumentContent), getStyledDocument(), getSelectionStart()));
                }
                FakeMoveSelection();
            }

            @Override
            public void copy() {
                StyledDoc2Html test = new StyledDoc2Html();
                String serializedStyledDocumentContent = test.convertStyledDocToHtml(getStyledDocument(), getSelectionStart(), getSelectionEnd()/*, textBgColor*/);
                CommonClassesLight.sendTextToClipboard(serializedStyledDocumentContent);
            }

            @Override
            public void cut() {
                copy();
                try {
                    getStyledDocument().remove(getSelectionStart(), getSelectionEnd() - getSelectionStart());
                } catch (Exception e) {
                }
            }

        };
        ;
        LookAndFeel laf = UIManager.getLookAndFeel();
        if (laf.getID().equals("Nimbus")) {
            Painter bgPainter = new Painter<JComponent>() {
                public void paint(Graphics2D g, JComponent c, int w, int h) {
                }
            };
            UIDefaults tdef = new UIDefaults();
            tdef.put("TextPane[Enabled].backgroundPainter", bgPainter);
            jTextPane1.putClientProperty("Nimbus.Overrides", tdef);
        }
        if (ctps.getTextBgColor() != null) {
            jTextPane1.setBackground(ctps.getTextBgColor());
        } else {
            jTextPane1.setBackground(new Color(128, 128, 128));
        }

        setBorder(javax.swing.BorderFactory.createTitledBorder("Title"));
        setMinimumSize(new java.awt.Dimension(703, 232));
        setPreferredSize(new java.awt.Dimension(703, 232));
        setRequestFocusEnabled(false);

        jComboBox1.setMaximumSize(new java.awt.Dimension(32767, 37));
        jComboBox1.setMinimumSize(new java.awt.Dimension(250, 37));
        jComboBox1.setPreferredSize(new java.awt.Dimension(220, 37));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontChanged(evt);
            }
        });
        add(jComboBox1);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.setMaximumSize(new java.awt.Dimension(60, 37));
        jSpinner1.setMinimumSize(new java.awt.Dimension(60, 37));
        jSpinner1.setPreferredSize(new java.awt.Dimension(60, 37));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeChanged(evt);
            }
        });
        add(jSpinner1);

        paintedButton1.setBackground(new java.awt.Color(255, 255, 255));
        paintedButton1.setForeground(new java.awt.Color(0, 0, 0));
        paintedButton1.setText("text color");
        paintedButton1.setFocusable(false);
        paintedButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        paintedButton1.setMaximumSize(new java.awt.Dimension(120, 37));
        paintedButton1.setMinimumSize(new java.awt.Dimension(100, 37));
        paintedButton1.setPreferredSize(new java.awt.Dimension(120, 37));
        paintedButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        paintedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintedButton1ActionPerformed(evt);
            }
        });
        add(paintedButton1);

        jButton1.setText("<html><b>B</b></html>");
        jButton1.setBorderPainted(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(2147483647, 37));
        jButton1.setMinimumSize(new java.awt.Dimension(22, 37));
        jButton1.setPreferredSize(new java.awt.Dimension(40, 37));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);

        jButton4.setText("<html><i>I</i></html>");
        jButton4.setBorderPainted(false);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(2147483647, 37));
        jButton4.setMinimumSize(new java.awt.Dimension(16, 37));
        jButton4.setPreferredSize(new java.awt.Dimension(40, 37));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        add(jButton4);

        jButton5.setText("<html>sub<sub>script</sub></html>");
        jButton5.setBorderPainted(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMinimumSize(new java.awt.Dimension(70, 37));
        jButton5.setPreferredSize(new java.awt.Dimension(90, 37));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        add(jButton5);

        jButton6.setText("<html>super<sup>script</sup></html>");
        jButton6.setBorderPainted(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMaximumSize(new java.awt.Dimension(2147483647, 37));
        jButton6.setMinimumSize(new java.awt.Dimension(100, 37));
        jButton6.setPreferredSize(new java.awt.Dimension(100, 37));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        add(jButton6);

        jButton3.setText('\u03B1'+ "" + '\u03B2'+ "" + '\u03B3'+ "");
        jButton3.setBorderPainted(false);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(99, 37));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 37));
        jButton3.setPreferredSize(new java.awt.Dimension(70, 37));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("no bg");
        jRadioButton1.setFocusable(false);
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformedChanged(evt);
            }
        });
        add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Black bg");
        jRadioButton2.setFocusable(false);
        jRadioButton2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformedChanged(evt);
            }
        });
        add(jRadioButton2);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("White bg");
        jRadioButton3.setFocusable(false);
        jRadioButton3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRadioButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformedChanged(evt);
            }
        });
        add(jRadioButton3);

        jLabel1.setText("Bg type:");
        add(jLabel1);

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Filled rect");
        jRadioButton4.setFocusable(false);
        jRadioButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameOrRect(evt);
            }
        });
        add(jRadioButton4);

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Empty rect");
        jRadioButton5.setFocusable(false);
        jRadioButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameOrRect(evt);
            }
        });
        add(jRadioButton5);

        jTextPane1.setPreferredSize(new java.awt.Dimension(680, 110));
        jTextPane1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                updateString(evt);
            }
        });
        jTextPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ColoredTextPane.this.mouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPane1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setSelectionBold();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        setSelectionItalic();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        setSelectionSubscript();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setSelectionSupercript();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        GreekLetterSelector iopane = new GreekLetterSelector();
        /*
         * this.getRootPane() is a bug fix to get the window to be centered over the UI
         */
        int result = JOptionPane.showOptionDialog(this.getRootPane(), new Object[]{iopane}, "Select A Greek Letter", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String greek = iopane.getGreekLetter();
            try {
                ctps.doc.insertString(jTextPane1.getCaretPosition(), greek, null);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void paintedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintedButton1ActionPerformed
        PaintedButton tmp = (PaintedButton) (evt.getSource());
        Color color = JColorChooser.showDialog(this, "Pick a Color", tmp.getBackground());
        if (color != null) {
            tmp.setColor(color);
            colorSelection(tmp.getColor());
        }
    }//GEN-LAST:event_paintedButton1ActionPerformed

    /**
     *
     * @return the text font size
     */
    public int getFontSize() {
        return (((Integer) jSpinner1.getValue()).intValue());
    }

    /**
     *
     * @param ft the font
     */
    public void applyFont(Font ft) {
        jComboBox1.setSelectedItem(ft.getFamily());
        jSpinner1.setValue(ft.getSize());
        ctps.ft = ft;
        if (ft.isItalic()) {
            ctps.setItalic();
        }
        if (ft.isBold()) {
            ctps.setBold();
        }
        fontChanged(null);
    }

    private void fontChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontChanged
        if (loading) {
            return;
        }
        jTextPane1.setFont(new Font(jComboBox1.getSelectedItem().toString(), Font.PLAIN, getFontSize()));
        Style style = jTextPane1.addStyle("font", null);
        ctps.ft = jTextPane1.getFont();
        StyleConstants.setFontFamily(style, ctps.ft.getFamily());
        StyleConstants.setFontSize(style, ctps.ft.getSize());
        ctps.doc.setCharacterAttributes(0, jTextPane1.getText().length(), style, false);
        ctps.doc.setLogicalStyle(jTextPane1.getText().length(), style);
        ctps.doc.setParagraphAttributes(0, jTextPane1.getText().length(), style, false);
        jTextPane1.setParagraphAttributes(style, false);
        jTextPane1.setLogicalStyle(style);
        jTextPane1.setCharacterAttributes(style, false);
        //jTextPane1.updateUI();
        jTextPane1.validate();
        FakeMoveSelection();
    }//GEN-LAST:event_fontChanged

    private void fontSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeChanged
        if (loading) {
            return;
        }
        fontChanged(null);
    }//GEN-LAST:event_fontSizeChanged

    private void updateString(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_updateString
        /*
         * the text may have changed --> we update it
         */
        ctps.createAttributedString();
    }//GEN-LAST:event_updateString

    private void buttonActionPerformedChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformedChanged
        if (jRadioButton2.isSelected()) {
            ctps.setTextBgColor(new Color(0, 0, 0));
            jTextPane1.setCaretColor(Color.WHITE);
        } else if (jRadioButton3.isSelected()) {
            ctps.setTextBgColor(new Color(255, 255, 255));
            jTextPane1.setCaretColor(Color.BLACK);
        } else if (jRadioButton1.isSelected()) {
            ctps.setTextBgColor(null);
            jTextPane1.setCaretColor(Color.BLACK);
        }
        if (ctps.getTextBgColor() != null) {
            jTextPane1.setBackground(ctps.getTextBgColor());
        } else {
            jTextPane1.setBackground(new Color(128, 128, 128));
        }
        FakeMoveSelection();
    }//GEN-LAST:event_buttonActionPerformedChanged

    private void frameOrRect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameOrRect
        ctps.setFrame(jRadioButton5.isSelected());
    }//GEN-LAST:event_frameOrRect

    private void mouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseEntered
        /*
         * somehow the cursor does not show by default so we force it
         */
        jTextPane1.getCaret().setVisible(true);
        jTextPane1.getCaret().setSelectionVisible(true);
        jTextPane1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_mouseEntered

    /**
     * Draws the current text in g2d at position x,y
     *
     * @param g2d
     * @param x
     * @param y
     * @return true if the text was succesfully drawn
     */
    public boolean draw(Graphics2D g2d, float x, float y) {
        Font simpleTextParameters = isTextSimple();
        if (simpleTextParameters != null) {
            return false;
        } else {
            ArrayList<AttributedString> multiline_text = ctps.createAttributedString();
            if (!multiline_text.isEmpty()) {
                for (Object attributedString : multiline_text) {
                    AttributedCharacterIterator aci = ((AttributedString) attributedString).getIterator();
                    if (aci == null || aci.getRunLimit() == 0) {
                        continue;
                    }
                    TextLayout tl = new TextLayout(aci, g2d.getFontRenderContext());
                    y += tl.getAscent();
                    tl.draw(g2d, x, y);
                    y += tl.getDescent() + tl.getLeading();
                }
                return true;
            }
            return false;
        }
    }

    /**
     *
     * @return true if the textpane conbtains text
     */
    public boolean hasText() {
        boolean hasText = !jTextPane1.getText().equals("") ? true : false;
        return hasText;
    }

    /**
     *
     * @return a new ColoredTextPaneSerializable
     */
    public ColoredTextPaneSerializable export() {
        return new ColoredTextPaneSerializable(ctps.doc, ctps.getTitle(), ctps.ft, ctps.textBgColor, ctps.isFrame);
    }

    /**
     *
     * @return the length of the current text selection
     */
    public int getSelectionLength() {
        return jTextPane1.getSelectionEnd() - jTextPane1.getSelectionStart();
    }

    /**
     * Sets the current caret listener to 'care'
     *
     * @param care
     */
    public void addCaretListener(CaretListener care) {
        removeListeners();
        jTextPane1.addCaretListener(care);
    }

    /**
     * supresses all caretListeners
     */
    public void removeListeners() {
        CaretListener[] cares = jTextPane1.getCaretListeners();
        for (CaretListener caretListener : cares) {
            jTextPane1.removeCaretListener(caretListener);
        }
    }

    /**
     * Handles caret update events (can be overriden if necessary)
     *
     * @param e
     */
    public void caretUpdate(CaretEvent e) {
        updateString(e);
    }

    /**
     * Changes the color of the selected text
     *
     * @param color
     */
    public void colorSelection(int color) {
        if (!hasStyle(color)) {
            createNewStyle(color);
        }
        /*
         * now we apply the style
         */

        if (getSelectionLength() == 0) {
            /*
             * we set it to false cause we don't want to replace the other attributes of the selected characters
             */
            ctps.doc.setParagraphAttributes(jTextPane1.getSelectionStart(), getSelectionLength(), jTextPane1.getStyle(CommonClassesLight.getHtmlColor(color)), false);
        } else {
            int begin = jTextPane1.getSelectionStart();
            int end = jTextPane1.getSelectionEnd();
            ctps.doc.setCharacterAttributes(jTextPane1.getSelectionStart(), getSelectionLength(), jTextPane1.getStyle(CommonClassesLight.getHtmlColor(color)), false);
            /*
             * bug fix to get the correct color when updating the letter
             */
            if (begin == 0 && end == ctps.doc.getLength()) {
                ctps.doc.setParagraphAttributes(jTextPane1.getSelectionStart(), getSelectionLength(), jTextPane1.getStyle(CommonClassesLight.getHtmlColor(color)), false);
            }
            jTextPane1.setCaretPosition(jTextPane1.getCaretPosition());
            jTextPane1.setSelectionStart(begin);
            jTextPane1.setSelectionEnd(end);
        }
    }

    /**
     * Used to test the function
     *
     * @param args
     */
    public static void main(String[] args) {
        String operating_system = System.getProperty("os.name");
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            boolean found = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("control", new Color(230, 230, 230));
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (!operating_system.contains("inu")) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } else {
                    //le look n feel par defaut sous linux est trop laid --> on prd GTK a la place
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to load L&F:");
            System.out.println(ex);
        }
        ColoredTextPane iopane = new ColoredTextPane("tototo");
        iopane.setFontToAllText(new Font("Comic Sans Ms", Font.ITALIC | Font.BOLD, 24)); //--> ca marche
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "test", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            SaverLight.saveObject(iopane, "/home/benoit/Bureau/test.y5fm");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextPane jTextPane1;
    private Commons.PaintedButton paintedButton1;
    // End of variables declaration//GEN-END:variables
}



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

import java.awt.Color;
import java.awt.Font;

/**
 * MyFontTool contains all default font settings and some extra settings such as
 * font color
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public class MyFontTool {

    private String fontFamily;
    private int fontSize = -1;
    private int fontStyle = -1;
    private Color fontColor;

    /**
     * Constructor
     *
     * @param fontFamily
     * @param fontStyle
     * @param fontSize
     * @param fontColor
     */
    public MyFontTool(String fontFamily, int fontStyle, int fontSize, Color fontColor) {
        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
    }

    /**
     * Empty constructor
     */
    public MyFontTool() {
    }

    /**
     *
     * @return the font family
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * Sets the font family
     *
     * @param fontFamily
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     *
     * @return the font size
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size
     *
     * @param fontSize
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     *
     * @return the font style
     */
    public int getFontStyle() {
        return fontStyle;
    }

    /**
     * Sets the font style
     *
     * @param fontStyle
     */
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     *
     * @return the font color
     */
    public Color getFontColor() {
        return fontColor;
    }

    /**
     * Sets the font color
     *
     * @param fontColor
     */
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    /**
     *
     * @return true if a font style has been set
     */
    public boolean hasFontStyle() {
        return fontStyle != -1;
    }

    /**
     *
     * @return true if a font size has been set
     */
    public boolean hasFontSize() {
        return fontSize != -1;
    }

    /**
     *
     * @return true if a font family has been set
     */
    public boolean hasFontFamily() {
        return fontFamily != null;
    }

    /**
     *
     * @return true if a font color has been set
     */
    public boolean hasFontColor() {
        return fontColor != null;
    }

    /**
     * TODO
     *
     * @return true if the font is valid on the system
     */
    public boolean isFontValid() {
        throw new Error("not supported yet!");
        /*
         * we check that the font exists
         * not necessary yet but would be cool in the future
         */
    }

    /**
     *
     * @param style
     * @return true if the style 'style' equals fontStyle
     */
    public boolean isSameStyle(int style) {
        return fontStyle == style ? true : false;
    }

    /**
     *
     * @param size
     * @return true if the size 'size' equals fontSize
     */
    public boolean isSameSize(int size) {
        return fontSize == size ? true : false;
    }

    /**
     *
     * @param family
     * @return true if the font family 'family' equals fontFamily
     */
    public boolean isSameFamily(String family) {
        return fontFamily.equalsIgnoreCase(family) ? true : false;
    }

    /**
     *
     * @param color
     * @return true if the font color 'color' equals fontColor
     */
    public boolean isSameColor(Color color) {
        return fontColor == color ? true : false;
    }

    /**
     *
     * @return a new Font out of the font parameters
     */
    public Font generateFont() {
        if (fontStyle == -1) {
            fontStyle = Font.PLAIN;
        }
        if (fontSize == -1 || fontFamily == null) {
            return null;
        }
        return new Font(fontFamily, fontStyle, fontSize);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        if (true) {
            System.out.println(Font.PLAIN + " " + Font.ITALIC + " " + Font.BOLD);
            return;
        }

        long start_time = System.currentTimeMillis();
        MyFontTool test = new MyFontTool();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



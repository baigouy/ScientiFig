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

//TODO get rid of this when I have time/recode it in a better and simpler way
package MyShapes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * MyFormattedString is a tool to measure and draw Strings
 *
 * @since <B>Packing Analyzer 8.1</B>
 * @author Benoit Aigouy
 */
public class MyFormattedString extends Font {

    /**
     * Variables
     */
    public static final long serialVersionUID = 187609976208769265L;
    String text;
    Map<AttributedCharacterIterator.Attribute, Object> attributes;

    /**
     * Constructor
     *
     * @param ft font
     */
    public MyFormattedString(Font ft) {
        super(ft);
    }

    /**
     * Constructor
     *
     * @param text a string
     * @param attributes text attributes
     */
    public MyFormattedString(String text, Map<AttributedCharacterIterator.Attribute, Object> attributes) {
        super(attributes);
        this.attributes = attributes;
        this.text = text;
    }

    /**
     * Constructor
     *
     * @param text a string
     * @param ft font
     */
    public MyFormattedString(String text, Font ft) {
        super(ft);
        this.text = text;
    }

    /**
     *
     * @return the text foreground color
     */
    public Color getFgColor() {
        if (attributes != null && attributes.containsKey(TextAttribute.FOREGROUND)) {
            return (Color) attributes.get(TextAttribute.FOREGROUND);
        }
        return Color.BLACK;
    }

    /**
     *
     * @return the unformatted text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the MyFormattedString object
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @param g2d
     * @param x
     * @param y
     * @return a bounding rectangle for the current text object
     */
    public Rectangle2D draw(Graphics2D g2d, float x, float y) {
        g2d.setFont(this);
        g2d.drawString(text, x, y);
        /*
         * TODO check if necessary cause it appears very complex to do it this way
         */
        return getBounds2D(g2d);
    }

    /**
     *
     * @param g2d
     * @param x
     * @param y
     * @return a bounding rectangle for the current text object after rotation
     * by 90° (vertical text)
     */
    public Rectangle2D draw90(Graphics2D g2d, float x, float y) {
        AffineTransform fat = new AffineTransform();
        fat.rotate(Math.toRadians(-90));
        g2d.setFont(this.deriveFont(fat));
        g2d.drawString(text, x, y);
        return getBounds2D(g2d);
    }

    /**
     *
     * @param g2d
     * @return the text ascent
     */
    public float getAscent(Graphics2D g2d) {
        if (text.equals("")) {
            return 0;
        }
        return new TextLayout(text, this, g2d.getFontRenderContext()).getAscent();
    }

    /**
     *
     * @param g2d
     * @return the text descent
     */
    public float getDescent(Graphics2D g2d) {
        if (text.equals("")) {
            return 0;
        }
        return new TextLayout(text, this, g2d.getFontRenderContext()).getDescent();
    }

    /**
     *
     * @param g2d
     * @return the text leading space
     */
    public float getLeading(Graphics2D g2d) {
        if (text.equals("")) {
            return 0;
        }
        return new TextLayout(text, this, g2d.getFontRenderContext()).getLeading();
    }

    /**
     *
     * @param g2d
     * @return the text bounds
     */
    public Rectangle2D getBounds2D(Graphics2D g2d) {
        GlyphVector v = this.createGlyphVector(g2d.getFontRenderContext(), text);
        return v.getLogicalBounds();
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



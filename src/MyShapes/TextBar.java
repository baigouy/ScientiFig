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

import Dialogs.ROIpanelLight;
import Commons.SaverLight;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * TextBar is a text side bar that can be horizontal or vertical
 *
 * @author Benoit Aigouy
 */
public abstract class TextBar extends MyRectangle2D implements PARoi, Transformable, Serializable, Incompressible {

    /**
     * Variables
     */
    public static final long serialVersionUID = 187409973208729265L;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public int ORIENTATION = HORIZONTAL;
    public ColoredTextPaneSerializable text;
    public boolean isFrame = true;
    public static final int CENTERED = 0;
    public static final int ALIGN_LEFT = 1;
    //TODO finalize right because it doesn't work especially for vertical
    public static final int ALIGN_RIGHT = 2;
    public int TEXT_POSITION = CENTERED;

    /**
     * a double precision TextBar
     */
    public static class Double extends TextBar implements Serializable {

        public static final long serialVersionUID = 9057588182065555825L;

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param width
         * @param text
         * @param g2d
         * @param orientation
         */
        public Double(double x, double y, double width, ColoredTextPaneSerializable text, Graphics2D g2d, int orientation, int position) {
            this.text = text;
            Rectangle2D bounds = text.getTextBounds(g2d);
            this.ORIENTATION = orientation;
            rec2d = new Rectangle2D.Double(x, y, width, bounds.getY() + 6);
            if (ORIENTATION == VERTICAL) {
                rec2d = new Rectangle2D.Double(x, y, rec2d.height, rec2d.width);
            }
            this.isFrame = text.isFrame();
            this.TEXT_POSITION = position;
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(TextBar.Double myel) {
            this.rec2d = myel.rec2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.isTransparent = myel.isTransparent;
            this.transparency = myel.transparency;
            this.text = myel.text;
            this.ORIENTATION = myel.ORIENTATION;
            this.isFrame = myel.isFrame;
            this.TEXT_POSITION = myel.TEXT_POSITION;
        }

        @Override
        public Object clone() {
            return new TextBar.Double(this).setZpos(ZstackPos);
        }

        /**
         *
         * @param ZstackNb
         * @return a vectorial object with a defined Z stack position
         */
        public Double setZpos(int ZstackNb) {
            this.ZstackPos = ZstackNb;
            return this;
        }
    }

    public int getTEXT_POSITION() {
        return TEXT_POSITION;
    }

    public void setTEXT_POSITION(int TEXT_POSITION) {
        this.TEXT_POSITION = TEXT_POSITION;
    }

    /**
     * recreate the styleDocument from html like code (called on file loading)
     */
    public void recreateStyledDoc() {
        text.recreateStyledDoc();
    }

    /**
     * Prepares the textBar for serialization (called on file saving)
     */
    public void getTextreadyForSerialization() {
        text.getReadyForSerialization();
    }

    /**
     * Apply a journal style to the current textbar
     *
     * @param jp
     */
    public void setJournalStyle(JournalParameters jp) {
        text.setJournalStyle(jp, false, false);
    }

    @Override
    public void drawTransparent(Graphics2D g2d) {
        drawTransparent(g2d, transparency);
    }

    @Override
    public void fillTransparent(Graphics2D g2d) {
        fillTransparent(g2d, transparency);
    }

    @Override
    public boolean isWidthIncompressible() {
        if (ORIENTATION == HORIZONTAL) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isHeightIncompressible() {
        if (ORIENTATION == VERTICAL) {
            return false;
        }
        return true;
    }

    @Override
    public double getIncompressibleWidth() {
        return rec2d.width;
    }

    @Override
    public double getIncompressibleHeight() {
        return rec2d.height;
    }

    /**
     *
     * @return the orientation of the text bar (horizontal / vertical)
     */
    public int getORIENTATION() {
        return ORIENTATION;
    }

    /**
     * Sets the text bar orientation
     *
     * @param ORIENTATION (horizontal or vertical)
     */
    public void setORIENTATION(int ORIENTATION) {
        this.ORIENTATION = ORIENTATION;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1f));
        Color frame_color = text.getTextBgColor();
        if (frame_color != null) {
            g2d.setColor(frame_color);
            if (isFrame) {
                g2d.draw(rec2d);
            } else {
                g2d.fill(rec2d);
            }
        }
        switch (ORIENTATION) {
            case HORIZONTAL:
                if (true) {
                    Rectangle2D bounds = text.getTextBounds(g2d);
                    //ça marche --> centre a gauche on dirait
                    double xPos = 0;
                    switch (TEXT_POSITION) {
                        case CENTERED:
                            xPos = (rec2d.getWidth() - bounds.getX()) / 2.;
                            break;
                        case ALIGN_LEFT:
                            break;
//                        case ALGN_RIGHT:
//                            System.out.println("here " +" "+bounds.getWidth()+ " "+rec2d.getWidth());
//                            xPos = (rec2d.getWidth() - bounds.getWidth());
//                            System.out.println(xPos);
//                            break;
                    }
                    float x = (float) (xPos + rec2d.x); //--> centrage en x
                    float y = (float) (rec2d.getHeight() / 2. - bounds.getY() / 2. + rec2d.y);//rec2d.getHeight() / 2. - bounds.getY() / 2.;
                    float x_backup = x;

                    ArrayList<AttributedString> as = text.createAttributedString();
                    ArrayList<ArrayList<MyFormattedString>> final_text = text.simplify_text(as);

                    int pos = 0;
                    for (ArrayList<MyFormattedString> arrayList : final_text) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        if (aci == null || aci.getRunLimit() == 0) {
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;

                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y + ascent);
                            x += curpos.getWidth();
                        }
                        pos++;
                        x = x_backup;
                        y += ascent + descent + leading;
                    }
                }
                break;
            case VERTICAL:
                if (true) {
                    Rectangle2D bounds = text.getTextBounds(g2d);
                    double xPos = 0;
                    switch (TEXT_POSITION) {
                        case CENTERED:
                            xPos = (rec2d.getHeight() - bounds.getX()) / 2.;
                            break;
                        case ALIGN_LEFT:
                            xPos = rec2d.getHeight() - bounds.getX();
                            break;
                        case ALIGN_RIGHT:
                            break;
                    }
//                    float x = (float) (xPos + rec2d.x); //--> centrage en x
                    float x = (float) ((rec2d.getWidth() / 2. - bounds.getY() / 2.) + rec2d.x);
                    //float y = (float) (rec2d.getHeight() / 2. - bounds.getX() / 2. + rec2d.y + bounds.getX());//rec2d.getHeight() / 2. - bounds.getY() / 2.;
                    float y = (float) (xPos + rec2d.y + bounds.getX());//rec2d.getHeight() / 2. - bounds.getY() / 2.;
                    float x_backup = x;
                    ArrayList<AttributedString> as = text.createAttributedString();
                    ArrayList<ArrayList<MyFormattedString>> final_text = text.simplify_text(as);

                    int pos = 0;
                    for (ArrayList<MyFormattedString> arrayList : final_text) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        if (aci == null || aci.getRunLimit() == 0) {
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;

                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw90(g2d, x + ascent, y);
                            y -= curpos.getWidth();
                        }
                        pos++;
                        x = x_backup;
                        y += ascent + descent + leading;
                    }
                }
                break;
        }
    }

    @Override
    public void scale(double factor) {
        /*
         * by construction we cannot compress it in one direction
         */
        if (isHeightIncompressible()) {
            rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width * factor, rec2d.height);
        } else {
            rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width, rec2d.height * factor);
        }
    }

    @Override
    public String getShapeName() {
        return "String";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.STRING;
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {

        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < test2.getWidth(); i++) {
            for (int j = 0; j < test2.getHeight(); j++) {
                test2.setRGB(i, j, 0xAAAAAA);
            }
        }
        Graphics2D g2d = test2.createGraphics();

        long start_time = System.currentTimeMillis();

        //tt marche nickel --> essayer gandeur nature maintenant 
        //c'est bon ce coup ci ca marche
        //si black bg --> balck rectangle sinon blc si null --> rien
        ColoredTextPaneSerializable coloredTextPaneSerializable = new ColoredTextPaneSerializable("toto");
        coloredTextPaneSerializable.setFontToAllText(new Font("Arial", Font.ITALIC, 50), false, false);
        coloredTextPaneSerializable.setTextBgColor(Color.MAGENTA);
        TextBar.Double test = new TextBar.Double(50, 0, 256, coloredTextPaneSerializable, g2d, HORIZONTAL, TextBar.CENTERED);
        System.out.println(test.isHeightIncompressible() + " " + test.isWidthIncompressible() + " " + test.getIncompressibleWidth() + " " + test.getIncompressibleHeight());
        test.scale(0.5);
        System.out.println(test.isHeightIncompressible() + " " + test.isWidthIncompressible() + " " + test.getIncompressibleWidth() + " " + test.getIncompressibleHeight());
        test.draw(g2d);

        coloredTextPaneSerializable = new ColoredTextPaneSerializable("sdqsdqsdq  sdqsd");
        coloredTextPaneSerializable.setFontToAllText(new Font("Arial", Font.ITALIC, 50), false, false);
        coloredTextPaneSerializable.setTextBgColor(Color.MAGENTA);
        TextBar.Double test3 = new TextBar.Double(0, 0, 256, coloredTextPaneSerializable, g2d, VERTICAL, TextBar.CENTERED);
        System.out.println(test3.isHeightIncompressible() + " " + test3.isWidthIncompressible() + " " + test3.getIncompressibleWidth() + " " + test3.getIncompressibleHeight());
        test3.scale(0.5);
        System.out.println(test3.isHeightIncompressible() + " " + test3.isWidthIncompressible() + " " + test3.getIncompressibleWidth() + " " + test3.getIncompressibleHeight());
        test3.draw(g2d);

        g2d.dispose();

        SaverLight.save(test2, "/home/benoit/toto.png");

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

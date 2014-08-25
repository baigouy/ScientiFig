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
import Commons.CommonClassesLight;
import Commons.G2dParameters;
import Commons.SaverLight;
import ij.gui.PointRoi;
import ij.gui.Roi;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * MyPoint2D is a vectorial object representing a point, it builds upon
 * myEllipse2D
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyPoint2D extends MyEllipse2D implements Serializable {

    public static final long serialVersionUID = 2483512266450860717L;
    ColoredTextPaneSerializable text; //--> reflechir à comment faire ça proprement en fait mais devrait pas etre trop dur
    /**
     * @deprecated please use setText("*") instead
     */
    boolean isAsterisk = false;

    /**
     * a double precision MyPoint2D
     */
    public static class Double extends MyPoint2D implements Serializable {

        /**
         * Variables
         */
        public static final long serialVersionUID = -7663519835012699241L;

        /**
         * Constructor
         *
         * @param x x coordinates of the point
         * @param y y coordinates of the point
         */
        public Double(double x, double y) {
            el2d = new Ellipse2D.Double(x - 0.5, y - 0.5, 1, 1);
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Point2D l2d) {
            this(l2d.getX(), l2d.getY());
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Point2D.Double l2d) {
            this.el2d = new Ellipse2D.Double(l2d.x - 0.5, l2d.y - 0.5, 1, 1);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyPoint2D.Double myel) {
            this.el2d = myel.el2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.isTransparent = myel.isTransparent;
            this.transparency = myel.transparency;
            this.isAsterisk = myel.isAsterisk;
            this.text = myel.text;
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

        /**
         * Constructor
         *
         * @param p
         */
        public Double(Polygon p) {
            this(p.xpoints[0], p.ypoints[0]);
        }

        @Override
        public Object clone() {
            //return new MyPoint2D.Double(this);
            MyPoint2D.Double clone = new MyPoint2D.Double(this);
                        /**
             * bug fix for duplicated text
             */
            if (clone.text != null)
            {
//        StyledDoc2Html test = new StyledDoc2Html();
////        myel.text.getReadyForSerialization();
//        clone.text = new ColoredTextPaneSerializable(test.reparse(clone.text.getFormattedText()), clone.text.getTitle());
                clone.text = clone.text.clone();
               }
            
            return clone;
        }
    }

    public void setJournalStyle(JournalParameters jp) {
        if (jp != null && text != null) {
            text.setFontToAllText(jp.getOutterTextFont(), false, false);
        }
    }

    public void recreateStyledDoc() {
        if (text != null) {
            text.recreateStyledDoc();
        }
    }

    public void getReadyForSerialization() {
        if (text != null) {
            text.getReadyForSerialization();
        }
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
    public void setTextAndBgColor(int color, Color bgColor, Font ft, boolean change_color, boolean change_bg_color, boolean change_ft) {
        if (text != null) {
            text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, false, false);
        }
    }

    /**
     * The point class can also be used to dispaly text
     *
     * @return the text associated to this object
     */
    public ColoredTextPaneSerializable getText() {
        return text;
    }

    /**
     * Sets the text to be centered on the position of the point
     */
    public void setText(ColoredTextPaneSerializable text) {
        this.text = text;
    }

    /**
     *
     * @return true if the point should behave as an asterisk
     * @deprecated use getText instead
     */
    public boolean getIsAsterisk() {
        return isAsterisk;
    }

    /**
     * defines if the point should be treated as an asterisk
     *
     * @param isAsterisk
     * @deprecated use setText instead
     */
    public void setIsAsterisk(boolean isAsterisk) {
        this.isAsterisk = isAsterisk;
    }

    @Override
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    @Override
    public boolean contains(Point2D p) {
        Rectangle r = el2d.getBounds();
        Rectangle r_bigger = new Rectangle(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
        return r_bigger.contains(p);
    }

    @Override
    public boolean contains(Rectangle2D r2) {
        Rectangle r = el2d.getBounds();
        Rectangle r_bigger = new Rectangle(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
        return r_bigger.contains(r2);
    }

    @Override
    public boolean contains(double x, double y) {
        return contains(new Point2D.Double(x, y));
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        Rectangle r = el2d.getBounds();
        Rectangle r_bigger = new Rectangle(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
        return r_bigger.contains(x, y, w, h);
    }

    //
    //    /**
    //     *
    //     * @return the real coordinates of the point
    //     */
    //    @Override
    //    public Point2D.Double getPoint() {
    //    }
    //    }
    @Override
    public boolean isRotable() {
        return false;
    }

    /**
     *
     * @return the real coordinates of the point
     */
    public Point getPoint1D() {
        return new Point((int) (el2d.x + el2d.width / 2.), (int) (el2d.y + el2d.height / 2.));
    }

    @Override
    public double getArea() {
        return 1.;
    }

    @Override
    public Polygon getPolygon() {
        int[] x = new int[1];
        int[] y = new int[1];
        x[0] = (int) getX();
        y[0] = (int) getY();
        return new Polygon(x, y, x.length);
    }

    @Override
    public void draw(Graphics2D g2d) {
//        System.out.println(text);
        if (!isAsterisk && text == null) {
            super.draw(g2d);
        } else {
            //le mieux serait de mettre un coloredtext pane serializable afin que je puisse l'editer et connaitre sa taille, etc

//            if (text != null) {
//                txt = text;
//            }
            G2dParameters g2dparams = new G2dParameters(g2d);
            if (text == null) {
                g2d.setColor(new Color(color));
                g2d.setStroke(new BasicStroke(strokeSize));
                g2d.setFont(new Font("Arial", 0, 12));//--> la aussi il me faudrait connaitre la font
                //en fait supprimer les asterisks et les remplacer par du texte
                String txt = "*";
                Dimension txtDims = CommonClassesLight.getTextSize(g2d, txt);
                el2d = new Ellipse2D.Double(getPoint().x - txtDims.getWidth() / 2., getPoint().y - txtDims.height / 2., txtDims.width, txtDims.height);
                //trouver exactement comment faire ca et replacer le truc par un coloredtextpane serializable afin de pas avoir de pbs
                g2d.drawString(txt, (float) (getPoint().x - (float) txtDims.width / 2.), (float) (getPoint().y + (float) txtDims.height / 2.));
            } else {
                //TODO en profiter pour updater le bounding rect qui serait un peu trop petit autrement --> car on n'est pas un point
                //maintenant la position du texte est parfaite --> tout gérer avec ça et voir comment intégrer du texte aux images mais je pense pas que ce soit trop dur
                //en fait c'est tout con il suffit de centrer le centre de la bounding box du truc sur le point clique par le gars --> en fait c'est assez simple a faire je pense
                //--> pas mal mais pas centre sur le point --> juste gerer ca et puis tt sera fini
                ArrayList<AttributedString> as = text.createAttributedString();
                Rectangle2D.Double bounds;
                ArrayList<ArrayList<MyFormattedString>> final_text = text.simplify_text(as);
                float x = (float) getPoint().x;
                float y = (float) getPoint().y;
                if (true) {
                    if (as.get(0).getIterator().getEndIndex() == 0) {
                        return;
                    }
                    TextLayout tl = new TextLayout(as.get(0).getIterator(), g2d.getFontRenderContext());
                    double ast = tl.getAscent();
                    double desct = tl.getDescent();
                    double lead = tl.getLeading();
                    y += lead + ast + desct;
                    g2d.setFont(final_text.get(0).get(0));
                    bounds = (Rectangle2D.Double) getTextBounds(as, g2d, g2d.getFont());
                    bounds = new Rectangle2D.Double(x, y, bounds.getX(), bounds.getY());
                    bounds.y -= lead + ast + desct;
                }

                //--> contient des erreurs de taille en fait --> voir comment fixer ça
                //TODO faire un self dessinateur dans le formatted string ? 
                //trouver comment recuperer la taille des objets pour pouvoir le positionner correctement
                //draw(final_text, as, g2d, _LEFT, TOP_, translation_because_of_the_letter, upper_left_text.getTextBgColor());
                //en gros c'est du top centered qu'il me faut comme draw --> a faire
                //TODO appliquer aussi la stroke size aux tetes de fleches histoire que ca ne se voit pas qd on retaille qu'il y a une couille --> a faire car vraiment tres simple
                //toute petite couille de position
                //si c'est du texte il faut que je modifie le bounding rect pour qu'il devienne sensible sur une plus grande distance en fait --> a faire

                /*
                 * here we have a scale bar rectangle
                 */
                //ca ne marche pas --> voir comment on fait
                bounds.x -= bounds.width / 2.;
                bounds.y -= bounds.height / 2.;
                bounds.x -= 3;
                bounds.width += 6;
                Color bg_color = text.getTextBgColor();
                if (bg_color != null) {
                    g2d.setColor(bg_color);
                    g2d.fill(bounds);

                }

                x += (float) bounds.getWidth() / 2.; //--> centrage x ok centrage y a chier --> a fixer en fait --> TODO
                y += -(float) bounds.getHeight() / 2;

                float x_backup = x;
                int pos = 0;
                for (int i = 0; i < as.size(); i++, pos++) {
                    AttributedCharacterIterator aci = as.get(pos).getIterator();
                    if (aci == null || aci.getRunLimit() == 0) {
                        continue;
                    }
                    TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                    ArrayList<MyFormattedString> arrayList = final_text.get(i);
                    x = x_backup;
                    x -= tl.getAdvance();
                    float additional_trans = (float) ((x + tl.getBounds().getWidth() / 2.) - ((x + (bounds.getWidth()) / 2.)));//centrage en x
                    //peut etre ajouter un truc pr centrer en y --> y reflechir
                    x += additional_trans;
                    float ascent = 0;
                    float descent = 0;
                    float leading = 0;
                    for (MyFormattedString myFormattedString : arrayList) {
                        g2d.setColor(myFormattedString.getFgColor());
                        g2d.setFont(myFormattedString);
                        ascent = tl.getAscent();
                        descent = tl.getDescent();
                        leading = tl.getLeading();
                        /*
                         * dirty fix for text position do this properly some day
                         */
                        Rectangle2D curpos = myFormattedString.draw(g2d, x, y - descent);
                        //ajouter tt les rects pr en faire un truc
                        x += curpos.getWidth();
                    }
                    y += ascent + descent + leading;
                }

                /*
                 * we update the bounding rect
                 */
                el2d = new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
//                g2d.draw(el2d.getBounds2D());

                //TODO lui faire calculer le bounding rect 
//                double min_x = Integer.MAX_VALUE;
//                double min_y = Integer.MAX_VALUE;
//                double max_x = Integer.MIN_VALUE;
//                double max_y = Integer.MIN_VALUE;
//                for (Rectangle2D rectangle2D : bounding_rects) {
//                    min_x = min_x < rectangle2D.getX() ? min_x : rectangle2D.getX();
//                    min_y = min_y < rectangle2D.getY() ? min_y : rectangle2D.getY();
//                    max_x = max_x > rectangle2D.getX() + rectangle2D.getWidth() ? max_x : rectangle2D.getX() + rectangle2D.getWidth();
//                    max_y = max_y > rectangle2D.getY() + rectangle2D.getHeight() ? max_y : rectangle2D.getY() + rectangle2D.getHeight();
//                }
//                el2d = new Ellipse2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
//                //el2d.x = x_begin;
//                //el2d.y = y_begin;
//                g2d.draw(el2d.getBounds2D());
            }
            g2dparams.restore(g2d);
        }
    }

    /**
     *
     * @param as
     * @param g2d
     * @param ft
     * @return the bounds of the text to write over the image
     */
    public Rectangle2D getTextBounds(ArrayList<AttributedString> as, Graphics2D g2d, Font ft) {
        float y = 0;
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        int max_x = Integer.MIN_VALUE;
        int max_y = Integer.MIN_VALUE;
        double width = 0;
        double height = 0;

        for (AttributedString attributedString : as) {

            AttributedCharacterIterator aci = attributedString.getIterator();
            if (aci == null || aci.getRunLimit() == 0) {
                continue;
            }
            TextLayout tl = new TextLayout(aci, g2d.getFontRenderContext());
            y += tl.getAscent();
            /*
             * bug fix for text containing spaces
             */
            Rectangle r4 = tl.getBlackBoxBounds(aci.getBeginIndex(), aci.getEndIndex()).getBounds();//tl.getPixelBounds(g2d.getFontRenderContext(), 0, y);
            y += tl.getDescent() + tl.getLeading();
            height = y;
            width = Math.max(width, r4.getWidth() + r4.getX());

            max_x = Math.max(max_x, r4.x + r4.width);
            max_y = Math.max(max_y, r4.y + r4.height);
            min_x = Math.min(min_x, r4.x);
            min_y = Math.min(min_y, r4.y);
        }
        Rectangle2D.Double r = new Rectangle2D.Double(width, height, max_x - min_x, max_y - min_y);
        return r;
    }

    @Override
    public void drawSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        Point pt = getPoint1D();
        g2d.drawLine(pt.x, pt.y, pt.x, pt.y);
        g2dparams.restore(g2d);

    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        drawSkeletton(g2d, color);
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        Point pt = getPoint1D();
        String txt = "Point\nx:" + pt.x + "\ny:" + pt.y;
        drawText(g2d, txt, position);
    }

    @Override
    public String getShapeName() {
        return "Point";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.POINT;
    }

    @Override
    public Point2D.Double getCentroid() {
        return getPoint();
    }

    @Override
    public Point2D.Double[] getExtremePoints() {
        Point2D.Double[] extreme_pts = new Point2D.Double[2];
        extreme_pts[0] = getPoint();
        extreme_pts[1] = getPoint();
        return extreme_pts;
    }

    @Override
    public int getIJType() {
        return IJCompatibility.IJ_POINT;
    }

    @Override
    public Roi getIJRoi() {
        Point pt = getPoint1D();
        Roi IJ_ROI = new PointRoi(pt.x, pt.y);
        return IJ_ROI;
    }

    @Override
    public void erode(int nb_erosions) {
    }

    @Override
    public void erode() {
    }

    @Override
    public void dilate(int nb_dilatations) {
    }

    @Override
    public void dilate() {
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = test2.createGraphics();

        long start_time = System.currentTimeMillis();
        int x = 128;
        int y = 128;
        MyPoint2D.Double test = new MyPoint2D.Double(x, y);
        test.isAsterisk = true;
        //bon si multiline mais pas bon si single line --> pkoi
        //en fait pr les asterisks lui faire mettre le truc comme il faut
        ColoredTextPaneSerializable text = new ColoredTextPaneSerializable("ouba ouba\nouba2\nouba2");//\nouba2 //\nouba2\nouba2\nouba5
        test.setText(text);
//        test.setText("ouba ouba");//--> ca marche
        int width = 6;
        int height = 6;
        g2d.draw(new Ellipse2D.Double(x - width / 2, y - height / 2, width, height));
        g2d.setColor(Color.red);
        g2d.drawLine(x, y, x, y);
        //--> ça marche vraiment bien
        test.draw(g2d);

        SaverLight.popJ(test2);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        g2d.dispose();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

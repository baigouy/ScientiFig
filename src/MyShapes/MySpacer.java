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

import Commons.G2dParameters;
import Commons.SaverLight;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * MySpacer extends myImage2D. Briefly it's a MyImage2D that contains no image
 * (it can be replaced anytime by a real image).
 *
 * @since <B>Packing Analyzer 8.5</B>
 * @author Benoit Aigouy
 */
public class MySpacer extends MyImage2D implements Drawable, Serializable, Namable, Transformable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -1676423765056197961L;
    /**
     * width and height of the spacer
     */
    int initilalWidth;
    int initilalHeight;

    /**
     * a double precision MySpacer
     */
    public static class Double extends MySpacer implements Serializable, Drawable {

        public static final long serialVersionUID = 1649852521666666666L;

        /**
         * Constructor
         */
        public Double() {
            super();
        }

        /**
         * Constructor
         *
         * @param width
         * @param height
         */
        public Double(int width, int height) {
            super();
            initilalWidth = width;
            initilalHeight = height;
            rec2d = new Rectangle2D.Double(0, 0, width, height);
        }

        /**
         * Constructor
         *
         * @param x x pos
         * @param y y pos
         * @param width
         * @param height
         */
        public Double(int x, int y, int width, int height) {
            super();
            initilalWidth = width;
            initilalHeight = height;
            rec2d = new Rectangle2D.Double(x, y, width, height);
        }
    }

    @Override
    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        super.setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getImageWidth() {
        return initilalWidth - left_crop - right_crop;
    }

    @Override
    public int getImageHeight() {
        return initilalHeight - up_crop - down_crop;
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        if (!keepAR) {
            rec2d.width = width;
        } else {
            rec2d.width = width;
            scale = (double) (initilalWidth - left_crop - right_crop) / rec2d.getWidth();
            rec2d.height = (double) (initilalHeight - up_crop - down_crop) / scale;
        }
    }

    @Override
    public void setShapeHeight(double height, boolean keepAR) {
        if (!keepAR) {
            rec2d.height = height;
        } else {
            rec2d.height = height;
            scale = (double) (initilalHeight - up_crop - down_crop) / rec2d.getHeight();
            rec2d.width = (double) (initilalWidth - left_crop - right_crop) / scale;
        }
    }

    /*
     * this override allows me to directly images to a Jlist --> easier to handle
     */
    @Override
    public String toString() {
        String letter_ = "";
        if (getLetter() != null) {
            letter_ = ":" + getLetter().getText();
            if (letter_.endsWith(":")) {
                letter_ = "";
            }
        }
        return "empty_panel" + letter_;
    }

    @Override
    public void crop(int left, int right, int up, int down) {
        this.left_crop = left;
        this.right_crop = right;
        this.up_crop = up;
        this.down_crop = down;
        if (left == 0 && right == 0 && up == 0 && down == 0) {
            isCropped = false;
            rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, initilalWidth, initilalHeight);
            return;
        }
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, initilalWidth - left - right, initilalHeight - up - down);
        isCropped = true;
    }

    @Override
    public void draw(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(Color.ORANGE);
        g2d.setStroke(getLineStroke());
        g2d.draw(rec2d);
        Line2D.Double diagonale1 = new Line2D.Double(rec2d.x + rec2d.width / 2., rec2d.y, rec2d.x + rec2d.width / 2., rec2d.y + rec2d.height);
        Line2D.Double diagonale2 = new Line2D.Double(rec2d.x, rec2d.y + rec2d.height / 2., rec2d.x + rec2d.width, rec2d.y + rec2d.height / 2.);
        g2d.draw(diagonale1);
        g2d.draw(diagonale2);
        g2dparams.restore(g2d);
        super.draw(g2d);
        super.drawInset(g2d);
    }

    @Override
    public BufferedImage getCurrentDisplay() {
        int width = initilalWidth - left_crop - right_crop;
        int height = initilalHeight - up_crop - down_crop;
        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tmp.createGraphics();
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(Color.yellow);
        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, width, height);
        g2d.draw(r);
        Line2D.Double diag1 = new Line2D.Double(0, 0, width, height);
        Line2D.Double diag2 = new Line2D.Double(width, 0, 0, height);
        g2d.draw(diag1);
        g2d.draw(diag2);
        g2d.dispose();
        return tmp;
    }

    @Override
    public void scale(double factor) {
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width * factor, rec2d.height * factor);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        MySpacer test = new MySpacer.Double(512, 512);
        BufferedImage tmp = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tmp.getWidth(); i++) {
            for (int j = 0; j < tmp.getHeight(); j++) {
                tmp.setRGB(i, j, 0xFFFFFFFF);
            }
        }
        Graphics2D g2d = tmp.createGraphics();
        test.setFirstCorner(new Point2D.Double(256, 256));
        test.draw(g2d);
        test.setFirstCorner(new Point2D.Double(0, 0));
        test.draw(g2d);
        g2d.dispose();
        SaverLight.popJ(tmp);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



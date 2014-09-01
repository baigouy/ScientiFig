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

import Commons.CommonClassesLight;
import Dialogs.ROIpanelLight;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * MyCircle2D is an extension to the class MyEllipse2D where width and height
 * must always be the same
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyCircle2D extends MyEllipse2D implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -3519601743760674799L;

    /**
     * a double precision MyCircle2D
     */
    public static class Double extends MyCircle2D implements Serializable {

        public static final long serialVersionUID = 3117343337920562375L;

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param width
         * @param height
         */
        public Double(double x, double y, double width, double height) {
            double size = Math.max(width, height);
            el2d = new Ellipse2D.Double(x, y, size, size);
        }

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param width
         */
        public Double(double x, double y, double width) {
            el2d = new Ellipse2D.Double(x, y, width, width);
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Ellipse2D.Double l2d) {
            double size = Math.max(l2d.width, l2d.height);
            this.el2d = new Ellipse2D.Double(l2d.x, l2d.y, size, size);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyCircle2D.Double myel) {
            this.el2d = myel.el2d;
            this.color = myel.color;
            this.fillColor = myel.fillColor;
            this.strokeSize = myel.strokeSize;
            this.opacity = myel.opacity;
            this.fillOpacity = myel.fillOpacity;
            this.LINESTROKE = myel.LINESTROKE;
            this.dashSize = myel.dashSize;
            this.dotSize = myel.dotSize;
            this.skipSize = myel.skipSize;
            this.angle = myel.angle;
        }

        /**
         * do I need that ???
         *
         * @return
         */
        @Override
        public Object clone() {
            return new MyCircle2D.Double(this).setZpos(ZstackPos);
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

    @Override
    public boolean isRotable() {
        return el2d.width != el2d.height;
    }

    @Override
    public String getShapeName() {
        return "Circle";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.CIRCLE;
    }

    @Override
    public String toString() {
        return "<html><center>" +  CommonClassesLight.roundNbAfterComma(getCenter().x, 1)+" "+ CommonClassesLight.roundNbAfterComma(getCenter().y, 1)+ " " + getShapeName() + " <font color=" + CommonClassesLight.toHtmlColor(color) + ">contour</font>" + ((this instanceof Fillable) ? " <font color=" + CommonClassesLight.toHtmlColor(fillColor) + ">fill</font>" : "") + "</html>";
    }

    /**
     * we have to override the ellipse one to preserve aspect ratio doesn't work
     * no big deal I'll fix it another day maybe the pb is not even in this
     * class
     *
     * @param magicPoints
     */
//    @Override
//    public void setMagicPoints(ArrayList<Object> magicPoints) {
//        double radius = el2d.width;
//        Point2D.Double center_up = (((MyEllipse2D.Double) magicPoints.get(0)).getPoint());
//        Point2D.Double right_center = (((MyEllipse2D.Double) magicPoints.get(1)).getPoint());
//        Point2D.Double bottom_center = (((MyEllipse2D.Double) magicPoints.get(2)).getPoint());
//        Point2D.Double left_center = (((MyEllipse2D.Double) magicPoints.get(3)).getPoint());
//        double width = right_center.distance(left_center);
//        double height = center_up.distance(bottom_center);
//        if (Math.abs(width - radius) > Math.abs(height - radius)) {
//            height = width;
//        } else {
//            width = height;
//        }
//        double x = Math.min(left_center.x, right_center.x);
//        double y = Math.min(center_up.y, bottom_center.y);
//        el2d = new Ellipse2D.Double(x, y, width, height);
//    }
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
        MyCircle2D.Double test = new MyCircle2D.Double(10, 10, 40, 60);
        test.drawAndFill(g2d);
        g2d.dispose();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

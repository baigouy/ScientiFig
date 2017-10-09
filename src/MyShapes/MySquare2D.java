/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2015 Benoit Aigouy 

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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * MySquare2D is a 4 sided vectorial object (a rectangle where width = height)
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MySquare2D extends MyRectangle2D implements PARoi, Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 4666331552903352567L;

    /**
     * a double precision MySquare2D
     */
    public static class Double extends MySquare2D implements Serializable {

        public static final long serialVersionUID = -1450247686452570704L;

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
            rec2d = new Rectangle2D.Double(x, y, size, size);
        }

        /**
         * Constructor
         *
         * @param r2d
         */
        public Double(Rectangle2D.Double r2d) {
            double size = Math.max(r2d.width, r2d.height);
            this.rec2d = new Rectangle2D.Double(r2d.x, r2d.y, size, size);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MySquare2D.Double myel) {
            this.rec2d = myel.rec2d;
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
         * I can probably remove this clone and only keep the parent one
         *
         * @return
         */
        @Override
        public Object clone() {
            return new MySquare2D.Double(this).setZpos(ZstackPos);
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
    public String getShapeName() {
        return "Rectangle";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.RECTANGLE;
    }

    @Override
    public String toString() {
        return "<html><center>" +  CommonClassesLight.roundNbAfterComma(getCenter().x, 1)+" "+ CommonClassesLight.roundNbAfterComma(getCenter().y, 1)+ " " + getShapeName() + " <font color=" + CommonClassesLight.toHtmlColor(color) + ">contour</font>" + ((this instanceof Fillable) ? " <font color=" + CommonClassesLight.toHtmlColor(fillColor) + ">fill</font>" : "") + "</html>";
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
        MySquare2D.Double test = new MySquare2D.Double(10, 10, 40, 60);
        test.drawAndFill(g2d);
        g2d.dispose();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

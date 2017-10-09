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

import Dialogs.ROIpanelLight;
import Commons.CommonClassesLight;
import Commons.G2dParameters;
import Commons.MyBufferedImage;
import ij.gui.EllipseRoi;
import ij.gui.OvalRoi;
import ij.gui.Roi;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * MyEllipse2D is my own implementation of a vectorial ellipse shape
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyEllipse2D extends Ellipse2D implements PARoi, Contourable, Fillable, Serializable, LineStrokable, Morphable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 2052017115978831914L;
    int color = 0xFFFF00;
    int fillColor = 0xFFFF00;
    float opacity = 1.f;
    float fillOpacity = 0.f;
    /**
     *
     */
    public Ellipse2D.Double el2d;
    float strokeSize = 0.65f;
    /**
     *
     */
    public int ZstackPos = 0;
    /*
     * line stroke variables
     */
    public int LINESTROKE = 0;
    public int dashSize = 6;
    public int dotSize = 1;
    public int skipSize = 6;
    /*
     * for rotations
     */
    double angle;

    /**
     * a double precision MyEllipse2D
     */
    public static class Double extends MyEllipse2D implements Serializable {

        /**
         *
         */
        public static final long serialVersionUID = 2070327142126833034L;

        /**
         * converts an IJ rect ROI to a SF one
         *
         * @param ijRoi
         */
        public Double(Roi ijRoi) {
//            if (ijRoi instanceof EllipseRoi)
//            {
            EllipseRoi elRoi = (EllipseRoi) ijRoi;
            double[] params = elRoi.getParams();
            el2d = new Ellipse2D.Double(params[0], params[1], params[2] - params[0], params[3] - params[1]);
//            }else if (ijRoi instanceof OvalRoi)
//            {
//             OvalRoi elRoi = (OvalRoi)ijRoi;
//             
//            //double[] params = elRoi.getParams();
//           // el2d = new Ellipse2D.Double(ijRoi.getXBase(), ijRoi.getYBase(),ijRoi.get, elRoi.y );    
//            }
            Color strokeCol = ijRoi.getStrokeColor();
            if (strokeCol != null) {
                color = strokeCol.getRGB();
            }
            //ijRoi.isLine()
            Color fillCol = ijRoi.getFillColor();
            if (fillCol != null) {
                fillColor = fillCol.getRGB();
            }
            strokeSize = ijRoi.getStrokeWidth();
            //ijRoi.getZpos
        }

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param width
         * @param height
         */
        public Double(double x, double y, double width, double height) {
            el2d = new Ellipse2D.Double(x, y, width, height);
        }

        /**
         * Constructor
         *
         * @param el2d
         */
        public Double(Ellipse2D.Double el2d) {
            this.el2d = el2d;
        }

        public Double(double center_x, double center_y, double size) {
            this.el2d = new Ellipse2D.Double(center_x - size / 2., center_y - size / 2., size, size);
        }

        public Double(Point2D.Double center, double size) {
            this.el2d = new Ellipse2D.Double(center.x - size / 2., center.y - size / 2., size, size);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyEllipse2D.Double myel) {
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
         * Constructor that allows for compatibility with ImageJ
         *
         * @param p a polygon
         */
        public Double(Polygon p) {
            int x_min = Integer.MAX_VALUE;
            int y_min = Integer.MAX_VALUE;
            int x_max = Integer.MIN_VALUE;
            int y_max = Integer.MIN_VALUE;
            for (int i = 0; i < p.npoints; i++) {
                int x = p.xpoints[i];
                int y = p.ypoints[i];
                x_min = x_min < x ? x_min : x;
                y_min = y_min < y ? y_min : y;
                x_max = x_max > x ? x_max : x;
                y_max = y_max > y ? y_max : y;
            }
            int width = x_max - x_min;
            int height = y_max - y_min;
            this.el2d = new Ellipse2D.Double(x_min, y_min, width, height);
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

    public Point2D.Double getPoint() {
        return new Point2D.Double(el2d.getCenterX(), el2d.getCenterY());
    }

    public void setPoint(Point2D.Double pos) {
        //en fait on va appliquer une translation --> plus simple et on perd pas la forme de l'ellipse comme ca
        Point2D.Double curPos = getPoint();
        double trans_x = curPos.x - pos.x;
        double trans_y = curPos.y - pos.y;
        translate(-trans_x, -trans_y);
        //return new Point2D.Double(el2d.x + el2d.width / 2., el2d.y + el2d.height / 2.);
    }

    @Override
    public BasicStroke getLineStroke() {
        float[] stroke_parameters = createLineStroke();
        if (stroke_parameters == null) {
            return new BasicStroke(strokeSize);
        } else {
            return new BasicStroke(
                    strokeSize,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_BEVEL,
                    1f,
                    stroke_parameters,
                    0f);
        }
    }

    @Override
    public float[] createLineStroke() {
        if (dotSize == 0 && skipSize == 0 && dashSize == 0) {
            return null;
        }
        float[] lineStroke;
        switch (LINESTROKE) {
            case LineStrokable.DASHED:
                lineStroke = new float[2];
                lineStroke[0] = dashSize;
                lineStroke[1] = skipSize;
                break;
            case LineStrokable.DOTTED:
                lineStroke = new float[2];
                lineStroke[0] = dotSize;
                lineStroke[1] = skipSize;
                break;
            case LineStrokable.DASH_DOT:
                lineStroke = new float[4];
                lineStroke[0] = dashSize;
                lineStroke[1] = skipSize;
                lineStroke[2] = dotSize;
                lineStroke[3] = skipSize;
                break;
            default: // <-->  case LineStrokable.PLAIN:
                lineStroke = null;
                break;
        }
        return lineStroke;
    }

    @Override
    public int getSkipSize() {
        return skipSize;
    }

    @Override
    public int getDashSize() {
        return dashSize;
    }

    @Override
    public int getDotSize() {
        return dotSize;
    }

    @Override
    public void setSkipSize(int size) {
        this.skipSize = size;
    }

    @Override
    public void setDashSize(int size) {
        this.dashSize = size;
    }

    @Override
    public void setDotSize(int size) {
        this.dotSize = size;
    }

    @Override
    public void setLineStrokeType(int LINESTROKE) {
        this.LINESTROKE = LINESTROKE;
    }

    @Override
    public int getLineStrokeType() {
        return LINESTROKE;
    }

    @Override
    public boolean isDashed() {
        return isLineStrokeOfType(LineStrokable.DASHED);
    }

    @Override
    public boolean isDotted() {
        return isLineStrokeOfType(LineStrokable.DOTTED);
    }

    @Override
    public boolean isPlain() {
        return isLineStrokeOfType(LineStrokable.PLAIN);
    }

    @Override
    public boolean isDashDot() {
        return isLineStrokeOfType(LineStrokable.DASH_DOT);
    }

    @Override
    public boolean isLineStrokeOfType(int TYPE) {
        return LINESTROKE == TYPE;
    }

    @Override
    public Object clone() {
        MyEllipse2D.Double r2d = new MyEllipse2D.Double(el2d);
        r2d.color = color;
        r2d.opacity = opacity;
        r2d.fillOpacity = fillOpacity;
        r2d.ZstackPos = ZstackPos;
        r2d.strokeSize = strokeSize;
        r2d.LINESTROKE = LINESTROKE;
        r2d.dashSize = dashSize;
        r2d.dotSize = dotSize;
        r2d.skipSize = skipSize;
        r2d.angle = angle;
        return (MyEllipse2D.Double) r2d;
    }

    @Override
    public double getX() {
        return el2d.getX();
    }

    @Override
    public double getY() {
        return el2d.getY();
    }

    @Override
    public double getWidth() {
        return el2d.getWidth();
    }

    @Override
    public double getHeight() {
        return el2d.getHeight();
    }

    @Override
    public boolean isEmpty() {
        return el2d.isEmpty();
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        el2d.setFrame(x, y, w, h);
    }

    @Override
    public Rectangle2D getBounds2D() {
        /*
         * small addition in case the shape has been rotated
         */
        if (angle != 0) {
            return getRotatedShape().getBounds2D();
        }
        return el2d.getBounds2D();
    }

    private Shape getRotatedShape() {
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angle), el2d.getCenterX(), el2d.getCenterY());
        Shape rotated_shp = at.createTransformedShape(el2d);
        return rotated_shp;
    }

    @Override
    public Rectangle getBounds() {
        if (angle != 0) {
            return getRotatedShape().getBounds();
        }
        return el2d.getBounds();
    }

//    @Override
//    public int getColor() {
//        return color & 0x00FFFFFF;
//    }
//
//    @Override
//    public void setColor(int color) {
//        this.color = color & 0x00FFFFFF;
//    }
//    @Override
//    public int getColorIn() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getColorOut() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    @Override
    public int getDrawColor() {
        return color & 0x00FFFFFF;
    }

    @Override
    public void setDrawColor(int color) {
        this.color = color & 0x00FFFFFF;
    }

    @Override
    public int getFillColor() {
        return fillColor & 0x00FFFFFF;
    }

    @Override
    public void setFillColor(int color) {
        this.fillColor = color & 0x00FFFFFF;
    }

    @Override
    public void setFillOpacity(float opacity) {
        this.fillOpacity = opacity <= 0f ? 0f : opacity > 1f ? 1f : opacity;
    }

    @Override
    public float getFillOpacity() {
        return fillOpacity;
    }

    @Override
    public float getDrawOpacity() {
        return opacity;
    }

    @Override
    public void setDrawOpacity(float opacity) {
        this.opacity = opacity <= 0f ? 0f : opacity > 1f ? 1f : opacity;
    }

    @Override
    public float getStrokeSize() {
        return strokeSize;
    }

    @Override
    public void setStrokeSize(float strokeSize) {
        this.strokeSize = strokeSize;
    }

//    @Override
//    public boolean isTransparent() {
//        return isTransparent;
//    }
    @Override
    public void drawSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(el2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.fill(el2d);
        g2dparams.restore(g2d);
    }

//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.drawAndFill(el2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void fill(Graphics2D g2d, int color, float transparency, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.fill(el2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSize) {
//        drawAndFill(g2d, color, transparency, strokeSize);
//        fill(g2d, color, transparency, strokeSize);
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d, float transparency) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.drawAndFill(el2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void fillTransparent(Graphics2D g2d, float transparency) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.fill(el2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFillTransparent(Graphics2D g2d, float transparency) {
//        drawTransparent(g2d, transparency);
//        fillTransparent(g2d, transparency);
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d) {
//        drawTransparent(g2d, opacity);
//    }
//
//    @Override
//    public void fillTransparent(Graphics2D g2d) {
//        fillTransparent(g2d, opacity);
//    }
//
//    @Override
//    public void drawAndFillTransparent(Graphics2D g2d) {
//        drawTransparent(g2d);
//        fillTransparent(g2d);
//    }
    @Override
    public void draw(Graphics2D g2d) {
        drawAndFill(g2d, opacity > 0f, false, true);
    }

    @Override
    public void fill(Graphics2D g2d) {
        drawAndFill(g2d, false, fillOpacity > 0f, true);
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        drawAndFill(g2d, opacity > 0f, fillOpacity > 0f, true);
    }

    private void drawAndFill(Graphics2D g2d, boolean drawContour, boolean fillShape, boolean forceShowShape) {
        if (drawContour || fillShape) {
            forceShowShape = false;
            G2dParameters g2dparams = new G2dParameters(g2d);
            Shape rotated_shp = null;
            if (angle != 0) {
                rotated_shp = getRotatedShape();
            }
            if (fillShape) {
                g2d.setColor(new Color(fillColor));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fillOpacity));
                /*
                 * small addition to handle rotation
                 */
                if (rotated_shp != null) {
                    g2d.fill(rotated_shp);
                } else {
                    g2d.fill(el2d);
                }
            }
            if (drawContour) {
                g2d.setColor(new Color(color));
                g2d.setStroke(getLineStroke());
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                /*
                 * small addition to handle rotation
                 */
                if (rotated_shp != null) {
                    g2d.draw(rotated_shp);
                } else {
                    g2d.draw(el2d);
                }
            }
            g2dparams.restore(g2d);
        }
        if (forceShowShape) {
            /**
             * if both are transparent we just draw the skeletton or maybe not
             * maybe draw bounding box with a cross
             */
            G2dParameters g2dparams = new G2dParameters(g2d);
            g2d.setColor(new Color(0xFFFF00));
            g2d.setStroke(new BasicStroke(1));
            Rectangle2D r2d = getBounds2D();
            g2d.draw(r2d);
            g2d.draw(el2d);
            g2d.draw(new Line2D.Double(r2d.getX(), r2d.getY(), r2d.getX() + r2d.getWidth(), r2d.getY() + r2d.getHeight()));
            g2dparams.restore(g2d);
        }
    }

//    @Override
//    public void drawAndFill(Graphics2D g2d) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.fill(el2d);
//        g2d.drawAndFill(el2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color) {
//        int bckup = this.color;
//        this.color = color;
//        drawAndFill(g2d);
//        this.color = bckup;
//    }
//
//    @Override
//    public void fill(Graphics2D g2d, int color) {
//        int bckup = this.color;
//        this.color = color;
//        fill(g2d);
//        this.color = bckup;
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color) {
//        int bckup = this.color;
//        this.color = color;
//        drawAndFill(g2d);
//        this.color = bckup;
//    }
    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        drawIfVisible(g2d, visibleRect);
    }

    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visibleRect) {
        if (visibleRect.intersects(this.getBounds2D())) {
            drawAndFill(g2d);
        }
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return this.getBounds2D().intersects(visibleRect);
    }

    @Override
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        el2d = new Ellipse2D.Double(el2d.x + x, el2d.y + y, el2d.width, el2d.height);
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            this.angle = angleInDegrees;
        }
    }

    @Override
    public void scale(double factor) {
        el2d = new Ellipse2D.Double(el2d.x, el2d.y, el2d.width * factor, el2d.height * factor);
    }

    @Override
    public Ellipse2D.Double getParentInstance() {
        return el2d;
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        Rectangle2D rec = getBounds2D();
        if (rec.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dottedSelection, 0f));
            g2d.draw(rec);
            g2dParams.restore(g2d);
        }
    }

    @Override
    public ArrayList<Object> getMagicPoints() {
        double size = 10;
        ArrayList<Object> magic_pts = new ArrayList<Object>();
        /*
         * the magic points are at the center of each bond
         */
        magic_pts.add(new MyEllipse2D.Double(el2d.x + el2d.width / 2. - size / 2., el2d.y - size / 2., size, size));//center up
        magic_pts.add(new MyEllipse2D.Double(el2d.x + el2d.width - size / 2., el2d.y + el2d.height / 2. - size / 2., size, size));//right center
        magic_pts.add(new MyEllipse2D.Double(el2d.x + el2d.width / 2. - size / 2., el2d.y + el2d.height - size / 2., size, size)); //bottom center
        magic_pts.add(new MyEllipse2D.Double(el2d.x - size / 2., el2d.y + el2d.height / 2. - size / 2., size, size));//left center

        /*
         * small addition to handle rotation
         */
        if (angle != 0) {
            magic_pts = new MyShapesTools().rotatePoints(magic_pts, Math.toRadians(angle), getCenter());
        }

        return magic_pts;
    }

    @Override
    public void setMagicPoints(ArrayList<Object> magicPoints) {
        Point2D.Double center_up = (((MyEllipse2D.Double) magicPoints.get(0)).getPoint());
        Point2D.Double right_center = (((MyEllipse2D.Double) magicPoints.get(1)).getPoint());
        Point2D.Double bottom_center = (((MyEllipse2D.Double) magicPoints.get(2)).getPoint());
        Point2D.Double left_center = (((MyEllipse2D.Double) magicPoints.get(3)).getPoint());

        double width = right_center.distance(left_center);
        double height = center_up.distance(bottom_center);
        double widthDif = Math.abs(width - el2d.width);
        double heightDif = Math.abs(height - el2d.height);

        double x = Math.min(left_center.x, right_center.x);
        double y = Math.min(center_up.y, bottom_center.y);
        /*
         * updated to handle rotation
         */
        if (angle != 0) {
            Point2D.Double neo_center1 = new Point2D.Double((left_center.x + right_center.x) / 2., (left_center.y + right_center.y) / 2.);
            Point2D.Double neo_center2 = new Point2D.Double((center_up.x + bottom_center.x) / 2., (center_up.y + bottom_center.y) / 2.);
            Point2D.Double neo_center;
            if (widthDif > heightDif) {
                neo_center = neo_center2;
            } else {
                neo_center = neo_center1;
            }
            left_center = CommonClassesLight.rotatePointInRadians(left_center, neo_center, Math.toRadians(-angle));
            right_center = CommonClassesLight.rotatePointInRadians(right_center, neo_center, Math.toRadians(-angle));
            bottom_center = CommonClassesLight.rotatePointInRadians(bottom_center, neo_center, Math.toRadians(-angle));
            center_up = CommonClassesLight.rotatePointInRadians(center_up, neo_center, Math.toRadians(-angle));
            width = Math.abs(right_center.x - left_center.x);
            height = Math.abs(center_up.y - bottom_center.y);
            el2d = new Ellipse2D.Double(neo_center.x - width / 2., neo_center.y - height / 2., width, height);
        } else {
            el2d = new Ellipse2D.Double(x, y, width, height);
        }
    }

    @Override
    public void setFirstCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - pos.x, bounding.getY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setLastCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getMaxX() - pos.x, bounding.getMaxY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setCenter(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getCenterX() - pos.x, bounding.getCenterY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public Point2D.Double getFirstCorner() {
        Rectangle2D bounding_rect = getBounds2D();
        Point2D.Double first_corner = new Point2D.Double(bounding_rect.getX(), bounding_rect.getY());
        return first_corner;
    }

    @Override
    public Point2D.Double getLastCorner() {
        Rectangle2D bounding_rect = getBounds2D();
        Point2D.Double last_corner = new Point2D.Double(bounding_rect.getX() + bounding_rect.getWidth(), bounding_rect.getY() + bounding_rect.getHeight());
        return last_corner;
    }

    @Override
    public Point2D.Double getCenter() {
        Rectangle2D bounding_rect = getBounds2D();
        Point2D.Double center = new Point2D.Double(bounding_rect.getCenterX(), bounding_rect.getCenterY());
        return center;
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        if (!keepAR) {
            el2d.width = width;
        } else {
            double factor = width / el2d.getWidth();
            el2d.width = width;
            el2d.height = el2d.height * factor;
        }
    }

    @Override
    public void setShapeHeight(double height, boolean keepAR) {
        if (!keepAR) {
            el2d.height = height;
        } else {
            double factor = height / el2d.getHeight();
            el2d.height = height;
            el2d.width = el2d.width * factor;
        }
    }

    @Override
    public double getShapeWidth() {
        return el2d.getWidth();
    }

    @Override
    public double getShapeHeight() {
        return el2d.getHeight();
    }

    /*
     * see http://www.math.hmc.edu/funfacts/ffiles/10006.3.shtml
     */
    @Override
    public double getAreaFast() {
        return getArea();
    }

    @Override
    public Polygon getPolygon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flipHorizontally() {
        /*
         * nothing to do (not true anymore now that I added the rotation --> fix this)
         */
    }

    @Override
    public void flipVertically() {
        /*
         * nothing to do (not true anymore now that I added the rotation --> fix this)
         */
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        String text = "Ellipse\ncenter_x:" + el2d.x + el2d.width / 2. + "\ncenter_y:" + el2d.y + el2d.height / 2. + "\nw:" + el2d.width + "\nh:" + el2d.height;
        drawText(g2d, text, position);
    }

    @Override
    public void drawText(Graphics2D g2d, String text, int position) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(strokeSize));

        Shape shp = g2d.getStroke().createStrokedShape(this.getBounds2D());
        Rectangle2D bounds = shp.getBounds2D();
        String[] splitted_text = text.split("\n");
        switch (position) {
            case BELOW:
                if (true) {
                    float last_height = (float) (bounds.getY() + bounds.getHeight());
                    for (String string : splitted_text) {
                        last_height += CommonClassesLight.getTextHeight(g2d);
                        g2d.drawString(string, (float) bounds.getX(), last_height);
                    }
                }
                break;
            case ABOVE:
                if (true) {
                    float last_height = (float) (bounds.getY() - CommonClassesLight.getTextHeight(g2d) * splitted_text.length - strokeSize);
                    for (String string : splitted_text) {
                        last_height += CommonClassesLight.getTextHeight(g2d);
                        g2d.drawString(string, (float) bounds.getX(), last_height);
                    }
                }
                break;
            case RIGHT:
                if (true) {
                    float last_height = (float) (bounds.getY());
                    for (String string : splitted_text) {
                        g2d.drawString(string, (float) (bounds.getX() + bounds.getWidth() + strokeSize), last_height);
                        last_height += CommonClassesLight.getTextHeight(g2d);
                    }
                }
                break;
            case LEFT:
                if (true) {
                    float last_height = (float) (bounds.getY());
                    float max_text_width = 0;
                    for (String string : splitted_text) {
                        float cur_text_with = CommonClassesLight.getTextWidth(g2d, string);
                        max_text_width = cur_text_with > max_text_width ? cur_text_with : max_text_width;
                    }
                    for (String string : splitted_text) {
                        float cur_text_with = CommonClassesLight.getTextWidth(g2d, string);
                        g2d.drawString(string, (float) (bounds.getX() - max_text_width + (max_text_width - cur_text_with) - strokeSize), last_height);
                        last_height += CommonClassesLight.getTextHeight(g2d);
                    }
                }
                break;
        }
        g2dparams.restore(g2d);
    }

    @Override
    public String getShapeName() {
        return "Ellipse";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.ELLIPSE;
    }

    @Override
    public String toString() {
        return "<html><center>" + CommonClassesLight.roundNbAfterComma(getCenter().x, 1) + " " + CommonClassesLight.roundNbAfterComma(getCenter().y, 1) + " " + getShapeName() + " <font color=" + CommonClassesLight.toHtmlColor(color) + ">contour</font>" + ((this instanceof Fillable) ? " <font color=" + CommonClassesLight.toHtmlColor(fillColor) + ">fill</font>" : "") + "</html>";
    }

    @Override
    public double getArea() {
        Rectangle2D bounds = getBounds2D();
        double area_count = 0;
        BufferedImage area = new MyBufferedImage(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
        Graphics2D g2d = area.createGraphics();
        drawSkeletton(g2d, 0xFFFFFF);
        fillSkeletton(g2d, 0xFFFFFF);
        for (int i = (int) bounds.getX(); i < (int) (bounds.getX() + bounds.getWidth()); i++) {
            for (int j = (int) bounds.getY(); j < (int) (bounds.getY() + bounds.getHeight()); j++) {
                int col = area.getRGB(i, j);
                if (col == 0xFFFFFFFF || col == 0x00FFFFFF) {
                    area_count++;
                }
            }
        }
        g2d.dispose();
        return area_count;
    }

    @Override
    public Point2D.Double getCentroid() {
        Rectangle2D bounds = getBounds2D();
        double x = 0.;
        double y = 0.;
        double counter = 0.;
        BufferedImage area = new MyBufferedImage(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
        Graphics2D g2d = area.createGraphics();
        drawSkeletton(g2d, 0xFFFFFF);
        fillSkeletton(g2d, 0xFFFFFF);
        for (int i = (int) bounds.getX(); i < (int) (bounds.getX() + bounds.getWidth()); i++) {
            for (int j = (int) bounds.getY(); j < (int) (bounds.getY() + bounds.getHeight()); j++) {
                int col = area.getRGB(i, j);
                if (col == 0xFFFFFFFF || col == 0x00FFFFFF) {
                    x += i;
                    y += i;
                    counter++;
                }
            }
        }
        g2d.dispose();
        return new Point2D.Double(x / counter, y / counter);
    }

    @Override
    public Point2D.Double[] getExtremePoints() {
        Point2D.Double[] extreme_pts = new Point2D.Double[2];
        extreme_pts[0] = getFirstCorner();
        extreme_pts[1] = getLastCorner();
        return extreme_pts;
    }

    /*
     * NB the rotated version is probably not compatible with the IJ one (TODO think about a fix)
     */
    @Override
    public int getIJType() {
        return IJCompatibility.IJ_ELLIPSE;
    }

    @Override
    public Roi getIJRoi() {
        Roi IJ_ROI = new OvalRoi((int) el2d.x, (int) el2d.y, (int) el2d.width, (int) el2d.height);
        return IJ_ROI;
    }

    @Override
    public void erode(int nb_erosions) {
        double x = el2d.x;
        double y = el2d.y;
        double width = el2d.width;
        double height = el2d.height;
        x += nb_erosions;
        y += nb_erosions;
        /*
         * we make it so that it can't be smaller than 1px
         */
        width -= nb_erosions * 2;
        height -= nb_erosions * 2;
        if (width < 1) {
            width = 1;
            x = el2d.getCenterX() - 0.5;
        }
        if (height < 1) {
            height = 1;
            y = el2d.getCenterY() - 0.5;
        }
        el2d = new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public void erode() {
        erode(1);
    }

    @Override
    public void dilate(int nb_dilatations) {
        double x = el2d.x;
        double y = el2d.y;
        double width = el2d.width;
        double height = el2d.height;
        x -= nb_dilatations;
        y -= nb_dilatations;
        width += nb_dilatations * 2;
        height += nb_dilatations * 2;
        el2d = new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public void dilate() {
        dilate(1);
    }

    @Override
    public void setZstackPosition(int ZstackPosition) {
        this.ZstackPos = ZstackPosition;

    }

    @Override
    public int getZStackposition() {
        return ZstackPos;
    }

    @Override
    public boolean contains(Point p) {
        if (angle != 0) {
            return getRotatedShape()./*getBounds2D().*/contains(p);
        }
        return el2d.contains(p);
    }

    @Override
    public boolean contains(Point2D p) {
        if (angle != 0) {
            return getRotatedShape()./*getBounds2D().*/contains(p);
        }
        return el2d.contains(p);
    }

    @Override
    public boolean contains(Rectangle2D r2) {
        if (angle != 0) {
            return getRotatedShape()./*getBounds2D().*/contains(r2);
        }
        return el2d.contains(r2);
    }

    @Override
    public boolean contains(double x, double y) {
        if (angle != 0) {
            return getRotatedShape()./*getBounds2D().*/contains(x, y);
        }
        return el2d.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        if (angle != 0) {
            return getRotatedShape()./*getBounds2D().*/contains(x, y, w, h);
        }
        return el2d.contains(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle r) {
        //TODO fix all intersects in all classes --> make sure it is not rotated
        return el2d.intersects(r);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return el2d.intersects(r);
    }

    @Override
    public boolean isRotable() {
        return true;
    }

    @Override
    public double getRotation() {
        if (!isRotable()) {
            return 0;
        } else {
            return angle;
        }
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
        MyEllipse2D.Double test = new MyEllipse2D.Double(10, 10, 40, 60);

//        System.out.println(test instanceof Ellipse2D); //--> true
        System.out.println(test instanceof MyEllipse2D.Double);//--> true
        System.out.println(test instanceof PARoi); //--> encore mieux --> c'est super puissant
        //--> avec les cast je pourrais faire des trucs marrants

        test.drawAndFill(g2d);
        g2d.dispose();
//        Saver2.poplong(test2);

//      test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

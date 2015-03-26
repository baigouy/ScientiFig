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
import Commons.SaverLight;
import ij.gui.Roi;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * MyRectangle2D is a rectangular vectorial object
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
/*
 * nb: keep it like this (without extends Rectangle2D) for now otherwise it creates a packing bug --> try to understand why in the future and fix it
 */
public abstract class MyRectangle2D /*extends Rectangle2D*/ implements PARoi, Contourable, Fillable, LineStrokable, Serializable, Morphable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -4016519115188053806L;
    public Rectangle2D.Double rec2d;
    public int color = 0xFFFF00;
    public int fillColor = 0xFFFF00;
    public float opacity = 1.f;
    float fillOpacity = 0.f;
    public float strokeSize = 0.65f;
    public int ZstackPos = 0;
    /*
     * line stroke variables
     */
    public int LINESTROKE = 0;
    public int dashSize = 6;
    public int dotSize = 1;
    public int skipSize = 6;
    /*
     * to allow rotation of the shape
     */
    double angle;

    /**
     * a double precision MyRectangle2D
     */
    public static class Double extends MyRectangle2D implements Serializable {

        public static final long serialVersionUID = 9057588182065555824L;

        /**
         * converts an IJ rect ROI to a SF one
         *
         * @param ijRoi
         */
        public Double(Roi ijRoi) {
            rec2d = new Rectangle2D.Double(ijRoi.getXBase(), ijRoi.getYBase(), ijRoi.getFloatWidth(), ijRoi.getFloatHeight());
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
            rec2d = new Rectangle2D.Double(x, y, width, height);
        }

        /**
         * Constructor
         *
         * @param r2d
         */
        public Double(Rectangle2D r2d) {
            this.rec2d = new Rectangle2D.Double(r2d.getX(), r2d.getY(), r2d.getWidth(), r2d.getHeight());
        }

        /**
         * Constructor
         *
         * @param r2d
         */
        public Double(Rectangle2D.Double r2d) {
            this.rec2d = new Rectangle2D.Double(r2d.getX(), r2d.getY(), r2d.getWidth(), r2d.getHeight());
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyRectangle2D.Double myel) {
            this.rec2d = (Rectangle2D.Double) myel.rec2d.clone();
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
         * Constructor
         *
         * @param p
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
            this.rec2d = new Rectangle2D.Double(x_min, y_min, width, height);
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

    /**
     * constructor and clone are duplicated --> clean this TODO
     *
     * @return
     */
    @Override
    public Object clone() {
        MyRectangle2D.Double r2d = new MyRectangle2D.Double(rec2d);
        r2d.color = color;
        r2d.fillColor = fillColor;
        r2d.opacity = opacity;
        r2d.fillOpacity = fillOpacity;
        r2d.ZstackPos = ZstackPos;
        r2d.strokeSize = strokeSize;
        r2d.LINESTROKE = LINESTROKE;
        r2d.dashSize = dashSize;
        r2d.dotSize = dotSize;
        r2d.skipSize = skipSize;
        r2d.angle = angle;
        return r2d;
    }

    @Override
    public Point2D.Double getCentroid() {
        return getCenter();
    }

//    @Override
    public double getX() {
        return rec2d.getX();
    }

//    @Override
    public double getY() {
        return rec2d.getY();
    }

//    @Override
    public double getWidth() {
        return (rec2d.getWidth());
    }

//    @Override
    public double getHeight() {
        return (rec2d.getHeight());
    }

//    @Override
    public boolean isEmpty() {
        return rec2d.isEmpty();
    }

//    @Override
    public void setFrame(double x, double y, double w, double h) {
        rec2d.setFrame(x, y, w, h);
    }

    @Override
    public Rectangle2D getBounds2D() {
        /*
         * small addition in case the shape has been rotated
         */
        if (angle != 0) {
            return getRotatedShape().getBounds2D();
        }
        return rec2d.getBounds2D();
    }

    @Override
    public boolean intersects(Rectangle r) {
        return rec2d.intersects(r);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return rec2d.intersects(r);
    }

    @Override
    public Rectangle getBounds() {
        return rec2d.getBounds();
    }

//    @Override
    public void setRect(double x, double y, double w, double h) {
        rec2d.setRect(x, y, w, h);
    }

//    @Override
    public int outcode(double x, double y) {
        return rec2d.outcode(x, y);
    }

//    @Override
    public Rectangle2D createIntersection(Rectangle2D r) {
        return rec2d.createIntersection(r);
    }

//    @Override
    public Rectangle2D createUnion(Rectangle2D r) {
        return rec2d.createUnion(r);
    }

//    @Override
//    public int getColor() {
//        return color & 0x00FFFFFF;
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
//    @Override
//    public void setColor(int color) {
//        this.color = color & 0x00FFFFFF;
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

    @Override
    public void drawSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(rec2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.fill(rec2d);
        g2dparams.restore(g2d);

    }

//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float opacity, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
//     drawAndFill2d.draw(rec2d);
//        g2dparams.restore(g2d);
//    }
//    @Override
//    public void fill(Graphics2D g2d, int color, float opacity, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
//        g2d.fill(rec2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float opacity, float strokeSizedrawAndFill//        draw(g2d, color, opacity, strokeSize);
//        fill(g2d, color, opacity, strokeSize);
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d, float opacity) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparedrawAndFill);
//        g2d.draw(rec2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void fillTransparent(Graphics2D g2d, float opacity) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
//        g2d.fill(rec2d);
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFillTransparent(Graphics2D g2d, float opacity) {
//        drawTransparent(g2d, opacity);
//        fillTransparent(g2d, opacity);
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
                    g2d.fill(rec2d);
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
                    g2d.draw(rec2d);
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
            g2d.draw(new Line2D.Double(r2d.getX(), r2d.getY(), r2d.getX() + r2d.getWidth(), r2d.getY() + r2d.getHeight()));
            g2dparams.restore(g2d);
        }
    }

//    /*
//     * TODO implementer ca pour tous ca permet de dessiner la forme a un angle par rapport a un autre object sans reelement modifier l'object en fait en faire une version avec la scale --> encore plus imple a gerer
//     * TODO faire aussi tourner tout les objects
//     */
//    public void drawAtAngle(Graphics2D g2d, double theta, Rectangle2D bounding_rect) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        if (theta != 0) {
//            AffineTransform at = new AffineTransform();
//            at.rotate(Math.toRadians(theta), bounding_rect.getX() + bounding_rect.getWidth() / 2., bounding_rect.getY() + bounding_rect.getHeight() / 2.);
//            g2d.setTransform(at);
//        }
//        g2d.draw(rec2d);
//        g2dparams.restore(g2d);
//    }
//    @Override
//    public void drawAndFill(Graphics2D g2d) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//    drawAndFillg2d.fill(rec2d);
//        g2d.draw(rec2d);
//        g2dparams.restore(g2d);
// drawAndFill
//
//    @Override
//    public void draw(Graphics2D g2d, int color) {
//        int bckup = thidrawAndFilllor;
//        this.color = color;
//        draw(g2d);
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
        if (visibleRect.intersects(rec2d.getBounds2D())) {
            drawAndFill(g2d);
        }
    }

    @Override
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        rec2d = new Rectangle2D.Double(rec2d.x + x, rec2d.y + y, rec2d.width, rec2d.height);
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        Rectangle2D rec = getBounds2D();
        if (rec.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dottedSelection, 0f));
            g2d.setColor(Color.red);
            g2d.draw(rec);
            g2dParams.restore(g2d);
        }
    }

    @Override
    public void scale(double factor) {
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width * factor, rec2d.height * factor);
    }

    private Shape getRotatedShape() {
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angle), rec2d.getCenterX(), rec2d.getCenterY());
        Shape rotated_shp = at.createTransformedShape(rec2d);
        return rotated_shp;
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            this.angle = angleInDegrees;
        }
    }

    @Override
    public double getRotation() {
        if (!isRotable()) {
            return 0;
        } else {
            return angle;
        }
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return this.getBounds2D().intersects(visibleRect);
    }

    @Override
    public Rectangle2D.Double getParentInstance() {
        return rec2d;
    }

    @Override
    public ArrayList<Object> getMagicPoints() {
        double size = 10;
        //need to apply the rotation to magic points
        ArrayList<Object> magic_pts = new ArrayList<Object>();
        /*
         * the magic points are at the center of each bond
         */
        magic_pts.add(new MyEllipse2D.Double(rec2d.x + rec2d.width / 2. - size / 2., rec2d.y - size / 2., size, size));//center up
        magic_pts.add(new MyEllipse2D.Double(rec2d.x + rec2d.width - size / 2., rec2d.y + rec2d.height / 2. - size / 2., size, size));//right center
        magic_pts.add(new MyEllipse2D.Double(rec2d.x + rec2d.width / 2. - size / 2., rec2d.y + rec2d.height - size / 2., size, size)); //bottom center
        magic_pts.add(new MyEllipse2D.Double(rec2d.x - size / 2., rec2d.y + rec2d.height / 2. - size / 2., size, size));//left center

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
        double widthDif = Math.abs(width - rec2d.width);
        double heightDif = Math.abs(height - rec2d.height);

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
            rec2d = new Rectangle2D.Double(neo_center.x - width / 2., neo_center.y - height / 2., width, height);
        } else {
            rec2d = new Rectangle2D.Double(x, y, width, height);
        }
    }

    @Override
    public void setLastCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getMaxX() - pos.x, bounding.getMaxY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setFirstCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - pos.x, bounding.getY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    public void setFirstCorner() {
        setFirstCorner(new Point2D.Double());
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
    public void setCenter(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getCenterX() - pos.x, bounding.getCenterY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        if (!keepAR) {
            rec2d.width = width;
        } else {
            double factor = width / rec2d.getWidth();
            rec2d.width = width;
            rec2d.height = rec2d.height * factor;
        }
    }

    @Override
    public void setShapeHeight(double height, boolean keepAR) {
        if (!keepAR) {
            rec2d.height = height;
        } else {
            double factor = height / rec2d.getHeight();
            rec2d.height = height;
            rec2d.width = rec2d.width * factor;
        }
    }

    /**
     *
     * @param width
     * @param height
     * @param keepAR
     */
    public void setShapeWidthOrHeight(double width, double height, boolean keepAR) {
        throw new Error("TODO");
    }

    @Override
    public double getShapeWidth() {
        return rec2d.getWidth();
    }

    @Override
    public double getShapeHeight() {
        return rec2d.getHeight();
    }

    @Override
    public double getArea() {
        double area = rec2d.width * rec2d.height;
        return area;
    }

    @Override
    public double getAreaFast() {
        return getArea();
    }

    @Override
    public Polygon getPolygon() {
        int[] x = new int[4];
        int[] y = new int[4];
        x[0] = (int) rec2d.x;
        x[1] = (int) (rec2d.x + rec2d.width);
        x[2] = (int) rec2d.x;
        x[3] = (int) (rec2d.x + rec2d.width);
        y[0] = (int) rec2d.y;
        y[1] = (int) rec2d.y;
        y[2] = (int) (rec2d.y + rec2d.height);
        y[3] = (int) (rec2d.y + rec2d.height);
        return new Polygon(x, y, x.length);
    }

    @Override
    public void flipHorizontally() {
        /*
         * rien a faire
         */
    }

    @Override
    public void flipVertically() {
        /*
         * rien a faire
         */
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        String text = "rectangle\nx:" + rec2d.x + "\ny:" + rec2d.y + "\nw:" + rec2d.getWidth() + "\nh:" + rec2d.getHeight();
        drawText(g2d, text, position);
    }

    @Override
    public void drawText(Graphics2D g2d, String text, int position) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(strokeSize));

        Shape shp = g2d.getStroke().createStrokedShape(rec2d);
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
        return "Rectangle";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.RECTANGLE;
    }

    @Override
    public Point2D.Double[] getExtremePoints() {
        Point2D.Double[] extreme_pts = new Point2D.Double[2];
        extreme_pts[0] = getFirstCorner();
        extreme_pts[1] = getLastCorner();
        return extreme_pts;
    }

    @Override
    public int getIJType() {
        return IJCompatibility.IJ_RECT;
    }

    @Override
    public Roi getIJRoi() {
        Roi IJ_ROI = new Roi((int) rec2d.x, (int) rec2d.y, (int) rec2d.width, (int) rec2d.height);
        return IJ_ROI;
    }

    @Override
    public void erode(int nb_erosions) {
        double x = rec2d.x;
        double y = rec2d.y;
        double width = rec2d.width;
        double height = rec2d.height;
        x += nb_erosions;
        y += nb_erosions;
        /*
         * we make it that it can't be smaller than 1px
         */
        width -= nb_erosions * 2;
        height -= nb_erosions * 2;
        if (width < 1) {
            width = 1;
            x = rec2d.getCenterX() - 0.5;
        }
        if (height < 1) {
            height = 1;
            y = rec2d.getCenterY() - 0.5;
        }
        rec2d = new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public void erode() {
        erode(1);
    }

    @Override
    public void dilate(int nb_dilatations) {
        double x = rec2d.x;
        double y = rec2d.y;
        double width = rec2d.width;
        double height = rec2d.height;
        x -= nb_dilatations;
        y -= nb_dilatations;
        width += nb_dilatations * 2;
        height += nb_dilatations * 2;
        rec2d = new Rectangle2D.Double(x, y, width, height);
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
            return getRotatedShape().getBounds2D().contains(p);
        }
        return rec2d.contains(p);
    }

    @Override
    public boolean contains(Point2D p) {
        if (angle != 0) {
            return getRotatedShape().getBounds2D().contains(p);
        }
        return rec2d.contains(p);
    }

    @Override
    public boolean contains(Rectangle2D r2) {
        if (angle != 0) {
            return getRotatedShape().getBounds2D().contains(r2);
        }
        return rec2d.contains(r2);
    }

    @Override
    public boolean contains(double x, double y) {
        if (angle != 0) {
            return getRotatedShape().getBounds2D().contains(x, y);
        }
        return rec2d.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        if (angle != 0) {
            return getRotatedShape().getBounds2D().contains(x, y, w, h);
        }
        return rec2d.contains(x, y, w, h);
    }

    @Override
    public boolean isRotable() {
        return true;
    }

    @Override
    public String toString() {
        return "<html><center>" + CommonClassesLight.roundNbAfterComma(getCenter().x, 1) + " " + CommonClassesLight.roundNbAfterComma(getCenter().y, 1) + " " + getShapeName() + " <font color=" + CommonClassesLight.toHtmlColor(color) + ">contour</font>" + ((this instanceof Fillable) ? " <font color=" + CommonClassesLight.toHtmlColor(fillColor) + ">fill</font>" : "") + "</html>";
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
        MyRectangle2D.Double test = new MyRectangle2D.Double(256, 256, 120, 90);

        /*
         * test rotation
         */
        if (true) {
            /*
             * ca marche et pas besoin de sur rotations
             */
            test.translate(56, -23);
            int count = 0;
            BufferedImage[] tmp = new BufferedImage[360];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
                Graphics2D g3d = tmp[i].createGraphics();
                test.angle = count;
                test.draw(g3d);
                g3d.dispose();
                count++;
            }
            SaverLight.popJLong(8000, tmp);
            System.exit(0);
        }

        if (false) {
            MyRectangle2D.Double test3 = new MyRectangle2D.Double(256, 256, 40, 60);

            System.out.println(test + " " + test3); //-->sont tous identiques --> big pb
//              System.out.println(test.print() +" "+test3.print());
            System.out.println(test.clone() + " " + test3.clone());
            test = (MyRectangle2D.Double) test.clone();

//            System.out.println(test.print() +" "+test3.print());
            System.out.println(test.clone() + " " + test3.clone());

            test.drawShapeInfo(g2d, Drawable.LEFT);

            System.out.println(test instanceof Editable);
        }

        test.draw(g2d);
        g2d.dispose();
        SaverLight.popJLong(5000, test2);
//        Saver2.poplong(test2);

//      test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

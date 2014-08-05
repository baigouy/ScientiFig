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
import Commons.MyBufferedImage;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import org.apache.batik.ext.awt.geom.Polygon2D;

/**
 * MyPolygon2D is a polygonal vectorial object that extends the apache Polygon2D
 * class
 *
 * @since <B>Packing Analyzer 8.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyPolygon2D extends Polygon2D implements PARoi, LineStrokable, Serializable, Callable<Object> {

    /**
     * Variables
     */
    public static final long serialVersionUID = -4795923230906927409L;
    Point2D.Double[] registrationPoints;
    public double minimum_distance_between_points = -1;
    public static final double sqrt = 2. - Math.sqrt(2.);
    public int color = 0xFFFF00;
    float transparency = 1.f;
    float strokeSize = 0.65f;
    public boolean isTransparent = false;
    public Polygon2D p2d;
    public int ZstackPos = 0;
    public Object[] callableParameters = null;
    /*
     * line stroke variables
     */
    public int LINESTROKE = 0;
    public int dashSize = 6;
    public int dotSize = 1;
    public int skipSize = 6;
    /**
     * for rotations
     */
    double angle;

    /**
     * a double precision MyPolygon2D
     */
    public static class Double extends MyPolygon2D implements Serializable {

        public static final long serialVersionUID = -6259937388282345606L;

        public Double() {
            p2d = new Polygon2D();
        }

        /**
         * Constructor
         *
         * @param p2d
         */
        public Double(Polygon2D p2d) {
            this.p2d = p2d;
        }

        /**
         * Constructor
         *
         * @param r2d
         */
        public Double(Rectangle2D r2d) {
            p2d = new Polygon2D(r2d);
        }

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param n
         */
        public Double(int[] x, int[] y, int n) {
            p2d = new Polygon2D(x, y, n);
        }

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param n
         */
        public Double(float[] x, float[] y, int n) {
            p2d = new Polygon2D(x, y, n);
        }

        /**
         * Constructor
         *
         * @param pts
         */
        public Double(ArrayList<Point> pts) {
            float[] x = new float[pts.size()];
            float[] y = new float[pts.size()];
            for (int i = 0; i < pts.size(); i++) {
                Point point = pts.get(i);
                x[i] = point.x;
                y[i] = point.y;
            }
            p2d = new Polygon2D(x, y, x.length);
        }

        public Double(Point2D.Double... pts) {
            int null_counter = 0;
            for (Point2D.Double double1 : pts) {
                if (double1 == null) {
                    null_counter++;
                }
            }
            float[] x = new float[pts.length - null_counter];
            float[] y = new float[pts.length - null_counter];
            int i = 0;
            for (Point2D.Double point : pts) {
                if (point == null) {
                    continue;
                }
                x[i] = (float) point.x;
                y[i] = (float) point.y;
                i++;
            }
            p2d = new Polygon2D(x, y, x.length);
        }

        /**
         * Constructor
         *
         * @param line_segs
         */
        public Double(LinkedHashSet<Line2D.Double> line_segs) {
            convertSegments2Polygon(line_segs);
        }

        /**
         * Constructor
         *
         * @param p
         */
        public Double(Polygon p) {
            float[] x = new float[p.npoints];
            float[] y = new float[p.npoints];
            for (int i = 0; i < p.npoints; i++) {
                x[i] = p.xpoints[i];
                y[i] = p.ypoints[i];
            }
            p2d = new Polygon2D(x, y, x.length);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyPolygon2D.Double myel) {
            this.p2d = myel.p2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.isTransparent = myel.isTransparent;
            this.transparency = myel.transparency;
            this.LINESTROKE = myel.LINESTROKE;
            this.dashSize = myel.dashSize;
            this.dotSize = myel.dotSize;
            this.skipSize = myel.skipSize;
            this.angle = myel.angle;
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

        @Override
        public Object clone() {
            return new MyPolygon2D.Double(this).setZpos(ZstackPos);
        }
    }

    @Override
    public Object call() throws Exception {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
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
     * A class that converts a series of overlapping line segments to a polygon
     *
     * @param line_segs
     */
    public void convertSegments2Polygon(LinkedHashSet<Line2D.Double> line_segs) {
        float[] x = new float[line_segs.size() + 1];
        float[] y = new float[line_segs.size() + 1];
        int i = 0;
        Line2D.Double last_line = null;
        for (Line2D.Double double1 : line_segs) {
            x[i] = (float) double1.getP1().getX();
            y[i] = (float) double1.getP1().getY();
            if (i == line_segs.size() - 1) {
                last_line = double1;
            }
            i++;
        }
        if (last_line != null) {
            x[x.length - 1] = (float) last_line.getP2().getX();
            y[y.length - 1] = (float) last_line.getP2().getY();
        }
        p2d = new Polygon2D(x, y, x.length);
    }

    /**
     * Add a new point to the polygon
     *
     * @param pt
     */
    public void addPoint(Point pt) {
        p2d.addPoint(pt.x, pt.y);
    }

    /**
     * Add a new point to the polygon
     *
     * @param pt
     */
    public void addPoint(Point2D.Double pt) {
        p2d.addPoint(pt);
    }

    /**
     * Add a new point to the polygon
     *
     * @param x
     * @param y
     */
    @Override
    public void addPoint(float x, float y) {
        p2d.addPoint(x, y);
    }

    @Override
    public int getColor() {
        return color & 0x00FFFFFF;
    }

    @Override
    public void setColor(int color) {
        this.color = color & 0x00FFFFFF;
    }

    /**
     *
     * @return the first point of the polygon
     */
    public Point2D.Double getFirstPoint() {
        return new Point2D.Double(p2d.xpoints[0], p2d.ypoints[0]);
    }

    /**
     *
     * @param pos
     * @return the point at position 'pos' in the polygon or null if no point is
     * found
     */
    public Point2D.Double getPoint(int pos) {
        if (pos < 0 || pos > p2d.npoints) {
            return null;
        }
        return new Point2D.Double(p2d.xpoints[pos], p2d.ypoints[pos]);
    }

    /**
     *
     * @return the number of points/vertices of the polygon
     */
    public int getNbOfPoints() {
        return p2d.npoints;
    }

    @Override
    public int getColorIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColorOut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getTransparency() {
        return transparency;
    }

    @Override
    public void setTransparency(float transparency) {
        this.transparency = transparency;
        if (transparency != 1.f) {
            isTransparent = true;
        } else {
            isTransparent = false;
        }
    }

    @Override
    public boolean intersects(Rectangle r) {
        return p2d.intersects(r);
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
    public double getAreaFast() {
        Rectangle2D bounds = getBounds2D();
        return bounds.getWidth() * bounds.getHeight();
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
    public boolean isTransparent() {
        return isTransparent;
    }

    @Override
    public Rectangle getBounds() {
        return p2d.getBounds();
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return p2d.getPathIterator(at);
    }

    @Override
    public boolean contains(Point2D pd) {
        return p2d.contains(pd);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return p2d.getBounds2D();
    }

    @Override
    public boolean contains(Point point) {
        return p2d.contains(point);
    }

    @Override
    public boolean contains(Rectangle2D rd) {
        return p2d.contains(rd);
    }

    /**
     *
     * @param x
     * @param y
     * @return true if the polygon contains the point
     */
    @Override
    public boolean contains(int x, int y) {
        return p2d.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        return p2d.contains(x, y);
    }

    @Override
    public boolean contains(double d, double y, double w, double h) {
        return p2d.contains(d, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D rd) {
        return p2d.intersects(rd);
    }

    @Override
    public boolean intersects(double d, double d1, double d2, double d3) {
        return p2d.intersects(d, d1, d2, d3);
    }

    @Override
    public void drawSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(1));
        g2d.fill(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void draw(Graphics2D g2d, int color, float transparency, float strokeSize) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.draw(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fill(Graphics2D g2d, int color, float transparency, float strokeSize) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.fill(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSize) {
        draw(g2d, color, transparency, strokeSize);
        fill(g2d, color, transparency, strokeSize);
    }

    @Override
    public void drawTransparent(Graphics2D g2d, float transparency) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.draw(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillTransparent(Graphics2D g2d, float transparency) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.fill(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void drawAndFillTransparent(Graphics2D g2d, float transparency) {
        drawTransparent(g2d, transparency);
        fillTransparent(g2d, transparency);
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
    public void drawAndFillTransparent(Graphics2D g2d) {
        drawTransparent(g2d);
        fillTransparent(g2d);
    }

    @Override
    public void draw(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(getLineStroke());
        g2d.draw(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fill(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.fill(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.fill(p2d);
        g2d.draw(p2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void draw(Graphics2D g2d, int color) {
        int bckup = this.color;
        this.color = color;
        draw(g2d);
        this.color = bckup;
    }

    @Override
    public void fill(Graphics2D g2d, int color) {
        int bckup = this.color;
        this.color = color;
        fill(g2d);
        this.color = bckup;
    }

    @Override
    public void drawAndFill(Graphics2D g2d, int color) {
        int bckup = this.color;
        this.color = color;
        drawAndFill(g2d);
        this.color = bckup;
    }

    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        drawIfVisible(g2d, visibleRect);
    }

    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visibleRect) {
        if (p2d.intersects(visibleRect)) {
            draw(g2d);
        }
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
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        float[] xvals = p2d.xpoints;
        float[] yvals = p2d.ypoints;
        for (int i = 0; i < p2d.npoints; i++) {
            xvals[i] += (float) x;
            yvals[i] += (float) y;
        }
        p2d = new Polygon2D(xvals, yvals, p2d.npoints);
    }

    @Override
    public void scale(double factor) {
        ArrayList<Line2D.Double> rescaled = new ArrayList<Line2D.Double>();
        ArrayList<Line2D.Double> polyline = getLineSegments();
        for (Line2D.Double l2d : polyline) {
            l2d = new Line2D.Double(l2d.x1 * factor, l2d.y1 * factor, l2d.x2 * factor, l2d.y2 * factor);
            rescaled.add(l2d);
        }
        convertSegments2Polygon(new LinkedHashSet<Line2D.Double>(rescaled));
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            double angleDiff = angleInDegrees - angle;
            ArrayList<Line2D.Double> rotated = new ArrayList<Line2D.Double>();
            ArrayList<Line2D.Double> polyline = getLineSegments();
            for (Line2D.Double l2d : polyline) {
                l2d = new Line2D.Double(CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP1(), getCenter(), Math.toRadians(angleDiff)), CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP2(), getCenter(), Math.toRadians(angleDiff)));
                rotated.add(l2d);
            }
            convertSegments2Polygon(new LinkedHashSet<Line2D.Double>(rotated));
            angle = angleInDegrees;
        }
    }

    @Override
    public Polygon2D getParentInstance() {
        return p2d;
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return p2d.getBounds2D().intersects(visibleRect);
    }

    /**
     *
     * @return the length of the smallest line segment of the polygon
     */
    public double getMinimum_distance_between_points() {
        return minimum_distance_between_points;
    }

    /**
     * Sets the length for a line segment in the polygon
     *
     * @param minimum_distance_between_points
     */
    public void setMinimum_distance_between_points(double minimum_distance_between_points) {
        this.minimum_distance_between_points = minimum_distance_between_points;
    }

    @Override
    public ArrayList<Object> getMagicPoints() {
        double size = 10.;
        if (minimum_distance_between_points <= 1.) {
            ArrayList<Object> magic_pts = new ArrayList<Object>();
            /*
             * the magic points are at the center of each bond
             */
            float[] x = p2d.xpoints;
            float[] y = p2d.ypoints;
            for (int i = 0; i < p2d.npoints; i++) {
                magic_pts.add(new MyEllipse2D.Double(x[i], y[i], size));
            }
            return magic_pts;
        } else {
            /*
             * we calculate cumulative distance sum between points to see if we
             * can simplifyLossless the contour or not
             */
            ArrayList<Point2D.Double> magic_pts = new ArrayList<Point2D.Double>();
            ArrayList<Point2D.Double> simplified_contour = new ArrayList<Point2D.Double>();
            float[] x = p2d.xpoints;
            float[] y = p2d.ypoints;
            for (int i = 0; i < p2d.npoints; i++) {
                magic_pts.add(new Point2D.Double(x[i], y[i]));
            }
            ArrayList<java.lang.Double> dist = compute_cumulative_distance();
            int pos = 0;
            simplified_contour.add(magic_pts.get(0));
            for (int i = 1; i < magic_pts.size(); i++) {
                double real_dist = dist.get(i) - dist.get(pos);
                if (real_dist >= minimum_distance_between_points) {
                    /*
                     * if the distance between 2 points is too big then we gonna
                     * split the line in 2 in order not to oversimplify too much
                     * the contour
                     */
                    if (real_dist >= 2 * minimum_distance_between_points) {
                        simplified_contour.add(new Point2D.Double((magic_pts.get(pos).x + magic_pts.get(i).x) / 2., (magic_pts.get(pos).y + magic_pts.get(i).y) / 2.));
                        simplified_contour.add(magic_pts.get(i));
                        pos = i;
                    } else {
                        simplified_contour.add(magic_pts.get(i));
                        pos = i;
                    }
                }
            }
            simplified_contour.add(magic_pts.get(magic_pts.size() - 1));
            simplified_contour = CommonClassesLight.remove_doublons_n_keep_order(simplified_contour);
            minimum_distance_between_points = 0.;
            ArrayList<Object> data = new ArrayList<Object>();
            for (Point2D.Double double1 : simplified_contour) {
                data.add(new MyEllipse2D.Double(double1, size));
            }
            return data;
        }
    }

    /**
     * This function tries to simplify the contour of the polygon (fuses line
     * segments that could belong to the same line) without losing information
     *
     * @param allowMinorloss if true minor changes to the contour can be made
     */
    public void simplifyLossless(boolean allowMinorloss) {
        simplifyLossless1(allowMinorloss);
        simplifyLossless1(allowMinorloss);
    }

    private void simplifyLossless1(boolean allowMinorloss) {
        /*
         * we calculate cumulative distance sum between points to see if we can
         * simplifyLossless the contour or not
         */
        double size = 10.;
        ArrayList<Point2D.Double> magic_pts = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> simplified_contour = new ArrayList<Point2D.Double>();
        float[] x = p2d.xpoints;
        float[] y = p2d.ypoints;
        for (int i = 0; i < p2d.npoints; i++) {
            magic_pts.add(new Point2D.Double(x[i], y[i]));
        }
        ArrayList<java.lang.Double> dist = compute_cumulative_distance();
        int pos = 0;

        simplified_contour.add(magic_pts.get(0));
        for (int i = 0; i < p2d.npoints; i++) {
            double cur_dist_line = new Point2D.Double(x[pos], y[pos]).distance(new Point2D.Double(x[i], y[i]));
            double real_dist = dist.get(i) - dist.get(pos);
            /*
             * if the contour can be approximated by a line we convert it to a line
             */
            if (real_dist != cur_dist_line) {
                /*
                 * this simplifies even more but very small mistakes --> think
                 * about it one day --> the pb is that there are very likely
                 * some small random mistakes of rounding the doubles
                 */
                if (allowMinorloss) {
                    if (Math.abs(real_dist - cur_dist_line) < sqrt) {
                        continue;
                    }
                }
                pos = i - 1;
                simplified_contour.add(magic_pts.get(i - 1));
            }
        }
        simplified_contour.add(magic_pts.get(magic_pts.size() - 1));
        simplified_contour = CommonClassesLight.remove_doublons_n_keep_order(simplified_contour);
        Collections.rotate(simplified_contour, simplified_contour.size() / 2);
        ArrayList<Object> data = new ArrayList<Object>();
        for (Point2D.Double double1 : simplified_contour) {
            data.add(new MyEllipse2D.Double(double1, size));
        }
        setMagicPoints(data);
    }

    /**
     *
     * @return a series of lines segments that belong to the contour of the
     * polygon
     */
    public ArrayList<Line2D.Double> getLineSegments() {
        ArrayList<Line2D.Double> segments = new ArrayList<Line2D.Double>();
        ArrayList<Point2D.Double> contour = getContour();
        for (int i = 1; i < contour.size(); i++) {
            Point2D.Double base = contour.get(i - 1);
            Point2D.Double tip = contour.get(i);
            segments.add(new Line2D.Double(base, tip));
        }
        segments.add(new Line2D.Double(contour.get(contour.size() - 1), contour.get(0)));
        return segments;
    }

    /**
     *
     * @return the points of the contour of the polygon
     */
    public ArrayList<Point2D.Double> getContour() {
        ArrayList<Point2D.Double> contour = new ArrayList<Point2D.Double>();
        float[] x = p2d.xpoints;
        float[] y = p2d.ypoints;
        for (int i = 0; i < p2d.npoints; i++) {
            contour.add(new Point2D.Double(x[i], y[i]));
        }
        return contour;
    }

    private ArrayList<java.lang.Double> compute_cumulative_distance() {
        ArrayList<Point2D.Double> pts = getContour();
        ArrayList<java.lang.Double> cumul = new ArrayList<java.lang.Double>();
        java.lang.Double total_dist = 0.;
        cumul.add(total_dist);
        for (int i = 1; i < pts.size(); i++) {
            double cur_dist = pts.get(i).distance(pts.get(i - 1));
            total_dist += cur_dist;
            cumul.add(total_dist);
        }
        return cumul;
    }

    @Override
    public void setMagicPoints(ArrayList<Object> magicPoints) {
        int size = magicPoints.size();
        float[] x = new float[size];
        float[] y = new float[size];
        for (int i = 0; i < size; i++) {
            Point2D.Double cur_pt = (((MyEllipse2D.Double) magicPoints.get(i)).getPoint());
            x[i] = (float) cur_pt.x;
            y[i] = (float) cur_pt.y;
        }
        p2d = new Polygon2D(x, y, size);
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
        System.out.println("Not supported yet.");
    }

    @Override
    public void setShapeHeight(double width, boolean keepAR) {
        System.out.println("Not supported yet.");
    }

    @Override
    public double getShapeWidth() {
        return p2d.getBounds2D().getWidth();
    }

    @Override
    public double getShapeHeight() {
        return p2d.getBounds2D().getHeight();
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public Polygon getPolygon() {
        int[] x = new int[p2d.npoints];
        int[] y = new int[p2d.npoints];
        for (int i = 0; i < p2d.npoints; i++) {
            x[i] = (int) p2d.xpoints[i];
            y[i] = (int) p2d.ypoints[i];
        }
        return new Polygon(x, y, x.length);
    }

    @Override
    public void flipHorizontally() {
        Rectangle2D r = p2d.getBounds2D();
        double center = r.getCenterX();
        for (int i = 0; i < p2d.npoints; i++) {
            double dist_to_center = p2d.xpoints[i] - center;
            if (dist_to_center < 0) {
                dist_to_center = Math.abs(dist_to_center);
            } else {
                dist_to_center = -dist_to_center;
            }
            p2d.xpoints[i] = (float) (center + dist_to_center);
        }
        p2d = new Polygon2D(p2d.xpoints, p2d.ypoints, p2d.npoints);
    }

    @Override
    public void flipVertically() {
        Rectangle2D r = p2d.getBounds2D();
        double center = r.getCenterY();
        for (int i = 0; i < p2d.npoints; i++) {
            double dist_to_center = p2d.ypoints[i] - center;
            if (dist_to_center < 0) {
                dist_to_center = Math.abs(dist_to_center);
            } else {
                dist_to_center = -dist_to_center;
            }
            p2d.ypoints[i] = (float) (center + dist_to_center);
        }
        p2d = new Polygon2D(p2d.xpoints, p2d.ypoints, p2d.npoints);
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        String text = "polygon\nn:" + p2d.npoints;
        drawText(g2d, text, position);
    }

    @Override
    public void drawText(Graphics2D g2d, String text, int position) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(strokeSize));
        Shape shp = g2d.getStroke().createStrokedShape(this.getBounds2D());
        Rectangle2D polygonBounds = shp.getBounds2D();
        String[] splitted_text = text.split("\n");
        switch (position) {
            case BELOW:
                if (true) {
                    float last_height = (float) (polygonBounds.getY() + polygonBounds.getHeight());
                    for (String string : splitted_text) {
                        last_height += CommonClassesLight.getTextHeight(g2d);
                        g2d.drawString(string, (float) polygonBounds.getX(), last_height);
                    }
                }
                break;
            case ABOVE:
                if (true) {
                    float last_height = (float) (polygonBounds.getY() - CommonClassesLight.getTextHeight(g2d) * splitted_text.length - strokeSize);
                    for (String string : splitted_text) {
                        last_height += CommonClassesLight.getTextHeight(g2d);
                        g2d.drawString(string, (float) polygonBounds.getX(), last_height);
                    }
                }
                break;
            case RIGHT:
                if (true) {
                    float last_height = (float) (polygonBounds.getY());
                    for (String string : splitted_text) {
                        g2d.drawString(string, (float) (polygonBounds.getX() + polygonBounds.getWidth() + strokeSize), last_height);
                        last_height += CommonClassesLight.getTextHeight(g2d);
                    }
                }
                break;
            case LEFT:
                if (true) {
                    float last_height = (float) (polygonBounds.getY());
                    float max_text_width = 0;
                    for (String string : splitted_text) {
                        float cur_text_with = CommonClassesLight.getTextWidth(g2d, string);
                        max_text_width = cur_text_with > max_text_width ? cur_text_with : max_text_width;
                    }
                    for (String string : splitted_text) {
                        float cur_text_with = CommonClassesLight.getTextWidth(g2d, string);
                        g2d.drawString(string, (float) (polygonBounds.getX() - max_text_width + (max_text_width - cur_text_with) - strokeSize), last_height);
                        last_height += CommonClassesLight.getTextHeight(g2d);
                    }
                }
                break;
        }
        g2dparams.restore(g2d);
    }

    @Override
    public String getShapeName() {
        return "Polygon";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.POLYGON;
    }

    @Override
    public Point2D.Double[] getExtremePoints() {
        Point2D.Double[] extreme_pts = new Point2D.Double[2];
        extreme_pts[0] = getFirstCorner();
        extreme_pts[1] = getLastCorner();
        return extreme_pts;
    }

    public void setCallableParameters(Object... parameters) {
        this.callableParameters = parameters;
    }

    @Override
    public int getIJType() {
        return IJCompatibility.IJ_POLYGON;
    }

    @Override
    public Roi getIJRoi() {
        Polygon p = getPolygon();
        Roi IJ_ROI = new PolygonRoi(p, getIJType());
        return IJ_ROI;
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
        int[] x = {10, 12, 14, 16, 15, 8};
        int[] y = {10, 8, 8, 36, 38, 50};

//        int[] x = {10, 20, 30, 40,50,60};
//        int[] y = {10, 10, 10, 10,10,10};    

//        int[] x = {10, 10, 10, 10, 10, 10};
//        int[] y = {10, 20, 30, 40, 50, 60};


        //int[] x = {10, 20, 30, 34, 40, 50, 60};
        //int[] y = {10, 20, 30, 34, 40, 50, 60};

        //MyPolygon2D test = new MyPolygon2D.Double(x, y, 6);
        MyPolygon2D.Double test = new MyPolygon2D.Double(x, y, x.length);
        //test.simplifyLossless(true);
        System.out.println(test.getContour());

//        test.getSortedContour(test.getSortedContour()); //ca a l'air de marcher et ce serait threadable
        //--> a tester

        //reflechir a comment faire ca depuis un flood


        test.draw(g2d);
        g2d.dispose();
//        Saver.poplong(test2);

//      test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



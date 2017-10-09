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
import Commons.SaverLight;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/*
 * TODO (low priority) clean the 
 * if (loading)
 * {
 *  return;
 * }
 * because there are many that are useless now
 */
/**
 * MyPolyline2D is a vectorial object that corresponds to an open polygon (i.e.
 * a series of interconnected line segments)
 *
 * @since <B>Packing Analyzer 8.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyPolyline2D implements PARoi, Contourable, LineStrokable, Morphable, Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6325645830640476692L;
    public ArrayList<Line2D.Double> polyline;
    public Rectangle2D.Double rec2d;
    boolean is_real_contain = false;
    int color = 0xFFFF00;
    float opacity = 1.f;
    float strokeSize = 0.65f;
    public int ZstackPos = 0;
    public static final double sqrt = 2. - Math.sqrt(2.);
    /*
     * line stroke variables
     */
    public int LINESTROKE = 0;
    public int dashSize = 6;
    public int dotSize = 1;
    public int skipSize = 6;
    /**
     * for rotation
     */
    double angle;

    /**
     * a double precision MyPolyline2D
     */
    public static class Double extends MyPolyline2D implements Serializable {

        public static final long serialVersionUID = 1776233867505009092L;

        /**
         * converts an IJ rect ROI to a SF one
         *
         * @param ijRoi
         */
        public Double(Roi ijRoi) {
            /**
             * may fail if type error
             */
            this(((PolygonRoi) ijRoi).getPolygon());
            Color strokeCol = ijRoi.getStrokeColor();
            if (strokeCol != null) {
                color = strokeCol.getRGB();
            }
            strokeSize = ijRoi.getStrokeWidth();
        }

        /**
         * Constructor
         *
         * @param X
         * @param Y
         */
        public Double(double X, double Y) {
            this(X, Y, X, Y);
        }

        /**
         * Constructor
         */
        public Double() {
            polyline = new ArrayList<Line2D.Double>();
        }

        /**
         * Constructor
         *
         * @param pt1
         * @param pt2
         */
        public Double(Point2D.Double pt1, Point2D.Double pt2) {
            this(pt1.x, pt1.y, pt2.x, pt2.y);
        }

        /**
         * Constructor
         *
         * @param pt1
         * @param pt2
         */
        public Double(Point pt1, Point pt2) {
            this(pt1.x, pt1.y, pt2.x, pt2.y);
        }

        /**
         * Constructor
         *
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         */
        public Double(double x1, double y1, double x2, double y2) {
            polyline = new ArrayList<Line2D.Double>();
            polyline.add(new Line2D.Double(x1, y1, x2, y2));
            updateMasterRect();
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Line2D.Double l2d) {
            this(l2d.x1, l2d.y1, l2d.x2, l2d.y2);
        }

        /**
         * Constructor
         *
         * @param <E>
         * @param vals
         */
        public <E> Double(ArrayList<E> vals) {
            if (!vals.isEmpty()) {
                Object val = vals.get(0);
                if (val instanceof Line2D.Double) {
                    this.polyline = (ArrayList<Line2D.Double>) vals;
                } else if (val instanceof Point) {
                    ArrayList<Point> pts = (ArrayList<Point>) vals;
                    polyline = new ArrayList<Line2D.Double>();
                    if (pts.size() != 1) {
                        for (int i = 1; i < pts.size(); i++) {
                            Point before = pts.get(i - 1);
                            Point cur = pts.get(i);
                            polyline.add(new Line2D.Double(before, cur));
                        }
                    } else {
                        polyline.add(new Line2D.Double(pts.get(0), pts.get(0)));
                    }
                }
                updateMasterRect();
            }
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyPolyline2D.Double myel) {
            this.rec2d = myel.rec2d;
            this.polyline = myel.polyline;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.opacity = myel.opacity;
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
            polyline = new ArrayList<Line2D.Double>();
            for (int i = 1; i < p.npoints; i++) {
                int x1 = p.xpoints[i - 1];
                int x2 = p.xpoints[i];
                int y1 = p.ypoints[i - 1];
                int y2 = p.ypoints[i];
                polyline.add(new Line2D.Double(x1, y1, x2, y2));
            }
            updateMasterRect();
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

    @Override
    public Object clone() {
        /*
         * This step (the duplication of line segments) is necessary otherwise the cloning fails
         */
        ArrayList<Line2D.Double> cloned_line = new ArrayList<Line2D.Double>();
        for (Line2D.Double double1 : polyline) {
            cloned_line.add(new Line2D.Double(double1.x1, double1.y1, double1.x2, double1.y2));
        }
        MyPolyline2D.Double r2d = new MyPolyline2D.Double(cloned_line);
        r2d.color = color;
        r2d.opacity = opacity;
        r2d.ZstackPos = ZstackPos;
        r2d.strokeSize = strokeSize;
        r2d.rec2d = rec2d;
        r2d.LINESTROKE = LINESTROKE;
        r2d.dashSize = dashSize;
        r2d.dotSize = dotSize;
        r2d.skipSize = skipSize;
        r2d.angle = angle;
        return (MyPolyline2D.Double) r2d;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return rec2d;
    }

    @Override
    public Rectangle getBounds() {
        return rec2d.getBounds();
    }

    @Override
    public boolean intersects(Rectangle r) {
        return rec2d.intersects(r);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return rec2d.intersects(r);
    }

    /**
     *
     * @return true if the polyline is empty
     */
    public boolean isEmpty() {
        return polyline == null || polyline.isEmpty();
    }

    /**
     * Sets the last point of the polyline and updates the bounding rectangle
     *
     * @param pt
     */
    public void setLastLine2D(Point2D.Double pt) {
        Point2D last_pt = polyline.get(polyline.size() - 1).getP1();
        polyline.set(polyline.size() - 1, new Line2D.Double(last_pt, pt));
        updateMasterRect();
    }

    /**
     * Sets the line segments of the polyline
     *
     * @param polyline
     */
    public void setLine2D(ArrayList<Line2D.Double> polyline) {
        this.polyline = polyline;
        updateMasterRect();
    }

    /**
     * Add new point to the current polyline (a new line will be created between
     * this point and the previous one)
     *
     * @param X
     * @param Y
     */
    public void addPoint(double X, double Y) {
        Point2D.Double pt = new Point2D.Double(X, Y);
        if (polyline != null && !polyline.isEmpty()) {
            Point2D last_pt = polyline.get(polyline.size() - 1).getP2();
            polyline.add(new Line2D.Double(last_pt, pt));
        } else {
            polyline = new ArrayList<Line2D.Double>();
            polyline.add(new Line2D.Double(pt, pt));
        }
        updateMasterRect();
    }

    /**
     * Add new point to the current polyline (a new line will be created between
     * this point and the previous one)
     *
     * @param pt
     */
    public void addPoint(Point2D.Double pt) {
        addPoint(pt.x, pt.y);
    }

    /**
     *
     * @return a general path corresponding to the polyline
     */
    public GeneralPath getPath() {
        ArrayList<Point> pts = getPoints();
        GeneralPath gp = new GeneralPath();
        Point first_pt = pts.get(0);
        gp.moveTo(first_pt.x, first_pt.y);
        for (Point point : pts) {
            gp.lineTo(point.x, point.y);
        }
        return gp;
    }

    /**
     *
     * @return a polygon (closed polyline)
     */
    public MyPolygon2D.Double finaliseAndConvertToPolygon2D() {
        finalise_line();
        return new MyPolygon2D.Double(new LinkedHashSet<Line2D.Double>(polyline));
    }

    /**
     * Finalizes the polyline
     */
    public void finalise_line() {
        if (polyline.get(polyline.size() - 1).getP1().equals(polyline.get(polyline.size() - 1).getP2())) {
            polyline.remove(polyline.size() - 1);//--> la premiere ligne n'existe pas
        }
        if (polyline.get(polyline.size() - 1).getP1().equals(polyline.get(polyline.size() - 1).getP2())) {
            polyline.remove(polyline.size() - 1);//--> la premiere ligne n'existe pas
        }
        updateMasterRect();
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
        for (Line2D.Double double1 : polyline) {
            g2d.draw(double1);
        }
        g2dparams.restore(g2d);
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        drawSkeletton(g2d, color);
    }

//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//     drawAndFill2d.draw(getPath());
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void fill(Graphics2D g2d, int color, float transparency, float strokeSize) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.fill(getPath());
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSizedrawAndFill//        draw(g2d, color, transparency, strokeSize);
//        fill(g2d, color, transparency, strokeSize);
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d, float transparency) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparedrawAndFill);
//        g2d.draw(getPath());
//        g2dparams.restore(g2d);
//    }
//
//    @Override
//    public void fillTransparent(Graphics2D g2d, float transparency) {
//        G2dParameters g2dparams = new G2dParameters(g2d);
//        g2d.setColor(new Color(color));
//        g2d.setStroke(new BasicStroke(strokeSize));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
//        g2d.fill(getPath());
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
        drawAndFill(g2d, opacity > 0f, true);
    }

    @Override
    public void fill(Graphics2D g2d) {
        drawAndFill(g2d, opacity > 0f, true);
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        drawAndFill(g2d, opacity > 0f, true);
    }

    private void drawAndFill(Graphics2D g2d, boolean drawContour, boolean forceShowShape) {
        if (drawContour) {
            forceShowShape = false;
            G2dParameters g2dparams = new G2dParameters(g2d);
            g2d.setColor(new Color(color));
            g2d.setStroke(getLineStroke());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            /*
             * small addition to handle rotation
             */
            g2d.draw(getPath());
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
            g2d.draw(getPath());
            g2d.draw(new Line2D.Double(r2d.getX(), r2d.getY(), r2d.getX() + r2d.getWidth(), r2d.getY() + r2d.getHeight()));
            g2dparams.restore(g2d);
        }
    }

//    @Override
//    public voidrawAndFillawAndFill(Graphics2D g2d) {
//        draw(g2drawAndFill//    }
//
//    @Override
//    public void draw(Graphics2D g2d, int color) {
//        int bckudrawAndFillthis.color;
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
        if (visibleRect.intersects(this.getBounds2D()) || visibleRect.contains(rec2d)) {
            drawAndFill(g2d);
        }
    }

    @Override
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        int counter = 0;
        for (Line2D.Double l2d : polyline) {
            l2d = new Line2D.Double(l2d.x1 + x, l2d.y1 + y, l2d.x2 + x, l2d.y2 + y);
            polyline.set(counter, l2d);
            counter++;
        }
        updateMasterRect();
    }

    @Override
    public void scale(double factor) {
        int counter = 0;
        for (Line2D.Double l2d : polyline) {
            l2d = new Line2D.Double(l2d.x1 * factor, l2d.y1 * factor, l2d.x2 * factor, l2d.y2 * factor);
            polyline.set(counter, l2d);
            counter++;
        }
        updateMasterRect();
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            double angleDiff = angleInDegrees - angle;
            int counter = 0;
            if (angleDiff != 0) {
                for (Line2D.Double l2d : polyline) {
                    l2d = new Line2D.Double(CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP1(), getCenter(), Math.toRadians(angleDiff)), CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP2(), getCenter(), Math.toRadians(angleDiff)));
                    polyline.set(counter, l2d);
                    counter++;
                }
                updateMasterRect();
            }
            angle = angleInDegrees;
        }
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return rec2d.getBounds2D().intersects(visibleRect);
    }

    @Override
    public boolean contains(Point2D p) {
        Rectangle r = rec2d.getBounds();
        Rectangle r_bigger = new Rectangle(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
        return r_bigger.contains(p);
    }

    @Override
    public ArrayList<Line2D.Double> getParentInstance() {
        return polyline;
    }

    /**
     * creates a bounding rect for the current polyline
     */
    public void updateMasterRect() {
        rec2d = null;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        for (Object object : polyline) {
            if (object != null) {
                if (object instanceof Shape) {
                    found = true;
                    Rectangle shp = ((Shape) object).getBounds();
                    double x = shp.x;
                    double y = shp.y;
                    double xend = shp.x + shp.width;
                    double yend = shp.y + shp.height;
                    min_x = x < min_x ? x : min_x;
                    min_y = y < min_y ? y : min_y;
                    max_x = xend > max_x ? xend : max_x;
                    max_y = yend > max_y ? yend : max_y;
                }
            }
        }
        if (!found) {
            return;
        }
        rec2d = new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    @Override
    public double getShapeWidth() {
        return rec2d.getBounds2D().getWidth();
    }

    @Override
    public double getShapeHeight() {
        return rec2d.getBounds2D().getHeight();
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        if (rec2d.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dottedSelection, 0f));
            g2d.draw(rec2d);
            g2dParams.restore(g2d);
        }
    }

    @Override
    public ArrayList<Object> getMagicPoints() {
        double size = 10;
        ArrayList<Object> sensitive_points = new ArrayList<Object>();
        for (Line2D.Double l2d : polyline) { //--> un pr deux ligne
            sensitive_points.add(new MyEllipse2D.Double(new Point2D.Double(l2d.x1, l2d.y1), size));
        }
        sensitive_points.add(new MyEllipse2D.Double((Point2D.Double) polyline.get(polyline.size() - 1).getP2(), size));
        return sensitive_points;
    }

    @Override
    public void setMagicPoints(ArrayList<Object> magicPoints) {
        for (int i = 0, j = 0; i < magicPoints.size() - 1; i++, j++) {
            Object base_pt = magicPoints.get(i);
            Object tip_pt = magicPoints.get(i + 1);
            polyline.set(j, new Line2D.Double(((MyEllipse2D.Double) base_pt).getPoint(), ((MyEllipse2D.Double) tip_pt).getPoint()));
        }
        updateMasterRect();
        /*
         * we reset the rotation
         */
        angle = 0;
    }

    @Override
    public void setFirstCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - pos.x, bounding.getY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setCenter(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getCenterX() - pos.x, bounding.getCenterY() - pos.y);
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setLastCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getMaxX() - pos.x, bounding.getMaxY() - pos.y);
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

    /**
     *
     * @return the cumulative length of the line segments that compose the
     * polyline
     */
    public double getLength() {
        double area = 0;
        for (Line2D.Double double1 : polyline) {
            area += double1.getP1().distance(double1.getP2());
        }
        return area;
    }

    @Override
    public double getAreaFast() {
        return getLength();
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
    public Polygon getPolygon() {
        int[] x = new int[polyline.size() + 1];
        int[] y = new int[polyline.size() + 1];
        int i = 0;
        for (Line2D.Double double1 : polyline) {
            x[i] = (int) double1.x1;
            y[i] = (int) double1.y1;
            i++;
        }
        Point2D last_pt = polyline.get(polyline.size() - 1).getP2();
        x[x.length - 1] = (int) last_pt.getX();
        y[y.length - 1] = (int) last_pt.getY();
        return new Polygon(x, y, x.length);
    }

    @Override
    public void flipHorizontally() {
        Rectangle2D r = getBounds2D();
        double center = r.getCenterX();
        int i = 0;
        for (Line2D.Double double1 : polyline) {
            double dist_to_center1 = double1.x1 - center;
            double dist_to_center2 = double1.x2 - center;
            if (dist_to_center1 < 0) {
                dist_to_center1 = Math.abs(dist_to_center1);
            } else {
                dist_to_center1 = -dist_to_center1;
            }
            if (dist_to_center2 < 0) {
                dist_to_center2 = Math.abs(dist_to_center2);
            } else {
                dist_to_center2 = -dist_to_center2;
            }
            double1.x1 = dist_to_center1;
            double1.x2 = dist_to_center2;
            polyline.set(i, double1);
        }
    }

    @Override
    public void flipVertically() {
        Rectangle2D r = getBounds2D();
        double center = r.getCenterY();
        int i = 0;
        for (Line2D.Double double1 : polyline) {
            double dist_to_center1 = double1.y1 - center;
            double dist_to_center2 = double1.y2 - center;
            if (dist_to_center1 < 0) {
                dist_to_center1 = Math.abs(dist_to_center1);
            } else {
                dist_to_center1 = -dist_to_center1;
            }
            if (dist_to_center2 < 0) {
                dist_to_center2 = Math.abs(dist_to_center2);
            } else {
                dist_to_center2 = -dist_to_center2;
            }
            double1.y1 = dist_to_center1;
            double1.y2 = dist_to_center2;
            polyline.set(i, double1);
        }
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        String text = "polyLine";
        if (polyline != null) {
            text += "\n:" + polyline.size();
        }
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

    /**
     *
     * @return the vertices of polyline
     */
    public ArrayList<Point> getPoints() {
        ArrayList<Point> pts = new ArrayList<Point>();
        int[] x = new int[polyline.size() + 1];
        int[] y = new int[polyline.size() + 1];
        int i = 0;
        for (Line2D.Double double1 : polyline) {
            x[i] = (int) double1.x1;
            y[i] = (int) double1.y1;
            pts.add(new Point(x[i], y[i]));
            i++;
        }
        Point2D last_pt = polyline.get(polyline.size() - 1).getP2();
        x[x.length - 1] = (int) last_pt.getX();
        y[y.length - 1] = (int) last_pt.getY();
        pts.add(new Point(x[x.length - 1], y[y.length - 1]));
        return pts;
    }

    private ArrayList<java.lang.Double> compute_cumulative_distance(ArrayList<Point> pts) {
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

    /**
     * This function tries to merge small line segments that can be merged into
     * a bigger line segment without losing information
     *
     * @param allowMinorloss if true minor loss is allowed
     */
    public void simplifyLossless(boolean allowMinorloss) {
        simplifyLossless1(allowMinorloss);
    }

    private void simplifyLossless1(boolean allowMinorloss) {
        /*
         * we calculate cumulative distance sum between points to see if we can
         * simplifyLossless the contour or not
         */
        ArrayList<Point2D.Double> magic_pts = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> simplified_contour = new ArrayList<Point2D.Double>();

        ArrayList<Point> pts = getPoints();
        for (Point point : pts) {
            magic_pts.add(new Point2D.Double(point.x, point.y));
        }
        ArrayList<java.lang.Double> dist = compute_cumulative_distance(pts);
        int pos = 0;

        simplified_contour.add(magic_pts.get(0));
        for (int i = 0; i < pts.size(); i++) {
            Point curpt = pts.get(i);
            Point pt_at_pos = pts.get(pos);
            double cur_dist_line = new Point2D.Double(pt_at_pos.x, pt_at_pos.y).distance(new Point2D.Double(curpt.x, curpt.y));
            double real_dist = dist.get(i) - dist.get(pos);
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

        ArrayList<Object> data = new ArrayList<Object>();
        for (Point2D.Double double1 : simplified_contour) {
            data.add(new MyPoint2D.Double(double1));
        }
        setMagicPoints(data);
    }

    @Override
    public String getShapeName() {
        return "Polyline";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.POLYLINE;
    }

    @Override
    public String toString() {
        return "<html><center>" + CommonClassesLight.roundNbAfterComma(getCenter().x, 1) + " " + CommonClassesLight.roundNbAfterComma(getCenter().y, 1) + " " + getShapeName() + " <font color=" + CommonClassesLight.toHtmlColor(color) + ">contour</font>" + "</html>";
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

    @Override
    public int getIJType() {
        return IJCompatibility.IJ_POLYLINE;
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
    public boolean contains(Point p) {
        return rec2d.contains(p);
    }

    @Override
    public boolean contains(Rectangle2D r2) {
        return rec2d.contains(r2);
    }

    @Override
    public boolean contains(double x, double y) {
        return rec2d.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return rec2d.contains(x, y, w, h);
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

    public void popEnds(int nbOfErosion) {
        if (nbOfErosion < 0) {
            nbOfErosion = -nbOfErosion;
        }
        for (int i = 0; i < nbOfErosion; i++) {
            popEnds();
        }
    }

    /**
     * In fact this works better than eroding lines, we just pop out the two
     * extremities, be careful lost pixels are irreversibly lost. This is for
     * example a good method to remove vertices of a bond
     */
    public void popEnds() {
        /**
         * we remove the first and the last line
         */
        if (polyline != null) {
            if (polyline.size() > 1) {
                polyline.remove(polyline.size() - 1);
            }
            if (polyline.size() > 1) {
                polyline.remove(0);
            }
            updateMasterRect();
        }
    }

    @Override
    public void erode(int nb_erosions) {
        computeNewMorphology(-nb_erosions);
    }

    @Override
    public void erode() {
        erode(1);
    }

    @Override
    public void dilate(int nb_dilatations) {
        computeNewMorphology(nb_dilatations);
    }

    @Override
    public void dilate() {
        dilate(1);
    }

    /**
     * nb in fact dilatation/erosion is a just a scaling of the shape in witdh
     * and height by exactly one pixel --> we compute the scaling factor based
     * on the bounding change in size of the bounding rect --> this can in
     * theory be applied to any shape
     */
    private void computeNewMorphology(int sizeChange) {
        Rectangle2D currentBoundingRect = getBounds2D();
        if (currentBoundingRect == null) {
            return;
        }
        double curWidth = currentBoundingRect.getWidth();
        double finalWitdth = curWidth + 2. * sizeChange;
        /**
         * just a security to prevent the shape from disappearing
         */
        if (finalWitdth < 1) {
            finalWitdth = 1;
        }
        Point2D.Double center2D = new Point2D.Double(currentBoundingRect.getCenterX(), currentBoundingRect.getCenterY());
        /**
         * since scaling is isotropic we only need to do it in one dimension
         * (e.g. in x)
         */
        double scale = finalWitdth / curWidth;
        scale(scale);
        setCenter(center2D);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {

        System.out.println(0x47efb2fa910226aaL);
        System.out.println(MyPolyline2D.serialVersionUID);

        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = test2.createGraphics();

        long start_time = System.currentTimeMillis();
        MyPolyline2D.Double test = new MyPolyline2D.Double(new Point2D.Double(10, 10), new Point2D.Double(40, 40));
        //drawAndFill      System.out.println(test instanceof Editable);

        test.draw(g2d);
        g2d.dispose();
        SaverLight.popJ(test2);

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

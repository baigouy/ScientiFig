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
import ij.gui.Line;
import ij.gui.Roi;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.batik.ext.awt.geom.Polygon2D;

/**
 * MyLine2D is a line vectorial object
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyLine2D extends Line2D implements PARoi, Serializable, LineStrokable, Morphable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 5183558485371135658L;
    boolean show_base = false;
    public int ZstackPos = 0;
    public boolean isArrow = false;
    /*
     * should be called isBracket but please keep like that to preserve serialization
     */
    boolean isAccolade = false;
    @Deprecated
    double barbEndSize = 20;
    @Deprecated
    double rescaledBarbEndSize = 20;
    double arrowHeadWidth = 15;
    double arrowHeadHeight = 30;
    double accoladeSize = 12;
    @Deprecated
    double rescaledAccoladeSize = 12;
    boolean is_real_contain = false;
    int color = 0xFFFF00;
    float transparency = 1.f;
    public Line2D.Double l2d;
    float strokeSize = 0.65f;
    public boolean isTransparent = false;
    public int ARROW_HEAD_TYPE = TYPE_FULL_ARROW;
    public static final int TYPE_FULL_ARROW = 0;
    public static final int TYPE_HALF_HEAD_UP_ARROW = 1;
    public static final int TYPE_HALF_HEAD_DOWN_ARROW = 2;
    public static final int TYPE_DOUBLE_HEAD_FULL_ARROW = 3;
    public static final int TYPE_INIBITION_ARROW = 4;
    public static final int TYPE_DOUBLE_HEADED_INIBITION = 5;
    public int FILLING = FILLING_FILLED;
    public static final int FILLING_FILLED = 0;
    public static final int FILLING_OUTLINE = 1;
    public static final int FILLING_FANCY = 2;
    double factor = 1.;
    /*
     * line stroke variables
     */
    public int LINESTROKE = 0;
    public int dashSize = 6;
    public int dotSize = 1;
    public int skipSize = 6;
    /**
     * to handle rotation
     */
    double angle;

    /**
     *
     * @return true if base must be shown
     */
    public boolean isShow_base() {
        return show_base;
    }

    public int getFILLING() {
        return FILLING;
    }

    public void setFILLING(int FILLING) {
        this.FILLING = FILLING;
    }

    public int getARROW_HEAD_TYPE() {
        return ARROW_HEAD_TYPE;
    }

    public void setARROW_HEAD_TYPE(int ARROW_HEAD_TYPE) {
        this.ARROW_HEAD_TYPE = ARROW_HEAD_TYPE;
    }

    /**
     * Defines whether the base of the line should be highlighted
     *
     * @param show_base
     */
    public void setShow_base(boolean show_base) {
        this.show_base = show_base;
    }

    /**
     * a double precision MyLine2D
     */
    public static class Double extends MyLine2D implements Serializable {

        public static final long serialVersionUID = 2120685573605367646L;

        /**
         * Constructor
         *
         * @param pt1
         * @param pt2
         */
        public Double(Point2D.Double pt1, Point2D.Double pt2) {
            l2d = new Line2D.Double(pt1, pt2);
        }

        /**
         * Constructor
         *
         * @param pt1
         * @param pt2
         */
        public Double(Point pt1, Point pt2) {
            l2d = new Line2D.Double(pt1.x, pt1.y, pt2.x, pt2.y);
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
            l2d = new Line2D.Double(x1, y1, x2, y2);
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Line2D.Double l2d) {
            this.l2d = l2d;
        }

        /**
         * Constructor
         *
         * @param l2d
         */
        public Double(Line2D l2d) {
            this.l2d = new Line2D.Double(l2d.getP1(), l2d.getP2());
        }

        /**
         * Constructor
         *
         * @param p
         */
        public Double(Polygon p) {
            this.l2d = new Line2D.Double(p.xpoints[0], p.ypoints[0], p.xpoints[1], p.ypoints[1]);
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyLine2D.Double myel) {
            this.l2d = myel.l2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.isTransparent = myel.isTransparent;
            this.transparency = myel.transparency;
            this.show_base = myel.show_base;
            this.isAccolade = myel.isAccolade;
            this.isArrow = myel.isArrow;
            this.barbEndSize = myel.barbEndSize;
            this.rescaledBarbEndSize = myel.rescaledBarbEndSize;
            this.arrowHeadHeight = myel.arrowHeadHeight;
            this.arrowHeadWidth = myel.arrowHeadWidth;
            this.ARROW_HEAD_TYPE = myel.ARROW_HEAD_TYPE;
            this.FILLING = myel.FILLING;
            this.accoladeSize = myel.accoladeSize;
            this.rescaledAccoladeSize = myel.rescaledAccoladeSize;
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

    @Override
    public BasicStroke getLineStroke() {
        float[] stroke_parameters = createLineStroke();
        if (stroke_parameters == null) {
            return new BasicStroke(strokeSize);
        } else {
            return new BasicStroke(
                    strokeSize,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND,
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

    private Line2D getLine(Point2D p, double theta, double barb) {
        double x = p.getX() + barb * Math.cos(theta);
        double y = p.getY() + barb * Math.sin(theta);
        return new Line2D.Double(p.getX(), p.getY(), x, y);
    }

    /**
     *
     * @return the size of the pointed end of arrows
     */
    public double getArrowHeadWidth() {
        return arrowHeadWidth;
    }

    public double getArrowHeadHeight() {
        return arrowHeadHeight;
    }

    /**
     * Sets the pointed end size of arrows
     *
     * @param barbEndSize
     * @deprecated
     */
    public void setBarbEndSize(double barbEndSize) {
        this.barbEndSize = barbEndSize;
        this.rescaledBarbEndSize = barbEndSize;//barbEndSize * factor;
    }

    /**
     * Sets the width and the height/length of the arrowhead
     *
     * @param arrowWidth
     * @param arrowHeight
     */
    public void setArrowWidthAndHeight(double arrowWidth, double arrowHeight) {
        setArrowHeadWidth(arrowHeadWidth);
        setArrowHeadHeight(arrowHeadHeight);
    }

    public void setArrowHeadWidth(double arrowHeadWidth) {
        this.arrowHeadWidth = arrowHeadWidth;
    }

    public void setArrowHeadHeight(double arrowHeadHeight) {
        this.arrowHeadHeight = arrowHeadHeight;
    }

    /**
     *
     * @return the size of the small line segment in a bracket
     */
    public double getBracketLength() {
        return accoladeSize;
    }

    /**
     *
     * @param accoladeSize
     */
    public void setBraketSize(double accoladeSize) {
        this.accoladeSize = accoladeSize;
        this.rescaledAccoladeSize = accoladeSize;
    }

    /**
     *
     * @return The coordinates of the small line segments of a bracket
     */
    public Object bracket() {
        double xBegin = getBase().x;
        double yBegin = getBase().y;
        double xEnd = getTip().x;
        double yEnd = getTip().y;
        double theta = getAngle();
        Line2D.Double base = new Line2D.Double(xBegin, yBegin, xBegin, yBegin + accoladeSize * factor);
        Line2D.Double tip = new Line2D.Double(xEnd, yEnd, xEnd, yEnd + accoladeSize * factor);
        AffineTransform atBase = new AffineTransform();
        atBase.rotate(theta, xBegin, yBegin);
        Shape rotatedBase = atBase.createTransformedShape(base);
        AffineTransform atTip = new AffineTransform();
        atTip.rotate(theta, xEnd, yEnd);
        Shape rotatedTip = atTip.createTransformedShape(tip);
        ArrayList<Shape> tips = new ArrayList<Shape>();
        tips.add(rotatedTip);
        tips.add(rotatedBase);
        return tips;
    }

    /**
     *
     * @param arrowhead_type
     * @return a polygon2D representing the arrowhead
     */
    public void arrowHeadRetroCompatibility() {
        if (arrowHeadHeight == 0) {
            arrowHeadHeight = barbEndSize * 2.;
            arrowHeadWidth = barbEndSize * 2.;
        }
    }

    public Object arrowhead(boolean tip) {
        arrowHeadRetroCompatibility();
        double curArrowHeadHeight = arrowHeadHeight * factor;
        Point2D.Double extremity;
        if (tip) {
            extremity = getTip();
        } else {
            extremity = getBase();
        }
        Point2D.Double point1 = new Point2D.Double(extremity.x, extremity.y - curArrowHeadHeight);
        Point2D.Double point2 = new Point2D.Double(point1.x, point1.y);
        Point2D.Double kink = new Point2D.Double(point1.x, point1.y + curArrowHeadHeight / 2.5);
        MyPolygon2D.Double arrowHead;
        switch (ARROW_HEAD_TYPE) {
            case TYPE_DOUBLE_HEADED_INIBITION:
            case TYPE_INIBITION_ARROW:
                /*
                 * reset height
                 */
                point1.y += curArrowHeadHeight;
                point2.y += curArrowHeadHeight;
                kink = null;
            case TYPE_DOUBLE_HEAD_FULL_ARROW:
            case TYPE_FULL_ARROW:
                point1.x -= (arrowHeadWidth / 2.) * factor;
                point2.x += (arrowHeadWidth / 2.) * factor;
                break;
            case TYPE_HALF_HEAD_DOWN_ARROW:
                point1.x -= (arrowHeadWidth / 2.) * factor;
                if (FILLING == FILLING_OUTLINE) {
                    point2 = null;
                }
                break;
            case TYPE_HALF_HEAD_UP_ARROW:
                if (FILLING == FILLING_OUTLINE) {
                    point1 = null;
                }
                point2.x += (arrowHeadWidth / 2.) * factor;
                break;
        }
        double arrowAngle = getAngle() - Math.PI / 2.;
        if (!tip) {
            arrowAngle += Math.PI;
        }
        AffineTransform at = new AffineTransform();
        at.rotate(arrowAngle, extremity.x, extremity.y);
        Object output = null;
        int currentFilling = (ARROW_HEAD_TYPE == TYPE_DOUBLE_HEADED_INIBITION || ARROW_HEAD_TYPE == TYPE_INIBITION_ARROW) ? FILLING_OUTLINE : FILLING;

        switch (currentFilling) {
            case FILLING_FANCY: {
                arrowHead = new MyPolygon2D.Double(extremity, point1, kink, point2);
                Polygon2D p2d = arrowHead.getParentInstance();
                Shape rotatedHead = at.createTransformedShape(p2d);
                output = rotatedHead;
            }
            break;
            case FILLING_FILLED: {
                arrowHead = new MyPolygon2D.Double(extremity, point1, point2);
                Polygon2D p2d = arrowHead.getParentInstance();
                Shape rotatedHead = at.createTransformedShape(p2d);
                output = rotatedHead;
            }
            break;
            case FILLING_OUTLINE:
                Line2D.Double l1 = null;
                if (point1 != null) {
                    l1 = new Line2D.Double(extremity.x, extremity.y, point1.x, point1.y);
                }
                Line2D.Double l2 = null;
                if (point2 != null) {
                    l2 = new Line2D.Double(extremity.x, extremity.y, point2.x, point2.y);
                }
                Shape l1rot = null;
                if (l1 != null) {
                    l1rot = at.createTransformedShape(l1);
                }
                Shape l2rot = null;
                if (l2 != null) {
                    l2rot = at.createTransformedShape(l2);
                }
                ArrayList<Object> shapes = new ArrayList<Object>();
                if (l1rot != null) {
                    shapes.add(l1rot);
                }
                if (l2rot != null) {
                    shapes.add(l2rot);
                }
                output = shapes;
                break;

        }
        return output;
    }

    /**
     *
     * @return true if the line should be converted to an arrow
     */
    public boolean isIsArrow() {
        return isArrow;
    }

    /**
     * Defines if a line should be treated as an arrow
     *
     * @param isArrow
     */
    public void setIsArrow(boolean isArrow) {
        this.isAccolade = false;
        this.isArrow = isArrow;
    }

    /**
     *
     * @return true if the line should be treated as a bracket
     */
    public boolean isBracket() {
        return isAccolade;
    }

    /**
     * Defines if a line should be treated as a bracket
     *
     * @param isBracket
     */
    public void setBracket(boolean isBracket) {
        this.isArrow = false;
        this.isAccolade = isBracket;
    }

    /**
     * Sets the primitive of the current object
     *
     * @param l2d
     */
    public void setLine(Line2D.Double l2d) {
        this.l2d = l2d;
    }

    /**
     * inverts the first and last coords of the line (useful if the line is a
     * vector/arrow for example)
     */
    public void invertBeginAndEnd() {
        this.l2d = new Line2D.Double(l2d.getP2(), l2d.getP1());
    }

    @Override
    public Object clone() {
        MyLine2D.Double r2d = new MyLine2D.Double(l2d);
        r2d.color = color;
        r2d.transparency = transparency;
        r2d.ZstackPos = ZstackPos;
        r2d.strokeSize = strokeSize;
        r2d.isTransparent = isTransparent;
        r2d.isAccolade = isAccolade;
        r2d.isArrow = isArrow;
        r2d.barbEndSize = barbEndSize;
        r2d.rescaledBarbEndSize = rescaledBarbEndSize;
        r2d.arrowHeadWidth = arrowHeadWidth;
        r2d.arrowHeadHeight = arrowHeadHeight;
        r2d.ARROW_HEAD_TYPE = ARROW_HEAD_TYPE;
        r2d.FILLING = FILLING;
        r2d.rescaledAccoladeSize = rescaledAccoladeSize;
        r2d.accoladeSize = accoladeSize;
        r2d.LINESTROKE = LINESTROKE;
        r2d.dashSize = dashSize;
        r2d.dotSize = dotSize;
        r2d.skipSize = skipSize;
        r2d.angle = angle;
        return r2d;
    }

    /**
     * Sets the primitive of the current object
     *
     * @param l2d
     */
    public void setLine2D(Line2D.Double l2d) {
        this.l2d = l2d;
    }

    @Override
    public double getX1() {
        return l2d.getX1();
    }

    @Override
    public double getY1() {
        return l2d.getY1();
    }

    @Override
    public Point2D getP1() {
        return l2d.getP1();
    }

    @Override
    public double getX2() {
        return l2d.getX2();
    }

    @Override
    public double getY2() {
        return l2d.getY2();
    }

    @Override
    public Point2D getP2() {
        return l2d.getP2();
    }

    @Override
    public void setLine(double x1, double y1, double x2, double y2) {
        l2d.setLine(x1, y1, x2, y2);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return l2d.getBounds2D();
    }

    @Override
    public Rectangle getBounds() {
        return l2d.getBounds();
    }

    @Override
    public boolean intersects(Rectangle r) {
        return l2d.intersects(r);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return l2d.intersects(r);
    }

    @Override
    public int getColor() {
        return color & 0x00FFFFFF;
    }

    @Override
    public void setColor(int color) {
        this.color = color & 0x00FFFFFF;
    }

    @Override
    public int getColorIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColorOut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return the length of the line
     */
    public double getLength() {
        return l2d.getP1().distance(l2d.getP2());
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
    public void drawSkeletton(Graphics2D g2d, int color) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.draw(l2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        drawSkeletton(g2d, color);

    }

    @Override
    public void drawTransparent(Graphics2D g2d, float transparency) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.draw(l2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fillTransparent(Graphics2D g2d, float transparency) {
        drawTransparent(g2d, transparency);
    }

    @Override
    public void drawAndFillTransparent(Graphics2D g2d, float transparency) {
        drawTransparent(g2d, transparency);
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
    }

    @Override
    public void draw(Graphics2D g2d, int color, float transparency, float strokeSize) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.draw(l2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void fill(Graphics2D g2d, int color, float transparency, float stroke_size) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.fill(l2d);
        g2dparams.restore(g2d);
    }

    @Override
    public void drawAndFill(Graphics2D g2d, int color, float transparency, float stroke_size) {
        draw(g2d, color, transparency, stroke_size);
        fill(g2d, color, transparency, stroke_size);
    }

    @Override
    public void draw(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setColor(new Color(color));
        g2d.setStroke(getLineStroke());
//        if (isArrow) {
            /*
         * fix for arrow layout/stroke pbs
         */
//            g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//        }
        g2d.draw(l2d);
        if (isArrow) {
            //only draw arrowhead if not null length
            if (getLength() != 0) {
                /*
                 * fix for arrow layout/stroke pbs
                 */
                g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Object head = arrowhead(true);
                drawLineExtras(head, g2d);
                if (ARROW_HEAD_TYPE == TYPE_DOUBLE_HEAD_FULL_ARROW || ARROW_HEAD_TYPE == TYPE_DOUBLE_HEADED_INIBITION) {
                    head = arrowhead(false);
                    drawLineExtras(head, g2d);
                }
            }
        }
        if (isAccolade) {
            Object head = bracket();
            drawLineExtras(head, g2d);
        }
        g2dparams.restore(g2d);
    }

    /**
     * Draws arrowhead or brackets
     *
     * @param head
     * @param g2d
     */
    private void drawLineExtras(Object head, Graphics2D g2d) {
        if (head instanceof Shape) {
            g2d.draw((Shape) head);//.draw(g2d, color);
            g2d.fill((Shape) head);//.fill(g2d, color);
        } else if (head instanceof ArrayList) {
            ArrayList<Shape> shapes = (ArrayList<Shape>) head;
            for (Shape shape : shapes) {
                g2d.draw(shape);
            }
        }
    }

    @Override
    public void fill(Graphics2D g2d) {
        draw(g2d);
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        draw(g2d);
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
        if (visibleRect.intersects(this.getBounds2D()) || visibleRect.contains(l2d.getP1()) || visibleRect.contains(l2d.getP2())) {
            draw(g2d);
        }
    }

    @Override
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        l2d = new Line2D.Double(l2d.x1 + x, l2d.y1 + y, l2d.x2 + x, l2d.y2 + y);
    }

    @Override
    public void scale(double factor) {
        double x = l2d.x2 - l2d.x1;
        double y = l2d.y2 - l2d.y1;
        this.factor = factor;
        l2d = new Line2D.Double(l2d.x1, l2d.y1, l2d.x1 + x * factor, l2d.y1 + y * factor);
    }

    @Override
    public void rotate(double angleInDegrees) {
        double angleDiff = angleInDegrees - angle;
        if (angleDiff != 0) {
            l2d = new Line2D.Double(CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP1(), getCenter(), Math.toRadians(angleDiff)), CommonClassesLight.rotatePointInRadians((Point2D.Double) l2d.getP2(), getCenter(), Math.toRadians(angleDiff)));
        }
        angle = angleInDegrees;
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return this.getBounds2D().intersects(visibleRect);
    }

    @Override
    public boolean contains(Point2D p) {
        if (is_real_contain) {
            return l2d.contains(p);
        } else {
            Rectangle r = l2d.getBounds();
            Rectangle r_bigger = new Rectangle(r.x - 2, r.y - 2, r.width + 4, r.height + 4);
            return r_bigger.contains(p);
        }
    }

    @Override
    public boolean contains(Rectangle2D r2) {
        return l2d.contains(r2);
    }

    @Override
    public boolean contains(double x, double y) {
        return l2d.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y, w, h);
    }

    @Override
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    @Override
    public Line2D.Double getParentInstance() {
        return l2d;
    }

    @Override
    public double getShapeWidth() {
        return l2d.getBounds2D().getWidth();
    }

    @Override
    public double getShapeHeight() {
        return l2d.getBounds2D().getHeight();
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        Rectangle2D rec = getBounds2D();
        if (l2d.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dottedSelection, 0f));
            g2d.draw(rec);
            g2dParams.restore(g2d);
        }
    }

    @Override
    public ArrayList<Object> getMagicPoints() {
        double size = 10.;
        ArrayList<Object> sensitive_points = new ArrayList<Object>();
        sensitive_points.add(new MyEllipse2D.Double(new Point2D.Double(l2d.x1, l2d.y1), size));
        sensitive_points.add(new MyEllipse2D.Double(new Point2D.Double(l2d.x2, l2d.y2), size));
        return sensitive_points;
    }

    @Override
    public void setMagicPoints(ArrayList<Object> magicPoints) {
        l2d = new Line2D.Double(((MyEllipse2D.Double) magicPoints.get(0)).getPoint(), ((MyEllipse2D.Double) magicPoints.get(1)).getPoint());
        /*
         * we reset rotation if edited
         */
        angle = 0;
    }

    /**
     *
     * @return the base of the line
     */
    public Point2D.Double getBase() {
        return new Point2D.Double(l2d.x1, l2d.y1);
    }

    /**
     *
     * @return the extremity of the line
     */
    public Point2D.Double getTip() {
        return new Point2D.Double(l2d.x2, l2d.y2);
    }

    /**
     *
     * @return both the base and the extremity of the line
     */
    public Point2D.Double[] getBaseAndTip() {
        Point2D.Double[] bas_n_tip = new Point2D.Double[2];
        bas_n_tip[0] = getBase();
        bas_n_tip[1] = getTip();
        return bas_n_tip;
    }

    /**
     *
     * @return the angle in radiant between the current line and an horizontal
     * line (the values are between 0 and Pi)
     */
    public double getAngle2DInRadiansBetween0AndPi() {
        return CommonClassesLight.getBoundedAngleRadians(getAngle(), CommonClassesLight.ZERO_TO_PI);
    }

    public double getAngle() {
        return Math.atan2(l2d.y2 - l2d.y1, l2d.x2 - l2d.x1);
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
        return new Point2D.Double((l2d.x1 + l2d.x2) / 2., (l2d.y1 + l2d.y2) / 2.);
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
    public double getArea() {
        double area = getLength();
        return area;
    }

    @Override
    public double getAreaFast() {
        return getArea();
    }

    @Override
    public Polygon getPolygon() {
        int[] x = new int[2];
        int[] y = new int[2];
        x[0] = (int) l2d.x1;
        y[0] = (int) l2d.y1;
        x[1] = (int) l2d.x2;
        y[1] = (int) l2d.y2;
        return new Polygon(x, y, x.length);
    }

    @Override
    public void flipHorizontally() {
        l2d = new Line2D.Double(l2d.x2, l2d.y1, l2d.x1, l2d.y2);
    }

    @Override
    public void flipVertically() {
        l2d = new Line2D.Double(l2d.x1, l2d.y2, l2d.x2, l2d.y1);
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
        String text = "Line\nx1:" + l2d.x1 + "\ny1:" + l2d.y1 + "\nx2:" + l2d.x2 + "\ny2:" + l2d.y2;
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
        return "Line";
    }

    @Override
    public int getShapeType() {
        return ROIpanelLight.LINE;
    }

    @Override
    public Point2D.Double getCentroid() {
        return getCenter();
    }

    @Override
    public Point2D.Double[] getExtremePoints() {
        Point2D.Double[] extreme_pts = new Point2D.Double[2];
        extreme_pts[0] = getBase();
        extreme_pts[1] = getTip();
        return extreme_pts;
    }

    @Override
    public int getIJType() {
        return IJCompatibility.IJ_LINE;
    }

    @Override
    public Roi getIJRoi() {
        Roi IJ_ROI = new Line((int) l2d.x1, (int) l2d.y1, (int) l2d.x2, (int) l2d.y2);// new PolygonRoi(p, getIJType());
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
//        return false;
        return true;
    }

    @Override
    public double getRotation() {
        if (isRotable()) {
            return angle;
        }
        return 0;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
//        BufferedImage tmp = new Loader().load("/D/sample_images_PA/egg_chambers/Series010.png");
//        new MyShapeTools().toRaster((ArrayList<Object>) new Loader().loadObject("/D/sample_images_PA/egg_chambers/Series010/ROIs_007.roi"), tmp.createGraphics(), 0xFFFFFF);
//        Saver.save(tmp, "/D/sample_images_PA/egg_chambers/Series010/ROIs_007.png");

//        BufferedImage tmp = new Loader().load("/D/sample_images_PA/egg_chambers/Series010.png");
//        new MyShapeTools().FillSkeletton((ArrayList<Object>) new Loader().loadObject("/D/sample_images_PA/egg_chambers/Series010/ROIs_004.roi"), tmp.createGraphics(), 0xFFFFFF);
//        new MyShapeTools().drawSkeletton((ArrayList<Object>) new Loader().loadObject("/D/sample_images_PA/egg_chambers/Series010/ROIs_004.roi"), tmp.createGraphics(), 0x000000);
//        Saver.save(tmp, "/D/sample_images_PA/egg_chambers/Series010/ROIs_004.png");
        //--> parfait en fait
        if (true) {
            return;
        }

        System.out.println(0x47efb2fa910226aaL);
        System.out.println(MyLine2D.serialVersionUID);

        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = test2.createGraphics();

//        ArrayList<String> list = new LoadListeToArrayList().apply("/list.lst");
        long start_time = System.currentTimeMillis();
        MyLine2D.Double test = new MyLine2D.Double(new Point2D.Double(10, 10), new Point2D.Double(40, 40));

        System.out.println(test instanceof Editable);

        test.draw(g2d);
        g2d.dispose();
//        Saver.poplong(test2);

//      test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

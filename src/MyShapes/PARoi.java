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

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * PARoi is a general ROI interface (previsouly only used in PA but now also in
 * SF, it is just a mix of all my other interfaces)
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public interface PARoi extends Transformable, Drawable, Editable, Serializable, IJCompatibility, Cloneable {

    public static final long serialVersionUID = 207300951241178289L;

    /**
     *
     * @param p
     * @return true if the point p is in the vectorial object
     */
    public boolean contains(Point p);

    /**
     *
     * @param p
     * @return true if the point p is in the vectorial object
     */
    public boolean contains(Point2D p);

    /**
     *
     * @param r2
     * @return true if the rectangle r is contained by the vectorial object
     */
    public boolean contains(Rectangle2D r2);

    /**
     *
     * @param x
     * @param y
     * @return true if the point (x,y) is in the vectorial object
     */
    public boolean contains(double x, double y);

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @return true if the rectangle (x,y,w,h) is contained by the vectorial
     * object
     */
    public boolean contains(double x, double y, double w, double h);

    /**
     *
     * @return the bounding rectangle for the current shape
     */
    public Rectangle2D getBounds2D();

    /**
     *
     * @return the bounding rectangle for the current shape
     */
    public Rectangle getBounds();

    /**
     *
     * @param r
     * @return true if the rectangle r intersects the vectorial object
     */
    public boolean intersects(Rectangle r);

    /**
     *
     * @param r
     * @return true if the rectangle r intersects the vectorial object
     */
    public boolean intersects(Rectangle2D r);

    /**
     *
     * @return the stroke size of the vectorial object
     */
    public float getStrokeSize();

    /**
     * Sets the stroke size of the vectorial object
     *
     * @param strokeSize
     */
    public void setStrokeSize(float strokeSize);

    /**
     *
     * @return the area in pixels of the vectorial object
     */
    public double getArea();

    /**
     *
     * @return a rough computation of the area of the vectorial object
     * (imprecise but fast)
     */
    public double getAreaFast();

    /**
     *
     * @param <T>
     * @return the primitive shape of the vectorial object
     */
    public <T> T getParentInstance();

    /**
     *
     * @param ZstackPosition
     */
    public void setZstackPosition(int ZstackPosition);

    /**
     *
     * @return the Z position in the satck
     */
    public int getZStackposition();

    /**
     *
     * @return a polygonal approxiamation of the vectorial object
     */
    public Polygon getPolygon();

    /**
     *
     * @return the name of the vectorial object
     */
    public String getShapeName();

    /**
     *
     * @return the type of the vectorial object
     */
    public int getShapeType();

    /**
     *
     * @return the centroid of the vectorial object
     */
    public Point2D.Double getCentroid();

    /**
     *
     * @return all the points necessary to recreate the contour of the vectorial
     * object
     */
    public Point2D.Double[] getExtremePoints();

    /**
     *
     * @return a copy of the vectorial object
     */
    public Object clone();
}

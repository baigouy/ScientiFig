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

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Transformable is an interface that allows the geometrical transformation of
 * vectorial objects (transalation, scaling, rotation, ...)
 *
 * @author Benoit Aigouy
 */
public interface Transformable extends Serializable {

    public static final long serialVersionUID = 2684244738908301340L;

    /**
     * Translate the vectorial object in 2D
     *
     * @param x
     * @param y
     */
    public void translate(double x, double y);

    /**
     * Scale the vectorial object
     *
     * @param factor rescaling factor
     */
    public void scale(double factor);

    /**
     * Rotate the vectorial object by an angle
     *
     * @param angleInDegrees
     */
    public void rotate(double angleInDegrees);

    /**
     *
     * @return true if the shape can rotated or not
     */
    public boolean isRotable();

    /**
     *
     * @return the current rotation of the shape
     */
    public double getRotation();

    /**
     * Translate the vectorial object in order for its upper left corner to be
     * at position (pos)
     *
     * @param pos
     */
    public void setFirstCorner(Point2D.Double pos);

    /**
     * Translate the vectorial object in order for its lower right corner to be
     * at position (pos)
     *
     * @param pos
     */
    public void setLastCorner(Point2D.Double pos);

    /**
     *
     * @return a point containing the coordinates of the upper left corner of
     * the vectorial object
     */
    public Point2D.Double getFirstCorner();

    /**
     *
     * @return a point containing the coordinates of the lower righ corner of
     * the vectorial object
     */
    public Point2D.Double getLastCorner();

    /**
     *
     * @return the center of the vectorial object
     */
    public Point2D.Double getCenter();

    /**
     * Translate the vectorial object to place its center at position (pos)
     *
     * @param pos
     */
    public void setCenter(Point2D.Double pos);

    /**
     * Sets the width of the vectorial objcet to 'width'
     *
     * @param width
     * @param keepAR true means preserve aspaect ratio
     */
    public void setShapeWidth(double width, boolean keepAR);

    /**
     * Sets the height of the vectorial objcet to 'height'
     *
     * @param height
     * @param keepAR true means preserve aspaect ratio
     */
    public void setShapeHeight(double height, boolean keepAR);

    /**
     *
     * @return the vectorial object width
     */
    public double getShapeWidth();

    /**
     *
     * @return the vectorial object height
     */
    public double getShapeHeight();

    /**
     * flip the vectorial object horizontally
     */
    public void flipHorizontally();

    /**
     * flip the vectorial object vertically
     */
    public void flipVertically();
}



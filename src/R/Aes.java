/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2013 Benoit Aigouy 

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
package R;

import java.io.Serializable;

/**
 * aes contains R ggplot2 aesthetics infos
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Aes implements Cloneable, Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -8645541683982974171L;
    public int BAR_TYPE = BOTH_BARS;
    public static final int UPPER_BAR = 0;
    public static final int LOWER_BAR = 1;
    public static final int BOTH_BARS = 2;
    String x;
    String y;
    String color;
    String fill;
    String shape;
    /*
     * useful for error bars and geom rect
     */
    String xmin;
    String xmax;
    String ymin;
    String ymax;
    String linetype;
    /*
     * for arrows or segments
     */
    String xend;
    String yend;

    /**
     * Empty constructor
     */
    public Aes() {
    }

    /**
     *
     * @return the column containing the x end coordinates of rects or lines
     */
    public String getXend() {
        return xend;
    }

    /**
     * Sets the column containing the x end coordinates of rects or lines
     *
     * @param xend
     */
    public void setXend(String xend) {
        this.xend = xend;
    }

    /**
     *
     * @return the column containing the y end coordinates of rects or lines
     */
    public String getYend() {
        return yend;
    }

    /**
     * Sets the column containing the y end coordinates of rects or lines
     *
     * @param yend
     */
    public void setYend(String yend) {
        this.yend = yend;
    }

    /**
     *
     * @return the line type
     */
    public String getAesLinetype() {
        return linetype;
    }

    /**
     * Sets the line type (solid, dashed, ...)
     *
     * @param linetype
     */
    public void setAesLinetype(String linetype) {
        this.linetype = linetype;
    }

    /**
     *
     * @return the lower x value for error bars
     */
    public String getXmin() {
        return xmin;
    }

    /**
     * Sets the lower x value for error bars
     *
     * @param xmin
     */
    public void setXmin(String xmin) {
        this.xmin = xmin;
    }

    /**
     *
     * @return the upper x value for error bars
     */
    public String getXmax() {
        return xmax;
    }

    /**
     * Sets the upper x value for error bars
     *
     * @param xmax
     */
    public void setXmax(String xmax) {
        this.xmax = xmax;
    }

    /**
     *
     * @return the lower y value for error bars
     */
    public String getYmin() {
        return ymin;
    }

    /**
     * Sets the lower y value for error bars
     *
     * @param ymin
     */
    public void setYmin(String ymin) {
        this.ymin = ymin;
    }

    /**
     *
     * @return the upper y value for error bars
     */
    public String getYmax() {
        return ymax;
    }

    /**
     * Sets the upper y value for error bars
     *
     * @param ymax
     */
    public void setYmax(String ymax) {
        this.ymax = ymax;
    }

    /**
     *
     * @return the point shape (triangle, square, ...)
     */
    public String getShape() {
        return shape;
    }

    /**
     * Sets the point shape
     *
     * @param shape
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     *
     * @return the X column
     */
    public String getX() {
        return x;
    }

    /**
     * Sets the X column
     *
     * @param x
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     *
     * @return the Y column
     */
    public String getY() {
        return y;
    }

    /**
     *
     * Sets the Y column
     *
     * @param y
     */
    public void setY(String y) {
        this.y = y;
    }

    /**
     *
     * @return the aesthetics color
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color the aesthetics color (or label)
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return the aesthetics fill (or label)
     */
    public String getFill() {
        return fill;
    }

    /**
     * Sets the aesthetics fill (or label)
     *
     * @param fill
     */
    public void setFill(String fill) {
        this.fill = fill;
    }

    /**
     * @return an R interpretable string that contains all the necessary ggplot2
     * aesthetics informations
     */
    @Override
    public String toString() {
        String ggplot = "aes(";
        if (x != null) {
            ggplot += "x=" + x + ", ";
        }
        if (y != null) {
            ggplot += "y=" + y + ", ";
        }
        if (fill != null) {
            ggplot += "fill=as.factor(" + fill + "), ";
        }
        if (color != null) {
            ggplot += "color=as.factor(" + color + "), ";
        }
        if (shape != null) {
            ggplot += "shape=as.factor(" + shape + "), ";
        }
        if (xmin != null) {
            ggplot += "xmin=" + xmin + ", ";
        }
        if (xmax != null) {
            ggplot += "xmax=" + xmax + ", ";
        }
        if (ymin != null) {
            ggplot += "ymin=" + ymin + ", ";
        }
        if (ymax != null) {
            ggplot += "ymax=" + ymax + ", ";
        }
        if (xend != null) {
            ggplot += "xend=" + xend + ", ";
        }
        if (yend != null) {
            ggplot += "yend=" + yend + ", ";
        }
        if (linetype != null) {
            ggplot += "linetype=" + linetype + ", ";
        }
        if (ggplot.endsWith(", ")) {
            ggplot = ggplot.substring(0, ggplot.length() - 2);
        }
        ggplot += ")";
        if (ggplot.equals("aes()")) {
            ggplot = "";
        }
        return ggplot;
    }

    @Override
    public Object clone() {
        Aes aes = new Aes();
        aes.x = this.x;
        aes.y = this.y;
        aes.shape = this.shape;
        aes.color = this.color;
        aes.fill = this.fill;
        aes.xmin = this.xmin;
        aes.ymin = this.ymin;
        aes.xmax = this.xmax;
        aes.ymax = this.ymax;
        aes.xend = this.xend;
        aes.yend = this.yend;
        aes.linetype = this.linetype;
        return aes;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        Aes aes = new Aes();
        System.out.println(aes);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



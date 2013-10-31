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
//TODO maybe make it extend geomplot because they are so similar or not ???? --> think about it in a way it's really nothing more than a geomplot
package R;

import java.io.Serializable;

/**
 * GeomErrorBars is a class to handle R error bars (should disappear and be
 * included as a new plot in geomplot).
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 * @deprecated error bars is now a typical plot
 */
public class GeomErrorBars implements Serializable {

    /*
     * Variables
     */
    public static final long serialVersionUID = 7625740975779049785L;
    boolean dodge = false;
    String width = 1 + "";
    Aes aes = new Aes();
    String alpha;
    String xmin;
    String xmax;
    String ymin;
    String ymax;
    String x;
    String y;
    String linetype;
    String size;
    String color;
    String x_or_y;

    /**
     * empty constructor
     */
    public GeomErrorBars() {
    }

    /**
     * Cosntructor
     *
     * @param data the source data
     * @param error the name of the error column
     * @param dodge
     * @param BAR_TYPE upper, lower ot both
     * @param width the error bar width in axis unit
     */
    public GeomErrorBars(String data, String error, boolean dodge, int BAR_TYPE, String width) {
        this.dodge = dodge;
        aes.BAR_TYPE = BAR_TYPE;
        this.width = width;
        if (error == null) {
            error = "se";
        }
    }

    /**
     *
     * @return the color/label of the error bar
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color/label of the error bar
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return the error bar aesthetics
     */
    public Aes getAes() {
        return aes;
    }

    /**
     * Sets the error bar aesthetics
     *
     * @param aes
     */
    public void setAes(Aes aes) {
        this.aes = aes;
    }

    /**
     *
     * @return the error bar width
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the error bar width
     *
     * @param width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     *
     * @return the eroor bar alpha/transparency
     */
    public String getAlpha() {
        return alpha;
    }

    /**
     * sets the eroor bar alpha/transparency
     *
     * @param alpha
     */
    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    /**
     *
     * @return the lower y value for the error bar
     */
    public String getYmin() {
        return aes.getYmin();
    }

    /**
     * Sets the lower y value for the error bar
     *
     * @param ymin
     */
    public void setYmin(String ymin) {
        aes.setYmin(ymin);
    }

    /**
     *
     * @return the upper y value for the error bar
     */
    public String getYmax() {
        return aes.getYmax();
    }

    /**
     * Sets the upper y value for the error bar
     *
     * @param ymax
     */
    public void setYmax(String ymax) {
        aes.setYmax(ymax);
    }

    /**
     * Sets the error bar central point
     *
     * @param y
     */
    public void setY(String y) {
        aes.setYmax(y);
        aes.setYmin(y);
    }

    /**
     *
     * @return the aesthetics color
     */
    public String getAesColor() {
        return aes.getColor();
    }

    /**
     * Sets the aesthetics color
     *
     * @param color
     */
    public void setAesColor(String color) {
        aes.setColor(color);
    }

    /**
     *
     * @return the aesthetics fill
     */
    public String getFill() {
        return aes.getFill();
    }

    /**
     * Sets the aesthetics fill
     *
     * @param fill
     */
    public void setFill(String fill) {
        aes.setFill(fill);
    }

    /**
     *
     * @return the aesthetics shape
     */
    public String getShape() {
        return aes.getShape();
    }

    /**
     * Sets the aesthetics shape
     *
     * @param shape
     */
    public void setShape(String shape) {
        aes.setShape(shape);
    }

    /**
     * Sets the error bar line type (dashed, ...)
     *
     * @param linetype
     */
    public void setLineType(String linetype) {
        this.linetype = linetype;
    }

    /**
     * Sets the error bar line width
     *
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     *
     * @return an R interpretable string that will plot horizontal or vertical
     * error bars
     */
    @Override
    public String toString() {
        String error_bars = "";
        if (x_or_y.equals("x")) {
            error_bars += "geom_errorbarh(";
        } else {
            error_bars += "geom_errorbar(";
        }
        error_bars += aes.toString() + ", ";
        if (alpha != null) {
            error_bars += "alpha=" + alpha + ", ";
        }
        if (!width.equals("0")) {
            error_bars += "width=" + width + ", ";
        }
        if (linetype != null) {
            error_bars += "linetype=" + linetype + ", ";
        }
        if (size != null) {
            error_bars += "size=" + size + ", ";
        }
        if (color != null) {
            error_bars += "color=\"" + color + "\", ";
        }
        if (error_bars.endsWith(", ")) {
            error_bars = error_bars.substring(0, error_bars.length() - 2);
        }
        error_bars += ")";
        return error_bars;
    }

    public void setAxis(String x_or_y) {
        this.x_or_y = x_or_y;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        GeomErrorBars test = new GeomErrorBars();
        test.setY("y");
        test.setFill("type");
        test.setAesColor("type");
        test.setShape("type");
        test.setWidth(0 + "");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



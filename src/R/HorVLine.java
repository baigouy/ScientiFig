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

/*
 * http://docs.ggplot2.org/current/geom_hline.html
 */
import java.io.Serializable;

/**
 * HorVLine draws ggplot2 x/y intercepts
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class HorVLine implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -2887951728963336693L;
    String pos;
    String orientation;
    String linetype;
    String size;
    String color;
    String alpha;

    /**
     * Constructor
     *
     * @param orientation orientation of the intercept (hor/ver)
     */
    public HorVLine(String orientation) {
        this.orientation = orientation;
    }

    /**
     *
     * @return the x or y position of the intercept
     */
    public String getPos() {
        return pos;
    }

    /**
     * Sets the x or y position of the intercept
     *
     * @param pos
     */
    public void setPos(String pos) {
        this.pos = pos;
    }

    /**
     *
     * @return the orientation (h or v) of the intercept
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation (h or v) of the intercept
     *
     * @param orientation
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     *
     * @return the linetype (dashed,...) of the intercept
     */
    public String getLinetype() {
        return linetype;
    }

    /**
     * Sets the linetype (dashed,...) of the intercept
     *
     * @param linetype
     */
    public void setLinetype(String linetype) {
        this.linetype = linetype;
    }

    /**
     *
     * @return the size of the line intercept
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size of the intercept intercept
     *
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     *
     * @return the color of the intercept
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the intercept
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return the alpha transparency of the intercept
     */
    public String getAlpha() {
        return alpha;
    }

    /**
     * Sets the alpha transparency of the intercept
     *
     * @param alpha
     */
    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    @Override
    public String toString() {
        String ggplot = "geom_" + orientation + "line(";
        if (pos != null) {
            if (orientation.equals("h")) {
                ggplot += "y";
            } else {
                ggplot += "x";
            }
        }
        ggplot += "intercept=" + pos + ", ";
        if (linetype != null) {
            ggplot += "linetype=" + linetype + ", ";
        }
        if (size != null) {
            ggplot += "size=" + size + ", ";
        }
        if (color != null) {
            ggplot += "color=\"" + color + "\", ";
        }
        if (alpha != null) {
            ggplot += "alpha=\"" + alpha + "\", ";
        }
        if (ggplot.endsWith(", ")) {
            ggplot = ggplot.substring(0, ggplot.length() - 2);
        }
        ggplot += ")";
        return ggplot;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        HorVLine test = new HorVLine("h");
        test.setColor("#FF00FF");
        test.setLinetype("1");
        test.setSize(0.5 + "");
        test.setPos("10");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



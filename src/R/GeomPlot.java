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
package R;

import MyShapes.GraphFont;
import java.io.Serializable;

/**
 * GeomPlot is a class that can handle various ggplot2 plots (plot styles)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class GeomPlot implements Serializable {

    /**
     * declaration of the variables
     */
    public static final long serialVersionUID = -211279822861003645L;
    Aes aes = new Aes();
    String alpha;
    String geomType;
    String color;
    String fill;
    String stat;
    String shape;
    String size;
    String arrow;
    int nb_of_Color_colors = 1;
    int nb_of_Fill_colors = 1;
    /**
     * specific for line plots
     */
    String linetype;
    /**
     * specific for histos
     */
    String binWidth;
    String position;
    /**
     * specific for smooth
     */
    public String SE;
    String REGRESSION_TYPE;
    String family;
    String formula;

    /**
     * empty constructor
     */
    public GeomPlot() {
    }

    /**
     *
     * @return the confidence interval settings for the geom_smooth
     */
    public String getSE() {
        return SE;
    }

    /**
     * Sets the confidence interval for geom_smooth
     *
     * @param SE
     */
    public void setSE(String SE) {
        this.SE = SE;
    }

    /**
     *
     * @return the family settings for geom_smooth
     */
    public String getFamily() {
        return family;
    }

    /**
     * Sets the family settings for geom_smooth
     *
     * @param family
     */
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     *
     * @return the formula settings for geom_smooth
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Sets the formula settings for geom_smooth
     *
     * @param formula
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     *
     * @return the regression type
     */
    public String getRegression() {
        return REGRESSION_TYPE;
    }

    /**
     * Sets the regression to apply
     *
     * @param REGRESSION_TYPE
     */
    public void setRegression(String REGRESSION_TYPE) {
        this.REGRESSION_TYPE = REGRESSION_TYPE;
    }

    /**
     *
     * @return a boolean indicating if the current plot is a line plot
     */
    public boolean isLineMode() {
        if (geomType == null) {
            return false;
        }
        /*
         * in fact error bar plots are pretty much line plots in many aspects
         */
        return geomType.toLowerCase().contains("line") || geomType.toLowerCase().contains("error");
    }

    /**
     *
     * @return a boolean indicating if the current plot is a scatter plot
     */
    public boolean isPointMode() {
        if (geomType == null) {
            return false;
        }
        return geomType.toLowerCase().contains("point");
    }

    /**
     *
     * @return the number of colors
     */
    public int getNb_of_Color_colors() {
        return nb_of_Color_colors;
    }

    /**
     * sets the nb of colors to nb_of_Color_colors
     *
     * @param nb_of_Color_colors
     */
    public void setNb_of_Color_colors(int nb_of_Color_colors) {
        this.nb_of_Color_colors = nb_of_Color_colors;
    }

    /**
     *
     * @return the number of fill colors
     */
    public int getNb_of_Fill_colors() {
        return nb_of_Fill_colors;
    }

    /**
     * sets the nb of fill colors to nb_of_Fill_colors
     *
     * @param nb_of_Fill_colors
     */
    public void setNb_of_Fill_colors(int nb_of_Fill_colors) {
        this.nb_of_Fill_colors = nb_of_Fill_colors;
    }

    /**
     *
     * @return the current ggplot2 aesthetics
     */
    public Aes getAes() {
        return aes;
    }

    /**
     * sets the aesthetics for the current plot
     *
     * @param aes
     */
    public void setAes(Aes aes) {
        this.aes = aes;
    }

//    /**
//     *
//     * @return the error bars
//     */
//    public GeomErrorBars getError_bars() {
//        return error_bars;
//    }
//    /**
//     * Sets the error bars for the current plot
//     *
//     * @param error_bars
//     */
//    public void setError_bars(GeomErrorBars error_bars) {
//        this.error_bars = error_bars;
//    }
    /**
     * constructor that sets the type of ggplot2 graph (geom_bar, geom_rect,
     * geom_line, ....)
     *
     * @param geomType
     */
    public GeomPlot(String geomType) {
        this.geomType = geomType;
    }

    /**
     *
     * @return the histogram bin width
     */
    public String getBinWidth() {
        return binWidth;
    }

    /**
     * Sets the histogram bin width
     *
     * @param binWidth
     */
    public void setBinWidth(String binWidth) {
        this.binWidth = binWidth;
    }

    /**
     *
     * @return the current ggplot2 plot type (geom_line, geom_point, ...)
     */
    public String getGeomType() {
        return geomType;
    }

    /**
     * Sets the type of ggplot2 graph (geom_bar, geom_rect, geom_line, ....)
     *
     * @param geomType
     */
    public void setGeomType(String geomType) {
        this.geomType = geomType;
    }

    /**
     *
     * @return the stat applied to the graph
     */
    public String getStat() {
        return stat;
    }

    /**
     * Sets the ggplot2 stat to apply to the plot
     *
     * @param stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

    /**
     *
     * @return the histogram bar position (possible values = stacked (default),
     * dodge)
     */
    public String getPosition() {
        return position;
    }

    /**
     * sets the histogram bar position (possible values = stacked (default),
     * dodge)
     *
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     *
     * @return the graph alpha (transparency)
     */
    public String getAlpha() {
        return alpha;
    }

    /**
     * Sets the graph alpha (transparency)
     *
     * @param alpha
     */
    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    /**
     * Changes the width of lines in a line plot
     *
     * @param defautGraphParameters
     */
    public void changeLineSize(GraphFont defautGraphParameters) {
        if (getSize() != null && (getGeomType().toLowerCase().contains("line") || getGeomType().toLowerCase().contains("error"))) {
            if (defautGraphParameters.getLineSize() > 0) {
                setSize(defautGraphParameters.getLineSize() + "");
            }
        }
    }

    /**
     * Changes the width of lines in a line plot
     *
     * @param stroke
     */
    public boolean changeLineSize(float stroke) {
        if (/*getSize() != null && */getGeomType().toLowerCase().contains("line")|| getGeomType().toLowerCase().contains("error")) {
            setSize(stroke + "");
            return true;
        }
        return false;
    }

    /**
     * Changes the size of points in a scatter plot
     *
     * @param defautGraphParameters
     */
    public void changePointSize(GraphFont defautGraphParameters) {
        if (/*getSize() != null && */getGeomType().toLowerCase().contains("point")) {
            if (defautGraphParameters.getPointSize() > 0) {
                setSize(defautGraphParameters.getPointSize() + "");
            }
        }
    }

    /**
     * Changes the size of points in a scatter plot
     *
     * @param stroke
     */
    public boolean changePointSize(float stroke) {
        if (getSize() != null && getGeomType().toLowerCase().contains("point")) {
            setSize(stroke + "");
            return true;
        }
        return false;
    }

    /**
     *
     * @return the size of a line/point plot
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size of a line/point plot
     *
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     *
     * @return the name of the data for the X axis
     */
    public String getX() {
        return aes.getX();
    }

    /**
     * Sets the name of the x axis for the graph
     *
     * @param x
     */
    public void setX(String x) {
        aes.setX(x);
    }

    /**
     *
     * @return the name of the data for the Y axis
     */
    public String getY() {
        return aes.getY();
    }

    /**
     * Sets the name of the y axis for the graph
     *
     * @param y
     */
    public void setY(String y) {
        aes.setY(y);
    }

    /**
     * Sets the column containing the X max coordinates of a rectangle
     *
     * @param xmax
     */
    public void setXmax(String xmax) {
        aes.setXmax(xmax);
    }

    /**
     * Sets the column containing the Y max coordinates of a rectangle
     *
     * @param ymax
     */
    public void setYmax(String ymax) {
        aes.setYmax(ymax);
    }

    /**
     * Sets the column containing the X min coordinates of a rectangle
     *
     * @param xmin
     */
    public void setXmin(String xmin) {
        aes.setXmin(xmin);
    }

    /**
     * Sets the column containing the Y min coordinates of a rectangle
     *
     * @param ymin
     */
    public void setYmin(String ymin) {
        aes.setYmin(ymin);
    }

    /**
     *
     * @return the column containing the X max coordinates of a rectangle
     */
    public String getXmax() {
        return aes.getXmax();
    }

    /**
     *
     * @return the column containing the Y max coordinates of a rectangle
     */
    public String getYmax() {
        return aes.getYmax();
    }

    /**
     *
     * @return the column containing the X min coordinates of a rectangle
     */
    public String getXmin() {
        return aes.getXmin();
    }

    /**
     *
     * @return the column containing the Y min coordinates of a rectangle
     */
    public String getYmin() {
        return aes.getYmin();
    }

    /**
     * Sets the column containing the x end coordinates of vectors or lines
     *
     * @param xend
     */
    public void setXend(String xend) {
        aes.setXend(xend);
    }

    /**
     * Sets the column containing the y end coordinates of vectors or lines
     *
     * @param yend
     */
    public void setYend(String yend) {
        aes.setYend(yend);
    }

    /**
     *
     * @return the column containing the x end coordinates of vectors or lines
     */
    public String getXend() {
        return aes.getXend();
    }

    /**
     *
     * @return the column containing the y end coordinates of vectors or lines
     */
    public String getYend() {
        return aes.getYend();
    }

    /**
     *
     * @return the current ggplot2 fill parameters
     */
    public String getFill() {
        return fill;
    }

    /**
     * sets the current ggplot2 fill parameters
     *
     * @param fill
     */
    public void setFill(String fill) {
        this.fill = fill;
    }

    /**
     *
     * @return the current ggplot2 color parameters
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the current ggplot2 color parameters
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return the current ggplot2 color parameters
     */
    public String getAesColor() {
        return aes.getColor();
    }

    /**
     * Sets the current ggplot2 aes color parameters
     *
     * @param color
     */
    public void setAesColor(String color) {
        aes.setColor(color);
    }

    /**
     *
     * @return the current ggplot2 aes fill parameters
     */
    public String getAesFill() {
        return aes.getFill();
    }

    /**
     * Sets the current ggplot2 aes fill parameters
     *
     * @param fill
     */
    public void setAesFill(String fill) {
        aes.setFill(fill);
    }

    /**
     *
     * @return the current shape of points (triangles, squares, ...)
     */
    public String getShape() {
        return shape;
    }

    /**
     * Sets the current ggplot2 shape parameters
     *
     * @param shape
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     *
     * @return the current ggplot2 aes shape parameters
     */
    public String getAesShape() {
        return aes.getShape();
    }

    /**
     * Sets the current ggplot2 aes shape parameters
     *
     * @param shape
     */
    public void setAesShape(String shape) {
        aes.setShape(shape);
    }

    /**
     * Sets the current line type in a line plot
     *
     * @param linetype
     */
    public void setAesLineType(String linetype) {
        aes.linetype = linetype;
    }

    /**
     *
     * @return the current line type in a line plot
     */
    public String getLineType() {
        return linetype;
    }

    /**
     * Sets the line type for a line plot (dashed, dotted, ...)
     *
     * @param linetype
     */
    public void setLineType(String linetype) {
        this.linetype = linetype;
    }

    /**
     *
     * @return if the current segment plot should has an arrowhead
     */
    public String getArrow() {
        return arrow;
    }

    /**
     * Sets if the current segment plot should has an arrowhead
     *
     * @param arrow
     */
    public void setArrow(String arrow) {
        this.arrow = arrow;
    }

    /**
     * @return an R compatible string representing the current plot
     */
    @Override
    public String toString() {
        if (geomType == null) {
            System.out.println("please specify a type first");
            return "";
        }
        /*
         * conversion of the figur vocabulary to the R vocabulary
         */
        String ggplot = geomType.replace("bar2", "bar").replace("bar3", "bar") + "(";
        ggplot += aes.toString() + ", ";
        if (ggplot.contains("(, ")) {
            ggplot = ggplot.replace("(, ", "(");
        }
        if (alpha != null) {
            ggplot += "alpha=" + alpha + ", ";
        }
        if (binWidth != null) {
            ggplot += "binwidth=" + binWidth + ", ";
        }
        if (color != null && aes.color == null) {
            ggplot += "color=\"" + color + "\", ";
        }
        if (fill != null) {
            ggplot += "fill=\"" + fill + "\", ";
        }
        if (stat != null) {
            ggplot += "stat=\"" + stat + "\", ";
        }
        if (position != null) {
            ggplot += "position=\"" + position + "\", ";
        }
        if (linetype != null) {
            ggplot += "linetype=" + linetype + ", ";
        }
        if (shape != null) {
            ggplot += "shape=" + shape + ", ";
        }
        if (size != null) {
            ggplot += "size=" + size + ", ";
        }
        if (REGRESSION_TYPE != null) {
            ggplot += "method=" + REGRESSION_TYPE + ", ";
            if (family != null) {
                ggplot += "family=\"" + family + "\", ";
            }
            if (formula != null && !formula.trim().equals("")) {
                ggplot += "formula=" + formula + ", ";
            }
        }

        if (SE == null) {
            ggplot += "se=FALSE, ";
        } else {
            if (geomType.toLowerCase().contains("_smooth")) {
                ggplot += "se=TRUE, ";
            }
        }
        //TODO fix that some day
        //example http://stat.ethz.ch/R-manual/R-devel/library/grid/html/unit.html
        //+geom_segment(data=curDataFigR, aes(x= x,y=y.qsdqsd.sqd,xend=x+10, yend=z), arrow=arrow(length=unit(5, "pt")))
        if (arrow != null) {
            ggplot += "arrow=arrow(), ";
        }
        if (geomType.endsWith("bar3")) {
            ggplot += "width=1, ";
        }
        if (ggplot.endsWith(", ")) {
            ggplot = ggplot.substring(0, ggplot.length() - 2);
        }
        ggplot += ")";
        if (geomType.endsWith("bar3")) {
            ggplot += " + coord_polar()";
        }
//        if (error_bars != null && !geomType.toLowerCase().contains("ensity") && !geomType.toLowerCase().endsWith("bar")) {
//            ggplot += " + " + error_bars;
//        }
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
        GeomPlot test = new GeomPlot();
        test.setGeomType("geom_line");
        test.setAlpha("0.3");
        test.setX("x");
        test.setY("y");
        test.setAesFill("type");
        test.setAesColor("type");
        test.setAesShape("type");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



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
package R;

import Commons.CommonClassesLight;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * ThemeGraph contains everything needed to produce an R theme
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ThemeGraph implements Serializable, Cloneable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -1586989255333614493L;
    String graphBgColor;
    String axisTicksColor;
    String xAxisLabelColor;
    String yAxisLabelColor;
    String xAxisTitleColor;
    String yAxisTitleColor;
    String plotTitleColor;
    String outsideGraphColor;
    /*
     * controls the of the contour of legend shapes --> don't touch
     */
    String legendKeyColor;
    String legendTextColor;
    String legendTitleColor;
    String legendBgColor;
    String graphFrameColor;
    String panelGridMajorColor;
    String panelGridMinorColor;
    String facetLabelFill;
    String facetFrameColor;
    String facetTextColor;
    /*
     * now the size parameters
     */
    String axisTicksStrokeWidth;
    String graphFrameStrokeWidth;
    String panelMajorGridStrokeWidth;
    String panelMinorGridStrokeWidth;
    String facetFrameStrokeWidth;
    String themeName;
//    /*
//     * hide major and minor axes
//     */
//    boolean hideMajorGrid;
//    boolean hideMajorGridX;
//    boolean hideMajorGridY;
//    boolean hideMinorGrid;
//    boolean hideMinorGridX;
//    boolean hideMinorGridY;
    /*
     * temp file path
     */
    private transient String path;

    /**
     * Empty constructor
     */
    public ThemeGraph() {
    }

    /**
     * Reads an xml (txt) ggplot2 theme document and loads all its parameters in
     * the current class
     *
     * @param filename
     * @return true upon success
     */
    public boolean loadParameters(String filename) {
        boolean all_ok = true;
        FileInputStream in = null;
        try {
            if (!new File(filename).exists()) {
                all_ok = false;
            }
            in = new FileInputStream(filename);
            Properties p = new Properties();
            /*
             * load additional parameters
             */
            try {
                p.loadFromXML(in);
                if (p.containsKey("Theme name")) {
                    setThemeName(p.getProperty("Theme name"));
                }
                if (p.containsKey("Background Color")) {
                    setOutsideGraphColor(p.getProperty("Background Color"));
                }
                /*
                 * the title
                 */
                if (p.containsKey("Main Plot Title Color")) {
                    setPlotTitleColor(p.getProperty("Main Plot Title Color"));
                }
                /*
                 * the plot area parameters
                 */
                if (p.containsKey("Plot Area Background Color")) {
                    setGraphBgColor(p.getProperty("Plot Area Background Color"));
                }
                if (p.containsKey("Plot Area Frame Color")) {
                    setGraphFrameColor(p.getProperty("Plot Area Frame Color"));
                }
                if (p.containsKey("Plot Area Frame Stroke Width")) {
                    setGraphFrameStrokeWidth(p.getProperty("Plot Area Frame Stroke Width"));
                }
                if (p.containsKey("Plot Area Major Grid Stroke Width")) {
                    setPanelMajorGridStrokeWidth(p.getProperty("Plot Area Major Grid Stroke Width"));
                }
                if (p.containsKey("Plot Area Minor Grid Stroke Width")) {
                    setPanelMinorGridStrokeWidth(p.getProperty("Plot Area Minor Grid Stroke Width"));
                }
                if (p.containsKey("Plot Area Major Grid Color")) {
                    setPanelGridMajorColor(p.getProperty("Plot Area Major Grid Color"));
                }
                if (p.containsKey("Plot Area Minor Grid Color")) {
                    setPanelGridMinorColor(p.getProperty("Plot Area Minor Grid Color"));
                }
                /*
                 * the axes parameters
                 */
                if (p.containsKey("Axis Ticks Color")) {
                    setAxisTicksColor(p.getProperty("Axis Ticks Color"));
                }
                if (p.containsKey("X Axis Text Color")) {
                    setxAxisLabelColor(p.getProperty("X Axis Text Color"));
                }
                if (p.containsKey("Y Axis Text Color")) {
                    setyAxisLabelColor(p.getProperty("Y Axis Text Color"));
                }
                if (p.containsKey("X Axis Title Color")) {
                    setxAxisTitleColor(p.getProperty("X Axis Title Color"));
                }
                if (p.containsKey("Y Axis Title Color")) {
                    setyAxisTitleColor(p.getProperty("Y Axis Title Color"));
                }
                if (p.containsKey("Axis Ticks Stroke Width")) {
                    setAxisTicksStrokeWidth(p.getProperty("Axis Ticks Stroke Width"));
                }
                /*
                 * the legend colors
                 */
                if (p.containsKey("Legend Text Color")) {
                    setLegendTextColor(p.getProperty("Legend Text Color"));
                }
                if (p.containsKey("Legend Title Color")) {
                    setLegendTitleColor(p.getProperty("Legend Title Color"));
                }
                if (p.containsKey("Legend Background Color")) {
                    setLegendBgColor(p.getProperty("Legend Background Color"));
                }
                /*
                 * the facet parameters
                 */
                if (p.containsKey("Facet Background Color")) {
                    setFacetLabelFill(p.getProperty("Facet Background Color"));
                }
                if (p.containsKey("Facet Frame Color")) {
                    setFacetFrameColor(p.getProperty("Facet Frame Color"));
                }
                if (p.containsKey("Facet Text Color")) {
                    setFacetTextColor(p.getProperty("Facet Text Color"));
                }
                if (p.containsKey("Facet Stroke Width")) {
                    setFacetFrameStrokeWidth(p.getProperty("Facet Stroke Width"));
                }
            } catch (Exception e) {
                all_ok = false;
            }
        } catch (Exception e) {
            all_ok = false;
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        return all_ok;
    }

    public String toXml(String graphThemeFolder, String forced_name) {
        File file;
        if (forced_name == null) {
            new File(graphThemeFolder).mkdirs();
            try {
                /*
                 * changed .xml for .txt extension in order to be compatible with the FIJI uploader
                 */
                file = java.io.File.createTempFile("theme", ".txt", new File(graphThemeFolder));//I had to remove the _ to prevent IJ from importing those files
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
                System.err.println("Error could not create the file file, maybe the disk is write protected");
                return null;
            }
        } else {
            file = new File(forced_name);
            file.delete();
        }
        FileOutputStream out = null;
        try {
            Properties p = new Properties();
            setPropertyIfNotNull(p, "Theme name", getThemeName());
            setPropertyIfNotNull(p, "Background Color", getOutsideGraphColor());
            /*
             * the title
             */
            setPropertyIfNotNull(p, "Main Plot Title Color", getPlotTitleColor());
            /*
             * the plot area parameters
             */
            setPropertyIfNotNull(p, "Plot Area Background Color", getGraphBgColor());
            setPropertyIfNotNull(p, "Plot Area Frame Color", getGraphFrameColor());
            setPropertyIfNotNull(p, "Plot Area Frame Stroke Width", getGraphFrameStrokeWidth());
            setPropertyIfNotNull(p, "Plot Area Major Grid Stroke Width", getPanelMajorGridStrokeWidth());
            setPropertyIfNotNull(p, "Plot Area Minor Grid Stroke Width", getPanelMinorGridStrokeWidth());
            setPropertyIfNotNull(p, "Plot Area Major Grid Color", getPanelGridMajorColor());
            setPropertyIfNotNull(p, "Plot Area Minor Grid Color", getPanelGridMinorColor());
            /*
             * the axes parameters
             */
            setPropertyIfNotNull(p, "Axis Ticks Color", getAxisTicksColor());
            setPropertyIfNotNull(p, "X Axis Text Color", getxAxisLabelColor());
            setPropertyIfNotNull(p, "Y Axis Text Color", getyAxisLabelColor());
            setPropertyIfNotNull(p, "X Axis Title Color", getxAxisTitleColor());
            setPropertyIfNotNull(p, "Y Axis Title Color", getyAxisTitleColor());
            setPropertyIfNotNull(p, "Axis Ticks Stroke Width", getAxisTicksStrokeWidth());
            /*
             * the legend colors
             */
            setPropertyIfNotNull(p, "Legend Text Color", getLegendTextColor());
            setPropertyIfNotNull(p, "Legend Title Color", getLegendTitleColor());
            setPropertyIfNotNull(p, "Legend Background Color", getLegendBgColor());
            /*
             * the facet parameters
             */
            setPropertyIfNotNull(p, "Facet Background Color", getFacetLabelFill());
            setPropertyIfNotNull(p, "Facet Frame Color", getFacetFrameColor());
            setPropertyIfNotNull(p, "Facet Text Color", getFacetTextColor());
            setPropertyIfNotNull(p, "Facet Stroke Width", getFacetFrameStrokeWidth());
            out = new FileOutputStream(file);
            p.storeToXML(out, "last update " + new Date().toString());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
        if (file != null) {
            return CommonClassesLight.change_path_separators_to_system_ones(file.toString());
        } else {
            return null;
        }
    }

    private String cloneString(String txt) {
        return txt == null ? null : new String(txt);
    }

    @Override
    public ThemeGraph clone() {
        ThemeGraph copy = new ThemeGraph();
        copy.graphBgColor = cloneString(graphBgColor);
        copy.axisTicksColor = cloneString(axisTicksColor);
        copy.xAxisLabelColor = cloneString(xAxisLabelColor);
        copy.yAxisLabelColor = cloneString(yAxisLabelColor);
        copy.xAxisTitleColor = cloneString(xAxisTitleColor);
        copy.yAxisTitleColor = cloneString(yAxisTitleColor);
        copy.plotTitleColor = cloneString(plotTitleColor);
        copy.outsideGraphColor = cloneString(outsideGraphColor);
        /*
         * controls the of the contour of legend shapes --> don't touch
         */
        copy.legendKeyColor = cloneString(legendKeyColor);
        copy.legendTextColor = cloneString(legendTextColor);
        copy.legendTitleColor = cloneString(legendTitleColor);
        copy.legendBgColor = cloneString(legendBgColor);
        copy.graphFrameColor = cloneString(graphFrameColor);
        copy.panelGridMajorColor = cloneString(panelGridMajorColor);
        copy.panelGridMinorColor = cloneString(panelGridMinorColor);
        copy.facetLabelFill = cloneString(facetLabelFill);
        copy.facetFrameColor = cloneString(facetFrameColor);
        copy.facetTextColor = cloneString(facetTextColor);
        /*
         * now the size parameters
         */
        copy.axisTicksStrokeWidth = cloneString(axisTicksStrokeWidth);
        copy.graphFrameStrokeWidth = cloneString(graphFrameStrokeWidth);
        copy.panelMajorGridStrokeWidth = cloneString(panelMajorGridStrokeWidth);
        copy.panelMinorGridStrokeWidth = cloneString(panelMinorGridStrokeWidth);
        copy.facetFrameStrokeWidth = cloneString(facetFrameStrokeWidth);
        copy.themeName = themeName;
        copy.path = path;

        return copy;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void setPropertyIfNotNull(Properties p, String key, String value) {
        if (value != null) {
            p.setProperty(key, value);
        }
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getAxisTicksStrokeWidth() {
        return axisTicksStrokeWidth;
    }

    public void setAxisTicksStrokeWidth(String axisTicksStrokeWidth) {
        this.axisTicksStrokeWidth = axisTicksStrokeWidth;
    }

    public String getGraphFrameStrokeWidth() {
        return graphFrameStrokeWidth;
    }

    public void setGraphFrameStrokeWidth(String graphFrameStrokeWidth) {
        this.graphFrameStrokeWidth = graphFrameStrokeWidth;
    }

    public String getPanelMajorGridStrokeWidth() {
        return panelMajorGridStrokeWidth;
    }

    public void setPanelMajorGridStrokeWidth(String panelMajorGridStrokeWidth) {
        this.panelMajorGridStrokeWidth = panelMajorGridStrokeWidth;
    }

    public String getPanelMinorGridStrokeWidth() {
        return panelMinorGridStrokeWidth;
    }

    public void setPanelMinorGridStrokeWidth(String panelMinorGridStrokeWidth) {
        this.panelMinorGridStrokeWidth = panelMinorGridStrokeWidth;
    }

    public String getFacetFrameStrokeWidth() {
        return facetFrameStrokeWidth;
    }

    public void setFacetFrameStrokeWidth(String facetFrameStrokeWidth) {
        this.facetFrameStrokeWidth = facetFrameStrokeWidth;
    }

    public String getLegendTitleColor() {
        return legendTitleColor;
    }

    public void setLegendTitleColor(String legendTitleColor) {
        this.legendTitleColor = legendTitleColor;
    }

    public String getLegendBgColor() {
        return legendBgColor;
    }

    public void setLegendBgColor(String legendBgColor) {
        this.legendBgColor = legendBgColor;
    }

    /**
     *
     * @return the color of the title
     */
    public String getPlotTitleColor() {
        return plotTitleColor;
    }

    /**
     * Sets the color of the contour of legend shapes --> don't touch
     *
     * @param plotTitleColor
     */
    public void setPlotTitleColor(String plotTitleColor) {
        this.plotTitleColor = plotTitleColor;
    }

    /**
     *
     * @return the color of legend entries
     */
    public String getLegendKeyColor() {
        return legendKeyColor;
    }

    /**
     * Controls the color of the contour of legend shapes --> don't touch
     *
     * @param legendKeyColor
     */
    public void setLegendKeyColor(String legendKeyColor) {
        this.legendKeyColor = legendKeyColor;
    }

    /**
     *
     * @return the color of the x axis labels
     */
    public String getxAxisLabelColor() {
        return xAxisLabelColor;
    }

    /**
     * Sets the color of the x axis labels
     *
     * @param xAxisLabelColor
     */
    public void setxAxisLabelColor(String xAxisLabelColor) {
        this.xAxisLabelColor = xAxisLabelColor;
    }

    /**
     *
     * @return the color of the y axis labels
     */
    public String getyAxisLabelColor() {
        return yAxisLabelColor;
    }

    /**
     * Sets the color of the y axis labels
     *
     * @param yAxisLabelColor
     */
    public void setyAxisLabelColor(String yAxisLabelColor) {
        this.yAxisLabelColor = yAxisLabelColor;
    }

    /**
     *
     * @return the color of the x axis title
     */
    public String getxAxisTitleColor() {
        return xAxisTitleColor;
    }

    /**
     * Sets the color of the x axis title
     *
     * @param xAxisTitleColor
     */
    public void setxAxisTitleColor(String xAxisTitleColor) {
        this.xAxisTitleColor = xAxisTitleColor;
    }

    /**
     *
     * @return the color of the y axis title
     */
    public String getyAxisTitleColor() {
        return yAxisTitleColor;
    }

    /**
     * Sets the color of the y axis title
     *
     * @param yAxisTitleColor
     */
    public void setyAxisTitleColor(String yAxisTitleColor) {
        this.yAxisTitleColor = yAxisTitleColor;
    }

    /**
     *
     * @return the background color outside the graph
     */
    public String getOutsideGraphColor() {
        return outsideGraphColor;
    }

    /**
     * Sets the background color outside the graph
     *
     * @param outsideGraphColor
     */
    public void setOutsideGraphColor(String outsideGraphColor) {
        this.outsideGraphColor = outsideGraphColor;
    }

    /**
     *
     * @return the color of the frame of the graph
     */
    public String getGraphFrameColor() {
        return graphFrameColor;
    }

    /**
     * Sets the color of the frame of the graph
     *
     * @param frameColor
     */
    public void setGraphFrameColor(String frameColor) {
        this.graphFrameColor = frameColor;
    }

    /**
     *
     * @return the color of the text of the legend
     */
    public String getLegendTextColor() {
        return legendTextColor;
    }

    /**
     * Sets the color of the text of the legend
     *
     * @param legendTextColor
     */
    public void setLegendTextColor(String legendTextColor) {
        this.legendTextColor = legendTextColor;
    }

    /**
     *
     * @return the color of the major grid lines
     */
    public String getPanelGridMajorColor() {
        return panelGridMajorColor;
    }

    /**
     * Sets the color of the major grid lines
     *
     * @param panelGridMajorColor
     */
    public void setPanelGridMajorColor(String panelGridMajorColor) {
        this.panelGridMajorColor = panelGridMajorColor;
    }

    /**
     *
     * @return the color of the minor grid lines
     */
    public String getPanelGridMinorColor() {
        return panelGridMinorColor;
    }

    /**
     * sets the color of the minor grid lines
     *
     * @param panelGridMinorColor
     */
    public void setPanelGridMinorColor(String panelGridMinorColor) {
        this.panelGridMinorColor = panelGridMinorColor;
    }

    /**
     *
     * @return the color of the facet text
     */
    public String getFacetTextColor() {
        return facetTextColor;
    }

    /**
     * Sets the color of the facet text
     *
     * @param facetTextColor
     */
    public void setFacetTextColor(String facetTextColor) {
        this.facetTextColor = facetTextColor;
    }

    /**
     *
     * @return the background fill color of the facets text area
     */
    public String getFacetLabelFill() {
        return facetLabelFill;
    }

    /**
     * Sets the background fill color of the facets text area
     *
     * @param facetLabelFill
     */
    public void setFacetLabelFill(String facetLabelFill) {
        this.facetLabelFill = facetLabelFill;
    }

    /**
     *
     * @return the color of facet labels
     */
    public String getFacetFrameColor() {
        return facetFrameColor;
    }

    /**
     *
     * @param facetLabelColor
     */
    public void setFacetFrameColor(String facetLabelColor) {
        this.facetFrameColor = facetLabelColor;
    }

    /**
     *
     * @return the background color for the graph
     */
    public String getGraphBgColor() {
        return graphBgColor;
    }

    /**
     * Sets the background color of the graph
     *
     * @param graphBgColor
     */
    public void setGraphBgColor(String graphBgColor) {
        this.graphBgColor = graphBgColor;
    }

    /**
     *
     * @return the color of axis ticks
     */
    public String getAxisTicksColor() {
        return axisTicksColor;
    }

    /**
     * Sets the color of axis ticks
     *
     * @param axisTicksColor
     */
    public void setAxisTicksColor(String axisTicksColor) {
        this.axisTicksColor = axisTicksColor;
    }

    /**
     *
     * @return an R interpretable string containing an R ggplot2 theme
     */
    @Override
    public String toString() {
        String theme = "theme(";
        if (axisTicksColor != null || axisTicksStrokeWidth != null) {
            theme += "axis.ticks = element_line(" + (axisTicksColor == null ? "" : "color = \"" + axisTicksColor + "\"") + (axisTicksColor != null && axisTicksStrokeWidth != null ? "," : "") + (axisTicksStrokeWidth == null ? "" : "size=" + axisTicksStrokeWidth) + "), ";
        }
        if (xAxisLabelColor != null) {
            theme += "axis.text.x = element_text(color = \"" + xAxisLabelColor + "\"), ";
        }
        if (yAxisLabelColor != null) {
            theme += "axis.text.y = element_text(color = \"" + yAxisLabelColor + "\"), ";
        }
        if (yAxisTitleColor != null) {
            theme += "axis.title.y = element_text(color = \"" + yAxisTitleColor + "\"), ";
        }
        if (xAxisTitleColor != null) {
            theme += "axis.title.x = element_text(color = \"" + xAxisTitleColor + "\"), ";
        }
        if (graphBgColor != null || graphFrameColor != null || graphFrameStrokeWidth != null) {
            theme += "panel.background = element_rect(";
            if (graphBgColor != null) {
                theme += "fill=\"" + graphBgColor + "\", ";
            }
            if (graphFrameColor != null) {
                theme += "color=\"" + graphFrameColor + "\", ";
            }
            if (graphFrameStrokeWidth != null) {
                theme += "size=" + graphFrameStrokeWidth + ", ";
            }
            if (theme.endsWith(", ")) {
                theme = theme.substring(0, theme.length() - 2);
            }
            theme += "), ";
        }
        if (panelGridMajorColor != null || panelMajorGridStrokeWidth != null) {
            if (panelMajorGridStrokeWidth != null && panelMajorGridStrokeWidth.toLowerCase().contains("element_blank()")) {
                theme += "panel.grid.major = element_blank(), ";
            } else {
                theme += "panel.grid.major = element_line(" + (panelGridMajorColor == null ? "" : "color =\"" + panelGridMajorColor + "\"") + (panelGridMajorColor != null && panelMajorGridStrokeWidth != null ? "," : "") + (panelMajorGridStrokeWidth == null ? "" : "size=" + panelMajorGridStrokeWidth) + "), ";
            }
        }
        if (panelGridMinorColor != null || panelMinorGridStrokeWidth != null) {
            if (panelMinorGridStrokeWidth != null && panelMinorGridStrokeWidth.toLowerCase().contains("element_blank()")) {
                theme += "panel.grid.minor = element_blank(), ";
            } else {
                theme += "panel.grid.minor = element_line(" + (panelGridMinorColor == null ? "" : "color =\"" + panelGridMinorColor + "\"") + (panelGridMinorColor != null && panelMinorGridStrokeWidth != null ? "," : "") + (panelMinorGridStrokeWidth == null ? "" : "size=" + panelMinorGridStrokeWidth) + "), ";
            }
        }
        if (facetLabelFill != null || facetFrameStrokeWidth != null || facetFrameColor != null) {
            theme += "strip.background = element_rect(";
            if (facetLabelFill != null) {
                theme += "fill=\"" + facetLabelFill + "\", ";
            }
            if (facetFrameStrokeWidth != null) {
                theme += "size=" + facetFrameStrokeWidth + ", ";
            }
            if (facetFrameColor != null) {
                theme += "color=\"" + facetFrameColor + "\", ";
            }
            if (theme.endsWith(", ")) {
                theme = theme.substring(0, theme.length() - 2);
            }
            theme += "), ";
        }
        if (facetTextColor != null) {
            theme += "strip.text = element_text(color = \"" + facetTextColor + "\"), ";
        }
        if (legendKeyColor != null) {
            theme += "legend.key = element_rect(color = \"" + legendKeyColor + "\"), ";
        }
        if (legendBgColor != null) {
            theme += "legend.background = element_rect(fill = \"" + legendBgColor + "\"), ";
        }
        if (legendTitleColor != null) {
            theme += "legend.title = element_text(color = \"" + legendTitleColor + "\"), ";
        }
        if (legendTextColor != null) {
            theme += "legend.text = element_text(color = \"" + legendTextColor + "\"), ";
        }
        if (facetTextColor != null) {
            theme += "strip.text = element_text(color = \"" + facetTextColor + "\"), ";
        }
        if (plotTitleColor != null) {
            theme += "plot.title = element_text(color = \"" + plotTitleColor + "\"), ";
        }
        if (outsideGraphColor != null) {
            theme += "plot.background = element_rect(fill = \"" + outsideGraphColor + "\"), ";
        }
        if (theme.endsWith(", ")) {
            theme = theme.substring(0, theme.length() - 2);
        }
        theme += ")";
        if (theme.equals("theme()")) {
            return "";
        }
        return theme;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        ThemeGraph test = new ThemeGraph();
        //test.setGraphBgColor("#FF00FF");
        //test.setAxisTicksColor("#AAAAAA"); //--> ca marche en fait
        //test.setLegendKeyColor("#FF0000");
        //test.setGraphFrameColor("#FF0000");
        //test.setPanelGridMajorColor("#FF0000");
        //test.setPanelGridMinorColor("#FF0000");
        //test.setFacetLabelFill("#000000"); //--> si a boir il faut changer la couleur du texte
        //test.setFacetLabelColor("#000000");
        //test.setLegendTextColor("#FF0000");
        //test.setxAxisLabelColor("#FF0000");
        //test.setPlotTitleColor("#FF00FF");
        test.setOutsideGraphColor("#FF0000");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



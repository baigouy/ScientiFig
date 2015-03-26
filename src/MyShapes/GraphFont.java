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

import java.awt.Font;
import java.io.Serializable;

/**
 * GraphFont is a class that contains all the font and stroke parameters for R
 * graphs
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 *
 */
public class GraphFont implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6186426765056167816L;
    float rescaling_factor = 1f;
    Font titleSize = new Font("Arial", 0, 12);
    Font xtitle = new Font("Arial", 0, 12);
    Font ytitle = new Font("Arial", 0, 12);
    Font xaxis = new Font("Arial", 0, 12);
    Font yaxis = new Font("Arial", 0, 12);
    Font legendTitle = new Font("Arial", 0, 12);
    Font legendText = new Font("Arial", 0, 12);
    String titleSizeName;
    String xtitleName;
    String ytitleName;
    String xaxisName;
    String yaxisName;
    String legendTitleName;
    String legendTextName;
    float lineSize = -1f;
    float PointSize = -1f;
    boolean warnIfTitleIsPresent = true;
    boolean warnIfLegendTitleIsPresent = true;
    boolean warnIfGridIsPresent = true;
    boolean warnIfHasBgColor = true;
    boolean warnIfUnitsAreMissing = true;
    boolean warnIfAxisTitlesAreMissing = true;
    boolean warnIfNobracketsAroundUnits = true;
    boolean warnIfColorsAreNotColorBlindFriendly = true;

    /**
     * Empty constructor
     */
    public GraphFont() {
    }

    /**
     * Constructor
     *
     * @param titleSize
     * @param xtitle
     * @param ytitle
     * @param xaxis
     * @param yaxis
     * @param legendTitle
     * @param legendText
     * @param lineSize
     * @param PointSize
     */
    public GraphFont(Font titleSize, Font xtitle, Font ytitle, Font xaxis, Font yaxis, Font legendTitle, Font legendText, float lineSize, float PointSize, boolean warnIfTitleIsPresent, boolean warnIfGridIsPresent, boolean warnIfHasBgColor, boolean warnIfUnitsAreMissing, boolean warnIfAxisTitlesAreMissing, boolean warnIfNobracketsAroundUnits, boolean warnIfLegendTitleIsPresent, boolean warnIfColorsAreNotColorBlindFriendly) {
        this.titleSize = titleSize;
        this.titleSizeName = titleSize.getFamily();
        this.xtitle = xtitle;
        this.xtitleName = xtitle.getFamily();
        this.ytitle = ytitle;
        this.ytitleName = ytitle.getFamily();
        this.xaxis = xaxis;
        this.xaxisName = xaxis.getFamily();
        this.yaxis = yaxis;
        this.yaxisName = yaxis.getFamily();
        this.legendText = legendText;
        this.legendTextName = legendText.getFamily();
        this.legendTitle = legendTitle;
        this.legendTitleName = legendTitle.getFamily();
        this.lineSize = lineSize;
        this.PointSize = PointSize;
        this.warnIfTitleIsPresent = warnIfTitleIsPresent;
        this.warnIfGridIsPresent = warnIfGridIsPresent;
        this.warnIfHasBgColor = warnIfHasBgColor;
        this.warnIfUnitsAreMissing = warnIfUnitsAreMissing;
        this.warnIfAxisTitlesAreMissing = warnIfAxisTitlesAreMissing;
        this.warnIfNobracketsAroundUnits = warnIfNobracketsAroundUnits;
        this.warnIfLegendTitleIsPresent = warnIfLegendTitleIsPresent;
        this.warnIfColorsAreNotColorBlindFriendly = warnIfColorsAreNotColorBlindFriendly;
    }

    /**
     *
     * @return true if there is only one font for all elements of the graph
     */
    public boolean hasCommonFont() {
        if (titleSize != null && titleSize.equals(xtitle) && titleSize.equals(ytitle) && titleSize.equals(xtitle) && titleSize.equals(xaxis) && titleSize.equals(yaxis) && titleSize.equals(legendText) && titleSize.equals(legendTitle)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return the common font
     */
    public Font getCommonFont() {
        if (hasCommonFont()) {
            return titleSize;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphFont)) {
            return false;
        }
        GraphFont gf = ((GraphFont) obj);
        /*
         * we check font families
         */
        if (!gf.getLegendTextName().equalsIgnoreCase(this.getLegendTextName())) {
            return false;
        }
        if (!gf.getLegendTitleName().equalsIgnoreCase(this.getLegendTitleName())) {
            return false;
        }
        if (!gf.getXaxisName().equalsIgnoreCase(this.getXaxisName())) {
            return false;
        }
        if (!gf.getXtitleName().equalsIgnoreCase(this.getXtitleName())) {
            return false;
        }
        if (!gf.getYaxisName().equalsIgnoreCase(this.getYaxisName())) {
            return false;
        }
        if (!gf.getYtitleName().equalsIgnoreCase(this.getYtitleName())) {
            return false;
        }
        if (!gf.getTitleSizeName().equalsIgnoreCase(this.getTitleSizeName())) {
            return false;
        }
        /*
         * now we check sizes
         */
        if (gf.getLegendText().getSize() != this.getLegendText().getSize()) {
            return false;
        }
        if (gf.getLegendTitle().getSize() != this.getLegendTitle().getSize()) {
            return false;
        }
        if (gf.getXaxis().getSize() != this.getXaxis().getSize()) {
            return false;
        }
        if (gf.getXtitle().getSize() != this.getXtitle().getSize()) {
            return false;
        }
        if (gf.getYaxis().getSize() != this.getYaxis().getSize()) {
            return false;
        }
        if (gf.getYtitle().getSize() != this.getYtitle().getSize()) {
            return false;
        }
        if (gf.getTitleSize().getSize() != this.getTitleSize().getSize()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return an R interpretable command to dealing with ggplot2 themes
     */
    @Override
    public String toString() {
        return "theme(plot.title = element_text(family=\"" + (titleSizeName == null ? titleSize.getFamily() : titleSizeName) + "\", size=" + (titleSize.getSize2D() / rescaling_factor) + "))"
                + "\n+ theme(axis.title.x = element_text(family=\"" + (xtitleName == null ? xtitle.getFamily() : xtitleName) + "\", size=" + xtitle.getSize2D() / rescaling_factor + "))"
                + "\n+ theme(axis.title.y = element_text(family=\"" + (ytitleName == null ? ytitle.getFamily() : ytitleName) + "\", size=" + ytitle.getSize2D() / rescaling_factor + "))"
                + "\n+ theme(legend.text = element_text(family=\"" + (legendTextName == null ? legendText.getFamily() : legendTextName) + "\", size=" + legendText.getSize2D() / rescaling_factor + "))"
                + "\n+ theme(legend.title = element_text(family=\"" + (legendTitleName == null ? legendTitle.getFamily() : legendTitleName) + "\", size=" + legendTitle.getSize2D() / rescaling_factor + "))"
                + "\n+ theme(axis.text.x = element_text(family=\"" + (xaxisName == null ? xaxis.getFamily() : xaxisName) + "\", size=" + xaxis.getSize2D() / rescaling_factor + "))"
                + "\n+ theme(axis.text.y = element_text(family=\"" + (yaxisName == null ? yaxis.getFamily() : yaxisName) + "\", size=" + yaxis.getSize2D() / rescaling_factor + "))";
    }

    /**
     *
     * @return the rescaling factor for the font size
     */
    public float getRescaling_factor() {
        return rescaling_factor;
    }

    /**
     * Sets the rescaling factor for the font size
     *
     * @param rescaling_factor
     */
    public void setRescaling_factor(float rescaling_factor) {
        this.rescaling_factor = rescaling_factor;
    }

    /**
     *
     * @return the title font
     */
    public Font getTitleSize() {
        return titleSize;
    }

    /**
     * Sets the title font
     *
     * @param titleSize
     */
    public void setTitleSize(Font titleSize) {
        this.titleSize = titleSize;
    }

    /**
     *
     * @return the X axis title font
     */
    public Font getXtitle() {
        return xtitle;
    }

    /**
     * Sets the X axis title font
     *
     * @param xtitle
     */
    public void setXtitle(Font xtitle) {
        this.xtitle = xtitle;
    }

    /**
     *
     * @return the Y axis title font
     */
    public Font getYtitle() {
        return ytitle;
    }

    /**
     * Sets the Y axis title font
     *
     * @param ytitle
     */
    public void setYtitle(Font ytitle) {
        this.ytitle = ytitle;
    }

    /**
     *
     * @return the x axis label font
     */
    public Font getXaxis() {
        return xaxis;
    }

    /**
     * Sets the x axis label font
     *
     * @param xaxis
     */
    public void setXaxis(Font xaxis) {
        this.xaxis = xaxis;
    }

    /**
     *
     * @return the y axis label font
     */
    public Font getYaxis() {
        return yaxis;
    }

    /**
     * Sets the y axis label font
     *
     * @param yaxis
     */
    public void setYaxis(Font yaxis) {
        this.yaxis = yaxis;
    }

    /**
     *
     * @return the legend title font
     */
    public Font getLegendTitle() {
        return legendTitle;
    }

    /**
     * Sets the legend title font
     *
     * @param legendTitle
     */
    public void setLegendTitle(Font legendTitle) {
        this.legendTitle = legendTitle;
    }

    /**
     *
     * @return the legend text font
     */
    public Font getLegendText() {
        return legendText;
    }

    /**
     * Sets the legend text font
     *
     * @param legendText
     */
    public void setLegendText(Font legendText) {
        this.legendText = legendText;
    }

    /**
     *
     * @return the line width
     */
    public float getLineSize() {
        return lineSize;
    }

    /**
     * Sets the line width (if line width is less than 0, settings will be
     * ignored)
     *
     * @param lineSize
     */
    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    /**
     *
     * @return the point size
     */
    public float getPointSize() {
        return PointSize;
    }

    /**
     * Sets the point size
     *
     * @param PointSize
     */
    public void setPointSize(float PointSize) {
        this.PointSize = PointSize;
    }

    public String getTitleSizeName() {
        return titleSizeName == null ? titleSize.getFamily() : titleSizeName;
    }

    public void setTitleSizeName(String titleSizeName) {
        this.titleSizeName = titleSizeName;
    }

    public String getXtitleName() {
        return xtitleName == null ? xtitle.getFamily() : xtitleName;
    }

    public void setXtitleName(String xtitleName) {
        this.xtitleName = xtitleName;
    }

    public String getYtitleName() {
        return ytitleName == null ? ytitle.getFamily() : ytitleName;
    }

    public void setYtitleName(String ytitleName) {
        this.ytitleName = ytitleName;
    }

    public String getXaxisName() {
        return xaxisName == null ? xaxis.getFamily() : xaxisName;
    }

    public void setXaxisName(String xaxisName) {
        this.xaxisName = xaxisName;
    }

    public String getYaxisName() {
        return yaxisName == null ? yaxis.getFamily() : yaxisName;
    }

    public void setYaxisName(String yaxisName) {
        this.yaxisName = yaxisName;
    }

    public String getLegendTitleName() {
        return legendTitleName == null ? legendTitle.getFamily() : legendTitleName;
    }

    public void setLegendTitleName(String legendTitleName) {
        this.legendTitleName = legendTitleName;
    }

    public String getLegendTextName() {
        return legendTextName == null ? legendText.getFamily() : legendTextName;
    }

    public void setLegendTextName(String legendTextName) {
        this.legendTextName = legendTextName;
    }

    public boolean isWarnIfTitleIsPresent() {
        return warnIfTitleIsPresent;
    }

    public void setWarnIfTitleIsPresent(boolean warnIfTitleIsPresent) {
        this.warnIfTitleIsPresent = warnIfTitleIsPresent;
    }

    public boolean isWarnIfColorsAreNotColorBlindFriendly() {
        return warnIfColorsAreNotColorBlindFriendly;
    }

    public void setWarnIfColorsAreNotColorBlindFriendly(boolean warnIfColorsAreNotColorBlindFriendly) {
        this.warnIfColorsAreNotColorBlindFriendly = warnIfColorsAreNotColorBlindFriendly;
    }

    public boolean isWarnIfLegendTitleIsPresent() {
        return warnIfLegendTitleIsPresent;
    }

    public void setWarnIfLegendTitleIsPresent(boolean warnIfLegendTitleIsPresent) {
        this.warnIfLegendTitleIsPresent = warnIfLegendTitleIsPresent;
    }

    public boolean isWarnIfGridIsPresent() {
        return warnIfGridIsPresent;
    }

    public void setWarnIfGridIsPresent(boolean warnIfGridIsPresent) {
        this.warnIfGridIsPresent = warnIfGridIsPresent;
    }

    public boolean isWarnIfHasBgColor() {
        return warnIfHasBgColor;
    }

    public void setWarnIfHasBgColor(boolean warnIfHasBgColor) {
        this.warnIfHasBgColor = warnIfHasBgColor;
    }

    public boolean isWarnIfUnitsAreMissing() {
        return warnIfUnitsAreMissing;
    }

    public void setWarnIfUnitsAreMissing(boolean warnIfUnitsAreMissing) {
        this.warnIfUnitsAreMissing = warnIfUnitsAreMissing;
    }

    public boolean isWarnIfAxisTitlesAreMissing() {
        return warnIfAxisTitlesAreMissing;
    }

    public void setWarnIfAxisTitlesAreMissing(boolean warnIfAxisTitlesAreMissing) {
        this.warnIfAxisTitlesAreMissing = warnIfAxisTitlesAreMissing;
    }

    public boolean isWarnIfNobracketsAroundUnits() {
        return warnIfNobracketsAroundUnits;
    }

    public void setWarnIfNobracketsAroundUnits(boolean warnIfNobracketsAroundUnits) {
        this.warnIfNobracketsAroundUnits = warnIfNobracketsAroundUnits;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
//           CommonClasses.setMaxNbOfProcessorsToMaxPossible();
//           ArrayList<String> list = new LoadListeToArrayList().apply("/list.lst");
        long start_time = System.currentTimeMillis();
        Font ft = new Font("Arial", 0, 12);
        GraphFont test = new GraphFont(ft, ft, ft, ft, ft, ft, ft, 1, 1, true, false, true, false, true, true, true, true);
        System.out.println(test);
//           test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



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

import R.String.RLabel;
import java.io.Serializable;

/**
 * GGtitles handles GGplot2 titles, axis titles and legend title
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class GGtitles implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -5065183274657124794L;
    String mainTitle;
    String xlabel;
    String ylabel;
    String legendlabel;

    /**
     * empty constructor
     */
    public GGtitles() {
    }

    public GGtitles(RLabel titleLabel, RLabel xAxisLabel, RLabel yAxisLabel, RLabel legendLabel) {
        if (titleLabel == null) {
            mainTitle = null;
        } else {
            String title = titleLabel.toRExpression();
            mainTitle = title == null ? null : title;
        }
        if (xAxisLabel == null) {
            xlabel = null;
        } else {
            String x = xAxisLabel.toRExpression();
            xlabel = x == null ? null : x;
        }
        if (yAxisLabel == null) {
            ylabel = null;
        } else {
            String y = yAxisLabel.toRExpression();
            ylabel = y == null ? null : y;
        }
        if (legendLabel == null) {
            legendlabel = null;
        } else {
            String legend = legendLabel.toRExpression();
            legendlabel = legend == null ? null : legend;
        }
    }

    /**
     * Constructor
     *
     * @param mainTitle main graph title
     */
    public GGtitles(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    /**
     * Constructor
     *
     * @param mainTitle main graph title
     * @param xlabel title for the x axis
     * @param ylabel title for the y axis
     * @param legendlabel title for the legend
     */
    public GGtitles(String mainTitle, String xlabel, String ylabel, String legendlabel) {
        this.mainTitle = mainTitle;
        this.xlabel = xlabel;
        this.ylabel = ylabel;
        this.legendlabel = legendlabel;
    }

    /**
     *
     * @return the title of the legend
     */
    public String getLegendlabel() {
        return legendlabel;
    }

    /**
     * Sets the title of the legend
     *
     * @param legendlabel
     */
    public void setLegendlabel(String legendlabel) {
        this.legendlabel = legendlabel;
    }

    /**
     *
     * @return the main title of the graph
     */
    public String getMainTitle() {
        return mainTitle;
    }

    /**
     * Sets the main title of the graph
     *
     * @param mainTitle
     */
    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    /**
     *
     * @return the title of the x axis
     */
    public String getXlabel() {
        return xlabel;
    }

    /**
     * Sets the title of the x axis
     *
     * @param xlabel
     */
    public void setXlabel(String xlabel) {
        this.xlabel = xlabel;
    }

    /**
     *
     * @return the title of the y axis
     */
    public String getYlabel() {
        return ylabel;
    }

    /**
     * Sets the title of the y axis
     *
     * @param ylabel
     */
    public void setYlabel(String ylabel) {
        this.ylabel = ylabel;
    }

    /**
     * @return an R interpretable string containing all the titles
     */
    @Override
    public String toString() {
        String master = "";
//        boolean mustForceRemoveLegendTitle = false;
//        String forceRemoveLegendTitle = " + theme(legend.title=element_blank())";
        if (mainTitle != null && !mainTitle.equals("")) {
            /*
             * hack for R stylized text
             */
            String quotes = "\"";
            if (mainTitle.startsWith("expression(")) {
                quotes = "";
            }
            master = "ggtitle(" + quotes + mainTitle + quotes + ")";
        }
        String x = "";
        if (xlabel != null && !xlabel.equals("")) {
            /*
             * hack for R stylized text
             */
            String quotes = "\"";
            if (xlabel.startsWith("expression(")) {
                quotes = "";
            }
            x = "xlab(" + quotes + xlabel + quotes + ")";
        }
        //use default title instead
//        else {
//            x = "xlab('')";
//        }
        String y = "";
        if (ylabel != null && !ylabel.equals("")) {
            /*
             * hack for R stylized text
             */
            String quotes = "\"";
            if (ylabel.startsWith("expression(")) {
                quotes = "";
            }
            y = "ylab(" + quotes + ylabel + quotes + ")";
        }
//        else {
//            y = "ylab('')";
//        }
        String titles = "";
        if (!master.equals("")) {
            titles += master + " + ";
        }
        if (!x.equals("")) {
            titles += x + " + ";
        }
        if (!y.equals("")) {
            titles += y + " + ";
        }
        if (legendlabel != null /*&& !legendlabel.equals("")*/) {
//            if (legendlabel.equals("")) {
//                mustForceRemoveLegendTitle = true;
//            } 
                /*
             * hack for R stylized text
             */
            String quotes = "\"";
            if (legendlabel.startsWith("expression(") || legendlabel.equals("")) {
                quotes = "";
            }
            //                    + "\n+ scale_colour_discrete(guide = guide_legend(title = NULL))"
            /*
             * Nb: null means remove the title, we actually remove the title if it is an empty text
             */
            titles += "labs(colour = " + quotes + (legendlabel.equals("") ? "NULL" : legendlabel) + quotes + ")"
                    + " + " + "labs(fill = " + quotes + (legendlabel.equals("") ? "NULL" : legendlabel) + quotes + ")"
                    + " + " + "labs(shape = " + quotes + (legendlabel.equals("") ? "NULL" : legendlabel) + quotes + ")";
        }
        while (titles.endsWith(" + ")) {
            titles = titles.substring(0, titles.length() - 3);
        }
//        if (mustForceRemoveLegendTitle) {
//            titles += forceRemoveLegendTitle;
//        }
        return titles;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        GGtitles test = new GGtitles();
        test.setMainTitle("totoµ"); //ca marche pas mal
        test.setXlabel("toto");
        test.setYlabel("toqsd");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



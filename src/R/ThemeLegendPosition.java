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
 * ThemeLegendPosition positions the R legend with respect to the graph
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ThemeLegendPosition implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 249426468777651148L;
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int CENTER = 4;
    public static final int NOBAR = 5;
    int POS = -1;
    int POS2 = -1;

    /**
     * empty constructor
     */
    public ThemeLegendPosition() {
    }

    /**
     * Constructor to position legend outside of the graph area
     *
     * @param POS
     */
    public ThemeLegendPosition(int POS) {
        this.POS = POS;
    }

    /**
     * Constructor to position legend inside of the graph area
     *
     * @param POS
     * @param POS2
     */
    public ThemeLegendPosition(int POS, int POS2) {
        this.POS = POS;
        this.POS2 = POS2;
    }

    /**
     * Sets the position of the legend width respect to the graph area
     *
     * @param POS
     */
    public void setLegendPosition(int POS) {
        this.POS = POS;
    }

    /**
     * Sets the position of the legend width respect to the graph area
     *
     * @param POS
     * @param POS2
     */
    public void setLegendPosition(int POS, int POS2) {
        this.POS = POS;
        this.POS2 = POS2;
    }

    /**
     *
     * @return an R/ggplot2 command to position the legend
     */
    @Override
    public String toString() {
        if ((POS == NOBAR) || (POS2 == NOBAR)) {
            return "theme(legend.position=\"none\")";
        }
        if (POS2 != -1) {
            if (POS == -1) {
                return "";
            }
            float x = 0;
            float y = 0;
            switch (POS) {
                case TOP:
                    x = 1;
                    break;
                case BOTTOM:
                    x = 0;
                    break;
                case CENTER:
                    x = 0.5f;
                    break;
            }
            switch (POS2) {
                case LEFT:
                    y = 0;
                    break;
                case RIGHT:
                    y = 1;
                    break;
                case CENTER:
                    y = 0.5f;
                    break;
            }
            return "theme(legend.position=c(" + y + "," + x + "), legend.justification=c(" + y + "," + x + "))";
        } else {
            if (POS == -1) {
                return "";
            }
            String legend_pos = "theme(legend.position=\"";
            switch (POS) {
                case TOP:
                    legend_pos += "top";
                    break;
                case BOTTOM:
                    legend_pos += "bottom";
                    break;
                case LEFT:
                    legend_pos += "left";
                    break;
                case RIGHT:
                    legend_pos += "right";
                    break;
            }
            legend_pos += "\")";
            return legend_pos;
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        ThemeLegendPosition test = new ThemeLegendPosition();
        test.setLegendPosition(ThemeLegendPosition.TOP);
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



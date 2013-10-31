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

import Commons.CommonClassesLight;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * ScaleColor is used to manually set R colors (for R fills and colors)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ScaleColor implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -428509210482399155L;
    String type;
    private ArrayList<Integer> colors = new ArrayList<Integer>();

    /**
     * Constructor
     *
     * @param type type of R scale (fill_manual, color_manual, ...)
     */
    public ScaleColor(String type) {
        this.type = type;

    }

    @Override
    public String toString() {
        String scale = "scale_" + type + "_manual(values=c(";
        for (Integer col : colors) {
            scale += "\"" + CommonClassesLight.toHtmlColor(col) + "\", ";
        }
        if (scale.endsWith(", ")) {
            scale = scale.substring(0, scale.length() - 2);
        }
        scale += "))";
        if (scale.equals("scale_" + type + "_manual(values=c())")) {
            return "";
        }
        return scale;
    }

    /**
     *
     * @return the type of R scale (fill_manual, color_manual, ...)
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of R scale (fill_manual, color_manual, ...)
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return the R colors used for the scale
     */
    public ArrayList<Integer> getColors() {
        return colors;
    }

    /**
     * Sets the R colors used for the scale
     *
     * @param colors
     */
    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    /**
     * Sets the nb of R colors allowed (IMPORTANTLY: if there are not enough
     * colors defined by the user, the soft generates random colors for the
     * missing ones)
     *
     * @param total_nb_of_colors
     */
    public void setNbOfColors(int total_nb_of_colors) {
        if (colors == null || colors.size() < total_nb_of_colors) {
            if (colors == null) {
                colors = new ArrayList<Integer>();
            }
            for (int i = colors.size(); i < total_nb_of_colors; i++) {
                if (i < CommonClassesLight.colorBlindFriendlyPalette.length) {
                    /*
                     * do first prefer color blind friendly colors if available
                     */
                    colors.add(CommonClassesLight.colorBlindFriendlyPalette[i]);
                } else {
                    int new_random_color = CommonClassesLight.new_unique_random_color(colors);
                    colors.add(new_random_color);
                }
            }
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
        ScaleColor test = new ScaleColor("color");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFF0000);
        colors.add(0x00FF00);
        colors.add(0x0000FF);
        test.setColors(colors);
        test.setNbOfColors(6);
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



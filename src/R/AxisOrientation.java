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

import java.io.Serializable;

/**
 * AxisOrientation controls the orientation of the labels of the x or y axes
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class AxisOrientation implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 741831237137522713L;
    int orientationInDegrees = -1;
    String axis;

    /**
     * Empty constructor
     */
    public AxisOrientation() {
    }

    /**
     * Constructor
     *
     * @param axis x or y axis
     * @param orientation angular inclination of the labels (in degrees)
     */
    public AxisOrientation(String axis, int orientation) {
        this.axis = axis;
        this.orientationInDegrees = orientation;
    }

    /**
     *
     * @return the angular inclination of the labels (in degrees)
     */
    public int getOrientationInDegrees() {
        return orientationInDegrees;
    }

    /**
     * Sets the angular inclination of the labels (in degrees)
     *
     * @param orientationInDegrees
     */
    public void setOrientationInDegrees(int orientationInDegrees) {
        this.orientationInDegrees = orientationInDegrees;
    }

    /**
     *
     * @return the current axis
     */
    public String getAxis() {
        return axis;
    }

    /**
     * Sets the current axis
     *
     * @param axis
     */
    public void setAxis(String axis) {
        this.axis = axis;
    }

    /**
     * @return an R compatible string containing axis labels orientation
     */
    @Override
    public String toString() {
        if (axis == null || axis.equals("")) {
            return "";
        }
        if (orientationInDegrees <= 0) {
            return "";
        }
        /*
         * we need to adjust the position of the text with respect to the ticks
         */
        String just = "";
        if (axis.equals("x")) {
            just = ", vjust = 0.5";
        }
        if (axis.equals("y")) {
            just = ", hjust = 0.5";
        }
        return "theme(axis.text." + axis + " = element_text(angle =" + orientationInDegrees + just + "))";
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        AxisOrientation test = new AxisOrientation();
        test.setAxis("y");
        test.setOrientationInDegrees(90);
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



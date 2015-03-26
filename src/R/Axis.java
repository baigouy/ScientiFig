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

import java.io.Serializable;

/**
 * The Axis class deals with ggplot2 axes (transormations, labels, range...)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Axis implements Serializable {

    /**
     * Varaibles declaration
     */
    public static final long serialVersionUID = -9063301461842781248L;
    String axis;
    boolean customRange = false;
    float beginRange = 0;
    float tickSpace = 0;
    float endRange = 0;
    boolean removeTicks = false;
    boolean axisFlip = false;
    boolean removeAxis = false;
    String transfo;

    /**
     * Axis constructor
     *
     * @param axis name of the axis (typically x or y)
     */
    public Axis(String axis) {
        this.axis = axis;
    }

    /**
     * Axis empty/default constructor
     */
    public Axis() {
    }

    /**
     *
     * @return a boolean indicating if the axis should be removed
     */
    public boolean isRemoveAxis() {
        return removeAxis;
    }

    /**
     * Defines whether the axis should be removed or not
     *
     * @param removeAxis
     */
    public void setRemoveAxis(boolean removeAxis) {
        this.removeAxis = removeAxis;
    }

    /**
     *
     * @return the axis name (x or y most likely)
     */
    public String getAxis() {
        return axis;
    }

    /**
     * Sets the axis name (x or y)
     *
     * @param axis
     */
    public void setAxis(String axis) {
        this.axis = axis;
    }

    /**
     *
     * @return a boolean indicating if the range of the axis is custom or not
     */
    public boolean isCustomRange() {
        return customRange;
    }

    /**
     * Defines whether the range for the axis should be custom
     *
     * @param customRange
     */
    public void setCustomRange(boolean customRange) {
        this.customRange = customRange;
    }

    /**
     *
     * @return the starting point for the axis
     */
    public float getBeginRange() {
        return beginRange;
    }

    /**
     * Sets the starting point for the axis
     *
     * @param beginRange
     */
    public void setBeginRange(float beginRange) {
        this.beginRange = beginRange;
    }

    /**
     *
     * @return the axis end point
     */
    public float getEndRange() {
        return endRange;
    }

    /**
     * Sets the axis end point
     *
     * @param endRange
     */
    public void setEndRange(float endRange) {
        this.endRange = endRange;
    }

    /**
     *
     * @return the current axis transformation (log, exp, ...)
     */
    public String getTRANSFORMATION() {
        return transfo;
    }

    /**
     * Sets the axis transformation (log, exp, ...)
     *
     * @param transfo
     */
    public void setTRANSFORMATION(String transfo) {
        this.transfo = transfo;
    }

    /**
     *
     * @return whether the axis has been flipped
     */
    public boolean isAxisFlip() {
        return axisFlip;
    }

    /**
     * Sets whether the axis should be flipped
     *
     * @param axisFlip
     */
    public void setAxisFlip(boolean axisFlip) {
        this.axisFlip = axisFlip;
    }

    /**
     *
     * @return the space between axis ticks
     */
    public float getTickSpace() {
        return tickSpace;
    }

    /**
     * Sets the space between axis ticks
     *
     * @param tickSpace
     */
    public void setTickSpace(float tickSpace) {
        this.tickSpace = tickSpace;
    }

    /**
     *
     * @return whether ticks should be removed
     */
    public boolean isRemoveTicks() {
        return removeTicks;
    }

    /**
     * Defines whether ticks should be removed
     *
     * @param removeTicks
     */
    public void setRemoveTicks(boolean removeTicks) {
        this.removeTicks = removeTicks;
    }

    /**
     * @return an R compatible string containing axis description
     */
    @Override
    public String toString() {
        String Axis = "";
        if (axis == null || axis.equals("")) {
            return Axis;
        }
        if (removeAxis) {
            return "theme(axis.ticks = element_blank(), axis.text." + axis + " = element_blank())";
        }
        if (removeTicks) {
            Axis += "scale_" + axis + "_discrete(breaks=NULL) + ";
        }
        if (customRange) {
            Axis += "coord_cartesian(" + axis + "lim=c(" + beginRange + "," + endRange + ")) + scale_" + axis + "_continuous(breaks=seq(" + (beginRange - tickSpace) + "," + (endRange + tickSpace) + "," + tickSpace + ")) + ";
        }
        if (axisFlip) {
            Axis += "scale_" + axis + "_reverse() + ";
        }
        if (transfo == null) {
            if (Axis.endsWith(" + ")) {
                Axis = Axis.substring(0, Axis.length() - 3);
            }
            return Axis;
        }
        return Axis + "coord_trans(" + axis + "=\"" + transfo + "\")";
    }

    /**
     * Defines the axis range
     *
     * @param begin
     * @param end
     * @param spacing
     */
    public void setRange(float begin, float end, float spacing) {
        this.customRange = true;
        this.beginRange = begin;
        this.endRange = end;
        this.tickSpace = spacing;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        Axis test = new Axis();
        test.setAxis("y");
        test.setAxisFlip(true);
        test.setTRANSFORMATION("log10");
        test.setRange(0, 100, 0.25f);
        test.setRemoveTicks(true);
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



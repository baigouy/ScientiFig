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
 * The Facets class handles ggplot2 powerful facets
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Facets implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -5566649296541116808L;
    String vertical = null;
    String horizontal = null;
    boolean freeScale = false;
    boolean freeSpace = false;

    /**
     * GGplot 2 facet constructor
     *
     * @param vertical vertcial facets
     * @param horizontal horizontal facets
     * @param freeScale
     * @param freeSpace
     */
    public Facets(String vertical, String horizontal, boolean freeScale, boolean freeSpace) {
        this.freeScale = freeScale;
        this.freeSpace = freeSpace;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /**
     * Empty constructor
     */
    public Facets() {
    }

    /**
     *
     * @return the vertical facet
     */
    public String getVertical() {
        return vertical;
    }

    /**
     * Sets the vertical facet
     *
     * @param vertical
     */
    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    /**
     *
     * @return the horizontal facet
     */
    public String getHorizontal() {
        return horizontal;
    }

    /**
     * Sets the horizontal facet
     *
     * @param horizontal
     */
    public void setHorizontal(String horizontal) {
        this.horizontal = horizontal;
    }

    /**
     *
     * @return if the scale is free
     */
    public boolean isFreeScale() {
        return freeScale;
    }

    /**
     * Sets if the scale should be free
     *
     * @param freeScale
     */
    public void setFreeScale(boolean freeScale) {
        this.freeScale = freeScale;
    }

    /**
     *
     * @return if it is space free
     */
    public boolean isFreeSpace() {
        return freeSpace;
    }

    /**
     * Defines if the plots should be space free
     *
     * @param freeSpace
     */
    public void setFreeSpace(boolean freeSpace) {
        this.freeSpace = freeSpace;
    }

    /**
     * @return creates an R interpretable string for the facets
     */
    @Override
    public String toString() {
        if (vertical == null || vertical.equals("")) {
            vertical = ".";
        }
        if (horizontal == null || horizontal.equals("")) {
            horizontal = ".";
        }
        String freescale = "";
        if (freeScale) {
            freescale = "scales=\"free\"";
        }
        String freespace = "";
        if (freeSpace) {
            freespace = "space=\"free\"";
        }
        String formatted_text = "";
        if (freeScale) {
            formatted_text += ", " + freescale;
        }
        if (freeSpace && freeScale) {
            formatted_text += ", " + freespace;
        } else if (freeSpace) {
            formatted_text += ", " + freespace;
        }
        String facet = "facet_grid(" + vertical + "~" + horizontal + formatted_text + ")";
        if (facet.contains(".~.")) {
            facet = "";
        }
        return facet;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        Facets test = new Facets();
        test.setHorizontal("type");
        test.setVertical("type");
        test.setFreeScale(true);
        test.setFreeSpace(true);
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



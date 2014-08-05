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
 * GGplot is a class that contains the source data for ggplot2 plots
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class GGplot implements Serializable {

    public static final long serialVersionUID = -1325841163339266124L;
    /*
     * name of the source data
     */
    String dataFrame;

    /**
     * Empty constructor
     */
    public GGplot() {
    }

    /**
     * Constructor
     *
     * @param dataFrame the name of the source R dataframe to use for plots
     */
    public GGplot(String dataFrame) {
        this.dataFrame = dataFrame;
    }

    /**
     *
     * @return the ggplot dataframe name
     */
    public String getDataFrame() {
        return dataFrame;
    }

    /**
     * Sets the ggplot dataframe
     *
     * @param dataFrame
     */
    public void setDataFrame(String dataFrame) {
        this.dataFrame = dataFrame;
    }

    /*
     * creates an R compatible string out of the current plot
     */
    @Override
    public String toString() {
        String ggplot = "ggplot(";
        if (dataFrame != null) {
            ggplot += "data=" + dataFrame + ", ";
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
        GGplot test = new GGplot();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



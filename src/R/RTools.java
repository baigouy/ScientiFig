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

/**
 * RTools contains various useful functions to create R arrays, or to parse R
 * commands
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class RTools {

    /**
     * Empty constructor
     */
    public RTools() {
    }

    /**
     * Creates an R array
     *
     * @param <T>
     * @param arrayType
     * @param object
     * @return the R code for an array
     */
    public static <T> String createRArray(String arrayType, T... object) {
        if (object == null) {
            return "";
        }
        String R_array = arrayType + "=c(";
        for (Object o : object) {
            R_array += "\"" + o.toString() + "\", ";
        }
        R_array = R_array.substring(0, R_array.length() - 2);
        R_array += ")";
        return R_array;
    }

    /**
     * Creates an R color array
     *
     * @param colors
     * @return the R code for a color array
     */
    public static String createColorArray(int... colors) {
        String R_array = "values=c(";
        for (int color : colors) {
            R_array += "\"" + CommonClassesLight.toHtmlColor(color) + "\", ";
        }
        R_array = R_array.substring(0, R_array.length() - 2);
        R_array += ")";
        return R_array;
    }

    /**
     * recovers aes from the text of an R plot command
     *
     * @param command
     * @return the string representation of an aes
     */
    public static String getAes(String command) {
        if (!command.contains("aes")) {
            return "";
        }
        return "aes" + CommonClassesLight.strCutLeftFirst(CommonClassesLight.strCutRightFisrt(command, "aes"), ")") + ")";
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        RTools test = new RTools();
        System.out.println(RTools.getAes("ggplot(data) + geom_point(aes(x, y, fill = type, colour = type, shape = type)) + geom_line(aes(x, y, fill = type, colour = type, shape = type))+geom_smooth(se=FALSE)"));
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



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
package Tools;

import Commons.CommonClassesLight;
import ij.plugin.PlugIn;

/**
 * A class that opens a web browser to get access to the online help for
 * ScientiFig and FiguR
 *
 * <BR>NB: the class is compatible with ImageJ/FIJI
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Help implements PlugIn {

    public static final String URL_SF = "http://srv-gred.u-clermont1.fr/labmirouse/software/";
    public static final String URL_TA = "http://srv-gred.u-clermont1.fr/labmirouse/software/WebPA/index.html";
    
    /**
     * Empty constructor
     */
    public Help() {
    }

    /**
     * Opens the SF main web page
     *
     * @param arg
     */
    @Override
    public void run(String arg) {
        CommonClassesLight.browse(arg);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //
        //http://srv-gred.u-clermont1.fr/labmirouse/software/WebPA/index.html
        String path = URL_SF;
        new Help().run(path);
    }
}



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
package Tools;

import java.awt.Toolkit;

/*
 * A little bit of biblio before we start
 * http://sebsauvage.net/comprendre/dpi/index.html
 * http://stackoverflow.com/questions/139655/convert-pixels-to-points
 */
/**
 * Cm2PxConverter converts pixels to centimeters or inches and centimeters to
 * inches
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Converter {

    /*
     * 72 Dpi is the defaut Illustrator dpi
     * 90 DPi is the defaut Inkscape dpi
     * too bad these guys can't use the same standards
     */
    final static double conversionFactorFromCmToPx72dpi = getPixelPerCmAt72dpi();
    final static double conversionFactorFromCmToPx90dpi = getPixelPerCmAt90dpi();
    final static double conversionFactorFromCmToPxScreen = getPixelsPerCmOfScreen();
    final static double dpp_screen = getScreenDpi();
    /**
     * conversion factor from inches to centimeters too bad people aren't using
     * the same standards
     */
    final static double INCH_IN_CM = 2.54;
    /**
     * now the conversion factors from px to points or em
     */
    private static final double PIXEL_TO_POINT_CONVERSION_FACTOR = 0.75;
    private static final double PIXEL_TO_EM = 1. / 16.;
    private static final double EM_TO_POINT = 16. * PIXEL_TO_POINT_CONVERSION_FACTOR;

    public static double pointsToPixels(double points) {
        return points / PIXEL_TO_POINT_CONVERSION_FACTOR;
    }

    public static double pixelsToPoints(double pixels) {
        return pixels * PIXEL_TO_POINT_CONVERSION_FACTOR;
    }

    public static double pixelsToEm(double pixels) {
        return pixels * PIXEL_TO_EM;
    }

    public static double emToPixels(double ems) {
        return ems / PIXEL_TO_EM;
    }

    public static double emToPoints(double ems) {
        return ems * EM_TO_POINT;
    }

    public static double pointToEm(double points) {
        return points / EM_TO_POINT;
    }

    /**
     *
     * @param inches
     * @return converts inches to centimeters
     */
    public static double inch2Cm(double inches) {
        return inches * INCH_IN_CM;
    }

    /**
     *
     * @param cm
     * @return converts centimeters to inches
     */
    public static double Cm2Inch(double cm) {
        return cm / INCH_IN_CM;
    }

    /**
     *
     * @param px size in pixels
     * @param dpi
     * @return converts pixels to cm at a particular dpi
     */
    public static double PxToCmAnyDpi(double px, double dpi) {
        return px / getPixelPerCmAtAnyDpi(dpi);
    }

    /**
     *
     * @param px
     * @return converts pixels to cm for a dpi = 90
     */
    public static double PxToCm90dpi(double px) {
        return px / conversionFactorFromCmToPx90dpi;
    }

    /**
     *
     * @param px
     * @return converts pixels to cm for a dpi = 72
     */
    public static double PxToCm72dpi(double px) {
        return px / conversionFactorFromCmToPx72dpi;
    }

    /**
     *
     * @param px
     * @return converts px to centimeters at screen resolution
     */
    public static double PxToCm(double px) {
        return px / conversionFactorFromCmToPxScreen;
    }

    /**
     *
     * @param cm
     * @param dpi
     * @return Converts pixels to centimeters at the specified dpi
     */
    public static double cmToPxAnyDpi(double cm, double dpi) {
        return cm * getPixelPerCmAtAnyDpi(dpi);
    }

    /**
     *
     * @param cm
     * @return Converts centimeters to pixels at dpi = 90
     */
    public static double cmToPx90dpi(double cm) {
        return cm * conversionFactorFromCmToPx90dpi;
    }

    /**
     *
     * @param cm
     * @return Converts centimeters to pixels at dpi = 72
     */
    public static double cmToPx72dpi(double cm) {
        return cm * conversionFactorFromCmToPx72dpi;
    }

    /**
     *
     * @param cm
     * @return Converts centimeters to pixels at current screen resolution
     */
    public static double cmToPx(double cm) {
        return cm * conversionFactorFromCmToPxScreen;
    }

    /**
     *
     * @return the conversion factor to convert centimeters to pixels
     */
    public static double getConversionFactorFromCmToPx() {
        return conversionFactorFromCmToPxScreen;
    }

    /**
     *
     * @return the dpi of the screen (usually 72)
     */
    public static int getScreenDpi() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int dpi = tk.getScreenResolution();
        return dpi;
    }

    /**
     *
     * @return the number of pixels per centimeter for a dpi = 90
     */
    public static double getPixelPerCmAt90dpi() {
        return 90. / INCH_IN_CM;
    }

    /**
     *
     * @return the number of pixels per centimeter for a dpi = 72
     */
    public static double getPixelPerCmAt72dpi() {
        return 72. / INCH_IN_CM;
    }

    /**
     *
     * @param dpi
     * @return the number of pixels per centimeter for any custom dpi
     */
    public static double getPixelPerCmAtAnyDpi(double dpi) {
        return dpi / INCH_IN_CM;
    }

    /**
     *
     * @return the number of pixels for 1 centimeter of screen
     */
    public static double getPixelsPerCmOfScreen() {
        return (double) getScreenDpi() / INCH_IN_CM;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        /*
         * px to cm or cm to pixels
         */
        System.out.println("pixels <--> cm");
        System.out.println(Converter.PxToCm72dpi(1350));
        System.out.println(Converter.PxToCm72dpi(1012));
        System.out.println(Converter.PxToCm72dpi(665));
        System.out.println(Converter.PxToCm72dpi(740));
        System.out.println(Converter.cmToPxAnyDpi(21, 90));
        System.out.println(Converter.cmToPx90dpi(21));
        System.out.println(Converter.cmToPx72dpi(21));
        System.out.println(Converter.cmToPx72dpi(29.7));
        System.out.println(Converter.getPixelsPerCmOfScreen());

        /*
         * now the px to point conversions
         */
        System.out.println("------------------");
        System.out.println("pixels <--> points");
        System.out.println(Converter.pixelsToPoints(16));
        System.out.println(Converter.pointsToPixels(12));
        System.out.println(Converter.pointsToPixels(6));
        System.out.println(Converter.pixelsToEm(16));
        System.out.println(Converter.emToPixels(1));
        System.out.println(Converter.pointToEm(12));
        System.out.println(Converter.emToPoints(1));

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



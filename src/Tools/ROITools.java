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
package Tools;

import MyShapes.Contourable;
import MyShapes.Fillable;
import MyShapes.MyLine2D;
import MyShapes.MyPoint2D;

/**
 * Common tools for ROIs
 *
 * @author benoit aigouy
 */
public class ROITools {

    /**
     *
     * @param obj
     * @return true if the contour of the object is transparent or if the object
     * has no contour
     */
    public static boolean isDrawTransparent(Object obj) {
        if (obj instanceof Contourable) {
            if (!isFloatingText(obj)) {
                if (((Contourable) obj).getDrawOpacity() == 0f) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param obj
     * @return true if the filling of the object is transparent or if the object
     * cannot be filled
     */
    public static boolean isFillTransparent(Object obj) {
        if (obj instanceof Fillable) {
            if (!isFloatingText(obj)) {
                if (((Fillable) obj).getFillOpacity() == 0f) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param obj
     * @return true if the object is transparent
     */
    public static boolean isTransparent(Object obj) {
        return isDrawTransparent(obj) && isFillTransparent(obj);
    }

    public static boolean isLine(Object obj) {
        return obj instanceof MyLine2D;
    }

    public static boolean isBracket(Object obj) {
        return isLine(obj) && ((MyLine2D) obj).isBracket();
    }

    public static boolean isArrow(Object obj) {
        return isLine(obj) && ((MyLine2D) obj).isIsArrow();
    }

    public static boolean isFloatingText(Object obj) {
        return (obj instanceof MyPoint2D) && ((MyPoint2D) obj).isText();
    }

}

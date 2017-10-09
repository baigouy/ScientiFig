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
package MyShapes;

import ij.gui.EllipseRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import java.util.ArrayList;

/**
 *
 * @author benoit
 */
public class RoiConverter {

    public ArrayList<Object> convertIJRoiToSFRoi(Roi[] IJroi) {
        if (IJroi == null) {
            return null;
        }
        ArrayList<Object> ROIs = new ArrayList<Object>();
        for (Roi IJroi1 : IJroi) {
            ROIs.add(processROI(IJroi1));
        }
        return ROIs;
    }

    public Object convertIJRoiToSFRoi(Roi IJroi) {
        if (IJroi == null) {
            return null;
        }
        return processROI(IJroi);
    }

    private Object processROI(Roi curROI) {
        Object RoiSF = null;
        if (curROI.getType() == IJCompatibility.IJ_RECT && !curROI.getTypeAsString().toLowerCase().contains("ext")) {
            RoiSF = new MyRectangle2D.Double(curROI);
        } else if (curROI.getType() == IJCompatibility.IJ_RECT && curROI.getTypeAsString().toLowerCase().contains("ext")) {
            RoiSF = new MyPoint2D.Double(curROI);

            MyPoint2D.Double txt = new MyPoint2D.Double(curROI);
        }
        if (curROI.getType() == IJCompatibility.IJ_LINE) {
            RoiSF = new MyLine2D.Double(curROI);
        }
        if (curROI.getType() == IJCompatibility.IJ_ELLIPSE) {
            if (curROI instanceof EllipseRoi) {
                RoiSF = new MyEllipse2D.Double(curROI);
            } else {
                /**
                 * dirty hack for ovalROI --> we convert it to a polygonROI, no
                 * clue why there are two ROIs for ellipses, ovalROI is
                 * particularly bad because none of the parameters can be
                 * accessed
                 */
                PolygonRoi pr = new PolygonRoi(curROI.getFloatPolygon(), PolygonRoi.FREEROI);
                RoiSF = new MyPolygon2D.Double(pr);
            }
        }
        if (curROI.getType() == IJCompatibility.IJ_POLYGON || curROI.getType() == IJCompatibility.IJ_FREEHAND) {
            RoiSF = new MyPolygon2D.Double(curROI);
        }
        if (curROI.getType() == IJCompatibility.IJ_POLYLINE) {
            RoiSF = new MyPolyline2D.Double(curROI);
        }
        if (curROI.getType() == IJCompatibility.IJ_POINT) {
            //    RoiSF = new MyPoint2D.Double(curROI);
            /**
             * in fact let's ignore it it doesn't make any sense to display a
             * point ROI over an image
             */
        }
//        System.out.println(curROI.getTypeAsString() + " " + curROI.getType());
        return RoiSF;
    }

}

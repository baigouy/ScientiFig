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
package MyShapes;

import Commons.CommonClassesLight;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * MyShapesTools is a set of tools for shapes
 *
 * @since <B>SF v2.3</B>
 * @author Benoit Aigouy
 */
public class MyShapesTools {

    public MyShapesTools() {
    }

    /**
     * calculates rotation of a series of points around a central point by an
     * angle (in degrees)
     *
     * @param points
     * @param angle
     * @param center
     * @return a set of rotated points around an anchor point
     */
    public ArrayList<Object> rotatePoints(ArrayList<Object> points, double angle, Point2D.Double center) {
        if (points == null) {
            return null;
        }
        for (Object double1 : points) {
            if (double1 instanceof MyEllipse2D.Double) {
                MyEllipse2D.Double curPoint = (MyEllipse2D.Double) double1;
                Point2D.Double pos = curPoint.getPoint();
                Point2D.Double rotatedPoint = CommonClassesLight.rotatePointInRadians(pos, center, angle);
                curPoint.setPoint(rotatedPoint);
            }
        }
        return points;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        MyShapesTools test = new MyShapesTools();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


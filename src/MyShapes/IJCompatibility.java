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

import ij.gui.Roi;
import java.io.Serializable;

/**
 * IJCompatibility is an interface to allow conversion of PA/TA/ScientiFig
 * geometrical shapes to their ImageJ counterparts
 *
 * @author Benoit Aigouy
 */
public interface IJCompatibility extends Serializable {

    public static final long serialVersionUID = 6279452597649430489L;
    public static final int IJ_RECT = Roi.RECTANGLE;
    public static final int IJ_ELLIPSE = Roi.OVAL;
    public static final int IJ_POLYGON = Roi.POLYGON;
    public static final int IJ_POLYLINE = Roi.POLYLINE;
    public static final int IJ_POINT = Roi.POINT;
    public static final int IJ_LINE = Roi.LINE;

    /**
     *
     * @return the ImageJ ROI type that best describes the curret vectorial
     * object
     */
    public int getIJType();

    /**
     *
     * @return the ImageJ ROI that best fits the current vectorial object
     */
    public Roi getIJRoi();
}



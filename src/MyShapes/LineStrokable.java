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

import java.awt.BasicStroke;
import java.io.Serializable;

/**
 * LineStrokable is an interface for objects with stroke
 *
 * @author Benoit Aigouy
 */
public interface LineStrokable extends Serializable {

    public static final long serialVersionUID = -916716261911251632L;
    /*
     * Line stroking
     */
    public static final int PLAIN = 0;
    public static final int DASHED = 1;
    public static final int DOTTED = 2;
    public static final int DASH_DOT = 3;

    /*
     * Defines whether the line should be plain, dashed or dotted for example
     */
    public void setLineStrokeType(int LINESTROKE);

    /*
     * returns the current line stroke
     */
    public int getLineStrokeType();

    public boolean isDashed();

    public boolean isDotted();

    public boolean isPlain();

    public boolean isDashDot();

    public boolean isLineStrokeOfType(int TYPE);

    public int getSkipSize();

    public int getDashSize();

    public int getDotSize();

    public void setSkipSize(int size);

    public void setDashSize(int size);

    public void setDotSize(int size);

    public float[] createLineStroke();

    public BasicStroke getLineStroke();
}



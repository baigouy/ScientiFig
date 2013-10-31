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
package MyShapes;

/**
 * The Incompressible interface contains the incompressible dimensions of a
 * vectorial object (e.g. text height).
 *
 * @since <B>Packing Analyzer 8.2</B>
 * @author Benoit Aigouy
 */
public interface Incompressible {

    public static final long serialVersionUID = 207600961241168289L;

    /**
     *
     * @return true if the width of the object is incompressible
     */
    public boolean isWidthIncompressible();

    /**
     *
     * @return true if the height of the object is incompressible
     */
    public boolean isHeightIncompressible();

    /**
     *
     * @return the width of the 'width incompressible' object
     */
    public double getIncompressibleWidth();

    /**
     *
     * @return the height of the 'height incompressible' object
     */
    public double getIncompressibleHeight();
}



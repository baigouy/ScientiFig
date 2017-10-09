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
package Commons;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * G2dParameters saves and/or restores Graphics2D parameters
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public class G2dParameters {

    /**
     * Variables
     */
    public Color color;
    public Stroke stroke;
    public Font font;
    public Composite alpha;
    public AffineTransform at;

    /**
     * Constructor
     *
     * @param g2d the source graphics2d from which we wanna extract all the
     * parameters
     */
    public G2dParameters(Graphics2D g2d) {
        this.color = g2d.getColor();
        this.stroke = g2d.getStroke();
        this.font = g2d.getFont();
        this.alpha = g2d.getComposite();
        this.at = g2d.getTransform();
    }

    /**
     *
     * @return the g2d affineTransfrom
     */
    public AffineTransform getAt() {
        return at;
    }

    /**
     * Sets the g2d affineTransfrom
     *
     * @param at
     */
    public void setAt(AffineTransform at) {
        this.at = at;
    }

    /**
     *
     * @return the g2d alphaComposite
     */
    public Composite getAlpha() {
        return alpha;
    }

    /**
     * Sets the g2d alphaComposite
     *
     * @param alpha
     */
    public void setAlpha(Composite alpha) {
        this.alpha = alpha;
    }

    /**
     *
     * @return the g2d default font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the g2d default font
     *
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     *
     * @return the g2d default color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the g2d default color
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     *
     * @return the g2d stroke size
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * Sets the g2d stroke size
     *
     * @param stroke
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * Applies the extracted parameters to a g2d
     *
     * @param g2d
     */
    public void restore(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(stroke);
        g2d.setFont(font);
        g2d.setComposite(alpha);
        g2d.setTransform(at);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



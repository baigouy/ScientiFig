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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * The Drawable interface takes care of the drawing of vectorial objects
 * <BR><br> nb: the names of the functions are self explanatory, so I
 * will not describe them further
 *
 * @author Benoit Aigouy
 */
public interface Drawable extends Serializable {

    public static final long serialVersionUID = -917714261910251633L;
    public static final int BELOW = 0;
    public static final int ABOVE = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final float[] dottedSelection = {2, 5, 1, 6};

    /**
     *
     * @param g2d
     * @param color
     * @param transparency
     * @param strokeSize
     */
    public void draw(Graphics2D g2d, int color, float transparency, float strokeSize);

    /**
     *
     * @param g2d
     * @param color
     * @param transparency
     * @param strokeSize
     */
    public void fill(Graphics2D g2d, int color, float transparency, float strokeSize);

    /**
     *
     * @param g2d
     * @param color
     * @param transparency
     * @param strokeSize
     */
    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSize);

    /**
     *
     * @param g2d
     * @param color
     */
    public void draw(Graphics2D g2d, int color);

    /**
     *
     * @param g2d
     * @param color
     */
    public void fill(Graphics2D g2d, int color);

    /**
     *
     * @param g2d
     * @param color
     */
    public void drawAndFill(Graphics2D g2d, int color);

    /**
     *
     * @param g2d
     * @param transparency
     */
    public void drawTransparent(Graphics2D g2d, float transparency);

    /**
     *
     * @param g2d
     * @param transparency
     */
    public void fillTransparent(Graphics2D g2d, float transparency);

    /**
     *
     * @param g2d
     * @param transparency
     */
    public void drawAndFillTransparent(Graphics2D g2d, float transparency);

    /**
     *
     * @param g2d
     */
    public void draw(Graphics2D g2d);

    /**
     *
     * @param g2d
     */
    public void fill(Graphics2D g2d);

    /**
     *
     * @param g2d
     */
    public void drawAndFill(Graphics2D g2d);

    /**
     *
     * @param g2d
     */
    public void drawTransparent(Graphics2D g2d);

    /**
     *
     * @param g2d
     */
    public void fillTransparent(Graphics2D g2d);

    /**
     *
     * @param g2d
     */
    public void drawAndFillTransparent(Graphics2D g2d);

    /**
     *
     * @param g2d
     * @param visibleRect
     */
    public void drawIfVisible(Graphics2D g2d, Rectangle visibleRect);

    /**
     *
     * @param g2d
     * @param visibleRect
     */
    public void drawSelection(Graphics2D g2d, Rectangle visibleRect);

    /**
     *
     * @param g2d
     * @param visibleRect
     */
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect);

    /**
     *
     * @param g2d
     * @param color
     */
    public void drawSkeletton(Graphics2D g2d, int color);

    /**
     *
     * @param g2d
     * @param color
     */
    public void fillSkeletton(Graphics2D g2d, int color);

    /**
     *
     * @param visibleRect
     * @return true if the vectorial object is visible
     */
    public boolean isVisible(Rectangle visibleRect);

    /**
     *
     * @param g2d
     * @param position
     */
    public void drawShapeInfo(Graphics2D g2d, int position);

    /**
     *
     * @param g2d
     * @param text
     * @param position
     */
    public void drawText(Graphics2D g2d, String text, int position);
}



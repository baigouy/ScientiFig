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

import Commons.Point3D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TextBar is a text side bar that can be horizontal or vertical
 *
 * @author Benoit Aigouy
 */
public class TopBar extends MyRectangle2D implements PARoi, Transformable, Drawable, Serializable, Incompressible {

    /**
     * Variables
     */
    public static final long serialVersionUID = 4684352783970000844L;
    transient Row associated_row;
    HashMap<Point3D.Integer, ColoredTextPaneSerializable> begin_n_ends_and_corresponding_text;
    int ORIENTATION;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL_LEFT = 1;
    public static final int VERTICAL_RIGHT = 2;
//    int TEXT_POSITION;
    ComplexShapeLight current;

    /**
     * Constructor
     *
     * @param associated_row
     * @param begin_n_ends_and_corresponding_text
     * @param ORIENTATION
     */
    public TopBar(Row associated_row, HashMap<Point3D.Integer, ColoredTextPaneSerializable> begin_n_ends_and_corresponding_text, int ORIENTATION) {
        this.associated_row = associated_row;
        this.begin_n_ends_and_corresponding_text = begin_n_ends_and_corresponding_text;
        this.ORIENTATION = ORIENTATION;
//        this.TEXT_POSITION = TEXT_POSITION;
        if (ORIENTATION == HORIZONTAL) {
            current = getHorizontalBar();
        } else {
            if (ORIENTATION == VERTICAL_LEFT) {
                current = getVerticalBarLeft();
            } else {
                current = getVerticalBarRight();
            }
        }
        Rectangle2D r = getBounds2D();
        if (r != null) {
            this.rec2d = new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        } else {
            rec2d = new Rectangle2D.Double();
        }
    }

    private ComplexShapeLight getHorizontalBar() {
        ComplexShapeLight c = null;
        ArrayList<Object> texts = new ArrayList<Object>();
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            Object val = entry.getKey();
            /**
             * added to support retrocompatibility
             */
            Point3D.Integer pos;
            if (val instanceof Point3D.Integer) {
                pos = (Point3D.Integer) val;
            } else {
                pos = new Point3D.Integer((Point) val, 0);
            }
            Rectangle2D bar = associated_row.getPositionForRows(pos.getX(), pos.getY());
            if (bar != null) {
                texts.add(new TextBar.Double(bar.getX(), bar.getY(), bar.getWidth(), entry.getValue(), null, TextBar.HORIZONTAL, pos.getZ()));
            }
        }
        if (!texts.isEmpty()) {
            c = new ComplexShapeLight(texts);
        }
        return c;
    }

    @Override
    public void setFirstCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - pos.x, bounding.getY() - pos.y);
        current.translate(-trans.x, -trans.y);
        updateRect();
    }

    @Override
    public Rectangle2D getBounds2D() {
        if (current == null) {
            return null;
        }
        return current.getBounds2D();
    }

    @Override
    public Rectangle getBounds() {
        if (current == null) {
            return new Rectangle();
        }
        return current.getBounds();
    }

    private ComplexShapeLight getVerticalBarLeft() {
        ComplexShapeLight c = null;
        ArrayList<Object> texts = new ArrayList<Object>();
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            Object val = entry.getKey();
            /**
             * added to support retrocompatibility
             */
            Point3D.Integer pos;
            if (val instanceof Point3D.Integer) {
                pos = (Point3D.Integer) val;
            } else {
                pos = new Point3D.Integer((Point) val, 0);
            }
            Rectangle2D bar = associated_row.getPositionForCols(pos.x, pos.y, TopBar.LEFT);
            /*
             * dirty fix to allow swapping of panels with text on the left --> erreur --> better fix needed some day, the thing is that it goes too far in the images, in the getpositionForCols --> il faudrait le bloquer avant
             *  && bar.getX()==0.
             */
            if (bar != null) {
                /*
                 * bug fix for swapping
                 */
                if (bar.getX() != 0.) {
                    bar = new Rectangle2D.Double(0., bar.getY(), bar.getWidth(), bar.getHeight());
                }
                texts.add(new TextBar.Double(bar.getX(), bar.getY(), bar.getHeight(), entry.getValue(), null, TextBar.VERTICAL, pos.z));
            }
        }
        if (!texts.isEmpty()) {
            c = new ComplexShapeLight(texts);
        }
        return c;
    }

    private ComplexShapeLight getVerticalBarRight() {
        ComplexShapeLight c = null;
        ArrayList<Object> texts = new ArrayList<Object>();
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            Object val = entry.getKey();
            /**
             * added to support retrocompatibility
             */
            Point3D.Integer pos;
            if (val instanceof Point3D.Integer) {
                pos = (Point3D.Integer) val;
            } else {
                pos = new Point3D.Integer((Point) val, 0);
            }
            Rectangle2D bar = associated_row.getPositionForCols(pos.x, pos.y, TopBar.RIGHT);
            if (bar != null) {
                if (bar.getX() != 0.) {
                    bar = new Rectangle2D.Double(0., bar.getY(), bar.getWidth(), bar.getHeight());
                }
                texts.add(new TextBar.Double(bar.getX(), bar.getY(), bar.getHeight(), entry.getValue(), null, TextBar.VERTICAL, pos.z));
            }
        }
        if (!texts.isEmpty()) {
            c = new ComplexShapeLight(texts);
        }
        return c;
    }

    public HashMap<Point3D.Integer, ColoredTextPaneSerializable> getBegin_n_ends_and_corresponding_text() {
        return begin_n_ends_and_corresponding_text;
    }

    public void setBegin_n_ends_and_corresponding_text(HashMap<Point3D.Integer, ColoredTextPaneSerializable> begin_n_ends_and_corresponding_text) {
        this.begin_n_ends_and_corresponding_text = begin_n_ends_and_corresponding_text;
    }

    /**
     * updates the side bar
     */
    public void update() {
        if (ORIENTATION == HORIZONTAL) {
            current = getHorizontalBar();
        } else {
            if (ORIENTATION == VERTICAL_LEFT) {
                current = getVerticalBarLeft();
            } else {
                current = getVerticalBarRight();
            }
        }
        updateRect();
    }

    private void updateRect() {
        if (current == null) {
            this.rec2d = new Rectangle2D.Double();
        } else {
            Rectangle2D r = current.getBounds2D();
            this.rec2d = new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight());//getBounds2D();
        }
    }

    @Override
    public void translate(double x, double y) {
        if (current == null) {
            return;
        }
        super.translate(x, y);
        current.translate(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (current == null) {
            return;
        }
        current.draw(g2d);
    }

    @Override
    public boolean isWidthIncompressible() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isHeightIncompressible() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getIncompressibleWidth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getIncompressibleHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getTextreadyForSerialization() {
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            ColoredTextPaneSerializable ctps = entry.getValue();
            if (ctps != null) {
                ctps.getReadyForSerialization();
            }
        }
    }

    void recreateStyledDoc() {
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            ColoredTextPaneSerializable ctps = entry.getValue();
            if (ctps != null) {
                ctps.recreateStyledDoc();
            }
        }
    }

    /**
     * translates the textbar so that the x position of the top left corner of
     * its bounding rect is at position x
     *
     * @param x
     */
    public void setX(double x) {
        if (current == null) {
            return;
        }
        current.setX(x);
        updateRect();
    }

    /**
     * translates the textbar so that the y position of the top left corner of
     * its bounding rect is at position y
     *
     * @param y
     */
    public void setY(double y) {
        if (current == null) {
            return;
        }
        current.setY(y);
        updateRect();
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    public boolean checkFonts(JournalParameters jp) {
        boolean modified = false;
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            ColoredTextPaneSerializable txt = entry.getValue();
            if (txt.checkFont(null, jp.getOutterTextFont(), jp.getOutterTextFontName())) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean checkText(JournalParameters jp) {
        boolean modified = false;
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            ColoredTextPaneSerializable txt = entry.getValue();
            if (txt.checkText(jp, null)) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean checkStyle() {
        boolean modified = false;
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            ColoredTextPaneSerializable txt = entry.getValue();
            if (txt.checkStyle(null)) {
                modified = true;
            }
        }
        return modified;
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

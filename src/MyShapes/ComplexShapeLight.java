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

import Dialogs.ColoredTextPane;
import Commons.G2dParameters;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * ComplexShape is a vectorial object that regroups other vectorial objects of
 * any kind
 *
 * @since <B>Packing Analyzer 6.6</B>
 * @author Benoit Aigouy
 */
public class ComplexShapeLight extends MyRectangle2D implements LineStrokable, Transformable, Drawable, Serializable, Morphable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6163652894050210829L;
    LinkedHashSet< Object> pos_n_shapes;
    public static final int ROWS_FIRST_THEN_COLUMNS = 0;
    public static final int COLUMNS_FIRST_THEN_ROWS = 1;

    /**
     * Constructs a ComplexShapeLight out of several vectorial objects
     *
     * @param shapes
     */
    public ComplexShapeLight(ArrayList<Object> shapes) {
        if (shapes != null && !shapes.isEmpty()) {
            pos_n_shapes = new LinkedHashSet<Object>(shapes);
        } else {
            pos_n_shapes = new LinkedHashSet<Object>();
        }
        updateMasterRect();
    }

    /**
     * Constructs a ComplexShapeLight out of several vectorial objects
     *
     * @param shapes
     */
    public ComplexShapeLight(LinkedHashSet<Object> shapes) {
        this.pos_n_shapes = shapes;
        updateMasterRect();
    }

    /**
     * Constructs a ComplexShapeLight out of another ComplexShapeLight
     *
     * @param myel
     */
    public ComplexShapeLight(ComplexShapeLight myel) {
        LinkedHashSet< Object> shapes = new LinkedHashSet<Object>();
        for (Object object : shapes) {
            shapes.add(((PARoi) object).clone());
        }
        this.pos_n_shapes = shapes;
        this.rec2d = myel.rec2d;
        this.color = myel.color;
        this.strokeSize = myel.strokeSize;
        this.opacity = myel.opacity;//transparencyve opacity from shapes that don't need it
    }

    /**
     * Constructs a ComplexShapeLight out of a single vectorial object, other
     * objects can be added later on
     *
     * @param shape
     */
    public ComplexShapeLight(Object shape) {
        pos_n_shapes = new LinkedHashSet<Object>();
        if (shape != null) {
            addShape(shape);
        }
    }

    public ComplexShapeLight(List<Object> shape) {
        pos_n_shapes = new LinkedHashSet<Object>();
        if (shape != null) {
            for (Object object : shape) {
                addShape(object);
            }
        }
    }

    public int size() {
        if (pos_n_shapes == null) {
            return 0;
        }
        return pos_n_shapes.size();
    }

    @Override
    public void setDotSize(int size) {
        for (Object object : pos_n_shapes) {
            if (object instanceof LineStrokable) {
                ((LineStrokable) object).setDotSize(size);
            }
        }
    }

    @Override
    public void setDashSize(int size) {
        for (Object object : pos_n_shapes) {
            if (object instanceof LineStrokable) {
                ((LineStrokable) object).setDashSize(size);
            }
        }
    }

    @Override
    public void setSkipSize(int size) {
        for (Object object : pos_n_shapes) {
            if (object instanceof LineStrokable) {
                ((LineStrokable) object).setSkipSize(size);
            }
        }
    }

    @Override
    public void setLineStrokeType(int type) {
        for (Object object : pos_n_shapes) {
            if (object instanceof LineStrokable) {
                ((LineStrokable) object).setLineStrokeType(type);
            }
        }
    }

    /**
     * translates the whole group so that the position of the x of its x upper
     * left corner position is at position 'x'
     *
     * @param x
     */
    public void setX(double x) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - x, bounding.getY());
        for (Object object : pos_n_shapes) {
            if (object instanceof Transformable) {
                ((Transformable) object).translate(-trans.x, 0);
            }
        }
        updateMasterRect();
    }

    /**
     * translates the whole group so that the position of the y of its y upper
     * left corner position is at position 'y'
     *
     * @param y
     */
    public void setY(double y) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX(), bounding.getY() - y);
        for (Object object : pos_n_shapes) {
            if (object instanceof Transformable) {
                ((Transformable) object).translate(0, -trans.y);
            }
        }
        updateMasterRect();
    }

    @Override
    public Object clone() {
        return new ComplexShapeLight(this);
    }

    /**
     * Add a new shape to the group
     *
     * @param shape
     */
    public void addShape(Object shape) {
        pos_n_shapes.add(shape);
        updateMasterRect();
    }

    /**
     *
     * @param table
     */
    public void match(ArrayList<Object> table) {
        int max = Math.min(table.size(), pos_n_shapes.size());
        int pos = 0;
        for (Object object : pos_n_shapes) {
            Point2D.Double center = ((Transformable) table.get(pos)).getCenter();
            ((Transformable) object).setCenter(center);
            pos++;
            if (pos == max) {
                return;
            }
        }
    }

    /**
     * Create a montage out of a series of images
     *
     * @param nb_cols number of columns
     * @param nb_lines number of lines
     * @param space space between images
     * @param MODE comb or meander (order of the images in the montage)
     * @deprecated
     */
    public void createTable(int nb_cols, int nb_lines, double space, int MODE) {
        int size = nb_cols * nb_lines;
        Point2D.Double max_size = getMaxSize(size);
        double width = max_size.x;
        double height = max_size.y;
        Point2D.Double min_pos = getMinPos(size);
        double trans_x = min_pos.x;
        double trans_y = min_pos.y;

        ArrayList<Object> positions = new ArrayList<Object>();
        switch (MODE) {
            case ROWS_FIRST_THEN_COLUMNS:
                for (int i = 0; i < nb_cols; i++) {
                    for (int j = 0; j < nb_lines; j++) {
                        positions.add(new MyRectangle2D.Double(new Rectangle2D.Double(trans_x + (double) i * (width + space), trans_y + (double) j * (height + space), width, height)));
                    }
                }
                break;
            case COLUMNS_FIRST_THEN_ROWS:
                for (int j = 0; j < nb_lines; j++) {
                    for (int i = 0; i < nb_cols; i++) {
                        positions.add(new MyRectangle2D.Double(new Rectangle2D.Double(trans_x + (double) i * (width + space), trans_y + (double) j * (height + space), width, height)));
                    }
                }
                break;
        }
        match(positions);
        updateMasterRect();
    }

    /**
     *
     * @param max
     * @return the top most left/up vectorial object position
     */
    public Point2D.Double getMinPos(int max) {
        Point2D.Double p2d = new Point2D.Double();
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        int counter = 0;
        for (Object object : pos_n_shapes) {
            PARoi obj = ((PARoi) object);
            double X = obj.getBounds2D().getX();
            double Y = obj.getBounds2D().getY();
            min_x = X < min_x ? X : min_x;
            min_y = Y < min_y ? Y : min_y;
            counter++;
            if (counter == max && max != -1) {
                break;
            }
        }
        p2d.x = min_x;
        p2d.y = min_y;
        return p2d;
    }

    /**
     *
     * @param max
     * @return The max width and height of objects
     */
    public Point2D.Double getMaxSize(int max) {
        Point2D.Double p2d = new Point2D.Double();
        double max_width = 0;
        double max_height = 0;
        int counter = 0;
        for (Object object : pos_n_shapes) {
            PARoi obj = ((PARoi) object);
            double width = obj.getBounds2D().getWidth();
            double height = obj.getBounds2D().getHeight();
            max_width = width > max_width ? width : max_width;
            max_height = height > max_height ? height : max_height;
            counter++;
            if (counter == max && max != -1) {
                break;
            }
        }
        p2d.x = max_width;
        p2d.y = max_height;
        return p2d;
    }

    /**
     * adds a shape if the shape is not already present in the group otherwise
     * removes it
     *
     * @param shape
     */
    public void addOrRemoveShape(Object shape) {
        if (!pos_n_shapes.isEmpty()) {
            if (pos_n_shapes.contains(shape)) {
                remove(shape);
            } else {
                addShape(shape);
            }
        } else {
            addShape(shape);
        }
        updateMasterRect();
    }

    /**
     *
     * @return the number of vectorial elements in the group
     */
    public int getSize() {
        if (pos_n_shapes == null) {
            return 0;
        }
        return pos_n_shapes.size();
    }

    /*
     * Creates a bounding rectangle around the group
     */
    private void updateMasterRect() {
        rec2d = null;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        if (pos_n_shapes != null && !pos_n_shapes.isEmpty()) {
            for (Object object : pos_n_shapes) {
                if (object != null) {
                    if (object instanceof PARoi) {
                        found = true;
                        Rectangle2D shp = ((PARoi) object).getBounds2D();
                        double x = shp.getX();
                        double y = shp.getY();
                        double xend = shp.getX() + shp.getWidth();
                        double yend = shp.getY() + shp.getHeight();
                        min_x = x < min_x ? x : min_x;
                        min_y = y < min_y ? y : min_y;
                        max_x = xend > max_x ? xend : max_x;
                        max_y = yend > max_y ? yend : max_y;
                    }
                }
            }
        }
        if (!found) {
            return;
        }
        rec2d = new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    /**
     * Remove a vectorial object from the group
     *
     * @param shape
     */
    public void remove(Object shape) {
        pos_n_shapes.remove(shape);
        updateMasterRect();
    }

    /**
     *
     * @param g2d
     * @param visibleRect
     */
    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        if (visibleRect.intersects(rec2d)) {
            if (pos_n_shapes != null) {
                for (Object object : pos_n_shapes) {
                    if (object instanceof PARoi) {
                        if (((PARoi) object).intersects(visibleRect)) {
                            ((PARoi) object).drawIfVisibleWhenDragged(g2d, visibleRect);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param g2d
     * @param visible_rect
     */
    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visible_rect) {
        if (visible_rect.intersects(rec2d)) {
            drawIfVisible(g2d, true, visible_rect);
        }
    }

    private void drawIfVisible(Graphics2D g2d, boolean draw_shape_only_when_in_visible_area, Rectangle visible_rect) {
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof PARoi) {
                    if (!draw_shape_only_when_in_visible_area) {
                        ((PARoi) object).drawAndFill(g2d);
                        continue;
                    }
                    if (((PARoi) object).intersects(visible_rect)) {
                        ((PARoi) object).drawAndFill(g2d);
                    }
                }
            }
        }
    }

    /**
     *
     * @param g2d
     */
    @Override
    public void drawAndFill(Graphics2D g2d) {
        for (Object object : pos_n_shapes) {
            if (object instanceof PARoi) {
                ((PARoi) object).drawAndFill(g2d);
            }
        }
    }

    /**
     *
     * @return all the vectorial objects contained in the group
     */
    public HashSet<Object> getGroup() {
        return pos_n_shapes;
    }

    @Override
    public void translate(double x, double y) {
        if (x == 0 && y == 0) {
            return;
        }
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof Transformable) {
                    ((Transformable) object).translate(x, y);
                }
            }
        }
        updateMasterRect();
    }

    /**
     * Sets the height of all vectorial objects the height of the smallest of
     * them
     */
    public void setHeightSmall() {
        double min_height = Integer.MAX_VALUE;
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof Transformable) {
                    double cur_height = ((Transformable) object).getShapeHeight();
                    min_height = cur_height < min_height ? cur_height : min_height;
                }
            }
        }
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof Transformable) {
                    ((Transformable) object).setShapeHeight(min_height, true);
                }
            }
        }
        updateMasterRect();
    }

    /**
     * Sets the width of all vectorial objects the width of the smallest of them
     */
    public void setWidthSmall() {
        double min_width = Integer.MAX_VALUE;
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof Transformable) {
                    double cur_width = ((Transformable) object).getShapeWidth();
                    min_width = cur_width < min_width ? cur_width : min_width;
                }
            }
        }
        if (pos_n_shapes != null) {
            for (Object object : pos_n_shapes) {
                if (object instanceof Transformable) {
                    ((Transformable) object).setShapeWidth(min_width, true);
                }
            }
        }
        updateMasterRect();
    }

    @Override
    public void scale(double factor) {
        for (Object object : pos_n_shapes) {
            if (object instanceof Transformable) {
                ((Transformable) object).scale(factor);
            }
        }
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            this.angle = angleInDegrees;
        }
    }

    /**
     *
     * @param g2d
     * @param visRect
     */
    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        if (rec2d == null) {
            return;
        }
        if (rec2d.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setColor(Color.magenta);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(rec2d);
            for (Object object : pos_n_shapes) {
                if (object instanceof Drawable) {
                    ((Drawable) object).drawSelection(g2d, visRect);
                }
            }
            g2dParams.restore(g2d);
        }
    }

    /**
     *
     * @param p
     * @return true if the point p is contained in bounding rectangle
     * surrounding the group
     */
    public boolean contains(Point2D.Double p) {
        if (rec2d == null) {
            return false;
        }
        return rec2d.contains(p);
    }

    /**
     * pack the vectorial objects in the X direction without space between them
     */
    public void packX() {
        packX(0);
    }

    /**
     * pack the vectorial objects in the X direction
     *
     * @param space sapce between vectorial objects
     */
    public void packX(double space) {
        Point2D.Double last_pos = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_pos == null) {
                last_pos = cur_pos;
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            } else {
                /*
                 * we recover the appropriate y pos
                 */
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(last_pos.x + space, first.y);
                t.setFirstCorner(trans);
                last_pos = t.getLastCorner();
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            }
        }
        updateMasterRect();
    }

    /**
     * pack the vectorial objects in the Y direction without space between them
     */
    public void packY() {
        packY(0);
    }

    /**
     * pack the vectorial objects in the X direction
     *
     * @param space sapce between vectorial objects
     */
    public void packY(double space) {
        Point2D.Double last_pos = null;
        if (pos_n_shapes != null && !pos_n_shapes.isEmpty()) {
            for (Object object : pos_n_shapes) {
                Transformable t = (Transformable) object;
                Point2D.Double cur_pos = t.getLastCorner();
                if (last_pos == null) {
                    last_pos = cur_pos;
                    last_pos = new Point2D.Double(last_pos.x, last_pos.y);
                } else {
                    /*
                     * we recover the appropriate y pos
                     */
                    Point2D.Double first = t.getFirstCorner();
                    Point2D.Double trans = new Point2D.Double(first.x, last_pos.y + space);
                    t.setFirstCorner(trans);
                    last_pos = t.getLastCorner();
                    last_pos = new Point2D.Double(last_pos.x, last_pos.y);
                }
            }
        }
        updateMasterRect();
    }

    /**
     * align the centers of all vectorial objects in Y
     */
    public void alignCenterY() {
        Point2D.Double last_center = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getCenter();
            if (last_center == null) {
                last_center = cur_pos;
            } else {
                Point2D.Double first = t.getCenter();
                Point2D.Double trans = new Point2D.Double(first.x, last_center.y);
                t.setCenter(trans);
            }
        }
        updateMasterRect();
    }

    /**
     * align the centers of all vectorial objects in X
     */
    public void alignCenterX() {
        Point2D.Double last_center = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getCenter();
            if (last_center == null) {
                last_center = cur_pos;
            } else {
                Point2D.Double first = t.getCenter();
                Point2D.Double trans = new Point2D.Double(last_center.x, first.y);
                t.setCenter(trans);
            }
        }
        updateMasterRect();
    }

    /**
     * Align vectorial objects to the left
     */
    public void alignLeft() {
        Point2D.Double last_left = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getFirstCorner();
            if (last_left == null) {
                last_left = cur_pos;
            } else {
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(last_left.x, first.y);
                t.setFirstCorner(trans);
            }
        }
        updateMasterRect();
    }

    /**
     * Align vectorial objects to the right
     */
    public void alignRight() {
        Point2D.Double last_right = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_right == null) {
                last_right = cur_pos;
            } else {
                Point2D.Double first = t.getLastCorner();
                Point2D.Double trans = new Point2D.Double(last_right.x, first.y);
                t.setLastCorner(trans);
            }
        }
        updateMasterRect();
    }

    /**
     * Align vectorial objects to the top
     */
    public void alignTop() {
        Point2D.Double first_left = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getFirstCorner();
            if (first_left == null) {
                first_left = cur_pos;
            } else {
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(first.x, first_left.y);
                t.setFirstCorner(trans);
            }
        }
        updateMasterRect();
    }

    /**
     * Align vectorial objects to the bottom
     */
    public void alignBottom() {
        Point2D.Double last_corner = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_corner == null) {
                last_corner = cur_pos;
            } else {
                Point2D.Double first = t.getLastCorner();
                Point2D.Double trans = new Point2D.Double(first.x, last_corner.y);
                t.setLastCorner(trans);
            }
        }
        updateMasterRect();
    }

    /**
     *
     * @return true if two objects' postions could be swapped
     */
    public boolean swap() {
        boolean all_Images = true;
        if (pos_n_shapes.size() == 2) {
            Point2D.Double pos_bckup = null;
            Object first_object = null;
            for (Object object : pos_n_shapes) {
                if (!(object instanceof MyImage2D)) {
                    all_Images = false;
                }
                if (pos_bckup == null) {
                    pos_bckup = ((Transformable) object).getFirstCorner();
                    first_object = object;
                } else {
                    Point2D.Double cur_pos = ((Transformable) object).getFirstCorner();
                    ((Transformable) object).setFirstCorner(pos_bckup);
                    ((Transformable) first_object).setFirstCorner(cur_pos);
                }
            }
            updateMasterRect();
            /*
             * we invert the positions
             */
            if (all_Images) {
                /*
                 * we also swap the letters because it makes sense to do so
                 */
                first_object = null;
                ColoredTextPaneSerializable first_letter = null;
                for (Object object : pos_n_shapes) {
                    if (first_letter == null) {
                        first_letter = ((MyImage2D.Double) object).getLetter();
                        first_object = object;
                    } else {
                        ColoredTextPaneSerializable letter2 = ((MyImage2D.Double) object).getLetter();
                        ((MyImage2D.Double) object).setLetter(new ColoredTextPane(first_letter));
                        ((MyImage2D.Double) first_object).setLetter(new ColoredTextPane(letter2));
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void setFirstCorner(Point2D.Double pos) {
        Rectangle2D bounding = getBounds2D();
        Point2D.Double trans = new Point2D.Double(bounding.getX() - pos.x, bounding.getY() - pos.y);
        for (Object object : pos_n_shapes) {
            if (object instanceof Transformable) {
                ((Transformable) object).translate(-trans.x, -trans.y);
            }
        }
    }

    @Override
    public Point2D.Double getFirstCorner() {
        Point2D.Double first_corner = new Point2D.Double(rec2d.x, rec2d.y);
        return first_corner;
    }

    @Override
    public Point2D.Double getLastCorner() {
        Point2D.Double last_corner = new Point2D.Double(rec2d.x + rec2d.width, rec2d.y + rec2d.height);
        return last_corner;
    }

    @Override
    public Point2D.Double getCenter() {
        Point2D.Double center = new Point2D.Double(rec2d.x + rec2d.width / 2., rec2d.y + rec2d.height / 2.);
        return center;
    }

    @Override
    public String toString() {
        return "Group";
    }

//    @Override
//    public void setColor(int color) {
//    }
    @Override
    public void flipHorizontally() {
        for (Object object : pos_n_shapes) {
            ((Transformable) object).flipHorizontally();
        }
    }

    @Override
    public void flipVertically() {
        for (Object object : pos_n_shapes) {
            ((Transformable) object).flipHorizontally();
        }
    }

    /**
     *
     * @param nb_erosions
     */
    @Override
    public void erode(int nb_erosions) {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).erode(nb_erosions);
        }
        updateMasterRect();

    }

    /**
     *
     */
    @Override
    public void erode() {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).erode();
        }
        updateMasterRect();
    }

    /**
     *
     * @param nb_dilatations
     */
    @Override
    public void dilate(int nb_dilatations) {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).dilate(nb_dilatations);
        }
        updateMasterRect();
    }

    /**
     *
     */
    @Override
    public void dilate() {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).dilate();
        }
        updateMasterRect();
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
//        long start_time = System.currentTimeMillis();
//        ArrayList<Object> shapes = new ArrayList<Object>();
//        MyLine2D.Double l2d = new MyLine2D.Double(10, 10, 40, 40);
//        l2d.setColor(0xFF0000);
//        shapes.add(l2d);
////        MyEllipse2D.Double el2d = new MyEllipse2D.Double(10, 10, 40, 40);
//        //Rectangle2D.Double r =  ((Shape)el2d).getBounds()
//        l2d.setColor(0x00FF00);
//        shapes.add(el2d);
//        MyRectangle2D.Double r2d = new MyRectangle2D.Double(10, 10, 40, 40);
//        r2d.setColor(0x000FFF);
//        shapes.add(r2d);
//        //ca a l'air de marcher
//        //--> pas mal du tout
//        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = test2.createGraphics();
//        ComplexShapeLight test = new ComplexShapeLight(shapes);
//        test.drawAndFill(g2d);
//        g2d.dispose();
//        Saver2.poplong(test2);
////s           test.apply(list); 
//        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
//        System.exit(0);
    }
}

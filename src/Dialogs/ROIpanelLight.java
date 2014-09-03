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
package Dialogs;

import MyShapes.*;
import Commons.CommonClassesLight;
import Commons.SaverLight;
import Tools.ROITools;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.List;

//TODO clean this at some point and ensure I don't have unecessary calls make sure to call all the modifiers also
/**
 * ROIpanelLight is the scientiFig vectorial panel (can be used to drawAndFill,
 * ve, edit, ... vectorial objects)
 *
 * @since Packing Analyzer 5.0
 * @author Benoit Aigouy
 */
public class ROIpanelLight extends javax.swing.JPanel implements MouseListener, MouseMotionListener, KeyListener {
    /*
     * Variables
     */

    public boolean keyStrokeAble = true;
    private boolean allowRefresh = true;
    public boolean selectable = true;
    public boolean draggable = true;
    public boolean finger_mode = true;
    public int finger_radius = 120;
    public MyEllipse2D.Double finger;
    public boolean ROIPanelActive = false;
    public boolean stick_to_pixels = true;
    public int DRAWING_PRIMITIVE = LINE;
    public boolean verbose_mode = false;
    public boolean clicked_on_a_shape = false;
    public Color ROIColor = Color.yellow;
    public Color ROIFillColor = Color.yellow;
    public ArrayList<Object> ROIS;
    public ArrayList<Object> copy = new ArrayList<Object>();
    public double zoom = 1.;
    public Object cur_shape = null;
    public boolean show_base_of_line = true;
    public HashSet<Integer> random_unique_colors;
    public Object selected_shape_or_group;
    public int selected_shape_index;
    public double X_clicked;
    public double Y_clicked;
    public double X_dragged;
    public double Y_dragged;
    public static final int EDIT_MODE = 1;
    public static final int DEFAULT_MODE = 0;
    public BufferedImage backgroundImage;
    public int MODE = DEFAULT_MODE;
    public Object shape_to_edit;
    public boolean dragging = false;
    public ArrayList<Object> sensitive_points;
    public boolean generateNewUniqueColorForEachShape = false;
    public boolean draw_only_mode = false;
    public boolean isDrawingPolyLine = false;
    public int lastDrawColorUsed = 0xFFFF00;
    public int lastFillColorUsed = 0xFFFF00;
    float stroke_size = 0.65f;
    public int dotSize = 1;
    public int dashSize = 6;
    public int skipSize = 6;
    public int LINESTROKE = 0;
    public HashMap<Integer, Object> id_n_shape = null;
    double bracketSize = 12;
    double arrowheadWidth = 15;
    double arrowheadHeight = 30;
    int ARROWHEAD_TYPE = 0;
    int FILLING = 0;
    public String output_name = null;
    private boolean multiClickAllowsForDeeperSelectionOfElements = false;

    /*
     * DRAWING PRIMITIVES
     */
    public static final int RECTANGLE = 0;
    public static final int SQUARE = 1;
    public static final int CIRCLE = 2;
    public static final int ELLIPSE = 3;
    public static final int ARROW = 11;
    public static final int FREEHAND = 4;
    public static final int LINE = 5;
    public static final int BRACKET = 12;
    public static final int POINT = 6;
    public static final int POLYLINE = 7;
    public static final int POLYGON = 8;
    public static final int STRING = 9;
    public static final int IMAGE = 10;
    public static final int NONE = -1;

    /**
     * Empty creator
     */
    public ROIpanelLight() {
        initComponents();
        this.setFocusable(true);
        this.requestFocus();
        repaint();
    }

    /**
     * Constructor
     *
     * @param ROIS Vectorial objects that should be contained and displayed in
     * the ROIPanel
     */
    public ROIpanelLight(ArrayList<Object> ROIS) {
        this();
        this.ROIS = ROIS;
    }

    public boolean isMultiClickAllowsForDeeperSelectionOfElements() {
        return multiClickAllowsForDeeperSelectionOfElements;
    }

    /**
     * If true, clicking several time on the same object allows for the
     * selection of elements within the selected object
     *
     * @param multiClickAllowsForDeeperSelectionOfElements
     */
    public void setMultiClickAllowsForDeeperSelectionOfElements(boolean multiClickAllowsForDeeperSelectionOfElements) {
        this.multiClickAllowsForDeeperSelectionOfElements = multiClickAllowsForDeeperSelectionOfElements;
    }

    /**
     *
     * @return if objects can be selected
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Defines whether vectorial objects can be selected or not
     *
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     *
     * @return whether vectorial objects can be dragged
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Defines whether vectorial objects can be dragged
     *
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     *
     * @return the id and shape of the cell below the mouse (requires cell
     * selection MODE to be active)
     */
    public HashMap<Integer, Object> getId_n_shape() {
        return id_n_shape;
    }

    /**
     * Sets the current cell id and shape
     *
     * @param id_n_shape
     */
    public void setId_n_shape(HashMap<Integer, Object> id_n_shape) {
        this.id_n_shape = id_n_shape;
    }

    /**
     *
     * @return whether additional parameters such as x y coordinates should be
     * displayed close to the selected object
     */
    public boolean isVerbose_mode() {
        return verbose_mode;
    }

    /**
     * Defines whether additional parameters such as x y coordinates should be
     * displayed close to the selected object
     *
     * @param verbose_mode
     */
    public void setVerbose_mode(boolean verbose_mode) {
        this.verbose_mode = verbose_mode;
    }

    /**
     *
     * @return whether the ROIpanel idrawAndFill drawdrawAndFilly MODE (d means
     * that the user is not allowed to move objectsdrawAndFillt it can d ones,
     * pretty useful in an overcrowded drawing area)
     */
    public boolean isDraw_only_mode() {
        return draw_only_mode;
    }

    /**
     * Defines whether the ROIpaneldrawAndFilluld be in drdrawAndFill or not
     * (draw only means that the user is not allowed to movdrawAndFill ut it can
     * draw new ones, pretty useful in an overcrowded drawing area)
     *
     * @param draw_only_mode
     */
    public void setDraw_only_mode(boolean draw_only_mode) {
        this.draw_only_mode = draw_only_mode;
    }

    /**
     *
     * @return whether shapes can be edited
     */
    public boolean isEditMode() {
        return MODE == EDIT_MODE;
    }

    /**
     * resets the edit MODE
     */
    public void resetEditMode() {
        shape_to_edit = null;
        sensitive_points = null;
        dragging = false;
    }

    /**
     * Defines if the ROIPanel should allow for shape edition
     *
     * @param editMode
     */
    public void setEditMode(int editMode) {
        this.MODE = editMode;
        finger_mode = false;
        finger_radius = 0;
        finger = null;
        switch (MODE) {
            case EDIT_MODE:
                if (selected_shape_or_group == null) {
                    CommonClassesLight.Warning(this, "please select a Shape first");
                    setEditMode(DEFAULT_MODE);
                    return;
                }
                if (selected_shape_or_group instanceof ComplexShapeLight) {
                    CommonClassesLight.Warning(this, "You cannot edit a group, please select a single shape");
                    setEditMode(DEFAULT_MODE);
                    return;
                }
                /*
                 * if the shape is a polygon we ask for a possible simplification of the shape
                 */
                if (selected_shape_or_group instanceof MyPolygon2D) {
                    PolygonSimplifierDialog iopane = new PolygonSimplifierDialog();
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Edit Shape", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        ((MyPolygon2D) selected_shape_or_group).setMinimum_distance_between_points(iopane.getDistance());
                        setShapeToEdit(null, selected_shape_or_group);
                        finger_mode = iopane.isFingerModeActive();
                        finger_radius = iopane.getFingerRadius();
                    } else {
                        setEditMode(DEFAULT_MODE);
                    }
                } else {
                    setShapeToEdit(null, selected_shape_or_group);
                }
                break;
            default:
                if (shape_to_edit != null) {
                    setSelectedShape(shape_to_edit);
                }
                shape_to_edit = null;
                resetEditMode();
                break;
        }
        repaint();
    }

    /**
     * Bring selected shape(s) to the front
     */
    public void bringToFront() {
        if (selected_shape_or_group == null) {
            CommonClassesLight.Warning(this, "please select a Shape first");
            return;
        }
        if (selected_shape_or_group instanceof ComplexShapeLight) {
            HashSet<Object> shapes = ((ComplexShapeLight) selected_shape_or_group).getGroup();
            for (Object object : shapes) {
                ROIS.remove(object);
            }
            for (Object object : shapes) {
                ROIS.add(object);
            }
            Collections.rotate(ROIS, -shapes.size());
        } else {
            ROIS.remove(selected_shape_or_group);
            ROIS.add(selected_shape_or_group);
            Collections.rotate(ROIS, -1);
        }
        repaint();
        /**
         * send signal that order of ROIs has changed
         */
        ROIOrderChanged();
    }

    /**
     * Send selected shape(s) to the back
     */
    public void sendToBack() {
        if (selected_shape_or_group == null) {
            CommonClassesLight.Warning(this, "please select a Shape first");
            return;
        }
        if (selected_shape_or_group instanceof ComplexShapeLight) {
            HashSet<Object> shapes = ((ComplexShapeLight) selected_shape_or_group).getGroup();
            for (Object object : shapes) {
                ROIS.remove(object);
            }
            for (Object object : shapes) {
                ROIS.add(object);
            }
            Collections.rotate(ROIS, shapes.size());
        } else {
            ROIS.remove(selected_shape_or_group);
            ROIS.add(selected_shape_or_group);
            Collections.rotate(ROIS, 1); //--> ca ca a marche
        }
        repaint();
        /**
         * send signal that order of ROIs has changed
         */
        ROIOrderChanged();
    }

    /**
     * Defines the shape to edit
     *
     * @param ROIS
     * @param ROItoEdit
     */
    public void setShapeToEdit(ArrayList<Object> ROIS, Object ROItoEdit) {
        initComponents();
        this.sensitive_points = new ArrayList<Object>();
        this.shape_to_edit = ROItoEdit;
        this.sensitive_points.addAll((((Editable) ROItoEdit).getMagicPoints()));
        makeAllNegativeColor();
        repaint();
    }

    /**
     *
     * @return whether the base of lines should be highlighted with a dot
     */
    public boolean isShow_base_of_line() {
        return show_base_of_line;
    }

    /**
     * Defines whether the base of lines should be highlighted with a dot
     *
     * @param show_base_of_line
     */
    public void setShow_base_of_line(boolean show_base_of_line) {
        this.show_base_of_line = show_base_of_line;
        repaint();
    }

    /**
     * Sets the default Drawing Primitive (square, rect, circle, ...)
     *
     * @param DRAWING_PRIMITIVE
     */
    public void setDrawingPrimitive(int DRAWING_PRIMITIVE) {
        this.DRAWING_PRIMITIVE = DRAWING_PRIMITIVE;
        cur_shape = null;
        isDrawingPolyLine = false;
    }

    /**
     * Sets the color of all ROIs to the negative of their initial color
     * (reversible if called again)
     */
    public void makeAllNegativeColor() {
        int color = ((Contourable) shape_to_edit).getDrawColor();
        color = CommonClassesLight.negative_color(color);
        for (Object object : sensitive_points) {
            ((Contourable) object).setDrawColor(color);
        }
    }

    /**
     * Changes the color of the current selection
     *
     * @param new_color
     */
    public void recolorSel(int new_color) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Contourable)) {
            ((Contourable) selected_shape_or_group).setDrawColor(new_color);
            lastDrawColorUsed = new_color;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    public boolean changeSelDrawOpacity(float opacity) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Contourable) && !ROITools.isFloatingText(selected_shape_or_group)) {
            ((Contourable) selected_shape_or_group).setDrawOpacity(opacity);
            repaint();
            return true;
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
        return false;
    }

    public boolean changeSelFillOpacity(float opacity) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Fillable) && !ROITools.isFloatingText(selected_shape_or_group)) {
            ((Fillable) selected_shape_or_group).setFillOpacity(opacity);
            repaint();
            return true;
        } else {
            CommonClassesLight.Warning(this, "please select a fillable shape first");
        }
        return false;
    }

    public void recolorFillSel(int new_color) {
        if (selected_shape_or_group != null) {
            ((Fillable) selected_shape_or_group).setFillColor(new_color);
            lastFillColorUsed = new_color;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    public void setDotSize(int size) {
        if (selected_shape_or_group != null && selected_shape_or_group instanceof LineStrokable) {
            ((LineStrokable) selected_shape_or_group).setDotSize(size);
            dotSize = size;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    public void setDashSize(int size) {
        if (selected_shape_or_group != null && selected_shape_or_group instanceof LineStrokable) {
            ((LineStrokable) selected_shape_or_group).setDashSize(size);
            dashSize = size;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    public void setSkipSize(int size) {
        if (selected_shape_or_group != null && selected_shape_or_group instanceof LineStrokable) {
            ((LineStrokable) selected_shape_or_group).setSkipSize(size);
            skipSize = size;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    public void setLineStroke(int type) {
        if (selected_shape_or_group != null && selected_shape_or_group instanceof LineStrokable) {
            ((LineStrokable) selected_shape_or_group).setLineStrokeType(type);
            LINESTROKE = type;
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape first");
        }
    }

    /**
     *
     * @return the ROIPanel visible rect taking the zoom into account
     */
    public Rectangle getVisibleRectCorrectedForZoom() {
        return rescaleRect(getVisibleRectangle());
    }

    /**
     *
     * @return the ROIPanel visible rect
     */
    public Rectangle getVisibleRectangle() {
        return (this.getVisibleRect());
    }

    /**
     * Adds a shape to the current visible rect
     *
     * @param shape shape to add to the visible rect
     */
    public void addInVisibleRect(Object shape) {
        if (shape instanceof Transformable) {
            Rectangle r = getVisibleRectCorrectedForZoom();
            ((Transformable) shape).setFirstCorner(new Point2D.Double(r.x + 10, r.y + 10));
        }
        add(shape);
    }

    /**
     * Add a new vectorial object
     *
     * @param shape
     */
    public void add(Object shape) {
        if (ROIS == null) {
            ROIS = new ArrayList<Object>();
        }
        ROIS.add(shape);
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        repaint();
    }

    /**
     * Defines the size of the size of small bars in an bracket
     *
     * @param accoladeSize
     */
    public void setBracketSize(double accoladeSize) {
        this.bracketSize = accoladeSize;
    }

    public int getARROWHEAD_TYPE() {
        return ARROWHEAD_TYPE;
    }

    public void setARROWHEAD_TYPE(int ARROWHEAD_TYPE) {
        this.ARROWHEAD_TYPE = ARROWHEAD_TYPE;
    }

    public int getFILLING() {
        return FILLING;
    }

    public void setFILLING(int FILLING) {
        this.FILLING = FILLING;
    }

    /**
     * Sets the size of an arrowhead in an arrow
     *
     * @param arrowheadWidth
     */
    public void setArrowheadWidth(double arrowheadWidth) {
        this.arrowheadWidth = arrowheadWidth;
    }

    public void setArrowheadHeight(double arrowheadHeight) {
        this.arrowheadHeight = arrowheadHeight;
    }

    /**
     * Sets the default ROI color
     *
     * @param color
     */
    public void setROIColor(int color) {
        ROIColor = new Color(color);
        //repaint();
    }

    /**
     * Sets the default ROI color
     *
     * @param color
     */
    public void setROIFillColor(int color) {
        ROIFillColor = new Color(color);
        //repaint();
    }

    /**
     * Dilate a shape (mathematical morphology)/grow a ROI
     *
     * @param nb_of_dilats number of dilatations
     */
    public void dilate(int nb_of_dilats) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Morphable)) {
            ((Morphable) selected_shape_or_group).dilate(nb_of_dilats);
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape or a group first");
        }
    }

    public void setFirstCorner(double centerX, double centerY) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Transformable)) {
            ((Transformable) selected_shape_or_group).setFirstCorner(new Point2D.Double(centerX, centerY));
            ROIPositionChanged();
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape or a group first");
        }
    }

    /**
     * Erode a shape (mathematical morphology)/shrink a ROI
     *
     * @param nb_of_erosions number of erosions
     */
    public void erode(int nb_of_erosions) {
        if (selected_shape_or_group != null && (selected_shape_or_group instanceof Morphable)) {
            ((Morphable) selected_shape_or_group).erode(nb_of_erosions);
            repaint();
        } else {
            CommonClassesLight.Warning(this, "please select a shape or a group first");
        }
    }

    /**
     * Sets the dimensions of the ROI panel
     *
     * @param width
     * @param height
     */
    public void setDimensions(int width, int height) {
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setBounds(0, 0, width, height);
    }

    /**
     * Sets the default ROI color
     *
     * @param color
     */
    public void setROIColor(Color color) {
        ROIColor = color;
        repaint();
    }

    /**
     * Remove all vectorial objects
     */
    public void clearROIs() {
        resetROIS();
        repaint();
    }

    /**
     *
     * @return all vectorial objects
     */
    public ArrayList<Object> getROIS() {
        return ROIS;
    }

    public boolean isEmpty() {
        ArrayList<Object> rois = getROIS();
        return (rois == null || rois.isEmpty());
    }

    Rectangle2D.Double rec2d;

    /**
     * Creates a bounding rect that encompasses all vectorial objects contained
     * in the image
     */
    public void updateMasterRect() {
        rec2d = null;
        double min_x = 0;
        double min_y = 0;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        if (backgroundImage != null) {
            found = true;
            max_x = Math.max(max_x, backgroundImage.getWidth());
            max_y = Math.max(max_y, backgroundImage.getHeight());
        }
        if (ROIS != null) {
            for (Object object : ROIS) {
                if (object != null) {
                    if (object instanceof PARoi) {
                        found = true;
                        Rectangle shp = ((PARoi) object).getBounds();
                        double x = shp.x;
                        double y = shp.y;
                        double xend = shp.x + shp.width;
                        double yend = shp.y + shp.height;
                        min_x = x < min_x ? x : min_x;
                        min_y = y < min_y ? y : min_y;
                        max_x = xend > max_x ? xend : max_x;
                        max_y = yend > max_y ? yend : max_y;
                    }
                }
            }
        }
        max_x += 1;
        max_y += 1;
        if (!found) {
            return;
        }
        rec2d = new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    /**
     * Defines the Zoom that must be applied to the ROI panel
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateMasterRect();
        if (rec2d != null) {
            setDimensions((int) (rec2d.width * zoom), (int) (rec2d.height * zoom));
        }
        repaint();
    }

    /**
     * Set the vectorial objects contained in the ROIPanel
     *
     * @param ROIS
     */
    public void setROIS(ArrayList<Object> ROIS) {
        setEditMode(DEFAULT_MODE);
        resetSelection();
        resetEditMode();
        this.ROIS = ROIS;
        if (generateNewUniqueColorForEachShape) {
            updateROIS();
        }
        repaint();
    }

    /**
     *
     * @param shp
     * @param clone
     */
    public void addROIs(List<Object> shp, boolean clone) {
        if (shp == null) {
            return;
        }
        if (ROIS == null) {
            ROIS = new ArrayList<Object>();
        }
        if (clone) {
            for (Object object : shp) {
                Object cloned = ((PARoi) object).clone();
                ROIS.add(cloned);
            }
        } else {
            ROIS.addAll(shp);
        }
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        repaint();
    }

    /**
     *
     * @param shp
     */
    public void addROIs(List<Object> shp) {
        addROIs(shp, false);
    }

    /**
     *
     * @param shp
     */
    public void addROI(Object shp) {
        if (shp == null) {
            return;
        }
        if (ROIS != null) {
            ROIS.add(shp);
        } else {
            ROIS = new ArrayList<Object>();
            ROIS.add(shp);
        }
        if (generateNewUniqueColorForEachShape) {
            if (shp instanceof Contourable) {
                random_unique_colors.add(((Contourable) shp).getDrawColor());
            }
        }
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        repaint();
    }

    /**
     * Remove current selection
     */
    public void removeROI() {
        /*
         * dirty fix for shape not being deleted
         */
        if (selected_shape_or_group instanceof ComplexShapeLight) {
            ROIS.removeAll(((ComplexShapeLight) selected_shape_or_group).getGroup());
        }
        ROIS.remove(cur_shape);
        ROIS.remove(selected_shape_or_group);
        ROIS.remove(cur_shape);
        ROIS.remove(selected_shape_or_group);
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        selected_shape_or_group = null;
        cur_shape = null;
        if (generateNewUniqueColorForEachShape) {
            if (selected_shape_or_group instanceof Contourable) {
                random_unique_colors.remove(((Contourable) selected_shape_or_group).getDrawColor());
            }
        }
        repaint();
    }

    /**
     * Remove selected dhape
     */
    public void deleteSelectedShape() {
        if (selected_shape_or_group instanceof ComplexShapeLight) {
            ROIS.removeAll(((ComplexShapeLight) selected_shape_or_group).getGroup());
        } else {
            ROIS.remove(selected_shape_or_group);
        }
        if (cur_shape instanceof ComplexShapeLight) {
            ROIS.removeAll(((ComplexShapeLight) selected_shape_or_group).getGroup());
        } else {
            ROIS.remove(cur_shape);
        }
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        selected_shape_or_group = null;
        cur_shape = null;
        repaint();
    }

    /**
     * remove ROI
     *
     * @param shp
     */
    public void removeROI(Object shp) {
        ROIS.remove(shp);
        /**
         * send signal that nb of ROI has changed
         */
        ROINumberChanged();
        if (generateNewUniqueColorForEachShape) {
            if (shp instanceof Contourable) {
                random_unique_colors.remove(((Contourable) shp).getDrawColor());
            }
        }
    }

    /**
     *
     * @return whether the ROIPanel is active
     */
    public boolean isROIPanelActive() {
        return ROIPanelActive;
    }

    /**
     * Acivates/inactivates the ROIPanel
     *
     * @param ROIPanelActive
     */
    public void setROIPanelActive(boolean ROIPanelActive) {
        this.ROIPanelActive = ROIPanelActive;
        this.setVisible(ROIPanelActive);
        addListeners(true, false);
    }

    /**
     * Make the ROIPanel sensitive to mouse/keyboard inputs
     *
     * @param mouse if true adds a mouselistener
     * @param keyboard if true adds a keyboard listener
     */
    public void addListeners(boolean mouse, boolean keyboard) {
        if (mouse || keyboard) {
            if (mouse) {
                addMouseLiseteners(mouse);
            }
            if (keyboard) {
                addKeyLisetener(keyboard);
            }
        } else {
            releaseMouseListeners();
        }
    }

    /**
     *
     * @return the current selection
     */
    public Object getSelectedShape() {
        return selected_shape_or_group;
    }

    /**
     * Sets the seletion to the vectorial object (shape)
     *
     * @param shape
     */
    public void setSelectedShape(Object shape) {
        selected_shape_or_group = shape;
        repaint();
    }

    public boolean isAllowRefresh() {
        return allowRefresh;
    }

    public void setAllowRefresh(boolean allowRefresh) {
        this.allowRefresh = allowRefresh;
    }

    @Override
    public void paint(Graphics g) {
        if (!allowRefresh) {
            return;
        }
        super.paint(g);
//        AffineTransform at = new AffineTransform();
//        at.setToTranslation(25, 10);
//        ((Graphics2D)g).setTransform(at);
        if (backgroundImage != null) {
            ((Graphics2D) g).drawImage(backgroundImage, 0, 0, (int) ((double) backgroundImage.getWidth() * zoom), (int) ((double) backgroundImage.getHeight() * zoom), null);
        }
        switch (MODE) {
            case EDIT_MODE:
                draw_edit_shape(g);
                break;
            default:
                draw_normal_mode(g);
                break;
        }
    }

    /**
     * Set the default background image
     *
     * @param backgroundImage
     */
    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Allow several shapes to be edited
     *
     * @param shape_to_draw
     * @param g2d
     * @param visibleRect
     */
    public void drawShapeEditMode(Object shape_to_draw, Graphics2D g2d, Rectangle visibleRect) {
        if (shape_to_draw != null) {
            ((PARoi) shape_to_draw).draw(g2d);
        }
    }

    public void fillShapeEditMode(Object shape_to_draw, Graphics2D g2d, Rectangle visibleRect) {
        if (shape_to_draw != null) {
            ((PARoi) shape_to_draw).fill(g2d);
        }
    }

    /**
     *
     * @param r
     * @return the size of the visible rectangle corrected for the zoom
     */
    public Rectangle rescaleRect(Rectangle r) {
        r = new Rectangle((int) (r.x / zoom), (int) (r.y / zoom), (int) (r.width / zoom), (int) (r.height / zoom));
        return r;
    }

    public Graphics2D getG2D() {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        if (zoom != 1.) {
            AffineTransform trans = new AffineTransform();
            trans.scale(zoom, zoom);
            g2d.setTransform(trans);
        }
        return g2d;
    }

    /**
     *
     * @param g
     */
    public void draw_normal_mode(Graphics g) {
        if (ROIPanelActive) {
            if ((ROIS != null && !ROIS.isEmpty()) || cur_shape != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(ROIColor);
                Rectangle r = this.getVisibleRect();
                r = rescaleRect(r);
                g2d.setStroke(new BasicStroke((float) (stroke_size / zoom)));
                AffineTransform bckup = null;
                if (zoom != 1.) {
                    bckup = g2d.getTransform();
                    AffineTransform trans = new AffineTransform(bckup);
                    trans.scale(zoom, zoom);
                    g2d.setTransform(trans);
                }
                if (ROIS != null && !ROIS.isEmpty()) {
                    for (Object cur_shape2 : ROIS) {
                        drawShape(cur_shape2, g2d, r);
                    }
                }
                if (cur_shape != null) {
                    drawShape(cur_shape, g2d, r);
                }
                if (selected_shape_or_group != null) {
                    if (selected_shape_or_group instanceof Drawable) {
                        ((Drawable) selected_shape_or_group).drawSelection(g2d, r);
                    } else if (selected_shape_or_group instanceof ArrayList) {
                        /*
                         * in case the object is just an array of rows
                         */
                        ArrayList<Row> rows = ((ArrayList<Row>) selected_shape_or_group);
                        for (Row row : rows) {
                            ((Drawable) row).drawSelection(g2d, r);
                        }
                    }
                    if (verbose_mode) {
                        ((Drawable) selected_shape_or_group).drawShapeInfo(g2d, Drawable.LEFT);
                    }
                }
                if (zoom != 1.) {
                    if (bckup != null) {
                        g2d.setTransform(bckup);
                    }
                }
            }
        }
    }

    /**
     *
     * @return the default drawing stroke size
     */
    public float getStroke_size() {
        return stroke_size;
    }

    /**
     * Sets the default drawing stroke size
     *
     * @param stroke_size
     */
    public void setStroke_size(float stroke_size) {
        this.stroke_size = stroke_size;
    }

    /**
     *
     * @param g
     */
    public void draw_edit_shape(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle r = this.getVisibleRect();
        g2d.setStroke(new BasicStroke((float) (stroke_size / zoom)));
        AffineTransform bckup = null;
        if (zoom != 1.) {
            bckup = g2d.getTransform();
            AffineTransform trans = new AffineTransform(bckup);
            trans.scale(zoom, zoom);
            g2d.setTransform(trans);
        }
        drawShapeEditMode(shape_to_edit, g2d, r);
        if (!dragging) {
            if (sensitive_points != null) {
                for (Object object : sensitive_points) {
                    drawShapeEditMode(object, g2d, r);
                }
            }
        }
        if (finger_mode) {
            drawShape(finger, g2d, r);
        }
        if (zoom != 1.) {
            g2d.setTransform(bckup);
        }
    }

    /**
     * Select all vectorial objects contained
     */
    public void selectAll() {
        selected_shape_or_group = new ComplexShapeLight(ROIS);
        clicked_on_a_shape = true;
    }

    /*
     * draws a shape
     */
    public void drawShape(Object shape_to_draw, Graphics2D g2d, Rectangle visibleRect) {
        if (shape_to_draw instanceof Drawable) {
            if (dragging) {
                ((Drawable) shape_to_draw).drawIfVisibleWhenDragged(g2d, visibleRect);
            } else {
                ((Drawable) shape_to_draw).drawIfVisible(g2d, visibleRect);
            }
        }
        //mon dieu que c'est crade --> essayer de recoder ca en fait
        if (shape_to_draw instanceof MyLine2D.Double) {
            if (show_base_of_line) {
                Line2D.Double ld = (Line2D.Double) ((PARoi) shape_to_draw).getParentInstance();
                Color bckup_col = g2d.getColor();
                g2d.setColor(Color.RED);
                g2d.draw(new Ellipse2D.Double(ld.x1 - 3, ld.y1 - 3, 6, 6));
                g2d.setColor(bckup_col);
            } else {
                ((MyLine2D.Double) shape_to_draw).drawIfVisible(g2d, visibleRect);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /*
     * deals with mouse click events
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!selectable) {
            return;
        }
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            if (cur_shape instanceof MyPolyline2D) {
                if (DRAWING_PRIMITIVE == POLYGON) {
                    cur_shape = ((MyPolyline2D) cur_shape).finaliseAndConvertToPolygon2D();
                } else {
                    ((MyPolyline2D) cur_shape).finalise_line();
                }
                isDrawingPolyLine = false;
            }
            isDrawingPolyLine = false;
            dragging = false;
            mouseReleased(e);
        }
    }

    /*
     * deals with mouse press events
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!selectable) {
            return;
        }
        if (!isDrawingPolyLine) {
            cur_shape = null;
        }
        this.requestFocus();
        X_clicked = (double) e.getX() / zoom;
        Y_clicked = (double) e.getY() / zoom;
        if (stick_to_pixels) {
            double xrint = (int) (X_clicked + 0.5d);
            double xceil = (int) (X_clicked);
            double yrint = (int) (Y_clicked + 0.5d);
            double yceil = (int) (Y_clicked);
            X_clicked = (xrint + xceil) / 2.;
            Y_clicked = (yrint + yceil) / 2.;
            if (X_clicked == Math.rint(X_clicked)) {
                X_clicked += 0.5d;
            }
            if (Y_clicked == Math.rint(Y_clicked)) {
                Y_clicked += 0.5d;
            }
        }
        Point2D.Double click_point_corrected = new Point2D.Double(X_clicked, Y_clicked);
        if (draw_only_mode && !(MODE == EDIT_MODE)) {
            clicked_on_a_shape = false;
            switch (DRAWING_PRIMITIVE) {
                case POINT:
                    cur_shape = new MyPoint2D.Double(X_clicked, Y_clicked);
                    setShapeColor(cur_shape, ROIColor);
                    break;
            }
            if (!isDrawingPolyLine) {
                switch (DRAWING_PRIMITIVE) {
                    case POLYGON:
                    case POLYLINE:
                        if (cur_shape == null) {
                            cur_shape = new MyPolyline2D.Double(X_clicked, Y_clicked);
                            setShapeColor(cur_shape, ROIColor);
                            isDrawingPolyLine = true;
                            dragging = false;
                        }
                        break;
                }
            } else {
                if (!(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)) {
                    ((MyPolyline2D.Double) cur_shape).addPoint(new Point2D.Double(X_clicked, Y_clicked));// = new MyPolyline2D.Double(X_clicked, Y_clicked);
                    setShapeColor(cur_shape, ROIColor);
                }
            }
            repaint();
            return;
        }
        if (MODE == EDIT_MODE) {
            mousePressedEditMode(click_point_corrected);
        } else {
            if (!isDrawingPolyLine) {
                mousePressedNormalMode(e, click_point_corrected);
            } else {
                if (!(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)) {
                    ((MyPolyline2D.Double) cur_shape).addPoint(new Point2D.Double(X_clicked, Y_clicked));// = new MyPolyline2D.Double(X_clicked, Y_clicked);
                    setShapeColor(cur_shape, ROIColor);
                }
            }
        }
    }

    /**
     *
     * @param click_point_corrected
     */
    public void mousePressedEditMode(Point2D.Double click_point_corrected) {
        if (sensitive_points != null && !sensitive_points.isEmpty()) {
            if (finger_mode) {
                finger = new MyEllipse2D.Double(new Ellipse2D.Double(click_point_corrected.x - finger_radius, click_point_corrected.y - finger_radius, 2. * finger_radius, 2. * finger_radius));
            }

            ArrayList<Object> points_below = new ArrayList<Object>();
            for (int i = sensitive_points.size() - 1; i >= 0; i--) {
                Object shp = sensitive_points.get(i);
                if (!finger_mode) {
                    if (shp instanceof PARoi) {
                        if (((PARoi) shp).contains(click_point_corrected)) {
                            clicked_on_a_shape = true;
                            selected_shape_or_group = shp;
                            break;
                        }
                    }
                } else {
                    if (finger.contains(((MyPoint2D.Double) shp).getPoint())) {
                        clicked_on_a_shape = true;
                        points_below.add(shp);
                    }
                }
            }
            if (finger_mode) {
                if (!points_below.isEmpty()) {
                    selected_shape_or_group = new ComplexShapeLight(points_below);
                } else {
                    selected_shape_or_group = null;
                    clicked_on_a_shape = false;
                }
            }
        }
        repaint();
    }
    public Object bckUpOfElementHigherInTheHierarchy = null;

    /**
     *
     * @param e
     * @param click_point_corrected
     */
    public void mousePressedNormalMode(MouseEvent e, Point2D.Double click_point_corrected) {
        clicked_on_a_shape = false;

        if (ROIS != null && !ROIS.isEmpty()) {
            loop1:
            for (int i = ROIS.size() - 1; i >= 0; i--) {
                Object shp = ROIS.get(i);
                if (shp instanceof PARoi) {
                    if (((PARoi) shp).contains(click_point_corrected)) {
                        clicked_on_a_shape = true;
                    }
                }
                if (clicked_on_a_shape) {
                    /*
                     * there is already a selection
                     */
                    if (selected_shape_or_group instanceof ComplexShapeLight) {
                        /*
                         * --> 24 --> alt et 20 --> mac command
                         */
                        if (e.getModifiers() == 18 || e.getModifiers() == 20 || e.getModifiers() == 24) {
                            ((ComplexShapeLight) selected_shape_or_group).addShape(shp);
                        } else {
                            selected_shape_or_group = shp;
                        }
                    } else {
                        /*
                         * there was no previous selection
                         */
                        if (e.getModifiers() == 18 || e.getModifiers() == 20 || e.getModifiers() == 24) {
                            if (selected_shape_or_group != null) {
                                selected_shape_or_group = new ComplexShapeLight(selected_shape_or_group);
                                ((ComplexShapeLight) selected_shape_or_group).addShape(shp);
                            } else {
                                selected_shape_or_group = new ComplexShapeLight(shp);
                            }
                        } else {
                            Object parentBckup = null;
                            /**
                             * allows to select sub elements from a group
                             */
                            if (multiClickAllowsForDeeperSelectionOfElements) {
                                //modification for review
                                /*
                                 * here I can handle double click to select lower order
                                 */
                                boolean goToLowerOrder = false;
                                boolean ignore = false;

                                parentBckup = shp;

                                if (selected_shape_or_group == shp) {
                                    goToLowerOrder = true;
                                } else {
                                    /**
                                     * fix for multiple selection error
                                     * (Arraylist is wrongly assigned to
                                     * selected_shape_or_group) --> check why in
                                     * the code when I have time
                                     */
                                    if (selected_shape_or_group != null && shp != null && !(selected_shape_or_group instanceof ArrayList) && ((PARoi) shp).intersects(((PARoi) selected_shape_or_group).getBounds())) {
                                        if (((PARoi) selected_shape_or_group).contains(click_point_corrected)) {//|| ((PARoi) shp).contains(click_point_corrected)
                                            /**
                                             * we should nevertheless check that
                                             * the mouse pointer is in the
                                             * current shape otherwise the
                                             * selection must be changed because
                                             * the user has clicked in another
                                             * region of the row
                                             */
                                            shp = selected_shape_or_group;
                                            goToLowerOrder = true;
                                        }
                                        /*
                                         * allow multiple selection within one element
                                         */
                                        if (!goToLowerOrder && ((bckUpOfElementHigherInTheHierarchy != null && ((PARoi) bckUpOfElementHigherInTheHierarchy).contains(click_point_corrected)))) {
                                            goToLowerOrder = true;
                                            shp = bckUpOfElementHigherInTheHierarchy;
                                        }

                                    }
//                                    System.out.println(bckUpOfElementHigherInTheHierarchy == selected_shape_or_group);
//                                     if (!goToLowerOrder && bckUpOfElementHigherInTheHierarchy == selected_shape_or_group)
//                                     {
//                                         System.out.println("bob");
////                                         ignore = true;
//                                               goToLowerOrder = true;
//                                               bckUpOfElementHigherInTheHierarchy = selected_shape_or_group;
//                                     }
                                }
                                /**
                                 * the current shape is already the selected
                                 * shape --> therefore we are going to try to
                                 * select the sub element of the shape TODO at
                                 * some point make it more generic because at
                                 * the moment it is optimized for SF and not
                                 * optimal for PA TODO 2 allow recursive
                                 * selection of lower elements TODO 3 maybe only
                                 * do this when the person double click on it
                                 */
                                /**
                                 * it works just the way I wanted but it's pure
                                 * chance ;-P
                                 */
                                if (multiClickAllowsForDeeperSelectionOfElements && !ignore && (bckUpOfElementHigherInTheHierarchy == null || ((PARoi) shp).contains(click_point_corrected))) {//!((PARoi) bckUpOfElementHigherInTheHierarchy).contains(click_point_corrected))
                                    bckUpOfElementHigherInTheHierarchy = shp;
                                }
                                if (goToLowerOrder) {
                                    /**
                                     * we list the content of the selected shape
                                     * and find a match for one of the lower
                                     * components otherwise return the
                                     */
//                                    else 
                                    if (shp instanceof Row) {
                                        ArrayList<Object> blocks = ((Row) shp).blocks;
                                        for (Object object : blocks) {
                                            if (object instanceof Montage) {
                                                if (((Montage) object).contains(click_point_corrected)) {
                                                    shp = object;
                                                    break;
                                                }
                                            }
                                        }
                                    } else if (shp instanceof Montage) {
                                        LinkedHashSet<Object> images = ((Montage) shp).pos_n_shapes;
                                        for (Object object : images) {
                                            if (object instanceof MyImage2D) {
                                                if (((MyImage2D) object).contains(click_point_corrected)) {
                                                    shp = object;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            selected_shape_or_group = null;
                            selected_shape_or_group = shp;
                            if (bckUpOfElementHigherInTheHierarchy == selected_shape_or_group && selected_shape_or_group == shp) {
                                selected_shape_or_group = parentBckup;
                            }
                        }
                    }
                    selected_shape_index = i;
                    cur_shape = null;
                    break loop1;
                }
            }
        }

        /*
         * selection changed
         * we send it to the listener
         * 
         */
        if (clicked_on_a_shape && selected_shape_or_group != null) {
            selectionChanged(selected_shape_or_group);
        }

        if (selected_shape_or_group instanceof ComplexShapeLight) {
            if (((ComplexShapeLight) selected_shape_or_group).contains(click_point_corrected)) {
                clicked_on_a_shape = true;
            }
        }

        if (!clicked_on_a_shape) {
            selected_shape_or_group = null;
            bckUpOfElementHigherInTheHierarchy = null;
            cur_shape = null;
            if (generateNewUniqueColorForEachShape && cur_shape == null) {
                int color = CommonClassesLight.new_unique_random_color(random_unique_colors);
                random_unique_colors.add(color);
                ROIColor = new Color(color);
            }
            switch (DRAWING_PRIMITIVE) {
//                case ASTERISK:
//                    cur_shape = new MyPoint2D.Double(X_clicked, Y_clicked).setZpos(Zpos);
//                    ((MyPoint2D.Double) cur_shape).setIsAsterisk(true);
//                    setShapeColor(cur_shape, ROIColor);
//                    break;
                case STRING:
                    /*
                     * we pop a window to get the text --> easy peasy
                     */
                    cur_shape = new MyPoint2D.Double(X_clicked, Y_clicked);
//                    ((MyPoint2D.Double) cur_shape).setIsAsterisk(true);
                    ColoredTextPane iopane = new ColoredTextPane();
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Please enter your text here", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        if (iopane.ctps.hasText()) {
                            ((MyPoint2D.Double) cur_shape).setText(iopane.ctps);
                        } else {
                            cur_shape = null;
                            return;
                        }
                    } else {
//                        ((MyPoint2D.Double) cur_shape).setIsAsterisk(true);
                        return;
                    }
                    /*
                     * dirty bug fix for STRINGs not being saved for some reason asterisk are saved upon mouserelease whereas points and asterisks are --> why ??? --> really need to clean the point at some point
                     * it could be because of the loss of focus due to the opening of a jdialog --> mouse release is never called --> could force call it
                     *                     if (cur_shape != null) {
                     addROI(cur_shape);
                     }
                     */
                    mouseReleased(e);
                    break;
                case POINT:
                    cur_shape = new MyPoint2D.Double(X_clicked, Y_clicked);
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case POLYGON:
                case POLYLINE:
                    if (cur_shape == null) {
                        cur_shape = new MyPolyline2D.Double(X_clicked, Y_clicked);
                        setShapeColor(cur_shape, ROIColor);
                        isDrawingPolyLine = true;
                        dragging = false;
                    }
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!selectable) {
            return;
        }
        dragging = false;
        clicked_on_a_shape = false;

        if (MODE == EDIT_MODE) {
            mouseReleasedEditMode();
        } else {
            if (!isDrawingPolyLine) {
                mouseReleasedNormalMode();
            }
        }
        ROIPositionChanged();
        repaint();
    }

    /**
     * deal with pouse release events in normal MODE
     */
    public void mouseReleasedNormalMode() {
        if (cur_shape != null) {
            if (cur_shape instanceof PARoi) {
                ((PARoi) cur_shape).setStrokeSize(stroke_size);
                /*
                 * bug fix for selection not updating
                 */
//                selectionChanged(cur_shape);
            }
            addROI(cur_shape);
            selectionChanged(cur_shape);
            selected_shape_or_group = cur_shape;
            if (!isDrawingPolyLine) {
                cur_shape = null;
            }
            selected_shape_index = ROIS.size() - 1;
        }
    }

    /**
     *
     */
    public void mouseReleasedEditMode() {
        dragging = false;
        ((Editable) shape_to_edit).setMagicPoints(sensitive_points);
        sensitive_points = (((Editable) shape_to_edit).getMagicPoints());
        makeAllNegativeColor();
        selectionChanged(shape_to_edit);
    }

    /**
     *
     * @return true if a new color is applied to each newly generated vectorial
     * object
     */
    public boolean isGenerateNewUniqueColorForEachShape() {
        return generateNewUniqueColorForEachShape;
    }

    /**
     *
     */
    public void updateROIS() {
        if (random_unique_colors != null) {
            random_unique_colors.clear();
            selected_shape_or_group = null;
        }
        random_unique_colors = new HashSet<Integer>(CommonClassesLight.forbiddenColors());
        for (Object shape : ROIS) {
            if (shape instanceof Contourable) {
                random_unique_colors.add(((Contourable) shape).getDrawColor());
            }
        }
    }

    /**
     *
     * @param generateNewUniqueColorForEachShape
     */
    public void setGenerateNewUniqueColorForEachShape(boolean generateNewUniqueColorForEachShape) {
        this.generateNewUniqueColorForEachShape = generateNewUniqueColorForEachShape;
        if (generateNewUniqueColorForEachShape) {
            updateROIS();
        } else {
            random_unique_colors = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        /**
         * force this component to steal the focus
         */
        this.requestFocusInWindow();
        this.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        dragging = false;
    }

    /**
     * add listeners (allow it to respond to mouse and keyboard events) to the
     * ROI panel
     */
    public void addMouseLiseteners(boolean mouse) {
        releaseMouseListeners();
        if (mouse) {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

    }

    public void addKeyLisetener(boolean keyboard) {
        releaseKeyListeners();
        if (keyboard) {
            this.addKeyListener(this);
        }
    }

    /**
     * remove all listeners
     */
    public void releaseMouseListeners() {
        MouseListener[] l = this.getMouseListeners();
        MouseMotionListener[] l2 = this.getMouseMotionListeners();
        for (MouseListener l1 : l) {
            this.removeMouseListener(l1);
        }
        for (MouseMotionListener l21 : l2) {
            this.removeMouseMotionListener(l21);
        }
    }

    public void releaseKeyListeners() {
        KeyListener[] k3 = this.getKeyListeners();
        for (KeyListener k31 : k3) {
            this.removeKeyListener(k31);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!draggable) {
            return;
        }
        dragging = true;
        X_dragged = (double) e.getX() / zoom;
        Y_dragged = (double) e.getY() / zoom;
        if (stick_to_pixels) {
            double xrint = (int) (X_dragged + 0.5d);
            double xceil = (int) (X_dragged);
            double yrint = (int) (Y_dragged + 0.5d);
            double yceil = (int) (Y_dragged);
            X_dragged = (xrint + xceil) / 2.;
            Y_dragged = (yrint + yceil) / 2.;

            if (X_dragged == Math.rint(X_dragged)) {
                X_dragged += 0.5d;
            }
            if (Y_dragged == Math.rint(Y_dragged)) {
                Y_dragged += 0.5d;
            }
        }
        if (MODE == EDIT_MODE) {
            if (finger_mode) {
                mousePressedEditMode(new Point2D.Double(X_dragged, Y_dragged));
            }
            mouseDraggedEditMode();
        } else {
            mouseDraggedNormalMode();
        }
    }

    /**
     *
     */
    public void mouseDraggedNormalMode() {
        if (selected_shape_or_group != null && clicked_on_a_shape) {
            double trans_x = X_dragged - X_clicked;
            double trans_y = Y_dragged - Y_clicked;

            if (selected_shape_or_group instanceof Transformable) {
                ((Transformable) selected_shape_or_group).translate(trans_x, trans_y);
            }

            X_clicked = X_dragged;
            Y_clicked = Y_dragged;

            if (selected_shape_or_group instanceof ComplexShapeLight) {
            } else {
                ROIS.set(selected_shape_index, selected_shape_or_group);
            }

            repaint();
            return;//nb ce truc est crucial
        }

        if (!clicked_on_a_shape) {
            switch (DRAWING_PRIMITIVE) {
                case RECTANGLE:
                    cur_shape = new MyRectangle2D.Double(Math.min(X_clicked, X_dragged), Math.min(Y_clicked, Y_dragged), Math.abs(X_clicked - X_dragged), Math.abs(Y_dragged - Y_clicked));
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case FREEHAND:
                    if (cur_shape != null) {
                        ((MyPolygon2D.Double) cur_shape).addPoint(new Point2D.Double(X_dragged, Y_dragged));
                    } else {
                        cur_shape = new MyPolygon2D.Double();
                        setShapeColor(cur_shape, ROIColor);
                        ((MyPolygon2D.Double) cur_shape).addPoint(new Point2D.Double(X_dragged, Y_dragged));
                    }
                    break;
                case ELLIPSE:
                    cur_shape = new MyEllipse2D.Double(Math.min(X_clicked, X_dragged), Math.min(Y_clicked, Y_dragged), Math.abs(X_clicked - X_dragged), Math.abs(Y_dragged - Y_clicked));
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case CIRCLE:
                    cur_shape = new MyCircle2D.Double(Math.min(X_clicked, X_dragged), Math.min(Y_clicked, Y_dragged), Math.abs(X_clicked - X_dragged), Math.abs(Y_dragged - Y_clicked));
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case SQUARE:
                    cur_shape = new MySquare2D.Double(Math.min(X_clicked, X_dragged), Math.min(Y_clicked, Y_dragged), Math.abs(X_clicked - X_dragged), Math.abs(Y_dragged - Y_clicked));
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case BRACKET:
                    cur_shape = new MyLine2D.Double(X_clicked, Y_clicked, X_dragged, Y_dragged);
                    ((MyLine2D.Double) cur_shape).setBracket(true);
                    ((MyLine2D.Double) cur_shape).setBraketSize(bracketSize);
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case ARROW:
                    cur_shape = new MyLine2D.Double(X_clicked, Y_clicked, X_dragged, Y_dragged);
                    ((MyLine2D.Double) cur_shape).setIsArrow(true);
                    //((MyLine2D.Double) cur_shape).setBarbEndSize(arrowheadWidth);
                    ((MyLine2D.Double) cur_shape).setArrowHeadWidth(arrowheadWidth);
                    ((MyLine2D.Double) cur_shape).setArrowHeadHeight(arrowheadHeight);
                    ((MyLine2D.Double) cur_shape).setARROW_HEAD_TYPE(ARROWHEAD_TYPE);
                    ((MyLine2D.Double) cur_shape).setFILLING(FILLING);
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case LINE:
                    cur_shape = new MyLine2D.Double(X_clicked, Y_clicked, X_dragged, Y_dragged);
                    setShapeColor(cur_shape, ROIColor);
                    break;
                case POINT:
                    cur_shape = new MyPoint2D.Double(X_dragged, Y_dragged);
                    setShapeColor(cur_shape, ROIColor);
                    break;
            }
            repaint();
        }
    }

    /**
     *
     */
    public void mouseDraggedEditMode() {
        /*
         * here we just update the position of the magic points and the corresponding object shape
         */
        if (selected_shape_or_group != null && clicked_on_a_shape) {
            dragging = true;
            double trans_x = X_dragged - X_clicked;
            double trans_y = Y_dragged - Y_clicked;
            if (selected_shape_or_group instanceof Transformable) {
                ((Transformable) selected_shape_or_group).translate(trans_x, trans_y);
            }

            if (finger_mode) {
                ((Transformable) finger).translate(trans_x, trans_y);
            }

            X_clicked = X_dragged;
            Y_clicked = Y_dragged;
            ((Editable) shape_to_edit).setMagicPoints(sensitive_points);
            repaint();
        }
        /*
         * if no bounds (magic points is selected) we allow for the translation of the whole shape and update the shape magic points
         */
        if (selected_shape_or_group != null && !clicked_on_a_shape) {
            if (((PARoi) shape_to_edit).contains(new Point2D.Double(X_clicked, Y_clicked))) {
                double trans_x = X_dragged - X_clicked;
                double trans_y = Y_dragged - Y_clicked;
                X_clicked = X_dragged;
                Y_clicked = Y_dragged;
                ((Transformable) shape_to_edit).translate(trans_x, trans_y);
                sensitive_points = ((Editable) shape_to_edit).getMagicPoints();
                repaint();
            }
        }
        if (finger_mode) {
            if (selected_shape_or_group == null) {
                X_clicked = X_dragged;
                Y_clicked = Y_dragged;
            }
        }
    }

    /**
     * Set the shape color
     *
     * @param shape
     * @param color
     */
    public void setShapeColor(Object shape, int color) {
        if (shape instanceof Contourable) {
            ((Contourable) shape).setDrawColor(color);
        }
    }

    /**
     * Set the shape color
     *
     * @param shape
     * @param color
     */
    public void setShapeColor(Object shape, Color color) {
        setShapeColor(shape, color.getRGB());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!draggable) {
            return;
        }
        double X = (double) e.getX() / zoom;
        double Y = (double) e.getY() / zoom;

        if (MODE == EDIT_MODE) {
            finger = new MyEllipse2D.Double(X - finger_radius, Y - finger_radius, 2 * finger_radius, 2 * finger_radius);
        }
        if (stick_to_pixels) {
            double xrint = (int) (X + 0.5d);
            double xceil = (int) (X);
            double yrint = (int) (Y + 0.5d);
            double yceil = (int) (Y);
            X = (xrint + xceil) / 2.;
            Y = (yrint + yceil) / 2.;

            if (X == Math.rint(X)) {
                X += 0.5d;
            }
            if (Y == Math.rint(Y)) {
                Y += 0.5d;
            }
        }
        switch (MODE) {
            default:
                switch (DRAWING_PRIMITIVE) {
                    case POLYGON:
                    case POLYLINE:
                        if (cur_shape != null) {
                            ((MyPolyline2D.Double) cur_shape).setLastLine2D(new Point2D.Double(X, Y));
                            dragging = true;
                        }
                        break;
                }
                break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     *
     * @return the default output name
     */
    public String getOutput_name() {
        return output_name;
    }

    /**
     * Sets the default output name
     *
     * @param output_name
     */
    public void setOutput_name(String output_name) {
        this.output_name = output_name;
    }

    /**
     * Copy selection
     */
    public void copy() {
        if (selected_shape_or_group != null) {
            copy.clear();
            if (selected_shape_or_group instanceof ComplexShapeLight) {
                HashSet<Object> Rois = ((ComplexShapeLight) selected_shape_or_group).getGroup();
                for (Object object : Rois) {
                    copy.add(((PARoi) object).clone());
                }
            } else {
                copy.add(((PARoi) selected_shape_or_group).clone());
            }
        } else {
            CommonClassesLight.Warning(this, "please select some shapes first");
        }
    }

    /**
     * paste selection if the ROIPanel does not contain any ROI
     */
    public void pasteIfEmpty() {
        if (ROIS == null || ROIS.isEmpty()) {
            addROIs(copy, true);
            selected_shape_or_group = null;
        }
    }

    /**
     * Paste the selection
     */
    public void paste() {
        addROIs(copy, true);
        selected_shape_or_group = null;
    }

    /*
     * keyboard shortcuts
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                removeROI();
                break;
            case KeyEvent.VK_ENTER:
                if (MODE == EDIT_MODE) {
                    setEditMode(DEFAULT_MODE);
                } else {
                    setEditMode(EDIT_MODE);
                }
                break;
            case KeyEvent.VK_A:
                if (e.getModifiers() == InputEvent.CTRL_MASK || e.getModifiers() == InputEvent.META_MASK) {
                    selectAll();
                }
                break;
            case KeyEvent.VK_R:
                if (e.getModifiers() == InputEvent.CTRL_MASK || e.getModifiers() == InputEvent.META_MASK) {
                    recolorSel(lastDrawColorUsed);
                }
                break;
            /**
             * ctrl + C copies ROIs
             */
            case KeyEvent.VK_C:
                if (e.getModifiers() == InputEvent.CTRL_MASK || e.getModifiers() == InputEvent.META_MASK) {
                    copy();
                }
                break;
            /**
             * ctrl + V pastes ROIs
             */
            case KeyEvent.VK_V:
                if (e.getModifiers() == InputEvent.CTRL_MASK || e.getModifiers() == InputEvent.META_MASK) {
                    paste();
                }
                break;
            case KeyEvent.VK_S:
                if (MODE == EDIT_MODE) {
                    setEditMode(DEFAULT_MODE);
                } else {
                    if (e.getModifiers() == InputEvent.CTRL_MASK || e.getModifiers() == InputEvent.META_MASK) {
                        if (output_name == null) {
                            JFileChooser fc;
                            fc = new JFileChooser(".");
                            fc.setDialogTitle("Save File...");
                            int action = fc.showSaveDialog(CommonClassesLight.getGUIComponent());
                            if (action == JFileChooser.APPROVE_OPTION) {
                                output_name = fc.getSelectedFile().toString();
                            }
                        }
                        if (output_name != null) {
                            save(output_name);
                        }
                    }
                }
                break;
            /*
             * allow objects to be moved using keyboard arrows
             * request from users
             */
            case KeyEvent.VK_LEFT:
                if (selected_shape_or_group != null) {
                    if (selected_shape_or_group instanceof PARoi) {
                        ((PARoi) selected_shape_or_group).translate(-1, 0);
                        repaint();
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (selected_shape_or_group != null) {
                    if (selected_shape_or_group instanceof PARoi) {
                        ((PARoi) selected_shape_or_group).translate(1, 0);
                        repaint();
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (selected_shape_or_group != null) {
                    if (selected_shape_or_group instanceof PARoi) {
                        ((PARoi) selected_shape_or_group).translate(0, -1);
                        repaint();
                    }
                }
                break;
            case KeyEvent.VK_DOWN:
                if (selected_shape_or_group != null) {
                    if (selected_shape_or_group instanceof PARoi) {
                        ((PARoi) selected_shape_or_group).translate(0, 1);
                        repaint();
                    }
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * releases the selection
     */
    public void resetSelection() {
        selected_shape_or_group = null;
        cur_shape = null;
        shape_to_edit = null;
        selected_shape_index = -1;
        dragging = false;
        clicked_on_a_shape = false;
    }

    /**
     * removes all vectorial objects contained in the ROI panel
     */
    public void resetROIS() {
        resetSelection();
        resetEditMode();
        output_name = null;
        if (ROIS != null) {
            ROIS.clear();
        } else {
            ROIS = new ArrayList<Object>();
        }
        if (random_unique_colors != null) {
            random_unique_colors.clear();
        }
    }

    /**
     * Save ROIs to a file
     *
     * @param filename output file name
     */
    public void save(String filename) {
        if (filename == null) {
            JFileChooser fc;
            fc = new JFileChooser(".");
            fc.setDialogTitle("Save File...");
            int action = fc.showSaveDialog(CommonClassesLight.getGUIComponent());
            if (action == JFileChooser.APPROVE_OPTION) {
                filename = fc.getSelectedFile().toString();
                if (!filename.endsWith(".roi")) {
                    filename += ".roi";
                }
            }
        }
        //si image
        if (filename != null) {
            SaverLight.saveObject(getROIS(), filename);
        }
    }

    /**
     * override this to handle ROI order change
     */
    public void ROIOrderChanged() {
        //System.out.println("ROI order changed");
    }

    /**
     * override this to detect when list have changed
     */
    public void ROINumberChanged() {
        //System.out.println("ROI nb changed");
    }

    /**
     * override this when the position of ROIs changed
     */
    public void ROIPositionChanged() {
        //System.out.println("ROI pos changed");
    }

    /**
     * I just put it so that people can override it and have a selection changed
     * listener in their code
     *
     * @param selection
     */
    public void selectionChanged(Object selection) {
        //System.out.println(e.getSource());
    /*
         * is just there to be overided
         */
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        /*
         * test edit MODE
         */
        if (true) {
//            palPane.setShapeToEdit(null, new MyLine2D.Double(20, 50, 120, 63));
//            palPane.setShapeToEdit(null, new MyRectangle2D.Double(20, 50, 120, 63));
//            palPane.setShapeToEdit(null, new MyEllipse2D.Double(20, 50, 120, 63));
//            float[]x = {10,20,30,40,50,60,20,10};
//            float[]y = {20,20,30,40,50,40,30,20};
//            palPane.setShapeToEdit(null, new MyPolygon2D.Double(x, y, x.length));
            ROIpanelLight palPane = new ROIpanelLight();//pas mal mais forcer l'aspect ratio
            palPane.setEditMode(EDIT_MODE);
//            palPane.setShapeToEdit(null, new MyImage2D.Double(10, 10, CommonClassesLight.getTempImage()));
            //palPane.addListeners(true);
            palPane.setDimensions(512, 512);
            palPane.setPreferredSize(new Dimension(512, 512));
            palPane.setVisible(true);
//            int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{palPane}, "test drawing vectoriel", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
//            return;
            JFrame ff = new JFrame();
            ff.setSize(512, 512);
            ff.setContentPane(palPane);
            ff.setVisible(true);
            return;
        }

        //marche pas 
        if (true) {
            ROIpanelLight palPane = new ROIpanelLight();
            palPane.addListeners(true, true);
            palPane.setDimensions(512, 512);
            palPane.setPreferredSize(new Dimension(512, 512));
            palPane.setVisible(true);
            palPane.addListeners(true, true);
            palPane.setROIPanelActive(true);
//             int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{palPane}, "test drawing vectoriel", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
//            return;
            JFrame ff = new JFrame();
            ff.setSize(512, 512);
            ff.setContentPane(palPane);
            ff.setVisible(true);
            return;
        }

        Line2D.Double l2d = new Line2D.Double(10, 10, 20, 20);
        System.out.println("base: " + l2d.x1 + " " + l2d.y1);
        System.out.println("tip: " + l2d.x2 + " " + l2d.y2);

        //que se passe t'il si on rotate un carre ?
        Rectangle r = new Rectangle(20, 20, 40, 40);
        AffineTransform at = new AffineTransform();
        at.rotate(10, 40, 40);
        //      at.translate(10,10);
//        Shape shp = at.createTransformedShape(r);
////        System.out.println(shp); //--> aura ete convertit en path2D.double
////        System.out.println(shp instanceof Rectangle);
////        r= (Rectangle)shp;//peut pas etre recaste apres une transformation --> pas cool
////        System.out.println(r);
//        System.out.println(shp);

//        Rectangle2D r2 = new Rectangle2D.Double(10, 20, 30, 40);
//        // r2.
//        shp = at.createTransformedShape(r2);
//        System.out.println(shp); //--> un path2D
        //System.out.println(at.); //en fait je pourrais tjrs garder l'inverse transfo pr ne jamais me perdre
        //on connait  le x1 et le x2, etc --> tres facile et bcp plus puissant que mes mergedshapes
        System.exit(0);
    }
}

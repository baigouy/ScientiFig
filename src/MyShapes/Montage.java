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
//TODO if montage contains only one col make a simple packing where the rules are a little bit changed --> allows more flexibility in the software
//--> test extensively before applying
package MyShapes;

import Checks.CheckGraphs;
import Checks.CheckLineArts;
import Checks.CheckSize;
import Dialogs.ColoredTextPane;
import Tools.Converter;
import Tools.MultiThreadExecuter;
import Commons.CommonClassesLight;
import Commons.G2dParameters;
import Commons.ImageColors;
import Commons.Loader;
import Commons.MyBufferedImage;
import Commons.SaverLight;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;

/**
 * The Montage class creates a montage out of a series of images having either
 * the same or different sizes
 *
 * @since <B>Packing Analyzer 8.2</B>
 * @author Benoit Aigouy
 */
public class Montage extends MyRectangle2D implements Transformable, Drawable, Serializable, MagnificationChangeLoggable {

    /**
     * variables are set to true once a check was run on them checks have to be
     * reset everytime something is run on these images that is not a check -->
     * think about how to do that or everytime the text is changed --> think
     * about it later
     */
    public transient boolean wasCheckedForText;
    public transient boolean wasCheckedForStyle;
    public transient boolean wasCheckedForFonts;
    public transient boolean wasCheckedForGraphs;
    public transient boolean wasCheckedForSize;
    public transient boolean wasCheckedForLineArts;

    /**
     * Variables
     */
    public static final long serialVersionUID = -6133352894050338827L;
    /*
     * data for ImageJ macro mode
     */
    String masterTagOpen = "<Montage ";
    String masterTagClose = "</Montage>";
    public static final HashMap<String, String> parameters = new HashMap<String, String>();
    public static final char null_char = '\u0000';
    /*
     * end IJ macro mode
     */
    public static final int ROWS_FIRST_THEN_COLUMNS = 0;
    public static final int COLUMNS_FIRST_THEN_ROWS = 1;
    public int CURRENT_TABLE_ORIENTATION = ROWS_FIRST_THEN_COLUMNS;
    private int montageID = 0;
    private static final int ROTATE_LEFT = 0;
    private static final int ROTATE_RIGHT = 1;
    public int nb_rows = 3;
    public int nb_cols = 2;
    public boolean isMeander;
    public int space_between_images = 3;
    public double width_in_cm;
    public double height_in_cm;
    public double width_in_px;
    public double height_in_px;
    public double resolution = 72;
    public static final int BIGGER_WIDTH_N_HEIGHT = 0;
    public static final int SMALLER_WIDTH_N_HEIGHT = 1;
    public static final int USER_DEFINED_BOX_WIDTH_N_HEIGHT = 2;
    public static final int SET_TO_BIGGER_WIDTH = 0;
    public static final int SET_TO_BIGGER_HEIGHT = 2;
    /**
     * allows for a better layout support of one liners (tables consisting of
     * only one row or col)
     */
    public static final int WISE_SIZE_AND_ALIGNMENT = 1;
    public static int CURRENT_ALIGNMENT = WISE_SIZE_AND_ALIGNMENT;
    public LinkedHashSet< Object> pos_n_shapes;

    static {
        parameters.put("order", "data-order=");
        parameters.put("space", "data-spaceBetweenImages=");
        parameters.put("nbCols", "data-nbCols=");
        parameters.put("nbRows", "data-nbRows=");
        parameters.put("width", "data-widthInPx=");
        /*
         * do I need anything else --> probably not but think a bit about it
         */
    }

    /**
     * Constructs a Montage out of a series of vectorial objects
     *
     * @param shapes vectorial objects belonging to the same montage
     * @param nbRows number of rows in the montage
     * @param nbCols umber of columns in the montage
     * @param meander if true the montage will be created in meander mode
     * @param first_letter the first letter of the figure (letters will be
     * incremented automatically)
     * @param space_between_images space between images of the montage
     */
    public Montage(ArrayList<Object> shapes, int nbRows, int nbCols, boolean meander, String first_letter, int space_between_images) {
        this(shapes, nbRows, nbCols, meander, space_between_images);
        reletter(first_letter);
    }

    /**
     * Constructs a Montage out of a series of vectorial objects
     *
     * @param shapes vectorial objects belonging to the same montage
     * @param nbRows number of rows in the montage
     * @param nbCols number of columns in the montage
     * @param meander if true the montage will be created in meander mode
     * @param space_between_images space between images of the montage
     */
    public Montage(ArrayList<Object> shapes, int nbRows, int nbCols, boolean meander, int space_between_images) {
        this.nb_rows = nbRows;
        this.nb_cols = nbCols;
        this.space_between_images = space_between_images;
        pos_n_shapes = new LinkedHashSet<Object>();
        CURRENT_ALIGNMENT = WISE_SIZE_AND_ALIGNMENT;//setHeightToMaxHeight ? SET_TO_BIGGER_HEIGHT : SET_TO_BIGGER_WIDTH;
        for (Object object : shapes) {
            pos_n_shapes.add(object);
        }
        if (meander) {
            CURRENT_TABLE_ORIENTATION = ROWS_FIRST_THEN_COLUMNS;
            createTable(nb_cols, nb_rows, space_between_images, ROWS_FIRST_THEN_COLUMNS, CURRENT_ALIGNMENT);
        } else {
            CURRENT_TABLE_ORIENTATION = COLUMNS_FIRST_THEN_ROWS;
            createTable(nb_cols, nb_rows, space_between_images, COLUMNS_FIRST_THEN_ROWS, CURRENT_ALIGNMENT);
        }
        updateMasterRect();
    }

    /**
     * Constructs a Montage out of a series of vectorial objects
     *
     * @param shapes
     */
    public Montage(ArrayList<Object> shapes) {
        pos_n_shapes = new LinkedHashSet<Object>(shapes);
        updateMasterRect();
    }

    /**
     * Constructs a Montage out of single vectorial objects (further objects can
     * be added later on)
     *
     * @param shape
     */
    public Montage(Object shape) {
        nb_cols = 1;
        nb_rows = 1;
        pos_n_shapes = new LinkedHashSet<Object>();
        if (shape != null) {
            addShape(shape);
        }
        updateMasterRect();
    }

    /**
     * Constructs a Montage out another one
     *
     * @param myel
     */
    public Montage(Montage myel) {
        LinkedHashSet< Object> shapes = new LinkedHashSet<Object>();
        for (Object object : shapes) {
            shapes.add(((PARoi) object).clone());
        }
        this.space_between_images = myel.space_between_images;
        this.pos_n_shapes = shapes;// myel.pos_n_shapes;
        this.rec2d = myel.rec2d;
        this.color = myel.color;
        this.strokeSize = myel.strokeSize;
        this.opacity = myel.opacity;
        this.nb_cols = myel.nb_cols;
        this.nb_rows = myel.nb_rows;
        updateMasterRect();
    }

    /**
     * Constructor that creates a montage out of macro code
     *
     * @param macro the IJ macro code
     */
    public Montage(String macro) {
        HashMap<String, String> params = reparseMacro(macro);
        parameterDispatcher(params);
        ArrayList<String> images = new ArrayList<String>();
        while (macro.toLowerCase().contains("data-src=")) {
            macro = CommonClassesLight.strCutRightFisrt(macro, "img");
            String current_image = CommonClassesLight.strCutLeftFirst(macro, "/>");
            if (current_image.contains("data-src=")) {
                images.add(current_image);
            }
        }
        pos_n_shapes = new LinkedHashSet<Object>();
        for (String string : images) {
            MyImage2D img;
            if (string.toLowerCase().contains(".svg")) {
                img = new MyImageVector.Double(string);
            } else {
                img = new MyImage2D.Double(string);
            }
            if ((img.bimg != null || (img instanceof MyImageVector && ((MyImageVector) img).document != null))) {
                pos_n_shapes.add(img);
            }
        }
        rec2d = new Rectangle2D.Double(0, 0, 512, 512);
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION);
        if (params.containsKey("width")) {
            try {
                double final_width = java.lang.Double.parseDouble(params.get("width"));
                setToWidth(final_width);
            } catch (Exception e) {
                System.err.println("Invalid montage width at:");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }

    public int size() {
        if (pos_n_shapes == null) {
            return 0;
        }
        return pos_n_shapes.size();
    }

    /**
     *
     * @param nb_tabs number of tabulations that should precede each line in the
     * macro code
     * @return an ImageJ macro that contains all the necessary information
     * required to build a montage
     */
    public String produceMacroCode(int nb_tabs) {
        String macro_code = "";
        for (int i = 0; i < nb_tabs - 1; i++) {
            macro_code += "\t";
        }
        macro_code += masterTagOpen;
        /*
         * now we add the parameters for the contained files
         */
        macro_code += parameters.get("nbCols") + "\"" + nb_cols + "\" ";
        macro_code += parameters.get("nbRows") + "\"" + nb_rows + "\" ";
        if (CURRENT_TABLE_ORIENTATION == ROWS_FIRST_THEN_COLUMNS) {
            macro_code += parameters.get("order") + "\"" + "meander" + "\" ";
        } else {
            macro_code += parameters.get("order") + "\"" + "comb" + "\" ";
        }
        macro_code += parameters.get("space") + "\"" + space_between_images + "\" ";
        macro_code += parameters.get("width") + "\"" + getWidth() + "\" ";
        /*
         * we finalise the tag
         */
        macro_code += ">\n";
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                for (int i = 0; i < nb_tabs; i++) {
                    macro_code += "\t";
                }
                macro_code += ((MyImage2D) object).produceMacroCode();
            }
        }
        for (int i = 0; i < nb_tabs - 1; i++) {
            macro_code += "\t";
        }
        macro_code += masterTagClose + "\n";
        return macro_code;
    }

    /**
     *
     * @param row_pos
     * @param col_pos
     * @return teh bounding for the vectorial object at position (row_pos,
     * col_pos)
     */
    public Rectangle2D getPanelPosition(int row_pos, int col_pos) {
        if (row_pos > nb_rows || col_pos > nb_cols) {
            return null;
        } else {
            ArrayList<Object> positions = new ArrayList<Object>(pos_n_shapes);
            switch (CURRENT_TABLE_ORIENTATION) {
                case ROWS_FIRST_THEN_COLUMNS:
                    for (int i = 0; i < nb_cols; i++) {
                        for (int j = 0; j < nb_rows; j++) {
                            if (i * nb_rows + j < positions.size()) {
                                if (i == col_pos && j == row_pos) {
                                    return ((MyRectangle2D) positions.get(i * nb_rows + j)).getBounds2D();
                                }
                            }
                        }
                    }
                    break;
                case COLUMNS_FIRST_THEN_ROWS:
                    for (int j = 0; j < nb_rows; j++) {
                        for (int i = 0; i < nb_cols; i++) {
                            if (j * nb_cols + i < positions.size()) {
                                if (i == col_pos && j == row_pos) {
                                    return ((MyRectangle2D) positions.get(j * nb_cols + i)).getBounds2D();
                                }
                            }
                        }
                    }
                    break;
            }
            return null;
        }
    }

    /**
     *
     * @return the incompressible width the montage itself (typically space
     * between teh vectorial objects contained in the montage)
     */
    public double getIncompressibleWidth() {
        return space_between_images * (double) (nb_cols - 1.);
    }

    /**
     *
     * @return the incompressible height the montage itself (typically space
     * between teh vectorial objects contained in the montage)
     */
    public double getIncompressibleHeight() {
        return space_between_images * (double) (nb_rows - 1.);
    }

    /**
     * Reads and ImageJ macro and loads all the parameters necessary to build a
     * new Montage
     *
     * @param txt
     * @return a set of parameters
     */
    private HashMap<String, String> reparseMacro(String txt) {
        ArrayList<String> splitters = new ArrayList<String>(parameters.keySet());
        HashMap<String, String> detected_parameters = new HashMap<String, String>();
        for (String string : splitters) {
            if (txt.contains(parameters.get(string))) {
                String cur_parameters = CommonClassesLight.strCutRightFisrt(txt, parameters.get(string));
                if (cur_parameters.contains(" data-")) {
                    cur_parameters = CommonClassesLight.strCutRightFisrt(CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutLeftFirst(cur_parameters, " data-"), "\""), "\"");
                } else {
                    cur_parameters = CommonClassesLight.strCutRightFisrt(CommonClassesLight.strCutLeftLast(cur_parameters, "\""), "\"");
                }
                detected_parameters.put(string, cur_parameters);
            }
        }
        return detected_parameters;
    }

    /**
     * Dispatch macro parameters
     *
     * @param macro_parameters
     */
    private void parameterDispatcher(HashMap<String, String> macro_parameters) {
        rec2d = new Rectangle2D.Double();
        ArrayList<String> keys = new ArrayList<String>(macro_parameters.keySet());
        for (String string : keys) {
            if (string.equals("order")) {
                if (macro_parameters.get(string).toLowerCase().contains("eande")) {
                    CURRENT_TABLE_ORIENTATION = ROWS_FIRST_THEN_COLUMNS;
                } else {
                    CURRENT_TABLE_ORIENTATION = COLUMNS_FIRST_THEN_ROWS;
                }
                continue;
            }
            if (string.equals("space")) {
                try {
                    space_between_images = Integer.parseInt(macro_parameters.get(string));
                } catch (Exception e) {
                }
                continue;
            }
            if (string.equals("nbCols")) {
                try {
                    nb_cols = Integer.parseInt(macro_parameters.get(string));
                } catch (Exception e) {
                }
                continue;
            }
            if (string.equals("nbRows")) {
                try {
                    nb_rows = Integer.parseInt(macro_parameters.get(string));
                } catch (Exception e) {
                }
                continue;
            }
        }
    }

    /**
     *
     * @return the number of rows in the montage
     */
    public int getNb_rows() {
        return nb_rows;
    }

    /**
     * Set the number of rows in the montage
     *
     * @param nb_rows
     */
    public void setNb_rows(int nb_rows) {
        this.nb_rows = nb_rows;
    }

    /**
     *
     * @return the number of columns in the montage
     */
    public int getNb_cols() {
        return nb_cols;
    }

    /**
     * Set the number of columns in the montage
     *
     * @param nb_cols
     */
    public void setNb_cols(int nb_cols) {
        this.nb_cols = nb_cols;
    }

    /**
     *
     * @return the space between vectorial objects contained in the montage
     */
    public int getSpace_between_images() {
        return space_between_images;
    }

    /**
     * Sets the space between vectorial objects contained in the montage
     *
     * @param space_between_images
     */
    public void setSpace_between_images(int space_between_images) {
        this.space_between_images = space_between_images;
    }

    public void setStrokeSize(float strokeSize, float pointSize, boolean isChangePointSize, boolean applyStrokeToGraphs, boolean applyStrokeToSVG, boolean applyStrokeToROIs, boolean isIllustrator) {
        for (Object object : pos_n_shapes) {
            if (!applyStrokeToGraphs && !applyStrokeToROIs && !applyStrokeToSVG && !isChangePointSize) {
                return;
            }
            if (!(applyStrokeToGraphs || isChangePointSize) && object instanceof MyPlotVector) {
                continue;
            } else if ((applyStrokeToGraphs || isChangePointSize) && object instanceof MyPlotVector && CommonClassesLight.isRReady()) {
                ((MyPlotVector) object).setStrokeSize(strokeSize, pointSize, applyStrokeToGraphs, isChangePointSize);
                continue;
            }
            if (!applyStrokeToSVG && object instanceof MyImageVector) {
                continue;
            } else if (applyStrokeToSVG && object instanceof MyImageVector) {
                ((MyImageVector) object).setStrokeSize(strokeSize, applyStrokeToSVG, isIllustrator);
                continue;
            }
            if ((applyStrokeToROIs || applyStrokeToSVG || applyStrokeToGraphs) && (object instanceof MyImage2D)) {
                ((MyImage2D) object).setAssociatedShapeStrokeSize(strokeSize);
            }
        }
    }

    @Override
    public Object clone() {
        return new Montage(this);
    }

    /**
     * Add a new vectorial object to the montage
     *
     * @param shape
     */
    public void addShape2(Object shape) {
        resetChecks();
        /*
         * we force small tables having only one image to have two columns when a shape is added to them
         */
        if (pos_n_shapes.size() == 1) {
            nb_cols = 2;
        }
        pos_n_shapes.add(shape);
        while (nb_cols * nb_rows < pos_n_shapes.size()) {
            nb_rows++;
        }
        ArrayList<ColoredTextPaneSerializable> letters = new ArrayList<ColoredTextPaneSerializable>();
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ColoredTextPaneSerializable letter = ((MyImage2D) object).getLetter();
                letters.add(letter);
            }
            ((MyRectangle2D) object).setFirstCorner(new Point2D.Double());
        }
        int i = 0;
        for (Object object : pos_n_shapes) {
            if (i < letters.size()) {
                if (object instanceof MyImage2D) {
                    ((MyImage2D) object).setLetter(new ColoredTextPane(letters.get(i++)));
                }
            }
        }
    }

    /**
     * force a montage layout update
     */
    public void update() {
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
        updateMasterRect();
    }

    /**
     * Add a new vectorial object to the montage
     *
     * @param shape
     */
    public final void addShape(Object shape) {
        resetChecks();
        pos_n_shapes.add(shape);
        updateMasterRect();
    }

    /**
     * Change the layout of the montage
     *
     * @param nb_rows number of rows
     * @param nb_cols number of columns
     * @param meander if true, the images will be plcaed in meander order
     */
    public void changeMontageLayout(int nb_rows, int nb_cols, boolean meander) {
        this.nb_rows = nb_rows;
        this.nb_cols = nb_cols;
        if (!meander) {
            this.CURRENT_TABLE_ORIENTATION = COLUMNS_FIRST_THEN_ROWS;
        } else {
            this.CURRENT_TABLE_ORIENTATION = ROWS_FIRST_THEN_COLUMNS;
        }
        for (Object object : pos_n_shapes) {
            ((MyRectangle2D) object).setFirstCorner(new Point2D.Double());
        }
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
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
     * Create a montage
     *
     * @param nb_cols number of columns
     * @param nb_lines umber of lines
     * @param space space between images
     * @param TABLE_ORDER omb or meander (order of the images in the montage)
     */
    private void createTable(int nb_cols, int nb_lines, double space, int TABLE_ORDER) {
        createTable(nb_cols, nb_lines, space, TABLE_ORDER, WISE_SIZE_AND_ALIGNMENT);
    }

    /**
     * Create a montage
     *
     * @param nb_cols number of columns
     * @param nb_lines umber of lines
     * @param space space between images
     * @param TABLE_ORDER omb or meander (order of the images in the montage)
     * @param ADJUSTEMENT_MODE adjust size of images to that of the smallest or
     * biggest image
     */
    public final void createTable(int nb_cols, int nb_lines, double space, int TABLE_ORDER, int ADJUSTEMENT_MODE) {
        int size = nb_cols * nb_lines;
        Point2D.Double min_pos = getMinPos(size);
        double width;
        double height;
        boolean unique = pos_n_shapes.size() == 1;
        boolean onlyVector = true;
        boolean Ractive = CommonClassesLight.isRReady();
        for (Object object : pos_n_shapes) {
            if (!(object instanceof MyPlotVector)) {
                onlyVector = false;
                break;
            }
        }
        int OPTIMIZED_ADJUSTMENT;
        if (ADJUSTEMENT_MODE == WISE_SIZE_AND_ALIGNMENT) {
            if (nb_lines == 1) {
                OPTIMIZED_ADJUSTMENT = SET_TO_BIGGER_HEIGHT;
            } else if (nb_cols == 1) {
                OPTIMIZED_ADJUSTMENT = SET_TO_BIGGER_WIDTH;
            } else {
                OPTIMIZED_ADJUSTMENT = SET_TO_BIGGER_HEIGHT;
            }
        } else {
            OPTIMIZED_ADJUSTMENT = SET_TO_BIGGER_HEIGHT;
        }
        switch (OPTIMIZED_ADJUSTMENT) {
            case SET_TO_BIGGER_HEIGHT:
                if (true) {
                    double max_height = Integer.MIN_VALUE;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            max_height = Math.max(((MyRectangle2D) object).getBounds2D().getHeight(), max_height);
                        }
                    }
                    double final_height_of_all_images_in_the_table = max_height;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            ((MyImage2D) object).setShapeHeight(final_height_of_all_images_in_the_table, true);
                        }
                    }
                    double max_width = Integer.MIN_VALUE;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            max_width = Math.max(((MyRectangle2D) object).getBounds2D().getWidth(), max_width);
                        }
                    }
                    /*we force the graphs to occupy the maximum possible size in width and height*/
                    width = max_width;
                    height = max_height;
                    for (Object object : pos_n_shapes) {
                        if ((object instanceof MyPlotVector) && Ractive) {
                            ((MyPlotVector) object).forceWidthAndHeight(max_width, max_height);
                        }
                    }
                }
                break;
            default:
                if (true) {
                    double max_width = Integer.MIN_VALUE;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            max_width = Math.max(((MyRectangle2D) object).getBounds2D().getWidth(), max_width);
                        }
                    }
                    double final_width_of_all_images_in_the_table = max_width;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            ((MyImage2D) object).setShapeWidth(final_width_of_all_images_in_the_table, true);
                        }
                    }
                    double max_height = Integer.MIN_VALUE;
                    for (Object object : pos_n_shapes) {
                        if (!(object instanceof MyPlotVector) || (!Ractive) || unique || onlyVector) {
                            max_height = Math.max(((MyRectangle2D) object).getBounds2D().getHeight(), max_height);
                        }
                    }
                    width = max_width;
                    height = max_height;
                    /*we force the graphs to occupy the maximum possible size in width and height*/
                    for (Object object : pos_n_shapes) {
                        if ((object instanceof MyPlotVector) && Ractive) {
                            ((MyPlotVector) object).forceWidthAndHeight(max_width, max_height);
                        }
                    }
                }
                break;

        }
        int cur_rows = 0;
        int cur_cols = 0;
        /**
         * added support for one liners (improves the layout of one liners)
         */
        if (ADJUSTEMENT_MODE == WISE_SIZE_AND_ALIGNMENT && (nb_cols == 1 || nb_lines == 1)) {
            if (nb_cols == 1) {
                packY(space);
                cur_rows = pos_n_shapes.size() - 1;
            } else {
                packX(space);
                cur_cols = pos_n_shapes.size() - 1;
            }
        } else {
            ArrayList<Object> positions = new ArrayList<Object>(pos_n_shapes);
            switch (TABLE_ORDER) {
                case ROWS_FIRST_THEN_COLUMNS:
                    for (int i = 0; i < nb_cols; i++) {
                        cur_cols = Math.max(i, cur_cols);
                        for (int j = 0; j < nb_lines; j++) {
                            if (i * nb_lines + j < positions.size()) {
                                Object cur = positions.get(i * nb_lines + j);
                                if (cur instanceof Transformable) {
                                    ((Transformable) cur).setCenter(new Point2D.Double(min_pos.x + i * (width + space) + width / 2., min_pos.y + j * (height + space) + height / 2.));
                                    positions.set(i * nb_lines + j, cur);
                                }
                            } else {
                                break;
                            }
                            cur_rows = Math.max(j, cur_rows);
                        }
                    }
                    break;
                case COLUMNS_FIRST_THEN_ROWS:
                    for (int j = 0; j < nb_lines; j++) {
                        cur_rows = Math.max(j, cur_rows);
                        for (int i = 0; i < nb_cols; i++) {
                            if (j * nb_cols + i < positions.size()) {
                                Object cur = positions.get(j * nb_cols + i);
                                if (cur instanceof Transformable) {
                                    ((Transformable) cur).setCenter(new Point2D.Double(min_pos.x + i * (width + space) + width / 2., min_pos.y + j * (height + space) + height / 2.));
                                    positions.set((j * nb_cols + i), cur);
                                }
                            } else {
                                break;
                            }
                            cur_cols = Math.max(i, cur_cols);
                        }
                    }
                    break;
            }
        }
        /*
         * we set the optimal nb of rows and cols
         */
        this.nb_cols = cur_cols + 1;
        this.nb_rows = cur_rows + 1;
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
     * @return the max width and height of objects
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
     * Adds a shape if the shape is not already present in the group otherwise
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

    @Override
    public Rectangle2D getBounds2D() {
        return rec2d;
    }

    private void updateMasterRect() {
        rec2d = null;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
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
        if (!found) {
            return;
        }
        rec2d = new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    /**
     * Remove a vectorial object and update the montage layout accordingly
     *
     * @param shape
     */
    public void removeObjectAndUpdateLetters(Object shape) {
        ArrayList<ColoredTextPaneSerializable> letters = new ArrayList<ColoredTextPaneSerializable>();//les lettres sont la seule chose que l'on ait besoin de changer
        pos_n_shapes.remove(shape);
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ColoredTextPaneSerializable letter = ((MyImage2D) object).getLetter();
                letters.add(letter);
            }
            ((MyRectangle2D) object).setFirstCorner(new Point2D.Double());
        }
        /*
         * we correct the table
         */
        if (pos_n_shapes.size() < nb_cols * nb_rows) {
            while (pos_n_shapes.size() < nb_rows * nb_cols) {
                if (nb_rows * nb_cols > pos_n_shapes.size() && (nb_rows - 1) * nb_cols < pos_n_shapes.size()) {
                    break;
                }
                nb_rows--;
            }
            while (pos_n_shapes.size() < nb_cols) {
                nb_cols--;
            }
        }
        int i = 0;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).setLetter(new ColoredTextPane(letters.get(i++)));
            }
        }
    }

    /**
     * force update the montage layout
     */
    public void updateTable() {
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION);
    }

    /**
     * Remove a vectorial object from the montage
     *
     * @param shape
     */
    public void remove(Object shape) {
        pos_n_shapes.remove(shape);
        updateMasterRect();
    }

    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        if (visibleRect.intersects(rec2d))//mai meme la ne dessiner que ce qui est necessaire --> gain de tps enorme et aussi de ressources
        {
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

    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visible_rect) {
        if (visible_rect.intersects(rec2d))//mai meme la ne dessiner que ce qui est necessaire --> gain de tps enorme et aussi de ressources
        {
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

    @Override
    public void drawAndFill(Graphics2D g2d) {
        for (Object object : pos_n_shapes) {
            ((Drawable) object).drawAndFill(g2d);
        }
    }

    //modification for review 
    @Override
    public String logMagnificationChanges(double zoom) {
        String finalLog = "";
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                finalLog += ((MyImage2D) object).logMagnificationChanges(zoom) + "\n";
            }
        }
        return finalLog;
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

    /**
     * Force the montage width to equal 'width_in_px'
     *
     * @param width_in_px
     */
    public void setToWidth(double width_in_px) {
        this.width_in_px = width_in_px;
        Rectangle2D r2d = getBounds2D();
        if (r2d != null) {
            double pure_image_width = r2d.getWidth() - (nb_cols - 1) * space_between_images;
            double ratio = (width_in_px - (nb_cols - 1) * space_between_images) / pure_image_width;
            for (Object object : pos_n_shapes) {
                ((Transformable) object).scale(ratio);
                ((MyRectangle2D) object).setFirstCorner(new Point2D.Double());
            }
            createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
        }
    }

    public void setToWidth() {
        setToWidth(width_in_px);
    }

    /**
     * Force the montage height to equal 'height_in_px'
     *
     * @param height_in_px
     */
    public void setToHeight(double height_in_px) {
        this.height_in_px = height_in_px;
        Rectangle2D r2d = getBounds2D();
        double pure_image_height = r2d.getHeight() - (nb_rows - 1) * space_between_images;
        double ratio = (height_in_px - (nb_rows - 1) * space_between_images) / pure_image_height;
        for (Object object : pos_n_shapes) {
            ((Transformable) object).scale(ratio);
            ((MyRectangle2D) object).setFirstCorner(new Point2D.Double());
        }
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
    }

    @Override
    public void scale(double factor) {
        for (Object object : pos_n_shapes) {
            ((Transformable) object).scale(factor);
        }
        createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
        updateMasterRect();
        setWidth_in_px(getBounds2D().getWidth());
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (isRotable()) {
            this.angle = angleInDegrees;
        }
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        if (rec2d.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setColor(Color.MAGENTA);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(rec2d);
            /*
             * draw diagonals over selection
             */
//            Line2D.Double diag1 = new Line2D.Double(rec2d.x, rec2d.y, rec2d.x + rec2d.width, rec2d.y + rec2d.height);
//            Line2D.Double diag2 = new Line2D.Double(rec2d.x + rec2d.width, rec2d.y, rec2d.x, rec2d.y drawAndFillc2d.height);
//     drawAndFill   g2d.draw(diag1);
//            g2d.draw(diag2);
            g2dParams.restore(g2d);
        }
    }

    /**
     *
     * @param p
     * @return true if the montage contains the point p
     */
    public boolean contains(Point2D.Double p) {
        if (rec2d == null) {
            return false;
        }
        return rec2d.contains(p);
    }

    /**
     * pack the vectorial objects in the X direction
     */
    public void packX() {
        packX(0);
    }

    /**
     * pack the vectorial objects in the X direction without space between them
     *
     * @param space
     */
    public void packX(double space) {
        //on recup les bounds de tout
        Point2D.Double last_pos = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_pos == null) {
                last_pos = cur_pos;
            } else {
                /*
                 * we recover the appropriate y pos
                 */
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(last_pos.x + space, first.y);
                t.setFirstCorner(trans);
                last_pos = t.getLastCorner();
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
     * @param space
     */
    public void packY(double space) {
        Point2D.Double last_pos = null;
        for (Object object : pos_n_shapes) {
            Transformable t = (Transformable) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_pos == null) {
                last_pos = cur_pos;
            } else {
                /*
                 * we recover the appropriate y pos
                 */
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(first.x, last_pos.y + space);
                t.setFirstCorner(trans);
                last_pos = t.getLastCorner();
            }
        }
        updateMasterRect();
    }

    public int getMontageID() {
        return montageID;
    }

    public void setMontageID(int montageID) {
        this.montageID = montageID;
    }

    @Override
    public String toString() {
//        /*
//         * if the montage is engaged in a figudrawAndFille add a lock symbol to it maybe I could also draw a lock icon somewhere
//         */
        return montageID + "";//+ (isUsedInAFigure ? "locked" : "locked");//"\uD83D\uDD12" \u02DF
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
     * Swaps the position of two objects
     *
     * @param pos1 position/number of the first object
     * @param pos2 position/number of the first object
     */
    public void swap(int pos1, int pos2) {
        if (pos1 >= 0 && pos2 >= 0 && pos1 < pos_n_shapes.size() && pos2 < pos_n_shapes.size()) {
            ArrayList<ColoredTextPaneSerializable> letters = new ArrayList<ColoredTextPaneSerializable>();

            for (Object object : pos_n_shapes) {
                if (object instanceof MyImage2D) {
                    ColoredTextPaneSerializable letter = ((MyImage2D) object).getLetter();
                    letters.add(letter);
                }
            }
            ArrayList<Object> tmp = new ArrayList<Object>(pos_n_shapes);
            Collections.swap(tmp, pos1, pos2);
            pos_n_shapes = new LinkedHashSet<Object>(tmp);
            int i = 0;
            for (Object object : pos_n_shapes) {
                if (object instanceof MyImage2D) {
                    if (i < letters.size()) {
                        ((MyImage2D) object).setLetter(new ColoredTextPane(letters.get(i++)));
                    }
                }
            }
            createTable(nb_cols, nb_rows, space_between_images, CURRENT_TABLE_ORIENTATION, CURRENT_ALIGNMENT);
            updateMasterRect();
        }
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
                 * we also swap the letters because it makes sense
                 */
                first_object = null;
                ColoredTextPaneSerializable first_letter = null;
                for (Object object : pos_n_shapes) {
                    if (first_letter == null) {
                        first_letter = ((MyImage2D) object).getLetter();
                        first_object = object;
                    } else {
                        ColoredTextPaneSerializable letter2 = ((MyImage2D) object).getLetter();
                        ((MyImage2D) object).setLetter(new ColoredTextPane(first_letter));
                        ((MyImage2D) first_object).setLetter(new ColoredTextPane(letter2));
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
        translate(-trans.x, -trans.y);
    }

    @Override
    public void setFirstCorner() {
        setFirstCorner(new Point2D.Double());
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

//    @Override
//    public void setColor(int color) {
//        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public void erode(int nb_erosions) {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).erode(nb_erosions);
        }
        updateMasterRect();

    }

    @Override
    public void erode() {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).erode();
        }
        updateMasterRect();
    }

    @Override
    public void dilate(int nb_dilatations) {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).dilate(nb_dilatations);
        }
        updateMasterRect();
    }

    @Override
    public void dilate() {
        for (Object object : pos_n_shapes) {
            ((Morphable) object).dilate();
        }
        updateMasterRect();
    }

    /**
     * Applies the journal style parameters to all the vectorial objects
     * contained in the montage
     *
     * @param jp
     */
    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        if (jp == null) {
            return;
        }
        for (Object object : pos_n_shapes) {
            if (object instanceof MyPlotVector) {
                ((MyPlotVector) object).setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
            } else if (object instanceof MyImageVector) {
                ((MyImageVector) object).setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
            } else if (object instanceof MyImage2D) {
                ((MyImage2D) object).setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
            }
        }
    }

    /**
     *
     * @return the width of the montage in cm
     */
    public double getWidth_in_cm() {
        return width_in_cm;
    }

    /**
     * Sets the width of the montage (in cm)
     *
     * @param width_in_cm
     */
    public void setWidth_in_cm(double width_in_cm) {
        this.width_in_cm = width_in_cm;
    }

    /**
     *
     * @return the height of the montage in cm
     */
    public double getHeight_in_cm() {
        return height_in_cm;
    }

    /**
     * Sets the height of the montage (in cm)
     *
     * @param height_in_cm
     */
    public void setHeight_in_cm(double height_in_cm) {
        this.height_in_cm = height_in_cm;
    }

    /**
     *
     * @return the width of the montage in pixels
     */
    public double getWidth_in_px() {
        return width_in_px;
    }

    /**
     * Sets the width of the montage in pixels
     *
     * @param width_in_px
     */
    public void setWidth_in_px(double width_in_px) {
        this.width_in_px = width_in_px;
    }

    /**
     *
     * @return the height of the montage in pixels
     */
    public double getHeight_in_px() {
        return height_in_px;
    }

    /**
     * Sets the height of the montage in pixels
     *
     * @param height_in_px
     */
    public void setHeight_in_px(double height_in_px) {
        this.height_in_px = height_in_px;
    }

    /**
     *
     * @return the resolution of the montage (typically 72dpi)
     */
    public double getResolution() {
        return resolution;
    }

    /**
     * Set the internal resolution of the montage
     *
     * @param resolution
     */
    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    /**
     * recalculate the montage size in cm (knowing the resolution and the size
     * of the montage in pixels)
     */
    public void updateSizeInCM() {
        setWidth_in_cm(Converter.PxToCmAnyDpi(width_in_px, resolution));
    }

    /**
     * Sets or changes the text, its color and the text background color
     *
     * @param color text color
     * @param bgColor text background color
     * @param ft text font
     * @param change_color override text color
     * @param change_bg_color override text background color
     * @param change_ft override text font
     */
    public void setTextAndBgColor(int color, Color bgColor, Font ft, boolean change_color, boolean change_bg_color, boolean change_ft, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
            }
        }
    }

    /**
     * make all images contained in the montage ready for serialization
     * (necessary to save images)
     */
    public void getTextreadyForSerialization() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).getTextreadyForSerialization();
            }
        }
    }

    /**
     * Recreate styleDoc from html-like string (necessary when a .figur file is
     * reloaded)
     */
    public void recreateStyledDoc() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).recreateStyledDoc();
            }
        }
    }

    public void setROIDrawOpacity(float opacity) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).setROIDrawOpacity(opacity);
            }
        }
    }

    public void setROIFillOpacity(float opacity) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).setROIFillOpacity(opacity);
            }
        }
    }

    /**
     * Change all the letters of the montage
     *
     * @param first_letter letter of the first image of the montage
     * @return the next non used letter (to be used as first letter for another
     * montage for example)
     */
    public final String reletter(String first_letter) {
        char letter = null_char;
        if (first_letter != null && first_letter.trim().length() == 1) {
            letter = first_letter.charAt(0);
            for (Object object : pos_n_shapes) {
                if (object instanceof MyImage2D) {
                    ((MyImage2D) object).setLetterText(letter + "");
                    letter++;
                }
            }
            return letter + "";
        } else {
            /*
             * we remove the letter if the user removes the letter or just typed 
             */
            for (Object object : pos_n_shapes) {
                if (object instanceof MyImage2D) {
                    ((MyImage2D) object).setLetterText("");
                }
            }
        }
        if (letter == null_char) {
            return null;
        }
        return letter + "";
    }

    /**
     *
     * @return all the vectorial objects contained in the montage
     */
    public LinkedHashSet<Object> getPos_n_shapes() {
        return pos_n_shapes;
    }

    /**
     * Sets the vectorial objects of the montage
     *
     * @param pos_n_shapes
     */
    public void setPos_n_shapes(LinkedHashSet<Object> pos_n_shapes) {
        this.pos_n_shapes = pos_n_shapes;
    }

    /**
     * Strips all text from the current montage
     */
    public void removeAllText() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).removeAllText();
            }
        }
    }

    /**
     * removes all scalebars from the current montage
     */
    public void removeAllBars() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).removeAllBars();
            }
        }
    }

    /**
     * removes all annotations from images
     */
    public void removeAllAnnotations() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).removeAllAnnotations();
            }
        }
    }

    /**
     *
     * @return true if the montage contains some R graphs
     */
    public boolean requiresRsession() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyPlotVector) {
                return true;
            }
        }
        return false;
    }

    /**
     * Splits the image into a series of gray channels and magenta green
     * combinations
     *
     * @param isRed
     * @param isgreen
     * @param isblue
     * @param isRedGreen
     * @param isGreenBlue
     * @param isRedBlue
     * @param pos
     */
    public void createColorBlindFriendlyImages(boolean isRed, boolean isgreen, boolean isblue, boolean isRedGreen, boolean isGreenBlue, boolean isRedBlue, int... pos) {
        /*
         * this is not easy to MT
         */
        ArrayList<Object> final_montage = new ArrayList<Object>();
        ArrayList<Integer> pos2 = new ArrayList<Integer>();
        for (int i = 0; i < pos.length; i++) {
            pos2.add(pos[i]);
        }
        int count = 0;
        int total_count = 0;
        for (Object object : pos_n_shapes) {
            final_montage.add(object);
            if (pos2.contains(count)) {
                ArrayList<MyImage2D.Double> spliited_images = new ArrayList<MyImage2D.Double>();
                if (object instanceof MyImage2D.Double) {
                    BufferedImage tmp = ((MyImage2D.Double) object).bimg.getBufferedImage();
                    BufferedImage red = (ImageColors.forceWhite(ImageColors.RGB2R(tmp), ImageColors.CH1_TO_WHITE));
                    BufferedImage green = (ImageColors.forceWhite(ImageColors.RGB2G(tmp), ImageColors.CH2_TO_WHITE));
                    BufferedImage blue = (ImageColors.forceWhite(ImageColors.RGB2B(tmp), ImageColors.CH3_TO_WHITE));
                    MyImage2D.Double redo = new MyImage2D.Double(0, 0, red);
                    String short_name = ((MyImage2D.Double) object).shortName;
                    redo.shortName = short_name + ":Red";
                    redo.setLower_left_text("Red");
                    redo.lower_left_text.setTextBgColor(Color.BLACK);
                    if (isRed) {
                        spliited_images.add(redo);
                    }
                    MyImage2D.Double greeno = new MyImage2D.Double(0, 0, green);
                    greeno.shortName = short_name + ":Green";
                    greeno.setLower_left_text("Green");
                    greeno.lower_left_text.setTextBgColor(Color.BLACK);
                    if (isgreen) {
                        spliited_images.add(greeno);
                    }
                    MyImage2D.Double blueo = new MyImage2D.Double(0, 0, blue);
                    blueo.shortName = short_name + ":Blue";
                    blueo.setLower_left_text("Blue");
                    blueo.lower_left_text.setTextBgColor(Color.BLACK);
                    if (isblue) {
                        spliited_images.add(blueo);
                    }
                    BufferedImage redgreen = (ImageColors.RGB2ColorBlindRed(tmp));
                    BufferedImage greenblue = (ImageColors.RGB2ColorBlindRed(ImageColors.RGB2BRG(tmp)));
                    BufferedImage redblue = (ImageColors.RGB2ColorBlindRed(ImageColors.RGB2RBG(tmp)));
                    MyImage2D.Double redgreeno = new MyImage2D.Double(0, 0, redgreen);
                    redgreeno.shortName = short_name + ":Red/Green";
                    redgreeno.setLower_left_text("Red/Green");
                    redgreeno.lower_left_text.setTextBgColor(Color.BLACK);
                    redgreeno.lower_left_text.colorText(0, 3, 0xFF00FF);
                    redgreeno.lower_left_text.colorText(3, 1, 0xFFFFFF);
                    redgreeno.lower_left_text.colorText(5, 5, 0x00FF00);
                    if (isRedGreen) {
                        spliited_images.add(redgreeno);
                    }
                    MyImage2D.Double greenblueo = new MyImage2D.Double(0, 0, greenblue);
                    greenblueo.shortName = short_name + ":Green/Blue";
                    greenblueo.setLower_left_text("Green/Blue");
                    greenblueo.lower_left_text.setTextBgColor(Color.BLACK);
                    greenblueo.lower_left_text.colorText(0, 5, 0xFF00FF);
                    greenblueo.lower_left_text.colorText(5, 1, 0xFFFFFF);
                    greenblueo.lower_left_text.colorText(6, 4, 0x00FF00);
                    if (isGreenBlue) {
                        spliited_images.add(greenblueo);
                    }
                    MyImage2D.Double redblueo = new MyImage2D.Double(0, 0, redblue);
                    redblueo.shortName = short_name + ":Red/Blue";
                    redblueo.setLower_left_text("Red/Blue");
                    redblueo.lower_left_text.setTextBgColor(Color.BLACK);
                    redblueo.lower_left_text.colorText(0, 3, 0xFF00FF);
                    redblueo.lower_left_text.colorText(3, 1, 0xFFFFFF);
                    redblueo.lower_left_text.colorText(5, 4, 0x00FF00);
                    if (isRedBlue) {
                        spliited_images.add(redblueo);
                    }
                }
                total_count += spliited_images.size();
                for (MyImage2D.Double double1 : spliited_images) {
                    final_montage.add(double1);
                }
            }
            count++;
        }
        while (nb_cols * nb_rows < pos_n_shapes.size() + total_count) {
            nb_rows++;
        }
        if (!final_montage.isEmpty()) {
            pos_n_shapes.clear();
            pos_n_shapes.addAll(final_montage);
        }
        updateTable();
    }

    public void extractImages(String name) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).extractImage(name);
            }
        }
    }

    public void reloadAfterSerialization() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).reloadAfterSerialization();
            }
        }
    }

    public void capitalizeText(String capitalization) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).capitalizeText(capitalization);
            }
        }
    }

    public void capitalizeLetter(String capitalization) {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).capitalizeLetter(capitalization);
            }
        }
    }
//
//    public void italicizeSingleLetters() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).italicizeSingleLetters();
//            }
//        }
//    }
//
//    public void reformatNumbersWithUnits() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).reformatNumbersWithUnits();
//            }
//        }
//    }
//
//    public void replaceMinusByEN_DASH() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).replaceMinusByEN_DASH();
//            }
//        }
//    }
//
//    public void placePercentSignAtTheEnd() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).placePercentSignAtTheEnd();
//            }
//        }
//    }
//
//    public void replacePlainTextDegrees() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).replacePlainTextDegrees();
//            }
//        }
//    }
//
//    public void replaceMutliplicationBySymbol() {
//        for (Object object : pos_n_shapes) {
//            if (object instanceof MyImage2D) {
//                ((MyImage2D) object).replaceMutliplicationBySymbol();
//            }
//        }
//    }

    public boolean replace(Object cur_sel, Object aDouble) {
        if (cur_sel == null || aDouble == null) {
            return false;
        }
        if (pos_n_shapes != null && pos_n_shapes.contains(cur_sel)) {
            LinkedHashSet<Object> final_pos = new LinkedHashSet<Object>();
            for (Object object : pos_n_shapes) {
                if (object == cur_sel) {
                    ((MyImage2D) aDouble).copyAllButImages((MyImage2D) object);
                    final_pos.add(aDouble);
                } else {
                    final_pos.add(object);
                }
            }
            pos_n_shapes.clear();
            pos_n_shapes.addAll(final_pos);
            updateMasterRect();
        }
        return true;
    }

    /*
     * various type verifications
     */
    /**
     *
     * @param o
     * @return true if the object is a vector image object
     */
    private boolean isImageVector(Object o) {
        return (o instanceof MyImageVector);
    }

    /**
     *
     * @param o
     * @return true if the object is an image2D
     */
    private boolean isImage2D(Object o) {
        return (o instanceof MyImage2D);
    }

    /**
     *
     * @param o
     * @return true if the object is a an R plot
     */
    private boolean isPLotVector(Object o) {
        return (o instanceof MyPlotVector);
    }

    /**
     *
     * @param o
     * @return true if the object is a an R plot
     */
    private boolean isStrictlyPLotVector(Object o) {
        return isPLotVector(o);
    }

    /**
     *
     * @param o
     * @return true if the object is a an an image vector and not an R plot
     */
    private boolean isStrictlyImageVector(Object o) {
        if (isPLotVector(o)) {
            return false;
        }
        if (isImageVector(o)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param o
     * @return true if the object is an image2D but not a vector version of it
     * nor an R plot
     */
    private boolean isStrictlyImage2D(Object o) {
        if (isPLotVector(o)) {
            return false;
        }
        if (isImageVector(o)) {
            return false;
        }
        if (isImage2D(o)) {
            return true;
        }
        return false;
    }

    public boolean containsStrictlyImages2D(int[] pos) {
        if (pos == null) {
            return false;
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i : pos) {
            positions.add(i);
        }
        int counter = 0;
        for (Object object : pos_n_shapes) {
            if (isStrictlyImages2D(object) && positions.contains(counter)) {
                return true;
            }
            counter++;
        }
        return false;
    }

    public void rotate(int[] pos, final int ORIENTATION) {
        if (pos == null) {
            return;
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i : pos) {
            positions.add(i);
        }
        int counter = 0;
        LinkedHashSet<Object> images_to_process = new LinkedHashSet<Object>();
        /*
         * I modified the code to allow multithreading
         */
        for (Object object : pos_n_shapes) {
            if (positions.contains(counter) && isStrictlyImage2D(object)) {
                images_to_process.add((MyImage2D) object);
            }
//            switch (ORIENTATION) {
//                    case LEFT:
//                        ((MyImage2D) object).rotateLeft();
//                        break;
//                    case RIGHT:
//                        ((MyImage2D) object).rotateRight();
//                        break;
//                }
            counter++;
        }
        if (!images_to_process.isEmpty()) {
            MultiThreadExecuter mt = new MultiThreadExecuter();
            String rotation;
//            ProgressMonitor pm;
            switch (ORIENTATION) {
                case ROTATE_LEFT:
                    rotation = "rotateRight";
//                    pm = new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Rotate Right", "Starting soon...", 0, 100);
                    break;
                default:
                    rotation = "rotateLeft";
//                    pm = new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Rotate Left", "Starting soon...", 0, 100);
                    //mt.addAvancementListener("Rotate Left");
                    break;
            }
//            mt.addAvancementListener(pm);
            mt.addAvancementListener();
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mt.call();

//                }
//            });
            mt.genericCallableWithSavingOutput(images_to_process, rotation);
            mt.closeMonitor();
        }
    }

    public void rotateLeft(int[] pos) {
        rotate(pos, ROTATE_LEFT);
    }

    public void rotateRight(int[] pos) {
        rotate(pos, ROTATE_RIGHT);
    }

    public void flip(int[] pos, String ORIENTATION) {
        if (pos == null) {
            return;
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i : pos) {
            positions.add(i);
        }
        int counter = 0;
        LinkedHashSet<Object> images_to_process = new LinkedHashSet<Object>();
        /*
         * I modified the code to allow multithreading
         */
        for (Object object : pos_n_shapes) {
            if (positions.contains(counter) && isStrictlyImage2D(object)) {
                images_to_process.add((MyImage2D) object);
            }
            counter++;
        }
        if (!images_to_process.isEmpty()) {
            MultiThreadExecuter mt = new MultiThreadExecuter();
            mt.addAvancementListener();
            mt.genericCallableWithSavingOutput(images_to_process, "flip", ORIENTATION);
            mt.closeMonitor();
        }
    }

    public boolean containsImagesVector(int[] pos) {
        if (pos == null) {
            return false;
        }
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i : pos) {
            positions.add(i);
        }
        int counter = 0;
        for (Object object : pos_n_shapes) {
            if (isImagesVector(object) && positions.contains(counter)) {
                return true;
            }
            counter++;
        }
        return false;
    }

    public boolean isImagesVector(Object o) {
        if (o instanceof MyImageVector) {
            return true;
        }
        return false;
    }

    public boolean isStrictlyImages2D(Object o) {
        if (o instanceof MyImageVector) {
            return false;
        }
        if (o instanceof MyImage2D) {
            return true;
        }
        return false;
    }

    public boolean containsStrictlyImages2D() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImageVector) {
                continue;
            }
            if (object instanceof MyImage2D) {
                return true;
            }
        }
        return false;
    }

    public boolean containsVectorImages() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImageVector) {
                return true;
            }
        }
        return false;
    }

    public boolean containsStrictlyMyPlotVector() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyPlotVector) {
                return true;
            }
        }
        return false;
    }

    public boolean containsStrictlyImageVector() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImageVector && !(object instanceof MyPlotVector)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPlotVector() {
        for (Object object : pos_n_shapes) {
            if (object instanceof MyPlotVector) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    private void resetChecks() {
        wasCheckedForFonts = false;
        wasCheckedForGraphs = false;
        wasCheckedForLineArts = false;
        wasCheckedForSize = false;
        wasCheckedForStyle = false;
        wasCheckedForText = false;
    }

    public BufferedImage getFormattedImageWithoutTranslation() {
        double x = rec2d.x;
        double y = rec2d.y;
        rec2d.x = 0;
        rec2d.y = 0;
        BufferedImage out = null;
        try {
            out = new MyBufferedImage((int) rec2d.width, (int) rec2d.height, BufferedImage.TYPE_INT_ARGB);
            int width = out.getWidth();
            int height = out.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    out.setRGB(i, j, 0x00000000);
                }
            }
            Graphics2D g2d = out.createGraphics();
            drawAndFill(g2d);
            g2d.dispose();
        } catch (Exception e) {
        } finally {
            rec2d.x = x;
            rec2d.y = y;
            return out;
        }
    }

    public void checkSize(JournalParameters jp) {
        wasCheckedForSize = true;
        double widthInpx = getWidth_in_px();
        BufferedImage snap = getFormattedImageWithoutTranslation();
        CheckSize iopane = new CheckSize(snap, jp, widthInpx);
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Check figure size", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                double size = iopane.getFinalSize();
                setToWidth(Converter.cmToPx72dpi(size));
                setWidth_in_px(Converter.cmToPx72dpi(size));
                setWidth_in_cm(size);
                setFirstCorner();
            }
        }
    }

    public void checkGraph(JournalParameters jp) {
        wasCheckedForGraphs = true;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyPlotVector.Double) {
                CheckGraphs iopane = new CheckGraphs(((MyPlotVector.Double) object), jp);
                if (!iopane.noError) {
                    int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Check graph", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                    if (result == JOptionPane.OK_OPTION) {
                        object = iopane.getSolution();
                    }
                }
            }
        }
    }

    public void checkStyle() {
        wasCheckedForStyle = true;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).checkStyle();
            }
        }
    }

    public void checkText(JournalParameters jp) {
        wasCheckedForText = true;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).checkText(jp);
            }
        }

    }

    public void checkFonts(JournalParameters jp) {
        wasCheckedForFonts = true;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
                ((MyImage2D) object).checkFonts(jp);
            }
        }
    }

    public void checklineArts(float objectsStrokeSize, boolean isIllustrator) {
        wasCheckedForLineArts = true;
        for (Object object : pos_n_shapes) {
            if (object instanceof MyImage2D) {
//                MyImage2D img = ((MyImage2D) object);
                CheckLineArts iopane = new CheckLineArts(((MyImage2D) object), objectsStrokeSize, isIllustrator);
                if (!iopane.noError) {
                    int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Check stroke width of line arts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                    if (result == JOptionPane.OK_OPTION) {
                        //--> replace objects with copy
                        Object strokeData = iopane.getModifiedStrokeData();
                        if (strokeData instanceof ArrayList) {
                            ((MyImage2D) object).setAssociatedObjects(((ArrayList<Object>) strokeData));
//                    for (Object string : img.getAssociatedObjects()) {
//                        if (string instanceof PARoi) {
//                            System.out.println(((PARoi) string).getStrokeSize());
//                        }
//                    }
                        } else if (strokeData instanceof String) {
                            ((MyImageVector) object).setSvg_content_serializable((String) strokeData);
                            ((MyImageVector) object).reloadDocFromString();
                        }
                    }
                }
            }
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //voila un montage en macro --> lui faire reparser ca pr recreer le montage
        //--> d'abord lui faire recreer les images puis 
//        String macro_montage = "<Montage data-nbCols=\"2\" data-nbRows=\"2\" data-order=\"meander\" data-spaceBetweenImages=\"3\" data-widthInPx=\"512.0\" >\n"
//                + "         	<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series010.png\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">A</txtFgcolor></font></font>\" />\n"
//                + "         	<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series014.png\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">B</txtFgcolor></font></font>\" />\n"
//                + "         	<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series015.png\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">C</txtFgcolor></font></font>\" />\n"
//                + "         	<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">D</txtFgcolor></font></font>\" />\n"
//                + "         	<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/toto.svg\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">D</txtFgcolor></font></font>\" />\n"
//                + "         </Montage>";

        long start_time = System.currentTimeMillis();
        ArrayList<Object> shapes = new ArrayList<Object>();
        //D:\sample_images_PA\trash_test_mem\mini
        MyImage2D.Double i2d1 = new MyImage2D.Double(0, 0, new Loader().load("/E/sample_images_SF/images_de_test_fromIJ/AuPbSn40.png"));
//        i2d1.setLetter("A");
        MyImage2D.Double i2d2 = new MyImage2D.Double(0, 0, new Loader().load("/E/sample_images_SF/images_de_test_fromIJ/flybrain.png"));
//        i2d2.setLetter("B");
        MyImage2D.Double i2d3 = new MyImage2D.Double(0, 0, new Loader().load("/E/sample_images_SF/images_de_test_fromIJ/blobs_long.png"));
//        i2d3.setLetter("C");
//        MyImage2D.Double i2d4 = new MyImage2D.Double(0, 0, new Loader2().load("D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png"));
//        i2d4.setLetter("D");
        shapes.add(i2d1);
        shapes.add(i2d2);
        shapes.add(i2d3);
//        shapes.add(i2d4);
        BufferedImage test2 = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
        int width = test2.getWidth();
        int height = test2.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                test2.setRGB(i, j, 0xFFFFFF);
            }
        }
        Graphics2D g2d = test2.createGraphics();
        Montage test = new Montage(shapes, 2,1, false, 3);
        //Montage test = new Montage(null);
        test.setToWidth(128);
        test.drawAndFill(g2d);
//
//        System.out.println(test.produceMacroCode(1));
//
        g2d.dispose();
//
        SaverLight.popJ(test2);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

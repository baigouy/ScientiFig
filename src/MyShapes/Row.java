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

import Checks.CheckSize;
import Commons.CommonClassesLight;
import Commons.MyBufferedImage;
import Tools.Converter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * Row is a group of vectorial objects arranged as a row
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public class Row extends MyRectangle2D implements Drawable, Transformable, MagnificationChangeLoggable {

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
     * variables
     */
    Rectangle2D.Double blocked_size;
    public static final int FLOW_HORIZONTAL = 0;
    public static final int FLOW_VERTICAL = 1;
    private int CURRENT_ORIENTATION = 0;
    public static final int FOR_ROWS = 0;
    public static final int FOR_COLS = 1;
    public static final long serialVersionUID = -1955843873191502558L;
    /*
     * data for ImageJ macro mode
     */
    String masterTagOpen = "<Row ";
    String masterTagClose = "</Row>";
    /**
     *
     */
    public final static HashMap<String, String> parameters = new HashMap<String, String>();

    static {
        parameters.put("space", "data-spaceBetweenMontages=");
        parameters.put("width", "data-widthInPx=");
    }
    /*
     * end IJ macro mode
     */
    public boolean fakeRow = false;
    public double width_in_cm;
    public double height_in_cm;
    public double width_in_px;
    public double height_in_px;
    public double resolution = 72;
    public static final int ALIGNMENT_TOP = 0;
    public static final int ALIGNMENT_BOTTOM = 1;
    public static final int ALIGNMENT_CENTER = 2;
    public int ALIGNMENT = ALIGNMENT_CENTER;
    public double space_between_blocks = 3.;
    public String formula;
    public ArrayList<Object> blocks = new ArrayList<Object>();
    HashMap<String, Object> extras = new HashMap<String, Object>();
    public double spaceFromExtra = 3.;

    /**
     * Constructor
     *
     * @param shape first vectorial object of the row (others can be added
     * later)
     */
    public Row(Object shape) {
        blocks.add(shape);
        updateMasterRect();
    }

    /**
     * Constructor
     *
     * @param blocks vectorial objects that must be aligned in the same row
     * @param space_between_blocks space between those vectorial objects
     */
    public Row(ArrayList<Object> blocks, double space_between_blocks) {
        this.blocks = blocks;
        this.space_between_blocks = space_between_blocks;
        pack(space_between_blocks);
        updateMasterRect();
    }

    /**
     * Constructor
     *
     * @param montage a montage
     * @param space_between_blocks space between those vectorial objects
     */
    public Row(Montage montage, double space_between_blocks) {
        this.space_between_blocks = space_between_blocks;
        if (blocks == null || blocks.isEmpty()) {
            blocks = new ArrayList<Object>();
        }
        blocks.add(montage);
        pack(space_between_blocks);
        updateMasterRect();
    }

    public void setStrokeSize(float strokeSize, float pointSize, boolean isChangePointSize, boolean applyStrokeToGraphs, boolean applyStrokeToSVG, boolean applyStrokeToROIs, boolean isIllustrator) {
        for (Object object : blocks) {
            ((Montage) object).setStrokeSize(strokeSize, pointSize, isChangePointSize, applyStrokeToGraphs, applyStrokeToSVG, applyStrokeToROIs, isIllustrator);
        }
    }

    /**
     * Constructor
     *
     * @param macro an IJ macro
     */
    public Row(String macro) {
        HashMap<String, String> parameters = reparseMacro(macro);
        parameterDispatcher(parameters);
        ArrayList<String> montages = new ArrayList<String>();
        while (macro.toLowerCase().contains("/montage")) {
            macro = CommonClassesLight.strCutRightFisrt(macro, "ontage");
            String current_montage = CommonClassesLight.strCutLeftFirst(macro, "</Montage");//tag de fin
            macro = CommonClassesLight.strCutRightFisrt(macro, "</Montage");
            if (current_montage.contains("data-nbCols")) {
                montages.add(current_montage);
            }
        }
        for (String string : montages) {
            Montage m = new Montage(string);
            blocks.add(m);
        }
        rec2d = new Rectangle2D.Double(0, 0, 512, 512);
        pack(space_between_blocks);
        updateMasterRect();
        if (parameters.containsKey("width")) {
            try {
                double final_width = java.lang.Double.parseDouble(parameters.get("width"));
                setToWidth(final_width);
                sameHeight(space_between_blocks);
                setToWidth(final_width);
                sameHeight(space_between_blocks);
            } catch (Exception e) {
                System.err.println("Invalid montage width at:");
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }

    /**
     * for future development
     *
     * @param blocked_size
     */
    public void setBlocked_size(Rectangle2D.Double blocked_size) {
        this.blocked_size = new Rectangle2D.Double(blocked_size.x, blocked_size.y, blocked_size.width, blocked_size.height);
    }

    /**
     *
     * @return the current orientation of the row (the row can be either
     * vertical or horizontal)
     */
    public int getCURRENT_ORIENTATION() {
        return CURRENT_ORIENTATION;
    }

    /**
     * Sets the current row orientation (horizontal or vertical)
     *
     * @param CURRENT_ORIENTATION
     */
    public void setCURRENT_ORIENTATION(int CURRENT_ORIENTATION) {
        this.CURRENT_ORIENTATION = CURRENT_ORIENTATION;
    }

    /**
     *
     * @param txt
     * @return a set of paramters
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

    private void parameterDispatcher(HashMap<String, String> macro_parameters) {
        rec2d = new Rectangle2D.Double();
        ArrayList<String> keys = new ArrayList<String>(macro_parameters.keySet());

        for (String string : keys) {
            if (string.equals("space")) {
                try {
                    space_between_blocks = java.lang.Double.parseDouble(macro_parameters.get(string));
                } catch (Exception e) {
                }
                continue;
            }
        }
    }

    /**
     * Swaps the position of two objects
     *
     * @param pos1 position/number of the first object
     * @param pos2 position/number of the first object
     */
    public boolean swap(int pos1, int pos2) {
        if (blocks.isEmpty()) {
            return false;
        }
        if (pos1 >= 0 && pos2 >= 0 && pos1 < blocks.size() && pos2 < blocks.size()) {
            Collections.swap(blocks, pos1, pos2);
            updateExtras();
            packX();
            updateMasterRect();
            return true;
        }
        return false;
    }

    /**
     *
     * @param nb_tab
     * @return an imageJ macro that contains the infos necessary to build a row
     */
    public String produceMacroCode(int nb_tab) {
        String macro_code = "";
        for (int i = 0; i < nb_tab; i++) {
            macro_code += "\t";
        }
        macro_code += masterTagOpen;
        /*
         * now we add the parameters for the contained files
         */
        macro_code += parameters.get("space") + "\"" + space_between_blocks + "\" ";
        macro_code += parameters.get("width") + "\"" + getWidth() + "\" ";
        /*
         * we finalise the tag
         */
        macro_code += ">\n";
        for (Object object : blocks) {
            if (object instanceof Montage) {
                macro_code += ((Montage) object).produceMacroCode(2 + nb_tab);
            }
        }
        for (int i = 0; i < nb_tab; i++) {
            macro_code += "\t";
        }
        macro_code += masterTagClose + "\n";
        return macro_code;
    }

    /**
     *
     * @return the text side bar at the top
     */
    public TopBar getTopTextBar() {
        Object tb = extras.get("top bar");
        if (tb != null) {
            return (TopBar) tb;
        } else {
            return null;
        }
    }

    /**
     * Sets the text side bar at the top
     *
     * @param topTextBar
     */
    public void setTopTextBar(TopBar topTextBar) {
        extras.put("top bar", topTextBar);
    }

    /**
     *
     * @return the text side bar at the bottom
     */
    public TopBar getBottomTextBar() {
        Object tb = extras.get("bottom bar");
        if (tb != null) {
            return (TopBar) tb;
        } else {
            return null;
        }
    }

    /**
     * Sets the text side bar at the bottom
     *
     * @param bottomTextBar
     */
    public void setBottomTextBar(TopBar bottomTextBar) {
        extras.put("bottom bar", bottomTextBar);
    }

    /**
     *
     * @return the text side bar on the left
     */
    public TopBar getLeftTextBar() {
        Object tb = extras.get("left bar");
        if (tb != null) {
            return (TopBar) tb;
        } else {
            return null;
        }
    }

    /**
     * Sets the text side bar on the left
     *
     * @param leftTextBar
     */
    public void setLeftTextBar(TopBar leftTextBar) {
        extras.put("left bar", leftTextBar);
    }

    /**
     *
     * @return the text side bar on the right
     */
    public TopBar getRightTextBar() {
        Object tb = extras.get("right bar");
        if (tb != null) {
            return (TopBar) tb;
        } else {
            return null;
        }
    }

    public void setAdditionbalLetterBar(TopBar letterBar) {
        extras.put("letter bar", letterBar);
    }

    public TopBar getAdditionbalLetterBar() {
        Object tb = extras.get("letter bar");
        if (tb != null) {
            return (TopBar) tb;
        } else {
            return null;
        }
    }

    /**
     * Sets the text side bar on the right
     *
     * @param rightTextBar
     */
    public void setRightTextBar(TopBar rightTextBar) {
        extras.put("right bar", rightTextBar);
    }

    /**
     * Applies a journal to all the vectorial components of the row
     *
     * @param jp
     */
    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        if (jp == null) {
            return;
        }
        for (Object object : blocks) {
            ((Montage) object).setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
        }
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
        for (Object object : blocks) {
            ((Montage) object).setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
        }
    }

    /**
     *
     * @return the number of images in the x direction (useful when one wants to
     * add a text bar over those images)
     */
    public int getImageCountInXDirection() {
        int image_count = 0;
        for (Object object : blocks) {
            if (object instanceof Montage) {
                image_count += ((Montage) object).getNb_cols();
            }
        }
        return image_count;
    }

    /**
     *
     * @return the number of images in the y direction (useful when one wants to
     * add a text bar over those images)
     */
    public int getImageCountInYDirectionLeft() {
        int image_count = 0;
        for (Object object : blocks) {
            if (object instanceof Montage) {
                image_count = ((Montage) object).getNb_rows();
                return image_count;
            }
        }
        return image_count;
    }

    /**
     *
     * @return the number of images in the y direction (useful when one wants to
     * add a text bar over those images)
     */
    public int getImageCountInYDirectionRight() {
        int image_count = 0;
        for (Object object : blocks) {
            if (object instanceof Montage) {
                image_count = ((Montage) object).getNb_rows();
            }
        }
        return image_count;
    }

    /**
     *
     * @return the row incompressible width
     */
    public double getIncompressibleWidth() {
        double extra_space = 0;
        if (extras == null || extras.isEmpty()) {
            return 0;
        }
        if (extras.containsKey("left bar")) {
            if (extras.get("left bar") != null) {
                if (((TopBar) extras.get("left bar")).getBounds2D() != null) {
                    extra_space += spaceFromExtra + ((TopBar) extras.get("left bar")).getBounds2D().getWidth();
                }
            }
        }
        if (extras.containsKey("right bar")) {
            if (extras.get("right bar") != null) {
                if (((TopBar) extras.get("right bar")).getBounds2D() != null) {
                    extra_space += spaceFromExtra + ((TopBar) extras.get("right bar")).getBounds2D().getWidth();
                }
            }
        }
        return extra_space;
    }

    public void setToWidth() {
        setToWidth(width_in_px);
    }

    /**
     * Forces the row to be of width (width_in_px)
     *
     * @param width_in_px
     */
    public void setToWidth(double width_in_px) {
        this.width_in_px = width_in_px;
        double nb_cols = blocks.size();
        Rectangle2D r2d = getBounds2D();
        double pure_image_width = (r2d.getWidth()) - (nb_cols - 1.) * space_between_blocks;
        /*
         * we do additional corrections if there are bars around
         */
        pure_image_width -= getIncompressibleWidth();
        width_in_px -= getIncompressibleWidth();
        /*
         * end additional bar corrections
         */
        double ratio = (width_in_px - (nb_cols - 1.) * space_between_blocks) / pure_image_width;
        for (Object object : blocks) {
            if (object instanceof Montage) {
                ((Montage) object).setResolution(resolution);
                /*
                 * nb the scale was causing errors because of the incompressible size of the space between montages
                 */
                ((Montage) object).setToWidth(((Montage) object).getWidth() * ratio);
                ((Montage) object).setFirstCorner(new Point2D.Double());
                ((Montage) object).updateSizeInCM();//--> a tester mais devrait etre ok puis totalement tt fini
            }
            if (object instanceof Row) {
                ((Row) object).setToWidth(((Row) object).getWidth() * ratio);
                ((Row) object).setFirstCorner(new Point2D.Double());
                ((Row) object).setResolution(resolution);
            }
        }
        pack(space_between_blocks);
        updateMasterRect();
    }

    @Override
    public Rectangle2D getBounds2D() {
        updateMasterRect();
        return rec2d;
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

    /**
     *
     * @return the formula used to create the rows (1+2 means montages 1 and 2
     * have been used to create the current row)
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Sets the row formula (4+3 means montage 4 and montage 3 belong to the
     * same row montage 4 is the left most member of the row)
     *
     * @param formula
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * align the row constituents in x or in y direction
     */
    public void pack() {
        if (CURRENT_ORIENTATION == FLOW_HORIZONTAL) {
            packX();
        } else {
            packY();
        }
    }

    /**
     * align the row constituents in x or in y direction
     *
     * @param space space between row constitutents
     */
    private void pack(double space) {
        if (CURRENT_ORIENTATION == FLOW_HORIZONTAL) {
            packX(space);
        } else {
            packY(space);
        }
    }

    /**
     * align the row constituents in x direction
     */
    public void packX() {
        packX(space_between_blocks);
    }

    /**
     * align the row constituents in y direction
     */
    public void packY() {
        packY(space_between_blocks);
    }

    /**
     * align the row constituents in y direction
     *
     * @param space space between row constitutents
     */
    public void packY(double space) {
        for (Object object : blocks) {
            ((PARoi) object).setFirstCorner(new Point2D.Double());
        }
        Point2D.Double last_pos = null;
        for (Object object : blocks) {
            PARoi t = (PARoi) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_pos == null) {
                last_pos = cur_pos;
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            } else {
                Point2D.Double first = t.getFirstCorner();
                Point2D.Double trans = new Point2D.Double(first.x, last_pos.y + space);
                t.setFirstCorner(trans);//on dirait que c'est ca qui marche pas mais pkoi
                last_pos = t.getLastCorner();
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            }
        }
        updateExtras();
        updateMasterRect();
    }

    private void packX(double space) {
        for (Object object : blocks) {
            ((PARoi) object).setFirstCorner(new Point2D.Double());
        }
        Point2D.Double last_pos = null;
        for (Object object : blocks) {
            PARoi t = (PARoi) object;
            Point2D.Double cur_pos = t.getLastCorner();
            if (last_pos == null) {
                last_pos = cur_pos;
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            } else {
                /*
                 * we recover the appropriate y count
                 */
                Point2D.Double first = t.getFirstCorner();
                /*
                 * nb il faut le -1 sinon va 1 pixel trop loin dans le spacing c'est du (je pense) au fait que le dernier pixel de la width n'appartient pas a la forme (<width) mais y reflechir
                 */
                Point2D.Double trans = new Point2D.Double(last_pos.x + space, first.y);
                t.setFirstCorner(trans);
                last_pos = t.getLastCorner();
                last_pos = new Point2D.Double(last_pos.x, last_pos.y);
            }
        }
        updateExtras();
        updateMasterRect();
    }

    /**
     * updates the position of the extras (text side bars)
     */
    public void updateExtras() {
        double extraY = 0;
        if (extras.isEmpty()) {
            return;
        }
        Rectangle2D mainSize = getCoreRect();
        if (extras.containsKey("top bar")) {
            TopBar tb = (TopBar) extras.get("top bar");
            if (tb != null) {
                tb.associated_row = this;
                tb.update();
                tb.setY(mainSize.getY());
                tb.translate(0, -tb.getHeight() - spaceFromExtra);
                extraY = tb.getHeight();
                tb.associated_row = null;
            }
        }

        double extraX = 0;
        if (extras.containsKey("left bar")) {
            TopBar tb = (TopBar) extras.get("left bar");
            if (tb != null) {
                tb.associated_row = this;
                tb.update();
                tb.setX(mainSize.getX());
                extraX = tb.getWidth();
                tb.translate(0 - tb.getWidth() - spaceFromExtra, 0);
                tb.associated_row = null;
            }
        }

        if (extras.containsKey("letter bar")) {
            TopBar tb = (TopBar) extras.get("letter bar");
            if (tb != null) {
                tb.associated_row = this;
                tb.update();
                tb.setY(mainSize.getY() - extraY);
                tb.translate(0 - extraX, -tb.getHeight() - spaceFromExtra);
                tb.associated_row = null;
            }
        }

        if (extras.containsKey("bottom bar")) {
            TopBar tb = (TopBar) extras.get("bottom bar");
            if (tb != null) {
                tb.associated_row = this;
                tb.update();
                tb.setY(mainSize.getY() + mainSize.getHeight() + spaceFromExtra);
                tb.associated_row = null;
            }
        }

        if (extras.containsKey("right bar")) {
            TopBar tb = (TopBar) extras.get("right bar");
            if (tb != null) {
                tb.associated_row = this;
                tb.update();
                tb.setX(mainSize.getX() + mainSize.getWidth() + spaceFromExtra);
                tb.associated_row = null;
            }
        }
        updateMasterRect();
    }

    @Override
    public void translate(double x, double y) {
        for (Object object : blocks) {
            ((Transformable) object).translate(x, y);
        }
        for (Object object : extras.values()) {
            if (object instanceof Transformable) {
                ((Transformable) object).translate(x, y);
            }
        }
    }

    /**
     *
     * @return the bounding rectangle surrounding the row (extras excluded)
     */
    public Rectangle2D getCoreRect() {
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        for (Object object : blocks) {
            if (object != null) {
                if (object instanceof Montage) {
                    found = true;
                    Rectangle2D shp = ((Montage) object).getBounds2D();
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
            return null;
        }
        return new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    /*
     * Computes the bounding rect surrounding the current row (extras included)
     */
    private void updateMasterRect() {
        if (blocked_size != null) {
            rec2d = new Rectangle2D.Double(blocked_size.x, blocked_size.y, blocked_size.width, blocked_size.height);
            return;
        }

        rec2d = null;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        for (Object object : blocks) {
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
        for (Object object : extras.values()) {
            if (object != null) {
                if (object instanceof PARoi) {
                    found = true;
                    Rectangle2D shp = ((PARoi) object).getBounds2D();
                    /*
                     * bug fix for row swapping with textbars unassociated to blocks anymore
                     */
                    if (shp == null) {
                        continue;
                    }
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

    /*
     * a bit dirty improve this at some point
     */
    private void resetChecks() {
        wasCheckedForFonts = false;
        wasCheckedForGraphs = false;
        wasCheckedForLineArts = false;
        wasCheckedForSize = false;
        wasCheckedForStyle = false;
        wasCheckedForText = false;
    }

    /**
     * Add a new vectorial object to the row
     *
     * @param shape
     */
    public void add(Object shape) {
        resetChecks();
        blocks.add(shape);
        pack(space_between_blocks);
        updateMasterRect();
    }

    /**
     * Add a new montage to the row
     *
     * @param block
     */
    public void addBlock(Montage block) {
        resetChecks();
        blocks.add(block);
        pack(space_between_blocks);
        updateMasterRect();
    }

    /**
     * Remove a montage from the row
     *
     * @param block
     */
    public boolean removeBlock(Montage block) {
        boolean success = blocks.remove(block);
        if (!blocks.isEmpty()) {
            pack(space_between_blocks);
            updateMasterRect();
        }
        return success;
    }

    /**
     * Aligns objects within the row
     */
    public void arrangeRow() {
        /*
         * let's align the objects contained in the row
         */
        ComplexShapeLight cs = new ComplexShapeLight(blocks);
        switch (ALIGNMENT) {
            case ALIGNMENT_CENTER:
                cs.alignCenterY();
                break;
            case ALIGNMENT_BOTTOM:
                cs.alignBottom();
                break;
            case ALIGNMENT_TOP:
                cs.alignTop();
                break;
        }
        updateExtras();
        updateMasterRect();
    }

    /**
     * Forces the vectorial objects contained in the row to have the same width
     *
     * @param spaceX
     */
    public void sameWidth(double spaceX) {
        double max_witdth = Integer.MIN_VALUE;
        for (Object block : blocks) {
            max_witdth = Math.max(((Montage) block).getWidth(), max_witdth);
        }
        for (Object block : blocks) {
            ((Montage) block).setToWidth(max_witdth);
            ((Montage) block).updateSizeInCM();
        }
        ComplexShapeLight cs = new ComplexShapeLight(blocks);
        cs.packX(spaceX);
        arrangeRow();
        updateMasterRect();
    }

    public void sameHeight() {
        sameHeight(space_between_blocks);
    }

    /**
     * Forces the vectorial objects contained in the row to have the same height
     * (useful when row orientation is horizontal, the default orientation)
     *
     * @param spaceX
     */
    public void sameHeight(double spaceX) {
        double max_height = Integer.MIN_VALUE;
        for (Object block : blocks) {
            max_height = Math.max(((Montage) block).getHeight(), max_height);
        }
        for (Object block : blocks) {
            ((Montage) block).setToHeight(max_height);
            ((Montage) block).updateSizeInCM();
        }
        ComplexShapeLight cs = new ComplexShapeLight(blocks);
        cs.packX(spaceX);
        arrangeRow();
        updateMasterRect();
    }

    //modification for review 
    @Override
    public String logMagnificationChanges(double zoom) {
        String finalLog = "";
        for (Object block : blocks) {
            finalLog += ((Montage) block).logMagnificationChanges(zoom) + "\n";
        }
        return finalLog;
    }

//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float opacity, float strokeSize) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawAndFill(g2d, color, transopacityokeSize);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFill(g2d, color, transparenopacityze);
//            }
//        }
//    }
//
//    @Override
//    public void fill(Graphics2D g2d, int color, float transparency, fopacityize) {
//        for (Object block : blocks) {
//            ((Drawable) block).fill(g2d, color, transparency, strokeopacity     }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).fill(g2d, color, transparency, strokeSize)opacity    }
//        }
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color, float transparency, float strokeSizeopacity  for (Object block : blocks) {
//            ((Drawable) block).drawAndFill(g2d, color, transparency, strokeSize);
//      opacity  for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFill(g2d, color, transparency, strokeSize);
//           opacity }
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawAndFill(g2d, color);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFill(g2d, color);
//            }
//        }
//    }
//
//    @Override
//    public void fill(Graphics2D g2d, int color) {
//        for (Object block : blocks) {
//            ((Drawable) block).fill(g2d, color);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).fill(g2d, color);
//            }
//        }
//    }
//
//    @Override
//    public void drawAndFill(Graphics2D g2d, int color) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawAndFill(g2d, color);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFill(g2d, color);
//            }
//        }
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d, float transparency) {
//        for (Object block :opacity/            ((Drawable) block).drawTransparent(g2d, transparency);
//        }
//        for (Object oopacityas.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawTransparent(g2d, transparency);
//            }
//        }
//    }
//
///opacityde
//    public void fillTransparent(Graphics2D g2d, float transparency) {
//        for (Object block : blocks) {
//  opacityDrawable) block).fillTransparent(g2d, transparency);
//        }
//        for (Object object : extras.opacity//            if (object instanceof Drawable) {
//                ((Drawable) object).fillTransparent(g2d, transparency);
//            }
//        }
//    }
//
//    @Override
//opacity void drawAndFillTransparent(Graphics2D g2d, float transparency) {
//        for (Object block : blocks) {
//            ((Draopacity).drawAndFillTransparent(g2d, transparency);
//        }
//        for (Object object : extras.values()) {
// opacityf (object instanceof Drawable) {
//                ((Drawable) object).drawAndFillTransparent(g2d, transparency);
//            }
//        }
//    }
    @Override
    public void drawAndFill(Graphics2D g2d) {
        for (Object block : blocks) {
            ((Drawable) block).drawAndFill(g2d);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).drawAndFill(g2d);
            }
        }
    }

    @Override
    public void fill(Graphics2D g2d) {
        for (Object block : blocks) {
            ((Drawable) block).fill(g2d);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).fill(g2d);
            }
        }
    }

//    @Override
//    public void drawAndFill(Graphics2D g2d) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawAndFill(g2d);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFill(g2d);
//            }
//        }
//    }
//
//    @Override
//    public void drawTransparent(Graphics2D g2d) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawTransparent(g2d);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawTransparent(g2d);
//            }
//        }
//    }
//
//    @Override
//    public void fillTransparent(Graphics2D g2d) {
//        for (Object block : blocks) {
//            ((Drawable) block).fillTransparent(g2d);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).fillTransparent(g2d);
//            }
//        }
//    }
//
//    @Override
//    public void drawAndFillTransparent(Graphics2D g2d) {
//        for (Object block : blocks) {
//            ((Drawable) block).drawAndFillTransparent(g2d);
//        }
//        for (Object object : extras.values()) {
//            if (object instanceof Drawable) {
//                ((Drawable) object).drawAndFillTransparent(g2d);
//            }
//        }
//    }
    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visibleRect) {
        for (Object block : blocks) {
            ((Drawable) block).drawIfVisible(g2d, visibleRect);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).drawIfVisible(g2d, visibleRect);
            }
        }
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visibleRect) {
        if (rec2d != null) {
            g2d.setColor(Color.cyan);
            g2d.setStroke(new BasicStroke(3f));
//            Line2D.Double diag1 = new Line2D.Double(rec2d.x, rec2d.y, rec2d.x + rec2d.width, rec2d.y + rec2d.height);
//            Line2D.Double diag2 = new Line2D.Double(rec2d.x + rec2d.width, rec2d.y, rec2d.x, rec2d.y + rec2d.height);
            g2d.draw(rec2d);
//            g2d.drawAndFill(diag1);
//            g2d.drawAndFill(diag2);
        }
    }

    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        for (Object block : blocks) {
            ((Drawable) block).drawIfVisibleWhenDragged(g2d, visibleRect);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).drawIfVisibleWhenDragged(g2d, visibleRect);
            }
        }
    }

    @Override
    public void drawSkeletton(Graphics2D g2d, int color) {
        for (Object block : blocks) {
            ((Drawable) block).drawSkeletton(g2d, color);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).drawSkeletton(g2d, color);
            }
        }
    }

    @Override
    public void fillSkeletton(Graphics2D g2d, int color) {
        for (Object block : blocks) {
            ((Drawable) block).fillSkeletton(g2d, color);
        }
        for (Object object : extras.values()) {
            if (object instanceof Drawable) {
                ((Drawable) object).fillSkeletton(g2d, color);
            }
        }
    }

    @Override
    public boolean isVisible(Rectangle visibleRect) {
        return getBounds2D().intersects(rec2d);
    }

    @Override
    public void drawShapeInfo(Graphics2D g2d, int position) {
    }

    @Override
    public void drawText(Graphics2D g2d, String text, int position) {
    }

    @Override
    public void erode(int nb_erosions) {
    }

    @Override
    public void erode() {
    }

    @Override
    public void dilate(int nb_dilatations) {
    }

    public int getNbColsLeft() {
        for (Object object : blocks) {
            System.out.println("ouba" + object);
            if (object instanceof Montage) {
                Montage tmp = ((Montage) object);
                int count;
                count = tmp.getNb_rows();
                return count;
            }
        }
        return 0;
    }

    private Rectangle2D getPosition(int beginpos, int endpos, int ROWS_OR_COLS, int LEFT_OR_RIGHT) {
        Rectangle2D masterRect = null;
        int cur_count = 1;
        int last = blocks.size();
        int counter = 0;
        loop1:
        for (Object object : blocks) {
            /*
             * dirty fix for position  on the right --> jump to the last montage to count cols
             */
            /*
             * if pos is right we go to the last block
             */
            if (LEFT_OR_RIGHT == RIGHT) {
                counter++;
                if (counter != last) {
                    continue;
                }
            }
            if (object instanceof Montage) {
                Montage tmp = ((Montage) object);
                int count;
                if (ROWS_OR_COLS == FOR_ROWS) {
                    count = tmp.getNb_cols();
                } else {
                    count = tmp.getNb_rows();
                }
                if (ROWS_OR_COLS == FOR_COLS) {
                    Rectangle2D r = null;
                    Rectangle2D r2 = tmp.getPanelPosition(beginpos - 1, 0);
                    Rectangle2D r3 = tmp.getPanelPosition(endpos - 1, 0);
                    if (r2 != null) {
                        r = r2;
                    }
                    if (r3 != null) {
                        r = r3;
                    }
                    if (r != null) {
                        if (r2 != null) {
                            r = r.createUnion(r2);
                        }
                        if (r3 != null) {
                            r = r.createUnion(r3);
                        }
                        return r;
                    }
                    return r;
                } else {
                    loop2:
                    for (int i = 1; i < count + 1; i++) {
                        if (cur_count > endpos) {
                            break loop1;
                        }
                        if (cur_count < beginpos) {
                            cur_count++;
                            continue loop2;
                        }
                        Rectangle2D r = null;
                        if (ROWS_OR_COLS == FOR_ROWS) {
                            r = tmp.getPanelPosition(0, i - 1);
                        }
                        if (masterRect == null && r != null) {
                            masterRect = r;
                        } else {
                            if (r != null) {
                                masterRect = masterRect.createUnion(r);
                            }
                        }
                        cur_count++;
                    }
                }
            }
        }
        return masterRect;
    }

    /**
     *
     * @param beginpos
     * @param endpos
     * @return the rectangle bounding images at position begin pos, end pos,
     * used to add a text bar on top of those images
     */
    public Rectangle2D getPositionForCols(int beginpos, int endpos, int LEFT_OR_RIGHT) {
        return getPosition(beginpos, endpos, FOR_COLS, LEFT_OR_RIGHT);
    }

    /**
     *
     * @param beginpos
     * @param endpos
     * @return the rectangle bounding images at position begin pos, end pos,
     * used to add a text bar on top of those images
     */
    public Rectangle2D getPositionForRows(int beginpos, int endpos) {
        return getPosition(beginpos, endpos, FOR_ROWS, LEFT);
    }

    @Override
    public void dilate() {
    }

    /**
     *
     * @return the width of the row in cm
     */
    public double getWidth_in_cm() {
        return width_in_cm;
    }

    /**
     * Sets the width of the row in cm
     *
     * @param width_in_cm
     */
    public void setWidth_in_cm(double width_in_cm) {
        this.width_in_cm = width_in_cm;
    }

    /**
     *
     * @return the height of the row in cm
     */
    public double getHeight_in_cm() {
        return height_in_cm;
    }

    /**
     * Sets the height of the row in cm
     *
     * @param height_in_cm
     */
    public void setHeight_in_cm(double height_in_cm) {
        this.height_in_cm = height_in_cm;
    }

    /**
     *
     * @return the width of the row in pixels
     */
    public double getWidth_in_px() {
        return width_in_px;
    }

    /**
     * Sets the width of the row in px
     *
     * @param width_in_px
     */
    public void setWidth_in_px(double width_in_px) {
        this.width_in_px = width_in_px;
    }

    /**
     *
     * @return the height of the row in px
     */
    public double getHeight_in_px() {
        return height_in_px;
    }

    /**
     * Sets the height of the row in px
     *
     * @param height_in_px
     */
    public void setHeight_in_px(double height_in_px) {
        this.height_in_px = height_in_px;
    }

    /**
     *
     * @return the resolution of the row (typically 72dpi)
     */
    public double getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution of the row
     *
     * @param resolution
     */
    public void setResolution(double resolution) {
        this.resolution = resolution;
        for (Object object : blocks) {
            ((Montage) object).setResolution(resolution);
        }
    }

    /**
     * make all images contained in the row ready for serialization (necessary
     * to save images)
     */
    public void getExtraTextreadyForSerialization() {
        /**
         * removed this cause this was redundant
         */
//        for (Object object : blocks) {
//            ((Montage) object).getExtraTextreadyForSerialization();
//        }
        if (extras != null) {
            for (Object object : extras.values()) {
                if (object instanceof TopBar) {
                    ((TopBar) object).getTextreadyForSerialization();
                }
            }
        }
    }

    /**
     * Recreate styleDoc from html-like string (necessary when a .figur file is
     * reloaded)
     */
    public void recreateStyledDocForExtras() {
        /**
         * removed this cause this was redundant
         */
//        for (Object object : blocks) {
//            ((Montage) object).recreateStyledDocForExtras();
//        }
        if (extras != null) {
            for (Object object : extras.values()) {
                if (object instanceof TopBar) {
                    ((TopBar) object).recreateStyledDoc();
                }
            }
        }
    }
    
    /**
     *
     * @return the space between rows
     */
    public double getSpace_between_panels() {
        return space_between_blocks;
    }

    /**
     * Sets the space between rows
     *
     * @param space_between_blocks
     */
    public void setSpaceBetweenPanels(double space_between_blocks) {
        this.space_between_blocks = space_between_blocks;
    }

    /**
     * Strips all text from the current montage
     */
    public void removeAllText() {
        /*
         * we get rid of all the text
         */
        for (Object object : blocks) {
            ((Montage) object).removeAllText();
        }
    }

    /**
     * removes all scalebars from the current montage
     */
    public void removeAllBars() {
        /*
         * we get rid of all the bars
         */
        for (Object object : blocks) {
            ((Montage) object).removeAllBars();
        }
    }

    /**
     * removes all annotations from images
     */
    public void removeAllAnnotations() {
        for (Object object : blocks) {
            ((Montage) object).removeAllAnnotations();
        }
    }

    public void capitalizeText(String capitalization) {
        for (Object object : blocks) {
            ((Montage) object).capitalizeText(capitalization);
        }
    }

    public void capitalizeLetter(String capitalization) {
        for (Object object : blocks) {
            ((Montage) object).capitalizeLetter(capitalization);
        }
    }

//    public void italicizeSingleLetters() {
//        for (Object object : blocks) {
//            ((Montage) object).italicizeSingleLetters();
//        }
//    }
//
//    public void reformatNumbersWithUnits() {
//        for (Object object : blocks) {
//            ((Montage) object).reformatNumbersWithUnits();
//        }
//    }
//
//    public void replaceMinusByEN_DASH() {
//        for (Object object : blocks) {
//            ((Montage) object).replaceMinusByEN_DASH();
//        }
//    }
//
//    public void placePercentSignAtTheEnd() {
//        for (Object object : blocks) {
//            ((Montage) object).placePercentSignAtTheEnd();
//        }
//    }
//
//    public void replacePlainTextDegrees() {
//        for (Object object : blocks) {
//            ((Montage) object).replacePlainTextDegrees();
//        }
//    }
//
//    public void replaceMutliplicationBySymbol() {
//        for (Object object : blocks) {
//            ((Montage) object).replaceMutliplicationBySymbol();
//        }
//    }
    /**
     * change row orientation (if the row was horizontal we make it vertical and
     * vice versa
     */
    public void changeOrientation() {
        if (CURRENT_ORIENTATION == FLOW_HORIZONTAL) {
            CURRENT_ORIENTATION = FLOW_VERTICAL;
        } else {
            CURRENT_ORIENTATION = FLOW_HORIZONTAL;
        }
        pack(space_between_blocks);
    }

    /**
     *
     * @return true if the row contains R graphs
     */
    public boolean requiresRsession() {
        for (Object object : blocks) {
            if (((Montage) object).requiresRsession()) {
                return true;
            }
        }
        return false;
    }

    public void reloadAfterSerialization() {
        for (Object object : blocks) {
            ((Montage) object).reloadAfterSerialization();
        }
    }

    public BufferedImage getFormattedImageWithoutTranslation() {
        double x = rec2d.x;
        double y = rec2d.y;
        rec2d.x = 0;
        rec2d.y = 0;
        BufferedImage out = null;
        try {
            setFirstCorner();
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
            setFirstCorner(new Point2D.Double(x, y));
            return out;
        }
    }

    public void checkGraph(JournalParameters jp) {
        wasCheckedForGraphs = true;
        for (Object object : blocks) {
            ((Montage) object).checkGraph(jp);
        }
    }

    public void checkStyle() {
        wasCheckedForStyle = true;
        for (Object object : blocks) {
            ((Montage) object).checkStyle();
        }
        boolean changed = false;
        if (extras != null) {
            for (Object object : extras.values()) {
                if (object instanceof TopBar) {
                    if (((TopBar) object).checkStyle()) {
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            /**
             * if the horizontal or vertical text bars have changed then we need
             * to update the row and to resize it
             */
            updateExtras();
            setToWidth();
        }
    }

    public void checkText(JournalParameters jp) {
        wasCheckedForText = true;
        for (Object object : blocks) {
            ((Montage) object).checkText(jp);
        }
        boolean changed = false;
        if (extras != null) {
            for (Object object : extras.values()) {
                if (object instanceof TopBar) {
                    if (((TopBar) object).checkText(jp)) {
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            /**
             * if the horizontal or vertical text bars have changed then we need
             * to update the row and to resize it
             */
            updateExtras();
            setToWidth();
        }
    }

    public void checklineArts(float objectsStrokeSize, boolean isIllustrator) {
        wasCheckedForLineArts = true;
        for (Object object : blocks) {
            ((Montage) object).checklineArts(objectsStrokeSize, isIllustrator);
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
                sameHeight();
                setToWidth(Converter.cmToPx72dpi(size));
                setWidth_in_px(Converter.cmToPx72dpi(size));
                setWidth_in_cm(size);
                sameHeight();
                setToWidth();
            }
        }
    }

    public void checkFonts(JournalParameters jp) {
        wasCheckedForFonts = true;
        for (Object object : blocks) {
            ((Montage) object).checkFonts(jp);
        }
        //TODO --> check also font for extra
        boolean changed = false;
        if (extras != null) {
            for (Object object : extras.values()) {
                if (object instanceof TopBar) {
                    if (((TopBar) object).checkFonts(jp)) {
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            /**
             * if the horizontal or vertical text bars have changed then we need
             * to update the row and to resize it
             */
            updateExtras();
            setToWidth();
        }
    }

    public boolean containsStrictlyMyPlotVector() {
        for (Object object : blocks) {
            if (((Montage) object).containsStrictlyMyPlotVector()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsStrictlyImageVector() {
        for (Object object : blocks) {
            if (((Montage) object).containsStrictlyImageVector()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRotable() {
        return false;
    }

//    public void markAllPanelsAsInFigure(boolean bool) {
//        for (Object object : blocks) {
//            if (object instanceof Montage) {
//                ((Montage) object).setIsUsedInAFigure(bool);
//            }
//        }
//    }
    @Override
    public String toString() {
        return getFormula();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {

//        ArrayList<String> list = new LoadListeToArrayList().apply("/list.lst");
        long start_time = System.currentTimeMillis();

//        ArrayList<Object> test = new ArrayList<Object>();
//        MyImage2D.Double test1 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series010.png");
//        MyImage2D.Double test2 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series014.png");
//        MyImage2D.Double test3 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series015.png");
//        MyImage2D.Double test4 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png");
//
//        MyImage2D.Double test10 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series010.png");
//        MyImage2D.Double test20 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series014.png");
//        MyImage2D.Double test30 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series015.png");
//        MyImage2D.Double test40 = new MyImage2D.Double(0, 0, "D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png");
//
//        Montage b1 = new Montage(test1);
//        Montage b2 = new Montage(test2);
//        Montage b3 = new Montage(test3);
//        Montage b4 = new Montage(test4);
//
//        ArrayList<Object> table = new ArrayList<Object>();
//        table.add(test10);
//        table.add(test20);
//        table.add(test30);
//        table.add(test40);
//
//        Montage b5 = new Montage(table, 2, 2, true, 3);
//
//        test.add(b5);
//
//        test.add(b1);
//        test.add(b2);
//        test.add(b3);
//        test.add(b4);
//        Row test_row = new Row(test, 1);
        String macro = "<Row data-spaceBetweenMontages=\"1.0\" data-widthInPx=\"1024\" >\n"
                + "	<Montage data-nbCols=\"2\" data-nbRows=\"2\" data-order=\"comb\" data-spaceBetweenImages=\"3\" data-widthInPx=\"169.6637398373984\" >\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series010.png\" />\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series014.png\" />\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series015.png\" />\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png\" />\n"
                + "	</Montage>\n"
                + "	<Montage data-nbCols=\"1\" data-nbRows=\"1\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"84.58406504065039\" >\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series010.png\" />\n"
                + "	</Montage>\n"
                + "	<Montage data-nbCols=\"1\" data-nbRows=\"1\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"84.58406504065039\" >\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series014.png\" />\n"
                + "	</Montage>\n"
                + "	<Montage data-nbCols=\"1\" data-nbRows=\"1\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"84.58406504065039\" >\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series015.png\" />\n"
                + "	</Montage>\n"
                + "	<Montage data-nbCols=\"1\" data-nbRows=\"1\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"84.58406504065039\" >\n"
                + "		<img data-src=\"D:/sample_images_PA/trash_test_mem/mini/focused_Series016.png\" />\n"
                + "	</Montage>\n"
                + "</Row>";

        Row test_row = new Row(macro);

//        test_row.setToWidth(512);
        Rectangle2D rec2d = test_row.getBounds2D();

        BufferedImage tmp = new BufferedImage((int) (rec2d.getX() + rec2d.getWidth() + 1.), (int) (rec2d.getY() + rec2d.getHeight() + 1.), BufferedImage.TYPE_INT_RGB);

        System.out.println(rec2d);

        Graphics2D g2d = tmp.createGraphics();
        test_row.drawAndFill(g2d);
        g2d.dispose();

        System.out.println(test_row.produceMacroCode(1));

//        SaverLight.popJ(tmp);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

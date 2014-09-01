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

import Commons.CommonClassesLight;
import Commons.SaverLight;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//TODO replace parsed string by regex
/**
 * Figure is a class that handles several rows (themselves containing several
 * montages)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Figure extends MyRectangle2D implements Drawable, MagnificationChangeLoggable {

    /**
     * Variables
     */
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public int ALIGNMENT = ALIGN_LEFT;
    public double spaceBetweenPanels = 1;
    ArrayList<Object> rows = new ArrayList<Object>();
    String masterTagOpen = "<Figure>";
    String masterTagClose = "</Figure>";

    /**
     * Constructor
     *
     * @param rows list of rows
     * @param space_between_blocks space between the rows
     */
    public Figure(ArrayList<Object> rows, double space_between_blocks) {
        rec2d = new Rectangle2D.Double();
        this.rows = rows;
        this.spaceBetweenPanels = space_between_blocks;
        packRowsInY(rows, space_between_blocks);
        updateMasterRect();
    }

    /**
     * Constructor that rebuilds a figure out of some ImageJ/FIJI macro code
     *
     * @param macro
     */
    public Figure(String macro) {
        ArrayList<String> rows_macros = new ArrayList<String>();
        while (macro.toLowerCase().contains("/row")) {
            macro = CommonClassesLight.strCutRightFisrt(macro, "Row");
            String current_montage = CommonClassesLight.strCutLeftFirst(macro, "</Row");//tag de fin
            macro = CommonClassesLight.strCutRightFisrt(macro, "</Row");
            if (current_montage.contains("data-spaceBetweenMontages")) {
                rows_macros.add(current_montage);
            }
        }
        for (String string : rows_macros) {
            Row r = new Row(string);
            rows.add(r);
        }
        rec2d = new Rectangle2D.Double(0, 0, 512, 512);
        packRowsInY(rows, spaceBetweenPanels);
        updateMasterRect();
    }

    /**
     *
     * @return the rows contained in the figure
     */
    public ArrayList<Object> getRows() {
        return rows;
    }

    /**
     *
     * @return some IJ macro code necessary and sufficient to recreate the
     * current figure
     */
    public String produceMacroCode() {
        String macro_code = "";
        macro_code += masterTagOpen;
        macro_code += "\n";
        for (Object object : rows) {
            if (object instanceof Row) {
                macro_code += ((Row) object).produceMacroCode(1);
            }
        }
        macro_code += masterTagClose + "\n";
        return macro_code;
    }

    /*
     * Creates the bounding rectangle surrounding all rows
     */
    private void updateMasterRect() {
        rec2d = null;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double max_y = Integer.MIN_VALUE;
        boolean found = false;
        if (rows != null && !rows.isEmpty()) {
            for (Object object : rows) {
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
        if (!found) {
            rec2d = new Rectangle2D.Double();
            return;
        }
        rec2d = new Rectangle2D.Double(min_x, min_y, max_x - min_x, max_y - min_y);
    }

    /**
     * Packs rows in the y direction
     *
     * @param rows
     * @param space_between_rows_in_px
     */
    private void packRowsInY(ArrayList<Object> rows, double space_between_rows_in_px) {
        if (rows != null && !rows.isEmpty()) {
            for (Object object : rows) {
                ((Row) object).setFirstCorner(new Point2D.Double());
            }
        }
        ComplexShapeLight cs = new ComplexShapeLight(rows);
        cs.packY(space_between_rows_in_px);
        updateMasterRect();
    }

    /**
     * Aligns rows
     *
     * @param rows
     */
    public void alignRows(ArrayList<Object> rows) {
        ComplexShapeLight cs = new ComplexShapeLight(rows);
        switch (ALIGNMENT) {
            case ALIGN_LEFT:
                cs.alignLeft();
                break;
            case ALIGN_RIGHT:
                cs.alignRight();
                break;
            case ALIGN_CENTER:
                cs.alignCenterX();
                break;
        }
        updateMasterRect();
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        for (Object object : rows) {
            ((Row) object).drawAndFill(g2d);
        }
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    @Override
    public String logMagnificationChanges(double zoom) {
        String finalLog = "";
        if (rows != null && !rows.isEmpty()) {
            for (Object object : rows) {
                if (object != null) {
                    if (object instanceof Row) {
                        finalLog += ((Row) object).logMagnificationChanges(zoom) + "\n";
                    }
                }
            }
        }
        return finalLog;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {

//        MyImage2D.Double m2d = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/mini/focused_Series010.png");
//        MyImage2D.Double m2d2 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/mini/focused_Series014.png");
//        MyImage2D.Double m2d3 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/mini/focused_Series016.png");
//        MyImage2D.Double m2d4 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/mini/focused_Series015.png");
//
//        ArrayList<Object> row1 = new ArrayList<Object>();
//        row1.add(m2d);
//        row1.add(m2d2);
//
//        ArrayList<Object> roww = new ArrayList<Object>();
//
//        Montage block = new Montage(row1);
//        roww.add(block);
//        Row r1 = new Row(roww, 1);
//        ArrayList<Object> row2 = new ArrayList<Object>();
//        row2.add(m2d3);
//        Row r2 = new Row(new Montage(row2), 1);
//        ArrayList<Object> row3 = new ArrayList<Object>();
//        row3.add(m2d4);
//        Row r3 = new Row(new Montage(row3), 1);
//
//        ArrayList<Object> rows = new ArrayList<Object>();
//        rows.add(r1);
//        rows.add(r2);
//        rows.add(r3);
        String macro = "<Figure>\n"
                + "	<Row data-spaceBetweenMontages=\"1.0\" data-widthInPx=\"512.0\" >\n"
                + "		<Montage data-nbCols=\"2\" data-nbRows=\"2\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"512.0\" >\n"
                + "			<img data-src=\"/D/sample_images_PA/trash_test_mem/mini/focused_Series010.png\" />\n"
                + "			<img data-src=\"/D/sample_images_PA/trash_test_mem/mini/focused_Series014.png\" />\n"
                + "		</Montage>\n"
                + "	</Row>\n"
                + "	<Row data-spaceBetweenMontages=\"1.0\" data-widthInPx=\"512.0\" >\n"
                + "		<Montage data-nbCols=\"2\" data-nbRows=\"3\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"512.0\" >\n"
                + "			<img data-src=\"/D/sample_images_PA/trash_test_mem/mini/focused_Series016.png\" />\n"
                + "		</Montage>\n"
                + "	</Row>\n"
                + "	<Row data-spaceBetweenMontages=\"1.0\" data-widthInPx=\"512.0\" >\n"
                + "		<Montage data-nbCols=\"2\" data-nbRows=\"3\" data-order=\"comb\" data-spaceBetweenImages=\"1\" data-widthInPx=\"512.0\" >\n"
                + "			<img data-src=\"/D/sample_images_PA/trash_test_mem/mini/focused_Series015.png\" />\n"
                + "		</Montage>\n"
                + "	</Row>\n"
                + "</Figure>";

        Figure rh = new Figure(macro);
//        Figure rh = new Figure(rows, 1);

        Rectangle2D rec2d = rh.getBounds2D();

        BufferedImage tmp = new BufferedImage((int) (rec2d.getX() + rec2d.getWidth() + 1.), (int) (rec2d.getY() + rec2d.getHeight() + 1.), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = tmp.createGraphics();
        rh.drawAndFill(g2d);
        g2d.dispose();

        System.out.println(rh.produceMacroCode());

        SaverLight.popJ(tmp);

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        long start_time = System.currentTimeMillis();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

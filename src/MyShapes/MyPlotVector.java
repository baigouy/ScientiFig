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

import R.GeomPlot;
import R.RSession.MyRsession;
import Commons.CommonClassesLight;
import Commons.SaverLight;
import R.GGtitles;
import R.ScaleColor;
import R.String.RLabel;
import R.ThemeGraph;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * MyPlotVector is a class that extends MyImageVector but was designed to
 * specifically and dynamically handle R graphs
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class MyPlotVector extends MyImageVector implements Drawable, Serializable, Namable, Transformable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6176423769051697816L;
    public boolean use_non_custom_code = false;
    public String Rcommand;
    public String source_file_path;
    public int sheetnb = 1;
    transient static MyRsession rsession;
    byte[] source_file;
    ThemeGraph theme;
    /**
     * temp file that contains the source data used to replot
     */
    transient File tmp_file;
    public GraphFont fonts;
    public String textSeparator;
    public String textDelimtor;
    public String decimal;
    /**
     * here we have all the data for the plots --> to be able to reload the file
     */
    /**
     * contains the main data
     */
    public ArrayList<Object> data = new ArrayList<Object>();
    /**
     * contains the plots
     */
    public ArrayList<Object> plots = new ArrayList<Object>();
    /**
     * contains extras for the different plots such as stats, ....
     */
    public ArrayList<Object> extras = new ArrayList<Object>();
    private static final int TYPE_POINT = 1;
    private static final int TYPE_LINE = 0;
    /**
     * below are the graph labels (that contain text, maths and/or units), new
     * addition to be more flexible with graph text modifications more flexible
     * (for example to set the text to upper case, to format the units, ...<BR>
     * It partially depreacted the ggtitle class
     */
    public RLabel titleLabel;
    RLabel xAxisLabel;
    RLabel yAxisLabel;
    RLabel legendLabel;

    public void updatePlot() {
        if (use_non_custom_code) {
            updateGGTitle();
            Rcommand = "";
            for (Object f : data) {
                Rcommand += f.toString() + "\n + ";
            }
            for (Object f : plots) {
                Rcommand += f.toString() + "\n + ";
            }
            ArrayList<Object> toRemove = new ArrayList<Object>();
            for (Object f : extras) {
                if (!(f instanceof ThemeGraph)) {
                    if (!f.toString().trim().equals("")) {
                        Rcommand += f.toString() + "\n + ";
                    }
                } else {
                    toRemove.add(f);
                }
            }
            extras.removeAll(toRemove);
            if (Rcommand.endsWith("\n + ")) {
                Rcommand = Rcommand.substring(0, Rcommand.length() - 4);
            }
        }
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            return;
        }
        updateSVG(source_data_file, initial_elems_width, initial_elems_height, true);
    }

    /**
     * a double precision MyPlotVector
     */
    public static class Double extends MyPlotVector implements Serializable, Drawable {

        public static final long serialVersionUID = 1649652121660999660L;

        /**
         * Constructor
         *
         * @param r the current rsession object
         * @param source_data_file the source file (typically an .xls or a .txt
         * file)
         * @param Rcommand the plot command
         * @param sheetnb the number of the sheet interest in an excel sheet
         * @param textSeparator the text separator (typically ',' ';', ...)
         * @param textDelimtor the text delimitor (typically '"')
         * @param decimal the decimal delimitor (, or .)
         * @param fonts the set of graph fonts
         * @param use_non_custom_code defines if custom code should be used or
         * not
         */
        public Double(MyRsession r, String source_data_file, String Rcommand, int sheetnb, String textSeparator, String textDelimtor, String decimal, GraphFont fonts, boolean use_non_custom_code, ThemeGraph theme) {
            this.Rcommand = Rcommand;
            this.sheetnb = sheetnb;
            this.fonts = fonts;
            this.textSeparator = textSeparator;
            this.textDelimtor = textDelimtor;
            this.decimal = decimal;
            this.theme = theme;
            try {
                r.eval("library(xlsx)");
                r.eval("library(ggplot2)");
                if (source_data_file.toLowerCase().endsWith(".xls") || source_data_file.toLowerCase().endsWith(".xlsx")) {
                    r.loadXLS("curDataFigR", source_data_file, sheetnb);
                }
                if (source_data_file.toLowerCase().endsWith(".txt") || source_data_file.toLowerCase().endsWith(".csv")) {
                    r.loadCSVorTXT("curDataFigR", source_data_file, textSeparator, textDelimtor, decimal);
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
            super.Rcommand = Rcommand;
            this.document = r.getPreviewSVG(Rcommand, fonts, theme, 512, 512);
            super.createDocStuff();
            Rectangle2D r2 = getSVGBounds2D();
            initial_elems_width = r2.getWidth();
            initial_elems_height = r2.getHeight();
            setFirstCorner(new Point2D.Double(0, 0));
            doc2String(true);
            this.fullName = source_data_file;
            this.shortName = CommonClassesLight.getName(new File(source_data_file).getName());
            this.source_file_path = source_data_file;
            file2BytesArray(source_data_file);
            this.use_non_custom_code = use_non_custom_code;
        }

        /**
         * Constructor that creates a MyPlotVector out of another one
         *
         * @param myel
         */
        public Double(MyPlotVector myel) {
            this.rec2d = myel.rec2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.opacity = myel.opacity;
            this.bimg = myel.bimg;
            this.letter = myel.letter;
            this.scale_bar_size_in_px_of_the_real_image = myel.scale_bar_size_in_px_of_the_real_image;
            this.scale_bar_text = myel.scale_bar_text;
            this.lower_left_text = myel.lower_left_text;
            this.lower_right_text = myel.lower_right_text;
            this.upper_right_text = myel.upper_right_text;
            this.upper_left_text = myel.upper_left_text;
            this.scale = myel.scale;
            this.SCALE_BAR_STROKE_SIZE = myel.SCALE_BAR_STROKE_SIZE;
            this.fullName = myel.fullName;
            this.shortName = myel.shortName;
            this.scalebarColor = myel.scalebarColor;
            this.left_crop = myel.left_crop;
            this.right_crop = myel.right_crop;
            this.up_crop = myel.up_crop;
            this.down_crop = myel.down_crop;
            this.associatedObjects = myel.cloneAssociatedObjects();
            this.theta = myel.theta;
            this.inset = myel.inset;
            this.INSET_POSITION = myel.INSET_POSITION;
            this.scale_barSize_in_pixels_PIP = myel.scale_barSize_in_pixels_PIP;
            this.scale = myel.scale;
            this.scale_bar_size_in_unit = myel.scale_bar_size_in_unit;
            this.size_of_one_px_in_unit = myel.size_of_one_px_in_unit;
            this.scalebarColor = myel.scalebarColor;
            this.gelBorderColor = myel.gelBorderColor;
            this.gelBorderSize = myel.gelBorderSize;
            this.Rcommand = myel.Rcommand;
            this.document = myel.document;
            this.init_transX = myel.init_transX;
            this.init_transY = myel.init_transY;
            this.rec2d = myel.rec2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.opacity = myel.opacity;
            this.bimg = myel.bimg;
            this.letter = myel.letter;
            this.scale_bar_size_in_px_of_the_real_image = myel.scale_bar_size_in_px_of_the_real_image;
            this.scale_bar_text = myel.scale_bar_text;
            this.lower_left_text = myel.lower_left_text;
            this.lower_right_text = myel.lower_right_text;
            this.upper_right_text = myel.upper_right_text;
            this.upper_left_text = myel.upper_left_text;
            this.scale = myel.scale;
            this.SCALE_BAR_STROKE_SIZE = myel.SCALE_BAR_STROKE_SIZE;
            this.fullName = myel.fullName;
            this.shortName = myel.shortName;
            this.source_file_path = myel.source_file_path;
            this.source_file = myel.source_file;
            this.tmp_file = myel.tmp_file;
            this.sheetnb = myel.sheetnb;
            this.use_non_custom_code = myel.use_non_custom_code;
            /*
             * now we copy the labels
             */
            this.titleLabel = myel.getTitleLabel();
            this.xAxisLabel = myel.getxAxisLabel();
            this.yAxisLabel = myel.getyAxisLabel();
            this.legendLabel = myel.getLegendLabel();
        }

        /**
         * Constructor
         *
         * @param source_data_file the source file (typically an .xls or a .txt
         * file)
         * @param Rcommand the plot command
         * @param sheetnb the number of the sheet interest in an excel sheet
         * @param textSeparator the text separator (typically ',' ';', ...)
         * @param textDelimtor the text delimitor (typically '"')
         * @param decimal the decimal delimitor (, or .)
         * @param fonts the set of graph fonts
         * @param use_non_custom_code defines if custom code should be used or
         * not
         */
        public Double(String source_data_file, String Rcommand, int sheetnb, String textSeparator, String textDelimtor, String decimal, GraphFont fonts, boolean use_non_custom_code, ThemeGraph theme) {
            this(rsession, source_data_file, Rcommand, sheetnb, textSeparator, textDelimtor, decimal, fonts, use_non_custom_code, theme);
        }

        /**
         *
         * @return the R plot command
         */
        public String getRcommand() {
            return Rcommand;
        }
    }

    public void updateFillColors(ArrayList<Integer> replacementColorColors) {
        boolean found = false;
        for (Object o : extras) {
            if ((o instanceof ScaleColor) && ((ScaleColor) o).getType().equals("fill")) {
                ((ScaleColor) o).setColors(replacementColorColors);
                found = true;
            }
        }
        if (!found) {
            ScaleColor sc = new ScaleColor("fill");
            sc.setColors(replacementColorColors);
            extras.add(sc);
        }
    }

    public void updateColorColors(ArrayList<Integer> replacementColorColors) {
        boolean found = false;
        for (Object o : extras) {
            if ((o instanceof ScaleColor) && !((ScaleColor) o).getType().equals("fill")) {
                ((ScaleColor) o).setColors(replacementColorColors);
                found = true;
            }
        }
        if (!found) {
            ScaleColor sc = new ScaleColor("color");
            sc.setColors(replacementColorColors);
            extras.add(sc);
        }
    }

    public ArrayList<Object> getPlots() {
        return plots;
    }

    public ArrayList<Object> getExtras() {
        return extras;
    }

    @Override
    public void extractImage(String name) {
        String ext = CommonClassesLight.strCutRightLast(source_file_path, ".");
        String name_no_ext = name + "/" + getShortName();
        if (!ext.startsWith(".")) {
            ext = "." + ext;
        }
        String final_name = name_no_ext + ext;
        int counter = 0;
        if (new File(final_name).exists()) {
            do {
                final_name = name_no_ext + "_" + CommonClassesLight.create_number_of_the_appropriate_size(counter++, 4) + ext;
            } while (new File(final_name).exists());
        }
        try {
            File someFile = new File(final_name);
            FileOutputStream fos2 = new FileOutputStream(someFile);
            fos2.write(source_file);
            fos2.flush();
            fos2.close();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    public RLabel getTitleLabel() {
        /*
         * retrocompatibility
         */
        if (titleLabel == null && extras != null) {
            for (Object o : extras) {
                if (o instanceof GGtitles) {
                    titleLabel = new RLabel(((GGtitles) o).getMainTitle());
                }
            }
        }
        return titleLabel;
    }

    public void setTitleLabel(RLabel titleLabel) {
        this.titleLabel = titleLabel;
    }

    public ThemeGraph getTheme() {
        if (theme != null) {
            return theme;
        }
        if (extras == null || extras.isEmpty()) {
            return null;
        }
        for (Object obj : extras) {
            if (obj instanceof ThemeGraph) {
                return (ThemeGraph) obj;
            }
        }
        return null;
    }

    public void setTheme(ThemeGraph theme) {
        this.theme = theme;
    }

    public RLabel getxAxisLabel() {
        /*
         * retrocompatibility
         */
        if (xAxisLabel == null && extras != null) {
            for (Object o : extras) {
                if (o instanceof GGtitles) {
                    xAxisLabel = new RLabel(((GGtitles) o).getXlabel());
                }
            }
        }
        return xAxisLabel;
    }

    public void setxAxisLabel(RLabel xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public RLabel getyAxisLabel() {
        if (yAxisLabel == null && extras != null) {
            for (Object o : extras) {
                if (o instanceof GGtitles) {
                    yAxisLabel = new RLabel(((GGtitles) o).getYlabel());
                }
            }
        }
        return yAxisLabel;
    }

    public void setyAxisLabel(RLabel yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public RLabel getLegendLabel() {
        if (legendLabel == null && extras != null) {
            for (Object o : extras) {
                if (o instanceof GGtitles) {
                    legendLabel = new RLabel(((GGtitles) o).getLegendlabel());
                }
            }
        }
        return legendLabel;
    }

    public void setLegendLabel(RLabel legendLabel) {
        this.legendLabel = legendLabel;
    }

    @Override
    public void getTextreadyForSerialization() {
        super.getTextreadyForSerialization();

        if (legendLabel != null) {
            legendLabel.getTextreadyForSerialization();
        }
        if (titleLabel != null) {
            titleLabel.getTextreadyForSerialization();
        }
        if (xAxisLabel != null) {
            xAxisLabel.getTextreadyForSerialization();
        }
        if (yAxisLabel != null) {
            yAxisLabel.getTextreadyForSerialization();
        }
    }

    @Override
    public void recreateStyledDoc() {
        super.recreateStyledDoc();

        if (legendLabel != null) {
            legendLabel.recreateStyledDoc();
        }
        if (titleLabel != null) {
            titleLabel.recreateStyledDoc();
        }
        if (xAxisLabel != null) {
            xAxisLabel.recreateStyledDoc();
        }
        if (yAxisLabel != null) {
            yAxisLabel.recreateStyledDoc();
        }
    }

    public void updateGGTitle() {
        GGtitles titles = new GGtitles(titleLabel, xAxisLabel, yAxisLabel, legendLabel);
        String oldGGtitle = null;
        if (extras != null) {
            ArrayList<Object> toRemove = new ArrayList<Object>();
            for (Object f : extras) {
                if (f instanceof GGtitles) {
                    oldGGtitle = ((GGtitles) f).toString();
                    toRemove.add(f);
                }
            }
            extras.removeAll(toRemove);
            extras.add(titles);
        } else {
            extras = new ArrayList<Object>();
            extras.add(titles);
        }
        if (use_non_custom_code && oldGGtitle != null) {
            Rcommand = Rcommand.replace(oldGGtitle, titles.toString());
        } else if (use_non_custom_code && oldGGtitle == null) {
            Rcommand += titles.toString();
        }
    }

    private boolean checkStroke(float desiredStrokeSize, int TYPE) {
        if (use_non_custom_code) {
            for (Object f : plots) {
                GeomPlot g = ((GeomPlot) f);
                boolean found = false;
                String size = null;
                switch (TYPE) {
                    case TYPE_LINE:
                        if (g.isLineMode()) {
                            size = g.getSize();
                            found = true;
                        }
                        break;
                    case TYPE_POINT:
                        if (g.isPointMode()) {
                            size = g.getSize();
                            found = true;
                        }
                        break;
                }
                if (!found) {
                    continue;
                }
                /*
                 * size not set --> we must force update the stroke
                 */
                if (size == null) {
                    return false;
                }
                float sizef = CommonClassesLight.String2Float(size);
                if (Math.abs(sizef - desiredStrokeSize) > 0.05) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPointSize(float desiredStrokeSize) {
        return checkStroke(desiredStrokeSize, TYPE_POINT);
    }

    public boolean checkStrokeSize(float desiredStrokeSize) {
        return checkStroke(desiredStrokeSize, TYPE_LINE);
    }

    /**
     *
     * @return the graph font settings
     */
    public GraphFont getFonts() {
        return fonts;
    }

    private void setStrokeSize(float size, int TYPE) {
        if (size <= 0) {
            return;
        }
        if (use_non_custom_code) {
            if (plots != null) {
                int pos = 0;
                for (Object object : plots) {
                    GeomPlot gp = (GeomPlot) object;
                    String old_val = gp.toString();
                    boolean found = false;
                    switch (TYPE) {
                        case TYPE_LINE:
                            if (gp.isLineMode()) {
                                gp.changeLineSize(size);
                                found = true;
                            }
                            break;
                        case TYPE_POINT:
                            if (gp.isPointMode()) {
                                gp.changePointSize(size);
                                found = true;
                            }
                            break;
                    }
                    if (!found) {
                        continue;
                    }
                    plots.set(pos, gp);
                    Rcommand = Rcommand.replace(old_val, gp.toString());
                    pos++;
                }
                String source_data_file = getTempFile();
                if (source_data_file == null || !CommonClassesLight.isRReady()) {
                    return;
                }
                updateSVG(source_data_file, initial_elems_width, initial_elems_height, true);
            }
        }
    }

    public void setPointSize(float pointSize) {
        setStrokeSize(pointSize, TYPE_POINT);
    }

    public void setLineStrokeSize(float lineStrokeValue) {
        setStrokeSize(lineStrokeValue, TYPE_LINE);
    }

    public void setStrokeSize(float strokeSize, float pointSize, boolean applyToLines, boolean applyToPoints) {
        if (strokeSize <= 0 && pointSize <= 0) {
            return;
        }
        if (applyToLines || applyToPoints) {
            if (use_non_custom_code) {
                boolean changed = false;
                if (plots != null) {
                    int pos = 0;
                    for (Object object : plots) {
                        GeomPlot gp = (GeomPlot) object;
                        String old_val = gp.toString();
                        if (applyToLines && strokeSize > 0) {
                            boolean ret = gp.changeLineSize(strokeSize);
                            if (ret) {
                                changed = true;
                            }
                        }
                        if (applyToLines && pointSize > 0) {
                            boolean ret = gp.changePointSize(pointSize);
                            if (ret) {
                                changed = true;
                            }
                        }
                        plots.set(pos, gp);
                        Rcommand = Rcommand.replace(old_val, gp.toString());
                        pos++;
                    }
                    /*
                     * we update the graph
                     */
                    if (!changed) {
                        return;
                    }
                    String source_data_file = getTempFile();
                    if (source_data_file == null || !CommonClassesLight.isRReady()) {
                        return;
                    }
                    updateSVG(source_data_file, initial_elems_width, initial_elems_height, true);

                }
            } else {
                /*
                 * TODO add support for custom code --> faire un mega parser de code perso --> y reflechir car c'est pas si simple a faire
                 * --> il faut que je parse le code pour recup le tag size associe au geom point ou au geom_line puis le remplacer par le truc
                 * peut etre aussi mettre la possibilite d'editer le code manuellement des plots
                 */
                if (Rcommand.contains("geom_line") || Rcommand.contains("geom_point")) {
                    System.err.println("Custom code plots not yet supported");
                }
            }
        }
    }

    /**
     * Defines the graph font settings
     *
     * @param fonts
     */
    public void setFonts(GraphFont fonts) {
        this.fonts = fonts;
    }

    /**
     * Converts a file to a byte array in order to serialize it
     *
     * @param filename
     */
    public void file2BytesArray(String filename) {
        File file = new File(filename);
        FileInputStream fis;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        source_file = null;
        try {
            fis = new FileInputStream(file);
            int read;
            while ((read = fis.read(buf)) != -1) {
                bos.write(buf, 0, read);
            }
            bos.flush();
            source_file = bos.toByteArray();

            bos.close();
            fis.close();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * Converts back a byte array to a file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void byteArrayToFile() throws FileNotFoundException, IOException {
        if (source_file == null) {
            return;
        }
        String tmp = CommonClassesLight.getTempDirectory();
        try {
            tmp_file = java.io.File.createTempFile("tmp_", "." + CommonClassesLight.strCutRightLast(source_file_path, "."), new File(tmp));
            FileOutputStream fos = new FileOutputStream(tmp_file);
            fos.write(source_file);
            fos.flush();
            fos.close();
            tmp_file.deleteOnExit();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     *
     * @return the path to the temporary source file
     */
    public String getTempFile() {
        if (CommonClassesLight.r != null) {
            MyPlotVector.rsession = CommonClassesLight.r;
        }
        try {
            if (tmp_file != null) {
                return CommonClassesLight.change_path_separators_to_system_ones(tmp_file.toString());
            } else {
                try {
                    byteArrayToFile();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                    return null;
                }
                try {
                    return CommonClassesLight.change_path_separators_to_system_ones(tmp_file.toString());
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                    return null;
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            return null;
        }
    }

    /**
     *
     * @return the current Rsession object
     */
    public static MyRsession getR() {
        return rsession;
    }

    /**
     * Sets the Rsession
     *
     * @param r
     */
    public static void setR(MyRsession r) {
        MyPlotVector.rsession = r;
    }

    /**
     * We ignore cropping for plots
     *
     * @param left
     * @param right
     * @param up
     * @param down
     */
    @Override
    public void crop(int left, int right, int up, int down) {
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setShapeWidth(width, keepAR);
            return;
        }
        double rescale = initial_elems_width / width;
        if (width != initial_elems_width) {
            loadData(source_data_file);
            updateSVG(source_data_file, width, (initial_elems_height / rescale), false);
        }
    }

    /**
     * Load any file to the R memory
     *
     * @param source_data_file
     */
    public void loadData(String source_data_file) {
        if (source_data_file.toLowerCase().endsWith(".xls") || source_data_file.toLowerCase().endsWith(".xlsx")) {
            rsession.loadXLS("curDataFigR", source_data_file, sheetnb);
        }
        if (source_data_file.toLowerCase().endsWith(".txt") || source_data_file.toLowerCase().endsWith(".csv")) {
            rsession.loadCSVorTXT("curDataFigR", source_data_file, textSeparator, textDelimtor, decimal);
        }
    }

    @Override
    public void setToWidth(double width) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setToWidth(width);
            return;
        }
        double rescale = initial_elems_width / width;
        if (initial_elems_width != width) {
            loadData(source_data_file);
            updateSVG(source_data_file, width, (initial_elems_height / rescale), false);
        }
    }

    /**
     * Force the height and the with of a graph (is possible for graphs since
     * they are dynamically replotted)
     *
     * @param max_width
     * @param max_height
     */
    public void forceWidthAndHeight(double max_width, double max_height) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setToWidth(max_width);
            super.setToHeight(max_width);
            return;
        }
        if (initial_elems_width == max_width && initial_elems_height == max_height) {
            return;
        }
        updateSVG(source_data_file, max_width, max_height, false);
    }

    void forceWidth(double final_width_of_all_images_in_the_table) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setToWidth(final_width_of_all_images_in_the_table);
            return;
        }
        if (initial_elems_width == final_width_of_all_images_in_the_table) {
            return;
        }
        updateSVG(source_data_file, final_width_of_all_images_in_the_table, initial_elems_height, false);
    }

    /**
     * Dynamically redraws the graph
     *
     * @param source_data_file
     * @param width
     * @param height
     */
    public void updateSVG(String source_data_file, double width, double height, boolean force_update) {
        if (!force_update && (width == initial_elems_width && height == initial_elems_height)) {
            return;
        }
        loadData(source_data_file);
        this.document = (rsession.getPreviewSVG(Rcommand, fonts, theme, /*(int)*/ width, /*(int)*/ height));
        createDocStuff();
        initial_elems_width = width;
        initial_elems_height = height;
        doc2String(true);
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, initial_elems_width, initial_elems_height);
        at = new AffineTransform();
        at.scale(1., 1.);
        scale = 1.;
        at.translate((cur_pos.x), (cur_pos.y));
    }

    /**
     * Force the height of the graph to 'height'
     *
     * @param height
     */
    public void forceHeight(double height) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setToHeight(height);
            return;
        }
        if (initial_elems_height == height) {
            return;
        }
        loadData(source_data_file);
        updateSVG(source_data_file, initial_elems_width, height, false);
    }

    @Override
    public void setToHeight(double height) {
        String source_data_file = getTempFile();
        if (source_data_file == null || !CommonClassesLight.isRReady()) {
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            super.setToHeight(height);
            return;
        }

        double rescale = initial_elems_height / height;
        if (initial_elems_height != height) {
            loadData(source_data_file);
            updateSVG(source_data_file, (initial_elems_width / rescale), height, false);
        }
    }

    @Override
    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        super.setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters);
        if (jp != null) {
            this.fonts = jp.getGf();
            if (use_non_custom_code) {
                if (applyToGraphs) {
                    setStrokeSize(fonts.lineSize, fonts.PointSize, applyToGraphs, changePointSize);
                }
            }
            Rectangle2D r = getSVGBounds2D();
            String source_data_file = getTempFile();
            if (source_data_file != null && CommonClassesLight.isRReady()) {
                initial_elems_width = r.getWidth();
                initial_elems_height = r.getHeight();
                this.document = (rsession.getPreviewSVG(Rcommand, fonts, theme, initial_elems_width, initial_elems_height));
                createDocStuff();
            }
        }
    }

    @Override
    public void checkStyle() {
        RLabel t = getTitleLabel();
        RLabel x = getxAxisLabel();
        RLabel y = getyAxisLabel();
        RLabel l = getLegendLabel();
        BufferedImage snap = getFormattedImageWithoutTranslation(false);
        boolean mustUpdateGraph = false;
        letter.checkStyle(snap);
        if (t.getMainText() != null) {
            if (t.getMainText().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (t.getUnit() != null) {
            if (t.getUnit().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (x.getMainText() != null) {
            if (x.getMainText().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (x.getUnit() != null) {
            if (x.getUnit().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (y.getMainText() != null) {
            if (y.getMainText().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (y.getUnit() != null) {
            if (y.getUnit().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (l.getMainText() != null) {
            if (l.getMainText().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (l.getUnit() != null) {
            if (l.getUnit().checkStyle(snap)) {
                mustUpdateGraph = true;
            }
        }
        if (mustUpdateGraph) {
            updatePlot();
        }
    }

    @Override
    public void checkText(JournalParameters jp) {
        RLabel t = getTitleLabel();
        RLabel x = getxAxisLabel();
        RLabel y = getyAxisLabel();
        RLabel l = getLegendLabel();
        BufferedImage snap = getFormattedImageWithoutTranslation(false);
        letter.checkText(jp, snap);
        boolean mustUpdateGraph = false;
        if (t.getMainText() != null) {
            if (t.getMainText().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (t.getUnit() != null) {
            if (t.getUnit().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (x.getMainText() != null) {
            if (x.getMainText().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (x.getUnit() != null) {
            if (x.getUnit().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (y.getMainText() != null) {
            if (y.getMainText().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (y.getUnit() != null) {
            if (y.getUnit().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (l.getMainText() != null) {
            if (l.getMainText().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        if (l.getUnit() != null) {
            if (l.getUnit().checkText(jp, snap)) {
                mustUpdateGraph = true;
            }
        }
        /**
         * the text has changed --> we must update the plot (it will require a
         * connexion to R)
         */
        if (mustUpdateGraph) {
            updatePlot();
        }
    }

    @Override
    public Object clone() {
        return new MyPlotVector.Double(this);
    }

    @Override
    public String logMagnificationChanges(double zoom) {
        System.out.println("the image called '" + toString() + "' is a graph, its magnification will not be calculated");
        return "";
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        System.out.println("ggplot(data=curDataFigR)\n"
                + "+ geom_line(aes(x=x, y=y.qsdqsd.sqd), linetype=2, size=0.7999999999999999, se=FALSE)\n"
                + "+ ggtitle(expression(paste(paste('dsfsdfsdfsdfsdf dsfsf sd P sdfdsf ssf sdf s T fsdfs'), ' ', paste(paste('y=',alpha,'x+',beta)), ' ', '(', paste('10',mu,'m'), ')'))) + xlab(expression(paste(paste(bold('dxfdsf sdf'),' ', italic('sfsdf'),' '['sdf'],' '^'sdf',' dsf ')))) + ylab(expression(paste(paste('sdfsdf dsfsf sdf sdf'))))".replace("ggtitle(expression(paste(paste('dsfsdfsdfsdfsdf dsfsf sd P sdfdsf ssf sdf s T fsdfs'), ' ', paste(paste('y=',alpha,'x+',beta)), ' ', '(', paste('10',mu,'m'), ')'))) + xlab(expression(paste(paste(bold('dxfdsf sdf'),' ', italic('sfsdf'),' '['sdf'],' '^'sdf',' dsf ')))) + ylab(expression(paste(paste('sdfsdf dsfsf sdf sdf'))))", "ggtitle(expression(paste(paste('AAAAAAAAAAAA'), ' ', paste(paste('y=',alpha,'x+',beta)), ' ', '(', paste('20',mu,'m'), ')'))) + xlab(expression(paste(paste(bold('dxfdsf sdf'),' ', italic('sfsdf'),' '['sdf'],' '^'sdf',' dsf ')))) + ylab(expression(paste(paste('sdfsdf dsfsf sdf sdf'))))"));

        // text 5-µm 1234µm 120 µm 10µm 10mm
        System.out.println("10 µm".replaceAll("([0-9]{1,})([^\\s-]{1,}[^ 0-9]{1,2})", "$1 $2"));
        System.out.println("Dsfsdfsdfsdfsdf dsfsf sd P sdfdsf ssf sdf s T fsdfs p".replaceAll("\\b([a-zA-Z]{1})\\s", "<html><i>$1</i>"));
        System.out.println("a".replaceAll("\\s{0,}([a-zA-Z]{1})\\s{1,}\\b", "<html><i>$1</i>"));
        System.out.println("p = 0.05".replaceAll("\\b([a-zA-Z]{1})\\s", "<html><i>$1</i>"));
        System.out.println("a".replaceAll("\\b([a-zA-Z]{1})\\s", "<html><i>$1</i>"));

        long start_time = System.currentTimeMillis();
        //ouvrir la Rsession dans common classes
        String RCommand = "ggplot(data=curDataFigR) + geom_line(aes(x=x, y=y), alpha=0.29999998) + geom_line(aes(x=x, y=y, color=type)) + facet_grid(.~type) + theme(plot.title = element_text(size=9.6))+ theme(axis.title.x = element_text(size=9.6))+ theme(axis.title.y = element_text(size=9.6))+ theme(legend.text = element_text(size=9.6))+ theme(legend.title = element_text(size=9.6))+ theme(axis.text.x = element_text(size=9.6))+ theme(axis.text.y = element_text(size=9.6))";
        MyRsession r = new MyRsession();
        MyPlotVector.setR(r);
        MyPlotVector.Double test = new MyPlotVector.Double("D:/sample_images_PA/trash_test_R/test.xlsx", RCommand, 1, "\t", "\"", ".", null, false, null);//Time_evolution_of_boundary_fraction_pAA
        BufferedImage tmp = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tmp.getWidth(); i++) {
            for (int j = 0; j < tmp.getHeight(); j++) {
                tmp.setRGB(i, j, 0xFFFFFFFF);
            }
        }
        Graphics2D g2d = tmp.createGraphics();
//        test.setFirstCorner(new Point2D.Double(256, 256));
        test.setToWidth(128);
        test.drawAndFill(g2d);
        //test.setFirstCorner(new Point2D.Double(0, 0));
//        test.setToWidth(256);
        test.setToWidth(128);
        test.drawAndFill(g2d);
        g2d.dispose();
        SaverLight.popJ(tmp);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



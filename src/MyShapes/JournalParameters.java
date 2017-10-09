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

import Checks.SFTextController;
import Commons.CommonClassesLight;
import Dialogs.JournalParametersDialog;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * JournalParameters contains all the parameters of a journal that are realted
 * to figures and graphs (font settings, stroke size, ...)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class JournalParameters implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -2616136530513522026L;
    private String path;
    private String name;
    private Font letterFont;
    private Font lowerLeftTextFont;
    private Font outterTextFont;
    private Font upperLeftTextFont;
    private Font lowerRightTextFont;
    private Font upperRightTextFont;
    private Font scaleBarTextFont;
    private String letterFontName;
    private String lowerLeftTextFontName;
    private String outterTextFontName;
    private String upperLeftTextFontName;
    private String lowerRightTextFontName;
    private String upperRightTextFontName;
    private String scaleBarTextFontName;
    private double columnSize;
    private double oneAndHalfColumn;
    private double twoColumnSizee;
    private double pageHeight;
    private float objectsStrokeSize;
    private String capitalisationOfLetter;
    /**
     * we set default dpi values to most common ones
     */
    private int colorDPI = 300;
    private int BWDPI = 600;
    /*
     * here are the graph settings
     */
    private GraphFont gf;
    /*
     * if some fonts were not found it is written in this string
     */
    private String fontErrors = "";
    /**
     * contains all the advanced text formatting rules
     */
    private ArrayList<SFTextController> regexRules = new ArrayList<SFTextController>();

    /**
     * Empty constructor
     */
    public JournalParameters() {
    }

    /**
     * Constructor
     *
     * @param name
     * @param letterFont
     * @param lowerLeftTextFont
     * @param upperLeftTextFont
     * @param lowerRightTextFont
     * @param upperRightTextFont
     * @param ScaleBarTextFont
     * @param outterTextFont
     * @param columnSize
     * @param oneAndHalfColumn
     * @param twoColumnSizee
     * @param pageHeight
     * @param gf contains the graph parameters
     */
    public JournalParameters(String name, Font letterFont, Font lowerLeftTextFont, Font upperLeftTextFont, Font lowerRightTextFont, Font upperRightTextFont, Font ScaleBarTextFont, Font outterTextFont, double columnSize, double oneAndHalfColumn, double twoColumnSizee, double pageHeight, GraphFont gf, float objectsStrokeSize, String capitalisationOfLetter, ArrayList<SFTextController> regexRules, int colorDPI, int BWDPI) {
        this.name = name;
        this.letterFont = letterFont;
        this.letterFontName = letterFont.getFamily();
        this.lowerLeftTextFont = lowerLeftTextFont;
        this.lowerLeftTextFontName = lowerLeftTextFont.getFamily();
        this.upperLeftTextFont = upperLeftTextFont;
        this.upperLeftTextFontName = upperLeftTextFont.getFamily();
        this.lowerRightTextFont = lowerRightTextFont;
        this.lowerRightTextFontName = lowerRightTextFont.getFamily();
        this.outterTextFont = outterTextFont;
        this.outterTextFontName = outterTextFont.getFamily();
        this.upperRightTextFont = upperRightTextFont;
        this.upperRightTextFontName = upperRightTextFont.getFamily();
        this.scaleBarTextFont = ScaleBarTextFont;
        this.scaleBarTextFontName = scaleBarTextFont.getFamily();
        this.columnSize = columnSize;
        this.oneAndHalfColumn = oneAndHalfColumn;
        this.twoColumnSizee = twoColumnSizee;
        this.pageHeight = pageHeight;
        this.objectsStrokeSize = objectsStrokeSize;
        this.gf = gf;
        this.capitalisationOfLetter = capitalisationOfLetter;
        this.regexRules = regexRules;
        this.colorDPI = colorDPI;
        this.BWDPI = BWDPI;
    }

    public int getColorDPI() {
        return colorDPI;
    }

    public void setColorDPI(int colorDPI) {
        this.colorDPI = colorDPI;
    }

    public int getBWDPI() {
        return BWDPI;
    }

    public void setBWDPI(int BWDPI) {
        this.BWDPI = BWDPI;
    }

    public String getLetterFontName() {
        return letterFontName;
    }

    public void setLetterFontName(String letterFontName) {
        this.letterFontName = letterFontName;
    }

    public String getLowerLeftTextFontName() {
        return lowerLeftTextFontName;
    }

    public void setLowerLeftTextFontName(String lowerLeftTextFontName) {
        this.lowerLeftTextFontName = lowerLeftTextFontName;
    }

    public String getOutterTextFontName() {
        return outterTextFontName;
    }

    public void setOutterTextFontName(String outterTextFontName) {
        this.outterTextFontName = outterTextFontName;
    }

    public String getUpperLeftTextFontName() {
        return upperLeftTextFontName;
    }

    public void setUpperLeftTextFontName(String upperLeftTextFontName) {
        this.upperLeftTextFontName = upperLeftTextFontName;
    }

    public String getLowerRightTextFontName() {
        return lowerRightTextFontName;
    }

    public void setLowerRightTextFontName(String lowerRightTextFontName) {
        this.lowerRightTextFontName = lowerRightTextFontName;
    }

    public String getUpperRightTextFontName() {
        return upperRightTextFontName;
    }

    public void setUpperRightTextFontName(String upperRightTextFontName) {
        this.upperRightTextFontName = upperRightTextFontName;
    }

    public String getScaleBarTextFontName() {
        return scaleBarTextFontName;
    }

    public void setScaleBarTextFontName(String scaleBarTextFontName) {
        this.scaleBarTextFontName = scaleBarTextFontName;
    }

    public String getCapitalisationOfLetter() {
        return capitalisationOfLetter;
    }

    public void setCapitalisationOfLetter(String capitalisationOfLetter) {
        this.capitalisationOfLetter = capitalisationOfLetter;
    }

    public float getObjectsStrokeSize() {
        return objectsStrokeSize;
    }

    public void setObjectsStrokeSize(float objectsStrokeSize) {
        this.objectsStrokeSize = objectsStrokeSize;
    }

    /**
     *
     * @return the graph parameters
     */
    public GraphFont getGf() {
        return gf;
    }

    /**
     * Sets the graph parameters
     *
     * @param gf
     */
    public void setGf(GraphFont gf) {
        this.gf = gf;
    }

    /**
     *
     * @return the path to the current journal style
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path to the current journal style
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return the name of the journal parameter (typically, 'Nature Methods',
     * 'Cell', 'Science', ...). This name will appear in combos in SF and FiguR.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the journal style (typically the name of the journal)
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the font assocaited to letters
     */
    public Font getLetterFont() {
        return letterFont;
    }

    /**
     * Sets the font of the letters
     *
     * @param letterFont
     */
    public void setLetterFont(Font letterFont) {
        this.letterFont = letterFont;
    }

    /**
     *
     * @return the font of the text over the image
     */
    public Font getOutterTextFont() {
        return outterTextFont;
    }

    /**
     * Sets the font of the text over the image
     *
     * @param outterTextFont
     */
    public void setOutterTextFont(Font outterTextFont) {
        this.outterTextFont = outterTextFont;
    }

    /**
     *
     * @return the font for the text at the lower left corner of the image
     */
    public Font getLowerLeftTextFont() {
        return lowerLeftTextFont;
    }

    /**
     * Sets the font for the text at the lower left corner of the image
     *
     * @param lowerLeftTextFont
     */
    public void setLowerLeftTextFont(Font lowerLeftTextFont) {
        this.lowerLeftTextFont = lowerLeftTextFont;
    }

    /**
     *
     * @return the font for the text at the upper left corner of the image
     */
    public Font getUpperLeftTextFont() {
        return upperLeftTextFont;
    }

    /**
     * Sets the font for the text at the upper left corner of the image
     *
     * @param upperLeftTextFont
     */
    public void setUpperLeftTextFont(Font upperLeftTextFont) {
        this.upperLeftTextFont = upperLeftTextFont;
    }

    /**
     *
     *
     * @return the font for the text at the lower right corner of the image
     */
    public Font getLowerRightTextFont() {
        return lowerRightTextFont;
    }

    /**
     * Sets the font for the text at the lower right corner of the image
     *
     * @param lowerRightTextFont
     */
    public void setLowerRightTextFont(Font lowerRightTextFont) {
        this.lowerRightTextFont = lowerRightTextFont;
    }

    /**
     *
     * @return the font for the text at the upper right corner of the image
     */
    public Font getUpperRightTextFont() {
        return upperRightTextFont;
    }

    /**
     * Sets the font for the text at the upper right corner of the image
     *
     * @param upperRightTextFont
     */
    public void setUpperRightTextFont(Font upperRightTextFont) {
        this.upperRightTextFont = upperRightTextFont;
    }

    /**
     * Sets the font for the text of the scalebar
     *
     * @param ScaleBarTextFont
     */
    public void setScaleBarTextFont(Font ScaleBarTextFont) {
        this.scaleBarTextFont = ScaleBarTextFont;
    }

    private void setObjectStrokeSize(float strokeSize) {
        this.objectsStrokeSize = strokeSize;
    }

    /**
     *
     * @return the font for the text of the scalebar
     */
    public Font getScaleBarTextFont() {
        return scaleBarTextFont;
    }

    /**
     *
     * @return the size of one journal column
     */
    public double getColumnSize() {
        return columnSize;
    }

    /**
     * Sets the size of one journal column
     *
     * @param columnSize
     */
    public void setColumnSize(double columnSize) {
        this.columnSize = columnSize;
    }

    /**
     *
     * @return the size of one and a half journal columns
     */
    public double getOneAndHalfColumn() {
        return oneAndHalfColumn;
    }

    /**
     * Sets the size of one and a half journal columns
     *
     * @param oneAndHalfColumn
     */
    public void setOneAndHalfColumn(double oneAndHalfColumn) {
        this.oneAndHalfColumn = oneAndHalfColumn;
    }

    /**
     *
     * @return the size of two journal columns
     */
    public double getTwoColumnSizee() {
        return twoColumnSizee;
    }

    /**
     * Sets the size of two journal columns
     *
     * @param twoColumnSizee
     */
    public void setTwoColumnSizee(double twoColumnSizee) {
        this.twoColumnSizee = twoColumnSizee;
    }

    /**
     *
     * @return the max height for a page for a particular journal
     */
    public double getPageHeight() {
        return pageHeight;
    }

    /**
     * Sets the max height for a page for a particular journal
     *
     * @param pageHeight
     */
    public void setPageHeight(double pageHeight) {
        this.pageHeight = pageHeight;
    }

    /**
     * Stores the content of a JournalParameters in an xml (txt) document
     *
     * @param journalStyleFolder
     * @param forced_name
     */
    public String toXML(String journalStyleFolder, String forced_name) {
        File file;
        if (forced_name == null) {
            new File(journalStyleFolder).mkdirs();
            try {
                /*
                 * changed .xml for .txt extension in order to be compatible with the FIJI uploader
                 */
                file = java.io.File.createTempFile("journal_", ".txt", new File(journalStyleFolder));
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw); e.printStackTrace(pw);
                String stacktrace = sw.toString();pw.close();
                System.err.println(stacktrace);
                System.err.println("Error could not create the file file, maybe the disk is write protected");
                return null;
            }
        } else {
            file = new File(forced_name);
            file.delete();
        }
        FileOutputStream out = null;
        try {
            Properties p = new Properties();
            p.setProperty("Journal Name", getName());
            p.setProperty("Journal Page Height", getPageHeight() + "");
            p.setProperty("Journal Column Width in cm", getColumnSize() + "");
            p.setProperty("Journal Page (2 columns) Width in cm", getTwoColumnSizee() + "");
            p.setProperty("Journal 3/4 Page Width in cm", getOneAndHalfColumn() + "");
            p.setProperty("Figure Letter Font", (letterFontName == null ? letterFont.getFamily() : letterFontName) + "#" + letterFont.getStyle() + "#" + letterFont.getSize());
            p.setProperty("Figure Upper Left Text Font", (upperLeftTextFontName == null ? letterFont.getFamily() : letterFontName) + "#" + upperLeftTextFont.getStyle() + "#" + upperLeftTextFont.getSize());
            p.setProperty("Figure Upper Right Text Font", (upperRightTextFontName == null ? upperRightTextFont.getFamily() : upperRightTextFontName) + "#" + upperRightTextFont.getStyle() + "#" + upperRightTextFont.getSize());
            p.setProperty("Figure Lower Left Text Font", (lowerLeftTextFontName == null ? lowerLeftTextFont.getFamily() : lowerLeftTextFontName) + "#" + lowerLeftTextFont.getStyle() + "#" + lowerLeftTextFont.getSize());
            p.setProperty("Figure Lower Right Text Font", (lowerRightTextFontName == null ? lowerRightTextFont.getFamily() : lowerRightTextFontName) + "#" + lowerRightTextFont.getStyle() + "#" + lowerRightTextFont.getSize());
            p.setProperty("Figure Outter Text", (outterTextFontName == null ? outterTextFont.getFamily() : outterTextFontName) + "#" + outterTextFont.getStyle() + "#" + outterTextFont.getSize());
            p.setProperty("Figure ScaleBar Font", (scaleBarTextFontName == null ? scaleBarTextFont.getFamily() : scaleBarTextFontName) + "#" + scaleBarTextFont.getStyle() + "#" + scaleBarTextFont.getSize());
            p.setProperty("ROI/objects Stroke Size", objectsStrokeSize + "");
            p.setProperty("Capitalization behaviour of the letter", capitalisationOfLetter);
            p.setProperty("Color DPI", colorDPI + "");
            p.setProperty("B&W DPI", BWDPI + "");
            if (gf != null) {
                Font title = gf.getTitleSize();
                p.setProperty("Graph Main Title", (gf.titleSizeName == null ? title.getFamily() : gf.titleSizeName) + "#" + title.getStyle() + "#" + title.getSize());
                Font legendTitle = gf.getLegendTitle();
                p.setProperty("Graph Legend Title", (gf.legendTitleName == null ? legendTitle.getFamily() : gf.legendTitleName) + "#" + legendTitle.getStyle() + "#" + legendTitle.getSize());
                Font legendText = gf.getLegendText();
                p.setProperty("Graph Legend Text", (gf.legendTextName == null ? legendText.getFamily() : gf.legendTextName) + "#" + legendText.getStyle() + "#" + legendText.getSize());
                Font xaxistitle = gf.getXtitle();
                p.setProperty("Graph X axis title", (gf.xtitleName == null ? xaxistitle.getFamily() : gf.xtitleName) + "#" + xaxistitle.getStyle() + "#" + xaxistitle.getSize());
                Font yaxistitle = gf.getYtitle();
                p.setProperty("Graph Y axis title", (gf.ytitleName == null ? yaxistitle.getFamily() : gf.ytitleName) + "#" + yaxistitle.getStyle() + "#" + yaxistitle.getSize());
                Font xaxistext = gf.getXaxis();
                p.setProperty("Graph X axis text", (gf.xaxisName == null ? xaxistext.getFamily() : gf.xaxisName) + "#" + xaxistext.getStyle() + "#" + xaxistext.getSize());
                Font yaxistext = gf.getYaxis();
                p.setProperty("Graph Y axis text", (gf.yaxisName == null ? yaxistext.getFamily() : gf.yaxisName) + "#" + yaxistext.getStyle() + "#" + yaxistext.getSize());
                p.setProperty("Graph Default Line Width", gf.getLineSize() + "");
                p.setProperty("Graph Default Point Width", gf.getPointSize() + "");
                p.setProperty("Warn if graph has a title", gf.warnIfTitleIsPresent + "");
                p.setProperty("Warn if graph legend has a title", gf.warnIfLegendTitleIsPresent + "");
                p.setProperty("Warn if graph has a grid", gf.warnIfGridIsPresent + "");
                p.setProperty("Warn if graph has a color", gf.warnIfHasBgColor + "");
                p.setProperty("Warn if units for x/y axes are missing", gf.warnIfUnitsAreMissing + "");
                p.setProperty("Warn if there are no brackets around units", gf.warnIfNobracketsAroundUnits + "");
                p.setProperty("Warn if axes have no titles", gf.warnIfAxisTitlesAreMissing + "");

            }
            if (regexRules != null) {
                int count = 0;
                for (SFTextController sFTextController : regexRules) {
                    p.setProperty("regex rule #" + count++, SFTextController.toString(sFTextController));
                }
            }
            out = new FileOutputStream(file);
            p.storeToXML(out, "last update " + new Date().toString());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw); e.printStackTrace(pw);
            String stacktrace = sw.toString();pw.close();
            System.err.println(stacktrace);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
        if (file != null) {
            return CommonClassesLight.change_path_separators_to_system_ones(file.toString());
        } else {
            return null;
        }
    }

    /**
     * Reads an xml (txt) document and loads all its parameters in the current
     * class
     *
     * @param filename
     * @return true upon success
     */
    public boolean loadParameters(String filename) {
        boolean all_ok = true;
        FileInputStream in = null;
        try {
            if (!new File(filename).exists()) {
                all_ok = false;
            }
            in = new FileInputStream(filename);
            Properties p = new Properties();
            /*
             * load additional parameters
             */
            try {
                p.loadFromXML(in);
                if (p.containsKey("Journal Name")) {
                    setName(p.getProperty("Journal Name"));
                }
                if (p.containsKey("Journal Page Height")) {
                    setPageHeight(CommonClassesLight.String2Double(p.getProperty("Journal Page Height")));
                }
                if (p.containsKey("Journal Column Width in cm")) {
                    setColumnSize(CommonClassesLight.String2Double(p.getProperty("Journal Column Width in cm")));
                }
                if (p.containsKey("Journal Page (2 columns) Width in cm")) {
                    setTwoColumnSizee(CommonClassesLight.String2Double(p.getProperty("Journal Page (2 columns) Width in cm")));
                }
                if (p.containsKey("Journal 3/4 Page Width in cm")) {
                    setOneAndHalfColumn(CommonClassesLight.String2Double(p.getProperty("Journal 3/4 Page Width in cm")));
                }
                if (p.containsKey("Figure Letter Font")) {
                    setLetterFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Letter Font"), "#"));
                    setLetterFont(string2Font(p.getProperty("Figure Letter Font")));
                }
                if (p.containsKey("Figure Upper Left Text Font")) {
                    setUpperLeftTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Upper Left Text Font"), "#"));
                    setUpperLeftTextFont(string2Font(p.getProperty("Figure Upper Left Text Font")));
                }
                if (p.containsKey("Figure Upper Right Text Font")) {
                    setUpperRightTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Upper Right Text Font"), "#"));
                    setUpperRightTextFont(string2Font(p.getProperty("Figure Upper Right Text Font")));
                }
                if (p.containsKey("Figure Lower Left Text Font")) {
                    setLowerLeftTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Lower Left Text Font"), "#"));
                    setLowerLeftTextFont(string2Font(p.getProperty("Figure Lower Left Text Font")));
                }
                if (p.containsKey("Figure Lower Right Text Font")) {
                    setLowerRightTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Lower Right Text Font"), "#"));
                    setLowerRightTextFont(string2Font(p.getProperty("Figure Lower Right Text Font")));
                }
                if (p.containsKey("Figure Outter Text")) {
                    setOutterTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure Outter Text"), "#"));
                    setOutterTextFont(string2Font(p.getProperty("Figure Outter Text")));
                }
                if (p.containsKey("Figure ScaleBar Font")) {
                    setScaleBarTextFontName(CommonClassesLight.strCutLeftFirst(p.getProperty("Figure ScaleBar Font"), "#"));
                    setScaleBarTextFont(string2Font(p.getProperty("Figure ScaleBar Font")));
                }
                if (p.containsKey("ROI/objects Stroke Size")) {
                    setObjectStrokeSize(CommonClassesLight.String2Float(p.getProperty("ROI/objects Stroke Size")));
                }
                if (p.containsKey("Capitalization behaviour of the letter")) {
                    setCapitalisationOfLetter(p.getProperty("Capitalization behaviour of the letter"));
                }
                if (p.containsKey("Color DPI")) {
                    setColorDPI(CommonClassesLight.String2Int(p.getProperty("Color DPI")));
                }
                if (p.containsKey("B&W DPI")) {
                    setBWDPI(CommonClassesLight.String2Int(p.getProperty("B&W DPI")));
                }
                GraphFont curGraphFt = new GraphFont();
                if (p.containsKey("Graph Main Title")) {
                    curGraphFt.setTitleSizeName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph Main Title"), "#"));
                    curGraphFt.setTitleSize(string2Font(p.getProperty("Graph Main Title")));
                }
                if (p.containsKey("Graph Legend Title")) {
                    curGraphFt.setLegendTitleName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph Legend Title"), "#"));
                    curGraphFt.setLegendTitle(string2Font(p.getProperty("Graph Legend Title")));
                }
                if (p.containsKey("Graph Legend Text")) {
                    curGraphFt.setLegendTextName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph Legend Text"), "#"));
                    curGraphFt.setLegendText(string2Font(p.getProperty("Graph Legend Text")));
                }
                if (p.containsKey("Graph X axis title")) {
                    curGraphFt.setTitleSizeName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph X axis title"), "#"));
                    curGraphFt.setXtitle(string2Font(p.getProperty("Graph X axis title")));
                }
                if (p.containsKey("Graph Y axis title")) {
                    curGraphFt.setYtitleName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph Y axis title"), "#"));
                    curGraphFt.setYtitle(string2Font(p.getProperty("Graph Y axis title")));
                }
                if (p.containsKey("Graph X axis text")) {
                    curGraphFt.setXaxisName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph X axis text"), "#"));
                    curGraphFt.setXaxis(string2Font(p.getProperty("Graph X axis text")));
                }
                if (p.containsKey("Graph Y axis text")) {
                    curGraphFt.setYaxisName(CommonClassesLight.strCutLeftFirst(p.getProperty("Graph Y axis text"), "#"));
                    curGraphFt.setYaxis(string2Font(p.getProperty("Graph Y axis text")));
                }
                if (p.containsKey("Graph Default Line Width")) {
                    curGraphFt.setLineSize(CommonClassesLight.String2Float(p.getProperty("Graph Default Line Width")));
                }
                if (p.containsKey("Graph Default Point Width")) {
                    curGraphFt.setPointSize(CommonClassesLight.String2Float(p.getProperty("Graph Default Point Width")));
                }
                if (p.containsKey("Warn if graph has a title")) {
                    curGraphFt.setWarnIfTitleIsPresent(CommonClassesLight.String2Boolean(p.getProperty("Warn if graph has a title")));
                }
                if (p.containsKey("Warn if graph legend has a title")) {
                    curGraphFt.setWarnIfLegendTitleIsPresent(CommonClassesLight.String2Boolean(p.getProperty("Warn if graph legend has a title")));
                }
                if (p.containsKey("Warn if graph has a grid")) {
                    curGraphFt.setWarnIfGridIsPresent(CommonClassesLight.String2Boolean(p.getProperty("Warn if graph has a grid")));
                }
                if (p.containsKey("Warn if graph has a color")) {
                    curGraphFt.setWarnIfHasBgColor(CommonClassesLight.String2Boolean(p.getProperty("Warn if graph has a color")));
                }
                if (p.containsKey("Warn if units for x/y axes are missing")) {
                    curGraphFt.setWarnIfUnitsAreMissing(CommonClassesLight.String2Boolean(p.getProperty("Warn if units for x/y axes are missing")));
                }
                if (p.containsKey("Warn if there are no brackets around units")) {
                    curGraphFt.setWarnIfNobracketsAroundUnits(CommonClassesLight.String2Boolean(p.getProperty("Warn if there are no brackets around units")));
                }
                if (p.containsKey("Warn if axes have no titles")) {
                    curGraphFt.setWarnIfAxisTitlesAreMissing(CommonClassesLight.String2Boolean(p.getProperty("Warn if axes have no titles")));
                }
                setGf(curGraphFt);
                /*
                 * now we get all the SF text formatting rules
                 */
                Set<Object> props = p.keySet();
                for (Object object : props) {
                    if (object != null) {
                        if (object.toString().toLowerCase().contains("regex rule #")) {
                            SFTextController rule = new SFTextController(p.get(object).toString());
                            if (rule.check(false, null)) {
                                regexRules.add(rule);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                all_ok = false;
            }
        } catch (Exception e) {
            all_ok = false;
            StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw); e.printStackTrace(pw);
            String stacktrace = sw.toString();pw.close();
            System.err.println(stacktrace);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        return all_ok;
    }

    //TODO also store real font name to warn user about errors
    private Font string2Font(String fontAsString) {
        String[] parsedFont = fontAsString.split("#");
        String style = parsedFont[1];
        if (style.toLowerCase().trim().equals("plain")) {
            style = Font.PLAIN + "";
        } else if (style.toLowerCase().trim().equals("bold")) {
            style = Font.BOLD + "";
        } else if (style.toLowerCase().trim().equals("italic")) {
            style = Font.ITALIC + "";
        } else if (style.toLowerCase().trim().equals("bold+italic") || style.toLowerCase().trim().equals("italic+bold")) {
            style = (Font.BOLD + Font.ITALIC) + "";
        }
        Font ft = new Font(parsedFont[0], CommonClassesLight.String2Int(style), CommonClassesLight.String2Int(parsedFont[2]));
        boolean isFontValid = isFontValid(ft, parsedFont[0]);
        if (!isFontValid) {
            ft = substituteFont(ft);
            /*
             * bug fix since Arial may not always be installed on the system.
             * NB: to install Arial under ubuntu use:
             * sudo apt-get install msttcorefonts 
             * or
             * sudo apt-get install ttf-mscorefonts-installer
             */
            fontErrors += "The Font \"" + parsedFont[0] + "\" was not found on your system --> it will be substituted by the \"" + ft.getFamily() + "\" Font\n";
        }
        return ft;
    }

    /**
     *
     * @return true if some fonts were missing from the system
     */
    public boolean hasFontErrors() {
        return !fontErrors.equals("");
    }

    /**
     *
     * @return the error encountered when trying to load a font
     */
    public String getFontLoadingErrors() {
        return fontErrors;
    }

    /**
     *
     * @param ft any font
     * @param fontFamily any font family
     * @return true if the font corresponds to the right family
     */
    public boolean isFontValid(Font ft, String fontFamily) {
        if (!fontFamily.toLowerCase().equals(ft.getFamily().toLowerCase())) {
            return false;
        }
        return true;
    }

    /**
     * If a font could not be found on the system we substitute it with Arial
     * (probably the most used font in informatics). NB: it might fail if Arial
     * is also not installed on the system (this is for example the case with
     * some linux distributions)
     *
     * @param ft desired missing font
     * @return an arial Font
     */
    public Font substituteFont(Font ft) {
        return new Font("Arial", ft.getStyle(), ft.getSize());
    }

    public ArrayList<SFTextController> getAdvancedtextFormattingRules() {
        return regexRules;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        JournalParameters test = new JournalParameters();
        JournalParametersDialog iopane = new JournalParametersDialog();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Journal Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getJournalStyle().getLetterFont() + " " + iopane.getJournalStyle().getLetterFontName());
        }
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



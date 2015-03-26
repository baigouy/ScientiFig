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
package Checks;

import Commons.CommonClassesLight;
import Commons.Loader;
import Commons.MyBufferedImage;
import Commons.SaverLight;
import Dialogs.ColorSelector;
import Dialogs.RLabelEditor;
import GUIs.ScientiFig;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.GraphFont;
import MyShapes.JournalParameters;
import MyShapes.MyPlotVector;
import R.GeomPlot;
import R.RSession.MyRsessionLogger;
import R.ScaleColor;
import R.String.RLabel;
import R.ThemeGraph;
import Tools.PopulateJournalStyles;
import Tools.PopulateThemes;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.rosuda.REngine.REXPMismatchException;

/**
 *
 * @author Benoit Aigouy
 */
public class CheckGraphs extends javax.swing.JPanel {

    ArrayList<Integer> replacementColorColors = new ArrayList<Integer>();
    ArrayList<Integer> replacementFillColors = new ArrayList<Integer>();
    public boolean noError = true;
    ThemeGraph currentTheme;
    ThemeGraph selectedTheme;
    RLabel t;
    RLabel x;
    RLabel y;
    RLabel l;
    GraphFont desiredFont;
    boolean themeShouldBeRepalced = false;
    boolean isTitleTextPerfect = true;
    boolean isTitleLegendPerfect = true;
    boolean isTitleXaxisPerfect = true;
    boolean isTitleYaxisPerfect = true;
    PopulateThemes themes = new PopulateThemes();
    String warning = "";
    GraphFont currentGraphFonts;
    MyPlotVector.Double graph;

    /**
     * Creates new form CheckGraphs
     */
    public CheckGraphs(MyPlotVector.Double graph, JournalParameters jp) {
        initComponents();
        title.setText(graph.getTitleLabel().toRExpression());
        xAxisTitle.setText(graph.getxAxisLabel().toRExpression());
        yAxisTitle.setText(graph.getyAxisLabel().toRExpression());
        legendTitle.setText(graph.getLegendLabel().toRExpression());
        this.currentTheme = graph.getTheme();
        this.graph = graph;
        this.desiredFont = jp.getGf();
        if (desiredFont.getPointSize() > 0) {
            jSpinner2.setValue(desiredFont.getPointSize());
        }
        if (desiredFont.getLineSize() > 0) {
            jSpinner1.setValue(desiredFont.getLineSize());
        }
        themes.reloadThemes(jComboBox1);
        jComboBox1.setSelectedIndex(-1);
        currentGraphFonts = graph.getFonts();
        if (CommonClassesLight.isRReady()) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            Rstatus.setText("R con opened");
            CommonClassesLight.r.reloadRFonts(jComboBox2);
        } else {
            warning += "<font color=\"#FF0000\">-Connection to R failed (list of R fonts is not available --> SF will always assume font exists for replacements)<br>Please click on the 'Show R installation guidelines' to install and configure R</font><br><br>";
        }
        if (currentTheme == null) {
            if (desiredFont.isWarnIfHasBgColor()) {
                warning += "<font color=\"#FF0000\">-The guidelines recommend graphs without background, yours has one</font><br>";
                jComboBox1.setSelectedItem("Basic");
                applySuggestedTheme.setSelected(true);
                themeShouldBeRepalced = true;
                noError = false;
            } else {
                warning += "<font color=\"#00AA00\">+Your graph has a background color as recommended by the guidelines</font><br>";
            }
            if (desiredFont.isWarnIfGridIsPresent()) {
                warning += "<font color=\"#FF0000\">-The guidelines recommend graphs without grid, yours has a grid</font><br>";
                jComboBox1.setSelectedItem("Basic");
                applySuggestedTheme.setSelected(true);
                themeShouldBeRepalced = true;
                noError = false;
            } else {
                warning += "<font color=\"#00AA00\">+Your graph has a grid as authorized by the guidelines</font><br>";
            }
        } else {
            if ((currentTheme.getPanelMajorGridStrokeWidth() == null || !currentTheme.getPanelMajorGridStrokeWidth().toLowerCase().contains("element_blank()")) || (currentTheme.getPanelMinorGridStrokeWidth() == null || !currentTheme.getPanelMinorGridStrokeWidth().toLowerCase().contains("element_blank()")))//check 
            {
                if (desiredFont.isWarnIfHasBgColor()) {
                    warning += "<font color=\"#FF0000\">-The guidelines recommend that graphs have no background, yours has one</font><br>";
                    jComboBox1.setSelectedItem("Basic");
                    applySuggestedTheme.setSelected(true);
                    themeShouldBeRepalced = true;
                    noError = false;
                } else {
                    warning += "<font color=\"#00AA00\">+Your graph has a background color as recommended in the guidelines</font><br>";
                }
            }
            if (currentTheme.getGraphBgColor() == null || !currentTheme.getGraphBgColor().equals("#FFFFFF")) {
                if (desiredFont.isWarnIfHasBgColor()) {
                    warning += "<font color=\"#FF0000\">-The guidelines recommend that graphs have no background, yours has one</font><br>";
                    jComboBox1.setSelectedItem("Basic");
                    applySuggestedTheme.setSelected(true);
                    themeShouldBeRepalced = true;
                    noError = false;
                } else {
                    warning += "<font color=\"#00AA00\">+Your graph has a background color as recommended by the guidelines</font><br>";
                }
            }
        }
        if (!themeShouldBeRepalced) {
            jLabel3.setVisible(false);
            jComboBox1.setVisible(false);
            applySuggestedTheme.setVisible(false);//prevents users from clicking on it
            imagePaneLight2.setVisible(false);
        }
        GraphFont desiredGraphFont = jp.getGf();
        if (currentGraphFonts != null && currentGraphFonts.equals(desiredGraphFont)) {
            warning += "<font color=\"#00AA00\">+All the fonts match those specified in the guidelines</font><br>";
            if (CommonClassesLight.isRReady()) {
                if (!checkGf(desiredGraphFont)) {
                    warning += "<font color=\"#FF0000\">-Some fonts specified in the guidelines are not available in R<br>TO FIX THIS if you never have installed R extrafonts<br>(if you already did that and it still does not work then there is no solution,<br>your plot will be created still but with incorrect fonts),<br>we suggest you open R and copy and paste the following commands to the R command:<br><br>install.packages(\"extrafont\")<br>library(extrafont)<br>font_import()<br>fonts()<br><br>NB:the last command should display the list of available fonts<br>(you may need to restart R and SF or your reboot system for the changes to be effective)<br>NB3: Again don't worry you will only have to do this painful operation once after a fresh R installation</font></font><br>";
                    noError = false;
                } else {
                    warning += "<font color=\"#00AA00\">+All the fonts specified in the journal guidelines are available in R</font><br>";
                }
            } else {
                warning += "<font color=\"#FF0000\">-Unfortunately connection failure prevents checking for fonts availability in R.<br>SF will therefore assume fonts are available</font><br>";
            }
            /*
             * fonts are correct hide replace options
             */
            jLabel12.setVisible(false);
            jCheckBox1.setVisible(false);
        } else {
            warning += "<font color=\"#FF0000\">-Some fonts do not match the guidelines, we suggest you replace graph fonts</font><br>";
            if (CommonClassesLight.isRReady()) {
                if (!checkGf(desiredGraphFont)) {
                    warning += "<font color=\"#FF0000\">-Some fonts specified in the guidelines are not available in R<br>TO FIX THIS if you never have installed R extrafonts<br>(if you already did that and it still does not work then there is no solution,<br>your plot will be created still but with incorrect fonts),<br>we suggest you open R and copy and paste the following commands to the R command:<br><br>install.packages(\"extrafont\")<br>library(extrafont)<br>font_import()<br>fonts()<br><br>NB:the last command should display the list of available fonts<br>(you may need to restart R and SF or your reboot system for the changes to be effective)<br>NB3: Again don't worry you will only have to do this painful operation once after a fresh R installation</font></font><br>";
                    warning += "Here is the availability of the curent graph fonts in R<br>";
                    noError = false;
                } else {
                    warning += "<font color=\"#00AA00\">+All the fonts specified in the journal guidelines are available in R</font><br>";
                }
            } else {
                noError = false;
                warning += "<font color=\"#FF0000\">-Unfortunately connection failure prevents checking for replacement fonts availability in R.<br>SF will therefore assume fonts are available</font><br>";
            }
        }
        /*
         * try to get the connection to R otherwise everything will be ok but preview will be not available and image will not be updated until next reboot
         */
        jEditorPane1.setContentType("text/html");
        if (desiredGraphFont.getPointSize() > 0 && !graph.checkPointSize(desiredGraphFont.getPointSize())) {
            warning += "<font color=\"#FF0000\">-Your graph has a point size that differs significantly from the guidelines</font><br>";
            jCheckBox3.setSelected(true);
            noError = false;
        } else {
            jLabel14.setVisible(false);
            jSpinner2.setVisible(false);
            jCheckBox3.setVisible(false);
        }
        if (desiredGraphFont.getLineSize() > 0 && !graph.checkStrokeSize(desiredGraphFont.getLineSize())) {
            warning += "<font color=\"#FF0000\">-Your graph has a line width that differs significantly from the guidelines</font><br>";
            jCheckBox2.setSelected(true);
            noError = false;
        } else {
            jLabel13.setVisible(false);
            jSpinner1.setVisible(false);
            jCheckBox2.setVisible(false);
        }
        if (graph.use_non_custom_code) {
            t = graph.getTitleLabel();
            x = graph.getxAxisLabel();
            y = graph.getyAxisLabel();
            l = graph.getLegendLabel();
            if (t != null) {
                if (t.hasMainText() || t.hasUnit() || t.hasFormula()) {
                    if (desiredGraphFont.isWarnIfTitleIsPresent()) {
                        warning += "<font color=\"#FF0000\">-Your graph has a title, we suggest you remove it</font><br>";
                        isTitleTextPerfect = false;
                        noError = false;
                        deleteTitle.setSelected(true);
                    }
                } else {
                    if (desiredGraphFont.isWarnIfTitleIsPresent()) {
                        warning += "<font color=\"#00AA00\">+Your graph has no title as suggested in the guidelines</font><br>";
                    }
                }
            } else {
                if (desiredGraphFont.isWarnIfTitleIsPresent()) {
                    warning += "<font color=\"#00AA00\">+Your graph has no title as suggested in the guidelines</font><br>";
                }
            }
            if (l != null) {
                if (l.hasMainText() || l.hasUnit() || l.hasFormula()) {
                    if (desiredGraphFont.isWarnIfLegendTitleIsPresent()) {
                        warning += "<font color=\"#FF0000\">-Your graph legend has a title, we suggest you remove it</font><br>";
                        isTitleLegendPerfect = false;
                        noError = false;
                        deleteLegendTitle.setSelected(true);
                    }
                } else {
                    if (desiredGraphFont.isWarnIfLegendTitleIsPresent()) {
                        warning += "<font color=\"#00AA00\">+Your graph legend has no title as suggested in the guidelines</font><br>";
                    }
                }
            } else {
                if (desiredGraphFont.isWarnIfLegendTitleIsPresent()) {
                    warning += "<font color=\"#FF0000\">-Your graph legend may have a title automatically generated by R, we suggest you remove it</font><br>";
                    isTitleLegendPerfect = false;
                    noError = false;
                    deleteLegendTitle.setSelected(true);
                }
            }
            if (x != null) {
                if (desiredGraphFont.isWarnIfAxisTitlesAreMissing()) {
                    if (!x.hasMainText() && !x.hasFormula()) {
                        warning += "<font color=\"#FF0000\">-Your x axis may not have a title (the title can be the title generated by R) we recommend you name the axis explicitly</font><br>";
                        isTitleXaxisPerfect = false;
                        noError = false;
                    } else {
                        warning += "<font color=\"#00AA00\">+Your x axis has a title as suggested in the guidelines</font><br>";
                    }
                }
                if (desiredGraphFont.isWarnIfUnitsAreMissing()) {
                    if (!x.hasUnit()) {
                        warning += "<font color=\"#FF0000\">-Your x axis does not have units, we recommend you add units to it (use a.u. for arbitrary units)</font><br>";
                        isTitleXaxisPerfect = false;
                        addaux.setSelected(true);
                        noError = false;
                    } else {
                        warning += "<font color=\"#00AA00\">+Your x axis has units as suggested in the guidelines</font><br>";
                    }
                }
            } else {
                if (desiredGraphFont.isWarnIfAxisTitlesAreMissing()) {
                    warning += "<font color=\"#FF0000\">-Your x axis may not have a title (the title can be the title generated by R) we recommend you name the axis explicitly</font><br>";
                    isTitleXaxisPerfect = false;
                    noError = false;
                }
                if (desiredGraphFont.isWarnIfUnitsAreMissing()) {
                    warning += "<font color=\"#FF0000\">-Your x axis does not have units, we recommend you add units to it (use a.u. for arbitrary units)</font><br>";
                    isTitleXaxisPerfect = false;
                    addaux.setSelected(true);
                    noError = false;
                }
            }
            if (y != null) {
                if (desiredGraphFont.isWarnIfAxisTitlesAreMissing()) {
                    if (!y.hasMainText() && !y.hasFormula()) {
                        warning += "<font color=\"#FF0000\">-Your y axis may not have a title (the title can be the title generated by R) we recommend you name the axis explicitly</font><br>";
                        isTitleYaxisPerfect = false;
                        noError = false;
                    } else {
                        warning += "<font color=\"#00AA00\">+Your y axis has a title as suggested in the guidelines</font><br>";
                    }
                }
                if (desiredGraphFont.isWarnIfUnitsAreMissing()) {
                    if (!y.hasUnit()) {
                        warning += "<font color=\"#FF0000\">-Your y axis does not have units, we recommend you add units to it (use a.u. for arbitrary units)</font><br>";
                        isTitleYaxisPerfect = false;
                        noError = false;
                        addauy.setSelected(true);
                    } else {
                        warning += "<font color=\"#00AA00\">+Your y axis has units as suggested in the guidelines</font><br>";
                    }
                }
            } else {
                if (desiredGraphFont.isWarnIfAxisTitlesAreMissing()) {
                    warning += "<font color=\"#FF0000\">-Your y axis may not have a title (the title can be the title generated by R) we recommend you name the axis explicitly</font><br>";
                    isTitleYaxisPerfect = false;
                    noError = false;
                }
                if (desiredGraphFont.isWarnIfUnitsAreMissing()) {
                    warning += "<font color=\"#FF0000\">-Your y axis does not have units, we recommend you add units to it (use a.u. for arbitrary units)</font><br>";
                    isTitleYaxisPerfect = false;
                    noError = false;
                    addauy.setSelected(true);
                }
            }
            if (desiredGraphFont.isWarnIfNobracketsAroundUnits()) {
                if (x != null && !x.isAutoBracket()) {
                    warning += "<font color=\"#FF0000\">-There might be no brackets around your x units we suggest you add some if not done</font><br>";
                    addBracketX.setSelected(true);
                    noError = false;
                    isTitleXaxisPerfect = false;
                } else if (x != null) {
                    warning += "<font color=\"#00AA00\">+There are brackets around your x units as recommended in the guidelines</font><br>";
                }
                if (y != null && !y.isAutoBracket()) {
                    warning += "<font color=\"#FF0000\">-There might be no brackets around your y units we suggest you add some if not done</font><br>";
                    addBracketY.setSelected(true);
                    isTitleYaxisPerfect = false;
                    noError = false;
                } else if (y != null) {
                    warning += "<font color=\"#00AA00\">+There are brackets around your y units as recommended in the guidelines</font><br>";
                }
            }
            if (desiredFont.isWarnIfColorsAreNotColorBlindFriendly()) {
                boolean hasColor = false;
                boolean hasFill = false;
                ArrayList<Object> extras = graph.getExtras();
                if (extras != null) {
                    for (Object object : extras) {
                        if (object instanceof ScaleColor) {
                            ScaleColor tmp = (ScaleColor) object;
                            String type = tmp.getType();
                            if (!type.equals("fill")) {
                                hasColor = true;
                            } else {
                                hasFill = true;
                            }
                            ArrayList<Integer> colors = tmp.getColors();
                            int max = Math.min(CommonClassesLight.colorBlindFriendlyPalette.length, colors.size());
                            for (int i = 0; i < max; i++) {
                                if (colors.get(i) != CommonClassesLight.colorBlindFriendlyPalette[i]) {
                                    /*
                                     * not using the color blind friendly palette
                                     */
                                    if (!type.equalsIgnoreCase("fill")) {
                                        jCheckBox4.setSelected(true);
                                        replacementColorColors.addAll(colors);
                                        int min = Math.min(replacementColorColors.size(), CommonClassesLight.colorBlindFriendlyPalette.length);
                                        for (int j = 0; j < min; j++) {
                                            replacementColorColors.set(j, CommonClassesLight.colorBlindFriendlyPalette[j]);
                                        }
                                    } else {
                                        jCheckBox5.setSelected(true);
                                        replacementFillColors.addAll(colors);
                                        int min = Math.min(replacementColorColors.size(), CommonClassesLight.colorBlindFriendlyPalette.length);
                                        for (int j = 0; j < min; j++) {
                                            replacementFillColors.set(j, CommonClassesLight.colorBlindFriendlyPalette[j]);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!hasColor) {
                    ArrayList<Object> plots = graph.getPlots();
                    if (plots != null) {
                        int nb_of_colors = 0;
                        for (Object object : plots) {
                            nb_of_colors += ((GeomPlot) object).getNb_of_Color_colors();
                        }
                        int min = Math.min(nb_of_colors, CommonClassesLight.colorBlindFriendlyPalette.length);
                        if (min > 1) {
                            int j;
                            for (j = 0; j < min; j++) {
                                replacementColorColors.add(CommonClassesLight.colorBlindFriendlyPalette[j]);
                            }
                            /**
                             * for the rest add random colors
                             */
                            for (int i = j; i < nb_of_colors; i++) {
                                replacementColorColors.add(CommonClassesLight.new_unique_random_color(replacementColorColors));
                            }
                            jCheckBox4.setSelected(true);
                        }
                    }
                }
                if (!hasFill) {
                    ArrayList<Object> plots = graph.getPlots();
                    if (plots != null) {
                        int nb_of_colors = 0;
                        for (Object object : plots) {
                            nb_of_colors += ((GeomPlot) object).getNb_of_Fill_colors();
                        }
                        int min = Math.min(nb_of_colors, CommonClassesLight.colorBlindFriendlyPalette.length);
                        if (min > 1) {
                            int j;
                            for (j = 0; j < min; j++) {
                                replacementFillColors.add(CommonClassesLight.colorBlindFriendlyPalette[j]);
                            }
                            /**
                             * for the rest add random colors
                             */
                            for (int i = j; i < nb_of_colors; i++) {
                                replacementFillColors.add(CommonClassesLight.new_unique_random_color(replacementFillColors));
                            }
                            jCheckBox5.setSelected(true);
                        }
                    }
                }

                if (!jCheckBox4.isSelected()) {
                    jLabel15.setVisible(false);
                    jButton5.setVisible(false);
                    jCheckBox4.setVisible(false);
                    warning += "<font color=\"#00AA00\">+Your graph (colors) seems to be color blind friendly</font><br>";
                } else {
                    warning += "<font color=\"#FF0000\">-Your graph (colors) may not be color blind friendly (nothing sure though)</font><br>";
                    noError = false;
                }
                if (!jCheckBox5.isSelected()) {
                    jLabel16.setVisible(false);
                    jButton7.setVisible(false);
                    jCheckBox5.setVisible(false);
                    warning += "<font color=\"#00AA00\">+Your graph (colors) seem to be color blind friendly</font><br>";
                } else {
                    warning += "<font color=\"#FF0000\">-Your graph (colors) may not be color blind friendly (nothing sure though)</font><br>";
                    noError = false;
                }
            } else {
                /**
                 * hide color settings
                 */
                jLabel15.setVisible(false);
                jButton5.setVisible(false);
                jCheckBox4.setVisible(false);
                jLabel16.setVisible(false);
                jButton7.setVisible(false);
                jCheckBox5.setVisible(false);
            }
            jEditorPane1.setText(warning);
        } else {
            jEditorPane1.setText("<font color=\"#FF0000\">-Your graph uses custom code, therefore the presence of titles and/units cannot be checked sorry</font><br>");
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
        }
        if (isTitleTextPerfect) {
            jLabel2.setVisible(false);
            title.setVisible(false);
            jButton1.setVisible(false);
            deleteTitle.setVisible(false);
        }
        if (isTitleXaxisPerfect) {
            jLabel4.setVisible(false);
            xAxisTitle.setVisible(false);
            jButton2.setVisible(false);
            addaux.setVisible(false);
            addBracketX.setVisible(false);
        }
        if (isTitleYaxisPerfect) {
            jLabel5.setVisible(false);
            yAxisTitle.setVisible(false);
            jButton3.setVisible(false);
            addauy.setVisible(false);
            addBracketY.setVisible(false);
        }
        if (isTitleLegendPerfect) {
            jLabel6.setVisible(false);
            legendTitle.setVisible(false);
            jButton4.setVisible(false);
            deleteLegendTitle.setVisible(false);
        }
        if (true) {
            //this preview does not require a connection to R we use the svg instead
            BufferedImage preview = graph.getFormattedImageWithoutTranslation(true);
            double AR1 = 297. / preview.getHeight();
            double AR2 = 199. / preview.getWidth();
            BufferedImage tmp = new MyBufferedImage(297, 199, BufferedImage.TYPE_INT_ARGB);
            int width = tmp.getWidth();
            int height = tmp.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    tmp.setRGB(i, j, 0x00000000);
                }
            }
            Graphics2D g2d = tmp.createGraphics();
            CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
            AffineTransform at = new AffineTransform();
            double scale = Math.min(AR2, AR1);
            at.scale(scale, scale);
            g2d.drawImage(preview, at, null);
            g2d.dispose();
            imagePaneLight1.setImage(tmp);
            imagePaneLight1.repaint();
        }
        if (themeShouldBeRepalced) {
            themeChanged(null);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePaneLight1 = new Dialogs.ImagePaneLight();
        imagePaneLight2 = new Dialogs.ImagePaneLight();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        title = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        xAxisTitle = new javax.swing.JTextField();
        yAxisTitle = new javax.swing.JTextField();
        legendTitle = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        Rstatus = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        deleteTitle = new javax.swing.JCheckBox();
        applySuggestedTheme = new javax.swing.JCheckBox();
        addaux = new javax.swing.JCheckBox();
        addauy = new javax.swing.JCheckBox();
        addBracketX = new javax.swing.JCheckBox();
        addBracketY = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        deleteLegendTitle = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout imagePaneLight2Layout = new javax.swing.GroupLayout(imagePaneLight2);
        imagePaneLight2.setLayout(imagePaneLight2Layout);
        imagePaneLight2Layout.setHorizontalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePaneLight2Layout.setVerticalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeChanged(evt);
            }
        });

        jLabel3.setText("Select a theme:");

        jScrollPane1.setViewportView(jEditorPane1);

        title.setEditable(false);

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        xAxisTitle.setEditable(false);

        yAxisTitle.setEditable(false);

        legendTitle.setEditable(false);

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jButton3.setText("Edit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jLabel1.setText("Suggested Theme Preview");

        jLabel2.setText("Title:");

        jLabel4.setText("x Axis:");

        jLabel5.setText("y Axis:");

        jLabel6.setText("Legend:");

        jLabel10.setText("R Fonts Available (con required):");

        jComboBox2.setMinimumSize(new java.awt.Dimension(64, 24));
        jComboBox2.setPreferredSize(new java.awt.Dimension(64, 24));

        Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png"))); // NOI18N
        Rstatus.setText("R con closed");
        Rstatus.setToolTipText("Should be green if connexion to R suceeded should remain red otherwise");
        Rstatus.setBorderPainted(false);
        Rstatus.setFocusable(false);
        Rstatus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Rstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tryConnectToR(evt);
            }
        });

        jButton6.setText("Guidelines to install fonts");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel12.setText("The graph Fonts do not match the journal suggested fonts:");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Replace with recommended fonts");

        deleteTitle.setText("Remove Title");

        applySuggestedTheme.setText("Apply selected/suggested theme");
        applySuggestedTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeChanged(evt);
            }
        });

        addaux.setText("add a.u. if units not found");

        addauy.setText("add a.u. if units not found");

        addBracketX.setText("add brackets around units");

        addBracketY.setText("add brackets around units");

        jLabel13.setText("Recommended line width:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.65f), Float.valueOf(0.01f), null, Float.valueOf(0.01f)));

        jLabel14.setText("Recommended point size:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.65f), Float.valueOf(0.01f), null, Float.valueOf(0.01f)));

        jCheckBox2.setText("Update");

        jCheckBox3.setText("Update");

        deleteLegendTitle.setText("Remove Title");

        jLabel15.setText("<html>Your colors might not be<br>color blind friendly:");

        jLabel16.setText("<html>Your fill colors might<br>not be color blind friendly:");

        jButton5.setText("<html>Select<br>Colors");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeColorColors(evt);
            }
        });

        jCheckBox4.setText("<html>Update<br>Colors");

        jCheckBox5.setText("<html>Update<br>Colors");

        jButton7.setText("<html>Select<br>Colors");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeColorColors(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(imagePaneLight2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applySuggestedTheme))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(title)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteTitle))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(xAxisTitle)
                                    .addComponent(yAxisTitle))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addauy)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addBracketY))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addaux)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addBracketX))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(legendTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteLegendTitle))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Rstatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jCheckBox2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox3)
                                .addGap(16, 16, 16))
                            .addComponent(jCheckBox5)))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Rstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(deleteTitle)
                            .addComponent(jButton1))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(xAxisTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jButton2)
                            .addComponent(addaux)
                            .addComponent(addBracketX))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(yAxisTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)
                            .addComponent(jLabel5)
                            .addComponent(addauy)
                            .addComponent(addBracketY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(legendTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4)
                            .addComponent(jLabel6)
                            .addComponent(deleteLegendTitle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(applySuggestedTheme))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jCheckBox1))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1))))
                    .addComponent(imagePaneLight2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBox1, jSpinner1, jSpinner2});

    }// </editor-fold>//GEN-END:initComponents

    private boolean checkGf() {
        if (jCheckBox1.isSelected()) {
            return checkGf(desiredFont);
        } else {
            return checkGf(currentGraphFonts);
        }
    }

    private boolean checkGf(GraphFont gf) {
        if (gf == null) {
            return true;
        }
        if (CommonClassesLight.isRReady()) {
            ArrayList<String> availableFonts = CommonClassesLight.r.getAvailableFontsInR();
            if (availableFonts != null && !availableFonts.isEmpty()) {
                boolean allCorrect = true;
                String tmp_warning = "Availability in R of the recommended graph fonts:<br>";
                if (!availableFonts.contains(gf.getLegendTitleName())) {
                    tmp_warning += "<font color=\"#FF0000\">-Legend title font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+Legend title font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getLegendTextName())) {
                    tmp_warning += "<font color=\"#FF0000\">-Legend text font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+Legend text font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getTitleSizeName())) {
                    tmp_warning += "<font color=\"#FF0000\">-Title font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+Title font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getXtitleName())) {
                    tmp_warning += "<font color=\"#FF0000\">-X axis title font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+X axis title font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getXaxisName())) {
                    tmp_warning += "<font color=\"#FF0000\">-X axis label font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+X axis label font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getYtitleName())) {
                    tmp_warning += "<font color=\"#FF0000\">-Y axis title font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+Y axis title font is available on your system</font><br>";
                }
                if (!availableFonts.contains(gf.getYaxisName())) {
                    tmp_warning += "<font color=\"#FF0000\">-Y axis label font is not available on your system</font><br>";
                    allCorrect = false;
                } else {
                    tmp_warning += "<font color=\"#00AA00\">+Y axis label font is available on your system</font><br>";
                }
                boolean arial = false;
                if (availableFonts.contains("Arial")) {
                    arial = true;
                }
                if (!allCorrect) {
                    noError = false;
                    warning += tmp_warning;
                }
                if (!allCorrect) {
                    if (arial) {
                        warning += "<font color=\"#00AA00\">+NB: Fonts not found will be replaced by Arial<br>(If the desired font was helvetica, please be aware that it is almost identical to Arial)</font><br>";
                    } else {
                        warning += "<font color=\"#00AA00\">+NB: Fonts not found will be replaced by default R substitution font</font><br>";
                    }
                }
                jEditorPane1.setText(warning);
                return allCorrect;
            }
        } else {
            /**
             * just keep the fonts as they are but R might just not be able to
             * apply them
             */
            warning += "Connection to R failed, the software will assume the specified fonts exist";
            return false;
        }
        return false;
    }

    private void themeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themeChanged
        /*
         * we check the connection
         */
        if (!imagePaneLight2.isVisible()) {
            return;
        }
        ThemeGraph theme = null;
        if (jComboBox1.getSelectedIndex() != -1) {
            theme = PopulateThemes.themes.get(jComboBox1.getSelectedIndex());
            selectedTheme = theme;
        }
        checkRstatus();
        /**
         * update the theme preview
         */
        if (CommonClassesLight.getR() == null || !CommonClassesLight.isRReady()) {
            BufferedImage black = new BufferedImage(imagePaneLight1.getSize().width, imagePaneLight1.getSize().height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = black.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.drawString("R connection Failed", 80, 80);
            g2d.drawString("No Preview Available", 80, 148);
            g2d.dispose();
            imagePaneLight1.setImage(black);
        } else {
            /**
             * we create some basic/generic plot code
             */
            String simplePlot = "ggplot(mtcars, aes(x=hp, y=mpg))"
                    + "\n+ geom_point(aes(colour=factor(cyl)))"
                    + "\n+ scale_colour_discrete(name=\"cyl\")"
                    //+ "\n+ labs(colour = NULL)"
                    + "\n+ facet_grid(cyl ~ .)"
                    + "\n+ ggtitle('title')";
            GraphFont font = jCheckBox1.isSelected() ? desiredFont : currentGraphFonts;
            if (font != null) {
                simplePlot += "\n+ " + font.toString();
            }
            if (!applySuggestedTheme.isSelected()) {
                theme = currentTheme;
            }
            if (theme != null) {
                if (!theme.toString().trim().equals("")) {
                    simplePlot += "\n+" + theme;
                }
            }
            imagePaneLight2.setImage(CommonClassesLight.r.getPreview(simplePlot, 297, 180));
        }
    }//GEN-LAST:event_themeChanged

    private void editText(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editText
        Object src = evt.getSource();
        if (src == jButton1) {
            RLabelEditor iopane = new RLabelEditor(CommonClassesLight.r, t == null ? new RLabel() : t);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                t = iopane.getRlabel();
                title.setText(t.toRExpression());
            }
        } else if (src == jButton2) {
            RLabelEditor iopane = new RLabelEditor(CommonClassesLight.r, x == null ? new RLabel() : x);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                x = iopane.getRlabel();
                xAxisTitle.setText(x.toRExpression());
            }
        } else if (src == jButton3) {
            RLabelEditor iopane = new RLabelEditor(CommonClassesLight.r, y == null ? new RLabel() : y);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                y = iopane.getRlabel();
                yAxisTitle.setText(y.toRExpression());
            }
        } else if (src == jButton4) {
            RLabelEditor iopane = new RLabelEditor(CommonClassesLight.r, l == null ? new RLabel() : l);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                l = iopane.getRlabel();
                legendTitle.setText(l.toRExpression());
            }
        }
    }//GEN-LAST:event_editText

    public void checkRstatus() {
        if (CommonClassesLight.r != null && CommonClassesLight.isRReady()) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            Rstatus.setText("R con opened");
        } else {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
            Rstatus.setText("R con closed");
        }
    }

    /**
     *
     * @return the connection to R
     */
    public void getRserver() {
        if (CommonClassesLight.r == null || !CommonClassesLight.r.isRserverRunning()) {
            try {
                ScientiFig.reinitRsession();
            } catch (Exception e) {
            }
        }
        checkRstatus();
    }

    private void tryConnectToR(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tryConnectToR
        getRserver();
        if (!CommonClassesLight.isRReady()) {
            jButton6ActionPerformed(null);
        } else {
            CommonClassesLight.r.reloadRFonts(jComboBox2);
            checkGf();
        }
    }//GEN-LAST:event_tryConnectToR

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        CommonClassesLight.Warning2(this, MyRsessionLogger.generateRInstallationText());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void changeColorColors(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeColorColors
        if (evt.getSource() == jButton5) {
            ColorSelector iopane = new ColorSelector(replacementColorColors.size(), replacementColorColors);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Colors (Work in progress)", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                replacementColorColors = iopane.getColors();
            }
        } else {
            ColorSelector iopane = new ColorSelector(replacementFillColors.size(), replacementFillColors);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Colors (Work in progress)", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                replacementFillColors = iopane.getColors();
            }
        }
    }//GEN-LAST:event_changeColorColors

    public MyPlotVector.Double getSolution() {
        if (deleteTitle.isSelected()) {
            graph.setTitleLabel(new RLabel());//we empty the title label
        }
        if (deleteLegendTitle.isSelected()) {
            graph.setLegendLabel(new RLabel());//we empty the title label
        }
        if (applySuggestedTheme.isSelected()) {
            graph.setTheme(selectedTheme);
        } else {
            graph.setTheme(currentTheme);
        }
        if (!isTitleXaxisPerfect) {
            graph.setxAxisLabel(x);
        }
        if (!isTitleYaxisPerfect) {
            graph.setyAxisLabel(y);
        }
        if (addBracketX.isSelected()) {
            RLabel r = graph.getxAxisLabel();
            if (r != null) {
                r.setAutoBracket(true);
            }
            graph.setxAxisLabel(r);
        }
        if (addBracketY.isSelected()) {
            RLabel r = graph.getyAxisLabel();
            if (r != null) {
                r.setAutoBracket(true);
            }
            graph.setyAxisLabel(r);
        }
        if (addaux.isSelected()) {
            RLabel r = graph.getxAxisLabel();
            if (r != null) {
                if (!r.hasUnit()) {
                    r.setUnit(new ColoredTextPaneSerializable("a.u."));
                }
            }
            graph.setxAxisLabel(r);
        }
        if (addauy.isSelected()) {
            RLabel r = graph.getyAxisLabel();
            if (r != null) {
                if (!r.hasUnit()) {
                    r.setUnit(new ColoredTextPaneSerializable("a.u."));
                }
            }
            graph.setyAxisLabel(r);
        }
        if (jCheckBox2.isSelected()) {
            graph.setLineStrokeSize(getLineStrokeValue());
        }
        if (jCheckBox3.isSelected()) {
            graph.setPointSize(getPointSizeValue());
        }
        if (jCheckBox4.isSelected()) {
            graph.updateColorColors(replacementColorColors);
        }
        if (jCheckBox5.isSelected()) {
            graph.updateFillColors(replacementFillColors);
        }
        /*
         * try to update SVG if we are connected to R
         */
        graph.updatePlot();
        return graph;
    }

    public float getLineStrokeValue() {
        return ((Float) jSpinner1.getValue());
    }

    public float getPointSizeValue() {
        return ((Float) jSpinner2.getValue());
    }

    public static void main(String[] args) {
        MyRsessionLogger r = new MyRsessionLogger();
        try {
            r.reopenConnection();
        } catch (REXPMismatchException ex) {
            ex.printStackTrace();
        }
        Font ft = new Font("Arial", 0, 12);//-->existing font
        //Font ft = new Font("Tamama", 0, 12);//-->non existing font
        GraphFont customTest = new GraphFont(ft, ft, ft, ft, ft, ft, ft, 1, 1, true, true, true, true, true, true, true, true);
        PopulateJournalStyles journals = new PopulateJournalStyles();
        JournalParameters jp = new JournalParameters();
        jp.setGf(customTest);
        //testWrongColors.figur
//        MyPlotVector.Double graph = (MyPlotVector.Double) new Loader().loadObjectRaw("/home/benoit/Bureau/testWrongColors.figur");
//        MyPlotVector.Double graph = (MyPlotVector.Double) new Loader().loadObjectRaw("/home/benoit/Bureau/neo_fig_for_tests.figur");//--> graph without theme
//        MyPlotVector.Double graph = (MyPlotVector.Double) new Loader().loadObjectRaw("/home/benoit/Bureau/neo_fig_for_tests_alpha_1.figur");//--> graph without theme        
        MyPlotVector.Double graph = (MyPlotVector.Double) new Loader().loadObjectRaw("/home/benoit/Bureau/test_new_graph_for_check_with_error_bars.figur");//--> graph without theme        
//        MyPlotVector.Double graph = (MyPlotVector.Double) new Loader().loadObjectRaw("C:\\Users\\benoit\\Desktop\\neo_fig_line.figur");//--> graph with theme //neo_fig_line_with_theme
        //CheckGraphs iopane = new CheckGraphs(graph, PopulateJournalStyles.journalStyles.get(6));//--> does not match
        CheckGraphs iopane = new CheckGraphs(graph, jp/*PopulateJournalStyles.journalStyles.get(0)*/);//--> does not match
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Check graph", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                //--> resize the image
//                double size = iopane.getFinalSize();
//                System.out.println("size --> " + size);
                graph = iopane.getSolution();
                SaverLight.popJ(graph.getFormattedImageWithoutTranslation(true));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
            }
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Rstatus;
    private javax.swing.JCheckBox addBracketX;
    private javax.swing.JCheckBox addBracketY;
    private javax.swing.JCheckBox addaux;
    private javax.swing.JCheckBox addauy;
    private javax.swing.JCheckBox applySuggestedTheme;
    private javax.swing.JCheckBox deleteLegendTitle;
    private javax.swing.JCheckBox deleteTitle;
    private Dialogs.ImagePaneLight imagePaneLight1;
    private Dialogs.ImagePaneLight imagePaneLight2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextField legendTitle;
    private javax.swing.JTextField title;
    private javax.swing.JTextField xAxisTitle;
    private javax.swing.JTextField yAxisTitle;
    // End of variables declaration//GEN-END:variables
}


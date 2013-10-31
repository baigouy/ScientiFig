/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2013 Benoit Aigouy 

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

import R.GeomPlot;
import Commons.CommonClassesLight;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * PlotSelectorDialog lets you select parameters for your ggplot2 plots
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class PlotSelectorDialog extends javax.swing.JPanel {

    String plotStyle;
    /**
     * error bar parameters
     */
    private static final int BOTH = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;

    /**
     * Constructor
     *
     * @param gp font, line width and point size parameteres for the graph
     * @param col list of all columns that can be used in plots
     * @param aleady_factorizable_columns columns that can be used as factors
     * @param safeMode if true factors can only be selected from the
     * aleady_factorizable_columns list, otherwise they ccan be selected form
     * the col lise
     */
    public PlotSelectorDialog(GeomPlot gp, ArrayList<String> col, ArrayList<String> aleady_factorizable_columns, boolean safeMode) {
        /*
         * we edit an existing plot
         */
        this(gp.getGeomType(), col, aleady_factorizable_columns, safeMode, (gp.getLineType() != null), (gp.getShape() != null));
        /*
         * we recover the line width parameter
         */
        if (gp.getSize() != null) {
            try {
                lineDialog1.setLineWidth(CommonClassesLight.String2Double(gp.getSize()));
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
        /*
         * here we recover and reapply the line style
         */
        if (gp.getLineType() != null) {
            try {
                lineDialog1.setLineStyle(CommonClassesLight.String2Int(gp.getLineType()));
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
        if (gp.getPosition() != null) {
            jComboBox12.setSelectedItem(gp.getPosition());
        }
        if (gp.getBinWidth() != null) {
            jCheckBox1.doClick();
            try {
                jSpinner1.setValue(CommonClassesLight.String2Float(gp.getBinWidth()));
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
        if (gp.getX() != null) {
            jComboBox1.setSelectedItem(gp.getX());
        }
        if (gp.getY() != null) {
            jComboBox2.setSelectedItem(gp.getY());
        }
        if (gp.getAlpha() != null) {
            jSpinner2.setValue(CommonClassesLight.String2Float(gp.getAlpha()));
        }
        if (gp.getShape() != null) {
            pointDialog1.setSelectedShape(CommonClassesLight.String2Int(gp.getShape()));
        }
        if (gp.getAesShape() != null) {
            jComboBox5.setSelectedItem(gp.getAesShape());
        }
        if (gp.getXend() != null) {
            jComboBox6.setSelectedItem(gp.getXend());
        }
        if (gp.getYend() != null) {
            jComboBox7.setSelectedItem(gp.getYend());
        }
        if (gp.getXmax() != null) {
            if (!plotStyle.toLowerCase().contains("error")) {
                jComboBox10.setSelectedItem(gp.getXmax());
            } else {
                try {
                    //use regex to try to reload the thing properly
                    if (gp.getXmax().contains("sd(")) {
                        jRadioButton11.doClick();
                        if (gp.getXmax().contains("sqrt(length(")) {
                            jComboBox17.setSelectedItem("sem");
                        }
                    } else {
                        if (gp.getXmax().contains("+")) {
                            jRadioButton13.doClick();
                            Pattern p = Pattern.compile("\\+as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getXmax());
                            if (m.find()) {
                                jComboBox13.setSelectedItem(m.group(1));
                            }
                        } else {
                            jRadioButton12.doClick();
                            Pattern p = Pattern.compile("as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getXmax());
                            if (m.find()) {
                                jComboBox16.setSelectedItem(m.group(1));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (gp.getXmin() != null) {

            if (!plotStyle.toLowerCase().contains("error")) {
                jComboBox8.setSelectedItem(gp.getXmin());
            } else {
                try {
                    //use regex to try to reload the thing properly
                     if (gp.getXmin().contains("sd(")) {
                        jRadioButton11.doClick();
                        if (gp.getXmin().contains("sqrt(length(")) {
                            jComboBox17.setSelectedItem("sem");
                        }
                    } else {
                        if (gp.getXmin().contains("-")) {
                            jRadioButton13.doClick();
                            Pattern p = Pattern.compile("-as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getXmin());
                            if (m.find()) {
                                jComboBox14.setSelectedItem(m.group(1));
                            }
                        } else {
                            jRadioButton12.doClick();
                            Pattern p = Pattern.compile("as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getXmin());
                            if (m.find()) {
                                jComboBox15.setSelectedItem(m.group(1));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (gp.getYmax() != null) {

            if (!plotStyle.toLowerCase().contains("error")) {
                jComboBox11.setSelectedItem(gp.getYmax());
            } else {
                try {
                    //use regex to try to reload the thing properly
                    if (gp.getYmax().contains("sd(")) {
                        jRadioButton11.doClick();
                        if (gp.getYmax().contains("sqrt(length(")) {
                            jComboBox17.setSelectedItem("sem");
                        }
                    } else {
                        if (gp.getYmax().contains("+")) {
                            jRadioButton13.doClick();
                            Pattern p = Pattern.compile("\\+as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getYmax());
                            if (m.find()) {
                                jComboBox13.setSelectedItem(m.group(1));
                            }
                        } else {
                            jRadioButton12.doClick();
                            Pattern p = Pattern.compile("as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getYmax());
                            if (m.find()) {
                                jComboBox16.setSelectedItem(m.group(1));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (gp.getYmin() != null) {
            if (!plotStyle.toLowerCase().contains("error")) {
                jComboBox9.setSelectedItem(gp.getYmin());
            } else {
                try {
                    //use regex to try to reload the thing properly
                    if (gp.getYmin().contains("sd(")) {
                        jRadioButton11.doClick();
                        if (gp.getYmin().contains("sqrt(length(")) {
                            jComboBox17.setSelectedItem("sem");
                        }
                    } else {
                        if (gp.getYmin().contains("-")) {
                            jRadioButton13.doClick();
                            Pattern p = Pattern.compile("-as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getYmin());
                            if (m.find()) {
                                jComboBox14.setSelectedItem(m.group(1));
                            }
                        } else {
                            jRadioButton12.doClick();
                            Pattern p = Pattern.compile("as.numeric\\((.*)\\)");
                            Matcher m = p.matcher(gp.getYmin());
                            if (m.find()) {
                                jComboBox15.setSelectedItem(m.group(1));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (gp.getRegression() != null) {
            smoothDialog1.setSE(gp.getSE());
            smoothDialog1.setRegression(gp.getRegression());
            smoothDialog1.setFamily(gp.getFamily());
            smoothDialog1.setFormula(gp.getFormula());
        }
        if (gp.getAesColor() != null) {
            if (gp.getAesColor().contains("\"")) {
                jTextField1.setText(gp.getAesColor().replace("\"", ""));
                jRadioButton2.doClick();
            } else {
                jComboBox3.setSelectedItem(gp.getAesColor());
            }
        }
        if (gp.getAesFill() != null) {
            if (gp.getAesFill().contains("\"")) {
                jTextField2.setText(gp.getAesFill().replace("\"", ""));
                jRadioButton3.doClick();
            } else {
                jComboBox4.setSelectedItem(gp.getAesFill());
            }
        }
        if (gp.getAesShape() != null) {
            jComboBox5.setSelectedItem(gp.getAesShape());
        }
    }

    /**
     * Constructor
     *
     * @param plotStyle the type of R plot (histogram, line plot, scatterplot,
     * ...)
     * @param col list of all columns that can be used in plots
     * @param aleady_factorizable_columns columns that can be used as factors
     * @param safeMode if true factors can only be selected from the
     * aleady_factorizable_columns list, otherwise they ccan be selected form
     * the col lise
     * @param line_mode if true, the plot is a line plot
     * @param point_mode if true the plot is a scatterplot
     */
    public PlotSelectorDialog(String plotStyle, ArrayList<String> col, ArrayList<String> aleady_factorizable_columns, boolean safeMode, boolean line_mode, boolean point_mode) {
        initComponents();
        this.plotStyle = plotStyle;
        if (!line_mode) {
            lineDialog1.setVisible(false);
        }
        if (!point_mode) {
            pointDialog1.setVisible(false);
        }
        String[] columns = col.toArray(new String[col.size()]);
        String[] already_factorizable = aleady_factorizable_columns.toArray(new String[aleady_factorizable_columns.size()]);
        String[] modified_table;
        if (safeMode) {
            int begin = 1;
            modified_table = new String[already_factorizable.length + 1];
            modified_table[0] = "None";
            for (String string : already_factorizable) {
                modified_table[begin] = string;
                begin++;
            }
        } else {
            int begin = 1;
            modified_table = new String[columns.length + 1];
            modified_table[0] = "None";
            for (String string : columns) {
                modified_table[begin] = string;
                begin++;
            }
        }
        if (plotStyle.contains("error")) {
            hideFill(true);
        }
        if (plotStyle.contains("line")) {
            hideFill(true);
            hideShape(true);
        }
        if (plotStyle.contains("point")) {
            hideFill(true);
        }
        jComboBox1.setModel(new DefaultComboBoxModel(columns));
        try {
            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (plotStyle.equals("geom_bar") || plotStyle.contains("density")) {
            hideBinWidth(false);
            hideShape(true);
            hideDodge(false);
        } else {
            if (!plotStyle.contains("geom_bar")) {
                hideDodge(true);
            } else {
                hideDodge(false);
            }
            hideBinWidth(true);
        }
        if (plotStyle.contains("bar") || plotStyle.contains("boxplot")) {
            hideShape(true);
        }
        if (!plotStyle.contains("smooth")) {
            hideSmooth(true);
        } else {
            hideShape(true);
        }
        if (plotStyle.equals("geom_bar") || plotStyle.contains("density")) {
            String[] options = {/*"None", */"..density..", "..count.."};//par defaut c'est le compte
            jComboBox2.setModel(new DefaultComboBoxModel(options));
        } else {
            jComboBox2.setModel(new DefaultComboBoxModel(columns));
            try {
                jComboBox2.setSelectedIndex(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!plotStyle.contains("geom_errorbar")) {
            jPanel5.setVisible(false);
        } else {
            jLabel19.setText((plotStyle.equals("geom_errorbar") ? "x" : "y") + jLabel19.getText());
            jLabel20.setText((plotStyle.equals("geom_errorbar") ? "x" : "y") + jLabel20.getText());
            if (plotStyle.equals("geom_errorbar")) {
                jRadioButton1.setText("Left");
                jRadioButton4.setText("Right");
            }
            jComboBox13.setModel(new DefaultComboBoxModel(columns));
            jComboBox14.setModel(new DefaultComboBoxModel(columns));
            jComboBox15.setModel(new DefaultComboBoxModel(columns));
            jComboBox16.setModel(new DefaultComboBoxModel(columns));
        }
        if (!plotStyle.contains("_rect")) {
            jComboBox8.setVisible(false);
            jComboBox9.setVisible(false);
            jComboBox10.setVisible(false);
            jComboBox11.setVisible(false);
            jLabel11.setVisible(false);
            jLabel10.setVisible(false);
            jLabel12.setVisible(false);
            jLabel13.setVisible(false);
        } else {
            jLabel1.setVisible(false);
            jLabel2.setVisible(false);
            jComboBox1.setVisible(false);
            jComboBox2.setVisible(false);
            jComboBox8.setModel(new DefaultComboBoxModel(columns));
            jComboBox8.setSelectedIndex(0);
            jComboBox9.setModel(new DefaultComboBoxModel(columns));
            jComboBox9.setSelectedIndex(0);
            jComboBox10.setModel(new DefaultComboBoxModel(columns));
            jComboBox10.setSelectedIndex(0);
            jComboBox11.setModel(new DefaultComboBoxModel(columns));
            jComboBox11.setSelectedIndex(0);
            hideShape(true);
            hideBinWidth(true);
        }

        if (!plotStyle.contains("segment")) {
            jComboBox6.setVisible(false);
            jComboBox7.setVisible(false);
            jLabel8.setVisible(false);
            jLabel9.setVisible(false);
        } else {
            jLabel1.setText("xBegin:");
            jLabel2.setText("yBegin:");
            jComboBox6.setModel(new DefaultComboBoxModel(columns));
            jComboBox6.setSelectedIndex(0);
            jComboBox7.setModel(new DefaultComboBoxModel(columns));
            jComboBox7.setSelectedIndex(0);
            hideShape(true);
            hideFill(true);
            hideBinWidth(true);
        }
        jComboBox3.setModel(new DefaultComboBoxModel(modified_table));
        jComboBox4.setModel(new DefaultComboBoxModel(modified_table));
        jComboBox5.setModel(new DefaultComboBoxModel(modified_table));
    }

    /**
     *
     * @return true if the histogram dodging parameters are accessible
     */
    public boolean isDodgeHidden() {
        return !jComboBox12.isVisible();
    }

    /**
     * @return whether the smooth should also display a a confidence interval
     */
    public String getSE() {
        if (isSmoothHidden()) {
            return null;
        }
        return smoothDialog1.getSE();
    }

    public String getRegressioMethod() {
        if (isSmoothHidden()) {
            return null;
        }
        return smoothDialog1.getSmoothType();
    }

    /**
     *
     * @return true if the filling parameters are accessible
     */
    public boolean isSFillHidden() {
        return !jComboBox4.isVisible();
    }

    /**
     *
     * @return true if the smooth/regression parameters are accessible
     */
    public boolean isSmoothHidden() {
        return !smoothDialog1.isVisible();
    }

    /**
     *
     * @return true if the point shape parameters are accessible
     */
    public boolean isShapeHidden() {
        return !jComboBox5.isVisible();
    }

    /**
     * hide the point shape parameters
     *
     * @param bool
     */
    public final void hideShape(boolean bool) {
        jLabel5.setVisible(!bool);
        jComboBox5.setVisible(!bool);
    }

    /**
     *
     * @return dodging parameters
     */
    public String getDodged() {
        if (isDodgeHidden()) {
            return null;
        }
        return getComboSelection(jComboBox12);
    }

    /**
     *
     * @return stats
     */
    public String getStat() {
        if (plotStyle == null || (!plotStyle.equals("geom_bar2") && !plotStyle.equals("geom_bar3"))) {
            return null;
        }
        return "identity";
    }

    /**
     *
     * @return line size
     */
    public String getLineSize() {
        if (!lineDialog1.isVisible()) {
            return null;
        }
        return lineDialog1.getLineWidth() + "";
    }

    /**
     *
     * @return line type (dashed, solid, ...)
     */
    public String getLineType() {
        if (!lineDialog1.isVisible()) {
            return null;
        }
        return lineDialog1.getLineStyle() + "";
    }

    /**
     *
     * @return point shpae
     */
    public String getPointType() {
        if (!pointDialog1.isVisible()) {
            return null;
        }
        return pointDialog1.getSelectedShape() + "";
    }

    /**
     * hide the dodging parameters
     *
     * @param bool
     */
    public final void hideDodge(boolean bool) {
        jLabel14.setVisible(!bool);
        jComboBox12.setVisible(!bool);
    }

    /**
     * hide the filling parameters
     *
     * @param bool
     */
    public final void hideFill(boolean bool) {
        jLabel4.setVisible(!bool);
        jComboBox4.setVisible(!bool);
        basedOnColContent1.setVisible(!bool);
        jRadioButton3.setVisible(!bool);
        jTextField2.setVisible(!bool);
    }

    /**
     * hide the smooth/regression parameters
     *
     * @param bool
     */
    public final void hideSmooth(boolean bool) {
        smoothDialog1.setVisible(!bool);
    }

    /**
     * hide the bin width parameters
     *
     * @param bool
     */
    public final void hideBinWidth(boolean bool) {
        jLabel6.setVisible(!bool);
        jCheckBox1.setVisible(!bool);
        jSpinner1.setVisible(!bool);
    }

    private boolean hasBinWidth() {
        return jSpinner1.isVisible() && jSpinner1.isEnabled();
    }

    /**
     *
     * @return the column corresponding to the x axis
     */
    public String getXaxis() {
        if (!jComboBox1.isVisible()) {
            return null;
        }
        if (getStat() != null) {
            return "as.factor(" + getComboSelection(jComboBox1) + ")";
        } else {
            return getComboSelection(jComboBox1);
        }
    }

    /**
     *
     * @return the column corresponding to the x axis
     */
    public String getYaxis() {
        if (!jComboBox2.isVisible()) {
            return null;
        }
        if (!jComboBox2.isEnabled()) {
            return null;
        }
        return getComboSelection(jComboBox2);
    }

    /**
     *
     * @return true if the plot is an arrow or a nematic plot
     */
    public boolean isArrowOrNematic() {
        return jComboBox6.isVisible();
    }

    /**
     *
     * @return true if the plot is a geom_rect plot
     */
    public boolean isRect() {
        return jComboBox10.isVisible();
    }

    /**
     *
     * @return the Xend column
     */
    public String getXend() {
        if (isArrowOrNematic()) {
            return jComboBox6.getSelectedItem().toString();
        }
        return null;
    }

    /**
     *
     * @return the Yend column
     */
    public String getYend() {
        if (isArrowOrNematic()) {
            return jComboBox7.getSelectedItem().toString();
        }
        return null;
    }

    public boolean isErrorBar() {
        return jPanel5.isVisible();
    }

    /**
     *
     * @return the Xmin column
     */
    public String getXmin() {
        if (isRect()) {
            return jComboBox8.getSelectedItem().toString();
        }
        if (isErrorBar()) {
            return getMin();
        }
        return null;
    }

    /**
     *
     * @return the Ymin column
     */
    public String getYmin() {
        if (isRect()) {
            return jComboBox9.getSelectedItem().toString();
        }
        if (isErrorBar()) {
            return getMin();
        }
        return null;
    }

    /**
     *
     * @return the Xmax column
     */
    public String getXmax() {
        if (isRect()) {
            return jComboBox10.getSelectedItem().toString();
        }
        if (isErrorBar()) {
            return getMax();
        }
        return null;
    }

    /**
     *
     * @return the Ymax column
     */
    public String getYmax() {
        if (isRect()) {
            return jComboBox11.getSelectedItem().toString();
        }
        if (isErrorBar()) {
            return getMax();
        }
        return null;
    }

    /**
     *
     * @return the fill aesthetics
     */
    public String getAesFill() {
        if (isSFillHidden()) {
            return null;
        }
        if (basedOnColContent1.isSelected()) {
            if (getStat() != null && getComboSelection(jComboBox4) == null) {
                return getComboSelection(jComboBox1);
            }
            return getComboSelection(jComboBox4);
        } else {
            if (jTextField2.getText().equals("")) {
                return null;
            }
            return "\"" + jTextField2.getText() + "\"";
        }
    }

    /**
     *
     * @return the color aesthetics
     */
    public String getAesColor() {
        if (basedOnColContent.isSelected()) {
            return getComboSelection(jComboBox3);
        } else {
            if (jTextField1.getText().equals("")) {
                return null;
            }
            return "\"" + jTextField1.getText() + "\"";
        }
    }

    /**
     *
     * @return the shape aesthetics
     */
    public String getShape() {
        if (isShapeHidden()) {
            return null;
        }
        return getComboSelection(jComboBox5);
    }

    private String getComboSelection(JComboBox combo) {
        if (combo.getSelectedItem().toString().toLowerCase().equals("none")) {
            return null;
        }
        return combo.getSelectedItem().toString();
    }

    /**
     *
     * @return the alpha transparency
     */
    public String getAlpha() {
        if (alpha() != 1.f) {
            return alpha() + "";
        }
        return null;
    }

    private float alpha() {
        return ((Float) jSpinner2.getValue()).floatValue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        basedOnColContent = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jRadioButton3 = new javax.swing.JRadioButton();
        basedOnColContent1 = new javax.swing.JRadioButton();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jComboBox11 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        lineDialog1 = new Dialogs.LineDialog();
        pointDialog1 = new Dialogs.PointDialog();
        smoothDialog1 = new Dialogs.SmoothDialog();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jRadioButton13 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox17 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jComboBox15 = new javax.swing.JComboBox();
        jComboBox16 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox13 = new javax.swing.JComboBox();
        jComboBox14 = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Select Data to be Plotted"));

        jLabel1.setText("x:");

        jLabel2.setText("y:");

        jLabel3.setText("label/color:");

        buttonGroup1.add(basedOnColContent);
        basedOnColContent.setSelected(true);
        basedOnColContent.setText("<html>Based on<br> col content</html>");
        basedOnColContent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorModeChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Manual label");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorModeChanged(evt);
            }
        });

        jTextField1.setEnabled(false);
        jTextField1.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextField1.setPreferredSize(new java.awt.Dimension(120, 19));

        jTextField2.setEnabled(false);
        jTextField2.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextField2.setName(""); // NOI18N
        jTextField2.setPreferredSize(new java.awt.Dimension(120, 19));

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("Manual label");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillBasedOnColContent(evt);
            }
        });

        buttonGroup2.add(basedOnColContent1);
        basedOnColContent1.setSelected(true);
        basedOnColContent1.setText("<html>Based on<br> col content</html>");
        basedOnColContent1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillBasedOnColContent(evt);
            }
        });

        jLabel4.setText("label/fill:");

        jLabel7.setText("alpha:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.100000024f)));

        jLabel5.setText("shape:");

        jLabel6.setText("Bin Width:");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Auto");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualBinWidth(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), null, null, Float.valueOf(1.0f)));
        jSpinner1.setEnabled(false);

        jLabel8.setText("xEnd:");

        jLabel9.setText("yEnd:");

        jLabel10.setText("xMin:");

        jLabel11.setText("yMin:");

        jLabel12.setText("xMax:");

        jLabel13.setText("yMax:");

        jLabel14.setText("Bar/Density positioning:");

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "dodge", "identity", "stack", "fill", "jitter" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(3, 3, 3)
                        .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(7, 7, 7)
                        .addComponent(jSpinner2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox11, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(basedOnColContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(basedOnColContent1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jRadioButton3)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3)
                    .addComponent(basedOnColContent)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(basedOnColContent1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jCheckBox1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {basedOnColContent, jComboBox3, jLabel3, jRadioButton2, jTextField1});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {basedOnColContent1, jComboBox4, jLabel4, jRadioButton3, jTextField2});

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Error bar parameters"));

        jLabel15.setText("Bar Width:");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(0.1d)));

        jLabel16.setText("Bar position:");
        jLabel16.setEnabled(false);

        buttonGroup5.add(jRadioButton1);
        jRadioButton1.setText("Top");
        jRadioButton1.setEnabled(false);

        buttonGroup5.add(jRadioButton4);
        jRadioButton4.setText("Bottom");
        jRadioButton4.setEnabled(false);

        buttonGroup5.add(jRadioButton5);
        jRadioButton5.setSelected(true);
        jRadioButton5.setText("Both");
        jRadioButton5.setEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner3))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton5)
                        .addGap(0, 342, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5)))
        );

        buttonGroup4.add(jRadioButton11);
        jRadioButton11.setSelected(true);
        jRadioButton11.setText("Compute Directly From Data");
        jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorDataChanged(evt);
            }
        });

        buttonGroup4.add(jRadioButton12);
        jRadioButton12.setText("Manual width user values");
        jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorDataChanged(evt);
            }
        });

        buttonGroup4.add(jRadioButton13);
        jRadioButton13.setText("Manual width user +/- values");
        jRadioButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorDataChanged(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Compute Directly From Data"));

        jLabel21.setText("Choose error bar type:");

        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "sd", "sem" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox17, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel21)
                .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Manual width user values"));
        jPanel2.setEnabled(false);

        jLabel19.setText(" min");
        jLabel19.setEnabled(false);

        jLabel20.setText(" max");
        jLabel20.setEnabled(false);

        jComboBox15.setEnabled(false);

        jComboBox16.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox16, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Manual width user +/- values"));
        jPanel3.setEnabled(false);

        jLabel17.setText("+");
        jLabel17.setEnabled(false);

        jLabel18.setText("-");
        jLabel18.setEnabled(false);

        jComboBox13.setEnabled(false);

        jComboBox14.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox14, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jRadioButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton13))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton11)
                    .addComponent(jRadioButton12)
                    .addComponent(jRadioButton13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(smoothDialog1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pointDialog1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lineDialog1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineDialog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pointDialog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smoothDialog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @return true whether we should let R calculate the binning for us
     */
    public boolean isAutoBin() {
        return jCheckBox1.isSelected();
    }

    private void manualBinWidth(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualBinWidth
        boolean autoBin = isAutoBin();
        jSpinner1.setEnabled(!autoBin);
    }//GEN-LAST:event_manualBinWidth

    private void colorModeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorModeChanged
        boolean mode = basedOnColContent.isSelected();
        jComboBox3.setEnabled(mode);
        jTextField1.setEnabled(!mode);
    }//GEN-LAST:event_colorModeChanged

    private void fillBasedOnColContent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillBasedOnColContent
        boolean mode = basedOnColContent1.isSelected();
        jComboBox4.setEnabled(mode);
        jTextField2.setEnabled(!mode);
    }//GEN-LAST:event_fillBasedOnColContent

    private void errorDataChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorDataChanged
        if (jRadioButton11.isSelected()) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel4, true);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, false);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel3, false);
        } else if (jRadioButton12.isSelected()) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel4, false);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel3, false);
        } else {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel4, false);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, false);
            CommonClassesLight.enableOrDisablePanelComponents(jPanel3, true);
        }
    }//GEN-LAST:event_errorDataChanged

    /**
     *
     * @return the width of bins
     */
    public String getBinWidth() {
        if (hasBinWidth()) {
            return ((Float) jSpinner1.getValue()).floatValue() + "";
        }
        return null;
    }

    public String getFormula() {
        return smoothDialog1.getFormula();
    }

    public String getFamily() {
        return smoothDialog1.getFamily();
    }

    /**
     *
     * @return true if error bars should be horizontal
     */
    public boolean isHorizontalBar() {
        return jRadioButton1.isSelected();
    }

    /**
     *
     * @return true if error bars should be vertical
     */
    public boolean isVerticalBar() {
        return jRadioButton3.isSelected();
    }

    /**
     *
     * @return the type of error bars (only top, only bottom or both)
     */
    public int getVerticalOrientation() {
        if (jRadioButton1.isSelected()) {
            return TOP;
        } else if (jRadioButton3.isSelected()) {
            return BOTTOM;
        } else {
            return BOTH;
        }
    }

    /**
     *
     * @return the min value for error bars
     */
    public String getMin() {
        String txt = null;
        if (plotStyle.equals("geom_errorbar")) {
            if (jPanel4.isEnabled()) {
                if (jComboBox17.getSelectedItem().toString().contains("em")) {
                    txt = getYaxis() + "-sd(" + getYaxis() + ")/sqrt(length(" + getYaxis() + "))";
                } else {
                    txt = getYaxis() + "-sd(" + getYaxis() + ")";
                }
            } else if (jPanel2.isEnabled()) {
                return "as.numeric(" + jComboBox15.getSelectedItem().toString() + ")";
            } else if (jPanel3.isEnabled()) {
                return "as.numeric(" + getYaxis() + ")" + "-as.numeric(" + jComboBox14.getSelectedItem().toString() + ")";
            }
        } else {
            if (jPanel4.isEnabled()) {
                if (jComboBox17.getSelectedItem().toString().contains("em")) {
                    txt = getXaxis() + "-sd(" + getXaxis() + ")/sqrt(length(" + getXaxis() + "))";
                } else {
                    txt = getXaxis() + "-sd(" + getXaxis() + ")";
                }
            } else if (jPanel2.isEnabled()) {
                return "as.numeric(" + jComboBox15.getSelectedItem().toString() + ")";
            } else if (jPanel3.isEnabled()) {
                return "as.numeric(" + getXaxis() + ")" + "-as.numeric(" + jComboBox14.getSelectedItem().toString() + ")";
            }
        }
        if (jRadioButton1.isSelected()) {
            return null;
        }
        return txt;
    }

    /**
     *
     * @return the max value for error bars
     */
    public String getMax() {
        String txt = null;
        if (plotStyle.equals("geom_errorbar")) {
            if (jPanel4.isEnabled()) {
                if (jComboBox17.getSelectedItem().toString().contains("em")) {
                    txt = getYaxis() + "+sd(" + getYaxis() + ")/sqrt(length(" + getYaxis() + "))";
                } else {
                    txt = getYaxis() + "+sd(" + getYaxis() + ")";
                }
            } else if (jPanel2.isEnabled()) {
                return "as.numeric(" + jComboBox16.getSelectedItem().toString() + ")";
            } else if (jPanel3.isEnabled()) {
                return "as.numeric(" + getYaxis() + ")" + "+as.numeric(" + jComboBox13.getSelectedItem().toString() + ")";
            }
        } else {
            if (jPanel4.isEnabled()) {
                if (jComboBox17.getSelectedItem().toString().contains("em")) {
                    txt = getXaxis() + "+sd(" + getXaxis() + ")/sqrt(length(" + getXaxis() + "))";
                } else {
                    txt = getXaxis() + "+sd(" + getXaxis() + ")";
                }
            } else if (jPanel2.isEnabled()) {
                return "as.numeric(" + jComboBox16.getSelectedItem().toString() + ")";
            } else if (jPanel3.isEnabled()) {
                return "as.numeric(" + getXaxis() + ")" + "+as.numeric(" + jComboBox13.getSelectedItem().toString() + ")";
            }
        }
        if (jRadioButton4.isSelected()) {
            return null;
        }
        return txt;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     */
    public static void main(String args[]) {
        ArrayList<String> list = new ArrayList<String>();//String[] vals = {"x", "y", "typ"};
        list.add("x");
        list.add("y");
        list.add("typ");
        ArrayList<String> factors = new ArrayList<String>();//String[] vals = {"x", "y", "typ"};
        factors.add("typ");
        ArrayList<Object> extras = new ArrayList<Object>();
        ArrayList<Object> plots = new ArrayList<Object>();
        PlotSelectorDialog iopane = new PlotSelectorDialog("geom_line", list, factors, true, true, false);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Select Data", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String xaxis = iopane.getXaxis();
            String yaxis = iopane.getYaxis();
            String fill = iopane.getAesFill();
            String color = iopane.getAesColor();
            String shape = iopane.getShape();
            GeomPlot geomPlot = new GeomPlot();
            geomPlot.setX(xaxis);
            geomPlot.setY(yaxis);
            geomPlot.setAesFill(fill);
            geomPlot.setAesColor(color);
            geomPlot.setAesShape(shape);
            geomPlot.setColor(color);
            //geomPlot.setAesFill(fill);
            //add the type of the plot first
            geomPlot.setGeomType("geom_line");
            plots.add(geomPlot);
            System.out.println(geomPlot);
            System.out.println(plots);
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton basedOnColContent;
    private javax.swing.JRadioButton basedOnColContent1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox14;
    private javax.swing.JComboBox jComboBox15;
    private javax.swing.JComboBox jComboBox16;
    private javax.swing.JComboBox jComboBox17;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private Dialogs.LineDialog lineDialog1;
    private Dialogs.PointDialog pointDialog1;
    private Dialogs.SmoothDialog smoothDialog1;
    // End of variables declaration//GEN-END:variables
}



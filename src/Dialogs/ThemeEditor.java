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

import R.ThemeGraph;
import Commons.CommonClassesLight;
import Commons.PaintedButton;
import R.RSession.MyRsessionLogger;
import Tools.PopulateThemes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import org.rosuda.REngine.REXPMismatchException;

//TODO clean this class and deduplicate functions I made it very quickly and it's totally suboptimal
//nb to get the parameters of the graph --> use theme_get() --> a tester
/**
 * ThemeEditor handles R themes
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ThemeEditor extends javax.swing.JPanel {
    
    String axisTicksColor;
    ThemeGraph theme = new ThemeGraph();
    MyRsessionLogger r;
    private boolean loading = false;
    PopulateThemes themes = new PopulateThemes();

    /**
     * Creates new form ThemeEditor
     */
    public ThemeEditor(MyRsessionLogger r) {
        initComponents();
        themes.reloadThemes(jComboBox1);
        jComboBox1.setSelectedIndex(0);//no selection
        this.r = r;
        updatePlotPreview();
    }
    
    public ThemeEditor(MyRsessionLogger r, ThemeGraph theme) {
        this(r);
        setTheme(theme);
    }

    /**
     * Is called every time a color is changed
     *
     * @param tmp
     * @return the new color
     */
    public boolean changed(PaintedButton tmp) {
        Color color = JColorChooser.showDialog(this, "Pick a Color", tmp.getBackground());
        if (color != null) {
            tmp.setColor(color);
            tmp.setToolTip();
            tmp.setText(CommonClassesLight.toHtmlColor(color.getRGB()));
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bgcolor = new Commons.PaintedButton();
        jLabel4 = new javax.swing.JLabel();
        graphbgcolor = new Commons.PaintedButton();
        jLabel5 = new javax.swing.JLabel();
        tickscolor = new Commons.PaintedButton();
        jLabel6 = new javax.swing.JLabel();
        xaxislabelcolor = new Commons.PaintedButton();
        jLabel7 = new javax.swing.JLabel();
        yaxislabelcolor = new Commons.PaintedButton();
        jLabel8 = new javax.swing.JLabel();
        xaxistitlecolor = new Commons.PaintedButton();
        jLabel9 = new javax.swing.JLabel();
        yaxistitlecolor = new Commons.PaintedButton();
        jLabel10 = new javax.swing.JLabel();
        legendtextcolor = new Commons.PaintedButton();
        jLabel11 = new javax.swing.JLabel();
        plottitlecolor = new Commons.PaintedButton();
        graphframecolor = new Commons.PaintedButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        majorgridcolor = new Commons.PaintedButton();
        jLabel14 = new javax.swing.JLabel();
        minorgridcolor = new Commons.PaintedButton();
        jLabel15 = new javax.swing.JLabel();
        facetlabelcolor = new Commons.PaintedButton();
        jLabel16 = new javax.swing.JLabel();
        facetbgcolor = new Commons.PaintedButton();
        facetframecolor = new Commons.PaintedButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        legendBgcolor = new Commons.PaintedButton();
        resetLegendBgColor = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        legendTilteColor = new Commons.PaintedButton();
        resetLegendTitleColor = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        saveTheme = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        imagePaneLight1 = new Dialogs.ImagePaneLight();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        jLabel1.setText("background color:");

        bgcolor.setText("default");
        bgcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgcolor(evt);
            }
        });

        jLabel4.setText("graph background color:");

        graphbgcolor.setText("default");
        graphbgcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphbgcolor(evt);
            }
        });

        jLabel5.setText("axis ticks color:");

        tickscolor.setText("default");
        tickscolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tickscolor(evt);
            }
        });

        jLabel6.setText("x axis label color:");

        xaxislabelcolor.setText("default");
        xaxislabelcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xaxislabelcolor(evt);
            }
        });

        jLabel7.setText("y axis label color:");

        yaxislabelcolor.setText("default");
        yaxislabelcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yaxislabelcolor(evt);
            }
        });

        jLabel8.setText("x axis title color:");

        xaxistitlecolor.setText("default");
        xaxistitlecolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xaxistitlecolor(evt);
            }
        });

        jLabel9.setText("y axis titlel color:");

        yaxistitlecolor.setText("default");
        yaxistitlecolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yaxistitlecolor(evt);
            }
        });

        jLabel10.setText("legend text color:");

        legendtextcolor.setText("default");
        legendtextcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                legendtextcolor(evt);
            }
        });

        jLabel11.setText("plot title color:");

        plottitlecolor.setText("default");
        plottitlecolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plottitlecolor(evt);
            }
        });

        graphframecolor.setText("default");
        graphframecolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphframecolor(evt);
            }
        });

        jLabel12.setText("graph frame color:");

        jLabel13.setText("color:");

        majorgridcolor.setText("default");
        majorgridcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                majorgridcolor(evt);
            }
        });

        jLabel14.setText("color:");

        minorgridcolor.setText("default");
        minorgridcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minorgridcolor(evt);
            }
        });

        jLabel15.setText("facet label color:");

        facetlabelcolor.setText("default");
        facetlabelcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facetlabelcolor(evt);
            }
        });

        jLabel16.setText("facet background color:");

        facetbgcolor.setText("default");
        facetbgcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facetbgcolor(evt);
            }
        });

        facetframecolor.setText("default");
        facetframecolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facetframecolor(evt);
            }
        });

        jLabel17.setText("facet frame color:");

        jLabel18.setText(" stroke width:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(0.01f)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                axisTicksStrokeWidthChanged(evt);
            }
        });

        jLabel19.setText("stroke width:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(0.01f)));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                graphFrameStrokeChanged(evt);
            }
        });

        jLabel20.setText("stroke width:");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(0.01f)));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                majorGridStrokeChanged(evt);
            }
        });

        jLabel21.setText("stroke width:");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(0.01f)));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minorGridStrokeChanged(evt);
            }
        });

        jLabel22.setText("stroke width:");

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(0.01f)));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                facetFrameStrokeChanged(evt);
            }
        });

        jButton1.setText("reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restBgColor(evt);
            }
        });

        jButton2.setText("reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetGraphBgColor(evt);
            }
        });

        jButton3.setText("reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetAxisTicks(evt);
            }
        });

        jButton4.setText("reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restXaxisLabel(evt);
            }
        });

        jButton5.setText("reset");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restYaxisLabel(evt);
            }
        });

        jButton6.setText("reset");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restXtitleLabel(evt);
            }
        });

        jButton7.setText("reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restYtitleLabel(evt);
            }
        });

        jButton8.setText("reset");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPlotTitle(evt);
            }
        });

        jButton9.setText("reset");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLegendText(evt);
            }
        });

        jButton10.setText("reset");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetGraphFrame(evt);
            }
        });

        jButton11.setText("reset");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMajorGrid(evt);
            }
        });

        jButton12.setText("reset");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMinorGrid(evt);
            }
        });

        jButton13.setText("reset");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFacetLabel(evt);
            }
        });

        jButton14.setText("reset");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFacetBackground(evt);
            }
        });

        jButton15.setText("reset");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFacetFrame(evt);
            }
        });

        jLabel23.setText("legend bg color:");

        legendBgcolor.setText("default");
        legendBgcolor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                legendBgColor(evt);
            }
        });

        resetLegendBgColor.setText("reset");
        resetLegendBgColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLegendBgColorActionPerformed(evt);
            }
        });

        jLabel24.setText("legend title color:");

        legendTilteColor.setText("default");
        legendTilteColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                legendTitleColor(evt);
            }
        });

        resetLegendTitleColor.setText("reset");
        resetLegendTitleColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLegendTitleColorActionPerformed(evt);
            }
        });

        jLabel3.setText("Select a theme:");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeChanged(evt);
            }
        });

        jLabel25.setText("Theme Name:");

        jTextField1.setText("Default");

        saveTheme.setText("Save");
        saveTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveThemeActionPerformed(evt);
            }
        });

        jButton17.setText("Add");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewTheme(evt);
            }
        });

        jButton18.setText("Delete");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTheme(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Active");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activateGrid(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Active");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activateGrid(evt);
            }
        });

        jLabel2.setText("panel major grid: ");

        jLabel26.setText("panel minor grid: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphbgcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xaxislabelcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yaxislabelcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xaxistitlecolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yaxistitlecolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(legendtextcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(plottitlecolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(facetlabelcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(facetbgcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tickscolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphframecolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(facetframecolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(legendBgcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(legendTilteColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(majorgridcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(minorgridcolor, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner4, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                        .addGap(1, 1, 1))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner3)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jButton1)
                                                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING))
                                                                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING))
                                                                .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                                                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING))
                                                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(resetLegendBgColor, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(resetLegendTitleColor, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(jButton12)
                                .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jButton14)
                            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(bgcolor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(90, 90, 90))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(saveTheme)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(bgcolor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(graphbgcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(tickscolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(xaxislabelcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(yaxislabelcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(xaxistitlecolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(yaxistitlecolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(plottitlecolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(legendtextcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel23)
                    .addComponent(legendBgcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetLegendBgColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel24)
                    .addComponent(legendTilteColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetLegendTitleColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(graphframecolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(majorgridcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(minorgridcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15)
                    .addComponent(facetlabelcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(facetbgcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17)
                    .addComponent(facetframecolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveTheme)
                    .addComponent(jButton17)
                    .addComponent(jButton18))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bgcolor, facetbgcolor, facetframecolor, facetlabelcolor, graphbgcolor, graphframecolor, jLabel1, legendBgcolor, legendTilteColor, legendtextcolor, majorgridcolor, minorgridcolor, plottitlecolor, tickscolor, xaxislabelcolor, xaxistitlecolor, yaxislabelcolor, yaxistitlecolor});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePaneLight1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePaneLight1, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void resetButton(PaintedButton pb) {
        pb.setText("default");
        pb.setToolTipText("");
    }
    
    private void bgcolor(Object evt) {
        if (evt != null) {
            bgcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            bgcolor.setToolTip();
            bgcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            bgcolor.setColor(0xFF000000);
            resetButton(bgcolor);
        }
        updatePlotPreview();
    }
    private void bgcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgcolor
        if (evt != null) {
            if (changed(bgcolor)) {
                theme.setOutsideGraphColor(CommonClassesLight.getHtmlColor(bgcolor.getColor()));
            }
        } else {
            theme.setOutsideGraphColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_bgcolor
    
    private void updatePlotPreview() {
        if (loading) {
            return;
        }
        if (r == null || !r.isRserverRunning()) {
            BufferedImage black = new BufferedImage(imagePaneLight1.getSize().width, imagePaneLight1.getSize().height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = black.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.drawString("R connection Failed", 128, 128);
            g2d.drawString("No Preview Available", 128, 256);
            g2d.dispose();
            imagePaneLight1.setImage(black);
        } else {
            /*
             * we create some basic/generic plot code
             */
            String simplePlot = "ggplot(mtcars, aes(x=hp, y=mpg))"
                    + "\n+ geom_point(aes(colour=factor(cyl)))"
                    + "\n+ scale_colour_discrete(name=\"cyl\")"
                    + "\n+ facet_grid(. ~ cyl)"
                    + "\n+ ggtitle('title')";
            if (!theme.toString().trim().equals("")) {
                simplePlot += "\n+" + theme;
            }
            imagePaneLight1.setImage(r.getPreview(simplePlot, imagePaneLight1.getSize().width, imagePaneLight1.getSize().height));
        }
    }
    
    private void graphbgcolor(Object evt) {
        if (evt != null) {
            graphbgcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            graphbgcolor.setToolTip();
            graphbgcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            graphbgcolor.setColor(0xFF000000);
            resetButton(graphbgcolor);
        }
        updatePlotPreview();
    }
    private void graphbgcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphbgcolor
        if (evt != null) {
            if (changed(graphbgcolor)) {
                theme.setGraphBgColor(CommonClassesLight.getHtmlColor(graphbgcolor.getColor()));
            }
        } else {
            theme.setGraphBgColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_graphbgcolor
    private void tickscolor(Object evt) {
        if (evt != null) {
            tickscolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            tickscolor.setToolTip();
            tickscolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            tickscolor.setColor(0xFF000000);
            resetButton(tickscolor);
        }
        updatePlotPreview();
    }
    private void tickscolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tickscolor
        if (evt != null) {
            if (changed(tickscolor)) {
                theme.setAxisTicksColor(CommonClassesLight.getHtmlColor(tickscolor.getColor()));
            }
        } else {
            theme.setAxisTicksColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_tickscolor
    private void xaxislabelcolor(Object evt) {
        if (evt != null) {
            xaxislabelcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            xaxislabelcolor.setToolTip();
            xaxislabelcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            xaxislabelcolor.setColor(0xFF000000);
            resetButton(xaxislabelcolor);
        }
        updatePlotPreview();
    }
    private void xaxislabelcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xaxislabelcolor
        if (evt != null) {
            if (changed(xaxislabelcolor)) {
                theme.setxAxisLabelColor(CommonClassesLight.getHtmlColor(xaxislabelcolor.getColor()));
            }
        } else {
            theme.setxAxisLabelColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_xaxislabelcolor
    private void yaxislabelcolor(Object evt) {
        if (evt != null) {
            yaxislabelcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            yaxislabelcolor.setToolTip();
            yaxislabelcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            yaxislabelcolor.setColor(0xFF000000);
            resetButton(yaxislabelcolor);
        }
        updatePlotPreview();
    }
    private void yaxislabelcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yaxislabelcolor
        if (evt != null) {
            if (changed(yaxislabelcolor)) {
                theme.setyAxisLabelColor(CommonClassesLight.getHtmlColor(yaxislabelcolor.getColor()));
            }
        } else {
            theme.setyAxisLabelColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_yaxislabelcolor
    private void xaxistitlecolor(Object evt) {
        if (evt != null) {
            xaxistitlecolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            xaxistitlecolor.setToolTip();
            xaxistitlecolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            xaxistitlecolor.setColor(0xFF000000);
            resetButton(xaxistitlecolor);
        }
        updatePlotPreview();
    }
    private void xaxistitlecolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xaxistitlecolor
        if (evt != null) {
            if (changed(xaxistitlecolor)) {
                theme.setxAxisTitleColor(CommonClassesLight.getHtmlColor(xaxistitlecolor.getColor()));
            }
        } else {
            theme.setxAxisTitleColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_xaxistitlecolor
    private void yaxistitlecolor(Object evt) {
        if (evt != null) {
            yaxistitlecolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            yaxistitlecolor.setToolTip();
            yaxistitlecolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            yaxistitlecolor.setColor(0xFF000000);
            resetButton(yaxistitlecolor);
        }
        updatePlotPreview();
    }
    private void yaxistitlecolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yaxistitlecolor
        if (evt != null) {
            if (changed(yaxistitlecolor)) {
                theme.setyAxisTitleColor(CommonClassesLight.getHtmlColor(yaxistitlecolor.getColor()));
            }
        } else {
            theme.setyAxisTitleColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_yaxistitlecolor
    private void plottitlecolor(Object evt) {
        if (evt != null) {
            plottitlecolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            plottitlecolor.setToolTip();
            plottitlecolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            plottitlecolor.setColor(0xFF000000);
            resetButton(plottitlecolor);
        }
        updatePlotPreview();
    }
    private void plottitlecolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plottitlecolor
        if (evt != null) {
            if (changed(plottitlecolor)) {
                theme.setPlotTitleColor(CommonClassesLight.getHtmlColor(plottitlecolor.getColor()));
            }
        } else {
            theme.setPlotTitleColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_plottitlecolor
    private void legendtextcolor(Object evt) {
        if (evt != null) {
            legendtextcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            legendtextcolor.setToolTip();
            legendtextcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            legendtextcolor.setColor(0xFF000000);
            resetButton(legendtextcolor);
        }
        updatePlotPreview();
    }
    private void legendtextcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legendtextcolor
        if (evt != null) {
            if (changed(legendtextcolor)) {
                theme.setLegendTextColor(CommonClassesLight.getHtmlColor(legendtextcolor.getColor()));
            }
        } else {
            theme.setLegendTextColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_legendtextcolor
    private void graphframecolor(Object evt) {
        if (evt != null) {
            graphframecolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            graphframecolor.setToolTip();
            graphframecolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            graphframecolor.setColor(0xFF000000);
            resetButton(graphframecolor);
        }
        updatePlotPreview();
    }
    private void graphframecolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphframecolor
        if (evt != null) {
            if (changed(graphframecolor)) {
                theme.setGraphFrameColor(CommonClassesLight.getHtmlColor(graphframecolor.getColor()));
            }
        } else {
            theme.setGraphFrameColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_graphframecolor
    private void majorgridcolor(Object evt) {
        if (evt != null) {
            majorgridcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            majorgridcolor.setToolTip();
            majorgridcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            majorgridcolor.setColor(0xFF000000);
            resetButton(majorgridcolor);
        }
        updatePlotPreview();
    }
    private void majorgridcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_majorgridcolor
        if (evt != null) {
            if (changed(majorgridcolor)) {
                theme.setPanelGridMajorColor(CommonClassesLight.getHtmlColor(majorgridcolor.getColor()));
            }
        } else {
            theme.setPanelGridMajorColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_majorgridcolor
    private void minorgridcolor(Object evt) {
        if (evt != null) {
            minorgridcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            minorgridcolor.setToolTip();
            minorgridcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            minorgridcolor.setColor(0xFF000000);
            resetButton(minorgridcolor);
        }
        updatePlotPreview();
    }
    private void minorgridcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minorgridcolor
        if (evt != null) {
            if (changed(minorgridcolor)) {
                theme.setPanelGridMinorColor(CommonClassesLight.getHtmlColor(minorgridcolor.getColor()));
            }
        } else {
            theme.setPanelGridMinorColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_minorgridcolor
    private void facetlabelcolor(Object evt) {
        if (evt != null) {
            facetlabelcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            facetlabelcolor.setToolTip();
            facetlabelcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            facetlabelcolor.setColor(0xFF000000);
            resetButton(facetlabelcolor);
        }
        updatePlotPreview();
    }
    private void facetlabelcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facetlabelcolor
        if (evt != null) {
            if (changed(facetlabelcolor)) {
                theme.setFacetTextColor(CommonClassesLight.getHtmlColor(facetlabelcolor.getColor()));
            }
        } else {
            theme.setFacetTextColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_facetlabelcolor
    
    private void facetbgcolor(Object evt) {
        if (evt != null) {
            facetbgcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            facetbgcolor.setToolTip();
            facetbgcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            facetbgcolor.setColor(0xFF000000);
            resetButton(facetbgcolor);
        }
        updatePlotPreview();
    }
    
    private void facetbgcolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facetbgcolor
        if (evt != null) {
            if (changed(facetbgcolor)) {
                theme.setFacetLabelFill(CommonClassesLight.getHtmlColor(facetbgcolor.getColor()));
            }
        } else {
            theme.setFacetLabelFill(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_facetbgcolor
    
    private void facetframecolor(Object evt) {
        if (evt != null) {
            facetframecolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            facetframecolor.setToolTip();
            facetframecolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            facetframecolor.setColor(0xFF000000);
            resetButton(facetframecolor);
        }
        updatePlotPreview();
    }
    
    private void facetframecolor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facetframecolor
        if (evt != null) {
            if (changed(facetframecolor)) {
                theme.setFacetFrameColor(CommonClassesLight.getHtmlColor(facetframecolor.getColor()));
            }
        } else {
            theme.setFacetFrameColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_facetframecolor
    
    private void restBgColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restBgColor
        setbgcolor(0xFF000000);
        bgcolor(null);
    }//GEN-LAST:event_restBgColor
    
    private void resetGraphBgColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetGraphBgColor
        setgraphbgcolor(0xFF000000);
        graphbgcolor(null);
    }//GEN-LAST:event_resetGraphBgColor
    
    private void resetAxisTicks(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetAxisTicks
        settickscolor(0xFF000000);
        setjSpinner1(0f);
        tickscolor(null);
    }//GEN-LAST:event_resetAxisTicks
    
    private void restXaxisLabel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restXaxisLabel
        setxaxislabelcolor(0xFF000000);
        xaxislabelcolor(null);
    }//GEN-LAST:event_restXaxisLabel
    
    private void restYaxisLabel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restYaxisLabel
        setyaxislabelcolor(0xFF000000);
        yaxislabelcolor(null);
    }//GEN-LAST:event_restYaxisLabel
    
    private void restXtitleLabel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restXtitleLabel
        setxaxistitlecolor(0xFF000000);
        xaxistitlecolor(null);
    }//GEN-LAST:event_restXtitleLabel
    
    private void restYtitleLabel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restYtitleLabel
        setyaxistitlecolor(0xFF000000);
        yaxistitlecolor(null);
    }//GEN-LAST:event_restYtitleLabel
    
    private void resetPlotTitle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPlotTitle
        setplottitlecolor(0xFF000000);
        plottitlecolor(null);
    }//GEN-LAST:event_resetPlotTitle
    
    private void resetLegendText(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLegendText
        setlegendtextcolor(0xFF000000);
        legendtextcolor(null);
    }//GEN-LAST:event_resetLegendText
    
    private void resetGraphFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetGraphFrame
        setgraphframecolor(0xFF000000);
        graphframecolor(null);
        setjSpinner2(0f);
    }//GEN-LAST:event_resetGraphFrame
    
    private void resetMajorGrid(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMajorGrid
        setmajorgridcolor(0xFF000000);
        jCheckBox1.setSelected(true);
        majorgridcolor(null);
        setjSpinner3(0f);
    }//GEN-LAST:event_resetMajorGrid
    
    private void resetMinorGrid(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMinorGrid
        setminorgridcolor(0xFF000000);
        jCheckBox2.setSelected(true);
        minorgridcolor(null);
        setjSpinner4(0f);
    }//GEN-LAST:event_resetMinorGrid
    
    private void resetFacetLabel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFacetLabel
        setfacetlabelcolor(0xFF000000);
        facetlabelcolor(null);
    }//GEN-LAST:event_resetFacetLabel
    
    private void resetFacetBackground(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFacetBackground
        setfacetbgcolor(0xFF000000);
        facetbgcolor(null);
    }//GEN-LAST:event_resetFacetBackground
    
    private void setFacetFrameStroke(float i) {
        jSpinner5.setValue(i);
    }
    
    private void setfacetbgcolor(int i) {
        facetbgcolor.setColor(i);
    }
    
    private void resetFacetFrame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFacetFrame
        setFacetFrameColor(0xFF000000);
        facetframecolor(null);
        setFacetFrameStroke(0);
    }//GEN-LAST:event_resetFacetFrame
    
    private void axisTicksStrokeWidthChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_axisTicksStrokeWidthChanged
        float stroke = getAxisTicksStrokeWidth();
        theme.setAxisTicksStrokeWidth(stroke == 0 ? null : stroke + "");
        updatePlotPreview();
    }//GEN-LAST:event_axisTicksStrokeWidthChanged
    
    private void graphFrameStrokeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_graphFrameStrokeChanged
        float stroke = getGraphFrameStrokeWidth();
        theme.setGraphFrameStrokeWidth(stroke == 0 ? null : stroke + "");
        updatePlotPreview();
    }//GEN-LAST:event_graphFrameStrokeChanged
    
    private void majorGridStrokeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_majorGridStrokeChanged
        float stroke = getPanelMajorGridStrokeWidth();
        theme.setPanelMajorGridStrokeWidth(stroke == 0 ? null : stroke + "");
        updatePlotPreview();
    }//GEN-LAST:event_majorGridStrokeChanged
    
    private void minorGridStrokeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minorGridStrokeChanged
        float stroke = getPanelMinorGridStrokeWidth();
        theme.setPanelMinorGridStrokeWidth(stroke == 0 ? null : stroke + "");
        updatePlotPreview();
    }//GEN-LAST:event_minorGridStrokeChanged
    
    private void facetFrameStrokeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_facetFrameStrokeChanged
        float stroke = getFacetFrameStrokeWidth();
        theme.setFacetFrameStrokeWidth(stroke == 0 ? null : stroke + "");
        updatePlotPreview();
    }//GEN-LAST:event_facetFrameStrokeChanged
    
    private void legendBgColor(Object evt) {
        if (evt != null) {
            legendBgcolor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            legendBgcolor.setToolTip();
            legendBgcolor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            legendBgcolor.setColor(0xFF000000);
            resetButton(legendBgcolor);
        }
        updatePlotPreview();
    }
    private void legendBgColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legendBgColor
        if (evt != null) {
            if (changed(legendBgcolor)) {
                theme.setLegendBgColor(CommonClassesLight.getHtmlColor(legendBgcolor.getColor()));
                
            }
        } else {
            theme.setLegendBgColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_legendBgColor
    private void legendTitleColor(Object evt) {
        if (evt != null) {
            legendTilteColor.setColor(CommonClassesLight.getColorFromHtmlColor(evt));
            legendTilteColor.setToolTip();
            legendTilteColor.setText(CommonClassesLight.toHtmlColor(CommonClassesLight.getColorFromHtmlColor(evt)));
        } else {
            legendTilteColor.setColor(0xFF000000);
            resetButton(legendTilteColor);
        }
        updatePlotPreview();
    }
    private void legendTitleColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legendTitleColor
        if (evt != null) {
            if (changed(legendTilteColor)) {
                theme.setLegendTitleColor(CommonClassesLight.getHtmlColor(legendTilteColor.getColor()));
            }
        } else {
            theme.setLegendTitleColor(null);
        }
        updatePlotPreview();
    }//GEN-LAST:event_legendTitleColor
    
    private void resetLegendBgColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLegendBgColorActionPerformed
        setlegendbgcolor(0xFF000000);
        legendBgColor(null);
    }//GEN-LAST:event_resetLegendBgColorActionPerformed
    
    private void resetLegendTitleColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLegendTitleColorActionPerformed
        setlegendtitlecolor(0xFF000000);
        legendTitleColor(null);
    }//GEN-LAST:event_resetLegendTitleColorActionPerformed
    
    private void ThemeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeChanged
        this.theme = PopulateThemes.themes.get(jComboBox1.getSelectedIndex()).clone();
        setTheme(theme);
    }//GEN-LAST:event_ThemeChanged
    
    private void saveThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveThemeActionPerformed
        ThemeGraph themeGraph = theme.clone();
        themeGraph.setThemeName(getThemeName());
        themeGraph.setPath(themeGraph.toXml(CommonClassesLight.getFolder(ThemeEditor.class, "GraphThemes"), themeGraph.getPath()));
        PopulateThemes.themes.set(jComboBox1.getSelectedIndex(), themeGraph);
//        themes = new PopulateThemes();
//        themes.reloadThemes(jComboBox1);
//        reloadTheme(themeGraph);
        this.theme = themeGraph;
    }//GEN-LAST:event_saveThemeActionPerformed
    
    private void addNewTheme(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewTheme
        ThemeGraph themeGraph = theme.clone();
        themeGraph.setThemeName(getThemeName());
        themeGraph.setPath(themeGraph.toXml(CommonClassesLight.getFolder(ThemeEditor.class, "GraphThemes"), null));
//        PopulateThemes.themes.add(themeGraph);
//        themes.reloadThemes(jComboBox1);
        themes = new PopulateThemes();
        themes.reloadThemes(jComboBox1);
        try {
            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
        }
        this.theme = themeGraph;
    }//GEN-LAST:event_addNewTheme
    
    private void deleteTheme(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTheme
        PopulateThemes.themes.remove(jComboBox1.getSelectedIndex());
        String name = theme.getPath();
        if (name != null) {
            new File(name).renameTo(new File(name + ".old"));
        }
        themes = new PopulateThemes();
        themes.reloadThemes(jComboBox1);
        try {
            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_deleteTheme
    
    private void activateGrid(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activateGrid
        if (loading && evt != null) {
            return;
        }
        boolean activate = jCheckBox1.isSelected();
        jLabel13.setEnabled(activate);
        jLabel20.setEnabled(activate);
        jSpinner3.setEnabled(activate);
        majorgridcolor.setEnabled(activate);
        /*
         * DIRTY HACK
         */
        if (!activate) {
            theme.setPanelMajorGridStrokeWidth("element_blank()");
        } else {
            float stroke = getPanelMajorGridStrokeWidth();
            if (stroke == 0) {
                theme.setPanelMajorGridStrokeWidth(null);
            } else {
                theme.setPanelMajorGridStrokeWidth(stroke + "");
            }
        }
        activate = jCheckBox2.isSelected();
        jLabel14.setEnabled(activate);
        jLabel21.setEnabled(activate);
        jSpinner4.setEnabled(activate);
        minorgridcolor.setEnabled(activate);
        /*
         * DIRTY HACK
         */
        if (!activate) {
            theme.setPanelMinorGridStrokeWidth("element_blank()");
        } else {
            float stroke = getPanelMinorGridStrokeWidth();
            if (stroke == 0) {
                theme.setPanelMinorGridStrokeWidth(null);
            } else {
                theme.setPanelMinorGridStrokeWidth(stroke + "");
            }
        }
        updatePlotPreview();
    }//GEN-LAST:event_activateGrid
    
    public final void setTheme(ThemeGraph theme) {
        loading = true;
        this.theme = theme;
        if (theme != null) {
            bgcolor(theme.getOutsideGraphColor());
            graphbgcolor(theme.getGraphBgColor());
            tickscolor(theme.getAxisTicksColor());
            xaxislabelcolor(theme.getxAxisLabelColor());
            yaxislabelcolor(theme.getyAxisLabelColor());
            xaxistitlecolor(theme.getxAxisTitleColor());
            yaxistitlecolor(theme.getyAxisTitleColor());
            plottitlecolor(theme.getPlotTitleColor());
            legendtextcolor(theme.getLegendTextColor());
            legendTitleColor(theme.getLegendTitleColor());
            legendBgColor(theme.getLegendBgColor());
            legendtextcolor(theme.getLegendTextColor());
            graphframecolor(theme.getGraphFrameColor());
            minorgridcolor(theme.getPanelGridMinorColor());
            majorgridcolor(theme.getPanelGridMajorColor());
            facetlabelcolor(theme.getFacetTextColor());
            facetbgcolor(theme.getFacetLabelFill());
            facetframecolor(theme.getFacetFrameColor());
            setjSpinner1(theme.getAxisTicksStrokeWidth());
            setjSpinner2(theme.getGraphFrameStrokeWidth());
            jCheckBox1.setSelected(true);
            jCheckBox2.setSelected(true);
            setjSpinner3(theme.getPanelMajorGridStrokeWidth());
            setjSpinner4(theme.getPanelMinorGridStrokeWidth());
            activateGrid(null);
            setFacetFrameStroke(theme.getFacetFrameStrokeWidth());
            setThemeName(theme.getThemeName());
        }
        loading = false;
        updatePlotPreview();
    }

    /**
     *
     * @return the new R theme
     */
    public ThemeGraph getTheme() {
        /*
         * the name is the only parameter we haven't set yet
         */
        theme.setThemeName(getThemeName());
        return theme;
    }
    
    public float getAxisTicksStrokeWidth() {
        return ((Float) jSpinner1.getValue()).floatValue();
    }
    
    public float getGraphFrameStrokeWidth() {
        return ((Float) jSpinner2.getValue()).floatValue();
    }
    
    public float getPanelMajorGridStrokeWidth() {
        return ((Float) jSpinner3.getValue()).floatValue();
    }
    
    public float getPanelMinorGridStrokeWidth() {
        return ((Float) jSpinner4.getValue()).floatValue();
    }
    
    public float getFacetFrameStrokeWidth() {
        return ((Float) jSpinner5.getValue()).floatValue();
    }
    
    private void setlegendbgcolor(int i) {
        legendBgcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(legendBgcolor);
        }
    }
    
    private void setlegendtitlecolor(int i) {
        legendTilteColor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(legendTilteColor);
        }
    }
    
    private void setfacetlabelcolor(int i) {
        facetlabelcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(facetlabelcolor);
        }
    }
    
    private void setlegendtextcolor(int i) {
        legendtextcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(legendtextcolor);
        }
    }
    
    private void setplottitlecolor(int i) {
        plottitlecolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(plottitlecolor);
        }
    }
    
    private void setyaxistitlecolor(int i) {
        yaxistitlecolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(yaxistitlecolor);
        }
    }
    
    private void setxaxistitlecolor(int i) {
        xaxistitlecolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(xaxistitlecolor);
        }
    }
    
    private void setyaxislabelcolor(int i) {
        yaxislabelcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(yaxislabelcolor);
        }
    }
    
    private void setxaxislabelcolor(int i) {
        xaxislabelcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(xaxislabelcolor);
        }
    }
    
    private void setgraphbgcolor(int i) {
        graphbgcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(graphbgcolor);
        }
    }
    
    private void setbgcolor(int i) {
        bgcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(bgcolor);
        }
    }
    
    private void settickscolor(int i) {
        tickscolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(tickscolor);
        }
    }
    
    private void setmajorgridcolor(int i) {
        majorgridcolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(majorgridcolor);
        }
    }
    
    private void setgraphframecolor(int i) {
        graphframecolor.setColor(i);
        if (i == 0xFF000000) {
            resetButton(graphframecolor);
        }
    }
    
    private void setjSpinner1(Object f) {
        if (f != null) {
            jSpinner1.setValue(CommonClassesLight.String2Float(f));
        } else {
            jSpinner1.setValue(0f);
        }
    }
    
    private void setjSpinner1(float f) {
        jSpinner1.setValue(f);
    }
    
    private void setjSpinner2(Object f) {
        if (f != null) {
            jSpinner2.setValue(CommonClassesLight.String2Float(f));
        } else {
            jSpinner2.setValue(0f);
        }
    }
    
    private void setjSpinner2(float f) {
        jSpinner2.setValue(f);
    }
    
    private void setjSpinner3(Object f) {
        if (f != null) {
            /*
             * DIRTY HACK
             */
            if (!f.toString().toLowerCase().contains("element_blank()")) {
                jSpinner3.setValue(CommonClassesLight.String2Float(f));
            } else {
                jSpinner3.setValue(0f);
                jCheckBox1.setSelected(false);
            }
        } else {
            jSpinner3.setValue(0f);
        }
    }
    
    private void setjSpinner3(float f) {
        jSpinner3.setValue(f);
    }
    
    private void setminorgridcolor(int i) {
        minorgridcolor.setColor(i);
    }
    
    private void setjSpinner4(Object f) {
        if (f != null) {
            /*
             * DIRTY HACK
             */
            if (!f.toString().toLowerCase().contains("element_blank()")) {
                jSpinner4.setValue(CommonClassesLight.String2Float(f));
            } else {
                jSpinner4.setValue(0f);
                jCheckBox2.setSelected(false);
            }
        } else {
            jSpinner4.setValue(0f);
        }
    }
    
    private void setjSpinner4(float f) {
        jSpinner4.setValue(f);
    }
    
    private void setFacetFrameColor(int i) {
        facetframecolor.setColor(i);
    }
    
    private void setFacetFrameStroke(Object i) {
        if (i != null) {
            jSpinner5.setValue(CommonClassesLight.String2Float(i));
        } else {
            jSpinner5.setValue(0f);
        }
    }
    
    private void setThemeName(String themeName) {
        if (themeName == null) {
            jTextField1.setText("");
        } else {
            jTextField1.setText(themeName);
        }
    }
    
    private String getThemeName() {
        return jTextField1.getText();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        MyRsessionLogger r = new MyRsessionLogger();
        try {
            r.reopenConnection();
        } catch (REXPMismatchException ex) {
        }
        ThemeEditor iopane = new ThemeEditor(r);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "ThemeEditor", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        r.close();
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Commons.PaintedButton bgcolor;
    private Commons.PaintedButton facetbgcolor;
    private Commons.PaintedButton facetframecolor;
    private Commons.PaintedButton facetlabelcolor;
    private Commons.PaintedButton graphbgcolor;
    private Commons.PaintedButton graphframecolor;
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JTextField jTextField1;
    private Commons.PaintedButton legendBgcolor;
    private Commons.PaintedButton legendTilteColor;
    private Commons.PaintedButton legendtextcolor;
    private Commons.PaintedButton majorgridcolor;
    private Commons.PaintedButton minorgridcolor;
    private Commons.PaintedButton plottitlecolor;
    private javax.swing.JButton resetLegendBgColor;
    private javax.swing.JButton resetLegendTitleColor;
    private javax.swing.JButton saveTheme;
    private Commons.PaintedButton tickscolor;
    private Commons.PaintedButton xaxislabelcolor;
    private Commons.PaintedButton xaxistitlecolor;
    private Commons.PaintedButton yaxislabelcolor;
    private Commons.PaintedButton yaxistitlecolor;
    // End of variables declaration//GEN-END:variables
}



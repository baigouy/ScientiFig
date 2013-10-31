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

import MyShapes.GraphFont;
import java.awt.Font;
import javax.swing.JOptionPane;

/**
 * RFontsParameters allows the user to define or edit graph parameters (such as
 * font, line width, ...)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class RFontsParameters extends javax.swing.JPanel {

    /**
     * Empty constructoe
     */
    public RFontsParameters() {
        initComponents();
        fontSelectorLight8.setTypeVisible(false);
        fontSelectorLight1.setTypeVisible(false);
        fontSelectorLight2.setTypeVisible(false);
        fontSelectorLight3.setTypeVisible(false);
        fontSelectorLight4.setTypeVisible(false);
        fontSelectorLight5.setTypeVisible(false);
        fontSelectorLight6.setTypeVisible(false);
        fontSelectorLight7.setTypeVisible(false);
    }

    /**
     * Constructor
     *
     * @param gf graph parameters
     */
    public RFontsParameters(GraphFont gf) {
        this();
        setGF(gf);
    }

    /**
     * Sets the dialog parameters to that contained in 'gf'
     *
     * @param gf
     */
    public final void setGF(GraphFont gf) {
        if (gf != null) {
            Font common = gf.getCommonFont();
            jSpinner1.setValue(gf.getLineSize());
            jSpinner2.setValue(gf.getPointSize());
            if (common != null) {
                jCheckBox1.setSelected(true);
                fontSelectorLight7.setSelectedFont(common);
            } else {
                jCheckBox1.doClick();
            }
            if (common == null) {
                fontSelectorLight1.setSelectedFont(gf.getTitleSize());
                fontSelectorLight2.setSelectedFont(gf.getLegendTitle());
                fontSelectorLight3.setSelectedFont(gf.getXtitle());
                fontSelectorLight4.setSelectedFont(gf.getYtitle());
                fontSelectorLight5.setSelectedFont(gf.getXaxis());
                fontSelectorLight6.setSelectedFont(gf.getYaxis());
                fontSelectorLight7.setSelectedFont(gf.getLegendText());
            } else {
                jCheckBox1.setSelected(true);
                fontSelectorLight8.setSelectedFont(common);
                fontSelectorLight1.setSelectedFont(common);
                fontSelectorLight2.setSelectedFont(common);
                fontSelectorLight3.setSelectedFont(common);
                fontSelectorLight4.setSelectedFont(common);
                fontSelectorLight5.setSelectedFont(common);
                fontSelectorLight6.setSelectedFont(common);
                fontSelectorLight7.setSelectedFont(common);
            }
            jRadioButton1.setSelected(gf.isWarnIfTitleIsPresent());
            jRadioButton2.setSelected(!gf.isWarnIfTitleIsPresent());
            jRadioButton3.setSelected(gf.isWarnIfGridIsPresent());
            jRadioButton4.setSelected(!gf.isWarnIfGridIsPresent());
            jRadioButton5.setSelected(gf.isWarnIfHasBgColor());
            jRadioButton6.setSelected(!gf.isWarnIfHasBgColor());
            jRadioButton7.setSelected(gf.isWarnIfUnitsAreMissing());
            jRadioButton8.setSelected(!gf.isWarnIfUnitsAreMissing());
            jRadioButton9.setSelected(gf.isWarnIfAxisTitlesAreMissing());
            jRadioButton10.setSelected(!gf.isWarnIfAxisTitlesAreMissing());
            jRadioButton12.setSelected(gf.isWarnIfNobracketsAroundUnits());
            jRadioButton11.setSelected(!gf.isWarnIfNobracketsAroundUnits());
            jRadioButton13.setSelected(gf.isWarnIfLegendTitleIsPresent());
            jRadioButton14.setSelected(!gf.isWarnIfLegendTitleIsPresent());
            jRadioButton15.setSelected(gf.isWarnIfColorsAreNotColorBlindFriendly());
            jRadioButton16.setSelected(!gf.isWarnIfColorsAreNotColorBlindFriendly());
        } else {
            jCheckBox1.setSelected(true);
        }
    }

    /**
     *
     * @param ft
     * @return the font that must be applied to all texts contained in a graph
     */
    public Font getCommonFont(FontSelectorLight ft) {
        if (jCheckBox1.isSelected()) {
            return fontSelectorLight8.getSelectedFont();
        } else {
            return ft.getSelectedFont();
        }
    }

    /**
     *
     * @return the font that should be applied to the graph main title
     */
    public Font getMainTitleFont() {
        return getCommonFont(fontSelectorLight1);
    }

    /**
     *
     * @return the font that should be applied to the title of the legend
     */
    public Font getLegendTitleFont() {
        return getCommonFont(fontSelectorLight2);
    }

    /**
     *
     * @return the font that should be applied to the text of the legend
     */
    public Font getLegendTextFont() {
        return getCommonFont(fontSelectorLight7);
    }

    /**
     *
     * @return the font that should be applied to the title of the X axis
     */
    public Font getXAxisTitleFont() {
        return getCommonFont(fontSelectorLight3);
    }

    /**
     *
     * @return the font that should be applied to the title of the Y axis
     */
    public Font getYAxisTitleFont() {
        return getCommonFont(fontSelectorLight4);
    }

    /**
     *
     * @return the font that should be applied to the text of the X axis
     */
    public Font getXAxisTextFont() {
        return getCommonFont(fontSelectorLight5);
    }

    /**
     *
     * @return the font that should be applied to the text of the Y axis
     */
    public Font getYAxisTextFont() {
        return getCommonFont(fontSelectorLight6);
    }

    /**
     *
     * @return the line width that should be applied to R graphs
     */
    public float getLineWidthSize() {
        return ((Float) jSpinner1.getValue()).floatValue();
    }

    /**
     *
     * @return the point size that should be applied to R scatterplots
     */
    public float getPointWidthSize() {
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
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        fontSelectorLight1 = new Dialogs.FontSelectorLight();
        fontSelectorLight2 = new Dialogs.FontSelectorLight();
        fontSelectorLight3 = new Dialogs.FontSelectorLight();
        fontSelectorLight4 = new Dialogs.FontSelectorLight();
        fontSelectorLight5 = new Dialogs.FontSelectorLight();
        fontSelectorLight6 = new Dialogs.FontSelectorLight();
        fontSelectorLight7 = new Dialogs.FontSelectorLight();
        jCheckBox1 = new javax.swing.JCheckBox();
        fontSelectorLight8 = new Dialogs.FontSelectorLight();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jRadioButton15 = new javax.swing.JRadioButton();
        jRadioButton16 = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Font Settings"));

        jLabel21.setText("Font size Main Title:");
        jLabel21.setEnabled(false);

        jLabel22.setText("Font size Legend  Title:");
        jLabel22.setEnabled(false);

        jLabel23.setText("Font size x Axis Title:");
        jLabel23.setEnabled(false);

        jLabel24.setText("Font size y Axis Title:");
        jLabel24.setEnabled(false);

        jLabel25.setText("Text:");
        jLabel25.setEnabled(false);

        jLabel26.setText("Text:");
        jLabel26.setEnabled(false);

        jLabel27.setText("Text:");
        jLabel27.setEnabled(false);

        fontSelectorLight1.setEnabled(false);

        fontSelectorLight2.setEnabled(false);

        fontSelectorLight3.setEnabled(false);

        fontSelectorLight4.setEnabled(false);

        fontSelectorLight5.setEnabled(false);

        fontSelectorLight6.setEnabled(false);

        fontSelectorLight7.setEnabled(false);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Use Same Font Throughout the Graph");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1changeSelection(evt);
            }
        });

        jLabel1.setText("Default Line Width (in pts) (values <0 mean inactive)");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(-1.0f), Float.valueOf(-1.0f), null, Float.valueOf(0.05f)));

        jLabel2.setText("Default Point Size (in pts) (values <0 mean inactive)");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(-1.0f), Float.valueOf(-1.0f), null, Float.valueOf(0.05f)));

        jLabel4.setText("Warn if the graph has a main title:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Yes");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("No");

        jLabel5.setText("Warn if the graph has a grid:");

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("Yes");

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("No");

        jLabel3.setText("Warn if the graph has a colored background:");

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setSelected(true);
        jRadioButton5.setText("Yes");

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setText("No");

        jLabel6.setText("Warn if units are missing:");

        buttonGroup5.add(jRadioButton7);
        jRadioButton7.setSelected(true);
        jRadioButton7.setText("Yes");

        buttonGroup5.add(jRadioButton8);
        jRadioButton8.setText("No");

        jLabel7.setText("Warn if axis title is missing:");

        buttonGroup4.add(jRadioButton9);
        jRadioButton9.setSelected(true);
        jRadioButton9.setText("Yes");

        buttonGroup4.add(jRadioButton10);
        jRadioButton10.setText("No");

        jLabel8.setText("Warn if units are not surrounded by brackets:");

        buttonGroup6.add(jRadioButton11);
        jRadioButton11.setText("No");

        buttonGroup6.add(jRadioButton12);
        jRadioButton12.setSelected(true);
        jRadioButton12.setText("Yes");

        jLabel9.setText("Warn if the graph legend has a title:");

        buttonGroup7.add(jRadioButton13);
        jRadioButton13.setSelected(true);
        jRadioButton13.setText("Yes");

        buttonGroup7.add(jRadioButton14);
        jRadioButton14.setText("No");

        jLabel10.setText("Warn if colors are not color blind friendly:");

        buttonGroup8.add(jRadioButton15);
        jRadioButton15.setSelected(true);
        jRadioButton15.setText("Yes");

        buttonGroup8.add(jRadioButton16);
        jRadioButton16.setText("No");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSelectorLight8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fontSelectorLight4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(fontSelectorLight3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(fontSelectorLight2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel26)
                                                .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fontSelectorLight5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(fontSelectorLight7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(fontSelectorLight6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                    .addComponent(fontSelectorLight1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton4)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton11))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton16)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(fontSelectorLight8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21)
                    .addComponent(fontSelectorLight1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(fontSelectorLight7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontSelectorLight2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(fontSelectorLight3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(fontSelectorLight5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontSelectorLight6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(fontSelectorLight4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel5)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton6)
                    .addComponent(jLabel7)
                    .addComponent(jRadioButton9)
                    .addComponent(jRadioButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jRadioButton12)
                        .addComponent(jRadioButton11))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jRadioButton7)
                        .addComponent(jRadioButton8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel9)
                        .addComponent(jRadioButton13)
                        .addComponent(jRadioButton14))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel10)
                        .addComponent(jRadioButton15)
                        .addComponent(jRadioButton16))))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fontSelectorLight1, fontSelectorLight2, fontSelectorLight3, fontSelectorLight4, fontSelectorLight5, fontSelectorLight6, fontSelectorLight7, jLabel21, jLabel22, jLabel23, jLabel24, jLabel25, jLabel26, jLabel27});

    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1changeSelection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1changeSelection
        boolean show = jCheckBox1.isSelected();
        jLabel21.setEnabled(!show);
        jLabel22.setEnabled(!show);
        jLabel23.setEnabled(!show);
        jLabel24.setEnabled(!show);
        jLabel25.setEnabled(!show);
        jLabel26.setEnabled(!show);
        jLabel27.setEnabled(!show);
        fontSelectorLight1.setEnabled(!show);
        fontSelectorLight2.setEnabled(!show);
        fontSelectorLight3.setEnabled(!show);
        fontSelectorLight4.setEnabled(!show);
        fontSelectorLight5.setEnabled(!show);
        fontSelectorLight6.setEnabled(!show);
        fontSelectorLight7.setEnabled(!show);
        fontSelectorLight8.setEnabled(show);
    }//GEN-LAST:event_jCheckBox1changeSelection

    public boolean warnIfTitleIsPresent() {
        return jRadioButton1.isSelected();
    }

    public boolean warnIfColorsAreNotColorBlindFriendly() {
        return jRadioButton15.isSelected();
    }

    public boolean warnIfLegendTitleIsPresent() {
        return jRadioButton13.isSelected();
    }

    public boolean warnIfGridIsPresent() {
        return jRadioButton3.isSelected();
    }

    public boolean warnIfHasBgColor() {
        return jRadioButton5.isSelected();
    }

    public boolean warnIfUnitsAreMissing() {
        return jRadioButton7.isSelected();
    }

    public boolean warnIfAxisTitlesAreMissing() {
        return jRadioButton9.isSelected();
    }

    public boolean warnIfNobracketsAroundUnits() {
        return jRadioButton12.isSelected();
    }

    /**
     *
     * @return the GraphFont that contains all the parameters that should be
     * applied to graphs
     */
    public GraphFont getGF() {
        return new GraphFont(getMainTitleFont(), getXAxisTitleFont(), getYAxisTitleFont(), getXAxisTextFont(), getYAxisTextFont(), getLegendTitleFont(), getLegendTextFont(), getLineSize(), getPointSize(), warnIfTitleIsPresent(), warnIfGridIsPresent(), warnIfHasBgColor(), warnIfUnitsAreMissing(), warnIfAxisTitlesAreMissing(), warnIfNobracketsAroundUnits(), warnIfLegendTitleIsPresent(), warnIfColorsAreNotColorBlindFriendly());
    }

    /**
     *
     * @return the stroke size for line
     */
    private float getLineSize() {
        return ((Float) jSpinner1.getValue()).floatValue();
    }

    /**
     *
     * @return the stroke size for points
     */
    private float getPointSize() {
        return ((Float) jSpinner2.getValue()).floatValue();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        Font ft = new Font("Arial", 0, 12);
        GraphFont test = new GraphFont(ft, ft, ft, ft, ft, ft, ft, 1, 1, true, true, true, true, true, true, true, true);
        RFontsParameters iopane = new RFontsParameters(test);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Font Settings", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getGF().isWarnIfGridIsPresent());
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup8;
    private Dialogs.FontSelectorLight fontSelectorLight1;
    private Dialogs.FontSelectorLight fontSelectorLight2;
    private Dialogs.FontSelectorLight fontSelectorLight3;
    private Dialogs.FontSelectorLight fontSelectorLight4;
    private Dialogs.FontSelectorLight fontSelectorLight5;
    private Dialogs.FontSelectorLight fontSelectorLight6;
    private Dialogs.FontSelectorLight fontSelectorLight7;
    private Dialogs.FontSelectorLight fontSelectorLight8;
    public javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton16;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    // End of variables declaration//GEN-END:variables
}



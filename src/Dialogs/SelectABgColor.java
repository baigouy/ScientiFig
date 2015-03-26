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
package Dialogs;

import MyShapes.JournalParameters;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * SelectABgColor allows the user to define the resolution of images when they
 * are exported as rasters and also allows for a background color to be chosen
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class SelectABgColor extends javax.swing.JPanel {

    int journalRecommendedColorDPI = 300;
    int journalRecommendedBlackAndWhiteDPI = 600;

    /**
     * Creates new form SelectABgColor
     *
     * @param show_color_selection
     */
    public SelectABgColor(boolean show_color_selection, boolean show_convert_to_gray, JournalParameters jp) {
        initComponents();
        if (!show_color_selection) {
            jLabel1.setVisible(false);
            paintedButton21.setVisible(false);
        }
        if (!show_convert_to_gray) {
            jLabel3.setVisible(false);
            jRadioButton1.setVisible(false);
            jRadioButton2.setVisible(false);
        }
        if (jp != null) {
            this.journalRecommendedColorDPI = jp.getColorDPI();
            this.journalRecommendedBlackAndWhiteDPI = jp.getBWDPI();
            /**
             * if dpi values make no sense (less or equal to 1) we use the
             * default DPI values (300 and 600)
             */
            journalRecommendedColorDPI = journalRecommendedColorDPI <= 1 ? 300 : journalRecommendedColorDPI;
            journalRecommendedBlackAndWhiteDPI = journalRecommendedBlackAndWhiteDPI <= 1 ? 600 : journalRecommendedBlackAndWhiteDPI;
        }
        paintedButton21.setColor(0xFFFFFF);
    }

    /**
     *
     * @return the background color that must be used as background of raster
     * images
     */
    public int getBgColor() {
        return paintedButton21.getColor();
    }

    /**
     *
     * @return the export dpi of raster images
     */
    public int getDPI() {
        return ((Integer) jSpinner1.getValue());
    }

    public boolean logMagnificationChanges() {
        return jCheckBox1.isSelected();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        paintedButton21 = new Commons.PaintedButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Export Parameters"));

        paintedButton21.setText("Bg Color");
        paintedButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeColorActionPerformed(evt);
            }
        });

        jLabel1.setText("Select a background color:");

        jLabel2.setText("Output DPI:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(300), Integer.valueOf(72), null, Integer.valueOf(1)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkDPI(evt);
            }
        });

        jLabel3.setText("Force RGB image to be gray:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Yes");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeResoultionAccordingToColor(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("No");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeResoultionAccordingToColor(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));

        jCheckBox1.setText("Log image magnification changes");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(paintedButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButton2))
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paintedButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ChangeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeColorActionPerformed
        Color color = JColorChooser.showDialog(this, "Pick a Color", paintedButton21.getBackground());
        if (color != null) {
            paintedButton21.setColor(color);
        }
    }//GEN-LAST:event_ChangeColorActionPerformed

    private void checkDPI(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkDPI
        boolean isColored = !isForceGray();
        int DPItoMatch = isColored ? journalRecommendedColorDPI : journalRecommendedBlackAndWhiteDPI;
        if (DPItoMatch != -1) {
            if (getDPI() < DPItoMatch) {
                jLabel4.setText("<html>The export resolution seems to be too low<br>for your color settings and the journal you<br>selected, the resulting figure might<br>therefore appear pixelated, we recommend you set the dpi to " + DPItoMatch);
            } else {
                jLabel4.setText("");
            }
        } else {
            jLabel4.setText("");
        }
    }//GEN-LAST:event_checkDPI

    private void changeResoultionAccordingToColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeResoultionAccordingToColor
        if (isForceGray() && journalRecommendedBlackAndWhiteDPI != -1) {
            jSpinner1.setValue(journalRecommendedBlackAndWhiteDPI);
        } else {
            if (journalRecommendedColorDPI != -1) {
                jSpinner1.setValue(journalRecommendedColorDPI);
            }
        }
    }//GEN-LAST:event_changeResoultionAccordingToColor

    public boolean isForceGray() {
        return jRadioButton1.isSelected();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        SelectABgColor iopane = new SelectABgColor(true, true, null);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "SelectABgColor", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSpinner jSpinner1;
    private Commons.PaintedButton paintedButton21;
    // End of variables declaration//GEN-END:variables
}



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

import java.awt.Color;
import java.awt.Font;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * TextColor let's the user change text font, text bg color and text color
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class TextColor extends javax.swing.JPanel {

    /**
     * Empty constructor
     */
    public TextColor() {
        initComponents();
        paintedButton21.setColor(0xFFFFFF);
    }

    /**
     *
     * @return the new color for the text
     */
    public int getColor() {
        return paintedButton21.getColor();
    }

    /**
     *
     * @return the new background color behind the text
     */
    public Color getBgColor() {
        if (jRadioButton1.isSelected()) {
            return new Color(0x000000);
        }
        if (jRadioButton2.isSelected()) {
            return new Color(0xFFFFFF);
        }
        return null;
    }

    /**
     *
     * @return the font to be applied to the text
     */
    public Font getTextFont() {
        return fontSelectorLight1.getSelectedFont();
    }

    /**
     *
     * @return true if bolding should be overriden
     */
    public boolean isOverrideBolding() {
        return jCheckBox4.isSelected();
    }

    /**
     *
     * @return true if italics should be overriden
     */
    public boolean isOverrideItalics() {
        return jCheckBox5.isSelected();
    }

    /**
     *
     * @return true if bolding for letters should be overriden
     */
    public boolean isOverrideBoldingForLetters() {
        return jCheckBox6.isSelected();
    }

    /**
     *
     * @return true if the font should be changed
     */
    public boolean isChangeFont() {
        return jCheckBox3.isSelected();
    }

    /**
     *
     * @return true if the text color should be changed
     */
    public boolean isChangeColor() {
        return jCheckBox1.isSelected();
    }

    /**
     *
     * @return true if the background color behind the text should be changed
     */
    public boolean isChangeBgColor() {
        return jCheckBox2.isSelected();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        paintedButton21 = new Commons.PaintedButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        fontSelectorLight1 = new Dialogs.FontSelectorLight();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Select A Color"));

        jLabel1.setText("Text Color:");

        jLabel2.setText("Background:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Black Bg");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("White Bg");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("No Bg");

        paintedButton21.setText("Click Here To Select A Text Color");
        paintedButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintedButton21ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Change Text Color");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textColorChanged(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Change Bg Color");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgColorChanged(evt);
            }
        });

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Change Font");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontChanged(evt);
            }
        });

        jCheckBox4.setText("Override bolding");

        jCheckBox5.setText("Override Italics");

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Override bolding for letters");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSelectorLight1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jCheckBox2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton3))
                                    .addComponent(paintedButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox6)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(paintedButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3)
                    .addComponent(fontSelectorLight1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void paintedButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintedButton21ActionPerformed
        Color color = JColorChooser.showDialog(this, "Pick a Color", paintedButton21.getBackground());
        if (color != null) {
            paintedButton21.setColor(color);
        }
    }//GEN-LAST:event_paintedButton21ActionPerformed

    private void textColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textColorChanged
        boolean active = jCheckBox1.isSelected();
        jLabel1.setEnabled(active);
        paintedButton21.setEnabled(active);
    }//GEN-LAST:event_textColorChanged

    private void bgColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgColorChanged
        boolean active = jCheckBox2.isSelected();
        jLabel2.setEnabled(active);
        jRadioButton1.setEnabled(active);
        jRadioButton2.setEnabled(active);
        jRadioButton3.setEnabled(active);
    }//GEN-LAST:event_bgColorChanged

    private void fontChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontChanged
        boolean active = jCheckBox3.isSelected();
        fontSelectorLight1.setEnabled(active);
        jCheckBox4.setEnabled(active);
        jCheckBox5.setEnabled(active);
        jCheckBox6.setEnabled(active);
    }//GEN-LAST:event_fontChanged

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        TextColor iopane = new TextColor();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "TextColor", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private Dialogs.FontSelectorLight fontSelectorLight1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private Commons.PaintedButton paintedButton21;
    // End of variables declaration//GEN-END:variables
}



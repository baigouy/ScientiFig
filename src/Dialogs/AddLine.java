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

import Commons.CommonClassesLight;
import Commons.PaintedButton;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * AddLine is a dialog that allows the user to add vertical or horizontal
 * intercepts to their images
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class AddLine extends javax.swing.JPanel {

    /**
     * Creates new form AddLine
     */
    public AddLine() {
        initComponents();
    }

    /**
     *
     * @return the color of the line
     */
    public String getColor() {
        return CommonClassesLight.toHtmlColor(paintedButton21.getColor());
    }

    /**
     *
     * @return the width of the line
     */
    public String getLineWidth() {
        return lineDialog1.getLineWidth() + "";
    }

    /**
     *
     * @return the type of the line (solid, dashed, ...)
     */
    public String getLineType() {
        return lineDialog1.getLineStyle() + "";
    }

    /**
     *
     * @return the x or y position of the line
     */
    public String getPos() {
        return jSpinner1.getValue().toString();
    }

    /**
     *
     * @return the alpha transparency of the line (1 non transparent, 0
     * invisible)
     */
    public String getAlpha() {
        return jSpinner2.getValue().toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        lineDialog1 = new Dialogs.LineDialog();
        jLabel2 = new javax.swing.JLabel();
        paintedButton21 = new Commons.PaintedButton();
        jLabel3 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        jLabel1.setText("position:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(0.1d)));

        lineDialog1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setText("Color:");

        paintedButton21.setText("color");
        paintedButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorChooser(evt);
            }
        });

        jLabel3.setText("Alpha:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, 1.0d, 0.1d));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lineDialog1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paintedButton21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinner2)
                    .addComponent(jSpinner1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(paintedButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineDialog1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void colorChooser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorChooser
        PaintedButton tmp = (PaintedButton) (evt.getSource());
        Color color = JColorChooser.showDialog(this, "Pick a Color", tmp.getBackground());
        if (color != null) {
            tmp.setColor(color);
        }
    }//GEN-LAST:event_colorChooser

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        AddLine iopane = new AddLine();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "AddLine", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private Dialogs.LineDialog lineDialog1;
    private Commons.PaintedButton paintedButton21;
    // End of variables declaration//GEN-END:variables
}



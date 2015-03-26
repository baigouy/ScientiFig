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

import R.Axis;
import R.AxisOrientation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * AxisParameters is a dialog that allows the user to hide the axis, change the
 * orientation of the labels, ...
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class AxisParameters extends javax.swing.JPanel {

    String axis;

    /**
     * Creates new form AxisParameters
     *
     * @param axis
     */
    public AxisParameters(String axis) {
        initComponents();
        this.axis = axis;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hide_x_labels = new javax.swing.JCheckBox();
        reverseXAcis = new javax.swing.JCheckBox();
        remove_ticks = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Axis"));

        hide_x_labels.setText("hide labels");
        hide_x_labels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideLabelsPressed(evt);
            }
        });

        reverseXAcis.setText("reverse axis");

        remove_ticks.setText("hide ticks");

        jLabel1.setText("Lower Range:");
        jLabel1.setEnabled(false);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(0.05f)));
        jSpinner1.setEnabled(false);

        jLabel2.setText("Upper Range:");
        jLabel2.setEnabled(false);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(0.05f)));
        jSpinner2.setEnabled(false);

        jLabel3.setText("Interval:");
        jLabel3.setEnabled(false);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(0.049999952f)));
        jSpinner3.setEnabled(false);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Range Auto");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setRange(evt);
            }
        });

        jLabel4.setText("Text Orientation (Degrees)");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(0, 0, 359, 1));

        jLabel5.setText("Axis transformation:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "asn", "exp", "identity", "log", "log10", "probit", "recip", "reverse", "sqrt" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner4))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinner2)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSpinner1)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hide_x_labels)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reverseXAcis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remove_ticks))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hide_x_labels)
                    .addComponent(reverseXAcis)
                    .addComponent(remove_ticks))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @return true if the range must is defined by R, false if a custom range
     * must be used
     */
    public boolean isAutoRange() {
        return jCheckBox1.isSelected();
    }

    private void setRange(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setRange
        boolean hidden = isAutoRange();
        jLabel1.setEnabled(!hidden);
        jLabel2.setEnabled(!hidden);
        jLabel3.setEnabled(!hidden);
        jSpinner1.setEnabled(!hidden);
        jSpinner2.setEnabled(!hidden);
        jSpinner3.setEnabled(!hidden);
    }//GEN-LAST:event_setRange

    /**
     *
     * @return the axis name
     */
    public String getAxis() {
        return axis;
    }

    /**
     *
     * @return true if the axis must be hidden
     */
    public boolean isAxisHidden() {
        return hide_x_labels.isSelected();
    }

    /**
     *
     * @return the lower range for the axis
     */
    public float getLowerRange() {
        return ((Float) jSpinner1.getValue());
    }

    /**
     *
     * @return the upper range for the axis
     */
    public float getUpperRange() {
        return ((Float) jSpinner2.getValue());
    }

    /**
     *
     * @return the axis tick interval
     */
    public float getInterval() {
        return ((Float) jSpinner3.getValue());
    }

    /**
     *
     * @return the orientatioon of the axis labels
     */
    public int getAngleInDegrees() {
        return ((Integer) jSpinner4.getValue());
    }

    /**
     *
     * @return true if the axis must be reversed
     */
    public boolean reverseAxis() {
        return reverseXAcis.isSelected();
    }

    /**
     *
     * @return true if the ticks should be hidden
     */
    public boolean hideTicks() {
        return remove_ticks.isSelected();
    }

    /**
     *
     * @return the axis transformation (log, exp, ...)
     */
    public String getTransformation() {
        String sel = jComboBox1.getSelectedItem().toString();
        if (sel.toLowerCase().contains("none")) {
            return null;
        }
        return sel;
    }

    private void hideLabelsPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideLabelsPressed
        boolean hidden = isAxisHidden();
        jLabel4.setEnabled(!hidden);
        jSpinner4.setEnabled(!hidden);
    }//GEN-LAST:event_hideLabelsPressed

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     */
    public static void main(String args[]) {
        ArrayList<Object> extras = new ArrayList<Object>();
        AxisParameters iopane = new AxisParameters("x");
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Axis Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            Axis a = new Axis(iopane.getAxis());
            if (iopane.isAxisHidden()) {
                a.setRemoveAxis(true);
            } else {
                int orientation = iopane.getAngleInDegrees();
                if (orientation != 0) {
                    AxisOrientation ao = new AxisOrientation(iopane.getAxis(), orientation);
                    extras.add(ao);
                    System.out.println(ao);
                }
            }
            if (iopane.hideTicks()) {
                a.setRemoveTicks(true);
            }
            if (!iopane.isAutoRange()) {
                a.setRange(iopane.getLowerRange(), iopane.getUpperRange(), iopane.getInterval());
            }
            if (iopane.reverseAxis()) {
                a.setAxisFlip(true);
            }
            extras.add(a);
            System.out.println(a);
            System.out.println(extras);
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox hide_x_labels;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JCheckBox remove_ticks;
    private javax.swing.JCheckBox reverseXAcis;
    // End of variables declaration//GEN-END:variables
}



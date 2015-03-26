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

import R.ThemeLegendPosition;
import Commons.CommonClassesLight;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * LegendDialog controls the position of the legend of R plots
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 *
 */
public class LegendDialog extends javax.swing.JPanel {

    /**
     * Empty constructor
     */
    public LegendDialog() {
        initComponents();
        positionChanged(null);
    }

    /**
     *
     * @return true if the legend should be plotted over the graph
     */
    public boolean isInsideTheGraph() {
        return jRadioButton2.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted over the graph on the left
     * side of it
     */
    public boolean isInsideLeft() {
        return jRadioButton9.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted over the graph, in the upper
     * part of the graph
     */
    public boolean isInsideTop() {
        return jRadioButton7.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted on top of the graph area
     */
    public boolean isTop() {
        return jRadioButton3.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted below the graph area
     */
    public boolean isBottom() {
        return jRadioButton4.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted to the left of the graph
     * area
     */
    public boolean isLeft() {
        return jRadioButton5.isSelected();
    }

    /**
     *
     * @return true if the legend should be plotted to the right of the graph
     * area
     */
    public boolean isRight() {
        return jRadioButton6.isSelected();
    }

    /**
     *
     * @return true if the legend should be hidden
     */
    public boolean hideLegend() {
        return removeAllLegends.isSelected();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        removeAllLegends = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Legend Parameters"));

        removeAllLegends.setText("Hide Legend");
        removeAllLegends.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideAll(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Outside Position"));

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("Up");

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("Down");

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Left");

        buttonGroup2.add(jRadioButton6);
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Right");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton6))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton6))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Legend Outside The Graph");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Legend Inside The Graph");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionChanged(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Inside Position"));

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setSelected(true);
        jRadioButton7.setText("Up");

        buttonGroup3.add(jRadioButton8);
        jRadioButton8.setText("Down");

        buttonGroup4.add(jRadioButton9);
        jRadioButton9.setSelected(true);
        jRadioButton9.setText("Left");

        buttonGroup4.add(jRadioButton10);
        jRadioButton10.setText("Right");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton10)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton7)
                    .addComponent(jRadioButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton9)
                    .addComponent(jRadioButton10)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
            .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(removeAllLegends, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(removeAllLegends)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, jPanel2});

    }// </editor-fold>//GEN-END:initComponents

    private void hideAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideAll
        boolean hidden = removeAllLegends.isSelected();
        CommonClassesLight.enableOrDisablePanelComponents(jPanel1, !hidden);
        CommonClassesLight.enableOrDisablePanelComponents(jPanel2, !hidden);
        jRadioButton1.setEnabled(!hidden);
        jRadioButton2.setEnabled(!hidden);
    }//GEN-LAST:event_hideAll

    private void positionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionChanged
        CommonClassesLight.enableOrDisablePanelComponents(jPanel1, jRadioButton1.isSelected());
        CommonClassesLight.enableOrDisablePanelComponents(jPanel2, jRadioButton2.isSelected());
    }//GEN-LAST:event_positionChanged

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        ArrayList<Object> extras = new ArrayList<Object>();
        LegendDialog iopane = new LegendDialog();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Position Legend", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            if (!iopane.hideLegend()) {
                if (iopane.isInsideTheGraph()) {
                    int POS1;
                    int POS2;
                    if (iopane.isInsideLeft()) {
                        POS2 = ThemeLegendPosition.LEFT;
                    } else {
                        POS2 = ThemeLegendPosition.RIGHT;
                    }
                    if (iopane.isInsideTop()) {
                        POS1 = ThemeLegendPosition.TOP;
                    } else {
                        POS1 = ThemeLegendPosition.BOTTOM;
                    }
                    extras.add(new ThemeLegendPosition(POS1, POS2));
                } else {
                    int POS1 = 0;
                    if (iopane.isLeft()) {
                        POS1 = ThemeLegendPosition.LEFT;
                    }
                    if (iopane.isRight()) {
                        POS1 = ThemeLegendPosition.RIGHT;
                    }
                    if (iopane.isTop()) {
                        POS1 = ThemeLegendPosition.TOP;
                    }
                    if (iopane.isBottom()) {
                        POS1 = ThemeLegendPosition.BOTTOM;
                    }
                    extras.add(new ThemeLegendPosition(POS1));
                }
            } else {
                extras.add(new ThemeLegendPosition(ThemeLegendPosition.NOBAR));
            }
            System.out.println(extras);
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JCheckBox removeAllLegends;
    // End of variables declaration//GEN-END:variables
}



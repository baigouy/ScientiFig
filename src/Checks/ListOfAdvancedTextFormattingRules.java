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
package Checks;

import Commons.CommonClassesLight;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Benoit Aigouy
 */
public class ListOfAdvancedTextFormattingRules extends javax.swing.JPanel {

    ArrayList<PreviewSFTextController> rulePreviews = new ArrayList<PreviewSFTextController>();

    /**
     * Creates new form ListOfAdvancedTextFormattingRules
     */
    public ListOfAdvancedTextFormattingRules(ArrayList<SFTextController> rules, boolean selectModeOnly) {
        initComponents();
        if (rules != null) {
            for (SFTextController sFTextController : rules) {
                PreviewSFTextController psftc = new PreviewSFTextController(sFTextController, selectModeOnly);
                rulePreviews.add(psftc);
                jPanel1.add(psftc);
            }
        }
        jButton1.setVisible(!selectModeOnly);
        jButton2.setVisible(!selectModeOnly);
        jButton3.setVisible(selectModeOnly);
        jButton4.setVisible(selectModeOnly);
        CommonClassesLight.speedUpJScrollpane(jScrollPane1);
        jScrollPane1.validate();
    }

    public ArrayList<SFTextController> getRules() {
        if (rulePreviews == null) {
            return new ArrayList<SFTextController>();
        }
        ArrayList<SFTextController> rules = new ArrayList<SFTextController>();
        for (PreviewSFTextController previewSFTextController : rulePreviews) {
            SFTextController rule = previewSFTextController.getRule();
            if (rule != null && rule.check(false, null)) {
                rules.add(rule);
            }
        }
        return rules;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jButton1.setText("Add New Advanced Text Rule");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewRule(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("Add Existing Rule");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addExistingRule(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton3.setText("Select All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAll(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton4.setText("Select None");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNone(evt);
            }
        });
        jPanel2.add(jButton4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addNewRule(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewRule
        RegexRuleDesigner iopane = new RegexRuleDesigner();
        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Advanced Text Rule", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            SFTextController rule = iopane.getRule();
            if (rule != null) {
                PreviewSFTextController psftc = new PreviewSFTextController(rule, false);
                rulePreviews.add(psftc);
                jPanel1.add(psftc);
            }
            jScrollPane1.validate();
        }
    }//GEN-LAST:event_addNewRule

    private void addExistingRule(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addExistingRule
        ArrayList<SFTextController> clone = new ArrayList<SFTextController>();
        for (SFTextController sFTextController : SFTextController.list) {
            clone.add(new SFTextController(sFTextController));
        }
        ListOfAdvancedTextFormattingRules iopane = new ListOfAdvancedTextFormattingRules(clone, true);
        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Available Text Rules", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            ArrayList<SFTextController> rules = iopane.getRules();
            for (SFTextController sFTextController : rules) {
                PreviewSFTextController psftc = new PreviewSFTextController(sFTextController, false);
                rulePreviews.add(psftc);
                jPanel1.add(psftc);
            }
            jScrollPane1.validate();
        }
    }//GEN-LAST:event_addExistingRule

    private void selectAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAll
        for (PreviewSFTextController previewSFTextController : rulePreviews) {
            previewSFTextController.select(true);
        }
    }//GEN-LAST:event_selectAll

    private void selectNone(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNone
        for (PreviewSFTextController previewSFTextController : rulePreviews) {
            previewSFTextController.select(false);
        }
    }//GEN-LAST:event_selectNone

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        ListOfAdvancedTextFormattingRules iopane = new ListOfAdvancedTextFormattingRules(null, false);//SFTextController.list
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Advanced Text Rules", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getRules());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}


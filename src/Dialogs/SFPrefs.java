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

import Commons.CommonClassesLight;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

/**
 * SFPrefs is a dialog that allows the user to change the default SF prefs
 *
 * @author Benoit Aigouy
 */
public class SFPrefs extends javax.swing.JPanel {

    /**
     * Creates new form SFPrefs
     *
     * @param isUndoRedoActivated
     * @param remove_from_list
     * @param max_nb
     */
    public SFPrefs(boolean isUndoRedoActivated, int max_nb, boolean remove_from_list, boolean warn_on_save, boolean use_native_dialogs, boolean showHelpInfoWindow, boolean logErrors, boolean showHints, boolean autoScroll, boolean useAllCores, int max_nb_of_cores) {
        initComponents();
        max_nb = max_nb < 1 ? 1 : max_nb;
        jSpinner1.setValue(max_nb);
        jCheckBox1.setSelected(isUndoRedoActivated);
        jCheckBox2.setSelected(remove_from_list);
        jCheckBox3.setSelected(warn_on_save);
        jCheckBox4.setSelected(use_native_dialogs);
        jCheckBox5.setSelected(showHelpInfoWindow);
        jCheckBox6.setSelected(logErrors);
        jCheckBox7.setSelected(showHints);
        jCheckBox8.setSelected(autoScroll);
        jCheckBox9.setSelected(useAllCores);
        max_nb_of_cores = max_nb_of_cores <= 0 ? 1 : max_nb_of_cores;
        max_nb_of_cores = max_nb_of_cores < CommonClassesLight.getNumberOfCores() ? max_nb_of_cores : CommonClassesLight.getNumberOfCores();
        jSpinner2.setModel(new SpinnerNumberModel(max_nb_of_cores, 1, CommonClassesLight.getNumberOfCores(), 1));
        jSpinner2.setValue(max_nb_of_cores);
        allowUndoRedoChanged(null);
        useAllCoresPressed(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Preferences"));

        jCheckBox1.setText("Activate undos/redos");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowUndoRedoChanged(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(10, 1, 100, 1));
        jSpinner1.setEnabled(false);

        jLabel1.setText("Max number of undos/redos");
        jLabel1.setEnabled(false);

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Remove images from the list when they are added to a panel");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Warn before quitting if the image has not been saved");

        jCheckBox4.setText("Use native file dialogs when applicable (inactivated under windows)");

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Show help info window");

        jCheckBox6.setText("Log errors (requires standalone ImageJ/FIJI restart)");

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Show hints");

        jCheckBox8.setSelected(true);
        jCheckBox8.setText("Auto scroll when selection is changed");

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("Use all processors");
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useAllCoresPressed(evt);
            }
        });

        jLabel2.setText("Max number of processors to use");
        jLabel2.setEnabled(false);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1))
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox9)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allowUndoRedoChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowUndoRedoChanged
        boolean activate = jCheckBox1.isSelected();
        jLabel1.setEnabled(activate);
        jSpinner1.setEnabled(activate);
    }//GEN-LAST:event_allowUndoRedoChanged

    private void useAllCoresPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useAllCoresPressed
        boolean activate = jCheckBox9.isSelected();
        jLabel2.setEnabled(!activate);
        jSpinner2.setEnabled(!activate);
    }//GEN-LAST:event_useAllCoresPressed

    /**
     *
     * @return whether images should be removed from the list if they are added
     * to a montage
     */
    public boolean getRemoveFromList() {
        return jCheckBox2.isSelected();
    }

    /**
     * @return whether the errors should be logged (unfortunately it will log
     * all system.err not only errors linked to the soft
     */
    public boolean islogErrors() {
        return jCheckBox6.isSelected();
    }

    /**
     *
     * @return true if doubleLayerdPanes should scroll automatically to show the
     * selected object
     */
    public boolean isAutoScroll() {
        return jCheckBox8.isSelected();
    }

    /**
     *
     * @return the nb of allowed undos and redos
     */
    public int getNbOfUndosRedos() {
        return ((Integer) jSpinner1.getValue());
    }

    /**
     *
     * @return whether undos and redos should be allowed or not (the soft is
     * faster if they are not allowed)
     */
    public boolean isUndoAllowed() {
        return jCheckBox1.isSelected();
    }

    /**
     *
     * @return true if the soft must try to use native file dialogs
     */
    public boolean isUseNativeDialogsWhenApplicable() {
        return jCheckBox4.isSelected();
    }

    /**
     *
     * @return true if the info window on how to use video help should be
     * displayed or not
     */
    public boolean isShowHelpWindow() {
        return jCheckBox5.isSelected();
    }

    /**
     *
     * @return true if glasspane hints should be shown
     */
    public boolean isShowHints() {
        return jCheckBox7.isSelected();
    }

    /**
     *
     * @return true if all CPUs must be used in multithreaded functions
     */
    public boolean isUseAllCores() {
        return jCheckBox9.isSelected();
    }

    /**
     *
     * @return the number of cpus to use
     */
    public int getNumberOfCPU2Use() {
        if (isUseAllCores()) {
            return CommonClassesLight.getNumberOfCores();
        }
        return ((Integer) jSpinner2.getValue());
    }

    /**
     *
     * @return true if the soft must warn the user if he hasn't saved his image
     * before quitting
     */
    public boolean isWarnOnQuit() {
        return jCheckBox3.isSelected();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        SFPrefs iopane = new SFPrefs(true, WIDTH, true, true, true, true, true, true, true, true, WHEN_FOCUSED);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "SFPrefs", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    // End of variables declaration//GEN-END:variables
}



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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Benoit Aigouy
 */
public class GenericTextCheckAndEditDialog extends javax.swing.JPanel {

    private boolean isRepaceMode = false;
    private static final String[] replace = new String[]{"replace", "replaceAll", "replaceFirst"};
    private static final String[] checks = new String[]{"contains", "startsWith", "endsWith", "matches"};

    /**
     * Creates new form GenericTextCheckAndEditDialog
     */
    public GenericTextCheckAndEditDialog(boolean isReplaceMode, String... previousValues) {
        initComponents();
        if (isReplaceMode) {
            jComboBox1.setModel(new DefaultComboBoxModel(replace));
            jButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jTextField2.setText(jTextField2.getText() + "a");
                }
            });
        } else {
            jComboBox1.setModel(new DefaultComboBoxModel(checks));
        }
        /*
         * we try to reload values
         */
        if (previousValues != null && previousValues.length >= 1) {
            jComboBox1.setSelectedItem(previousValues[0]);
            if (previousValues.length >= 2) {
                jTextField1.setText(previousValues[1]);
            }
            if (previousValues.length >= 3) {
                jTextField2.setText(previousValues[2]);
            }
        }
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextField1.setText(jTextField1.getText() + "b");
            }
        });
        this.isRepaceMode = isReplaceMode;
        jLabel2.setVisible(isReplaceMode);
        jTextField2.setVisible(isReplaceMode);
        jButton2.setVisible(isReplaceMode);
        /*
         * TODO replace this at some point by some unicode char selector
         */
        if (true) {
            jButton2.setVisible(false);
            jButton1.setVisible(false);
        }
    }

    public String getFormattedOuptut() {
        if (jTextField1.getText().equals("")) {
            String mode = "control";
            if (isRepaceMode) {
                mode = "replacement";
            }
            CommonClassesLight.Warning(this, "(text length is 0) your" + mode + " is invalid,\nplease put a longer text");
            return null;
        }
        String command = jComboBox1.getSelectedItem().toString() + "(\"" + jTextField1.getText() + "\"";
        if (isRepaceMode) {
            command += ", \"" + jTextField2.getText() + "\"";
        }
        command += ")";
        return command;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new JButton(""+CommonClassesLight.greek.get("alpha")+CommonClassesLight.SMILEY+CommonClassesLight.ANGSTROMS);
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new JButton(""+CommonClassesLight.greek.get("alpha")+CommonClassesLight.SMILEY+CommonClassesLight.ANGSTROMS);

        jComboBox1.setPreferredSize(new java.awt.Dimension(190, 19));

        jLabel1.setText("operation");

        jTextField1.setText("something");
        jTextField1.setPreferredSize(new java.awt.Dimension(190, 19));

        jLabel2.setText("by");

        jTextField2.setText("something else (including unicode \\u0063)");
        jTextField2.setPreferredSize(new java.awt.Dimension(190, 19));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jComboBox1, 0, 172, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jButton1)
                .addGap(5, 5, 5)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jComboBox1});

    }// </editor-fold>//GEN-END:initComponents

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        GenericTextCheckAndEditDialog iopane = new GenericTextCheckAndEditDialog(true, "replaceFirst", "toto", "tutu");
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "GenericTextCheckAndEditDialog", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getFormattedOuptut());
            //do smthg
        }
        iopane = new GenericTextCheckAndEditDialog(false);
        result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "GenericTextCheckAndEditDialog", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getFormattedOuptut());
            //do smthg
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}


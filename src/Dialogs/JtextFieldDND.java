/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2014 Benoit Aigouy 

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
import Commons.DropTargetHandler;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 * JtextFieldDND is just a textField that supports DND
 *
 * @author Benoit Aigouy
 */
public class JtextFieldDND extends javax.swing.JPanel {

    public static final long serialVersionUID = 5447412807391271349L;

    /**
     * Creates new form JtextFieldDND (a jtextfield that supports DND)
     */
    public JtextFieldDND() {
        initComponents();
        DropTarget dt = new DropTarget(jTextField1, new DropTargetHandler() {
            @Override
            public void doSomethingWithTheFile(String data) {
                setText(data);
            }
        });
        jLabel1.setVisible(false);
        jButton1.setVisible(false);
    }

    /**
     * Defines whether the label preceding the JtextField should be shown or not
     *
     * @param bool
     */
    public void showLabel(boolean bool) {
        jLabel1.setVisible(bool);
    }

    /**
     * Defines whether the jbutton following the JtextField should be shown or
     * not
     *
     * @param bool
     */
    public void showButton(boolean bool) {
        jButton1.setVisible(bool);
    }

    /**
     * Sets the text of the jButton and eventually show it depending on the text
     *
     * @param text
     */
    public void setButtonText(String text) {
        if (text == null || text.equals("")) {
            showButton(false);
            return;
        }
        jButton1.setText(text);
        showButton(true);
    }

    /**
     * Sets the text of the jLabel and eventually show it depending on the text
     *
     * @param text
     */
    public void setLabelText(String text) {
        if (text == null || text.equals("")) {
            showLabel(false);
            return;
        }
        jLabel1.setText(text);
        showLabel(true);
    }

    /**
     *
     * @return the text of the jTextField
     */
    public String getText() {
        return CommonClassesLight.change_path_separators_to_system_ones(jTextField1.getText());
    }

    /**
     * Sets the text of the jTextField
     *
     * @param text
     */
    public void setText(String text) {
        if (text != null) {
            jTextField1.setText(CommonClassesLight.change_path_separators_to_system_ones(text));
        } else {
            jTextField1.setText("");
        }
    }

    /**
     *
     * @return true if the textField does not contain text
     */
    public boolean isEmpty() {
        if (jTextField1.getText() == null || jTextField1.getText().equals("")) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Text:");
        add(jLabel1);
        add(jTextField1);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPressed(evt);
            }
        });
        add(jButton1);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * just override this to get the button to execute your own code
     */
    public void executeCustomCode(ActionEvent evt) {
    }

    private void buttonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPressed
        executeCustomCode(evt);
    }//GEN-LAST:event_buttonPressed

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        JtextFieldDND iopane = new JtextFieldDND();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "JtextFieldDND", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}



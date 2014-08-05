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

import javax.swing.JOptionPane;

/**
 * VectorOutputParameters is a dialog that allows the user to select the
 * resolution of vector graphics upon export as svg (typically 72dpi for an
 * Illustrator svg and 90 dpi for an inkscape one)
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class VectorOutputParameters extends javax.swing.JPanel {

    /**
     * Creates new form SelectABgColor
     */
    public VectorOutputParameters() {
        initComponents();
    }

    /**
     *
     * @return the output dpi
     */
    public int getDpi() {
        String sel = jComboBox1.getSelectedItem().toString();
        if (sel.contains("72")) {
            return 72;
        } else {
            return 90;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Export Parameters"));
        setLayout(new java.awt.GridLayout(2, 1));

        jLabel3.setText("Vector Graphics Editor:");
        add(jLabel3);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default (72 dpi)", "Illustrator (72 dpi) (quick)", "Inkscape (90 dpi) (slow)" }));
        add(jComboBox1);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        VectorOutputParameters iopane = new VectorOutputParameters();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "VectorOutputParameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}



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

import R.Facets;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * FacetParameters is a dialog that allows the user to select ggplot2 facets
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class FacetParameters extends javax.swing.JPanel {

    /**
     * Constructor
     *
     * @param col list of all columns that can be used in plots
     * @param aleady_factorizable_columns columns that can be used as factors
     * @param safeMode if true factors can only be selected from the
     */
    public FacetParameters(ArrayList<String> col, ArrayList<String> aleady_factorizable_columns, boolean safeMode) {
        initComponents();
        String[] columns = col.toArray(new String[col.size()]);
        String[] already_factorizable = aleady_factorizable_columns.toArray(new String[aleady_factorizable_columns.size()]);
        String[] modified_table;
        if (safeMode) {
            int begin = 1;
            modified_table = new String[already_factorizable.length + 1];
            modified_table[0] = "None";
            for (String string : already_factorizable) {
                modified_table[begin] = string;
                begin++;
            }
        } else {
            int begin = 1;
            modified_table = new String[columns.length + 1];
            modified_table[0] = "None";
            for (String string : columns) {
                modified_table[begin] = string;
                begin++;
            }
        }
        jComboBox6.setModel(new DefaultComboBoxModel(modified_table));
        jComboBox7.setModel(new DefaultComboBoxModel(modified_table));
    }

    /**
     *
     * @return the horizontal facet
     */
    public String getFacetH() {
        return getComboSelection(jComboBox6);
    }

    /**
     *
     * @return the vertical facet
     */
    public String getFacetV() {
        return getComboSelection(jComboBox7);
    }

    private String getComboSelection(JComboBox combo) {
        if (combo.getSelectedItem().toString().toLowerCase().equals("none")) {
            return null;
        }
        return combo.getSelectedItem().toString();
    }

    /**
     *
     * @return true if the free scale mode should be used
     */
    public boolean isFreeScale() {
        return jCheckBox1.isSelected();
    }

    /**
     *
     * @return true if the free space mode should be used
     */
    public boolean isFreeSpace() {
        return jCheckBox2.isSelected();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     */
    public static void main(String args[]) {
        ArrayList<String> vals = new ArrayList<String>();
        vals.add("x");
        vals.add("y");
        vals.add("type");
        ArrayList<String> alreadyfactoirizable = new ArrayList<String>();
        alreadyfactoirizable.add("type");
        ArrayList<Object> extras = new ArrayList<Object>();
        FacetParameters iopane = new FacetParameters(vals, alreadyfactoirizable, true);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Select Data", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String facetH = iopane.getFacetH();
            String facetV = iopane.getFacetV();
            if (facetH != null || facetV != null) {
                Facets f = new Facets(facetV, facetH, iopane.isFreeScale(), iopane.isFreeSpace());
                extras.add(f);
                System.out.println(f);
                System.out.println(extras);
            }
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("R Facets"));

        jLabel6.setText("facet horiz:");

        jLabel7.setText("facet vert:");

        jCheckBox1.setText("Free Scale");

        jCheckBox2.setText("Free Space");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox7, 0, 287, Short.MAX_VALUE)
                            .addComponent(jComboBox6, 0, 287, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}



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
package R.Maths;

import Commons.CommonClassesLight;
import R.RSession.MyRsessionLogger;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.rosuda.REngine.REXPMismatchException;

/**
 *
 * @author Benoit Aigouy
 */
public class RMathFormulaEditor extends javax.swing.JPanel {
    
    DefaultListModel listModelOfMathsElements = new DefaultListModel();
    DefaultListModel listModelOfFinalMathsFormula = new DefaultListModel();
    MyRsessionLogger r;

    /**
     * Creates new form RMathFormulaEditor
     */
    public RMathFormulaEditor(MyRsessionLogger r) {
        initComponents();
        this.r = r;
        jList1.setModel(listModelOfMathsElements);
        jList2.setModel(listModelOfFinalMathsFormula);
        updatePlotPreview(null);
        hideOrShowJcombo1(null);
        hideOrShowJcombo2(null);
        hideOrShowJcombo3(null);
        hideOrShowJcombo4(null);
        selectionChanged(null);
    }
    
    public RMathFormulaEditor(MyRsessionLogger r, RFormula formula) {
        this(r);
        if (formula != null) {
            ArrayList<RMathElement> mathElements = formula.getElements();
            for (RMathElement rMathElement : mathElements) {
                listModelOfFinalMathsFormula.addElement(rMathElement);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        imagePaneLight2 = new Dialogs.ImagePaneLight();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jTextField3 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jTextField4 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jButton11 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Formula"));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jList1.setBorder(javax.swing.BorderFactory.createTitledBorder("Formula elements (temporary only)"));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                currentMathElementChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton2.setText("add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jList2.setBorder(javax.swing.BorderFactory.createTitledBorder("Final formula (will be exported)"));
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                currentMathElementChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList2);

        javax.swing.GroupLayout imagePaneLight2Layout = new javax.swing.GroupLayout(imagePaneLight2);
        imagePaneLight2.setLayout(imagePaneLight2Layout);
        imagePaneLight2Layout.setHorizontalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePaneLight2Layout.setVerticalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_27.png"))); // NOI18N
        jButton3.setToolTipText("Move element to final formula");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3runAll(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_29.png"))); // NOI18N
        jButton4.setToolTipText("remove selected element from final formula");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4runAll(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "custom text (with greek)", "custom text", "raw R code (experts only)", "concatenate", "space", "fraction", "atop", "sum", "integral", "limit", "minimum of", "union", "italic", "bold", "bold+italic", "hat", "wide hat", "square root", "square root custom power", "tilde", "wide tilde", "dot", "ring", "bar", "underline", "infimum", "supremum", "group in between [ ... ]", "group in between ( ... )", "group in between { ... }", "bgroup in between [ ... ] wide brackets", "bgroup in between ( ... ) wide brackets", "bgroup in between { ... } wide brackets", "superior or equal", "inferior or equal", "small", "very small", "congruent", "different", "distributed as", "divide", "multiply", "dot product", "double arrow", "double up arrow", "double down arrow", "down arrow", "equal", "approximately equal to", "equivalent arrow", "identical", "implies left", "implies right", "in", "left arrow", "not in", "not subset", "+/-", "product of", "proper subset", "proper superset", "proportional to", "right arrow", "subscript", "superscript", "superset", "up arrow" }));
        jComboBox1.setToolTipText("");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionChanged(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_30.png"))); // NOI18N
        jButton5.setToolTipText("reorder elements (move selection up)");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_28.png"))); // NOI18N
        jButton6.setToolTipText("reorder elements (move selection down)");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel1.setText("Operation:");

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Cutsom");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo2(evt);
            }
        });
        jPanel2.add(jRadioButton4);

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("Existing Element");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo2(evt);
            }
        });
        jPanel2.add(jRadioButton3);
        jPanel2.add(jTextField2);

        jPanel2.add(jComboBox3);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Cutsom");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo3(evt);
            }
        });
        jPanel3.add(jRadioButton6);

        buttonGroup3.add(jRadioButton5);
        jRadioButton5.setText("Existing Element");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo3(evt);
            }
        });
        jPanel3.add(jRadioButton5);
        jPanel3.add(jTextField3);

        jPanel3.add(jComboBox4);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        buttonGroup4.add(jRadioButton8);
        jRadioButton8.setSelected(true);
        jRadioButton8.setText("Cutsom");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo4(evt);
            }
        });
        jPanel4.add(jRadioButton8);

        buttonGroup4.add(jRadioButton7);
        jRadioButton7.setText("Existing Element");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo4(evt);
            }
        });
        jPanel4.add(jRadioButton7);
        jPanel4.add(jTextField4);

        jPanel4.add(jComboBox5);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Cutsom");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo1(evt);
            }
        });
        jPanel5.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Existing Element");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideOrShowJcombo1(evt);
            }
        });
        jPanel5.add(jRadioButton2);
        jPanel5.add(jTextField1);

        jPanel5.add(jComboBox2);

        jButton11.setText("preview");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        jButton7.setToolTipText("reorder elements (move selection down)");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jButton1.setText("help");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openHelp(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton3)
                                            .addComponent(jButton4))))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imagePaneLight2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {imagePaneLight1, jScrollPane1});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {imagePaneLight2, jScrollPane3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton11)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(imagePaneLight2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, Short.MAX_VALUE)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel2, jPanel3, jPanel4, jPanel5});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAll
        Object src = null;
        if (evt != null) {
            src = evt.getSource();
        }
        if (src == jButton2 || src == jButton11) {
            String operation = jComboBox1.getSelectedItem().toString();
            String param1 = jRadioButton2.isSelected() && jComboBox2.getSelectedItem() != null ? jComboBox2.getSelectedItem().toString() : jTextField1.getText();
            String param2 = jRadioButton3.isSelected() && jComboBox3.getSelectedItem() != null ? jComboBox3.getSelectedItem().toString() : jTextField2.getText();
            String param3 = jRadioButton5.isSelected() && jComboBox4.getSelectedItem() != null ? jComboBox4.getSelectedItem().toString() : jTextField3.getText();
            String param4 = jRadioButton7.isSelected() && jComboBox5.getSelectedItem() != null ? jComboBox5.getSelectedItem().toString() : jTextField4.getText();
            RMathElement maths = new RMathElement(operation, param1, param2, param3, param4);
            if (maths.isEmpty()) {
                maths = null;
            }
            /*
             * ignore empty statements
             */
            if (maths != null && maths.toString().equals("paste(')")) {
                return;
            }
            if (src == jButton2) {
                listModelOfMathsElements.addElement(maths);
                jList1.setSelectedIndex(listModelOfMathsElements.size() - 1);
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
            } else {
                updatePlotPreview(maths);
            }
        }
        if (src == jButton5) {
            int idx = jList2.getSelectedIndex();
            if (CommonClassesLight.swap(listModelOfFinalMathsFormula, idx, idx - 1)) {
                jList2.setSelectedIndex(idx - 1);
            }
        }
        if (src == jButton6) {
            int idx = jList2.getSelectedIndex();
            if (CommonClassesLight.swap(listModelOfFinalMathsFormula, idx, idx + 1)) {
                jList2.setSelectedIndex(idx + 1);
            }
        }
        if (src == jButton7) {
            int idx = jList2.getSelectedIndex();
            if (idx != -1) {
                listModelOfFinalMathsFormula.remove(idx);
            }
        }
    }//GEN-LAST:event_runAll
    
    private void jButton3runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3runAll
        int idx = jList1.getSelectedIndex();
        if (idx != -1) {
            listModelOfFinalMathsFormula.addElement(listModelOfMathsElements.get(idx));
            listModelOfMathsElements.remove(idx);
            jList2.setSelectedIndex(listModelOfFinalMathsFormula.size() - 1);
        }
    }//GEN-LAST:event_jButton3runAll
    
    private void jButton4runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4runAll
        int idx = jList2.getSelectedIndex();
        if (idx != -1) {
            listModelOfMathsElements.addElement(listModelOfFinalMathsFormula.get(idx));
            listModelOfFinalMathsFormula.remove(idx);
            jList1.setSelectedIndex(listModelOfMathsElements.size() - 1);
        }
    }//GEN-LAST:event_jButton4runAll
    
    private void currentMathElementChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_currentMathElementChanged
        if (!evt.getValueIsAdjusting()) {
            updatePlotPreview((JList) evt.getSource());
            updateCombos();
        }
    }//GEN-LAST:event_currentMathElementChanged
    
    private void hideOrShowJcombo1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideOrShowJcombo1
        boolean isSelected = jRadioButton1.isSelected();
        jTextField1.setVisible(isSelected);
        jComboBox2.setVisible(!isSelected);
    }//GEN-LAST:event_hideOrShowJcombo1
    
    private void hideOrShowJcombo3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideOrShowJcombo3
        boolean isSelected = jRadioButton6.isSelected();
        jTextField3.setVisible(isSelected);
        jComboBox4.setVisible(!isSelected);
    }//GEN-LAST:event_hideOrShowJcombo3
    
    private void hideOrShowJcombo4(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideOrShowJcombo4
        boolean isSelected = jRadioButton8.isSelected();
        jTextField4.setVisible(isSelected);
        jComboBox5.setVisible(!isSelected);
    }//GEN-LAST:event_hideOrShowJcombo4
    
    private void selectionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionChanged
        String selection = jComboBox1.getSelectedItem().toString();
        //custom text, space, fraction, atop, sum, integral
        CommonClassesLight.enableOrDisablePanelComponents(jPanel5, false);
        CommonClassesLight.enableOrDisablePanelComponents(jPanel2, false);
        CommonClassesLight.enableOrDisablePanelComponents(jPanel3, false);
        CommonClassesLight.enableOrDisablePanelComponents(jPanel4, false);
        jPanel5.setBorder(null);
        jPanel2.setBorder(null);
        jPanel3.setBorder(null);
        jPanel4.setBorder(null);
        if (selection.contains("text") || selection.contains("bold")
                || selection.contains("italic") || selection.contains("hat")
                || selection.equals("square root") || selection.contains("tilde")
                || selection.equals("dot") || selection.contains("ring")
                || selection.contains("bar") || selection.contains("underline")
                || selection.contains("infimum") || selection.contains("supremum")
                || selection.contains("group") || selection.contains("small")
                || selection.contains("raw")) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Text or Formula"));
        } else if (selection.contains("fraction") || selection.contains("atop") || selection.contains("erior") || selection.contains("square root custom power")) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Numerator"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Denominator"));
        } else if (selection.contains("congruent") || selection.contains("different")
                || selection.contains("distributed") || selection.contains("divide")
                || selection.contains("multiply") || selection.contains("dot product")
                || selection.contains("arrow") || selection.contains("equal")
                || selection.contains("identical") || selection.contains("implies")
                || selection.equals("in") || selection.contains("product of")
                || selection.contains("subset") || selection.contains("superset")
                || selection.contains("proportional") || selection.contains("subscript")
                || selection.contains("superscript") || selection.equals("concatenate")) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Left term"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Right term"));
        } else if (selection.contains("sum") || selection.contains("intersection") || selection.contains("union")) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Equation"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Variable"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel3, true);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("From"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel4, true);
            jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("To"));
        } else if (selection.contains("integral") || selection.contains("limit") || selection.contains("minimum of")) {
            CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Equation"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("From/Letter"));
            CommonClassesLight.enableOrDisablePanelComponents(jPanel3, true);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("To"));
        }
    }//GEN-LAST:event_selectionChanged
    
    private void hideOrShowJcombo2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideOrShowJcombo2
        boolean isSelected = jRadioButton4.isSelected();
        jTextField2.setVisible(isSelected);
        jComboBox3.setVisible(!isSelected);
    }//GEN-LAST:event_hideOrShowJcombo2
        
    private void openHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openHelp
        /*
         * we open the help for R maths //ca marche pas pkoi ???
         */
        CommonClassesLight.browse("http://stat.ethz.ch/R-manual/R-devel/library/grDevices/html/plotmath.html");
    }//GEN-LAST:event_openHelp
    
    private void updateCombos() {
        Object[] objects = listModelOfMathsElements.toArray();
        if (objects != null) {
            jComboBox2.setModel(new DefaultComboBoxModel(objects));
            jComboBox3.setModel(new DefaultComboBoxModel(objects));
            jComboBox4.setModel(new DefaultComboBoxModel(objects));
            jComboBox5.setModel(new DefaultComboBoxModel(objects));
        } else {
            jComboBox2.setModel(new DefaultComboBoxModel());
            jComboBox3.setModel(new DefaultComboBoxModel());
            jComboBox4.setModel(new DefaultComboBoxModel());
            jComboBox5.setModel(new DefaultComboBoxModel());
        }
        if (objects != null && objects.length > 0) {
            jComboBox2.setSelectedIndex(0);
            jComboBox3.setSelectedIndex(0);
            jComboBox4.setSelectedIndex(0);
            jComboBox5.setSelectedIndex(0);
        } else {
            jComboBox2.setSelectedIndex(-1);
            jComboBox3.setSelectedIndex(-1);
            jComboBox4.setSelectedIndex(-1);
            jComboBox5.setSelectedIndex(-1);
        }
    }

    private void updatePlotPreview(Object list) {
        if (list == null || r == null || !r.isRserverRunning()) {
            BufferedImage black = new BufferedImage(imagePaneLight1.getSize().width, imagePaneLight1.getSize().height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = black.createGraphics();
            g2d.setColor(Color.WHITE);
            if (r == null || !r.isRserverRunning()) {
                g2d.drawString("R connection Failed", 128, 32);
            }
            g2d.drawString("No Preview Available", 128, 64);
            g2d.dispose();
            imagePaneLight1.setImage(black);
            imagePaneLight2.setImage(black);
        } else {
            /**
             * We create an empty plot so that only the text of the formula is
             * visible
             */
            String simplePlot = "ggplot(mtcars, aes(x=hp, y=mpg))"
                    + "\n+ geom_blank()"
                    + "\n+ theme(aspect.ratio = 0.0001)" //--> on triche un peu pr cacher la plot area
                    + "\n+ theme(axis.ticks = element_blank(), axis.text = element_blank())"
                    + "\n+scale_x_discrete(breaks=NULL)"
                    + "\n+scale_y_discrete(breaks=NULL)"
                    + "\n+ xlab('')"
                    + "\n+ ylab('')";
            
            if (list == jList1 || list instanceof RMathElement) {
                int idx = -1;
                if (list instanceof JList) {
                    idx = ((JList) list).getSelectedIndex();
                }
                if (idx != -1) {
                    simplePlot += "\n+ ggtitle(expression(" + ((JList) list).getSelectedValue().toString() + "))";
                } else if (list instanceof RMathElement) {
                    simplePlot += "\n+ ggtitle(expression(" + list.toString() + "))";
                }
                imagePaneLight1.setImage(r.getPreview(simplePlot, imagePaneLight1.getSize().width, imagePaneLight1.getSize().height));
            } else {
                RFormula concatenatedFormula = concatenateFormula();
                if (!concatenatedFormula.isEmpty()) {
                    simplePlot += "\n+ ggtitle(" + concatenatedFormula.toRExpression() + ")";
                }
                imagePaneLight2.setImage(r.getPreview(simplePlot, imagePaneLight2.getSize().width, imagePaneLight2.getSize().height));
            }
        }
    }
    
    public RFormula concatenateFormula() {
        int size = listModelOfFinalMathsFormula.getSize();
        if (size == 0) {
            if (jList1.getSelectedIndex() == -1) {
                return null;
            } else {
                /**
                 * the user didn't create a final formula and did not cancel -->
                 * we assume he is happy with the current formula element, so we
                 * return it
                 */
                return new RFormula((RMathElement) jList1.getSelectedValue());
            }
        }
        RFormula rf = new RFormula(listModelOfFinalMathsFormula.toArray());
        return rf;
    }
    
    public static void main(String[] args) {
        //ggplot(mtcars, aes(x=hp, y=mpg))+ geom_blank()+ theme(aspect.ratio = 0.0001)+ theme(axis.ticks = element_blank(), axis.text = element_blank())+scale_x_discrete(breaks=NULL)+ xlab('')+ ylab('')
        MyRsessionLogger r = new MyRsessionLogger();
        try {
            r.reopenConnection();
        } catch (REXPMismatchException ex) {
        }
        RMathFormulaEditor iopane = new RMathFormulaEditor(r);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "RMathFormulaEditor", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("-->" + iopane.concatenateFormula());
        }
        r.close();
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private Dialogs.ImagePaneLight imagePaneLight1;
    private Dialogs.ImagePaneLight imagePaneLight2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}


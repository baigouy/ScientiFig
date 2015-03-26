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
import R.Maths.RFormula;
import R.Maths.RMathFormulaEditor;
import R.RSession.MyRsessionLogger;
import R.String.RLabel;
import R.StyledDoc2R;
import Tools.StyledDocTools;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Benoit Aigouy
 */
public class RLabelEditor extends javax.swing.JPanel {

    MyRsessionLogger r;
    RLabel rlabel;

    public RLabelEditor() {
        initComponents();
    }

    /**
     * Creates new form RLabelEditor
     */
    public RLabelEditor(MyRsessionLogger r, RLabel rlabel) {
        this();
        this.r = r;
        this.rlabel = rlabel;
        if (this.rlabel == null) {
            this.rlabel = new RLabel();
        }
        reloadTextToLabels();
    }

    private void reloadTextToLabels() {
        text.setText("");
        maths.setText("");
        unit.setText("");
        if (rlabel != null) {
            text.setText(rlabel.getMainTextAsString());
            maths.setText(rlabel.getFormulaAsString());
            unit.setText(rlabel.getUnitAsString());
            jCheckBox1.setSelected(rlabel.isAutoBracket());
        }
    }

    public RLabelEditor(MyRsessionLogger r, String rlabel) {
        this(r, new RLabel(rlabel));
    }

    public RLabel getRlabel() {
        return rlabel;
    }

    public void setTitle(String title) {
        setBorder(javax.swing.BorderFactory.createTitledBorder(title));
    }

    public void setRlabel(RLabel rlabel) {
        this.rlabel = rlabel;
        reloadTextToLabels();
    }

    public void setR(MyRsessionLogger r) {
        this.r = r;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel19 = new javax.swing.JLabel();
        text = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        maths = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        unit = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("R label"));

        jLabel19.setText("Text:");

        text.setEditable(false);

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        maths.setEditable(false);

        jButton6.setText("Edit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        unit.setEditable(false);

        jButton8.setText("Edit");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("surround with brackets");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        jLabel20.setText("Maths:");

        jLabel21.setText("Units:");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(unit, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(maths)
                            .addComponent(text))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jButton3)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(maths, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6)
                        .addComponent(jLabel20))
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(unit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8)
                        .addComponent(jCheckBox1)
                        .addComponent(jLabel21))
                    .addComponent(jButton4))
                .addGap(0, 1, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton6, jButton8, jCheckBox1, jLabel19, jLabel20, jLabel21, maths, text, unit});

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Captures any change performed to the Rlabel. NB override this to capture
     * changes from outside.
     *
     * @param source
     */
    public void somethingChanged(Object source) {
        if (source == jButton8) {
            StyledDocument copy = null;
            if (rlabel.getUnit() != null) {
                copy = StyledDocTools.cloneDoc(rlabel.getUnit().doc);
            }
            ColoredTextPane iopane = new ColoredTextPane(rlabel.getUnit(), false, false, false);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add text", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                unit.setText(new StyledDoc2R().convertStyledDocToR(iopane.getDoc()));
                rlabel.setUnit(iopane.ctps);
                rlabel.setAutoBracket(jCheckBox1.isSelected());
            } else {
                if (rlabel.getUnit() != null) {
                    rlabel.getUnit().doc = copy;
                }
            }
        }
        if (source == jButton6) {
            if (r == null) {
                r = CommonClassesLight.getR();
            }
            RMathFormulaEditor iopane = new RMathFormulaEditor(r, rlabel.getFormula());
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Create a formula", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                RFormula rf = iopane.concatenateFormula();
                rlabel.setFormula(rf);
                if (rf == null) {
                    maths.setText("");
                } else {
                    maths.setText(rf.toString());
                }
            }
        }
        if (source == jButton1) {
            StyledDocument copy = null;
            if (rlabel.getMainText() != null) {
                copy = StyledDocTools.cloneDoc(rlabel.getMainText().doc);
            }
            ColoredTextPane iopane = new ColoredTextPane(rlabel.getMainText(), false, false, false);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add text", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                text.setText(new StyledDoc2R().convertStyledDocToR(iopane.getDoc()));
                rlabel.setMainText(iopane.ctps);
            } else {
                if (rlabel.getMainText() != null) {
                    rlabel.getMainText().doc = copy;
                }
            }
        }
        if (source == jButton2) {
            rlabel.setMainText(null);
            text.setText("");
        }
        if (source == jButton4) {
            rlabel.setUnit(null);
            unit.setText("");
        }
        if (source == jButton3) {
            rlabel.setFormula(null);
            maths.setText("");
        }
        if (source == jCheckBox1) {
            rlabel.setAutoBracket(jCheckBox1.isSelected());
        }
    }

    private void editPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPressed
        Object src = evt.getSource();
        somethingChanged(src);
    }//GEN-LAST:event_editPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JTextField maths;
    private javax.swing.JTextField text;
    private javax.swing.JTextField unit;
    // End of variables declaration//GEN-END:variables
}


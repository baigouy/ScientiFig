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

import javax.swing.JOptionPane;

/**
 * LineGraphicsDialog is a dialog to handle whether line strokes of various
 * objects should be set according to the selectd journal style otherwise change
 * is ignored
 *
 * @author Benoit Aigouy
 */
public class LineGraphicsDialog extends javax.swing.JPanel {

    /**
     * Constructor
     */
    public LineGraphicsDialog(boolean showWarning, boolean showROI, boolean showSVG, boolean showGraph, float init_value, boolean showTextSettings) {
        initComponents();
        jPanel1.setVisible(showGraph);
        jPanel2.setVisible(showROI);
        jPanel3.setVisible(showSVG);
        jPanel4.setVisible(showWarning);
        jPanel5.setVisible(!showWarning);
        jPanel6.setVisible(showTextSettings);
        if (init_value > 0) {
            jSpinner1.setValue(init_value);
            jSpinner2.setValue(init_value);
        }
        repaint();
        validate();
    }

    /**
     *
     * @return true if bold must be overriden for text (except for the letter)
     */
    public boolean isOverrideBold() {
        return jCheckBox5.isSelected();
    }

    /**
     *
     * @return true is bold must be overriden for letters
     */
    public boolean isOverrideBoldForLetter() {
        return jCheckBox7.isSelected();
    }

    /**
     *
     * @return true if italic must be overriden
     */
    public boolean isOverrideItalic() {
        return jCheckBox6.isSelected();
    }

    /**
     *
     * @return true if the stroke size of image associated ROIs should be
     * changed
     */
    public boolean isChangeStrokeSizeForImageROIs() {
        return jCheckBox3.isSelected();
    }

    /**
     *
     * @return true if the stroke size of SVGs should be changed
     */
    public boolean isChangeStrokeSizeForSVGs() {
        return jCheckBox4.isSelected();
    }

    /**
     *
     * @return true if the stroke size of graphs should be changed
     */
    public boolean isChangeStrokeSizeForGraphs() {
        return jCheckBox1.isSelected();
    }

    public boolean isStrokeIllustrator() {
        return jRadioButton1.isSelected();
    }

    /**
     *
     * @return true if point size of scatterplots should be changed
     */
    public boolean isChangePointSize() {
        return jCheckBox2.isSelected();
    }

    /**
     * @return the new stroke size that must be applied to all shapes in the
     * image
     */
    public float getNewStrokeSize() {
        return ((Float) jSpinner1.getValue());
    }

    /**
     * @return the new point size that must be applied to scatter plots
     */
    public float getNewPointSize() {
        return ((Float) jSpinner2.getValue());
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //LineGraphicsDialog iopane = new LineGraphicsDialog(true, true, true, true, TOP_ALIGNMENT, true);
        LineGraphicsDialog iopane = new LineGraphicsDialog(true, false, true, false, 6, false);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "LineGraphicsDialog", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.X_AXIS));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Text Parameters"));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jCheckBox5.setText("Override bolding");
        jPanel6.add(jCheckBox5);

        jCheckBox6.setText("Override italic");
        jPanel6.add(jCheckBox6);

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Override bolding (letters only Recommended)");
        jPanel6.add(jCheckBox7);

        jPanel7.add(jPanel6);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Stroking Parameters"));
        jPanel5.setLayout(new java.awt.GridLayout(2, 2));

        jLabel5.setText("Stroke Size:");
        jPanel5.add(jLabel5);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.1f), null, Float.valueOf(0.1f)));
        jPanel5.add(jSpinner1);

        jLabel6.setText("Point Size:");
        jPanel5.add(jLabel6);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.1f), null, Float.valueOf(0.1f)));
        jPanel5.add(jSpinner2);

        jPanel7.add(jPanel5);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Graphs (FiguR)"));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jCheckBox1.setText("Change Line Width");
        jPanel1.add(jCheckBox1);

        jCheckBox2.setText("Change Point Size");
        jPanel1.add(jCheckBox2);

        jLabel2.setText("<html><font color=\"#FF0000\">NB: Changing line stroke or point size is irreversible</font></html>");
        jPanel1.add(jLabel2);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("ROIs associated to images"));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jCheckBox3.setText("Change ROI Stroke size");
        jPanel2.add(jCheckBox3);

        jLabel3.setText("<html><font color=\"#FF0000\">NB: Changing ROI stroke size is irreversible</font></html>");
        jPanel2.add(jLabel3);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("SVG images"));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jCheckBox4.setText("Change SVG Stroke Size");
        jPanel3.add(jCheckBox4);

        jLabel4.setText("<html><font color=\"#FF0000\">NB: Changing SVG stroke size is irreversible and will permanently affect <BR> the embedded svg file. So please really think twice before changing svg stroke.</font></html>");
        jPanel3.add(jLabel4);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("<html>SVG output will be read/edited in Illustrator<br>(or be exported as a raster)");
        jPanel3.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("SVG output will be read/edited in Inkscape");
        jPanel3.add(jRadioButton2);

        jLabel7.setText("<html><font color=\"#FF0000\">NB: To make my life simpler Inkscape and Illustrator handle stroke size<BR>differently so please precise the software you might be using to open<BR>the SVG filegenerated by SF, if you want to export as raster please use<BR>the Illustrator settings.</font></html>");
        jPanel3.add(jLabel7);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Warning"));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("<html><font color=\"#FF0000\">This will automatically change the font of all the images in the current table<BR>Do you really want to continue ?</font></html>");
        jPanel4.add(jLabel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public static javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    // End of variables declaration//GEN-END:variables
}



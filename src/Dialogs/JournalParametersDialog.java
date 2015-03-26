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

import Checks.ListOfAdvancedTextFormattingRules;
import Checks.SFTextController;
import MyShapes.GraphFont;
import MyShapes.JournalParameters;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 * JournalParametersDialog is a dialog where the user can enter/modify font and
 * graph settings for a journal
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class JournalParametersDialog extends javax.swing.JPanel {

    String path;
    ArrayList<SFTextController> regexRules = new ArrayList<SFTextController>();

    /**
     * Loads parameters from a journal style in the dialog
     *
     * @param jp the journal style
     */
    public JournalParametersDialog(JournalParameters jp) {
        this();
        /*
         * here we load all the parameters
         */
        setJournalName(jp.getName());
        if (!(jp.getLetterFont().equals(jp.getUpperRightTextFont()) && jp.getUpperLeftTextFont().equals(jp.getUpperRightTextFont()) && jp.getScaleBarTextFont().equals(jp.getUpperRightTextFont()) && jp.getLowerLeftTextFont().equals(jp.getUpperRightTextFont()))) {
            jCheckBox1.setSelected(false);
            changeSelection(null);
        } else {
            jCheckBox1.setSelected(true);
            changeSelection(null);
            setSelectedFont(commonFont, jp.getLetterFont());
        }
        /*
         * we reload font settings
         */
        setSelectedFont(letter, jp.getLetterFont());
        setSelectedFont(upperLeft, jp.getUpperLeftTextFont());
        setSelectedFont(lowerLeft, jp.getLowerLeftTextFont());
        setSelectedFont(upperRight, jp.getUpperRightTextFont());
        setSelectedFont(lowerRight, jp.getLowerRightTextFont());
        setSelectedFont(scalebarFont, jp.getScaleBarTextFont());
        setSelectedFont(outterFont, jp.getOutterTextFont());
        /*
         * we reload page size settings
         */
        setSpinnerValue(jSpinner1, jp.getTwoColumnSizee());
        setSpinnerValue(jSpinner2, jp.getPageHeight());
        setSpinnerValue(jSpinner3, jp.getColumnSize());
        setSpinnerValue(jSpinner4, jp.getOneAndHalfColumn());

        /**
         * reload output DPI
         */
        setSpinnerValue(jSpinner6, jp.getColorDPI());
        setSpinnerValue(jSpinner7, jp.getBWDPI());

        rFontsParameters21.setGF(jp.getGf());
        regexRules = jp.getAdvancedtextFormattingRules();
        /*
         * we reload the letter capitalization parameters 
         */
        jComboBox1.setSelectedItem(jp.getCapitalisationOfLetter());
        if (jComboBox1.getSelectedIndex() == -1) {
            jComboBox1.setSelectedIndex(0);
        }
    }

    /**
     * Empty constructor
     */
    public JournalParametersDialog() {
        initComponents();
    }

    /**
     *
     * @return the graphFont contained in the journalParameters
     */
    private GraphFont getGF() {
        return rFontsParameters21.getGF();
    }

    /**
     * if true the journal parameters are hidden, so that just the graph
     * parameters remain
     *
     * @param bool
     */
    public void hideJournalParameters(boolean bool) {
        jPanel1.setVisible(!bool);
    }

    private void setSpinnerValue(JSpinner spin, double value) {
        spin.setValue(value);
    }
    
    private void setSpinnerValue(JSpinner spin, int value) {
        spin.setValue(value);
    }

    private void setSelectedFont(FontSelectorLight fsl, Font ft) {
        fsl.setSelectedFont(ft);
    }

    private void setJournalName(String name) {
        jTextField1.setText(name);
    }

    private String getJournalName() {
        return jTextField1.getText();
    }

    private Font getLetterFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return letter.getSelectedFont();
    }

    private Font getUpperRightFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return upperRight.getSelectedFont();
    }

    private Font getLowerRightFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return lowerLeft.getSelectedFont();
    }

    private Font getUpperLeftFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return upperLeft.getSelectedFont();
    }

    private Font getLowerLeftFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return lowerRight.getSelectedFont();
    }

    private Font getOutterFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return outterFont.getSelectedFont();
    }

    private Font getScaleBarFont() {
        if (commonFont.isEnabled()) {
            return getCommonFont();
        }
        return scalebarFont.getSelectedFont();
    }

    private Font getCommonFont() {
        return commonFont.getSelectedFont();
    }

    private double getDoubleColumnSize() {
        return ((Double) jSpinner1.getValue());
    }

    private double getSingleColumnSize() {
        return ((Double) jSpinner3.getValue());
    }

    private double getOneAndHalfColumnSize() {
        return ((Double) jSpinner4.getValue());
    }

    private double getPageHeight() {
        return ((Double) jSpinner2.getValue());
    }

    private float getStrokeSize() {
        return ((Float) jSpinner5.getValue());
    }

    public String getLetterCapitalizationBehaviour() {
        return jComboBox1.getSelectedItem().toString();
    }

    private int getColorDPI() {
        return ((Integer) jSpinner6.getValue());
    }

    private int getBWDPI() {
        return ((Integer) jSpinner7.getValue());
    }

    /**
     *
     * @return a new journal style that contains user settings
     */
    public JournalParameters getJournalStyle() {
        JournalParameters js = new JournalParameters(getJournalName(), getLetterFont(), getLowerLeftFont(), getUpperLeftFont(), getLowerRightFont(), getUpperRightFont(), getScaleBarFont(), getOutterFont(), getSingleColumnSize(), getOneAndHalfColumnSize(), getDoubleColumnSize(), getPageHeight(), getGF(), getStrokeSize(), getLetterCapitalizationBehaviour(), regexRules, getColorDPI(), getBWDPI());
        if (path != null) {
            js.setPath(path);
        }
        return js;
    }

    /**
     * This function save the current journal style to the journal style folder
     * <br><br>nb: this function is going to generate a new jounral style with a
     * random unique name
     *
     * @param journalStyleFolder
     */
    public void saveStyle(String journalStyleFolder) {
        JournalParameters js = getJournalStyle();
        String name = js.toXML(journalStyleFolder, null);
        this.path = name;
    }

    /**
     * This function writes the journal style to the journal style
     * folder<br><br>nb: this function can overwrite an existing style
     *
     * @param journalStyleFolder
     * @param filename
     */
    public void overWriteStyle(String journalStyleFolder, String filename) {
        JournalParameters js = getJournalStyle();
        js.setPath(filename);
        js.toXML(journalStyleFolder, filename);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        commonFont = new Dialogs.FontSelectorLight();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        letter = new Dialogs.FontSelectorLight();
        jLabel16 = new javax.swing.JLabel();
        upperLeft = new Dialogs.FontSelectorLight();
        jLabel3 = new javax.swing.JLabel();
        lowerRight = new Dialogs.FontSelectorLight();
        jLabel4 = new javax.swing.JLabel();
        upperRight = new Dialogs.FontSelectorLight();
        jLabel5 = new javax.swing.JLabel();
        lowerLeft = new Dialogs.FontSelectorLight();
        jLabel6 = new javax.swing.JLabel();
        scalebarFont = new Dialogs.FontSelectorLight();
        jLabel12 = new javax.swing.JLabel();
        outterFont = new Dialogs.FontSelectorLight();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        rFontsParameters21 = new Dialogs.RFontsParameters();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Journal Parameters"));

        jLabel11.setText("Journal Name");

        jLabel1.setText("Common Font");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Use Same Font for all the text");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSelection(evt);
            }
        });

        jLabel2.setText("Letter Font");
        jLabel2.setEnabled(false);

        letter.setEnabled(false);

        jLabel16.setText("Upper Left Text Font");
        jLabel16.setEnabled(false);

        upperLeft.setEnabled(false);

        jLabel3.setText("Lower Right Text Font");
        jLabel3.setEnabled(false);

        lowerRight.setEnabled(false);

        jLabel4.setText("Upper Right Text Font");
        jLabel4.setEnabled(false);

        upperRight.setEnabled(false);

        jLabel5.setText("Lower Left Text Font");
        jLabel5.setEnabled(false);

        lowerLeft.setEnabled(false);

        jLabel6.setText("Scale Bar Text Font");
        jLabel6.setEnabled(false);

        scalebarFont.setEnabled(false);

        jLabel12.setText("Outter Text Font");
        jLabel12.setEnabled(false);

        outterFont.setEnabled(false);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(21.0d), Double.valueOf(0.1d), null, Double.valueOf(0.01d)));

        jLabel7.setText("Full (2 Columns) Page Width (in cm)");

        jLabel8.setText("Full Page Height (in cm)");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(29.7d), Double.valueOf(0.1d), null, Double.valueOf(0.01d)));

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(10.0d), Double.valueOf(0.1d), null, Double.valueOf(0.01d)));

        jLabel9.setText("Column Width (in cm)");

        jLabel10.setText("1.5 Column Width (in cm)");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(16.0d), Double.valueOf(0.1d), null, Double.valueOf(0.01d)));

        jLabel13.setText("Objects Stroke Size");

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.65f), Float.valueOf(-1.0f), null, Float.valueOf(0.01f)));

        jLabel14.setText("The letter should be:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kept as it is", "upper case (capitalized)", "lower case (not capitalized)" }));

        jLabel15.setText("Advanced Text Formatting Rules:");

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAdvancedFormattingRules(evt);
            }
        });

        jLabel17.setText("preferred output DPI for colored:");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(300), Integer.valueOf(72), null, Integer.valueOf(1)));

        jLabel18.setText("or B&W images:");

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(600), Integer.valueOf(72), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(scalebarFont, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(upperRight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(upperLeft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(letter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lowerLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(outterFont, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lowerRight, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(commonFont, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {letter, lowerLeft, lowerRight, outterFont, scalebarFont, upperLeft, upperRight});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(commonFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(letter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(upperLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(lowerRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(upperRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(lowerLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(scalebarFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(outterFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jButton1)
                    .addComponent(jLabel17)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rFontsParameters21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rFontsParameters21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void changeSelection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSelection
        boolean show = jCheckBox1.isSelected();
        jLabel2.setEnabled(!show);
        jLabel3.setEnabled(!show);
        jLabel4.setEnabled(!show);
        jLabel5.setEnabled(!show);
        jLabel6.setEnabled(!show);
        jLabel12.setEnabled(!show);
        upperLeft.setEnabled(!show);
        lowerLeft.setEnabled(!show);
        upperRight.setEnabled(!show);
        lowerRight.setEnabled(!show);
        scalebarFont.setEnabled(!show);
        outterFont.setEnabled(!show);
        letter.setEnabled(!show);
        jLabel16.setEnabled(!show);
        commonFont.setEnabled(show);
        jLabel1.setEnabled(show);
    }//GEN-LAST:event_changeSelection

    private void editAdvancedFormattingRules(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAdvancedFormattingRules
        ListOfAdvancedTextFormattingRules iopane = new ListOfAdvancedTextFormattingRules(regexRules, false);
        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Advanced Text Rules", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            this.regexRules = iopane.getRules();
        }
    }//GEN-LAST:event_editAdvancedFormattingRules

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String[] args) {
        JournalParametersDialog iopane = new JournalParametersDialog();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "test", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getJournalStyle());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private Dialogs.FontSelectorLight commonFont;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JTextField jTextField1;
    private Dialogs.FontSelectorLight letter;
    private Dialogs.FontSelectorLight lowerLeft;
    private Dialogs.FontSelectorLight lowerRight;
    private Dialogs.FontSelectorLight outterFont;
    private Dialogs.RFontsParameters rFontsParameters21;
    private Dialogs.FontSelectorLight scalebarFont;
    private Dialogs.FontSelectorLight upperLeft;
    private Dialogs.FontSelectorLight upperRight;
    // End of variables declaration//GEN-END:variables
}



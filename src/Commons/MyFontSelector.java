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
package Commons;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalTextFieldUI;

/**
 *
 * @author Benoit Aigouy
 */
public class MyFontSelector extends javax.swing.JPanel {

    Font font = new Font("Arial", Font.PLAIN, 12);
    int color = 0xFFFFFF;

    public MyFontSelector(boolean ColorButton) {
        this();
        paintedButton1.setVisible(ColorButton);
        setFontMe(font);
    }

    public MyFontSelector(Font font) {
        this();
        this.font = font;
        setFontMe(font);
        update();
    }

    public MyFontSelector(String font_name, int size) {
        this();
        jComboBox1.setSelectedItem(font_name);
        jSpinner1.setValue(size);
        update();
    }

    public void setTypeVisible(boolean bool) {
        jLabel4.setVisible(bool);
        jComboBox2.setVisible(bool);
    }

    /**
     * Creates new form MyFontSelector
     */
    public MyFontSelector() {
        initComponents();
        jTextField1.setUI(new MetalTextFieldUI());
        jTextField1.setBackground(Color.WHITE);
        jTextField1.setForeground(Color.BLACK);
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] avialable_fonts = g.getAvailableFontFamilyNames();
        jComboBox1.setModel(new DefaultComboBoxModel(avialable_fonts));
        jComboBox2.setModel(new DefaultComboBoxModel(new String[]{"Plain", "Italic", "Bold", "Bold+Italic"}));
        jComboBox1.setSelectedItem("Arial");
        fontChanged(null);
        update();
    }

    public final void update() {
        paintedButton1ActionPerformed(null);
        fontChanged(null);
    }

    public final void setFontMe(Font font) {
        if (font == null) {
            return;
        }
        this.font = font;
        /*
         * the font name has a bug when it is bold --> gives mistakes
         */
        String fontname = font.getFamily();
        if (fontname == null) {
            return;
        }
        jComboBox1.setSelectedItem(fontname);
        jSpinner1.setValue(font.getSize());
        if (font.isBold() && font.isItalic()) {
            jComboBox2.setSelectedItem("Bold+Italic");
        } else if (font.isBold()) {
            jComboBox2.setSelectedItem("Bold");
        } else if (font.isItalic()) {
            jComboBox2.setSelectedItem("Italic");
        } else {
            jComboBox2.setSelectedItem("Plain");
        }
        update();
    }

    public void setSize(int val) {
        jSpinner1.setValue(val);
    }

    public void setFontName(String font_name) {
        jComboBox1.setSelectedItem(font_name);
    }

    public void setColor(Color color) {
        this.color = color.getRGB();
        update();
    }

    public int getColor() {
        return paintedButton1.getColor();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        paintedButton1 = new Commons.PaintedButton();

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Your Text Here 0123456789");

        jLabel3.setText("Preview (X2):");

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Font Name:");
        jPanel2.add(jLabel1);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontChanged(evt);
            }
        });
        jPanel2.add(jComboBox1);

        jLabel2.setText("Size:");
        jPanel2.add(jLabel2);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(8), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizeChanged(evt);
            }
        });
        jPanel2.add(jSpinner1);

        jLabel4.setText("Type:");
        jPanel2.add(jLabel4);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeCHanged(evt);
            }
        });
        jPanel2.add(jComboBox2);

        paintedButton1.setBackground(new java.awt.Color(255, 255, 255));
        paintedButton1.setForeground(new java.awt.Color(0, 0, 0));
        paintedButton1.setText("Color");
        paintedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintedButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(paintedButton1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public int getFontType() {
        if (jComboBox2.getSelectedItem().toString().equalsIgnoreCase("bold")) {
            return Font.BOLD;
        } else if (jComboBox2.getSelectedItem().toString().equalsIgnoreCase("italic")) {
            return Font.ITALIC;
        } else if (jComboBox2.getSelectedItem().toString().contains("+")) {
            return Font.BOLD | Font.ITALIC;
        } else {
            return Font.PLAIN;
        }
    }

    public String getFontName() {
        return jComboBox1.getSelectedItem().toString();
    }

    public int getFontSize() {
        return ((Integer) jSpinner1.getValue());
    }

    public Font getTextFont() {
        return font;
    }

    private void sizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizeChanged
        fontChanged(null);
    }//GEN-LAST:event_sizeChanged

    private void typeCHanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeCHanged
        fontChanged(null);
    }//GEN-LAST:event_typeCHanged

    private void paintedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintedButton1ActionPerformed
        if (evt != null) {
            Color userSelectedColor = JColorChooser.showDialog(this, "Pick a Color", paintedButton1.getBackground());
            if (userSelectedColor != null) {
                paintedButton1.setColor(userSelectedColor);
                this.color = userSelectedColor.getRGB() & 0x00FFFFFF;
                jTextField1.setBackground(new Color(CommonClassesLight.negative_color(this.color)));
                jTextField1.setForeground(userSelectedColor);
            }
        } else {
            color = color & 0x00FFFFFF;
            paintedButton1.setColor(color);
            jTextField1.setBackground(new Color((CommonClassesLight.negative_color(color) & 0x00FFFFFF)));
            jTextField1.setForeground(new Color(color));
        }
    }//GEN-LAST:event_paintedButton1ActionPerformed

    private void fontChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontChanged
        font = new Font(getFontName(), getFontType(), getFontSize());
        Font fontX2 = new Font(getFontName(), getFontType(), getFontSize() * 2);
        jTextField1.setFont(fontX2);
    }//GEN-LAST:event_fontChanged

    public static void main(String[] arg) {
        MyFontSelector palPane = new MyFontSelector(false);
        int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{palPane}, "test font sel", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private Commons.PaintedButton paintedButton1;
    // End of variables declaration//GEN-END:variables
}



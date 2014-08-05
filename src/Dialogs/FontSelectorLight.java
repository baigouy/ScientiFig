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

import Commons.MyFontSelector;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;

/**
 * This class let's the user select a font
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class FontSelectorLight extends javax.swing.JPanel {
    
    MyFontSelector iopane = new MyFontSelector(false);

    /**
     * Creates a new form
     */
    public FontSelectorLight() {
        initComponents();
        /*
         * we set the default font to Arial 12 (probably the most common font used in the sceintific press)
         */
        Font ft = new Font("Arial", Font.PLAIN, 12);
        iopane.setFontMe(ft);
        applyFont(ft);
    }
    
    public void setTypeVisible(boolean bool) {
        iopane.setTypeVisible(bool);
    }
    
    @Override
    public boolean isEnabled() {
        return jButton1.isEnabled();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        jButton1.setEnabled(enabled);
        jTextField1.setEnabled(enabled);
    }

    /**
     *
     * @return the user selected font
     */
    public Font getSelectedFont() {
        return iopane.getTextFont();
    }

    /**
     * Sets the default font
     *
     * @param font
     */
    public void setSelectedFont(Font font) {
        iopane.setFontMe(font);
        iopane.setColor(Color.BLACK);
        applyFont(font);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jTextField1.setEditable(false);

        jButton1.setText("Sel Font");
        jButton1.setToolTipText("Sel Font");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField1)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void overridableActionListener(Object source) {
        int result = JOptionPane.showOptionDialog(this.getRootPane(), new Object[]{iopane}, "Select a font", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            Font ft = iopane.getTextFont();
            applyFont(ft);
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*
         * this.getRootPane() is a bug fix to get the window to be centered over the UI
         */
        overridableActionListener(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Sets the default font
     *
     * @param ft font that should be used
     */
    private void applyFont(Font ft) {
        if (ft != null) {
            jTextField1.setFont(ft);
            String style;
            if (ft.getStyle() == Font.PLAIN) {
                style = "Plain";
            } else if (ft.getStyle() == Font.ITALIC) {
                style = "Italic";
            } else if (ft.getStyle() == Font.BOLD) {
                style = "Bold";
            } else {
                style = "Bold+Italic";
            }
            jTextField1.setText(ft.getFamily() + " " + ft.getSize() + " " + style);
        }
    }

    /**
     * Use it to test the function
     */
    public static void main(String[] args) {
        FontSelectorLight iopane = new FontSelectorLight();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Select a font", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(iopane.getSelectedFont());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}



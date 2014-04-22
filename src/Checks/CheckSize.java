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

import Commons.MyBufferedImage;
import MyShapes.GraphFont;
import MyShapes.JournalParameters;
import Tools.Converter;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Benoit Aigouy
 */
public class CheckSize extends javax.swing.JPanel {

    public boolean noError = false;
    JournalParameters jp;

    /**
     * Creates new form CheckSize
     */
    public CheckSize(BufferedImage preview, JournalParameters jp, double sizeInPx) {
        initComponents();
        double sizeInCm = Converter.PxToCm72dpi(sizeInPx);
        this.jp = jp;
        double minDiff = Math.min(Math.abs(jp.getColumnSize() - sizeInCm), Math.min(Math.abs(jp.getOneAndHalfColumn() - sizeInCm), Math.abs(jp.getTwoColumnSizee() - sizeInCm)));
        boolean foundAmatch = minDiff < 0.05;
        /**
         * if the size/width of the figure is correct return
         */
        if (foundAmatch) {
            noError = true;
            return;
        }
        /**
         * otherwise determine the closest size and suggest resize
         */
        double suggestedSize = jp.getColumnSize();
        jComboBox1.setSelectedIndex(0);
        if (minDiff != Math.abs(sizeInCm - suggestedSize)) {
            suggestedSize = jp.getOneAndHalfColumn();
            jComboBox1.setSelectedIndex(1);
            if (minDiff != Math.abs(sizeInCm - suggestedSize)) {
                jComboBox1.setSelectedIndex(2);
            }
        }
        if (preview != null) {
            double AR1 = 140. / preview.getHeight();
            double AR2 = 240. / preview.getWidth();
            BufferedImage tmp = new MyBufferedImage(240, 140, BufferedImage.TYPE_INT_ARGB);
            int width = tmp.getWidth();
            int height = tmp.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    tmp.setRGB(i, j, 0x00000000);
                }
            }
            Graphics2D g2d = tmp.createGraphics();
            AffineTransform at = new AffineTransform();
            double scale = Math.min(AR2, AR1);
            at.scale(scale, scale);
            g2d.drawImage(preview, at, null);
            g2d.dispose();
            imagePaneLight1.setImage(tmp);
            imagePaneLight1.repaint();
        }
        jEditorPane1.setContentType("text/html");
        DecimalFormat df = new DecimalFormat("#.##");
        jEditorPane1.setText("Your figure is " + df.format(sizeInCm) + "cm.<br>'" + jp.getName() + "' suggests the following sizes:<br><br>1 column: " + jp.getColumnSize() + " cm<br>1.5 columns: " + jp.getOneAndHalfColumn() + " cm<br>2 columns: " + jp.getTwoColumnSizee() + " cm");
    }

    public double getFinalSize() {
        return ((Double) jSpinner1.getValue());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

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

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Your image size is not optimal:");

        jLabel2.setText("please select the number of columns the figure should be:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 col", "1,5 cols", "2 cols" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionChanged(evt);
            }
        });

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.01d), Double.valueOf(0.01d), null, Double.valueOf(0.05d)));

        jLabel3.setText("or please specify its size in cm:");

        jEditorPane1.setEditable(false);
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSpinner1)
                            .addComponent(jScrollPane1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionChanged
        int idx = jComboBox1.getSelectedIndex();
        if (idx == 0) {
            jSpinner1.setValue(jp.getColumnSize());
        } else if (idx == 1) {
            jSpinner1.setValue(jp.getOneAndHalfColumn());
        } else {
            jSpinner1.setValue(jp.getTwoColumnSizee());
        }
    }//GEN-LAST:event_selectionChanged

    public static void main(String[] args) {
        BufferedImage img = new BufferedImage(512, 1024, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getWidth(); i++) {
            img.setRGB(i, i, 0xFFFFFF);
        }
        JournalParameters jp = new JournalParameters("test", new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), new Font("Arial", Font.BOLD, 16), 10., 16., 21., 29.7, new GraphFont(), 1, null, new ArrayList<SFTextController>(), 300, 600);
        CheckSize iopane = new CheckSize(img, jp, 12);
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Check Fonts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                //--> resize the image
                double size = iopane.getFinalSize();
                System.out.println("size --> " + size);
            }
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}


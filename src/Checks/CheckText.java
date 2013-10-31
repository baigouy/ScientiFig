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

import Commons.CommonClassesLight;
import Commons.MyBufferedImage;
import Dialogs.ColoredTextPane;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.StyledDoc2Html;
import Tools.StyledDocTools;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Benoit Aigouy
 */
public class CheckText extends javax.swing.JPanel {

    public boolean noError = true;
    ArrayList<ColoredTextPane> correctedTexts = new ArrayList<ColoredTextPane>();
    int count = 1;

    /**
     * Creates new form CheckText
     */
    public CheckText(BufferedImage img, StyledDocument doc, SFTextController control) {
        initComponents();
        CommonClassesLight.speedUpJScrollpane(jScrollPane1);
        String[] controls = control.getControls();
        for (String string : controls) {
            Object out = SFTextController.executeCommand(doc, string);
            if (out instanceof Boolean) {
                if ((Boolean) out) {
                    noError = false;
                    break;
                }
            }
        }
        if (!noError) {
            ColoredTextPane ctp = new ColoredTextPane(new ColoredTextPaneSerializable(StyledDocTools.cloneDoc(doc), null));
            ctp.setTitle("The following text generated a warning");
            ctp.showFont(false);
            jPanel2.add(add(ctp));
            String[] warning_messages = control.getMessages();
            String concat = "";
            for (String string : warning_messages) {
                concat += string + "\n";
            }
            jEditorPane1.setText(concat);
            String[] solutions = control.getSolutions();
            for (String string : solutions) {
                StyledDocument clone = StyledDocTools.cloneDoc(doc);
                Object sol = SFTextController.executeCommand(clone, string);
                if (sol instanceof StyledDocument) {
                    /*
                     * texts are equal do a deep check to see if styles also match if so ignore
                     */
                    if (StyledDocTools.deepEquals((StyledDocument) sol, doc)) {
                        continue;
                    }
                    ColoredTextPane correction = new ColoredTextPane(new ColoredTextPaneSerializable((StyledDocument) sol, null));
                    correction.setTitle("suggested solution #" + CommonClassesLight.create_number_of_the_appropriate_size(count, 3));
                    correction.showFont(false);
                    correctedTexts.add(correction);
                    count++;
                }
            }
        }
        if (count == 1 || noError) {
            noError = true;
            return;
        }
        if (img == null) {
            jPanel4.setVisible(false);
        } else {
            double AR1 = 170. / img.getHeight();
            double AR2 = 240. / img.getWidth();
            BufferedImage tmp = new MyBufferedImage(240, 170, BufferedImage.TYPE_INT_ARGB);
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
            g2d.drawImage(img, at, null);
            g2d.dispose();
            imagePaneLight1.setImage(tmp);
            imagePaneLight1.repaint();
        }
        for (ColoredTextPane correction : correctedTexts) {
            jPanel3.add(correction);
        }
        jPanel3.validate();
        jScrollPane1.validate();
    }

    public int getCount() {
        return count;
    }

    public StyledDocument getDoc(int pos) {
        return correctedTexts.get(pos).getDoc();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        BufferedImage img = new BufferedImage(512, 1024, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getWidth(); i++) {
            img.setRGB(i, i, 0xFFFFFF);
        }
        for (SFTextController test : SFTextController.list) {
            StyledDocument doc = new StyledDoc2Html().reparse(test.getTestStrings()[0]);
            CheckText iopane = new CheckText(img, doc, test);
            if (iopane.noError) {
                System.out.println(test.toString() + " " + test.getTestStrings()[0]);
            }
            if (!iopane.noError) {
                int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "VisualCheckWithMultiSolutions", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                if (result == JOptionPane.OK_OPTION) {
                    if (iopane.getCount() > 2) {
                        ChooseASolution iopane2 = new ChooseASolution(iopane.getCount());
                        result = JOptionPane.showOptionDialog(null, new Object[]{iopane2}, "Choose a solution", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            int pos = iopane2.getPos();
                            StyledDocument finalDoc = iopane.getDoc(pos);
                            System.out.println(new StyledDoc2Html().convertStyledDocToHtml(doc));
                            System.out.println(new StyledDoc2Html().convertStyledDocToHtml(finalDoc));
                        }
                    } else {
                        StyledDocument finalDoc = iopane.getDoc(0);
                        System.out.println(new StyledDoc2Html().convertStyledDocToHtml(finalDoc));
                    }
                }
            }
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Suggested solution(s)"));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel3);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview Of The Corresponding Image"));

        imagePaneLight1.setMaximumSize(new java.awt.Dimension(216, 216));
        imagePaneLight1.setMinimumSize(new java.awt.Dimension(216, 216));
        imagePaneLight1.setPreferredSize(new java.awt.Dimension(216, 216));

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePaneLight1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePaneLight1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel4);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Journal Warning Message"));

        jEditorPane1.setContentType("text/html"); // NOI18N
        jScrollPane3.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}


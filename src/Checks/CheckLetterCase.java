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
import Dialogs.ColoredTextPane;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.StyledDoc2Html;
import Tools.StyledDocTools;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Benoit Aigouy
 */
public class CheckLetterCase extends javax.swing.JPanel {

    public boolean noError = false;
    public ColoredTextPaneSerializable out;

    /**
     * Creates new form CheckFont
     */
    public CheckLetterCase(BufferedImage img, ColoredTextPaneSerializable original, String caseOfTheFirstLetter) {
        initComponents();

        if (caseOfTheFirstLetter == null || caseOfTheFirstLetter.toLowerCase().contains("as it")) {
            noError = true;
            return;
        }

        String caseAsString = caseOfTheFirstLetter.toLowerCase().contains("lower") ? "set to lower case" : caseOfTheFirstLetter.toLowerCase().contains("upper") ? "Capitalized" : null;
        if (caseAsString == null) {
            if (caseOfTheFirstLetter.toLowerCase().contains("as it")) {
                System.err.println("unknown case for the first letter --> ignoring");
                noError = true;
                return;
            }
        }

        if (StyledDocTools.getText(original.getDoc()).equals("")) {
            /*
             * No text no need to warn the user, let's just apply the font directly
             */
            noError = true;
            return;
        }
        out = new ColoredTextPaneSerializable(StyledDocTools.cloneDoc(original.getDoc()), "");
        out.treatWiselyFirstLetterCapitalization(caseOfTheFirstLetter);

        /**
         * a bit dirty and useles should check this better some day --> really
         * check that the letter is capitalized or not and wether it can be
         * capitalized, etc --> peut etre faire un getFirstLetter
         */
        if (StyledDocTools.deepEquals(original.getDoc(), out.getDoc())) {
            noError = true;
            return;
        }

        ColoredTextPane copy = new ColoredTextPane(out);
        copy.setTitle("This is the text with the correct case");

        jEditorPane1.setContentType("text/html");
        ColoredTextPane orig = new ColoredTextPane(new ColoredTextPaneSerializable(StyledDocTools.cloneDoc(original.getDoc()), ""));
        orig.setTitle("This is the text with the wrong case");
        jPanel4.add(orig);

        String initialText = "<html><font color=\"#FF0000\">The journal recommends the first letter should be " + caseAsString + "</font>";
        jEditorPane1.setText(initialText);
        if (img == null) {
            jPanel4.setVisible(false);
        } else {
            double AR1 = 140. / img.getHeight();
            double AR2 = 240. / img.getWidth();
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
            g2d.drawImage(img, at, null);
            g2d.dispose();
            imagePaneLight1.setImage(tmp);
            imagePaneLight1.repaint();
        }

        jPanel6.add(copy);
        jPanel4.validate();
        jPanel6.validate();
        this.out = copy.getColoredTextPaneSerializable();
    }

    public ColoredTextPaneSerializable getColoredTextPaneSerializable() {
        return out;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

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

        jEditorPane1.setEditable(false);
        jScrollPane1.setViewportView(jEditorPane1);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel4, jPanel6});

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        BufferedImage img = new BufferedImage(512, 1024, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getWidth(); i++) {
            img.setRGB(i, i, 0xFFFFFF);
        }
        StyledDocument doc = new StyledDoc2Html().reparse("<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ff0000\">Toto<i> Bobo</txtFgcolor><txtFgcolor color=\"#ffff33\"> </i>α</txtFgcolor></font>");
//        StyledDocument doc = new StyledDoc2Html().reparse("");        
        ColoredTextPaneSerializable ctp = new ColoredTextPaneSerializable(doc, "test");
        CheckLetterCase iopane = new CheckLetterCase(img, ctp, "lower");
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Check Fonts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                ctp.setDoc(iopane.getColoredTextPaneSerializable().getDoc());
                ctp.ft = iopane.getColoredTextPaneSerializable().ft;
                System.out.println(new StyledDoc2Html().convertStyledDocToHtml(ctp.doc) + " " + ctp.ft);
                //System.out.println(new StyledDoc2Html().convertStyledDocToHtml(finalDoc));
            }
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}


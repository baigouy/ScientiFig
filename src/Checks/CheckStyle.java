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
public class CheckStyle extends javax.swing.JPanel {

    public static final int ITALIC = StyledDocTools.ITALIC;
    public static final int BOLD = StyledDocTools.BOLD;
    public static final int SUBSCRIPT = StyledDocTools.SUBSCRIPT;
    public static final int SUPERSCRIPT = StyledDocTools.SUPERSCRIPT;
    public static final int UNDERLINED = StyledDocTools.UNDERLINED;
    public static final int COLORFUL = StyledDocTools.COLORFUL;
    public boolean noError = true;
    public StyledDocument styleCorrectedDoc;

    /**
     * Creates new form VisualCheckWithMultiSolutions
     */
    public CheckStyle(BufferedImage img, StyledDocument doc, int STYLE_TO_CHECK) {
        initComponents();
        String errorMesage = "";
        Object[] stats = StyledDocTools.getStyleStats(doc);
        switch (STYLE_TO_CHECK) {
            case ITALIC:
                if (StyledDocTools.isItalic(stats)) {
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text contains italics, we suggest you remove it if not necessary</font>";
                }
                break;
            case BOLD:
                if (StyledDocTools.isBold(stats) && StyledDocTools.getNbOfCHars(stats) > 1) { //ignore single capitalized letters
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text contains bold characters, we suggest you remove bold except if necessary</font>";
                }
                break;
            case UNDERLINED:
                if (StyledDocTools.isUnderlined(stats)) {
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text contains underlined characters, we suggest you remove underline</font>";
                }
                break;
            case SUPERSCRIPT:
                if (StyledDocTools.isOnlySuperscript(stats)) {
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text only consists of superscript characters, we suggest you remove superscript and use a smaller font instead</font>";
                }
                break;
            case SUBSCRIPT:
                if (StyledDocTools.isOnlySubscript(stats)) {
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text only consists of subscript characters, we suggest you remove superscript and use a smaller font instead</font>";
                }
                break;
            case COLORFUL:
                if (StyledDocTools.getNbOfColor(stats) > 1) {
                    noError = false;
                    errorMesage = "<html><font color=\"#FF0000\">Your text contains two or more colors, we suggest you use only one color except if necessary</font>";
                }
                break;
        }
        if (!noError) {
            ColoredTextPane ctp = new ColoredTextPane(new ColoredTextPaneSerializable(StyledDocTools.cloneDoc(doc), null));
            ctp.setTitle("The following text generated a warning");
            ctp.showFont(false);
            jPanel2.add(add(ctp));
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
            jEditorPane1.setText(errorMesage);
            styleCorrectedDoc = StyledDocTools.cloneDoc(doc);

            switch (STYLE_TO_CHECK) {
                case ITALIC:
                    styleCorrectedDoc = StyledDocTools.unitalicize(styleCorrectedDoc);
                    break;
                case BOLD:
                    styleCorrectedDoc = StyledDocTools.unbold(styleCorrectedDoc);
                    break;
                case UNDERLINED:
                    styleCorrectedDoc = StyledDocTools.ununderline(styleCorrectedDoc);
                    break;
                case SUPERSCRIPT:
                    styleCorrectedDoc = StyledDocTools.unsuperscript(styleCorrectedDoc);
                    break;
                case SUBSCRIPT:
                    styleCorrectedDoc = StyledDocTools.unsubscript(styleCorrectedDoc);
                    break;
                case COLORFUL:
                    styleCorrectedDoc = StyledDocTools.uncolorize(styleCorrectedDoc);
                    break;
            }

            if (styleCorrectedDoc == null) {
                noError = true;
                return;
            }
            ColoredTextPane correction = new ColoredTextPane(new ColoredTextPaneSerializable(styleCorrectedDoc, null));
            correction.setTitle("suggested solution:");
            correction.showFont(false);
            jPanel6.add(correction);
            jPanel6.validate();
        }
    }

    public StyledDocument getStyleCorrectedDoc() {
        return styleCorrectedDoc;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //SFTextController test = SFTextController.STARTS_WITH_WHITE_SPACES;
        //SFTextController test = SFTextController.CONTAINS_CONSECUTIVE_SPACES;
        //SFTextController test = SFTextController.ENDS_WITH_WHITE_SPACES;
        //SFTextController test = SFTextController.REPLACE_SLASHES_BY_AND_OR_HYPHEN_NO_SPACE;

        BufferedImage img = new BufferedImage(512, 1024, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getWidth(); i++) {
            img.setRGB(i, i, 0xFFFFFF);
//            for (int j = 0; j < test.getHeight(); j++) {
//                
//            }
        }
        //StyledDocument doc = new StyledDoc2Html().reparse("<font face=\"Monospaced\" size=\"24\"><txtFgcolor color=\"#ffffff\">     dswsdfsd <b><i></txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</b></i></txtFgcolor><txtFgcolor color=\"#ffffff\"> <i>sdfdsfsdf</i> <sup><i></txtFgcolor><txtFgcolor color=\"#66ff00\">sdfdsf</sup></i></txtFgcolor><txtFgcolor color=\"#ffffff\"> sdf <sub>sdf</sub> dsf</txtFgcolor><txtFgcolor color=\"#ffffff\">  dswsd fsd fsdf         sdfdsfsdf  sdfdsf   sdf sdf dsf              </txtFgcolor></font>");
        //System.out.println(test.getTestStrings()[0]);
        for (int i = 0; i <= 5; i++) {
            StyledDocument doc = new StyledDoc2Html().reparse(StyledDocTools.MULTICOLOR_ALL_STYLES);
            CheckStyle iopane = new CheckStyle(img, doc, i);
            if (!iopane.noError) {
                int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "CheckStyle", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
                if (result == JOptionPane.OK_OPTION) {
                    StyledDocument finalDoc = iopane.getStyleCorrectedDoc();
                    System.out.println(new StyledDoc2Html().convertStyledDocToHtml(finalDoc));
                }
            }
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel6 = new javax.swing.JPanel();

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
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel1);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}


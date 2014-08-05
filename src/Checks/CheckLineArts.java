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

import Commons.CommonClassesLight;
import Commons.Loader;
import Commons.MyBufferedImage;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.MyImage2D;
import MyShapes.MyImageVector;
import MyShapes.MyPlotVector;
import MyShapes.MyPoint2D;
import MyShapes.MyRectangle2D;
import MyShapes.StyledDoc2Html;
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
public class CheckLineArts extends javax.swing.JPanel {

    public boolean noError = false;
    Object modifiedStrokeData;

    /**
     * Creates new form ImageBeforeAfter
     */
    public CheckLineArts(Object orig, float recommendedStrokeSize, boolean isIllustrator) {
        if (orig instanceof MyPlotVector) {
            /*
             * Image is a graph --> better process it using the check graph tool
             */
            noError = true;
            return;
        }
        initComponents();
        jEditorPane1.setContentType("text/html");
        MyImage2D clone;
        if (((MyImage2D) orig).hasLineArts()) {
            /**
             * check for stroke size
             */
            boolean isStrokeSizeOk = ((MyImage2D) orig).checkStrokeSize(recommendedStrokeSize, isIllustrator);
            if (isStrokeSizeOk) {
                noError = true;
                return;
            }
            /**
             * The stroke size of at least one of your shapes does not match the
             * journal guideline --> offer replacement
             */
            if (orig instanceof MyImageVector) {
                clone = new MyImageVector.Double(((MyImageVector) orig));
                ((MyImageVector) clone).setSvg_content_serializable(((MyImageVector) orig).doc2String(false));
                ((MyImageVector) clone).recreateStyledDoc();
                ((MyImageVector) clone).setStrokeSize(recommendedStrokeSize, true, isIllustrator);
                modifiedStrokeData = ((MyImageVector) clone).doc2String(false);
            } else {
                clone = new MyImage2D.Double(((MyImage2D.Double) orig));
                clone.setAssociatedShapeStrokeSize(recommendedStrokeSize);
                modifiedStrokeData = clone.getAssociatedObjects();
            }
            if (true) {
                BufferedImage preview = ((MyImage2D) orig).getFormattedImageWithoutTranslation(true);
                double AR1 = 512. / preview.getHeight();
                double AR2 = 369. / preview.getWidth();
                BufferedImage tmp = new MyBufferedImage(512, 369, BufferedImage.TYPE_INT_ARGB);
                int width = tmp.getWidth();
                int height = tmp.getHeight();
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        tmp.setRGB(i, j, 0x00000000);
                    }
                }
                Graphics2D g2d = tmp.createGraphics();
                CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                AffineTransform at = new AffineTransform();
                double scale = Math.min(AR2, AR1);
                at.scale(scale, scale);
                g2d.drawImage(preview, at, null);
                g2d.dispose();
                imagePaneLight1.setImage(tmp);
                imagePaneLight1.repaint();
            }
            if (true) {
                BufferedImage preview = clone.getFormattedImageWithoutTranslation(true);
                double AR1 = 512. / preview.getHeight();
                double AR2 = 369. / preview.getWidth();
                BufferedImage tmp = new MyBufferedImage(512, 369, BufferedImage.TYPE_INT_ARGB);
                int width = tmp.getWidth();
                int height = tmp.getHeight();
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        tmp.setRGB(i, j, 0x00000000);
                    }
                }
                Graphics2D g2d = tmp.createGraphics();
                CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                AffineTransform at = new AffineTransform();
                double scale = Math.min(AR2, AR1);
                at.scale(scale, scale);
                g2d.drawImage(preview, at, null);
                g2d.dispose();
                imagePaneLight2.setImage(tmp);
                imagePaneLight2.repaint();
            }
            DecimalFormat df = new DecimalFormat("#.##");
            jEditorPane1.setText("<font color=\"#FF0000\">The journal recommends a stroke width of " + df.format(recommendedStrokeSize) + " for line arts.<br> We have detected that at least one of your line arts has a stroke size that significantly differs from this value.<br>Right panel is our solution to this problem (left panel is your original image).</font>");
        } else if (!((MyImage2D) orig).hasLineArts()) {
            /*
             * no line arts --> lets not bother the user
             */
            noError = true;
        }
    }

    public Object getModifiedStrokeData() {
        return modifiedStrokeData;
    }

    public static void main(String[] args) {
        //MyImageVector.Double img = new MyImageVector.Double(new Loader().loadSVGDocument("D:\\sample_images_PA\\trash_test_mem\\images_de_test_pr_figure_assistant\\graph_enhanced.svg"));
        MyImageVector.Double img = new MyImageVector.Double(new Loader().loadSVGDocument("D:\\sample_images_PA\\trash_test_mem\\images_de_test_pr_figure_assistant\\test_scaling_SVG.svg"));
        //MyImage2D img = new MyImage2D.Double(0, 0, "D:\\sample_images_PA\\trash_test_mem\\mini\\focused_Series010.png");
        img.addAssociatedObject(new MyRectangle2D.Double(10, 10, 128, 256));
        img.addAssociatedObject(new MyRectangle2D.Double(200, 20, 256, 256));
        img.addAssociatedObject(new MyRectangle2D.Double(100, 10, 256, 128));
        MyPoint2D.Double text = new MyPoint2D.Double(220, 220);
        text.setText(new ColoredTextPaneSerializable(new StyledDoc2Html().reparse("<i>test<i>"), ""));
        img.addAssociatedObject(text);
        CheckLineArts iopane = new CheckLineArts(img, 1.5f, true);
        if (!iopane.noError) {
            int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Check stroke width of line arts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Accept automated solution", "Ignore"}, null);
            if (result == JOptionPane.OK_OPTION) {
                //--> replace objects with copy
                Object strokeData = iopane.getModifiedStrokeData();
                if (strokeData instanceof ArrayList) {
                    img.setAssociatedObjects(((ArrayList<Object>) strokeData));
//                    for (Object string : img.getAssociatedObjects()) {
//                        if (string instanceof PARoi) {
//                            System.out.println(((PARoi) string).getStrokeSize());
//                        }
//                    }
                } else if (strokeData instanceof String) {
                    ((MyImageVector) img).setSvg_content_serializable((String) strokeData);
                    ((MyImageVector) img).reloadDocFromString();
                }
            }
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePaneLight1 = new Dialogs.ImagePaneLight();
        imagePaneLight2 = new Dialogs.ImagePaneLight();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout imagePaneLight2Layout = new javax.swing.GroupLayout(imagePaneLight2);
        imagePaneLight2.setLayout(imagePaneLight2Layout);
        imagePaneLight2Layout.setHorizontalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );
        imagePaneLight2Layout.setVerticalGroup(
            imagePaneLight2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jEditorPane1);

        jLabel1.setText("Original");

        jLabel2.setText("Altered Stroke");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imagePaneLight2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagePaneLight1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imagePaneLight2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ImagePaneLight imagePaneLight1;
    private Dialogs.ImagePaneLight imagePaneLight2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}


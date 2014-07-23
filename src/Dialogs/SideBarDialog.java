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

import MyShapes.ColoredTextPaneSerializable;
import MyShapes.JournalParameters;
import MyShapes.Montage;
import MyShapes.MyImage2D;
import MyShapes.Row;
import MyShapes.TopBar;
import Commons.CommonClassesLight;
import Commons.Point3D;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * SideBarDialog is a dialog used to create top and lateral text bars on the
 * side of images
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class SideBarDialog extends javax.swing.JPanel {

    /**
     * Variables
     */
    Row r;
    ArrayList<BarSizeAndTextDialog> bars = new ArrayList<BarSizeAndTextDialog>();
    public static final int SPEED_UP_FACTOR = 20;
    private int max_val;
    public static final int HORIZONTAL_TOP = 0;
    public static final int HORIZONTAL_BOTTOM = 3;
    public static final int ADDITIONAL_LETTER = 4;
    public static final int VERTICAL_LEFT = 1;
    public static final int VERTICAL_RIGHT = 2;
    public int ORIENTATION = HORIZONTAL_TOP;
    public static boolean isHorizontal = true;
    public int row_size = 0;
    private JournalParameters jp;

    /**
     * Constructor
     *
     * @param r the row that should carry some text bars
     * @param ORIENTATION the orientation of the text bar
     * @param jp the journal parameters of the textbar
     */
    public SideBarDialog(Row r, int ORIENTATION, JournalParameters jp) {
        initComponents();
        CommonClassesLight.speedUpJScrollpane(jScrollPane1, SPEED_UP_FACTOR);
        this.r = r;
        this.ORIENTATION = ORIENTATION;
        this.jp = jp;
        row_size = r.getImageCountInXDirection() - 1;
        /*
         * if side textbars exist we try to reload them
         */
        if (ORIENTATION == HORIZONTAL_TOP || ORIENTATION == HORIZONTAL_BOTTOM || ORIENTATION == ADDITIONAL_LETTER) {
            isHorizontal = true;
            this.max_val = r.getImageCountInXDirection();
        } else {
            isHorizontal = false;
            if (ORIENTATION == VERTICAL_LEFT) {
                this.max_val = r.getImageCountInYDirectionLeft();
            } else {
                this.max_val = r.getImageCountInYDirectionRight();
            }
        }
        barSizeAndTextDialog1.setBeginEnd(0, max_val);
        /*
         * bug fix for windows getting too big
         */
        jScrollPane1.setMaximumSize(new Dimension(820, 512));
        jScrollPane1.setPreferredSize(new Dimension(820, 512));
        if (jp != null) {
            barSizeAndTextDialog1.setTextFont(jp.getOutterTextFont());
        }
        bars.add(barSizeAndTextDialog1);
        switch (ORIENTATION) {
            case HORIZONTAL_TOP:
                if (r.getTopTextBar() != null) {
                    reloadLateralText(((TopBar) r.getTopTextBar()).getBegin_n_ends_and_corresponding_text());
                    //also reaload position
                }
                break;
            case HORIZONTAL_BOTTOM:
                if (r.getBottomTextBar() != null) {
                    reloadLateralText(((TopBar) r.getBottomTextBar()).getBegin_n_ends_and_corresponding_text());
                }
                break;
            case VERTICAL_LEFT:
                if (r.getLeftTextBar() != null) {
                    reloadLateralText(((TopBar) r.getLeftTextBar()).getBegin_n_ends_and_corresponding_text());
                }
                break;
            case VERTICAL_RIGHT:
                if (r.getRightTextBar() != null) {
                    reloadLateralText(((TopBar) r.getRightTextBar()).getBegin_n_ends_and_corresponding_text());
                }
                break;
            case ADDITIONAL_LETTER:
                if (r.getAdditionbalLetterBar() != null) {
                    reloadLateralText(((TopBar) r.getAdditionbalLetterBar()).getBegin_n_ends_and_corresponding_text());
                }
                break;
        }
    }

    /**
     * We reload existing textbars
     *
     * @param begin_n_ends_and_corresponding_text
     */
    private void reloadLateralText(HashMap<Point3D.Integer, ColoredTextPaneSerializable> begin_n_ends_and_corresponding_text) {
        boolean first_found = true;
        for (Map.Entry<Point3D.Integer, ColoredTextPaneSerializable> entry : begin_n_ends_and_corresponding_text.entrySet()) {
            Object val = entry.getKey();
            /**
             * bug fix for retrocompatibility before alignment (assume centered
             * --> alignment = 0 = centered
             */
            Point3D.Integer pos;
            if (val instanceof Point3D.Integer) {
                pos = (Point3D.Integer) val;
            } else {
                pos = new Point3D.Integer((Point) val, 0);
            }

            ColoredTextPaneSerializable ctps = entry.getValue();
            if (ctps != null) {
                /*
                 * we check that the bounds are correct for the new image otherwise we ignore
                 */
                if (pos.x <= max_val && pos.y <= max_val) {
                    BarSizeAndTextDialog bsatd = new BarSizeAndTextDialog();
                    bsatd.setColoredTextPaneSerializable(ctps);
                    bsatd.setMaxMin(1, max_val);
                    bsatd.setValuesDirectly(pos.x, pos.y);
                    bsatd.setAlignement(pos.z);
                    if (first_found) {
                        bars.clear();
                        jPanel2.removeAll();
                        first_found = false;
                    }
                    bars.add(bsatd);
                    jPanel2.add(bars.get(bars.size() - 1));
                    jPanel2.updateUI();
                }
            }
        }
    }

    /**
     *
     * @return the text of the text bar and its begin and position
     */
    public HashMap<Point3D.Integer, ColoredTextPaneSerializable> getPosAndText() {
        HashMap<Point3D.Integer, ColoredTextPaneSerializable> pos_n_text = new HashMap<Point3D.Integer, ColoredTextPaneSerializable>();
        if (!bars.isEmpty()) {
            for (BarSizeAndTextDialog barSizeAndTextDialog : bars) {
                ColoredTextPaneSerializable ctps = barSizeAndTextDialog.getText();
                /*
                 * if text is empty --> ignore bar
                 */
                if (ctps != null) {
                    pos_n_text.put(new Point3D.Integer(barSizeAndTextDialog.getBarBegin(), barSizeAndTextDialog.getBarEnd(), barSizeAndTextDialog.getAlignment()), ctps);
                }
            }
        }
        return pos_n_text;
    }

//    public HashMap<Point, Integer> getPosAndAlignment() {
//        HashMap<Point, ColoredTextPaneSerializable> pos_n_text = new HashMap<Point, ColoredTextPaneSerializable>();
//        if (!bars.isEmpty()) {
//            for (BarSizeAndTextDialog barSizeAndTextDialog : bars) {
//                ColoredTextPaneSerializable ctps = barSizeAndTextDialog.getText();
//                /*
//                 * if text is empty --> ignore bar
//                 */
//                if (ctps != null) {
//                    pos_n_text.put(new Point(barSizeAndTextDialog.getBarBegin(), barSizeAndTextDialog.getBarEnd()), ctps);
//                }
//            }
//        }
//        return pos_n_text;
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        barSizeAndTextDialog1 = new Dialogs.BarSizeAndTextDialog();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setMaximumSize(new java.awt.Dimension(800, 512));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        barSizeAndTextDialog1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        jPanel2.add(barSizeAndTextDialog1);

        jScrollPane1.setViewportView(jPanel2);

        jPanel1.add(jScrollPane1);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnotherBar(evt);
            }
        });

        jButton2.setText("Delete Last");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(0, 657, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );
    }// </editor-fold>//GEN-END:initComponents

    //here the default stuff

    private void addAnotherBar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnotherBar
        BarSizeAndTextDialog current = new BarSizeAndTextDialog(0, max_val, 0);
        if (jp != null) {
            current.setTextFont(jp.getOutterTextFont());
        }
        bars.add(current);
        jPanel2.add(bars.get(bars.size() - 1));
        jPanel2.updateUI();
    }//GEN-LAST:event_addAnotherBar

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!bars.isEmpty()) {
            jPanel2.remove(bars.get(bars.size() - 1));
            bars.remove(bars.size() - 1);
        } else {
            jPanel2.removeAll();
        }
        jPanel2.updateUI();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Object> test = new ArrayList<Object>();
        MyImage2D.Double test1 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png");
        MyImage2D.Double test2 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png");
        MyImage2D.Double test3 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png");
        MyImage2D.Double test4 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png");
        MyImage2D.Double test10 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png");
        MyImage2D.Double test20 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png");
        MyImage2D.Double test30 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series015.png");
        MyImage2D.Double test40 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png");

        Montage b1 = new Montage(test1);
        Montage b2 = new Montage(test2);
        Montage b3 = new Montage(test3);
        Montage b4 = new Montage(test4);

        ArrayList<Object> table = new ArrayList<Object>();
        table.add(test10);
        table.add(test20);
        table.add(test30);
        table.add(test40);

        Montage b5 = new Montage(table, 4, 1, true, 3);
        test.add(b5);
        test.add(b1);
        test.add(b2);
        test.add(b3);
        test.add(b4);

        Row test_row = new Row(test, 1);
        test_row.setToWidth(512);
        Rectangle2D rec2d = test_row.getBounds2D();

        BufferedImage tmp = new BufferedImage((int) (rec2d.getX() + rec2d.getWidth() + 1.), (int) (rec2d.getY() + rec2d.getHeight() + 1.), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = tmp.createGraphics();
        test_row.draw(g2d);

        SideBarDialog iopane = new SideBarDialog(test_row, SideBarDialog.HORIZONTAL_TOP, null);

        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Additional Text Bars", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            //horizontal
            if (true) {
                HashMap<Point3D.Integer, ColoredTextPaneSerializable> pos_n_text = iopane.getPosAndText();
                TopBar tb = new TopBar(test_row, pos_n_text, TopBar.HORIZONTAL);
            }
            //vertical
            if (true) {
                HashMap<Point3D.Integer, ColoredTextPaneSerializable> pos_n_text = iopane.getPosAndText();
                TopBar tb = new TopBar(test_row, pos_n_text, TopBar.VERTICAL_LEFT);
            }
        }
        g2d.dispose();
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.BarSizeAndTextDialog barSizeAndTextDialog1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

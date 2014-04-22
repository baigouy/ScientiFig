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
import MyShapes.Montage;
import MyShapes.MyImage2D;
import MyShapes.Row;
import MyShapes.TopBar;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * BarSizeAndTextDialog is a dialog used to create top and lateral text bars on
 * the side of images
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class BarSizeAndTextDialog extends JPanel {

    /*
     * Variables
     */
    int max_end;
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    ColoredTextPaneSerializable ctps;

    /*
     * Empty constructor
     */
    public BarSizeAndTextDialog() {
        initComponents();
        coloredTextPane1.enableFrameSelection(true);
    }

    /**
     * Constructor
     *
     * @param begin first column/or row (depending on the orientation of the
     * bar)
     * @param end last possible column (this number cannot exceed the max nb of
     * images in a row or a column)
     * @param isHorizontalBar true if the text bar has to be an horizontal bar
     */
    public BarSizeAndTextDialog(int begin, int end, boolean isHorizontalBar) {
        this();
        coloredTextPane1.setTitle("Text Bar Parameters");
        jLabel3.setVisible(false);
        jComboBox1.setVisible(false);
        if (isHorizontalBar) {
            jComboBox1.removeItem("Left");
            jComboBox1.removeItem("Right");
        } else {
            jComboBox1.removeItem("Top");
            jComboBox1.removeItem("Bottom");
        }
        setBeginEnd(begin, end);
    }

    /**
     *
     * @return the alignment of the text
     */
    public int getAlignment() {
        if (jComboBox1.getSelectedItem().toString().equals("Left")) {
            return LEFT;
        } else if (jComboBox1.getSelectedItem().toString().equals("Right")) {
            return RIGHT;
        } else if (jComboBox1.getSelectedItem().toString().equals("Top")) {
            return TOP;
        } else {
            return BOTTOM;
        }
    }

    /**
     *
     * @return the first image to be covered by the text bar
     */
    public int getBarBegin() {
        return ((Integer) jSpinner1.getValue());
    }

    /**
     *
     * @return the last image to be covered by the text bar
     */
    public int getBarEnd() {
        return ((Integer) jSpinner2.getValue());
    }

    /**
     * Sets the begin and the end values for a spinner
     *
     * @param spinner
     * @param begin
     * @param end
     */
    public void setBarMaxBeginAndMaxEnd(JSpinner spinner, int begin, int end) {
        spinner.setModel(new SpinnerNumberModel(begin, begin, end, 1));
    }

    /**
     * Sets the begin and the end values for a spinner
     *
     * @param begin
     * @param end
     */
    public void setBeginEnd(int begin, int end) {
        this.max_end = end;
        setBarMaxBeginAndMaxEnd(jSpinner1, begin + 1, end);
        setBarMaxBeginAndMaxEnd(jSpinner2, begin + 1, end);
    }

    /**
     * Sets the min and the max values for spinners
     *
     * @param min
     * @param max
     */
    public void setMaxMin(int min, int max) {
        setBarMaxBeginAndMaxEnd(jSpinner1, min, max);
        setBarMaxBeginAndMaxEnd(jSpinner2, min, max);
    }

    public void setValuesDirectly(int begin, int end) {
        jSpinner1.setValue(begin);
        jSpinner2.setValue(end);
    }

    /**
     * Loads font settings from a coloredTextPaneSerializable
     *
     * @param coloredTextPaneSerializable
     */
    public void loadParams(ColoredTextPaneSerializable coloredTextPaneSerializable) {
        this.coloredTextPane1.applyFont(coloredTextPaneSerializable.ft);
    }

    /**
     * Sets font
     *
     * @param outterTextFont
     */
    public void setTextFont(Font outterTextFont) {
        this.coloredTextPane1.applyFont(outterTextFont);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        coloredTextPane1 = new Dialogs.ColoredTextPane();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Bar Parameters (From Left to Right or Top to Bottom)"));

        jLabel1.setText("Bar Begin (image nb):");

        jLabel2.setText("Bar End(image nb):");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                beginChanged(evt);
            }
        });

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel3.setText("Alignment:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Left", "Right", "Top", "Bottom" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jSpinner1, jSpinner2});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jLabel2)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(coloredTextPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coloredTextPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void beginChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_beginChanged
        int begin = getBarBegin();
        int end = getBarEnd();
        if (begin > end) {
            jSpinner2.setValue(begin);
        } else {
            jSpinner2.setValue(end);
        }
    }//GEN-LAST:event_beginChanged

    /**
     *
     * @return the text entered by the user
     */
    public ColoredTextPaneSerializable getText() {
        if (coloredTextPane1.getText().trim().isEmpty()) {
            return null;
        }
        return coloredTextPane1.export();
    }

    public void setColoredTextPaneSerializable(ColoredTextPaneSerializable ctps) {
        coloredTextPane1.setColoredTextPaneSerializable(ctps);// = new ColoredTextPane(ctps);
        repaint();
    }

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

        BarSizeAndTextDialog iopane = new BarSizeAndTextDialog(0, test_row.getImageCountInYDirectionLeft(), false);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Select a font", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            if (true) {
                HashMap<Point, ColoredTextPaneSerializable> pos_n_text = new HashMap<Point, ColoredTextPaneSerializable>();
                pos_n_text.put(new Point(1, 1), iopane.getText());
                pos_n_text.put(new Point(2, 2), iopane.getText());
                pos_n_text.put(new Point(3, 4), iopane.getText());
                TopBar tb = new TopBar(test_row, pos_n_text, TopBar.HORIZONTAL);
            }
            //vertical
            if (true) {
                HashMap<Point, ColoredTextPaneSerializable> pos_n_text = new HashMap<Point, ColoredTextPaneSerializable>();
                pos_n_text.put(new Point(1, 1), iopane.getText());
                pos_n_text.put(new Point(2, 2), iopane.getText());
                pos_n_text.put(new Point(3, 4), iopane.getText());
                TopBar tb = new TopBar(test_row, pos_n_text, TopBar.VERTICAL_LEFT);
            }

        }

        g2d.dispose();
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Dialogs.ColoredTextPane coloredTextPane1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    // End of variables declaration//GEN-END:variables
}



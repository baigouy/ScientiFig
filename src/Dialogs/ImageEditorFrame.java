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

import GUIs.ScientiFig_;
import MyShapes.MyImage2D;
import MyShapes.MyImageVector;
import MyShapes.MyPlotVector;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * ImageEditorFrame is an interactive dialog that allows the user to change the
 * text and to add a scale bar to images
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ImageEditorFrame extends javax.swing.JFrame {

    MyImage2D curSel;
    /*
     * prevents cyclic updating of spinners 
     */
    boolean block_update = false;

    /**
     * Creates a new ImageEditorFrame
     */
    public ImageEditorFrame() {
        ScientiFig_.setLNF();
        initComponents();
    }

    /**
     * Sets the selection to curSel
     *
     * @param curSel
     */
    public void setCurSel(Object curSel) {
        ScientiFig_.AR.setText(" N.A. ");
        ScientiFig_.imageWidth.setText(" N.A. ");
        ScientiFig_.imageHeight.setText(" N.A. ");
        if (curSel == null) {
            this.curSel = null;
            this.setVisible(false);
            return;
        }
        if (curSel == this.curSel) {
            loadCurSelParameters();
            return;
        }
        if (curSel instanceof MyImage2D) {
            this.curSel = (MyImage2D) curSel;
        } else {
            this.curSel = null;
            this.setVisible(false);
            return;
        }
        loadCurSelParameters();
    }

    /**
     *
     * @return the stroke size
     */
    public float getStrokeSize() {
        return ((Float) jSpinner6.getValue());
    }

    /**
     *
     * @return the scale bar size (in pixels) for insets
     */
    public int getInsetScaleBarSize() {
        return ((Integer) jSpinner8.getValue());
    }

    /**
     * refresh and display the current image parameters (width, height, AR)
     */
    public void updateImageSize() {
        if (curSel != null) {
            int width = curSel.getImageWidth();
            int height = curSel.getImageHeight();
            double AR = (double) width / (double) height;
            ScientiFig_.imageWidth.setText(" " + width + " ");
            ScientiFig_.imageHeight.setText(" " + height + " ");
            NumberFormat formatter = new DecimalFormat("#.##");
            ScientiFig_.AR.setText(" " + formatter.format(AR));// + " "
        }
    }

    /**
     * Loads parameters from the current selection and applies them to the
     * dialog objects
     */
    public void loadCurSelParameters() {
        if (curSel instanceof MyImage2D) {
            try {
                updateImageSize();
                jRadioButton12.setText("Upper Right Corner");
                jRadioButton6.setText("Upper Left Corner");
                jRadioButton3.setText("Lower Left Corner");
                jRadioButton5.setText("Lower Right Corner");
                if (curSel instanceof MyPlotVector) {
                    jRadioButton12.setText("Graph Title");
                    jRadioButton6.setText("y Axis Title");
                    jRadioButton3.setText("x Axis Title");
                    jRadioButton5.setText("legend title");
                    rLabelEditor1.setVisible(true);
                    jRadioButton4.setVisible(false);
                    if (jRadioButton4.isSelected()) {
                        jRadioButton5.doClick();
                    }
                    jPanel5.setVisible(false);
                } else if (curSel instanceof MyImageVector) {
                    jRadioButton4.setVisible(false);
                    if (jRadioButton4.isSelected()) {
                        jRadioButton5.doClick();
                    }
                    jPanel5.setVisible(false);
                    rLabelEditor1.setVisible(false);
                } else {
                    jRadioButton4.setVisible(true);
                    jPanel5.setVisible(true);
                    rLabelEditor1.setVisible(false);
                }
                /*
                 * bug fix for unwanted update of comments
                 */
                CaretListener[] listeners = jTextArea1.getCaretListeners();
                for (CaretListener caretListener : listeners) {
                    jTextArea1.removeCaretListener(caretListener);
                }
                String comments = curSel.getAssociatedComments();
                if (comments != null) {
                    jTextArea1.setText(comments);
                } else {
                    jTextArea1.setText("");
                }
                jTextArea1.addCaretListener(new CaretListener() {
                    @Override
                    public void caretUpdate(CaretEvent e) {
                        curSel.setAssociatedComments(jTextArea1.getText());
                    }
                });
                block_update = true;
                jSpinner1.setValue((int) curSel.getScale_bar_size_in_px_of_the_real_image());
                jSpinner3.setValue(curSel.getScale_bar_size_in_unit());
                jSpinner4.setValue(curSel.getSize_of_one_px_in_unit());
                paintedButton21.setColor(curSel.getScalebarColor());
                jSpinner5.setValue(curSel.getSCALE_BAR_STROKE_SIZE());
                /**
                 * bug fix for old saved files
                 */
                if (curSel.getFraction_of_parent_image_width() == 0) {
                    curSel.setFraction_of_parent_image_width(0.25);
                }
                jSpinner2.setValue(curSel.getFraction_of_parent_image_width());
                jSpinner7.setValue(curSel.getInsetInternalSpace());
                paintedButton22.setColor(curSel.getInsetBorderColor());

                jSpinner9.setValue(curSel.getGelBorderSize());
                paintedButton23.setColor(curSel.getGelBorderColor());

                jSpinner8.setValue(curSel.getScale_barSize_in_pixels_PIP());
                switch (curSel.getINSET_POSITION()) {
                    case MyImage2D.TOP_ | MyImage2D._LEFT:
                        jRadioButton7.setSelected(true);
                        break;
                    case MyImage2D.BOTTOM_ | MyImage2D._LEFT:
                        jRadioButton8.setSelected(true);
                        break;
                    case MyImage2D.BOTTOM_ | MyImage2D._RIGHT_:
                        jRadioButton10.setSelected(true);
                        break;
                    default:
                        /*case MyImage2D.TOP_ | MyImage2D._RIGHT_:*/
                        jRadioButton9.setSelected(true);
                        break;
                }
                block_update = false;
                textPositionChanged(null);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        } else {
            this.setVisible(false);
            ScientiFig_.AR.setText(" N.A. ");
            ScientiFig_.imageWidth.setText(" N.A. ");
            ScientiFig_.imageHeight.setText(" N.A. ");
        }
    }

    /**
     *
     * @return the position where the image inset should be placed
     */
    public int getInsetPosition() {
        if (jRadioButton9.isSelected()) {
            return MyImage2D.TOP_ | MyImage2D._RIGHT_;
        }
        if (jRadioButton8.isSelected()) {
            return MyImage2D.BOTTOM_ | MyImage2D._LEFT;
        }
        if (jRadioButton10.isSelected()) {
            return MyImage2D.BOTTOM_ | MyImage2D._RIGHT_;
        }
        return MyImage2D.TOP_ | MyImage2D._LEFT;
    }

    /**
     *
     * @return the fraction of the image width that should be dedicated to the
     * inset
     */
    public double getFraction() {
        return ((Double) jSpinner2.getValue());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        forceStrokeSizeChange = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jSpinner9 = new javax.swing.JSpinner();
        paintedButton23 = new Commons.PaintedButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        addPiP = new javax.swing.JButton();
        removePiP = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        paintedButton22 = new Commons.PaintedButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSpinner8 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        paintedButton21 = new Commons.PaintedButton();
        rLabelEditor1 = new RLabelEditor() {
            @Override
            public void somethingChanged(Object source) {
                super.somethingChanged(source);
                if (curSel instanceof MyPlotVector) {
                    ((MyPlotVector)curSel).updateGGTitle();
                    ((MyPlotVector)curSel).updatePlot();
                }
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        };
        jPanel7 = new javax.swing.JPanel();
        imageLabel = new Dialogs.ColoredTextPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setTitle("Edit Image");
        setFocusable(false);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Icons/icon_029.png")).getImage());
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setMinimumSize(new java.awt.Dimension(735, 893));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setMinimumSize(new java.awt.Dimension(735, 260));
        jPanel2.setPreferredSize(new java.awt.Dimension(365, 260));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Image/Text"));
        jPanel6.setMinimumSize(new java.awt.Dimension(0, 320));
        jPanel6.setPreferredSize(new java.awt.Dimension(353, 320));

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("Upper Left Corner");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("<html><b><h3>Select the position of <BR>the text to edit</h3></b></html>");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Lower Left Corner");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("Lower Right Corner");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Scale Bar");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        jLabel12.setText("Stroke Size:");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.65f), Float.valueOf(0.1f), null, Float.valueOf(0.1f)));

        forceStrokeSizeChange.setText("Force");
        forceStrokeSizeChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reForceStrokeSize(evt);
            }
        });

        jLabel13.setText("<html><center><font color=\"#FF0000\">NB: changing stroke by pressing 'Force' is not reversible</font></html>");
        jLabel13.setMaximumSize(new java.awt.Dimension(2147483647, 3000));

        buttonGroup1.add(jRadioButton11);
        jRadioButton11.setText("Letter");
        jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton12);
        jRadioButton12.setText("Upper Right Corner");
        jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPositionChanged(evt);
            }
        });

        jLabel14.setText("Gel border:");

        jSpinner9.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jSpinner9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gelBorderSizeChanged(evt);
            }
        });

        paintedButton23.setText("Color");
        paintedButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borderColorChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jRadioButton11)
                        .addComponent(jRadioButton6)
                        .addComponent(jRadioButton12, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSpinner9)
                                .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(forceStrokeSizeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(paintedButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jRadioButton3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButton4)
                                .addComponent(jRadioButton5))))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(2, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton11)
                    .addComponent(jRadioButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(forceStrokeSizeChange)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jSpinner9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paintedButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel6);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Inset/PiP"));
        jPanel5.setMinimumSize(new java.awt.Dimension(380, 320));
        jPanel5.setPreferredSize(new java.awt.Dimension(380, 320));

        jLabel7.setText("<html>Fraction of the image width <br>(dedicated for the inset):</html>");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0.25d, 0.01d, 1.0d, 0.01d));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PiPFractionChanged(evt);
            }
        });

        jLabel8.setText("Inset position:");

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setText("upper left corner");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PiPPositionChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton8);
        jRadioButton8.setText("lower left corner");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PiPPositionChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton9);
        jRadioButton9.setSelected(true);
        jRadioButton9.setText("upper right corner");
        jRadioButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PiPPositionChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton10);
        jRadioButton10.setText("lower right corner");
        jRadioButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PiPPositionChanged(evt);
            }
        });

        addPiP.setText("Add/Replace Inset(s)");
        addPiP.setToolTipText("Add/Replace Inset(s)");
        addPiP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        removePiP.setText("Remove Inset(s)");
        removePiP.setToolTipText("Remove Inset(s)");
        removePiP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel9.setText("Inset spacer:");

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                insetSpacerChanged(evt);
            }
        });

        jLabel10.setText("Inset Border Color:");

        paintedButton22.setBackground(new java.awt.Color(255, 255, 255));
        paintedButton22.setForeground(new java.awt.Color(51, 51, 51));
        paintedButton22.setText("Color");
        paintedButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InsetBorderColorChanged(evt);
            }
        });

        jButton1.setText("B");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forceBlackOrWhite(evt);
            }
        });

        jButton2.setText("W");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forceBlackOrWhite(evt);
            }
        });

        jSpinner8.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                InsetScaleBarChanged(evt);
            }
        });

        jLabel11.setText("Inset Scale Bar Size (px):");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton8)
                            .addComponent(jRadioButton7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner7)
                            .addComponent(jSpinner8)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jRadioButton10, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paintedButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(addPiP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removePiP, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel9))
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel11))
                    .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel10))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton1)
                        .addComponent(paintedButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addGap(3, 3, 3)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton7)
                    .addComponent(jRadioButton9))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton8)
                    .addComponent(jRadioButton10))
                .addGap(2, 2, 2)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addPiP)
                    .addComponent(removePiP))
                .addGap(0, 0, 0))
        );

        jPanel2.add(jPanel5);

        jPanel3.add(jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("ScaleBar Parameters"));
        jPanel1.setMaximumSize(null);
        jPanel1.setLayout(new java.awt.GridLayout(5, 4));

        jLabel1.setText("Bar Size in Px Of The Non Scaled Image:");
        jPanel1.add(jLabel1);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        jSpinner1.setMinimumSize(new java.awt.Dimension(90, 28));
        jSpinner1.setPreferredSize(new java.awt.Dimension(90, 28));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scaleBarSizeChanged(evt);
            }
        });
        jPanel1.add(jSpinner1);

        jLabel4.setText("Bar Size in whatever unit (µm, mm,...):");
        jPanel1.add(jLabel4);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.001d)));
        jSpinner3.setMinimumSize(new java.awt.Dimension(90, 28));
        jSpinner3.setPreferredSize(new java.awt.Dimension(90, 28));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scaleLengthChanged(evt);
            }
        });
        jPanel1.add(jSpinner3);

        jLabel5.setText("Px Size In the Same unit (Conversion Factor):");
        jPanel1.add(jLabel5);

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.001d), null, Double.valueOf(0.001d)));
        jSpinner4.setMinimumSize(new java.awt.Dimension(90, 28));
        jSpinner4.setPreferredSize(new java.awt.Dimension(90, 28));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                conversionFactorChanged(evt);
            }
        });
        jPanel1.add(jSpinner4);

        jLabel6.setText("Bar thickness/height (in px):");
        jPanel1.add(jLabel6);

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(3.0f), Float.valueOf(0.1f), null, Float.valueOf(0.1f)));
        jSpinner5.setMinimumSize(new java.awt.Dimension(90, 28));
        jSpinner5.setPreferredSize(new java.awt.Dimension(90, 28));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                barHeightChanged(evt);
            }
        });
        jPanel1.add(jSpinner5);

        jLabel3.setText("Bar Color:");
        jPanel1.add(jLabel3);

        paintedButton21.setBackground(new java.awt.Color(255, 255, 255));
        paintedButton21.setForeground(new java.awt.Color(0, 0, 0));
        paintedButton21.setText("Color");
        paintedButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeColor(evt);
            }
        });
        jPanel1.add(paintedButton21);

        jPanel3.add(jPanel1);
        jPanel3.add(rLabelEditor1);

        imageLabel.setMaximumSize(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Comments (just for you, they will not be displayed)"));

        jScrollPane1.setMinimumSize(null);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        jPanel3.add(jPanel7);

        getContentPane().add(jPanel3);
        jPanel3.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @return the length of the scalebar in pixels
     */
    public int getScaleBarLengthInPx() {
        jSpinner1.validate();
        return ((Integer) jSpinner1.getValue());
    }

    /**
     *
     * @return the height of the scalebar in pixels
     */
    private float getScaleBarStrokeSize() {
        jSpinner5.validate();
        return ((Float) jSpinner5.getValue());
    }

    /**
     *
     * @return the length of the scalebar in any unit
     */
    public double getScaleBarLengthInUnit() {
        jSpinner3.validate();
        return ((Double) jSpinner3.getValue());
    }

    /**
     *
     * @return the conversion factor between bar size in pixels and bar size in
     * any unit
     */
    public double getScaleBarPixelSize() {
        jSpinner4.validate();
        return ((Double) jSpinner4.getValue());
    }

    /**
     *
     * @return the color of the scalebar
     */
    public int getScaleBarColor() {
        return 0xFF000000 + (paintedButton21.getColor() & 0x00FFFFFF);
    }

    private void scaleBarSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_scaleBarSizeChanged
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                jSpinner1.commitEdit();
                if (!block_update) {
                    double size = (getScaleBarLengthInPx() * getScaleBarPixelSize());
                    jSpinner3.setValue(size);
                }
                if (curSel instanceof MyImage2D) {
                    ((MyImage2D) curSel).setScale_bar_size_in_px_of_the_real_image(getScaleBarLengthInPx());
                }
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_scaleBarSizeChanged

    private void scaleLengthChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_scaleLengthChanged
        try {
            jSpinner3.commitEdit();
            int size_in_px = (int) Math.round(getScaleBarLengthInUnit() / getScaleBarPixelSize());
            block_update = true;
            jSpinner1.setValue(size_in_px);
            scaleBarSizeChanged(evt);
            block_update = false;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_scaleLengthChanged

    private void conversionFactorChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_conversionFactorChanged
        if (block_update) {
            return;
        }
        try {
            jSpinner4.commitEdit();
            int size_in_px = (int) Math.round(getScaleBarLengthInUnit() / getScaleBarPixelSize());
            block_update = true;
            jSpinner1.setValue(size_in_px);
            if (curSel == null) {
                return;
            }
            ((MyImage2D) curSel).setSize_of_one_px_in_unit(getScaleBarPixelSize());
            if (curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setScale_bar_size_in_unit(getScaleBarLengthInUnit());
            }
            scaleBarSizeChanged(evt);
            block_update = false;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_conversionFactorChanged

    private void changeColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeColor
        Color color = JColorChooser.showDialog(this, "Pick a Color", paintedButton21.getBackground());
        if (color != null) {
            paintedButton21.setColor(color);
            try {
                if (curSel != null && curSel instanceof MyImage2D) {
                    ((MyImage2D) curSel).setScalebarColor(getScaleBarColor());
                    if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                        ScientiFig_.updateTable(curSel);
                    } else {
                        ScientiFig_.updateFigure(curSel);
                    }
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }//GEN-LAST:event_changeColor

    private void barHeightChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_barHeightChanged
        if (block_update) {
            return;
        }
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setSCALE_BAR_STROKE_SIZE(getScaleBarStrokeSize());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_barHeightChanged

    /**
     * button dispatcher
     *
     * @param evt
     */
    private void runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAll
        Object source = evt.getSource();
        if (source == null) {
            return;
        }
        if (source == addPiP) {
            ScientiFig_.PIP.doClick();
            if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                ScientiFig_.updateTable();
            } else {
                ScientiFig_.updateFigure();
            }
        }
        if (source == removePiP) {
            ScientiFig_.removePiP.doClick();
            if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                ScientiFig_.updateTable();
            } else {
                ScientiFig_.updateFigure();
            }
        }
    }//GEN-LAST:event_runAll

    private void PiPFractionChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PiPFractionChanged
        if (block_update) {
            return;
        }
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setFraction_of_parent_image_width(getFraction());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_PiPFractionChanged

    private void PiPPositionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PiPPositionChanged
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setINSET_POSITION(getInsetPosition());
                ((MyImage2D) curSel).setFraction_of_parent_image_width(getFraction());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_PiPPositionChanged

    public int getGelBorderSize() {
        return ((Integer) jSpinner9.getValue());
    }

    public int getInternalSpace() {
        return ((Integer) jSpinner7.getValue());
    }

    private void insetSpacerChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_insetSpacerChanged
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setInsetInternalSpace(getInternalSpace());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_insetSpacerChanged

    private void InsetBorderColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsetBorderColorChanged
        Color color = JColorChooser.showDialog(this, "Pick a Color", paintedButton22.getBackground());
        if (color != null) {
            paintedButton22.setColor(color);
            try {
                if (curSel != null && curSel instanceof MyImage2D) {
                    ((MyImage2D) curSel).setInsetBorderColor(getInsetBorderColor());
                    if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                        ScientiFig_.updateTable(curSel);
                    } else {
                        ScientiFig_.updateFigure(curSel);
                    }
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }//GEN-LAST:event_InsetBorderColorChanged

    private void forceBlackOrWhite(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forceBlackOrWhite
        Object source = evt.getSource();
        int color = 0xFFFFFF;
        if (source == jButton1) {
            color = 0x000000;
        }
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setInsetBorderColor(color);
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_forceBlackOrWhite

    private void InsetScaleBarChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_InsetScaleBarChanged
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setScale_barSize_in_pixels_PIP(getInsetScaleBarSize());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_InsetScaleBarChanged

    private void borderColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borderColorChanged
        Color color = JColorChooser.showDialog(this, "Pick a Color", paintedButton23.getBackground());
        if (color != null) {
            paintedButton23.setColor(color);
            try {
                if (curSel != null && curSel instanceof MyImage2D) {
                    ((MyImage2D) curSel).setGelBorderColor(getGelBorderColor());
                    if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                        ScientiFig_.updateTable(curSel);
                    } else {
                        ScientiFig_.updateFigure(curSel);
                    }
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }//GEN-LAST:event_borderColorChanged

    private void gelBorderSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gelBorderSizeChanged
        try {
            if (curSel != null && curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setGelBorderSize(getGelBorderSize());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_gelBorderSizeChanged

    private void textPositionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPositionChanged
        if (jRadioButton11.isSelected()) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getLetter());
            imageLabel.setTitle("Text: " + jRadioButton11.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            rLabelEditor1.setVisible(false);
            jPanel1.setVisible(false);
        }
        if (jRadioButton6.isSelected() && !(curSel instanceof MyPlotVector)) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getUpper_left_text());
            imageLabel.setTitle("Text: " + jRadioButton6.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            jPanel1.setVisible(false);
        } else if (jRadioButton6.isSelected() && (curSel instanceof MyPlotVector)) {
            rLabelEditor1.setRlabel(((MyPlotVector) curSel).getyAxisLabel());
            rLabelEditor1.setTitle(jRadioButton6.getText());
            rLabelEditor1.setVisible(true);
            imageLabel.setVisible(false);
            jPanel1.setVisible(false);
        }
        if (jRadioButton3.isSelected() && !(curSel instanceof MyPlotVector)) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getLower_left_text());
            imageLabel.setTitle("Text: " + jRadioButton3.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            jPanel1.setVisible(false);
        } else if (jRadioButton3.isSelected() && (curSel instanceof MyPlotVector)) {
            rLabelEditor1.setRlabel(((MyPlotVector) curSel).getxAxisLabel());
            rLabelEditor1.setVisible(true);
            rLabelEditor1.setTitle(jRadioButton3.getText());
            imageLabel.setVisible(false);
            jPanel1.setVisible(false);
        }
        if (jRadioButton12.isSelected() && !(curSel instanceof MyPlotVector)) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getUpper_right_text());
            imageLabel.setTitle("Text: " + jRadioButton12.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            jPanel1.setVisible(false);
        } else if (jRadioButton12.isSelected() && (curSel instanceof MyPlotVector)) {
            rLabelEditor1.setRlabel(((MyPlotVector) curSel).getTitleLabel());
            rLabelEditor1.setVisible(true);
            rLabelEditor1.setTitle(jRadioButton12.getText());
            imageLabel.setVisible(false);
            jPanel1.setVisible(false);
        }
        if (jRadioButton5.isSelected() && !(curSel instanceof MyPlotVector)) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getLower_right_text());
            imageLabel.setTitle("Text: " + jRadioButton5.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            jPanel1.setVisible(false);
        } else if (jRadioButton5.isSelected() && (curSel instanceof MyPlotVector)) {
            rLabelEditor1.setRlabel(((MyPlotVector) curSel).getLegendLabel());
            rLabelEditor1.setVisible(true);
            rLabelEditor1.setTitle(jRadioButton5.getText());
            imageLabel.setVisible(false);
            jPanel1.setVisible(false);
        }
        if (jRadioButton4.isSelected()) {
            imageLabel.setVisible(true);
            if (imageLabel != null) {
                imageLabel.removeListeners();
            }
            imageLabel.setColoredTextPaneSerializable(curSel.getScale_bar_txt());
            imageLabel.setTitle("Text: " + jRadioButton4.getText());
            imageLabel.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    if (curSel != null) {
                        if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                            ScientiFig_.updateTable(curSel);
                        } else if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 2) {
                            ScientiFig_.updateFigure(curSel);
                        }
                    }
                }
            });
            jPanel1.setVisible(true);
            rLabelEditor1.setVisible(false);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pack();
                repaint();
            }
        });
    }//GEN-LAST:event_textPositionChanged

    private void reForceStrokeSize(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reForceStrokeSize
        try {
            if (curSel == null) {
                return;
            }
            if (curSel instanceof MyPlotVector) {
                LineGraphicsDialog iopane = new LineGraphicsDialog(false, false, false, true, getStrokeSize(), false);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Stroking Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    ((MyPlotVector) curSel).setStrokeSize(iopane.getNewStrokeSize(), iopane.getNewPointSize(), iopane.isChangeStrokeSizeForGraphs(), iopane.isChangePointSize());
                    if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                        ScientiFig_.updateTable(curSel);
                    } else {
                        ScientiFig_.updateFigure(curSel);
                    }
                }
                return;
            }
            if (curSel instanceof MyImageVector) {
                LineGraphicsDialog iopane = new LineGraphicsDialog(true, false, true, false, getStrokeSize(), false);
                LineGraphicsDialog.jPanel4.setVisible(false);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Stroking Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    ((MyImageVector) curSel).setStrokeSize(getStrokeSize(), iopane.isChangeStrokeSizeForSVGs(), iopane.isStrokeIllustrator());
                    if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                        ScientiFig_.updateTable(curSel);
                    } else {
                        ScientiFig_.updateFigure(curSel);
                    }
                }
                return;
            }
            if (curSel instanceof MyImage2D) {
                ((MyImage2D) curSel).setAssociatedShapeStrokeSize(getStrokeSize());
                if (ScientiFig_.jTabbedPane1.getSelectedIndex() == 1) {
                    ScientiFig_.updateTable(curSel);
                } else {
                    ScientiFig_.updateFigure(curSel);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }//GEN-LAST:event_reForceStrokeSize

    private int getGelBorderColor() {
        return paintedButton23.getColor();
    }

    /**
     *
     * @return inset border color
     */
    private int getInsetBorderColor() {
        return paintedButton22.getColor();
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            boolean found = false;
            if (!found) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            }
        } catch (Exception ex) {
            System.out.println("Failed to load L&F:");
            System.out.println(ex);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                ImageEditorFrame ipf = new ImageEditorFrame();
                ipf.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPiP;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton forceStrokeSizeChange;
    private Dialogs.ColoredTextPane imageLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JSpinner jSpinner9;
    private javax.swing.JTextArea jTextArea1;
    private Commons.PaintedButton paintedButton21;
    private Commons.PaintedButton paintedButton22;
    private Commons.PaintedButton paintedButton23;
    private Dialogs.RLabelEditor rLabelEditor1;
    private javax.swing.JButton removePiP;
    // End of variables declaration//GEN-END:variables
}

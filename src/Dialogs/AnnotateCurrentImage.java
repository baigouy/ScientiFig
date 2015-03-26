/*
 License ScientiFig (new BSD license)

 Copyright (C) 2012-2015 Benoit Aigouy 

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

import MyShapes.LineStrokable;
import MyShapes.MyImage2D;
import MyShapes.MyLine2D;
import MyShapes.MyPoint2D;
import MyShapes.MyRectangle2D;
import MyShapes.PARoi;
import MyShapes.Transformable;
import Commons.CommonClassesLight;
import Commons.ImageTransformations;
import Commons.Loader;
import MyShapes.ComplexShapeLight;
import MyShapes.Contourable;
import MyShapes.Fillable;
import MyShapes.MyEllipse2D;
import MyShapes.MyPolygon2D;
import MyShapes.SerializableBufferedImage2;
import Tools.ROITools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

/**
 * AnnotateCurrentImage is a tool to drawAndFill/handle and edit vectorial
 * objects
 *
 * @author Benoit Aigouy
 */
public class AnnotateCurrentImage extends javax.swing.JPanel {

    /**
     * Variables
     */
    Object current_selection;
    MyImage2D img;
    int left_crop;
    int up_crop;
    double zoom = 1.;
    SerializableBufferedImage2 inset;
    boolean loading = false;
    DefaultListModel ROIListModel = new DefaultListModel();
    ArrayList<Object> oneLinerCrops = new ArrayList<Object>();

    /**
     * Constructor
     *
     * @param bg_image backround image over which various vectorial shapes will
     * be added
     */
    public AnnotateCurrentImage(MyImage2D bg_image, ArrayList<Object> copiedROIs) {
        initComponents();
        jList1.setModel(ROIListModel);
        rOIpanelLight1.setROIPanelActive(true);
        rOIpanelLight1.addKeyLisetener(true);
        setImage(bg_image);
        rOIpanelLight1.show_base_of_line = false;
        speedUpScrollbars();
        /*
         * we load the combo with arrows
         */
        Vector arrows = new Vector();
        /*
         * NB: the order should match ARROW_HEAD_TYPE in MyLine2D
         */
        /*
         //somehow some of these unicodes characters don't display on some systems --> I replaced them by icons
         arrows.add(CommonClassesLight.RIGHT_ARROW);
         arrows.add(CommonClassesLight.RIGHT_ARROW_UP_BARB);
         arrows.add(CommonClassesLight.RIGHT_ARROW_DOWN_BARB);
         arrows.add(CommonClassesLight.LEFT_RIGHT_ARROW);
         arrows.add(CommonClassesLight.RIGHT_INHIBITION_SYMBOL);
         arrows.add(CommonClassesLight.LEFT_INHIBITION_SYMBOL + "" + CommonClassesLight.RIGHT_INHIBITION_SYMBOL);
         */
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/arrow.png")));
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/arrow_half_head_down.png")));
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/arrow_half_head_up.png")));
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/double_headed_arrow.png")));
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/inhibition_arrow.png")));
        arrows.add(new javax.swing.ImageIcon(getClass().getResource("/Icons/double_headed_inhibition_arrow.png")));
        jComboBox2.setModel(new DefaultComboBoxModel(arrows));
        if (copiedROIs != null && (copiedROIs instanceof ArrayList)) {
            rOIpanelLight1.copy = copiedROIs;
        }
    }

    public ArrayList<Object> getCopiedROIs() {
        return rOIpanelLight1.copy;
    }

    /**
     * Constructor
     *
     * @param bg_image backround image over which various vectorial shapes will
     * be added
     * @param contained_shapes vectorial objects to associate to the image
     */
    public AnnotateCurrentImage(MyImage2D bg_image, ArrayList<Object> contained_shapes, ArrayList<Object> copiedROIs) {
        this(bg_image, copiedROIs);
        rOIpanelLight1.setROIS(contained_shapes);
    }

    /**
     * A small trick to increase the scrolling speed of scrollbars
     */
    private void speedUpScrollbars() {
        CommonClassesLight.speedUpJScrollpane(jScrollPane1);
    }

    /**
     * Sets the background image over which we want to add vectorial objects
     *
     * @param bg
     */
    private void setImage(MyImage2D bg) {
        BufferedImage bimg = bg.getCurrentDisplay();
        rOIpanelLight1.setBackgroundImage(bimg);
        left_crop = bg.left_crop;
        up_crop = bg.up_crop;
        /*
         * bug fix for cancel --> we clone all objects
         */
        ArrayList<Object> extras = bg.cloneAssociatedObjects();
        /*
         * dirty fix to position shapes keep for now and do better some other day
         * bug fix to ensure compatibility with SF version 1.0 images, bug should not occur with recent .yf5m files
         */
        if (extras != null) {
            for (Object object : extras) {
                if (object instanceof PARoi) {
                    ((PARoi) object).translate(-bg.left_crop, -bg.up_crop);
                }
            }
        }
        rOIpanelLight1.setROIS(extras);
        rOIpanelLight1.setDimensions(bg.getImageWidth(), bg.getImageHeight());
        this.img = bg;
    }

    boolean preventROIselChanged = false;

    private void updateListContent() {
        preventROIselChanged = true;
        try {
            int[] selected_indices = jList1.getSelectedIndices();
            jList1.setModel(new DefaultListModel());
            ArrayList<Object> obj = rOIpanelLight1.getROIS();
            ROIListModel.clear();
            if (obj != null) {
                for (Object object : obj) {
                    ROIListModel.addElement(object);
                }
            }
            jList1.setModel(ROIListModel);
            jList1.setSelectedIndices(selected_indices);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            preventROIselChanged = false;
        }
    }

    /**
     *
     * @return a list of vectorial objects associated to the image
     */
    public ArrayList<Object> getAssociatedObjects() {
        ArrayList<Object> extras = rOIpanelLight1.getROIS();
        for (Object object : extras) {
            if (object instanceof PARoi) {
                ((PARoi) object).translate(left_crop, up_crop);
            }
        }
        return extras;
    }

    private void updateDrawColor(float opacity) {
        CommonClassesLight.enableOrDisableAnyComponent(opacity > 0, jLabel2, contourColor);
        contourColor.setActive(opacity > 0f);
    }

    private void updateFillColor(float opacity) {
        CommonClassesLight.enableOrDisableAnyComponent(opacity > 0, jLabel12, fillColor);
        fillColor.setActive(opacity > 0f);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        rOIpanelLight1 = new ROIpanelLight() {

            public void ROIPositionChanged()
            {
                if (jTabbedPane1.getSelectedIndex()==1)
                {
                    updateListContent();
                }
            }

            @Override
            public void ROIOrderChanged()
            {
                if (jTabbedPane1.getSelectedIndex()==1)
                {
                    updateListContent();
                }
            }

            @Override
            public void ROINumberChanged()
            {
                if (jTabbedPane1.getSelectedIndex()==1)
                {
                    updateListContent();
                }
            }

            @Override
            public void selectionChanged(Object selection) {
                try {
                    loading = true;
                    if (selection != null) {
                        current_selection = selection;
                        preventROIselChanged = true;
                        if(!(current_selection instanceof ComplexShapeLight))
                        {
                            jList1.setSelectedValue(current_selection, true);
                        }
                        else
                        {
                            /**
                            * get all the contained elements
                            */
                            HashSet<Object> objs = ((ComplexShapeLight) current_selection).getGroup();

                            ArrayList<Integer> sel = new ArrayList<Integer>();
                            for (Object object : objs) {
                                sel.add(ROIListModel.indexOf(object));
                            }
                            int[] selec = new int[sel.size()];
                            for (int i = 0; i < selec.length; i++) {
                                selec[i] = sel.get(i);
                            }
                            jList1.setSelectedIndices(selec);
                        }
                        preventROIselChanged = false;
                        /**
                        * enable or disable the bracket panel according to shape type
                        */
                        CommonClassesLight.enableOrDisablePanelComponents(jPanel5, ROITools.isBracket(current_selection));
                        /**
                        * enable or disable arrowhead oprions depending on shape type
                        */
                        CommonClassesLight.enableOrDisablePanelComponents(jPanel4, ROITools.isArrow(current_selection));

                        float stroke = ((PARoi) current_selection).getStrokeSize();
                        jSpinner1.setValue(stroke);
                        if (selection instanceof Contourable)
                        {
                            drawOpacitySpinner.setValue(((Contourable)selection).getDrawOpacity());
                        }
                        boolean isDrawContour = !ROITools.isDrawTransparent(selection);
                        if (isDrawContour)
                        {
                            if ((selection instanceof Contourable) && !((selection instanceof MyPoint2D) && ((MyPoint2D.Double)selection).isText()))
                            {
                                int color = ((Contourable) current_selection).getDrawColor();
                                contourColor.setColor(color);
                                updateDrawColor(((Contourable) current_selection).getDrawOpacity());
                                this.setROIColor(color);
                                CommonClassesLight.enableOrDisableAnyComponent(true, jLabel2, contourColor);
                            }
                            else
                            isDrawContour = false;
                        }
                        if (!isDrawContour)
                        {
                            CommonClassesLight.enableOrDisableAnyComponent(false, jLabel2, contourColor);
                            updateDrawColor(0);
                        }
                        if (selection instanceof Fillable)
                        {
                            fillOpacitySpinner.setValue(((Fillable)selection).getFillOpacity());
                        }
                        if ((selection instanceof Fillable) && !((selection instanceof MyPoint2D) && ((MyPoint2D.Double)selection).isText()))
                        {
                            int color = ((Fillable) current_selection).getFillColor();
                            fillColor.setColor(color);
                            updateFillColor(((Fillable) current_selection).getFillOpacity());
                            this.setROIFillColor(color);
                            CommonClassesLight.enableOrDisableAnyComponent(true, jLabel12);
                        }else
                        {
                            CommonClassesLight.enableOrDisableAnyComponent(false, jLabel12);
                            updateFillColor(0);
                        }
                        this.setStroke_size(stroke);
                        if (selection instanceof MyLine2D) {
                            jSpinner2.setValue(((MyLine2D) selection).getArrowHeadWidth());
                            this.setArrowheadWidth(((MyLine2D) selection).getArrowHeadWidth());
                            jSpinner8.setValue(((MyLine2D) selection).getArrowHeadHeight());
                            this.setArrowheadHeight(((MyLine2D) selection).getArrowHeadHeight());
                            jSpinner3.setValue(((MyLine2D) selection).getBracketLength());
                            this.setBracketSize(((MyLine2D) selection).getBracketLength());
                            jComboBox2.setSelectedIndex(((MyLine2D) selection).getARROW_HEAD_TYPE());
                            this.setARROWHEAD_TYPE(((MyLine2D) selection).getARROW_HEAD_TYPE());
                            jComboBox4.setSelectedIndex(((MyLine2D) selection).getFILLING());
                            this.setFILLING(((MyLine2D) selection).getFILLING());
                        }
                        if (selection instanceof LineStrokable) {
                            int dotSize = ((LineStrokable) selection).getDotSize();
                            if (dotSize != 0) {
                                jSpinner4.setValue(dotSize);
                            } else {
                                jSpinner4.setValue(1);
                                ((LineStrokable) selection).setDotSize(1);
                            }
                            int dashSize = ((LineStrokable) selection).getDashSize();
                            if (dashSize != 0) {
                                jSpinner5.setValue(dashSize);
                            } else {
                                jSpinner5.setValue(3);
                                ((LineStrokable) selection).setDashSize(3);
                            }
                            int skipSize = ((LineStrokable) selection).getSkipSize();
                            if (skipSize != 0) {
                                jSpinner6.setValue(skipSize);
                            } else {
                                jSpinner6.setValue(3);
                                ((LineStrokable) selection).setSkipSize(3);
                            }
                            jComboBox1.setSelectedIndex(((LineStrokable) selection).getLineStrokeType());
                        }
                        if (selection instanceof Transformable)
                        {
                            if (((Transformable)selection).isRotable())
                            {
                                jSpinner7.setModel(new SpinnerNumberModel(((Transformable)selection).getRotation(),0.,359.,1.));
                            }else
                            {
                                jSpinner7.setModel(new SpinnerNumberModel(0.,0.,0.,0.));
                            }
                        }

                    }else
                    {
                        jList1.setSelectedIndex(-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    loading = false;
                }
            }
        };
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        zoomplus = new javax.swing.JButton();
        zoomminus = new javax.swing.JButton();
        one2one = new javax.swing.JButton();
        auto = new javax.swing.JButton();
        send2back = new javax.swing.JButton();
        bring2front = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        rectangle = new javax.swing.JButton();
        circle = new javax.swing.JButton();
        arrow = new javax.swing.JButton();
        accolade = new javax.swing.JButton();
        text = new javax.swing.JButton();
        line = new javax.swing.JButton();
        freehand = new javax.swing.JButton();
        square = new javax.swing.JButton();
        polyline = new javax.swing.JButton();
        Ellipse = new javax.swing.JButton();
        polygon = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        contourColor = new Commons.PaintedButton();
        delete = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        toImage = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        fillColor = new Commons.PaintedButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        drawOpacitySpinner = new javax.swing.JSpinner();
        fillOpacitySpinner = new javax.swing.JSpinner();
        toCrop = new javax.swing.JButton();
        changeWidthHeight = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jSpinner4 = new javax.swing.JSpinner();
        jSpinner5 = new javax.swing.JSpinner();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        erode = new javax.swing.JButton();
        dilate = new javax.swing.JButton();
        reCenter = new javax.swing.JButton();
        delete2 = new javax.swing.JButton();

        javax.swing.GroupLayout rOIpanelLight1Layout = new javax.swing.GroupLayout(rOIpanelLight1);
        rOIpanelLight1.setLayout(rOIpanelLight1Layout);
        rOIpanelLight1Layout.setHorizontalGroup(
            rOIpanelLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        rOIpanelLight1Layout.setVerticalGroup(
            rOIpanelLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 997, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(rOIpanelLight1);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("ROI panel tools"));

        zoomplus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/zoom in.png"))); // NOI18N
        zoomplus.setToolTipText("Zoom +");
        zoomplus.setMaximumSize(new java.awt.Dimension(48, 36));
        zoomplus.setMinimumSize(new java.awt.Dimension(48, 36));
        zoomplus.setPreferredSize(new java.awt.Dimension(48, 36));
        zoomplus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        zoomminus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/zoom out.png"))); // NOI18N
        zoomminus.setToolTipText("Zoom -");
        zoomminus.setMaximumSize(new java.awt.Dimension(48, 36));
        zoomminus.setMinimumSize(new java.awt.Dimension(48, 36));
        zoomminus.setPreferredSize(new java.awt.Dimension(48, 36));
        zoomminus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        one2one.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/1in1.png"))); // NOI18N
        one2one.setToolTipText("1:1");
        one2one.setMaximumSize(new java.awt.Dimension(48, 36));
        one2one.setMinimumSize(new java.awt.Dimension(48, 36));
        one2one.setPreferredSize(new java.awt.Dimension(48, 36));
        one2one.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/fit_2_screen.gif"))); // NOI18N
        auto.setToolTipText("Fit To Screen");
        auto.setMaximumSize(new java.awt.Dimension(48, 36));
        auto.setMinimumSize(new java.awt.Dimension(48, 36));
        auto.setPreferredSize(new java.awt.Dimension(48, 36));
        auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        send2back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/send_to_back.png"))); // NOI18N
        send2back.setToolTipText("send to back");
        send2back.setMaximumSize(new java.awt.Dimension(48, 36));
        send2back.setMinimumSize(new java.awt.Dimension(48, 36));
        send2back.setPreferredSize(new java.awt.Dimension(48, 36));
        send2back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        bring2front.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bring_to_front.png"))); // NOI18N
        bring2front.setToolTipText("Bring to front");
        bring2front.setMaximumSize(new java.awt.Dimension(48, 36));
        bring2front.setMinimumSize(new java.awt.Dimension(48, 36));
        bring2front.setPreferredSize(new java.awt.Dimension(48, 36));
        bring2front.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(zoomplus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zoomminus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(one2one, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(auto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(send2back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bring2front, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bring2front, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zoomplus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zoomminus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(one2one, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(auto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(send2back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Shapes"));

        rectangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rectangle.png"))); // NOI18N
        rectangle.setToolTipText("Rectangle");
        rectangle.setMaximumSize(new java.awt.Dimension(48, 30));
        rectangle.setMinimumSize(new java.awt.Dimension(48, 30));
        rectangle.setPreferredSize(new java.awt.Dimension(48, 30));
        rectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        circle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/circle.png"))); // NOI18N
        circle.setToolTipText("circle");
        circle.setMaximumSize(new java.awt.Dimension(48, 30));
        circle.setMinimumSize(new java.awt.Dimension(48, 30));
        circle.setPreferredSize(new java.awt.Dimension(48, 30));
        circle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        arrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/arrow.png"))); // NOI18N
        arrow.setToolTipText("arrow");
        arrow.setMaximumSize(new java.awt.Dimension(48, 30));
        arrow.setMinimumSize(new java.awt.Dimension(48, 30));
        arrow.setPreferredSize(new java.awt.Dimension(48, 30));
        arrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        accolade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/accolade.png"))); // NOI18N
        accolade.setToolTipText("accolade");
        accolade.setMaximumSize(new java.awt.Dimension(48, 30));
        accolade.setMinimumSize(new java.awt.Dimension(48, 30));
        accolade.setPreferredSize(new java.awt.Dimension(48, 30));
        accolade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        text.setText("txt");
        text.setToolTipText("add text");
        text.setMaximumSize(new java.awt.Dimension(48, 30));
        text.setMinimumSize(new java.awt.Dimension(48, 30));
        text.setPreferredSize(new java.awt.Dimension(48, 30));
        text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        line.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/line.png"))); // NOI18N
        line.setToolTipText("Line");
        line.setMaximumSize(new java.awt.Dimension(48, 30));
        line.setMinimumSize(new java.awt.Dimension(48, 30));
        line.setPreferredSize(new java.awt.Dimension(48, 30));
        line.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        freehand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/freehand.png"))); // NOI18N
        freehand.setToolTipText("FREEHAND");
        freehand.setMaximumSize(new java.awt.Dimension(48, 30));
        freehand.setMinimumSize(new java.awt.Dimension(48, 30));
        freehand.setPreferredSize(new java.awt.Dimension(48, 30));
        freehand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        square.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/square.png"))); // NOI18N
        square.setToolTipText("square");
        square.setMaximumSize(new java.awt.Dimension(48, 30));
        square.setMinimumSize(new java.awt.Dimension(48, 30));
        square.setPreferredSize(new java.awt.Dimension(48, 30));
        square.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        polyline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/line_brisee.png"))); // NOI18N
        polyline.setToolTipText("POLYLINE");
        polyline.setMaximumSize(new java.awt.Dimension(48, 30));
        polyline.setMinimumSize(new java.awt.Dimension(48, 30));
        polyline.setPreferredSize(new java.awt.Dimension(48, 30));
        polyline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        Ellipse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ellipse.png"))); // NOI18N
        Ellipse.setToolTipText("Ellipse");
        Ellipse.setMaximumSize(new java.awt.Dimension(48, 30));
        Ellipse.setMinimumSize(new java.awt.Dimension(48, 30));
        Ellipse.setPreferredSize(new java.awt.Dimension(48, 30));
        Ellipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        polygon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/polygon.png"))); // NOI18N
        polygon.setToolTipText("POLYGON");
        polygon.setMaximumSize(new java.awt.Dimension(48, 30));
        polygon.setMinimumSize(new java.awt.Dimension(48, 30));
        polygon.setPreferredSize(new java.awt.Dimension(48, 30));
        polygon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        Edit.setText("Edit/Finalise selected ROI");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        jLabel2.setText("Contour:");

        contourColor.setBackground(new java.awt.Color(255, 255, 0));
        contourColor.setForeground(new java.awt.Color(0, 0, 255));
        contourColor.setText(" ");
        contourColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        delete.setToolTipText("Delete ROI");
        delete.setMaximumSize(new java.awt.Dimension(48, 30));
        delete.setMinimumSize(new java.awt.Dimension(48, 30));
        delete.setPreferredSize(new java.awt.Dimension(48, 30));
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        jLabel9.setText("Rotation :");

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 359.0d, 1.0d));
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rotationChanged(evt);
            }
        });

        toImage.setText("ROI --> inset/PIP");
        toImage.setToolTipText("COnvert a rectangular ROI to an image inset");
        toImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        jLabel12.setText("Fill:");

        fillColor.setBackground(new java.awt.Color(255, 255, 0));
        fillColor.setForeground(new java.awt.Color(0, 0, 255));
        fillColor.setText(" ");
        fillColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        jLabel14.setText("color");

        jLabel15.setText("Contour:");

        jLabel16.setText("Fill:");

        jLabel17.setText("Opacity");

        drawOpacitySpinner.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.05f)));
        drawOpacitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                drawOpacitySpinnerStateChanged(evt);
            }
        });

        fillOpacitySpinner.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.05f)));
        fillOpacitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                drawOpacitySpinnerStateChanged(evt);
            }
        });

        toCrop.setText("ROI --> Crop(s)");
        toCrop.setToolTipText("COnvert a rectangular ROI to an image inset");
        toCrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        changeWidthHeight.setText("width/height");
        changeWidthHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSpinner7))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(contourColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fillColor, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel14))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(toImage, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(toCrop, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(freehand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(polygon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(square, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(accolade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(rectangle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(circle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(arrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(changeWidthHeight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(polyline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(Ellipse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drawOpacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fillOpacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rectangle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(circle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ellipse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(square, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accolade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freehand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(polygon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(polyline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeWidthHeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(fillColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(contourColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(drawOpacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(fillOpacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toImage)
                    .addComponent(toCrop)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Edit, Ellipse, accolade, arrow, circle, delete, freehand, line, polygon, polyline, rectangle, square, text});

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Line stroke parameters"));

        jLabel5.setText("Type:");

        jLabel6.setText("Dash:");

        jLabel7.setText("Gap:");

        jLabel8.setText("Dot:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Plain", "Dashed", "Dotted", "DashDot" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LineStrokeChanged(evt);
            }
        });

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dotSizeChanged(evt);
            }
        });

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(6), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dashSizeChanged(evt);
            }
        });

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(6), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                skipSizeChanged(evt);
            }
        });

        jLabel1.setText("Size:");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.65f), Float.valueOf(0.01f), null, Float.valueOf(0.25f)));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1strokeChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(1, 1, 1)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(15, 15, 15)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Arrow head parameters"));

        jLabel3.setText("Width:");

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(15.0d), Double.valueOf(1.0d), null, Double.valueOf(1.0d)));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                arrowHeadWidthChanged(evt);
            }
        });

        jLabel10.setText("Height:");

        jSpinner8.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(30.0d), Double.valueOf(1.0d), null, Double.valueOf(1.0d)));
        jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                arrowHeadHeightOrLengthChanged(evt);
            }
        });

        jLabel11.setText("Type:");

        jComboBox2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowHeadTypeChanged(evt);
            }
        });

        jLabel13.setText("Filling:");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filled", "Outline", "Fancy" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowHeadFillingChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinner2))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinner8)
                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBox2, jComboBox4});

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Bracket parameters"));

        jLabel4.setText("Bracket Width:");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(6.0d), Double.valueOf(1.0d), null, Double.valueOf(1.0d)));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3arrowheadSizeChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner3)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("ROI Tools", jPanel6);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ROISelChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        erode.setText("erode");
        erode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        dilate.setText("dilate");
        dilate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        reCenter.setText("Move sel to upper left corner");
        reCenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        delete2.setText("del");
        delete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectanglerunAll(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(erode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dilate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reCenter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delete2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(erode)
                    .addComponent(dilate)
                    .addComponent(reCenter)
                    .addComponent(delete2)))
        );

        jTabbedPane1.addTab("Advanced Options", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rectanglerunAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectanglerunAll
        Object src = null;
        if (evt != null) {
            src = evt.getSource();
        }
        if (src == changeWidthHeight) {
            Object raw = rOIpanelLight1.getSelectedShape();
            if (raw == null || !(raw instanceof MyRectangle2D)) {
                CommonClassesLight.Warning(this, "Please select or draw a rectangular ROI first");
                return;
            } else {
                ChangeRectangleROIWidthOrJHeight iopane = new ChangeRectangleROIWidthOrJHeight( (int)((MyRectangle2D) raw).rec2d.width, (int)((MyRectangle2D) raw).rec2d.height);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Edit Rectangle Width or Height", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    ((MyRectangle2D) raw).rec2d.width = iopane.getRectWidth();
                    ((MyRectangle2D) raw).rec2d.height = iopane.getRectHeight();
                     rOIpanelLight1.repaint();
                }
            }
        }
        if (src == erode) {
            rOIpanelLight1.erode(1);
        }
        if (src == dilate) {
            rOIpanelLight1.dilate(1);
        }
        if (src == reCenter) {
            try {
                if (img != null) {
                    rOIpanelLight1.setFirstCorner(0, 0);
                }
            } catch (Exception e) {
            }
        }
        if (src == toCrop) {
            /**
             * add current ROI as a crop (quick n dirty needs to be recoded)
             */
            Object raw = rOIpanelLight1.getSelectedShape();
            if (raw == null || !(raw instanceof MyRectangle2D)) {
                CommonClassesLight.Warning(this, "Please select or draw a rectangular ROI first");
                return;
            }
            Object ROI = ((PARoi) raw).getParentInstance();
            Rectangle2D.Double r = ((Rectangle2D.Double) ROI);
            double fullRotation = (img.getTheta() - ((PARoi) rOIpanelLight1.getSelectedShape()).getRotation());
            MyRectangle2D.Double shiftedBack = new MyRectangle2D.Double(r.x + img.getLeft_crop(), r.y + img.getUp_crop(), r.width, r.height);
            int origWidth = img.getOriginalDisplay().getWidth();
            int origHeight = img.getOriginalDisplay().getHeight();
            AffineTransform at2 = new AffineTransform();
            at2.rotate(-Math.toRadians(img.getTheta()), img.getLeft_crop() + (origWidth - img.getLeft_crop() - img.getRight_crop()) / 2, img.getUp_crop() + (origHeight - img.getUp_crop() - img.getDown_crop()) / 2); //--> ca marche tant qu'il y a pas de displacement
            AffineTransform at3 = new AffineTransform();
            Shape shiftedBack2 = at2.createTransformedShape(shiftedBack.rec2d);
            at3.rotate(Math.toRadians(((PARoi) rOIpanelLight1.getSelectedShape()).getRotation()), shiftedBack2.getBounds2D().getCenterX(), shiftedBack2.getBounds2D().getCenterY());
            Shape shiftedBack3 = at3.createTransformedShape(shiftedBack2);
            Dimension rotated = ImageTransformations.get_size_of_the_rotated_image(origWidth, origHeight, fullRotation);
            Point2D.Double trans = new Point2D.Double((rotated.getWidth() - origWidth) / 2., (rotated.getHeight() - origHeight) / 2.);
            MyRectangle2D.Double rect = new MyRectangle2D.Double(r);
            rect.setCenter(new Point2D.Double(shiftedBack3.getBounds2D().getCenterX(), shiftedBack3.getBounds2D().getCenterY()));
            rect.setDrawColor(0x0000FF);
            rect.rotate(-(img.getTheta() - ((PARoi) rOIpanelLight1.getSelectedShape()).getRotation()));
            AffineTransform at5 = new AffineTransform();
            at5.rotate(Math.toRadians(fullRotation), origWidth / 2, origHeight / 2);
            Shape finalShape = at5.createTransformedShape(shiftedBack3);
            rect.rotate(0);
            rect.setCenter(new Point2D.Double(finalShape.getBounds2D().getCenterX() + trans.x, finalShape.getBounds2D().getCenterY() + trans.y));
            AffineTransform at6 = new AffineTransform();
            at6.rotate(Math.toRadians(-fullRotation), rotated.getWidth() / 2, rotated.getHeight() / 2);
            MyRectangle2D.Double originalRect = new MyRectangle2D.Double(rect);
            finalShape = at6.createTransformedShape(originalRect.rec2d);
            originalRect.setCenter(new Point2D.Double(finalShape.getBounds2D().getCenterX() - trans.x, finalShape.getBounds2D().getCenterY() - trans.y));
            originalRect.rotate(-fullRotation);
            MyImage2D.Double newCrop = new MyImage2D.Double(0, 0, img.getOriginalDisplay());
            newCrop.crop((int) originalRect.getX(), (int) ((origWidth - (int) originalRect.getX() - r.getWidth())), (int) originalRect.getY(), ((int) (origHeight - (int) originalRect.getY() - r.getHeight())));
            newCrop.rotate((fullRotation < 0 ? 360 + fullRotation : fullRotation));
            newCrop.setFullName(img.getFullName());
            newCrop.setShortName(img.getShortName() + "-crop-" + CommonClassesLight.create_number_of_the_appropriate_size(oneLinerCrops.size() + 1, 2));
            newCrop.setSize_of_one_px_in_unit(img.getScale_bar_size_in_unit());
            oneLinerCrops.add(newCrop);
            CommonClassesLight.showMessage(this, "Congratulations a crop corresponding to the selected ROI has been succesfully to the panels array of ScientiFig.");
        }
        if (src == toImage) {
            /**
             * converts ROI to inset (quick n dirty needs to be recoded)
             */
            Object raw = rOIpanelLight1.getSelectedShape();
            if (raw == null || !(raw instanceof MyRectangle2D)) {
                CommonClassesLight.Warning(this, "Please select or draw a rectangular ROI first");
                inset = null;
                return;
            }
            Object ROI = ((PARoi) raw).getParentInstance();
            Rectangle2D.Double r = ((Rectangle2D.Double) ROI);
            double fullRotation = (img.getTheta() - ((PARoi) rOIpanelLight1.getSelectedShape()).getRotation());
            MyRectangle2D.Double shiftedBack = new MyRectangle2D.Double(r.x + img.getLeft_crop(), r.y + img.getUp_crop(), r.width, r.height);
            BufferedImage orig = CommonClassesLight.copyImg(img.getOriginalDisplay());
            AffineTransform at2 = new AffineTransform();
            at2.rotate(-Math.toRadians(img.getTheta()), img.getLeft_crop() + (orig.getWidth() - img.getLeft_crop() - img.getRight_crop()) / 2, img.getUp_crop() + (orig.getHeight() - img.getUp_crop() - img.getDown_crop()) / 2); //--> ca marche tant qu'il y a pas de displacement
            AffineTransform at3 = new AffineTransform();
            Shape shiftedBack2 = at2.createTransformedShape(shiftedBack.rec2d);
            at3.rotate(Math.toRadians(((PARoi) rOIpanelLight1.getSelectedShape()).getRotation()), shiftedBack2.getBounds2D().getCenterX(), shiftedBack2.getBounds2D().getCenterY());
            Shape shiftedBack3 = at3.createTransformedShape(shiftedBack2);
            BufferedImage rotated = new ImageTransformations().rotate(orig, fullRotation, ImageTransformations.BICUBIC, true);
            Point2D.Double trans = new Point2D.Double((rotated.getWidth() - orig.getWidth()) / 2., (rotated.getHeight() - orig.getHeight()) / 2.);
            MyRectangle2D.Double rect = new MyRectangle2D.Double(r);
            rect.setCenter(new Point2D.Double(shiftedBack3.getBounds2D().getCenterX(), shiftedBack3.getBounds2D().getCenterY()));
            rect.setDrawColor(0x0000FF);
            rect.rotate(-(img.getTheta() - ((PARoi) rOIpanelLight1.getSelectedShape()).getRotation()));
            BufferedImage crop = new BufferedImage((int) r.getWidth(), (int) r.getHeight(), BufferedImage.TYPE_INT_RGB);
            AffineTransform at5 = new AffineTransform();
            at5.rotate(Math.toRadians(fullRotation), orig.getWidth() / 2, orig.getHeight() / 2);
            Shape finalShape = at5.createTransformedShape(shiftedBack3);
            rect.rotate(0);
            rect.setCenter(new Point2D.Double(finalShape.getBounds2D().getCenterX() + trans.x, finalShape.getBounds2D().getCenterY() + trans.y));
            rect.setDrawColor(0xFF00FF);
            int width = crop.getWidth();
            int height = crop.getHeight();
            int driftX = (int) rect.getX();
            int driftY = (int) rect.getY();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    try {
                        crop.setRGB(i, j, rotated.getRGB(driftX + i, driftY + j));
                    } catch (Exception e) {
                    }
                }
            }
            inset = new SerializableBufferedImage2(crop);
            CommonClassesLight.showMessage(this, "Congratulations an inset corresponding to the selected ROI has been succesfully added to the picture.");
        }
        if (src == delete || src == delete2) {
            preventROIselChanged = true;
            jList1.clearSelection();
            rOIpanelLight1.deleteSelectedShape();
            preventROIselChanged = false;
        }
        if (src == rectangle) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.RECTANGLE);
        }
        if (src == circle) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.CIRCLE);
        }
        if (src == line) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.LINE);
        }
        if (src == arrow) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.ARROW);
        }
        if (src == square) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.SQUARE);
        }
        if (src == polygon) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.POLYGON);
        }
        if (src == polyline) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.POLYLINE);
        }
        if (src == freehand) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.FREEHAND);
        }
        if (src == Edit) {
            if (rOIpanelLight1.isEditMode()) {
                rOIpanelLight1.setEditMode(ROIpanelLight.DEFAULT_MODE);
            } else {
                try {
                    if (rOIpanelLight1.getSelectedShape() instanceof MyImage2D) {
                        rOIpanelLight1.repaint();
                    } else if ((rOIpanelLight1.getSelectedShape() instanceof MyPoint2D) && ((MyPoint2D.Double) rOIpanelLight1.getSelectedShape()).getText() != null) {
                        /*
                         * the shape is a text we allow the text to be edited
                         */
                        ColoredTextPane iopane = new ColoredTextPane(((MyPoint2D.Double) rOIpanelLight1.getSelectedShape()).getText());
                        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Please enter your text here", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            ((MyPoint2D.Double) rOIpanelLight1.getSelectedShape()).setText(iopane.ctps);
                            rOIpanelLight1.repaint();
                        }
                    } else if ((rOIpanelLight1.getSelectedShape() instanceof MyPoint2D)) {
                        /*
                         * the shape is a point there is nothing to edit so we don't do anything
                         * will also work for asterisks
                         */
                        return;
                    } else {
                        rOIpanelLight1.setEditMode(ROIpanelLight.EDIT_MODE);
                    }
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            }

            if (rOIpanelLight1.isEditMode()) {
                /*
                 * we disable buttons
                 */
                CommonClassesLight.enableOrDisablePanelComponents(jPanel1, false);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel2, false);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel3, false);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel4, false);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel5, false);
                Edit.setForeground(Color.red);
                Edit.setEnabled(true);
            } else {
                /*
                 * we enable buttons back
                 */
                Edit.setForeground(Color.black);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel1, true);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel2, true);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel3, true);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel4, true);
                CommonClassesLight.enableOrDisablePanelComponents(jPanel5, true);
            }
        }
        if (src == text) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.STRING);
        }
        if (src == Ellipse) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.ELLIPSE);
        }
        if (src == accolade) {
            rOIpanelLight1.setDrawingPrimitive(ROIpanelLight.BRACKET);
        }
        if (src == drawOpacitySpinner) {
            try {
                drawOpacitySpinner.commitEdit();
                float opacity = (Float) drawOpacitySpinner.getValue();
                if (rOIpanelLight1.changeSelDrawOpacity(opacity)) {
                    updateDrawColor(opacity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (src == fillOpacitySpinner) {
            try {
                fillOpacitySpinner.commitEdit();
                float opacity = (Float) fillOpacitySpinner.getValue();
                if (rOIpanelLight1.changeSelFillOpacity(opacity)) {
                    updateFillColor(opacity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (src == contourColor) {
            Color color = JColorChooser.showDialog(this, "Pick an outline color", contourColor.getBackground());
            if (color != null) {
                contourColor.setColor(color);
                if (rOIpanelLight1.getSelectedShape() != null) {
                    int col = contourColor.getColor();
                    rOIpanelLight1.recolorSel(col);
                    rOIpanelLight1.setROIColor(col);
                }
            }
        }
        if (src == fillColor) {
            Color color = JColorChooser.showDialog(this, "Pick a filling color", fillColor.getBackground());
            if (color != null) {
                fillColor.setColor(color);
                if (rOIpanelLight1.getSelectedShape() != null) {
                    int col = fillColor.getColor();
                    rOIpanelLight1.recolorFillSel(col);
                    rOIpanelLight1.setROIFillColor(col);
                }
            }
        }
        if (src == zoomplus) {
            zoom += 0.1;
            rOIpanelLight1.setZoom(zoom);
        }
        if (src == send2back) {
            rOIpanelLight1.sendToBack();
        }
        if (src == bring2front) {
            rOIpanelLight1.bringToFront();
        }
        if (src == zoomminus) {
            zoom -= 0.1;
            rOIpanelLight1.setZoom(zoom);
        }
        if (src == one2one) {
            zoom = 1.;
            rOIpanelLight1.setZoom(zoom);
        }
        if (src == auto) {
            double max_width = jScrollPane1.getViewportBorderBounds().width;
            double max_height = jScrollPane1.getViewportBorderBounds().height;
            max_width -= 35;
            max_height -= 35;
            double cur_width = rOIpanelLight1.backgroundImage.getWidth();
            double cur_height = rOIpanelLight1.backgroundImage.getHeight();
            if (cur_width != 0 && cur_height != 0) {
                double ratio_w = (double) max_width / (double) cur_width;
                double ratio_h = (double) max_height / (double) cur_height;
                double best_fit_ratio = Math.min(ratio_h, ratio_w);
                if (best_fit_ratio == ratio_w) {
                    rOIpanelLight1.setZoom(ratio_w);
                } else {
                    rOIpanelLight1.setZoom(ratio_h);
                }
            }
            rOIpanelLight1.repaint();
        }
    }//GEN-LAST:event_rectanglerunAll

    private void jSpinner1strokeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1strokeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null) {
                float strokeSize = ((Float) jSpinner1.getValue());
                ((PARoi) sel).setStrokeSize(strokeSize);
                rOIpanelLight1.setStroke_size(strokeSize);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_jSpinner1strokeChanged

    private void jSpinner3arrowheadSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3arrowheadSizeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof MyLine2D) {
                double size = ((Double) jSpinner3.getValue());
                ((MyLine2D) sel).setBraketSize(size);
                rOIpanelLight1.setBracketSize(size);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_jSpinner3arrowheadSizeChanged

    private void dotSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dotSizeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof LineStrokable) {
                ((LineStrokable) sel).setDotSize(((Integer) jSpinner4.getValue()));
                rOIpanelLight1.setDotSize(((Integer) jSpinner4.getValue()));
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_dotSizeChanged

    private void dashSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dashSizeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof LineStrokable) {
                ((LineStrokable) sel).setDashSize(((Integer) jSpinner5.getValue()));
                rOIpanelLight1.setDashSize(((Integer) jSpinner5.getValue()));
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_dashSizeChanged

    private void skipSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_skipSizeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof LineStrokable) {
                ((LineStrokable) sel).setSkipSize(((Integer) jSpinner6.getValue()));
                rOIpanelLight1.setSkipSize(((Integer) jSpinner6.getValue()));
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_skipSizeChanged

    private void LineStrokeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LineStrokeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof LineStrokable) {
                ((LineStrokable) sel).setLineStrokeType(jComboBox1.getSelectedIndex());
                rOIpanelLight1.setLineStroke(jComboBox1.getSelectedIndex());
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_LineStrokeChanged

    private void rotationChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rotationChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof Transformable) {
                ((Transformable) sel).rotate(((Double) jSpinner7.getValue()));
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_rotationChanged

    private void arrowHeadWidthChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_arrowHeadWidthChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof MyLine2D) {
                double width = ((Double) jSpinner2.getValue());
                ((MyLine2D) sel).setArrowHeadWidth(width);
                rOIpanelLight1.setArrowheadWidth(width);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_arrowHeadWidthChanged

    private void arrowHeadHeightOrLengthChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_arrowHeadHeightOrLengthChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof MyLine2D) {
                double height = ((Double) jSpinner8.getValue());
                ((MyLine2D) sel).setArrowHeadHeight(height);
                rOIpanelLight1.setArrowheadHeight(height);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_arrowHeadHeightOrLengthChanged

    private void arrowHeadFillingChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowHeadFillingChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof MyLine2D) {
                int filling = jComboBox4.getSelectedIndex();
                ((MyLine2D) sel).setFILLING(filling);
                rOIpanelLight1.setFILLING(filling);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_arrowHeadFillingChanged

    private void arrowHeadTypeChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowHeadTypeChanged
        if (!loading) {
            Object sel = rOIpanelLight1.getSelectedShape();
            if (sel != null && sel instanceof MyLine2D) {
                int type = jComboBox2.getSelectedIndex();
                ((MyLine2D) sel).setARROW_HEAD_TYPE(type);
                rOIpanelLight1.setARROWHEAD_TYPE(type);
                rOIpanelLight1.repaint();
            }
        }
    }//GEN-LAST:event_arrowHeadTypeChanged

    private void drawOpacitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_drawOpacitySpinnerStateChanged
        if (!loading) {
            rectanglerunAll(new ActionEvent(evt.getSource(), 0, ""));
        }
    }//GEN-LAST:event_drawOpacitySpinnerStateChanged

    private void tabChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabChanged
        if (jTabbedPane1.getSelectedIndex() == 1) {
            updateListContent();
        }
    }//GEN-LAST:event_tabChanged

    private void ROISelChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ROISelChanged
        if (!preventROIselChanged && evt != null && !evt.getValueIsAdjusting()) {
            try {
                int[] idx = jList1.getSelectedIndices();
                ArrayList<Object> sel = new ArrayList<Object>();
                for (int i : idx) {
                    sel.add(ROIListModel.elementAt(i));
                }
                if (!sel.isEmpty()) {
                    if (sel.size() > 1) {
                        ComplexShapeLight csl = new ComplexShapeLight(sel);
                        rOIpanelLight1.setSelectedShape(csl);
                    } else {
                        rOIpanelLight1.setSelectedShape(sel.get(0));
                    }
                    rOIpanelLight1.repaint();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_ROISelChanged

    public boolean hasInset() {
        return inset != null;
    }

    public boolean hasCrops() {
        return !oneLinerCrops.isEmpty();
    }

    public ArrayList<Object> getCrops() {
        return oneLinerCrops;
    }

    public SerializableBufferedImage2 getInset() {
        return inset;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MyImage2D.Double tmp = new MyImage2D.Double(0, 0, new Loader().load("/E/sample_images_PA/trash_test_mem/egg_chambers/Series010.png"));
//        System.out.println(new Loader().load("C:/Users/aigouy/Desktop/sample_images_PA/trash_test_mem/mini/focused_Series012.png").getHeight());
        tmp.crop(64, 128, 64, 128);
        tmp.rotate(45);
        System.out.println("image rotation " + tmp.getTheta()); //--> recup l'angle de l'image
        //tmp.rotate(45);

        MyRectangle2D r2d = new MyRectangle2D.Double(128, 128, 256, 128);
        r2d.setDrawColor(0xFF0000);
        r2d.setFillColor(0x00FF00);
        r2d.setFillOpacity(0.3f);
        r2d.setDrawOpacity(0.6f);
        tmp.addAssociatedObject(r2d);
        //--> get a ROI of the image ??? or make a crop of it with a rotation ??? --> TODO
        //--> a tester
        //--> si ROI --> recup image

        MyEllipse2D.Double el2d = new MyEllipse2D.Double(128, 256, 128, 256);
        el2d.setDrawColor(0xFF00FF);
        el2d.setFillColor(0x0FF0F0);
        tmp.addAssociatedObject(el2d);

        MyPolygon2D.Double p2d = new MyPolygon2D.Double(10, 10, 100, 100, 10, 100, 100, 9);
        p2d.translate(128, 128);
        p2d.setDrawColor(0x00FFFF);
        p2d.setFillColor(0xFF0FF0);
        tmp.addAssociatedObject(p2d);

        MyLine2D.Double l2d = new MyLine2D.Double(0, 0, 256, 128);
        l2d.setDrawColor(0x0000FF);
        tmp.addAssociatedObject(l2d);
        AnnotateCurrentImage fp = new AnnotateCurrentImage(tmp, null);
        int result = JOptionPane.showOptionDialog(null, new Object[]{fp}, "Add Extra Objects To The Image", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            //
        }
        System.exit(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Edit;
    private javax.swing.JButton Ellipse;
    private javax.swing.JButton accolade;
    private javax.swing.JButton arrow;
    private javax.swing.JButton auto;
    private javax.swing.JButton bring2front;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton changeWidthHeight;
    private javax.swing.JButton circle;
    private Commons.PaintedButton contourColor;
    private javax.swing.JButton delete;
    private javax.swing.JButton delete2;
    private javax.swing.JButton dilate;
    private javax.swing.JSpinner drawOpacitySpinner;
    private javax.swing.JButton erode;
    private Commons.PaintedButton fillColor;
    private javax.swing.JSpinner fillOpacitySpinner;
    private javax.swing.JButton freehand;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton line;
    private javax.swing.JButton one2one;
    private javax.swing.JButton polygon;
    private javax.swing.JButton polyline;
    private Dialogs.ROIpanelLight rOIpanelLight1;
    private javax.swing.JButton reCenter;
    private javax.swing.JButton rectangle;
    private javax.swing.JButton send2back;
    private javax.swing.JButton square;
    private javax.swing.JButton text;
    private javax.swing.JButton toCrop;
    private javax.swing.JButton toImage;
    private javax.swing.JButton zoomminus;
    private javax.swing.JButton zoomplus;
    // End of variables declaration//GEN-END:variables
}

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

import Tools.Converter;
import MyShapes.JournalParameters;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * This class contains a layered pane showing your figure. <br>-The first layer
 * contains rectangles displaying the size of one column, 1.5 columns or 2
 * columns of the journal the user selected <br>-The second layer is the
 * vectorial layer, that contains images, graphs, ... (this layer is mouse
 * sensitive)
 *
 * @author Benoit Aigouy
 */
public class DoubleLayerPane extends javax.swing.JLayeredPane {

    private static boolean ruler = true;
    public static double pageWidth = 21;
    public static double colWidth = 0;
    public static double oneAndHalfColWidth = 0;
    public static double pageHeight = 29.7;
    public static double dpi = 72;
    private int curWidth = 0;
    private int curHeight = 0;
    public ROIpanelLight ROIS;
    public double zoom = 1.d;
    boolean allowRefresh = true;

    /**
     * Constructor
     *
     * @param ROI various shapes the vectorial panel should contain
     */
    public DoubleLayerPane(ROIpanelLight ROI) {
        this.ROIS = ROI;
        initComponents();
        initLayers();
    }

    /**
     * Creates new form DoubleLayerPane
     */
    public DoubleLayerPane() {
        ROIS = new ROIpanelLight();
        initComponents();
        initLayers();
    }

    /**
     *
     * @return true if display refresh is allowed
     */
    public boolean isAllowRefresh() {
        return allowRefresh;
    }

    /**
     * Sets wether display refresh is allowed
     *
     * @param allowRefresh
     */
    public void setAllowRefresh(boolean allowRefresh) {
        ROIS.setAllowRefresh(allowRefresh);
        this.allowRefresh = allowRefresh;
    }

    /**
     * Sets the zoom
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        ROIS.setZoom(zoom);
        resizePanel();
        repaint();
    }

    /**
     * Defines whether the ROI contained should be selectable or not
     *
     * @param boobool
     */
    public void setSelectable(boolean boobool) {
        ROIS.setSelectable(boobool);
    }
    /**
     * this panel just draws journal page size limitations
     */
    public JPanel original_pane = new JPanel() {
        @Override
        public void paint(Graphics g) {
            if (!allowRefresh) {
                return;
            }
            super.paint(g);
            if (!ruler) {
                return;
            }
            if (pageWidth == 0 && pageHeight == 0 && colWidth == 0 && oneAndHalfColWidth == 0) {
                return;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            double height_in_px = (Converter.cmToPxAnyDpi(pageHeight, dpi)) * zoom;
            Rectangle2D.Double fullpage = new Rectangle2D.Double(0, 0, (Converter.cmToPxAnyDpi(pageWidth, dpi)) * zoom, height_in_px);
            g2d.draw(fullpage);
            if (colWidth == 0 && oneAndHalfColWidth == 0) {
                return;
            }
            g2d.setColor(Color.ORANGE);
            Rectangle2D.Double oneandhalfcolumn = new Rectangle2D.Double(0, 0, (Converter.cmToPxAnyDpi(oneAndHalfColWidth, dpi)) * zoom, height_in_px);
            g2d.draw(oneandhalfcolumn);
            g2d.setColor(Color.BLUE);
            Rectangle2D.Double column = new Rectangle2D.Double(0, 0, (Converter.cmToPxAnyDpi(colWidth, dpi)) * zoom, height_in_px);
            g2d.draw(column);
        }

        @Override
        public void repaint(Rectangle r) {
            if (!allowRefresh) {
                return;
            }
            super.repaint(r);
        }
    };

    /**
     * Applies journal size restrictions to the vectorial panel
     *
     * @param jp the journal style
     */
    public void setJournalStyle(JournalParameters jp) {
        if (jp == null) {
            resetRuler();
            repaint();
            return;
        }
        pageWidth = jp.getTwoColumnSizee();
        pageHeight = jp.getPageHeight();
        colWidth = jp.getColumnSize();
        oneAndHalfColWidth = jp.getOneAndHalfColumn();
        repaint();
    }

    /**
     * resets page size
     */
    public void resetRuler() {
        pageWidth = 21;
        colWidth = 0;
        oneAndHalfColWidth = 0;
        pageHeight = 29.7;
    }

    /**
     * Calculates best fit zoom from visible area
     *
     * @param max_width width of the visible area
     * @param max_height height of the visible area
     */
    public void best_fit_zoom(int max_width, int max_height) {
        max_width -= 35;
        max_height -= 35;

        if (curWidth != 0 && curHeight != 0) {
            double ratio_w = (double) max_width / (double) curWidth;
            double ratio_h = (double) max_height / (double) curHeight;
            double best_fit_ratio = Math.min(ratio_h, ratio_w);

            if (best_fit_ratio == ratio_w) {
                setZoom(ratio_w);
            } else {
                setZoom(ratio_h);
            }
        }
    }

    /**
     * Defines the size of the panels
     *
     * @param jp panel to resize
     * @param width
     * @param height
     */
    public void resizePanel(Component jp, int width, int height) {
        if (ruler) {
            width = (int) Math.max(width, Converter.cmToPxAnyDpi(pageWidth, dpi)) + 1;
            height = (int) Math.max(height, Converter.cmToPxAnyDpi(pageHeight, dpi)) + 1;
            width++;
            height++;
        }
        this.curWidth = width;
        this.curHeight = height;

        width *= zoom;
        height *= zoom;

        jp.setSize(width, height);
        jp.setPreferredSize(new Dimension(width, height));
        jp.setMinimumSize(new Dimension(width, height));
        jp.setMaximumSize(new Dimension(width, height));
    }

    /**
     * update panel size
     */
    public void resizePanel() {
        resizePanel(curWidth, curHeight);
    }

    /**
     * Defines panels size
     *
     * @param width
     * @param height
     */
    public void resizePanel(int width, int height) {
        resizePanel(this, width, height);
        resizePanel(original_pane, width, height);
        resizePanel(ROIS, width, height);
        ROIS.setROIPanelActive(true);
        ROIS.setDraggable(false);
    }

    /**
     * The doubleLayeredPane contains several layers. Here we set their position
     * and activate the transparency.
     */
    private void initLayers() {
        original_pane.setOpaque(false);
        this.add(ROIS, JLayeredPane.DEFAULT_LAYER);
        this.add(original_pane, JLayeredPane.PALETTE_LAYER);
        resizePanel(1024, 1024);
    }

    /**
     * Defines whether rulers should be visible or not
     *
     * @param ruler
     */
    public void setRuler(boolean ruler) {
        DoubleLayerPane.ruler = ruler;
    }

    /**
     * increment zoom factor by 0.1
     */
    public void incrementZoom() {
        zoom += 0.1d;
        setZoom(zoom);
    }

    /**
     * decrease zoom factor by 0.1
     */
    public void decrementZoom() {
        /*
         * prevent zoom from reaching 0 (it would not make sense
         */
        if (zoom > 0.12d) {
            zoom -= 0.1d;
        }
        setZoom(zoom);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 578, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}



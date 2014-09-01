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

import MyShapes.Drawable;
import MyShapes.MyPoint2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * GlassPane is a glasspane for ScientiFig. I use it to display hints and to
 blink components to drdrawAndFillser's attention.
 *
 * @author Benoit Aigouy
 */
public class GlassPane extends javax.swing.JPanel {

    /*
     * bounding rect for any interface object we want to blink
     */
    Object blink;
    ArrayList<Object> objects_to_draw_in_glasspane = new ArrayList<Object>();

    /**
     * Creates new form GlassPane
     */
    public GlassPane() {
        initComponents();
    }

    /**
     * add drawable objectdrawAndFill draw to the glasspane
     *
     * @param o
     */
    public void addObject(Object o) {
        objects_to_draw_in_glasspane.add(o);
    }

    /**
     * Sets the blink object (usually a timer from another class wil this
     * in orddrawAndFillo draw or not something)
     *
     * @param pos
     */
    public void setBlink(Object pos) {
        this.blink = pos;
    }

    /**
     * remove the blink object
     */
    public void clearBlink() {
        this.blink = null;
    }

    /**
     * remove all drawable objects from the glasspane
     */
    public void clearPane() {
        objects_to_draw_in_glasspane = new ArrayList<Object>();
    }

    /**
     * Sets a series of drawable objects that must be painted on the glasspane
     *
     * @param objects_to_draw_in_glasspane
     */
    public void setObjectsToDraw(ArrayList<Object> objects_to_draw_in_glasspane) {
        this.objects_to_draw_in_glasspane = objects_to_draw_in_glasspane;
    }

    @Override
    public void paint(Graphics g) {
        if (objects_to_draw_in_glasspane == null || objects_to_draw_in_glasspane.isEmpty() && blink == null) {
            /*
             drawAndFillthing to draw
             */
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        for (Object object : objects_to_draw_in_glasspane) {
            if (object instanceof MyPoint2D) {
                ((MyPoint2D) object).drawAndFill(g2d);
                continue;
            }
            if (object instanceof Drawable) {
            ((Drawable) object).drawAndFill(g2d);
            }
        }
        if (blink instanceof Drawable)
        {
            ((Drawable) blink).drawAndFill(g2d);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

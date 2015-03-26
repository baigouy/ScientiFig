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
/*
 * ColoredButton.java
 *
 * Created on 27-Aug-2008, 14:52:37
 */
package Commons;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * Just a colored button, behaves like a button but its bg is given a certain
 * color (importantly we override the UI to avoid pbs especially on MacOS X)
 *
 * @since <B>Packing Analyzer 1.0</B>
 * @author Benoit Aigouy
 */
public class PaintedButton extends JButton {

    /**
     * We just extend the button interface for this class no big deal
     */
    static final long serialVersionUID = 2162480067214638293L;
    Color color = Color.BLACK;
    String textWhenActive;
    String position;

    /**
     * Empty constructor
     */
    public PaintedButton() {
        this.setBackground(Color.BLACK);
        setColor(Color.BLACK);
        setUI(null);
    }

    /**
     * Constructor
     *
     * @param text text of the button
     * @param color_me background color of the button
     */
    public PaintedButton(String text, int color_me) {
        super(text);
        textWhenActive = text;
        color = new Color(color_me);
        this.setBackground(color);
        setColor(color);
        setUI(null);
    }

    /**
     * Constructor for use in palettes
     *
     * @param color_me color of the button
     * @param pos_in_ramp position in the ramp/palette
     */
    public PaintedButton(int color_me, int pos_in_ramp) {
        this.position = "pos: " + pos_in_ramp;
        color = new Color(color_me);
        this.setBackground(color);
        setColor(color);
        setUI(null);
    }

    @Override
    public void setText(String text) {
        textWhenActive = text;
        super.setText(text);
    }

    public void setActive(boolean active) {
        if (!active) {
            super.setBackground(Color.BLACK);
            super.setForeground(new Color(0xFFFFFFFF));
            super.setText("N.A.");
            //super.setEnabled(false);
        } else {
            setColor(color);
            super.setText(textWhenActive);
            //super.setEnabled(true);
        }
    }

    /**
     * Sets the bg color of the button and assigns a negative color as fg color
     * for a better contrast
     *
     * @param col
     */
    public void setColor(int col) {
        color = new Color(col);
        this.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue()));
        this.setForeground(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
        repaint();
    }

    /**
     * we force the UI to be metal to avoid color pbs especially on Macs or when
     * using the Nimbus L&F
     */
    @Override
    public void setUI(ButtonUI ui) {
        /*
         * to avoid pbs on Macs and under linux when people change PA look & feel
         */
        super.setUI(new MetalButtonUI());
    }

    /**
     * As a tooltip for the button we show its color (R,G and B components)
     */
    public void setToolTip() {
        if (position != null) {
            /**
             * here we also show its position in the ramp/palette
             */
            this.setToolTipText("<html>" + position + "<BR>" + CommonClassesLight.getHtmlColor(this.getColor()) + "<BR>R:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getRed(getColor()), 3) + " G:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getGreen(getColor()), 3) + " B:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getBlue(getColor()), 3) + "<BR></html>");
        } else {
            this.setToolTipText("<html>" + CommonClassesLight.getHtmlColor(this.getColor()) + "<BR>R:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getRed(getColor()), 3) + " G:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getGreen(getColor()), 3) + " B:" + CommonClassesLight.create_number_of_the_appropriate_size(CommonClassesLight.getBlue(getColor()), 3) + "<BR></html>");
        }
    }

    /**
     * Sets the bg color of the button and takes the negative color as fg color
     *
     * @param tmp
     */
    public final void setColor(Color tmp) {
        if (tmp != null) {
            this.setBackground(new Color(tmp.getRed(), tmp.getGreen(), tmp.getBlue()));
            this.setForeground(new Color(255 - tmp.getRed(), 255 - tmp.getGreen(), 255 - tmp.getBlue()));
            color = tmp;
            repaint();
            setToolTip();
        }
    }

    /**
     *
     * @return the current bg color of the button
     */
    public int getColor() {
        return (this.getBackground().getRed() << 16) + (this.getBackground().getGreen() << 8) + this.getBackground().getBlue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

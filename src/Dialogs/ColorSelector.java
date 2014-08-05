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

import Commons.CommonClassesLight;
import Commons.PaintedButton;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * ColorSelector allows the user to manually specify colors or fill colors for
 * their plots
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ColorSelector extends javax.swing.JPanel {

    ArrayList<PaintedButton> names_and_colors = new ArrayList<PaintedButton>();

    /**
     * Constructor
     *
     * @param nb_of_cols number of fill/color colors
     * @param colors already attributed colors
     */
    public ColorSelector(int nb_of_cols, ArrayList<Integer> colors) {
        this(nb_of_cols);
        if (colors != null) {
            int count = 0;
            int max = colors.size();
            for (PaintedButton nameAndColor : names_and_colors) {
                if (count < max) {
                    nameAndColor.setColor(colors.get(count));
                    nameAndColor.setToolTip();
                } else {
                    if (count < CommonClassesLight.colorBlindFriendlyPalette.length) { 
                        nameAndColor.setColor(CommonClassesLight.colorBlindFriendlyPalette[count]); 
                        nameAndColor.setToolTip();
                    }
                }
                count++;
            }
        }
    }

    /**
     * Constructor
     *
     * @param nb_of_cols number of fill/color colors
     */
    public ColorSelector(int nb_of_cols) {
        initComponents();
        for (int i = 0; i < nb_of_cols; i++) {
            PaintedButton pt = new PaintedButton();
            pt.setText("               ");
            pt.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    chg_color(evt);
                }
            });
            names_and_colors.add(pt);
            jPanel1.add(pt);
        }
        jPanel1.revalidate();
        jScrollPane1.revalidate();
    }

    private void chg_color(java.awt.event.ActionEvent evt) {
        PaintedButton tmp = (PaintedButton) (evt.getSource());
        Color color = JColorChooser.showDialog(this, "Pick a Color", tmp.getBackground());
        if (color != null) {
            tmp.setColor(color);
            tmp.setToolTip();
        }
    }

    /**
     *
     * @return the list of user selected colors
     */
    public ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (PaintedButton nameAndColor : names_and_colors) {
            colors.add(nameAndColor.getColor());
        }
        return colors;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        ColorSelector iopane = new ColorSelector(10);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "ColorSelector", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}



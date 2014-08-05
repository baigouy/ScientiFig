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
package Commons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.plaf.IconUIResource;

/**
 * Modified Jlabel that also contains a thumb
 *
 * @author Benoit Aigouy
 */
public final class IconLabel extends JLabel implements Callable<IconLabel> {

    String complete_name;

    /**
     * New instance
     *
     * @param name path to the image file
     * @since <B>Packing Analyzer 3.0</B>
     */
    public IconLabel(String name) {
        setLabel(name);
        updateThumb();
    }

    /**
     * New instance
     *
     * @param name text to display
     * @param img thumb to display
     */
    public IconLabel(String name, BufferedImage img) {
        setLabel(name);
        updateThumb(img);
    }

    @Override
    public IconLabel call() throws Exception {
        return this;
    }

    /**
     * Returns the Jlabel_text
     *
     * @return the text of the label (short name for the file)
     * @since <B>Packing Analyzer 3.0</B>
     */
    public String getLabel() {
        return super.getText();
    }

    public void setLabel(String name) {
        this.setText(new File(name).getName());
        this.complete_name = name;
    }

    /**
     * Returns the full path to the file instead of the text of the jLabel
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    @Override
    public String toString() {
        return complete_name;
    }

    /**
     * Reload the thumb
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public void updateThumb() {
        BufferedImage tmp = null;
        if (!complete_name.toLowerCase().endsWith(".svg")) {
            if (!complete_name.toLowerCase().endsWith(".figur")) {
                tmp = new Loader().loadWithImageJ8bitFix(complete_name);
            } else {
                try {
                    Icon icon11 = new javax.swing.ImageIcon(this.getClass().getResource("/Icons/tableur_icon.png"));
                    this.setIcon(icon11);
                    return;
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            }
        } else {
            tmp = new Loader().loadSVG2BufferedImage(complete_name);
            if (tmp == null) {
                return;
            }
        }
        if (tmp == null) {
            return;
        }
        if (tmp instanceof MyBufferedImage) {
            if (((MyBufferedImage) tmp).is16Bits()) {
                tmp = ((MyBufferedImage) tmp).get8BitsImage();
            }
        }
        BufferedImage tmpImage = new MyBufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tmpImage.createGraphics();
        g.drawImage(tmp, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
        g.dispose();
        Icon ico = new IconUIResource(new ImageIcon(tmpImage));
        this.setIcon(ico);
    }

    /**
     * Sets the label thumb
     *
     * @param tmp image to be displayed as a thumb
     */
    public void updateThumb(BufferedImage tmp) {
        if (tmp == null) {
            return;
        }
        if (tmp instanceof MyBufferedImage) {
            if (((MyBufferedImage) tmp).is16Bits()) {
                tmp = ((MyBufferedImage) tmp).get8BitsImage();
            }
        }
        BufferedImage tmpImage = new MyBufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tmpImage.createGraphics();
        g.drawImage(tmp, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
        g.dispose();
        Icon ico = new IconUIResource(new ImageIcon(tmpImage));
        this.setIcon(ico);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
    }
}

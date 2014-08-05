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

import Commons.MyBufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * ImagePaneLight is just a panel to show images
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class ImagePaneLight extends JPanel {
    
    static final long serialVersionUID = 6524888059107252808L;
    private BufferedImage buf_img;
    private boolean isEnabled = true;

    /**
     * Empty constructor
     */
    public ImagePaneLight() {
        this(new MyBufferedImage(512, 512, BufferedImage.TYPE_INT_RGB));
    }

    /**
     * Constructor
     *
     * @param image image to display
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ImagePaneLight(BufferedImage image) {
        super(true);
        setImage(image);
    }
    
    @Override
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Defines the image the panel should display
     *
     * @param image a bufferedImeage
     */
    public void setImage(BufferedImage image) {
        if (image != null) {
            /*
             * TODO do i really need to copy it ???
             */
            buf_img = image;
            this.setSize(image.getWidth(null), image.getHeight(null));
            this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
            this.setMinimumSize(new Dimension(image.getWidth(null), image.getHeight(null)));
            this.setMaximumSize(new Dimension(image.getWidth(null), image.getHeight(null)));
            repaint();
        }
    }

    /**
     *
     * @return the image currently displayed
     */
    public BufferedImage getBufferedImage() {
        return buf_img;
        
    }

    /**
     * here we simply draw the image in the panel
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isEnabled && buf_img != null) {
            g.drawImage(buf_img, 0, 0, null);
        }
    }
}



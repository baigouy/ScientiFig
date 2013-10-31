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
package MyShapes;

import Commons.ImageTransformations;
import Commons.MyBufferedImage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * A serializable version of a BufferedImage<br><br>Sadly bufferedimages can't
 * be serialized natively in java so they have to be converted to int arrays to
 * allow serialization. In this vertsion I also have a transient bufferedimage
 * that makes the drawing much faster.
 *
 * initial hints found at https://forums.oracle.com/message/4666276
 *
 * @since <B>SF v2.0</B>
 * @author Benoit Aigouy
 */
public class SerializableBufferedImage2 implements Serializable {

    static final long serialVersionUID = -9202273175045827152L;
    int width;
    int height;
    int[] pixels;
    /*
     * dramatically speeds up drawing but doubles memory requirements
     */
    transient BufferedImage tmp;

    /**
     * Creates a new instance of a SerializableBufferedImage from a
     * BufferedImage
     *
     * @param bimg source image
     *
     */
    public SerializableBufferedImage2(BufferedImage bimg) {
        setBufferedImage(bimg);
    }

    /**
     * @return height of the image
     *
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return width of the image
     *
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the image data for the current SerializableBufferedImage
     *
     * @param bimg image
     *
     */
    public final void setBufferedImage(BufferedImage bimg) {
        /*
         * we make a copy of the image and we assign it to the transient BufferedImage
         */
        width = bimg.getWidth();
        height = bimg.getHeight();
        BufferedImage out = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(bimg, 0, 0, null);
        pixels = out.getRGB(0, 0, width, height, pixels, 0, width);
        g2.dispose();
        tmp = out;
    }

    /**
     * Converts a serialized BufferedImage to a BufferedImage or returns the
     * transient bufferedimage
     *
     *
     */
    public BufferedImage getBufferedImage() {
        if (tmp == null) {
            tmp = getImage();
        }
        return tmp;
    }

    /**
     * create a transient image after the serialized object has been reloaded
     * (upon serialization transient objects are lost)
     */
    public void reloadAfterSerialization() {
        if (tmp == null) {
            tmp = getImage();
        }
    }

    /**
     * reloads the serialized pixel array to a bufferedImage
     *
     *
     */
    public BufferedImage getImage() {
        if (pixels == null) {
            return null;
        }
        BufferedImage bi = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, width, height, pixels, 0, width);
        return bi;
    }

    /**
     * rotate current image left and update the parameters
     */
    public void rotateLeft() {
        tmp = new ImageTransformations().rotateLeft(tmp);
        setBufferedImage(tmp);
    }

    /**
     * rotate current image right and update the parameters
     */
    public void rotateRight() {
        tmp = new ImageTransformations().rotateRight(tmp);
        setBufferedImage(tmp);
    }

    /**
     * flip the current image
     *
     * @param orientation
     */
    public void flip(String orientation) {
        if (orientation.toLowerCase().contains("+")) {
            setBufferedImage(new ImageTransformations().flipHorizontallyAndVertically(tmp));
        } else if (orientation.toLowerCase().contains("ori")) {
            setBufferedImage(new ImageTransformations().horizontalFlip(tmp));
        } else if (orientation.toLowerCase().contains("ertic")) {
            setBufferedImage(new ImageTransformations().verticalFlip(tmp));
        }
    }

    public static void main(String[] args) {
    }
}

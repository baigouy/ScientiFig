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
package Commons;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Series of methods to transform images (rotation, translation, ...)
 *
 * @author Benoit Aigouy
 */
//TODO add translation
public class ImageTransformations {

    /**
     * interpolations for the rotation
     */
    public static final int NEAREST_NEIGHBOUR = 0;
    public static final int BICUBIC = 1;
    public static final int BILINEAR = 2;

    /**
     * Flips an image horizontally and vertically<BR><BR><B>NB: </B>no pixel
     * information is lost during this operation
     *
     * @param image source image
     * @return Bufferedimage containing the flipped image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage flipHorizontallyAndVertically(BufferedImage image) {
        return flipHorizontally(flipVertically(image));
    }

    /**
     * Flips an image horizontally<BR><BR><B>NB: </B>no pixel information is
     * lost during this operation
     *
     * @param image source image
     * @return Bufferedimage containing the flipped image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage horizontalFlip(BufferedImage image) {
        return flipHorizontally(image);
    }

    /**
     * Flips an image horizontally<BR><BR><B>NB: </B>no pixel information is
     * lost during this operation
     *
     * @param original source image
     * @return Bufferedimage containing the flipped image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage flipHorizontally(BufferedImage original) {
        BufferedImage output = new MyBufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0, k = width - 1; i < width; k--, i++) {
            for (int j = 0, l = 0; j < height; j++, l++) {
                output.setRGB(k, l, original.getRGB(i, j));
            }
        }
        return output;
    }

    /**
     * Flips an image vertically<BR><BR><B>NB: </B>no pixel information is lost
     * during this operation
     *
     * @param image source image
     * @return Bufferedimage containing the flipped image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage verticalFlip(BufferedImage image) {
        return flipVertically(image);
    }

    /**
     * Flips an image vertically<BR><BR><B>NB: </B>no pixel information is lost
     * during this operation
     *
     * @param original source image
     * @return Bufferedimage containing the flipped image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage flipVertically(BufferedImage original) {
        BufferedImage sortie = new MyBufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0, k = 0; i < width; k++, i++) {
            for (int j = 0, l = height - 1; j < height; j++, l--) {
                sortie.setRGB(k, l, original.getRGB(i, j));
            }
        }
        return sortie;
    }

    /**
     * Rotates an image to the right (clockwise)<BR><BR><B>NB: </B>no pixel
     * information is lost during the rotation
     *
     * @param original source image
     * @return Bufferedimage containing the rotated image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage rotateRight(BufferedImage original) {
        BufferedImage output = new MyBufferedImage(original.getHeight(), original.getWidth(), BufferedImage.TYPE_INT_RGB);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0, k = 0; i < width; k++, i++) {
            for (int j = 0, l = height - 1; j < height; j++, l--) {
                output.setRGB(l, k, original.getRGB(i, j));
            }
        }
        return output;
    }

    /**
     * Rotates an image to the left (counter clockwise)<BR><BR><B>NB: </B>no
     * pixel information is lost during the rotation
     *
     * @param original source image
     * @return Bufferedimage containing the rotated image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage rotateLeft(BufferedImage original) {
        BufferedImage output = new MyBufferedImage(original.getHeight(), original.getWidth(), BufferedImage.TYPE_INT_RGB);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0, k = width - 1; i < width; k--, i++) {
            for (int j = 0, l = 0; j < height; j++, l++) {
                output.setRGB(l, k, original.getRGB(i, j));
            }
        }
        return output;
    }

    /**
     * Rotates an image by an arbitrary angle<BR><BR><B>NB: </B>no pixel
     * information is changed during the rotation in an interpolation dependent
     * manner
     *
     * @param image source image
     * @param theta angle of the rotation
     * @param interpol interpolation to be used during rotation
     * @param resize tells whether the image should be resized or not, if false
     * the image will be trimmed
     * @return Bufferedimage containing the rotated image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public BufferedImage rotate(BufferedImage image, double theta, int interpol, boolean resize) {
        theta = correctAngle(theta);
        if (theta == 90. || theta == 270.) {
            return rotateRight(image);
        }
        if (theta == -90. || theta == -270.) {
            return rotateLeft(image);
        }
        if (theta == 180.) {
            return flipVertically(image);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        theta *= Math.PI / 180.;
        Dimension out = get_size_of_the_rotated_image(width, height, theta);//caculate the bounding image after rotation
        int final_width = out.width;
        int final_height = out.height;
        int center_x = width / 2;
        int center_y = height / 2;
        BufferedImage original;
        if (resize) {
            original = new MyBufferedImage(final_width, final_height, BufferedImage.TYPE_INT_RGB);
        } else {
            original = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        width = original.getWidth();
        height = original.getHeight();
        Graphics2D g2D = original.createGraphics();
        switch (interpol) {
            case BILINEAR:
                g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
            case NEAREST_NEIGHBOUR:
                g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                break;
            default://case BICUBIC:
                g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                break;
        }
        g2D.rotate(theta, width / 2, height / 2);
        g2D.translate(width / 2 - center_x, height / 2 - center_y);
        g2D.drawImage(image, 0, 0, null);
        g2D.dispose();
        image.flush();
        return original;
    }

    /**
     * we want to have values between 0 and 360 degrees
     *
     * @param angle
     * @return an angle between 0 and 360 degress
     */
    private static double correctAngle(double angle) {
        while (angle < 0.) {
            angle += 360.;
        }
        while (angle >= 360.) {
            angle -= 360;
        }
        return angle;
    }

    private static Point2D.Double calc_pos(int x, int y, int width, int height, double theta) {
        double newx = (x - width / 2.) * Math.cos(theta) - (y - height / 2.) * Math.sin(theta) + width / 2.;
        double newy = (x - width / 2.) * Math.sin(theta) + (y - height / 2.) * Math.cos(theta) + height / 2.;
        return new Point2D.Double(newx, newy);
    }

    private static double max(double d1, double d2) {
        return d1 < d2 ? d2 : d1;
    }

    private static double min(double d1, double d2) {
        return d1 < d2 ? d1 : d2;
    }

    public static Dimension get_size_of_the_rotated_image(int width, int height, double angle) {
        Point2D.Double upper_left_corner = calc_pos(0, 0, width, height, angle);
        Point2D.Double lower_left_corner = calc_pos(0, height - 1, width, height, angle);
        Point2D.Double lower_right_corner = calc_pos(width - 1, height - 1, width, height, angle);
        Point2D.Double upper_right_corner = calc_pos(width - 1, 0, width, height, angle);
        double min_x = min(upper_left_corner.x, min(lower_left_corner.x, min(lower_right_corner.x, upper_right_corner.x)));
        double max_x = max(upper_left_corner.x, max(lower_left_corner.x, max(lower_right_corner.x, upper_right_corner.x)));
        double min_y = min(upper_left_corner.y, min(lower_left_corner.y, min(lower_right_corner.y, upper_right_corner.y)));
        double max_y = max(upper_left_corner.y, max(lower_left_corner.y, max(lower_right_corner.y, upper_right_corner.y)));
        int new_width = (int) Math.round(Math.abs(min_x) + Math.abs(max_x));
        int new_height = (int) Math.round(Math.abs(min_y) + Math.abs(max_y));
        return new Dimension(new_width, new_height);
    }

    public static void main(String[] args) {
        BufferedImage test = new Loader().load("c:/test.png");
        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageTransformations.rotateRight(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageTransformations.rotateRight(test));
////
////        test = new Loader().load("c:/test.png");
////        start = System.currentTimeMillis();
////        for (int i = 0; i < 100; i++) {
////            ImageTransformations.rotateLeft(test);
////        }
////        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
////        Saver.pop(ImageTransformations.rotateLeft(test));
////
////        test = new Loader().load("c:/test.png");
////        start = System.currentTimeMillis();
////        for (int i = 0; i < 100; i++) {
////            ImageTransformations.rotate(test, 30, BILINEAR, true);
////        }
////        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
////        Saver.pop(ImageTransformations.rotate(test, 45, BILINEAR, false));
////
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageTransformations.horizontalFlip(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageTransformations.horizontalFlip(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageTransformations.verticalFlip(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageTransformations.verticalFlip(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageTransformations.flipHorizontallyAndVertically(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageTransformations.flipHorizontallyAndVertically(test));
    }
}



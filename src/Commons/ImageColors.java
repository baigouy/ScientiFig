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

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Series of static methods to modify the colors of an image
 *
 * @author Benoit Aigouy
 */
public class ImageColors { 

    public final static int CH1_TO_WHITE = 0;
    public final static int CH2_TO_WHITE = 1;
    public final static int CH3_TO_WHITE = 2;
    public final static int GRAY = 3;

    /**
     * Swaps the Red and the Blue channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2BGR(BufferedImage image) {
        return RGBtoBGR(image);
    }

    /**
     * Swaps the Red and the Blue channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoBGR(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int newRGB = (b << 16) + (g << 8) + (r);//0xff creespond e la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Places the Green channel into the the Red channel, the Red channel into
     * the Blue channel and the Blue channel into the Green one.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2BRG(BufferedImage image) {
        return RGBtoBRG(image);
    }

    /**
     * Places the Green channel into the the Red channel, the Red channel into
     * the Blue channel and the Blue channel into the Green one.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoBRG(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int newRGB = (g << 16) + (b << 8) + (r);//0xff creespond e la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Places the Green channel into the the blue channel, the Red channel into
     * the green channel and the Blue channel into the Red one.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2GBR(BufferedImage image) {
        return RGBtoGBR(image);
    }

    /**
     * Places the Green channel into the the blue channel, the Red channel into
     * the green channel and the Blue channel into the Red one.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoGBR(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int newRGB = ((b) << 16) + ((r) << 8) + (g);//0xff creespond e la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Swaps the Red and the Green channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2GRB(BufferedImage image) {
        return RGBtoGRB(image);
    }

    /**
     * Swaps the Red and the Green channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoGRB(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int newRGB = (g << 16) + (r << 8) + (b);//0xff creespond e la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Swaps the Green and the Blue channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2RBG(BufferedImage image) {
        return RGBtoRBG(image);
    }

    /**
     * Swaps the Green and the Blue channels of an image<BR><BR><B>NB: </B> the
     * other channel remains unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoRBG(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);

                int newRGB = (r << 16) + (b << 8) + (g);//0xff creespond e la composante alpha(opacite)

                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Creates a magenta green image that can be seen by color blind people. In
     * practice the blue channel of an image is copied into the red channel.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2ColorBlindBlue(BufferedImage image) {
        return colorBlindBlue(image);
    }

    /**
     * Creates a magenta green image that can be seen by color blind people. In
     * practice the blue channel of an image is copied into the red channel.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage colorBlindBlue(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
//                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int newRGB = (b << 16) + (g << 8) + (b);
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Creates a magenta green image that can be seen by color blind people. In
     * practice the red channel of the image is copied into the blue channel.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2ColorBlindRed(BufferedImage image) {
        return colorBlindRed(image);
    }

    /**
     * Creates a magenta green image that can be seen by color blind people. In
     * practice the red channel of the image is copied into the blue channel.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage colorBlindRed(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
//                int b = (rgb & 0xff);
                int newRGB = (r << 16) + (g << 8) + (r);//0xff creespond a la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Converts the Blue channel of an image to white<BR><BR><B>NB: </B> the two
     * other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2RGW(BufferedImage image) {
        return RGB2W(image, CH3_TO_WHITE);
    }

    /**
     * Converts the Blue channel of an image to white<BR><BR><B>NB: </B> the two
     * other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoRGW(BufferedImage image) {
        return RGB2W(image, CH3_TO_WHITE);
    }

    /**
     * Converts the Green channel of an image to white<BR><BR><B>NB: </B> the
     * two other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2RWB(BufferedImage image) {
        return RGB2W(image, CH2_TO_WHITE);
    }

    /**
     * Converts the Green channel of an image to white<BR><BR><B>NB: </B> the
     * two other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoRWB(BufferedImage image) {
        return RGB2W(image, CH2_TO_WHITE);
    }

    /**
     * Converts the Red channel of an image to white<BR><BR><B>NB: </B> the two
     * other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2WGB(BufferedImage image) {
        return RGB2W(image, CH1_TO_WHITE);
    }

    /**
     * Converts the Red channel of an image to white<BR><BR><B>NB: </B> the two
     * other channels remain unchanged.
     *
     * @param image source image
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoWGB(BufferedImage image) {
        return RGB2W(image, CH1_TO_WHITE);
    }

    private static int max(int i1, int i2) {
        return i1 < i2 ? i2 : i1;
    }

    /**
     * Converts the Red, Green or Blue channel of an image to
     * white<BR><BR><B>NB: </B> the two other channels remain unchanged.
     *
     * @param image source image
     * @param CHANNEL channel that need to be converted to white. R = 0; G = 1
     * and B = 2.
     * @return Bufferedimage containing the modified image
     * @since <B>Packing Analyzer 2.0</B>
     */
    private static BufferedImage RGB2W(BufferedImage image, final int CHANNEL) {
        BufferedImage output = null;
        try {
            BufferedImage temp = CommonClassesLight.copyImg(image);

            int width = temp.getWidth();
            int height = temp.getHeight();
            output = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int RGB = temp.getRGB(i, j);
                    int red = (RGB >> 16) & 0xFF;
                    int green = (RGB >> 8) & 0xFF;
                    int blue = (RGB & 0xFF);

                    if (CHANNEL == CH1_TO_WHITE) {
                        green = max(green, red);
                        blue = max(blue, red);
                    } else if (CHANNEL == CH2_TO_WHITE) {
                        red = max(red, green);
                        blue = max(blue, green);
                    } else {
                        red = max(red, blue);
                        green = max(green, blue);
                    }
                    int RGB_out = (red << 16) + (green << 8) + blue;
                    output.setRGB(i, j, RGB_out);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw); e.printStackTrace(pw);
            String stacktrace = sw.toString();pw.close();
            System.err.println(stacktrace);
        }
        return output;
    }

    /**
     * Get the red channel of an RGB image
     *
     * @param image source image
     * @return Bufferedimage containing the red channel of the source image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2R(BufferedImage image) {
        return RGB2R_G_B(image, true, false, false);
    }

    /**
     * Get the green channel of an RGB image
     *
     * @param image source image
     * @return Bufferedimage containing the green channel of the source image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2G(BufferedImage image) {
        return RGB2R_G_B(image, false, true, false);
    }

    /**
     * Get the blue channel of an RGB image
     *
     * @param image source image
     * @return Bufferedimage containing the blue channel of the source image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2B(BufferedImage image) {
        return RGB2R_G_B(image, false, false, true);
    }

    /**
     * Get R,G, or B channels of an RGB image<br><br>the method creates a
     * bitmask of the type 0xRRGGBB to filter each pixel
     *
     * @param image source image
     * @param R dipslay red channel
     * @param G dipslay green channel
     * @param B dipslay blue channel
     * @return Bufferedimage containing the selected channels
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2R_G_B(BufferedImage image, boolean R, boolean G, boolean B) {
        BufferedImage original = CommonClassesLight.copyImg(image);

        int mask = 0x000000;
        if (R) {
            mask += 0xff << 16;
        }
        if (G) {
            mask += 0xff << 8;
        }
        if (B) {
            mask += 0xff;
        }

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                original.setRGB(i, j, original.getRGB(i, j) & mask);
            }
        }
        return original;
    }

    /**
     * Converts and RGB image to a gray scaled image
     *
     * @param image source image
     * @return Bufferedimage containing the grayscaled image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoGrey(BufferedImage image) {
        return RGB2Gray(image);
    }

    /**
     * Converts and RGB image to a gray scaled image
     *
     * @param image source image
     * @return Bufferedimage containing the grayscaled image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2Grey(BufferedImage image) {
        return RGB2Gray(image);
    }

    /**
     * Converts and RGB image to a gray scaled image
     *
     * @param image source image
     * @return Bufferedimage containing the grayscaled image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGBtoGray(BufferedImage image) {
        return RGB2Gray(image);
    }

    /**
     * Converts and RGB image to a gray scaled image
     *
     * @param image source image
     * @return Bufferedimage containing the grayscaled image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage RGB2Gray(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                original.setRGB(i, j, (int) (0.299 * r + 0.587 * g + 0.114 * b));
            }
        }
        return original;
    }

    public static BufferedImage setGreenChannel(BufferedImage image, BufferedImage red_channel) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb_ori = original.getRGB(i, j);
                int rgb_green = red_channel.getRGB(i, j);
                int r = (rgb_ori & 0xff0000) >> 16;
                int g = (rgb_green & 0xff00) >> 8;
                int b = (rgb_ori & 0xff);
                int newRGB = (r << 16) + (g << 8) + (b);//0xff creespond a la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    public static BufferedImage setBlueChannel(BufferedImage image, BufferedImage red_channel) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb_ori = original.getRGB(i, j);
                int rgb_blue = red_channel.getRGB(i, j);
                int r = (rgb_ori & 0xff0000) >> 16;
                int g = (rgb_ori & 0xff00) >> 8;
                int b = (rgb_blue & 0xff);
                int newRGB = (r << 16) + (g << 8) + (b);//0xff creespond a la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    public static BufferedImage setRedChannel(BufferedImage image, BufferedImage red_channel) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb_ori = original.getRGB(i, j);
                int rgb_red = red_channel.getRGB(i, j);
                int r = (rgb_red & 0xff0000) >> 16;
                int g = (rgb_ori & 0xff00) >> 8;
                int b = (rgb_ori & 0xff);
                int newRGB = (r << 16) + (g << 8) + (b);//0xff creespond a la composante alpha(opacite)
                original.setRGB(i, j, newRGB);
            }
        }
        return original;
    }

    /**
     * Increases the luminosity of an image by 1
     *
     * @param image source image
     * @return Bufferedimage containing the output image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage lighter(BufferedImage image) {
        return luminosity(image, 1);
    }

    /**
     * Decreases the luminosity of an image by 1
     *
     * @param image source image
     * @return Bufferedimage containing the output image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage darker(BufferedImage image) {
        return luminosity(image, -1);
    }

    /**
     * Changes the luminosity of an image
     *
     * @param image source image
     * @param increment positive or negative int, typically between 1 and 255
     * @return Bufferedimage containing the output image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage luminosity(BufferedImage image, int increment) {
        return luminosity(image, increment, true, true, true);
    }

    /**
     * Changes the luminosity of an image
     *
     * @param image source image
     * @param increment positive or negative int, typically between 1 and 255
     * @param R in/decrement red channel
     * @param G in/decrement green channel
     * @param B in/decrement blue channel
     * @return Bufferedimage containing the output image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage luminosity(BufferedImage image, int increment, boolean R, boolean G, boolean B) {
        BufferedImage original = CommonClassesLight.copyImg(image);

        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);

                if (R) {
                    //in/decrement pixel intensity
                    r += increment;
                    //we check that it intensity of R, G and B is > 0
                    r = (r < 0) ? 0 : r;
                    //we check that it intensity of R, G and B is < 255
                    r = (r > 255) ? 255 : r;
                }
                if (G) {
                    g += increment;
                    g = (g < 0) ? 0 : g;
                    g = (g > 255) ? 255 : g;
                }
                if (B) {
                    b += increment;
                    b = (b < 0) ? 0 : b;
                    b = (b > 255) ? 255 : b;
                }
                original.setRGB(i, j, (r << 16) + (g << 8) + (b));
            }
        }
        return original;
    }

    /**
     * Creates the negative of an RGB image
     *
     * @param image source image
     * @return Bufferedimage containing the negative image
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage negative(BufferedImage image) {
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                original.setRGB(i, j, ((255 - r) << 16) + ((255 - g) << 8) + (255 - b));
            }
        }
        return original;
    }
    private static final int red_mask = 0xFF0000;
    private static final int green_mask = 0x00FF00;
    private static final int blue_mask = 0x0000FF;

    /**
     * Checks how many channels the image has. If the image has only one channel
     * it copies it into the two other channels.
     *
     * @param image source image
     * @return Bufferedimage made white or not
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage makeWhite(BufferedImage image) {
        if (image == null) {
            return null;
        }
        if (image instanceof MyBufferedImage) {
            if (((MyBufferedImage) image).is16Bits()) {
                return CommonClassesLight.copyImg(((MyBufferedImage) image).get8BitsImage());
            }
        }
        BufferedImage original = CommonClassesLight.copyImg(image);

        boolean is_green = false;
        boolean is_red = false;
        boolean is_blue = false;

        int width = original.getWidth();
        int height = original.getHeight();

        loop1:
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int RGB = original.getRGB(i, j);
                if ((RGB & red_mask) != 0) {
                    is_red = true;
                    if ((RGB & green_mask) != 0) {
                        is_green = true;
                    }
                    if ((RGB & blue_mask) != 0) {
                        is_blue = true;
                    }
                    break loop1;
                }
            }
        }

        if (is_green == false) {
            loop2:
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int RGB = original.getRGB(i, j);
                    if ((RGB & green_mask) != 0) {
                        is_green = true;
                        if ((RGB & blue_mask) != 0) {
                            is_blue = true;
                        }
                        break loop2;
                    }
                }
            }
        }

        if (!is_blue) {
            loop3:
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int RGB = original.getRGB(i, j);
                    if ((RGB & blue_mask) != 0) {
                        is_blue = true;
                        if ((RGB & green_mask) != 0) {
                            is_green = true;
                        }
                        break loop3;
                    }
                }
            }
        }

        if (is_red && (is_green || is_blue)) {
            return original;
        } else if (is_red && !is_green && !is_blue) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int RGB = (original.getRGB(i, j) >> 16) & 0xFF;
                    original.setRGB(i, j, (RGB << 16) + (RGB << 8) + RGB);
                }
            }
            return original;
        } else {
            if (!is_red && is_green && !is_blue) {
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int RGB = (original.getRGB(i, j) >> 8) & 0xFF;
                        original.setRGB(i, j, (RGB << 16) + (RGB << 8) + RGB);
                    }
                }
                return original;
            } else {
                if (!is_red && !is_green && is_blue) {
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            int RGB = original.getRGB(i, j) & 0xFF;
                            original.setRGB(i, j, (RGB << 16) + (RGB << 8) + (RGB));
                        }
                    }
                    return original;
                }
            }
        }
        return original;
    }

    private static boolean isSingleChannel(boolean showRed, boolean showGreen, boolean showBlue) {
        int count = 0;
        if (showRed) {
            count++;
        }
        if (showGreen) {
            count++;
        }
        if (showBlue) {
            count++;
        }
        return count == 1 ? true : false;
    }

    public static BufferedImage forceWhite(BufferedImage orig, boolean showRed, boolean showGreen, boolean showBlue) {
        if (!isSingleChannel(showRed, showGreen, showBlue)) {
            return orig;
        }
        if (showRed) {
            return ImageColors.forceWhite(orig, ImageColors.CH1_TO_WHITE);
        }
        if (showGreen) {
            return ImageColors.forceWhite(orig, ImageColors.CH2_TO_WHITE);
        }
        if (showBlue) {
            return ImageColors.forceWhite(orig, ImageColors.CH3_TO_WHITE);
        }
        return orig;
    }

    public static BufferedImage forceWhite(BufferedImage image, int mode) {
//        if (CommonClassesLight.is48Bits(image))
//        {
//            return CommonClassesLight.get16BitsChannel(mode, image);
//        }
        
        BufferedImage original = CommonClassesLight.copyImg(image);
        int width = original.getWidth();
        int height = original.getHeight();
        switch (mode) {
            case GRAY:
                original = RGB2Grey(original);
            case CH3_TO_WHITE:
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int RGB = original.getRGB(i, j) & 0xFF;
                        original.setRGB(i, j, (RGB << 16) + (RGB << 8) + (RGB));
                    }
                }
                break;
            case CH2_TO_WHITE:
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int RGB = (original.getRGB(i, j) >> 8) & 0xFF;
                        original.setRGB(i, j, (RGB << 16) + (RGB << 8) + RGB);
                    }
                }
                break;
            default:
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int RGB = (original.getRGB(i, j) >> 16) & 0xFF;
                        original.setRGB(i, j, (RGB << 16) + (RGB << 8) + RGB);
                    }
                }
                break;
        }
        return original;
    }

    public static void main(String[] args) {
//        BufferedImage test = new Loader().load("c:/test.png");
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoBGR(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoBGR(test));
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoBRG(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoBRG(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoGBR(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoGBR(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoGRB(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoGRB(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoRBG(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoRBG(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.colorBlindRed(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.colorBlindRed(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.colorBlindBlue(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.colorBlindBlue(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGBtoBGR(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGBtoBGR(test));
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2WGB(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2WGB(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2RWB(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2RWB(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2RGW(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2RGW(test));
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2R(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2R(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2G(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2G(test));
//
//        test = new Loader().load("c:/test.png");
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            ImageColors.RGB2B(test);
//        }
//        System.out.println("ellapsed time --> " + ((System.currentTimeMillis() - start) / 1000.));
//        Saver.pop(ImageColors.RGB2B(test));
    }
}



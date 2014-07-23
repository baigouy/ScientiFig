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
package Commons;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * My own image class that is a just a faster version of a BufferedImage
 *
 * @since <B>Packing Analyzer 2.0</B>
 * @author Benoit Aigouy
 */
public class MyBufferedImage extends BufferedImage {

    int nb_channels = 1;
    private BufferedImage eightBitsImage;
    double[][] values_double;
    float[][] values_float;
    int[] pixels;
    private WritableRaster wr;
    private DataBufferInt dbi;
    private int width = -1;
    private int height = -1;
    boolean is16Bits = false;
    boolean is48Bits = false;
    boolean isFloat = false;
    boolean isDouble = false;
    int cur_px_pos = 0;
    int max_nb_of_pixels = 0;
    ImagePlus ip;
    boolean isStack = false;
    /**
     * to allow 48 bits images to be read
     */
    short[][] chs;
    //short[]ch2;
    //short[]ch3;

    public MyBufferedImage(String name) {
        this(new Loader().load(name));
    }

    public MyBufferedImage(int width, int height) {
        this(width, height, TYPE_INT_RGB);
    }

    public MyBufferedImage(double width, double height, int imageType, boolean bool) {
        super((int) width, (int) height, imageType);
        wr = super.getRaster();
        dbi = (DataBufferInt) wr.getDataBuffer();
        this.width = super.getWidth();
        this.height = super.getHeight();
        this.max_nb_of_pixels = this.width * this.height;
    }

    public MyBufferedImage(double width, double height) {
        this(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public MyBufferedImage(double width, double height, int imageType) {
        super((int) Math.rint(width), (int) Math.rint(height), imageType);
        wr = super.getRaster();
        dbi = (DataBufferInt) wr.getDataBuffer();
        this.width = super.getWidth();
        this.height = super.getHeight();
        this.max_nb_of_pixels = this.width * this.height;
    }

    public MyBufferedImage(double width, double height, int imageType, int bgColor) {
        this((int) (width + 0.5), (int) (height + 0.5), imageType, bgColor);
    }

    public MyBufferedImage(int width, int height, int imageType, int bgColor) {
        super(width, height, imageType);
        wr = super.getRaster();
        dbi = (DataBufferInt) wr.getDataBuffer();
        this.width = super.getWidth();
        this.height = super.getHeight();
        this.max_nb_of_pixels = width * height;
        fill(bgColor);
    }

    public MyBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
        wr = super.getRaster();
        dbi = (DataBufferInt) wr.getDataBuffer();
        this.width = super.getWidth();
        this.height = super.getHeight();
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(int width, int height, int[] image) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
        this.width = width;
        this.height = height;
        setRGB(0, 0, width, height, image, 0, width);
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(int width, int height, double[] image) {
        super(1, 1, BufferedImage.TYPE_INT_RGB);
        this.width = width;
        this.height = height;
        values_double = new double[width][height];
        for (int j = 0; j < height; j++) {
            int pos = j * width;
            for (int i = 0; i < width; i++) {
                values_double[i][j] = image[pos + i];
            }
        }
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(int width, int height, float[] image) {
        super(1, 1, BufferedImage.TYPE_INT_RGB);
        this.width = width;
        this.height = height;
        values_float = new float[width][height];
        for (int j = 0; j < height; j++) {
            int pos = j * width;
            for (int i = 0; i < width; i++) {
                values_float[i][j] = image[pos + i];
            }
        }
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(double[][] image) {
        super(1, 1, BufferedImage.TYPE_INT_RGB);
        this.width = image[0].length;
        this.height = image.length;
        this.values_double = image;
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(float[][] image) {
        super(1, 1, BufferedImage.TYPE_INT_RGB);
        this.width = image[0].length;
        this.height = image.length;
        this.values_float = image;
        this.max_nb_of_pixels = width * height;
    }

    public MyBufferedImage(BufferedImage image) {
        super(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
        wr = super.getRaster();
        dbi = (DataBufferInt) wr.getDataBuffer();
        this.width = super.getWidth();
        this.height = super.getHeight();
        this.max_nb_of_pixels = width * height;
    }

    /**
     * Sets the associated ImageJ image stack
     *
     * @param ip
     */
    public void setStack(ImagePlus ip/*, boolean convertIfComposite*/) {
        /**
         * apparently it's useless --> remove it
         */
//        if (convertIfComposite) {
//            if (ip != null && ip.isComposite()) {
//                if (ip.getNChannels() > 1) {
//                    ij.IJ.run(ip, "Stack to RGB", "slices");
//                    this.ip = ip;
//                    return;
//                }
//            }
//        }
        this.ip = ip;
        int nbChannels = ip.getNChannels();
        chs = new short[nbChannels][];
        try {
            setStackImage(0);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     *
     * @return true if MyBufferedImage contains an ImageJ stack
     */
    public boolean isStack() {
        return ip != null;
    }

    /**
     *
     * @return the current ImageJ stack
     */
    public ImagePlus getStack() {
        return ip;
    }

    /**
     * Sets the current bufferedImage to the slice n of an ImageJ stack
     *
     * @param slice
     */
    public void setStackImage(int slice) {
        if (ip != null) {
            ip.setSlice(slice + 1);
            ip.setPosition(slice + 1);
            ip.setPosition(1, slice + 1, 1);
            ip.setSliceWithoutUpdate(slice + 1);
            BufferedImage img = CommonClassesLight.copyImg(ip.getBufferedImage());
            this.wr = img.getRaster();
            try {
                this.dbi = (DataBufferInt) wr.getDataBuffer();
            } catch (Exception e) {
            }
            super.setData(wr);
        }
    }

    /**
     *
     * @param slice
     * @return the image at position slice of an ImageJ stack
     */
    public BufferedImage getImageAt(int slice) {
        if (ip != null) {
            int slice_bckup = ip.getSlice();
            ip.setSlice(slice + 1);
            ip.setPosition(slice + 1);
            ip.setPosition(1, slice + 1, 1);
            BufferedImage img = CommonClassesLight.copyImg(ip.getBufferedImage());
            ip.setSlice(slice_bckup);
            ip.setPosition(slice_bckup);
            ip.setPosition(1, slice_bckup, 1);
            return img;
        }
        return null;
    }

    private BufferedImage get16BitsCh(int chNb) {
        if (is48Bits) {
            ImageStack stack = ip.getStack();
            ImageProcessor ch = stack.getProcessor(chNb);
            /**
             * we get rid of the LUT
             */
            ch.setColorModel(null);
            ImagePlus ip2 = new ImagePlus(null, ch);
            BufferedImage img = CommonClassesLight.copyImg(ip2.getBufferedImage());
            ip2.close();
            return img;
        } else {
            return null;
        }
    }

    public BufferedImage get16bitsCh1() {
        return get16BitsCh(1);
    }

    public BufferedImage get16bitsCh2() {
        return get16BitsCh(2);
    }

    public BufferedImage get16bitsCh3() {
        return get16BitsCh(3);
    }

    /**
     *
     * @return the number of slices of an ImageJ stack or -1 if no stack is
     * present
     */
    public int getNbSlices() {
        if (ip != null) {
            return ip.getNSlices();
        }
        return -1;
    }

    /**
     *
     * @return the pixel currently scanned
     */
    public int getCur_px_pos() {
        return cur_px_pos;
    }

    /**
     * Sets the current pixel
     *
     * @param cur_px_pos
     */
    public void setCur_px_pos(int cur_px_pos) {
        this.cur_px_pos = cur_px_pos;
    }

    /**
     *
     * @return true if there are still some pixels after the current position,
     * false if the next pixel is out of the image
     */
    public boolean hasNext() {
        return cur_px_pos < max_nb_of_pixels;
    }

    /**
     * go to the next pixel
     */
    public void next() {
        cur_px_pos++;
    }

    /**
     * replaces the current pixel
     *
     * @param val value of the pixel
     */
    public void put(int val) {
        pixels[cur_px_pos] = val;
    }

    /**
     *
     * @return the number of pixels of the image
     */
    public int getNbOfPx() {
        return width * height;
    }

    /**
     * sets pixel at position 'pos'
     *
     * @param pos
     * @param val
     */
    public void put(int pos, int val) {
        pixels[pos] = val;
    }

    /**
     *
     * @return the value of the current pixel
     */
    public int get() {
        return pixels[cur_px_pos];
    }

    /**
     *
     * @param pos
     * @return pixel value at position 'pos'
     */
    public int get(int pos) {
        return pixels[pos];
    }

    /**
     *
     * @return true if the image is a float image
     */
    public boolean isFloatImage() {
        return values_float != null;
    }

    /**
     *
     * @return true if the image is a double image
     */
    public boolean isDoubleImage() {
        return values_double != null;
    }

    /**
     *
     * @return the 8 bit version of the current image
     */
    public BufferedImage get8BitsImage() {
        return eightBitsImage;
    }

    /**
     * Sets the 8 bit image
     *
     * @param eightBitsImage
     */
    public void set8BitsImage(BufferedImage eightBitsImage) {
        this.eightBitsImage = eightBitsImage;
    }

    /**
     *
     * @return true id the image is a 16 bits image
     */
    public boolean is16Bits() {
        return is16Bits;
    }

    public boolean is48Bits() {
//        if (is16Bits) {
//            if (ip.getNChannels() >= 3 && ip.getBitDepth() == 16) {
//                return true;
//            }
//        }
//        return false;.
        return is48Bits;
    }

    /**
     * Defines whether the image is 16 bits or not
     *
     * @param is16Bits
     */
    public void set16Bits(boolean is16Bits) {
        this.is16Bits = is16Bits;
    }

    public void set48Bits(boolean is48Bits) {
        this.is48Bits = is48Bits;
    }

    /**
     * below are just various fast setpixels functions
     */
    public synchronized void setRGB(int i, int j, int alpha, int red, int green, int blue) {
        dbi.setElem(j * width + i, (alpha << 24) + (red << 16) + (green << 8) + (blue));
    }

    public synchronized void setRGB(int i, int j, int red, int green, int blue) {
        dbi.setElem(j * width + i, (red << 16) + (green << 8) + (blue));
    }

    @Override
    public synchronized void setRGB(int i, int j, int val) {
        dbi.setElem(j * width + i, val);
    }

    public double[][] getDoubleRGB() {
        return values_double;
    }

    public void setDoubleRGB(double[][] values_double) {
        this.values_double = values_double;
    }

    public void setRGB(int i, int j, double val) {
        setDoubleRGB(i, j, val);
    }

    public void setDoubleRGB(int i, int j, double val) {
        values_double[i][j] = val;
    }

    public double getDoubleRGB(int i, int j) {
        return values_double[i][j];
    }

    public int[] getRGBs() {
        return getRGB(0, 0, width, height, null, 0, width);
    }

    public void setRGB(int i, int j, float val) {
        setFloatRGB(i, j, val);
    }

    public void setFloatRGB(int i, int j, float val) {
        values_float[i][j] = val;
    }

    public double getRGBd(int i, int j) {
        return getDoubleRGB(i, j);
    }

    public float getRGBf(int i, int j) {
        return getFloatRGB(i, j);
    }

    public float getFloatRGB(int i, int j) {
        return values_float[i][j];
    }

    public float[][] getFloatRGB() {
        return values_float;
    }

    public void setFloatRGB(float[][] values_float) {
        this.values_float = values_float;
    }

    public int getRGB(int i) {
        return dbi.getElem(i);
    }

    @Override
    public int getRGB(int i, int j) {
        return dbi.getElem(j * width + i);
    }

    //ça marche mais c'est super slow n'y aurait il pas un moyen d'accéder rapidement et directement aux pixels peu importe la couleur en plus dans setC il y a plein d'etapes inutiles comme le refresh de l'image qui ne sert à rien
    //now this is a much faster version of it
    public int getRGB(int i, int j, int c) {
//        System.out.println(ip);
        if (ip != null) {
            /**
             * ip.setC is very slow and makes the recovery of pixels very long
             * so I simpy store 16 bits processors in a short array
             * corresponding to the channel and then I just recover the pixels from this array
             */
            if (chs == null || chs[c - 1] == null) {
                ip.setC(c);
                chs[c - 1] = (short[]) ip.getProcessor().getPixels();
            }

//            if (ip.getC() != c) //                          ip.setPositionWithoutUpdate(c, ip.getZ(), ip.getT());  
//            {
//                ip.setC(c);
//            }
//            //c'est très long --> essayer de limiter les changements de channel en fait --> quantifier tt un channel avant de passer à un autre parce que c'est trop long
//            int[] pxs = ip.getPixel(i, j);
//            if (pxs != null) {
//                return pxs[0];
//            }
            if (chs[c - 1] == null) {
                return -1;
            }

            short val = chs[c - 1][j * ip.getWidth() + i];
            return val;

            //return -1;
        } else {
            return -1;
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static int getRGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) + (red << 16) + (green << 8) + (blue);
    }

    public static int combineRGB(int red, int green, int blue) {
        return (red << 16) + (green << 8) + (blue);
    }

    public int getAlpha(int i, int j) {
        return (getRGB(i, j) >> 24) & 0xFF;
    }

    public int getRed(int i, int j) {
        return (getRGB(i, j) >> 16) & 0xFF;
    }

    public int getGreen(int i, int j) {
        return (getRGB(i, j) >> 8) & 0xFF;
    }

    public int getBlue(int i, int j) {
        return getRGB(i, j) & 0xFF;
    }

    public static int getAlpha(int RGB) {
        return (RGB >> 24) & 0xFF;
    }

    public static int getRed(int RGB) {
        return (RGB >> 16) & 0xFF;
    }

    public static int getGreen(int RGB) {
        return (RGB >> 8) & 0xFF;
    }

    public static int getBlue(int RGB) {
        return RGB & 0xFF;
    }

    public void preparePixels() {
        pixels = getPixels();
        cur_px_pos = 0;
    }

    public int[] getPixels() {
        return getRGB(0, 0, width, height, null, 0, width);
    }

    @Override
    public void finalize() {
        if (pixels != null) {
            setRGB(0, 0, width, height, pixels, 0, width);
        }
        pixels = null;
        cur_px_pos = 0;
    }

    /**
     * Produces a float array out of a bufferedImage
     *
     * @param red
     * @param green
     * @param blue
     * @return a float array corresponding to the bufferedImage
     */
    public float[][] floatProducer(boolean red, boolean green, boolean blue) {
        int[] tmp = getRGBs();
        float[][] vals = new float[width][height];
        if (red && green || red && blue || green && blue) {
            for (int j = 0; j < height; j++) {
                int pos = width * j;
                for (int i = 0; i < width; i++) {
                    int init_val = tmp[pos + i];
                    int final_val = 0;
                    if (blue) {
                        final_val += init_val & 0xFF;
                    }
                    if (green) {
                        final_val += (((init_val >> 8) & 0xFF) << 8);
                    }
                    if (red) {
                        final_val += (((init_val >> 16) & 0xFF) << 16);
                    }
                    vals[i][j] = final_val;
                }
            }
        } else {
            for (int j = 0; j < height; j++) {
                int pos = width * j;
                for (int i = 0; i < width; i++) {
                    int init_val = tmp[pos + i];
                    int final_val = 0;
                    if (blue) {
                        final_val = init_val & 0xFF;
                    }
                    if (green) {
                        final_val = ((init_val >> 8) & 0xFF);
                    }
                    if (red) {
                        final_val = ((init_val >> 16) & 0xFF);
                    }
                    vals[i][j] = final_val;
                }
            }
        }
        this.values_float = vals;
        return vals;
    }

    /**
     * Produces a double array out of a bufferedImage
     *
     * @param red
     * @param green
     * @param blue
     * @return a double array corresponding to the bufferedImage
     */
    public double[][] doubleProducer(boolean red, boolean green, boolean blue) {
        int[] tmp = getRGBs();
        double[][] vals = new double[width][height];
        if (red && green || red && blue || green && blue) {
            for (int j = 0; j < height; j++) {
                int pos = width * j;
                for (int i = 0; i < width; i++) {
                    int init_val = tmp[pos + i];
                    int final_val = 0;
                    if (blue) {
                        final_val += init_val & 0xFF;
                    }
                    if (green) {
                        final_val += (((init_val >> 8) & 0xFF) << 8);
                    }
                    if (red) {
                        final_val += (((init_val >> 16) & 0xFF) << 16);
                    }
                    vals[i][j] = final_val;
                }
            }
        } else {
            for (int j = 0; j < height; j++) {
                int pos = width * j;
                for (int i = 0; i < width; i++) {
                    int init_val = tmp[pos + i];
                    int final_val = 0;
                    if (blue) {
                        final_val = init_val & 0xFF;
                    }
                    if (green) {
                        final_val = ((init_val >> 8) & 0xFF);
                    }
                    if (red) {
                        final_val = ((init_val >> 16) & 0xFF);
                    }
                    vals[i][j] = final_val;
                }
            }
        }
        this.values_double = vals;
        return vals;
    }

    /**
     * Produces a double array out of a bufferedImage
     *
     * @return a double array corresponding to the image
     */
    public double[][] doubleProducer() {
        int[] tmp = getRGBs();
        double[][] vals = new double[width][height];
        for (int j = 0; j < height; j++) {
            int pos = width * j;
            for (int i = 0; i < width; i++) {
                vals[i][j] = tmp[pos + i];
            }
        }
        this.values_double = vals;
        return vals;
    }

    /**
     * Produces a float array out of a bufferedImage
     *
     * @return a float array corresponding to the image
     */
    public float[][] floatProducer() {
        int[] tmp = getRGBs();
        float[][] vals = new float[width][height];
        for (int j = 0; j < height; j++) {
            int pos = width * j;
            for (int i = 0; i < width; i++) {
                vals[i][j] = tmp[pos + i];
            }
        }
        this.values_float = vals;
        return vals;
    }

    /**
     *
     * @return a bufferedImage out of a float array
     */
    public BufferedImage float2Image() {
        BufferedImage bimg = new MyBufferedImage(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bimg.setRGB(i, j, (int) (values_float[i][j] * 255.));
            }
        }
        return bimg;
    }

    public void double2Image() {
        if (values_double != null) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    setRGB(i, j, (int) values_double[i][j]);
                }
            }
        }
    }

    public void normalize(double rescalingFactor) {
        double max = Double.MIN_VALUE;
        double min = Double.MIN_VALUE;
        if (values_double != null) {
            for (double[] ds : values_double) {
                for (double d : ds) {
                    max = max < d ? d : max;
                    min = min > d ? d : min;
                }
            }
            for (double[] ds : values_double) {
                for (double d : ds) {
                    d = ((d - min) / (max - min)) * rescalingFactor;
                }
            }
        }
    }

    public void normalize2float(boolean isRed, boolean isGreen, boolean isBlue) {
        values_float = new float[width][height];
        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int RGB = getRGB(i, j);
                int red = (RGB >> 16) & 0xFF;
                int green = (RGB >> 8) & 0xFF;
                int blue = (RGB & 0xFF);

                if (isRed && isGreen || isRed && isBlue || isGreen && isBlue) {
                    RGB = 0;
                    if (isBlue) {
                        RGB += blue;
                    }
                    if (isGreen) {
                        RGB += green << 8;
                    }
                    if (isRed) {
                        RGB += red << 16;
                    }
                } else {
                    if (isBlue) {
                        RGB = blue;
                    }
                    if (isGreen) {
                        RGB = green << 8;
                    }
                    if (isRed) {
                        RGB = red << 16;
                    }
                }
                max = Math.max(max, RGB);
                min = Math.min(min, RGB);
            }
        }
        if (max != min) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int RGB = getRGB(i, j);
                    int red = (RGB >> 16) & 0xFF;
                    int green = (RGB >> 8) & 0xFF;
                    int blue = (RGB & 0xFF);

                    if (isRed && isGreen || isRed && isBlue || isGreen && isBlue) {
                        RGB = 0;
                        if (isBlue) {
                            RGB += blue;
                        }
                        if (isGreen) {
                            RGB += green << 8;
                        }
                        if (isRed) {
                            RGB += red << 16;
                        }
                    } else {
                        if (isBlue) {
                            RGB = blue;
                        }
                        if (isGreen) {
                            RGB = green << 8;
                        }
                        if (isRed) {
                            RGB = red << 16;
                        }
                    }
                    float output_float = (float) (RGB - min) / (float) (max - min);
                    values_float[i][j] = output_float;
                }
            }
        }
    }

    private void fill(int bgColor) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setRGB(i, j, bgColor);
            }
        }
    }

    public static void main(String args[]) {

        if (true) {
            int test = 10;
            System.out.println((Integer) test);

            return;
        }
//        MyBufferedImage toto = new MyBufferedImage();
//        
//        Saver.pop(toto);

//        int red = 255;
//        int green = 255;
//        int blue = 255;
//
//        int RGB = 0;
//
//        RGB += blue;
//        RGB += (green << 8);
//        RGB += (red << 16);
//        System.out.println(CommonClasses.getHtmlColorWithAlpha(RGB));
        if (true) {
            return;
        }

        float[][] img = {{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        MyBufferedImage bi = new MyBufferedImage(img);
        System.out.println(bi.getWidth() + " " + bi.getHeight() + " " + bi.getFloatRGB(0, 0));

//        Saver.pop(bi);
        if (true) {
            return;
        }

        BufferedImage original = new Loader().load("/C/test2.png");//ca ne marche pas mais j'en ai besoin si je veux faire ca
//        MyGraphics2D g2d = ((MyBufferedImage) original).createMyGraphics2D();
//        g2d.drawOval(256, 256, 20, 20);
////        SaverLight.save(SaverLight.FORMAT_AUTO, g2d, "/C/test3.png");
//        g2d.dispose();

//        long start_time = System.currentTimeMillis();
//        for (int i = 0; i < 50; i++) {
//            BufferedImage test = new MyBufferedImage("c:/big8.png");//ca marche
//        }
//        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
//        Saver.pop(new MyBufferedImage("c:/big8.png"));
//
//        start_time = System.currentTimeMillis();
//        for (int i = 0; i < 50; i++) {
//            BufferedImage test = new Loader().load("c:/big8.png");//ca marche
//        }
//        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
//        Saver.pop(new Loader().load("c:/big8.png"));
//
//        ((MyBufferedImage) original).setRGB(128, 128, 255, 0, 0); // on peut faire ca mais faire attention aux parentheses elles doivent exactement etre comme ca
        System.exit(0);
    }
}

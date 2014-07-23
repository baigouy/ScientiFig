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

import ij.CompositeImage;
import ij.ImagePlus;
import ij.io.FileInfo;
import ij.process.ImageProcessor;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GVTTreeWalker;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

public class Loader {

    public static boolean loaderCanCancel = false;
    FileInfo fifo = null;

    public Loader() {
    }

    /**
     * Reloads a serialized (non compressed) object
     *
     * @param fichier path to the file containing the serialized object
     * @return an object
     */
    public Object loadObjectRaw(String fichier) {
        Object obj = null;
        ObjectInputStream in = null;
        try {
            if (!new File(fichier).exists()) {
                return null;
            }
            in = new ObjectInputStream((new FileInputStream(fichier)));
            obj = in.readObject();
            in.close();
        } catch (Exception e) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e3) {
            }
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return obj;
    }

    /**
     * Reloads a compressed serialized object
     *
     * @param fichier path to the file containing the serialized object
     * @return an object
     */
    public Object loadObject(String fichier) {
        Object obj = null;
        ObjectInputStream in = null;
        try {
            if (!new File(fichier).exists()) {
                return null;
            }
            in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(fichier)));
            obj = in.readObject();
            in.close();
        } catch (Exception e) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return obj;
    }

    /**
     * Loads a SVG file
     *
     * @param complete_name
     * @return a SVGDocument
     */
    public SVGDocument loadSVGDocument(String complete_name) {
        System.setProperty("org.apache.batik.warn_destination", "false");
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory saxsvgdf = new SAXSVGDocumentFactory(parser);
            File file = new File(complete_name);
            SVGDocument document = (SVGDocument) saxsvgdf.createDocument(file.toURI().toURL().toString());
            return document;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * tries to convert an SVG file to a bufferedimage. It may fail (out of
     * memory error, so to prevent this I check the bounds of the svg, if it is
     * too big I just return null).
     *
     * @param complete_name
     * @return a bufferedimage corresponding to the svg file or null if loading
     * or drawing failed
     */
    public BufferedImage loadSVG2BufferedImage(String complete_name) {
        System.setProperty("org.apache.batik.warn_destination", "false");
        try {
            SVGDocument document = loadSVGDocument(complete_name);
            return loadSVG2BufferedImage(document);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            return null;
        }
    }

    /**
     * tries to convert an SVGDocument to a bufferedimage. It may fail (out of
     * memory error, so to prevent this I check the bounds of the svg, if it is
     * too big I just return null).
     *
     * @param document
     * @return a bufferedImage corresponding to the SVG or null if fails
     */
    public BufferedImage loadSVG2BufferedImage(SVGDocument document) {
        System.setProperty("org.apache.batik.warn_destination", "false");
        try {
            UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
            BridgeContext bridgeContext = new BridgeContext(userAgentAdapter);
            GVTBuilder builder = new GVTBuilder();
            GraphicsNode graphicsNode = builder.build(bridgeContext, document);
            CanvasGraphicsNode canvasGraphicsNode = (CanvasGraphicsNode) graphicsNode.getRoot().getChildren().get(0);
            Rectangle2D bounds = canvasGraphicsNode.getSensitiveBounds();
            /*
             * we reset the scale (pb in ggsave not respecting the size somehow) --> resetting the transform fixes the bug
             */
            canvasGraphicsNode.setTransform(new AffineTransform());
            GVTTreeWalker treeWalker = new GVTTreeWalker(graphicsNode);
            GraphicsNode gNode;
            loop1:
            while ((gNode = treeWalker.nextGraphicsNode()) != null) {
                /**
                 * needed to get the text back after changing scale in a ggsave
                 * R image a trick found here:
                 * http://www.mail-archive.com/batik-users@xml.apache.org/msg06110.html
                 */
                gNode.setClip(null);
            }
            /**
             * my bug fix for empty SVGs
             */
            if (bounds == null) {
                return null;
            }
            int width = (int) ((Math.abs(bounds.getX()) + bounds.getWidth()));
            int height = (int) ((Math.abs(bounds.getY()) + bounds.getHeight()));
            if (width > 2048) {
                return null;
            }
            if (height > 2048) {
                return null;
            }
            BufferedImage bufferedImage = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            /**
             * set the bg to white like in inkscape or illustrator
             */
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bufferedImage.setRGB(i, j, 0xFFFFFF);
                }
            }
            Graphics2D g2d = bufferedImage.createGraphics();
            graphicsNode.paint(g2d);
            g2d.dispose();
            return bufferedImage;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return null;
    }

    /**
     * Opens a file and returns the corresponding BufferedImage
     *
     * @param chemin path to file
     * @since <B>Packing Analyzer 3.0</B>
     */
    public BufferedImage load(String chemin) {
        return load(chemin, true);
    }

    /**
     * loads an image
     *
     * @param path path to the image
     * @param bool useless
     * @return a bufferedImage
     */
    public BufferedImage load(String path, boolean bool) {
        fifo = null;
        BufferedImage original = null;
        try {
            if (!path.toLowerCase().endsWith(".tiff") && !path.toLowerCase().endsWith(".tif")) {
                original = ImageIO.read(new File(path));
            }
            if (original != null) {
                ColorModel cm = original.getColorModel();
                int bit_depth = cm.getPixelSize();
                if (bit_depth == 8 || bit_depth == 24) {
                    return CommonClassesLight.copyImg(original);
                } else {
                    return loadWithImageJ(path);
                }
            } else {
                /**
                 * loading failed we then try to open the image with IJ
                 */
                return loadWithImageJ(path);
            }
        } catch (Exception e) {
        }
        return original;
    }

    /**
     * Opens an image using ImageJ
     *
     * @param name
     * @return an imagePlus
     */
    public ImagePlus loadJ(String name) {
        if (!new File(name).exists()) {
            return null;
        }
        ImagePlus original = ij.IJ.openImage(CommonClassesLight.change_path_separators_to_system_ones(name));
        return original;
    }

    public MyBufferedImage ImagePlusToRaster(ImagePlus original) {
        int width = original.getWidth();
        int height = original.getHeight();
        ImageProcessor ip = original.getProcessor();
        int[][] tmp = ip.getIntArray();
        int[] out = new int[width * height];
        for (int j = 0; j < height; j++) {
            int pos = j * width;
            for (int i = 0; i < width; i++) {
                out[pos + i] = tmp[i][j];
            }
        }
        MyBufferedImage img = new MyBufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //bizarre ca marche avec des MyBufferedimage mais pas avec des bufferedimage
        img.setRGB(0, 0, width, height, out, 0, width);
        return img;
    }

    public FileInfo getFileInfo() {
        return fifo;
    }

    /**
     * I had some palette errors in my load functions this code aims at fixing
     * this (basically I just use ImageJ to open the image if I know the palette
     * is going to be corrupted)
     *
     * @param name
     * @return a bufferedImage
     */
    public BufferedImage loadWithImageJ8bitFix(String name) {
        if (name == null) {
            return null;
        }
        if (!name.toLowerCase().endsWith(".tif") && !name.toLowerCase().endsWith(".tiff")) {
            return load(name, true);
        }
        if (!new File(name).exists()) {
            return null;
        }
        ImagePlus original = ij.IJ.openImage(CommonClassesLight.change_path_separators_to_system_ones(name));
        BufferedImage img = null;
        if (original != null) {
            fifo = original.getFileInfo();
            if (original.getBitDepth() == 24) {
                BufferedImage tmp = original.getBufferedImage();
                img = CommonClassesLight.copyImg(tmp);
                /**
                 * bug fix for negative values
                 */
                int width = img.getWidth();
                int height = img.getHeight();
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        img.setRGB(i, j, img.getRGB(i, j) & 0x00FFFFFF);
                    }
                }
            } else if (original.getBitDepth() == 8) {
                if (original.isComposite()) {
                    ((CompositeImage) original).setMode(CompositeImage.COMPOSITE);
                }
                BufferedImage tmp = original.getBufferedImage();
                img = CommonClassesLight.copyImg(tmp);

                /**
                 * bug fix for 8 bits images not loading
                 */
//                int width = img.getWidth();
//                int height = img.getHeight();
//                for (int i = 0; i < width; i++) {
//                    for (int j = 0; j < height; j++) {
//                        int blue = img.getRGB(i, j) & 0xFF;
//                        int blue = img.getRGB(i, j) & 0xFF;
//                        int blue = img.getRGB(i, j) & 0xFF;
//                        int RGB = ((blue) << 16) + ((blue) << 8) + (blue);
//                        img.setRGB(i, j, RGB);
//                    }
//                }
            } else {
                img = loadWithImageJ(name);
            }
            original.close();
        }
        return img;
    }

    /**
     * Loads an image using ImageJ
     *
     * @param name
     * @return a bufferedImage
     */
    public BufferedImage loadWithImageJ(String name) {
        if (!new File(name).exists()) {
            return null;
        }

        //si image est 48bits --> faire un overlay
        ImagePlus original = ij.IJ.openImage(CommonClassesLight.change_path_separators_to_system_ones(name));

        MyBufferedImage img = null;
        if (original != null) {
            fifo = original.getFileInfo();
            img = ImagePlusToRaster(original);
            int bit_depth = original.getBitDepth();
            if (bit_depth == 16) {
                img.set16Bits(true);
                //bug fiw for images with two 16bits channels
                if (original.getNChannels() >= 2 && original.getBitDepth() == 16) {
                    img.set48Bits(true);
                    /**
                     * we force the compoosite image to be displayed
                     */
//                          int nChannels = original.getNChannels();
//                for (int i = 1; i <= nChannels; i++) {
//                    original.setC(i);
//                    original.resetDisplayRange();
//                }
                    ((CompositeImage) original).setMode(CompositeImage.COMPOSITE);
                }
                img.set8BitsImage(original.getBufferedImage());

            }
            if (original.getNSlices() != 1 || img.is48Bits) {
                /**
                 * removed because it apparently is useless and prevented proper
                 * loading of images
                 */
                img.setStack(original/*, true*/);
            } else {
                original.close();
            }
        }
        return img;
    }

    public static void main(String args[]) {

        if (true) {
            BufferedImage bad = new Loader().load("C:/test.png");
            return;
        }

        String name = "/home/benoit/Bureau/trash/3D/3D.tif";
        for (int i = 0; i < 10; i++) {
//            SaverLight.pop(new LoaderLight().loadPartially(name, i));
        }

        if (true) {
            return;
        }

        long start_time = System.currentTimeMillis();

        //check static vs non static
        for (int i = 0; i < 100; i++) {
            BufferedImage bimg = new Loader().load("C:/test.png");
        }
//
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");

        start_time = System.currentTimeMillis();

        //check static vs non static
        for (int i = 0; i < 100; i++) {
            BufferedImage bimg = new Loader().load("C:/test.png");
        }
//
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            //Logger.getLogger(Negative.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        SaverLight.save( tmp, "c:/negat2.png");
//        start_time = System.currentTimeMillis();
//
//        for (int i = 0; i < 100; i++) { 
////            new Negative().apply(raster);
////new Negative().apply(tmp);
//            BufferedImage bimg = new LoaderLight().load_with_jimi("C:/test24.png");
//        }
//
//        System.collectedCallableOutput.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

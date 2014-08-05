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

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.macro.Interpreter;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.imageio.ImageIO;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * A class to save images to files
 *
 * @since <B>Packing Analyzer 3.0</B>
 */
public class SaverLight {

    /**
     * Saving formats
     */
    public static int PNG_compression_level = 4;
    public static final int FORMAT_RAW_GNUPLOT = -1;
    public static final int FORMAT_RAW = -1;
    public static final int FORMAT_PNG = 1;
    public static final int FORMAT_BMP = 2;
    public static final int FORMAT_GIF = 3;
    public static final int FORMAT_PPM_P6 = 5;
    public static final int FORMAT_PPM = 5;
    public static final int FORMAT_PPM_P3 = 6;
    public static final int FORMAT_JPEG = 0;
    public static final int FORMAT_TIF = 7;
    public static final int FORMAT_TIFF = 7;
    public static final int FORMAT_AUTO = 10;
    /*BATIK specific output*/
    public static final int FORMAT_SVG = 11;
    /*POPUP WINDOW*/
    public static final int FORMAT_VOLATILE = 16;
    public static boolean extreme_mode = false;

    public SaverLight() {
    }

    /**
     * Saves a serialized object (without compression)
     *
     * @param obj object to serialize
     * @param fichier name of the output file
     */
    public static void saveObjectRaw(Object obj, String fichier) {
        serialExportationRaw(obj, fichier);
    }

    /**
     * Saves a serialized object (without compression)
     *
     * @param obj object to serialize
     * @param fichier name of the output file
     */
    public static void serialExportationRaw(Object obj, String fichier) {
        ObjectOutputStream out = null;
        try {
            File outf = new File(new File(fichier).getParent());
            outf.mkdirs();
            out = new ObjectOutputStream((new FileOutputStream(fichier)));
            out.writeObject(obj);
            out.close();
        } catch (Exception e2) {
            try {
                out.close();
            } catch (Exception e) {
            }
            System.err.println(e2.toString());
        }
    }

    /**
     * Saves a serialized object (with compression)
     *
     * @param obj object to serialize
     * @param fichier name of the output file
     */
    public static void saveObject(Object obj, String fichier) {
        serialExportation(obj, fichier);
    }

    /**
     * Saves a serialized object (with compression)
     *
     * @param obj object to serialize
     * @param fichier name of the output file
     */
    public static void serialExportation(Object obj, String fichier) {
        ObjectOutputStream out = null;
        try {
            File outf = new File(new File(fichier).getParent());
            outf.mkdirs();
            out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(fichier)));
            out.writeObject(obj);
            out.close();
        } catch (Exception e2) {
            try {
                out.close();
            } catch (Exception e) {
            }
            System.err.println(e2.toString());
        }
    }

    /**
     * pops a bufferedimage using ImageJ
     *
     * @param bimgs
     */
    public static void popJ(BufferedImage... bimgs) {
        popJ(null, bimgs);
    }

    public static void popJ(String title, BufferedImage... bimgs) {
        if (bimgs == null || bimgs.length == 0) {
            return;
        }
        int width = bimgs[0].getWidth();
        int height = bimgs[0].getHeight();

        ImageStack is = new ImageStack(width, height);
        if (bimgs[0].getType() != BufferedImage.TYPE_INT_RGB) {
            for (BufferedImage bimg : bimgs) {
                is.addSlice(null, new ImagePlus(null, CommonClassesLight.copyImg(bimg)).getProcessor());
            }
        } else {
            for (BufferedImage bimg : bimgs) {
                is.addSlice(null, new ImagePlus(null, bimg).getProcessor());
            }
        }
        ImagePlus ip = new ImagePlus(title, is);
        ip.show();
    }

    public static void addPopToJStack(BufferedImage... bimgs) {
        if (bimgs == null || bimgs.length == 0) {
            return;
        }
        ImagePlus ip = WindowManager.getCurrentImage();
        if (ip == null) {
            ip = new ImagePlus(null, bimgs[0]);
        }
        ImageStack is = ip.getImageStack();
        for (BufferedImage bufferedImage : bimgs) {
            is.addSlice(null, new ImagePlus(null, bufferedImage).getProcessor());
        }
        ip.setStack(is);
        ip.show();
    }

    /**
     * pops a bufferedimage using ImageJ and pause the thread for some ms
     *
     * @param ms time to wait in milliseconds
     * @param bimgs
     */
    public static void popJLong(int ms, BufferedImage... bimgs) {
        popJ(bimgs);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(SaverLight.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * pops an imagePlus using ImageJ
     *
     * @param ip
     */
    public static void popJ(ImagePlus ip) {
        if (ip == null) {
            return;
        }
        ip.show();
    }

    /**
     * pops an image plus using imageJ
     *
     * @param path path to the image
     */
    public static void popJ(String path) {
        if (path == null) {
            return;
        }
        ImagePlus ip = IJ.openImage(path);
        if (ip != null) {
            ip.show();
        }
    }

    /*
     * opens with imageJ even if already opened
     */
    public static void popJAgain(ImagePlus ip) {
        if (ip == null) {
            return;
        }
        ip.duplicate().show();
    }

    /**
     * Saves an image to a file
     *
     * @param format image format
     * @param image image to save
     * @param fichier output name
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static void save(int format, BufferedImage image, String fichier) {
        save(format, image, new File(fichier));
    }

    /**
     * Saves an image to a file
     *
     * @param image image to save
     * @param fichier output name
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static void save(BufferedImage image, String fichier) {
        save(FORMAT_AUTO, image, fichier);
    }

    /**
     * Saves an image to a file
     *
     * @param format image format
     * @param image image to save
     * @param fichier output name
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static void save(int format, BufferedImage image, File fichier) {
        CommonClassesLight.create_folder_if_it_does_not_exist(fichier.toString());
        if (image != null) {
            if (fichier.getParent() != null) {
                File parent_folder = new File(fichier.getParent());
                if (!parent_folder.exists() && !parent_folder.toString().equals("")) {
                    parent_folder.mkdirs();
                }
            }
            if (format == FORMAT_AUTO) {
                if (fichier.toString().toLowerCase().endsWith(".bmp")) {
                    format = FORMAT_BMP;
                } else if (fichier.toString().toLowerCase().endsWith(".jpg") || fichier.toString().toLowerCase().endsWith(".jpeg")) {
                    format = FORMAT_JPEG;
                } else if (fichier.toString().toLowerCase().endsWith(".gif")) {
                    format = FORMAT_GIF;
                } else if (fichier.toString().toLowerCase().endsWith(".ppm")) {
                    format = FORMAT_PPM;
                } else if (fichier.toString().toLowerCase().endsWith(".tif") || fichier.toString().toLowerCase().endsWith(".tiff")) {
                    format = FORMAT_TIF;
                } else if (fichier.toString().toLowerCase().endsWith(".raw") || fichier.toString().toLowerCase().endsWith(".rgb")) {
                    format = FORMAT_RAW;
                } else if (fichier.toString().toLowerCase().endsWith(".png")) {
                    format = FORMAT_PNG;
                } else if (fichier.toString().toLowerCase().endsWith(".vol") || fichier.toString().toLowerCase().endsWith(".tmp") || fichier.toString().toLowerCase().endsWith(".temp")) {
                    format = FORMAT_VOLATILE;
                } else if (fichier.toString().toLowerCase().endsWith(".svg")) {
                    format = FORMAT_SVG;
                } else {
                    /*
                     * default save format since TA 1.0
                     */
                    fichier = new File(fichier.toString().concat(".tif"));
                    format = FORMAT_TIF;
                }
            }
            if (format == FORMAT_JPEG) {
                try {
                    ImageIO.write(image, "jpg", fichier);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_PNG) {
                try {
                    ImageIO.write(image, "png", fichier);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_GIF) {
                try {
                    ImageIO.write(image, "gif", fichier);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_PPM_P6) {
                try {
                    DataOutputStream sortie = new DataOutputStream(new FileOutputStream(fichier));
                    int width = image.getWidth();
                    int height = image.getHeight();
                    /*
                     * ppm sign
                     */
                    sortie.write("P6\n".getBytes("utf8"));
                    sortie.write((String.valueOf(width) + " " + String.valueOf(height) + "\n").getBytes("utf8"));
                    sortie.write("#Created using the SF software (C) Benoit Aigouy 2012\n".getBytes("utf8"));
                    sortie.write("255\n".getBytes("utf8"));
                    byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                    sortie.write(pixels);
                    sortie.close();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_PPM_P3) {
                try {
                    DataOutputStream sortie = new DataOutputStream(new FileOutputStream(fichier));
                    int width = image.getWidth();
                    int height = image.getHeight();
                    String value;
                    sortie.write("P3\n".getBytes("utf8"));
                    sortie.write((String.valueOf(width) + " " + String.valueOf(height) + "\n").getBytes("utf8"));
                    sortie.write("#Created using the SF software (C) Benoit Aigouy 2012\n".getBytes("utf8"));
                    sortie.write("255\n".getBytes("utf8"));
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int rgb = image.getRGB(x, y);
                            value = String.valueOf((rgb >> 16) & 0xFF);
                            while (value.length() < 3) {
                                value = 0 + value;
                            }
                            sortie.write((value + " ").getBytes("utf8"));
                            value = String.valueOf((rgb >> 8) & 0xFF);
                            while (value.length() < 3) {
                                value = 0 + value;
                            }
                            sortie.write((value + " ").getBytes("utf8"));
                            value = String.valueOf((rgb) & 0xFF);
                            while (value.length() < 3) {
                                value = 0 + value;
                            }
                            sortie.write((value + " ").getBytes("utf8"));
                        }
                    }
                    sortie.close();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_RAW || format == FORMAT_RAW_GNUPLOT) {
                try {
                    byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                    DataOutputStream sortie = new DataOutputStream(new FileOutputStream(fichier));
                    sortie.write(pixels);
                    sortie.close();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            } else if (format == FORMAT_BMP) {
                CommonClassesLight.notSupportedYet();
            } else if (format == FORMAT_SVG) {
                DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
                String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
                SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
                SVGGraphics2D g2d = new SVGGraphics2D(doc);
                g2d.drawImage(image, 0, 0, null);
                Element root = doc.getDocumentElement();
                g2d.getRoot(root);
                root.setAttributeNS(null, "width", String.valueOf(image.getWidth()));
                root.setAttributeNS(null, "height", String.valueOf(image.getHeight()));
                saveAsSVG(doc, fichier.getAbsolutePath());
            } else if (format == FORMAT_VOLATILE) {
                popJ(image);
            } else if (format == FORMAT_TIF) {
                /**
                 * bug fix for imageJ showtime error, does not seem to cause
                 * problems elsewhere
                 */
                Interpreter.batchMode = true;
                /*
                 * I use ImageJ to save as tif (could also use JAI)
                 */
                ImagePlus ip = new ImagePlus(null, image);
                ij.IJ.saveAs(ip, "tiff", fichier.toString());
                ip.close();
                ip.flush();
//                Interpreter.batchMode=false;
            }
        }
    }

    public static void save(String name, BufferedImage... images) {
        if (images == null || images.length == 0) {
            return;
        }
        BufferedImage first = images[0];
        ImageStack is = new ImageStack(first.getWidth(), first.getHeight());
        for (int i = 0; i < images.length; i++) {
            BufferedImage img = images[i];
            ImageProcessor ips = new ImagePlus(null, img).getProcessor();
            is.addSlice(null, ips);
        }
        ImagePlus ip = new ImagePlus(null, is);
        if (!name.toLowerCase().endsWith(".tiff") && !name.toLowerCase().endsWith(".tif")) {
            name += ".tif";
        }
        ij.IJ.saveAs(ip, "tiff", name);
        ip.close();
        ip.flush();
    }

    /**
     * Saves svg to a file
     *
     * @param in input an SVGDocument
     * @param out name of the output file
     */
    public static void saveAsSVG(SVGDocument in, String out) {
        File f = new File(out);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            DOMUtilities.writeDocument(in, writer);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Sets the size of the svg document (sets the size of the page)
     *
     * @param in SVG doc
     * @param width
     * @param height
     */
    public static void setSize(SVGDocument in, double width, double height) {
        Element root = in.getDocumentElement();
        root.setAttributeNS(null, "width", String.valueOf(width));
        root.setAttributeNS(null, "height", String.valueOf(height));
    }

    /**
     * Saves svg to a file
     *
     * @param in input an SVGDocument
     * @param width SVG display width
     * @param height SVG display height
     * @param out name of the output file
     */
    public static void saveAsSVG(SVGDocument in, int width, int height, String out) {
        setSize(in, width, height);
        saveAsSVG(in, out);
    }

    public static void main(String args[]) {
    }
}

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

import Commons.CommonClassesLight;
import Commons.Loader;
import Commons.SaverLight;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GVTTreeWalker;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 * MyImageVector extends myImage2D, but specifically handles vector images
 *
 * @since <B>Packing Analyzer 8.5</B>
 * @author Benoit Aigouy
 */
public class MyImageVector extends MyImage2D implements Drawable, Serializable, Namable, Transformable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6176423765056197861L;

    static {
        System.setProperty("org.apache.batik.warn_destination", "false");
    }
    double initial_elems_width;
    double initial_elems_height;
    AffineTransform at = new AffineTransform();
    double init_transX = 0;
    double init_transY = 0;
    Point2D.Double cur_pos = new Point2D.Double();
    String unit = "";
    transient SVGDocument document;
    String svg_content_serializable = "";
    transient GraphicsNode graphicsNode;
    double top_initial_elems_width;
    double top_initial_elems_height;
    double top_init_transX = 0;
    double top_init_transY = 0;
    Rectangle2D.Double cropping_rect = new Rectangle2D.Double();

    /**
     * a double precision MyImageVector
     */
    public static class Double extends MyImageVector implements Serializable, Drawable {

        public static final long serialVersionUID = 1649852521660666660L;

        //text might be missing
        /*
         * the cloning is crappy and incomplete + very dirty --> make it better some day
         */
        public Double(MyImageVector myel) {
            this.rec2d = myel.rec2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.isTransparent = myel.isTransparent;
            this.transparency = myel.transparency;
            this.bimg = myel.bimg;
            this.letter = myel.letter;
            this.scale_bar_size_in_px_of_the_real_image = myel.scale_bar_size_in_px_of_the_real_image;
            this.scale_bar_text = myel.scale_bar_text;
            this.lower_left_text = myel.lower_left_text;
            this.lower_right_text = myel.lower_right_text;
            this.upper_right_text = myel.upper_right_text;
            this.upper_left_text = myel.upper_left_text;
            this.scale = myel.scale;
            this.SCALE_BAR_STROKE_SIZE = myel.SCALE_BAR_STROKE_SIZE;
            this.fullName = myel.fullName;
            this.shortName = myel.shortName;
            this.scalebarColor = myel.scalebarColor;
            this.left_crop = myel.left_crop;
            this.right_crop = myel.right_crop;
            this.up_crop = myel.up_crop;
            this.down_crop = myel.down_crop;
            this.associatedObjects = myel.cloneAssociatedObjects();
            this.theta = myel.theta;
            this.inset = myel.inset;
            this.INSET_POSITION = myel.INSET_POSITION;
            this.scale_barSize_in_pixels_PIP = myel.scale_barSize_in_pixels_PIP;
            this.scale = myel.scale;
            this.scale_bar_size_in_unit = myel.scale_bar_size_in_unit;
            this.size_of_one_px_in_unit = myel.size_of_one_px_in_unit;
            this.scalebarColor = myel.scalebarColor;
            this.gelBorderColor = myel.gelBorderColor;
            this.gelBorderSize = myel.gelBorderSize;
            this.rec2d = myel.rec2d;
            this.svg_content_serializable = myel.doc2String(false);
            this.initial_elems_height = myel.initial_elems_height;
            this.at = myel.at;
            this.init_transX = myel.init_transX;
            this.init_transY = myel.init_transY;
            this.cur_pos = myel.cur_pos;
            this.unit = myel.unit;
            this.top_initial_elems_width = myel.top_initial_elems_width;
            this.top_initial_elems_height = myel.top_initial_elems_height;
            this.top_init_transX = myel.top_init_transX;
            this.top_init_transY = myel.top_init_transY;
            this.cropping_rect = myel.cropping_rect;
            reloadDocFromString();
        }

        /**
         * Constructor
         *
         * @param document a SVGDocument
         */
        public Double(SVGDocument document) {
            super();
            super.document = document;
            createDocStuff();
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            setFirstCorner(new Point2D.Double(0, 0));
            doc2String(true);
        }

        /**
         * Constructor
         *
         * @param x
         * @param y
         * @param filename
         * @param document a SVGDocument
         */
        public Double(int x, int y, String filename, SVGDocument document) {
            super();
            super.document = document;
            createDocStuff();
            Rectangle2D r = getSVGBounds2D();
            initial_elems_width = r.getWidth();
            initial_elems_height = r.getHeight();
            init_transX = -r.getX();
            init_transY = -r.getY();
            super.fullName = filename;
            super.shortName = CommonClassesLight.getName(new File(filename).getName());
            setFirstCorner(new Point2D.Double(0, 0));
            doc2String(true);
        }

        /**
         * Construct a MyImageVector object from an ImageJ macro code
         *
         * @param macro an ImageJ macro
         */
        public Double(String macro) {
            super();
            HashMap<String, String> parameters = reparseMacro(macro);
            parameterDispatcher(parameters);
        }
    }

    /**
     *
     * @return the svgdocument embedded in this document
     */
    public SVGDocument getDocument() {
        if (document == null) {
            reloadDocFromString();
        }
        return document;
    }

    /**
     *
     * @param macro_parameters
     */
    public void parameterDispatcher(HashMap<String, String> macro_parameters) {
        rec2d = new Rectangle2D.Double();
        letter = new ColoredTextPaneSerializable("");
        scale_bar_text = new ColoredTextPaneSerializable("");
        upper_left_text = new ColoredTextPaneSerializable("");
        upper_right_text = new ColoredTextPaneSerializable("");
        lower_left_text = new ColoredTextPaneSerializable("");
        lower_right_text = new ColoredTextPaneSerializable("");
        ArrayList<String> keys = new ArrayList<String>(macro_parameters.keySet());
        for (String string : keys) {
            if (string.equals("src")) {
                String name = macro_parameters.get(string);
                if (!new File(name).exists()) {
                    return;
                }
                document = new Loader().loadSVGDocument(name);
                createDocStuff();
                Rectangle2D r = getSVGBounds2D();
                initial_elems_width = r.getWidth();
                initial_elems_height = r.getHeight();
                init_transX = -r.getX();
                init_transY = -r.getY();
                super.fullName = name;
                super.shortName = CommonClassesLight.strcutl_last(CommonClassesLight.strcutr_last(CommonClassesLight.change_path_separators_to_system_ones(name), "/"), ".");
                setFirstCorner(new Point2D.Double(0, 0));
                doc2String(true);
                continue;
            }
        }
        super.parameterDispatcher(macro_parameters, true);
    }

    public void createDocStuff() {
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        BridgeContext bridgeContext = new BridgeContext(userAgent, loader);
        GVTBuilder builder = new GVTBuilder();
        if (document == null) {
            reloadDocFromString();
        }
        graphicsNode = builder.build(bridgeContext, document);
        /*
         * necessary for R which somehow does not save at the right size otherwise
         */
        CanvasGraphicsNode canvasGraphicsNode = (CanvasGraphicsNode) graphicsNode.getRoot().getChildren().get(0);
        /*
         * we reset scaling using an empty AT
         */
        canvasGraphicsNode.setTransform(new AffineTransform());
        GVTTreeWalker treeWalker = new GVTTreeWalker(graphicsNode);
        GraphicsNode gNode;
        while ((gNode = treeWalker.nextGraphicsNode()) != null) {
            /*
             * Fix for clipping errors.
             * a trick found here: http://www.mail-archive.com/batik-users@xml.apache.org/msg06110.html
             */
            gNode.setClip(null);
        }
    }

    public void setSvg_content_serializable(String svg_content_serializable) {
        this.svg_content_serializable = svg_content_serializable;
    }

    @Override
    public void setScale_bar_size_in_px_of_the_real_image(double scale_bar_size_in_px) {
        /*
         * we do not allow scale bars for svg embeding since svg pixel size cannot be trusted
         */
    }

    @Override
    public boolean hasLineArts() {
        String txtEquivalent = doc2String(false);
        return txtEquivalent.contains("stroke-width");
    }

    @Override
    public boolean checkStrokeSize(float desiredStrokeSize, boolean isIllustrator) {
        String txtEquivalent = doc2String(false);
        if (!isIllustrator) {
            desiredStrokeSize *= scale;
        }
        Pattern p = Pattern.compile("stroke-width[=:]{1}[\"]{0,1}\\s{0,}([^;\"a-zA-Z\\s]{1,})[\"]{0,1}");
        Matcher m = p.matcher(txtEquivalent);
        boolean found = false;
        while (m.find()) {
//            System.out.println(m.group(1) + " in1");
            try {
                float curStroke = CommonClassesLight.String2Float(m.group(1));
                if (Math.abs(desiredStrokeSize - curStroke) > 0.05) {
                    /**
                     * the stroke of objects in the svg significantly differs
                     * from the desired stroke --> return false
                     */
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            found = true;
        }
        if (!found) {
            /**
             * no stroke found --> nothing to do --> return true
             */
            return true;
        }
        return false;
    }

    @Override
    public BufferedImage getFormattedImageWithoutTranslation(boolean precise) {
        BufferedImage img = null;
        double x = cur_pos.x;
        double y = cur_pos.y;
        try {
            cur_pos = new Point2D.Double();
            img = super.getFormattedImageWithoutTranslation(precise);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur_pos.x = x;
            cur_pos.y = y;
            return img;
        }
    }

    //essayer un matcher pr savoir si ca marche ou pas
    public void setStrokeSize(float strokeSize, boolean applyStrokeToSVG, boolean isIllustrator) {
        if (strokeSize <= 0 || !applyStrokeToSVG) {
            return;
        }
        try {
            if (!isIllustrator) {
                strokeSize *= scale;
            }
            /*
             * I have found again a crappy difference between Inkscape and Illustrator they seem to deal differently with stroke size
             * one of them includes zoom in the calculation of the stroke size whereas the other does not
             */
            String txtEquivalent = doc2String(false);
            String final_text = "";
            /*
             * crappy code to modify the stroke size of SVGs (we just parse the text of the svg and edit all 'stroke' tags
             */
            if (txtEquivalent.contains("stroke-width")) {
                if (txtEquivalent.contains("stroke-width=")) {
                    while (txtEquivalent.contains("stroke-width")) {
                        String pre = txtEquivalent.substring(0, txtEquivalent.indexOf("stroke-width"));
                        txtEquivalent = txtEquivalent.substring(pre.length());
                        txtEquivalent = txtEquivalent.substring(txtEquivalent.indexOf("=") + 1);
                        txtEquivalent = txtEquivalent.substring(txtEquivalent.indexOf("\"") + 1);
                        txtEquivalent = txtEquivalent.substring(txtEquivalent.indexOf("\"") + 1);
                        final_text += pre + "stroke-width=\"" + strokeSize + "\" ";//
                    }
                } else {
                    while (txtEquivalent.contains("stroke-width")) {
                        String pre = txtEquivalent.substring(0, txtEquivalent.indexOf("stroke-width"));
                        txtEquivalent = txtEquivalent.substring(pre.length());
                        txtEquivalent = txtEquivalent.substring(txtEquivalent.indexOf(":") + 1);
                        String sep = "\"";
                        int closest_end = txtEquivalent.indexOf("\"");
                        int closest_end2 = txtEquivalent.indexOf(";");
                        if (closest_end == -1) {
                            closest_end = closest_end2;
                            sep = ";";
                        }
                        if (closest_end > closest_end2) {
                            closest_end = closest_end2;
                            sep = ";";
                        }
                        txtEquivalent = txtEquivalent.substring(closest_end + 1);
                        final_text += pre + "stroke-width:" + strokeSize + sep;//
                    }
                }
                final_text += txtEquivalent;
                if (final_text.equals("")) {
                    return;
                } else {
                    svg_content_serializable = final_text;
                    reloadDocFromString();
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    @Override
    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        super.setJournalStyle(jp, applyToSVG, applyToROIs, applyToGraphs, changePointSize, isIllustrator, isOverrideItalic, isOverRideBold, isOverrideBoldForLetters); //To change body of generated methods, choose Tools | Templates.
        setStrokeSize(jp.getObjectsStrokeSize(), applyToSVG, isIllustrator);
    }

    @Override
    public BufferedImage getOriginalDisplay() {
        return new Loader().loadSVG2BufferedImage(document);
    }

    /**
     * since SVGdocuments can't be serialized (too bad) we convert it to a
     * string to serialize it
     */
    public String doc2String(boolean assign) {
        String txt = null;
        try {
            Writer w = new StringWriter();
            DOMUtilities.writeDocument(document, w);
            txt = w.toString();
            w.close();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        if (assign) {
            svg_content_serializable = txt;
        }
        return txt;
    }

    @Override
    public double getAR() {
        return (double) getImageWidth() / (double) getImageHeight();
    }

    @Override
    public int getImageWidth() {
        return (int) (initial_elems_width - cropping_rect.x - cropping_rect.width);
    }

    @Override
    public int getImageHeight() {
        return (int) (initial_elems_height - cropping_rect.y - cropping_rect.height);
    }

    @Override
    public void setShapeHeight(double height, boolean keepAR) {
        setToHeight(height);
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        setToWidth(width);
    }

    @Override
    public void crop(int left, int right, int up, int down) {
        double width = initial_elems_width;
        double height = initial_elems_height;
        if (width - left - right <= 0 || height - up - down <= 0) {
            System.err.println("You're cropping too much, your image doesn't have enough pixels");
            return;
        }
        this.left_crop = left;
        this.right_crop = right;
        this.up_crop = up;
        this.down_crop = down;
        if (left == 0 && right == 0 && up == 0 && down == 0) {
            isCropped = false;
            cropping_rect = new Rectangle2D.Double();
            return;
        }
        cropping_rect.x = left;
        cropping_rect.width = right;
        cropping_rect.y = up;
        cropping_rect.height = down;
        isCropped = true;
    }

    @Override
    public void scale(double factor) {
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width * factor, rec2d.height * factor);
        setToWidth(rec2d.width);
    }

    @Override
    public void setFirstCorner(Point2D.Double pt) {
        cur_pos = pt;
        updateAT();
    }

    /**
     * Force the vectorial image to be a certain height
     *
     * @param height
     */
    public void setToHeight(double height) {
        scale = (initial_elems_height - cropping_rect.y - cropping_rect.height) / height;
        updateAT();
    }

    /**
     * Force the vectorial image to be a certain width
     *
     * @param width
     */
    public void setToWidth(double width) {
        scale = (initial_elems_width - cropping_rect.x - cropping_rect.width) / width;//-cropping_rect.x-cropping_rect.width
        updateAT();
    }

    /**
     *
     * @return the bounds of the current vectorial image
     */
    public Rectangle2D getSVGBounds2D() {
        if (document == null) {
            reloadDocFromString();
        }
        CanvasGraphicsNode canvasGraphicsNode = (CanvasGraphicsNode) graphicsNode.getRoot().getChildren().get(0);
        canvasGraphicsNode.setTransform(new AffineTransform());
        Rectangle2D bounds = canvasGraphicsNode.getSensitiveBounds();
        int width = 0;
        int height = 0;
        /*
         * bug fix for empty SVGs although it probably does not make sense to have an empty svg in a figure
         */
        if (bounds != null) {
            width = (int) ((Math.abs(bounds.getX()) + bounds.getWidth()));
            height = (int) ((Math.abs(bounds.getY()) + bounds.getHeight()));
        } else {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }
        return new Rectangle2D.Double(bounds.getX(), bounds.getY(), width, height);
    }

    /**
     * Converts a String representation of a vector image into a SVGdocument
     */
    public void reloadDocFromString() {
        Reader sr;
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory saxsvgdf = new SAXSVGDocumentFactory(parser);
            sr = new StringReader(svg_content_serializable);
            document = saxsvgdf.createSVGDocument(null, sr);
            createDocStuff();
        } catch (java.io.IOException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (document == null) {
            reloadDocFromString();
        }
        AffineTransform at2 = g2d.getTransform();
        Shape clip = g2d.getClip();
        Paint pt = g2d.getPaint();
        if (clip != null) {
            int begin_x = (int) Math.max((rec2d.x), clip.getBounds2D().getX());
            int begin_y = (int) Math.max((rec2d.y), clip.getBounds2D().getY());
            int end_x = (int) Math.min(rec2d.x + rec2d.width, clip.getBounds2D().getWidth() + clip.getBounds2D().getX());
            int end_y = (int) Math.min(rec2d.y + rec2d.height, clip.getBounds2D().getHeight() + clip.getBounds2D().getY());
            g2d.setClip(begin_x, begin_y, end_x - begin_x, end_y - begin_y);
        } else {
            g2d.setClip((int) rec2d.x, (int) rec2d.y, (int) rec2d.width, (int) rec2d.height);
        }
        /*
         * I implemented the crop of SVG via clipping
         */
        updateAT();
        at.translate(-cropping_rect.x, -cropping_rect.y);
        g2d.transform(at);
        /*
         * we paint the svg to the g2d
         */
        graphicsNode.paint(g2d);
        /*
         * we restore the initial paint parameters to avoid pbs
         */
        g2d.setClip(null);
        g2d.setTransform(at2);
        g2d.setPaint(pt);
        super.draw(g2d);
        g2d.setClip(clip);
    }

    @Override
    public void translate(double x, double y) {
        cur_pos.x += x;
        cur_pos.y += y;
        updateAT();
    }

    /**
     * update affine transform according to scale, cropping, ...
     */
    public void updateAT() {
        rec2d = new Rectangle2D.Double(cur_pos.x, cur_pos.y, (initial_elems_width - cropping_rect.x - cropping_rect.width) / scale, (initial_elems_height - cropping_rect.y - cropping_rect.height) / scale);//(initial_elems_width-cropping_rect.x-cropping_rect.width) * scale, (initial_elems_height-cropping_rect.y-cropping_rect.height) * scale);        
        at = new AffineTransform();
        at.scale(1. / scale, 1. / scale);
        at.translate((cur_pos.x) * scale, (cur_pos.y) * scale);//-cropping_rect.y-cropping_rect.height
    }

    /**
     * Exports the embedded SVG
     *
     * @param name
     */
    @Override
    public void extractImage(String name) {
        if (document == null) {
            reloadDocFromString();
        }
        if (document != null) {
            String ext = ".svg";
            String name_no_ext = name + "/" + getShortName();
            String final_name = name_no_ext + ext;
            int counter = 0;
            if (new File(final_name).exists()) {
                do {
                    final_name = name_no_ext + "_" + CommonClassesLight.create_number_of_the_appropriate_size(counter++, 4) + ext;
                } while (new File(final_name).exists());
            }
            SaverLight.saveAsSVG(document, final_name);
        }
    }

    @Override
    public String logMagnificationChanges(double zoom) {
        System.out.println("the image called '" + toString() + "' is a vectorial image, magnification will not be calculated");
        return "";
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        MyImageVector test = new MyImageVector.Double(new Loader().loadSVGDocument("/D/sample_images_PA/trash_test_mem/test_vectoriel/Time_evolution_of_boundary_fraction_pAA.svg"));//Time_evolution_of_boundary_fraction_pAA
        BufferedImage tmp = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tmp.getWidth(); i++) {
            for (int j = 0; j < tmp.getHeight(); j++) {
                tmp.setRGB(i, j, 0xFFFFFFFF);
            }
        }
        Graphics2D g2d = tmp.createGraphics();
        test.setFirstCorner(new Point2D.Double(256, 256));
        test.setToWidth(128);
        test.draw(g2d);
        test.setFirstCorner(new Point2D.Double(0, 0));
        test.setToWidth(256);
        test.draw(g2d);
        g2d.dispose();
        SaverLight.popJ(tmp);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



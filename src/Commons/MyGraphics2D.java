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

import static Commons.MyGraphics2D.GRAPHICS2D_ONLY;
import static Commons.MyGraphics2D.MIXED_DRAWING_SVG_GRAPHICS2D;
import static Commons.MyGraphics2D.SVG_ONLY;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.ext.awt.image.codec.png.PNGRegistryEntry;
//import org.apache.batik.ext.awt.image.codec.tiff.TIFFRegistryEntry;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
//import org.apache.batik.svggen.CachedImageHandlerBase64Encoder;
import org.apache.batik.svggen.DefaultExtensionHandler;
//import org.apache.batik.svggen.GenericImageHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
//import org.apache.batik.svggen.ImageHandlerPNGEncoder;
//import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.util.Base64EncoderStream;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * My own version of graphics2d that allows to draw vector graphics shapes
 *
 * @since <B>Packing Analyzer 2.0</B>
 * @author Benoit Aigouy
 */
public class MyGraphics2D extends Graphics2D {

    static {
        final ImageTagRegistry registry = ImageTagRegistry.getRegistry();
        registry.register(new PNGRegistryEntry());
//        registry.register(new TIFFRegistryEntry());
        System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        /* yet another batik warning that we have to deactivate*/
        System.setProperty("org.apache.batik.warn_destination", "false"); 
    }
    public static int MIXED_DRAWING_SVG_GRAPHICS2D = 1;
    public static int GRAPHICS2D_ONLY = 2;
    public static int SVG_ONLY = 3;
//    public static int MIXED_DRAWING_SVG_PPT_GRAPHICS2D = 4;
//    public static int PPT_ONLY = 5; 
//    public static int PDF_ONLY = 6;
    public int DEFAULT_DRAWING = GRAPHICS2D_ONLY;//by default we just do the basic drawing
    public SVGDocument doc;
    Element mainmap;
//    public SlideShow ppt;
//    public Slide slide;
    private SVGGraphics2D g2dSVG;
//    private PPGraphics2D g2dPPT;
    private Graphics2D g2d;
    private BufferedImage img;
//    public ByteArrayOutputStream baos;
//    PDFDocumentGraphics2D g2dPDF;
    Font ft = null;
    Color c = null;

    public MyGraphics2D(int mode, BufferedImage img) {
        this.img = img;
        g2d = this.img.createGraphics();
        DEFAULT_DRAWING = mode;
        if (mode == MIXED_DRAWING_SVG_GRAPHICS2D || mode == SVG_ONLY /*|| mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            initSVGGraphics();
            g2dSVG.drawImage(img, 0, 0, null);
        }
//        if (mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D || mode == PPT_ONLY) {
//            initPPTgraphics();
//            //and draw the image in it
//            //g2dPPT.drawImage(img, 0, 0, null);
//            pptDrawImage(img, 0, 0, null);
//        }
    }

    public MyGraphics2D(Graphics2D g2d) {
        this.g2d = g2d;
        DEFAULT_DRAWING = GRAPHICS2D_ONLY;
    }

    public MyGraphics2D(Graphics2D g2d, int mode) {
        this.g2d = g2d;
        DEFAULT_DRAWING = mode;
        if (mode == MIXED_DRAWING_SVG_GRAPHICS2D || mode == SVG_ONLY /*|| mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            initSVGGraphics();
        }
//        if (mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D || mode == PPT_ONLY) {
//            initPPTgraphics();
//        }
//        if (mode == PDF_ONLY) {
//            initPDFgraphics();
//        }
    }

    public MyGraphics2D() {
    }

    public MyGraphics2D(BufferedImage img) {
        this.img = img;
        g2d = this.img.createGraphics();
    }

    public MyGraphics2D(int mode) {
        DEFAULT_DRAWING = mode;
        if (mode == MIXED_DRAWING_SVG_GRAPHICS2D || mode == SVG_ONLY /*|| mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            initSVGGraphics();
        }
//        if (mode == MIXED_DRAWING_SVG_PPT_GRAPHICS2D || mode == PPT_ONLY) {
//            initPPTgraphics();
//        }
//        if (mode == PDF_ONLY) {
//            initPDFgraphics();
//        }
    }

    public int getDrawingMode() {
        return DEFAULT_DRAWING;
    }

    public void setDrawingMode(int DEFAULT_DRAWING) {
        this.DEFAULT_DRAWING = DEFAULT_DRAWING;
    }

//    public void setBufferedImage(BufferedImage img) {
//        this.img = img;
//    }
    public BufferedImage getBufferedImage() {
        return img;
    }

    private void initSVGGraphics() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
//            g2dSVG = new SVGGraphics2D(doc);///old way --> embed
//        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);

//        ImageHandlerPNGEncoder ihandler;
//        try {
//            ihandler = new ImageHandlerPNGEncoder("res/images", null);
//            ctx.setImageHandler(ihandler);
//        } catch (SVGGraphics2DIOException ex) {
//            ex.printStackTrace();
//        }
// Reuse our embedded base64-encoded image data.
//ImageHandlerBase64Encoder ihandler = new ImageHandlerBase64Encoder();
//ctx.setImageHandler(ihandler);
        SVGGeneratorContext ctx = SVGGraphics2D.buildSVGGeneratorContext(doc, new ImageHandlerBase64Encoder(), new DefaultExtensionHandler());
        ctx.setComment("Generated by ScientiFig using Batik");
        /**
         * we embed fonts to avoid pbs of text bot showing when font is missing
         */
        ctx.setEmbeddedFontsOn(true);
//        ctx.se
        g2dSVG = new SVGGraphics2D(ctx, false);
        mainmap = doc.getDocumentElement();
    }

//    private void initPDFgraphics() {
//        try {
// FIXME--> creer un ppt avec une page
//            baos = new ByteArrayOutputStream();
//            g2dPDF = new PDFDocumentGraphics2D(false, baos, 595, 842);
//            g2dPDF.setGraphicContext(new GraphicContext());
//        } catch (Exception e) {
//        }
//    }
//    public void finish() {
//        if (g2dPDF != null) {
//            try {
//                g2dPDF.finish();
//            } catch (IOException ex) {
//                //Logger.getLogger(MyGraphics2D.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//    private void initPPTgraphics() {
// FIXME--> creer un ppt avec une page
//        ppt = new SlideShow();
//        slide = ppt.createSlide();
//        /*
//         * we set page size
//         */
////        ppt.setPageSize(new java.awt.Dimension(1024, 768));
//        ShapeGroup group = new ShapeGroup();
//        Rectangle bounds = new java.awt.Rectangle(200, 100, 350, 300);
//        group.setAnchor(bounds);
//        slide.addShape(group);
//        g2dPPT = new PPGraphics2D(group);
//    }
    @Override
    public Font getFont() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getFont();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /* || DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getFont();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getFont();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getFont();
//        }
        return null;
    }

    @Override
    public void setFont(Font font) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setFont(font);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setFont(font);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setFont(font);
//            ft = font;
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setFont(font);
//        }
    }

    @Override
    public Color getColor() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getColor();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getColor();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getColor();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getColor();
//        }
        return null;
    }

    public void setColor(int c) {
        setColor(new Color(c));
    }

    @Override
    public void setColor(Color c) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setColor(c);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setColor(c);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setColor(c);
//            this.c = c;
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setColor(c);
//        }
    }

    @Override
    public Stroke getStroke() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getStroke();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getStroke();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getStroke();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getStroke();
//        }
        return null;
    }

    @Override
    public void setStroke(Stroke s) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setStroke(s);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setStroke(s);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setStroke(s);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setStroke(s);
//        }
    }

    @Override
    public Paint getPaint() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getPaint();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getPaint();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getPaint();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getPaint();
//        }
        return null;
    }

    @Override
    public void setPaint(Paint paint) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setPaint(paint);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setPaint(paint);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setPaint(paint);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setPaint(paint);
//        }
    }

    @Override
    public AffineTransform getTransform() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getTransform();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getTransform();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getTransform();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getTransform();
//        }
        return null;
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setTransform(Tx);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setTransform(Tx);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setTransform(Tx);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setTransform(Tx);
//        }
    }

    @Override
    public void draw(Shape shape) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.draw(shape);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.draw(shape);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.draw(shape);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.draw(shape);
//        }
    }

    /* 
     * drawstring does not work in apache POI
     * see http://poi.apache.org/spreadsheet/quick-guide.html
     */
    @Override
    public void drawString(String s, float x, float y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawString(s, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawString(s, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            TextBox textBox = new TextBox();
//            textBox.setText(s);
//            textBox.setAnchor(new java.awt.Rectangle((int) x, (int) y, 100, 100)); //--> presque bon
//            if (ft != null) {
//                RichTextRun richtextrun = textBox.getTextRun().getRichTextRuns()[0];
//                richtextrun.setFontSize(ft.getSize());
//                richtextrun.setFontName(ft.getFamily());
//                richtextrun.setBold(ft.isBold());
//                richtextrun.setItalic(ft.isItalic());
////                richtextrun.
//                if (c != null) {
//                    richtextrun.setFontColor(c);
//                }
//                richtextrun.setAlignment(TextBox.AlignLeft);
//            }
//            slide.addShape(textBox);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawString(s, x, y);
//        }
    }

    @Override
    public void fill(Shape shape) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fill(shape);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fill(shape);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fill(shape);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fill(shape);
//        }
    }

    @Override
    public void translate(int x, int y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.translate(x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.translate(x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.translate(x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.translate(x, y);
//        }
    }

    @Override
    public void clip(Shape s) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.clip(s);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.clip(s);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.clip(s);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.clip(s);
//        }
    }

    @Override
    public Shape getClip() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getClip();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getClip();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getClip();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getClip();
//        }
        return null;
    }

    @Override
    public void scale(double sx, double sy) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.scale(sx, sy);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.scale(sx, sy);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.scale(sx, sy);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.scale(sx, sy);
//        }
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
//        }
    }

    @Override
    public void drawString(String str, int x, int y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawString(str, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawString(str, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawString(str, x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawString(str, x, y);
//        }
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fillOval(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fillOval(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fillOval(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fillOval(x, y, width, height);
//        }
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
//        }
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fillArc(x, y, width, height, startAngle, arcAngle);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fillArc(x, y, width, height, startAngle, arcAngle);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fillArc(x, y, width, height, startAngle, arcAngle);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fillArc(x, y, width, height, startAngle, arcAngle);
//        }
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawArc(x, y, width, height, startAngle, arcAngle);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawArc(x, y, width, height, startAngle, arcAngle);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawArc(x, y, width, height, startAngle, arcAngle);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawArc(x, y, width, height, startAngle, arcAngle);
//        }
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawPolyline(xPoints, yPoints, nPoints);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawPolyline(xPoints, yPoints, nPoints);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawPolyline(xPoints, yPoints, nPoints);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawPolyline(xPoints, yPoints, nPoints);
//        }
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawOval(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawOval(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawOval(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawOval(x, y, width, height);
//        }
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, x, y, bgcolor, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, x, y, bgcolor, observer);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  || DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D) {
//            ok3 = g2dPPT.drawImage(img, x, y, bgcolor, observer);
//            //les draw images ne sont pas implementes --> le faire
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, x, y, bgcolor, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, x, y, width, height, bgcolor, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, x, y, width, height, bgcolor, observer);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            ok3 = g2dPPT.drawImage(img, x, y, width, height, bgcolor, observer);
////            ok3 = pptDrawImage(img, x, y, width, height, observer);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, x, y, width, height, bgcolor, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            ok3 = g2dPPT.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            ok3 = g2dPPT.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, x, y, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            //bug with base64 cached png images
            ok2 = g2dSVG.drawImage(img, x, y, observer);
//drawImage(img, x, y, img.getWidth(null), img.getHeight(null));

        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
////            java.lang.String tmpdir = java.lang.System.getProperty("java.io.tmpdir");
////            File temp_file = null;
////            try {
////                temp_file = java.io.File.createTempFile("tmp", ".png", new java.io.File(tmpdir));
////                SaverLight.save(commons.SaverLight.FORMAT_AUTO, CommonClasses.copyImg(img), temp_file.toString());
////            } catch (Exception ex) {
////                System.out.println(ex.toString());
////            }
////            int idx;
////            try {
////                idx = ppt.addPicture(new File(temp_file.toString()), Picture.PNG);
////                Picture pict = new Picture(idx);
////                pict.setAnchor(new java.awt.Rectangle(x, y, img.getWidth(null), img.getHeight(null)));
////                ///Slide slide = ppt.createSlide();
////                slide.addShape(pict);
////                ok3 = true;
////            } catch (IOException ex) {
////                //Logger.getLogger(MyGraphics2D.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            temp_file.delete();
//            ok3 = pptDrawImage(img, x, y, observer);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, x, y, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public void dispose() {
        if (g2d != null) {
            if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
                g2d.dispose();
            }
        }
        if (g2dSVG != null) {
            if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
                g2dSVG.dispose();
            }
        }
//        if (g2dPPT != null) {
//            g2dPPT.dispose();
//        }
//        if (g2dPDF != null) {
//            g2dPDF.dispose();
//            try {
//                baos.close();
//            } catch (Exception e) {
//            }
//        }
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawLine(x1, y1, x2, y2);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawLine(x1, y1, x2, y2);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawLine(x1, y1, x2, y2);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawLine(x1, y1, x2, y2);
//        }
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fillPolygon(xPoints, yPoints, nPoints);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fillPolygon(xPoints, yPoints, nPoints);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fillPolygon(xPoints, yPoints, nPoints);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fillPolygon(xPoints, yPoints, nPoints);
//        }
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.fillRect(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.fillRect(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.fillRect(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.fillRect(x, y, width, height);
//        }
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawPolygon(xPoints, yPoints, nPoints);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawPolygon(xPoints, yPoints, nPoints);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawPolygon(xPoints, yPoints, nPoints);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawPolygon(xPoints, yPoints, nPoints);
//        }
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.clipRect(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.clipRect(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.clipRect(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.clipRect(x, y, width, height);
//        }
    }

    @Override
    public void setClip(Shape clip) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setClip(clip);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setClip(clip);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setClip(clip);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setClip(clip);
//        }
    }

    @Override
    public Rectangle getClipBounds() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getClipBounds();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getClipBounds();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getClipBounds();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getClipBounds();
//        }
        return null;
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawString(iterator, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawString(iterator, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawString(iterator, x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawString(iterator, x, y);
//        }
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.clearRect(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.clearRect(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.clearRect(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.clearRect(x, y, width, height);
//        }
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.copyArea(x, y, width, height, dx, dy);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.copyArea(x, y, width, height, dx, dy);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.copyArea(x, y, width, height, dx, dy);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.copyArea(x, y, width, height, dx, dy);
//        }
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setClip(x, y, width, height);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setClip(x, y, width, height);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setClip(x, y, width, height);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setClip(x, y, width, height);
//        }
    }

    @Override
    public void rotate(double theta) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.rotate(theta);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.rotate(theta);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.rotate(theta);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.rotate(theta);
//        }
    }

    @Override
    public void rotate(double theta, double x, double y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.rotate(theta, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.rotate(theta, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.rotate(theta, x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.rotate(theta, x, y);
//        }
    }

    @Override
    public void shear(double shx, double shy) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.shear(shx, shy);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.shear(shx, shy);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.shear(shx, shy);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.shear(shx, shy);
//        }
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getFontRenderContext();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getFontRenderContext();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getFontRenderContext();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getFontRenderContext();
//        }
        return null;
    }

    @Override
    public void transform(AffineTransform Tx) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.transform(Tx);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.transform(Tx);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.transform(Tx);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.transform(Tx);
//        }
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawImage(img, op, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawImage(img, op, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawImage(img, op, x, y);//meme si pas supporte on s'en fout
////            System.err.println("not supported yet");
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawImage(img, op, x, y);
//        }
    }

    @Override
    public void setBackground(Color color) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setBackground(color);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setBackground(color);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setBackground(color);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setBackground(color);
//        }
    }

    @Override
    public Color getBackground() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getBackground();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getBackground();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getBackground();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getBackground();
//        }
        return null;
    }

    @Override
    public void setComposite(Composite comp) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setComposite(comp);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setComposite(comp);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setComposite(comp);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setComposite(comp);
//        }
    }

    @Override
    public Composite getComposite() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getComposite();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getComposite();
        }
//        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getComposite();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getComposite();
//        }
        return null;
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getRenderingHint(hintKey);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getRenderingHint(hintKey);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getRenderingHint(hintKey);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getRenderingHint(hintKey);
//        }
        return null;
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setRenderingHint(hintKey, hintValue);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setRenderingHint(hintKey, hintValue);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setRenderingHint(hintKey, hintValue);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setRenderingHint(hintKey, hintValue);
//        }
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawGlyphVector(g, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawGlyphVector(g, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawGlyphVector(g, x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawGlyphVector(g, x, y);
//        }
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getDeviceConfiguration();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getDeviceConfiguration();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getDeviceConfiguration();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getDeviceConfiguration();
//        }
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    @Override
    public void addRenderingHints(Map hints) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.addRenderingHints(hints);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.addRenderingHints(hints);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.addRenderingHints(hints);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.addRenderingHints(hints);
//        }
    }

    @Override
    public void translate(double tx, double ty) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.translate(tx, ty);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.translate(tx, ty);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.translate(tx, ty);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.translate(tx, ty);
//        }
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawString(iterator, x, y);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawString(iterator, x, y);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawString(iterator, x, y);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawString(iterator, x, y);
//        }
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.hit(rect, s, onStroke);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.hit(rect, s, onStroke);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.hit(rect, s, onStroke);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.hit(rect, s, onStroke);
//        }
        return false;
    }

    @Override
    public RenderingHints getRenderingHints() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getRenderingHints();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getRenderingHints();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getRenderingHints();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getRenderingHints();
//        }
        return null;
    }

    @Override
    public void setRenderingHints(Map hints) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setRenderingHints(hints);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setRenderingHints(hints);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setRenderingHints(hints);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setRenderingHints(hints);
//        }
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, xform, obs);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, xform, obs);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            ok3 = g2dPPT.drawImage(img, xform, obs);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, xform, obs);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    public boolean isSVGPrecision() {
        return DEFAULT_DRAWING == SVG_ONLY;
    }

    /*
     * super dirty fix for layer pbs in vector images (PIP)
     * TODO do this properly some day
     */
    public void drawRectangle(Rectangle2D.Double r, int fillColor) {
        drawRectangle(r.x, r.y, r.width, r.height, fillColor);
    }

    /*
     * super dirty fix for layer pbs in vector images (PIP)
     * TODO do this properly some day
     */
    public void drawRectangle(double x, double y, double w, double h, int fillColor) {
//        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
//            Rectangle2D.Double r = new Rectangle2D.Double(x, y, w, h);
//            g2d.setColor(new Color(fillColor));
//            g2d.draw(r);
//        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            //g2dSVG.drawShape(shape);//draw rect

            Element rectangle = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
            rectangle.setAttributeNS(null, "x", x + "");
            rectangle.setAttributeNS(null, "y", y + "");
            rectangle.setAttributeNS(null, "width", w + "");
            rectangle.setAttributeNS(null, "height", h + "");
            rectangle.setAttributeNS(null, "fill", CommonClassesLight.toHtmlColor(fillColor));
            rectangle.setAttributeNS(null, "stroke", "none");//CommonClasses2.toHtmlColor(fillColor)
            mainmap.appendChild(rectangle);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            Rectangle2D.Double r = new Rectangle2D.Double(x, y, w, h);
//            g2d.setColor(new Color(fillColor));
//            g2dPPT.draw(r);
//        }
    }

    /**
     * Hack allowing for double precision drawing of images in a svg file.
     * Second half of the code is modified from
     * http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/201209.mbox/%3CCAKiJDQQ8QGheMUWu+nX5qAZT9cKJzrvg-nJD2L7E==yPqRN8nw@mail.gmail.com%3E
     */
    public void drawImage(Image img, double x, double y, double width, double height) {
        Element image = doc.createElementNS("http://www.w3.org/2000/svg", "image");
        image.setAttributeNS(null, "x", x + "");
        image.setAttributeNS(null, "y", y + "");
        image.setAttributeNS(null, "width", width + "");
        image.setAttributeNS(null, "height", height + "");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imgBytes = null;
        try {
            /*
             * we convert bufferedImage to a png and save it as a byte array to embed it in the svg
             */
            ImageIO.write((BufferedImage) img, "png", baos);
            baos.flush();
            imgBytes = baos.toByteArray();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stacktrace = sw.toString();
            pw.close();
            System.err.println("\nError:\n" + stacktrace);
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stacktrace = sw.toString();
                pw.close();
                System.err.println("\nError:\n" + stacktrace);
            }
        }
        if (imgBytes != null) {
            image.setAttributeNS(SVGConstants.XLINK_NAMESPACE_URI, SVGConstants.XLINK_HREF_QNAME, "data:image/png;base64," + base64Encode(imgBytes).toString());
            mainmap.appendChild(image);
        }
    }

    private String base64Encode(byte[] imgBytes) {
        String out = "";
        ByteArrayOutputStream b64out = new ByteArrayOutputStream();
        Base64EncoderStream encoder = new Base64EncoderStream(b64out);
        try {
            encoder.write(imgBytes);
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stacktrace = sw.toString();
            pw.close();
            System.err.println("\nError:\n" + stacktrace);
        } finally {
            try {
                encoder.close();
                out = b64out.toString();
                b64out.close();
            } catch (IOException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stacktrace = sw.toString();
                pw.close();
                System.err.println("\nError:\n" + stacktrace);
            }
            return out;
        }
    }

    /**
     * Hack allowing better embedding of svg file that is non damaging
     *
     * @param svgAsString
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawImage(String svgAsString, double x, double y, double width, double height) {
        Element image = doc.createElementNS("http://www.w3.org/2000/svg", "image");
        image.setAttributeNS(null, "x", x + "");
        image.setAttributeNS(null, "y", y + "");
        image.setAttributeNS(null, "width", width + "");
        image.setAttributeNS(null, "height", height + "");

        //   image.setAttributeNS(null, "preserveAspectRatio", x + "xMinYMin meet");
        // Attr attr = document.createAttribute("preserveAspectRatio");
        //  attr.setNodeValue("xMidYMid meet");
        image.setAttributeNS(SVGConstants.XLINK_NAMESPACE_URI, SVGConstants.XLINK_HREF_QNAME, "data:image/svg+xml;base64," + base64Encode(svgAsString.getBytes()));
        mainmap.appendChild(image);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        boolean ok = true;
        boolean ok2 = true;
        boolean ok3 = true;
        boolean ok4 = true;
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            ok = g2d.drawImage(img, x, y, width, height, observer);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            ok2 = g2dSVG.drawImage(img, x, y, width, height, observer);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            //ok3 = g2dPPT.drawImage(img, x, y, width, height, observer);
//            ok3 = pptDrawImage(img, x, y, width, height, observer);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            ok4 = g2dPDF.drawImage(img, x, y, width, height, observer);
//        }
        return ok && ok2 && ok3 && ok4 ? true : false;
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            return g2d.getFontMetrics(f);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            return g2dSVG.getFontMetrics(f);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            return g2dPPT.getFontMetrics(f);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            return g2dPDF.getFontMetrics(f);
//        }
        return null;
    }

    @Override
    public void setXORMode(Color c1) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setXORMode(c1);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setXORMode(c1);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setXORMode(c1);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setXORMode(c1);
//        }
    }

    @Override
    public void setPaintMode() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.setPaintMode();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.setPaintMode();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.setPaintMode();
//
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.setPaintMode();
//        }
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawRenderedImage(img, xform);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawRenderedImage(img, xform);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawRenderedImage(img, xform);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawRenderedImage(img, xform);
//        }
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.drawRenderableImage(img, xform);
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.drawRenderableImage(img, xform);
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.drawRenderableImage(img, xform);
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.drawRenderableImage(img, xform);
//        }
    }

    @Override
    public Graphics create() {
        if (DEFAULT_DRAWING == GRAPHICS2D_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D) {
            g2d.create();
        }
        if (DEFAULT_DRAWING == SVG_ONLY || DEFAULT_DRAWING == MIXED_DRAWING_SVG_GRAPHICS2D /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
            g2dSVG.create();
        }
//        if (DEFAULT_DRAWING == PPT_ONLY  /*|| DEFAULT_DRAWING == MIXED_DRAWING_SVG_PPT_GRAPHICS2D*/) {
//            g2dPPT.create();
//        }
//        if (DEFAULT_DRAWING == PDF_ONLY) {
//            g2dPDF.create();
//        }
        return this;
    }

    public Graphics2D getGraphics2D() {
        return g2d;
    }

    public SVGDocument getSVGDoc() {
        Element root = doc.getDocumentElement();
        g2dSVG.getRoot(root);
        g2dSVG.dispose();
        return doc;
    }

    public SVGGraphics2D getGraphics2DSVG() {
        return g2dSVG;
    }

    public void setGraphics(Graphics2D g2d) {
        this.g2d = g2d;
    }

    public static void main(String args[]) {
        BufferedImage bimg = new Loader().load("/C/test.png");
        MyGraphics2D test = new MyGraphics2D(MyGraphics2D.MIXED_DRAWING_SVG_GRAPHICS2D, bimg);
        test.drawImage(bimg, 0, 0, null);
        test.drawLine(0, 0, 512, 512);
        System.exit(0);
    }
}

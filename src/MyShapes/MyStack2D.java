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
package MyShapes;

import Commons.CommonClassesLight;
import Commons.ImageColors;
import Commons.Loader;
import Commons.MyBufferedImage;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

//alternatively do this as part of the image2D and just for memory purposes allow to remove the stacks when not necessary anymore
//maybe also allow conversion of the ROIs masks to a real binary mask
//maybe it would make more sense maybe ask when saving if people want to include the stack data or not
//also allow this class toà change channel color --> I really need that especially if chosen colors are damaging each other --> are overlapping --> detect this in IJ
//--> get the stacks and offer a change of palette --> but think how I can deal with images when they contain more than 3 channels because it is nonb trivial in that case
/**
 * MyStack2D is a stack compatible with myImage2D specifically designed for handling
 * stacks (can be used to do partial projections)
 *
 * @since <B>ScientiFig v2.99</B>
 * @author Benoit Aigouy
 */
public abstract class MyStack2D extends MyImage2D implements Transformable, Serializable, Namable, MagnificationChangeLoggable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6176426765056167816L;
    /**
     * first and last image for projection alternatively could use an array
     */
    int start_image = -1;
    int end_image = -1;
    boolean mask = false; //by default we don't mask
    //try to put this in as part of the annotate image because almost everything is in there maybe just add a button to preview the projection

    //do a proj algo independent of palette and all this crap
    
    /**
     * in this we will store the ROIs for each image
     */
    HashMap<Integer, ArrayList<Object>> selectedImageNMaskRois = new HashMap<Integer, ArrayList<Object>>();

    /**
     * selects an image from the satck
     *
     * @param pos
     */
    public void setImage(int pos) {
        //select the image then set it as the default drawn myImage2D
    }

    public void goToFirstImage() {
        //select the image then set it as the default drawn myImage2D
    }

    public void goToLastImage() {
        //select the image then set it as the default drawn myImage2D
    }

    public void doProjection(int begin, int end, boolean mask) {
        this.start_image = begin;
        this.end_image = end;
        //do the projection of the stack with or without any masking
    }

    public int getNbOfImages() {
        return -1;
    }

    public void reset() {
        resetBegin();
        resetEnd();
        resetMasks();
    }

    public void resetBegin() {
        start_image = -1;
    }

    public void resetEnd() {
        end_image = -1;
    }

    public void resetMasks() {
        selectedImageNMaskRois = new HashMap<Integer, ArrayList<Object>>();
    }

    //--> really need this tool so let's do it soon
    //only project what the guy wants
    //allow complex combinations of stuff and store those also make sure to restore those
    //maybe make masks and convert them to ROI for easier handling --> think about it
    //the only pb is that I have to store all the stack if I wanna do that
    //allow positive or negative of ROI to be included in the projection
    //maybe allow to change colors --> change channel color
    //allow it to do projections also --> would be cool then once done save the main image and store it in an image2D that is going to be plotted pretty, much like any other image
    //TODO build proper constructors
    /**
     * a double precision MyImage2D
     */
    public static class Double extends MyStack2D implements Serializable {

        public static final long serialVersionUID = 1649855521660999160L;

        /**
         * Constructor
         *
         * @param x_left_corner
         * @param y_left_corner
         * @param filename
         */
        public Double(double x_left_corner, double y_left_corner, String filename) {
            this(x_left_corner, y_left_corner, new Loader().loadWithImageJ8bitFix(filename));
            super.fullName = filename;
            super.shortName = CommonClassesLight.getName(new File(filename).getName());
        }

        /**
         * Constructor
         *
         * @param x_left_corner
         * @param y_left_corner
         * @param filename
         * @param bimg
         */
        public Double(double x_left_corner, double y_left_corner, String filename, BufferedImage bimg) {
            this(x_left_corner, y_left_corner, bimg);
            super.fullName = filename;
            super.shortName = CommonClassesLight.getName(new File(filename).getName());
        }

        /**
         * Constructor
         *
         * @param x_left_corner
         * @param y_left_corner
         * @param bimg
         */
        public Double(double x_left_corner, double y_left_corner, BufferedImage bimg) {
            if (bimg instanceof MyBufferedImage) {
                if (((MyBufferedImage) bimg).is16Bits()) {
                    MyBufferedImage tmp = new MyBufferedImage(bimg.getWidth(), bimg.getHeight());
                    Graphics2D g2d = tmp.createGraphics();
                    BufferedImage img = ((MyBufferedImage) bimg).get8BitsImage();
                    g2d.drawImage(img, 0, 0, null);
                    g2d.dispose();
                    bimg = tmp;
                    bimg = ImageColors.makeWhite(bimg);
                }
            }
            if (bimg != null) {
                this.bimg = new SerializableBufferedImage2(bimg);
                rec2d = new Rectangle2D.Double(x_left_corner, y_left_corner, bimg.getWidth(), bimg.getHeight());
            }
        }

        /**
         * Constructor
         *
         * @param myel
         */
        public Double(MyStack2D.Double myel) {
            this.rec2d = myel.rec2d;
            this.color = myel.color;
            this.strokeSize = myel.strokeSize;
            this.opacity = myel.opacity;
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
        }

        /**
         *
         * @param macro
         */
        public Double(String macro) {
            HashMap<String, String> parameters = reparseMacro(macro);
            parameterDispatcher(parameters, false);
        }

        @Override
        public Object clone() {
            return new MyStack2D.Double(this).setZpos(ZstackPos);
        }

        /**
         *
         * @param ZstackNb
         * @return a vectorial object with a defined Z stack position
         */
        public Double setZpos(int ZstackNb) {
            this.ZstackPos = ZstackNb;
            return this;
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        System.exit(0);
    }
}

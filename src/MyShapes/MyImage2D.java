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
package MyShapes;

import Dialogs.ColoredTextPane;
import static MyShapes.Drawable.LEFT;
import Commons.CommonClassesLight;
import Commons.G2dParameters;
import Commons.ImageColors;
import Commons.Loader;
import Commons.MyBufferedImage;
import Commons.MyGraphics2D;
import Commons.SaverLight;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * MyImage2D is a vectorial object that behaves like a myRectangle2D but
 * contains and draws images
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public abstract class MyImage2D extends MyRectangle2D implements Transformable, Serializable, Namable, MagnificationChangeLoggable {

    /**
     * Variables
     */
    public static final long serialVersionUID = -6171423725056197831L;
    /*
     * data for ImageJ macro mode
     */
    String masterTagOpen = "<img ";
    public static final HashMap<String, String> parameters = new HashMap<String, String>();
    boolean isRotated = false;
    String associatedComments = "";

    static {
        parameters.put("src", "data-src=");
        parameters.put("cropLeft", "data-cropLeft=");
        parameters.put("cropRight", "data-cropRight=");
        parameters.put("cropTop", "data-cropTop=");
        parameters.put("cropBottom", "data-cropBottom=");
        parameters.put("LText", "data-LText=");
        parameters.put("ULText", "data-ULText=");
        parameters.put("URText", "data-URText=");
        parameters.put("LLText", "data-LLText=");
        parameters.put("LRText", "data-LRText=");
        parameters.put("SBText", "data-SBText=");
        parameters.put("LFont", "data-LFont=");
        parameters.put("ULFont", "data-ULFont=");
        parameters.put("URFont", "data-URFont=");
        parameters.put("LLFont", "data-LLFont=");
        parameters.put("LRFont", "data-LRFont=");
        parameters.put("SBFont", "data-SBFont=");
        parameters.put("SBSize", "data-SBSize=");
        /*
         * better id, directly get the formatted text
         */
        parameters.put("LFormattedText", "data-LFormattedText=");
        parameters.put("ULFormattedText", "data-ULFormattedText=");
        parameters.put("URFormattedText", "data-URFormattedText=");
        parameters.put("LLFormattedText", "data-LLFormattedText=");
        parameters.put("LRFormattedText", "data-LRFormattedText=");
        parameters.put("SBFormattedText", "data-SBFormattedText=");
    }
    public int gelBorderColor = 0;
    public int gelBorderSize = 0;
    public final static float additional_space_left_or_right = 3;
    SerializableBufferedImage2 bimg;
    SerializableBufferedImage2 original;
    SerializableBufferedImage2 inset;
    /*
     * additional space in y in case Pip is active
     */
    float additional_space_up_left = 0;
    float additional_space_up_right = 0;
    float additional_space_down_left = 0;
    float additional_space_down_right = 0;
    int scale_barSize_in_pixels_PIP = 0;
    double scale_bar_size_in_px_of_the_real_image = 0.;
    double scale_bar_size_in_unit = 0.;
    double size_of_one_px_in_unit = 1.;
    int scalebarColor = 0xFFFFFFFF;
    ColoredTextPaneSerializable letter = new ColoredTextPaneSerializable("", "Letter");
    ColoredTextPaneSerializable scale_bar_text = new ColoredTextPaneSerializable("", "Text: Above Scale Bar");
    ColoredTextPaneSerializable upper_left_text = new ColoredTextPaneSerializable("", "Text: Upper Left Corner");
    ColoredTextPaneSerializable lower_left_text = new ColoredTextPaneSerializable("", "Text: Lower Left Corner");
    ColoredTextPaneSerializable upper_right_text = new ColoredTextPaneSerializable("", "Text: Upper Right Corner");
    ColoredTextPaneSerializable lower_right_text = new ColoredTextPaneSerializable("", "Text: Lower Right Corner");
    double translation_because_of_the_scale_bar = 0;
    double translation_because_of_the_letter = 0;
    double scale = 1.;
    public float SCALE_BAR_STROKE_SIZE = 3.f;
    public boolean isCropped = false;
    public int left_crop = 0;
    public int right_crop = 0;
    public int up_crop = 0;
    public int down_crop = 0;
    boolean high_pres = false;
    public static final int _LEFT = 0xF00000;
    public static final int _RIGHT_ = 0x0F0000;
    public static final int TOP_ = 0x0000F0;
    public static final int BOTTOM_ = 0x00000F;
    public static final int BAR = 0x000F00;
    public static final int LETTER = 0x00F000;
    public String shortName;
    public String fullName;
    public double theta = 0;
    /*
     * inset parameters
     */
    int internal_space = 3;
    int insetBorderColor = 0xFFFFFF;
    /**
     * we set the default inset position to top right
     */
    int INSET_POSITION = TOP_ | _RIGHT_;
    /**
     * Contains asterisks, squares or various ROIs
     */
    public ArrayList<Object> associatedObjects = new ArrayList<Object>();
    /*
     * Parameters for the insets
     */
    double fraction_of_parent_image_width = 0.25;

    /**
     *
     * @return the image inset (picture in picture)
     */
    public SerializableBufferedImage2 getInset() {
        return inset;
    }

    /**
     * Sets the image inset
     *
     * @param inset
     */
    public void setInset(SerializableBufferedImage2 inset) {
        this.inset = inset;
        /*
         * FIX for compatibility with older versions of the software where the function was missing
         */
        if (fraction_of_parent_image_width == 0) {
            fraction_of_parent_image_width = 0.25;
        }
    }

    public int getGelBorderColor() {
        return gelBorderColor;
    }

    public void setGelBorderColor(int gelBorderColor) {
        this.gelBorderColor = gelBorderColor & 0x00FFFFFF;
    }

    public int getGelBorderSize() {
        return gelBorderSize;
    }

    public void setGelBorderSize(int gelBorderSize) {
        this.gelBorderSize = gelBorderSize;
    }

    /**
     *
     * @return the original (non cropped) image
     */
    public SerializableBufferedImage2 getOriginalImage() {
        if (original != null) {
            return original;
        }
        return bimg;
    }

    /**
     *
     * @return a list of vectorial obejcts associated to the current image
     */
    public ArrayList<Object> getAssociatedObjects() {
        return associatedObjects;
    }

    public ArrayList<Object> cloneAssociatedObjects() {
        if (associatedObjects == null) {
            return null;
        } else {
            ArrayList<Object> cloned_extras = new ArrayList<Object>();
            for (Object f : associatedObjects) {
                if (f instanceof PARoi) {
                    cloned_extras.add(((PARoi) f).clone());
                }
            }
            return cloned_extras;
        }
    }

    public boolean checkStrokeSize(float desiredStrokeSize, boolean isIllustrator) {
        if (associatedObjects == null || associatedObjects.isEmpty()) {
            return true;
        }
        for (Object f : associatedObjects) {
            if (f instanceof PARoi && !(f instanceof MyPoint2D)) {
                float stroke = ((PARoi) f).getStrokeSize();
                if (Math.abs(desiredStrokeSize - stroke) >= 0.05) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a list of vectorial obejcts associated to the current image
     *
     * @param associatedShapes
     */
    public void setAssociatedObjects(ArrayList<Object> associatedShapes) {
        ArrayList<Object> cloned_associated_shapes = new ArrayList<Object>();
        if (associatedShapes != null) {
            for (Object object : associatedShapes) {
                if (object instanceof PARoi) {
                    cloned_associated_shapes.add(((PARoi) object).clone());
                }
            }
        }
        this.associatedObjects = cloned_associated_shapes;
    }

    /**
     * Associate a new object to the current image
     *
     * @param o
     */
    public void addAssociatedObject(Object o) {
        if (associatedObjects == null) {
            associatedObjects = new ArrayList<Object>();
        }
        associatedObjects.add(o);
    }

    /**
     *
     * @return the ImageJ macro code for the current image
     */
    public String produceMacroCode() {
        String macro_code = masterTagOpen;
        /*
         * now we add the curParams for the contained files
         */
        macro_code += parameters.get("src") + "\"" + fullName + "\" ";
        if (left_crop != 0) {
            macro_code += parameters.get("cropLeft") + "\"" + left_crop + "\" ";
        }
        if (right_crop != 0) {
            macro_code += parameters.get("cropRight") + "\"" + right_crop + "\" ";
        }
        if (up_crop != 0) {
            macro_code += parameters.get("cropTop") + "\"" + up_crop + "\" ";
        }
        if (down_crop != 0) {
            macro_code += parameters.get("cropBottom") + "\"" + down_crop + "\" ";
        }
        if (letter != null && letter.hasText()) {
            macro_code += parameters.get("LFormattedText") + "\"" + letter.getFormattedText() + "\" ";
        }
        if (upper_left_text != null && upper_left_text.hasText()) {
            macro_code += parameters.get("ULFormattedText") + "\"" + upper_left_text.getFormattedText() + "\" ";
        }
        if (lower_left_text != null && lower_left_text.hasText()) {
            macro_code += parameters.get("LLFormattedText") + "\"" + lower_left_text.getFormattedText() + "\" ";
        }
        if (upper_right_text != null && upper_right_text.hasText()) {
            macro_code += parameters.get("URFormattedText") + "\"" + upper_right_text.getFormattedText() + "\" ";
        }
        if (lower_right_text != null && lower_right_text.hasText()) {
            macro_code += parameters.get("LRFormattedText") + "\"" + lower_right_text.getFormattedText() + "\" ";
        }
        if (scale_bar_text != null && scale_bar_text.hasText()) {
            macro_code += parameters.get("SBFormattedText") + "\"" + scale_bar_text.getFormattedText() + "\" ";
        }
        if (scale_bar_size_in_px_of_the_real_image != 0) {
            macro_code += parameters.get("SBSize") + "\"" + scale_bar_size_in_px_of_the_real_image + "\" ";
        }
        /*
         * we finalise the tag
         */
        macro_code += "/>\n";
        return macro_code;
    }

    /**
     *
     * @return the image scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the image scale
     *
     * @param scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     *
     * @return the stroke size of the scalebar
     */
    public float getSCALE_BAR_STROKE_SIZE() {
        return SCALE_BAR_STROKE_SIZE;
    }

    /**
     * Set the stroke size for the scalebar
     *
     * @param SCALE_BAR_STROKE_SIZE
     */
    public void setSCALE_BAR_STROKE_SIZE(float SCALE_BAR_STROKE_SIZE) {
        this.SCALE_BAR_STROKE_SIZE = SCALE_BAR_STROKE_SIZE;
    }

    /**
     *
     * @return the formatted text at the lower left corner of the image
     */
    public ColoredTextPaneSerializable getLower_left_text() {
        return lower_left_text;
    }

    /**
     * Set the formatted text at the lower left corner of the image
     *
     * @param lower_left_text
     */
    public void setLower_left_text(ColoredTextPane lower_left_text) {
        this.lower_left_text = lower_left_text.export();
    }

    /**
     * Set the text at the lower left corner of the image
     *
     * @param lower_left_text
     */
    public void setLower_left_text(String lower_left_text) {
        this.lower_left_text = new ColoredTextPaneSerializable(lower_left_text);
    }

    /**
     *
     * @return the formatted text at the upper left corner of the image
     */
    public ColoredTextPaneSerializable getUpper_left_text() {
        return upper_left_text;
    }

    /**
     * Set the formatted text at the upper left corner of the image
     *
     * @param upper_left_txt
     */
    public void setUpper_left_text(ColoredTextPane upper_left_txt) {
        if (upper_left_text != null) {
            this.upper_left_text = upper_left_txt.export();
        }
    }

    /**
     * Set the text at the upper left corner of the image
     *
     * @param upper_left_text
     */
    public void setUpper_left_text(String upper_left_text) {
        this.upper_left_text = new ColoredTextPaneSerializable(upper_left_text);
    }

    /**
     *
     * @return the formatted text at the upper right corner of the image
     */
    public ColoredTextPaneSerializable getUpper_right_text() {
        return upper_right_text;
    }

    /**
     * Set the text at the upper right corner of the image
     *
     * @param upper_right_text
     */
    public void setUpper_right_text(String upper_right_text) {
        this.upper_right_text = new ColoredTextPaneSerializable(upper_right_text);
    }

    /**
     * Set the formatted text at the upper right corner of the image
     *
     * @param upper_right_text
     */
    public void setUpper_right_text(ColoredTextPane upper_right_text) {
        if (upper_right_text != null) {
            this.upper_right_text = upper_right_text.export();
        }
    }

    /**
     *
     * @return the formatted text at the lower right corner of the image
     */
    public ColoredTextPaneSerializable getLower_right_text() {
        return lower_right_text;
    }

    /**
     * Set the text at the lower right corner of the image
     *
     * @param lower_right_text
     */
    public void setLower_right_text(String lower_right_text) {
        this.lower_right_text = new ColoredTextPaneSerializable(lower_right_text);
    }

    /**
     * Set the formatted text at the lower right corner of the image
     *
     * @param lower_right_text
     */
    public void setLower_right_text(ColoredTextPane lower_right_text) {
        if (lower_right_text != null) {
            this.lower_right_text = lower_right_text.export();
        }
    }

    /**
     *
     * @return the formatted text for the letter
     */
    public ColoredTextPaneSerializable getLetter() {
        return letter;
    }

    public String getAssociatedComments() {
        return associatedComments;
    }

    public void setAssociatedComments(String associatedComments) {
        this.associatedComments = associatedComments;
    }

    /**
     * Recreate styleDocuments from html-like string
     */
    public void recreateStyledDoc() {
        letter.recreateStyledDoc();
        upper_left_text.recreateStyledDoc();
        upper_right_text.recreateStyledDoc();
        lower_left_text.recreateStyledDoc();
        lower_right_text.recreateStyledDoc();
        scale_bar_text.recreateStyledDoc();

        reloadAfterSerialization();
        /*
         * create the doc for the associated text
         */
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    ((MyPoint2D) object).recreateStyledDoc();
                }
            }
        }
    }

    /**
     * Get the ColoredTextPaneSerializable ready for serialization
     */
    public void getTextreadyForSerialization() {
        letter.getReadyForSerialization();
        upper_left_text.getReadyForSerialization();
        upper_right_text.getReadyForSerialization();
        lower_left_text.getReadyForSerialization();
        lower_right_text.getReadyForSerialization();
        scale_bar_text.getReadyForSerialization();
        /*
         * loads the doc for the associated text
         */
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    ((MyPoint2D) object).getReadyForSerialization();
                }
            }
        }
    }

    /**
     * Set the formatted text for letters
     *
     * @param letter
     */
    public void setLetter(ColoredTextPane letter) {
        if (letter != null) {
            this.letter = letter.export();
        }
    }

    /**
     * Set the text for letters
     *
     * @param letter
     */
    public void setLetter(String letter) {
//        Color tmp = null;
        if (this.letter == null || letter.equals(" ") || letter.equals("")) {
//            tmp = this.letter.getTextBgColor();
            this.letter = new ColoredTextPaneSerializable(letter);
        } else {
            this.letter.setText(letter);
        }
        //this.letter.setTextBgColor(tmp);
    }

    /**
     *
     * @return the formatted text over the scalebar
     */
    public ColoredTextPaneSerializable getScale_bar_txt() {
        return scale_bar_text;
    }

    /**
     * Sets the text over the scalebar
     *
     * @param scale_bar_txt
     */
    public void setScale_bar_txt(String scale_bar_txt) {
        this.scale_bar_text = new ColoredTextPaneSerializable(scale_bar_txt);
    }

    /**
     * Sets the formatted text over the scalebar
     *
     * @param scale_bar_txt
     */
    public void setScale_bar_txt(ColoredTextPane scale_bar_txt) {
        if (scale_bar_text != null) {
            this.scale_bar_text = scale_bar_txt.export();
        }
    }

    /**
     * recreate styleDocs if they don't exist
     */
    public void createIfNull() {
        if (letter != null) {
            if (letter.doc == null) {
                letter.recreateStyledDoc();
            }
        }
        if (upper_left_text != null) {
            if (upper_left_text.doc == null) {
                upper_left_text.recreateStyledDoc();
            }
        }
        if (upper_right_text != null) {
            if (upper_right_text.doc == null) {
                upper_right_text.recreateStyledDoc();
            }
        }
        if (lower_left_text != null) {
            if (lower_left_text.doc == null) {
                lower_left_text.recreateStyledDoc();
            }
        }
        if (lower_right_text != null) {
            if (lower_right_text.doc == null) {
                lower_right_text.recreateStyledDoc();
            }
        }
        if (scale_bar_text != null) {
            if (scale_bar_text.doc == null) {
                scale_bar_text.recreateStyledDoc();
            }
        }
    }

    public void setJournalStyle(JournalParameters jp, boolean applyToSVG, boolean applyToROIs, boolean applyToGraphs, boolean changePointSize, boolean isIllustrator, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        if (jp == null) {
            return;
        }
        createIfNull();
        letter.treatWiselyFirstLetterCapitalization(jp.getCapitalisationOfLetter());
        letter.setFontToAllText(jp.getLetterFont(), isOverrideItalic, isOverrideBoldForLetters);
        upper_left_text.setFontToAllText(jp.getUpperLeftTextFont(), isOverrideItalic, isOverRideBold);
        upper_right_text.setFontToAllText(jp.getUpperRightTextFont(), isOverrideItalic, isOverRideBold);
        lower_left_text.setFontToAllText(jp.getLowerLeftTextFont(), isOverrideItalic, isOverRideBold);
        lower_right_text.setFontToAllText(jp.getLowerRightTextFont(), isOverrideItalic, isOverRideBold);
        scale_bar_text.setFontToAllText(jp.getScaleBarTextFont(), isOverrideItalic, isOverRideBold);
        /*
         * apply to all extras of type point the new font
         */
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    ((MyPoint2D) object).setJournalStyle(jp);
                }
            }
        }

        if (applyToROIs) {
            setAssociatedShapeStrokeSize(jp.getObjectsStrokeSize());
        }
    }

    /**
     * Sets or changes the text, its color and the text background color
     *
     * @param color text color
     * @param bgColor text background color
     * @param ft text font
     * @param change_color override text color
     * @param change_bg_color override text background color
     * @param change_ft override text font
     */
    public void setTextAndBgColor(int color, Color bgColor, Font ft, boolean change_color, boolean change_bg_color, boolean change_ft, boolean isOverrideItalic, boolean isOverRideBold, boolean isOverrideBoldForLetters) {
        createIfNull();
        letter.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverrideBoldForLetters);//setFontToAllText(jp.getLetterFont());
        upper_left_text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold);//.setFontToAllText(jp.getUpperLeftTextFont());
        upper_right_text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold);//.setFontToAllText(jp.getUpperRightTextFont());
        lower_left_text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold);//.setFontToAllText(jp.getLowerLeftTextFont());
        lower_right_text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold);//.setFontToAllText(jp.getLowerRightTextFont());
        scale_bar_text.setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft, isOverrideItalic, isOverRideBold);//.setFontToAllText(jp.getScaleBarTextFont());

        /*
         * apply to all extras of type point the new font
         */
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    ((MyPoint2D) object).setTextAndBgColor(color, bgColor, ft, change_color, change_bg_color, change_ft);
                }
            }
        }
    }

    /**
     * Sets the text for the letter field
     *
     * @param string
     */
    public void setLetterText(String string) {
        letter.setText(string);
    }

    /**
     * Remove all the text over the current image
     */
    public void removeAllText() {
        createIfNull();
        letter.setText("");
        upper_left_text.setText("");
        upper_right_text.setText("");
        lower_left_text.setText("");
        lower_right_text.setText("");
        scale_bar_text.setText("");

        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            ArrayList<Object> textsToRemove = new ArrayList<Object>();
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    /**
                     * In fact all point in SF are text so the next check is
                     * completely useless except if I revive the possibility to
                     * draw points.
                     */
                    if (((MyPoint2D) object).text != null) {
                        textsToRemove.add(object);
                    }
                }
            }
            associatedObjects.removeAll(textsToRemove);
        }
    }

    /**
     * removes the scalebar from the current image
     */
    public void removeAllBars() {
        setScale_bar_size_in_px_of_the_real_image(0);
    }

    /**
     * removes all annotations
     */
    public void removeAllAnnotations() {
        if (associatedObjects != null) {
            associatedObjects.clear();
        } else {
            associatedObjects = new ArrayList<Object>();
        }
    }

    /**
     *
     * @return the current image
     */
    public BufferedImage getOriginalDisplay() {
        if (original != null) {
            return original.getBufferedImage();
        }
        if (bimg != null) {
            return bimg.getBufferedImage();
        }
        return null;
    }

    public BufferedImage getCurrentDisplay() {
        if (bimg != null) {
            return bimg.getBufferedImage();
        }
        return null;
    }

    public void extractImage(String name) {
        /*
         * we extract the original image
         */
        if (bimg != null || original != null) {
            String ext = ".png";
            String name_no_ext = name + "/" + getShortName().replace(":", "").replace("/", "_");
            String final_name = name_no_ext + ext;
            int counter = 0;
            if (new File(final_name).exists()) {
                do {
                    final_name = name_no_ext + "_" + CommonClassesLight.create_number_of_the_appropriate_size(counter++, 4) + ext;
                } while (new File(final_name).exists());
            }
            if (original != null) {
                SaverLight.save(original.getBufferedImage(), final_name);
            } else if (bimg != null) {
                SaverLight.save(bimg.getBufferedImage(), final_name);
            }
        }
        /*
         * we extract the PIP
         */
        if (inset != null) {
            String ext = ".png";
            String name_no_ext = name + "/" + getShortName().replace(":", "") + "_PIP";
            String final_name = name_no_ext + ext;
            int counter = 0;
            if (new File(final_name).exists()) {
                do {
                    final_name = name_no_ext + "_" + CommonClassesLight.create_number_of_the_appropriate_size(counter++, 4) + ext;
                } while (new File(final_name).exists());
            }
            SaverLight.save(inset.getBufferedImage(), final_name);
        }
    }

    public int getInsetBorderColor() {
        return insetBorderColor;
    }

    public void setInsetBorderColor(int insetBorderColor) {
        this.insetBorderColor = insetBorderColor;
    }

    public void setInsetInternalSpace(int space_between_images) {
        this.internal_space = space_between_images;
    }

    public int getInsetInternalSpace() {
        internal_space = internal_space == 0 ? 3 : internal_space;
        return internal_space;
    }

    public void setAssociatedShapeStrokeSize(float strokeSize) {
        if (strokeSize <= 0) {
            return;
        }
        if ((associatedObjects != null && !associatedObjects.isEmpty())) {
            for (Object object : associatedObjects) {
                if (object instanceof PARoi) {
                    ((PARoi) object).setStrokeSize(strokeSize);
                }
            }
        }
    }

    public void reloadAfterSerialization() {
        if (bimg != null) {
            bimg.reloadAfterSerialization();
        }
        if (original != null) {
            original.reloadAfterSerialization();
        }
        if (inset != null) {
            inset.reloadAfterSerialization();
        }
    }

    public void copyAllButImages(MyImage2D myImage2D) {
        this.left_crop = myImage2D.left_crop;
        this.right_crop = myImage2D.right_crop;
        this.up_crop = myImage2D.up_crop;
        this.down_crop = myImage2D.down_crop;
        this.associatedObjects = myImage2D.associatedObjects;
        this.strokeSize = myImage2D.strokeSize;
        this.letter = myImage2D.letter;
        this.scale_bar_size_in_px_of_the_real_image = myImage2D.scale_bar_size_in_px_of_the_real_image;
        this.scale_bar_text = myImage2D.scale_bar_text;
        this.lower_left_text = myImage2D.lower_left_text;
        this.lower_right_text = myImage2D.lower_right_text;
        this.upper_right_text = myImage2D.upper_right_text;
        this.upper_left_text = myImage2D.upper_left_text;
        this.scale = myImage2D.scale;
        this.SCALE_BAR_STROKE_SIZE = myImage2D.SCALE_BAR_STROKE_SIZE;
        this.scalebarColor = myImage2D.scalebarColor;
        crop(left_crop, right_crop, up_crop, down_crop);
        this.theta = myImage2D.theta;
        rotate(theta);
        this.inset = myImage2D.inset;
        this.INSET_POSITION = myImage2D.INSET_POSITION;
        this.scale_barSize_in_pixels_PIP = myImage2D.scale_barSize_in_pixels_PIP;
        this.scale = myImage2D.scale;
        this.scale_bar_size_in_unit = myImage2D.scale_bar_size_in_unit;
        this.size_of_one_px_in_unit = myImage2D.size_of_one_px_in_unit;
        this.scalebarColor = myImage2D.scalebarColor;
        this.gelBorderColor = myImage2D.gelBorderColor;
        this.gelBorderSize = myImage2D.gelBorderSize;
    }

    private void rotateCrops(int ORIENTATION) {
        switch (ORIENTATION) {
            case RIGHT:
                if (true) {
                    int tmp = up_crop;
                    up_crop = left_crop;
                    left_crop = down_crop;
                    down_crop = right_crop;
                    right_crop = tmp;
                }
                break;
            case LEFT:
                if (true) {
                    int tmp = up_crop;
                    up_crop = right_crop;
                    right_crop = down_crop;
                    down_crop = left_crop;
                    left_crop = tmp;
                }
                break;
        }
    }

    public void rotateLeft() {
        boolean success = false;
        if (bimg != null) {
            bimg.rotateLeft();
            success = true;
        }
        if (original != null) {
            original.rotateLeft();
            success = true;
        }
        if (success) {
            rotateCrops(LEFT);
        }
    }

    public void rotateRight() {
        boolean success = false;
        if (bimg != null) {
            bimg.rotateRight();
            success = true;
        }
        if (original != null) {
            original.rotateRight();
            success = true;
        }
        if (success) {
            rotateCrops(RIGHT);
        }
    }

    public void flip(String orientation) {
        if (bimg != null) {
            bimg.flip(orientation);
        }
        if (original != null) {
            original.flip(orientation);
        }
    }

    public void capitalizeText(String capitalization) {
        upper_left_text.treatWiselyFirstLetterCapitalization(capitalization);
        lower_left_text.treatWiselyFirstLetterCapitalization(capitalization);
        lower_right_text.treatWiselyFirstLetterCapitalization(capitalization);
        upper_right_text.treatWiselyFirstLetterCapitalization(capitalization);
    }

    public void capitalizeLetter(String capitalization) {
        letter.treatWiselyFirstLetterCapitalization(capitalization);
    }

    public BufferedImage getFormattedImageWithoutTranslation(boolean precise) {
        double x = rec2d.x;
        double y = rec2d.y;
        rec2d.x = 0;
        rec2d.y = 0;
        BufferedImage out = null;
        try {
            out = new MyBufferedImage((int) rec2d.width, (int) rec2d.height, BufferedImage.TYPE_INT_ARGB);
            int width = out.getWidth();
            int height = out.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    out.setRGB(i, j, 0x00000000);
                }
            }
            Graphics2D g2d = out.createGraphics();
            if (precise) {
                CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
            }
            drawAndFill(g2d);
            g2d.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rec2d.x = x;
            rec2d.y = y;
            return out;
        }
    }

    public void checkStyle() {
        BufferedImage snap = getFormattedImageWithoutTranslation(false);
        letter.checkStyle(snap);
        upper_left_text.checkStyle(snap);
        lower_left_text.checkStyle(snap);
        lower_right_text.checkStyle(snap);
        upper_right_text.checkStyle(snap);
        scale_bar_text.checkStyle(snap);
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    if (((MyPoint2D) object).text != null) {
                        ((MyPoint2D) object).text.checkStyle(snap);
                    }
                }
            }
        }
    }

    public void checkText(JournalParameters jp) {
//        createIfNull();
        //ca marche plutot bien et ca devrait meme marcher pr les graphes, etc
        //peut etre faire des clones
        //--> pr la capitalization de la first letter --> ignorer les lettres seules car ce sont 
        BufferedImage snap = getFormattedImageWithoutTranslation(false);
        letter.checkText(jp, snap);
        upper_left_text.checkText(jp, snap);
        lower_left_text.checkText(jp, snap);
        lower_right_text.checkText(jp, snap);
        upper_right_text.checkText(jp, snap);
        scale_bar_text.checkText(jp, snap);
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    if (((MyPoint2D) object).text != null) {
                        ((MyPoint2D) object).text.checkText(jp, snap);
                    }
                }
            }
        }
    }

    public void checkFonts(JournalParameters jp) {
//        createIfNull();
        BufferedImage snap = getFormattedImageWithoutTranslation(false);
        if (upper_left_text != null) {
            upper_left_text.checkFont(snap, jp.getUpperLeftTextFont(), jp.getLetterFontName());
        }
        if (lower_left_text != null) {
            lower_left_text.checkFont(snap, jp.getLowerLeftTextFont(), jp.getLowerLeftTextFontName());
        }
        if (lower_right_text != null) {
            lower_right_text.checkFont(snap, jp.getLowerRightTextFont(), jp.getLowerRightTextFontName());
        }
        if (upper_right_text != null) {
            upper_right_text.checkFont(snap, jp.getUpperRightTextFont(), jp.getUpperRightTextFontName());
        }
        if (scale_bar_text != null) {
            scale_bar_text.checkFont(snap, jp.getScaleBarTextFont(), jp.getScaleBarTextFontName());
        }
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof MyPoint2D) {
                    if (((MyPoint2D) object).text != null) {
                        ((MyPoint2D) object).text.checkFont(snap, jp.getOutterTextFont(), jp.getOutterTextFontName());
                    }
                }
            }
        }
        /*
         * now we perform the check for the capitalization of the first letter
         */
        if (letter != null) {
            letter.checkFont(snap, jp.getLetterFont(), jp.getLetterFontName());
            letter.checkCase(snap, jp.getCapitalisationOfLetter());
        }
    }

    public boolean hasLineArts() {
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (!(object instanceof MyPoint2D)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * a double precision MyImage2D
     */
    public static class Double extends MyImage2D implements Serializable {

        public static final long serialVersionUID = 1349852521760666160L;

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
        public Double(MyImage2D.Double myel) {
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
            return new MyImage2D.Double(this).setZpos(ZstackPos);
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

    public void setROIDrawOpacity(float opacity) {
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof Contourable) {
                    ((Contourable) object).setDrawOpacity(opacity);
                }
            }
        }
    }

    public void setROIFillOpacity(float opacity) {
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof Fillable) {
                    ((Fillable) object).setFillOpacity(opacity);
                }
            }
        }
    }

    /**
     *
     * @return the orientation of the image in radians
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Set the image orientation to the angle theta
     *
     * @param theta
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String name) {
        this.shortName = name;
    }

    @Override
    public String getFullName() {
        if (fullName != null) {
            return CommonClassesLight.change_path_separators_to_system_ones(fullName);
        }
        return null;
    }

    @Override
    public void setFullName(String name) {
        if (name != null) {
            this.fullName = CommonClassesLight.change_path_separators_to_system_ones(name);
        } else {
            fullName = null;
        }
    }

    /**
     *
     * @return the scale bar size (unit = pixels of the real image)
     */
    public double getScale_bar_size_in_px_of_the_real_image() {
        return scale_bar_size_in_px_of_the_real_image;
    }

    /**
     * Sets the size of the scale bar (in pixels of the real image)
     *
     * @param scale_bar_size_in_px
     */
    public void setScale_bar_size_in_px_of_the_real_image(double scale_bar_size_in_px) {
        this.scale_bar_size_in_px_of_the_real_image = scale_bar_size_in_px;
    }

    /**
     *
     * @return the conversion factor between pixels and arbitrary units
     */
    public double getSize_of_one_px_in_unit() {
        return size_of_one_px_in_unit;
    }

    /**
     * Sets the conversion factor between pixels and arbitrary units
     *
     * @param size_of_one_px_in_unit
     */
    public void setSize_of_one_px_in_unit(double size_of_one_px_in_unit) {
        this.size_of_one_px_in_unit = size_of_one_px_in_unit;
    }

    /**
     *
     * @return the scalebar size in any arbitrary units
     */
    public double getScale_bar_size_in_unit() {
        return scale_bar_size_in_unit;
    }

    /**
     * Sets the scalebar size in any arbitrary units
     *
     * @param scale_bar_size_in_unit
     */
    public void setScale_bar_size_in_unit(double scale_bar_size_in_unit) {
        this.scale_bar_size_in_unit = scale_bar_size_in_unit;
    }

    /**
     *
     * @return the color of the scalebar
     */
    public int getScalebarColor() {
        return scalebarColor;
    }

    /**
     * Sets the color of the scalebar
     *
     * @param scalebarColor
     */
    public void setScalebarColor(int scalebarColor) {
        this.scalebarColor = scalebarColor;
    }

    /**
     *
     * @param g2
     */
    public void createSelection(Graphics2D g2) {
        Shape cur_shp = rec2d;
        AlphaComposite myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
        g2.setComposite(myAlpha);
        createSelectedShape(g2, getBounds2D());
        g2.setStroke(new BasicStroke((float) getStrokeSize()));
        myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) getDrawOpacity());
        g2.setComposite(myAlpha);
        if (cur_shp != null) {
            g2.draw(cur_shp);
        }
    }

    private void createSelectedShape(Graphics2D g2D, Rectangle2D r) {
        double x = r.getX();
        double y = r.getY();
        double w = r.getWidth();
        double h = r.getHeight();
        g2D.setColor(Color.RED);
        g2D.setStroke(new BasicStroke(1.0f));
        g2D.fill(new Rectangle.Double(x + w * 0.5, y, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w - 6.0, y, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x, y + h * 0.5, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w - 6.0, y + h * 0.5, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x, y + h - 6.0, 6.0, 6.0));
        g2D.fill(new Rectangle.Double(x + w * 0.5, y + h - 6.0, 6.0, 6.0));
        int xCenter = (int) (x + w);
        int yCenter = (int) (y + h);
        int radius = 3;
        x = xCenter;
        y = yCenter;
        h = 0;
        w = 0;
        int step = 1;
        x -= radius;
        for (int counter = 0; counter < 4; counter++) {
            y -= (counter * step);
            h += (counter * step * 2);
            w += (counter * step * 2);
            g2D.drawArc((int) x, (int) y, (int) h, (int) w, 0, 180);
            x -= (counter * step * 2);
            y -= (counter * step);
            h += (counter * step * 2);
            w += (counter * step * 2);
            g2D.drawArc((int) x, (int) y, (int) h, (int) w, 180, 180);
        }
    }

    /**
     * Rescales the image NB: since it's for a scientific publication, we will
     * never change the aspect ratio
     *
     * @param rescaling_factor
     */
    public void rescale(double rescaling_factor) {
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, (double) bimg.getWidth() * rescaling_factor, (double) bimg.getHeight() * rescaling_factor);
        scale = rescaling_factor;
    }

    @Override
    public void scale(double factor) {
        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, rec2d.width * factor, rec2d.height * factor);
        scale = (double) bimg.getWidth() / rec2d.getWidth();//je pense que c'est correct mais y reflechir //--> ca ou l'inverse
    }

    /**
     * Set the image size the desired size
     *
     * @param width
     * @param height
     */
    public void setSize(double width, double height) {
        double ratio_x = width / (double) bimg.getWidth();
        double ratio_y = height / (double) bimg.getHeight();
        double rescaling_factor;
        if (ratio_x > ratio_y) {
            rescaling_factor = ratio_x;
        } else {
            rescaling_factor = ratio_y;
        }
        rescale(rescaling_factor);
    }

    /**
     *
     * @return the image aspect ratio
     */
    public double getAR() {
        return (double) getImageWidth() / (double) getImageHeight();
    }

    /**
     *
     * @return the width of the original image
     */
    public int getImageWidth() {
        return bimg.getWidth();
    }

    /**
     *
     * @return the height of the original image
     */
    public int getImageHeight() {
        return bimg.getHeight();
    }

    /**
     *
     * @param g2d
     */
    public void drawSimple(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.draw(rec2d);
        Line2D.Double l2d = new Line2D.Double(rec2d.x, rec2d.y, rec2d.x + rec2d.width, rec2d.y + rec2d.height);
        g2d.draw(l2d);
        l2d = new Line2D.Double(rec2d.x, rec2d.y + rec2d.height, rec2d.x + rec2d.width, rec2d.y);
        g2d.draw(l2d);
        g2dparams.restore(g2d);
    }

    public int getINSET_POSITION() {
        return INSET_POSITION;
    }

    public void setINSET_POSITION(int INSET_POSITION) {
        this.INSET_POSITION = INSET_POSITION;
    }

    public void setINSET_POSITION(int TOP_OR_BOTTOM, int LEFT_OR_RIGHT) {
        this.INSET_POSITION = TOP_OR_BOTTOM | LEFT_OR_RIGHT;
    }

    public double getFraction_of_parent_image_width() {
        return fraction_of_parent_image_width;
    }

    public void setFraction_of_parent_image_width(double fraction_of_parent_image_width) {
        this.fraction_of_parent_image_width = fraction_of_parent_image_width;
    }

    public int getScale_barSize_in_pixels_PIP() {
        return scale_barSize_in_pixels_PIP;
    }

    public void setScale_barSize_in_pixels_PIP(int scale_barSize_in_pixels_PIP) {
        this.scale_barSize_in_pixels_PIP = scale_barSize_in_pixels_PIP;
    }

    /**
     * hdrawAndFillwe draw the inset if it exists
     *
     * @param g2d
     */
    public void drawInset(Graphics2D g2d) {
        int ADDITIONAL_SPACE = 7;

        additional_space_up_left = 0;
        additional_space_up_right = 0;
        additional_space_down_left = 0;
        additional_space_down_right = 0;

        if (inset != null) {
            double rescaling_factor = (rec2d.width * fraction_of_parent_image_width) / (double) inset.getWidth();
            double image_height = ((inset.getHeight() * rescaling_factor));
            double image_width = ((inset.getWidth() * rescaling_factor));
            /*
             * bug fix for retrocompatibility
             */
            if (internal_space == 0) {
                internal_space = 3;
            }
            if (!(g2d instanceof MyGraphics2D) || ((g2d instanceof MyGraphics2D) && !((MyGraphics2D) g2d).isSVGPrecision())) {
                image_height = (int) image_height;
                image_width = (int) image_width;
                switch (INSET_POSITION) {
                    case TOP_ | _RIGHT_:
                        if (true) {
                            Rectangle2D.Double r = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - image_width), (int) rec2d.y, (int) (image_width), (int) (image_height));
                            Rectangle2D.Double border = new Rectangle2D.Double(r.x - internal_space, r.y, r.width + internal_space, r.height + internal_space);
                            g2d.setColor(new Color(insetBorderColor));
                            g2d.fill(border);
                            additional_space_up_right = (float) image_height + internal_space;
                            g2d.drawImage(inset.getBufferedImage(), (int) Math.round(rec2d.x + rec2d.getWidth() - image_width), (int) rec2d.y, (int) (image_width), (int) (image_height), null);
                            if (scale_barSize_in_pixels_PIP != 0) {
                                g2d.setColor(new Color(scalebarColor));
                                Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) rec2d.y + image_height - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE, (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                g2d.draw(rescaled_bar);
                                g2d.fill(rescaled_bar);
                            }
                        }
                        break;
                    case BOTTOM_ | _RIGHT_:
                        if (true) {
                            Rectangle2D.Double r = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - image_width), (int) Math.round(rec2d.y + rec2d.getHeight() - image_height), (int) (image_width), (int) (image_height));
                            Rectangle2D.Double border = new Rectangle2D.Double(r.x - internal_space, r.y - internal_space, r.width + internal_space, r.height + internal_space);
                            g2d.setColor(new Color(insetBorderColor));
                            g2d.fill(border);
                            additional_space_down_right = (float) image_height + internal_space;
                            g2d.drawImage(inset.getBufferedImage(), (int) Math.round(rec2d.x + rec2d.getWidth() - image_width), (int) Math.round(rec2d.y + rec2d.getHeight() - image_height), (int) (image_width), (int) (image_height), null);
                            if (scale_barSize_in_pixels_PIP != 0) {
                                g2d.setColor(new Color(scalebarColor));
                                Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) Math.round(rec2d.y + rec2d.getHeight() - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE), (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                g2d.draw(rescaled_bar);
                                g2d.fill(rescaled_bar);
                            }
                        }
                        break;
                    case BOTTOM_ | _LEFT:
                        if (true) {
                            Rectangle2D.Double r = new Rectangle2D.Double((int) rec2d.x, (int) (rec2d.y + rec2d.getHeight() - image_height), (int) (image_width), (int) (image_height));
                            Rectangle2D.Double border = new Rectangle2D.Double(r.x, r.y - internal_space, r.width + internal_space, r.height + internal_space);
                            g2d.setColor(new Color(insetBorderColor));
                            g2d.fill(border);
                            additional_space_down_left = (float) image_height + internal_space;
                            g2d.drawImage(inset.getBufferedImage(), (int) rec2d.x, (int) (rec2d.y + rec2d.getHeight() - image_height), (int) (image_width), (int) (image_height), null);
                            if (scale_barSize_in_pixels_PIP != 0) {
                                g2d.setColor(new Color(scalebarColor));
                                Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + image_width - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) Math.round(rec2d.y + rec2d.getHeight() - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE), (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                g2d.draw(rescaled_bar);
                                g2d.fill(rescaled_bar);
                            }
                        }
                        break;
                    /*case TOP_ | _LEFT:*/
                    default:
                        if (true) {
                            Rectangle2D.Double r = new Rectangle2D.Double((int) rec2d.x, (int) rec2d.y, (int) (image_width), (int) (image_height));
                            Rectangle2D.Double border = new Rectangle2D.Double(r.x, r.y, r.width + internal_space, r.height + internal_space);//en fait faire que ca soit l'espace entre images pour etre consistant
                            g2d.setColor(new Color(insetBorderColor));
                            g2d.fill(border);
                            additional_space_up_left = (float) image_height + internal_space;
                            g2d.drawImage(inset.getBufferedImage(), (int) rec2d.x, (int) rec2d.y, (int) (image_width), (int) (image_height), null);
                            if (scale_barSize_in_pixels_PIP != 0) {
                                g2d.setColor(new Color(scalebarColor));
                                Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + image_width - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) rec2d.y + image_height - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE, (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                g2d.draw(rescaled_bar);
                                g2d.fill(rescaled_bar);
                            }
                        }
                        break;
                }
            } else {
                MyGraphics2D tmp = ((MyGraphics2D) g2d);
                if (tmp.isSVGPrecision()) {
                    switch (INSET_POSITION) {
                        case TOP_ | _RIGHT_:
                            if (true) {
                                Rectangle2D.Double r = new Rectangle2D.Double((rec2d.x + rec2d.getWidth() - image_width), rec2d.y, image_width, image_height);
                                Rectangle2D.Double border = new Rectangle2D.Double(r.x - internal_space, r.y, r.width + internal_space, r.height + internal_space);
                                additional_space_up_right = (float) image_height + internal_space;
                                tmp.drawRectangle(border, insetBorderColor);
                                tmp.drawImage(inset.getBufferedImage(), (rec2d.x + rec2d.getWidth() - image_width), rec2d.y, (image_width), (image_height));
                                if (scale_barSize_in_pixels_PIP != 0) {
                                    g2d.setColor(new Color(scalebarColor));
                                    Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) rec2d.y + image_height - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE, (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                    g2d.draw(rescaled_bar);
                                    g2d.fill(rescaled_bar);
                                }
                            }
                            break;
                        case BOTTOM_ | _RIGHT_:
                            if (true) {
                                Rectangle2D.Double r = new Rectangle2D.Double((rec2d.x + rec2d.getWidth() - image_width), (rec2d.y + rec2d.getHeight() - image_height), image_width, image_height);
                                Rectangle2D.Double border = new Rectangle2D.Double(r.x - internal_space, r.y - internal_space, r.width + internal_space, r.height + internal_space);
                                additional_space_down_right = (float) image_height + internal_space;
                                tmp.drawRectangle(border, insetBorderColor);
                                tmp.drawImage(inset.getBufferedImage(), (rec2d.x + rec2d.getWidth() - image_width), (rec2d.y + rec2d.getHeight() - image_height), (image_width), (image_height));
                                if (scale_barSize_in_pixels_PIP != 0) {
                                    g2d.setColor(new Color(scalebarColor));
                                    Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + rec2d.getWidth() - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) Math.round(rec2d.y + rec2d.getHeight() - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE), (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                    g2d.draw(rescaled_bar);
                                    g2d.fill(rescaled_bar);
                                }
                            }
                            break;
                        case BOTTOM_ | _LEFT:
                            if (true) {
                                Rectangle2D.Double r = new Rectangle2D.Double(rec2d.x, (rec2d.y + rec2d.getHeight() - image_height), image_width, image_height);
                                Rectangle2D.Double border = new Rectangle2D.Double(r.x, r.y - internal_space, r.width + internal_space, r.height + internal_space);
                                additional_space_down_left = (float) image_height + internal_space;
                                tmp.drawRectangle(border, insetBorderColor);
                                tmp.drawImage(inset.getBufferedImage(), rec2d.x, (rec2d.y + rec2d.getHeight() - image_height), (image_width), (image_height));
                                if (scale_barSize_in_pixels_PIP != 0) {
                                    g2d.setColor(new Color(scalebarColor));
                                    Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + image_width - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) Math.round(rec2d.y + rec2d.getHeight() - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE), (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                    g2d.draw(rescaled_bar);
                                    g2d.fill(rescaled_bar);
                                }
                            }
                            break;
                        /*case TOP_ | _LEFT:*/
                        default:
                            if (true) {
                                Rectangle2D.Double r = new Rectangle2D.Double(rec2d.x, rec2d.y, image_width, image_height);
                                Rectangle2D.Double border = new Rectangle2D.Double(r.x, r.y, r.width + internal_space, r.height + internal_space);//en fait faire que ca soit l'espace entre images pour etre consistant
                                additional_space_up_left = (float) image_height + internal_space;
                                tmp.drawRectangle(border, insetBorderColor);
                                tmp.drawImage(inset.getBufferedImage(), rec2d.x, rec2d.y, (image_width), (image_height));
                                if (scale_barSize_in_pixels_PIP != 0) {
                                    g2d.setColor(new Color(scalebarColor));
                                    Rectangle2D.Double rescaled_bar = new Rectangle2D.Double((int) Math.round(rec2d.x + image_width - scale_barSize_in_pixels_PIP * rescaling_factor - additional_space_left_or_right - additional_space_left_or_right / 2.), (int) rec2d.y + image_height - ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE, (double) scale_barSize_in_pixels_PIP * rescaling_factor, SCALE_BAR_STROKE_SIZE);
                                    g2d.draw(rescaled_bar);
                                    g2d.fill(rescaled_bar);
                                }
                            }
                            break;
                    }
                }
            }
        }

    }

    @Override
    public void draw(Graphics2D g2d) {
        drawAndFill(g2d);
    }

    @Override
    public void drawAndFill(Graphics2D g2d) {
        G2dParameters g2dparams = new G2dParameters(g2d);
        g2d.setStroke(new BasicStroke(1));
        if (bimg != null) {
            if (!(g2d instanceof MyGraphics2D) || ((g2d instanceof MyGraphics2D) && !((MyGraphics2D) g2d).isSVGPrecision())) {
                /*
                 * ajout de rotation --> incomplete
                 */
//                if (left_crop+right_crop+up_crop+down_crop!=0)
//                {
//                    g2d.setClip(left_crop, right_crop, up_crop, down_crop);
//                }
//                if (theta!=0)
//                {
//                AffineTransform at = new AffineTransform();
//                at.translate(getX() - left_crop, getY() - right_crop);
//                at.rotate(theta, getWidth() / 2, getHeight() / 2); //--> ca marche en fait plutot bien
//                }
                /*
                 * end ajout de rotation
                 */
                g2d.drawImage(bimg.getBufferedImage(), (int) rec2d.x, (int) rec2d.y, (int) Math.round(rec2d.width), (int) Math.round(rec2d.height), null);
                if (gelBorderSize != 0) {
                    Area area = new Area(new Rectangle((int) rec2d.x, (int) rec2d.y, (int) Math.round(rec2d.width), (int) Math.round(rec2d.height)));
                    Rectangle2D.Double inner = new Rectangle2D.Double(((int) rec2d.x) + gelBorderSize, ((int) rec2d.y) + gelBorderSize, ((int) Math.round(rec2d.width)) - gelBorderSize * 2, ((int) Math.round(rec2d.height)) - gelBorderSize * 2.);
                    area.subtract(new Area(inner));
                    g2d.setColor(new Color(gelBorderColor));
                    g2d.fill(area);
                }
                drawInset(g2d);
                high_pres = false;
            } else {
                MyGraphics2D tmp = ((MyGraphics2D) g2d);
                if (tmp.isSVGPrecision()) {
                    tmp.drawImage(bimg.getBufferedImage(), rec2d.x, rec2d.y, rec2d.width, rec2d.height);
                    Area area = new Area(rec2d);
                    Rectangle2D.Double inner = new Rectangle2D.Double(rec2d.x + gelBorderSize, rec2d.y + gelBorderSize, rec2d.width - gelBorderSize * 2., rec2d.height - gelBorderSize * 2.);
                    area.subtract(new Area(inner));
                    g2d.setColor(new Color(gelBorderColor));
                    drawInset(g2d);
                    high_pres = true;
                }
            }
        }
        g2dparams.restore(g2d);
        if (associatedObjects != null && !associatedObjects.isEmpty()) {
            for (Object object : associatedObjects) {
                if (object instanceof PARoi) {
                    Object clone = ((PARoi) object).clone();
                    PARoi shape = (PARoi) clone;
                    Rectangle2D bounds = shape.getBounds2D();
                    shape.scale(1. / scale);
                    /*
                     * not needed anymore but keep in case I want to reactivate this for future development
                     */
                    shape.setFirstCorner(new Point2D.Double(getX() + (bounds.getX() - left_crop) / scale, getY() + (bounds.getY() - up_crop) / scale));
                    shape.drawAndFill(g2d);
                }
            }
        }
        addLetter(g2d);
        addScalebar(g2d);
        addTextLowerLeftCorner(g2d);
        addTextUpperRightCorner(g2d);
        addTextLowerRightCorner(g2d);
        addTextUpperLeftCorner(g2d);
        g2dparams.restore(g2d);
        high_pres = false;
    }

    @Override
    public void drawIfVisible(Graphics2D g2d, Rectangle visibleRect) {
        if (rec2d != null && visibleRect.intersects(rec2d.getBounds2D())) {
            drawAndFill(g2d);
        }
    }

    @Override
    public void drawIfVisibleWhenDragged(Graphics2D g2d, Rectangle visibleRect) {
        if (visibleRect.intersects(rec2d.getBounds2D())) {
            drawSimple(g2d);
        }
    }

    //modification for review 
    @Override
    public String logMagnificationChanges(double zoom) {
        String finalLog = "";
        double initialWidth = getImageWidth();
        double initialHeight = getImageHeight();
        String name = toString();
        double finalWidth = (getWidth() * zoom);
        double finalHeight = (getHeight() * zoom);
        /**
         * since I don't change the aspect ratio of images by default I just
         * need to calculate the magnification for one of the dimensions
         */
        double finalMagnificationComparedToOriginalImage = finalWidth / initialWidth;
        finalLog += "the image called '" + name + "' had an initial size of:" + initialWidth + "x" + initialHeight + " px; the image final size is: " + finalWidth + "x" + finalHeight + " --> the original image was therefore magnified: " + finalMagnificationComparedToOriginalImage + " time(s)" + "\n";
        if (Math.abs(finalMagnificationComparedToOriginalImage - 1.) >= 0.1) {
            /**
             * Magnification differs significantly from 1. --> warn about the
             * consequences
             */
            if (finalMagnificationComparedToOriginalImage - 1. > 0) {
                finalLog += "Your original image has been significantly oversampled " + finalMagnificationComparedToOriginalImage + " times --> pixels had to be interpolated (using the bicubic algorithm) to achieve the desired final resolution" + "\n";
            } else {
                finalLog += "Your original image has been significantly undersampled " + (1. / finalMagnificationComparedToOriginalImage) + " times --> some signal from the original image may have been lost, you may want to use a higher output resolution if permitted" + "\n";
            }
        }
        return finalLog;
    }

    public void addTextUpperLeftCorner(Graphics2D g2d) {
        if (upper_left_text == null || !upper_left_text.hasText()) {
            return;
        }
        ArrayList<AttributedString> as = upper_left_text.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = upper_left_text.simplify_text(as);
        draw(final_text, as, g2d, _LEFT, TOP_, translation_because_of_the_letter, upper_left_text.getTextBgColor());
    }

    public void addTextUpperRightCorner(Graphics2D g2d) {
        if (upper_right_text == null || !upper_right_text.hasText()) {
            return;
        }
        ArrayList<AttributedString> as = upper_right_text.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = upper_right_text.simplify_text(as);
        draw(final_text, as, g2d, _RIGHT_, TOP_, 0, upper_right_text.getTextBgColor());
    }

    public void addTextLowerLeftCorner(Graphics2D g2d) {
        if (lower_left_text == null || !lower_left_text.hasText()) {
            return;
        }
        ArrayList<AttributedString> as = lower_left_text.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = lower_left_text.simplify_text(as);
        draw(final_text, as, g2d, _LEFT, BOTTOM_, 0, lower_left_text.getTextBgColor());
    }

    public void addTextLowerRightCorner(Graphics2D g2d) {
        if (lower_right_text == null || !lower_right_text.hasText()) {
            return;
        }
        ArrayList<AttributedString> as = lower_right_text.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = lower_right_text.simplify_text(as);
        draw(final_text, as, g2d, _RIGHT_, BOTTOM_, -translation_because_of_the_scale_bar, lower_right_text.getTextBgColor());
    }

    public void addScalebar(Graphics2D g2d) {
        if (scale_bar_size_in_px_of_the_real_image == 0) {
            return;
        }
        ArrayList<AttributedString> as = scale_bar_text.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = scale_bar_text.simplify_text(as);
        draw(final_text, as, g2d, BAR, 0, 0, scale_bar_text.getTextBgColor());
    }

    public void addLetter(Graphics2D g2d) {
        if (letter == null || !letter.hasText()) {
            return;
        }
        ArrayList<AttributedString> as = letter.createAttributedString();
        ArrayList<ArrayList<MyFormattedString>> final_text = letter.simplify_text(as);
        draw(final_text, as, g2d, LETTER, 0, 0, letter.getTextBgColor());
    }

    /**
     *
     * @param as
     * @param g2d
     * @param ft
     * @return the bounds of the text to write over the image
     */
    public Rectangle2D getTextBounds(ArrayList<AttributedString> as, Graphics2D g2d, Font ft) {
        float y = 0;
        int min_x = Integer.MAX_VALUE;
        int min_y = Integer.MAX_VALUE;
        int max_x = Integer.MIN_VALUE;
        int max_y = Integer.MIN_VALUE;
        double width = 0;
        double height = 0;

        for (AttributedString attributedString : as) {
            AttributedCharacterIterator aci = attributedString.getIterator();
            if (aci == null || aci.getRunLimit() == 0) {
                continue;
            }
            TextLayout tl = new TextLayout(aci, g2d.getFontRenderContext());
            y += tl.getAscent();
            /*
             * bug fix for text containing spaces
             */
            Rectangle r4 = tl.getBlackBoxBounds(aci.getBeginIndex(), aci.getEndIndex()).getBounds();//tl.getPixelBounds(g2d.getFontRenderContext(), 0, y);
            y += tl.getDescent() + tl.getLeading();
            height = y;
            width = Math.max(width, r4.getWidth() + r4.getX());

            max_x = Math.max(max_x, r4.x + r4.width);
            max_y = Math.max(max_y, r4.y + r4.height);
            min_x = Math.min(min_x, r4.x);
            min_y = Math.min(min_y, r4.y);
        }
        Rectangle2D.Double r = new Rectangle2D.Double(width, height, max_x - min_x, max_y - min_y);
        return r;
    }

    /**
     *
     * @param text
     * @param as
     * @param g2d
     * @param LEFT_OR_RIGHT_ALIGNMENT
     * @param TOP_OR_BOTTOM
     * @param addtional_y_shift
     * @param bg_color
     * @return the position after the text has been drawn
     */
    public double draw(ArrayList<ArrayList<MyFormattedString>> text, ArrayList<AttributedString> as, Graphics2D g2d, int LEFT_OR_RIGHT_ALIGNMENT, int TOP_OR_BOTTOM, double addtional_y_shift, Color bg_color) {

        if (text.isEmpty()) {
            return 0;
        }
        g2d.setFont(text.get(0).get(0));
        Rectangle2D bounds = getTextBounds(as, g2d, g2d.getFont());
        switch (LEFT_OR_RIGHT_ALIGNMENT | TOP_OR_BOTTOM) {
            case LETTER:
            case TOP_ | _LEFT:
                if (true) {
                    Rectangle2D rect;
                    rect = getBounds2D();

                    float x = (float) rect.getX() + additional_space_left_or_right;
                    float x_backup = x;
                    float y = (float) (rect.getY() + addtional_y_shift + additional_space_up_left);

                    bounds = new Rectangle2D.Double(x - additional_space_left_or_right, y, bounds.getX() + 2. * additional_space_left_or_right + additional_space_left_or_right / 2., bounds.getY());
                    if (bg_color != null) {
                        g2d.setColor(bg_color);
                        g2d.fill(bounds);
                    }

                    int pos = 0;
                    for (ArrayList<MyFormattedString> arrayList : text) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        /*
                         * we ignore line breaks see if I can find a solution some day to take them into account
                         */
                        if (aci == null || aci.getRunLimit() == 0) {
                            pos++;
                            continue;
                        }

                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;

                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y + ascent);
                            x += curpos.getWidth();
                        }
                        pos++;
                        x = x_backup;
                        y += ascent + descent + leading;
                    }
                    if ((LEFT_OR_RIGHT_ALIGNMENT | TOP_OR_BOTTOM) == LETTER) {
                        translation_because_of_the_letter = bounds.getHeight();
                    }
                }
                break;
            case TOP_ | _RIGHT_:
                if (true) {
                    Rectangle2D rect;
                    rect = getBounds2D();
                    float x = (float) (rect.getX() + rect.getWidth() - additional_space_left_or_right); //getBounds().getWidth()); //--> ca marche --> il me faut dc ca
                    float x_backup = x;
                    float y = (float) rect.getY() + additional_space_up_right;
                    int pos = 0;
                    bounds = new Rectangle2D.Double(x - bounds.getX() - additional_space_left_or_right - additional_space_left_or_right / 2., y, bounds.getX() + 2 * additional_space_left_or_right + additional_space_left_or_right / 2., bounds.getY());
                    if (bg_color != null) {
                        g2d.setColor(bg_color);
                        g2d.fill(bounds);
                    }
                    for (ArrayList<MyFormattedString> arrayList : text) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        /*
                         * we ignore line breaks see if I can find a solution some day to take them into account
                         */
                        if (aci == null || aci.getRunLimit() == 0) {
                            pos++;
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        x = x_backup;
                        x -= tl.getAdvance();
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;

                        pos++;
                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y + ascent);
                            x += curpos.getWidth();
                        }
                        y += ascent + descent + leading;
                    }
                }
                break;
            case BOTTOM_ | _LEFT:
                if (true) {
                    Rectangle2D rect;
                    rect = getBounds2D();
                    float x = (float) rect.getX() + additional_space_left_or_right;
                    float x_backup = x;
                    float y = (float) (rect.getY() + rect.getHeight() - additional_space_down_left);
                    bounds = new Rectangle2D.Double(x - additional_space_left_or_right, y - bounds.getY(), bounds.getX() + 2 * additional_space_left_or_right + additional_space_left_or_right / 2., bounds.getY());
                    if (bg_color != null) {
                        g2d.setColor(bg_color);
                        g2d.fill(bounds);
                    }
                    int pos = as.size() - 1;
                    for (int i = as.size() - 1; i >= 0; i--, pos--) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        /*
                         * we ignore line breaks see if I can find a solution some day to take them into account
                         */
                        if (aci == null || aci.getRunLimit() == 0) {
                            pos++;
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        ArrayList<MyFormattedString> arrayList = text.get(i);
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;
                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y - descent);
                            x += curpos.getWidth();
                        }
                        x = x_backup;
                        y -= ascent + descent + leading;
                    }
                }
                break;
            case BOTTOM_ | _RIGHT_:
                if (true) {
                    Rectangle2D rect;
                    rect = getBounds2D();
                    float x = (float) (rect.getX() + rect.getWidth()) - additional_space_left_or_right; //getBounds().getWidth()); //--> ca marche --> il me faut dc ca
                    float x_backup = x;
                    float y = (float) (rect.getY() + rect.getHeight() + addtional_y_shift - additional_space_down_right);
                    int pos = as.size() - 1;
                    bounds = new Rectangle2D.Double(x - bounds.getX() - additional_space_left_or_right - additional_space_left_or_right / 2., y - bounds.getY(), bounds.getX() + 2 * additional_space_left_or_right + additional_space_left_or_right / 2., bounds.getY());
                    if (bg_color != null) {
                        g2d.setColor(bg_color);
                        g2d.fill(bounds);
                    }
                    for (int i = as.size() - 1; i >= 0; i--, pos--) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        /*
                         * we ignore line breaks see if I can find a solution some day to take them into account
                         */
                        if (aci == null || aci.getRunLimit() == 0) {
                            pos++;
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        ArrayList<MyFormattedString> arrayList = text.get(i);
                        x = x_backup;
                        x -= tl.getAdvance();
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;
                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y - descent);
                            x += curpos.getWidth();
                        }
                        y -= ascent + descent + leading;
                    }
                }
                break;
            case BAR:
                if (true) {
                    Rectangle2D rect;
                    rect = getBounds2D(); //--> les bounds du rect
                    if (scale_bar_size_in_px_of_the_real_image == 0) {
                        translation_because_of_the_scale_bar = 0;
                        return 0;
                    }
                    float MORE_ADDITIONAL_SPACE = 4;
                    float BAR_ADDITIONAL_SPACE = 7;
                    float x = (float) (rect.getX() + rect.getWidth() - additional_space_left_or_right);
                    float y = (float) (rect.getY() + rect.getHeight() - additional_space_down_right);
                    double x_bar_begin = (x - (scale_bar_size_in_px_of_the_real_image / scale) - BAR_ADDITIONAL_SPACE);
                    /*
                     * FIX for scale bar position when SCALE_BAR_STROKE_SIZE != 3f --> (- SCALE_BAR_STROKE_SIZE / 2f);
                     */
                    double y_bar_begin = y - BAR_ADDITIONAL_SPACE - SCALE_BAR_STROKE_SIZE / 2f;
                    double x_bar_end = x - BAR_ADDITIONAL_SPACE;
                    double y_bar_end = y - BAR_ADDITIONAL_SPACE;
                    /*
                     * here we have a scale bar rectangle
                     */
                    Rectangle2D.Double r2d2 = new Rectangle2D.Double(x_bar_begin, y_bar_begin - SCALE_BAR_STROKE_SIZE / 2., x_bar_end - x_bar_begin, SCALE_BAR_STROKE_SIZE);
                    y -= (SCALE_BAR_STROKE_SIZE + BAR_ADDITIONAL_SPACE + MORE_ADDITIONAL_SPACE);
                    /*
                     * bug fix for text position of bounding box (removed unnecessary MORE_ADDITIONAL_SPACE)
                     */
                    bounds = new Rectangle2D.Double(x - bounds.getX() - additional_space_left_or_right, y - bounds.getY(), bounds.getX() + 2. * additional_space_left_or_right, bounds.getY());
                    int pos = as.size() - 1;
                    double additional_trans_x = (bounds.getX() + bounds.getWidth() / 2.) - (r2d2.getX() + r2d2.getWidth() / 2.);
                    if (scale_bar_text.hasText()) {
                        if (bounds.getCenterX() < r2d2.getCenterX()) {
                            r2d2.x += additional_trans_x;
                        } else {
                            bounds = new Rectangle2D.Double(bounds.getX() - additional_trans_x, bounds.getY(), bounds.getWidth(), bounds.getHeight());
                            x -= additional_trans_x;
                        }
                    }
                    float x_backup = x;
                    if (bg_color != null) {
                        g2d.setColor(bg_color);
                        g2d.fill(bounds);
                    }
                    for (int i = as.size() - 1; i >= 0; i--, pos--) {
                        AttributedCharacterIterator aci = as.get(pos).getIterator();
                        /*
                         * we ignore line breaks see if I can find a solution some day to take them into account
                         */
                        if (aci == null || aci.getRunLimit() == 0) {
                            pos++;
                            continue;
                        }
                        TextLayout tl = new TextLayout(as.get(pos).getIterator(), g2d.getFontRenderContext());
                        ArrayList<MyFormattedString> arrayList = text.get(i);
                        x = x_backup;
                        x -= tl.getAdvance();
                        float additional_trans = (float) ((x + tl.getBounds().getWidth() / 2.) - ((x + (bounds.getWidth() - additional_space_left_or_right * 2.) / 2.)));
                        x += additional_trans;
                        float ascent = 0;
                        float descent = 0;
                        float leading = 0;
                        for (MyFormattedString myFormattedString : arrayList) {
                            g2d.setColor(myFormattedString.getFgColor());
                            g2d.setFont(myFormattedString);
                            ascent = tl.getAscent();
                            descent = tl.getDescent();
                            leading = tl.getLeading();
                            Rectangle2D curpos = myFormattedString.draw(g2d, x, y - descent);
                            x += curpos.getWidth();
                        }
                        y -= ascent + descent + leading;
                    }
                    if (scale_bar_text.hasText()) {
                        translation_because_of_the_scale_bar = (BAR_ADDITIONAL_SPACE + MORE_ADDITIONAL_SPACE + MORE_ADDITIONAL_SPACE / 2 + SCALE_BAR_STROKE_SIZE + bounds.getHeight());//BAR_ADDITIONAL_SPACE + SCALE_BAR_STROKE_SIZE + bounds.getHeight()//y-curpos.getHeight();//y=(int)(y_bar_end - 4-curpos.getHeight());
                    } else {
                        translation_because_of_the_scale_bar = BAR_ADDITIONAL_SPACE + MORE_ADDITIONAL_SPACE / 2 + SCALE_BAR_STROKE_SIZE;
                    }
                    /*
                     * useless since SF v1.5 but keep for retro-compatibility with SF v1.0
                     */
                    if (scalebarColor == 0) {
                        scalebarColor = 0xFFFFFFFF;
                    }
                    g2d.setColor(new Color(scalebarColor));
                    g2d.draw(r2d2);
                    g2d.fill(r2d2);
                    return translation_because_of_the_scale_bar;
                }
                break;
        }
        return 0;
    }

    /**
     *
     * @param as
     * @param g2d
     * @return the bounds of the text
     */
    public Rectangle2D getBounds2D(ArrayList<AttributedString> as, Graphics2D g2d) {
        Rectangle2D masterrect;
        float x = 0;
        float y = 0;
        double min_x = Integer.MAX_VALUE;
        double max_x = Integer.MIN_VALUE;
        double min_y = Integer.MAX_VALUE;
        double max_y = Integer.MIN_VALUE;
        for (AttributedString attributedString : as) {
            TextLayout tl = new TextLayout(attributedString.getIterator(), g2d.getFontRenderContext());
            y += tl.getAscent();
            Rectangle test = tl.getPixelBounds(g2d.getFontRenderContext(), x, y);
            min_x = Math.min(min_x, test.x);
            max_x = Math.max(max_x, test.x + test.width);
            min_y = Math.min(min_y, test.y);
            max_y = Math.max(max_y, test.y + test.height);
            y += tl.getDescent() + tl.getLeading();
        }
        masterrect = new Rectangle2D.Double(min_x, min_y, max_x, max_y);
        return masterrect;
    }

    @Override
    public void setShapeWidth(double width, boolean keepAR) {
        if (!keepAR) {
            rec2d.width = width;
        } else {
            rec2d.width = width;
            scale = (double) bimg.getWidth() / rec2d.getWidth();
            rec2d.height = (double) bimg.getHeight() / scale;
        }
    }

    @Override
    public void setShapeHeight(double height, boolean keepAR) {
        if (!keepAR) {
            rec2d.height = height;
        } else {
            rec2d.height = height;
            scale = (double) bimg.getHeight() / rec2d.getHeight();
            rec2d.width = (double) bimg.getWidth() / scale;
        }
    }

    @Override
    public void flipHorizontally() {
//        bimg.flip("horizontally");
    }

    @Override
    public void flipVertically() {
//        bimg.flip("vertically");
    }

    @Override
    public String getShapeName() {
        return "Image";
    }

    /**
     *
     * @return true if the image is cropped
     */
    public boolean isCropped() {
        return isCropped;
    }

    /**
     * Sets whether the image should be cropped
     *
     * @param isCropped
     */
    public void setIsCropped(boolean isCropped) {
        this.isCropped = isCropped;
    }

    /**
     *
     * @return the amount of pixels that must be cropped from the left side
     */
    public int getLeft_crop() {
        return left_crop;
    }

    /**
     * Sets the amount of pixels that must be cropped on the left side
     *
     * @param left_crop
     */
    public void setLeft_crop(int left_crop) {
        this.left_crop = left_crop;
    }

    /**
     *
     * @return the amount of pixels that must be cropped from the right side
     */
    public int getRight_crop() {
        return right_crop;
    }

    /**
     * Sets the amount of pixels that must be cropped on the right
     *
     * @param right_crop
     */
    public void setRight_crop(int right_crop) {
        this.right_crop = right_crop;
    }

    /**
     *
     * @return the amount of pixels that must be cropped from the top
     */
    public int getUp_crop() {
        return up_crop;
    }

    /**
     * Sets the amount of pixels that must be cropped from the top
     *
     * @param up_crop
     */
    public void setUp_crop(int up_crop) {
        this.up_crop = up_crop;
    }

    /**
     *
     * @return the amount of pixels that must be cropped from the bottom
     */
    public int getDown_crop() {
        return down_crop;
    }

    /**
     * Sets the amount of pixels that must be cropped from the bottom
     *
     * @param down_crop
     */
    public void setDown_crop(int down_crop) {
        this.down_crop = down_crop;
    }

    /**
     * Crops images
     *
     * @param left
     * @param right
     * @param up
     * @param down
     */
    public void crop(int left, int right, int up, int down) {
        if (!isCropped && !isRotated && bimg != null) {
            BufferedImage tmp = bimg.getBufferedImage();
            if (tmp != null) {
                original = new SerializableBufferedImage2(tmp);
            }
        }
        this.left_crop = left;
        this.right_crop = right;
        this.up_crop = up;
        this.down_crop = down;
        if (left == 0 && right == 0 && up == 0 && down == 0 && theta == 0) {
            bimg = original;
            original = null;
            isCropped = false;
            crop_and_rotate();
            return;
        }
        isCropped = true;
        crop_and_rotate();
    }

    @Override
    public void rotate(double angleInDegrees) {
        if (!isCropped && !isRotated && bimg != null) {
            original = new SerializableBufferedImage2(bimg.getBufferedImage());
        }
        theta = angleInDegrees;
        if (theta == 0. && left_crop == 0 && right_crop == 0 && up_crop == 0 && down_crop == 0) {
            bimg = original;
            original = null;
            isRotated = false;
            crop_and_rotate();
            return;
        }
        isRotated = true;
        crop_and_rotate();
    }

//    public int getCroppedWidth() {
//        if (original == null) {
//            return -1;
//        }
//        return original.getWidth() - left_crop - right_crop;
//    }
//
//    public int getCroppedHeight() {
//        if (original == null) {
//            return -1;
//        }
//        return original.getHeight() - up_crop - down_crop;
//    }

    /*
     * tres crade mais ca marche un jour gerer qd meme la taille
     * TODO gerer la co rotation des objects vectoriels
     * faire un changement de version de soft avec la release de la rotation
     * si il y a rotation il faut tourner aussi toutes les shapes vectorielles --> tres simple je pense juste ajouter une at au truc mais y reflechir un peu sinon loader l'image en cours au lieu de loader l'original --> en fait ca serait plus simple
     */
    private void crop_and_rotate() {
        if (original == null) {
            isCropped = false;
            isRotated = false;
            return;
        }
        BufferedImage tmp = original.getBufferedImage();
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage rotated = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if (theta != 0) {
            Graphics2D g2d = rotated.createGraphics();
            g2d = CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
            AffineTransform at = new AffineTransform();
            /*
             * rotation bug fix for images that are not squares
             */
            int cropped_width = width - left_crop - right_crop;
            int cropped_height = height - up_crop - down_crop;
            int centerx_crop = left_crop + cropped_width / 2;
            int centery_crop = up_crop + cropped_height / 2;
            at.rotate(Math.toRadians(theta), centerx_crop, centery_crop);
            /* previous code
             * //width / 2, height / 2); //--> ca marche en fait plutot bien
             * */
            g2d.setTransform(at);
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            bimg = new SerializableBufferedImage2(rotated);
        } else {
            bimg = new SerializableBufferedImage2(original.getBufferedImage());
        }
        if (left_crop != 0 || right_crop != 0 || up_crop != 0 || down_crop != 0) {
            BufferedImage tmp2 = bimg.getBufferedImage();
            int final_width = width - left_crop - right_crop;
            int final_height = height - up_crop - down_crop;
            if (final_width <= 0) {
                left_crop = 0;
                right_crop = 0;
                final_width = width;
            }
            if (final_height <= 0) {
                up_crop = 0;
                right_crop = 0;
                final_height = height;
            }
            if (final_width != width || final_height != height) {
                BufferedImage cropped = new BufferedImage(final_width, final_height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d2 = cropped.createGraphics();
                g2d2.drawImage(tmp2, 0, 0, width - left_crop - right_crop, height - up_crop - down_crop, left_crop, up_crop, width - right_crop, height - down_crop, null);
                g2d2.dispose();
                bimg = new SerializableBufferedImage2(cropped);
            }
        }
        /*
         * why do I reset the scale ???
         */
        /**
         * bug fix for size error after rotate or crop
         */
//        scale = 1.;
//        rec2d = new Rectangle2D.Double(rec2d.x, rec2d.y, bimg.getWidth(), bimg.getHeight());
    }

    /**
     * Rebuild a myImage2D object from an ImageJ macro
     *
     * @param txt
     * @return a set of parameters
     */
    public HashMap<String, String> reparseMacro(String txt) {
        ArrayList<String> splitters = new ArrayList<String>(parameters.keySet());
        HashMap<String, String> detected_parameters = new HashMap<String, String>();
        for (String string : splitters) {
            if (txt.contains(parameters.get(string))) {
                String curParams = CommonClassesLight.strCutRightFisrt(txt, parameters.get(string));
                if (curParams.contains(" data-")) {
                    curParams = CommonClassesLight.strCutRightFisrt(CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutLeftFirst(curParams, " data-"), "\""), "\"");
                } else {
                    curParams = CommonClassesLight.strCutRightFisrt(CommonClassesLight.strCutLeftLast(curParams, "\""), "\"");
                }
                detected_parameters.put(string, curParams);
            }
        }
        return detected_parameters;
    }

    /**
     * Dispatches IJ macro parameters
     *
     * @param macro_parameters
     * @param ignore_image
     */
    public void parameterDispatcher(HashMap<String, String> macro_parameters, boolean ignore_image) {
        rec2d = new Rectangle2D.Double();
        letter = new ColoredTextPaneSerializable("");
        scale_bar_text = new ColoredTextPaneSerializable("");
        upper_left_text = new ColoredTextPaneSerializable("");
        upper_right_text = new ColoredTextPaneSerializable("");
        lower_left_text = new ColoredTextPaneSerializable("");
        lower_right_text = new ColoredTextPaneSerializable("");
        ArrayList<String> keys = new ArrayList<String>(macro_parameters.keySet());
        for (String string : keys) {
            if (!ignore_image && string.equals("src")) {
                String name = macro_parameters.get(string);
                if (!new File(name).exists()) {
                    return;
                }
                this.bimg = new SerializableBufferedImage2(new Loader().load(name));
                this.rec2d = new Rectangle2D.Double(0, 0, bimg.getWidth(), bimg.getHeight());
                this.fullName = name;
                this.shortName = CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutRightLast(CommonClassesLight.change_path_separators_to_system_ones(name), "/"), ".");
                continue;
            }
            if (string.equals(("LFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.letter = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    letter.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    letter.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("ULFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.upper_left_text = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    upper_left_text.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    upper_left_text.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("URFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.upper_right_text = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    upper_right_text.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    upper_right_text.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("LRFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.lower_right_text = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    lower_right_text.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    lower_right_text.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("LLFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.lower_left_text = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    lower_left_text.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    lower_left_text.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("SBFormattedText"))) {
                String formatted_text = macro_parameters.get(string);
                this.lower_left_text = new ColoredTextPaneSerializable(formatted_text);
                if (formatted_text.contains("<")) {
                    lower_left_text.setSerializedStyledDocumentContent(macro_parameters.get(string));
                    lower_left_text.recreateStyledDoc();
                }
                continue;
            }
            if (string.equals(("cropLeft"))) {
                String value = macro_parameters.get(string);
                try {
                    this.left_crop = (Integer.parseInt(value));
                } catch (Exception e) {
                }
                continue;
            }
            if (string.equals(("cropRight"))) {
                String value = macro_parameters.get(string);
                try {
                    this.right_crop = (Integer.parseInt(value));
                } catch (Exception e) {
                }
                continue;
            }
            if (string.equals(("cropBottom"))) {
                String value = macro_parameters.get(string);
                try {
                    this.down_crop = (Integer.parseInt(value));
                } catch (Exception e) {
                }
                continue;
            }
            if (string.equals(("cropTop"))) {
                String value = macro_parameters.get(string);
                try {
                    this.up_crop = (Integer.parseInt(value));
                } catch (Exception e) {
                }
                continue;
            }
            this.scale_bar_size_in_px_of_the_real_image = 0;
            if (string.equals(("data-SBSize"))) {
                String value = macro_parameters.get(string);
                try {
                    this.scale_bar_size_in_px_of_the_real_image = (Integer.parseInt(value));
                } catch (Exception e) {
                }
                continue;
            }
        }
    }

    /*
     * this override allows me to directly images to a Jlist --> easier to handle
     */
    @Override
    public String toString() {
        String letter_ = "";
        if (getLetter() != null) {
            letter_ = ":" + getLetter().getText();
            if (letter_.endsWith(":")) {
                letter_ = "";
            }
        }
        return shortName + letter_;
    }

    @Override
    public void drawSelection(Graphics2D g2d, Rectangle visRect) {
        Rectangle2D.Double rec = (Rectangle2D.Double) getBounds2D();
        if (rec.intersects(visRect)) {
            G2dParameters g2dParams = new G2dParameters(g2d);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.setColor(Color.red);
            g2d.draw(rec);
//            Line2D.Double diag1 = new Line2D.Double(rec.x, rec.y, rec.x + rec.width, rec.y + rec.height);
//            Line2D.Double diag2 = new Line2D.Double(rec.x + rec.width, rec.y, rec.x, rec.drawAndFillrec.height);
//            g2d.ddrawAndFilldiag1);            
//            g2d.draw(diag2);
            g2dParams.restore(g2d);
        }
    }

    @Override
    public boolean isRotable() {
        return false;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        if (true) {
            //--> on y est presque mais gerer le fait que les images puissent avoir != AR
            //gerer le bg derriere ???

            MyImage2D.Double img = new MyImage2D.Double(0, 0, new Loader().load("/D/sample_images_PA/trash_test_mem/egg_chambers/Series010.png"));
            //--> en effet il y a un pb d'AR --> a calculer
            BufferedImage pip = new Loader().load("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/toto.png");//PIP //focused_Series012.png //real_imageJ_image.tif
            img.scale(0.5);
            //--> TODO gerer les erreurs d'arrondi derriere --> puis ce sera tt fini apres plus que la position du texte a gerer
            //--> permettre a tout les textes de bouger en fonction de la taille de l'image
//            img.set(new SerializableBufferedImage2(pip)); //--> draw as a fraction of the original image in width and keep AR --> rather simple
            //aussi ajouter un rectangle derriere --> a faire
//            img.setINSET_POSITION(img.TOP_, img._RIGHT_);
            img.setINSET_POSITION(Double.TOP_, Double._LEFT);
//            img.setINSET_POSITION(img.BOTTOM_, img._LEFT);
//            img.setINSET_POSITION(img.BOTTOM_, img._RIGHT_);
            //--> ca marche presque mais de petites erreurs de rounding existent --> essayer de fixer ca
            //peut etre gerer le carre a dessiner derrier a l'aide d'un floating point ???
            //ou alors toujours le mettre fixe à une certaine valeur, je pense que 3 px est ce que je veux
            img.setFraction_of_parent_image_width(0.3); //ou alors lui mettre une width fixe en pixels --> plus simple a gerer mais moins fun a coder //--> ? dessiner a l'echelle ???
            //--> ca a l'air bon mais gerer maintenant la taille de la border en pixels de l'image originale ???? --> a tester puis gerer la position du texte
            //ne pas permettre de PIP pr les vectoriels et ne pas permettre aux images vectorielles d'etres utilisees en pip
            img.setLetter("toto qsdqsd qsd qsd "); //--> sinon permettre de bouger le texte //--> assez simple?
            //juste ajouter une fraction de la taille initiale de l'image pour que ca donne bien puis dessiner une border --> reflechir un peu
            //mais devrait pas etre trop dur je pense

            //ca marche mais juste ajouter une border autour et aussi gerer la taille de cette image --> a gerer //--> peut etre demander le % de taille de l'image parente que doit faire le pip a la construction
            //--> pas trop dur je pense
            BufferedImage out = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = out.createGraphics();
            //ca a l'air de marcher mais gerer ca mieux si possible
//            img.rotate(Math.toRadians(23)); //--> erreur de trans
//            img.setFidrawAndFillorner(new Point2D.Double(128, 64));
            img.draw(g2d);
            g2d.setColor(Color.YELLOW);
            img.drawSelection(g2d, new Rectangle(-100, -100, 2000, 2000));
            System.out.println(img.getBounds());//--> ca c'est faux
            g2d.dispose();
            SaverLight.popJ(out);
            try {
                Thread.sleep(4010);
            } catch (InterruptedException ex) {
            }
            System.exit(0);
            return;
        }
//        if (true) {
//            //TODO permettre la rotation --> voir code si dessous et voir comment l'appliquer --> le plus simple est de dessiner sur une image avec un fond transparent et de sauver rotation et translation afin de pouvoir dessiner la ou il faut je pense
//            //ou bien recalc le new rect et laisser faire --> reflechir car dans le cas du vectoriel on veut dessiner l'image orig en vectoriel et c'est tout
//            //voir ou mettre ce truc et commment faire pr avoir la taille --> recalculer l'angle depuis le depart ?
//            BufferedImage out = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g2d = out.createGraphics();
//            BufferedImage tmp = new LoaderLight().load("/D/sample_images_PA/trash_test_mem/egg_chambers/Series010.png");
//
//            AffineTransform at = new AffineTransform();
//            at.setToTranslation(212.0773439350246, 212.0773439350246);
//            at.rotate(Math.toRadians(30), 512, 512); //--> ca marche en fait plutot bien
//            //juste voir comment l'appliquer a de vraies images
//            g2d.setTransform(at);
//            g2d.drawImage(tmp, 0, 0, null);
//            //c'est bon mais il faudrait lui ajouter une translation supplementaire --> comment le faire --> modifier le bounding rect 
//            //faire une foction qui recup le bounding rect depuis 
//            //just have to get the size of the new figure and align everything --> faut ensuite refaire tt le packing de la figure et c'est la que ca se corse
//            int width = tmp.getWidth();
//            int height = tmp.getHeight();
//            //ca marche on dirait mais voir comment l'appliquer
//            //--> dois je dessiner sur une image de mem taille
//            //--> IL FAUT ATTRIBUER LA TAILLE AU CARRE
//            Point2D.Double pt1 = CommonClassesLight.rotatePoint(new Point2D.Double(0, 0), new Point2D.Double(width / 2., height / 2.), Math.toRadians(45));
//            Point2D.Double pt2 = CommonClassesLight.rotatePoint(new Point2D.Double(width, 0), new Point2D.Double(width / 2., height / 2.), Math.toRadians(45));
//            Point2D.Double pt3 = CommonClassesLight.rotatePoint(new Point2D.Double(width, height), new Point2D.Double(width / 2., height / 2.), Math.toRadians(45));
//            Point2D.Double pt4 = CommonClassesLight.rotatePoint(new Point2D.Double(0, height), new Point2D.Double(width / 2., height / 2.), Math.toRadians(45));
//            Rectangle2D.Double r = new Rectangle2D.Double();
//            r.x = Math.min(pt4.x, Math.min(pt3.x, Math.min(pt1.x, pt2.x)));
//            r.y = Math.min(pt4.y, Math.min(pt3.y, Math.min(pt1.y, pt2.y)));
//            r.width = Math.max(pt4.x, Math.max(pt3.x, Math.max(pt1.x, pt2.x)));
//            r.height = Math.max(pt4.y, Math.max(pt3.y, Math.max(pt1.y, pt2.y)));
//            r.width -= r.x;
//            r.height -= r.y;
//            System.out.println(r);
//      drawAndFill  //use that to get the new bounding box and find a way to draw in it
//            g2d.dispose();
//            SaverLight.popJ(out);
//            try {
//                Thread.sleep(4010);
//            } catch (InterruptedException ex) {
//            }
//            System.exit(0);
//        }
        String reparse = "<img data-src=\"C:/Users/ben/Desktop/test.png\" data-LLFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">A</txtFgcolor></font></font>\" data-LFormattedText=\"<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">A</txtFgcolor></font></font>\" data-SBSize=\"120.0\" >";
        BufferedImage test2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = test2.createGraphics();

        long start_time = System.currentTimeMillis();
        MyImage2D.Double test = new MyImage2D.Double(reparse);
//     drawAndFillest.setScale_bar_size_in_px_of_the_real_image(120D);
        test.draw(g2d);
        System.out.println(test.produceMacroCode());
        g2d.dispose();
        SaverLight.popJ(test2);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        System.exit(0);
    }
}

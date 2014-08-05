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
package R;

import R.Maths.RMaths;
import Commons.CommonClassesLight;
import Dialogs.ColoredTextPane;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.StyledDoc2Html;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

/**
 * StyledDoc2Html converts an sttributed string to an html string --> best
 * choice for serialisation TODO faire un styledDoc 2 html
 *
 * @since <B>Packing Analyzer 8.1</B>
 * @author Benoit Aigouy
 */
public class StyledDoc2R extends StyledDoc2Html {

    //see http://127.0.0.1:26198/library/grDevices/html/plotmath.html for a complete list
    public String OFF = "'),'";
    public String BOLD_ON = "', bold('";
    public String BOLD_OFF = OFF;
    public String ITALIC_ON = "', italic('";
    public String ITALIC_OFF = OFF;
    public String BOLDITALIC_ON = "', bolditalic('";
    public String BOLDITALIC_OFF = OFF;
    public String UNDERLINE_ON = "', underline('";
    public String UNDERLINE_OFF = OFF;
    public String WIDEHAT_ON = "', widehat('";
    public String WIDEHAT_OFF = OFF;
    public String DOT_ON = "', dot('";
    public String DOT_OFF = OFF;
    public String SUBSCRIPT_ON = "'['";
    public String SUBSCRIPT_OFF = "'],'";
    public String SUPERSCRIPT_ON = "'^'";
    public String SUPERSCRIPT_OFF = "','";
    public String SPACE_IN_FORMULAS = "~";
    public String MULTIPLY = "%*%";
    public String DIVIDE = "%/%";
    public String PLUSMINUS = "%+-%";
//    String ALPHA = "alpha";
    public String ARROW_RIGHT = "%->%";
    public String ARROW_LEFT = "%<-%";
    public String INFINITY = "infinity";
    static final ArrayList<String> greek = new ArrayList<String>(CommonClassesLight.greek.keySet()); //we recover all the greek names from the greek hashmap
        /*
     * initialise a green retrotranslator
     */
    static final HashMap<String, String> revertedGreek = new HashMap<String, String>();

    static {
        for (Map.Entry<String, String> entry : CommonClassesLight.greek.entrySet()) {
            revertedGreek.put(entry.getValue(), entry.getKey());
        }
    }

    /*
     * fake tags only useful for me
     */
    int begin_italic = -1;
    int begin_bold = -1;
    int begin_FgColor = -1;
    int begin_underline = -1;
    int begin_superscript = -1;
    int begin_color = -1;
    int begin_subscript = -1;
    int begin_font = -1;
    Color fg_color = null;
    Font ft = null;
    Color bg_color = null;

    /**
     * Empty constructor
     */
    public StyledDoc2R() {
    }

    /**
     * Adds an italic TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    @Override
    public String italic(String htmlCode, boolean on) {
        if (on) {
            htmlCode += ITALIC_ON;
        } else {
            htmlCode += OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a bold TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    @Override
    public String bold(String htmlCode, boolean on) {
        if (on) {
            htmlCode += BOLD_ON;
        } else {
            htmlCode += OFF;
        }
        return htmlCode;
    }
    public String boldItalic(String htmlCode, boolean on) {
        if (on) {
            htmlCode += BOLDITALIC_ON;
        } else {
            htmlCode += OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a underline TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    @Override
    public String underline(String htmlCode, boolean on) {
        if (on) {
            htmlCode += UNDERLINE_ON;
        } else {
            htmlCode += UNDERLINE_OFF;
        }
        return htmlCode;
    }
//
    /**
     * Adds a superscript TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    @Override
    public String superscript(String htmlCode, boolean on) {
        if (on) {
            htmlCode += SUPERSCRIPT_ON;
        } else {
            htmlCode += SUPERSCRIPT_OFF;
        }
        return htmlCode;
    }

    /**
     * Adds a subscript TAG to the html like code
     *
     * @param htmlCode
     * @param on
     * @return html string equivalent
     */
    @Override
    public String subscript(String htmlCode, boolean on) {
        if (on) {
            htmlCode += SUBSCRIPT_ON;
        } else {
            htmlCode += SUBSCRIPT_OFF;
        }
        return htmlCode;
    }
    //ca marche mais les dernieres lettres sont splittees sans raison dans le texte
    //TODO also retrotranslate the greek characters to their alpha equivalent but keep font settings for those --> ???
    //--> easy
    /**
     * Converts a StyleDocument to some html-like code
     *
     * @param doc
     * @return html-like code
     */
    public String convertStyledDocToR(StyledDocument doc) {
        String R_text = "";
        if (doc == null) {
            return R_text;
        }

        String text = getText(doc);
        if (text.isEmpty()) {
            return R_text;
        }

        R_text += "paste('";

        boolean isUnderlined = false;
        boolean isSuperScript = false;
        boolean isSubScript = false;
        boolean isBold = false;
        boolean isBoldItalic = false;
        boolean isItalic = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Element el = doc.getCharacterElement(i);
            AttributeSet as = el.getAttributes();
            if (isSubscript(as) || isSuperscript(as)) {
                if (isSubscript(as)) {
                    if (isSuperScript) {
                        isSuperScript = false;
                        R_text = superscript(R_text, false);
                    }
                    if (!isSubScript) {
                        R_text = subscript(R_text, true);
                    }
                    isSubScript = true;
                } else {
                    if (isSubScript) {
                        isSubScript = false;
                        R_text = subscript(R_text, false);
                    }
                    if (!isSuperScript) {
                        R_text = superscript(R_text, true);
                    }
                    isSuperScript = true;
                }
            } else {
                if (isSubScript) {
                    R_text = subscript(R_text, false);
                    isSubScript = false;
                }
                if (isSuperScript) {
                    R_text = superscript(R_text, false);
                    isSuperScript = false;
                }
            }
            if (isBold(as) && !isItalic(as)) {
                if (!isBold) {
                    R_text = bold(R_text, true);
                }
                isBold = true;
            } else if (!isItalic(as)) {
                if (isBold) {
                    R_text = bold(R_text, false);
                    isBold = false;
                }
            }
            if (!isBold(as) && isItalic(as)) {
                if (!isItalic) {
                    R_text = italic(R_text, true);
                }
                isItalic = true;
            } else if (!isBold(as)) {
                if (isItalic) {
                    R_text = italic(R_text, false);
                    isItalic = false;
                }
            }
            if (isBold(as) && isItalic(as)) {
                if (isBold) {
                    R_text = bold(R_text, false);
                    isBold = false;
                }
                if (isItalic) {
                    R_text = italic(R_text, false);
                    isItalic = false;
                }
                if (!isBoldItalic) {
                    R_text = boldItalic(R_text, true);
                }
                isBoldItalic = true;
            } else {
                if (isBoldItalic) {
                    R_text = boldItalic(R_text, false);
                    isBoldItalic = false;
                }
            }
            if (isUnderline(as)) {
                if (!isUnderlined) {
                    R_text = underline(R_text, true);
                }
                isUnderlined = true;
            } else {
                if (isUnderlined) {
                    R_text = underline(R_text, false);
                    isUnderlined = false;
                }
            }
            R_text += c;
        }
        if (R_text.equals("")) {
            return R_text;
        }
        if (isBold) {
            R_text = bold(R_text, false);
        }
        if (isItalic) {
            R_text = italic(R_text, false);
        }
        if (isUnderlined) {
            R_text = underline(R_text, false);
        }
        if (isSubScript) {
            R_text = subscript(R_text, false);
        }
        if (isSuperScript) {
            R_text = superscript(R_text, false);
        }

        R_text += "')";
        R_text = R_text.replaceAll("'', ", "");
        R_text = R_text.replaceAll(",''", "");

        /**
         * handle greek --> retrotranslate greek character to R litteral
         */
        for (Map.Entry<String, String> entry : revertedGreek.entrySet()) {
            String greek = entry.getKey();
            if (R_text.contains(greek)) {
                R_text = R_text.replaceAll(greek, "'," + entry.getValue() + ",'");
            }
        }
        R_text = R_text.replaceAll("µ", "'," + "mu" + ",'");//french keyboard mu
        R_text = R_text.replaceAll(",''", "");
        R_text = R_text.replaceAll("'',", "");

        return R_text;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        //expression(paste('qsdqsdq sqsd ', bold(italic('sqdqs')),' dqsd qsd qsdqs'))
        //double bold
        //expression(paste('sdqdq sdqsd sqdsdq sdsqds ', bold(italic('qdsqdqsdqsdq'),' sqds'),'qdsqdqsdqsd'))
        //bold then italic
        //expression(paste('qsdsqdsqdsqd qsdqsdqsdqsd ', bold('qsdqsdqsds', italic('qdsqdqsd')),' qsddsqdsqd'))
        //ggplot(mtcars, aes(x=hp, y=mpg)) + geom_point(aes(colour=factor(cyl)))+xlab(expression(paste('scqsdsqd '['sqdsqdsqd'],' qsd qsd qsd ')))
        ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable();
        ColoredTextPane iopane = new ColoredTextPane(ctps);
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "StyledDoc2R", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println(RMaths.getMathAsString(new StyledDoc2R().convertStyledDocToR(ctps.doc)));
        }
        System.exit(0);
    }
}



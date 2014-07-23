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
package Tools;

import Commons.CommonClassesLight;
import Dialogs.ColoredTextPane;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.StyledDoc2Html;
import java.awt.Color;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * StyledDocTools does...
 *
 * @author Benoit Aigouy
 */
public class StyledDocTools {

    public static final String ITALIC_ONLY = "<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\"><i>dswsdfsd </txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</txtFgcolor><txtFgcolor color=\"#ffffff\">sdfdsfsdf<sup>sdfdsf</sup> sdf <sub>sdf</sub> dsf</txtFgcolor></font></i>";
    public static final String BOLD_ONLY = "<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\"><b>dswsdfsd </txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</txtFgcolor><txtFgcolor color=\"#ffffff\">sdfdsfsdf<sup>sdfdsf</sup> sdf <sub>sdf</sub> dsf</txtFgcolor></font></b>";
    public static final String SUPERSCRIPT_ONLY = "<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\"><sup>dswsdfsd </txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</txtFgcolor><txtFgcolor color=\"#ffffff\">sdfdsfsdfsdfdsf sdf sdf dsf</txtFgcolor></font></sup>";
    public static final String MULTICOLOR_ALL_STYLES = "<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">to<u>tototoot</u> <b>reoto</txtFgcolor><txtFgcolor color=\"#ff0000\">otero</txtFgcolor><txtFgcolor color=\"#00ff00\">otero</txtFgcolor><txtFgcolor color=\"#ffffff\">toreoteort</b> <i>oreotoer</i> <b><i>oterot</b></i> oerotreoto<sub>reoto</sub><sup>ertoe</sup>rotret</txtFgcolor></font>";
    /*
     * now some style variables
     */
    public static final int ITALIC = 0;
    public static final int BOLD = 1;
    public static final int SUBSCRIPT = 2;
    public static final int SUPERSCRIPT = 3;
    public static final int UNDERLINED = 4;
    public static final int COLORFUL = 5;
    public static final int MAX_STYLES = 6;

    public static boolean startsWith(StyledDocument doc, String regexPattern) {
        if (doc == null || regexPattern == null || regexPattern.isEmpty()) {
            return false;
        }
        String txt = getText(doc);
        if (txt.startsWith(regexPattern)) {
            return true;
        }
        return false;
    }

    public static boolean endsWith(StyledDocument doc, String regexPattern) {
        if (doc == null || regexPattern == null || regexPattern.isEmpty()) {
            return false;
        }
        String txt = getText(doc);
        if (txt.endsWith(regexPattern)) {
            return true;
        }
        return false;
    }

    public static boolean contains(StyledDocument doc, String regexPattern) {
        if (doc == null || regexPattern == null || regexPattern.isEmpty()) {
            return false;
        }
        String txt = getText(doc);
        if (txt.contains(regexPattern)) {
            return true;
        }
        return false;
    }

    public static boolean matches(StyledDocument doc, String regexPattern) {
        if (doc == null || regexPattern == null || regexPattern.isEmpty()) {
            return false;
        }
        String txt = getText(doc);
        if (txt.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(txt);
        return m.find();
    }

    /**
     * dirty fix --> improve this when I have time pb is sometime the deepequal
     * comparison is a bit too deep and does return false whereas things are
     * almost ok --> this provides a fix to this we just compare a few important
     * case things
     */
    public static boolean deepEqualsCaseOnly(StyledDocument styledDocument, StyledDocument doc, boolean italic, boolean bold, boolean underline, boolean font) {
        String txt1 = getText(styledDocument);
        String txt2 = getText(doc);
        /**
         * checks whether strings are the same
         */
        if (!txt1.equals(txt2)) {
            return false;
        }
        /**
         * if strings are the same then check whether the formatting is also the
         * same, if so the two styleddocs are the same
         */
        for (int i = 0; i < txt1.length(); i++) {
            AttributeSet as = styledDocument.getCharacterElement(i).getAttributes();
            AttributeSet as2 = doc.getCharacterElement(i).getAttributes();

            if (italic) {
                if (isItalic(as) != isItalic(as2)) {
                    return false;
                }
            }
            if (bold) {
                if (isBold(as) != isBold(as2)) {
                    return false;
                }
            }
            if (underline) {
                if (isUnderline(as) != isUnderline(as2)) {
                    return false;
                }
            }
            /*
             * bug fix for font size check
             */
            if (font) {
                if (!getFontFamily(as).equals(getFontFamily(as2)) || getFontSize(as) != getFontSize(as2)) {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean deepEquals(StyledDocument styledDocument, StyledDocument doc) {
        String txt1 = getText(styledDocument);
        String txt2 = getText(doc);
        /**
         * checks whether strings are the same
         */
        if (!txt1.equals(txt2)) {
            return false;
        }
        /**
         * if strings are the same then check whether the formatting is also the
         * same, if so the two styleddocs are the same
         */
        for (int i = 0; i < txt1.length(); i++) {
            AttributeSet as = styledDocument.getCharacterElement(i).getAttributes();
            AttributeSet as2 = doc.getCharacterElement(i).getAttributes();
            if (!as.isEqual(as2)) {
                return false;
            }
        }
        return true;
    }
//faire un custom check qui retourne un array de booleans dont la taille de ce que l'utilisateur veut recup ou pas --> assez simple je pense en fait

    public static Object[] getStyleStats(StyledDocument doc) {
        boolean isEmpty = false;
        if (doc == null || doc.getLength() == 0) {
            isEmpty = true;
        }
        boolean hasItalic = false;
        boolean isOnlyItalic = true;
        boolean hasBold = false;
        boolean isOnlyBold = true;
        boolean hasUnderline = false;
        boolean isOnlyUnderlined = true;
        boolean hasSubscript = false;
        boolean isOnlySubscript = true;
        boolean hasSuperscript = false;
        boolean isOnlySuperscript = true;
        int colors = 0;
        int nbChars = 0;
        boolean hasGreekLitterals = false;
        boolean hasGreekCharacters = false;
        if (doc != null) {
            HashSet<Integer> individualColors = new HashSet<Integer>();
            nbChars = doc.getLength();
            for (int i = 0; i < nbChars; i++) {
                Element el = doc.getCharacterElement(i);
                AttributeSet as = el.getAttributes();
                if (StyledDocTools.isItalic(as)) {
                    hasItalic = true;
                } else {
                    isOnlyItalic = false;
                }
                if (StyledDocTools.isBold(as)) {
                    hasBold = true;
                } else {
                    isOnlyBold = false;
                }
                if (StyledDocTools.isUnderline(as)) {
                    hasUnderline = true;
                } else {
                    isOnlyUnderlined = false;
                }
                if (StyledDocTools.isSubscript(as)) {
                    hasSubscript = true;
                } else {
                    isOnlySubscript = false;
                }
                if (StyledDocTools.isSuperscript(as)) {
                    hasSuperscript = true;
                } else {
                    isOnlySuperscript = false;
                }
                Color c = StyledDocTools.getFgColor(as);
                if (c != null) {
                    individualColors.add(c.getRGB());
                }
            }

            String text = StyledDocTools.getText(doc);
            String textlower = text.toLowerCase();

            for (String greek : CommonClassesLight.greekLitterals) {
                if (textlower.contains(greek)) {
                    hasGreekLitterals = true;
                    break;
                }
            }
            for (String greek : CommonClassesLight.greekCharacters) {
                if (text.contains(greek)) {
                    hasGreekCharacters = true;
                    break;
                }
            }

            colors = individualColors.size();
        }
        /*
         * for debug
         */
        /*
         System.out.println("isEmpty:" + isEmpty
         + "\n" + " hasItalic:" + hasItalic + " isOnlyItalic:" + isOnlyItalic
         + "\n" + " hasBold:" + hasBold + " isOnlyBold:" + isOnlyBold
         + "\n" + " hasUnderline:" + hasUnderline + " isOnlyUnderlined:" + isOnlyUnderlined
         + "\n" + " hasSubscript:" + hasSubscript + " isOnlySubscript:" + isOnlySubscript
         + "\n" + " hasSuperscript:" + hasSuperscript + " isOnlySuperscript:" + isOnlySuperscript
         + "\n" + " hasGreekLitterals:" + hasGreekLitterals + " hasGreekCharacters:" + hasGreekCharacters
         + "\n" + " nb of colors:" + colors +" nb of chars:"+nbChars);
         */

        Object[] styleStats = new Object[]{
            //0
            isEmpty,
            //1
            hasItalic,
            //2
            isOnlyItalic,
            //3
            hasBold,
            //4
            isOnlyBold,
            //5
            hasUnderline,
            //6
            isOnlyUnderlined,
            //7
            hasSubscript,
            //8
            isOnlySubscript,
            //9
            hasSuperscript,
            //10
            isOnlySuperscript,
            //11
            hasGreekLitterals,
            //12
            hasGreekCharacters,
            //13
            colors,
            //14
            nbChars};
        return styleStats;
    }

    public static boolean isEmpty(Object[] styleStats) {
        if (styleStats == null) {
            return true;
        }
        return (Boolean) styleStats[0];
    }

    public static boolean isItalic(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[1];
    }

    public static boolean isOnlyItalic(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[2];
    }

    public static boolean isBold(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[3];
    }

    public static boolean isOnlyBold(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[4];
    }

    public static boolean isUnderlined(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[5];
    }

    public static boolean isOnlyUnderlined(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[6];
    }

    public static boolean isSubscript(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[7];
    }

    public static boolean isOnlySubscript(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[8];
    }

    public static boolean isSuperscript(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[9];
    }

    public static boolean isOnlySuperscript(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[10];
    }

    public static boolean isHasGreekAsLitterals(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[11];
    }

    public static boolean isHasGreekCharacters(Object[] styleStats) {
        if (styleStats == null) {
            return false;
        }
        return (Boolean) styleStats[12];
    }

    public static int getNbOfColor(Object[] styleStats) {
        if (styleStats == null) {
            return 0;
        }
        return (Integer) styleStats[13];
    }

    public static int getNbOfCHars(Object[] styleStats) {
        if (styleStats == null) {
            return 0;
        }
        return (Integer) styleStats[14];
    }

    public StyledDocTools() {
    }

    public static StyledDocument getSampleDoc() {
        StyledDocument doc = new StyledDoc2Html().reparse("<font face=\"Arial\" size=\"12\"><txtFgcolor color=\"#ffffff\">dswsdfsd <b><i></txtFgcolor><txtFgcolor color=\"#ff3300\">fsdf</b></i></txtFgcolor><txtFgcolor color=\"#ffffff\"> <i>sdfdsfsdf</i> <sup>sdfdsf</sup> sdf <sub>sdf</sub> dsf</txtFgcolor></font>");
        return doc;
    }

    public static StyledDocument getSampleDoc(String xml) {
        StyledDocument doc = new StyledDoc2Html().reparse(xml);
        return doc;
    }

    /**
     *
     * @param as
     * @return if the attributeSet is underlined
     */
    public static boolean isUnderline(AttributeSet as) {
        return StyleConstants.isUnderline(as);
    }

    /**
     *
     * @param as
     * @return if the attributeSet is superscripted
     */
    public static boolean isSuperscript(AttributeSet as) {
        return StyleConstants.isSuperscript(as);
    }

    /**
     *
     * @param as
     * @return if the attributeSet is subscripted
     */
    public static boolean isSubscript(AttributeSet as) {
        return StyleConstants.isSubscript(as);
    }

    /**
     *
     * @param as
     * @return if the attributeSet is italicized
     */
    public static boolean isItalic(AttributeSet as) {
        return StyleConstants.isItalic(as);
    }

    /**
     *
     * @param as
     * @return if the attributeSet is in bold
     */
    public static boolean isBold(AttributeSet as) {
        return StyleConstants.isBold(as);
    }

    /**
     *
     * @param as attributeSet
     * @return the foreground color from the attributeSet
     */
    public static Color getFgColor(AttributeSet as) {
        return StyleConstants.getForeground(as);
    }

    /**
     *
     * @param as
     * @return the font size from the attributeSet
     */
    public static int getFontSize(AttributeSet as) {
        return StyleConstants.getFontSize(as);
    }

    /**
     *
     * @param as
     * @return the font family from the attributeSet
     */
    public static String getFontFamily(AttributeSet as) {
        return StyleConstants.getFontFamily(as);
    }

    /*
     * my hack for the temporary support of upper or lower case support 
     */
    public static boolean isForceUpperCase(AttributeSet as) {
        Object val = as.getAttribute("forceUpperCase");
        return val == null ? false : val.toString().toLowerCase().trim().contains("true") ? true : false;
    }

    /*
     * my hack for the temporary support of upper or lower case support 
     */
    public static boolean isForceLowerCase(AttributeSet as) {
        Object val = as.getAttribute("forceLowerCase");
        return val == null ? false : val.toString().toLowerCase().trim().contains("true") ? true : false;
    }

    /**
     *
     * @param doc
     * @return the text of the styleDoc
     */
    public static String getText(StyledDocument doc) {
        try {
            return doc.getText(0, doc.getLength());
        } catch (Exception e) {
        }
        return "";
    }

    public static String getText(StyledDocument doc, int begin, int end) {
        try {
            if (begin <= 0) {
                begin = 0;
            }
            end = end <= 0 ? 0 : end;
            end = end > begin + doc.getLength() ? doc.getLength() - begin : end;
            return doc.getText(begin, end-begin);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * the replace is a very basic non regex based text replacement utility it
     * also does not support style (can I make it to support style or not ???
     * maybe later)
     */
    public static StyledDocument replace(StyledDocument doc, String stringToBeReplaced, String replacement) {
        if (replacement == null || doc == null || stringToBeReplaced == null) {
            return doc;
        }
        String txt;
        try {
            txt = doc.getText(0, doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
            return doc;
        }

        if (!txt.contains(stringToBeReplaced)) {
            return doc;
        }

        String regexifiedReplace = stringToBeReplaced;

        for (String regexChar : CommonClassesLight.regexVocabulary) {
            regexifiedReplace = regexifiedReplace.replace(regexChar, "\\" + regexChar);
        }

        Pattern p = Pattern.compile(stringToBeReplaced);
        Matcher m = p.matcher(txt);

        LinkedHashMap<Point, ArrayList<AttributeSet>> pos_n_styles = new LinkedHashMap<Point, ArrayList<AttributeSet>>();

        while (m.find()) {
            /**
             * regex found in replacement pattern --> split it into its
             * components
             */
            int start = m.start();
            int end = m.end();

            ArrayList<AttributeSet> style = new ArrayList<AttributeSet>();
            for (int i = start; i < end; i++) {
                style.add(doc.getCharacterElement(i).getAttributes());
            }
            pos_n_styles.put(new Point(start, end), style);
        }

        ArrayList<Point> pos = new ArrayList<Point>(pos_n_styles.keySet());
        Collections.reverse(pos);

        for (Point point : pos) {
            int start = point.x;
            int end = point.y;
            try {
                /*
                 * we remove the text to be replaced
                 */
                doc.remove(start, end - start);
                /*
                 * we insert the replacement string
                 */
                doc.insertString(start, replacement, null);
                /*
                 * we restore the text style as best as we can
                 */
                ArrayList<AttributeSet> asses = pos_n_styles.get(point);
                int lastAs = asses.size() - 1;
                for (int i = start, j = 0; j < replacement.length(); i++, j++) {
                    AttributeSet curAs = j < asses.size() ? asses.get(j) : asses.get(lastAs);
                    doc.setCharacterAttributes(i, 1, curAs, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return doc;
    }

    public static StyledDocument replaceAll(StyledDocument doc, String regexPattern, String replacement) {
        return advancedPatternMatcher(doc, regexPattern, replacement, true);
    }

    public static StyledDocument replaceFirst(StyledDocument doc, String regexPattern, String replacement) {
        return advancedPatternMatcher(doc, regexPattern, replacement, false);
    }

    private static StyledDocument advancedPatternMatcher(StyledDocument doc, String regexPattern, String replacement, boolean replaceAll) {
        if (replacement == null || doc == null || regexPattern == null) {
            return doc;
        }
        String txt;
        try {
            txt = doc.getText(0, doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
            return doc;
        }

        /*
         * pattern not found --> ignore
         */
        String regexReplacement = replacement;
        ArrayList<AttributeSet> hmlStyles = null;
        /**
         * extracting html style from regexReplacement if and only if it starts
         * with the html tag
         */
        /*
         * bug fix for upper case or lowercase around a dollar variable
         */
        if (replacement.toLowerCase().startsWith("<html>")) {
            StyledDocument tmp = new StyledDoc2Html().reparse(replacement);
            regexReplacement = StyledDocTools.getText(tmp);
            hmlStyles = StyledDocTools.getTextAttributes(tmp);//maintenant on a tt ce qu'il faut
        }

        /**
         * trying to match regex in the regexReplacement pattern (this man is
         * getting crazy ;-P)
         */
        String crazyRegexPatternMatcher = "[^\\\\]([\\$][0-9]{1,})|\\b([\\$][0-9]{1,})|^([\\$][0-9]{1,})";//"([\\$][0-9]){1,}";//"\\$[0-9]";// "\\$[0-9]";//.*[^(?\\)] //this regex is not perfect it takes one char too many or takes escaped dollars --> fix that some day
        Pattern nbOfDollarsInReplace = Pattern.compile(crazyRegexPatternMatcher);
        Matcher replaceMatcher = nbOfDollarsInReplace.matcher(regexReplacement);
        int init_pos = 0;
        int end_pos = 0;
        ArrayList<String> text = new ArrayList<String>();
        int count = replaceMatcher.groupCount();
        boolean replacementAlsoRegex = false;
        while (replaceMatcher.find()) {
            /**
             * regex found in replacement pattern --> split it into its
             * components
             */
            replacementAlsoRegex = true;
            int start = replaceMatcher.start();
            int end = replaceMatcher.end();
            int idxOfdollar = replaceMatcher.group().indexOf("$");
            if (idxOfdollar != -1) {
                if (start + idxOfdollar != init_pos) {
                    text.add(regexReplacement.substring(init_pos, start + idxOfdollar));
                }
            }
            /**
             * find the only non null output of regex, this is our dollar
             */
            String dollar = "";// = matcher.group(1);
            for (int i = 1; i <= count; i++) {
                String match = replaceMatcher.group(i);
                if (match != null) {
                    dollar = match;
                }
            }
            text.add(dollar);
            init_pos = end - idxOfdollar;
            end_pos = end;
        }
        /**
         * add the rest of the text
         */
        if (end_pos < regexReplacement.length()) {
            text.add(regexReplacement.substring(end_pos, regexReplacement.length()));
        }

        /*
         * we create an array of style attributes that are going to be used to stylize the text
         */
        ArrayList<ArrayList<AttributeSet>> charStyles = null;
        /*
         * so far so good
         */
        if (hmlStyles != null) {
            int i = 0;
            charStyles = new ArrayList<ArrayList<AttributeSet>>();
            for (String string : text) {
                ArrayList<AttributeSet> curAttrs = new ArrayList<AttributeSet>();
                char[] letters = string.toCharArray();
                for (char c : letters) {
                    curAttrs.add(hmlStyles.get(i));
                    i++;
                }
                charStyles.add(curAttrs);
            }
        }

        /*
         * debug
         */
//        for (String string : text) {
//            System.out.println("--> '" + string + "'");
//        }
        ArrayList<String> replacements = new ArrayList<String>();
        LinkedHashMap<Point, ArrayList<AttributeSet>> pos_and_attributes = new LinkedHashMap<Point, ArrayList<AttributeSet>>();
        LinkedHashMap<Point, ArrayList<AttributeSet>> pos_and_attributes_from_html = new LinkedHashMap<Point, ArrayList<AttributeSet>>();
        try {
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(txt);
            int nbGroups = matcher.groupCount();

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                ArrayList<AttributeSet> ases = new ArrayList<AttributeSet>();

                for (int i = start; i < end; i++) {
                    ases.add(doc.getCharacterElement(i).getAttributes());
                }
                Point pos = new Point(start, end);
                pos_and_attributes.put(pos, ases);

                if (replacementAlsoRegex) {
                    /**
                     * create the appropriate replacemnt string
                     */
                    ArrayList<AttributeSet> ases_html = new ArrayList<AttributeSet>();
                    String finalReplacement = "";
                    int cur = 0;
                    for (String string : text) {

                        ArrayList<AttributeSet> htmlForTheCurrentWord = null;
                        if (charStyles != null) {
                            htmlForTheCurrentWord = charStyles.get(cur);
                        }
                        int initialSize = finalReplacement.length();
                        if (string.matches("\\$[0-9]{1,}")) { //!!!!TODO fix this match to also ignore escaped dollars
                            int groupNb = CommonClassesLight.String2Int(string.substring(1));
                            if (groupNb <= nbGroups) {
                                String match = matcher.group(groupNb);
                                finalReplacement += match;
                            } else {
                                finalReplacement += string;
                            }
                        } else {
                            finalReplacement += string;
                        }
                        int finalSize = finalReplacement.length();
                        if (htmlForTheCurrentWord != null) {

                            int size = htmlForTheCurrentWord.size();
                            int last = htmlForTheCurrentWord.size() - 1;
                            for (int i = 0; i < finalSize - initialSize; i++) {
                                if (i < size) {
                                    ases_html.add(htmlForTheCurrentWord.get(i));
                                } else {
                                    ases_html.add(htmlForTheCurrentWord.get(last));
                                }
                            }
                        }
                        cur++;
                    }
                    replacements.add(finalReplacement);
                    pos_and_attributes_from_html.put(pos, ases_html);
                }
                if (!replaceAll) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Point> pos = new ArrayList<Point>(pos_and_attributes.keySet());
        Collections.reverse(pos);
        Collections.reverse(replacements);

        char[] chars = regexReplacement.toCharArray();
        int l = 0;
        for (Point point : pos) {
            int start = point.x;
            int end = point.y;
            try {
                doc.remove(start, end - start);
                ArrayList<AttributeSet> ases = pos_and_attributes.get(point);
                ArrayList<AttributeSet> ases_html = pos_and_attributes_from_html.get(point);
                if (!replacementAlsoRegex) {
                    /*
                     * basic text replacement
                     */
                    for (int i = chars.length - 1; i >= 0; i--) {
                        char c = chars[i];
                        AttributeSet as = i < ases.size() ? ases.get(i) : ases.get(ases.size() - 1);
                        AttributeSet extra = null;
                        if (ases_html != null) {
                            extra = i < ases_html.size() ? ases_html.get(i) : ases_html.get(ases_html.size() - 1);
                        }
                        /*
                         * dirty hack for upper/lower case support in dynamic replacements
                         */
                        if (extra != null) {
                            if (isForceUpperCase(extra)) {
                                c = Character.toUpperCase(c);
                            }
                            if (isForceLowerCase(extra)) {
                                c = Character.toLowerCase(c);
                            }
                        }
                        doc.insertString(start, c + "", as);
                        if (extra != null) {
                            doc.setCharacterAttributes(start, 1, extra, false);
                        }
                    }
                } else {
                    /*
                     * complex text replacement (regex replacement)
                     */
                    char[] chars2 = replacements.get(l).toCharArray();
                    for (int i = chars2.length - 1; i >= 0; i--) {
                        char c = chars2[i];
                        AttributeSet as = i < ases.size() ? ases.get(i) : ases.get(ases.size() - 1);
                        AttributeSet extra = null;
                        if (ases_html != null && !ases_html.isEmpty()) {
                            extra = i < ases_html.size() ? ases_html.get(i) : ases_html.get(ases_html.size() - 1);
                        }
                        /*
                         * dirty hack for upper/lower case support in dynamic replacements
                         */
                        if (extra != null) {
                            if (isForceUpperCase(extra)) {
                                c = Character.toUpperCase(c);
                            }
                            if (isForceLowerCase(extra)) {
                                c = Character.toLowerCase(c);
                            }
                        }
                        doc.insertString(start, c + "", as);
                        if (extra != null) {
                            doc.setCharacterAttributes(start, 1, extra, false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            l++;
            if (!replaceAll) {
                break;
            }
        }
        return doc;
    }

    public static StyledDocument cloneDoc(StyledDocument doc) {
        StyledDocument copy = new DefaultStyledDocument();
        ColoredTextPaneSerializable.initDoc(copy);
        if (doc == null) {
            return copy;
        }
        return copy(doc, copy);
    }

    public static StyledDocument copy(StyledDocument src, StyledDocument copy) {
        try {
            copy.insertString(0, getText(src), null);
            for (int i = 0; i < copy.getLength(); i++) {
                copy.setCharacterAttributes(i, 1, src.getCharacterElement(i).getAttributes(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copy;
    }

    public static StyledDocument insert(StyledDocument input, StyledDocument destination, int positionInDestination) {
        try {
        ArrayList<AttributeSet> attributes = getTextAttributes(input);
        destination.insertString(positionInDestination, getText(input), null);
        int pos = positionInDestination;
        for (AttributeSet attributeSet : attributes) {
            destination.setCharacterAttributes(pos++, 1, attributeSet, true);
        }
        } catch (Exception e) {
        }
        return destination;
    }

    public static ArrayList<AttributeSet> getTextAttributes(StyledDocument doc) {
        if (doc == null || doc.getLength() == 0) {
            return null;
        }
        ArrayList<AttributeSet> ases = new ArrayList<AttributeSet>();
        int length = doc.getLength();
        for (int i = 0; i < length; i++) {
            AttributeSet as = doc.getCharacterElement(i).getAttributes();
            MutableAttributeSet attrs = new SimpleAttributeSet(as);
            attrs.removeAttribute(TextAttribute.FOREGROUND);
            ases.add(attrs);
        }
        return ases;
    }
    
    //prendre juste le getext entre deux positions
    public static ArrayList<AttributeSet> getTextAttributes(StyledDocument doc, int begin , int end) {
        if (doc == null || doc.getLength() == 0) {
            return null;
        }
        ArrayList<AttributeSet> ases = new ArrayList<AttributeSet>();
        for (int i = begin; i < end; i++) {
            AttributeSet as = doc.getCharacterElement(i).getAttributes();
            MutableAttributeSet attrs = new SimpleAttributeSet(as);
            attrs.removeAttribute(TextAttribute.FOREGROUND);
            ases.add(attrs);
        }
        return ases;
    }

    public static StyledDocument removeStyle(StyledDocument doc, int... STYLES) {
        if (doc == null || doc.getLength() == 0) {
            return null;
        }
        if (STYLES == null || STYLES.length == 0) {
            return doc;
        }
        int length = doc.getLength();
        Object style = null;
        for (int i : STYLES) {
            switch (i) {
                case SUBSCRIPT:
                    style = StyleConstants.Subscript;
                    break;
                case SUPERSCRIPT:
                    style = StyleConstants.Superscript;
                    break;
                case UNDERLINED:
                    style = StyleConstants.Underline;
                    break;
                case BOLD:
                    style = StyleConstants.Bold;
                    break;
                case ITALIC:
                    style = StyleConstants.Italic;
                    break;
                case COLORFUL:
                    style = StyleConstants.Foreground;
                    break;
            }
            if (style != null) {
                for (int c = 0; c < length; c++) {
                    AttributeSet as = doc.getCharacterElement(c).getAttributes();
                    MutableAttributeSet attrs = new SimpleAttributeSet(as);
                    attrs.removeAttribute(style);
                    doc.setCharacterAttributes(c, 1, attrs, true);
                }
            }
        }
        return doc;
    }

    public static StyledDocument unitalicize(StyledDocument doc) {
        return removeStyle(doc, ITALIC);
    }

    public static StyledDocument uncolorize(StyledDocument doc) {
        return removeStyle(doc, COLORFUL);
    }

    public static StyledDocument unbold(StyledDocument doc) {
        return removeStyle(doc, BOLD);
    }

    public static StyledDocument ununderline(StyledDocument doc) {
        return removeStyle(doc, UNDERLINED);
    }

    public static StyledDocument unsubscript(StyledDocument doc) {
        return removeStyle(doc, SUBSCRIPT);
    }

    public static StyledDocument unsuperscript(StyledDocument doc) {
        return removeStyle(doc, SUPERSCRIPT);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
//--> for style        
        /*
         * //--> get around $1

         "<i>$1</i><b>$2</b><sup>-1</sup>" //-->sinon reparser le string final
         dans un doc --> faisable mais super complique
         //--> just find tags around the pattern --> is that so hard ??? --> go
         recursivley around ????
         //-->puissant mais faut reparser le pattern
         italic(length-2, length) --> bcp plus simple a coder mais moins flexible
         //sinon recup tt les mots et
         //sinon faire un string de meme taille avec codage du style par des nombres
         //sinon un codage par chunk le pb est qu'il faut que le nb de chunks match
         //sinon ignore ???
         0123 --> bold italic plain or both --> juste faire 2 de plus pour
         subscript ou superscript
         ou
         le plus sinple est le italic()
         sup()
         bold()
         bolditalic()
         sub()

         */
        //TODO faire des fonctions insert and 
        long start_time = System.currentTimeMillis();

        //--> ca marche --> pas mal
        //peut etre ajouter deux tags --> <cap> <low> --> mais les implementer proprement et ils ne seront pas sauves mais c'est pas grave il faudrait juste pouvoir les parser et les appliquer tt de suite (rien de plus)
        //String cur = "   ttoot   toto  yuyq toto qs  ";
        String cur = "P&#xsup0.5 text P &#xinf 0.05 x plane T=0.3";//"<html> qsd qsd qqsdqd   <cap>tto<b><i>i</i></b>ot</cap>   <B>toto  <I>yuyq</I></B> 120µm/s toto <SuP>qs</sUp>  ";//"qsqsdqsd  qqsdqd qs sq 120s µm<sup>-1</sup> 30 s m-1 <i>300/60</i> 30s m-1 ouba toto/tutu";
        //"   ttoot toto yuyq qs  ".replaceAll("^ {1,}", "")
        StyledDocument doc = new StyledDoc2Html().reparse(cur);
        StyledDocTools.getTextAttributes(doc);
        ColoredTextPane ctps = new ColoredTextPane(new ColoredTextPaneSerializable(doc, "test"));
        //ca marche --> tester pleins d'autres cas
        /*
         * test pattern modif
         */

        System.out.println("--> " + cur.replaceAll("&#xsup", ">").replaceAll("&#xinf", "<").replaceAll("([a-zA-Z]{1})(\\s{0,}[=><])", "<html><i>$1</i>$2"));

        //ca marche mais voir comment gerer les 
//        String noLeadingSpace = cur.replaceAll("toto", "zoumba");//.replaceAll(" {1,}$", "");//.replaceAll(" {2,}", " ");//"   ttoot toto yuyq qs    ".replaceAll("^ {1,}", "");
//        System.out.println("\"" + noLeadingSpace + "\"");
        int result = JOptionPane.showOptionDialog(null, ctps, "text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.NO_OPTION, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            //doc = StyledDocTools.matchStyledDocumentContentToStringByDeleting(doc, noLeadingSpace);
            //doc = StyledDocTools.matchStyledDocumentContentToStringByInserting(doc, noLeadingSpace);
            //doc = test.testPatternMatcher(ctps.getDoc(), " {2,}", " ");// "toto", "tit");//toto
            //TODO gerer le upper case --> comment ajouter uppercase apres coup ??? //--> detecter si 
            doc = replaceAll(doc, "([a-zA-Z]{1})(\\s{0,}[=><])", "<html><i>$1</i>$2");//"([^\\s0-9]{1,2})/\\b([^\\s0-9]{1,2})\\b", "<html>$1 <cap>$2</cap><sup>-1</sup>"
            //doc = replaceFirst(doc, "qsd", "bob");//"([^\\s0-9]{1,2})/\\b([^\\s0-9]{1,2})\\b", "<html>$1 <cap>$2</cap><sup>-1</sup>"
            ctps = new ColoredTextPane(doc, "test");
            System.out.println(ctps.getColoredTextPaneSerializable().getFormattedText());
        }

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

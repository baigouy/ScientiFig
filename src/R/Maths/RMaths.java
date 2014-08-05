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
package R.Maths;

import Commons.CommonClassesLight;
import java.util.ArrayList;

/**
 * RMaths does...
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
//http://127.0.0.1:10209/library/grDevices/html/plotmath.html
//?plotmath 
public class RMaths {

    public static final String PARTIALDIFF = "partialdiff";
    public static final String INFINITY = "infinity";
    public static final String NABLA = "nabla";
    public static final String GRADIENT = NABLA;
    public static final String DEGREE = "degree";
    public static final String MINUTE = "minute";
    public static final String SECOND = "second";
    public static final String SPACE = "''~''";//"' '"; //"''~''"//dirty but it works (should use ~ instead but ~ is much harder to handle)
    private static final ArrayList<String> greekPlain = new ArrayList<String>(CommonClassesLight.greek.keySet());
    private static final ArrayList<String> additionalKeywords = new ArrayList<String>();
    public static final String SQRT = "sqrt(";
    public static final String HAT = "hat(";
    public static final String SPACE2 = "~";
    public static final String BOLD = "bold(";
    public static final String ITALIC = "italic(";
    public static final String BOLDITALIC = "bolditalic(";
    public static final String POWER = "^";
    public static final String subscript = "[";
    public static final String DEGREES = "*" + DEGREE;
    public static final String DEGREES2 = "* " + DEGREE;
    public static final String MINUTES = "*" + MINUTE;
    public static final String MINUTES2 = "* " + MINUTE;
    public static final String SECONDS = "*" + SECOND;
    public static final String SECONDS2 = "* " + SECOND;
    public static final ArrayList<String> RLanguageElements = new ArrayList<String>(additionalKeywords);

    /*
     * supported R language terms
     infinity
     degree
     minute
     second
     nabla
     partialdiff
     paste(
     frac(
     over(
     == 
     %~~%
     %=~%
     %prop%
     %~%
     %subset%
     %subseteq%
     %notsubset%
     %supset%
     %supseteq%
     %in%
     %notin%
     %<->%
     %up%
     %down%
     %+-%
     %*%
     %.%
     list(
     atop(
     %<-%
     %->%
     %<=>%
     %=>%
     %<=%
     %dblup%
     %dbldown%
     !=
     tilde(
     dot(
     ring(
     bar(
     underline(
     sum(
     union(
     intersect(
     lim(
     min(
     prod(
     scriptstyle(
     scriptscriptstyle(
     integral(
     inf(
     sup(
     group(
     bgroup(
     >
     <
     */
    public static final String PASTE = "paste(";
    public static final String FRAC = "frac(";
    public static final String OVER = "over(";
    public static final String EQUALEQUAL = "==";
    public static final String PROP = "%";
    public static final String LIST = "list(";
    public static final String ATOP = "atop(";
    public static final String TILDE = "tilde(";
    public static final String DOT = "dot(";
    public static final String RING = "ring(";
    public static final String BAR = "bar(";
    public static final String UNDERLINE = "underline(";
    public static final String SUM = "sum(";
    public static final String UNION = "union(";
    public static final String INTERSECT = "intersect(";
    public static final String LIM = "lim(";
    public static final String MIN = "min(";
    public static final String PROD = "prod(";
    public static final String SCRIPTSTYLE = "scriptstyle(";
    public static final String SCRIPTSCRIPTSTYLE = "scriptscriptstyle(";
    public static final String INTEGRAL = "integral(";
    public static final String INF = "inf(";
    public static final String SUP = "sup(";
    public static final String GROUP = "group(";

    static {
        RLanguageElements.add(SQRT);
        RLanguageElements.add(BOLD);
        RLanguageElements.add(ITALIC);
        RLanguageElements.add(BOLDITALIC);
        RLanguageElements.add(POWER);
        RLanguageElements.add(subscript);
        RLanguageElements.add(HAT);
        RLanguageElements.add(SPACE2);
        RLanguageElements.add(DEGREES);
        RLanguageElements.add(DEGREES2);
        RLanguageElements.add(MINUTES);
        RLanguageElements.add(MINUTES2);
        RLanguageElements.add(NABLA);
        RLanguageElements.add(INFINITY);
        RLanguageElements.add(PARTIALDIFF);
        RLanguageElements.add(PASTE);
        RLanguageElements.add(FRAC);
        RLanguageElements.add(OVER);
        RLanguageElements.add(EQUALEQUAL);
        RLanguageElements.add(PROP);
        RLanguageElements.add(LIST);
        RLanguageElements.add(ATOP);
        RLanguageElements.add(TILDE);
        RLanguageElements.add(DOT);
        RLanguageElements.add(RING);
        RLanguageElements.add(BAR);
        RLanguageElements.add(UNDERLINE);
        RLanguageElements.add(SUM);
        RLanguageElements.add(UNION);
        RLanguageElements.add(INTERSECT);
        RLanguageElements.add(LIM);
        RLanguageElements.add(MIN);
        RLanguageElements.add(PROD);
        RLanguageElements.add(SCRIPTSTYLE);
        RLanguageElements.add(SCRIPTSCRIPTSTYLE);
        RLanguageElements.add(INTEGRAL);
        RLanguageElements.add(INF);
        RLanguageElements.add(SUP);
        RLanguageElements.add(GROUP);
    }

    static {
        additionalKeywords.add(PARTIALDIFF);
        additionalKeywords.add(INFINITY);
        additionalKeywords.add(NABLA);
        additionalKeywords.add(NABLA);
        additionalKeywords.add(DEGREE);
        additionalKeywords.add(SECOND);
        additionalKeywords.add(MINUTE);
    }

    public RMaths() {
    }

    /**
     * interprets a command
     *
     * @param commands the first string should be a keyword correponding to the
     * command to execute, the following ones are the parameters of the command
     * @return the result of the command
     */
    public static String interprete(String... commands) {
        String out = "";
        if (commands == null || commands.length == 0) {
            return out;
        }
        String operation = commands[0].toLowerCase();
        try {
            /*
             * TODO replace by a switch in java 7
             */
            if (operation.equals("fraction")) {
                out = RMaths.FRACTION(commands[1], commands[2]);
            } else if (operation.equals("atop")) {
                out = RMaths.ATOP(commands[1], commands[2]);
            } else if (operation.equals("space")) {
                out = RMaths.SPACE;
            } else if (operation.equals("infinity")) {
                out = RMaths.INFINITY;
            } else if (operation.equals("sum")) {
                out = RMaths.SUM(commands[1], commands[2], commands[3], commands[4]);
            } else if (operation.equals("integral")) {
                out = RMaths.INTEGRAL(commands[1], commands[2], commands[3]);
            } else if (operation.equals("concatenate")) {
                out = RMaths.CONCATENATE(commands[1], commands[2]);
            } else if (operation.equals("limit")) {
                out = RMaths.LIMIT(commands[1], commands[2], commands[3]);
            } else if (operation.equals("custom text")) {
                out = "'" + commands[1] + "'";
            } else if (operation.contains("greek")) {
                /**
                 * For debug test:<br>
                 * zoumba = alpha + theta + beta + zeta + eta + Beta + Theta +
                 * Zeta + Eta + qsdqsd qs+ 23*2
                 */
                ArrayList<String> keywords = new ArrayList<String>();
                keywords.addAll(greekPlain);
                keywords.addAll(additionalKeywords);
                String command = commands[1];
                keywords.remove("eta");
                for (String string : keywords) {
                    if (command.contains(string)) {
                        command = command.replaceAll(string, "'," + string + "','");
                    }
                }
                keywords.add("eta");
                if (command.contains("eta")) {
                    command = command.replaceAll("Beta", "Bita").replaceAll("Zeta", "Zita").replaceAll("Theta", "Thita").replaceAll("zeta", "zita").replaceAll("theta", "thita").replaceAll("beta", "bita").replaceAll("eta", "'," + "eta" + "','").replaceAll("thita", "theta").replaceAll("bita", "beta").replaceAll("zita", "zeta").replaceAll("Bita", "Beta").replaceAll("Zita", "Zeta").replaceAll("Thita", "Theta");
                }
                if (command.endsWith(",'")) {
                    command = command.substring(0, command.length() - 2);
                }
                if (!command.startsWith("',") && !command.startsWith("'")) {
                    command = "'" + command;
                }
                if (command.startsWith("',")) {
                    command = command.substring(2);
                }
                if (!command.endsWith("'")) {
                    command += "'";
                }
                if (command.contains("',',")) {
                    command = command.replace("',',", "',");
                }
                for (String string : keywords) {
                    if (command.endsWith(string + "'")) {
                        command = command.substring(0, command.length() - 1);
                    }
                }
                command = "paste(" + command + ")";
                for (String string : keywords) {
                    if (command.contains(string + "',")) {
                        command = command.replaceAll(string + "',", string + ",");
                    }
                }
                out = command;
            } else if (operation.contains("hat")) {
                if (operation.contains("wide")) {
                    out = WIDEHAT(commands[1]);
                } else {
                    out = HAT(commands[1]);
                }
            } else if (operation.contains("bold") || operation.contains("italic")) {
                if (operation.contains("+")) {
                    out = BOLDITALIC(commands[1]);
                } else {
                    if (operation.contains("bold")) {
                        out = BOLD(commands[1]);
                    } else {
                        out = ITALIC(commands[1]);
                    }
                }
            } else if (operation.contains("square root")) {
                if (operation.contains("custom")) {
                    out = SQRT(commands[1], commands[1]);
                } else {
                    out = SQRT(commands[1]);
                }
            } else if (operation.contains("tilde")) {
                if (operation.contains("wide")) {
                    out = WIDETILDE(commands[1]);
                } else {
                    out = TILDE(commands[1]);
                }
            } else if (operation.contains("dot")) {
                out = DOT(commands[1]);
            } else if (operation.contains("ring")) {
                out = RING(commands[1]);
            } else if (operation.contains("bar")) {
                out = BAR(commands[1]);
            } else if (operation.contains("underline")) {
                out = UNDERLINE(commands[1]);
            } else if (operation.contains("infimum")) {
                out = INFIMUM(commands[1]);
            } else if (operation.contains("supremum")) {
                out = SUPREMUM(commands[1]);
            } else if (operation.contains("group")) {
                String parenthesis = "(";
                if (operation.contains("{")) {
                    parenthesis = "{";
                } else if (operation.contains("[")) {
                    parenthesis = "[";
                }
                if (operation.contains("bgroup")) {
                    out = BGROUP(commands[1], parenthesis);
                } else {
                    out = GROUP(commands[1], parenthesis);
                }
            } else if (operation.contains("erior")) {
                if (operation.contains("inf")) {
                    out = INFERIOR(commands[1], commands[2], true);
                } else {
                    out = SUPERIOR(commands[1], commands[2], true);
                }
            } else if (operation.contains("small")) {
                if (operation.contains("inf")) {
                    out = SMALL(commands[1]);
                } else {
                    out = VERYSMALL(commands[1]);
                }
            } else if (operation.contains("congruent")) {
                out = CONGRUENT(commands[1], commands[2]);
            } else if (operation.contains("different")) {
                out = DIFFERENT(commands[1], commands[2]);
            } else if (operation.contains("distributed as")) {
                out = DISTRIBUTEDAS(commands[1], commands[2]);
            } else if (operation.contains("divide")) {
                out = DIVIDE(commands[1], commands[2]);
            } else if (operation.contains("multiply")) {
                out = MULTIPLY(commands[1], commands[2]);
            } else if (operation.contains("dot product")) {
                out = DOTPRODUCT(commands[1], commands[2]);
            } else if (operation.contains("double arrow")) {
                out = DOUBLEARROW(commands[1], commands[2]);
            } else if (operation.contains("double up arrow")) {
                out = DOUBLEUPARROW(commands[1], commands[2]);
            } else if (operation.contains("down arrow")) {
                if (operation.contains("double")) {
                    out = DOUBLEDOWNARROW(commands[1], commands[2]);
                } else {
                    out = DOWNARROW(commands[1], commands[2]);
                }
            } else if (operation.contains("equal")) {
                if (operation.contains("approximately")) {
                    out = APPROXIMATELYEQUALTO(commands[1], commands[2]);
                } else {
                    out = EQUAL(commands[1], commands[2]);
                }
            } else if (operation.contains("equivalent arrow")) {
                out = EQUIVALENT_ARROW(commands[1], commands[2]);
            } else if (operation.contains("identical")) {
                out = IDENTICAL(commands[1], commands[2]);
            } else if (operation.contains("implies")) {
                if (operation.contains("left")) {
                    out = IMLPLIESLEFT(commands[1], commands[2]);
                } else {
                    out = IMLPLIESRIGHT(commands[1], commands[2]);
                }
            } else if (operation.contains("in") && !operation.contains("tegra")) {
                if (operation.contains("not")) {
                    out = NOTIN(commands[1], commands[2]);
                } else {
                    out = IN(commands[1], commands[2]);
                }
            } else if (operation.contains("left arrow")) {
                out = LEFT_ARROW(commands[1], commands[2]);
            } else if (operation.contains("left arrow")) {
                out = LEFT_ARROW(commands[1], commands[2]);
            } else if (operation.contains("subset")) {
                if (operation.contains("not")) {
                    out = NOTSUBSET(commands[1], commands[2]);
                } else if (operation.contains("proper")) {
                    out = PROPERSUBSET(commands[1], commands[2]);
                } else {
                    out = SUBSET(commands[1], commands[2]);
                }
            } else if (operation.contains("superset")) {
                if (operation.contains("proper")) {
                    out = PROPERSUPERSET(commands[1], commands[2]);
                } else {
                    out = SUPERSET(commands[1], commands[2]);
                }
            } else if (operation.contains("subscript")) {
                out = SUBSCRIPT(commands[1], commands[2]);
            } else if (operation.contains("superscript")) {
                out = SUPERSCRIPT(commands[1], commands[2]);
            } else if (operation.contains("up arrow ")) {
                out = UPARROW(commands[1], commands[2]);
            } else if (operation.contains("right arrow ")) {
                out = RIGHT_ARROW(commands[1], commands[2]);
            } else if (operation.contains("+/-")) {
                out = PLUSORMINUS(commands[1], commands[2]);
            } else if (operation.contains("product of")) {
                out = PRODUCTOF(commands[1], commands[2]);
            } else if (operation.contains("proportional to")) {
                out = PROPORTIONALTO(commands[1], commands[2]);
            } else if (operation.contains("minimum of")) {
                out = MINIMUMOF(commands[1], commands[2], commands[3]);
            } else if (operation.contains("intersection")) {
                out = INTERSECTION(commands[1], commands[2], commands[3], commands[4]);
            } else if (operation.contains("union")) {
                out = UNION(commands[1], commands[2], commands[3], commands[4]);
            } else if (operation.contains("raw")) {
                out = commands[1];
            }
            if (!operation.contains("raw") && !operation.equals("custom text")) {
                /*
                 * dirty fix for conversion of R keywords to strings --> perform the retroconversion
                 */
                out = out.replaceAll("'infinity'", "infinity").replaceAll("'degree'", "degree").replaceAll("'minute'", "minute").replaceAll("'second'", "second").replaceAll("'nabla'", "nabla").replaceAll("'partialdiff'", "partialdiff");
                /*
                 * similarly dirty fix for greek (only in text)
                 */
                for (String string : greekPlain) {
                    out = out.replace("'" + string + "'", string);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        return out;
    }

    private static boolean isFormula(String math) {
        if (math.contains("'")) {
            return true;
        } else {
            for (String string : RLanguageElements) {
                if (math.contains(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String formatValue(String in) {
        if (!isFormula(in)) {
            return "'" + in + "'";
        } else {
            return in;
        }
    }

    public static String CONCATENATE(boolean autospace, String... text) {
        if (text == null || text.length == 0) {
            return "";
        }
        String out = "paste(";
        for (String string : text) {
            if (string == null || string.equals("")) {
                continue;
            }
            out += formatValue(string) + ", ";
            if (autospace) {
                out += " ' ', ";
            }
        }
        if (out.endsWith(" ' ', ")) {
            out = out.substring(0, out.length() - 6);
        }
        if (out.endsWith(", ")) {
            out = out.substring(0, out.length() - 2);
        }
        out += ")";
        out = out.replace("paste()", "");
        return out;
    }

    private static RFormula CONCATENATE(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }
        left = left instanceof RMathElement ? left : new RMathElement("greek", left.toString());
        right = right instanceof RMathElement ? right : new RMathElement("greek", right.toString());
        return new RFormula(left, right);
    }

    private static String CONCATENATE(String left, String right) {
        return "paste(" + formatValue(left) + ", " + formatValue(right) + ")";
    }

    public static String FRACTION(String num, String denom) {
        return "frac(" + formatValue(num) + ", " + formatValue(denom) + ")";
    }

    public static String getMathAsString(String formula) {
        return "expression(" + formula + ")";
    }

    public static String EQUAL(String left, String right) {
        return formatValue(left) + " == " + formatValue(right);
    }

    public static String APPROXIMATELYEQUALTO(String left, String right) {
        return formatValue(left) + "%~~%" + formatValue(right);
    }

    public static String CONGRUENT(String left, String right) {
        return formatValue(left) + "%=~%" + formatValue(right);
    }

    public static String PROPORTIONALTO(String left, String right) {
        return formatValue(left) + "%prop%" + formatValue(right);
    }

    public static String DISTRIBUTEDAS(String left, String right) {
        return formatValue(left) + "%~%" + formatValue(right);
    }

    public static String PROPERSUBSET(String left, String right) {
        return formatValue(left) + "%subset%" + formatValue(right);
    }

    public static String SUBSET(String left, String right) {
        return formatValue(left) + "%subseteq%" + formatValue(right);
    }

    public static String NOTSUBSET(String left, String right) {
        return formatValue(left) + "%notsubset%" + formatValue(right);
    }

    public static String PROPERSUPERSET(String left, String right) {
        return formatValue(left) + "%supset%" + formatValue(right);
    }

    public static String SUPERSET(String left, String right) {
        return formatValue(left) + "%supseteq%" + formatValue(right);
    }

    public static String IN(String left, String right) {
        return formatValue(left) + "%in%" + formatValue(right);
    }

    public static String NOTIN(String left, String right) {
        return formatValue(left) + "%notin%" + formatValue(right);
    }

    public static String DOUBLEARROW(String left, String right) {
        return formatValue(left) + "%<->%" + formatValue(right);
    }

    public static String UPARROW(String left, String right) {
        return formatValue(left) + "%up%" + formatValue(right);
    }

    public static String DOWNARROW(String left, String right) {
        return formatValue(left) + "%down%" + formatValue(right);
    }

    public static String IDENTICAL(String left, String right) {
        return formatValue(left) + "%==%" + formatValue(right);
    }

    public static String PLUSORMINUS(String left, String right) {
        return formatValue(left) + "%+-%" + formatValue(right);
    }

    public static String DIVIDE(String left, String right) {
        return formatValue(left) + "%/%" + formatValue(right);
    }

    public static String MULTIPLY(String left, String right) {
        return formatValue(left) + "%*%" + formatValue(right);
    }

    public static String DOTPRODUCT(String left, String right) {
        return formatValue(left) + "%.%" + formatValue(right);
    }

    public static String SQRT(String math, String power) {
        return "sqrt(" + formatValue(math) + ", " + formatValue(power) + ")";
    }

    public static String SQRT(String math) {
        return "sqrt(" + formatValue(math) + ")";
    }

    public static String LIST(String... list) {
        String lst = "list(";
        for (String string : list) {
            lst += formatValue(string) + ",";
        }
        if (lst.endsWith(", ")) {
            lst = lst.substring(0, lst.length() - 2);
        }
        lst += ")";
        return lst;
    }

    /**
     * puts one top over bottom without the fraction sign
     *
     * @param top
     * @param bottom
     * @return
     */
    public static String ATOP(String top, String bottom) {
        return "atop(" + formatValue(top) + ", " + formatValue(bottom) + ")";
    }

    public static String LEFT_ARROW(String left, String right) {
        return formatValue(left) + "%<-%" + formatValue(right);
    }

    public static String RIGHT_ARROW(String left, String right) {
        return formatValue(left) + "%->%" + formatValue(right);
    }

    public static String EQUIVALENT_ARROW(String left, String right) {
        return formatValue(left) + "%<=>%" + formatValue(right);
    }

    public static String IMLPLIESRIGHT(String left, String right) {
        return formatValue(left) + "%=>%" + formatValue(right);
    }

    public static String IMLPLIESLEFT(String left, String right) {
        return formatValue(left) + "%<=%" + formatValue(right);
    }

    public static String DOUBLEUPARROW(String left, String right) {
        return formatValue(left) + "%dblup%" + formatValue(right);
    }

    public static String DOUBLEDOWNARROW(String left, String right) {
        return formatValue(left) + "%dbldown%" + formatValue(right);
    }

    public static String DIFFERENT(String left, String right) {
        return formatValue(left) + " !=  " + formatValue(right);
    }

    public static String SUPERSCRIPT(String left, String right) {
        return formatValue(left) + " ^ " + formatValue(right);
    }

    public static String SUBSCRIPT(String left, String right) {
        return formatValue(left) + " [" + formatValue(right) + "]";
    }

    public static String HAT(String maths) {
        return "hat(" + formatValue(maths) + ")";
    }

    public static String WIDEHAT(String maths) {
        return "widehat(" + formatValue(maths) + ")";
    }

    public static String TILDE(String maths) {
        return "tilde(" + formatValue(maths) + ")";
    }

    public static String WIDETILDE(String maths) {
        return "widetilde(" + formatValue(maths) + ")";
    }

    public static String DOT(String maths) {
        return "dot(" + formatValue(maths) + ")";
    }

    public static String RING(String maths) {
        return "ring(" + formatValue(maths) + ")";
    }

    public static String BAR(String maths) {
        return "bar(" + formatValue(maths) + ")";
    }

    public static String ITALIC(String maths) {
        return "italic(" + formatValue(maths) + ")";
    }

    public static String UNDERLINE(String maths) {
        return "underline(" + formatValue(maths) + ")";
    }

    public static String BOLD(String maths) {
        return "bold(" + formatValue(maths) + ")";
    }

    public static String BOLDITALIC(String maths) {
        return "bolditalic(" + formatValue(maths) + ")";
    }

    public static String SUM(String formula, String fromLetter, String from, String to) {
        return "sum(" + formatValue(formula) + " , " + formatValue(fromLetter) + "==" + formatValue(from) + ", " + formatValue(to) + ")";
    }

    public static String UNION(String formula, String fromLetter, String from, String to) {
        return "union(" + formatValue(formula) + " , " + formatValue(fromLetter) + "==" + formatValue(from) + ", " + formatValue(to) + ")";
    }

    public static String INTERSECTION(String formula, String fromLetter, String from, String to) {
        return "intersect(" + formatValue(formula) + " , " + formatValue(fromLetter) + "==" + formatValue(from) + ", " + formatValue(to) + ")";
    }

    public static String LIMIT(String formula, String fromLetter, String to) {
        return "lim(" + formatValue(formula) + " , " + formatValue(fromLetter) + "%->%" + formatValue(to) + ")";
    }

    //TODO recode maybe in a more flexible way for the sign
    public static String MINIMUMOF(String formula, String fromLetter, String to) {
        return "min(" + formatValue(formula) + " , " + formatValue(fromLetter) + "%->%" + formatValue(to) + ")";
    }

    public static String PRODUCTOF(String formula, String FOR) {
        return "prod(" + formatValue(formula) + " , " + formatValue(FOR) + ")";
    }

    public static String SMALL(String formula) {
        return "scriptstyle(" + formatValue(formula) + ")";
    }

    public static String VERYSMALL(String formula) {
        return "scriptscriptstyle(" + formatValue(formula) + ")";
    }

    public static String INTEGRAL(String formula, String from, String to) {
        return "integral(" + formatValue(formula) + " , " + formatValue(from) + ", " + formatValue(to) + ")";
    }

    public static String INFIMUM(String maths) {
        return "inf(" + formatValue(maths) + ")";
    }

    public static String SUPREMUM(String maths) {
        return "sup(" + formatValue(maths) + ")";
    }

    public static String GROUP(String formula, String delimiter) {
        String delimiterLeft = "(";
        String delimiterRight = ")";
        if (delimiter.equals("{") || delimiter.equals("}")) {
            delimiterLeft = "{";
            delimiterRight = "}";
        } else if (delimiter.equals("]") || delimiter.equals("[")) {
            delimiterLeft = "[";
            delimiterRight = "]";
        }
        return "group(" + "'" + delimiterLeft + "', " + formatValue(formula) + ", '" + delimiterRight + "')";
    }

    public static String SCALABLEBRACKETS(String formula, String delimiter) {
        return BGROUP(formula, delimiter);
    }

    public static String BGROUP(String formula, String delimiter) {
        String delimiterLeft = "(";
        String delimiterRight = ")";
        if (delimiter.equals("{") || delimiter.equals("}")) {
            delimiterLeft = "{";
            delimiterRight = "}";
        } else if (delimiter.equals("]") || delimiter.equals("[")) {
            delimiterLeft = "[";
            delimiterRight = "]";
        }
        return "bgroup(" + "'" + delimiterLeft + "', " + formatValue(formula) + ", '" + delimiterRight + "')";
    }

    public static String SPECIALDELIMITER(String formula) {
        return "bgroup(" + "lceil" + ", " + formatValue(formula) + ", " + "rceil" + ")";
    }

    public static String SUPERIOR(String left, String right, boolean equal) {
        String SUP = ">";
        if (equal) {
            SUP += "=";
        }
        return formatValue(left) + " " + SUP + " " + formatValue(right);
    }

    public static String INFERIOR(String left, String right, boolean equal) {
        String SUP = "<";
        if (equal) {
            SUP += "=";
        }
        return formatValue(left) + " " + SUP + " " + formatValue(right);
    }

    public String MINUTE(String left) {
        return formatValue(left) + " *minute";
    }

    public String SECOND(String left) {
        return formatValue(left) + " *second";
    }

    public String DEGREE(String left) {
        return formatValue(left) + " *degree";
    }

    //add integrals and so on
    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        RMaths test = new RMaths();
        System.out.println(test.getMathAsString(test.FRACTION("10", "3")));
        System.out.println(test.getMathAsString(test.LEFT_ARROW("10", "3")));
        System.out.println(RMaths.getMathAsString(RMaths.SUM("x^n", "n", "0", "100")));
        System.out.println(test.getMathAsString(test.SUPERIOR("10", "3", true)));
        System.out.println(test.getMathAsString(test.LIST("10", "3", "true")));
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        //test de combinaisons
        //ca marche --> le faire de partout et mettre ttes les formules
        System.out.println(test.getMathAsString(test.FRACTION(test.FRACTION(test.FRACTION("10", "2"), "3"), "4"))); //--> does not work
        System.out.println(test.getMathAsString(test.LEFT_ARROW(test.FRACTION("10", "3"), "3")));

        System.out.println(test.getMathAsString(test.FRACTION(test.RIGHT_ARROW("10", "3"), "3")));

        System.out.println(RMaths.getMathAsString(RMaths.INTEGRAL("f(x)", "x", "y")));




        //TODO essayer une formule tres complexe pr voir si sa marche

        //on dirait que ca mache
        System.out.println(RMaths.getMathAsString(RMaths.INTEGRAL(test.FRACTION("f(x)", test.FRACTION("ax+b", "2")), "x", "y")));
        RMathElement rme1 = new RMathElement("raw", "sqrt(3)");
        //RMathElement rme2 = new RMathElement("greek", "test");
        String rme2 = "alphax";
        System.out.println(test.CONCATENATE(rme1, rme2));//ca marche mais voir si hybride //--> si hybride --> convertir en texte ???

        //+xlab(expression(frac(frac('10', '2'), '3'))) --> ok 
        //+xlab(expression(frac(paste(frac('10', '2')), '3'))) --> ok
        //+xlab(expression(frac('frac('10', '2')', '3'))) --> bad
        //expression(frac(frac('10', '2'), '3'))
        System.exit(0);
    }
}


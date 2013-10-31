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
//TODO since order of the rules does matter --> make it possible to reorder rules --> TODO
package Checks;

import Commons.CommonClassesLight;
import Tools.StyledDocTools;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyledDocument;

/**
 * SFRules contains text controls, a warning message and suggested regex
 * solutions to fix the pb. NB: if any of the controls returns true, we warn the
 * user and propose some solutions to solve the pbs of text formatting.
 *
 * @author Benoit Aigouy
 */
public class SFTextController {
    /**
     * Rules with a nick name are official rules and can be reloaded via their
     * nickname, other rules are unofficial and should be reloaded from text. As
     * soon as a rule is edited we reset its nickname and consider it as a
     * custom rule that should not be overriden by an official rule.
     */
    private String nickname = null;
    private static String[] validControls = new String[]{"contains", "matches", "startswith", "endswith"};
    private static String[] validReplace = new String[]{"replace", "replaceAll", "replaceFirst",};
    public static final SFTextController STARTS_WITH_PERCENTAGE_SIGN = new SFTextController(
            /**
             * we give official rules a nickname (non official rules have no
             * nicknames)
             */
            "STARTS_WITH_PERCENTAGE_SIGN",
            /**
             * we set the controls typically startswith, contains endswis or
             * matches a specific regex pattern
             */
            new String[]{STARTS_WITH("%")},
            /**
             * now we set the message(s) the users will see when one of the
             * controls is positive (returned true)
             */
            new String[]{"Your label starts with a '%' symbol, please move it to the end:<br><font color=#00AA00>'Yield (%)' </font>instead of<font color=#ff0000> '% Yield'</font>"},
            /**
             * now we set the solutions, typically some replaceAll, replaceFirst
             * with some custom matching patterns and some replacement string or
             * regex to fix the text
             */
            new String[]{REPLACE_ALL("^(%)\\s{0,}(.*)", "$2 ($1)")},
            /**
             * now we just store a sample string to test the function
             */
            new String[]{"% test", "%test"});
    public static final SFTextController CONTAINS_INFINITY = new SFTextController(
            "CONTAINS_INFINITY",
            new String[]{MATCHES("(?i)infinity")},
            new String[]{"Your label contains plain text 'infinity', we suggest you replace it by the '" + CommonClassesLight.INFINITY + "' symbol:<br><font color=#00AA00>'+" + CommonClassesLight.INFINITY + " -" + CommonClassesLight.INFINITY + "' </font>instead of<font color=#ff0000> '+infinity -infinity'</font>"},
            new String[]{REPLACE_ALL("(?i)infinity", CommonClassesLight.INFINITY + "")},
            new String[]{"+infinity -infinity"});
    public static final SFTextController CONTAINS_GREATER_OR_EQUAL = new SFTextController(
            "CONTAINS_GREATER_OR_EQUAL",
            new String[]{CONTAINS(">=")},
            new String[]{"Your label contains '>=' instead of the '" + CommonClassesLight.GREATER_OR_EQUAL + "' symbol, we recommend you change this:<br><font color=#00AA00>'10 " + CommonClassesLight.GREATER_OR_EQUAL + " 1' </font>instead of<font color=#ff0000> '10 >= 1'</font>"},
            new String[]{REPLACE_ALL(">=", CommonClassesLight.GREATER_OR_EQUAL + "")},
            new String[]{"10&#xsup=1"});
    public static final SFTextController CONTAINS_LESS_OR_EQUAL = new SFTextController(
            "CONTAINS_LESS_OR_EQUAL",
            new String[]{CONTAINS("<=")},
            new String[]{"Your label contains '<=' instead of the '" + CommonClassesLight.LESS_OR_EQUAL + "' symbol, we recommend you change this:<br><font color=#00AA00>'1 " + CommonClassesLight.LESS_OR_EQUAL + " 10' </font>instead of<font color=#ff0000> '1 <= 10'</font>"},
            new String[]{REPLACE_ALL("<=", CommonClassesLight.LESS_OR_EQUAL + "")},
            new String[]{"1&#xinf=10"});
    public static final SFTextController CONTAINS_PLUS_MINUS = new SFTextController(
            "CONTAINS_PLUS_MINUS",
            new String[]{CONTAINS("+/-")},
            new String[]{"Your label contains '+/-' instead of the '" + CommonClassesLight.PLUS_MINUS + "' symbol, we recommend you change this:<br><font color=#00AA00>'10 " + CommonClassesLight.PLUS_MINUS + " 1' </font>instead of<font color=#ff0000> '10 +/- 1'</font>"},
            new String[]{REPLACE_ALL("\\+/-", CommonClassesLight.PLUS_MINUS + "")},
            new String[]{"10 +/- 1"});
    public static final SFTextController STARTS_WITH_PERCENTAGE_SIGN2 = new SFTextController(
            "STARTS_WITH_PERCENTAGE_SIGN2",
            new String[]{STARTS_WITH("(%)")},
            new String[]{"Your label starts with '(%)', please move it to the end:<br><font color=#00AA00>'Yield (%)' </font>instead of<font color=#ff0000> '(%) Yield'</font>"},
            new String[]{REPLACE_ALL("^\\s{0,}\\((%)\\)\\s{0,}(.*)", "$2 ($1)")},
            new String[]{"(%) test", "(%)test"});
    public static final SFTextController STARTS_WITH_WHITE_SPACES = new SFTextController(
            "STARTS_WITH_WHITE_SPACES",
            new String[]{STARTS_WITH(" ")},
            new String[]{"Your text starts with one or more spaces, we suggest that you remove all leading spaces:<br><font color=#00AA00> 'Your text' </font>instead of<font color=#ff0000> '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your text'</font>"},
            new String[]{REPLACE_ALL("^\\ {1,}", "")},
            new String[]{"    Your text"});
    public static final SFTextController ENDS_WITH_WHITE_SPACES = new SFTextController(
            "ENDS_WITH_WHITE_SPACES",
            new String[]{ENDS_WITH(" ")},
            new String[]{"Your text ends with one or more spaces, we suggest that you remove all trailing spaces:<br><font color=#00AA00>'Your text' </font>instead of<font color=#ff0000> 'Your text&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'</font>"},
            new String[]{REPLACE_ALL("\\ {1,}$", "")},
            new String[]{"Your text         "});
    public static final SFTextController CONTAINS_CONSECUTIVE_SPACES = new SFTextController(
            "CONTAINS_CONSECUTIVE_SPACES",
            new String[]{CONTAINS("  ")},
            new String[]{"Your text contains two or more consecutive spaces, we suggest that you remove all duplicated spaces:<br><font color=#00AA00>'Your text contains too many white spaces' </font>instead of<font color=#ff0000> 'Your&nbsp;&nbsp;text&nbsp;contains&nbsp;&nbsp;&nbsp;too many&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;white spaces'</font>"},
            new String[]{REPLACE_ALL(" {2,}", " ")},
            new String[]{"there   are too  many      spaces here"});
    public static final SFTextController REPLACE_STAR_BY_MULTIPLICATION_SIGN = new SFTextController(
            "REPLACE_STAR_BY_MULTIPLICATION_SIGN",
            new String[]{MATCHES("[0-9]{1,} {0,}\\* {0,}[0-9]{1,}")}, //any * sign surrounded by numbers
            new String[]{"Your text contains one ot more '*' (multiplication) symbol we suggest you replace them with '" + CommonClassesLight.MULTIPLICATION + "' symbols:<br><font color=#00AA00>'a=2" + CommonClassesLight.MULTIPLICATION + "b+c' </font>instead of<font color=#ff0000> 'a=2*b+c'</font>"},
            new String[]{REPLACE_ALL("([0-9]{1,} {0,})(\\*)( {0,}[0-9]{1,})", "$1" + CommonClassesLight.MULTIPLICATION + "$3")},
            new String[]{" **** 10*20=200 10 * 10 = 100 10+10 = 20"});
    public static final SFTextController REPLACE_MINUS_BY_EN_DASH_SIGN = new SFTextController(
            "REPLACE_MINUS_BY_EN_DASH_SIGN",
            new String[]{MATCHES("- {0,}[0-9]{1,}")}, //any - sign surrounded by numbers
            new String[]{"Your text contains '-' (hyphen) symbols before negative values or as indicators for ranges, we suggest you replace them with (EN_DASH) '" + CommonClassesLight.EN_DASH + "' symbols:<br><font color=#00AA00>'text-text2 " + CommonClassesLight.EN_DASH + "10 // 10" + CommonClassesLight.EN_DASH + "20' </font>instead of<font color=#ff0000> 'text-text2 -10 // 10-20'</font>"},
            new String[]{REPLACE_ALL("(-)( {0,}[0-9]{1,})", CommonClassesLight.EN_DASH + "$2")},
            new String[]{"text-text  text - text - 10 -10 10 -30 10-40 tot 10-20"});
    public static final SFTextController REPLACE_SLASHES_BY_AND_OR_HYPHEN_NO_SPACE = new SFTextController(
            "REPLACE_SLASHES_BY_AND_OR_HYPHEN_NO_SPACE",
            new String[]{MATCHES("[^\\d+]{1,2}/[^\\d-]{1,2}")}, //any - sign surrounded by numbers
            new String[]{"Your text contains slashes '/', we suggest you replace them with plain text 'and', 'or' or hyphens '-':<br><font color=#00AA00>'red-green-blue' </font>instead of<font color=#ff0000> 'red/green/blue'</font>"},
            new String[]{REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1-$3"), //modified it to exclude +/- from this rule
        REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1 and $3"),
        REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1 or $3")},
            new String[]{"red/green/blue 10/20"});
    public static final SFTextController REPLACE_SLASHES_BY_AND_OR_HYPHEN = new SFTextController(
            "REPLACE_SLASHES_BY_AND_OR_HYPHEN",
            new String[]{MATCHES("[^\\d+]{1,2}/[^\\d-]{1,2}")}, //any - sign surrounded by numbers
            new String[]{"Your text contains slashes '/', we suggest you replace them with plain text 'and', 'or' or hyphens '-':<br><font color=#00AA00>'red - green - blue' </font>instead of<font color=#ff0000> 'red / green / blue'</font>"},
            new String[]{REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1-$3"),
        REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1and$3"),
        REPLACE_ALL("([^\\d+]{1,2} {0,})(/)( {0,}[^\\d-]{1,2})", "$1or$3")},
            new String[]{"red/green/blue 10/20"});
    public static final SFTextController REPLACE_UNIT_DIVISIONS_BY_SUPERSCRIPT = new SFTextController(
            "REPLACE_UNIT_DIVISIONS_BY_SUPERSCRIPT",
            new String[]{MATCHES("[^\\s0-9]{1,2}/\\b[^\\s0-9]{1,2}\\b")},
            new String[]{"<html>Your text (probably) contains unit divisions, we suggest you replace them with superscript:<br><font color=#00AA00>'10 ms<SUP>-1</SUP>' </font>instead of<font color=#ff0000> '10 m/s'</html>"},
            new String[]{REPLACE_ALL("([^\\s0-9]{1,2})/\\b([^\\s0-9]{1,2})\\b", "<html>$1" + CommonClassesLight.MIDDLE_DOT + "$2<sup>-1</sup>")},
            new String[]{"text 120m/s 30 m/s 300/60 texte 30µm/s txt qsd/test"});
    public static final SFTextController ADD_SPACE_BETWEEN_NUMBER_AND_TEXT_EXCEPT_IF_NOT_HYPHENATED = new SFTextController(
            "ADD_SPACE_BETWEEN_NUMBER_AND_TEXT_EXCEPT_IF_NOT_HYPHENATED",
            new String[]{MATCHES("([0-9]{1,})([^ 0-9-+*=/×°\\.,]{1})")},
            new String[]{"You did not leave a space between a text and a number, we suggest you put one:<br><font color=#00AA00>'10 mm 10 µm' </font>instead of<font color=#ff0000> '10mm 10µm'</font>"},
            new String[]{REPLACE_ALL("([0-9]{1,})([^ 0-9-+*=/×°\\.,]{1})", "$1 $2")},
            new String[]{"text 5-µm 1234µm 120 µm 10µm 10mm 0.01mm 0,01mm"});
    public static final SFTextController CAPITALIZE_FIRST_LETTER_WISELY = new SFTextController(
            "CAPITALIZE_FIRST_LETTER_WISELY",
            new String[]{MATCHES("(^[a-z]{1})([^A-Z]{1,}\\b)")},
            new String[]{"You did not capitalize the first letter, we suggest you do so:<br><font color=#00AA00>'This is a test' </font>instead of<font color=#ff0000> 'this is a test'</font>"},
            new String[]{REPLACE_FIRST("(^[a-z]{1})([^A-Z]{1,}\\b)", "<html><cap>$1</cap>$2")},
            new String[]{"this is a test"});
    public static final SFTextController ITALICIZE_ISOLATED_LETTERS_NO_RULES = new SFTextController(
            "ITALICIZE_ISOLATED_LETTERS_NO_RULES",
            new String[]{MATCHES("\\b([a-zA-Z]{1})\\s")},//the .+ is to ignore single letters
            new String[]{"<html>You did not italicize isolated letters, we suggest you do so:<br><font color=#00AA00>'text <I>P</I> &lt; 0.05 <I>x</I> plane' </font>instead of<font color=#ff0000> 'text P &lt; 0.05 x plane'</html>"},
            new String[]{REPLACE_ALL("\\b([a-zA-Z]{1})(\\s)", "<html><i>$1</i>$2")},
            new String[]{"P&#xsup0.5 a text P &#xinf 0.05 x plane T=0.3"});
    public static final SFTextController ITALICIZE_ISOLATED_LETTERS_ASSOCIATED_TO_MATH_SIGN = new SFTextController( //TODO try to find a more intelligent rule maybe exclude also 'a'--> think about it
            "ITALICIZE_ISOLATED_LETTERS_ASSOCIATED_TO_MATH_SIGN",
            new String[]{MATCHES("[a-zA-Z]{1}\\s{0,}[=><]")},
            new String[]{"<html>You did not italicize isolated letters in front of maths, we suggest you do so:<br><font color=#00AA00>'text <I>P</I> &lt; 0.05 x plane' </font>instead of<font color=#ff0000> 'text P &lt; 0.05 x plane'</html>"},
            new String[]{REPLACE_ALL("([a-zA-Z]{1})(\\s{0,}[=><])", "<html><i>$1</i>$2")},
            new String[]{"P&#xsup0.5 a text P &#xinf 0.05 x plane T=0.3"});
    public static final SFTextController REPLACE_PRIMES_PLAIN_BY_PRIME_SYMBOLS = new SFTextController( //TODO try to find a more intelligent rule maybe exclude also 'a' --> think about it TODO en faire une version precedee par un symbol
            "REPLACE_PRIMES_PLAIN_BY_PRIME_SYMBOLS",
            new String[]{MATCHES("\\b[Pp]rime[s]{0,1}\\b")},
            new String[]{"You used plain text 'prime' instead of the \"'\" symbol, we suggest you replace plain text:<br><font color=#00AA00>\" ' \" </font>instead of<font color=#ff0000> \" prime \""},
            new String[]{REPLACE_ALL("\\b\\s{0,}[Pp]rime[s]{0,1}\\b", "'")},
            new String[]{"prime a > a prime"});
    public static final SFTextController REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_NUMBER = new SFTextController(
            "REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_NUMBER",
            new String[]{MATCHES("[0-9]\\s{0,}\\b[Dd]egree[s]{0,1}\\b")},
            new String[]{"You used plain text 'degree(s)' instead of the '°' symbol, we suggest you replace plain text:<br><font color=#00AA00>'30°' </font>instead of<font color=#ff0000> '30 degrees'</font>"},
            new String[]{REPLACE_ALL("([0-9])\\s{0,}\\b[Dd]egree[s]{0,1}\\b", "$1°")},
            new String[]{"degrees 60 degrees +/- 1 degree other text degree"});
    public static final SFTextController REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_TEXT = new SFTextController(
            "REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_TEXT",
            new String[]{MATCHES("\\s{0,}[Dd]egree[s]{0,1}\\b")},
            new String[]{"You used plain text 'degree(s)' instead of the '°' symbol, we suggest you replace plain text:<br><font color=#00AA00>'several °' </font>instead of<font color=#ff0000> 'several degrees'</font>"},
            new String[]{REPLACE_ALL("\\s{0,}[Dd]egree[s]{0,1}\\b", "°")},
            new String[]{"degrees 60 degrees +/- 1 degree other text degree"});
    /**
     * we group them in an arrayList
     */
    public static final ArrayList<SFTextController> list = new ArrayList<SFTextController>();

    static {
        list.add(CONTAINS_PLUS_MINUS);
        list.add(CONTAINS_INFINITY);
        list.add(CONTAINS_GREATER_OR_EQUAL);
        list.add(CONTAINS_LESS_OR_EQUAL);
        list.add(STARTS_WITH_PERCENTAGE_SIGN);
        list.add(STARTS_WITH_PERCENTAGE_SIGN2);
        list.add(STARTS_WITH_WHITE_SPACES);
        list.add(ENDS_WITH_WHITE_SPACES);
        list.add(CONTAINS_CONSECUTIVE_SPACES);
        list.add(REPLACE_STAR_BY_MULTIPLICATION_SIGN);
        list.add(REPLACE_MINUS_BY_EN_DASH_SIGN);
        list.add(REPLACE_SLASHES_BY_AND_OR_HYPHEN_NO_SPACE);
        list.add(REPLACE_SLASHES_BY_AND_OR_HYPHEN);
        list.add(REPLACE_UNIT_DIVISIONS_BY_SUPERSCRIPT);
        list.add(ADD_SPACE_BETWEEN_NUMBER_AND_TEXT_EXCEPT_IF_NOT_HYPHENATED);
        list.add(CAPITALIZE_FIRST_LETTER_WISELY);
        list.add(ITALICIZE_ISOLATED_LETTERS_NO_RULES);
        list.add(ITALICIZE_ISOLATED_LETTERS_ASSOCIATED_TO_MATH_SIGN);
        list.add(REPLACE_PRIMES_PLAIN_BY_PRIME_SYMBOLS);
        list.add(REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_NUMBER);
        list.add(REPLACE_DEGREES_PLAIN_BY_DEGREES_SYMBOLS_PRECEDED_BY_A_TEXT);
    }
    private static final int CONTROLS = 0;
    private static final int MESSAGE = 1;
    private static final int SOLUTIONS = 2;
    private static final int TEST_STRING = 3;
    String[][] SFController = new String[4][];

    public SFTextController() {
    }

    public SFTextController(SFTextController rule) {
        this.nickname = rule.nickname;
        this.SFController = rule.SFController.clone();
    }

    public SFTextController(String ruleAsXmlText) {
        this.SFController = getRuleFromString(ruleAsXmlText).SFController;
    }

    /**
     *
     * @param nickname nicknames are reserved for official rules only
     * @param controls
     * @param messages
     * @param solutions
     * @param testStrings
     */
    public SFTextController(String nickname, String[] controls, String[] messages, String[] solutions, String[] testStrings) {
        this.nickname = nickname;
        SFController[CONTROLS] = controls;
        SFController[MESSAGE] = messages;
        SFController[SOLUTIONS] = solutions;
        SFController[TEST_STRING] = testStrings;
    }

    public String[][] getSFController() {
        return SFController;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSFController(String[][] SFController) {
        this.SFController = SFController;
    }

    public void setControls(String... controls) {
        SFController[CONTROLS] = controls;
    }

    public void setMessages(String... messages) {
        SFController[MESSAGE] = messages;
    }

    public void setSolutions(String... solutions) {
        SFController[SOLUTIONS] = solutions;
    }

    public void setTestStrings(String... testStrings) {
        SFController[TEST_STRING] = testStrings;
    }

    public String[] getControls() {
        return SFController[CONTROLS];
    }

    public String[] getMessages() {
        return SFController[MESSAGE];
    }

    public String[] getSolutions() {
        return SFController[SOLUTIONS];
    }

    public String[] getTestStrings() {
        return SFController[TEST_STRING];
    }

    public static String CONTAINS(String regexPattern) {
        return CREATE_CONTROL_COMMAND("contains", regexPattern);
    }

    public static String STARTS_WITH(String regexPattern) {
        return CREATE_CONTROL_COMMAND("startsWith", regexPattern);
    }

    public static String ENDS_WITH(String regexPattern) {
        return CREATE_CONTROL_COMMAND("endsWith", regexPattern);
    }

    public static String MATCHES(String regexPattern) {
        return CREATE_CONTROL_COMMAND("matches", regexPattern);
    }

    public static String REPLACE_FIRST(String oldSequence, String newSequence) {
        return CREATE_REPLACE_COMMAND("replaceFirst", oldSequence, newSequence);
    }

    public static String REPLACE_ALL(String oldSequence, String newSequence) {
        return CREATE_REPLACE_COMMAND("replaceAll", oldSequence, newSequence);
    }

    public static String DELETE(String oldSequence) {
        return CREATE_REPLACE_COMMAND("replaceAll", oldSequence, "");
    }

    public static String DELETE_FIRST(String oldSequence) {
        return CREATE_REPLACE_COMMAND("replaceFirst", oldSequence, "");
    }

    private static String CREATE_REPLACE_COMMAND(String command, String oldSequence, String newSequence) {
        if (command == null || command.isEmpty() || oldSequence == null || newSequence == null) {
            return null;
        }
        return command + "(\"" + oldSequence + "\", \"" + newSequence + "\")";
    }

    private static String CREATE_CONTROL_COMMAND(String control, String regexPattern) {
        if (control == null || control.isEmpty() || regexPattern == null || regexPattern.isEmpty()) {
            return null;
        }
        return control + "(\"" + regexPattern + "\")";
    }

    public static String[] extractCommandParametersButDontConvertUnicode(String command) {
        if (command == null || command.isEmpty()) {
            return null;
        }
        /**
         * pattern is of type: bla("qsdsq","blaaze")
         */
        Pattern pattern = Pattern.compile("(.*)\\([\\s]{0,}\"(.*)\"[\\s]{0,},[\\s]{0,}[\\s]{0,}\"(.*)\"[\\s]{0,}\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(command);
        if (!matcher.find()) {
            /**
             * pattern is of type: bla("qsdsq")
             */
            pattern = Pattern.compile("(.*)\\([\\s]{0,}\"(.*)\"[\\s]{0,}\\)", Pattern.DOTALL);
            matcher = pattern.matcher(command);
        } else {
            /*
             * NB we must reset the matcher after running a matcher.matches() call
             */
            matcher.reset();
        }

        if (!matcher.find()) {
            return null;
        } else {
            matcher.reset();
        }

        int count = matcher.groupCount();
        String[] regexs = new String[count];
        while (matcher.find()) {
            for (int i = 1; i <= count; i++) {
                String macth = matcher.group(i);
                if (macth == null) {
                    return null;
                }
                regexs[i - 1] = macth;
            }
        }
        return regexs;
    }

    private static String[] extractCommandParameters(String command) {
        String[] regexs = extractCommandParametersButDontConvertUnicode(command);

        if (regexs == null || regexs.length == 0) {
            return null;
        }
        /*
         * bug fix for erroneous conversion of unicode to string
         */
        for (int i = 1; i < regexs.length; i++) {
            Pattern p = Pattern.compile("\\\\u[0-9a-zA-Z]{4}");
            String cleanedString = regexs[i];
            Matcher m = p.matcher(cleanedString);
            while (m.find()) {
                cleanedString = cleanedString.replace(m.group(), CommonClassesLight.getCharFromUnicode(m.group()) + "");
            }
            regexs[i] = cleanedString;
        }

        return regexs;
    }

    public static Object executeCommand(String txt, String commandAndPatterns) {
        try {
            if (txt == null) {
                return null;
            }
            String[] commands = extractCommandParameters(commandAndPatterns);
            if (commands == null) {
                return null;
            }
            String command = commands[0];
            if (command == null) {
                return null;
            }

            command = command.toLowerCase();
            /**
             * here we perform a real replace not a replace all the advantage of
             * replace is that it does rely on a series of chars and not on
             * regex, sometimes it can be interesting to have it as well
             */
            if (command.contains("replace") && !command.contains("irst") && !command.contains("ll")) {
                return txt.replace(commands[1], commands[2]);
            }
            if (command.contains("contains")) {
                return txt.contains(commands[1]);
            }
            if (command.contains("matches")) {
                if (txt.isEmpty()) {
                    return false;
                }
                Pattern p = Pattern.compile(commands[1]);
                Matcher m = p.matcher(txt);
                return m.find();
            }
            if (command.contains("startswith")) {
                return txt.startsWith(commands[1]);
            }
            if (command.contains("endswith")) {
                return txt.endsWith(commands[1]);
            }
            if (command.contains("replacefirst")) {
                return txt.replaceFirst(commands[1], commands[2]);
            }
            if (command.contains("replaceall")) {
                return txt.replaceAll(commands[1], commands[2]);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static Object executeCommand(StyledDocument doc, String commandAndPatterns) {
        if (doc == null) {
            return null;
        }
        String[] commands = extractCommandParameters(commandAndPatterns);
        if (commands == null) {
            return doc;
        }
        String command = commands[0];
        if (command == null) {
            return doc;
        }
        command = command.toLowerCase();
        /**
         * here we perform a real replace not a replace all the advantage of
         * replace is that it does rely on a series of chars and not on regex,
         * sometimes it can be interesting to have it as well
         */
        if (command.contains("replace") && !command.contains("irst") && !command.contains("ll")) {
            return StyledDocTools.replace(doc, commands[1], commands[2]);
        }
        if (command.contains("contains")) {
            return StyledDocTools.contains(doc, commands[1]);
        }
        if (command.contains("matches")) {
            return StyledDocTools.matches(doc, commands[1]);
        }
        if (command.contains("startswith")) {
            return StyledDocTools.startsWith(doc, commands[1]);
        }
        if (command.contains("endswith")) {
            return StyledDocTools.endsWith(doc, commands[1]);
        }
        if (command.contains("replacefirst")) {
            return StyledDocTools.replaceFirst(doc, commands[1], commands[2]);
        }
        if (command.contains("replaceall")) {
            return StyledDocTools.replaceAll(doc, commands[1], commands[2]);
        }
        return null;
    }

    @Override
    public String toString() {
        return toString(this);
    }

    public static String toString(SFTextController rule) {
        if (rule == null || rule.SFController == null) {
            return null;
        }
        String out = "";
        if (rule.nickname != null) {
            out = "\"" + rule.nickname + "\";\n";
        }
        for (String[] content : rule.SFController) {
            out += "{";
            for (String string : content) {
                out += "\"" + string + "\",";
            }
            if (out.endsWith(",")) {
                out = out.substring(0, out.length() - 1);
            }
            out += "};\n";
        }
        if (out.endsWith("\n")) {
            out = out.substring(0, out.length() - 1);
        }
        return out;
    }

    private static String getRuleTitle(String ruleText) {
        String ruleRegexPattern = "^\\s{0,}\"(.*)\"\\s{0,}[;]";
        Pattern p = Pattern.compile(ruleRegexPattern);
        Matcher m = p.matcher(ruleText);
        if (m.find()) {
            String title = m.group(1);
            if (title.trim().toLowerCase().equals("null") || title.trim().toLowerCase().equals("")) {
                return null;
            }
            return title;
        }
        return null;
    }

    public static SFTextController getRuleFromString(String ruleText) {
        if (ruleText == null || ruleText.isEmpty()) {
            return null;
        }
        SFTextController rule = new SFTextController();
        try {
            String nickName = getRuleTitle(ruleText);
            /**
             * we have found an official rule we try to reload it properly
             */
            if (nickName != null) {
                for (SFTextController officialRule : list) {
                    if (officialRule.getNickname().equals(nickName)) {
                        return new SFTextController(officialRule);
                    }
                }
            }
            /**
             * apparently the rule is a custom one or is a derivative from an
             * official rule --> better reload it from text
             */
            String ruleRegexPattern = "(\\{\".*?\"\\}[;])";
            Pattern p = Pattern.compile(ruleRegexPattern, Pattern.DOTALL);
            Matcher m = p.matcher(ruleText);

            /**
             * DOTALL is necessary to avoid pbs with \n in a string
             */
            ArrayList<String> chunks = new ArrayList<String>();
            while (m.find()) {
                String out = m.group(1);
                chunks.add(out);
            }
            /**
             * first step is to parse the 4 tables of the rule
             */
            String[][] reloadedRule = new String[chunks.size()][];
            int pos = 0;
            for (String string : chunks) {
                Pattern subP = Pattern.compile("\"(.*)\"[^,}]{0,1}", Pattern.DOTALL);
                Matcher subM = subP.matcher(string);
                ArrayList<String> content = new ArrayList<String>();
                while (subM.find()) {
                    String out = subM.group(1);
                    content.add(out);
                }
                reloadedRule[pos] = content.toArray(new String[content.size()]);;
                pos++;
            }
            rule.setSFController(reloadedRule);
            rule.setNickname(nickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule;
    }

    public boolean check(boolean verbose, Component c) {
        String[] controls = getControls();
        if (controls == null || controls.length == 0) {
            if (verbose) {
                CommonClassesLight.Warning(c, "(SFRule) no controls/checks found");
            }
            return false;
        } else {
            for (String string : controls) {
                /*
                 * check the validity of controls
                 */
                String[] params = extractCommandParameters(string);
                if (params == null) {
                    if (verbose) {
                        CommonClassesLight.Warning(c, "(SFRule) invalid control detected:\n" + string);
                    }
                    return false;
                }
                boolean found = false;
                for (String string1 : validControls) {
                    if (params[0].toLowerCase().contains(string1)) {
                        found = true;
                    }
                }
                if (!found) {
                    if (verbose) {
                        CommonClassesLight.Warning(c, "(SFRule) invalid control detected:\n" + string);
                    }
                    return false;
                }
            }
        }
        String[] solutions = getSolutions();
        if (solutions == null || solutions.length == 0) {
            if (verbose) {
                CommonClassesLight.Warning(c, "(SFRule) no solutions found");
            }
            return false;
        } else {
            for (String string : solutions) {
                /*
                 * check the validity of solutions
                 */
                String[] params = extractCommandParameters(string);
                if (params == null) {
                    if (verbose) {
                        CommonClassesLight.Warning(c, "(SFRule) invalid solution detected:\n" + string);
                    }
                    return false;
                }
                boolean found = false;
                for (String string1 : validReplace) {
                    if (params[0].toLowerCase().contains(string1)) {
                        found = true;
                    }
                }
                if (!found) {
                    if (verbose) {
                        CommonClassesLight.Warning(c, "(SFRule) invalid solution detected:\n" + string);
                    }
                    return false;
                }
            }
        }
        /**
         * if a solution could be found
         */ 
        return true;
    }

    public String getFirstControl() {
        if (SFController == null) {
            return "";
        }
        if (SFController[CONTROLS] == null || SFController[CONTROLS].length == 0) {
            return "";
        }
        return SFController[CONTROLS][0];
    }

    public String getFirstMessage() {
        if (SFController == null) {
            return "";
        }
        if (SFController[MESSAGE] == null || SFController[MESSAGE].length == 0) {
            return "";
        }
        return SFController[MESSAGE][0];
    }

    public String getFirstSolution() {
        if (SFController == null) {
            return "";
        }
        if (SFController[SOLUTIONS] == null || SFController[SOLUTIONS].length == 0) {
            return "";
        }
        return SFController[SOLUTIONS][0];
    }

    public String getFirstTestString() {
        if (SFController == null) {
            return "";
        }
        if (SFController[TEST_STRING] == null || SFController[TEST_STRING].length == 0) {
            return "";
        }
        return SFController[TEST_STRING][0];
    }

    public void mutate() {
        nickname = null;
    }

    public boolean equals(SFTextController obj) {
        if (nickname != null) {
            if (obj.getNickname() == null) {
                return false;
            } else {
                return nickname.equals(obj.getNickname());
            }
        } else {
            if (obj.getNickname() != null) {
                return false;
            }
        }
        return false;
    }

    public boolean equalsContent(SFTextController obj) {
        try {
            if (Arrays.deepEquals(SFController, obj.SFController)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean equalsCoreContent(SFTextController obj) {
        if (obj == null) {
            return false;
        }
        if (SFController == null && obj.SFController != null) {
            return false;
        }
        if (SFController != null && obj.SFController == null) {
            return false;
        }
        if (SFController == null && obj.SFController == null) {
            return true;
        }
        try {
            if (Arrays.deepEquals(SFController[CONTROLS], obj.SFController[CONTROLS]) && Arrays.deepEquals(SFController[SOLUTIONS], obj.SFController[SOLUTIONS])) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        if (true) {
            System.out.println("t=0.01".replaceAll("([0-9]{1,})([^ 0-9-+*=/×°\\.]{1})", "$1 $2"));
            System.out.println("t=0,01".replaceAll("([0-9]{1,})([^ 0-9-+*=/×°\\.,]{1})", "$1 $2"));


            String test = "\u00b1";
            System.out.println("this is a test".replaceAll("test", test));
            System.out.println("10 +/- 1".replace("+/-", "\u00b1"));
            System.out.println("10 +/- 1".replaceAll("\\+/-", "\u00b1"));
            System.out.println(SFTextController.extractCommandParameters("replaceAll(\"+/-\",\"\\u00b1\")")[2]);
            return;
        }
        long start_time = System.currentTimeMillis();
        /*
         * roundtrip test
         */
        if (true) {
            System.out.println("+infinity -infinity".replaceAll("(?i)infinity", CommonClassesLight.INFINITY + ""));
            System.out.println("10 >= toto".replaceAll(">=", CommonClassesLight.GREATER_OR_EQUAL + ""));
            System.out.println("10 <= toto".replaceAll("<=", CommonClassesLight.LESS_OR_EQUAL + ""));
            System.out.println("test 10 +/- 1".replaceAll("\\+/-", CommonClassesLight.PLUS_MINUS + ""));
            System.out.println("Text 120m·s–1 30 m·s–1 300/60 texte 30µm·s–1 txt qsd-test --> error space".replaceAll("([^\\s-]{1,})([^0-9]{1,2})", "$1 $2"));
            System.out.println("Text 50-µm 120µm 120 µm 120m·s–1 30 m·s–1 t=0.01 300/60 texte 30µm·s–1 txt qsd-test --> error space".replaceAll("([0-9]{1,})([^ 0-9-+*=/]{1})", "$1 $2"));
            System.exit(0);
        }
        //--> avoid mutating
        System.out.println(SFTextController.STARTS_WITH_PERCENTAGE_SIGN.nickname);
        System.out.println(SFTextController.STARTS_WITH_PERCENTAGE_SIGN.toString());

        //--> to avoid mutating the parent
        SFTextController test = new SFTextController(SFTextController.STARTS_WITH_PERCENTAGE_SIGN);

        System.out.println(test.equals(SFTextController.STARTS_WITH_PERCENTAGE_SIGN));

        System.out.println(getRuleFromString(test.toString()).toString());//try reload an official rule

        /*
         * make it a custom rule
         */
        test.mutate();
        System.out.println("------------------------------");
        System.out.println(test.toString());
        System.out.println("------------------------------");
        System.out.println(getRuleFromString(test.toString()).toString().equals(SFTextController.STARTS_WITH_PERCENTAGE_SIGN.toString()));
        System.out.println("------------------------------");
        System.out.println(test.equals(SFTextController.STARTS_WITH_PERCENTAGE_SIGN));
        System.out.println(test.equalsContent(SFTextController.STARTS_WITH_PERCENTAGE_SIGN));
        System.out.println(SFTextController.STARTS_WITH_PERCENTAGE_SIGN.nickname + " " + test.nickname);//--> ca marche mais il faut le cloner
        if (true) {
            System.exit(0);
        }

//--> parfait ca marche tres bien
        //tt marche --> reste plus qu'a cloner les docs etc...

        //String[] commandAndParameters = SFTextController.extractCommandParameters(" replaceFirst ( \"t$2o\" t\\$it'i$1to\" ,  \"ti\"ti\" )");
//        StyledDocument doc = new StyledDoc2Html().reparse("this <cap>is</cap> toto a test <i>toto</i> titi");
//        Object ret = executeCommand(doc, " replaceAll ( \"toto\" ,  \"titi\" )");
//        if (ret instanceof StyledDocument) {
//            ColoredTextPane ctps = new ColoredTextPane(new ColoredTextPaneSerializable((StyledDocument) ret, "test"));
//            int result = JOptionPane.showOptionDialog(null, ctps, "text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.NO_OPTION, null, null, null);
//            if (result == JOptionPane.OK_OPTION) {
//                System.out.println("ok");
//            }
//        } else if (ret instanceof Boolean) {
//            System.out.println(ret);
//        }

        //System.out.println("otototo".replace("toto", "\u0061 titi"));//--> ca marche --> pas mettre de '' dans le truc
        //TODO tester chacune des regles pr voir si ca marche puis passer au processing a grande echelle, en fait ne plus appliquer le style --> juste le selectionner et verif si a jour ou pas --> trois bouttons --> 1 pr la font, un pr les line arts et un 
//           test.apply(liste); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


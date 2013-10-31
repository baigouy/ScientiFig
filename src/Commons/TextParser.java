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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Functions to parse txt or .csv files (if they are tab separated and not coma
 * separated)
 *
 * @since <B>Packing Analyzer 1.0</B>
 */
public class TextParser {

    /**
     * Parses a txt file into an array of array of doubles
     *
     * @param input_file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public ArrayList<ArrayList<Double>> apply(String input_file) {
        ArrayList<String> text = getText(input_file, true);
        return toDouble(text);
    }

    private ArrayList<ArrayList<Double>> toDouble(ArrayList<String> text) {
        ArrayList<ArrayList<Double>> test2 = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < text.size(); i++) {
            StringTokenizer st = new StringTokenizer(text.get(i));
            if (st.countTokens() > 0) {
                if (st.countTokens() > test2.size()) {
                    for (int j = test2.size(); j < st.countTokens(); j++) {
                        test2.add(new ArrayList<Double>());
                    }
                }
                try {
                    int pos = 0;
                    while (st.hasMoreTokens()) {
                        double curval = Double.parseDouble(st.nextToken());
                        test2.get(pos).add(curval);
                        pos++;
                    }
                } catch (NumberFormatException ioe) {
                }
            }
        }
        return test2;
    }

    /**
     * remove NAN and infinite lines
     *
     * @param txt
     * @return
     */
    private ArrayList<String> ignoreNansAndInf(ArrayList<String> txt) {
        ArrayList<String> corrected = new ArrayList<String>();
        for (String string : txt) {
            if ((string.toLowerCase().contains("nan") || string.toLowerCase().contains("inf"))) {
                corrected.add(string);
            }
        }
        txt.removeAll(corrected);
        return txt;
    }

    /**
     * Parses a txt file into an array of array of doubles, but ignores lines
     * that contain a NAN value
     *
     * @param input_file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public ArrayList<ArrayList<Double>> apply_ignore_NAN(String input_file) {
        ArrayList<String> text = getText(input_file, true);
        text = ignoreNansAndInf(text);
        return toDouble(text);
    }

    /**
     * The main class reading lines of files
     *
     * @param inputFile the file to read
     * @param ignoreCommentedLines if true skips line starting by #
     * @return an ArrayList<String> containing all the lines of the read file
     */
    private ArrayList<String> getText(String inputFile, boolean ignoreCommentedLines) {
        ArrayList<String> text = new ArrayList<String>();
        try {
            String current_record;
            FileReader fr = new FileReader(inputFile);
            BufferedReader br = new BufferedReader(fr);
            while ((current_record = br.readLine()) != null) {
                /*
                 * we ignore lines that start with a # (these are typically my comments lines and they should not be taken into account)
                 */
                if (ignoreCommentedLines) {
                    if (!current_record.startsWith("#")) {
                        text.add(current_record);
                    }
                } else {
                    text.add(current_record);
                }
            }
            br.close();
            fr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return text;
    }

    /**
     * Returns the first line of a file
     *
     * @param input_file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public String getFirstLineOnly(String input_file) {
        String line1 = "";
        try {
            String current_record;
            FileReader fr = new FileReader(input_file);
            BufferedReader br = new BufferedReader(fr);
            current_record = br.readLine();
            line1 = current_record;
            br.close();
            fr.close();
        } catch (IOException ioe) {
        }
        return line1;
    }

    /**
     * Parses a txt file into an array of strings, ignores lines that start with
     * a #
     *
     * @param input_file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public ArrayList<String> OnlyGetText(String input_file) {
        return getText(input_file, true);
    }

    /**
     * Get all text do not skip commentary lines starting with a '#' character
     *
     * @param input_file
     * @return
     */
    public ArrayList<String> OnlyGetTextNoEscape(String input_file) {
        return getText(input_file, false);
    }

    /**
     * Parses the first line of a file and return it as an array of strings
     *
     * @param input_file
     * @since <B>Packing Analyzer 1.0</B>
     */
    public ArrayList<String> parseHeader(String entete) {
        ArrayList<String> parsed = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(entete);
        if (st.countTokens() > 0) {
            while (st.hasMoreTokens()) {
                try {
                    String curval = st.nextToken();
                    parsed.add(curval);
                } catch (Exception ioe) {
                    System.out.println(ioe.toString());
                }
            }
        }
        return parsed;
    }

    /**
     * Parses a line as an array of double
     *
     * @param line input line
     * @since <B>Packing Analyzer 1.0</B>
     */
    public ArrayList<Double> parse_line_as_Double(String line) {
        ArrayList<Double> parsed = new ArrayList<Double>();
        StringTokenizer st = new StringTokenizer(line);
        if (st.countTokens() > 0) {
            while (st.hasMoreTokens()) {
                try {
                    double curval = Double.parseDouble(st.nextToken());
                    parsed.add(curval);
                } catch (Exception ioe) {
                }
            }
        }
        return parsed;
    }

    /**
     * Converts an ArrayList<ArrayList<Double>> to an
     * ArrayList<ArrayList<Integer>>
     *
     * @param double_table input table
     * @since <B>Packing Analyzer 2.0</B>
     */
    public ArrayList<ArrayList<Integer>> Double2IntTable(ArrayList<ArrayList<Double>> double_table) {
        ArrayList<ArrayList<Integer>> int_table = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < double_table.size(); i++) {
            ArrayList<Integer> int_val = new ArrayList<Integer>();
            ArrayList<Double> cur_double = double_table.get(i);
            for (int j = 0; j < cur_double.size(); j++) {
                int_val.add(cur_double.get(j).intValue());
            }
            int_table.add(int_val);
        }
        return int_table;
    }
    
     /**
     * Parses a txt file into a single String
     *
     * @param input_file
     * @since <B>Packing Analyzer 8.0</B>
     */
    public static String getTextAsString(String input_file) {
        String text = "";
        try {
            String current_record;
            FileReader fr = new FileReader(input_file);
            BufferedReader br = new BufferedReader(fr);
            while ((current_record = br.readLine()) != null) {
                text += current_record + "\n";
            }
            br.close();
            fr.close();
        } catch (IOException ioe) {
            //System.out.println("erreur : " + ioe);
        }
        return text;
    }

    //ca marche --> utiliser la premiere colonne pr faire determiner ou placer les point
    public static void main(String args[]) {
        /* paste the following to a txt file
         10	20	30
         10	10	10
         #blah blah
         10	20	10
         NaN	10 30
         */

        //long start_time = System.currentTimeMillis();
//        TextParser test = new TextParser();
//        ArrayList<ArrayList> toto = test.apply("C:/test.txt");
//        //  System.out.println(toto.size());
//        for (int i = 0; i < toto.get(0).size(); i++) {
//            System.out.println(toto.get(0).get(i));
//        }

        //calculate av angle + stdev

        //C:/plosts_trashme/woundedwing/
//        new TextParser().minOfAllCols("C:/plosts_trashme/WT/graph1.txt");
//        System.out.println("------------------------------");
//        new TextParser().maxOfAllCols("C:/plosts_trashme/WT/graph1.txt");
//        System.out.println("------------------------------------------------------------");
//        new TextParser().minOfAllCols("C:/plosts_trashme/WT/graph2.txt");
//        System.out.println("------------------------------");
//        new TextParser().maxOfAllCols("C:/plosts_trashme/WT/graph2.txt");

        System.out.println(new TextParser().apply("/home/benoit/Bureau/test_CSV_parser.txt"));
        System.out.println(new TextParser().apply_ignore_NAN("/home/benoit/Bureau/test_CSV_parser.txt"));
        System.out.println(new TextParser().OnlyGetText("/home/benoit/Bureau/test_CSV_parser.txt"));
        System.out.println(new TextParser().OnlyGetTextNoEscape("/home/benoit/Bureau/test_CSV_parser.txt"));

        //System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


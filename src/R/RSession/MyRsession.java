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
package R.RSession;

import Commons.CommonClassesLight;
import Commons.Loader;
import MyShapes.GraphFont;
import R.RTools;
import R.ThemeGraph;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RserveException;
import org.w3c.dom.svg.SVGDocument;

/**
 * MyRsession connects ScientiFig and FiguR to R
 *
 * @since <B>FiguR 0.1</B>
 * @author Benoit Aigouy
 */
public class MyRsession {

    /**
     * Variables
     */
    Rsession s;
    boolean verbose = true;
    /*
     * Various R libraries we need to load
     */
    static final String GRIDEXTRA_MULTIPLOT = "gridExtra";
    static final String GGPLOT2_QPLOT_NICE_PLOT = "ggplot2";
    static final String Rserver = "Rserve";
    static final String RSQLite = "RSQLite";
//    public static final String DDPLY = "plyr"; //if I put this it corrupts ggplot2 because of some version errors so I removed it and implemented dependencies load when installing a package a package
    static final String SQLDF = "sqldf";
    static final String EXCEL = "xlsx";
    static final String RJAVA = "rJava";
    static final String XLSXJARS = "xlsxjars";
    static final String GRID = "grid";
    static final String EXTRAFONTS = "extrafont";
    public PrintStream in;
//    public static final String ERROR = "qsdsqdqsdq";
    /*
     * geom_smooth libraries
     */
    static final String MGCV = "mgcv";//interpolation gam
    static final String MASS = "MASS";//interpolation rlm
    /*
     * a set of required packages
     */
    static final ArrayList<String> required_packages = new ArrayList<String>();
    File temporary_folder;
    String path2tempFolder = "";
    File preview;
    String preview_file = "previewFigR.png";

    static {
        required_packages.add(GGPLOT2_QPLOT_NICE_PLOT);
//        required_packages.add(DDPLY);
        required_packages.add(RJAVA);
        required_packages.add(XLSXJARS);
        required_packages.add(EXCEL);
        required_packages.add(GRID);
        required_packages.add(MGCV);
        required_packages.add(MASS);
        required_packages.add(EXTRAFONTS);
        required_packages.add(Rserver);
//        required_packages.add(ERROR);
    }

    /**
     * Empty constructor (opens a connection to R)
     */
    public MyRsession() {
    }

    /**
     *
     * @return an R command that should install several R plugins when executed
     */
    public static String installAllPackages() {
        String out = "";
        for (int l = 0; l < required_packages.size(); l++) {
            String curPackage = required_packages.get(l);
            out += installPackageText(curPackage);
        }
        return out;
    }

    /**
     *
     * @param plugin
     * @return an R command to display an R plugin citation
     */
    public String getCitation(String plugin) {
        if (!plugin.equals("")) {
            plugin = "\"" + plugin + "\"";
        }
        try {
            return s.asString("citation(" + plugin + ")");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     *
     * @return a string that contains the citation for all the R plugins uded in
     * FiguR
     */
    public String getCitations() {
        if (!isRserverRunning()) {
            return "A connection to R (green icon close to the 'Rsession status' button) is required to get the citations";
        }
        String out = getCitation("") + "\n";
        for (int l = 0; l < required_packages.size(); l++) {
            String curPackage = required_packages.get(l);
            String cur_citation = getCitation(curPackage);
            if (cur_citation != null) {
                out += cur_citation + "\n";
            }
        }
        String FiguR = "To cite FiguR in publications, please use:"
                + "\n\nB. Aigouy. and V. Mirouse. FiguR: a GUI for R focusing on figure creation\nhttp://srv-gred.u-clermont1.fr/labmirouse/software/";
        out += FiguR;
        return out;
    }

    public ArrayList<String> getAvailableFontsInR() {
        ArrayList<String> fonts = new ArrayList<String>();
        REXP r = eval("fonts()");
        try {
            fonts.addAll(Arrays.asList(r.asStrings()));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            //likely to be no fonts availbale in R
            return fonts;
        }
        return fonts;
    }

    /**
     *
     * @return an R command that loads all packages (NB: those packages must
     * have been installed before)
     */
    public static String loadAllPackages() {
        String out = "";
        for (int l = 0; l < required_packages.size(); l++) {
            String curPackage = required_packages.get(l);
            out += loadPackageText(curPackage);
        }
        return out;
    }

    /**
     *
     * @param package_name
     * @return and R command to install an R package
     */
    private static String installPackageText(String package_name) {
        return "install.packages(\"" + package_name + "\")\n";
    }

    /**
     *
     * @param package_name
     * @return and R command to remove an R package
     */
    private static String removePackageText(String package_name) {
        return "remove.packages(\"" + package_name + "\")\n";
    }

    /**
     *
     * @param package_name
     * @return an R command to load an R package
     */
    private static String loadPackageText(String package_name) {
        if (!package_name.equals(Rserver)) {
            return "library(" + package_name + ")\n";
        } else {
            return "library(" + package_name + ")\nRserve(args='--vanilla')\n";
        }
    }

    /**
     *
     * @return true if the Rserver is running
     */
    public boolean isRserverRunning() {
        if (s != null) {
            return s.connected;
        }
        return false;
    }

    /**
     * This command should execute an R script file (never really tested it yet)
     *
     * @param file
     */
    public void executeRScript(String file) {
        try {
            eval("source(\"" + file + "\")");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     *
     * @return the path to the R home directory
     */
    public String getRHome() {
        String home;
        try {
            REXP r = eval("R.home()");
            home = CommonClassesLight.change_path_separators_to_system_ones(r.asString());
        } catch (Exception e) {
            return null;
        }
        return home;
    }

    /**
     *
     * @return guidelines to install or execute R
     */
    public static String generateRInstallationText() {
        int arch = CommonClassesLight.getJVMArchitecture();
        return "Connection to R failed! "
                + "\nIMPORTANT: SF/FiguR seem to be running in a " + arch + " bits JVM so"
                + "\nplease make sure you install and run the " + arch + " bits version of R"
                + "\n\n1/ If R is not installed on your system please install it:\n"
                + "\nR for windows can be downloaded from:"
                + "\nhttp://cran.r-project.org/bin/windows/base/\n"
                + "\nR for mac can be downloaded from:"
                + "\nhttp://cran.r-project.org/bin/macosx/"
                + "\n(on mac you also need to install the tcltk .dmg file --> in tools)"
                + "\nhttp://cran.r-project.org/bin/macosx/tools/"
                + "\non OSX Mountain Lion you also have to install XQuartz and then to restart your system"
                + "\nhttp://xquartz.macosforge.org/landing/\n"
                + "\nR for ubuntu based distributions can be installed by using the following commands:"
                + "\nsudo apt-get install r-base"
                + "\nsudo apt-get install r-cran-rjava"
                + "\nsudo apt-get install r-cran-cairodevice"
                + "\n\n\n2/The first time you start R, please run the following commands to increase the number of fonts available in R (this might take several minutes, and you may need to inactivate your antivirus)"
                + "\n\ninstall.packages('extrafont')"
                + "\nlibrary(extrafont)"
                + "\nfont_import()"
                + "\n\nNB: please close and reopen R for the installation to complete (fonts will be available only after a restart"
                + "\nNB2: to see if the fonts where installed properly you could type 'fonts()' in the R terminal"
                + "\n\n\n3/ If the Rsession status icon is not green and if the R " + arch + " bits version (and not the " + (arch == 64 ? 32 : 64) + " bits version)"
                + "\nis installed and running on your system please copy and paste the following lines in (R):"
                + "\n\nif ('Rserve' %in% rownames(installed.packages())==FALSE) install.packages('Rserve')"
                + "\nlibrary(Rserve)"
                + "\nRserve(args='--vanilla')\n"
                + "\nNB1: If you are asked to choose a mirror when executing the first line please do so"
                + "\nNB2: Please hit Enter after you paste the commands to make sure that the last command gets also executed"
                + "\nNB3: If this is the first time you launch R with SF, please be patient because downloading and loading all the plugins can take time"
                + "\nNB4: Upon success you should see R output something like 'Starting Rserve...'"
                + "\n\n4/ Finally click on the \"Rsession status\" Button to verify the connection."
                + "\nIt should become green, otherwise sorry an error must have occured ask an"
                + "\nR expert around you (you can also try to repeat steps 3 and 4 just in case)"
                + "\nthe software will continue to run but the graph functionalities"
                + "\n(i.e. auto scaling of graphs and font settings for R graphs) will be disabled"
                + "\n\nNB: additional steps under linux if rJava installation failed\nsudo R CMD javareconf\ninstall.packages(\"rJava\")";
        //NB: to install RSQLite under linux --> sudo apt-get install r-cran-rsqlite 
    }

    /**
     * loads current journal styles in a combobox
     *
     * @param jComboBox1
     */
    public void reloadRFonts(JComboBox jComboBox1) {
        Vector v = new Vector();
        ArrayList<String> fonts = getAvailableFontsInR();
        if (fonts != null) {
        }
        for (String family : fonts) {
            if (family != null) {
                v.add(family);
            }
        }
        jComboBox1.setModel(new DefaultComboBoxModel(v));
    }

    public void importRfonts() {
        try {
            eval("font_import()");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * Tries to open/reopen the connection to R
     *
     * @throws REXPMismatchException
     */
    public void reopenConnection() throws REXPMismatchException {
        try {
            if (s != null) {
                s.end();
                s = null;
            }
        } catch (Exception e) {
        }
        /*
         * Forcing the port fixes a bug in fiji. 
         * Otherwise it uses the wrong port under windows. 
         * My naive guess is that they set some port property in FIJI which occupies the Rsession port on windows systems.
         */
        RserverConf r = new RserverConf("localhost", 6311, null, null, null);
        try {
            if (in == null) {
                s = Rsession.newInstanceTry(System.out, r);//new RConnection();//Rsession.newInstanceTry(System.out, r);
            } else {
                s = Rsession.newInstanceTry(in, r);//new RConnection();//Rsession.newInstanceTry(System.out, r);
            }
            if (s != null) {
                String lastSuccessfulPathToR = CommonClassesLight.change_path_separators_to_system_ones(s.eval("R.home()").asString());
                System.setProperty("R_HOME", lastSuccessfulPathToR);
//                ScientiFig_.lastSuccessfulPathToR = lastSuccessfulPathToR;
                installAndLoadPlugins();
            }
        } catch (Exception e) {
            /*
             * ignore
             */
//            try {
//                /*
//                 * gros bug sous windows, je pense qu'il loade la version 32 bits de R si on met ca --> cause de gros pbs avec rJava --> 
//                 */
//                System.setProperty("R_HOME", ScientiFig_.lastSuccessfulPathToR);
//                s = Rsession.newInstanceTry(System.out, r);//new RConnection();//Rsession.newInstanceTry(System.out, r);
//                if (s != null) {
//                    installAndLoadPlugins();
//                }
//            } catch (Exception e2) {
//            }
            //e.printStackTrace();
        }
        String tmp = CommonClassesLight.getTempDirectory();
        if (temporary_folder == null) {
            try {
                temporary_folder = java.io.File.createTempFile("FigR_tmp", ".png", new File(tmp));
                temporary_folder.delete();
                temporary_folder.deleteOnExit();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
            if (temporary_folder != null) {
                path2tempFolder = CommonClassesLight.change_path_separators_to_system_ones(CommonClassesLight.getName(temporary_folder.toString()));
            }
        }
        if (preview == null) {
            try {
                preview = java.io.File.createTempFile("previewFigR", ".png", new File(tmp));
                preview.delete();
                preview.deleteOnExit();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
            if (preview != null) {
                preview_file = CommonClassesLight.change_path_separators_to_system_ones(preview.toString());
            }
        }
    }

    /**
     * This function can be used to rename columns in an R dataframe
     *
     * @param table R dataframe name
     * @param names new column names
     */
    public void setColumnNames(String table, String... names) {
        String formattedNames = RTools.createRArray("c", names);
        try {
            eval("names(" + table + ") <- " + formattedNames);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     *
     * @param dataFrame
     * @return an arraylist that contains all R column names
     */
    public ArrayList<String> getColumnNames(String dataFrame) {
        ArrayList<String> column_names = new ArrayList<String>();
        try {
            RList list = eval("{column_names<-colnames(" + dataFrame + ");lapply(column_names,as.character)}").asList();//c.eval("{library(xlsx);data <- read.xlsx(\"C:/Users/ben/Desktop/test2.xlsx\", 1);column_names<-colnames( data );lapply(column_names,as.character)}").asList();
            int cols = list.size();
            for (int i = 0; i < cols; i++) {
                String[] colNames = list.at(i).asStrings();
                column_names.addAll(Arrays.asList(colNames));
            }
            return column_names;
        } catch (Exception e) {
        }
        return column_names;
    }

    /**
     *
     * @param col_names
     * @return a list of columns that can be used as factors
     */
    public ArrayList<String> getAlreadyFactorizableColumns(ArrayList<String> col_names) {
        ArrayList<String> alreadyFactorizableColumns = new ArrayList<String>();
        if (col_names == null) {
            return alreadyFactorizableColumns;
        }
        for (String string : col_names) {
            REXP exp = eval("is.factor(curDataFigR$" + string + ")");
            try {
                if (exp.asString().toLowerCase().contains("true")) {
                    alreadyFactorizableColumns.add(string);
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
        return alreadyFactorizableColumns;
    }

    /**
     *
     * @param column_names
     * @return the number of R factors per column
     */
    public ArrayList<Integer> getNbOfFactors(ArrayList<String> column_names) {
        ArrayList<Integer> factor_count = new ArrayList<Integer>();
        for (String string : column_names) {
            factor_count.add(countFactors(string));
        }
        return factor_count;
    }

    /**
     *
     * @param colName
     * @return the number of R factors contained in the column named colName
     */
    public int countFactors(String colName) {
        REXP r = eval("length(levels(as.factor(curDataFigR$" + colName + ")))");
        int nb = -1;
        try {
            nb = r.asInteger();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return nb;
    }

    /**
     *
     * @param col_names list of columns to check
     * @param cutOff (max nb of factors per column to consider it likely to be
     * used as a factor)
     * @return a complete list of columns that are likely to be used as factors
     * (i.e. that do contain factors but not too many factors which would not
     * make sense)
     */
    public Object[] getAllLikelyFactors(ArrayList<String> col_names, int cutOff) {
        Object[] values = new Object[2];
        ArrayList<Integer> factorCounts = getNbOfFactors(col_names);
        HashMap<String, Integer> colname_and_nb_of_cols = new HashMap<String, Integer>();
        HashMap<String, ArrayList<String>> colname_and_factors = new HashMap<String, ArrayList<String>>();
        int count = 0;
        for (String string : col_names) {
            colname_and_nb_of_cols.put(string, factorCounts.get(count));
            count++;
        }
        count = 0;
        for (String string : col_names) {
            if (colname_and_nb_of_cols.containsKey(string)) {
                int value = colname_and_nb_of_cols.get(string);
                if (value <= cutOff) {
                    colname_and_factors.put(string, getFactors(string));
                }
            }
            count++;
        }
        values[0] = colname_and_nb_of_cols;
        values[1] = colname_and_factors;
        return values;
    }

    /**
     *
     * @param column_names list of columns we want to extract the factors from
     * @return all the factors for all the columns (the format is
     * map<column_name, factors>)
     */
    public HashMap<String, ArrayList<String>> getFactors(ArrayList<String> column_names) {
        HashMap<String, ArrayList<String>> factors = new HashMap<String, ArrayList<String>>();
        for (String string : column_names) {
            factors.put(string, getFactors(string));
        }
        return factors;
    }

    /**
     *
     * @param colName
     * @return a string arraylist that contains all the factors for the column
     * 'colName'
     */
    public ArrayList<String> getFactors(String colName) {
        ArrayList<String> factors = new ArrayList<String>();
        REXP r = eval("levels(as.factor(curDataFigR$" + colName + "))");
        try {
            factors.addAll(Arrays.asList(r.asStrings()));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return factors;
    }

    /**
     * Loads a package
     *
     * @param packageName
     */
    public void loadPackage(String packageName) {
        try {
            String cmd = "library(" + packageName + ")";
            REXP r = s.eval(cmd);
            if (r == null) {
                if (packageName.equals(RJAVA)) {
                    s.eval(setR_JAVA_HOME(false));
                    r = s.eval(cmd);
                    if (r == null) {
                        s.eval(setR_JAVA_HOME(true));
                        r = s.eval(cmd);
                    }
                    if (r == null) {
                        CommonClassesLight.Warning(null, "Loading of package " + packageName + " failed");
                    }
                } else {
                    CommonClassesLight.Warning(null, "Loading of package " + packageName + " failed");
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    public void installPackage(String packageName) {
        /**
         * really keep like this otherwhise it does not work
         */
        s.eval("install.packages('" + packageName + "',repos='http://cran.r-project.org',dependencies=TRUE)");
    }

    /**
     * Installs then loads a package
     *
     * @param packageName
     */
    public void installAndLoadPackage(String packageName) {
        if (s != null && s.connected) {
            try {
                boolean wasInstalled = false;
                if (!isInstalled(packageName)) {
                    installPackage(packageName);
                    wasInstalled = true;
                }
                if (wasInstalled && isInstalled(packageName)) {
                    loadPackage(packageName);
                } else {
                    loadPackage(packageName);
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
    }

    /**
     * Loads an excel file to a dataFrame using the read.xlsx command
     *
     * @param variableName name of the dataFrame
     * @param xls name of the source excel file
     * @param sheet_nb nb of the sheet of the excel file to open
     */
    public void loadXLS(String variableName, String xls, int sheet_nb) {
        sheet_nb = sheet_nb <= 0 ? 1 : sheet_nb;
        try {
            eval(variableName + " <- read.xlsx(\"" + xls + "\", " + sheet_nb + ")");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * Loads a text file in an R dataFrame
     *
     * @param variableName name of the dataFrame
     * @param csv name of the source csv or txt file
     * @param sep the text separator character
     * @param delim the text delimitor character
     * @param decimal the decimal separator character (usually . or ,)
     */
    public void loadCSVorTXT(String variableName, String csv, String sep, String delim, String decimal) {
        try {
            String decimalTxt = "dec=\"" + decimal + "\"";
            String delimTxt = "quote=\"" + delim + "\"";
            if (delim == null || delim.toLowerCase().equals("none")) {
                delimTxt = null;
            }
            String out = variableName + " <- read.table(\"" + csv + "\", header=TRUE, sep= \"" + sep + "\"";
            if (decimalTxt != null) {
                out += ", " + decimalTxt;
            }
            if (delimTxt != null) {
                out += ", " + delimTxt;
            }
            out += ")";
            eval(out);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * loads a csv txt or excel file depending on the extension of the file
     *
     * @param variableName name of the dataFrame
     * @param anyFile name of the source file
     * @param sheetnb nb of the sheet of the excel file to open
     * @param textSeparator the text separator character
     * @param textDelimtor the text delimitor character
     * @param decimal the decimal separator character (usually . or ,)
     */
    public void loadCSVOrXls(String variableName, String anyFile, int sheetnb, String textSeparator, String textDelimtor, String decimal) {
        if (anyFile.toLowerCase().endsWith(".txt") || anyFile.toLowerCase().endsWith(".csv")) {
            loadCSVorTXT(variableName, anyFile, textSeparator, textDelimtor, decimal);
        }
        if (anyFile.toLowerCase().endsWith(".xls") || anyFile.toLowerCase().endsWith(".xlsx")) {
            loadXLS(variableName, anyFile, 1);
        }
    }

    /**
     * Sets the verbose mode to the boolean 'mode'
     *
     * @param verbose
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Evaluates an R command
     *
     * @param command
     * @return a REXP that contains information about the execution in R
     */
    public REXP eval(String command) {
        try {
            return s.eval(command);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            System.out.println("Execution of the following command has FAILED:\n" + "\"" + command + "\"");
        }
        return null;
    }

    /**
     * Evaluate an R command and convert its result to a double
     *
     * @param command
     * @return a double
     * @throws REXPMismatchException
     * @throws RserveException
     */
    public double evalAsDouble(String command) throws REXPMismatchException, RserveException {
        double value = eval(command).asDouble();
        return value;
    }

    /**
     * Evaluate an R command and convert its result to a double array
     *
     * @param command
     * @return an array of doubles
     * @throws REXPMismatchException
     * @throws RserveException
     */
    public double[] evalAsDoubleArray(String command) throws REXPMismatchException, RserveException {
        double[] value = eval(command).asDoubles();
        return value;
    }

    /**
     * Splits a multi-line command into several single line commands
     *
     * @param multiLineCommand
     * @return several executable lines
     */
    public String executeMultiline(String multiLineCommand) {
        String[] lines = multiLineCommand.trim().split(";");
        int max = lines.length - 1;
        int counter = 0;
        if (max > 0) {
            for (String string : lines) {
                if (string.trim().startsWith("#")) {
                    continue;
                }
                eval(string);
                counter++;
                if (counter == max) {
                    break;
                }
            }
        }
        return lines[lines.length - 1];
    }

    /**
     *
     * @param plot the R plot command to execute
     * @param width width of the preview
     * @param height height of the preview
     * @return a preview of the plot generated by R
     */
    public BufferedImage getPreview(String plot, int width, int height) {
        double width_in_inches = (float) width / 72f;
        double height_in_inches = (float) height / 72f;
        if (plot.contains(";")) {
            plot = executeMultiline(plot);
        }
        try {
            String cmd = "ggsave(" + plot + ", filename=\"" + preview_file + "\", width=" + width_in_inches + ", height=" + height_in_inches + ", dpi=72)";
            REXP r = s.eval(cmd);
            if (r == null) {
                CommonClassesLight.Warning(null, "Plot failed :\n" + cmd);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return new Loader().load(preview_file);
    }

    /**
     *
     * @param plot the R plot command to execute
     * @param font the set of journal styles that should be applied to the R
     * graph
     * @param width width of the preview
     * @param height height of the preview
     * @return an svgDocument that contains a vectorial representation of the R
     * graph
     */
    public SVGDocument getPreviewSVG(String plot, GraphFont font, ThemeGraph theme, double width, double height) {
        double width_in_inches = width / 72d;
        double height_in_inches = height / 72d;
        if (plot.contains(";")) {
            plot = executeMultiline(plot);
        }
        String command_to_plot = plot;
        if (theme != null) {
            command_to_plot += " + " + theme.toString();
        }
        if (font != null) {
            command_to_plot += " + " + font.toString();
        }
        try {
            String cmd = "ggsave(" + command_to_plot + ", filename=\"" + CommonClassesLight.getName(preview_file) + ".svg" + "\", width=" + width_in_inches + ", height=" + height_in_inches + ", dpi=72)";
            REXP r = s.eval(cmd);
            if (r == null) {
                CommonClassesLight.Warning(null, "Plot failed :\n" + cmd);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return new Loader().loadSVGDocument(CommonClassesLight.getName(preview_file) + ".svg");
    }

    /**
     *
     * @return a String that corresponding to the version of R
     * @throws REXPMismatchException
     * @throws RserveException
     */
    public String getRVersion() throws REXPMismatchException, RserveException {
        String txt = eval("R.version$version.string").asString(); //format in text
        return txt;
    }

    /**
     * Evaluates an R command (using try)
     *
     * @param command
     */
    public void evalTry(String command) {
        try {
            eval("try{" + command + "}");
        } catch (Exception e) {
        }
        throw new Error("TODO");
    }

    public boolean isInstalled(String packageName) throws REXPMismatchException {
        try {
            return eval("'" + packageName + "' %in% rownames(installed.packages())").asString().toLowerCase().contains("true");
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Install and load all plugins
     *
     * @throws REXPMismatchException
     */
    public void installAndLoadPlugins() throws REXPMismatchException {
        if (s != null && s.connected) {
            for (String string : required_packages) {
                /*
                 * continue loading plugins even if one fails
                 */
                try {
                    installAndLoadPackage(string);
                } catch (Exception e) {
                }
            }
        }
    }

    public String getR_JAVA_HOME() {
        try {
            return eval("Sys.getenv('JAVA_HOME')").asString();
        } catch (Exception e) {
        }
        return null;
    }

    private String setR_JAVA_HOME(boolean forceSameJAVA_HOME_AsSF) {
        String R_JAVA_HOME = getR_JAVA_HOME();
        if (R_JAVA_HOME == null || R_JAVA_HOME.equals("") || forceSameJAVA_HOME_AsSF) {
            return "Sys.setenv(JAVA_HOME='" + CommonClassesLight.getJAVA_HOME() + "/')";
        } else {
            return R_JAVA_HOME == null ? "" : "Sys.setenv(JAVA_HOME='" + R_JAVA_HOME + "/')";
        }
    }

    /**
     * closes the connection to R (please call this on close)
     */
    public void close() {
        try {
            s.end();
            try {
                Thread.sleep(400);
            } catch (Exception e) {
            }
//            s.connection.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     *
     * @return an R command that should install several R plugins when executed
     */
    public static String removeAllPackages() {
        String out = "";
        for (String curPackage : required_packages) {
            out += removePackageText(curPackage);
        }
        return out;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @throws REXPMismatchException
     * @throws FileNotFoundException
     * @throws IOException
     * @author Benoit Aigouy
     */
    public static void main(String args[]) throws REXPMismatchException, FileNotFoundException, IOException {
        /*
         remove.packages("ggplot2")
         remove.packages("rJava")
         remove.packages("xlsxjars")
         remove.packages("xlsx")
         remove.packages("grid")
         remove.packages("plyr")
         remove.packages("mgcv")
         remove.packages("MASS")
         remove.packages("Rserve")
         remove.packages("qsdsqdqsdq")
         */

        MyRsessionLogger rlt = new MyRsessionLogger();

        if (true) {

            System.out.println(MyRsession.installAllPackages());

            //System.out.println(rlt.removeAllPackages());
            rlt.reopenConnection();

            rlt.getAvailableFontsInR();

            rlt.close();
            return;
        }

        /*
         colnames(curDataFigR);
         ggplot(data=curDataFigR)
         + geom_line(aes(x=x, y=y, shape=type, color=type)) 
         + geom_point(aes(x=x, y=y, shape=type, color=type)) 
         +ggtitle("toto")+facet_grid(type~.);
         */
        String stuff2parse = "curDataFigR <- read.xlsx(\"C:/Users/ben/Desktop/test.xlsx\", 1);\n"
                + "colnames(curDataFigR);\n"
                + "ggplot(data=curDataFigR)\n"
                + "+ geom_line(aes(x=x, y=y, shape=type, color=type)) \n"
                + "+ geom_point(aes(x=x, y=y, shape=type, color=type)) \n"
                + "+ggtitle(\"toto\")+facet_grid(type~.);"
                + "colnames(curDataFigR);\n";
        System.out.println(rlt.executeMultiline(stuff2parse));
        rlt.close();
        System.exit(0);
    }
}

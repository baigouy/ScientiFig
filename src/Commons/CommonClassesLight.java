package Commons;

import R.RSession.MyRsessionLogger;
import ij.*;
import ij.io.FileInfo;
import java.awt.*; 
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.*;

/**
 * Basic class that contains many useful tools to manipulate Strings, images,
 * ...
 *
 * @since <B>Packing Analyzer 1.0</B>
 * @author Benoit Aigouy
 */
public class CommonClassesLight {

    public static final ArrayList<String> regexVocabulary = new ArrayList<String>();

    /**
     * here is the regexvocabulary that can be used to regexify a string
     */
    static {
        regexVocabulary.add("\\");
        regexVocabulary.add(".");
        regexVocabulary.add("^");
        regexVocabulary.add("$");
        regexVocabulary.add("|");
        regexVocabulary.add("?");
        regexVocabulary.add("*");
        regexVocabulary.add("+");
        regexVocabulary.add("[");
        regexVocabulary.add("]");
        regexVocabulary.add("(");
        regexVocabulary.add(")");
        regexVocabulary.add("{");
        regexVocabulary.add("}");
    }
    /**
     * Color blind friendly colors derived from
     * http://jfly.iam.u-tokyo.ac.jp/color/
     */
    public static final int[] colorBlindFriendlyPalette = new int[]{
        CommonClassesLight.getColorFromRGB(64, 64, 64),
        CommonClassesLight.getColorFromRGB(230, 159, 0),
        CommonClassesLight.getColorFromRGB(86, 180, 233),
        CommonClassesLight.getColorFromRGB(0, 158, 115),
        CommonClassesLight.getColorFromRGB(240, 228, 66),
        CommonClassesLight.getColorFromRGB(0, 114, 178),
        CommonClassesLight.getColorFromRGB(213, 94, 0),
        CommonClassesLight.getColorFromRGB(204, 121, 167)};
    /**
     * here is a list of greek characters (unicode)
     */
    public final static HashMap<String, String> greek = new HashMap<String, String>();

    static {
        greek.put("Alpha", "\u0391");
        greek.put("Beta", "\u0392");
        greek.put("Gamma", "\u0393");
        greek.put("Delta", "\u0394");
        greek.put("Epsilon", "\u0395");
        greek.put("Zeta", "\u0396");
        greek.put("Eta", "\u0397");
        greek.put("Theta", "\u0398");
        greek.put("Iota", "\u0399");
        greek.put("Kappa", "\u039A");
        greek.put("Lambda", "\u039B");
        greek.put("Mu", "\u039C");
        greek.put("Nu", "\u039D");
        greek.put("Xi", "\u039E");
        greek.put("Omicron", "\u039F");
        greek.put("Pi", "\u03A0");
        greek.put("Rho", "\u03A1");
        greek.put("Sigma", "\u03A3");
        greek.put("Tau", "\u03A4");
        greek.put("Upsilon", "\u03A5");
        greek.put("upsih", "\u03D2");
        greek.put("Phi", "\u03A6");
        greek.put("Chi", "\u03A7");
        greek.put("Psi", "\u03A8");
        greek.put("Omega", "\u03A9");
        greek.put("alpha", "\u03B1");
        greek.put("beta", "\u03B2");
        greek.put("gamma", "\u03B3");
        greek.put("delta", "\u03B4");
        greek.put("epsilon", "\u03B5");
        greek.put("zeta", "\u03B6");
        greek.put("eta", "\u03B7");
        greek.put("theta", "\u03B8");
        greek.put("thetasym", "\u03D1");
        greek.put("iota", "\u03B9");
        greek.put("kappa", "\u03BA");
        greek.put("lambda", "\u03BB");
        greek.put("mu", "\u03BC");
        greek.put("nu", "\u03BD");
        greek.put("xi", "\u03BE");
        greek.put("omicron", "\u03BF");
        greek.put("pi", "\u03C0");
        greek.put("piv", "\u03D6");
        greek.put("rho", "\u03C1");
        greek.put("sigmaf", "\u03C2");
        greek.put("sigma", "\u03C3");
        greek.put("tau", "\u03C4");
        greek.put("upsilon", "\u03C5");
        greek.put("phi", "\u03C6");
        greek.put("chi", "\u03C7");
        greek.put("psi", "\u03C8");
        greek.put("omega", "\u03C9");
        greek.put("thetasym", "\u03D1");
        greek.put("upsih", "\u03D2");
        greek.put("piv", "\u03D6");
    }
    public final static ArrayList<String> greekLitterals = new ArrayList<String>(greek.keySet());
    public final static ArrayList<String> greekCharacters = new ArrayList<String>(greek.values());
    /*
     * A detailed list of unicode characters can be found at the following places:
     * http://en.wikipedia.org/wiki/List_of_Unicode_characters
     * http://utf8-characters.com/arrows/
     * http://www.fileformat.info/info/unicode/category/Sm/list.htm
     */
    //TODO maybe convert them to strings ???? or not
    public static final char NULL_CHAR = '\u0000';
    public static final char COPYRIGHT = '\u00a9';
    public static final char EURO = '\u20ac';
    public static final char AT = '\u0040';
    public static final char MICRON = '\u00B5';
    public static final char MALE = '\u2642';
    public static final char FEMALE = '\u2640';
    public static final char SMILEY = '\u263A';
    public static final char SKULL = '\u2620';
    public static final char LOWER_RIGHT_PENCIL = '\u270E';
    public static final char ANGSTROMS = '\u00C5';
    public static final char DEGREES = '\u00B0';
    /**
     * EN_DASH is a sort of -
     */
    public static final char EN_DASH = '\u2013';
    public static final char MIDDLE_DOT = '\u00B7';
    /*
     * Maths
     */
    public static final char MULTIPLICATION = '\u00D7';
    public static final char PLUS_MINUS = '\u00B1';
    public static final char DIVISION = '\u00F7';
    public static final char INFINITY = '\u221E';
    public static final char LESS_OR_EQUAL = '\u2264';
    public static final char GREATER_OR_EQUAL = '\u2265';
    /*
     * Arrows
     */
    public static final char LEFT_ARROW = '\u2190';
    public static final char UP_ARROW = '\u2191';
    public static final char RIGHT_ARROW = '\u2192';
    public static final char DOWN_ARROW = '\u2193';
    public static final char LEFT_RIGHT_ARROW = '\u2194';
    public static final char UP_DOWN_ARROW = '\u2195';
    public static final char LEFT_ARROW_OPEN_HEADED = '\u21FD';
    public static final char RIGHT_ARROW_OPEN_HEADED = '\u21FE';
    public static final char LEFT_RIGHT_ARROW_OPEN_HEADED = '\u21FF';
    public static final char RIGHT_ARROW_TRIANGLE_HEADED = '\u279D';
    /*
     * Arrows (harpoons)
     */
    public static final char LEFT_ARROW_UP_BARB = '\u21BC';
    public static final char LEFT_ARROW_DOWN_BARB = '\u21BD';
    public static final char RIGHT_ARROW_UP_BARB = '\u21C0';
    public static final char RIGHT_ARROW_DOWN_BARB = '\u21c1';
    public static final char UP_ARROW_LEFT_BARB = '\u21BF';
    public static final char UP_ARROW_RIGHT_BARB = '\u21BE';
    public static final char DOWN_ARROW_LEFT_BARB = '\u21C3';
    public static final char DOWN_ARROW_RIGHT_BARB = '\u21C2';
    /*
     * Tack
     */
    public static final char RIGHT_INHIBITION_SYMBOL = '\u27DE';
    public static final char LEFT_INHIBITION_SYMBOL = '\u27DD';
    public static final char UP_INHIBITION_SYMBOL = '\u27D9';
    public static final char DOWN_INHIBITION_SYMBOL = '\u27D8';
    /*
     * Tape player commands
     */
    public static final char FAST_REWIND = '\uE318';
    public static final char PREV = '\uE312';
    public static final char NEXT = '\uE313';
    public static final char PLAY = NEXT;
    public static final char FAST_FORWARD = '\uE319';
    /*
     * Square root of 2
     */
    public final static double SQRT2 = Math.sqrt(2.);
    /*
     * We only open one instance of IJ
     */
    public static ImageJ ij;
    public static MyRsessionLogger r;
    public static boolean ErrorLoggerActivated = false;
    public static boolean LogLoggerActivated = false;
    /*
     * we set up a psuedo random
     */
    public static Random pRandom = new Random(6789); //pseudo random
    /*
     * file size variables
     */
    public static final int Mo = 1024 * 1024;
    public static final int Ko = 1024;
    /*
     * multiThreading settings
     */
    public static int nb_of_processors_to_use = 1;
    /*
     * Misc
     */
    public static Object GUI;
    public static boolean isWarningShowing = false;
    /*
     * angular variables
     */
    public static final int ZERO_TO_2PI = 0;
    public static final int ZERO_TO_PI = 1;
    public static final int ZERO_TO_PI_OVER2 = 2;
    public static final double TWO_PI = Math.PI * 2.;
    public static final double PI = Math.PI;
    public static final double PI_OVER_TWO = Math.PI / 2.;
    public static final double ONE_DEGREE_IN_RADIANS = Math.PI / 180.;
    public static final double TWO_DEGREE_IN_RADIANS = 2. * Math.PI / 180.;
    public static final double THREE_DEGREE_IN_RADIANS = 3. * Math.PI / 180.;
    public static final double FOUR_DEGREE_IN_RADIANS = 4. * Math.PI / 180.;
    public static final double FIVE_DEGREE_IN_RADIANS = 5. * Math.PI / 180.;
    public static final double TWO_PI_DEGREES = 360;
    public static final double PI_DEGREES = 180;
    public static final double PI_OVER_TWO_DEGREES = 90;

    /**
     * Sends a String to the system clipboard
     *
     * @param text
     */
    public static void sendTextToClipboard(String text) {
        /*
         * don't send text to clipboard if text is empty or invalid
         */
        if (text == null || text.trim().equals("")) {
            return;
        }
        StringSelection ss = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(ss, ss);
    }

    /**
     * returns screen dimensions
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * returns the width of the screen
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int getScreenWidth() {
        Dimension screen = getScreenSize();
        return screen.width;
    }

    /**
     * Returns the height of the screen
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int getScreenHeight() {
        Dimension screen = getScreenSize();
        return screen.height;
    }

    /**
     * create unicode from html or unicode string
     *
     * @param c
     */
    public static char getCharFromUnicode(String unicode) {
        if (unicode == null || unicode.isEmpty()) {
            return 0;
        }
        if (unicode.endsWith(";")) {
            unicode = unicode.substring(0, unicode.length() - 1);
        }
        try {
            if (unicode.startsWith("\\u")) {
                return (char) Integer.parseInt(unicode.substring(2), 16);
            } else if (unicode.startsWith("&#x")) {
                return (char) Integer.parseInt(unicode.substring(3), 16);
            } else if (unicode.startsWith("&#")) {
                return (char) Integer.parseInt(unicode.substring(2));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * makes a pause
     *
     * @param duration_in_ms duration of the pauseDialog in milliseconds
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static void pause(long duration_in_ms) {
        try {
            Thread.sleep(duration_in_ms);
        } catch (Exception e) {
        }
    }

    public static String getFolder(Class c, String folder_name) {
        String lib_folder = null;
        String cur_folder = null;
        try {
            cur_folder = new File(c.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        if (cur_folder != null) {
            if (new File(cur_folder + "/" + folder_name).exists()) {
                lib_folder = cur_folder + "/" + folder_name;
            } else {
                lib_folder = new File(cur_folder).getParent() + "/" + folder_name;
                /*
                 * if also does not exist we roll back to initial folder
                 * bug fix for FIJI update site
                 */
                if (!new File(lib_folder).exists()) {
                    lib_folder = cur_folder + "/" + folder_name;
                }
            }
        }
        if (lib_folder != null) {
            lib_folder = CommonClassesLight.change_path_separators_to_system_ones(lib_folder);
        } else {
            lib_folder = "";
        }
        return lib_folder;
    }

    /**
     *
     * @return the architecture of the JVM (32 or 64 bits)
     */
    public static int getJVMArchitecture() {
        try {
            return System.getProperty("sun.arch.data.model").indexOf("64") == -1 ? 32 : 64; //check wether it also works on a openjdk or not
        } catch (Exception e) {
        }
        return System.getProperty("os.arch").indexOf("64") == -1 ? 32 : 64;
    }

    public static String getJAVA_HOME() {

        return change_path_separators_to_system_ones(System.getProperty("java.home"));
    }

    /**
     * Small code to rotate a point around another one
     *
     * @param point
     * @param center
     * @param angle
     * @return the rotated point
     */
    public static Point2D.Double rotatePointInRadians(Point2D.Double point, Point2D.Double center, double angle) {
        return rotatePointInRadiansWithPrecomputedAngles(point, center, Math.cos(angle), Math.sin(angle));
    }

    /**
     * same as rotatePointInRadians but with precomputed cosines and sines
     *
     * @param point
     * @param center
     * @param cosAngle
     * @param sinAngle
     * @return the rotated point
     */
    public static Point2D.Double rotatePointInRadiansWithPrecomputedAngles(Point2D.Double point, Point2D.Double center, double cosAngle, double sinAngle) {
        double x = center.x + (cosAngle * (point.x - center.x) - sinAngle * (point.y - center.y));
        double y = center.y + (sinAngle * (point.x - center.x) + cosAngle * (point.y - center.y));
        return new Point2D.Double(x, y);
    }

    /**
     * Recovers an unformatted string from an attributedString
     *
     * @param attributedText
     * @return a string containing the text of the attributedstring passed in
     */
    public static String getText(AttributedString attributedText) {
        String text = "";
        AttributedCharacterIterator iter = attributedText.getIterator();
        while (iter.getIndex() < iter.getEndIndex()) {
            text = text + iter.current();
            iter.next();
        }
        return text;
    }

    /**
     *
     * @return true if errors are being logged
     */
    public static boolean isErrorLoggerActivated() {
        return ErrorLoggerActivated;
    }

    /**
     * Defines whether errors should be logged or not
     *
     * @param ErrorLoggerActivated
     */
    public static void setErrorLoggerActivated(boolean ErrorLoggerActivated) {
        CommonClassesLight.ErrorLoggerActivated = ErrorLoggerActivated;
    }

    /**
     *
     * @return true if the System.out is being logged
     */
    public static boolean isLogLoggerActivated() {
        return LogLoggerActivated;
    }

    /**
     * Defines whether System.out should be logged
     *
     * @param LogLoggerActivated
     */
    public static void setLogLoggerActivated(boolean LogLoggerActivated) {
        CommonClassesLight.LogLoggerActivated = LogLoggerActivated;
    }

    /**
     * This function can disable all the components contained in a jpanel
     *
     * @param jp the panel
     * @param show if true enables otherwise disables
     */
    public static void enableOrDisablePanelComponents(JPanel jp, boolean show) {
        jp.setEnabled(show);
        Component[] comps = jp.getComponents();
        for (Component component : comps) {
            component.setEnabled(show);
        }
    }

    /**
     *
     * @return a connection to R
     */
    public static MyRsessionLogger getR() {
        return r;

    }

    /**
     * Defines the connection to R
     *
     * @param r
     */
    public static void setR(MyRsessionLogger r) {
        CommonClassesLight.r = r;
    }

    /**
     *
     * @return true if a connection to R is established
     */
    public static boolean isRReady() {
        if (r == null) {
            return false;
        }
        if (r.isRserverRunning()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param filename
     * @return true if the file is likely to be opened by R
     */
    public static boolean isRDataFrameCompatible(String filename) {
        if (filename == null) {
            return false;
        }
        String name = filename.toLowerCase();
        if (name.endsWith(".xls") || name.endsWith(".txt") || name.endsWith(".csv") || name.endsWith(".xlsx")) {
            return true;
        }
        return false;
    }

    public static void getDependencies() {
        getDependencies(false);
    }

    /**
     * Recovers all the execution chain leading to the method --> place
     * commonClasses.getDependencies() in the method you want to use
     *
     * @since <B>Packing Analyzer 5.5</B>
     */
    public static void getDependencies(boolean toErrorStream) {
        Throwable t = new Throwable();
        StackTraceElement traces[] = t.getStackTrace();
        for (int i = 0; i < traces.length; i++) {
            StackTraceElement stackTraceElement = traces[i];
            if (toErrorStream) {
                System.err.println(stackTraceElement);
            } else {
                System.out.println(stackTraceElement);
            }
        }
    }

    /**
     * Recursive file listing contained in a folder
     *
     * @param parentFolder
     * @param recurse if true search folders recursively
     * @param extensionPatterns extensions to be searched
     * @return a list of file contained in the parent folder
     * @since SF v2.3
     */
    public static ArrayList<String> list_whatever_full_path(String parentFolder, boolean recurse, ArrayList<String> extensionPatterns) {
        String[] data = new String[extensionPatterns.size()];
        int i = 0;
        for (String string : extensionPatterns) {
            data[i] = string;
        }
        return list_whatever_full_path(parentFolder, recurse, data);
    }

    /**
     * Recursive file listing contained in a folder
     *
     * @param parentFolder
     * @param recurse if true search folders recursively
     * @param extensionPatterns extensions to be searched
     * @return a list of file contained in the parent folder
     * @since SF v2.3
     */
    public static ArrayList<String> list_whatever_full_path(String parentFolder, boolean recurse, String... extensionPatterns) {
        ArrayList<String> list_folder = new ArrayList<String>();
        boolean takeAllFiles = false;
        if (extensionPatterns != null && extensionPatterns.length > 0) {
            int i = 0;
            loop0:
            for (String string : extensionPatterns) {
                if (string == null) {
                    i++;
                    continue;
                }
                /*
                 * we clean the extensions
                 */
                string = string.trim().toLowerCase();
                if (string.equals("*.*")) {
                    takeAllFiles = true;
                    break loop0;
                } else {
                    if (string.startsWith("*")) {
                        string = string.substring(1);
                    }
                }
                extensionPatterns[i] = string;
                i++;
            }
        } else {
            takeAllFiles = true;
        }
        if (!parentFolder.endsWith("/")) {
            parentFolder += "/";
        }
        File[] list_of_files = new File(change_path_separators_to_system_ones(parentFolder)).listFiles();
        if (list_of_files != null) {
            for (int i = 0; i < list_of_files.length; i++) {
                if (takeAllFiles) {
                    list_folder.add(CommonClassesLight.change_path_separators_to_system_ones(list_of_files[i].toString()));
                } else {
                    loop1:
                    for (String string : extensionPatterns) {
                        if (string == null) {
                            continue;
                        }
                        if (takeAllFiles || list_of_files[i].toString().toLowerCase().endsWith(string.toLowerCase().trim())) {
                            list_folder.add(CommonClassesLight.change_path_separators_to_system_ones(list_of_files[i].toString()));
                            break loop1;
                        }
                    }
                }
                if (recurse && list_of_files[i].isDirectory()) {
                    list_folder.addAll(list_whatever_full_path(list_of_files[i].toString(), recurse, extensionPatterns));
                }
            }
        }
        return list_folder;
    }

    /**
     *
     * @return an arraylist of file names from an arraylist of file paths
     */
    public static ArrayList<String> getFileNames(ArrayList<String> in) {
        if (in == null) {
            return null;
        }
        ArrayList<String> out = new ArrayList<String>();
        for (String string : in) {
            out.add(new File(string).getName());
        }
        return out;
    }

    /**
     *
     * @return the component of the current GUI or null
     */
    public static Component getGUIComponent() {
        if (GUI instanceof Component) {
            return (Component) GUI;
        } else {
            return null;
        }
    }

    /**
     *
     * @return the current GUI object
     */
    public static Object getGUI() {
        return GUI;
    }

    /**
     *
     * @return the ImageJ fileinfo for the current image
     */
    public static FileInfo getFileInfo() {
        ImagePlus ip = WindowManager.getCurrentImage();
        if (ip != null) {
            FileInfo fifo = ip.getFileInfo();
            return fifo;
        }
        return null;
    }

    /**
     * Swaps two objects in a listModel
     *
     * @param listModel
     * @param first_pos
     * @param second_pos
     */
    public static boolean swap(DefaultListModel listModel, int first_pos, int second_pos) {
        if (listModel != null && !listModel.isEmpty() && first_pos >= 0 && second_pos >= 0 && first_pos < listModel.size() && second_pos < listModel.size()) {
            Object tmp = listModel.get(first_pos);
            listModel.setElementAt(listModel.get(second_pos), first_pos);
            listModel.setElementAt(tmp, second_pos);
            return true;
        }
        return false;
    }

    /**
     * Opens a bufferedImage with ImageJ
     *
     * @param bimg
     */
    public static void sendToimageJ(BufferedImage bimg) {
        if (ImageJ.getArgs() == null) {
            CommonClassesLight.openImageJ();
        }
        /*
         * we force imageJ to show even if the user closed it
         */
        try {
            if (ij != null) {
                if (!ij.isVisible()) {
                    ij.setVisible(true);
                }
            }
        } catch (Exception e) {
            /*
             * if it fails it's no big deal
             */
        }
        ImagePlus ip = new ImagePlus("exported from ScientiFig", bimg);
        ip.show();
    }

    /**
     *
     * @return a bufferedImage corresponding for the selected image in ImageJ
     */
    public static BufferedImage getFromImageJ() {
        /*
         * in standalone mode we hide the import from ImageJ button
         * we show the button only if IJ is not showing
         */
        if (ImageJ.getArgs() == null) {
            CommonClassesLight.openImageJ();
        }
        ImagePlus ip = WindowManager.getCurrentImage();
        if (ip != null) {
            BufferedImage img_from_IJ = ip.getBufferedImage();
            return img_from_IJ;
        }
        return null;
    }

    /**
     *
     * @return the title of the selected window in ImageJ
     */
    public static String getTitleJ() {
        try {
            return WindowManager.getCurrentImage().getTitle();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Sets the maximum number of processors to be used in multitreaded
     * functions
     *
     * @param max max nb of processors to be used
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int setMaxNbOfProcessors(int max) {
        max = Math.min(max, getNbOfCores());
        max = Math.max(max, 1);
        nb_of_processors_to_use = max;
        return nb_of_processors_to_use;
    }

    public static void setMaxNbOfProcessorsToMaxPossibleDividedByValue(int val) {
        val = val > 0 ? val : 1;
        nb_of_processors_to_use = getNbOfCPUs() / val;
    }

    public static void setMaxNbOfProcessorsToMaxPossible() {
        nb_of_processors_to_use = getNbOfCPUs();
    }

    /**
     * Returns true if the computer has more than one CPU
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static boolean isMultiThreadable() {
        return getNbOfCPUs() > 1 ? true : false;
    }

    /**
     * Returns the number of CPUs of the computer
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int getNumberOfCores() {
        return getNumberOfCPUs();
    }

    /**
     * Returns the number of CPUs of the computer
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int getNbOfCores() {
        return getNumberOfCPUs();
    }

    /**
     * Returns the number of CPUs of the computer
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int getNbOfCPUs() {
        return getNumberOfCPUs();
    }

    /**
     * Returns the number of CPUs of the computer
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int getNumberOfCPUs() {
        int processors = Runtime.getRuntime().availableProcessors();
        return processors;
    }

    /**
     * gets current time ("dd.MM.yyyy 'at' HH:mm:ss")
     *
     * @param dateFormat format of the date
     * @return a string containing current date
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String getTime(String dateFormat) {
        return now(dateFormat);
    }

    public static String getDate(String dateFormat) {
        return now(dateFormat);
    }

    /**
     * gets current time ("dd.MM.yyyy 'at' HH:mm:ss")
     *
     * @param dateFormat format of the date
     * @return a string containing current date
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @param isMultisel
     * @return the path to a file
     */
    public static Object open(Component parent, String root_folder, final Object ext, boolean isMultisel) {
        JFileChooser jfc = new JFileChooser(root_folder);
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    String fichier = f.toString().toLowerCase();
                    if (ext instanceof String) {
                        return f.isDirectory() || fichier.endsWith("." + ext) ? true : false;
                    } else if (ext instanceof ArrayList) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        ArrayList<String> exts = (ArrayList<String>) ext;
                        for (String string : exts) {
                            if (fichier.endsWith(string)) {
                                return true;
                            }
                        }
                    }
                } else {
                    return false;
                }
                return false;
            }

            @Override
            public String getDescription() {
                if (ext instanceof String) {
                    return (String) ext;
                } else if (ext instanceof ArrayList) {
                    String final_ext = "";
                    ArrayList<String> txt = (ArrayList<String>) ext;
                    for (String string : txt) {
                        final_ext += string + "; ";
                    }
                    if (final_ext.length() >= 2) {
                        final_ext = final_ext.substring(0, final_ext.length() - 2);
                    }
                    return final_ext;
                }
                return "supported files";
            }
        });
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(isMultisel);
        int retour;
        if (parent == null) {
            retour = jfc.showOpenDialog(CommonClassesLight.getGUIComponent());
        } else {
            retour = jfc.showOpenDialog(parent);
        }
        if (retour == JFileChooser.APPROVE_OPTION) {
            if (!isMultisel) {
                File nom = jfc.getSelectedFile();
                return nom.toString();
            } else {
                File[] nom = jfc.getSelectedFiles();
                return nom;
            }
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String open(Component parent, String root_folder, final String ext) {
        JFileChooser jfc = new JFileChooser(root_folder);
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                try {
                    if (f != null) {
                        String fichier = f.toString().toLowerCase();
                        return f.isDirectory() || fichier.endsWith("." + ext) ? true : false;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "." + ext.toLowerCase() + ",." + ext.toUpperCase();
            }
        });
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        int retour;
        if (parent == null) {
            retour = jfc.showOpenDialog(CommonClassesLight.getGUIComponent());
        } else {
            retour = jfc.showOpenDialog(parent);
        }
        if (retour == JFileChooser.APPROVE_OPTION) {
            File nom = jfc.getSelectedFile();
            if (!nom.toString().endsWith("." + ext)) {
                nom = new File(nom.toString().concat("." + ext));
            }
            return nom.toString();
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String open(Component parent, String root_folder, final ArrayList<String> ext) {
        JFileChooser jfc = new JFileChooser(root_folder);
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                try {
                    if (f != null) {
                        String fichier = f.toString().toLowerCase();
                        if (f.isDirectory()) {
                            return true;
                        }
                        for (String object : ext) {
                            if (fichier.endsWith("." + object)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                }
                return false;
            }

            @Override
            public String getDescription() {
                String exts = "";
                for (String string : ext) {
                    exts += "." + string + ",";
                }
                return exts;
            }
        });
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        int retour;
        if (parent == null) {
            retour = jfc.showOpenDialog(CommonClassesLight.getGUIComponent());
        } else {
            retour = jfc.showOpenDialog(parent);
        }
        if (retour == JFileChooser.APPROVE_OPTION) {
            File nom = jfc.getSelectedFile();
            return nom.toString();
        }
        return null;
    }

    /**
     * Code to save a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String saveFileNative(Frame parent, String root_folder, final String ext) {
        Object ret = openative(parent, root_folder, "", ext, false, FileDialog.SAVE);
        if (ret != null) {
            return (String) ret;
        }
        return null;
    }

    /**
     * Code to save a file
     *
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String saveFileNative(String root_folder, final String ext) {
        Object ret = openative(new Frame(), root_folder, null, ext, false, FileDialog.SAVE);
        if (ret != null) {
            return (String) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String openFileNative(Frame parent, String root_folder, final String ext) {
        Object ret = openative(parent, root_folder, null, ext, false, FileDialog.LOAD);
        if (ret != null) {
            return (String) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String openFileNative(Frame parent, String root_folder, final ArrayList<String> ext) {
        Object ret = openative(parent, root_folder, null, ext, false, FileDialog.LOAD);
        if (ret != null) {
            return (String) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String openFileNative(String root_folder, final String ext) {
        Object ret = openative(new Frame(), root_folder, null, ext, false, FileDialog.LOAD);
        if (ret != null) {
            return (String) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static File[] openFilesNative(String root_folder, final String ext) {
        Object ret = openative(new Frame(), root_folder, null, ext, true, FileDialog.LOAD);
        if (ret != null) {
            return (File[]) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static File[] openFilesNative(Frame parent, String root_folder, final String ext) {
        Object ret = openative(parent, root_folder, null, ext, true, FileDialog.LOAD);
        if (ret != null) {
            return (File[]) ret;
        }
        return null;
    }

    /**
     * Code to open files
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to selected files
     */
    public static File[] openFilesNative(Frame parent, String root_folder, final ArrayList<String> ext) {
        Object ret = openative(parent, root_folder, null, ext, true, FileDialog.LOAD);
        if (ret != null) {
            return (File[]) ret;
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param filename
     * @param ext
     * @param isMultiSel
     * @param mode
     * @return the path to a file or to files
     */
    public static Object openative(Frame parent, String root_folder, String filename, final Object ext, boolean isMultiSel, int mode) {
        String title = "Open...";
        if (mode == FileDialog.SAVE) {
            title = "Save...";
        }
        FileDialog jfc = new FileDialog(parent, title, mode);
        if (root_folder != null) {
            jfc.setDirectory(root_folder);
        } else {
            jfc.setDirectory(System.getProperty("user.home"));
        }
        jfc.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String fichier = name.toLowerCase();
                if (ext instanceof String) {
                    return name.endsWith((String) ext) ? true : false;
                } else {
                    ArrayList<String> txt = (ArrayList<String>) ext;
                    for (String string : txt) {
                        if (fichier.endsWith(string)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        });
        jfc.setMultipleMode(isMultiSel);
        jfc.setAlwaysOnTop(true);
        jfc.setVisible(true);
        jfc.requestFocus();
        if (isMultiSel) {
            return jfc.getFiles();
        } else {
            if (jfc.getDirectory() == null) {
                return null;
            }
            String path = CommonClassesLight.change_path_separators_to_system_ones(jfc.getDirectory());
            if (!path.endsWith("/")) {
                path += "/";
            }
            return path + jfc.getFile();
        }
    }

    /**
     * Code to open a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @param nativeDialog
     * @return the path to a file
     */
    public static String openFile(Frame parent, String root_folder, Object ext, boolean nativeDialog) {
        if (nativeDialog) {
            if (ext instanceof String) {
                return openFileNative(parent, root_folder, (String) ext);
            } else if (ext instanceof ArrayList) {
                return openFileNative(parent, root_folder, (ArrayList<String>) ext);
            }
        } else {
            Object ret = open(parent, root_folder, ext, false);
            if (ret != null) {
                return (String) ret;
            }
        }
        return null;
    }

    /**
     * Code to open files
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @param nativeDialog
     * @return the path to selected files
     */
    public static File[] openFiles(Frame parent, String root_folder, Object ext, boolean nativeDialog) {
        if (nativeDialog) {
            if (ext instanceof String) {
                return openFilesNative(parent, root_folder, (String) ext);
            } else if (ext instanceof ArrayList) {
                return openFilesNative(parent, root_folder, (ArrayList<String>) ext);
            }
        } else {
            Object ret = open(parent, root_folder, ext, true);
            if (ret != null) {
                return (File[]) ret;
            }
        }
        return null;
    }

    /**
     * Code to open a file
     *
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String open(String root_folder, final String ext) {
        return open(null, root_folder, ext);
    }

    /**
     * Code to save a file
     *
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String save(String root_folder, final String ext) {
        return save(null, root_folder, ext);
    }

    /**
     * Code to save a file
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @return the path to a file
     */
    public static String save(Component parent, String root_folder, final String ext) {
        JFileChooser jfc = new JFileChooser(root_folder);
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                try {
                    if (f != null) {
                        String fichier = f.toString().toLowerCase();
                        return f.isDirectory() || fichier.endsWith("." + ext) ? true : false;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "." + ext.toLowerCase() + ",." + ext.toUpperCase();
            }
        });
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        int retour;
        if (parent == null) {
            retour = jfc.showSaveDialog(CommonClassesLight.getGUIComponent());
        } else {
            retour = jfc.showSaveDialog(parent);
        }
        if (retour == JFileChooser.APPROVE_OPTION) {
            File nom = jfc.getSelectedFile();
            if (!nom.toString().endsWith("." + ext)) {
                nom = new File(nom.toString().concat("." + ext));
            }
            return nom.toString();
        }
        return null;
    }

    /**
     * code to save a file (allows to add an accessory panel to the save dialog)
     *
     * @param parent
     * @param root_folder
     * @param ext
     * @param accessory
     * @return the path to a file
     */
    public static String save(Component parent, String root_folder, final String ext, JPanel accessory) {
        JFileChooser jfc = new JFileChooser(root_folder);
        jfc.setAccessory(accessory);
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                try {
                    if (f != null) {
                        String fichier = f.toString().toLowerCase();
                        return f.isDirectory() || fichier.endsWith("." + ext) ? true : false;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "." + ext.toLowerCase() + ",." + ext.toUpperCase();
            }
        });
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        int retour;
        if (parent == null) {
            retour = jfc.showSaveDialog(CommonClassesLight.getGUIComponent());//sel.showOpenDialog(CommonClasses.getGUIComponent()
        } else {
            retour = jfc.showSaveDialog(parent);//sel.showOpenDialog(CommonClasses.getGUIComponent()
        }
        if (retour == JFileChooser.APPROVE_OPTION) {
            File nom = jfc.getSelectedFile();
            if (!nom.toString().endsWith("." + ext)) {
                nom = new File(nom.toString().concat("." + ext));
            }
            return nom.toString();
        }
        return null;
    }

    /**
     *
     * @return a hashmap containing greek letters
     */
    public static HashMap<String, String> getGreek() {
        return greek;
    }

    /**
     *
     * @param g2d
     * @return a graphics2D ready for high quality drawings
     */
    public static Graphics2D setHighQualityAndLowSpeedGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return g2d;
    }

    /**
     *
     * @param g2d
     * @return a graphics2D ready for low quality/fast drawings
     */
    public static Graphics2D setLowQualityAndHighSpeedGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        return g2d;
    }

    public static void create_folder_if_it_does_not_exist(String path) {
        if (path == null) {
            return;
        }
        String folder_name = new File(path).getParent();
        if (folder_name != null) {
            File test_folder = new File(folder_name);
            if (!test_folder.exists()) {
                test_folder.mkdirs();
            }
        }
    }

    public static Frame getParentFrame() {
        if (GUI != null) {
            if (GUI instanceof Frame) {
                return ((Frame) GUI);
            }
        }
        return null;
    }

    public static int getColorFromHtmlColor(Object evt) {
        return Integer.decode(evt.toString());
    }

    /**
     * creates a temporary file
     *
     * @param extension extension of the temporary file
     * @return unique temporary file
     * @since <B>Packing Analyzer 2.0</B>
     */
    public File CreateTempFile(String extension) {
        File temp_file = null;
        String tmp = getTempDirectory();
        if (extension == null || extension.equals("") || extension.equals(".")) {
            extension = ".tmp";
        }
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        try {
            temp_file = java.io.File.createTempFile("tmp_", extension, new File(tmp));
            temp_file.deleteOnExit();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
        return temp_file;
    }

    /**
     * Opens the default web browser to connect to the internet
     *
     * @param url url to browse
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static void browse(String url) {
        try {
            URI uri = new URI(url);
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                desktop.browse(uri);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * opens a file using the system default application to open it
     *
     * @param file file to open
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static void openUsingDefaultApplication(String file) {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                File f = new File(file);
                if (f.exists()) {
                    desktop.open(f);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        }
    }

    /**
     * Opens an image with ImageJ
     *
     * @param path path to the image
     */
    public static void openImageJ(String path) {
        if (ij == null || !ij.isShowing()) {
            CommonClassesLight.openImageJ();
        }
        ImagePlus ip = IJ.openImage(path);
        ip.show();
    }

    /**
     * Opens ImageJ
     */
    public static void openImageJ() {
        if ((ij == null || !ij.isVisible()) && ImageJ.getArgs() == null) {
            /**
             * nb: this does not work unless the code is compiled and run from
             * the jar file
             */
            if (CommonClassesLight.GUI != null) {
                System.getProperties().setProperty("plugins.dir", CommonClassesLight.getApplicationFolder(CommonClassesLight.GUI.getClass()));
            }
            if (ij == null) {
                ij = new ImageJ(null);
            }
            ij.exitWhenQuitting(false);
        }
    }

    /**
     * Selects a directory
     *
     * @param parent
     * @param init default folder/initial folder
     * @return path to the directory
     */
    public static String selectADirectory(Component parent, String init) {
        JFileChooser sel = new JFileChooser();
        File cur_dir = null;
        if (init != null) {
            if (new File(init).exists()) {
                cur_dir = new java.io.File(init);
            }
        }
        sel.setCurrentDirectory(cur_dir);
        /**
         * bug fix to allow the last folder to be reloader don't know if there
         * would be a better way to to so
         */
        if (cur_dir != null && cur_dir.exists()) {
            sel.setSelectedFile(cur_dir);
        }
        sel.setDialogTitle("Please Select An Output Folder");
        sel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        sel.setAcceptAllFileFilterUsed(false);
        if (parent == null) {
            if (sel.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                return sel.getSelectedFile().toString();
            }
        } else {
            if (sel.showOpenDialog(CommonClassesLight.getGUIComponent()) == JFileChooser.APPROVE_OPTION) {
                return sel.getSelectedFile().toString();
            }
        }
        return null;
    }

    /**
     * Selects a directory
     *
     * @param init default folder/initial folder
     * @return path to the directory
     */
    public static String selectADirectory(String init) {
        return selectADirectory(null, init);
    }

    /**
     * throws a 'not supported error'
     *
     * @throws Error
     */
    public static void notSupportedYet() throws Error {//--> facile a s'en rappeler comme ca
        throw new Error("not supported yet");
    }

    /**
     * Returns the current year
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static String getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * the default scroll speed of jscrollpane is too slow --> this function can
     * be used to increase jscrollpane speed
     *
     * @param speed_up_factor factor by which we want the scrolling speed to be
     * increased
     * @param jScrollPane1 the scrollpane we want to speed up
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static void speedUpJScrollpane(JScrollPane jScrollPane1, int speed_up_factor) {
        int init_speed = jScrollPane1.getVerticalScrollBar().getUnitIncrement();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(init_speed * speed_up_factor);
        init_speed = jScrollPane1.getHorizontalScrollBar().getUnitIncrement();
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(init_speed * speed_up_factor);
    }

    public static void speedUpJScrollpane(JScrollPane jScrollPane1) {
        speedUpJScrollpane(jScrollPane1, 20);
    }

    /**
     * Gets the system default temp directory
     *
     * @return temp directory
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String getTempDirectory() {
        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);
        return tempDir;
    }

    /**
     * Converts a point to a string (ready to be written to a file)
     *
     * @param pt
     * @return a string representing a point
     */
    public static String Point2DToString(Point2D.Double pt) {
        return pt.x + "\t" + pt.y;
    }

    /**
     * Returns the negative of a color
     *
     * @param color input color TYPE_INT_RGB
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int negative_color(int color) {
        int rgb = color;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb & 0xff);
        int negat = ((255 - red) << 16) + ((255 - green) << 8) + (255 - blue);
        return negat;
    }

    /**
     * Returns the name of the operating system
     *
     * @return a string containing the OS name (Linux, Mac or Windows)
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String getOS() {
        String operating_system = System.getProperty("os.name");
        if (operating_system.contains("inu") == true) {
            return "linux";
        } else if (operating_system.contains("ac") == true) {
            return "mac";
        } else {
            return "windows";
        }
    }

    /**
     * Converts an angle so that it fits within the bounds
     *
     * @param angle initial angle
     * @param ANGULAR_BOUND
     * @return a double containing the angle in radians.
     * @since <B>Packing Analyzer 4.4</B>
     */
    public static double getBoundedAngleRadians(double angle, int ANGULAR_BOUND) {
        while (angle < 0) {
            angle += TWO_PI;
        }
        while (angle >= TWO_PI) {
            angle -= TWO_PI;
        }
        switch (ANGULAR_BOUND) {
            case ZERO_TO_2PI:
                /**
                 * The code above did it we therefore don't have anything to do
                 */
                break;
            case ZERO_TO_PI:
                while (angle >= PI) {
                    angle -= PI;
                }
                break;
            case ZERO_TO_PI_OVER2:
                while (angle >= PI) {
                    angle -= PI;
                }
                if (angle > PI_OVER_TWO) {
                    angle = PI - angle;
                }
                break;
            default:
                System.err.println("wrong bounding angle");
                break;
        }
        return angle;
    }

    /**
     * Makes a path Linux friendly<br>NB: it also works under windows, so it's a
     * portable way to read a file path.
     *
     * @param path contains a path f
     * @return a string where all \ characters have been replaced by /
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String change_path_separators_to_system_ones(String path) {
        if (path == null) {
            return null;
        }
        String temp_strg = path;
        temp_strg = temp_strg.replace('\\', '/').replace("//", "/");
        return temp_strg;
    }

    /**
     * Returns a formattedstringout of a number
     *
     * @param val int number to be formatted
     * @param nb_of_digits number of digits of the formatted number
     * @return formatted number
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String create_number_of_the_appropriate_size(int val, int nb_of_digits) {
        String formatter = "%0" + nb_of_digits + "d";
        return String.format(formatter, val);
    }

    /**
     * Returns the path to the java executable
     *
     * @return path to the .jar executable
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static String getPath2JavaExecutable() {
        if (isUnix() || isMac()) {
            return change_path_separators_to_system_ones(System.getProperty("java.home")) + "/bin/java";
        } else {
            return change_path_separators_to_system_ones(System.getProperty("java.home")) + "/bin/java.exe";    
        }
    }

    /**
     * Provides the size of the file f in Mo
     *
     * @param f file name
     * @return size
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static double getFileSize(String f) {
        return getFileSize(new File(f));
    }

    /**
     * Provides the size of the file in Mo
     *
     * @param f file
     * @return size
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static double getFileSize(File f) {
        return ((f.length() / 1024.) / 1024.);
    }

    /*
     * this one uses real random numbers, not seeded random
     */
    public static int new_random_color_non_seeded_random() {
        int red = (int) (255 * Math.random());
        int green = (int) (255 * Math.random());
        int blue = (int) (255 * Math.random());
        int random_color = (red << 16) + (green << 8) + blue;
        return random_color;
    }

    /**
     * creates an RGB (pseudo)random color
     *
     * @return TYPE_INT_RGB color
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_random_color() {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        return random_color;
    }

    public static int new_random_gray() {
        int gray = pRandom.nextInt(255);
        int random_color = (gray << 16) + (gray << 8) + gray;
        return random_color;
    }

    public static int new_random_binary() {
        int gray = pRandom.nextInt(255);
        gray = (gray <= 128) ? 0 : 255;
        int random_color = gray;
        return random_color;
    }

    /**
     * Returns the width of a f
     *
     * @param gc Current Graphics Context
     * @param text input text
     * @return size
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int getTextWidth(Graphics2D gc, String text) {
        FontMetrics FM = gc.getFontMetrics(gc.getFont());
        return FM.stringWidth(text);
    }

    /**
     * Returns the height of a f
     *
     * @param gc Current Graphics Context
     * @return size
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int getTextHeight(Graphics2D gc) {
        FontMetrics FM = gc.getFontMetrics(gc.getFont());
        return FM.getHeight();
    }

    /**
     * Returns the width and the height of a f
     *
     * @param gc Current Graphics Context
     * @param text input text
     * @return width and height of the f
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static Dimension getTextSize(Graphics2D gc, String text) {
        FontMetrics FM = gc.getFontMetrics(gc.getFont());
        return new Dimension(FM.stringWidth(text), FM.getHeight());
    }

    public static Dimension getTextSize(String text, Font ft) {
        BufferedImage junk = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = junk.createGraphics();
        FontMetrics FM = g2d.getFontMetrics(ft);
        g2d.dispose();
        return new Dimension(FM.stringWidth(text), FM.getHeight());
    }

    /**
     * Returns an ArrayList containing all the colors that should not be used in
     * this software
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static ArrayList<Integer> forbiddenColors() {
        return createArrayListWithForbiddenColors();
    }

    /**
     * Returns an ArrayList containing all the colors that should not be used in
     * this software
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static ArrayList<Integer> createArrayListWithForbiddenColors() {
        ArrayList<Integer> forbidden_colors = new ArrayList<Integer>();
        forbidden_colors.add(0x000000);
        forbidden_colors.add(0xFFFFFF);
        forbidden_colors.add(0x0000FF);
        forbidden_colors.add(0x00FF00);
        forbidden_colors.add(0xFF0000);
        forbidden_colors.add(0xEFFFFF);
        return forbidden_colors;
    }

    /**
     * Reseeds the (pseudo)random (in order to get a reproducible series of
     * random numbers)
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static void reinitRandom() {
        pRandom = new Random(6789);
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color() {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (random_color == 0x000000 || random_color == 0xFFFFFF) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @param colors already existing random numbers
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color(ArrayList<Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.contains(random_color)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     *
     * @param colors
     * @return a random color that is quite bright
     */
    public static int new_light_random_color(ArrayList<Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.contains(random_color) && !(red > 128 || green > 128 || blue > 128)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @param colors already existing random numbers
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color(CopyOnWriteArraySet<Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.contains(random_color)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @param colors already existing random numbers
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color(Set<Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.contains(random_color)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @param colors already existing random numbers
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color(CopyOnWriteArrayList<Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.contains(random_color)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * Returns a new unique pseudo random color
     *
     * @param colors already existing random numbers
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int new_unique_random_color(ConcurrentHashMap<ArrayList<Integer>, Integer> colors) {
        int red = pRandom.nextInt(255);
        int green = pRandom.nextInt(255);
        int blue = pRandom.nextInt(255);
        int random_color = (red << 16) + (green << 8) + blue;
        while (colors.containsValue(random_color)) {
            red = pRandom.nextInt(255);
            green = pRandom.nextInt(255);
            blue = pRandom.nextInt(255);
            random_color = (red << 16) + (green << 8) + blue;
        }
        return random_color;
    }

    /**
     * creates a new BufferedImage that is a copy of the original bufferedImage
     *
     * @param tmp BufferedImage to be copied
     * @return A bufferedOmage that is a copy of the original
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage copyImg(BufferedImage tmp) {
        if (tmp == null) {
            return null;
        }
        BufferedImage copy = new MyBufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copy.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return copy;
    }

    /**
     * creates a new BufferedImage that is a copy of the original Image
     *
     * @param tmp to be copied
     * @return A BufferedImage that is a copy of the original
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static BufferedImage copyImg(Image tmp) {
        if (tmp == null) {
            return null;
        }
        BufferedImage copy = new MyBufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = copy.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return copy;
    }

    /**
     * Removes all the duplicated entries of an ArrayList (order is lots)
     *
     * @param vec input ArrayList
     * @return an ArrayList without duplicated entries
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static <T> ArrayList<T> remove_doublons(ArrayList<T> vec) {
        Set<T> set = new HashSet<T>(vec);
        vec.clear();
        vec.addAll(set);
        return vec;
    }

    /**
     * Removes all the duplicated entries of an ArrayList and keeps the numbers
     * in their initial order
     *
     * @param vec input ArrayList
     * @return an ArrayList without duplicated entries
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static <T> ArrayList<T> remove_doublons_n_keep_order(ArrayList<T> vec) {
        Set<T> set = new LinkedHashSet<T>(vec);
        vec.clear();
        vec.addAll(set);
        return vec;
    }

    public static <T> ArrayDeque<T> remove_doublons_n_keep_order(ArrayDeque<T> vec) {
        Set<T> set = new LinkedHashSet<T>(vec);
        vec.clear();
        vec.addAll(set);
        return vec;
    }

    /**
     * Returns the HTML value of a color
     *
     * @param color input pixel color (RGB)
     * @return HTML color
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String getHtmlColor(int color) {
        String hex_color = Integer.toHexString(color);
        while (hex_color.length() < 8) {
            hex_color = "0" + hex_color;
        }
        String hexa_color = "#" + hex_color.substring(2);
        return hexa_color;
    }

    /**
     * an html code representing the color (alpha included)
     *
     * @param color
     * @return a html code representing a color with some alpha
     */
    public static String toHtmlColorWithAlpha(int color) {
        return getHtmlColorWithAlpha(color);
    }

    /**
     * an html code representing the color (alpha included)
     *
     * @param color
     * @return a html code representing a color with some alpha
     */
    public static String getHtmlColorWithAlpha(int color) {
        String hex_color = Integer.toHexString(color);
        while (hex_color.length() < 8) {
            hex_color = "0" + hex_color;
        }
        String hexa_color = "#" + hex_color;
        return hexa_color;
    }

    /**
     * Returns the HTML value of a color
     *
     * @param color input pixel color (RGB)
     * @return HTML color
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String toHtmlColor(int color) {
        return getHtmlColor(color);
    }

    /**
     * Returns the HTML value of the color
     *
     * @param red red 8 bit (0-255)
     * @param green green 8 bit (0-255)
     * @param blue blue 8 bit (0-255)
     * @return HTML color
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String getHtmlColor(int red, int green, int blue) {
        int color = (red << 16) + (green << 8) + blue;
        return getHtmlColor(color);
    }

    /**
     * converts R,G and B ints to a single 24 bits color
     *
     * @param red
     * @param green
     * @param blue
     * @return a 24bits int containing red, green and blue channels (8bits each)
     */
    public static int getColorFromRGB(int red, int green, int blue) {
        int color = (red << 16) + (green << 8) + blue;
        return color;
    }

    /**
     *
     * @return the path to the lut folder of ImageJ
     */
    public String getImageJLUTFolder() {
        return Prefs.getHomeDir() + File.separator + "luts" + File.separator;
    }

    /**
     *
     * @param color
     * @return the red component of a color
     */
    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     *
     * @param color
     * @return the green component of a color
     */
    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     *
     * @param color
     * @return the blue component of a color
     */
    public static int getBlue(int color) {
        return (color) & 0xFF;
    }

    /**
     * Converts a "TRUE" or "FALSE" object to its corresponding boolean
     *
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static boolean String2Boolean(Object in) {
        if (in != null) {
            if (in.toString().toLowerCase().contains("ue")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a filename without the extension
     *
     * @param in input file name
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static String getName(String in) {
        if (in == null) {
            return null;
        }
        if (in.contains(".")) {
            return strcutl_last(in, ".");
        } else {
            return in;
        }
    }

    /**
     * Returns the string on the left of a pattern
     *
     * @param in input f
     * @param pattern cut left of this pattern
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static String strcutl_first(String in, String pattern) {
        int idx = in.indexOf(pattern);
        if (idx == -1) {
            /**
             * maybe I should better return null or return in
             */
            return "";
        }
        return in.substring(0, idx);
    }

    /**
     * Returns a string on the left of a pattern
     *
     * @param in input f
     * @param pattern cut left of this pattern
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static String strcutl_last(String in, String pattern) {
        int idx = in.lastIndexOf(pattern);
        if (idx == -1) {
            /**
             * maybe I should better return null or return in
             */
            return "";
        }
        return in.substring(0, idx);
    }

    /**
     * Returns a string on the right of a pattern
     *
     * @param in input f
     * @param pattern cut right of this pattern
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static String strcutr_fisrt(String in, String pattern) {
        int idx = in.indexOf(pattern);
        if (idx == -1) {
            /**
             * maybe I should better return null or return in
             */
            return "";
        }
        return in.substring(idx + pattern.length(), in.length());
    }

    /**
     * Returns a string on the right of a pattern
     *
     * @param in input f
     * @param pattern cut right of this pattern
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static String strcutr_last(String in, String pattern) {
        int idx = in.lastIndexOf(pattern);
        if (idx == -1) {
            /**
             * maybe I should better return null or return in
             */
            return "";
        }
        return in.substring(idx + pattern.length(), in.length());
    }

    /**
     * Converts an object to a string (ideally the object should be a string
     * object)
     *
     * @param word
     * @return a string representation of an object
     */
    public static String String2String(Object word) {
        return word == null ? null : String2String(word.toString());
    }

    /**
     * Converts a syting to a string (i.e. nothing to do, but makes sense in the
     * view of the previous method)
     *
     * @param word
     * @return a string
     */
    public static String String2String(String word) {
        return word;
    }

    /**
     * Converts an Object to an int
     *
     * @param word f
     * @return int
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int String2Int(Object word) {
        if (word instanceof Integer) {
            return ((Integer) word);
        }
        return word == null ? null : String2Int(word.toString());
    }

    /**
     * Converts a string to an int
     *
     * @param word f
     * @return int
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static int String2Int(String word) {
        return Integer.parseInt(word.trim());
    }

    public static Integer String2Integer(String word) {
        try {
            return Integer.parseInt(word.trim());
        } catch (Exception e) {
        }
        return null;
    }

    public static Float String2Float(Object word) {
        if (word instanceof Float) {
            return ((Float) word);
        }
        return word == null ? null : String2Float(word.toString());
    }

    /**
     * Converts a string to a float
     *
     * @param word f
     * @return float
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static Float String2Float(String word) {
        try {
            return Float.parseFloat(word.trim());
        } catch (Exception e) {
        }
        return null;
    }

    public static Double String2Double(Object word) {
        if (word instanceof Double) {
            return ((Double) word);
        }
        return word == null ? null : String2Double(word.toString());
    }

    /**
     * Converts a string to a double
     *
     * @param word f
     * @return double
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static Double String2Double(String word) {
        try {
            return Double.parseDouble(word.trim());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Converts a Double to a String
     *
     * @param val A Double
     * @return f
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String Double2String(Double val) {
        return String.valueOf(val);
    }

    /**
     * Converts an int to a String
     *
     * @param val an int
     * @return f
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String Int2String(int val) {
        return String.valueOf(val);
    }

    /**
     * Converts a float to a String
     *
     * @param val A float
     * @return f
     * @since <B>Packing Analyzer 2.0</B>
     */
    public static String float2String(float val) {
        return String.valueOf(val);
    }

    /**
     * Finds the parent folder where the class is
     *
     * @param cur_class class we want to know where it is
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static String getApplicationFolder(Class cur_class) {
        String cur_folder = null;
        try {
            cur_folder = new File(cur_class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return cur_folder;
    }

    /**
     * Returns true if the computer is a Mac
     *
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static boolean isMac() {
        if (getOS().contains("ac")) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the computer is a Linux/Unix
     *
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static boolean isUnix() {
        return !isWindows() && !isMac();
    }

    public static boolean isLinux() {
        return isUnix();
    }

    /**
     * Returns true if the computer runs a Windows OS
     *
     * @since <B>Packing Analyzer 1.0</B>
     */
    public static boolean isWindows() {
        if (getOS().contains("indo")) {
            return true;
        }
        return false;
    }

    public static String image2HtmlToolTip(String tooltip_text, String... image_names) {
        /*
         * converts a image path to an html tooltip
         */
        tooltip_text = tooltip_text.replace("\n", "<BR>");
        String out = "<html>" + tooltip_text;
        if (image_names != null) {
            for (int i = 0; i < image_names.length; i++) {
                String string = image_names[i];
                out += "<BR><img src=\"file:" + change_path_separators_to_system_ones(string) + "\">";
            }
        }
        out += "</html>";
        return out;
    }

    public static String generateMultilineJlabel(String multiline_String) {
        return "<html>" + multiline_String.replace("\n", "<BR>") + "</html>";
    }

    public static void PrintGenerateMultilineJlabel(String multiline_String) {
        System.out.println(generateMultilineJlabel(multiline_String));
    }

    public static String generateJLabelColorString(String txt, int color) {
        return "<html><font color=\"" + toHtmlColor(color) + "\">" + txt + "</font></html>";
    }

    public static void PrintGenerateJLabelColorString(String txt, int color) {
        System.out.println(generateJLabelColorString(txt, color));
    }

    /**
     * Make a pixel look transparent, the output is similar to the
     * alphacomposition of java (see CommonClasses2.setPixelTransparency)
     *
     * @param pixel_value pixel that we want to make transparent
     * @param transparent_color transparent color to apply
     * @param transparency
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int makePixelTransparent(int pixel_value, int transparent_color, float transparency) {
        return setPixelTransparency(pixel_value, transparent_color, transparency);
    }

    /**
     * Make a pixel look transparent, the output is similar to the
     * alphacomposition of java
     *
     * @param pixel_value pixel that we want to make transparent
     * @param transparent_color transparent color to apply
     * @param transparency
     * @since <B>Packing Analyzer 3.0</B>
     */
    public static int setPixelTransparency(int pixel_value, int transparent_color, float transparency) {
        int red = (pixel_value >> 16) & 0xFF;
        int green = (pixel_value >> 8) & 0xFF;
        int blue = (pixel_value & 0xFF);
        int red_trans = (transparent_color >> 16) & 0xFF;
        int green_trans = (transparent_color >> 8) & 0xFF;
        int blue_trans = (transparent_color & 0xFF);
        red_trans = (int) (red_trans * transparency + (1. - transparency) * red);
        green_trans = (int) (green_trans * transparency + (1. - transparency) * green);
        blue_trans = (int) (blue_trans * transparency + (1. - transparency) * blue);
        return (0xff << 24) + ((red_trans) << 16) + ((green_trans) << 8) + (blue_trans);
    }

    /**
     * Converts an URI list to an array of file names
     *
     * @param s URI list string
     * @return a list of file names
     */
    public static ArrayList<String> UriListToStringArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        ArrayList<String> files = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            URI uri;
            try {
                uri = new URI(st.nextToken());
                files.add(uri.getPath());
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                System.err.println(stacktrace);
            }
        }
        return files;
    }

    /**
     * pops up a warning dialog
     *
     * @param parent
     * @param text
     */
    public static void Warning(Component parent, String text) {
        String formatted_text = generateMultilineJlabel(text);
        if (!isWarningShowing) {
            isWarningShowing = true;
            JOptionPane.showMessageDialog(parent, formatted_text, "Warning...", JOptionPane.ERROR_MESSAGE);
            isWarningShowing = false;
            return;
        }
        isWarningShowing = false;
    }

    public static void showMessage(Component parent, String text) {
        String formatted_text = generateMultilineJlabel(text);
        JOptionPane.showMessageDialog(parent, formatted_text, "Information...", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * pops up a warning dialog
     *
     * @param text
     */
    public static void Warning(String text) {
        if (GUI instanceof Component) {
            Warning((Component) GUI, text);
        } else {
            Warning(null, text);
        }
    }

    /**
     * pops up an info dialog
     *
     * @param parent
     * @param text
     */
    public static void infos(Component parent, String text) {
        JTextArea jt = new JTextArea(text);
        JScrollPane js = new JScrollPane(jt);
        js.setSize(800, 500);
        js.setPreferredSize(new Dimension(800, 500));
        js.setMaximumSize(new Dimension(800, 500));
        JOptionPane.showMessageDialog(parent, js, "About...", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * pops up a warning dialog with a scrollable textarea
     *
     * @param parent
     * @param text
     */
    public static void Warning2(Component parent, String text) {
        JTextArea jt = new JTextArea(text);
        JScrollPane js = new JScrollPane(jt);
        js.setSize(800, 500);
        js.setPreferredSize(new Dimension(800, 500));
        js.setMaximumSize(new Dimension(800, 500));
        JOptionPane.showMessageDialog(parent, js, "Warning...", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String args[]) {
        if (true) {
            System.out.println(CommonClassesLight.getJVMArchitecture());
            return;
        }
        System.out.println(list_whatever_full_path("/D/sample_images_PA/trash_test_mem/images_de_test_fromIJ", true, ".png", ".TXT"));
        System.out.println(CommonClassesLight.RIGHT_ARROW_UP_BARB);
        System.out.println(CommonClassesLight.RIGHT_INHIBITION_SYMBOL);
        System.out.println(CommonClassesLight.LEFT_INHIBITION_SYMBOL);
        System.out.println(CommonClassesLight.LEFT_RIGHT_ARROW);
        System.out.println(CommonClassesLight.LEFT_INHIBITION_SYMBOL + "" + CommonClassesLight.RIGHT_INHIBITION_SYMBOL);
        System.exit(0);
    }
}


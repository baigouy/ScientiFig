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
package Tools;

import Commons.CommonClassesLight;
import MyShapes.JournalParameters;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * PopulateJournalStyles contains all journal styles found on the system
 *
 * @since <B>Packing Analyzer 5.0</B>
 * @author Benoit Aigouy
 */
public class PopulateJournalStyles {

    /**
     * Variables
     */
    public static ArrayList<JournalParameters> journalStyles = new ArrayList<JournalParameters>();
    public static String journalStyleFolder;

    /**
     * New Instance
     */
    public PopulateJournalStyles() {
        retroCompatibility();
        journalStyleFolder = CommonClassesLight.getFolder(this.getClass(), "JournalStyles");
        /*
         * now we load all available journal styles in the combo
         */
        File[] detectedStyles = new File(journalStyleFolder).listFiles();
        /*
         * bug fix for FIJI update site that for unknown reasons seems to exclude .xml files from their upload and update
         */
        if (detectedStyles != null) {
            for (File file : detectedStyles) {
                /*
                 * changed .xml for .txt extension in order to be compatible with the FIJI uploader
                 */
                if (file.toString().toLowerCase().endsWith(".txt")) {
                    JournalParameters jp = new JournalParameters();
                    boolean success = jp.loadParameters(file.toString());
                    if (success) {
                        jp.setPath(file.toString());
                        journalStyles.add(jp);
                    }
                }
            }
        }
    }

    /**
     * checks whether there are .xml files if the journal style folder and
     * rename them as .txt files. I had to change that because to bypass some
     * FIJI uploader file restrictions.
     *
     * @see
     * https://github.com/imagej/imagej/blob/master/core/updater/src/main/java/imagej/updater/core/Checksummer.java#L416
     * @source Johannes Schindelin
     */
    private void retroCompatibility() {
        journalStyleFolder = CommonClassesLight.getFolder(this.getClass(), "JournalStyles");
        File[] detectedStyles = new File(journalStyleFolder).listFiles();
        if (detectedStyles != null) {
            for (File file : detectedStyles) {
                String name_without_ext = CommonClassesLight.getName(file.toString());
                if (file.toString().toLowerCase().endsWith(".xml")) {
                    File renamed = new File(name_without_ext + ".txt");
                    if (!renamed.exists()) {
                        file.renameTo(renamed);
                    } else {
                        file.renameTo(new File(name_without_ext + ".txt.old"));
                    }
                }
            }
        }
    }

    /**
     * loads current journal styles in a combobox
     *
     * @param jComboBox1
     */
    public void reloadStyles(JComboBox jComboBox1) {
        Vector v = new Vector();
        for (JournalParameters style : journalStyles) {
            v.add(style.getName());
        }
        jComboBox1.setModel(new DefaultComboBoxModel(v));
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        PopulateJournalStyles test = new PopulateJournalStyles();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



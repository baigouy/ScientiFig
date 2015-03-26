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
package Tools;

import Commons.CommonClassesLight;
import R.ThemeGraph;
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
public class PopulateThemes {

    /**
     * Variables
     */
    public static ArrayList<ThemeGraph> themes = new ArrayList<ThemeGraph>();
    public static String themeFolder;

    /**
     * New Instance
     */
    public PopulateThemes() {
        themes.clear();
        themeFolder = CommonClassesLight.getFolder(this.getClass(), "GraphThemes");
        /*
         * now we load all available themes in the combo
         */
        File[] detectedStyles = new File(themeFolder).listFiles();
        ThemeGraph theme = new ThemeGraph();
        theme.setThemeName("Default");
        themes.add(theme);//the first theme will be the default theme where basically nothing is set
        if (detectedStyles != null) {
            for (File file : detectedStyles) {
                if (file.toString().toLowerCase().endsWith(".txt")) {
                    ThemeGraph tg = new ThemeGraph();
                    boolean success = tg.loadParameters(file.toString());
                    if (success) {
                        tg.setPath(file.toString());
                        themes.add(tg);
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
    public void reloadThemes(JComboBox jComboBox1) {
        Vector v = new Vector();
        for (ThemeGraph style : themes) {
            String name = style.getThemeName();
            if (name != null) {
                if (v.contains(name)) {
                    while (v.contains(name)) {
                        name += "_2";
                    }
                }
                v.add(name);
            }
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
        PopulateThemes test = new PopulateThemes();
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}



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
package MyShapes;

import Commons.CommonClassesLight;
import Commons.Loader;
import Commons.SaverLight;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ProgressMonitor;

/**
 * Exportable is a serializable object that contains everything needed to
 * recreate a figure or a montage, this object is typically saved with the
 * extension .yf5m
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class Exportable implements Serializable {

    /**
     * Variables
     */
    public static final long serialVersionUID = 5209962906960838061L;
    /**
     * version can be used to handle retrocompatibility
     */
    int version = 1;
    HashMap<Integer, Montage> tables = new HashMap<Integer, Montage>();
    ArrayList<Object> rows = new ArrayList<Object>();
    ArrayList<String> list_of_files = new ArrayList<String>();
    HashMap<String, SerializableBufferedImage2> imageJImports = new HashMap<String, SerializableBufferedImage2>();
    HashMap<String, Double> importJPixelSize = new HashMap<String, Double>();
    public String selectedStyle;

    /**
     * empty constructor
     */
    public Exportable() {
    }

    /**
     * Constructor
     *
     * @param montages contains all the montages created in ScientiFig
     * @param rows contains all rows of the figure created in ScientiFig
     * @param list_of_files the list of files that was initially contained in
     * the 'image list' list
     * @param selectedStyle is the last journal style selected by the user
     * @param imageJImports contains all the images imported from imageJ
     * @param importJPixelSize contains pixel size for all images imported from
     * ImageJ/FIJI
     */
    public void save(HashMap<Integer, Montage> montages, ArrayList<Object> rows, ArrayList<String> list_of_files, String selectedStyle, HashMap<String, SerializableBufferedImage2> imageJImports, HashMap<String, Double> importJPixelSize, boolean show_progress, String fichier) {
        /*
         * Although the name montage would suit this variable better we must keep this name in order to preserve compatibility with older versions of the software
         */
        ProgressMonitor pr = null;
        if (show_progress) {
            pr = new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Saving", "Please wait...", 0, 100);
            pr.setProgress(0);
            pr.setMillisToDecideToPopup(0);
            pr.setMillisToPopup(0);
            pr.setProgress(5);
        }

        this.tables = montages;
        this.rows = rows;
        /*
         * I removed this because it is redundant with the following loop
         */
        if (rows != null) {
            for (Object row : rows) {
                ((Row) row).getExtraTextreadyForSerialization();
            }
        }
        if (pr != null) {
            pr.setProgress(5);
        }
        if (montages != null) {
            for (Montage object : montages.values()) {
                object.getTextreadyForSerialization();
            }
        }
        if (pr != null) {
            pr.setProgress(40);
        }
        this.list_of_files = list_of_files;
        this.selectedStyle = selectedStyle;
        if (imageJImports != null) {
            this.imageJImports = imageJImports;
        }
        if (pr != null) {
            pr.setProgress(90);
        }
        if (importJPixelSize != null) {
            this.importJPixelSize = importJPixelSize;
        }
        if (pr != null) {
            pr.setProgress(95);
        }
        SaverLight.saveObjectRaw(this, fichier);

        if (pr != null) {
            pr.setProgress(100);
            pr.close();
        }
    }

    /**
     *
     * @return the list of files that was initially contained in the 'image
     * list' list
     */
    public ArrayList<String> getList_of_files() {
        return list_of_files;
    }

    /**
     * Assign the variable list_of_files the content of 'image list'
     *
     * @param list_of_files
     */
    public void setList_of_files(ArrayList<String> list_of_files) {
        this.list_of_files = list_of_files;
    }

    /**
     *
     * @return the Montages
     */
    public HashMap<Integer, Montage> getTables() {
        return tables;
    }

    /**
     * Store all the montages contained in ScientiFig
     *
     * @param tables
     */
    public void setTables(HashMap<Integer, Montage> tables) {
        this.tables = tables;
    }

    /**
     *
     * @return all the rows used to create a figure
     */
    public ArrayList<Object> getRows() {
        return rows;
    }

    /**
     * Store all the rows used to create a figure in ScientiFig
     *
     * @param rows
     */
    public void setRows(ArrayList<Object> rows) {
        this.rows = rows;
    }

    /**
     *
     * @return the
     */
    public String getSelectedStyle() {
        return selectedStyle;
    }

    /**
     *
     * @param selectedStyle
     */
    public void setSelectedStyle(String selectedStyle) {
        this.selectedStyle = selectedStyle;
    }

    /**
     *
     * @param input_file_name
     */
    public void load(String input_file_name) {
        Exportable ex;
        ProgressMonitor pr = null;
        try {
            pr = new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Importing data", "Please wait...", 0, 100);
            pr.setProgress(5);
            pr.setMillisToDecideToPopup(0);
            pr.setMillisToPopup(0);
            if (!new File(input_file_name).exists()) {
                return;
            }
            Object obj = new Loader().loadObjectRaw(input_file_name);
            pr.setProgress(50);
            if (obj == null || !(obj instanceof Exportable)) {
                CommonClassesLight.Warning("Loading failed! Your input file is corrupted.");
                return;
            }
            ex = (Exportable) obj;
            /*
             * we reload all the parameters
             */
            this.rows = ex.getRows();
            this.tables = ex.getTables();
            this.version = ex.version;
            if (rows != null) {
                for (Object row : rows) {
                    ((Row) row).recreateStyledDocForExtras();
                }
            }
            pr.setProgress(75);
            if (tables != null) {
                for (Montage object : tables.values()) {
                    object.recreateStyledDoc();
                    object.updateGraphScripts();
                }

            }
            
            //TODO force update plot code to get rid of errors
            //but do not update display
            
            if (version < 1) {
                /**
                 * allows for retrocompatibility of files saved using SF
                 * versions < v2.95 i.e. before the introcdution of transparency
                 * and filling of shapes
                 */
                if (tables != null) {
                    for (Montage object : tables.values()) {
                        object.setROIDrawOpacity(1f);
                    }
                }
            }

            pr.setProgress(90);
            this.list_of_files = ex.getList_of_files();
            /**
             * bug fix for files that do not exist any longer and that have not
             * been imported from ImageJ
             */
            if (list_of_files != null) {
                ArrayList<String> nonExistingFiles = new ArrayList<String>();
                for (String string : list_of_files) {
                    if (!string.contains("importJ:")) {
                        if (!new File(string).exists()) {
                            nonExistingFiles.add(string);
                        }
                    }
                }
                list_of_files.removeAll(nonExistingFiles);
            }
            this.selectedStyle = ex.getSelectedStyle();
            if (ex.getImageJImports() != null) {
                this.imageJImports = ex.getImageJImports();
                for (Map.Entry<String, SerializableBufferedImage2> entry : imageJImports.entrySet()) {
                    entry.getValue().reloadAfterSerialization();
                }
            }
            if (ex.getImageJImportsPxSize() != null) {
                this.importJPixelSize = ex.getImageJImportsPxSize();
            }
            pr.setProgress(100);
        } catch (Exception e) {
            /**
             * TODO remove that because it is in the finally already so it's
             * useless
             */
            if (pr != null) {
                pr.close();
            }
            StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw); e.printStackTrace(pw);
            String stacktrace = sw.toString();pw.close();
            System.err.println(stacktrace);
        } finally {
            /*
             * bug fix for progressMonitor not closing if the file is corrupted
             */
            if (pr != null) {
                pr.close();
            }
        }
    }

    /**
     *
     * @return all the images imported from imageJ
     */
    public HashMap<String, SerializableBufferedImage2> getImageJImports() {
        return imageJImports;
    }

    /**
     *
     * @return the pixel size for the images imported with imageJ
     */
    public HashMap<String, Double> getImageJImportsPxSize() {
        return importJPixelSize;
    }

    public int getVersion() {
        return version;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        MyImage2D.Double i2d = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png");
        MyImage2D.Double i2d2 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png");
        MyImage2D.Double i2d3 = new MyImage2D.Double(0, 0, "/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png");
        ArrayList<Object> comps = new ArrayList<Object>();
        comps.add(i2d);
        comps.add(i2d2);
        Montage b = new Montage(comps);
        Montage b2 = new Montage(i2d3);
        HashMap<Integer, Montage> tables = new HashMap<Integer, Montage>();
        tables.put(1, b);
        Row r1 = new Row(b2, 1);
        ArrayList<Object> rows = new ArrayList<Object>();
        rows.add(r1);
        r1.setFormula("24");
        ArrayList<String> list = new ArrayList<String>();
        list.add("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png");
        list.add("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png");
        list.add("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series016.png");
        HashMap<String, SerializableBufferedImage2> imageJ_imports = new HashMap<String, SerializableBufferedImage2>();
        imageJ_imports.put("importJ:" + 1, new SerializableBufferedImage2(new Loader().load("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series010.png")));
        imageJ_imports.put("importJ:" + 2, new SerializableBufferedImage2(new Loader().load("/D/sample_images_PA/trash_test_mem/images_de_test_pr_figure_assistant/Series014.png")));
        long start_time = System.currentTimeMillis();
        Exportable ex = new Exportable();
        ex.save(tables, rows, list, "Nature2", imageJ_imports, null, true, "/home/benoit/Bureau/test_save.yf5m");
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}

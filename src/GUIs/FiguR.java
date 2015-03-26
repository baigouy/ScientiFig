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
package GUIs;

import Commons.CommonClassesLight;
import Dialogs.AboutFigur;
import Dialogs.AddLine;
import Dialogs.AxisParameters;
import Dialogs.ColorSelector;
import Dialogs.DataOpener;
import Dialogs.ExportDialog;
import Dialogs.FacetParameters;
import Dialogs.FileOpeningParameters;
import Dialogs.LegendDialog;
import Dialogs.PlotSelectorDialog;
import Dialogs.ThemeEditor;
import MyShapes.MyPlotVector;
import R.Axis;
import R.AxisOrientation;
import R.Facets;
import R.GGplot;
import R.GGtitles;
import R.GeomPlot;
import R.HorVLine;
import R.RTools;
import R.ScaleColor;
import R.ThemeGraph;
import R.ThemeLegendPosition;
import Tools.PopulateJournalStyles;
import Commons.DropTargetHandler;
import Commons.Loader;
import Commons.SaverLight;
import Dialogs.RLabelEditor;
import R.RSession.MyRsessionLogger;
import R.String.RLabel;
import ij.WindowManager;
import ij.plugin.PlugIn;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.rosuda.REngine.REXPMismatchException;

//need to force as numeric in text if I want it to work --> put as.numeric in plots for x and y ...-->
//TODO force aes as.numeric only when necessary
/**
 * FiguR is a tool to create R graphs (FiguR takes care of font settings for
 * you)
 *
 * @author Benoit Aigouy
 */
public class FiguR extends javax.swing.JFrame implements PlugIn {

    /**
     * Variables
     */
    String lockerID = "FR";//short for FiguR
    boolean loading = false;
    ThemeGraph theme;
    String software_name = "FiguR";
    String version = "1.21 beta";
    RLabel titleLabel = new RLabel();
    RLabel xaxisLabel = new RLabel();
    RLabel yaxisLabel = new RLabel();
    RLabel legendLabel = new RLabel();
    int sheet_nb = 1;
    String textSeparator;
    String textDelimtor;
    String decimal;
    String lastPath = null;
    public static LogFrame logger;
    ArrayList<String> column_names = new ArrayList<String>();
    ArrayList<String> alreadyFactorizableColumns = new ArrayList<String>();
    ArrayList<Integer> nb_of_factors = new ArrayList<Integer>();
    MyRsessionLogger rsession;
    String R_code = "";
    String last_folder = null;
    String last_export_folder = null;
    boolean warnForSave = false;
    HashMap<String, Integer> colnames_and_nb_of_factors = new HashMap<String, Integer>();
    HashMap<String, ArrayList<String>> colnames_and_factors = new HashMap<String, ArrayList<String>>();
    DefaultListModel plotsListModel = new DefaultListModel();
    String filename;
    static DropTarget dt;
    /*
     * contains the main data
     */
    ArrayList<Object> data = new ArrayList<Object>();
    /*
     * contains the plots
     */
    ArrayList<Object> plots = new ArrayList<Object>();
    /*
     * contains extras for the different plots such as stats, ....
     */
    ArrayList<Object> extras = new ArrayList<Object>();

    /**
     * Empty constructor
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public FiguR() {
        if (isInstanceAlreadyExisting()) {
            this.dispose();
            return;
        }
        if (CommonClassesLight.isMac()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "FiguR");
        }
        if (!ScientiFig.propertiesLoaded) {
            ScientiFig.load_properties(this.getClass());
        }
        initComponents();
        pack();
        redirectErrorStream();
        if (logger != null) {
            logger.addLocker(lockerID);
        }
        System.out.println("Opening R connection.\nPlease Wait...");
        if (!isVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setVisible(true);
                }
            });
        }
        if (CommonClassesLight.ij == null) {
            CommonClassesLight.ij = ij.IJ.getInstance();
            if (CommonClassesLight.ij != null) {
                CommonClassesLight.isImageJEmbedded = false;
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (CommonClassesLight.ij == null) {
                    openWithIJ.setVisible(false);
                }
                checkRstatus();
            }
        });
        dt = new DropTarget(this, new DropTargetHandler() {
            @Override
            public void doSomethingWithTheFile(String data) {
                if (data.toLowerCase().endsWith(".figur")) {
                    openFile(data);
                } else if (data.toLowerCase().endsWith(".txt") || data.toLowerCase().endsWith(".csv") || data.toLowerCase().endsWith(".xls") || data.toLowerCase().endsWith(".xlsx")) {
                    openData(data);
                }
            }
        });
        WindowManager.addWindow(this);
    }

    /**
     *
     * @return the journal styles
     */
    public static PopulateJournalStyles getStyles() {
        if (ScientiFig.styles == null) {
            return new PopulateJournalStyles();
        } else {
            return ScientiFig.styles;
        }
    }

    /**
     *
     * @return the last instance of FiguR or a new one if no other instance
     * could be found
     */
    public static FiguR getInstance() {
        FiguR ls = isInstanceAlreadyExisting() ? getPreviousInstance() : new FiguR();
        return ls;
    }

    /**
     *
     * @return true if an instance of FiguR already exists false otherweise
     */
    public static boolean isInstanceAlreadyExisting() {
        Frame[] data = WindowManager.getNonImageWindows();
        for (Frame frame : data) {
            if (frame instanceof FiguR) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the previous FiguR instance
     */
    private static FiguR getPreviousInstance() {
        Frame[] data = WindowManager.getNonImageWindows();
        for (Frame frame : data) {
            if (frame instanceof FiguR) {
                if (!((FiguR) frame).isVisible()) {
                    ((FiguR) frame).setVisible(true);
                }
                return ((FiguR) frame);
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel4 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        xlsxFile = new javax.swing.JTextField();
        legendTitle = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        xAxisTitle = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        yAxisTitle = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jList1.setModel(plotsListModel);
        jButton7 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        edit = new javax.swing.JButton();
        title = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        colors = new javax.swing.JButton();
        noColors = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        fills = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        imagePaneLight1 = new Dialogs.ImagePaneLight();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        Rstatus = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        openFigurFile = new javax.swing.JMenuItem();
        save = new javax.swing.JMenuItem();
        saveAs = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        exportSvg = new javax.swing.JMenuItem();
        exportPDF = new javax.swing.JMenuItem();
        exportTIF = new javax.swing.JMenuItem();
        exportPNG = new javax.swing.JMenuItem();
        exportJPG = new javax.swing.JMenuItem();
        quit = new javax.swing.JMenuItem();
        Captions = new javax.swing.JMenu();
        capitalizeFirstLetter = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        ThemePreferences = new javax.swing.JMenuItem();
        resetTheme = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        yintercept = new javax.swing.JMenuItem();
        xintercept = new javax.swing.JMenuItem();
        remove_intercept = new javax.swing.JMenuItem();
        flipXY = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        showXAxisTile = new javax.swing.JCheckBoxMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        showYAxisTile = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        openWithIJ = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        about = new javax.swing.JMenuItem();
        citations = new javax.swing.JMenuItem();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(software_name+" v"+version);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onQuit(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));

        jLabel1.setText("Main Title:");

        jLabel2.setText("Legend Title:");

        xlsxFile.setEditable(false);
        xlsxFile.setEnabled(false);
        xlsxFile.setFocusable(false);

        legendTitle.setEditable(false);
        legendTitle.setFocusable(false);
        legendTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel19.setText("x Axis Title:");

        xAxisTitle.setEditable(false);
        xAxisTitle.setFocusable(false);
        xAxisTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel20.setText("y Axis Title:");

        yAxisTitle.setEditable(false);
        yAxisTitle.setFocusable(false);
        yAxisTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add Green Button.png"))); // NOI18N
        jButton3.setToolTipText("Add Plot");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Density", "Density Histogram", "Histogram/Barplot", "Rose Plot", "Lines", "Points", "Smooth/Regression", "BoxPlot", "Nematic/Line segment", "Arrow", "Rectangles", "Vertical Error Bars", "Horizontal Error Bars" }));

        jList1.setBorder(javax.swing.BorderFactory.createTitledBorder("Plots List"));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(jList1);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        jButton7.setToolTipText("Remove Selected Plot");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel3.setText("<html>Select A Plot style<br>then click on +</html>");

        edit.setText("Edit");
        edit.setToolTipText("Remove Selected Plot");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        title.setEditable(false);
        title.setFocusable(false);
        title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel5.setText("Input File:");

        jButton4.setText("Open");
        jButton4.setToolTipText("Open/Change Input File");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFile(evt);
            }
        });

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("use default software code");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customCodeOrSoftCode(evt);
            }
        });

        buttonGroup2.add(jRadioButton2);
        jRadioButton2.setText("use my custom code");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customCodeOrSoftCode(evt);
            }
        });

        colors.setText("\"Color\"");
        colors.setToolTipText("Colors");
        colors.setAlignmentY(0.0F);
        colors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        noColors.setText("Auto");
        noColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jLabel7.setText("Colors:");

        fills.setText("\"Fill\"");
        fills.setToolTipText("Colors");
        fills.setAlignmentY(0.0F);
        fills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jButton6.setText("Edit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jButton8.setText("Edit");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        jButton9.setText("Edit");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editText(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fills, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noColors, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(colors, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(xlsxFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(title)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2))
                    .addComponent(jLabel1))
                .addGap(0, 182, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(legendTitle))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xAxisTitle)
                            .addComponent(yAxisTitle))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xlsxFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(legendTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(xAxisTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(yAxisTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton7)
                    .addComponent(edit)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colors, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fills, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noColors)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {edit, jButton3, jButton7, jComboBox1, jLabel3});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {colors, fills, noColors});

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));

        imagePaneLight1.setMaximumSize(new java.awt.Dimension(256, 256));
        imagePaneLight1.setMinimumSize(new java.awt.Dimension(256, 256));
        imagePaneLight1.setPreferredSize(new java.awt.Dimension(256, 256));

        javax.swing.GroupLayout imagePaneLight1Layout = new javax.swing.GroupLayout(imagePaneLight1);
        imagePaneLight1.setLayout(imagePaneLight1Layout);
        imagePaneLight1Layout.setHorizontalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        imagePaneLight1Layout.setVerticalGroup(
            imagePaneLight1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 757, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(imagePaneLight1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Current Plot code"));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Code"));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane1.setViewportView(jTextArea3);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Knob Play Green.png"))); // NOI18N
        jButton5.setToolTipText("Refresh Plot");
        jButton5.setBorderPainted(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 80, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setAlignmentX(0.0F);
        jToolBar1.setBorderPainted(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/refresh.png"))); // NOI18N
        jButton2.setToolTipText("Refresh Plot");
        jButton2.setBorderPainted(false);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(jButton2);

        Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png"))); // NOI18N
        Rstatus.setText("R status");
        Rstatus.setBorderPainted(false);
        Rstatus.setFocusable(false);
        Rstatus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Rstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(Rstatus);

        jLabel6.setText("Mode :");
        jToolBar1.add(jLabel6);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("Safe (use most likely factors)");
        jRadioButton3.setFocusable(false);
        jToolBar1.add(jRadioButton3);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Unsafe (any column can be a factor)");
        jRadioButton4.setFocusable(false);
        jRadioButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jRadioButton4);

        jMenu1.setText("File");

        jMenuItem7.setText("Import Data File (.xls, .xlsx, .txt, .csv)");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFile(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        openFigurFile.setText("Open .FiguR file");
        openFigurFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(openFigurFile);

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(save);

        saveAs.setText("Save As...");
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(saveAs);

        jMenu4.setText("Export");

        exportSvg.setText("to svg");
        exportSvg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(exportSvg);

        exportPDF.setText("to pdf");
        exportPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(exportPDF);

        exportTIF.setText("to tif");
        exportTIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(exportTIF);

        exportPNG.setText("to png");
        exportPNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(exportPNG);

        exportJPG.setText("to jpg");
        exportJPG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(exportJPG);

        jMenu1.add(jMenu4);

        quit.setText("Quit");
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(quit);

        jMenuBar1.add(jMenu1);

        Captions.setText("Captions");

        capitalizeFirstLetter.setText("Format For Nature");
        Captions.add(capitalizeFirstLetter);

        jMenuBar1.add(Captions);

        jMenu2.setText("Data");

        jMenuItem6.setText("Preview (Only for small datasets)");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem8.setText("Edit (only for small datasets)");
        jMenuItem8.setEnabled(false);
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu10.setText("Themes");

        ThemePreferences.setText("preferences");
        ThemePreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu10.add(ThemePreferences);

        resetTheme.setText("reset theme");
        resetTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu10.add(resetTheme);

        jMenuBar1.add(jMenu10);

        jMenu3.setText("Legend");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jMenuItem1.setText("Hide or reposition");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        jMenu7.setText("Facets");

        jMenuItem4.setText("Add Facets");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu7.add(jMenuItem4);

        jMenuItem5.setText("Remove Facets");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu7.add(jMenuItem5);

        jMenuBar1.add(jMenu7);

        jMenu9.setText("Intercepts");

        yintercept.setText("add X intercept");
        yintercept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu9.add(yintercept);

        xintercept.setText("add Y intercept");
        xintercept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu9.add(xintercept);

        remove_intercept.setText("remove all intercepts");
        remove_intercept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu9.add(remove_intercept);

        jMenuBar1.add(jMenu9);

        flipXY.setText("Axes");

        jMenu5.setText("X axis");

        jMenuItem2.setText("Parameters And Text Orientation");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        showXAxisTile.setSelected(true);
        showXAxisTile.setText("Show X Axis Title");
        showXAxisTile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu5.add(showXAxisTile);

        flipXY.add(jMenu5);

        jMenu6.setText("Y axis");

        jMenuItem3.setText("Parameters and text orientation");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu6.add(jMenuItem3);

        showYAxisTile.setSelected(true);
        showYAxisTile.setText("Show Y Axis Title");
        showYAxisTile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu6.add(showYAxisTile);

        flipXY.add(jMenu6);

        jCheckBoxMenuItem1.setText("Flip x/y");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        flipXY.add(jCheckBoxMenuItem1);

        jMenuBar1.add(flipXY);

        openWithIJ.setText("ScientiFig");

        jMenuItem9.setText("Launch");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchScientiFig(evt);
            }
        });
        openWithIJ.add(jMenuItem9);

        jMenuBar1.add(openWithIJ);

        jMenu8.setText("About/Citations");

        jMenuItem12.setText("Show R installation guidelines");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu8.add(jMenuItem12);

        about.setText("About");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu8.add(about);

        citations.setText("Citations");
        citations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu8.add(citations);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane5)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Opens/reopens a connection to R
     */
    public void reinitRsession() {
        if (rsession != null) {
            try {
                rsession.close();
            } catch (Exception e) {
            }
            try {
                rsession.reopenConnection();
            } catch (Exception e) {
            }
        } else {
            if (CommonClassesLight.r != null) {
                rsession = CommonClassesLight.r;
                try {
                    rsession.reopenConnection();
                } catch (REXPMismatchException ex) {
                }
            } else {
                try {
                    rsession = new MyRsessionLogger();
                    rsession.reopenConnection();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     *
     * @return an R connection
     */
    public MyRsessionLogger getRserver() {
        if (rsession == null || !rsession.isRserverRunning()) {
            reinitRsession();
        }
        return rsession;
    }

    /**
     * Checks the status of the connection to R, upon success show a green
     * light, upon failure show a red light and display guidelines to get the
     * connection to R to become active
     */
    public void checkRstatus() {
        Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
        /*
         * the order has to be this one if we want the tool to work properly
         */
        if (rsession != null && rsession.isRserverRunning()) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            return;
        }
        if (CommonClassesLight.r != null && CommonClassesLight.r.isRserverRunning()) {
            this.rsession = CommonClassesLight.r;
            if (rsession.isRserverRunning()) {
                Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            }
            return;
        }
        getRserver();
        boolean isconnected = CommonClassesLight.isRReady();
        if (isconnected) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
        } else {
            if (rsession.isRserverRunning()) {
                Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            } else {
                Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
                /*
                 * conncetion failed --> we open the guidelines indicating how one can connect to R
                 */
                CommonClassesLight.Warning2(this, MyRsessionLogger.generateRInstallationText());
            }
        }
    }

    /**
     *
     * @return the number of graph colors
     */
    public int getTotalNbOfColorColors() {
        checkNbOfColorFactors();
        int total_nb_of_colors = 0;
        for (Object object : plots) {
            total_nb_of_colors += ((GeomPlot) object).getNb_of_Color_colors();
        }
        return total_nb_of_colors;
    }

    /**
     *
     * @return the number of graph fills
     */
    public int getTotalNbOfFillColors() {
        checkNbOfFillFactors();
        int total_nb_of_colors = 0;
        for (Object object : plots) {
            total_nb_of_colors += ((GeomPlot) object).getNb_of_Fill_colors();
        }
        return total_nb_of_colors;
    }

    /**
     *
     * @return true if only likely factors can be used as factors
     */
    public boolean isSafeMode() {
        return jRadioButton3.isSelected();
    }

    /**
     * Clean all dialog entries
     */
    public void clearAll() {
        column_names.clear();
        alreadyFactorizableColumns.clear();
        nb_of_factors.clear();
        plotsListModel.clear();
        jTextArea3.setText("");
        jTextArea2.setText("");
        title.setText("");
        legendTitle.setText("");
        xAxisTitle.setText("");
        yAxisTitle.setText("");
        xlsxFile.setText("");
        titleLabel = null;
        xaxisLabel = null;
        yaxisLabel = null;
        legendLabel = null;
    }
    private static final LinkedHashMap<String, String> replacements = new LinkedHashMap<String, String>();
    
    static {
        replacements.put("degrees", "°");
        replacements.put("*", CommonClassesLight.MULTIPLICATION + "");
        replacements.put("micron", CommonClassesLight.MICRON + "");
        
    }

    //can't do this in a string --> improve this
    public String italicizeSingleLetterVariable(String in) {
        return in;
    }

    /**
     * button dispatcher (here we centralize execution of all buttons)
     *
     * @param evt
     */
    private void runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAll
        Object source = null;
        if (evt != null) {
            source = evt.getSource();
        }
        if (source == jMenuItem12) {
            CommonClassesLight.Warning2(this, MyRsessionLogger.generateRInstallationText());
            return;
        }
        warnForSave = true;
        checkRstatus();
        if (source == quit) {
            closing(null);
            return;
        }
        
        if (source == capitalizeFirstLetter) {
            /*
             * formats all the text for nature related journals
             */
        }
        if (source == openFigurFile) {
            /*
             * we reopen and reload an existing .figur file
             */
            String name;
            if (!CommonClassesLight.isWindows() && ScientiFig.useNativeDialog) {
                if (filename != null) {
                    name = CommonClassesLight.openFileNative(this, new File(filename).getParent(), ".figur");
                } else {
                    name = CommonClassesLight.openFileNative(this, filename, ".figur");
                }
            } else {
                name = CommonClassesLight.open(this, filename, "figur");
            }
            if (name != null) {
                openFile(name);
            } else {
                return;
            }
        }
        if (source == about) {
            /*
             * opens an about box
             */
            AboutFigur iopane = new AboutFigur();
            JOptionPane.showOptionDialog(this, new Object[]{iopane}, "About", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            return;
        }
        if (source == citations) {
            /*
             * opens a citation dialog
             */
            if (rsession.isRserverRunning()) {
                CommonClassesLight.infos(this, rsession.getCitations());
            } else {
                CommonClassesLight.Warning(this, "A connection to R is needed to get the citations");
            }
            return;
        }
        if (source == Rstatus) {
            /*
             * checks the status of the connection to R
             */
            checkRstatus();
            return;
        }
        if (data.isEmpty()) {
            if (this.isVisible() && !loading) {
                CommonClassesLight.Warning(this, "Please open an .xls/.xlsx file first\nClick on File>Open File");
            }
            return;
        }
        R_code = "";
        if (source == save) {
            /*
             * we save the object as a .figur file
             */
            if (last_folder != null) {
                warnForSave = false;
                MyPlotVector.Double plot = getGraph();
                plot.data = data;
                plot.extras = extras;
                plot.plots = plots;
                plot.setTitleLabel(titleLabel);
                plot.setxAxisLabel(xaxisLabel);
                plot.setyAxisLabel(yaxisLabel);
                plot.setLegendLabel(legendLabel);
                plot.getTextreadyForSerialization();
                plot.doc2String(true);
                plot.setFullName(last_folder);
                plot.setShortName(CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutRightLast(CommonClassesLight.change_path_separators_to_system_ones(last_folder), "/"), "."));
                SaverLight.saveObjectRaw(plot, last_folder);
            } else {
                source = saveAs;
            }
        }
        if (source == saveAs) {
            /*
             * we save the object as a .figur file
             */
            JLabel infos = new JLabel("<html><font color=\"#FF0000\">NB: The save .figur file can<BR>be imported in ScientiFig<BR>and be dynamically replotted<BR>there</font></html>");
            JPanel jp = new JPanel();
            jp.add(infos);
            String name = CommonClassesLight.save(this, last_folder, "figur", jp);
            if (name != null) {
                warnForSave = false;
                name = CommonClassesLight.change_path_separators_to_system_ones(name);
                MyPlotVector.Double plot = getGraph();
                plot.data = data;
                plot.extras = extras;
                plot.plots = plots;
                plot.setTitleLabel(titleLabel);
                plot.setxAxisLabel(xaxisLabel);
                plot.setyAxisLabel(yaxisLabel);
                plot.setLegendLabel(legendLabel);
                plot.getTextreadyForSerialization();
                plot.doc2String(true);
                plot.setFullName(name);
                plot.setShortName(CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutRightLast(CommonClassesLight.change_path_separators_to_system_ones(name), "/"), "."));
                SaverLight.saveObjectRaw(plot, name);
                last_folder = name;
            }
            return;
        }
        if (source == noColors) {
            /*
             * remove all custom colors
             */
            ArrayList<Object> to_remove = new ArrayList<Object>();
            for (Object object : extras) {
                if (object instanceof ScaleColor) {
                    to_remove.add(object);
                }
            }
            extras.removeAll(to_remove);
        }
        if (source == colors) {
            /*
             * apply custom colors to plots
             */
            if (plots == null || plots.isEmpty()) {
                return;
            }
            String type = "color";
            int total_nb_of_colors = getTotalNbOfColorColors();
            ArrayList<Integer> current_colors = new ArrayList<Integer>();
            ArrayList<Object> to_remove = new ArrayList<Object>();
            for (Object object : extras) {
                if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals(type)) {
                    to_remove.add(object);
                    current_colors.addAll(((ScaleColor) object).getColors());
                }
            }
            ColorSelector iopane = new ColorSelector(total_nb_of_colors, current_colors);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Colors (Work in progress)", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                extras.removeAll(to_remove);
                ScaleColor sc = new ScaleColor(type);
                sc.setColors(iopane.getColors());
                extras.add(sc);
            } else {
                return;
            }
        }
        if (source == fills) {
            /*
             * apply custom fills to plots
             */
            if (plots == null || plots.isEmpty()) {
                return;
            }
            String type = "fill";
            int total_nb_of_colors = getTotalNbOfFillColors();
            ArrayList<Integer> current_colors = new ArrayList<Integer>();
            ArrayList<Object> to_remove = new ArrayList<Object>();
            for (Object object : extras) {
                if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals(type)) {
                    to_remove.add(object);
                    current_colors.addAll(((ScaleColor) object).getColors());
                }
            }
            ColorSelector iopane = new ColorSelector(total_nb_of_colors, current_colors);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Colors (Work in progress)", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                extras.removeAll(to_remove);
                ScaleColor sc = new ScaleColor(type);
                sc.setColors(iopane.getColors());
                extras.add(sc);
            } else {
                return;
            }
        }
        if (source == resetTheme) {
            theme = null;
        }
        if (source == ThemePreferences) {
            /*
             * Edit the R theme
             * TODO make a combo to select from several R themes stored as xml or txt ???
             */
            ThemeEditor iopane = new ThemeEditor(rsession);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Theme preferences", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                theme = iopane.getTheme();
            } else {
                return;
            }
        }
        if (source == jMenuItem2 || source == jMenuItem3) {
            /*
             * Change axis parameters
             */
            String axis = "x";
            if (source == jMenuItem3) {
                axis = "y";
            }
            AxisParameters iopane = new AxisParameters(axis);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Axis Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                Axis a = new Axis(iopane.getAxis());
                if (iopane.isAxisHidden()) {
                    a.setRemoveAxis(true);
                } else {
                    int orientation = iopane.getAngleInDegrees();
                    if (orientation != 0) {
                        AxisOrientation ao = new AxisOrientation(iopane.getAxis(), orientation);
                        extras.add(ao);
                    }
                }
                if (iopane.hideTicks()) {
                    a.setRemoveTicks(true);
                }
                if (!iopane.isAutoRange()) {
                    a.setRange(iopane.getLowerRange(), iopane.getUpperRange(), iopane.getInterval());
                }
                if (iopane.reverseAxis()) {
                    a.setAxisFlip(true);
                }
                a.setTRANSFORMATION(iopane.getTransformation());
                ArrayList<Object> to_remove = new ArrayList<Object>();
                if (extras != null && !extras.isEmpty()) {
                    for (Object extra : extras) {
                        if (extra instanceof Axis) {
                            if (((Axis) extra).getAxis().toLowerCase().equals(axis)) {
                                to_remove.add(extra);
                            }
                        }
                    }
                }
                extras.removeAll(to_remove);
                extras.add(a);
            } else {
                return;
            }
        }
        if (source == xintercept || source == yintercept) {
            /*
             * add x or y intercepts
             */
            AddLine iopane = new AddLine();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Intercepts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                String orientation = "h";
                if (source == xintercept) {
                    orientation = "v";
                }
                HorVLine hvline = new HorVLine(orientation);
                hvline.setColor(iopane.getColor());
                hvline.setLinetype(iopane.getLineType());
                hvline.setPos(iopane.getPos());
                hvline.setSize(iopane.getLineWidth());
                hvline.setAlpha(iopane.getAlpha());
                extras.add(hvline);
            } else {
                return;
            }
        }
        if (source == remove_intercept) {
            /*
             * remove all intercepts
             */
            ArrayList<Object> obj_to_remove = new ArrayList<Object>();
            for (Object obj : extras) {
                if (obj instanceof HorVLine) {
                    obj_to_remove.add(obj);
                }
            }
            extras.removeAll(obj_to_remove);
        }
        if (source == jMenuItem6) {
            /*
             * preview the current R dataFrame
             */
            if (data == null || data.isEmpty()) {
                CommonClassesLight.Warning(this, "Please open an .xls/.xlsx file first\nClick on File>Open File");
                return;
            }
            rsession.eval("edit(curDataFigR)");
            return;
        }
        if (source == jMenuItem8) {
            /*
             * here we edit/updtae the R dataFrame
             */
            if (data == null || data.isEmpty()) {
                CommonClassesLight.Warning(this, "Please open an .xls/.xlsx file first\nClick on File>Open File");
                return;
                
            }
            rsession.eval("curDataFigR <- edit(curDataFigR)");
        }
        if (source == exportJPG || source == exportPDF || source == exportPNG || source == exportSvg || source == exportTIF) {
            /*
             * we save using gsave
             */
            boolean isRaster = false;
            boolean allowFontSetting = true;
            String device = "device=";
            String extension = "svg";
            if (source == exportSvg) {
                device += "svg, ";
            }
            if (source == exportJPG) {
                extension = "jpg";
                device += "jpeg, limitsize=FALSE, ";
                isRaster = true;
                allowFontSetting = false;
            }
            /*
             TODO some day add all the missing formats
             eps <- ps <- function(..., width, height) grDevices::postscript(..., 
             tex <- function(..., width, height) grDevices::pictex(..., 
             pdf <- function(..., version = "1.4") grDevices::pdf(..., 
             svg <- function(...) grDevices::svg(...)
             wmf <- function(..., width, height) grDevices::win.metafile(..., 
             emf <- function(..., width, height) grDevices::win.metafile(..., 
             png <- function(..., width, height) grDevices::png(..., width = width, 
             jpg <- jpeg <- function(..., width, height) grDevices::jpeg(..., 
             bmp <- function(..., width, height) grDevices::bmp(..., width = width, 
             tiff <- function(
             */
            if (source == exportTIF) {
                extension = "tif";
                device += "tiff, limitsize=FALSE, ";
                isRaster = true;
            }
            if (source == exportPNG) {
                extension = "png";
                device += "png, limitsize=FALSE, ";
                isRaster = true;
            }
            if (source == exportPDF) {
                extension = "pdf";
                device += "pdf, ";
                allowFontSetting = false;
            }
            ExportDialog iopane = new ExportDialog(isRaster, allowFontSetting);
            String name = CommonClassesLight.save(this, last_export_folder, extension, iopane);
            if (name != null) {
                name = CommonClassesLight.change_path_separators_to_system_ones(name);
                double width = iopane.getImageWidth();
                double height = iopane.getImageHeight();
                boolean ignore_font = iopane.ignoreFont();
                String final_dpi = ", dpi=" + 72;
                String unit = ", unit=\"" + iopane.getUnit() + "\"";
                if (isRaster) {
                    final_dpi = "";
                    unit = "";
                }
                rsession.eval("ggsave(" + device + "file=\"" + name + "\", plot=" + getFormattedGraph(true) + ", width=" + width + ", height=" + height + final_dpi + unit + ")");
                last_export_folder = name;
            }
            return;
        }
        if (source == jMenuItem1) {
            /*
             * change the position of the legend
             */
            LegendDialog iopane = new LegendDialog();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Position Legend", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (!iopane.hideLegend()) {
                    if (iopane.isInsideTheGraph()) {
                        int POS1;
                        int POS2;
                        if (iopane.isInsideLeft()) {
                            POS2 = ThemeLegendPosition.LEFT;
                        } else {
                            POS2 = ThemeLegendPosition.RIGHT;
                        }
                        if (iopane.isInsideTop()) {
                            POS1 = ThemeLegendPosition.TOP;
                        } else {
                            POS1 = ThemeLegendPosition.BOTTOM;
                        }
                        extras.add(new ThemeLegendPosition(POS1, POS2));
                    } else {
                        int POS1 = 0;
                        if (iopane.isLeft()) {
                            POS1 = ThemeLegendPosition.LEFT;
                        }
                        if (iopane.isRight()) {
                            POS1 = ThemeLegendPosition.RIGHT;
                        }
                        if (iopane.isTop()) {
                            POS1 = ThemeLegendPosition.TOP;
                        }
                        if (iopane.isBottom()) {
                            POS1 = ThemeLegendPosition.BOTTOM;
                        }
                        extras.add(new ThemeLegendPosition(POS1));
                    }
                } else {
                    extras.add(new ThemeLegendPosition(ThemeLegendPosition.NOBAR));
                }
            } else {
                return;
            }
        }
        if (source == jMenuItem4) {
            /*
             * add facets to the plot
             */
            FacetParameters iopane = new FacetParameters(column_names, alreadyFactorizableColumns, isSafeMode());
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Select Data", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                removeAllFacets();
                String facetH = iopane.getFacetH();
                String facetV = iopane.getFacetV();
                if (facetH != null || facetV != null) {
                    Facets f = new Facets(facetV, facetH, iopane.isFreeScale(), iopane.isFreeSpace());
                    extras.add(f);
                }
            } else {
                return;
            }
        }
        if (source == jMenuItem5) {
            /*
             * removes all facets
             */
            removeAllFacets();
        }
        if (source == jButton3 || source == edit) {
            /*
             * Create a new plot or edit an existing one
             */
            if (source == edit && jList1.getSelectedIndex() == -1) {
                CommonClassesLight.Warning(this, "Please select a plot to edit in the 'Plots List'");
                return;
            }
            boolean line = false;
            boolean point = false;
            boolean isArrow = false;
            PlotSelectorDialog iopane;
            GeomPlot gp = null;
            if (source == edit) {
                if (jList1.getSelectedIndex() != -1) {
                    gp = (GeomPlot) plotsListModel.get(jList1.getSelectedIndex());
                }
            }
            String plot = "";
            /*
             * if there is no plot selected or if we want to create a new plor
             */
            if (source == jButton3 || gp == null) {
                /*
                 * creation of a graph
                 */
                String plotStyle = jComboBox1.getSelectedItem().toString();
                
                if (plotStyle.toLowerCase().contains("error")) {
                    if (plotStyle.contains("oriz")) {
                        plot = "geom_errorbarh";
                    } else {
                        plot = "geom_errorbar";
                    }
                    line = true;
                }
                if (plotStyle.toLowerCase().equals("boxplot")) {
                    plot = "geom_boxplot";
                }
                if (plotStyle.toLowerCase().contains("lines")) {
                    plot = "geom_line";
                    line = true;
                }
                if (plotStyle.toLowerCase().contains("points")) {
                    plot = "geom_point";
                    point = true;
                }
                if (plotStyle.toLowerCase().contains("rrow") || plotStyle.toLowerCase().contains("ematic")) {
                    plot = "geom_segment";
                }
                if (plotStyle.toLowerCase().contains("rrow")) {
                    isArrow = true;
                }
                if (plotStyle.toLowerCase().contains("ectangle")) {
                    plot = "geom_rect";
                }
                if (plotStyle.toLowerCase().contains("egression")) {
                    plot = "geom_smooth";
                    line = true;
                }
                /*
                 * nb les histos ne prennent pas de y --> faire un truc qui recup la data au debut et aussi gerer les facets comme c'est le truc le plus cool de R il faut que je m'y colle
                 */
                if (plotStyle.toLowerCase().contains("density")) {
                    if (plotStyle.toLowerCase().contains("histogram")) {
                        plot = "geom_bar";
                    } else {
                        plot = "geom_density";
                    }
                } else if (plotStyle.toLowerCase().contains("histogram")) {
                    plot = "geom_bar2";
                }
                if (plotStyle.toLowerCase().contains("ose")) {
                    plot = "geom_bar3";
                }
                iopane = new PlotSelectorDialog(plot, column_names, alreadyFactorizableColumns, isSafeMode(), line, point);
            } else {
                /*
                 * edit a graph
                 */
                plot = gp.getGeomType();
                iopane = new PlotSelectorDialog(gp, column_names, alreadyFactorizableColumns, isSafeMode());
                isArrow = (gp.getArrow() != null);
            }
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Select Data", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                GeomPlot geomPlot = new GeomPlot(plot);
                geomPlot.setX(iopane.getXaxis());
                geomPlot.setY(iopane.getYaxis());
                geomPlot.setBinWidth(iopane.getBinWidth());
                geomPlot.setAlpha(iopane.getAlpha());
                geomPlot.setAesFill(iopane.getAesFill());
                geomPlot.setAesColor(iopane.getAesColor());
                geomPlot.setAesShape(iopane.getShape());
                geomPlot.setStat(iopane.getStat());
                geomPlot.setLineType(iopane.getLineType());
                geomPlot.setShape(iopane.getPointType());
                geomPlot.setSize(iopane.getLineSize());
                geomPlot.setXmin(iopane.getXmin());
                geomPlot.setXmax(iopane.getXmax());
                geomPlot.setYmin(iopane.getYmin());
                geomPlot.setYmax(iopane.getXmax());
                geomPlot.setPosition(iopane.getDodged());
                geomPlot.setSE(iopane.getSE());
                geomPlot.setRegression(iopane.getRegressioMethod());
                geomPlot.setFormula(iopane.getFormula());
                geomPlot.setFamily(iopane.getFamily());
                if (isArrow) {
                    geomPlot.setArrow("sdqsd");
                }
                geomPlot.setXend(iopane.getXend());
                geomPlot.setYend(iopane.getYend());
                if (source == jButton3) {
                    plots.add(0, geomPlot);
                    plotsListModel.addElement(geomPlot);
                } else {
                    /*
                     * we update the plot
                     */
                    if (jList1.getSelectedIndex() != -1) {
                        plotsListModel.set(jList1.getSelectedIndex(), geomPlot);
                        plots.set(plots.indexOf(gp), geomPlot);
                    }
                }
            } else {
                return;
            }
        }
        if (source == jButton7) {
            /*
             * remove selected plots
             */
            int[] selectedPlots = jList1.getSelectedIndices();
            if (selectedPlots != null && selectedPlots.length > 0) {
                Arrays.sort(selectedPlots);
                for (int i = 0; i < selectedPlots.length / 2; i++) {
                    int tmp = selectedPlots[i];
                    selectedPlots[i] = selectedPlots[selectedPlots.length - (i + 1)];
                    selectedPlots[selectedPlots.length - (i + 1)] = tmp;
                }
                for (int i : selectedPlots) {
                    plots.remove(plotsListModel.elementAt(i));
                    plotsListModel.remove(i);
                }
                jList1.revalidate();
            }
        }
        updateGraph(source);
    }//GEN-LAST:event_runAll

    /*
     * Loads a data file to the R dataFrame called curDataFigR
     */
    private void openFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFile
        DataOpener iopane = new DataOpener(lastPath);
        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Open A Data File", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            checkRstatus();
            String name = CommonClassesLight.change_path_separators_to_system_ones(iopane.getFileName());
            if (name != null) {
                if (!new File(name).exists()) {
                    CommonClassesLight.Warning(this, "Please enter a valid file");
                    return;
                }
                xlsxFile.setText(name);
                sheet_nb = iopane.getSheetNumber();
                loadData(name, iopane.getSheetNumber(), iopane.getFieldSeparator(), iopane.getTextSeparator(), iopane.getDecimal());
                this.filename = name;
                this.lastPath = new File(name).getParent();
                enableOrDisableTextField();
            }
        }
    }//GEN-LAST:event_openFile

    /**
     * Loads a data file to the R dataFrame called curDataFigR
     *
     * @param name source file
     * @param sheet_nb the number of the sheet interest in an excel sheet
     * @param textSeparator the text separator (typically ',' ';', ...)
     * @param textDelimtor the text delimitor (typically '"')
     * @param decimal the decimal delimitor (, or .)
     */
    public void loadData(String name, int sheet_nb, String textSeparator, String textDelimtor, String decimal) {
        if (name == null) {
            return;
        }
        if (name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx")) {
            rsession.loadXLS("curDataFigR", name, sheet_nb);
        }
        if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".csv")) {
            rsession.loadCSVorTXT("curDataFigR", name, textSeparator, textDelimtor, decimal);
        }
        /*
         * we get rid of columns that only contain NAs --> formula is incorrect
         */
//        rsession.eval("curDataFigR <- curDataFigR[,colSums(is.na(curDataFigR)) == 0]");

        //nb in fact only do plot for complete case for the columns of interest --> should be easy
        //plot(V~Day, type="b", data=na.omit(data.frame(V,Day)))
//plot(V~Day, type="b", subset=complete.cases(V,Day))
        /**
         * we get rid of lines that contain NA (maybe don't do that) --> see
         * impact on plots
         */
        //or maybe check and warn the user and ask for the action to be taken
//        rsession.eval("curDataFigR <- curDataFigR[complete.cases((curDataFigR)),]");      //
        //par defaut le language omet les points manquants tout seul --> parfait si on force le omit il enleve toutes les lignes mauvaises ce qui n'a pas de sens la plupart du temps je pense donc le garder comme ca
        //sans mettre de na.omit(curDataFigR) ou curDataFigR[complete.cases((curDataFigR)),]
        column_names = rsession.getColumnNames("curDataFigR");
        alreadyFactorizableColumns = rsession.getAlreadyFactorizableColumns(column_names);
        Object[] values = rsession.getAllLikelyFactors(column_names, 20);
        /*
         * contains the nb of factors per column
         */
        if (values[0] != null) {
            colnames_and_nb_of_factors = (HashMap<String, Integer>) values[0];
        } else {
            colnames_and_nb_of_factors.clear();
        }
        /*
         * get all factors for each columns
         */
        if (values[1] != null) {
            colnames_and_factors = (HashMap<String, ArrayList<String>>) values[1];
        } else {
            colnames_and_factors.clear();
        }
        checkFiles();
        data.clear();
        data.add(new GGplot("curDataFigR"));
    }
    
    private void checkFiles() {
        if (data == null) {
            data = new ArrayList<Object>();
        }
        if (plots == null) {
            plots = new ArrayList<Object>();
        }
        if (extras == null) {
            extras = new ArrayList<Object>();
        }
    }

    /**
     * The user quits the soft --> we do a bit of cleaning
     */
    private void onQuit(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onQuit
        if (logger != null) {
            logger.removeLocker(lockerID);
            if (!logger.isLocked()) {
                logger.restoreSystem();
                logger.dispose();
                CommonClassesLight.logger = null;
            }
        }
        this.setVisible(false);
        if (CommonClassesLight.isImageJEmbedded && !ScientiFig.isInstanceAlreadyExisting()) {
            System.exit(0);
        }
    }//GEN-LAST:event_onQuit

    /**
     *
     * @return true if the user custom plot code should be used
     */
    public boolean isCustomCode() {
        return jRadioButton2.isSelected();
    }

    /*
     * Here are the guidelines for users that want to use their own custom code
     */
    private void customCodeOrSoftCode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customCodeOrSoftCode
        if (loading == true) {
            return;
        }
        if (isCustomCode()) {
            CommonClassesLight.Warning2(this, "You can enter your own custom R code, but there are"
                    + "\nseveral rules that you have to respect if you want things to work properly:"
                    + "\n\n-1/It only works for ggplot2/ggplot plots (library ggplot2) but not"
                    + "\nfor default R plots (plot, histogram, pairs, ...)"
                    + "\n\n-2/The last command of your script (not necessarily the last line )should"
                    + "\nbe your ggplot2 plot command (I will append to this line all the font settings automatically)"
                    + "\n\n-3/Please don't set fonts and text size or any other ggplot2 theme/opt commands"
                    + "\nor anything related (I'll do it for you)"
                    + "\n\n-4/Don't put any saving commands and don't set saving devices (png, svg, ...)"
                    + "\nas they might cause trouble"
                    + "\n\n-5/Make sure to install and load all the libraries required to run your code"
                    + "\n\n-6/The only that will be available will be the table called"
                    + "\n\"curDataFigR\" so all your commands should refer to it"
                    + "\n\n-7/You can execute several commands before your plot command but please end"
                    + "\nevery command with a semicolon, it is also possible to have commands"
                    + "\nspan over several line (the following example is valid, you can try it), for example:"
                    + "\n\nprint(\"hello R\");"
                    + "\nggplot (iris, aes (x = Sepal.Length, y = Sepal.Width, colour = Species))"
                    + "\n+ geom_line()"
                    + "\n+ geom_point()"
                    + "\n+ggtitle(\"petal/sepal\")"
                    + "\n+facet_grid(Species~.);"
                    + "\n\nPS: please don't put commentaries (line starting with a '#') in your code"
                    + "\n\n-8/Another valid example (that you can try to execute):"
                    + "\n\ncolnames(iris);"
                    + "\nggplot (iris, aes (x = Sepal.Length, y = Sepal.Width, colour = Species))+geom_line();"
                    + "\n\n-9/An invalid example (it is invalid because the last command is not a plot command)"
                    + "\n\nggplot (iris, aes (x = Sepal.Length, y = Sepal.Width, colour = Species))+geom_line();"
                    + "\ncolnames(iris);"
                    + "\n\n-10/An invalid example (it is invalid because commands are not separated with a semicolon)"
                    + "\n\ncolnames(iris)"
                    + "\nggplot (iris, aes (x = Sepal.Length, y = Sepal.Width, colour = Species))+geom_line()"
                    + "\n\n-11/An invalid example (it is invalid because even if the last command"
                    + "\nis a plot command it is not a ggplot2 command):"
                    + "\n\ncolnames(iris);"
                    + "\nplot(iris$Petal.Length, iris$Sepal.Length);");
        }
        updateGraph(evt);
    }//GEN-LAST:event_customCodeOrSoftCode

    private void launchScientiFig(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchScientiFig
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ScientiFig.getInstance().run(null);
            }
        });
    }//GEN-LAST:event_launchScientiFig

    /**
     * the user plans to quit we check that everything has been saved
     *
     * @param evt
     */
    private void closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closing
        if (warnForSave && !plotsListModel.isEmpty() && !jTextArea3.getText().trim().equals("")) {
            JLabel jl = new JLabel("<html><font color=\"#FF0000\">FiguR thinks you are about to quit without saving, do you want to continue ?<BR><BR>-Click 'Ok' to quit without saving<br>-Or click 'Cancel' to abort</font></html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{jl}, "Warning...", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }
        }
        if (!ScientiFig.isInstanceAlreadyExisting()) {
            try {
                rsession.close();
                CommonClassesLight.r = null;
            } catch (Exception e) {
            }
        }
        WindowManager.removeWindow(this);
        this.dispose();
    }//GEN-LAST:event_closing

    private void editText(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editText
        Object src = evt.getSource();
        if (src == jButton1) {
            RLabelEditor iopane = new RLabelEditor(rsession, xaxisLabel == null ? new RLabel(xAxisTitle.getText()) : xaxisLabel);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                xaxisLabel = iopane.getRlabel();
                xAxisTitle.setText(xaxisLabel.toRExpression());
                runAll(null);//just to update
            }
        } else if (src == jButton9) {
            RLabelEditor iopane = new RLabelEditor(rsession, titleLabel == null ? new RLabel(title.getText()) : titleLabel);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                titleLabel = iopane.getRlabel();
                title.setText(titleLabel.toRExpression());
                runAll(null);//just to update
            }
        } else if (src == jButton8) {
            RLabelEditor iopane = new RLabelEditor(rsession, legendLabel == null ? new RLabel(legendTitle.getText()) : legendLabel);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                legendLabel = iopane.getRlabel();
                legendTitle.setText(legendLabel.toRExpression());
                runAll(null);//just to update
            }
        } else if (src == jButton6) {
            RLabelEditor iopane = new RLabelEditor(rsession, yaxisLabel == null ? new RLabel(yAxisTitle.getText()) : yaxisLabel);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add/Edit text", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                yaxisLabel = iopane.getRlabel();
                yAxisTitle.setText(yaxisLabel.toRExpression());
                runAll(null);//just to update
            }
        }
    }//GEN-LAST:event_editText
    
    public void checkStatus() {
        if (rsession != null & rsession.isRserverRunning()) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            Rstatus.setText("R con opened");
        } else {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
            Rstatus.setText("R con closed");
        }
    }

    /**
     * Update the current plot
     *
     * @param src
     */
    public void updateGraph(Object src) {
        if (plots.isEmpty() && !isCustomCode() && src != jButton7) {
            imagePaneLight1.setImage(null);
            CommonClassesLight.Warning(this, "Please create at least one plot style by pressing on the green + button");
            return;
        }
        R_code = "";
        if (!plots.isEmpty()) {
            R_code = getFormattedGraph(false);
        }
        if (isCustomCode()) {
            R_code = jTextArea3.getText();
        }
        jTextArea2.setText(R_code);
        /**
         * we apply the theme without showing its code
         */
        updatePreview(getFormattedGraph(true));
    }

    /**
     * Counts the number of available fill colors and compares it to the number
     * of plot factors; if these two numbers don't match and the number of
     * colors < nb of fill factors then create new random colors for the missing
     * factors
     */
    public void checkNbOfFillFactors() {
        for (Object object : plots) {
            GeomPlot gp = ((GeomPlot) object);
            String aesFill = gp.getAesFill();
            if (aesFill != null) {
                if (aesFill.startsWith("as.factor(")) {
                    aesFill = CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutRightFisrt(aesFill, "("), ")");
                }
            }
            if (column_names.contains(aesFill)) {
                int nb;
                if (colnames_and_nb_of_factors.containsKey(aesFill)) {
                    nb = colnames_and_nb_of_factors.get(aesFill);
                } else {
                    nb = rsession.countFactors(aesFill);
                    colnames_and_nb_of_factors.put(aesFill, nb);
                }
                gp.setNb_of_Fill_colors(nb);
            } else {
                if (aesFill != null) {
                    gp.setNb_of_Fill_colors(1);
                } else {
                    gp.setNb_of_Fill_colors(0);
                }
            }
        }
    }

    /**
     * Counts the number of available Color colors and compares it to the number
     * of plot factors; if these two numbers don't match and the number of
     * colors < nb of Color factors then create new random colors for the
     * missing factors
     */
    public void checkNbOfColorFactors() {
        for (Object object : plots) {
            GeomPlot gp = ((GeomPlot) object);
            String aesColor = gp.getAesColor();
            if (aesColor != null) {
                if (aesColor.startsWith("as.factor(")) {
                    aesColor = CommonClassesLight.strCutLeftLast(CommonClassesLight.strCutRightFisrt(aesColor, "("), ")");
                }
            }
            if (column_names.contains(aesColor)) {
                int nb;
                if (colnames_and_nb_of_factors.containsKey(aesColor)) {
                    nb = colnames_and_nb_of_factors.get(aesColor);
                } else {
                    nb = rsession.countFactors(aesColor);
                    colnames_and_nb_of_factors.put(aesColor, nb);
                }
                gp.setNb_of_Color_colors(nb);
            } else {
                if (aesColor != null) {
                    gp.setNb_of_Color_colors(1);
                } else {
                    gp.setNb_of_Color_colors(0);
                }
            }
        }
    }

    /**
     *
     * @param include_fonts if true font settings will be added to the plot
     * @return a string that contains the R code of the plot
     */
    public String getFormattedGraph(boolean showTheme/*boolean include_fonts*/) {
        if (isCustomCode()) {
            String txt = jTextArea3.getText();
            if (theme != null) {
                txt += "\n+ " + theme.toString();
            }
            return txt;
        }
        R_code = "";
        String mainTitle = title.getText();
        String legendtitle = legendTitle.getText();
        String xaxistitle = xAxisTitle.getText();
        String yaxistitle = yAxisTitle.getText();
        ArrayList<Object> tmp = new ArrayList<Object>();
        for (Object object : extras) {
            if (object instanceof GGtitles) {
                tmp.add(object);
            }
        }
        extras.removeAll(tmp);
        extras.add(new GGtitles(mainTitle, xaxistitle, yaxistitle, legendtitle));
        /*
         * contains the main data
         */
        for (Object object : data) {
            R_code += object.toString() + "\n+ ";
        }
        /*
         * contains the plots
         */
        for (Object object : plots) {
            R_code += object.toString() + "\n+ ";
        }
        int total_nb_of_Color_colors = getTotalNbOfColorColors();
        int total_nb_of_Fill_colors = getTotalNbOfFillColors();
        if (total_nb_of_Color_colors == 0) {
            ArrayList<Object> scales_to_remove = new ArrayList<Object>();
            for (Object object : extras) {
                if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals("color")) {
                    scales_to_remove.add(object);
                }
            }
            extras.removeAll(scales_to_remove);
        }
        if (total_nb_of_Fill_colors == 0) {
            ArrayList<Object> scales_to_remove = new ArrayList<Object>();
            for (Object object : extras) {
                if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals("fill")) {
                    scales_to_remove.add(object);
                }
            }
            extras.removeAll(scales_to_remove);
        }
        /*
         * contains extras for the different plots such as stats, ....
         */
        for (Object object : extras) {
            if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals("color")) {
                ((ScaleColor) object).setNbOfColors(total_nb_of_Color_colors);
            }
            if (object instanceof ScaleColor && ((ScaleColor) object).getType().equals("fill")) {
                ((ScaleColor) object).setNbOfColors(total_nb_of_Fill_colors);
            }
            String val = object.toString();
            if (!val.trim().equals("")) {
                R_code += val + "\n+ ";
            }
        }
        if (jCheckBoxMenuItem1.isSelected()) {
            R_code += "coord_flip()\n+ ";
        }
        while (R_code.endsWith("\n+ ")) {
            R_code = R_code.substring(0, R_code.length() - 3);
        }
        if (!showXAxisTile.isSelected()) {
            R_code += "\n+ theme(axis.title.x = element_blank()) ";
        }
        if (!showYAxisTile.isSelected()) {
            R_code += "\n+ theme(axis.title.y = element_blank()) ";
        }
        /**
         * bug fix for theme not showing
         */
        if (showTheme && theme != null) {
            R_code += "\n+ " + theme.toString();
        }
        return R_code;
    }

    /**
     * Refresh the graph
     *
     * @param code code of the graph
     */
    public void updatePreview(String code) {
        /*
         * we have to give it a bit more space to avoid pbs with the scrollbars
         */
        //check it doesn't break custom code
        if (plots.isEmpty() && !isCustomCode()) {
            /**
             * nothing to plot, so we set the plot to null and empty all texts
             */
            imagePaneLight1.setImage(null);
//clearText(); //maybe not necessary ??? cause people have to retype it just put a delete button close to edit
        } else {
            BufferedImage bimg = rsession.getPreview(code, jScrollPane5.getViewportBorderBounds().width - 3, jScrollPane5.getViewportBorderBounds().height - 3);
            imagePaneLight1.setImage(bimg);
        }
    }

    /**
     * maybe delete text when there is no plot left or maybe keep it ?
     */
    public void clearText() {
//              column_names.clear();
//        alreadyFactorizableColumns.clear();
//        nb_of_factors.clear();
        plotsListModel.clear();
//        jTextArea3.setText("");
        jTextArea2.setText("");
        title.setText("");
        legendTitle.setText("");
        xAxisTitle.setText("");
        yAxisTitle.setText("");
//        xlsxFile.setText("");
//        titleLabel = null;
//        xaxisLabel = null;
//        yaxisLabel = null;
//        legendLabel = null;
    }

    /**
     *
     * @param order
     */
    public void orderLegend(String... order) {
        String R_Legend = "+ scale_fill_discrete(";
        R_Legend += RTools.createRArray("breaks", order);
        R_Legend += ")";
        R_code += R_Legend;
    }
    
    private void removeAllFacets() {
        /*
         * remove all facets objects
         */
        ArrayList<Object> tmp = new ArrayList<Object>();
        for (Object object : extras) {
            if (object instanceof Facets) {
                tmp.add(object);
            }
        }
        extras.removeAll(tmp);
    }

    /**
     *
     * @param labels
     */
    public void setLabel(String... labels) {
        String R_labels = "+ scale_x_discrete(";
        R_labels += RTools.createRArray("labels", labels);
        R_labels += ")";
        R_code += R_labels;
    }

    /**
     *
     * @param break_names
     * @param labels
     */
    public void setLabel(String[] break_names, String[] labels) {
        String R_labels = "+ scale_x_discrete(";
        R_labels += RTools.createRArray("breaks", break_names);
        R_labels += ", " + RTools.createRArray("labels", labels);
        R_labels += ")";
        R_code += R_labels;
    }

    /**
     *
     * @return a MyPlotVector that is a vectorial object that contains
     * everything needed to view/edit a graph in ScientiFig or in FiguR
     */
    public MyPlotVector.Double getGraph() {
        boolean use_non_custom_code = jRadioButton1.isSelected();
        return new MyPlotVector.Double(rsession, xlsxFile.getText(), getFormattedGraph(false), sheet_nb, textSeparator, textDelimtor, decimal, null, use_non_custom_code, theme);
    }
    
    private static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    @Override
    public void run(String arg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FiguR fR = FiguR.getInstance();
                Dimension size = fR.getSize();
                if (size.width < 1024 || size.height < 800) {
                    fR.setSize(1024, 800);
                    size = fR.getSize();
                }
                Dimension screen = getScreenSize();
                fR.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
                if (!fR.isVisible()) {
                    fR.setVisible(true);
                }
                rsession = fR.rsession;
                if (CommonClassesLight.r != null && CommonClassesLight.isRReady()) {
                    rsession = CommonClassesLight.r;
                }
            }
        });
    }

    /**
     * we redirect the serr and sout streams to the log window
     */
    private void redirectErrorStream() {
        if (CommonClassesLight.logger == null) {
            logger = new LogFrame(-1);
            CommonClassesLight.logger = logger;
        }
        logger = CommonClassesLight.logger;
        if (logger != null) {
            logger.setVisible(true);
            logger.toFront();
        }
    }

    /**
     * We load an existing .figur file
     *
     * @param name
     */
    private void openFile(String name) {
        loading = true;
        if (name == null) {
            loading = false;
            return;
        }
        name = CommonClassesLight.change_path_separators_to_system_ones(name);
        if (!new File(name).exists()) {
            loading = false;
            CommonClassesLight.Warning(this, "Please enter a valid .figur file");
            return;
        }
        this.filename = name;
        MyPlotVector.Double tmp = (MyPlotVector.Double) new Loader().loadObjectRaw(name);
        tmp.recreateStyledDoc();
        /*
         * we reload all the plot data (if available)
         */
        clearAll();
        this.data = tmp.data;
        this.plots = tmp.plots;
        this.extras = tmp.extras;
        this.titleLabel = tmp.getTitleLabel();
        this.xaxisLabel = tmp.getxAxisLabel();
        this.yaxisLabel = tmp.getyAxisLabel();
        this.legendLabel = tmp.getLegendLabel();
        
        jTextArea3.setText(tmp.Rcommand);
        
        if (!tmp.use_non_custom_code || data == null) {
            jRadioButton2.setSelected(true);
            jRadioButton1.setSelected(false);
        } else {
            jRadioButton1.setSelected(true);
            jRadioButton1.setSelected(false);
        }
        if (tmp.Rcommand.toLowerCase().contains("coord_flip()") && tmp.use_non_custom_code) {
            jCheckBoxMenuItem1.setSelected(true);
        } else {
            jCheckBoxMenuItem1.setSelected(false);
        }
        checkFiles();
        if (titleLabel != null) {
            title.setText(titleLabel.toRExpression());
        }
        if (xaxisLabel != null) {
            xAxisTitle.setText(xaxisLabel.toRExpression());
        }
        if (yaxisLabel != null) {
            yAxisTitle.setText(yaxisLabel.toRExpression());
        }
        if (legendLabel != null) {
            legendTitle.setText(legendLabel.toRExpression());
        }
        if (plots != null) {
            Collections.reverse(plots);
            for (Object plot : plots) {
                plotsListModel.addElement(plot);
            }
            Collections.reverse(plots);
        }
        if (new File(tmp.source_file_path).exists()) {
            FileOpeningParameters iopane = new FileOpeningParameters();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Options", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (iopane.isUseOriginalFile()) {
                    xlsxFile.setText(tmp.source_file_path);
                } else {
                    xlsxFile.setText(tmp.getTempFile());
                }
                loadData(xlsxFile.getText(), tmp.sheetnb, tmp.textSeparator, tmp.textDelimtor, tmp.decimal);
            } else {
                loading = false;
                return;
            }
        } else {
            xlsxFile.setText(tmp.getTempFile());
            loadData(xlsxFile.getText(), tmp.sheetnb, tmp.textSeparator, tmp.textDelimtor, tmp.decimal);
        }
        jButton2.doClick();
        loading = false;
    }

    /**
     * We create an R dataset from a .txt, .csv, .xls or .xlsx file
     *
     * @param name
     */
    private void openData(String name) {
        /*
         * this somehow crashes the Rserver under widows --> fix this later but inactivate for now
         */
        if (name == null || !(name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".csv") || name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx"))) {
            return;
        }
        DataOpener iopane = new DataOpener(false, name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx"));
        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Data source parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            checkRstatus();
            if (!new File(name).exists()) {
                CommonClassesLight.Warning(this, "Please enter a valid file");
                return;
            }
            /*
             * keep the next line like this as it fixes a Rserver crash upon DND of files on windows (if the path is not linux style Rserver crashes)
             */
            name = CommonClassesLight.change_path_separators_to_system_ones(name);
            xlsxFile.setText(name);
            sheet_nb = iopane.getSheetNumber();
            loadData(name, iopane.getSheetNumber(), iopane.getFieldSeparator(), iopane.getTextSeparator(), iopane.getDecimal());
            this.filename = name;
            this.lastPath = new File(name).getParent();
            enableOrDisableTextField();
        }
    }
    
    public void enableOrDisableTextField() {
        if (!xlsxFile.getText().trim().equals("")) {
            xlsxFile.setEnabled(true);
        } else {
            xlsxFile.setEnabled(false);
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FiguR fa = FiguR.getInstance();
                Dimension screen = getScreenSize();
                fa.setSize(1024, 800);
                Dimension size = fa.getSize();
                fa.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
                fa.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Captions;
    private javax.swing.JButton Rstatus;
    private javax.swing.JMenuItem ThemePreferences;
    private javax.swing.JMenuItem about;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JMenuItem capitalizeFirstLetter;
    private javax.swing.JMenuItem citations;
    private javax.swing.JButton colors;
    private javax.swing.JButton edit;
    private javax.swing.JMenuItem exportJPG;
    private javax.swing.JMenuItem exportPDF;
    private javax.swing.JMenuItem exportPNG;
    private javax.swing.JMenuItem exportSvg;
    private javax.swing.JMenuItem exportTIF;
    private javax.swing.JButton fills;
    private javax.swing.JMenu flipXY;
    private Dialogs.ImagePaneLight imagePaneLight1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField legendTitle;
    private javax.swing.JButton noColors;
    private javax.swing.JMenuItem openFigurFile;
    private javax.swing.JMenu openWithIJ;
    private javax.swing.JMenuItem quit;
    private javax.swing.JMenuItem remove_intercept;
    private javax.swing.JMenuItem resetTheme;
    private javax.swing.JMenuItem save;
    private javax.swing.JMenuItem saveAs;
    private javax.swing.JCheckBoxMenuItem showXAxisTile;
    private javax.swing.JCheckBoxMenuItem showYAxisTile;
    private javax.swing.JTextField title;
    private javax.swing.JTextField xAxisTitle;
    private javax.swing.JMenuItem xintercept;
    private javax.swing.JTextField xlsxFile;
    private javax.swing.JTextField yAxisTitle;
    private javax.swing.JMenuItem yintercept;
    // End of variables declaration//GEN-END:variables
}

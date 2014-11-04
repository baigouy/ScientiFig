//TODO allow spacing to be changed once panel is in a row --> need to check the type of object selected
//TODO implement clean split of IJ images by channel rather than by color in case users gave weird colors to their channels
/*
 * obfuscateSFnZIP --> compile then obfuscate and compress
 */
package GUIs;

import Checks.IllustratorOrInkscape;
import Dialogs.AnnotateCurrentImage;
import Dialogs.AuthorPane2;
import Dialogs.ChannelSelection;
import Dialogs.DoubleLayerPane;
import Dialogs.EmptyImage;
import Dialogs.FlipDialog;
import Dialogs.GlassPane;
import Dialogs.ImageEditorFrame;
import Dialogs.JournalParametersDialog;
import Dialogs.LineGraphicsDialog;
import Dialogs.MontageParameters;
import Dialogs.MyListLight;
import Dialogs.ROIpanelLight;
import Dialogs.SFPrefs;
import Dialogs.SelectABgColor;
import Dialogs.SideBarDialog;
import Dialogs.TextColor;
import Dialogs.VectorOutputParameters;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.ComplexShapeLight;
import MyShapes.Drawable;
import MyShapes.Exportable;
import MyShapes.Figure;
import MyShapes.JournalParameters;
import MyShapes.Montage;
import MyShapes.MyImage2D;
import MyShapes.MyImageVector;
import MyShapes.MyPlotVector;
import MyShapes.MyPoint2D;
import MyShapes.MyRectangle2D;
import MyShapes.MySpacer;
import MyShapes.Namable;
import MyShapes.PARoi;
import MyShapes.Row;
import MyShapes.SerializableBufferedImage2;
import MyShapes.TopBar;
import Tools.Converter;
import Tools.Help_;
import Tools.MultiThreadExecuter;
import Tools.PopulateJournalStyles;
import Commons.CommonClassesLight;
import Commons.DropTargetHandler;
import Commons.LimitedTextField;
import Commons.Loader;
import Commons.MyGraphics2D;
import Commons.MyWriter;
import Commons.Point3D;
import Commons.SaverLight;
import Dialogs.CapitalizeFirstLetterDialog;
import Dialogs.ListOfShortcuts;
import MyShapes.MagnificationChangeLoggable;
import R.RSession.MyRsessionLogger;
import Tools.ComponentBlinker;
import ij.Macro;
import ij.WindowManager;
import ij.io.FileInfo;
import ij.plugin.PlugIn;
import ij.plugin.frame.Recorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ActionMapUIResource;
import org.apache.batik.svggen.SVGGraphics2D;
import org.rosuda.REngine.REXPMismatchException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * ScientiFig_ is a tool to create format and or reformat figures and graphs for
 * scientific publications
 *
 * @author Benoit Aigouy
 */
public class ScientiFig_ extends javax.swing.JFrame implements PlugIn {

    /**
     * list of shortcuts
     */
    private static final LinkedHashMap<String, String> shortcuts = new LinkedHashMap<String, String>();

    static {
        /**
         * Update list of shortcuts for mac users
         */
        String CtrlButtonName = "Ctrl";
        if (CommonClassesLight.isMac()) {
            CtrlButtonName = "Cmd";
        }
        shortcuts.put(CtrlButtonName + " & N", "New File");
        shortcuts.put(CtrlButtonName + " & O", "Open Files");
        shortcuts.put(CtrlButtonName + " & I", "Import Files");
        shortcuts.put(CtrlButtonName + " & S", "Save");
        shortcuts.put(CtrlButtonName + " & Shift & S", "Save");
        shortcuts.put(CtrlButtonName + " & Q", "Quit");
        shortcuts.put(CtrlButtonName + " & +", "Zoom +");
        shortcuts.put(CtrlButtonName + " & -", "Zoom -");
        shortcuts.put(CtrlButtonName + " & Z", "Undo (undo/redo needs to be active)");
        shortcuts.put(CtrlButtonName + " & Y", "Redo (undo/redo needs to be active)");
        shortcuts.put("L", "Rotate image left");
        shortcuts.put("R", "Rotate image right");
        shortcuts.put(CtrlButtonName + " & J", "Create a new journal style");
        shortcuts.put(CtrlButtonName + " & E", "Edit selected journal style");
        shortcuts.put(CtrlButtonName + " & Left arrow", "Move selection left");
        shortcuts.put(CtrlButtonName + " & Right arrow", "Move selection right");
        shortcuts.put("- / Del", "Delete selection");
        shortcuts.put("+", "Add images to the selected panel");
        shortcuts.put("F1", "Help");
    }
    /**
     * Variables
     */
    String lockerID = "SF";
    /**
     * allows for ROIs to be copied bewteen images
     */
    public ArrayList<Object> copiedROIs = new ArrayList<Object>();
    public static Object cur_sel_image1;
    public static Object cur_sel_image2;
    public static Montage curPanel;
    public static Object current_row;
    public boolean inactivateListUpdate = false;
    public static boolean showHints = true;
    public boolean autoSwitchTopreview = true;
    boolean showHelpVideoForClickedButton = false;
    public static boolean propertiesLoaded = false;
    boolean block_update = false;
    static String lastExportFolder = null;
    static DropTarget dt;
    private boolean sizeLock = false;
    boolean warnForSave = false;
    public static boolean mustWarnOnQuit = true;
    public static boolean autoPositionScrollpanes = true;
    public static boolean useAllCores = true;
    public static int nbOfCPUs2Use = CommonClassesLight.getNumberOfCores();
    private static int import_J = 0;
    public static boolean rowAutoSameHeight = true;
    String letter = "A";
    public static final String currentyear = CommonClassesLight.getYear();
    public static String name_to_load;
    public boolean loading = false;
    public static final String version = "2.98";
    public static final String software_name = "ScientiFig";
    public static ArrayList<String> yf5m_files = new ArrayList<String>();
    private int PanelCounter = 1;
    DefaultListModel tableListModel = new DefaultListModel();
    DefaultListModel tableContentListModel = new DefaultListModel();
    DefaultListModel imagesInFigureModel = new DefaultListModel();
    DefaultListModel figureListModel = new DefaultListModel();
    DefaultListModel RowContentListModel = new DefaultListModel();
    private static String lastSaveName;
    private static String lastOpenName;
    JournalParameters defaultStyle;
    HashMap<Integer, Montage> panels = new HashMap<Integer, Montage>();
    double resolution = 72;
    public static HashMap<String, SerializableBufferedImage2> imported_from_J = new HashMap<String, SerializableBufferedImage2>();
    HashMap<String, Double> imported_from_pixel_size = new HashMap<String, Double>();
    public static boolean isUndoRedoAllowed = false;
    public static int MAX_UNDOS = 10;
    ArrayDeque<String> undos_and_redos = new ArrayDeque<String>(MAX_UNDOS);
    public static int cur_pos = -1;
    public static boolean remove_when_added = true;
    boolean dontChangeCropSize = false;
    boolean dontChangeAngle = false;
    boolean warned_once = false;
    private static String macro = null;
    private static String save = null;
    static boolean alreadyExists = false;
    private static String ScientiFigMacroName = null;
    public static boolean useNativeDialog = false;
    public static boolean showHelpInfoWindow = true;
    ScheduledExecutorService refreshMemory = Executors.newSingleThreadScheduledExecutor();
    private static final ArrayList<String> exts = new ArrayList<String>();

    static {
        exts.add(".figur");
        exts.add(".jpg");
        exts.add(".jpeg");
        exts.add(".png");
        exts.add(".bmp");
        exts.add(".gif");
        exts.add(".tga");
        exts.add(".tif");
        exts.add(".tiff");
        exts.add(".svg");
    }
    Object current_selected_shapeBlock;
    Object current_selected_shapeRow;
    /**
     * we add a glasspane to give a few infos about the soft
     */
    GlassPane glasspane = new GlassPane();
    ROIpanelLight r2 = new ROIpanelLight() {
        @Override
        public void selectionChanged(Object selection) {
            current_selected_shapeRow = this.getSelectedShape();
            selectionUpdateRow();
            /*
             * we set options according to the type of sdelection
             */
        }
    };
    /**
     * we now initialize a component blinker
     */
    private static ComponentBlinker blinker;
    private final static ArrayList<Double> JournalPageWidth = new ArrayList<Double>();

    /**
     * initial size parameters
     */
    static {
        JournalPageWidth.add(21.);
        JournalPageWidth.add(16.);
        JournalPageWidth.add(10.);
    }
    Montage selected_panel = null;
    private static ArrayList<Object> rows = new ArrayList<Object>();
    public static ImageEditorFrame ief = new ImageEditorFrame();
    /*
     * ScientiFig output formats
     */
    public static final int FORMAT_SVG = 0;
    public static final int FORMAT_PNG = 1;
    public static final int FORMAT_JPEG = 2;
    public static final int FORMAT_TIFF = 5;
    public static final int FORMAT_IJ = 7;
    public static final int FORMAT_FIGURE_ASSISTANT = 12;
    static final int currentResolution = 72;
    /*
     * A log window for java errors
     */
    LogFrame logger;

    public static PopulateJournalStyles styles = getStyles();
    ROIpanelLight r1 = new ROIpanelLight() {
        @Override
        public void selectionChanged(Object selection) {
            current_selected_shapeBlock = this.getSelectedShape();
            inactivateListUpdate = true;
            selectionUpdate();
            showAppropriateOptions(tableContentList);
            inactivateListUpdate = false;
        }
    };

    /**
     * Creates new form Figure_Assistants
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public ScientiFig_() {
        if (isInstanceAlreadyExisting()) {
            this.dispose();
            return;
        }
        initComponents();
        if (CommonClassesLight.isMac()) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            ImageEditorFrame.setDefaultLookAndFeelDecorated(true);
            /*
             * does not work but dunno why
             */
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ScientiFig");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
        }
        if (!ScientiFig_.propertiesLoaded) {
            load_properties(this.getClass());
        }
        CommonClassesLight.setMaxNbOfProcessors(nbOfCPUs2Use);
        tableList.setModel(tableListModel);
        tableContentList.setModel(tableContentListModel);
        imagesInFigureList.setModel(imagesInFigureModel);
        figureList.setModel(figureListModel);
        RowContentList.setModel(RowContentListModel);
        styles.reloadStyles(journalCombo);
        ief.setLocationRelativeTo(this);
        journalColumnsCombo.setSelectedIndex(-1);

        if (CommonClassesLight.ij == null) {
            CommonClassesLight.ij = ij.IJ.getInstance();
            if (CommonClassesLight.ij != null) {
                CommonClassesLight.isImageJEmbedded = false;
            }
        }

        /**
         * we now force the logger to appear serr and sout are restored upon
         * closing the software
         */
        redirectErrorStream();
        if (logger != null) {
            logger.addLocker(lockerID);
        }

        speedUpScrollbars();
        pack();

        WindowManager.addWindow(this);
        CommonClassesLight.GUI = this;
        doubleLayerPane1.ROIS.setDraggable(false);
        doubleLayerPane2.ROIS.setDraggable(false);
        doubleLayerPane3.ROIS.setDraggable(false);
        doubleLayerPane3.setSelectable(false);
        doubleLayerPane1.ROIS.setROIPanelActive(true);
        doubleLayerPane2.ROIS.setROIPanelActive(true);
        doubleLayerPane3.ROIS.setROIPanelActive(true);
        if (CommonClassesLight.ij == null) {
            jMenu6.setVisible(false);
        }
        /*
         * we add a drop listener
         */
        dt = new DropTarget(this, new DropTargetHandler() {
            @Override
            public void doSomethingWithTheFile(String data) {
                myList1.acceptdata(data);
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (loading) {
                    return;
                }
                loading = true;
                try {
                    super.drop(dtde);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
                myList1.addAllToList();
                loading = false;
            }
        });
        memoryRefreshTimer();
        warnForSave = false;
        showAppropriateOptions(null);
        myList1.list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        JRootPane rtPane = this.getRootPane();
        rtPane.setGlassPane(glasspane);
        glasspane.setVisible(true);
        glasspane.setOpaque(false);
        this.setVisible(true);
        updateGlassPane();
        /*
         * we make sure that when the user presses Enter a changeEvent is sent to the spinner
         */
        overrideListenersForSpinners();
        /**
         * we set up a new blinker
         */
        /**
         * the pb is here --> see why
         */
        blinker = new ComponentBlinker(this, glasspane);
        /*
         * try to override back the FIJI icon override
         */
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("Icons/icon_029.png"));
        this.setIconImage(icon.getImage());
        ief.setIconImage(icon.getImage());
        r2.setMultiClickAllowsForDeeperSelectionOfElements(true);
        addAccelerators();
    }

    private void addAccelerators() {
        /*
         * We add shortcuts to existing buttons
         */
        /*
         * ctrl +/zoom +
         */
        InputMap im = new ComponentInputMap(zoomPlus);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "zoomPlus");
        ActionMap am = new ActionMapUIResource();
        am.put("zoomPlus", new AbstractAction("zoomPlus") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(zoomPlus);
            }
        });
        SwingUtilities.replaceUIActionMap(zoomPlus, am);
        SwingUtilities.replaceUIInputMap(zoomPlus, JComponent.WHEN_IN_FOCUSED_WINDOW, im);

        /*
         * ctrl -/zoom -
         */
        im = new ComponentInputMap(zoomMinus);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "zoomMinus");
        am = new ActionMapUIResource();
        am.put("zoomMinus", new AbstractAction("zoomMinus") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(zoomMinus);
            }
        });
        SwingUtilities.replaceUIActionMap(zoomMinus, am);
        SwingUtilities.replaceUIInputMap(zoomMinus, JComponent.WHEN_IN_FOCUSED_WINDOW, im);

        /*
         * L --> rotate left
         */
        im = new ComponentInputMap(rotateLeft);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "rotateLeft");
        am = new ActionMapUIResource();
        am.put("rotateLeft", new AbstractAction("rotateLeft") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(rotateLeft);
            }
        });
        SwingUtilities.replaceUIActionMap(rotateLeft, am);
        SwingUtilities.replaceUIInputMap(rotateLeft, JComponent.WHEN_IN_FOCUSED_WINDOW, im);

        /*
         * R --> rotate right
         */
        im = new ComponentInputMap(rotateRight);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotateRight");
        am = new ActionMapUIResource();
        am.put("rotateRight", new AbstractAction("rotateRight") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(rotateRight);
            }
        });
        SwingUtilities.replaceUIActionMap(rotateRight, am);
        SwingUtilities.replaceUIInputMap(rotateRight, JComponent.WHEN_IN_FOCUSED_WINDOW, im);

        /*
         * shortcut to add images
         */
        im = new ComponentInputMap(addSelectedImagesInCurPanel);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "add");
        am = new ActionMapUIResource();
        am.put("add", new AbstractAction("add") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(addSelectedImagesInCurPanel);
            }
        });
        SwingUtilities.replaceUIActionMap(addSelectedImagesInCurPanel, am);
        SwingUtilities.replaceUIInputMap(addSelectedImagesInCurPanel, JComponent.WHEN_IN_FOCUSED_WINDOW, im);
        /*
         * now we add shortcuts to the GUI
         */
        im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "substract");
        rootPane.getActionMap().put("substract", new AbstractAction("substract") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(deleteButton);
            }
        });
        im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "minus");
        rootPane.getActionMap().put("minus", new AbstractAction("minus") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(deleteButton);
            }
        });
        im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "del");
        rootPane.getActionMap().put("del", new AbstractAction("del") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(deleteButton);
            }
        });
        im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Ctrl+LEFT");
        rootPane.getActionMap().put("Ctrl+LEFT", new AbstractAction("Ctrl+LEFT") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(moveLeft);
            }
        });
        im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Ctrl+RIGHT");
        rootPane.getActionMap().put("Ctrl+RIGHT", new AbstractAction("Ctrl+RIGHT") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAllnow(moveRight);
            }
        });
    }

    /**
     * We refresh memory usage every 'memoryRefreshSpeed' milliseconds
     */
    private void memoryRefreshTimer() {
        int memoryRefreshSpeed = 2000;
        refreshMemory.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (jProgressBar1 != null && jProgressBar1.isVisible() && !jProgressBar1.isIndeterminate()) {
                    Runtime runtime = Runtime.getRuntime();
                    long allocatedMemory = runtime.totalMemory();
                    long max = Math.max(runtime.totalMemory(), runtime.maxMemory());
                    long freeMemory = runtime.freeMemory();
                    jProgressBar1.setMinimum(0);
                    jProgressBar1.setMaximum(100);
                    int used_memory_in_percent = (int) (((double) (allocatedMemory - freeMemory) / (double) (max)) * 100.);
                    jProgressBar1.setValue(used_memory_in_percent);
                    jProgressBar1.setStringPainted(true);
                    jProgressBar1.setString("Mem used: " + used_memory_in_percent + "%");
                    try {
                        System.gc();
                    } catch (Exception e) {
                    }
                }
            }
        }, 0, memoryRefreshSpeed, TimeUnit.MILLISECONDS);
    }

    /**
     * only useful for debug otherwise buttons are missing
     */
    public void addAllPanelsToOptions() {
        optionsPanel.add(imageSizePanel);
        optionsPanel.add(jPanel4);
        optionsPanel.add(jPanel11);
        optionsPanel.add(jPanel7);
        optionsPanel.add(jPanel10);
        optionsPanel.add(jPanel6);
        optionsPanel.add(jPanel9);
        optionsPanel.add(jPanel12);
        optionsPanel.add(jPanel16);
        optionsPanel.add(jPanel1666);
        optionsPanel.add(jPanel1);
        optionsPanel.add(rotateNFlip);
    }

    /**
     * We use this glasspane to drawAndFill indications/guidelines for the
     * software on the screen.
     */
    private void updateGlassPane() {
        /*
         * We drawAndFill some hints/guidelines in the glasspane but only if lists are empty and if the user wants it.
         */
        if (!isThereAnythingInTheLists() && showHints) {
            ArrayList<Object> annotations = new ArrayList<Object>();
            /*
             * annotate image list
             */
            Rectangle2D pos_of_list = ComponentBlinker.getComponentPosition(myList1);
            MyRectangle2D pos = new MyRectangle2D.Double(pos_of_list);
            pos.setFillColor(0xFF0000);
            pos.setDrawColor(0xFF0000);
            pos.setFillOpacity(0.3f);
            annotations.add(pos);

            /*
             * annotate add row to figure
             */
            Rectangle2D pos_of_help = ComponentBlinker.getComponentPosition(buttonHelp);
            MyRectangle2D pos5 = new MyRectangle2D.Double(pos_of_help);
            pos5.setFillColor(0xFF0000);
            pos5.setDrawColor(0xFF0000);
            pos5.setFillOpacity(0.3f);
            annotations.add(pos5);
            MyPoint2D.Double DND_text5 = new MyPoint2D.Double(pos_of_help.getCenterX(), pos_of_help.getCenterY());
            DND_text5.setText(new ColoredTextPaneSerializable("<-- press here to view a video demo of buttons/spinners/combos functionalities", Color.RED, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text5.translate(275, 0);
            annotations.add(DND_text5);
            /*
             * annotate main window to give general infos
             */
            Rectangle2D pos_of_main_window = ComponentBlinker.getComponentPosition(jTabbedPane1);
            MyPoint2D.Double DND_text6 = new MyPoint2D.Double(pos_of_main_window.getCenterX(), pos_of_main_window.getCenterY());
            DND_text6.setText(new ColoredTextPaneSerializable("Hints will disappear automatically as soon as you create a panel or select an image in the 'image list'\n \nTo inactivate hints click on Edit>Preferences and untick 'Show Hints'\n \nTo view additional Help videos click on Help/Licenses/Citations>Help Content", Color.DARK_GRAY, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text6);
            /*
             * annotate main window to give general infos
             */
            Rectangle2D pos_of_options = ComponentBlinker.getComponentPosition(optionsPanel);
            MyPoint2D.Double DND_text7 = new MyPoint2D.Double(pos_of_options.getCenterX(), pos_of_options.getCenterY());
            DND_text7.setText(new ColoredTextPaneSerializable("The 'option' panel gives access to functionalities adapted to your selection.\nClick on one of the lists (labeled in blue) to view new options.", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text7);
            /*
             * annotate all lists of the tool
             */
            Rectangle2D pos_of_myList1 = ComponentBlinker.getComponentPosition(myList1);
            MyPoint2D.Double DND_text8bis = new MyPoint2D.Double(pos_of_myList1.getCenterX(), pos_of_myList1.getCenterY());
            DND_text8bis.setText(new ColoredTextPaneSerializable("List 1", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text8bis.translate(0, 20);
            annotations.add(DND_text8bis);

            Rectangle2D pos_of_tableList = ComponentBlinker.getComponentPosition(tableList.getParent());
            MyPoint2D.Double DND_text8 = new MyPoint2D.Double(pos_of_tableList.getCenterX(), pos_of_tableList.getCenterY());
            DND_text8.setText(new ColoredTextPaneSerializable("List 2", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text8);

            Rectangle2D pos_of_tableContentList = ComponentBlinker.getComponentPosition(tableContentList.getParent());
            MyPoint2D.Double DND_text9 = new MyPoint2D.Double(pos_of_tableContentList.getCenterX(), pos_of_tableContentList.getCenterY());
            DND_text9.setText(new ColoredTextPaneSerializable("List 3", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text9);

            Rectangle2D pos_of_figureList = ComponentBlinker.getComponentPosition(figureList.getParent());
            MyPoint2D.Double DND_text10 = new MyPoint2D.Double(pos_of_figureList.getCenterX(), pos_of_figureList.getCenterY());
            DND_text10.setText(new ColoredTextPaneSerializable("List 4", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text10);

            Rectangle2D pos_of_RowContentList = ComponentBlinker.getComponentPosition(RowContentList.getParent());
            MyPoint2D.Double DND_text11 = new MyPoint2D.Double(pos_of_RowContentList.getCenterX(), pos_of_RowContentList.getCenterY());
            DND_text11.setText(new ColoredTextPaneSerializable("List 5", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text11);

            Rectangle2D pos_of_imagesInFigureList = ComponentBlinker.getComponentPosition(imagesInFigureList.getParent());
            MyPoint2D.Double DND_text12 = new MyPoint2D.Double(pos_of_imagesInFigureList.getCenterX(), pos_of_imagesInFigureList.getCenterY());
            DND_text12.setText(new ColoredTextPaneSerializable("List 6", Color.BLUE, new Font("Monospaced", Font.PLAIN, 12)));
            annotations.add(DND_text12);

            MyPoint2D.Double DND_text = new MyPoint2D.Double(pos_of_list.getCenterX(), pos_of_list.getCenterY());
            /*
             * I now use the monospaced font because this ugly font is present and behaves the same on all systems
             */
            DND_text.setText(new ColoredTextPaneSerializable("<-- 1/ Drag and drop your images here", Color.BLACK, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text.translate(90, 0);
            annotations.add(DND_text);
            /*
             * annotate Panel creation
             */
            Rectangle2D pos_of_Auto = ComponentBlinker.getComponentPosition(createPanelAutomatically);
            MyRectangle2D pos2 = new MyRectangle2D.Double(pos_of_Auto);
            pos2.setFillColor(0xFF0000);
            pos2.setDrawColor(0xFF0000);
            pos2.setFillOpacity(0.3f);
            annotations.add(pos2);
            MyPoint2D.Double DND_text2 = new MyPoint2D.Double(pos_of_Auto.getCenterX(), pos_of_Auto.getCenterY());
            DND_text2.setText(new ColoredTextPaneSerializable("<-- 2/ press here to create panels automatically", Color.BLACK, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text2.translate(170, 0);
            annotations.add(DND_text2);
            /*
             * annotate add row to figure
             */
            Rectangle2D pos_of_add_row = ComponentBlinker.getComponentPosition(addSelectedPanelToNewRow);
            MyRectangle2D pos3 = new MyRectangle2D.Double(pos_of_add_row);
            pos3.setFillColor(0xFF0000);
            pos3.setDrawColor(0xFF0000);
            pos3.setFillOpacity(0.3f);
            annotations.add(pos3);
            /*
             * annotate add col to row
             */
            Rectangle2D pos_of_add_col = ComponentBlinker.getComponentPosition(addSelectedPanelToSelectedRow);
            MyRectangle2D pos4 = new MyRectangle2D.Double(pos_of_add_col);
            pos4.setFillColor(0xFF0000);
            pos4.setDrawColor(0xFF0000);
            pos4.setFillOpacity(0.3f);
            annotations.add(pos4);
            MyPoint2D.Double DND_text4 = new MyPoint2D.Double(pos_of_add_col.getCenterX(), pos_of_add_col.getCenterY());
            DND_text4.setText(new ColoredTextPaneSerializable("<-- 4/ press here to add a column to an existing row", Color.BLACK, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text4.translate(186, 8);
            annotations.add(DND_text4);
            /*
             * we put the text here so that it is on top
             */
            MyPoint2D.Double DND_text3 = new MyPoint2D.Double(pos_of_add_row.getCenterX(), pos_of_add_row.getCenterY());
            DND_text3.setText(new ColoredTextPaneSerializable("<-- 3/ press here to add a row to your figure", Color.BLACK, new Font("Monospaced", Font.PLAIN, 12)));
            DND_text3.translate(164, -8);
            annotations.add(DND_text3);
            /*
             * add all objects to the glasspane
             */
            glasspane.setObjectsToDraw(annotations);
            /*
             * refresh display
             */
            glasspane.repaint();
        } else {
            /*
             * nothing to drawAndFill in the glasspane
             */
            glasspane.clearPane();
            glasspane.repaint();
            repaint();
        }
    }

    /**
     *
     * @return A list of journal styles
     */
    public static PopulateJournalStyles getStyles() {
        return new PopulateJournalStyles();
    }

    /**
     * updates image selection upon mouse click on the vectorial drawing panel
     */
    public void selectionUpdate() {
        if (current_selected_shapeBlock != null) {
            if (current_selected_shapeBlock instanceof MyImage2D) {
                if (curPanel != null) {
                    tableContentList.setSelectedIndex(new ArrayList<Object>(((Montage) curPanel).pos_n_shapes).indexOf(current_selected_shapeBlock));
                    cur_sel_image1 = current_selected_shapeBlock;
                    ief.setCurSel(cur_sel_image1);
                } else {
                    ief.setCurSel(null);
                }
            }
            if (current_selected_shapeBlock instanceof ComplexShapeLight) {
                /*
                 * necessary for crops, etc...
                 */
                cur_sel_image1 = current_selected_shapeBlock;
                ief.setCurSel(cur_sel_image1);
                HashSet<Object> shapes = ((ComplexShapeLight) current_selected_shapeBlock).getGroup();
                int[] indices = new int[shapes.size()];
                int i = 0;
                for (Object object : shapes) {
                    if (object instanceof MyImage2D) {
                        if (curPanel != null) {
                            indices[i] = (new ArrayList<Object>(((Montage) curPanel).pos_n_shapes).indexOf(object));
                        }
                    }
                    i++;
                }
                tableContentList.setSelectedIndices(indices);
            }
        }
    }

    /**
     * updates row selection upon mouse click on the vectorial drawing panel
     */
    public void selectionUpdateRow() {
        if (current_selected_shapeRow != null) {
            if (current_selected_shapeRow instanceof Row) {
                figureList.setSelectedIndex(rows.indexOf(current_selected_shapeRow));
                showAppropriateOptions(figureList);
            }
            /**
             * yurp this code is totally ugly --> I really need to recode this
             * more wisely when I have time
             */
            if (current_selected_shapeRow instanceof Montage) {
                int pos = ((Row) rows.get(figureList.getSelectedIndex())).blocks.indexOf(current_selected_shapeRow);
                RowContentList.setSelectedIndex(pos);
                showAppropriateOptions(RowContentList);
            }
            if (current_selected_shapeRow instanceof MyImage2D) {
                int pos = new ArrayList<Object>(((Montage) (((Row) rows.get(figureList.getSelectedIndex())).blocks.get(RowContentList.getSelectedIndex()))).pos_n_shapes).indexOf(current_selected_shapeRow);
                imagesInFigureList.setSelectedIndex(pos);
                showAppropriateOptions(imagesInFigureList);
            }
            if (current_selected_shapeRow instanceof ComplexShapeLight) {
                boolean is_row = false;
                HashSet<Object> shapes = ((ComplexShapeLight) current_selected_shapeRow).getGroup();
                int[] indices = new int[shapes.size()];
                int i = 0;
                for (Object object : shapes) {
                    if (object instanceof Row) {
                        indices[i] = (rows.indexOf(object));
                        is_row = true;
                    }
                    i++;
                }
                if (is_row) {
                    /*
                     * bug fix for multiple selection
                     */
                    loading = true;
                    figureList.setSelectedIndices(indices);
                    loading = false;
                    figureListValueChanged(new ListSelectionEvent(figureList, i, i, false));
                }
            }
        }
    }

    /**
     * we override the spinner listeners to handle VK_ENTER entries better than
     * java does. When enter is pressed we fake a changevent to force the
     * execution.
     */
    private void overrideListenersForSpinners() {
        /*
         * sizeInPx size in px
         */
        if (true) {
            ((JSpinner.DefaultEditor) sizeInPx.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        sizeInPxsizeChanged(new ChangeEvent(sizeInPx));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * sizeInCM size in cm
         */
        if (true) {
            ((JSpinner.DefaultEditor) sizeInCM.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        cmSizeChanged(new ChangeEvent(sizeInCM));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * changeSpaceBetweenImageInBlock space between images in a panel
         */
        if (true) {
            ((JSpinner.DefaultEditor) changeSpaceBetweenImageInPanel.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        changeSpaceBetweenImageInPanelspaceBetweenImagesChanged(new ChangeEvent(changeSpaceBetweenImageInPanel));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * changeSpaceBetweenRows space between panels in a row and between nbRows
         */
        if (true) {
            ((JSpinner.DefaultEditor) changeSpaceBetweenRows.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        changeSpaceBetweenRowsspaceBetweenImagesChanged(new ChangeEvent(changeSpaceBetweenRows));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * cropLeftSpinner crop left
         */
        if (true) {
            ((JSpinner.DefaultEditor) cropLeftSpinner.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        cropChanged(new ChangeEvent(cropLeftSpinner));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * cropRightSpinner crop right
         */
        if (true) {
            ((JSpinner.DefaultEditor) cropRightSpinner.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        cropChanged(new ChangeEvent(cropRightSpinner));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * cropUpSpinner crop up
         */
        if (true) {
            ((JSpinner.DefaultEditor) cropUpSpinner.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        cropChanged(new ChangeEvent(cropUpSpinner));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * cropDownSpinner crop down
         */
        if (true) {
            ((JSpinner.DefaultEditor) cropDownSpinner.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        cropChanged(new ChangeEvent(cropDownSpinner));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
        /*
         * rotateSpinner rotate
         */
        if (true) {
            ((JSpinner.DefaultEditor) rotateSpinner.getEditor()).getTextField().addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        angleChanged(new ChangeEvent(rotateSpinner));
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
        }
    }

    /**
     * sout is written to the left part of the logger and serr to the right part
     * of it
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
     *
     * @return true if ScientiFig is already running
     */
    public static boolean isInstanceAlreadyExisting() {
        Frame[] data = WindowManager.getNonImageWindows();
        for (Frame frame : data) {
            if (frame instanceof ScientiFig_) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the previous instance of SF if it exists or null otherwise
     */
    public static ScientiFig_ getPreviousInstance() {
        Frame[] data = WindowManager.getNonImageWindows();
        for (Frame frame : data) {
            if (frame instanceof ScientiFig_) {
                ((ScientiFig_) frame).setVisible(true);
                return ((ScientiFig_) frame);
            }
        }
        return null;
    }

    /**
     * Checks whether R is running (green light or not running red light) and
     * displays instructions on how one can connect ScientiFig to R
     */
    public void checkRstatus() {
        if (warned_once) {
            return;
        }
        checkStatus();
        if (CommonClassesLight.r != null && CommonClassesLight.r.isRserverRunning()) {
            checkStatus();
            return;
        }
        getRserver();
        boolean isconnected = CommonClassesLight.isRReady();
        if (isconnected) {
            checkStatus();
        } else {
            /*
             * try reconnect or print the guidelines
             */
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
            blinkAnything(Rstatus);
            CommonClassesLight.Warning2(this, MyRsessionLogger.generateRInstallationText());
            warned_once = true;
        }
    }

    /*
     * reloads ScientiFig/FiguR preferences
     */
    public static void load_properties(Class c) {
        propertiesLoaded = true;
        FileInputStream in = null;
        try {
            String folder_of_jar = CommonClassesLight.getApplicationFolder(c);
            if (!new File(folder_of_jar + "/ScientiFigPrefs.txt").exists()) {
                allowUndoRedo();
                return;
            }
            in = new FileInputStream(folder_of_jar + "/ScientiFigPrefs.txt");
            Properties p = new Properties();
            /*
             * prefs are saved in xml format
             */
            p.loadFromXML(in);
            /*
             * load soft parameters
             */
            try {
                if (p.containsKey("allow undo/redo")) {
                    isUndoRedoAllowed = CommonClassesLight.String2Boolean(p.getProperty("allow undo/redo"));
                }
                if (p.containsKey("MaxUndosRedos")) {
                    MAX_UNDOS = CommonClassesLight.String2Int(p.getProperty("MaxUndosRedos"));
                }
                if (p.containsKey("Remove Images form the List When They Are Added To A Panel")) {
                    remove_when_added = CommonClassesLight.String2Boolean(p.getProperty("Remove Images form the List When They Are Added To A Panel"));
                }
                if (p.containsKey("Warn for save on quit")) {
                    mustWarnOnQuit = CommonClassesLight.String2Boolean(p.getProperty("Warn for save on quit"));
                }
                if (p.containsKey("Auto scroll")) {
                    autoPositionScrollpanes = CommonClassesLight.String2Boolean(p.getProperty("Auto scroll"));
                }
                if (p.containsKey("Show hints")) {
                    showHints = CommonClassesLight.String2Boolean(p.getProperty("Show hints"));
                }
                if (p.containsKey("Use Native Dialogs When Apllicable")) {
                    useNativeDialog = CommonClassesLight.String2Boolean(p.getProperty("Use Native Dialogs When Apllicable"));
                }
                if (p.containsKey("Show help info window")) {
                    showHelpInfoWindow = CommonClassesLight.String2Boolean(p.getProperty("Show help info window"));
                }
                if (p.containsKey("Max number of processors to use")) {
                    nbOfCPUs2Use = CommonClassesLight.String2Int(p.getProperty("Max number of processors to use"));
                    nbOfCPUs2Use = CommonClassesLight.setMaxNbOfProcessors(nbOfCPUs2Use);
                }
                if (p.containsKey("Force usage of all cores")) {
                    useAllCores = CommonClassesLight.String2Boolean(p.getProperty("Force usage of all cores"));
                    if (useAllCores) {
                        nbOfCPUs2Use = CommonClassesLight.getNumberOfCores();
                        CommonClassesLight.setMaxNbOfProcessors(nbOfCPUs2Use);
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                }
            }
        }
        allowUndoRedo();
    }

    /**
     * Enable undo and redo buttons in the GUI
     */
    public static void allowUndoRedo() {
        if (undo != null) {
            undo.setVisible(isUndoRedoAllowed);
            redo.setVisible(isUndoRedoAllowed);
            jMenuItem14.setEnabled(isUndoRedoAllowed);
            jMenuItem21.setEnabled(isUndoRedoAllowed);
        }
    }

    /*
     * Saves ScientiFig preferences
     */
    private void save_properties() {
        FileOutputStream out = null;
        try {
            String folder_of_jar = CommonClassesLight.getApplicationFolder(this.getClass());
            Properties p = new Properties();
            p.setProperty("allow undo/redo", isUndoRedoAllowed + "");
            p.setProperty("MaxUndosRedos", MAX_UNDOS + "");
            p.setProperty("Remove Images form the List When They Are Added To A Panel", remove_when_added + "");
            p.setProperty("Warn for save on quit", mustWarnOnQuit + "");
            p.setProperty("Auto scroll", autoPositionScrollpanes + "");
            p.setProperty("Show hints", showHints + "");
            p.setProperty("Use Native Dialogs When Apllicable", useNativeDialog + "");
            p.setProperty("Show help info window", showHelpInfoWindow + "");
            p.setProperty("Force usage of all cores", useAllCores + "");
            p.setProperty("Max number of processors to use", nbOfCPUs2Use + "");
            out = new FileOutputStream(folder_of_jar + "/ScientiFigPrefs.txt");
            p.storeToXML(out, "last update " + new Date().toString());//put date
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /*
     * speeds up scrollbars that would otherwise be terribly slow
     */
    private void speedUpScrollbars() {
        CommonClassesLight.speedUpJScrollpane(jScrollPane13);
        CommonClassesLight.speedUpJScrollpane(jScrollPane16);
        CommonClassesLight.speedUpJScrollpane(jScrollPane17);
        CommonClassesLight.speedUpJScrollpane(jScrollPane8);
        CommonClassesLight.speedUpJScrollpane(jScrollPane12);
    }

    /**
     * Updates spacing between images
     */
    public void updateSpaceInPanel() {
        if (curPanel == null) {
            if (tableList.getSelectedIndex() != -1) {
                int real_pos = getRealPos();
                curPanel = panels.get(real_pos);
            }
        }
        if (curPanel != null) {
            curPanel.setSpace_between_images(getSpaceBetweenImages()); //--> will also be used for internal images
            curPanel.updateTable();
            /*
             * we force a panel size update when space is changed
             */
            curPanel.setToWidth(getSizeInPx());
            curPanel.setWidth_in_cm(getSizeInCm());
            curPanel.setWidth_in_px(getSizeInPx());
            curPanel.setResolution(resolution);
            /*
             * bug fix for position drift when images contained are not
             */
            curPanel.setFirstCorner();
            updateTable();
            createBackup();
        } else {
            blinkAnything(tableList.getParent());
            CommonClassesLight.Warning(this, "Please select a panel in the 'Panels' List");
        }
    }

    /**
     *
     * @return the space between images
     */
    public int getSpaceBetweenImages() {
        return ((Integer) changeSpaceBetweenImageInPanel.getValue());
    }

    /**
     * Sets the space between images
     *
     * @param space
     */
    public void setSpaceBetweenImages(double space) {
        changeSpaceBetweenImageInPanel.setValue((int) space);
    }

    /**
     *
     * @param nb_of_images number of images in a montage
     * @return a dimension that contains the best number of columns and nbRows a
     * montage should have (we force the shape to be rectangular, but this
     * rectangle remains the closest possible rectangle to a square)
     */
    public static Dimension best_rectangle_finder(int nb_of_images) {
        int sqrt = (int) Math.rint(Math.sqrt(nb_of_images));
        int nbRows = sqrt;
        int nbCols = sqrt;
        while (nbCols * nbRows < nb_of_images) {
            nbCols++;
        }
        nbRows = nbRows < 1 ? 1 : nbRows;
        nbCols = nbCols < 1 ? 1 : nbCols;
        return new Dimension(nbRows, nbCols);
    }

    /*
     * TODO clean this cause I'm probably doing a lot of useless operations
     */
    public static void updateFigure(Object shapeToDraw) {
        if (shapeToDraw != null && shapeToDraw instanceof PARoi) {
            doubleLayerPane2.setAllowRefresh(false);
            ((PARoi) shapeToDraw).drawAndFill(doubleLayerPane2.ROIS.getG2D());
            doubleLayerPane2.setAllowRefresh(true);
        }
    }

    /**
     * Redraw the figure
     */
    public static void updateFigure() {
        try {
            Figure rh = new Figure(rows, getSpaceBetweenRows());
            doubleLayerPane2.setAllowRefresh(false);
            doubleLayerPane2.ROIS.resetROIS();
            doubleLayerPane2.ROIS.addROIs(rows);
            Rectangle2D bounds = rh.getBounds2D();
            doubleLayerPane2.resizePanel((int) (bounds.getX() + bounds.getWidth() + 1.), (int) (bounds.getY() + bounds.getHeight() + 1.));
            doubleLayerPane2.setAllowRefresh(true);
            doubleLayerPane2.repaint();
        } catch (OutOfMemoryError E) {
            StringWriter sw = new StringWriter();
            E.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            try {
                System.gc();
                CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
            } catch (Exception e) {
            }
        } finally {
            doubleLayerPane2.setAllowRefresh(true);
        }
    }

    /**
     * We redraw just the selected image in the Panel (to memoryRefreshSpeed up
     * refresh)
     *
     * @param shapeToDraw
     */
    public static void updateTable(Object shapeToDraw) {
        if (shapeToDraw != null && shapeToDraw instanceof PARoi) {
            doubleLayerPane1.setAllowRefresh(false);
            ((PARoi) shapeToDraw).drawAndFill(doubleLayerPane1.ROIS.getG2D());
            doubleLayerPane1.setAllowRefresh(true);
        }
    }

    /**
     * Redraws the complete Panel
     */
    public static void updateTable() {
        if (curPanel != null) {
            doubleLayerPane1.setAllowRefresh(false);
            Rectangle2D bounds = curPanel.getBounds2D();
            doubleLayerPane1.resizePanel((int) (bounds.getX() + bounds.getWidth() + 1.), (int) (bounds.getY() + bounds.getHeight() + 1.));
        } else {
            doubleLayerPane1.resizePanel(0, 0);
        }
        doubleLayerPane1.setAllowRefresh(true);
        doubleLayerPane1.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton11 = new javax.swing.JButton();
        PIP = new javax.swing.JButton();
        removePiP = new javax.swing.JButton();
        loadAllyf5m = new javax.swing.JButton();
        sizeInPx = new javax.swing.JSpinner();
        deleteButton = new javax.swing.JButton();
        moveLeft = new javax.swing.JButton();
        moveRight = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        buttonHelp = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        undo = new javax.swing.JButton();
        redo = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton13 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        Rstatus = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        journalCombo = new javax.swing.JComboBox();
        checkSize = new javax.swing.JButton();
        checkStyle = new javax.swing.JButton();
        checkFont = new javax.swing.JButton();
        checkText = new javax.swing.JButton();
        checkLineArts = new javax.swing.JButton();
        checkGraph = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        sizeInCM = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        journalColumnsCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tableList = new javax.swing.JList();
        jScrollPane14 = new javax.swing.JScrollPane();
        figureList = new javax.swing.JList();
        jScrollPane13 = new javax.swing.JScrollPane();
        tableContentList = new javax.swing.JList();
        myList1 = new Dialogs.MyListLight();
        jToolBar2 = new javax.swing.JToolBar();
        createPanelAutomatically = new javax.swing.JButton();
        createPanelFromSelection = new javax.swing.JButton();
        addSelectionToCurrentPanel = new javax.swing.JButton();
        addEmptyImageToCurrentBlock = new javax.swing.JButton();
        DeleteSelectedBlock = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        addSelectedPanelToNewRow = new javax.swing.JButton();
        addSelectedPanelToSelectedRow = new javax.swing.JButton();
        removeSelectedRow = new javax.swing.JButton();
        jScrollPane18 = new javax.swing.JScrollPane();
        RowContentList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        imagesInFigureList = new javax.swing.JList();
        zoomPlus = new javax.swing.JButton();
        zoomMinus = new javax.swing.JButton();
        bestFitZoom = new javax.swing.JButton();
        realSizeZoom = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane17 = new javax.swing.JScrollPane();
        doubleLayerPane3 = new Dialogs.DoubleLayerPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        doubleLayerPane1 = new DoubleLayerPane(r1);
        jScrollPane16 = new javax.swing.JScrollPane();
        doubleLayerPane2 = new DoubleLayerPane(r2);
        optionsPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        deleteSelectedImageFromCurrentBlock = new javax.swing.JButton();
        moveImageInCurrentPanelLeft = new javax.swing.JButton();
        moveImageInCurrentPanelRight = new javax.swing.JButton();
        swapImagesFromCurrentPanel = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cropLeftSpinner = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        cropRightSpinner = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        rotateSpinner = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        cropUpSpinner = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        cropDownSpinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        AnnotateImageText = new javax.swing.JButton();
        AnnotateImageROIs = new javax.swing.JButton();
        removeAnnotations = new javax.swing.JButton();
        replaceImage = new javax.swing.JButton();
        splitColoredImagesToMagentaGreen = new javax.swing.JButton();
        imageSizePanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        imageWidth = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        imageHeight = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        AR = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        moveRowLeft = new javax.swing.JButton();
        moveRowRight = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        changeSpaceBetweenImageInPanel = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField1.setDocument(new LimitedTextField(LimitedTextField.ONE_LETTER));
        updateLetters = new javax.swing.JButton();
        changeSpaceBetweenRows = new javax.swing.JSpinner();
        jPanel12 = new javax.swing.JPanel();
        reformatTable = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        addSelectedImagesInCurPanel = new javax.swing.JButton();
        deleteSelectColumn = new javax.swing.JButton();
        moveColLeft = new javax.swing.JButton();
        moveColRight = new javax.swing.JButton();
        jPanel1666 = new javax.swing.JPanel();
        addLettersOutside = new javax.swing.JButton();
        addTextAboveRow = new javax.swing.JButton();
        addTextBelowRow = new javax.swing.JButton();
        AddTextLeftOfRow = new javax.swing.JButton();
        AddTextRightOfRow = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        deleteImagesFromTheList = new javax.swing.JButton();
        moveUpInList = new javax.swing.JButton();
        moveDownInList = new javax.swing.JButton();
        importFromIJ = new javax.swing.JButton();
        rotateNFlip = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        rotateLeft = new javax.swing.JButton();
        rotateRight = new javax.swing.JButton();
        Flip = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        New = new javax.swing.JMenuItem();
        OpenYF5M = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        Save = new javax.swing.JMenuItem();
        SaveAs = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        exportAsSVG = new javax.swing.JMenuItem();
        exportAsPNG = new javax.swing.JMenuItem();
        exportAsTiff = new javax.swing.JMenuItem();
        exportAsJpeg = new javax.swing.JMenuItem();
        exportToIJ = new javax.swing.JMenuItem();
        exportToIJMacro = new javax.swing.JMenuItem();
        extract_images = new javax.swing.JMenuItem();
        Quit = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        newJournalStyle = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        deleteJournalStyle = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        ChangeStrokeSize = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        removeAllText = new javax.swing.JMenuItem();
        removeAllScaleBars = new javax.swing.JMenuItem();
        removeAllAnnotations = new javax.swing.JMenuItem();
        capitalizeFirstLetter = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        launchFigur = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        showShortcuts = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        citations = new javax.swing.JMenuItem();

        jButton11.setText("jButton11");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        PIP.setText("PIP");
        PIP.setToolTipText("Add the selected image in the 'Image List' as an inset to the selected images of the montage");
        PIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        removePiP.setText("PIP");
        removePiP.setToolTipText("Add the selected image in the 'Image List' as an inset to the selected images of the montage");
        removePiP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        loadAllyf5m.setText("jButton1");
        loadAllyf5m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        sizeInPx.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(595), Integer.valueOf(1), null, Integer.valueOf(1)));
        sizeInPx.setToolTipText("Size of the panel/row in px");
        sizeInPx.setMinimumSize(new java.awt.Dimension(90, 24));
        sizeInPx.setPreferredSize(new java.awt.Dimension(90, 24));
        sizeInPx.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizeInPxsizeChanged(evt);
            }
        });

        deleteButton.setText("jButton1");

        moveLeft.setText("jButton1");

        moveRight.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(software_name+" v"+version);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Icons/icon_029.png")).getImage());
        setMinimumSize(new java.awt.Dimension(1024, 630));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                windowSizeChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onQuit(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        buttonHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_37.png"))); // NOI18N
        buttonHelp.setToolTipText("click here then click on a button,a spinner or a combo of interest to see the video demo demonstrating its functionalities");
        buttonHelp.setBorderPainted(false);
        buttonHelp.setFocusable(false);
        buttonHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(buttonHelp);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_11.png"))); // NOI18N
        jButton7.setToolTipText("Save");
        jButton7.setBorderPainted(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(jButton7);

        undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Knob Snapback.png"))); // NOI18N
        undo.setToolTipText("undo");
        undo.setBorderPainted(false);
        undo.setFocusable(false);
        undo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(undo);

        redo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Knob Snapforward.png"))); // NOI18N
        redo.setToolTipText("redo");
        redo.setBorderPainted(false);
        redo.setFocusable(false);
        redo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        redo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(redo);

        jProgressBar1.setMaximumSize(new java.awt.Dimension(140, 27));
        jProgressBar1.setMinimumSize(new java.awt.Dimension(100, 27));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(120, 27));
        jToolBar1.add(jProgressBar1);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/refresh.png"))); // NOI18N
        jButton13.setToolTipText("Free unused memory");
        jButton13.setBorderPainted(false);
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testMemory(evt);
            }
        });
        jToolBar1.add(jButton13);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ruler.png"))); // NOI18N
        jToggleButton1.setSelected(true);
        jToggleButton1.setToolTipText("Show/Hide Rulers");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png"))); // NOI18N
        Rstatus.setText("R status");
        Rstatus.setToolTipText("Should be green if connexion to R suceeded should remain red otherwise");
        Rstatus.setBorderPainted(false);
        Rstatus.setFocusable(false);
        Rstatus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Rstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(Rstatus);

        jLabel1.setText("Journal:");
        jLabel1.setToolTipText("Select a journal style");
        jToolBar1.add(jLabel1);

        journalCombo.setToolTipText("Select a journal style");
        journalCombo.setMinimumSize(new java.awt.Dimension(100, 27));
        journalCombo.setPreferredSize(new java.awt.Dimension(140, 27));
        journalCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(journalCombo);

        checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkSize.setText("Size");
        checkSize.setToolTipText("Check figure size");
        checkSize.setBorderPainted(false);
        checkSize.setFocusable(false);
        checkSize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkSize);

        checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkStyle.setText("Style");
        checkStyle.setToolTipText("Check text style (incorrect use of italics, bold,...)");
        checkStyle.setBorderPainted(false);
        checkStyle.setFocusable(false);
        checkStyle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkStyle);

        checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkFont.setText("Font");
        checkFont.setToolTipText("Check fonts");
        checkFont.setBorderPainted(false);
        checkFont.setFocusable(false);
        checkFont.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkFont);

        checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkText.setText("Text");
        checkText.setToolTipText("Check annotation/label text");
        checkText.setBorderPainted(false);
        checkText.setFocusable(false);
        checkText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkText);

        checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkLineArts.setText("Line arts");
        checkLineArts.setToolTipText("Check line art stroke width");
        checkLineArts.setBorderPainted(false);
        checkLineArts.setFocusable(false);
        checkLineArts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkLineArts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkLineArts);

        checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png"))); // NOI18N
        checkGraph.setText("Graph");
        checkGraph.setToolTipText("Check graphs");
        checkGraph.setBorderPainted(false);
        checkGraph.setFocusable(false);
        checkGraph.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar1.add(checkGraph);

        jLabel2.setText("Size (cm):");
        jLabel2.setToolTipText("Size of the panel/row in cm");
        jToolBar1.add(jLabel2);

        sizeInCM.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(21.0d), Double.valueOf(0.001d), null, Double.valueOf(0.05d)));
        sizeInCM.setToolTipText("Size of the panel/row in cm");
        sizeInCM.setMinimumSize(new java.awt.Dimension(90, 24));
        sizeInCM.setPreferredSize(new java.awt.Dimension(90, 24));
        sizeInCM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cmSizeChanged(evt);
            }
        });
        jToolBar1.add(sizeInCM);

        jLabel4.setText("(cols)");
        jLabel4.setToolTipText("Width of the block/row in journal columns");
        jToolBar1.add(jLabel4);

        journalColumnsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2 Cols", "1.5 Cols", "1 Col" }));
        journalColumnsCombo.setToolTipText("Width of the panel/row in journal columns");
        journalColumnsCombo.setMinimumSize(new java.awt.Dimension(90, 24));
        journalColumnsCombo.setPreferredSize(new java.awt.Dimension(90, 24));
        journalColumnsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageWidthChanged(evt);
            }
        });
        jToolBar1.add(journalColumnsCombo);

        tableList.setBorder(javax.swing.BorderFactory.createTitledBorder("Panels"));
        tableList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        tableList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                montageChanged(evt);
            }
        });
        jScrollPane12.setViewportView(tableList);

        figureList.setBorder(javax.swing.BorderFactory.createTitledBorder("Final figure"));
        figureList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        figureList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                figureListValueChanged(evt);
            }
        });
        jScrollPane14.setViewportView(figureList);

        tableContentList.setBorder(javax.swing.BorderFactory.createTitledBorder("Panel content"));
        tableContentList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        tableContentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                newObjectSelected(evt);
            }
        });
        jScrollPane13.setViewportView(tableContentList);

        myList1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        myList1.setMinimumSize(new java.awt.Dimension(24, 400));
        myList1.setPreferredSize(new java.awt.Dimension(244, 400));

        jToolBar2.setBorder(null);
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        createPanelAutomatically.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_28.png"))); // NOI18N
        createPanelAutomatically.setText("Auto");
        createPanelAutomatically.setToolTipText("Create panels out of consecutive images having the same size/aspect ratio");
        createPanelAutomatically.setBorderPainted(false);
        createPanelAutomatically.setFocusable(false);
        createPanelAutomatically.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createPanelAutomatically.setMaximumSize(new java.awt.Dimension(55, 80));
        createPanelAutomatically.setMinimumSize(new java.awt.Dimension(55, 80));
        createPanelAutomatically.setPreferredSize(new java.awt.Dimension(55, 80));
        createPanelAutomatically.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createPanelAutomatically.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar2.add(createPanelAutomatically);

        createPanelFromSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_28.png"))); // NOI18N
        createPanelFromSelection.setToolTipText("Add selected images to new panel");
        createPanelFromSelection.setBorderPainted(false);
        createPanelFromSelection.setFocusable(false);
        createPanelFromSelection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createPanelFromSelection.setMaximumSize(new java.awt.Dimension(36, 80));
        createPanelFromSelection.setMinimumSize(new java.awt.Dimension(36, 80));
        createPanelFromSelection.setPreferredSize(new java.awt.Dimension(36, 80));
        createPanelFromSelection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createPanelFromSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar2.add(createPanelFromSelection);

        addSelectionToCurrentPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add Green Button.png"))); // NOI18N
        addSelectionToCurrentPanel.setToolTipText("Append selected images to the current panel");
        addSelectionToCurrentPanel.setBorderPainted(false);
        addSelectionToCurrentPanel.setFocusable(false);
        addSelectionToCurrentPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSelectionToCurrentPanel.setMaximumSize(new java.awt.Dimension(36, 80));
        addSelectionToCurrentPanel.setMinimumSize(new java.awt.Dimension(36, 80));
        addSelectionToCurrentPanel.setPreferredSize(new java.awt.Dimension(36, 80));
        addSelectionToCurrentPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addSelectionToCurrentPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar2.add(addSelectionToCurrentPanel);

        addEmptyImageToCurrentBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add Green Button.png"))); // NOI18N
        addEmptyImageToCurrentBlock.setText("Empty");
        addEmptyImageToCurrentBlock.setToolTipText("Add empty image (to be replaced later on by a real image)");
        addEmptyImageToCurrentBlock.setBorderPainted(false);
        addEmptyImageToCurrentBlock.setFocusable(false);
        addEmptyImageToCurrentBlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addEmptyImageToCurrentBlock.setMaximumSize(new java.awt.Dimension(60, 80));
        addEmptyImageToCurrentBlock.setMinimumSize(new java.awt.Dimension(60, 80));
        addEmptyImageToCurrentBlock.setPreferredSize(new java.awt.Dimension(60, 80));
        addEmptyImageToCurrentBlock.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addEmptyImageToCurrentBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar2.add(addEmptyImageToCurrentBlock);

        DeleteSelectedBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_30.png"))); // NOI18N
        DeleteSelectedBlock.setToolTipText("Delete panel (send back panel content to the image list)");
        DeleteSelectedBlock.setBorderPainted(false);
        DeleteSelectedBlock.setFocusable(false);
        DeleteSelectedBlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DeleteSelectedBlock.setMaximumSize(new java.awt.Dimension(36, 80));
        DeleteSelectedBlock.setMinimumSize(new java.awt.Dimension(36, 80));
        DeleteSelectedBlock.setPreferredSize(new java.awt.Dimension(36, 80));
        DeleteSelectedBlock.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DeleteSelectedBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar2.add(DeleteSelectedBlock);

        jToolBar4.setBorder(null);
        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        addSelectedPanelToNewRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_28.png"))); // NOI18N
        addSelectedPanelToNewRow.setToolTipText("Add Selected Panel As A New Row");
        addSelectedPanelToNewRow.setBorderPainted(false);
        addSelectedPanelToNewRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSelectedPanelToNewRow.setMaximumSize(new java.awt.Dimension(55, 80));
        addSelectedPanelToNewRow.setMinimumSize(new java.awt.Dimension(55, 80));
        addSelectedPanelToNewRow.setPreferredSize(new java.awt.Dimension(55, 80));
        addSelectedPanelToNewRow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addSelectedPanelToNewRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar4.add(addSelectedPanelToNewRow);

        addSelectedPanelToSelectedRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add Green Button.png"))); // NOI18N
        addSelectedPanelToSelectedRow.setToolTipText("Add Panel To Current Row");
        addSelectedPanelToSelectedRow.setBorderPainted(false);
        addSelectedPanelToSelectedRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSelectedPanelToSelectedRow.setMaximumSize(new java.awt.Dimension(55, 49));
        addSelectedPanelToSelectedRow.setMinimumSize(new java.awt.Dimension(55, 49));
        addSelectedPanelToSelectedRow.setPreferredSize(new java.awt.Dimension(55, 49));
        addSelectedPanelToSelectedRow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addSelectedPanelToSelectedRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar4.add(addSelectedPanelToSelectedRow);

        removeSelectedRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_30.png"))); // NOI18N
        removeSelectedRow.setToolTipText("Remove the current panel/column from the selected row");
        removeSelectedRow.setBorderPainted(false);
        removeSelectedRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeSelectedRow.setMaximumSize(new java.awt.Dimension(55, 49));
        removeSelectedRow.setMinimumSize(new java.awt.Dimension(55, 49));
        removeSelectedRow.setPreferredSize(new java.awt.Dimension(55, 49));
        removeSelectedRow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removeSelectedRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jToolBar4.add(removeSelectedRow);

        RowContentList.setBorder(javax.swing.BorderFactory.createTitledBorder("Row content (cols)"));
        RowContentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        RowContentList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        RowContentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                RowContentListValueChanged(evt);
            }
        });
        jScrollPane18.setViewportView(RowContentList);

        imagesInFigureList.setBorder(javax.swing.BorderFactory.createTitledBorder("Panel/col content"));
        imagesInFigureList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        imagesInFigureList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listClicked(evt);
            }
        });
        imagesInFigureList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                imagesInFigureChanged(evt);
            }
        });
        jScrollPane2.setViewportView(imagesInFigureList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(myList1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(myList1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)))
        );

        myList1.getList().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                img_list_valueChanged(e);
            }
        });

        zoomPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/zoom in.png"))); // NOI18N
        zoomPlus.setToolTipText("Zoom +");
        zoomPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        zoomMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/zoom out.png"))); // NOI18N
        zoomMinus.setToolTipText("Zoom -");
        zoomMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        bestFitZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/fit_2_screen.gif"))); // NOI18N
        bestFitZoom.setToolTipText("Best Fit");
        bestFitZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        realSizeZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/1in1.png"))); // NOI18N
        realSizeZoom.setToolTipText("Original Size");
        realSizeZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        jTabbedPane1.setEnabled(false);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabSelChanged(evt);
            }
        });

        javax.swing.GroupLayout doubleLayerPane3Layout = new javax.swing.GroupLayout(doubleLayerPane3);
        doubleLayerPane3.setLayout(doubleLayerPane3Layout);
        doubleLayerPane3Layout.setHorizontalGroup(
            doubleLayerPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );
        doubleLayerPane3Layout.setVerticalGroup(
            doubleLayerPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );

        jScrollPane17.setViewportView(doubleLayerPane3);

        jTabbedPane1.addTab("Preview Image", jScrollPane17);

        javax.swing.GroupLayout doubleLayerPane1Layout = new javax.swing.GroupLayout(doubleLayerPane1);
        doubleLayerPane1.setLayout(doubleLayerPane1Layout);
        doubleLayerPane1Layout.setHorizontalGroup(
            doubleLayerPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );
        doubleLayerPane1Layout.setVerticalGroup(
            doubleLayerPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );

        jScrollPane8.setViewportView(doubleLayerPane1);

        jTabbedPane1.addTab("Preview Panel", jScrollPane8);

        javax.swing.GroupLayout doubleLayerPane2Layout = new javax.swing.GroupLayout(doubleLayerPane2);
        doubleLayerPane2.setLayout(doubleLayerPane2Layout);
        doubleLayerPane2Layout.setHorizontalGroup(
            doubleLayerPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );
        doubleLayerPane2Layout.setVerticalGroup(
            doubleLayerPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
        );

        jScrollPane16.setViewportView(doubleLayerPane2);

        jTabbedPane1.addTab("Preview Figure", jScrollPane16);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options for the current selection"));
        optionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        deleteSelectedImageFromCurrentBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Remove.png"))); // NOI18N
        deleteSelectedImageFromCurrentBlock.setToolTipText("Delete Selected Image From Block (send them back to the image List)");
        deleteSelectedImageFromCurrentBlock.setMaximumSize(new java.awt.Dimension(57, 37));
        deleteSelectedImageFromCurrentBlock.setMinimumSize(new java.awt.Dimension(57, 37));
        deleteSelectedImageFromCurrentBlock.setPreferredSize(new java.awt.Dimension(57, 37));
        deleteSelectedImageFromCurrentBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel11.add(deleteSelectedImageFromCurrentBlock);

        moveImageInCurrentPanelLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_29.png"))); // NOI18N
        moveImageInCurrentPanelLeft.setToolTipText("Change image order (swap with previous image)");
        moveImageInCurrentPanelLeft.setMaximumSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelLeft.setMinimumSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelLeft.setPreferredSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel11.add(moveImageInCurrentPanelLeft);

        moveImageInCurrentPanelRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_27.png"))); // NOI18N
        moveImageInCurrentPanelRight.setToolTipText("Change image order (swap with next image)");
        moveImageInCurrentPanelRight.setMaximumSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelRight.setMinimumSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelRight.setPreferredSize(new java.awt.Dimension(57, 37));
        moveImageInCurrentPanelRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel11.add(moveImageInCurrentPanelRight);

        swapImagesFromCurrentPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/swap.png"))); // NOI18N
        swapImagesFromCurrentPanel.setToolTipText("Swap 2 Images");
        swapImagesFromCurrentPanel.setMaximumSize(new java.awt.Dimension(57, 37));
        swapImagesFromCurrentPanel.setMinimumSize(new java.awt.Dimension(57, 37));
        swapImagesFromCurrentPanel.setPreferredSize(new java.awt.Dimension(57, 37));
        swapImagesFromCurrentPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel11.add(swapImagesFromCurrentPanel);

        jPanel4.add(jPanel11);

        jPanel7.setLayout(new java.awt.GridLayout(2, 6));

        jLabel10.setText("Crop left:");
        jLabel10.setToolTipText("Crop images from the left");
        jPanel7.add(jLabel10);

        cropLeftSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        cropLeftSpinner.setToolTipText("Crop images from the left");
        cropLeftSpinner.setMaximumSize(null);
        cropLeftSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cropChanged(evt);
            }
        });
        jPanel7.add(cropLeftSpinner);

        jLabel11.setText("Crop right:");
        jLabel11.setToolTipText("Crop images from the right");
        jPanel7.add(jLabel11);

        cropRightSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        cropRightSpinner.setToolTipText("Crop images from the right");
        cropRightSpinner.setMaximumSize(null);
        cropRightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cropChanged(evt);
            }
        });
        jPanel7.add(cropRightSpinner);

        jLabel6.setText("Angle:");
        jLabel6.setToolTipText("Rotate images");
        jPanel7.add(jLabel6);

        rotateSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 359, 1));
        rotateSpinner.setToolTipText("Rotate images");
        rotateSpinner.setMaximumSize(null);
        rotateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                angleChanged(evt);
            }
        });
        jPanel7.add(rotateSpinner);

        jLabel12.setText("Crop up:");
        jLabel12.setToolTipText("Crop images from the top");
        jPanel7.add(jLabel12);

        cropUpSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        cropUpSpinner.setToolTipText("Crop images from the top");
        cropUpSpinner.setMaximumSize(null);
        cropUpSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cropChanged(evt);
            }
        });
        jPanel7.add(cropUpSpinner);

        jLabel13.setText("Crop down:");
        jLabel13.setToolTipText("Crop images from the bottom");
        jPanel7.add(jLabel13);

        cropDownSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        cropDownSpinner.setToolTipText("Crop images from the bottom");
        cropDownSpinner.setMaximumSize(null);
        cropDownSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cropChanged(evt);
            }
        });
        jPanel7.add(cropDownSpinner);
        jPanel7.add(jLabel9);

        jPanel4.add(jPanel7);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        AnnotateImageText.setText("<html><center>Edit image text, <br> add scale bars or insets");
        AnnotateImageText.setToolTipText("Edit letter, text at the corners, add a scale bar or an inset to your image");
        AnnotateImageText.setMaximumSize(new java.awt.Dimension(200, 37));
        AnnotateImageText.setMinimumSize(new java.awt.Dimension(200, 37));
        AnnotateImageText.setPreferredSize(new java.awt.Dimension(200, 37));
        AnnotateImageText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel10.add(AnnotateImageText);

        AnnotateImageROIs.setText("<html><center>Add ROIs, arrows, asterisks, floating text,...");
        AnnotateImageROIs.setToolTipText("Add/Edit arrows/ROIs/Asterisks/lines/floating text, ... to the selected image");
        AnnotateImageROIs.setMaximumSize(new java.awt.Dimension(220, 37));
        AnnotateImageROIs.setMinimumSize(new java.awt.Dimension(200, 37));
        AnnotateImageROIs.setPreferredSize(new java.awt.Dimension(220, 37));
        AnnotateImageROIs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel10.add(AnnotateImageROIs);

        removeAnnotations.setText("<html><center>Remove ROIS,<br>arrows, ...");
        removeAnnotations.setToolTipText("Remove all the ROIs, arrows, asteriks, ... added to the image");
        removeAnnotations.setActionCommand("<html><center>Remove  ROIs,<br>brackets, arrows, ...");
        removeAnnotations.setMaximumSize(new java.awt.Dimension(120, 37));
        removeAnnotations.setMinimumSize(new java.awt.Dimension(120, 37));
        removeAnnotations.setPreferredSize(new java.awt.Dimension(120, 37));
        removeAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel10.add(removeAnnotations);

        replaceImage.setText("Replace");
        replaceImage.setToolTipText("Replace selected images");
        replaceImage.setMaximumSize(new java.awt.Dimension(90, 37));
        replaceImage.setMinimumSize(new java.awt.Dimension(90, 37));
        replaceImage.setPreferredSize(new java.awt.Dimension(90, 37));
        replaceImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel10.add(replaceImage);

        splitColoredImagesToMagentaGreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/color_blind.png"))); // NOI18N
        splitColoredImagesToMagentaGreen.setToolTipText("Make The Selected Images Color Blind Friendly/Split Channels");
        splitColoredImagesToMagentaGreen.setMaximumSize(new java.awt.Dimension(57, 37));
        splitColoredImagesToMagentaGreen.setMinimumSize(new java.awt.Dimension(57, 37));
        splitColoredImagesToMagentaGreen.setPreferredSize(new java.awt.Dimension(57, 37));
        splitColoredImagesToMagentaGreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel10.add(splitColoredImagesToMagentaGreen);

        jPanel4.add(jPanel10);

        optionsPanel.add(jPanel4);

        imageSizePanel.setLayout(new javax.swing.BoxLayout(imageSizePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("<html><center>Image<br>width: ");
        imageSizePanel.add(jLabel8);

        imageWidth.setText(" N.A. ");
        imageSizePanel.add(imageWidth);

        jLabel14.setText("<html><center>Image<br>height:");
        imageSizePanel.add(jLabel14);

        imageHeight.setText(" N.A. ");
        imageSizePanel.add(imageHeight);

        jLabel16.setText("<html><center>Aspect<br>ratio:");
        imageSizePanel.add(jLabel16);

        AR.setText(" N.A. ");
        imageSizePanel.add(AR);

        optionsPanel.add(imageSizePanel);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        moveRowLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_29.png"))); // NOI18N
        moveRowLeft.setToolTipText("Change row order (swap selected row with the previous one)");
        moveRowLeft.setFocusable(false);
        moveRowLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        moveRowLeft.setMaximumSize(new java.awt.Dimension(57, 37));
        moveRowLeft.setMinimumSize(new java.awt.Dimension(57, 37));
        moveRowLeft.setPreferredSize(new java.awt.Dimension(57, 37));
        moveRowLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        moveRowLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel6.add(moveRowLeft);

        moveRowRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_27.png"))); // NOI18N
        moveRowRight.setToolTipText("Change row order (swap selected row with the next one)");
        moveRowRight.setFocusable(false);
        moveRowRight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        moveRowRight.setMaximumSize(new java.awt.Dimension(57, 37));
        moveRowRight.setMinimumSize(new java.awt.Dimension(57, 37));
        moveRowRight.setPreferredSize(new java.awt.Dimension(57, 37));
        moveRowRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        moveRowRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel6.add(moveRowRight);

        optionsPanel.add(jPanel6);

        jLabel5.setText("<html><center>Space<br>(px):");
        jLabel5.setToolTipText("Empty space between images of the same block");

        changeSpaceBetweenImageInPanel.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(3), Integer.valueOf(1), null, Integer.valueOf(1)));
        changeSpaceBetweenImageInPanel.setToolTipText("Empty space between images of the same block");
        changeSpaceBetweenImageInPanel.setMinimumSize(new java.awt.Dimension(90, 37));
        changeSpaceBetweenImageInPanel.setPreferredSize(new java.awt.Dimension(80, 37));
        changeSpaceBetweenImageInPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                changeSpaceBetweenImageInPanelspaceBetweenImagesChanged(evt);
            }
        });

        jLabel7.setText("<html><center>1<sup>st</sup><br>letter:</html>");
        jLabel7.setToolTipText("Put here the first letter of the block");

        jTextField1.setText("A");
        jTextField1.setToolTipText("Put here the first letter of the block");
        jTextField1.setMinimumSize(new java.awt.Dimension(6, 37));
        jTextField1.setPreferredSize(new java.awt.Dimension(12, 37));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        updateLetters.setText("<html><center>Update<br>letters</center></html>");
        updateLetters.setToolTipText("Letters of the images in the block will be incremented for each new image");
        updateLetters.setMaximumSize(new java.awt.Dimension(85, 37));
        updateLetters.setMinimumSize(new java.awt.Dimension(85, 37));
        updateLetters.setPreferredSize(new java.awt.Dimension(85, 37));
        updateLetters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });

        changeSpaceBetweenRows.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(3), Integer.valueOf(1), null, Integer.valueOf(1)));
        changeSpaceBetweenRows.setToolTipText("Empty space between rows and columns within a row");
        changeSpaceBetweenRows.setMinimumSize(new java.awt.Dimension(90, 37));
        changeSpaceBetweenRows.setPreferredSize(new java.awt.Dimension(80, 37));
        changeSpaceBetweenRows.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                changeSpaceBetweenRowsspaceBetweenImagesChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(changeSpaceBetweenImageInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeSpaceBetweenRows, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateLetters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(updateLetters, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addComponent(changeSpaceBetweenImageInPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(changeSpaceBetweenRows, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {changeSpaceBetweenImageInPanel, jLabel5, jLabel7, jTextField1, updateLetters});

        optionsPanel.add(jPanel9);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        reformatTable.setText("<html><center>Reformat<br>table</center></html>");
        reformatTable.setToolTipText("Edit Block Format (change nb of rows and columns)");
        reformatTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel12.add(reformatTable);

        optionsPanel.add(jPanel12);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        addSelectedImagesInCurPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add Green Button.png"))); // NOI18N
        addSelectedImagesInCurPanel.setToolTipText("Append selected images to the current panel");
        addSelectedImagesInCurPanel.setMaximumSize(new java.awt.Dimension(57, 37));
        addSelectedImagesInCurPanel.setMinimumSize(new java.awt.Dimension(57, 37));
        addSelectedImagesInCurPanel.setPreferredSize(new java.awt.Dimension(57, 37));
        addSelectedImagesInCurPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel16.add(addSelectedImagesInCurPanel);

        deleteSelectColumn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Remove.png"))); // NOI18N
        deleteSelectColumn.setToolTipText("Remove selected block");
        deleteSelectColumn.setMaximumSize(new java.awt.Dimension(57, 37));
        deleteSelectColumn.setMinimumSize(new java.awt.Dimension(57, 37));
        deleteSelectColumn.setPreferredSize(new java.awt.Dimension(57, 37));
        deleteSelectColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel16.add(deleteSelectColumn);

        moveColLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_29.png"))); // NOI18N
        moveColLeft.setToolTipText("Move block to the left");
        moveColLeft.setMaximumSize(new java.awt.Dimension(57, 37));
        moveColLeft.setMinimumSize(new java.awt.Dimension(57, 37));
        moveColLeft.setPreferredSize(new java.awt.Dimension(57, 37));
        moveColLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel16.add(moveColLeft);

        moveColRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_27.png"))); // NOI18N
        moveColRight.setToolTipText("Move block to the right");
        moveColRight.setMaximumSize(new java.awt.Dimension(57, 37));
        moveColRight.setMinimumSize(new java.awt.Dimension(57, 37));
        moveColRight.setPreferredSize(new java.awt.Dimension(57, 37));
        moveColRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel16.add(moveColRight);

        optionsPanel.add(jPanel16);

        jPanel1666.setLayout(new java.awt.GridLayout(2, 3));

        addLettersOutside.setText("<html><center>Add letters outside</center>");
        addLettersOutside.setToolTipText("Add text bars above images");
        addLettersOutside.setMaximumSize(new java.awt.Dimension(132, 37));
        addLettersOutside.setMinimumSize(new java.awt.Dimension(132, 37));
        addLettersOutside.setPreferredSize(new java.awt.Dimension(132, 37));
        addLettersOutside.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1666.add(addLettersOutside);

        addTextAboveRow.setText("<html><center>Add text bars<br>above images");
        addTextAboveRow.setToolTipText("Add text bars above images");
        addTextAboveRow.setMaximumSize(new java.awt.Dimension(132, 37));
        addTextAboveRow.setMinimumSize(new java.awt.Dimension(132, 37));
        addTextAboveRow.setPreferredSize(new java.awt.Dimension(132, 37));
        addTextAboveRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1666.add(addTextAboveRow);

        addTextBelowRow.setText("<html><center>Add text bars<br>below images");
        addTextBelowRow.setToolTipText("Add text bars below images");
        addTextBelowRow.setMaximumSize(new java.awt.Dimension(132, 37));
        addTextBelowRow.setMinimumSize(new java.awt.Dimension(132, 37));
        addTextBelowRow.setPreferredSize(new java.awt.Dimension(132, 37));
        addTextBelowRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1666.add(addTextBelowRow);

        AddTextLeftOfRow.setText("<html><center>Add text bars<br>on the left");
        AddTextLeftOfRow.setToolTipText("Add text bars on the left");
        AddTextLeftOfRow.setMaximumSize(new java.awt.Dimension(132, 37));
        AddTextLeftOfRow.setMinimumSize(new java.awt.Dimension(132, 37));
        AddTextLeftOfRow.setPreferredSize(new java.awt.Dimension(132, 37));
        AddTextLeftOfRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1666.add(AddTextLeftOfRow);

        AddTextRightOfRow.setText("<html><center>Add text bars<br>on the right");
        AddTextRightOfRow.setToolTipText("Add text bars on the right");
        AddTextRightOfRow.setMaximumSize(new java.awt.Dimension(132, 37));
        AddTextRightOfRow.setMinimumSize(new java.awt.Dimension(132, 37));
        AddTextRightOfRow.setPreferredSize(new java.awt.Dimension(132, 37));
        AddTextRightOfRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1666.add(AddTextRightOfRow);

        optionsPanel.add(jPanel1666);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        deleteImagesFromTheList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Trash Empty.png"))); // NOI18N
        deleteImagesFromTheList.setToolTipText("Permanently delete an image from the list");
        deleteImagesFromTheList.setMaximumSize(new java.awt.Dimension(57, 37));
        deleteImagesFromTheList.setMinimumSize(new java.awt.Dimension(57, 37));
        deleteImagesFromTheList.setPreferredSize(new java.awt.Dimension(57, 37));
        deleteImagesFromTheList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1.add(deleteImagesFromTheList);

        moveUpInList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_29.png"))); // NOI18N
        moveUpInList.setToolTipText("Move Selection Up In The List");
        moveUpInList.setMaximumSize(new java.awt.Dimension(57, 37));
        moveUpInList.setMinimumSize(new java.awt.Dimension(57, 37));
        moveUpInList.setPreferredSize(new java.awt.Dimension(57, 37));
        moveUpInList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1.add(moveUpInList);

        moveDownInList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_27.png"))); // NOI18N
        moveDownInList.setToolTipText("Move Selection Down In The List");
        moveDownInList.setMaximumSize(new java.awt.Dimension(57, 37));
        moveDownInList.setMinimumSize(new java.awt.Dimension(57, 37));
        moveDownInList.setPreferredSize(new java.awt.Dimension(57, 37));
        moveDownInList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1.add(moveDownInList);

        importFromIJ.setText("<html><center>Import From:<br>ImageJ");
        importFromIJ.setMaximumSize(new java.awt.Dimension(130, 37));
        importFromIJ.setMinimumSize(new java.awt.Dimension(130, 37));
        importFromIJ.setPreferredSize(new java.awt.Dimension(130, 37));
        importFromIJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jPanel1.add(importFromIJ);

        optionsPanel.add(jPanel1);

        rotateNFlip.setLayout(new javax.swing.BoxLayout(rotateNFlip, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Flip:");
        jLabel3.setMaximumSize(new java.awt.Dimension(53, 37));
        jLabel3.setMinimumSize(new java.awt.Dimension(53, 37));
        jLabel3.setPreferredSize(new java.awt.Dimension(53, 37));
        rotateNFlip.add(jLabel3);

        rotateLeft.setText("Left");
        rotateLeft.setToolTipText("Rotate image by exactly 90° on the left (lossless transformation)");
        rotateLeft.setMaximumSize(new java.awt.Dimension(70, 37));
        rotateLeft.setMinimumSize(new java.awt.Dimension(70, 37));
        rotateLeft.setPreferredSize(new java.awt.Dimension(70, 37));
        rotateLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        rotateNFlip.add(rotateLeft);

        rotateRight.setText("Right");
        rotateRight.setToolTipText("Rotate image by exactly 90° on the right (lossless transformation)");
        rotateRight.setMaximumSize(new java.awt.Dimension(80, 37));
        rotateRight.setMinimumSize(new java.awt.Dimension(80, 37));
        rotateRight.setPreferredSize(new java.awt.Dimension(80, 37));
        rotateRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        rotateNFlip.add(rotateRight);

        Flip.setText("Other");
        Flip.setToolTipText("Flip selection horizontally or vertically");
        Flip.setMaximumSize(new java.awt.Dimension(70, 37));
        Flip.setMinimumSize(new java.awt.Dimension(60, 37));
        Flip.setPreferredSize(new java.awt.Dimension(70, 37));
        Flip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        rotateNFlip.add(Flip);

        optionsPanel.add(rotateNFlip);

        jMenu1.setText("File");

        New.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        New.setText("New");
        New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(New);

        OpenYF5M.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        OpenYF5M.setText("Open (or drag and drop)");
        OpenYF5M.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(OpenYF5M);

        jMenuItem19.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem19.setText("Import images (or drag and drop)");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(jMenuItem19);

        Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        Save.setText("Save");
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(Save);

        SaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        SaveAs.setText("Save as...");
        SaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(SaveAs);

        jMenu3.setText("Export as...");

        exportAsSVG.setText("SVG (vector graphics) (Highly Recommended For Publications)");
        exportAsSVG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu3.add(exportAsSVG);

        exportAsPNG.setText("PNG (lossless raster with transparent background) (Best choice for office douments)");
        exportAsPNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu3.add(exportAsPNG);

        exportAsTiff.setText("TIFF (lossless raster)");
        exportAsTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu3.add(exportAsTiff);

        exportAsJpeg.setText("JPG (raster) not for publications");
        exportAsJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu3.add(exportAsJpeg);

        jMenu1.add(jMenu3);

        exportToIJ.setText("Export to ImageJ");
        exportToIJ.setToolTipText("Export to ImageJ");
        exportToIJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(exportToIJ);

        exportToIJMacro.setText("Export to ImageJ macro");
        exportToIJMacro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToIJMacroActionPerformed(evt);
            }
        });
        jMenu1.add(exportToIJMacro);

        extract_images.setText("Extract images from opened figures/blocks");
        extract_images.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(extract_images);

        Quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        Quit.setText("Quit");
        Quit.setToolTipText("Quit");
        Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu1.add(Quit);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Edit");

        jMenuItem18.setText("Preferences");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu5.add(jMenuItem18);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem14.setText("Undo");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        jMenuItem21.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem21.setText("Redo");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem21);

        jMenuBar1.add(jMenu5);

        jMenu11.setText("Journals");
        jMenu11.setToolTipText("");

        newJournalStyle.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        newJournalStyle.setText("Create new journal/plot style");
        newJournalStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu11.add(newJournalStyle);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Edit current journal/plot style");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu11.add(jMenuItem11);

        deleteJournalStyle.setText("Permanently Delete journal/plot style");
        deleteJournalStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu11.add(deleteJournalStyle);

        jMenuBar1.add(jMenu11);

        jMenu4.setText("Panels");

        jMenuItem12.setText("Select and apply a font or text color or background color to all components");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        ChangeStrokeSize.setText("Select and apply a specific line width/point size to graphs/ROIs/embedded SVG images");
        ChangeStrokeSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(ChangeStrokeSize);

        jMenuItem16.setText("Apply the selected journal style to all components");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(jMenuItem16);

        jMenuItem17.setText("Apply selected width (in cm) to all components");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        removeAllText.setText("Remove all text");
        removeAllText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(removeAllText);

        removeAllScaleBars.setText("Remove all scale bars");
        removeAllScaleBars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(removeAllScaleBars);

        removeAllAnnotations.setText("Remove all annotations");
        removeAllAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(removeAllAnnotations);

        capitalizeFirstLetter.setText("Capitalize first letter");
        capitalizeFirstLetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu4.add(capitalizeFirstLetter);

        jMenuBar1.add(jMenu4);

        jMenu6.setText("FiguR");

        launchFigur.setText("Launch plugin");
        launchFigur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchFiguR(evt);
            }
        });
        jMenu6.add(launchFigur);

        jMenuBar1.add(jMenu6);

        jMenu14.setText("Help/Licenses/Citations");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem8.setText("Help content");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu14.add(jMenuItem8);

        jMenuItem13.setText("View a demo of the functionalities of the next button/spinner you are going to click on");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpVideo(evt);
            }
        });
        jMenu14.add(jMenuItem13);

        showShortcuts.setText("Shortcuts");
        showShortcuts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu14.add(showShortcuts);

        jMenuItem2.setText("Show R installation guidelines");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu14.add(jMenuItem2);

        jMenuItem9.setText("About/licenses");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu14.add(jMenuItem9);

        citations.setText("Citations");
        citations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAll(evt);
            }
        });
        jMenu14.add(citations);

        jMenuBar1.add(jMenu14);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zoomPlus)
                            .addComponent(zoomMinus)
                            .addComponent(bestFitZoom)
                            .addComponent(realSizeZoom)))
                    .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(zoomPlus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zoomMinus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bestFitZoom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(realSizeZoom))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @return a letter
     */
    public String getLetter() {
        return jTextField1.getText();
    }

    /**
     *
     * @return true if the user has created at least a row or a Panel
     */
    public boolean isThereAnythingCreated() {
        return !tableListModel.isEmpty() || !figureListModel.isEmpty();
    }

    /**
     *
     * @return true if there are images available in the 'image list' to create
     * or to add insets or to replace images something
     */
    public boolean isThereAnythingToCreate() {
        return !myList1.isEmpty();
    }

    /**
     *
     * @return true if at least one list in the software is not empty
     */
    public boolean isThereAnythingInTheLists() {
        return isThereAnythingCreated() || isThereAnythingToCreate();
    }

    /**
     * Updates the title of the software
     *
     * @param name
     */
    public void updateTitle(String name) {
        try {
            if (name == null) {
                setTitle(software_name + " v" + version);
            } else {
                setTitle(software_name + " v" + version + " - " + new File(name).getName());
            }
        } catch (Exception e) {
            setTitle(software_name + " v" + version);
        }
    }

    private void updateChecksStatus(Object in) {
        /**
         * TODO faire une interface pr les checks
         */
        if (in instanceof Montage) {
            Montage tmp = (Montage) in;
            if (tmp.wasCheckedForSize) {
                checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForFonts) {
                checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForGraphs) {
                checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForLineArts) {
                checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForStyle) {
                checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForText) {
                checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
        } else if (in instanceof Row) {
            Row tmp = (Row) in;
            if (tmp.wasCheckedForSize) {
                checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForFonts) {
                checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForGraphs) {
                checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForLineArts) {
                checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForStyle) {
                checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
            if (tmp.wasCheckedForText) {
                checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_34.png")));
            } else {
                checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            }
        } else {
            checkSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            checkFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            checkGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            checkLineArts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            checkStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
            checkText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/onebit_33.png")));
        }
    }

    /**
     * loads panel/row parameters (such as space between images or panels, ...)
     * to the GUI
     */
    public void loadCurPanelParameters() {
        if (jTabbedPane1.getSelectedIndex() == 1 && curPanel != null) {
            if (curPanel instanceof Montage) {
                Montage tmp = ((Montage) curPanel);
                loading = true;
                updateChecksStatus(tmp);
                int width_in_px = (int) tmp.getWidth_in_px();
                double width_in_cm = tmp.getWidth_in_cm();
                sizeInPx.setValue(width_in_px);
                setSpaceBetweenImages(tmp.getSpace_between_images());
                if (!sizeLock) {
                    sizeInCM.setValue(width_in_cm);
                }
                journalColumnsCombo.setSelectedIndex(-1);
                loading = false;
            }
        } else if (jTabbedPane1.getSelectedIndex() == 2 && current_row != null) {
            loading = true;
            Row row;
            if (current_row instanceof ArrayList) {
                row = ((ArrayList<Row>) current_row).get(0);
            } else {
                row = (Row) current_row;
                updateChecksStatus(row);
            }
            int width_in_px = (int) row.getWidth_in_px();
            setSpaceBetweenRows(row.getSpace_between_panels());
            double width_in_cm = row.getWidth_in_cm();
            sizeInPx.setValue(width_in_px);
            if (!sizeLock) {
                sizeInCM.setValue(width_in_cm);
            }
            journalColumnsCombo.setSelectedIndex(-1);
            loading = false;
        } else if (curPanel == null || cur_sel_image1 == null) {
            dontChangeAngle = true;
            dontChangeCropSize = true;
            cropLeftSpinner.setValue(0);
            cropRightSpinner.setValue(0);
            cropUpSpinner.setValue(0);
            cropDownSpinner.setValue(0);
            rotateSpinner.setValue(0);
            dontChangeAngle = false;
            dontChangeCropSize = false;
            updateChecksStatus(null);
        }
    }

    /**
     * Change the spacing between rows
     */
    public void changeRowSpacing() {
        if (rows != null && !rows.isEmpty()) {
            for (Object cur : rows) {
                Row row = (Row) cur;
                /*
                 * we force size to be fixed even when size is changed
                 */
                row.setSpaceBetweenPanels(getSpaceBetweenRows());
                row.packX();
                reforceSameHeight(row);
            }
            updateFigure();
            doubleLayerPane2.ROIS.setSelectedShape(current_row);
            createBackup();
        } else {
            blinkAnything(figureList.getParent());
            CommonClassesLight.Warning(this, "Please select a row in the 'Final Figure' List");
        }
    }

    /*
     * TODO clean this mess and deduplicate functions
     */
    /**
     * Reinit R session
     */
    public static void reinitRsession() {
        if (CommonClassesLight.r != null) {
            try {
                CommonClassesLight.r.close();
            } catch (Exception e) {
            }
            try {
                CommonClassesLight.r.reopenConnection();
            } catch (Exception e) {
            }
        } else {
            if (CommonClassesLight.r != null) {
                try {
                    CommonClassesLight.r.reopenConnection();
                } catch (REXPMismatchException ex) {
                }
            } else {
                try {
                    CommonClassesLight.r = new MyRsessionLogger();
                    CommonClassesLight.r.reopenConnection();
                } catch (Exception e) {
                }
            }
        }
    }

    public void checkStatus() {
        if (Rstatus != null && CommonClassesLight.r != null && CommonClassesLight.r.isRserverRunning()) {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/green_light.png")));
            Rstatus.setText("R con opened");
        } else {
            Rstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/red_light.png")));
            Rstatus.setText("R con closed");
        }
    }

    /**
     *
     * restores the connection to R
     */
    public void getRserver() {
        if (CommonClassesLight.r == null || !CommonClassesLight.isRReady()) {
            try {
                reinitRsession();
            } catch (Exception e) {
            }
        }
        checkStatus();
    }

    /**
     * shows appropriate options according to the passed source
     *
     * @param src source
     */
    public final void showAppropriateOptions(Object src) {
        if (src == null) {
            optionsPanel.removeAll();
            optionsPanel.validate();
            optionsPanel.repaint();
            return;
        }
        if (src == myList1 || src == myList1.list) {
            if (autoSwitchTopreview) {
                if (jTabbedPane1.getSelectedIndex() != 0) {
                    jTabbedPane1.setSelectedIndex(0);
                }
                ief.setVisible(false);
            }
            optionsPanel.removeAll();
            optionsPanel.add(jPanel1);
            optionsPanel.validate();
            optionsPanel.repaint();
        } else if (src == tableList) {
            if (jTabbedPane1.getSelectedIndex() != 1) {
                jTabbedPane1.setSelectedIndex(1);
            }
            getCurrentgetPanel();
            optionsPanel.removeAll();
            optionsPanel.add(jPanel12);
            optionsPanel.add(jPanel9);
            changeSpaceBetweenImageInPanel.setVisible(true);
            changeSpaceBetweenRows.setVisible(false);
            optionsPanel.validate();
            optionsPanel.repaint();
        } else if (src == figureList) {
            if (jTabbedPane1.getSelectedIndex() != 2) {
                jTabbedPane1.setSelectedIndex(2);
            }
            getCurrentRow();
            if (current_row != null) {
                doubleLayerPane2.ROIS.setSelectedShape(current_row);
            }
            ief.setVisible(false);
            optionsPanel.removeAll();
            optionsPanel.add(jPanel6);
            optionsPanel.add(jPanel9);
            changeSpaceBetweenImageInPanel.setVisible(false);
            changeSpaceBetweenRows.setVisible(true);
            optionsPanel.add(jPanel1666);
            optionsPanel.validate();
            optionsPanel.repaint();
        } else if (src == tableContentList || src == imagesInFigureList) {
            optionsPanel.removeAll();
            optionsPanel.add(imageSizePanel);
            if (src == tableContentList) {
                if (jTabbedPane1.getSelectedIndex() != 1) {
                    jTabbedPane1.setSelectedIndex(1);
                }
                swapImagesFromCurrentPanel.setVisible(true);
                optionsPanel.add(jPanel11);
                optionsPanel.add(jPanel9);
                changeSpaceBetweenImageInPanel.setVisible(true);
                changeSpaceBetweenRows.setVisible(false);
                ief.setCurSel(cur_sel_image1);
                loadCurSelParameters(cur_sel_image1);
                getCurrentgetPanel();
            } else {
                if (jTabbedPane1.getSelectedIndex() != 2) {
                    jTabbedPane1.setSelectedIndex(2);
                }
                getCurrentRow();
                ief.setCurSel(cur_sel_image2);
                loadCurSelParameters(cur_sel_image2);
                doubleLayerPane2.ROIS.setSelectedShape(cur_sel_image2);
                swapImagesFromCurrentPanel.setVisible(false);
                optionsPanel.add(jPanel11);
                optionsPanel.add(jPanel9);
                changeSpaceBetweenImageInPanel.setVisible(false);
                changeSpaceBetweenRows.setVisible(true);
            }
            optionsPanel.add(jPanel7);
            if (src == tableContentList) {
                optionsPanel.add(rotateNFlip);
            }
            optionsPanel.add(jPanel10);
            optionsPanel.validate();
            optionsPanel.repaint();
        } else if (src == RowContentList) {
            if (RowContentList.getSelectedIndex() == -1) {
                return;
            }
            if (jTabbedPane1.getSelectedIndex() != 2) {
                jTabbedPane1.setSelectedIndex(2);
            }
            getCurrentRow();
            if (RowContentList.getSelectedIndex() != -1) {
                doubleLayerPane2.ROIS.setSelectedShape(RowContentList.getSelectedValue());
            } else {
                doubleLayerPane2.ROIS.setSelectedShape(null);
            }
            ief.setVisible(false);
            optionsPanel.removeAll();
            optionsPanel.add(jPanel12);
            optionsPanel.add(jPanel16);
            optionsPanel.add(jPanel9);
            changeSpaceBetweenImageInPanel.setVisible(false);
            changeSpaceBetweenRows.setVisible(true);
            optionsPanel.validate();
            optionsPanel.repaint();
        }
    }

    /**
     * This function is called when the selection in the image list has been
     * changed
     *
     * @param evt
     */
    public void img_list_valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            doubleLayerPane3.ROIS.clearROIs();
            /*
             * small fix to prevent weird behaviour when deleting images from a Panel
             */
            if (myList1.getSelectedIndex() == -1) {
                return;
            }
            showAppropriateOptions(myList1);
            if (myList1.getSelectedIndex() >= 0 && myList1.getSelectedIndex() < myList1.Size()) {
                String sel = myList1.getSelectedValue();
                if (!sel.contains("importJ:")) {
                    if (!sel.toLowerCase().endsWith(".svg") && !sel.toLowerCase().endsWith(".figur")) {
                        setImageDisplay(new MyImage2D.Double(0, 0, myList1.getSelectedValue()));
                    } else if (sel.toLowerCase().endsWith(".figur")) {
                        MyPlotVector.Double tmp = (MyPlotVector.Double) (new Loader().loadObjectRaw(sel));
                        setImageDisplay(tmp);//new MyImage2D.Double(0, 0, new Loader().loadSVG2BufferedImage(tmp.getDocument())));
                    } else {
                        setImageDisplay(new MyImageVector.Double(new Loader().loadSVGDocument(sel)));
                    }
                } else {
                    if (imported_from_J.containsKey(sel)) {
                        setImageDisplay(new MyImage2D.Double(0, 0, sel, imported_from_J.get(sel).getBufferedImage()));
                    }
                }
            }
        }
    }

    /**
     * Sets the montage to display
     *
     * @param montage
     */
    public void setCurrentDisplay(Montage montage) {
        doubleLayerPane1.ROIS.resetROIS();
        doubleLayerPane1.ROIS.addROIs(new ArrayList<Object>(montage.getGroup()));
        Rectangle2D r2d = montage.getBounds2D();
        doubleLayerPane1.resizePanel((int) (r2d.getX() + r2d.getWidth() + 1.), (int) (r2d.getY() + r2d.getHeight() + 1.));
    }

    /**
     * Sets the image to display
     *
     * @param cur_img
     */
    public void setImageDisplay(Object cur_img) {
        doubleLayerPane3.ROIS.resetROIS();
        if (cur_img != null) {
            doubleLayerPane3.ROIS.add(cur_img);
            try {
                Rectangle2D r2d = ((PARoi) cur_img).getBounds2D();
                doubleLayerPane3.resizePanel((int) (r2d.getX() + r2d.getWidth() + 1.), (int) (r2d.getY() + r2d.getHeight() + 1.));
            } catch (Exception e) {
            }
        }
    }

    /**
     * Sets the current figure to display
     *
     * @param shapes
     */
    public void setCurrentFigure(ArrayList<Object> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            return;
        }
        doubleLayerPane2.ROIS.resetROIS();
        doubleLayerPane2.ROIS.addROIs(shapes);
        ComplexShapeLight cs = new ComplexShapeLight(shapes);
        Rectangle2D r2d = cs.getBounds2D();
        doubleLayerPane2.resizePanel((int) (r2d.getX() + r2d.getWidth() + 1.), (int) (r2d.getY() + r2d.getHeight() + 1.));
    }

    /**
     * Set the current display according to the selected tab
     *
     * @param shapes
     */
    public void setCurrentDisplay(ArrayList<Object> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            return;
        }
        doubleLayerPane1.ROIS.resetROIS();
        doubleLayerPane1.ROIS.addROIs(shapes);
        ComplexShapeLight cs = new ComplexShapeLight(shapes);
        Rectangle2D r2d = cs.getBounds2D();
        doubleLayerPane1.resizePanel((int) (r2d.getX() + r2d.getWidth() + 1.), (int) (r2d.getY() + r2d.getHeight() + 1.));

    }

    /**
     * Swap the position of two nbRows in the figure
     *
     * @param c list of nbRows
     * @param first_pos
     * @param second_pos
     */
    public void swapRows(List c, int first_pos, int second_pos) {
        if (c != null && !c.isEmpty()) {
            Collections.swap(c, first_pos, second_pos);
        }
    }

    /**
     * Swap the position of two objects in a list
     *
     * @param c listModel
     * @param first_pos
     * @param second_pos
     */
    public void swapList(DefaultListModel c, int first_pos, int second_pos) {
        if (c != null && !c.isEmpty()) {
            Object tmp = c.get(first_pos);
            c.setElementAt(c.get(second_pos), first_pos);
            c.setElementAt(tmp, second_pos);
        }
    }

    /**
     * deletes an object from a list by its position in the list
     *
     * @param list
     * @param pos
     */
    private void deleteFromList(DefaultListModel list, int pos) {
        if (pos >= 0 && pos < list.getSize()) {
            list.remove(pos);
        }
    }

    /**
     * Remove a Montage by its name
     *
     * @param panelNb
     */
    public void removeMontage(String panelNb) {
        removeMontage(CommonClassesLight.String2Int(panelNb));
    }

    /**
     * Remove a montage by its position/id
     *
     * @param panelNb
     */
    public void removeMontage(int panelNb) {
        if (panels != null) {
            if (remove_when_added) {
                Montage b = panels.get(panelNb);
                HashSet<Object> group = b.getGroup();
                ArrayList<String> files_to_add = new ArrayList<String>();
                for (Object object : group) {
                    if (object instanceof Namable) {
                        String name = ((Namable) object).getFullName();
                        if (name != null && name.contains("importJ:")) {
                            name = CommonClassesLight.change_path_separators_to_system_ones(name);
                            SerializableBufferedImage2 sb = ((MyImage2D) object).getOriginalImage();
                            if (!imported_from_J.containsKey(name)) {
                                imported_from_J.put(name, sb);
                            } else {
                                while (imported_from_J.containsKey(name)) {
                                    name += "2";
                                }
                                imported_from_J.put(name, sb);
                            }
                            myList1.addDirectlyToList(name, sb.getBufferedImage());
                        } else {
                            if (name != null) {
                                name = CommonClassesLight.change_path_separators_to_system_ones(name);
                            }
                            if (name != null) {//bug fix for color blind splits
                                if (new File(name).exists()) {
                                    files_to_add.add(name);
                                }
                            }
                        }
                    }
                }
                myList1.addAllNoCheck(files_to_add);
            }
            panels.remove(panelNb);
        }
    }

    /**
     * Converts dpi to an affine transform (i.e. converts dpi to a scaling
     * factor)
     *
     * @param g2d
     * @param desired_resolution
     */
    public void setATforDPI(Graphics2D g2d, double desired_resolution) {
        double zoom = getMagnificationForDesiredResolution(desired_resolution);
        if (zoom != 1.) {
            AffineTransform trans = new AffineTransform();
            trans.scale(zoom, zoom);
            g2d.setTransform(trans);
        }
    }

    private double getMagnificationForDesiredResolution(double desired_resolution) {
        double zoom = desired_resolution / resolution;
        return zoom;
    }

    /**
     *
     * @param drawable drawable vectorial object
     * @param desired_resolution desired output dpi
     * @param hasAlpha if true the image has an alpha transparency
     * @return an image rescaled to mimmick a dpi change
     */
    public BufferedImage getImageAtfinalDpi(Drawable drawable, double desired_resolution, boolean hasAlpha) {
        double zoom = getMagnificationForDesiredResolution(desired_resolution);
        Rectangle2D r2d = ((MyRectangle2D) drawable).getBounds2D();
        int type = BufferedImage.TYPE_INT_RGB;
        if (hasAlpha) {
            type = BufferedImage.TYPE_INT_ARGB;
        }
        BufferedImage tmp = new BufferedImage((int) ((Math.rint(r2d.getX() + r2d.getWidth())) * zoom), (int) ((Math.rint(r2d.getY() + r2d.getHeight())) * zoom), type);
        return tmp;
    }

    /**
     * Export images to svg
     *
     * @param g2d the vectorial graphics2D
     * @param fichier name of the output file
     * @param dpi output resolution (72dpi --> illustrator 90dpi --> inkscape)
     */
    public void saveAsSVG(SVGGraphics2D g2d, String fichier, double dpi) {
        SVGDocument doc = (SVGDocument) g2d.getDOMFactory();
        Element root = doc.getDocumentElement();
        /*
         * Illustrator always uses a 72 dpi resolution
         * Inkscape uses a 90 dpi resolution
         * (that would have been too simple otherwise)
         * http://inkscape-forum.andreas-s.net/topic/202198
         * http://www.inkscapeforum.com/viewtopic.php?f=20&t=836
         */
        /*
         * 21 X 29.7cm in pixels at 72 dpi
         * 595.275590551181
         * 841.8897637795275
         */
        if (dpi > 76) {
            /*
             * 90 dpi resolution (Inkscape)
             */
            root.setAttributeNS(null, "width", "744.1");
            root.setAttributeNS(null, "height", "1052.3625");
            root.setAttributeNS(null, "viewBox", "0 0 744.1 1052.3625");
        } else {
            /*
             * default (Illustrator) resolution 72 dpi
             */
            root.setAttributeNS(null, "width", "595.28");
            root.setAttributeNS(null, "height", "841.89");
            root.setAttributeNS(null, "viewBox", "0 0 595.28 841.89");
        }
        g2d.getRoot(root);
        g2d.dispose();
        SaverLight.saveAsSVG(doc, fichier);
    }

    /**
     * This method saves the current Drawable object to a file or sends it to IJ
     *
     * @param drawable drawable object
     * @param FORMAT export format (tif, jpg, IJ, ...)
     * @param name output name
     */
    private void SaveBlock(Drawable drawable, int FORMAT, String name) {
        JournalParameters jp = null;
        if (journalCombo.getSelectedIndex() != -1) {
            jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
        }
        try {
            switch (FORMAT) {
                /*
                 * Save as svg
                 */
                case FORMAT_SVG:
                    if (true) {
                        VectorOutputParameters iopane = new VectorOutputParameters();
                        if (name == null) {
                            name = CommonClassesLight.save(this, lastExportFolder, "svg", iopane);
                            if (name != null) {
                                lastExportFolder = name;
                            }
                        }
                        recordSave(name);
                        if (name != null) {
                            double unique_old_width = 0;
                            ArrayList<Double> widths = new ArrayList<Double>();
                            if (iopane.getDpi() > 76) {
                                if (drawable instanceof Montage) {
                                    double width_in_px = Converter.cmToPxAnyDpi(getSizeInCm(), 90);
                                    unique_old_width = ((Montage) drawable).getWidth();
                                    ((Montage) drawable).setToWidth(width_in_px);
                                } else if (jTabbedPane1.getSelectedIndex() == 2 && current_row != null) {
                                    if (drawable instanceof Figure) {
                                        ArrayList<Object> selRows = ((Figure) drawable).getRows();
                                        for (Object o : selRows) {
                                            Row row = ((Row) o);
                                            double width_in_px = Converter.cmToPxAnyDpi(getSizeInCm(), 90);
                                            int new_width = ((int) Math.round(width_in_px));
                                            double old_width = row.getWidth();
                                            widths.add(old_width);
                                            if (rowAutoSameHeight) {
                                                row.setToWidth(getSizeInPx());
                                                row.sameHeight(getSpaceBetweenRows());
                                            }
                                            row.setToWidth(new_width);
                                            if (rowAutoSameHeight) {
                                                row.sameHeight(getSpaceBetweenRows());
                                            }
                                        }
                                        drawable = new Figure(selRows, getSpaceBetweenRows());
                                    }
                                }
                            }
                            MyGraphics2D g2d = new MyGraphics2D(MyGraphics2D.SVG_ONLY);
                            drawable.drawAndFill(g2d);
                            saveAsSVG(g2d.getGraphics2DSVG(), name, iopane.getDpi());
                            g2d.dispose();
                            if (iopane.getDpi() > 76) {
                                if (drawable instanceof Montage) {
                                    ((Montage) drawable).setToWidth(unique_old_width);
                                } else if (jTabbedPane1.getSelectedIndex() == 2 && current_row != null) {
                                    if (drawable instanceof Figure) {
                                        ArrayList<Object> selRows = ((Figure) drawable).getRows();
                                        int counter = 0;
                                        for (Object o : selRows) {
                                            Row row = ((Row) o);
                                            if (rowAutoSameHeight) {
                                                row.setToWidth(getSizeInPx());
                                                row.sameHeight(getSpaceBetweenRows());
                                            }
                                            row.setToWidth(widths.get(counter));
                                            if (rowAutoSameHeight) {
                                                row.sameHeight(getSpaceBetweenRows());
                                            }
                                            counter++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case FORMAT_TIFF:
                    /*
                     * Save as tif
                     */
                    if (true) {
                        SelectABgColor iopane = new SelectABgColor(true, true, jp);
                        if (name == null) {
                            name = CommonClassesLight.save(this, lastExportFolder, "tif", iopane);
                            if (name != null) {
                                lastExportFolder = name;
                            }
                        }
                        recordSave(name);
                        if (name != null) {
                            BufferedImage tmp = getImageAtfinalDpi(drawable, iopane.getDPI(), false);
                            int w = tmp.getWidth();
                            int h = tmp.getHeight();
                            int color = iopane.getBgColor();
                            for (int i = 0; i < w; i++) {
                                for (int j = 0; j < h; j++) {
                                    tmp.setRGB(i, j, color);
                                }
                            }
                            Graphics2D g2d = tmp.createGraphics();
                            CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                            setATforDPI(g2d, iopane.getDPI());
                            drawable.drawAndFill(g2d);
                            logMagnificationChanges(iopane.logMagnificationChanges(), iopane.getDPI(), name, drawable);
                            g2d.dispose();
                            if (iopane.isForceGray()) {
                                tmp = toGray(tmp);
                            }
                            SaverLight.save(tmp, name);
                        }
                    }
                    break;
                case FORMAT_PNG:
                    /*
                     * Save as png with a transparent background
                     */
                    if (true) {
                        SelectABgColor iopane = new SelectABgColor(false, true, jp);
                        if (name == null) {
                            name = CommonClassesLight.save(this, lastExportFolder, "png", iopane);
                            if (name != null) {
                                lastExportFolder = name;
                            }
                        }
                        recordSave(name);
                        if (name != null) {
                            BufferedImage tmp = getImageAtfinalDpi(drawable, iopane.getDPI(), true);
                            /*
                             * We make the background of the image transparent --> better for insertion in a ppt presentation for example
                             */
                            for (int i = 0; i < tmp.getWidth(); i++) {
                                for (int j = 0; j < tmp.getHeight(); j++) {
                                    tmp.setRGB(i, j, 0x00000000);
                                }
                            }
                            Graphics2D g2d = tmp.createGraphics();
                            CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                            //en fait il me suffit d'appliquer un zoom ici aussi pour avoir le dessin fait correctement je pense mais a tester
                            setATforDPI(g2d, iopane.getDPI());
                            drawable.drawAndFill(g2d);
                            logMagnificationChanges(iopane.logMagnificationChanges(), iopane.getDPI(), name, drawable);
                            g2d.dispose();
                            if (iopane.isForceGray()) {
                                tmp = toGray(tmp);
                            }
                            SaverLight.save(tmp, name);
                        }
                    }
                    break;
                case FORMAT_IJ:
                    /*
                     * export to ImageJ
                     */
                    if (true) {
                        recordSave("IJ/FIJI");
                        SelectABgColor iopane = new SelectABgColor(true, true, jp);
                        int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Please select a background color", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            BufferedImage tmp = getImageAtfinalDpi(drawable, iopane.getDPI(), false);
                            int w = tmp.getWidth();
                            int h = tmp.getHeight();
                            int color = iopane.getBgColor();
                            for (int i = 0; i < w; i++) {
                                for (int j = 0; j < h; j++) {
                                    tmp.setRGB(i, j, color);
                                }
                            }
                            Graphics2D g2d = tmp.createGraphics();
                            CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                            setATforDPI(g2d, iopane.getDPI());
                            drawable.drawAndFill(g2d);
                            logMagnificationChanges(iopane.logMagnificationChanges(), iopane.getDPI(), null, drawable);
                            g2d.dispose();
                            if (iopane.isForceGray()) {
                                tmp = toGray(tmp);
                            }
                            CommonClassesLight.sendToimageJ(tmp);
                        }
                    }
                    break;
                case FORMAT_JPEG:
                    /*
                     * Saves as jpeg
                     */
                    if (true) {
                        SelectABgColor iopane = new SelectABgColor(true, true, jp);
                        if (name == null) {
                            name = CommonClassesLight.save(this, lastExportFolder, "jpg", iopane);
                            if (name != null) {
                                lastExportFolder = name;
                            }
                        }
                        recordSave(name);
                        if (name != null) {
                            BufferedImage tmp = getImageAtfinalDpi(drawable, iopane.getDPI(), false);
                            int w = tmp.getWidth();
                            int h = tmp.getHeight();
                            int color = iopane.getBgColor();
                            for (int i = 0; i < w; i++) {
                                for (int j = 0; j < h; j++) {
                                    tmp.setRGB(i, j, color);
                                }
                            }
                            Graphics2D g2d = tmp.createGraphics();
                            CommonClassesLight.setHighQualityAndLowSpeedGraphics(g2d);
                            setATforDPI(g2d, iopane.getDPI());
                            drawable.drawAndFill(g2d);
                            logMagnificationChanges(iopane.logMagnificationChanges(), iopane.getDPI(), name, drawable);
                            g2d.dispose();
                            if (iopane.isForceGray()) {
                                tmp = toGray(tmp);
                            }
                            SaverLight.save(tmp, name);
                        }
                    }
                    break;
            }
        } catch (OutOfMemoryError E) {
            StringWriter sw = new StringWriter();
            E.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            try {
                /*
                 * we try to clean the memory
                 */
                System.gc();
                CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
            } catch (Exception e) {
            }
        }
    }

    private void logMagnificationChanges(boolean logMagnificationChanges, int exportDPI, String name, Object image) {
        //modification for review
        if (logMagnificationChanges) {
            try {
                if (image instanceof MagnificationChangeLoggable) {
                    String magLog = ((MagnificationChangeLoggable) image).logMagnificationChanges(getMagnificationForDesiredResolution(exportDPI));
                    /**
                     * we just clean a bit the log string
                     */
                    magLog = magLog.replace("\n\n\n", "\n\n");
                    magLog = magLog.replace("\n\n\n", "\n\n");
                    while (magLog.endsWith("\n")) {
                        magLog = magLog.substring(0, magLog.length() - 1);
                    }
                    if (name != null) {
                        new MyWriter().apply(name + ".log.txt", magLog);
                    } else {
                        CommonClassesLight.Warning2(this, magLog);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //save the file as file.log.txt
        }
    }

    /**
     *
     * @param in input RGB image
     * @return a gray version of the image
     */
    private BufferedImage toGray(BufferedImage in) {
        int width = in.getWidth();
        int height = in.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int RGB = in.getRGB(i, j);
                /*
                 * we preserve transparency
                 */
                int alpha = ((RGB >> 24) & 0xFF);
                int red = ((RGB >> 16) & 0xFF);
                int green = ((RGB >> 8) & 0xFF);
                int blue = (RGB & 0xFF);
                int RGB2gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int gray = (alpha << 24) | (RGB2gray << 16) | (RGB2gray << 8) | RGB2gray;
                in.setRGB(i, j, gray);
            }
        }
        return in;
    }

    public boolean warnIfChecksNotPerformed(Object in) {
        boolean mustWarn = false;
        boolean isFig = false;
        if (in instanceof Montage) {
            Montage tmp = (Montage) in;
            if (!tmp.wasCheckedForFonts || !tmp.wasCheckedForGraphs || !tmp.wasCheckedForLineArts || !tmp.wasCheckedForSize || !tmp.wasCheckedForStyle || !tmp.wasCheckedForText) {
                mustWarn = true;
            }
        } else if (in instanceof Row) {
            Row tmp = (Row) in;
            if (!tmp.wasCheckedForFonts || !tmp.wasCheckedForGraphs || !tmp.wasCheckedForLineArts || !tmp.wasCheckedForSize || !tmp.wasCheckedForStyle || !tmp.wasCheckedForText) {
                mustWarn = true;
            }
        } else if (in instanceof Figure) {
            isFig = true;
            for (Object r : rows) {
                if (r instanceof Row) {
                    Row tmp = ((Row) r);
                    if (!tmp.wasCheckedForFonts || !tmp.wasCheckedForGraphs || !tmp.wasCheckedForLineArts || !tmp.wasCheckedForSize || !tmp.wasCheckedForStyle || !tmp.wasCheckedForText) {
                        mustWarn = true;
                        break;
                    }
                }
            }
        }
        if (mustWarn) {
            //TODO faire un blinker qui peut blinker plusieurs composants a la fois
            blinkAnything(checkFont);
            String additionalTextForFigs = "";
            if (isFig) {
                additionalTextForFigs = "<br>NB: maybe only some of the rows of your figure haven't been controlled";
            }
            JLabel warning = new JLabel("<html><font color=\"#FF0000\">We recommend you perform all checks before<br>exporting your image for a publication.<br>Buttons with a red icon indicate missing controls.<br>Buttons with a green icon indicate that the check was run at least once." + additionalTextForFigs + "<br><br>Click 'Cancel' to abort export and run the controls.</font>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{warning}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Export an image to a raster or a vector image
     *
     * @param format output format (i.e. svg, ppt, jpg, ...)
     * @param name output file name
     */
    public void export(int format, String name) {
        JDialog jd = null;
        try {
            jd = new JDialog(this, "Saving...");
            JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when saving is over!</center></html>");
            jd.add(jl);
            jd.validate();
            jd.pack();
            jd.setLocationRelativeTo(this);
            jd.setVisible(true);
            if (jTabbedPane1.getSelectedIndex() == 1) {
                if (tableList.getSelectedIndex() != -1) {
                    int real_pos = getRealPos();
                    Montage b = panels.get(real_pos);
                    if (!warnIfChecksNotPerformed(b)) {
                        return;
                    }
                    SaveBlock(b, format, name);
                } else {
                    blinkAnything(tableList.getParent());
                    CommonClassesLight.Warning(this, "Please select a Panel in the 'Panels' List First !");
                }
            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                if (rows != null && !rows.isEmpty()) {
                    Figure rh = new Figure(rows, getSpaceBetweenRows());
                    if (!warnIfChecksNotPerformed(rh)) {
                        return;
                    }
                    SaveBlock(rh, format, name);
                } else {
                    blinkAnything(figureList.getParent());
                    CommonClassesLight.Warning(this, "Please create a panel or a Figure first!");
                }
            } else {
                CommonClassesLight.Warning(this, "Please create a panel or a Figure first!");
            }
        } catch (Exception e) {
        } finally {
            try {
                if (jd != null) {
                    jd.dispose();
                }
            } catch (Exception e2) {
            }
        }
    }

    /**
     * loads an arraylist containing several yf5m files (prevents concurrent
     * access exceptions)
     *
     * @param names
     * @param add_backup
     */
    public void loadFiles(ArrayList<String> names, boolean add_backup) {
        if (!add_backup) {
            clearAll();
        }
        boolean first = true;
        for (String string : names) {
            loadNonMTFile(string, first);
            first = false;
        }
    }

    /**
     * loads a .yf5m file
     *
     * @param name name of the file to load
     * @param add_backup if true creates a backup before loading new file
     * @param MT if true loading will take place in a separate process
     */
    public void loadFile(final String name, final boolean add_backup, boolean MT) {
        if (name != null) {
            if (!add_backup) {
                clearAll();
            }
            if (MT) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadNonMTFile(name, add_backup);
                        } catch (Exception e) {
                            StringWriter sw = new StringWriter();
                            e.printStackTrace(new PrintWriter(sw));
                            String stacktrace = sw.toString();
                            System.err.println(stacktrace);
                        }
                    }
                }).start();
            } else {
                loadNonMTFile(name, add_backup);
            }
        }
    }

    /**
     * loads a .yf5m file
     *
     * @param name name of the file to load
     * @param add_backup if true creates a backup before loading new file
     */
    public void loadNonMTFile(String name, boolean add_backup) {
        /**
         * bug fix for erroneous saving of files
         */
        warnForSave = isThereAnythingInTheLists();//force save if there was already something in the list and we load another on top
        JDialog jd = null;
        try {
            loading = true;
            /*
             * we pop a window to warn the user that he must wait
             * (maybe a good idea would be to make it modal ?)
             */
            jd = new JDialog(this, "Loading...");
            JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window will be closed automatically when loading is over!</center></html>");
            jd.add(jl);
            jd.validate();
            jd.pack();
            jd.setLocationRelativeTo(this);
            jd.setVisible(true);
            /*
             * we start the loading of the .yf5m file
             */
            PanelCounter = 1;
            Exportable ex = new Exportable();
            /*
             * we recover the serialized exportable object
             */
            ex.load(name);
            HashMap<Integer, Integer> id_transformation_of_montages = new HashMap<Integer, Integer>();
            HashMap<Integer, Montage> cur_panels = ex.getTables();
            if (cur_panels != null && !cur_panels.isEmpty()) {
                ArrayList<Integer> pos = new ArrayList<Integer>(cur_panels.keySet());
                for (Integer cur : pos) {
                    int last_pos = cur;
                    Montage cur_panel = cur_panels.get(cur);
                    /*
                     * we reload text to transient styledDoc and int[] to transient images to memoryRefreshSpeed up drawing of images
                     */
                    cur_panel.reloadAfterSerialization();
                    while (panels.containsKey(cur)) {
                        cur++;
                    }
                    id_transformation_of_montages.put(last_pos, cur);
                    panels.put(cur, cur_panel);
                }
            }
            ArrayList<Integer> allpos = new ArrayList<Integer>(panels.keySet());
            for (Integer integer : allpos) {
                PanelCounter = Math.max(PanelCounter, integer);
            }
            PanelCounter++;
            ArrayList<Object> rows2Add = ex.getRows();
            if (rows2Add != null && !rows2Add.isEmpty()) {
                for (Object row : rows2Add) {
                    String formula = ((Row) row).getFormula();
                    String[] parsed_formulas = formula.split("\\+");
                    int i = 0;
                    for (String string : parsed_formulas) {
                        parsed_formulas[i] = id_transformation_of_montages.get(Integer.parseInt(string)) + "";
                        i++;
                    }
                    String modified_formula = "";
                    for (String string : parsed_formulas) {
                        modified_formula += "+" + string;
                    }
                    if (modified_formula.startsWith("+")) {
                        modified_formula = modified_formula.substring(1);
                    }
                    ((Row) row).setFormula(modified_formula);
                }
            }
            rows.addAll(rows2Add);
            myList1.addAllIfNotAlreadyContained(ex.getList_of_files());
            styles.reloadStyles(journalCombo);
            defaultStyle = null;
            journalCombo.setSelectedItem(ex.selectedStyle);
            if (journalCombo.getSelectedItem() != null) {
                if (!journalCombo.getSelectedItem().toString().equals(ex.selectedStyle)) {
                    defaultStyle = null;
                    journalCombo.setSelectedIndex(-1);
                } else {
                    int idx = journalCombo.getSelectedIndex();
                    if (idx != -1) {
                        if ((tableListModel.isEmpty() && jTabbedPane1.getSelectedIndex() == 1) || (figureListModel.isEmpty() && jTabbedPane1.getSelectedIndex() == 2)) {
                            defaultStyle = PopulateJournalStyles.journalStyles.get(idx);
                            setRuler();
                        }
                    }
                }
            }
            /**
             * super dirty bug fix for DND error caused by jlist refresh if in a
             * different thread than the main thread
             */
            tableList.setModel(new DefaultListModel());
            figureList.setModel(new DefaultListModel());
            RowContentList.setModel(new DefaultListModel());
            tableListModel.clear();
            figureListModel.clear();
            RowContentListModel.clear();
            imagesInFigureModel.clear();
            HashMap<String, SerializableBufferedImage2> imports = ex.getImageJImports();
            HashMap<String, String> name_transformation = new HashMap<String, String>();
            if (imports != null && !imports.isEmpty()) {
                for (Map.Entry<String, SerializableBufferedImage2> entry : imports.entrySet()) {
                    if (!ScientiFig_.imported_from_J.containsKey(entry.getKey())) {
                        ScientiFig_.imported_from_J.put(entry.getKey(), entry.getValue());
                        name_transformation.put(entry.getKey(), entry.getKey());
                        myList1.addDirectlyToList(entry.getKey(), entry.getValue().getBufferedImage());
                    } else {
                        String new_name = entry.getKey() + "2";
                        while (ScientiFig_.imported_from_J.containsKey(new_name)) {
                            new_name += "2";
                        }
                        ScientiFig_.imported_from_J.put(new_name, entry.getValue());
                        name_transformation.put(entry.getKey(), new_name);
                        myList1.addDirectlyToList(new_name, entry.getValue().getBufferedImage());
                    }
                }
            }
            HashMap<String, Double> importsSize = ex.getImageJImportsPxSize();
            if (importsSize != null && !importsSize.isEmpty()) {
                for (Map.Entry<String, Double> entry : importsSize.entrySet()) {
                    imported_from_pixel_size.put(name_transformation.get(entry.getKey()), entry.getValue());
                }
            }
            /*
             * we check if we need to have a rsession active or not (i.e. we check whether the file contains R graphs or not)
             */
            boolean requireRsession = false;

            if (panels != null && !panels.isEmpty()) {
                loopR:
                for (Map.Entry<Integer, Montage> entry : panels.entrySet()) {
                    int pos = entry.getKey();
                    tableListModel.addElement(/*lock + */pos);
                    if (!requireRsession) {
                        if ((((Montage) entry.getValue()).requiresRsession())) {
                            requireRsession = true;
                        }
                    }
                }
            }

            if (rows != null && !rows.isEmpty()) {
                for (Object row : rows) {
                    /*
                     * removed this cause it was redundant with the previous because all rows are made of panels and these panels are not removed from panels
                     */
                    String formula = ((Row) row).getFormula();
                    ((Row) row).reloadAfterSerialization();
                    String[] parsed_formulas = formula.split("\\+");
                    figureListModel.addElement(formula);
                    for (String string : parsed_formulas) {
                        try {
                            tableListModel.removeElement(CommonClassesLight.String2Int(string));
                        } catch (Exception e) {
                            StringWriter sw = new StringWriter();
                            e.printStackTrace(new PrintWriter(sw));
                            String stacktrace = sw.toString();
                            System.err.println(stacktrace);
                        }
                    }
                }
            }

            /*
             * if needed we open a connection to R
             * connecting to R is a very time consuming process (probably because I load several libraries)
             */
            if (requireRsession && (CommonClassesLight.r == null || !CommonClassesLight.r.isRserverRunning())) {
                try {
                    checkRstatus();
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stacktrace = sw.toString();
                    System.err.println(stacktrace);
                }
            }
            updateFigure();
            /*
             * if loading failed we set the name to null
             */
            if (!rows.isEmpty() || !panels.isEmpty()) {
                lastSaveName = name;
            } else {
                lastSaveName = null;
            }
            updateTitle(lastSaveName);

            name_to_load = null;
            if (add_backup) {
                createBackup();
            }
            /*
             * super dirty bug fix for DND error of jlist refresh --> needs to do the threading shit better
             * we need to put this here to avoid problems of loading
             */
            tableList.setModel(tableListModel);
            figureList.setModel(figureListModel);
            RowContentList.setModel(RowContentListModel);
            updateGlassPane();
            loading = false;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            loading = false;
        } catch (OutOfMemoryError E) {
            /**
             * file too big to be loaded
             */
            StringWriter sw = new StringWriter();
            E.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            loading = false;
            try {
                System.gc();
            } catch (Exception e) {
            }
            CommonClassesLight.Warning(this, "Sorry, not enough memory!\nPlease restart the software with more memory.");
        } finally {
            try {
                if (jd != null) {
                    jd.dispose();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     * @return space between rows
     */
    private static int getSpaceBetweenRows() {
        return ((Integer) changeSpaceBetweenRows.getValue());
    }

    /**
     * sets space between rows
     *
     * @param space
     */
    private static void setSpaceBetweenRows(double space) {
        changeSpaceBetweenRows.setValue((int) space);
    }

    /**
     * Clear all drawing areas and all lists, ... and tries to free some memory
     */
    public void clearAll() {
        cur_sel_image1 = null;
        cur_sel_image2 = null;
        current_row = null;
        curPanel = null;
        lastSaveName = null;
        updateTitle(lastSaveName);
        lastExportFolder = null;
        rows.clear();
        panels.clear();
        imported_from_J.clear();
        myList1.clearList();
        tableContentListModel.clear();
        tableListModel.clear();
        figureListModel.clear();
        RowContentListModel.clear();
        imagesInFigureModel.clear();
        doubleLayerPane1.ROIS.clearROIs();
        doubleLayerPane2.ROIS.clearROIs();
        doubleLayerPane3.ROIS.clearROIs();
        showHelpVideoForClickedButton = false;
        /**
         * necessary to reactivate DND in case of errors
         */
        loading = false;
        /*
         * try to flush memory
         */
        try {
            System.gc();
        } catch (Exception e) {
        }
        updateGlassPane();
    }

    private int getRealPos() {
        return getRealPos(tableList.getSelectedIndex());
    }

    private int getRealPos(int pos) {
        String value = tableListModel.elementAt(pos).toString();
        if (value.contains(">")) {
            value = CommonClassesLight.strCutRightLast(value, ">");
        }
        int real_pos = CommonClassesLight.String2Int(value);
        return real_pos;
    }

    /**
     * gets the selected panel
     */
    private void getCurrentgetPanel() {
        if (tableList.getSelectedIndex() != -1) {
            int real_pos = getRealPos();
            curPanel = panels.get(real_pos);
            loadCurPanelParameters();
        } else {
            curPanel = null;
        }
    }

    /**
     * gets the selected row
     */
    public void getCurrentRow() {
        if (figureList.getSelectedIndex() != -1) {
            int[] tmp = figureList.getSelectedIndices();
            if (tmp != null && tmp.length != 1) {
                ArrayList<Object> sel = new ArrayList<Object>();
                for (int object : tmp) {
                    sel.add(rows.get(object));
                }
                current_row = sel;
                cur_sel_image1 = null;
                ief.setCurSel(null);
            } else {
                current_row = rows.get(figureList.getSelectedIndex());
            }
        } else {
            current_row = null;
        }
    }

    /**
     * this function is called when the selection has changed in the figure list
     *
     * @param evt
     */
    private void figureListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_figureListValueChanged
        if (loading) {
            return;
        }
        //modification for review
        if (evt == null || !evt.getValueIsAdjusting()) {
            if (jTabbedPane1.getSelectedIndex() != 2) {
                jTabbedPane1.setSelectedIndex(2);
            }
            RowContentListModel.clear();
            imagesInFigureModel.clear();
            showAppropriateOptions(figureList);
            if (current_row != null && current_row instanceof Row) {
                loading = true;
                ArrayList<Object> montages = ((Row) current_row).blocks;
                int counter = 1;
                for (Object object : montages) {
                    if (object instanceof Montage) {
                        Montage tmp2 = (Montage) object;
                        tmp2.setMontageID(counter);
                        counter++;
                        RowContentListModel.addElement(tmp2);
                    }
                }
                updatePositionFigure(current_row);
                loading = false;
            }
            if (current_row instanceof ArrayList) {
                doubleLayerPane2.ROIS.setSelectedShape(new ComplexShapeLight((ArrayList<Object>) current_row));
            } else {
                doubleLayerPane2.ROIS.setSelectedShape(current_row);
            }
            /*
             * We move to the selected row --> easier to see it
             */
            loadCurPanelParameters();
            if (figureListModel.isEmpty()) {
                rows.clear();
                RowContentListModel.clear();
                imagesInFigureModel.clear();
            }
            updateGlassPane();
        }
    }//GEN-LAST:event_figureListValueChanged

    /**
     * this function is called if an image is selected in a panel
     *
     * @param evt
     */
    private void newObjectSelected(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_newObjectSelected
        if (inactivateListUpdate) {
            return;
        }
        if (!evt.getValueIsAdjusting()) {
            int pos = tableList.getSelectedIndex();
            int[] pos2 = tableContentList.getSelectedIndices();
            showAppropriateOptions(tableContentListModel);
            if (pos == -1) {
                tableContentListModel.clear();
                cur_sel_image1 = null;
                ief.setCurSel(null);
                return;
            }
            if (pos2 != null & pos2.length >= 1) {
                dontChangeCropSize = true;
                dontChangeAngle = true;
                if (pos2.length > 1) {
                    int min_left = Integer.MAX_VALUE;
                    int min_right = Integer.MAX_VALUE;
                    int min_up = Integer.MAX_VALUE;
                    int min_down = Integer.MAX_VALUE;
                    int min_angle = Integer.MAX_VALUE;
                    ArrayList<Object> images = new ArrayList<Object>();
                    for (int i : pos2) {
                        Object obj = tableContentListModel.get(i);
                        images.add(obj);
                        MyImage2D tmpImg = (MyImage2D) obj;
                        if (obj instanceof MyImage2D) {
                            min_left = Math.min(min_left, tmpImg.getLeft_crop());
                            min_right = Math.min(min_right, tmpImg.getRight_crop());
                            min_up = Math.min(min_up, tmpImg.getUp_crop());
                            min_down = Math.min(min_down, tmpImg.getDown_crop());
                            min_angle = Math.min(min_angle, (int) tmpImg.getTheta());
                        }
                    }
                    cur_sel_image1 = new ComplexShapeLight(images);
                    min_left = min_left == Integer.MAX_VALUE ? 0 : min_left;
                    min_right = min_right == Integer.MAX_VALUE ? 0 : min_right;
                    min_up = min_up == Integer.MAX_VALUE ? 0 : min_up;
                    min_down = min_down == Integer.MAX_VALUE ? 0 : min_down;
                    min_angle = min_angle == Integer.MAX_VALUE ? 0 : min_angle;
                    cropLeftSpinner.setValue(min_left);
                    cropRightSpinner.setValue(min_right);
                    cropUpSpinner.setValue(min_up);
                    cropDownSpinner.setValue(min_down);
                    rotateSpinner.setValue(min_angle);
                } else {
                    Object obj = tableContentList.getSelectedValue();
                    cur_sel_image1 = obj;
                    MyImage2D tmpImg = (MyImage2D) obj;
                    updatePositionInPanel(tmpImg);
                    cropLeftSpinner.setValue(tmpImg.getLeft_crop());
                    cropRightSpinner.setValue(tmpImg.getRight_crop());
                    cropUpSpinner.setValue(tmpImg.getUp_crop());
                    cropDownSpinner.setValue(tmpImg.getDown_crop());
                    rotateSpinner.setValue((int) tmpImg.getTheta());
                }
                doubleLayerPane1.ROIS.setSelectedShape(cur_sel_image1);
                ief.setCurSel(cur_sel_image1);
            } else {
                if (pos == -1) {
                    ief.setCurSel(null);
                    ief.setVisible(false);
                    cur_sel_image1 = null;
                }
            }
            dontChangeCropSize = false;
            dontChangeAngle = false;
        }
    }//GEN-LAST:event_newObjectSelected

    /**
     *
     * @param images
     * @return Converts selected images to vectorial objects
     */
    public ArrayList<Object> getShapes(ArrayList<String> images, boolean ignore_size_errors) {
        ArrayList<Object> shapes = new ArrayList<Object>();
        try {
            int size_x = -1;
            int size_y = -1;
            double AspectRatio = -1;
//            boolean ignore_size_errors = false;
            for (String string : images) {
                MyImage2D tmp;
                if (!string.contains("importJ:")) {
                    if (!string.toLowerCase().endsWith(".svg") && !string.toLowerCase().endsWith(".figur")) {
                        Loader l2 = new Loader();
                        BufferedImage bimg = l2.loadWithImageJ8bitFix(string);
                        FileInfo fifo = l2.getFileInfo();
                        tmp = new MyImage2D.Double(0, 0, string, bimg);
                        if (fifo != null) {
                            tmp.setSize_of_one_px_in_unit(fifo.pixelWidth);
                        }
                    } else if (string.toLowerCase().endsWith(".figur")) {
                        checkRstatus();
                        tmp = (MyPlotVector.Double) new Loader().loadObjectRaw(string);
                        ((MyPlotVector.Double) tmp).reloadDocFromString();
                        ((MyPlotVector.Double) tmp).setFullName(CommonClassesLight.change_path_separators_to_system_ones(string));
                    } else {
                        tmp = new MyImageVector.Double(0, 0, string, new Loader().loadSVGDocument(string));
                    }
                } else {
                    tmp = new MyImage2D.Double(0, 0, string, imported_from_J.get(string).getBufferedImage());
                    if (imported_from_pixel_size.containsKey(string)) {
                        tmp.setSize_of_one_px_in_unit(imported_from_pixel_size.get(string));
                    }
                }
                if (size_x == -1) {
                    size_x = tmp.getImageWidth();
                    size_y = tmp.getImageHeight();
                    AspectRatio = tmp.getAR();
                }
                if ((size_x == tmp.getImageWidth() && size_y == tmp.getImageHeight()) || AspectRatio == tmp.getAR() || tmp instanceof MyPlotVector) {
                    shapes.add(tmp);
                } else if (!ignore_size_errors) {
                    JLabel iopane = new JLabel("<html><font color=\"#FF0000\">Your images have different sizes and different aspect-ratios,<br>it probably does not make sense to have them in the same panel. <BR>Alternatively you may crop some images to recover a visually appealing panel.<BR><BR>Do you really want to continue ?</font></html>");
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        ignore_size_errors = true;
                        shapes.add(tmp);
                    } else {
                        return null;
                    }
                } else {
                    shapes.add(tmp);
                }
            }
        } catch (OutOfMemoryError E) {
            StringWriter sw = new StringWriter();
            E.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println(stacktrace);
            try {
                System.gc();
                CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
            } catch (Exception e) {
            }
        }
        return shapes;
    }

    private void stopMemoryThread() {
        try {
            refreshMemory.shutdownNow();
        } catch (Exception e) {
        }

    }

    /**
     * this function is called upon quitting
     *
     * @param evt
     */
    private void onQuit(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onQuit
        blinker.stop();
        /**
         * fixes the FIJI not closing error when memory thread is active
         */
        stopMemoryThread();
        save_properties();
        /*
         * in theory useless because I've already set the files to be deletedonexit. But in fact it is useful if the soft is used as an IJ or a FIJI plugin.
         */
        for (String files : undos_and_redos) {
            new File(files).delete();
        }
        try {
            ief.dispose();
        } catch (Exception e) {
        }
        /*
         * we clean up a bit
         */
        clearAll();
        this.setVisible(false);
        try {
            System.gc();
        } catch (Exception e) {
        }

        if (logger != null) {
            logger.removeLocker(lockerID);
            if (!logger.isLocked()) {
                logger.restoreSystem();
                logger.dispose();
                CommonClassesLight.logger = null;
            }
        }

        if (CommonClassesLight.isImageJEmbedded && !FiguR_.isInstanceAlreadyExisting()) {
            System.exit(0);
        }
    }//GEN-LAST:event_onQuit

    /**
     * Resets the size of columns in display panels
     */
    public void resetSize() {
        JournalPageWidth.clear();
        JournalPageWidth.add(21.);
        JournalPageWidth.add(16.);
        JournalPageWidth.add(10.);
        /*
         * we then update the spinner accordingly
         */
        imageWidthChanged(null);
    }

    /**
     * Updates the current journal style and displays column size in display
     * panels
     */
    public void setJournalDefaultSizes() {
        if (defaultStyle == null) {
            resetSize();
            return;
        }
        JournalPageWidth.clear();
        JournalPageWidth.add(defaultStyle.getTwoColumnSizee());
        JournalPageWidth.add(defaultStyle.getOneAndHalfColumn());
        JournalPageWidth.add(defaultStyle.getColumnSize());
    }

    /**
     * Sets the size of columns in display panels
     */
    public void setRuler() {
        setJournalDefaultSizes();
        doubleLayerPane1.setJournalStyle(defaultStyle);
        doubleLayerPane2.setJournalStyle(defaultStyle);
        doubleLayerPane3.setJournalStyle(defaultStyle);
    }

    /**
     * update the width of the selection when the 'size in px' spinner value is
     * changed (or when enter is pressed in it)
     *
     * @param evt
     */
    private void sizeInPxsizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizeInPxsizeChanged
        if (loading) {
            return;
        }
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        try {
            if (evt != null && evt.getSource() == sizeInPx) {
                double size_in_cm = Converter.PxToCmAnyDpi(getSizeInPx(), resolution);
                if (!sizeLock) {
                    sizeInCM.setValue(size_in_cm);
                }
            }
            if (jTabbedPane1.getSelectedIndex() == 1 && curPanel != null && curPanel instanceof Montage) {
                ((Montage) curPanel).setToWidth(getSizeInPx());
                ((Montage) curPanel).setWidth_in_cm(getSizeInCm());
                ((Montage) curPanel).setWidth_in_px(getSizeInPx());
                ((Montage) curPanel).setResolution(resolution);
                /*
                 * bug fix for alignment error when the panel contains images having different sizes
                 */
                ((Montage) curPanel).setFirstCorner();
                updateTable();
                createBackup();
            } else if (jTabbedPane1.getSelectedIndex() == 2 && current_row != null) {
                if (current_row instanceof ArrayList) {
                    ArrayList<Row> selRows = (ArrayList<Row>) current_row;
                    for (Row row : selRows) {
                        forceSameHeight(row);
                    }
                } else {
                    Row row = (Row) current_row;
                    forceSameHeight(row);
                }
                /*
                 * removed cause useless
                 */
                updateFigure();
                createBackup();
                doubleLayerPane2.ROIS.setSelectedShape(current_row);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_sizeInPxsizeChanged

    /**
     * ca a l'air suffisant mais a checker
     *
     * @param o
     */
    public void reforceSameHeight(Object o) {
        if (o instanceof Row) {
            Row row = (Row) o;
            if (rowAutoSameHeight) {
                row.sameHeight(getSpaceBetweenRows());
                row.setToWidth();
            }
            if (rowAutoSameHeight) {
                row.sameHeight(getSpaceBetweenRows());
                row.setToWidth();
            }
        }
    }

    /**
     *
     * @param row
     */
    private void forceSameHeight(Object o) {
        if (o instanceof Row) {
            Row row = (Row) o;
            if (rowAutoSameHeight) {
                row.sameHeight(getSpaceBetweenRows());
                row.setToWidth(getSizeInPx());
            }
            if (rowAutoSameHeight) {
                row.sameHeight(getSpaceBetweenRows());
                row.setToWidth(getSizeInPx());
            }
            row.setWidth_in_cm(getSizeInCm());
            row.setWidth_in_px(getSizeInPx());
            row.setResolution(resolution);
        }
    }

    /**
     *
     * @return the size in pixel of the montage or of the selected Figure row
     */
    public int getSizeInPx() {
        return ((Integer) sizeInPx.getValue());
    }

    /**
     *
     * @return the size in cm
     */
    public double getSizeInCm() {
        return ((Double) sizeInCM.getValue());
    }

    /**
     * the user changed the size of a journal (in columns) so we apply the
     * changes
     *
     * @param evt
     */
    private void imageWidthChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageWidthChanged
        if (loading) {
            return;
        }
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        int idx = journalColumnsCombo.getSelectedIndex();
        if (idx != -1) {
            double val = JournalPageWidth.get(idx);
            sizeInCM.setValue(val);
        }
    }//GEN-LAST:event_imageWidthChanged

    /**
     * the user modified size of selection (size in cm)
     *
     * @param evt
     */
    private void cmSizeChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cmSizeChanged
        if (loading) {
            return;
        }
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        /*
         * we have it update the size in px
         */
        if (evt != null && evt.getSource() == sizeInCM) {
            double width_in_px = Converter.cmToPxAnyDpi(getSizeInCm(), resolution);
            sizeLock = true;
            sizeInPx.setValue((int) Math.round(width_in_px));
            sizeLock = false;
        }
    }//GEN-LAST:event_cmSizeChanged

    /**
     *
     * @return the current seleted row
     */
    public int getSelectedRow() {
        int selection = figureList.getSelectedIndex();
        return selection;
    }

    /**
     * called when the user changes the space between images in a panel
     *
     * @param evt
     */
    private void changeSpaceBetweenImageInPanelspaceBetweenImagesChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_changeSpaceBetweenImageInPanelspaceBetweenImagesChanged
        if (evt != null) {
            runAllnow(evt.getSource());
        }

        if (loading) {
            return;
        }
        updateSpaceInPanel();
    }//GEN-LAST:event_changeSpaceBetweenImageInPanelspaceBetweenImagesChanged

    /**
     * creates a backup (temporary .yf5m file) corresponding to the current
     * figure/montage (if undo/redo mode is active)
     */
    public void createBackup() {
        if (!isUndoRedoAllowed) {
            return;
        }
        File tmp = new CommonClassesLight().CreateTempFile(".yf5m");
        tmp.deleteOnExit();
        String selectedStyle = null;
        if (journalCombo.getSelectedIndex() != -1) {
            selectedStyle = journalCombo.getSelectedItem().toString();
        }
        Exportable ex = new Exportable();
        ex.save(panels, rows, myList1.getFullList(), selectedStyle, imported_from_J, imported_from_pixel_size, false, tmp.toString());
        undos_and_redos.add(tmp.toString());
        while (undos_and_redos.size() >= MAX_UNDOS) {
            File f = new File(undos_and_redos.getFirst());
            f.delete();
            undos_and_redos.pop();
        }
        cur_pos = undos_and_redos.size() - 1;
    }

    /**
     * Get a myImage2D from a file
     *
     * @param name
     * @return a myimage2D for a file
     */
    public MyImage2D getImage(String name) {
        MyImage2D tmp;
        if (!name.contains("importJ:")) {
            if (!name.toLowerCase().endsWith(".svg") && !name.toLowerCase().endsWith(".figur")) {
                Loader l2 = new Loader();
                BufferedImage bimg = l2.loadWithImageJ8bitFix(name);
                FileInfo fifo = l2.getFileInfo();
                tmp = new MyImage2D.Double(0, 0, name, bimg);
                if (fifo != null) {
                    tmp.setSize_of_one_px_in_unit(fifo.pixelWidth);
                }
            } else if (name.toLowerCase().endsWith(".figur")) {
                checkRstatus();
                tmp = (MyPlotVector.Double) (new Loader().loadObjectRaw(name));
                ((MyPlotVector.Double) tmp).reloadDocFromString();
            } else {
                tmp = new MyImageVector.Double(0, 0, name, new Loader().loadSVGDocument(name));
            }
        } else {
            tmp = new MyImage2D.Double(0, 0, name, imported_from_J.get(name).getBufferedImage());
            if (imported_from_pixel_size != null) {
                if (imported_from_pixel_size.get(name) != null) {
                    tmp.setSize_of_one_px_in_unit(imported_from_pixel_size.get(name));
                }
            }
        }
        return tmp;
    }

    public void runAllnow(Object source) {
        /*
         * quickly checks connection to R
         */
        checkStatus();
        /*
         * keep it here allows video demos for buttons
         */
        if ((source instanceof JSpinner) && !showHelpVideoForClickedButton) {
            return;
        }
        /*
         * Video demo
         * we show a video demo of the cliked button spinner or combo
         */
        if (source == buttonHelp || showHelpVideoForClickedButton) {
            boolean success = false;
            if (showHelpVideoForClickedButton) {
                if (source == replaceImage) {
                    CommonClassesLight.browse("http://youtu.be/gvsqkl_oxK4");
                    success = true;
                }

                if (source == moveColLeft || source == moveColRight) {
                    CommonClassesLight.browse("http://youtu.be/dbvTZHULTys");
                    success = true;
                }
                if (source == deleteJournalStyle) {
                    CommonClassesLight.browse("http://youtu.be/afLH98xysG8");
                    success = true;
                }
                if (source == deleteSelectColumn) {
                    CommonClassesLight.browse("http://youtu.be/ra8_BwLhtT8");
                    success = true;
                }

                if (source == zoomPlus) {
                    CommonClassesLight.browse("http://youtu.be/Q0yZRvQK3b8");
                    success = true;
                }

                if (source == zoomMinus) {
                    CommonClassesLight.browse("http://youtu.be/d3Yeek-oSys");
                    success = true;
                }

                if (source == bestFitZoom) {
                    CommonClassesLight.browse("http://youtu.be/_51H265QLwA");
                    success = true;
                }

                if (source == realSizeZoom) {
                    CommonClassesLight.browse("http://youtu.be/V4DHpo2f9zQ");
                    success = true;
                }

                if (source == addEmptyImageToCurrentBlock) {
                    CommonClassesLight.browse("http://youtu.be/Lg_aDI2QwoY");
                    success = true;
                }
                if (source == checkSize || source == checkStyle || source == checkFont || source == checkText || source == checkGraph || source == checkLineArts) {
                    CommonClassesLight.browse("http://youtu.be/uvrXs4t4luk");
                    success = true;
                }
                if (source == createPanelAutomatically) {
                    /*
                     * demo auto
                     */
                    CommonClassesLight.browse("http://youtu.be/l0MayjoUcWo");
                    success = true;
                }
                if (source == showShortcuts) {
                    /*
                     * demo + add to panel
                     */
                    CommonClassesLight.browse("http://youtu.be/vUroHr_A4PI");
                    success = true;
                }
                if (source == addSelectionToCurrentPanel) {
                    /*
                     * demo + add to panel
                     */
                    CommonClassesLight.browse("http://youtu.be/mk1P4koujl0");
                    success = true;
                }
                if (source == addSelectedImagesInCurPanel) {
                    CommonClassesLight.browse("http://youtu.be/mk1P4koujl0");
                    success = true;
                }

                if (source == reformatTable /*&& jTabbedPane1.getSelectedIndex() == 1*/) {
                    /*
                     * demo panel format button
                     */
                    CommonClassesLight.browse("http://youtu.be/XdH46WBK_pE");
                    success = true;
                }
                if (source == journalColumnsCombo || source == sizeInCM || source == sizeInPx) {
                    /*
                     * demo changing size
                     */
                    CommonClassesLight.browse("http://youtu.be/0PCWSE6_ijw");
                    success = true;
                }
                if (source == jTextField1 || source == updateLetters /*&& jTabbedPane1.getSelectedIndex() == 1*/) {
                    /*
                     * demo changing letters for panel
                     */
                    CommonClassesLight.browse("http://youtu.be/9WDWYxDDnh4");
                    success = true;
                }
                if (source == changeSpaceBetweenImageInPanel) {
                    /*
                     * demo changing space in panel
                     */
                    CommonClassesLight.browse("http://youtu.be/4m9TOduT8Sk");
                    success = true;
                }
                if (source == createPanelFromSelection) {
                    /*
                     * demo creating a montage arrow button
                     */
                    CommonClassesLight.browse("http://youtu.be/0ujHIeYHGlA");
                    success = true;
                }
                if ((source == cropLeftSpinner || source == cropRightSpinner || source == cropUpSpinner || source == cropDownSpinner)) {
                    /*
                     * demo crop and rotate in panels
                     */
                    CommonClassesLight.browse("http://youtu.be/cvPAEptL-mY");
                    success = true;
                }
                if (source == rotateSpinner) {
                    CommonClassesLight.browse("http://youtu.be/OAyHEBYlGz0");
                    success = true;
                }
                if (source == deleteSelectedImageFromCurrentBlock) {
                    /*
                     * demo delete an image from a panel
                     */
                    CommonClassesLight.browse("http://youtu.be/et6aL1bhM2U");
                    success = true;
                }
                if (source == DeleteSelectedBlock) {
                    /*
                     * demo delete panel
                     */
                    CommonClassesLight.browse("http://youtu.be/bwBEdwvpnIQ");
                    success = true;
                }
                if (source == addTextAboveRow || source == addTextBelowRow || source == AddTextLeftOfRow || source == AddTextRightOfRow) {
                    /*
                     * demo text bars
                     */
                    CommonClassesLight.browse("http://youtu.be/USraEocAkWQ");
                    success = true;
                }
                if (source == deleteImagesFromTheList) {
                    /*
                     * demo delete panel
                     */
                    CommonClassesLight.browse("http://youtu.be/71f3OSAjBMs");
                    success = true;
                }
                if (source == addSelectedPanelToNewRow) {
                    /*
                     * demo delete add new row
                     */
                    CommonClassesLight.browse("http://youtu.be/cAKBZZph4l4");
                    success = true;
                }
                if (source == addSelectedPanelToSelectedRow) {
                    /*
                     * demo delete add new row
                     */
                    CommonClassesLight.browse("http://youtu.be/cAKBZZph4l4");
                    success = true;
                }
                if (source == journalCombo) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/cPQzlNAyKcw");
                    success = true;
                }
                if (source == moveRowRight || source == moveRowLeft) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/oPtKeSd6xM0");
                    success = true;
                }
                if (source == removeSelectedRow) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/w0GluC5WBv4");
                    success = true;
                }
                if (source == AnnotateImageROIs) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/gZ_u6EXq6X8");
                    success = true;
                }
                if (source == AnnotateImageText) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/-0YcXXC53RE");
                    success = true;
                }
                if (source == removeAllAnnotations || source == removeAnnotations) {
                    /*
                     * demo remove annotations
                     */
                    CommonClassesLight.browse("http://youtu.be/vbPatQVxt-Y");
                    success = true;
                }
                if (source == changeSpaceBetweenRows) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/U2YM_pxg1sg");
                    success = true;
                }
                if (source == importFromIJ) {
                    CommonClassesLight.browse("http://youtu.be/4oVx98t-YPI");
                    success = true;
                }
                if (source == moveUpInList || source == moveDownInList) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/00odzpvqEFI");
                    success = true;
                }
                if (source == splitColoredImagesToMagentaGreen) {
                    /*
                     * demo changing journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/G7TWJrXTrCU");
                    success = true;
                }
                if (source == moveImageInCurrentPanelLeft || source == moveImageInCurrentPanelRight) {
                    /*
                     * demo crop and rotate in panels
                     */
                    CommonClassesLight.browse("http://youtu.be/VDGscaiWCVw");
                    success = true;
                }
                if (source == swapImagesFromCurrentPanel) {
                    /*
                     * demo crop and rotate in panels
                     */
                    CommonClassesLight.browse("http://youtu.be/m8Fxbkv5tP4");
                    success = true;
                }
                if (source == PIP || source == removePiP) {
                    /*
                     * demo add remove PIP
                     */
                    CommonClassesLight.browse("http://youtu.be/lxn0t-jD448");
                    success = true;
                }
                if (source == newJournalStyle) {
                    /*
                     * demo create a new journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/GpK-nC2Ra0o");
                    success = true;
                }
                if (source == removeAllScaleBars) {
                    /*
                     * demo create a new journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/5Y-h_lEgDCY");
                    success = true;
                }
                if (source == removeAllText) {
                    /*
                     * demo create a new journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/sB-oXKNA4z4");
                    success = true;
                }
                if (source == ChangeStrokeSize) {
                    /*
                     * demo create a new journal style
                     */
                    CommonClassesLight.browse("http://youtu.be/pia-zYgcYqM");
                    success = true;
                }
                if (source == jMenuItem12) {
                    /*
                     * demo change font and bg color of text
                     */
                    CommonClassesLight.browse("http://youtu.be/E2XTfpWm_Kw");
                    success = true;
                }
            }
            if (showHelpVideoForClickedButton && !success) {
                CommonClassesLight.Warning(this, "Sorry there is no demo yet for this button");
            }
            if (source == buttonHelp && !showHelpVideoForClickedButton) {
                showHelpVideoForClickedButton = true;
                if (showHelpInfoWindow) {
                    JOptionPane.showOptionDialog(this, new Object[]{"<html>Click on any button to view a video<br>tutorial demoing the button functionalities<html>"}, "Infos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                }
            } else {
                showHelpVideoForClickedButton = false;
            }
            return;
        }
        /**
         * bug fix that prevents export to be considered as an image
         * modification
         */
        if (source == exportAsTiff) {
            /*
             * Save selected panel or figure to a tif file
             */
            export(FORMAT_TIFF, null);
            return;
        }
        if (source == exportAsPNG) {
            /*
             * Save selected panel or figure to a png file
             */
            export(FORMAT_PNG, null);
            return;
        }
        if (source == exportAsJpeg) {
            /*
             * Save selected panel or figure to a jpg file
             */
            export(FORMAT_JPEG, null);
            return;
        }
        if (source == exportAsSVG) {
            /*
             * Save selected panel or figure to an svg file
             */
            export(FORMAT_SVG, null);
            return;
        }
        /**
         * allows to skip asking for save when the image is not changed
         */
        boolean ignoreWarnForSave = false;

        if (source == New) {
            /**
             * New is going to erase everything so we warn the user if he hasn't
             * saved his image
             */
            if (warnForSave && (isThereAnythingInTheLists())) {
                blinkAnything(jButton7);
                JLabel jl = new JLabel("<html><font color=\"#FF0000\">You are about to create a new Figure,<br>i.e. all unsaved images/panels and figures will be lost.<br><br>Do you really want to continue ?<br>(click cancel to abort)</font></html>");
                int result = JOptionPane.showOptionDialog(this, new Object[]{jl}, "Warning...", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                } else {
                    /*
                     * we stop the blinking
                     */
                    blinker.stop();
                    glasspane.setBlink(null);
                    glasspane.repaint();
                }
            }
            /**
             * clears all lists and resets everything so that the user can start
             * a brand new figure
             */
            clearAll();
           PanelCounter=1;
           letter = "A";
        }
        if (source == zoomPlus) {
            /**
             * pressed on the zoom + button
             */
            int src = jTabbedPane1.getSelectedIndex();
            switch (src) {
                case 0:
                    doubleLayerPane3.incrementZoom();
                    break;
                case 1:
                    doubleLayerPane1.incrementZoom();
                    break;
                case 2:
                    doubleLayerPane2.incrementZoom();
                    break;
            }
            ignoreWarnForSave = true;
        }
        if (source == showShortcuts) {
            ListOfShortcuts spane = new ListOfShortcuts(shortcuts);
            JOptionPane.showMessageDialog(this, spane, "List of shortcuts", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if (source == zoomMinus) {
            /**
             * pressed on the zoom - button
             */
            int src = jTabbedPane1.getSelectedIndex();
            switch (src) {
                case 0:
                    doubleLayerPane3.decrementZoom();
                    break;
                case 1:
                    doubleLayerPane1.decrementZoom();
                    break;
                case 2:
                    doubleLayerPane2.decrementZoom();
                    break;
            }
            ignoreWarnForSave = true;
        }
        if (source == bestFitZoom) {
            /**
             * adjusts zoom to best adapt to the content
             */
            int src = jTabbedPane1.getSelectedIndex();
            switch (src) {
                case 0:
                    doubleLayerPane3.best_fit_zoom(jScrollPane17.getViewportBorderBounds().width, jScrollPane17.getViewportBorderBounds().height);
                    break;
                case 1:
                    doubleLayerPane1.best_fit_zoom(jScrollPane8.getViewportBorderBounds().width, jScrollPane8.getViewportBorderBounds().height);
                    break;
                case 2:
                    doubleLayerPane2.best_fit_zoom(jScrollPane16.getViewportBorderBounds().width, jScrollPane16.getViewportBorderBounds().height);
                    break;
            }
            ignoreWarnForSave = true;
        }
        if (source == realSizeZoom) {
            /**
             * sets zoom factor to 1D (reinits zoom)
             */
            int src = jTabbedPane1.getSelectedIndex();
            switch (src) {
                case 0:
                    doubleLayerPane3.setZoom(1.);
                    break;
                case 1:
                    doubleLayerPane1.setZoom(1.);
                    break;
                case 2:
                    doubleLayerPane2.setZoom(1.);
                    break;
            }
            ignoreWarnForSave = true;
        }
        if (source == jMenuItem2) {
            CommonClassesLight.Warning2(this, MyRsessionLogger.generateRInstallationText());
            return;
        }
        if (source == extract_images) {
            if (panels == null || panels.isEmpty()) {
                CommonClassesLight.Warning(this, "Sorry there is nothing to export");
                return;
            }
            /*
             * we extract all the original images contained in the myImage2d and derivative objects as well as imports from imageJ
             */
            String name = CommonClassesLight.selectADirectory(this, lastExportFolder);
            if (name != null) {
                name = CommonClassesLight.change_path_separators_to_system_ones(name);
                lastExportFolder = name;
                final String saveName = name;
                if (panels == null) {
                    return;
                }
                final int max = panels.size();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressMonitor progressMonitor = new ProgressMonitor(CommonClassesLight.getGUIComponent(), "Exporting Images", "Progress...", 0, 100);
                        progressMonitor.setProgress(0);
                        progressMonitor.setMillisToDecideToPopup(0);
                        progressMonitor.setMillisToPopup(0);
                        progressMonitor.setProgress(0);
                        int count = 0;
                        for (Map.Entry<Integer, Montage> entry : panels.entrySet()) {
                            progressMonitor.setProgress((int) (100. * (double) count / (double) max));
                            Montage m = entry.getValue();
                            m.extractImages(saveName);
                            count++;
                        }
                        progressMonitor.setProgress(100);
                        progressMonitor.close();

                    }
                }).start();
            }
            return;
        }

        if (source == OpenYF5M || source == jButton11 || (source instanceof String && source.toString().toLowerCase().contains("import"))) {
            /*
             * load a .yf5m file
             */
            String inputFolder = null;
            if (lastSaveName != null) {
                inputFolder = new File(lastSaveName).getParent();
            }
            String name;
            if (source == jButton11) {
                name = (name_to_load);
            } else {
                name = CommonClassesLight.openFile(this, inputFolder, "yf5m", !CommonClassesLight.isWindows() && useNativeDialog);
            }
            loadFile(name, true, true);
            /**
             * bug fix for erroneous asking to save files
             */
            ignoreWarnForSave = true;
        }

        /**
         * bug fix for erroneous asking to save files
         */
        if (!ignoreWarnForSave && !((source instanceof JComboBox) && loading)) {
            warnForSave = true;
        }

        if (source == Rstatus) {
            warned_once = false;
            checkRstatus();
        }

        if (source == addEmptyImageToCurrentBlock) {
            /*
             * here we add an empty image that can be replaced by a real image later on
             */
            EmptyImage iopane = new EmptyImage();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add empty object", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (tableList.getSelectedIndex() != -1) {
                    int real_pos = getRealPos();
                    Montage b = panels.get(real_pos);
                    b.addShape2(new MySpacer.Double(iopane.getEmptyWidth(), iopane.getEmptyHeight()));
                    b.update();
                    b.setResolution(resolution);
                    b.setToWidth(getSizeInPx());
                    b.setWidth_in_px(getSizeInPx());
                    b.setWidth_in_cm(getSizeInCm());
                    curPanel = b;
                    panels.put(real_pos, b);
                    montageChanged(null);
                    curPanel.setToWidth(getSizeInPx());
                    if (remove_when_added) {
                        myList1.removeSelection();
                    }
                    showAppropriateOptions(tableList);
                    createBackup();
                } else {
                    if (result == JOptionPane.OK_OPTION) {
                        ArrayList<Object> shapes = new ArrayList<Object>();
                        shapes.add(new MySpacer.Double(iopane.getEmptyWidth(), iopane.getEmptyHeight()));
                        addMontage(shapes, 1, 1, true, "", getSpaceBetweenImages());
                        showAppropriateOptions(tableList);
                        createBackup();
                    }
                }
                updateTable();
            } else {
                return;
            }
        }
        if (source == replaceImage) {
            /**
             * here we replace a selected image by another one while keeping the
             * annotations, letters, scale bars, etc...
             */
            if ((cur_sel_image1 == null || cur_sel_image1 instanceof ComplexShapeLight) && jTabbedPane1.getSelectedIndex() == 1) {
                blinkAnything(tableContentList.getParent());
                CommonClassesLight.Warning(this, "Please select a single image from the 'Panel content' list");
                return;
            }
            if ((cur_sel_image2 == null || cur_sel_image2 instanceof ComplexShapeLight) && jTabbedPane1.getSelectedIndex() == 2) {
                blinkAnything(imagesInFigureList.getParent());
                CommonClassesLight.Warning(this, "Please select a single image from the 'Panel content' list");
                return;
            }
            if (jTabbedPane1.getSelectedIndex() == 1) {
                if (myList1.isEmpty()) {
                    blinkAnything(myList1);
                    CommonClassesLight.Warning(this, "Please add images to your 'image list' first");
                    return;
                }
                if (!myList1.isEmpty()) {
                    MyListLight ml2 = new MyListLight((DefaultListModel) myList1.list.getModel());
                    ml2.list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                    int result = JOptionPane.showOptionDialog(this, new Object[]{ml2}, "Add inset", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        String sel = ml2.getSelectedValue();
                        if (sel != null) {
                            Object cur_sel;
                            if (jTabbedPane1.getSelectedIndex() == 1) {
                                cur_sel = cur_sel_image1;
                            } else {
                                cur_sel = cur_sel_image2;
                            }
                            if (cur_sel instanceof MyImage2D) {
                                if (curPanel != null) {
                                    MyImage2D replacement = getImage(sel);
                                    if (curPanel.replace(cur_sel, replacement)) {
                                        curPanel.update();
                                        curPanel.setToWidth();
                                        if (jTabbedPane1.getSelectedIndex() == 1) {
                                            cur_sel_image1 = replacement;
                                            ief.setCurSel(cur_sel_image1);
                                        }
                                        if (jTabbedPane1.getSelectedIndex() == 2) {
                                            cur_sel_image2 = replacement;
                                            ief.setCurSel(cur_sel_image2);
                                        }
                                        montageChanged(null);
                                        updateTable();
                                    }
                                }
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    blinkAnything(myList1);
                    CommonClassesLight.Warning(this, "Please select an image in your 'image list'");
                    return;
                }
            } else {
                if (!myList1.isEmpty()) {
                    if (myList1.isEmpty()) {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please add images to your 'image list' first");
                        return;
                    }
                    if (!myList1.isEmpty()) {
                        MyListLight ml2 = new MyListLight((DefaultListModel) myList1.list.getModel());
                        ml2.list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                        int result = JOptionPane.showOptionDialog(this, new Object[]{ml2}, "Add inset", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            String sel = ml2.getSelectedValue();
                            if (sel != null) {
                                Object cur_sel;
                                if (jTabbedPane1.getSelectedIndex() == 1) {
                                    cur_sel = cur_sel_image1;
                                } else {
                                    cur_sel = cur_sel_image2;
                                }
                                if (cur_sel instanceof MyImage2D) {
                                    if (curPanel != null) {
                                        MyImage2D m = getImage(sel);
                                        if (curPanel.replace(cur_sel, m)) {
                                            curPanel.update();
                                            forceSameHeight(current_row);
                                            figureListValueChanged(null);
                                            updateFigure();
                                        }
                                    }
                                }
                            }
                        } else {
                            return;
                        }
                    } else {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please add images to your 'image list' first");
                        return;
                    }
                } else {
                    blinkAnything(myList1);
                    CommonClassesLight.Warning(this, "Please add images to your image list then select one of them");
                    return;
                }
            }
        }
        if (source == PIP || source == removePiP) {
            /**
             * here we add/remove insets to/from selected images (PIP stands for
             * picture in picture)
             */
            String cumulative_error = "";
            if (!myList1.isEmpty() || (source == removePiP)) {
                ArrayList<String> images_names = new ArrayList<String>();
                if (source != removePiP) {
                    if (!myList1.isEmpty()) {
                        MyListLight ml2 = new MyListLight((DefaultListModel) myList1.list.getModel());
                        ml2.list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                        int result = JOptionPane.showOptionDialog(this, new Object[]{ml2}, "Add inset", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            String sel = ml2.getSelectedValue();
                            if (sel != null) {
                                images_names.add(sel);
                            }
                        } else {
                            return;
                        }
                    } else {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please add images to your 'image list' first");
                        return;
                    }
                }
                if (images_names.size() != 1 && !(source == removePiP)) {
                    cumulative_error += "Please select one and only one image in the 'Image List'\n";
                } else {
                    String name = "";
                    if (source == PIP) {
                        name = images_names.get(0);
                    }
                    if (!name.toLowerCase().endsWith(".figur") && !name.toLowerCase().endsWith(".svg") || (source == removePiP)) {
                        Object cur_sel;
                        if (jTabbedPane1.getSelectedIndex() == 1) {
                            cur_sel = cur_sel_image1;
                        } else {
                            cur_sel = cur_sel_image2;
                        }
                        if (cur_sel != null) {
                            double fraction = ief.getFraction();
                            int pos = ief.getInsetPosition();
                            boolean deletePiP = false;
                            if (source == removePiP) {
                                deletePiP = true;
                            }
                            if (cur_sel instanceof ComplexShapeLight) {
                                HashSet<Object> images = ((ComplexShapeLight) cur_sel).getGroup();
                                for (Object object : images) {
                                    if (object instanceof MyImage2D) {
                                        if (name.contains("importJ:")) {
                                            if (!deletePiP) {
                                                ((MyImage2D) object).setInset(imported_from_J.get(name));
                                            } else {
                                                ((MyImage2D) object).setInset(null);
                                            }
                                            ((MyImage2D) object).setINSET_POSITION(pos);
                                            ((MyImage2D) object).setFraction_of_parent_image_width(fraction);
                                        } else {
                                            if (!deletePiP) {
                                                ((MyImage2D) object).setInset(new SerializableBufferedImage2(new Loader().loadWithImageJ8bitFix(name)));
                                            } else {
                                                ((MyImage2D) object).setInset(null);
                                            }
                                            ((MyImage2D) object).setINSET_POSITION(pos);
                                            ((MyImage2D) object).setFraction_of_parent_image_width(fraction);
                                        }
                                    }
                                }
                            } else {
                                if (cur_sel instanceof MyImage2D) {
                                    if (name.contains("importJ:")) {
                                        if (!deletePiP) {
                                            ((MyImage2D) cur_sel).setInset(imported_from_J.get(name));
                                        } else {
                                            ((MyImage2D) cur_sel).setInset(null);
                                        }
                                        ((MyImage2D) cur_sel).setINSET_POSITION(pos);
                                        ((MyImage2D) cur_sel).setFraction_of_parent_image_width(fraction);
                                    } else {
                                        if (!deletePiP) {
                                            ((MyImage2D) cur_sel).setInset(new SerializableBufferedImage2(new Loader().loadWithImageJ8bitFix(name)));
                                        } else {
                                            ((MyImage2D) cur_sel).setInset(null);
                                        }
                                        ((MyImage2D) cur_sel).setINSET_POSITION(pos);
                                        ((MyImage2D) cur_sel).setFraction_of_parent_image_width(fraction);
                                    }
                                }
                            }
                        } else {
                            cumulative_error += "please select at least one image in the montage 'content list'\n";
                        }
                    } else {
                        cumulative_error += "please select a raster image from the image list (no .svg and no .figur files)\n";
                    }
                }
            } else {
                cumulative_error += "Please add images to your 'Image List' First!\n";
            }
            if (!cumulative_error.equals("")) {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, cumulative_error);
                return;
            }
            createBackup();
        }
        if (source == citations) {
            /**
             * we popup citations
             */
            CommonClassesLight.infos(this, "To cite ScientiFig in publications, please use:"
                    + "\n\nScientiFig: a tool to build publication-ready scientific figures.\n"
                    + "Aigouy B, Mirouse V.\n"
                    + "Nat Methods.\n"
                    + "2013 Oct 30;10(11):1048.\n"
                    + "doi: 10.1038/nmeth.2692.\n"
                    + "\nweb: http://srv-gred.u-clermont1.fr/labmirouse/software/"
                    + "\n\nTo cite FiguR please launch FiguR and click on About/Citations>Citations to get a full list of citations");
        }
        if ((source == updateLetters || source == jTextField1) && jTabbedPane1.getSelectedIndex() == 1) {
            /*
             * here we reletter a panel
             */
            if (curPanel == null) {
                if (tableList.getSelectedIndex() != -1) {
                    int real_pos = getRealPos();
                    curPanel = (Montage) panels.get(real_pos);
                }
            }
            if (curPanel != null) {
                char c;
                if (getLetter().length() != 0) {
                    c = getLetter().charAt(0);
                } else {
                    c = ' ';
                }
                if (c != ' ') {
                    for (Object integer : curPanel.getGroup()) {
                        if (integer instanceof MyImage2D) {
                            ((MyImage2D) integer).setLetter(c + "");
                            c++;
                        }
                    }
                } else {
                    for (Object integer : curPanel.getGroup()) {
                        if (integer instanceof MyImage2D) {
                            ((MyImage2D) integer).setLetter(" ");
                        }
                    }
                }
                updateTable();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tableContentList.updateUI();
                    }
                });
            } else {
                blinkAnything(tableList.getParent());
                CommonClassesLight.Warning(this, "Please select a Block in the 'Blocks' list");
                return;
            }
            createBackup();
        }
        if (source == splitColoredImagesToMagentaGreen) {
            /**
             * we split colored images to their R, G and B components and we
             * also create pairwise combinations of colors in magenta green
             */
            ChannelSelection iopane = new ChannelSelection();
            if (curPanel != null) {
                int[] pos = null;
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    pos = tableContentList.getSelectedIndices();
                } else if (jTabbedPane1.getSelectedIndex() == 2) {
                    pos = imagesInFigureList.getSelectedIndices();
                }
                if (pos == null || pos.length == 0) {
                    if (jTabbedPane1.getSelectedIndex() == 1) {
                        blinkAnything(tableContentList.getParent());
                        CommonClassesLight.Warning(this, "Please Select At Least One Image In The 'Panel content' List");
                        return;
                    } else if (jTabbedPane1.getSelectedIndex() == 2) {
                        blinkAnything(imagesInFigureList.getParent());
                        CommonClassesLight.Warning(this, "Please Select At Least One Image In The 'Panel/col content' List");
                        return;
                    }
                }
                if (((Montage) curPanel).containsStrictlyImages2D(pos)) {
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Create a Block", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        ((Montage) curPanel).createColorBlindFriendlyImages(iopane.isRed(), iopane.isGreen(), iopane.isBlue(), iopane.isRedGreen(), iopane.isGreenBLue(), iopane.isRedBlue(), pos);
                        curPanel.updateTable();

                        curPanel.setToWidth(getSizeInPx());
                        curPanel.setWidth_in_px(getSizeInPx());
                        curPanel.setWidth_in_cm(getSizeInCm());
                        curPanel.setResolution(resolution);
                        if (jTabbedPane1.getSelectedIndex() == 1) {
                            montageChanged(null);
                        } else if (jTabbedPane1.getSelectedIndex() == 2) {
                            Row r = ((Row) rows.get(figureList.getSelectedIndex()));
                            forceSameHeight(r);
                            updateFigure();
                            /**
                             * dirty fix to display the selection --> check if
                             * really necessary
                             */
                            showAppropriateOptions(imagesInFigureList);
                        }
                        if (((Montage) curPanel).containsImagesVector(pos)) {
                            CommonClassesLight.Warning(this, "Vector images and plots could not be splitted");
                        }
                        createBackup();
                    }
                } else {
                    CommonClassesLight.Warning(this, "Vector images and plots cannot be splitted");
                }
            } else {
                blinkAnything(tableContentList.getParent());
                CommonClassesLight.Warning(this, "Please Select At Least One Image In The 'Panel content' List");
                return;
            }
        }
        if (source == rotateLeft || source == rotateRight) {
            final Object src = source;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /**
                     * lossless (90°) image rotation to the left or to the right
                     */
                    if (curPanel != null) {
                        int[] pos = tableContentList.getSelectedIndices();
                        if (pos == null || pos.length == 0) {
                            blinkAnything(tableContentList.getParent());
                            CommonClassesLight.Warning("Please Select At Least One Image In The 'Panel content' List");
                            return;
                        }
                        if (src == rotateLeft) {
                            ((Montage) curPanel).rotateLeft(pos);
                        } else {
                            ((Montage) curPanel).rotateRight(pos);
                        }
                        curPanel.updateTable();
                        curPanel.setToWidth(getSizeInPx());
                        curPanel.setWidth_in_px(getSizeInPx());
                        curPanel.setWidth_in_cm(getSizeInCm());
                        curPanel.setResolution(resolution);
                        montageChanged(null);
                        createBackup();
                    } else {
                        blinkAnything(tableContentList.getParent());
                        CommonClassesLight.Warning("Please Select At Least One Image In The 'Panel content' List");
                    }
                }
            }).start();
        }
        if (source == Flip) {
            /**
             * here we handle flipping of images (hor, ver or both)
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (curPanel != null) {
                        int[] pos = tableContentList.getSelectedIndices();
                        if (pos == null || pos.length == 0) {
                            blinkAnything(tableContentList.getParent());
                            CommonClassesLight.Warning("Please Select At Least One Image In The 'Panel content' List");
                            return;
                        }
                        FlipDialog iopane = new FlipDialog();
                        int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Create a Block", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            ((Montage) curPanel).flip(pos, iopane.getOrientation());
                            curPanel.updateTable();
                            curPanel.setToWidth(getSizeInPx());
                            curPanel.setWidth_in_px(getSizeInPx());
                            curPanel.setWidth_in_cm(getSizeInCm());
                            curPanel.setResolution(resolution);
                            montageChanged(null);
                            createBackup();
                        }
                    } else {
                        blinkAnything(tableContentList.getParent());
                        CommonClassesLight.Warning("Please Select At Least One Image In The 'Panel content' List");
                    }
                }
            }).start();
        }
        if (source == exportToIJ) {
            /**
             * we export the current selection to IJ
             */
            export(FORMAT_IJ, null);
        }
        if (source == swapImagesFromCurrentPanel || source == moveImageInCurrentPanelRight || source == moveImageInCurrentPanelLeft || (source == moveLeft && (jTabbedPane1.getSelectedIndex() == 1)) || (source == moveRight && (jTabbedPane1.getSelectedIndex() == 1))) {
            /*
             * we swap two images of a panel
             */
            int[] pos;
            int curPos = -1;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                curPos = tableContentList.getSelectedIndex();
            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                curPos = imagesInFigureList.getSelectedIndex();
            }
            if (source == swapImagesFromCurrentPanel) {
                pos = tableContentList.getSelectedIndices();
            } else {
                pos = new int[2];
                if (source == moveImageInCurrentPanelRight || source == moveRight) {
                    pos[0] = curPos;
                    pos[1] = curPos + 1;
                } else {
                    pos[0] = curPos;
                    pos[1] = curPos - 1;
                }
            }
            if (pos != null && pos.length == 2 && curPos != -1) {
                ((Montage) curPanel).swap(pos[0], pos[1]);
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    CommonClassesLight.swap(tableContentListModel, pos[0], pos[1]);
                } else if (jTabbedPane1.getSelectedIndex() == 2) {
                    CommonClassesLight.swap(imagesInFigureModel, pos[0], pos[1]);
                }
                if (source != swapImagesFromCurrentPanel) {
                    if (source == moveImageInCurrentPanelLeft || source == moveLeft) {
                        if (curPos - 1 >= 0) {
                            if (jTabbedPane1.getSelectedIndex() == 1) {
                                tableContentList.setSelectedIndex(curPos - 1);
                            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                                imagesInFigureList.setSelectedIndex(curPos - 1);
                            }
                        }
                    } else {
                        if (jTabbedPane1.getSelectedIndex() == 1) {
                            if (curPos + 1 < tableContentListModel.getSize()) {
                                tableContentList.setSelectedIndex(curPos + 1);
                            }
                        } else if (jTabbedPane1.getSelectedIndex() == 2) {
                            if (curPos + 1 < imagesInFigureModel.getSize()) {
                                imagesInFigureList.setSelectedIndex(curPos + 1);
                            }
                        }
                    }
                }
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    /*
                     * TODO in theory after a swap of exactly 2 images I should update the selection
                     */
                    updateBlockSize();
                    curPanel.setFirstCorner();
                    updateTable();
                } else {
                    Row r = ((Row) rows.get(figureList.getSelectedIndex()));
                    r.arrangeRow();
                    /**
                     * bug fix for packing errors when images were swapped in a
                     * panel within a row
                     */
                    forceSameHeight(r);
                    updateFigure();
                    /**
                     * dirty fix to display the selection --> check if really
                     * necessary
                     */
                    showAppropriateOptions(imagesInFigureList);
                }
                /*
                 * bug fix if images are not of the same size
                 */
                createBackup();
            } else {
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    if (source == swapImagesFromCurrentPanel) {
                        blinkAnything(tableContentList.getParent());
                        CommonClassesLight.Warning(this, "Please Select Exactly 2 Images In The 'Panel content' List");
                        return;
                    } else {
                        blinkAnything(tableContentList.getParent());
                        CommonClassesLight.Warning(this, "Please select a single image from the 'Panel content' list");
                        return;
                    }
                } else {
                    blinkAnything(imagesInFigureList.getParent());
                    CommonClassesLight.Warning(this, "Please select a single image from the 'Panel/col content' list");
                    return;
                }
            }
        }
        /*
         * need to force a resize here --> very last bug
         * TODO clean this code because very dirty and inefficient
         */
        //modification for review
        if (source == deleteSelectedImageFromCurrentBlock || (source == deleteButton && jTabbedPane1.getSelectedIndex() == 1) || ((source == deleteButton && jTabbedPane1.getSelectedIndex() == 2) && jPanel11.getParent() == optionsPanel)) {
            int curPos = -1;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                curPos = tableContentList.getSelectedIndex();
            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                curPos = imagesInFigureList.getSelectedIndex();
            }
            if (curPos != -1) {
                Montage b;
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    b = panels.get(getRealPos());
                } else {
                    b = curPanel;
                }
                Object cur_sel;
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    cur_sel = cur_sel_image1;
                } else {
                    cur_sel = cur_sel_image2;
                }
                /**
                 * bug fix when deleting too many images so that a panel has to
                 * be deleted
                 */
                boolean goOn = true;
                if (cur_sel instanceof ComplexShapeLight) {
                    if (((ComplexShapeLight) cur_sel).size() == b.size()) {
                        source = DeleteSelectedBlock;
                        goOn = false;
                    }
                } else {
                    if (cur_sel != null) {
                        if (b.size() == 1) {
                            if (jTabbedPane1.getSelectedIndex() == 1) {
                                source = DeleteSelectedBlock;
                            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                                source = deleteSelectColumn;
                            }
                            goOn = false;
                        }
                    }
                }
                if (goOn) {
                    try {
                        tableContentList.setModel(new DefaultListModel());
                        imagesInFigureList.setModel(new DefaultListModel());
                        if (cur_sel instanceof ComplexShapeLight) {
                            HashSet<Object> images = ((ComplexShapeLight) cur_sel).getGroup();
                            ArrayList<String> files_to_add = new ArrayList<String>();
                            for (Object object : images) {
                                if (object instanceof Namable) {
                                    String name = ((Namable) object).getFullName();
                                    if (name != null && name.contains("importJ:")) {
                                        SerializableBufferedImage2 sb = ((MyImage2D) object).getOriginalImage();
                                        if (!imported_from_J.containsKey(name)) {
                                            imported_from_J.put(name, sb);
                                        } else {
                                            while (imported_from_J.containsKey(name)) {
                                                name += "2";
                                            }
                                            imported_from_J.put(name, sb);
                                        }
                                        myList1.addDirectlyToList(name, sb.getBufferedImage());
                                    } else {
                                        if (name != null && new File(name).exists()) {
                                            files_to_add.add(name);
                                        }
                                    }
                                }
                            }
                            myList1.addAllNoCheck(files_to_add);
                            for (Object object : images) {
                                b.removeObjectAndUpdateLetters(object);
                            }
                            tableContentListModel.clear();
                            inactivateListUpdate = true;
                            for (Object object : b.getGroup()) {
                                tableContentListModel.addElement(object);
                            }
                            inactivateListUpdate = false;
                        } else {
                            b.removeObjectAndUpdateLetters(cur_sel);
                            if (jTabbedPane1.getSelectedIndex() == 1) {
                                tableContentListModel.remove(curPos);
                            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                                imagesInFigureModel.remove(curPos);
                            }
                            if (cur_sel instanceof Namable) {
                                ArrayList<String> files_to_add = new ArrayList<String>();
                                String name = ((Namable) cur_sel).getFullName();
                                if (name != null && name.contains("importJ:")) {
                                    SerializableBufferedImage2 sb = ((MyImage2D) cur_sel).getOriginalImage();
                                    if (!imported_from_J.containsKey(name)) {
                                        imported_from_J.put(name, sb);
                                    } else {
                                        while (imported_from_J.containsKey(name)) {
                                            name += "2";
                                        }
                                        imported_from_J.put(name, sb);
                                    }
                                    myList1.addDirectlyToList(name, sb.getBufferedImage());
                                } else {
                                    if (name != null) {
                                        if (new File(name).exists()) {
                                            files_to_add.add(name);
                                        }
                                    }
                                }
                                myList1.addAllNoCheck(files_to_add);
                            }
                        }
                        b.updateTable();
                        if (b.getGroup().isEmpty()) {
                            DeleteSelectedBlock.doClick();
                        } else {
                            /*
                             * force update size change
                             */
                            b.setResolution(resolution);
                            b.setToWidth(getSizeInPx());
                            b.setWidth_in_px(getSizeInPx());
                            b.setWidth_in_cm(getSizeInCm());
                        }
                        if (jTabbedPane1.getSelectedIndex() == 1) {
                            montageChanged(null);
                            /*
                             * bug fix when deleting images from a panel having images of different sizes
                             */
                            if (!b.isEmpty()) {
                                b.setFirstCorner();
                            }
                        } else if (jTabbedPane1.getSelectedIndex() == 2) {
                            forceSameHeight(rows.get(figureList.getSelectedIndex()));
                            updateFigure();
                        }
                        createBackup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        tableContentList.setModel(tableContentListModel);
                        imagesInFigureList.setModel(imagesInFigureModel);
                    }
                }
            } else {
                blinkAnything(tableContentList.getParent());
                CommonClassesLight.Warning(this, "Please select an image in the 'Panel Content' list first!");
                return;
            }
        }
        if (source == DeleteSelectedBlock) {
            /*
             * we remove a panel
             */
            if (tableList.getSelectedIndex() == -1) {
                blinkAnything(tableList.getParent());
                CommonClassesLight.Warning(this, "Please select a Panel in the 'Panels' list");
                return;
            }
            if (tableList.getSelectedIndex() != -1) {
                removeMontage(tableListModel.get(tableList.getSelectedIndex()).toString());
                deleteFromList(tableListModel, tableList.getSelectedIndex());
                tableContentListModel.clear();
                createBackup();
                try {
                    System.gc();
                } catch (Exception e) {
                }
            }
        }
        if (source == reformatTable) {
            /*
             * here we change the layout of an existing panel (i.e. we change its number of rows and cols)
             */
            if (jTabbedPane1.getSelectedIndex() == 1) {
                if (tableList.getSelectedIndex() != -1) {
                    int real_pos = getRealPos();
                    Montage b = panels.get(real_pos);
                    MontageParameters iopane = new MontageParameters(new Dimension(b.nb_rows, b.nb_cols), true, b.getSize());
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Create a Block", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        b.changeMontageLayout(iopane.getNbRows(), iopane.getNbCols(), iopane.isMeander());
                        b.setResolution(resolution);
                        b.setToWidth(getSizeInPx());
                        b.setWidth_in_px(getSizeInPx());
                        b.setWidth_in_cm(getSizeInCm());
                        montageChanged(null);
                        createBackup();
                    }
                } else {
                    blinkAnything(tableList.getParent());
                    CommonClassesLight.Warning(this, "Please select a Panel in the 'Panels' list first!");
                    return;
                }
            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                if (curPanel != null) {
                    Montage b = curPanel;
                    MontageParameters iopane = new MontageParameters(new Dimension(b.nb_rows, b.nb_cols), true, b.getSize());
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Create a Block", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        b.changeMontageLayout(iopane.getNbRows(), iopane.getNbCols(), iopane.isMeander());
                        forceSameHeight(rows.get(figureList.getSelectedIndex()));
                        RowContentListValueChanged(null);
                        updateFigure();
                        createBackup();
                    }
                }
            }
        }
        if (source == deleteImagesFromTheList) {
            /*
             * removes selected images from the 'image list'
             */
            if (!isThereAnythingToCreate() || myList1.getSelectedIndex() == -1) {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, "Please select something in the 'Image List' first");
                return;
            }
            ArrayList<String> list = myList1.getSelection();
            for (String string : list) {
                imported_from_J.remove(string);
                imported_from_pixel_size.remove(string);
                myList1.removeSelection();
            }
            try {
                System.gc();
            } catch (Exception e) {
            }
        }
        if (source == moveUpInList) {
            /*
             * changes order of images in the 'image list'
             */
            if (!isThereAnythingToCreate() || myList1.getSelectedIndex() == -1) {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, "Please select something in the 'Image List' first");
                return;
            }
            int selection = myList1.getSelectedIndex();
            if (selection - 1 >= 0) {
                myList1.swap(selection, selection - 1);
                myList1.setSelectedIndex(selection - 1);
            }
        }
        if (source == moveDownInList) {
            /*
             * changes order of images in the 'image list'
             */
            if (!isThereAnythingToCreate() || myList1.getSelectedIndex() == -1) {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, "Please select something in the 'Image List' first");
                return;
            }
            int selection = myList1.getSelectedIndex();
            if (selection + 1 < myList1.Size()) {
                myList1.swap(selection, selection + 1);
                try {
                    myList1.setSelectedIndex(selection + 1);
                } catch (Exception e) {
                }
            }
        }
        if (source == importFromIJ) {
            /*
             * imports to SF the selected image in ImageJ
             */
            BufferedImage img = CommonClassesLight.getFromImageJ();
            if (img != null) {
                String title = CommonClassesLight.getTitleJ();
                if (title == null || title.trim().equals("")) {
                    title = CommonClassesLight.create_number_of_the_appropriate_size(import_J++, 5);
                }
                title = "importJ:" + title;
                if (imported_from_J.containsKey(title)) {
                    while (imported_from_J.containsKey(title)) {
                        title += "2";
                    }
                }
                myList1.addDirectlyToList(title, img);
                imported_from_J.put(title, new SerializableBufferedImage2(img));
                imported_from_pixel_size.put(title, CommonClassesLight.getFileInfo().pixelWidth);
            }
        }
        if (source == createPanelAutomatically) {
            /*
             * automatically creates several panels of images having the same size or the same AR
             */
            if (!isThereAnythingToCreate()) {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, "Please add some images in your 'image list' first");
                return;
            }
            ArrayList<String> images = myList1.getFullList();
            ArrayList<Object> shapes = new ArrayList<Object>();
            int size_x = -1;
            int size_y = -1;
            for (String string : images) {
                MyImage2D tmp = getImage(string);
                if (size_x == -1) {
                    size_x = tmp.getImageWidth();
                    size_y = tmp.getImageHeight();
                }
                if (size_x == tmp.getImageWidth() && size_y == tmp.getImageHeight()) {
                    shapes.add(tmp);
                } else {
                    /*
                     * we put all the images that have the same size or same AR in a panel
                     */
                    Dimension squary_size = best_rectangle_finder(shapes.size());
                    addMontage(shapes, squary_size.width, squary_size.height, false, "", getSpaceBetweenImages());
                    shapes.clear();
                    shapes.add(tmp);
                    size_x = tmp.getImageWidth();
                    size_y = tmp.getImageHeight();
                }
            }
            if (!shapes.isEmpty()) {
                Dimension squary_size = best_rectangle_finder(shapes.size());
                addMontage(shapes, squary_size.width, squary_size.height, false, "", getSpaceBetweenImages());
                if (remove_when_added) {
                    myList1.clearList();
                }
            }
            showAppropriateOptions(tableList);
            createBackup();
        }
        if (source == addSelectionToCurrentPanel || source == addSelectedImagesInCurPanel) {
            /**
             * add an image to the current panel
             */
            if (jTabbedPane1.getSelectedIndex() == 1 || jTabbedPane1.getSelectedIndex() == 0) {
                if (tableList.getSelectedIndex() != -1) {
                    if (myList1.getSelectedIndex() != -1) {
                        int real_pos = getRealPos();
                        Montage b = panels.get(real_pos);
                        ArrayList<String> image_names = myList1.getSelection();//MyList2.MULTI_SEL);
                        for (String string : image_names) {
                            if (!string.contains("importJ:")) {
                                if (!string.toLowerCase().endsWith(".svg") && !string.toLowerCase().endsWith(".figur")) {
                                    b.addShape2(new MyImage2D.Double(0, 0, string));
                                } else if (string.toLowerCase().endsWith(".figur")) {
                                    checkRstatus();
                                    MyPlotVector.Double tmp = (MyPlotVector.Double) new Loader().loadObjectRaw(string);
                                    /**
                                     * dirty fix for erroneous name of figur
                                     * files since files can be moved we need to
                                     * update their filename at loading
                                     */
                                    if (tmp instanceof Namable) {
                                        ((Namable) tmp).setFullName(string);
                                    }
                                    ((MyPlotVector.Double) tmp).reloadDocFromString();
                                    b.addShape2(tmp);
                                } else {
                                    b.addShape2(new MyImageVector.Double(0, 0, string, new Loader().loadSVGDocument(string)));
                                }
                            } else {
                                MyImage2D.Double tmp = new MyImage2D.Double(0, 0, string, imported_from_J.get(string).getBufferedImage());
                                if (imported_from_pixel_size.get(string) != null) {
                                    tmp.setSize_of_one_px_in_unit(imported_from_pixel_size.get(string));
                                } else {
                                    tmp.setSize_of_one_px_in_unit(1);
                                }
                                b.addShape2(tmp);
                            }
                        }
                        b.update();
                        b.setResolution(resolution);
                        b.setToWidth(getSizeInPx());
                        b.setWidth_in_px(getSizeInPx());
                        b.setWidth_in_cm(getSizeInCm());
                        curPanel = b;
                        panels.put(real_pos, b);
                        montageChanged(null);
                        if (remove_when_added) {
                            myList1.removeSelection();
                        }
                        showAppropriateOptions(tableList);
                        createBackup();
                    } else {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please Selecet One Or More Images In The 'Image List' First");
                        return;
                    }
                } else {
                    createPanelFromSelection.doClick();
                }
            } else if (jTabbedPane1.getSelectedIndex() == 2) {
                /**
                 * add an image to the current panel
                 */
                if (RowContentList.getSelectedIndex() != -1) {
                    ArrayList<String> image_names = new ArrayList<String>();
                    if (!myList1.isEmpty()) {
                        MyListLight ml2 = new MyListLight((DefaultListModel) myList1.list.getModel());
                        ml2.list.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                        int result = JOptionPane.showOptionDialog(this, new Object[]{ml2}, "Add images to current panel", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (result == JOptionPane.OK_OPTION) {
                            image_names = ml2.getSelectedValues();
                        } else {
                            return;
                        }
                    } else {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please add images to your 'image list' first");
                        return;
                    }

                    if (!image_names.isEmpty()) {
                        Montage b = curPanel;
                        for (String string : image_names) {
                            if (!string.contains("importJ:")) {
                                if (!string.toLowerCase().endsWith(".svg") && !string.toLowerCase().endsWith(".figur")) {
                                    b.addShape2(new MyImage2D.Double(0, 0, string));
                                } else if (string.toLowerCase().endsWith(".figur")) {
                                    checkRstatus();
                                    MyPlotVector.Double tmp = (MyPlotVector.Double) new Loader().loadObjectRaw(string);
                                    ((MyPlotVector.Double) tmp).reloadDocFromString();
                                    b.addShape2(tmp);
                                } else {
                                    b.addShape2(new MyImageVector.Double(0, 0, string, new Loader().loadSVGDocument(string)));
                                }
                            } else {
                                MyImage2D.Double tmp = new MyImage2D.Double(0, 0, string, imported_from_J.get(string).getBufferedImage());
                                if (imported_from_pixel_size.get(string) != null) {
                                    tmp.setSize_of_one_px_in_unit(imported_from_pixel_size.get(string));
                                } else {
                                    tmp.setSize_of_one_px_in_unit(1);
                                }
                                b.addShape2(tmp);
                            }
                        }
                        b.update();
                        forceSameHeight(rows.get(figureList.getSelectedIndex()));
                        RowContentListValueChanged(null);
                        updateFigure();
                        curPanel = b;
                        if (remove_when_added) {
                            myList1.removeSelection();
                        }
                        showAppropriateOptions(RowContentList);
                        createBackup();
                    } else {
                        blinkAnything(myList1);
                        CommonClassesLight.Warning(this, "Please Selecet One Or More Images In The 'Image List' First");
                        return;
                    }
                } else {
                    createPanelFromSelection.doClick();
                }
            }
        }
        if (source == createPanelFromSelection) {
            /*
             * create a panel out of selected images in the 'image list'
             */
            if (!myList1.isEmpty() && myList1.getSelectedIndex() != -1) {
                Dimension squary_size = best_rectangle_finder(myList1.getSelection().size());
                MontageParameters iopane = new MontageParameters(squary_size, myList1.getSelection().size());
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Create a Panel", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    ArrayList<String> images = myList1.getSelection();
                    ArrayList<Object> shapes = getShapes(images, iopane.getNbCols() == 1 || iopane.getNbRows() == 1);
                    if (shapes == null || shapes.isEmpty()) {
                        return;
                    }
                    addMontage(shapes, iopane.getNbRows(), iopane.getNbCols(), iopane.isMeander(), iopane.getLetter(), getSpaceBetweenImages());
                    showAppropriateOptions(tableList);
                    createBackup();
                }
            } else {
                blinkAnything(myList1);
                CommonClassesLight.Warning(this, "Please select some images in the 'Image List' First!");
                return;
            }
        }
        if (source == addSelectedPanelToNewRow) {
            /*
             * create a new row from the selected panel
             */
            int selection;
            selection = tableList.getSelectedIndex();
            if (selection != -1) {
                loading = true;
                int real_pos = getRealPos(selection);
                ArrayList<Object> row_panels = new ArrayList<Object>();
                Montage m = panels.get(real_pos);
                row_panels.add(m);
                Row row = new Row(row_panels, getSpaceBetweenRows());
                row.setResolution(resolution);
                row.setToWidth(getSizeInPx());
                row.setWidth_in_px(getSizeInPx());
                row.setWidth_in_cm(getSizeInCm());
                rows.add(row);
                figureListModel.addElement("" + real_pos);
                figureList.setSelectedIndex(figureListModel.getSize() - 1);
                row.setFormula(figureListModel.lastElement().toString());
                figureListValueChanged(null);
                tableListModel.remove(selection);
                tableList.setSelectedIndex(selection - 1);
                if (tableList.getSelectedIndex() == -1) {
                    tableList.setSelectedIndex(0);
                }
                loading = false;
                updateFigure();
                figureListValueChanged(null);
                showAppropriateOptions(figureList);
                /*
                 * bug fix pour display
                 */
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jScrollPane16.validate();
                    }
                });
                updatePositionFigure(current_row);
                createBackup();
            } else {
                blinkAnything(tableList.getParent());
                CommonClassesLight.Warning(this, "Please Select A Panel In The 'Panels' List First");
                return;
            }
        }
        if (source == addSelectedPanelToSelectedRow) {
            /*
             * add the selected panel to the current row --> add a new column to the current row
             */
            int selection;
            selection = tableList.getSelectedIndex();
            if (selection != -1) {
                int real_pos = getRealPos(selection);
                Row row;
                int pos_fig = figureList.getSelectedIndex();
                if (pos_fig == -1) {
                    if (figureListModel.isEmpty()) {
                        addSelectedPanelToNewRow.doClick();
                        return;
                    }
                    blinkAnything(figureList.getParent());
                    CommonClassesLight.Warning(this, "Please Select A Row in The 'Final Figure' list");
                    return;
                }
                if (rows != null && !rows.isEmpty()) {
                    row = (Row) rows.get(pos_fig);
                    Montage m = panels.get(real_pos);
                    row.addBlock(m);
                    current_row = row;
                    reforceSameHeight(row);
                    rows.set(pos_fig, row);
                    figureListModel.set(pos_fig, figureListModel.get(pos_fig) + "+" + getRealPos(selection));
                    row.setFormula(figureListModel.get(pos_fig).toString());
                } else {
                    row = new Row(panels.get(real_pos), getSpaceBetweenRows());
                    forceSameHeight(row);
                    rows.add(row);
                    figureListModel.addElement(rows.size());
                    row.setFormula(figureListModel.lastElement().toString());
                }
                figureListValueChanged(null);
                tableListModel.remove(selection);
                updateFigure();
                showAppropriateOptions(figureList);
                updatePositionFigure(row);
                createBackup();
            } else {
                addSelectedPanelToNewRow.doClick();
            }
        }
        if (source == moveColLeft || source == moveColRight || (source == moveLeft && (jTabbedPane1.getSelectedIndex() == 2)) || (source == moveRight && (jTabbedPane1.getSelectedIndex() == 2))) {
            /*
             * we change the order of columns
             */
            int neo_pos = -1;
            if (source == moveColRight || source == moveRight) {
                neo_pos = 1;
            }
            int pos = RowContentList.getSelectedIndex();
            if (curPanel == null) {
                blinkAnything(RowContentList.getParent());
                CommonClassesLight.Warning(this, "Please select a single panel from the 'row content' list");
                return;
            }
            boolean success = ((Row) current_row).swap(pos, pos + neo_pos);
            if (success) {
                RowContentList.setSelectedIndex(pos + neo_pos);
                swapList(RowContentListModel, pos, pos + neo_pos);
                RowContentListValueChanged(null);
                updateFigure();
                createBackup();
                doubleLayerPane2.ROIS.setSelectedShape(curPanel);
                updatePositionFigure(curPanel);
            }
        }
        if (source == deleteSelectColumn || ((source == deleteButton && jTabbedPane1.getSelectedIndex() == 2) && jPanel16.getParent() == optionsPanel)) {
            /*
             * we remove a single panel from the figure and we update
             */
            imagesInFigureModel.clear();
            cur_sel_image2 = null;
            String newtext = "";
            if (figureList.getSelectedIndex() == -1) {
                blinkAnything(RowContentList.getParent());
                CommonClassesLight.Warning(this, "Please select a single panel from the 'row content' list");
                return;
            }
            Object cur_Block = RowContentList.getSelectedValue();
            if (cur_Block == null) {
                blinkAnything(RowContentList.getParent());
                CommonClassesLight.Warning(this, "Please select a single panel from the 'row content' list");
                return;
            }
            String txt = figureListModel.get(figureList.getSelectedIndex()).toString();
            if (txt.contains("+")) {
                String[] nbs = txt.split("\\+");
                if (nbs.length > 0) {
                    int count = 0;
                    for (String string : nbs) {
                        Montage panel = panels.get(CommonClassesLight.String2Int(string));
                        if (panel != cur_Block) {
                            newtext += nbs[count] + "+";
                        } else {
                            tableListModel.addElement(string);
                        }
                        count++;
                    }
                    if (newtext.endsWith("+")) {
                        newtext = newtext.substring(0, newtext.length() - 1);
                    }
                    ((Row) current_row).setFormula(newtext);
                    figureListModel.set(figureList.getSelectedIndex(), ((Row) current_row).getFormula());
                    if (cur_Block instanceof Montage) {
                        ((Row) current_row).removeBlock((Montage) cur_Block);
                        ((Montage) cur_Block).setFirstCorner();
                        reforceSameHeight(current_row);
                    }
                    RowContentListModel.removeElement(cur_Block);
                    figureListValueChanged(null);
                    updateFigure();
                    updatePositionFigure(current_row);
                    createBackup();
                } else {
                    source = removeSelectedRow;
                }
            } else {
                source = removeSelectedRow;
            }
        }
        if (source == removeSelectedRow) {
            /*
             * we remove an entire row from a figure
             */
            if (figureList.getSelectedIndex() == -1) {
                blinkAnything(figureList.getParent());
                CommonClassesLight.Warning(this, "Please select a row in the 'Final Figure' list");
                return;
            }
            /*
             * we remove several panels from the figure
             */
            int[] selection = figureList.getSelectedIndices();
            cur_sel_image2 = null;
            imagesInFigureModel.clear();
            RowContentListModel.clear();
            if (selection != null) {
                Arrays.sort(selection);
                for (int i = 0; i < selection.length / 2; i++) {
                    int tmp = selection[i];
                    selection[i] = selection[selection.length - (i + 1)];
                    selection[selection.length - (i + 1)] = tmp;
                }
                for (int i : selection) {
                    Row r = (Row) rows.get(i);
                    rows.remove(i);
                    String txt = figureListModel.get(i).toString();
                    String[] nbs = txt.split("\\+");
                    for (String string : nbs) {
                        tableListModel.addElement(string);
                        Montage panel = panels.get(CommonClassesLight.String2Int(string));
                        panel.setFirstCorner();
                    }
                }
                for (int i : selection) {
                    loading = true;
                    figureListModel.remove(i);
                    loading = false;
                }
                current_row = null;
                figureListValueChanged(null);
                createBackup();
                try {
                    System.gc();
                } catch (Exception e) {
                }
            }
            updateFigure();
        }
        if (source == moveRowRight || source == moveRowLeft) {
            /*
             * we change the order of rows
             */
            int selection = figureList.getSelectedIndex();
            /*
             * bug fix for a bug discovered using the debug button
             */
            if (selection != -1) {
                int neo_sel;
                if (source == moveRowLeft) {
                    neo_sel = selection - 1;
                } else {
                    neo_sel = selection + 1;
                }
                if (neo_sel < figureListModel.size() && neo_sel >= 0) {
                    swapRows(rows, selection, neo_sel);
                    swapList(figureListModel, selection, neo_sel);
                    doubleLayerPane2.ROIS.resetROIS();
                    updateFigure();
                    figureList.setSelectedIndex(neo_sel);
                    updatePositionFigure(current_row);
                    createBackup();
                }
            } else {
                blinkAnything(figureList.getParent());
                CommonClassesLight.Warning(this, "Please select a row in the 'Final Figure' list");
                return;
            }
        }
        if ((source == updateLetters || source == jTextField1) && jTabbedPane1.getSelectedIndex() == 2) {
            /*
             * we reletter the figure
             */
            String last_letter = jTextField1.getText();
            if (rows != null && !rows.isEmpty()) {
                for (Object row : rows) {
                    Row cur_row = ((Row) row);
                    for (Object panel : cur_row.blocks) {
                        Montage cur_panel = ((Montage) panel);
                        last_letter = cur_panel.reletter(last_letter);
                    }
                }
                updateFigure();
                doubleLayerPane2.ROIS.setSelectedShape(current_row);
            } else {
                blinkAnything(figureList.getParent());
                CommonClassesLight.Warning(this, "Please create a Figure first");
                return;
            }
            createBackup();
        }
        if (source == newJournalStyle) {
            /*
             * we get it to create a new unique journal style that will then be loaded to a combo
             */
            JournalParametersDialog iopane = new JournalParametersDialog();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Journal Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                iopane.saveStyle(PopulateJournalStyles.journalStyleFolder);
                PopulateJournalStyles.journalStyles.add(iopane.getJournalStyle());
                loading = true;
                String style = null;
                if (journalCombo.getSelectedIndex() != -1) {
                    style = journalCombo.getSelectedItem().toString();
                }
                styles.reloadStyles(journalCombo);
                if (style != null) {
                    journalCombo.setSelectedItem(style);
                } else {
                    journalCombo.setSelectedIndex(-1);
                }
                loading = false;
            }
        }
        if (source == jMenuItem11) {
            /* 
             * we edit the current journal style
             */
            int style_pos = journalCombo.getSelectedIndex();
            if (style_pos != -1 && PopulateJournalStyles.journalStyles != null && !PopulateJournalStyles.journalStyles.isEmpty()) {
                JournalParameters jp = PopulateJournalStyles.journalStyles.get(style_pos);
                JournalParametersDialog iopane = new JournalParametersDialog(jp);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Journal Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    iopane.overWriteStyle(PopulateJournalStyles.journalStyleFolder, jp.getPath());
                    JournalParameters jpMod = iopane.getJournalStyle();
                    jpMod.setPath(jp.getPath());
                    PopulateJournalStyles.journalStyles.set(style_pos, jpMod);
                    defaultStyle = jpMod;
                    styles.reloadStyles(journalCombo);
                    journalCombo.setSelectedIndex(style_pos);
                }
            } else {
                if (style_pos != -1) {
                    CommonClassesLight.Warning(this, "No Journal Style could be found\nin the folder entitled '" + PopulateJournalStyles.journalStyleFolder + "'");
                    return;
                } else {
                    blinkAnything(journalCombo);
                    CommonClassesLight.Warning(this, "Please select a journal style first");
                    return;
                }
            }
        }
        if (source == deleteJournalStyle) {
            /*
             * we delete the selected journal style
             */
            if (journalCombo.getSelectedIndex() == -1) {
                blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }
            JLabel iopane = new JLabel("<html>The file will be permanently deleted<BR>Are you sure you want to continue ?</html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                int pos = journalCombo.getSelectedIndex();
                if (pos != -1) {
                    JournalParameters jp = PopulateJournalStyles.journalStyles.get(pos);
                    new File(jp.getPath()).renameTo(new File(jp.getPath() + ".old"));//comme ca c'est pas trop mechant
                    PopulateJournalStyles.journalStyles.remove(pos);
                    styles.reloadStyles(journalCombo);
                    journalCombo.setSelectedIndex(-1);
                }
            }
        }
        if (source == jMenuItem18 || (source instanceof String && source.toString().toLowerCase().contains("option"))) {
            /*
             * Edit SF prefs
             */
            SFPrefs iopane = new SFPrefs(isUndoRedoAllowed, MAX_UNDOS, remove_when_added, mustWarnOnQuit, useNativeDialog, showHelpInfoWindow, showHints, autoPositionScrollpanes, useAllCores, nbOfCPUs2Use);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "ScientiFig Preferences", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                isUndoRedoAllowed = iopane.isUndoAllowed();
                MAX_UNDOS = iopane.getNbOfUndosRedos();
                remove_when_added = iopane.getRemoveFromList();
                mustWarnOnQuit = iopane.isWarnOnQuit();
                autoPositionScrollpanes = iopane.isAutoScroll();
                showHints = iopane.isShowHints();
                updateGlassPane();
                useNativeDialog = iopane.isUseNativeDialogsWhenApplicable();
                showHelpInfoWindow = iopane.isShowHelpWindow();//  p.setProperty("Show help info window", showHelpInfoWindow + "");p.setProperty("Show help info window", showHelpInfoWindow + "");
                nbOfCPUs2Use = iopane.getNumberOfCPU2Use();
                CommonClassesLight.setMaxNbOfProcessors(nbOfCPUs2Use);
                useAllCores = iopane.isUseAllCores();
                if (useAllCores) {
                    CommonClassesLight.setMaxNbOfProcessorsToMaxPossible();
                }
                allowUndoRedo();
                /*
                 * we need to create a first backup
                 */
                if (isUndoRedoAllowed) {
                    createBackup();
                }
            }
        }
        if (source == removeAnnotations) {
            /*
             * removes image annotations
             */
            Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel instanceof MyImage2D) {
                ((MyImage2D) cur_sel).removeAllAnnotations();
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    updateTable();
                    doubleLayerPane1.ROIS.setSelectedShape(cur_sel);
                } else {
                    updateFigure();
                    doubleLayerPane2.ROIS.setSelectedShape(cur_sel);
                }
            } else {
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    blinkAnything(tableContentList.getParent());
                } else if (jTabbedPane1.getSelectedIndex() == 2) {
                    blinkAnything(imagesInFigureList.getParent());
                }
                CommonClassesLight.Warning(this, "Please Select A Single Image From The 'Panel content' List");
                return;
            }
        }
        if (source == AnnotateImageROIs) {
            /*
             * here we annotate the selected image (add floating text, ROIs, ...)
             */
            Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel instanceof MyImageVector) {
                CommonClassesLight.Warning(this, "Vector images and plots cannot be annotated from within the software\n You can annotate them externally and 'replace'\nthe non annotated version with the annotated one");
                return;
            }
            if (cur_sel instanceof MyImage2D) {
                AnnotateCurrentImage iopane = new AnnotateCurrentImage((MyImage2D) cur_sel, copiedROIs);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Annotate The Image", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    ((MyImage2D) cur_sel).setAssociatedObjects(iopane.getAssociatedObjects());
                    if (iopane.hasInset()) {
                        ((MyImage2D) cur_sel).setInset(iopane.getInset());
                    }
                    if (jTabbedPane1.getSelectedIndex() == 1) {
                        updateTable();
                        doubleLayerPane1.ROIS.setSelectedShape(cur_sel);
                    } else {
                        updateFigure();
                        doubleLayerPane2.ROIS.setSelectedShape(cur_sel);
                    }

                }
                /**
                 * we keep the copied ROIs so that we can paste them on another
                 * image
                 */
                try {
                    this.copiedROIs = iopane.getCopiedROIs();
                } catch (Exception e) {
                }
            } else {
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    blinkAnything(tableContentList.getParent());
                } else if (jTabbedPane1.getSelectedIndex() == 2) {
                    blinkAnything(imagesInFigureList.getParent());
                }
                CommonClassesLight.Warning(this, "Please Select A Single Image From The 'Panel Content' List");
                return;
            }
        }

        if (source == AnnotateImageText) {
            /*
             * force the imageEditor frame to show if not showing. Can be useful if the user manually closed it.
             */
            ief.setVisible(!ief.isVisible());
            return;
        }
        if (source == ChangeStrokeSize) {
            /*
             * here we change stroke size of objects contained in images and graphs
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            LineGraphicsDialog iopane = new LineGraphicsDialog(false, true, true, true, -1, false);
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Stroking Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).setStrokeSize(iopane.getNewStrokeSize(), iopane.getNewPointSize(), iopane.isChangePointSize(), iopane.isChangeStrokeSizeForGraphs(), iopane.isChangeStrokeSizeForSVGs(), iopane.isChangeStrokeSizeForImageROIs(), iopane.isStrokeIllustrator());
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).setStrokeSize(iopane.getNewStrokeSize(), iopane.getNewPointSize(), iopane.isChangePointSize(), iopane.isChangeStrokeSizeForGraphs(), iopane.isChangeStrokeSizeForSVGs(), iopane.isChangeStrokeSizeForImageROIs(), iopane.isStrokeIllustrator());
                    }
                    updateTable();
                    createBackup();
                }
            }
        }
        if (source == jMenuItem12) {
            /*
             * easy way to change font settings of a figure without using styles
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            TextColor iopane = new TextColor();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "General Text Parameters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).setTextAndBgColor(iopane.getColor(), iopane.getBgColor(), iopane.getTextFont(), iopane.isChangeColor(), iopane.isChangeBgColor(), iopane.isChangeFont(), iopane.isOverrideItalics(), iopane.isOverrideBolding(), iopane.isOverrideBoldingForLetters());
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).setTextAndBgColor(iopane.getColor(), iopane.getBgColor(), iopane.getTextFont(), iopane.isChangeColor(), iopane.isChangeBgColor(), iopane.isChangeFont(), iopane.isOverrideItalics(), iopane.isOverrideBolding(), iopane.isOverrideBoldingForLetters());
                    }
                    updateTable();
                    createBackup();
                }
            }
        }
        if (source == jMenuItem16) {
            /*
             * applies a style to all panels or all rows
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (defaultStyle == null) {
                blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please Select A Style First!!!\nStyles Can Be Selected In The Toolbar Below The Menu Bar.");
                return;
            } else {
                LineGraphicsDialog iopane = new LineGraphicsDialog(true, true, true, true, -1, true);
                int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    if (jTabbedPane1.getSelectedIndex() == 2) {
                        for (Object row : rows) {
                            ((Row) row).setJournalStyle(defaultStyle, iopane.isChangeStrokeSizeForSVGs(), iopane.isChangeStrokeSizeForImageROIs(), iopane.isChangeStrokeSizeForGraphs(), iopane.isChangePointSize(), iopane.isStrokeIllustrator(), iopane.isOverrideItalic(), iopane.isOverrideBold(), iopane.isOverrideBoldForLetter());
                        }
                        updateFigure();
                        createBackup();
                    } else {
                        ArrayList<String> list_of_tables = getListContent(tableListModel);
                        for (String string : list_of_tables) {
                            ((Montage) panels.get(CommonClassesLight.String2Int(string))).setJournalStyle(defaultStyle, iopane.isChangeStrokeSizeForSVGs(), iopane.isChangeStrokeSizeForImageROIs(), iopane.isChangeStrokeSizeForGraphs(), iopane.isChangePointSize(), iopane.isStrokeIllustrator(), iopane.isOverrideItalic(), iopane.isOverrideBold(), iopane.isOverrideBoldForLetter());
                        }
                        updateTable();
                        createBackup();
                    }
                }
            }
        }
        if (source == jMenuItem17) {
            /*
             * resize panels or rows
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            int size = getSizeInPx();
            if (jTabbedPane1.getSelectedIndex() == 2) {
                for (Object row : rows) {
                    ((Row) row).setToWidth(size);
                }
                updateFigure();
                createBackup();
            } else {
                ArrayList<String> list_of_tables = getListContent(tableListModel);
                for (String string : list_of_tables) {
                    ((Montage) panels.get(CommonClassesLight.String2Int(string))).setToWidth(size);
                }
                updateTable();
                createBackup();
            }
        }
        if (source == removeAllText) {
            /*
             * strips all text, letters, ... from panels or rows
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }

            /*
             * we warn that it will remove all the text
             */
            String montage_or_fig = "Montages";
            if (jTabbedPane1.getSelectedIndex() == 2) {
                montage_or_fig = "Figure";
            }
            JLabel iopane = new JLabel("<html>This will remove the text from your " + montage_or_fig + ".<BR>Do you really want to continue ?</html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                ief.setVisible(false);
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).removeAllText();
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).removeAllText();
                    }
                    updateTable();
                    createBackup();
                }
            }
        }
        if (source == removeAllAnnotations) {
            /*
             * strips all scale bars from panels or rows
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            /*
             * we warn that it will remove all the scale bars
             */
            String montage_or_fig = "Montages";
            if (jTabbedPane1.getSelectedIndex() == 2) {
                montage_or_fig = "Figure";
            }
            JLabel iopane = new JLabel("<html>This will remove annotations from your " + montage_or_fig + ".<BR>Do you really want to continue ?</html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).removeAllAnnotations();
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).removeAllAnnotations();
                    }
                    updateTable();
                    createBackup();
                }
            }
        }

        if (source == checkGraph) {
            /*
             * Capitalizes or sets to lower case the first letter of a caption
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking graphs...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);
                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            boolean containsGraphs = false;
                            for (Object row : rows) {
                                if (((Row) row).containsStrictlyMyPlotVector()) {
                                    containsGraphs = true;
                                    break;
                                }
                            }
                            if (!containsGraphs) {
                                for (Object row : rows) {
                                    ((Row) row).wasCheckedForGraphs = true;
                                }
                                figureListValueChanged(null);
                                return;
                            }
                            if (!CommonClassesLight.isRReady()) {
                                reinitRsession();
                                if (!CommonClassesLight.isRReady()) {
                                    blinkAnything(Rstatus);
                                    JLabel warning = new JLabel("<html><font color=\"#FF0000\">We recommend you first establish a connection<br>to R before running this check</font>");
                                    int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{warning}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                                    if (result == JOptionPane.CANCEL_OPTION) {
                                        return;
                                    }
                                }
                            }
                            for (Object row : rows) {
                                ((Row) row).checkGraph(jp);
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            boolean containsGraphs = false;
                            for (String string : list_of_tables) {
                                if (((Montage) panels.get(CommonClassesLight.String2Int(string))).containsStrictlyMyPlotVector()) {
                                    containsGraphs = true;
                                    break;
                                }
                            }
                            if (!containsGraphs) {
                                for (String string : list_of_tables) {
                                    ((Montage) panels.get(CommonClassesLight.String2Int(string))).wasCheckedForGraphs = true;
                                }
                                montageChanged(null);
                                return;
                            }
                            if (!CommonClassesLight.isRReady()) {
                                reinitRsession();
                                if (!CommonClassesLight.isRReady()) {
                                    blinkAnything(Rstatus);
                                    JLabel warning = new JLabel("<html><font color=\"#FF0000\">We recommend you first establish a connection<br>to R before running this check</font>");
                                    int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{warning}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                                    if (result == JOptionPane.CANCEL_OPTION) {
                                        return;
                                    }
                                }
                            }
                            for (String string : list_of_tables) {
                                ((Montage) panels.get(CommonClassesLight.String2Int(string))).checkGraph(jp);
                            }
                            updateTable();
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }

        if (source == checkText) {
            /*
             * Capitalizes or sets to lower case the first letter of a caption
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking text...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);
                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            for (Object row : rows) {
                                ((Row) row).checkText(jp);
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            for (String string : list_of_tables) {
                                ((Montage) panels.get(CommonClassesLight.String2Int(string))).checkText(jp);
                            }
                            updateTable();
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }

        if (source == checkStyle) {
            /*
             * Capitalizes or sets to lower case the first letter of a caption
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking text style...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);

                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            for (Object row : rows) {
                                ((Row) row).checkStyle();
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            for (String string : list_of_tables) {
                                ((Montage) panels.get(CommonClassesLight.String2Int(string))).checkStyle();
                            }
                            updateTable();
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }
        if (source == checkSize) {
            /*
             * Capitalizes or sets to lower case the first letter of a caption
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking figure size...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);

                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            for (Object row : rows) {
                                ((Row) row).checkSize(jp);
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            for (String string : list_of_tables) {
                                ((Montage) panels.get(CommonClassesLight.String2Int(string))).checkSize(jp);
                            }
                            updateTable();
                            /*
                             * we update the size of the montage
                             */
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }
        if (source == checkLineArts) {
            /*
             * Checks the line/point width of line arts
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking line arts...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);

                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            boolean containsImportedVectorGraphics = false;
                            for (Object row : rows) {
                                if (((Row) row).containsStrictlyImageVector()) {
                                    containsImportedVectorGraphics = true;
                                    break;
                                }
                            }
                            IllustratorOrInkscape iopane = new IllustratorOrInkscape();
                            if (containsImportedVectorGraphics) {
                                int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Illustrator or Inkscape", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                                if (result == JOptionPane.OK_OPTION) {
                                    boolean isIllustrator = iopane.isIllustrator();
                                    for (Object row : rows) {
                                        ((Row) row).checklineArts(jp.getObjectsStrokeSize(), isIllustrator);
                                    }
                                }
                            } else {
                                for (Object row : rows) {
                                    ((Row) row).checklineArts(jp.getObjectsStrokeSize(), true);
                                }
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            boolean containsImportedVectorGraphics = false;
                            for (String string : list_of_tables) {
                                if (((Montage) panels.get(CommonClassesLight.String2Int(string))).containsStrictlyImageVector()) {
                                    containsImportedVectorGraphics = true;
                                    break;
                                }
                            }
                            IllustratorOrInkscape iopane = new IllustratorOrInkscape();
                            if (containsImportedVectorGraphics) {
                                int result = JOptionPane.showOptionDialog(CommonClassesLight.getGUIComponent(), new Object[]{iopane}, "Illustrator or Inkscape", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                                if (result == JOptionPane.OK_OPTION) {
                                    boolean isIllustrator = iopane.isIllustrator();
                                    for (String string : list_of_tables) {
                                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).checklineArts(jp.getObjectsStrokeSize(), isIllustrator);
                                    }
                                }
                            } else {
                                for (String string : list_of_tables) {
                                    ((Montage) panels.get(CommonClassesLight.String2Int(string))).checklineArts(jp.getObjectsStrokeSize(), true);
                                }
                            }
                            updateTable();
                            /*
                             * we update the size of the montage
                             */
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }

        if (source == checkFont) {
            /*
             * Checks text font settings
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            if (journalCombo.getSelectedIndex() == -1) {
                blinker.blinkAnything(journalCombo);
                CommonClassesLight.Warning(this, "Please select a journal style first");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*
                     * TODO maybe replace by a progress bar
                     */
                    JDialog jd = null;
                    try {
                        jd = new JDialog(CommonClassesLight.getParentFrame(), "Checking fonts...");
                        JLabel jl = new JLabel("<html><center><h1><font color=\"#FF0000\">Please wait...</font></h1><br><br>NB: this window be closed automatically when controls are over!</center></html>");
                        jd.add(jl);
                        jd.validate();
                        jd.pack();
                        jd.setLocationRelativeTo(CommonClassesLight.getParentFrame());
                        jd.setVisible(true);
                        JournalParameters jp = PopulateJournalStyles.journalStyles.get(journalCombo.getSelectedIndex());
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            for (Object row : rows) {
                                ((Row) row).checkFonts(jp);
                            }
                            updateFigure();
                            figureListValueChanged(null);
                            createBackup();
                        } else {
                            ArrayList<String> list_of_tables = getListContent(tableListModel);
                            for (String string : list_of_tables) {
                                ((Montage) panels.get(CommonClassesLight.String2Int(string))).checkFonts(jp);
                            }
                            updateTable();
                            montageChanged(null);
                            createBackup();
                        }
                    } catch (OutOfMemoryError E) {
                        StringWriter sw = new StringWriter();
                        E.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                        try {
                            System.gc();
                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String stacktrace = sw.toString();
                        System.err.println(stacktrace);
                    } finally {
                        try {
                            if (jd != null) {
                                jd.dispose();
                            }
                        } catch (Exception e2) {
                        }
                    }
                }
            }).start();
        }
        if (source == capitalizeFirstLetter) {
            /*
             * Capitalizes or sets to lower case the first letter of a caption
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            CapitalizeFirstLetterDialog iopane = new CapitalizeFirstLetterDialog();
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Capitalize letters", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).capitalizeText(iopane.getFirstLetterOfTextModification());
                        ((Row) row).capitalizeLetter(iopane.getFirstLetterOfLetterModification());
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).capitalizeText(iopane.getFirstLetterOfTextModification());
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).capitalizeLetter(iopane.getFirstLetterOfLetterModification());
                    }
                    updateTable();
                    createBackup();
                }
            }
        }
        if (source == removeAllScaleBars) {
            /*
             * strips all scale bars from panels or rows
             */
            if (!isThereAnythingCreated()) {
                CommonClassesLight.Warning(this, "Please create some panels or a figure first");
                return;
            }
            /*
             * we warn that it will remove all the scale bars
             */
            String montage_or_fig = "Montages";
            if (jTabbedPane1.getSelectedIndex() == 2) {
                montage_or_fig = "Figure";
            }
            JLabel iopane = new JLabel("<html>This will remove the scale bars from your " + montage_or_fig + ".<BR>Do you really want to continue ?</html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Warning", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == JOptionPane.OK_OPTION) {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    for (Object row : rows) {
                        ((Row) row).removeAllBars();
                    }
                    updateFigure();
                    createBackup();
                } else {
                    ArrayList<String> list_of_tables = getListContent(tableListModel);
                    for (String string : list_of_tables) {
                        ((Montage) panels.get(CommonClassesLight.String2Int(string))).removeAllBars();
                    }
                    updateTable();
                    createBackup();
                }
            }
        }
        if (source == jMenuItem8) {
            /*
             * browse the SF default web page
             */
            new Help_().run(Help_.URL_SF);
        }
        if (source == jMenuItem9) {
            /*
             * show copyright notice
             */
            AuthorPane2 apane = new AuthorPane2("Benoit Aigouy", "Copyright " + CommonClassesLight.COPYRIGHT + " 2012-" + currentyear, software_name + " " + version, "<html>e-mail: <a href=\"mailto:baigouy@gmail.com\">baigouy@gmail.com</a></html>");
            Object[] pane = new Object[1];
            pane[0] = apane;
            JOptionPane.showMessageDialog(this, pane, "About " + software_name + " ...", JOptionPane.PLAIN_MESSAGE);
        }

        if (source == jMenuItem19) {
            /*
             * import images to the 'image list' for heroic people who don't want to use DND
             */
            File[] nom = CommonClassesLight.openFiles(this, lastOpenName, exts, !CommonClassesLight.isWindows() && useNativeDialog);
            if (nom != null) {
                try {
                    lastOpenName = nom[0].getParent();
                } catch (Exception e) {
                }
                myList1.addAll(nom);
                /*
                 * bug fix for DND
                 */
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        myList1.updateUI();
                    }
                });
            }
        }
        if (source == Save || source == jButton7) {
            /*
             * save your work
             */
            if (panels == null || panels.isEmpty()) {
                CommonClassesLight.Warning(this, "Sorry there is nothing to save");
                return;
            }
            if (lastSaveName == null) {
                String name;
                if (!CommonClassesLight.isWindows() && useNativeDialog) {
                    String outFolder = null;
                    if (lastSaveName != null) {
                        outFolder = new File(lastSaveName).getParent();
                    }
                    name = CommonClassesLight.saveFileNative(this, outFolder, ".yf5m");
                    if (name != null && !name.endsWith(".yf5m")) {
                        name += ".yf5m";
                    }
                } else {
                    name = CommonClassesLight.save(this, lastSaveName, "yf5m");
                }
                if (name != null) {
                    String selectedStyle = null;
                    if (journalCombo.getSelectedIndex() != -1) {
                        selectedStyle = journalCombo.getSelectedItem().toString();
                    }
                    final String style = selectedStyle;
                    final String saveName = name;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Exportable ex = new Exportable();
                                ex.save(panels, rows, myList1.getFullList(), style, imported_from_J, imported_from_pixel_size, true, saveName);
                            } catch (Exception e) {
                                StringWriter sw = new StringWriter();
                                e.printStackTrace(new PrintWriter(sw));
                                String stacktrace = sw.toString();
                                System.err.println(stacktrace);
                            } catch (OutOfMemoryError E) {
                                StringWriter sw = new StringWriter();
                                E.printStackTrace(new PrintWriter(sw));
                                String stacktrace = sw.toString();
                                System.err.println(stacktrace);
                                try {
                                    System.gc();
                                    CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                                } catch (Exception e) {
                                }
                            }
                            warnForSave = false;
                        }
                    }).start();
                    lastSaveName = name;
                    updateTitle(lastSaveName);
                    recordSave(name);
                }
            } else {
                String selectedStyle = null;
                if (journalCombo.getSelectedIndex() != -1) {
                    selectedStyle = journalCombo.getSelectedItem().toString();
                }
                final String style = selectedStyle;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Exportable ex = new Exportable();
                            ex.save(panels, rows, myList1.getFullList(), style, imported_from_J, imported_from_pixel_size, true, lastSaveName);
                        } catch (Exception e) {
                        } catch (OutOfMemoryError E) {
                            StringWriter sw = new StringWriter();
                            E.printStackTrace(new PrintWriter(sw));
                            String stacktrace = sw.toString();
                            System.err.println(stacktrace);
                            try {
                                System.gc();
                                CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                            } catch (Exception e) {
                            }
                        }
                        warnForSave = false;
                        recordSave(lastSaveName);
                    }
                }).start();
            }
        }
        if (source == SaveAs) {
            /*
             * save your work
             */
            if (panels == null || panels.isEmpty()) {
                CommonClassesLight.Warning(this, "Sorry there is nothing to save");
                return;
            }
            String outFolder = null;
            if (lastSaveName != null) {
                outFolder = new File(lastSaveName).getParent();
            }
            String name;
            if (!CommonClassesLight.isWindows() && useNativeDialog) {
                name = CommonClassesLight.saveFileNative(this, outFolder, ".yf5m");
                if (name != null && !name.endsWith(".yf5m")) {
                    name += ".yf5m";
                }
            } else {
                name = CommonClassesLight.save(this, lastSaveName, "yf5m");
            }
            if (name != null) {
                String selectedStyle = null;
                if (journalCombo.getSelectedIndex() != -1) {
                    selectedStyle = journalCombo.getSelectedItem().toString();
                }
                final String style = selectedStyle;
                final String saveName = name;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Exportable ex = new Exportable();
                            ex.save(panels, rows, myList1.getFullList(), style, imported_from_J, imported_from_pixel_size, true, saveName);
                        } catch (Exception e) {
                            StringWriter sw = new StringWriter();
                            e.printStackTrace(new PrintWriter(sw));
                            String stacktrace = sw.toString();
                            System.err.println(stacktrace);
                        } catch (OutOfMemoryError E) {
                            StringWriter sw = new StringWriter();
                            E.printStackTrace(new PrintWriter(sw));
                            String stacktrace = sw.toString();
                            System.err.println(stacktrace);
                            try {
                                System.gc();
                                CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                            } catch (Exception e) {
                            }
                        }
                        warnForSave = false;
                    }
                }).start();
                lastSaveName = name;
                updateTitle(lastSaveName);
                recordSave(name);
            }
        }
        if (source == Quit) {
            /*
             * quit the soft
             */
            closing(null);
            return;
        }
        if (source == undo) {
            /*
             * undo 
             */
            if (isUndoRedoAllowed && !undos_and_redos.isEmpty() && cur_pos >= 0) {
                if (cur_pos > 0) {
                    cur_pos--;
                }
                String last_file = undos_and_redos.toArray()[cur_pos].toString();
                loadFile(last_file, false, true);
            }
        }
        if (source == redo) {
            /*
             * redo
             */
            if (isUndoRedoAllowed && !undos_and_redos.isEmpty() && cur_pos < undos_and_redos.size()) {
                if (cur_pos + 1 < undos_and_redos.size()) {
                    cur_pos++;
                } else {
                    cur_pos = undos_and_redos.size() - 1;
                }
                if (cur_pos == -1) {
                    return;
                }
                String last_file = undos_and_redos.toArray()[cur_pos].toString();
                loadFile(last_file, false, true);
            }
        }

        if (source == AddTextLeftOfRow || source == AddTextRightOfRow) {
            /*
             * add text boxes on the left or on the right of the selected row
             */
            if (current_row != null) {
                if (current_row instanceof ArrayList) {
                    blinkAnything(figureList.getParent());
                    CommonClassesLight.Warning(this, "Please only select one row if you want to use this function");
                    return;
                } else {
                    SideBarDialog iopane;
                    if (source == AddTextLeftOfRow) {
                        iopane = new SideBarDialog((Row) current_row, SideBarDialog.VERTICAL_LEFT, defaultStyle);
                    } else {
                        iopane = new SideBarDialog((Row) current_row, SideBarDialog.VERTICAL_RIGHT, defaultStyle);
                    }
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Additional Vertical Text", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        if (true) {
                            HashMap<Point3D.Integer, ColoredTextPaneSerializable> pos_n_text = iopane.getPosAndText();
                            if (!pos_n_text.isEmpty()) {
                                TopBar tb;
                                if (source == AddTextLeftOfRow) {
                                    tb = new TopBar((Row) current_row, pos_n_text, TopBar.VERTICAL_LEFT);
                                    ((Row) current_row).setLeftTextBar(tb);
                                } else {
                                    tb = new TopBar((Row) current_row, pos_n_text, TopBar.VERTICAL_RIGHT);
                                    ((Row) current_row).setRightTextBar(tb);
                                }
                            } else {
                                if (source == AddTextLeftOfRow) {
                                    ((Row) current_row).setLeftTextBar(null);
                                } else {
                                    ((Row) current_row).setRightTextBar(null);
                                }
                            }
                            ((Row) current_row).arrangeRow();
                            /*
                             * We update height settings
                             */
                            reforceSameHeight(current_row);
                        }
                    }
                    updateFigure();
                }
            } else {
                blinkAnything(figureList.getParent());
                CommonClassesLight.Warning(this, "Please select a row in the 'Final Figure' list");
                return;
            }
        }
        if (source == addTextAboveRow || source == addTextBelowRow || source == addLettersOutside) {
            /*
             * add text boxes above or below the selected row
             */
            if (current_row != null) {
                if (current_row instanceof ArrayList) {
                    blinkAnything(figureList.getParent());
                    CommonClassesLight.Warning(this, "Please only select one row if you want to use this function");
                    return;
                } else {
                    SideBarDialog iopane;
                    if (source == addTextAboveRow || source == addLettersOutside) {
                        if (source == addTextAboveRow) {
                            iopane = new SideBarDialog((Row) current_row, SideBarDialog.HORIZONTAL_TOP, defaultStyle);
                        } else {
                            iopane = new SideBarDialog((Row) current_row, SideBarDialog.ADDITIONAL_LETTER, defaultStyle);
                        }
                    } else {
                        iopane = new SideBarDialog((Row) current_row, SideBarDialog.HORIZONTAL_BOTTOM, defaultStyle);
                    }
                    int result = JOptionPane.showOptionDialog(this, new Object[]{iopane}, "Add Horizontal Text Bar", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        HashMap<Point3D.Integer, ColoredTextPaneSerializable> pos_n_text = iopane.getPosAndText();
                        if (!pos_n_text.isEmpty()) {
                            TopBar tb = new TopBar((Row) current_row, pos_n_text, TopBar.HORIZONTAL);
                            if (source == addTextAboveRow || source == addLettersOutside) {
                                if (source == addTextAboveRow) {
                                    ((Row) current_row).setTopTextBar(tb);
                                } else {
                                    ((Row) current_row).setAdditionbalLetterBar(tb);
                                }
                            } else {
                                ((Row) current_row).setBottomTextBar(tb);
                            }
                        } else {
                            if (source == addTextAboveRow) {
                                ((Row) current_row).setTopTextBar(null);
                            } else {
                                ((Row) current_row).setBottomTextBar(null);
                            }
                        }
                        ((Row) current_row).arrangeRow();
                        updateFigure();
                    }
                }
            } else {
                blinkAnything(figureList.getParent());
                CommonClassesLight.Warning(this, "Please select a row in the 'Final Figure' list");
                return;
            }
        }
        //TODO remove all of this
        if (source == journalCombo) {
            /*
             * apply the selected journal style
             */
            if (loading) {
                return;
            }
            int idx = journalCombo.getSelectedIndex();
            /*
             * forces popup window to hide --> suggestion from Falko
             */
            journalCombo.hidePopup();
            if (idx != -1) {
                defaultStyle = PopulateJournalStyles.journalStyles.get(idx);
                setRuler();
                return;
            }
        }
        if (source == jToggleButton1) {
            doubleLayerPane1.setRuler(jToggleButton1.isSelected());
            doubleLayerPane2.setRuler(jToggleButton1.isSelected());
            doubleLayerPane3.setRuler(jToggleButton1.isSelected());
            doubleLayerPane1.repaint();
            doubleLayerPane2.repaint();
            doubleLayerPane3.repaint();
        }
    }

    /**
     * button dispatcher
     *
     * @param evt
     */
    private void runAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAll
        /*
         * we reset multithreading cancellation
         */
        MultiThreadExecuter.cancel = false;
        Object source = null;
        if (evt != null) {
            source = evt.getSource();
        }
        if (source instanceof JMenu) {
            return;
        }
        if (source == loadAllyf5m) {
            loadFiles(yf5m_files, true);
            return;
        }
        runAllnow(source);
    }//GEN-LAST:event_runAll

    /*
     * undo
     */
    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        undo.doClick();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    /*
     * redo
     */
    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        redo.doClick();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void updatePositionInPanel(Object object) {
        if (object == null) {
            return;
        }
        if (autoPositionScrollpanes) {
            try {
                /*
                 * We move to the selected row --> easier to see it
                 */
                if (object instanceof PARoi) {
                    Rectangle scroll = new Rectangle(0, jScrollPane8.getVerticalScrollBar().getValue(), 1, jScrollPane8.getVerticalScrollBar().getVisibleAmount());
                    Rectangle shape = ((PARoi) object).getBounds();
                    shape.x = 0;
                    shape.width = 1;
                    shape.y = (int) ((double) shape.y * doubleLayerPane1.zoom);
                    shape.height = (int) ((double) shape.height * doubleLayerPane1.zoom);
                    Rectangle intersection = scroll.intersection(shape);
                    /*
                     * we scroll to the shape but only if it is not visible enough
                     */
                    if (!scroll.intersects(shape) || (intersection.getHeight() < (int) (0.15 * scroll.height) && intersection.getHeight() < (int) (0.15 * shape.height))) {
                        jScrollPane8.getVerticalScrollBar().setValue((int) ((((PARoi) object).getBounds2D().getY()) * doubleLayerPane1.zoom));
                        jScrollPane8.getHorizontalScrollBar().setValue(0);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void updatePositionFigure(Object objectOfInterest) {
        if (objectOfInterest == null) {
            return;
        }
        if (block_update) {
            return;
        }
        if (autoPositionScrollpanes) {
            try {
                /*
                 * We move to the selected row --> easier to see it
                 */
                if (objectOfInterest instanceof PARoi) {
                    Rectangle scroll = new Rectangle(0, jScrollPane16.getVerticalScrollBar().getValue(), 1, jScrollPane16.getVerticalScrollBar().getVisibleAmount());
                    Rectangle shape = ((PARoi) objectOfInterest).getBounds();
                    shape.x = 0;
                    shape.width = 1;
                    shape.y = (int) ((double) shape.y * doubleLayerPane2.zoom);
                    shape.height = (int) ((double) shape.height * doubleLayerPane2.zoom);
                    Rectangle intersection = scroll.intersection(shape);
                    /*
                     * we scroll to the shape but only if it is not visible enough
                     */
                    if (!scroll.intersects(shape) || (intersection.getHeight() < (int) (0.15 * scroll.height) && intersection.getHeight() < (int) (0.15 * shape.height))) {
                        jScrollPane16.getVerticalScrollBar().setValue((int) ((((PARoi) objectOfInterest).getBounds2D().getY()) * doubleLayerPane2.zoom));
                        jScrollPane16.getHorizontalScrollBar().setValue(0);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private int getRotation() {
        return (((Integer) rotateSpinner.getValue()));
    }

    /**
     *
     * @return the size of the crop on the left
     */
    public int getLeftCrop() {
        return ((Integer) cropLeftSpinner.getValue());
    }

    /**
     *
     * @return the size of the crop on the right
     */
    public int getRightCrop() {
        return ((Integer) cropRightSpinner.getValue());
    }

    /**
     *
     * @return the size to crop at the top
     */
    public int getUpCrop() {
        return ((Integer) cropUpSpinner.getValue());
    }

    /**
     *
     * @return the size to crop at the bottom
     */
    public int getDownCrop() {
        return ((Integer) cropDownSpinner.getValue());
    }

    /**
     * updates crop for selected images
     *
     * @param evt
     */
    private void cropChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cropChanged
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        if (loading || dontChangeCropSize) {
            return;
        }
        if (jTabbedPane1.getSelectedIndex() == 1) {
            Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel != null && curPanel != null) {
                if (cur_sel instanceof ComplexShapeLight) {
                    HashSet<Object> selected_images = ((ComplexShapeLight) cur_sel).getGroup();
                    /*
                     * multi threaded version
                     */
                    MultiThreadExecuter mt = new MultiThreadExecuter();
                    mt.genericCallableWithSavingOutput(selected_images, "crop", getLeftCrop(), getRightCrop(), getUpCrop(), getDownCrop());
                } else {
                    ((MyImage2D) cur_sel).crop(getLeftCrop(), getRightCrop(), getUpCrop(), getDownCrop());
                }
                curPanel.updateTable();
                updateBlockSize();
                /*
                 * bug fix for images of different sizes
                 */
                curPanel.setFirstCorner();
                updatePositionInPanel(cur_sel);
                createBackup();
                updateTable();
            } else {
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    blinkAnything(tableContentList.getParent());
                    if (isThereAnythingInTheLists()) {
                        CommonClassesLight.Warning("Please Select At Least One Image In The 'Panel Content' List");
                    }
                }
                dontChangeCropSize = true;
                cropLeftSpinner.setValue(0);
                cropRightSpinner.setValue(0);
                cropUpSpinner.setValue(0);
                cropDownSpinner.setValue(0);
                dontChangeCropSize = false;
            }
        } else if (jTabbedPane1.getSelectedIndex() == 2) {
            Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel != null && current_row != null) {
                if (cur_sel instanceof ComplexShapeLight) {
                    HashSet<Object> selected_images = ((ComplexShapeLight) cur_sel).getGroup();
                    MultiThreadExecuter mt = new MultiThreadExecuter();
                    mt.genericCallableWithSavingOutput(selected_images, "crop", getLeftCrop(), getRightCrop(), getUpCrop(), getDownCrop());
                } else {
                    ((MyImage2D) cur_sel).crop(getLeftCrop(), getRightCrop(), getUpCrop(), getDownCrop());
                }
                reforceSameHeight(current_row);
                updateFigure();
                doubleLayerPane2.ROIS.setSelectedShape(cur_sel);
                createBackup();
            } else {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    blinkAnything(imagesInFigureList.getParent());
                    if (isThereAnythingInTheLists()) {
                        CommonClassesLight.Warning("Please Select A Single Image From The 'Panel Content' List");
                    }
                }
                dontChangeCropSize = true;
                cropLeftSpinner.setValue(0);
                cropRightSpinner.setValue(0);
                cropUpSpinner.setValue(0);
                cropDownSpinner.setValue(0);
                dontChangeCropSize = false;
            }
        }
        ief.updateImageSize();
    }//GEN-LAST:event_cropChanged

    public void blinkAnything(Component component) {
        if (blinker != null) {
            blinker.blinkAnything(component);
        }
    }

    private void exportToIJMacroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToIJMacroActionPerformed
        recordMacro();
    }//GEN-LAST:event_exportToIJMacroActionPerformed

    private void launchFiguR(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchFiguR
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FiguR_.getInstance().run(null);
            }
        });
    }//GEN-LAST:event_launchFiguR

    /**
     * it is called everytime selection changes in the 'image list'
     *
     * @param evt
     */
    private void imagesInFigureChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_imagesInFigureChanged
        if (loading) {
            return;
        }
        if (evt == null || !evt.getValueIsAdjusting()) {
            int sel = imagesInFigureList.getSelectedIndex();
            if (sel == -1) {
                loading = true;
                ief.setVisible(false);
                ief.setCurSel(null);
                cur_sel_image2 = null;
                cropLeftSpinner.setValue(0);
                cropRightSpinner.setValue(0);
                cropUpSpinner.setValue(0);
                cropDownSpinner.setValue(0);
                rotateSpinner.setValue(0);
                loading = false;
                return;
            }
            cur_sel_image2 = imagesInFigureModel.get(sel);
            ief.setCurSel(cur_sel_image2);
            updatePositionFigure(cur_sel_image2);
            loadCurSelParameters(cur_sel_image2);
            if (evt != null && cur_sel_image2 != null) {
                showAppropriateOptions(imagesInFigureList);
            }
            doubleLayerPane2.ROIS.setSelectedShape(cur_sel_image2);
        }
    }//GEN-LAST:event_imagesInFigureChanged

    /**
     * updates rotation of selected images
     *
     * @param evt
     */
    private void angleChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_angleChanged
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        if (loading || dontChangeAngle) {
            return;
        }
        if (jTabbedPane1.getSelectedIndex() == 1) {
            final Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel != null && curPanel != null) {
                if (cur_sel instanceof ComplexShapeLight) {
                    HashSet<Object> selected_images = ((ComplexShapeLight) cur_sel).getGroup();
                    MultiThreadExecuter mt = new MultiThreadExecuter();
                    mt.genericCallableWithSavingOutput(selected_images, "rotate", getRotation());
                } else {
                    int angle = (((Integer) rotateSpinner.getValue()).intValue());
                    ((MyImage2D) cur_sel).rotate(angle);
                }
                /*
                 * bug fix for images of different sizes
                 */
                updateTable(cur_sel);
                createBackup();
            } else {
                if (jTabbedPane1.getSelectedIndex() == 1) {
                    blinkAnything(tableContentList.getParent());
                    if (isThereAnythingInTheLists()) {
                        CommonClassesLight.Warning(this, "Please Select At Least One Image In The 'Panel Content' List");
                    }
                }
                dontChangeAngle = true;
                rotateSpinner.setValue(0);
                dontChangeAngle = false;
            }
        } else if (jTabbedPane1.getSelectedIndex() == 2) {
            final Object cur_sel;
            if (jTabbedPane1.getSelectedIndex() == 1) {
                cur_sel = cur_sel_image1;
            } else {
                cur_sel = cur_sel_image2;
            }
            if (cur_sel != null && current_row != null) {
                /*
                 * on croppe les images et on repacke le tout
                 */
                if (cur_sel instanceof ComplexShapeLight) {
                    HashSet<Object> selected_images = ((ComplexShapeLight) cur_sel).getGroup();
                    MultiThreadExecuter mt = new MultiThreadExecuter();
                    mt.genericCallableWithSavingOutput(selected_images, "rotate", getRotation());
                } else {
                    int angle = getRotation();
                    ((MyImage2D) cur_sel).rotate(angle);
                }
                updateFigure(cur_sel);
                doubleLayerPane2.ROIS.setSelectedShape(cur_sel);
                createBackup();
            } else {
                if (jTabbedPane1.getSelectedIndex() == 2) {
                    blinkAnything(imagesInFigureList.getParent());
                    if (isThereAnythingInTheLists()) {
                        CommonClassesLight.Warning(this, "Please Select A Single Image From The 'Panel Content' List");
                    }
                }
                dontChangeAngle = true;
                rotateSpinner.setValue(0);
                dontChangeAngle = false;
            }
        }
    }//GEN-LAST:event_angleChanged

    public void updateBlockSize() {
        if (curPanel != null) {
            /*
             * we onlu update block size if necessary
             */
            if ((int) curPanel.getWidth() != curPanel.width_in_px) {
                curPanel.setToWidth();
            }
        }
    }

    /**
     * this function is called everytime a new montage is selected
     */
    private void montageChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_montageChanged
        if (evt == null || !evt.getValueIsAdjusting()) {
            tableContentListModel.clear();
            showAppropriateOptions(tableList);
            JList tmp;
            if (evt != null) {
                tmp = (JList) evt.getSource();
            } else {
                tmp = tableList;
            }
            cur_sel_image1 = null;
            ief.setCurSel(null);
            curPanel = null;
            /*
             * necessary to do that to avoid crop and rotation pbs
             */
            doubleLayerPane1.ROIS.clearROIs();
            doubleLayerPane1.ROIS.resetSelection();
            int pos = tmp.getSelectedIndex();
            if (pos != -1) {
                int real_pos = getRealPos(pos);
                curPanel = panels.get(real_pos);
                //modification for review
                //ca a l'air de fixer le bug de position
                curPanel.setFirstCorner();
                loadCurPanelParameters();
                setCurrentDisplay(new ArrayList<Object>(curPanel.getGroup()));
                ArrayList<Object> objs = new ArrayList<Object>(curPanel.getGroup());
                tableContentListModel.clear();
                inactivateListUpdate = true;
                for (Object object : objs) {
                    tableContentListModel.addElement(object);
                }
                inactivateListUpdate = false;
            } else {
                curPanel = ((Montage) RowContentList.getSelectedValue());
            }
            //modification for review
            /*
             * we lock the size if the panel is part of a figure
             */
            if (curPanel != null) {
                //modification for review
                /**
                 * we center the scrollpane over the selected image panel
                 */
                updatePositionInPanel(curPanel);
            }
            if (tableListModel.isEmpty()) {
                updateTable();
            }
            updateGlassPane();
        }
    }//GEN-LAST:event_montageChanged

    private void testMemory(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testMemory
        try {
            System.gc();
        } catch (Exception e) {
        }
        Runtime runtime = Runtime.getRuntime();
        long allocatedMemory = runtime.totalMemory();
        long max = Math.max(runtime.totalMemory(), runtime.maxMemory());
        long freeMemory = runtime.freeMemory();
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(100);
        int used_memory_in_percent = (int) (((double) (allocatedMemory - freeMemory) / (double) (max)) * 100.);
        jProgressBar1.setValue(used_memory_in_percent);
        jProgressBar1.setStringPainted(true);
        jProgressBar1.setString("Mem used: " + used_memory_in_percent + "%");
    }//GEN-LAST:event_testMemory

    /**
     * the user wants to quit we chack that evrything is saved or we prompt a
     * dialog
     *
     * @param evt
     */
    private void closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closing
        if (mustWarnOnQuit && warnForSave && !(tableListModel.isEmpty() && figureListModel.isEmpty())) {
            blinkAnything(jButton7);
            JLabel jl = new JLabel("<html><font color=\"#FF0000\">ScientiFig thinks you are about to quit without saving, do you want to continue ?<BR><BR>-Click 'Ok' to quit without saving<br>-Or click 'Cancel' to abort<BR><BR>-NB: you can inactivate this warning in Edit>Preferences</font></html>");
            int result = JOptionPane.showOptionDialog(this, new Object[]{jl}, "Warning...", JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result != JOptionPane.OK_OPTION) {
                /*
                 * we stop the blinking
                 */
                blinker.stop();
                glasspane.setBlink(null);
                glasspane.repaint();
                return;
            }
        }

        /**
         * need to remove the window here to avoid FIJI pbs (FIJI not closing)
         */
        WindowManager.removeWindow(this);
        if (!FiguR_.isInstanceAlreadyExisting()) {
            try {
                CommonClassesLight.r.close();
                CommonClassesLight.r = null;
            } catch (Exception e) {
            }
        }
        this.dispose();

    }//GEN-LAST:event_closing

    /**
     * this function is called every time a row is selected
     *
     * @param evt
     */
    private void RowContentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_RowContentListValueChanged
        if (loading) {
            return;
        }
        showAppropriateOptions(RowContentList);
        imagesInFigureModel.clear();
        cur_sel_image2 = null;
        ief.setCurSel(null);
        if (RowContentList.getSelectedIndex() != -1) {
            curPanel = (Montage) RowContentList.getSelectedValue();
            /*
             * we update the selection
             */
            if (curPanel != null) {
                loading = true;
                ArrayList<Object> images = new ArrayList<Object>(curPanel.pos_n_shapes);
                for (Object object : images) {
                    imagesInFigureModel.addElement(object);
                }
                loading = false;
            }
            doubleLayerPane2.ROIS.setSelectedShape(curPanel);
            updatePositionFigure(curPanel);
        } else {
            curPanel = null;
        }
    }//GEN-LAST:event_RowContentListValueChanged

    private void helpVideo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpVideo
        buttonHelp.doClick();
    }//GEN-LAST:event_helpVideo

    private void listClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listClicked
        updateGlassPane();
        showAppropriateOptions(evt.getSource());
    }//GEN-LAST:event_listClicked

    private void changeSpaceBetweenRowsspaceBetweenImagesChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_changeSpaceBetweenRowsspaceBetweenImagesChanged
        if (evt != null) {
            runAllnow(evt.getSource());
        }
        if (loading) {
            return;
        }
        changeRowSpacing();
    }//GEN-LAST:event_changeSpaceBetweenRowsspaceBetweenImagesChanged

    private void tabSelChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabSelChanged
        int sel = jTabbedPane1.getSelectedIndex();
        switch (sel) {
            case 0:
                jMenu4.setText("Panels");
                exportToIJ.setEnabled(false);
                exportToIJMacro.setEnabled(false);
                jMenu3.setEnabled(false);
                break;
            case 1:
                jMenu4.setText("Panels");
                exportToIJ.setEnabled(true);
                exportToIJMacro.setEnabled(true);
                jMenu3.setEnabled(true);
                break;
            case 2:
                jMenu4.setText("Figure");
                exportToIJ.setEnabled(true);
                exportToIJMacro.setEnabled(true);
                jMenu3.setEnabled(true);
                break;
        }
        //bug fix for size refresh pbs
        loadCurPanelParameters();
    }//GEN-LAST:event_tabSelChanged

    /**
     * if the window is resized we update the glasspane drawings
     *
     * @param evt
     */
    private void windowSizeChanged(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_windowSizeChanged
        updateGlassPane();
    }//GEN-LAST:event_windowSizeChanged

    /**
     *
     * @param listModel
     * @return the content of the list
     */
    public ArrayList<String> getListContent(DefaultListModel listModel) {
        ArrayList<String> list2 = new ArrayList<String>();//pb of list model is that
        for (int i = 0; i < listModel.size(); i++) {
            list2.add(listModel.get(i).toString());
        }
        return list2;
    }

    /**
     *
     * @param first_letter the first letter (this letter will be automatically
     * incremented)
     * @param shapes series of vectorial objects that should be attributed a
     * letter
     * @return the last attributed letter
     */
    public String incrementLetter(String first_letter, ArrayList<Object> shapes) {
        if (first_letter != null && first_letter.trim().length() == 1) {
            char c = first_letter.charAt(0);
            for (Object object : shapes) {
                if (object instanceof MyImage2D.Double) {
                    c++;
                }
            }
            return c + "";
        }
        return "A";
    }

    /**
     * Creates a montage out of a series of vectorial objects
     *
     * @param shapes list of vectorial objects
     * @param nb_rows number of nbRows of the montage
     * @param nb_cols number of columns of the montage
     * @param meander if true images will be placed in meander order
     * @param letter the first letter of the montage
     * @param space_between_images the space between images of the montage
     */
    public void addMontage(ArrayList<Object> shapes, int nb_rows, int nb_cols, boolean meander, String letter, int space_between_images) {
        Montage panel = new Montage(shapes, nb_rows, nb_cols, meander, letter, space_between_images);
        panel.setResolution(resolution);
        panel.setWidth_in_px(getSizeInPx());
        panel.setWidth_in_cm(getSizeInCm());
        panel.setJournalStyle(defaultStyle, false, false, false, false, true, false, false, false);
        panel.setToWidth(getSizeInPx());
        /*
         * bug fix for images having different sizes
         */
        panel.setFirstCorner();
        panels.put(PanelCounter, panel);
        setCurrentDisplay(shapes);
        tableListModel.addElement("" + PanelCounter);
        tableList.setSelectedIndex(tableListModel.getSize() - 1);
        PanelCounter++;
        this.letter = incrementLetter(letter, shapes);
        if (remove_when_added) {
            myList1.removeSelection();
        }
    }

    /*
     * implementation of the ImageJ plugin 'run' function
     * this function can create a new instance of the software
     * this function can interprete macros
     */
    @Override
    public void run(String arg) {

        final String args = arg;

        /*
         * on fait le menage
         */
        try {
            System.gc();
        } catch (Exception e) {
        }
        alreadyExists = ScientiFig_.isInstanceAlreadyExisting();
        if (Macro.getOptions() == null) {
            /*
             * Create and display the form
             */
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ScientiFig_ fa = ScientiFig_.getInstance();
                    CommonClassesLight.GUI = fa;
                    /*
                     * bug fix for buttons showing after restart in FIJI
                     */
                    allowUndoRedo();
                    /*
                     * We reload important parameters
                     */
                    panels = fa.panels;
                    tableList = fa.tableList;
                    tableListModel = fa.tableListModel;
                    PanelCounter = fa.PanelCounter;
                    figureListModel = fa.figureListModel;
                    figureList = fa.figureList;
                    journalCombo = fa.journalCombo;
                    myList1 = fa.myList1;
                    updateTitle(lastSaveName);
                    imported_from_pixel_size = fa.imported_from_pixel_size;
                    Rstatus = fa.Rstatus;
                    checkStatus();
                    fa.setVisible(true);
                    Dimension screen = getScreenSize();
                    Dimension size = fa.getSize();
                    if (!alreadyExists) {
                        fa.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
                    }

                    /*
                     * here we interprete IJ/FIJI command line args
                     */
                    parseIJCommands(fa, args);
                }
            });
        } else {
            if (Macro.getOptions().contains("code")) {
                macro = Macro.getValue(Macro.getOptions(), "code", null);
            }
            if (Macro.getOptions().contains("save")) {
                save = Macro.getValue(Macro.getOptions(), "save", null);
                save = CommonClassesLight.change_path_separators_to_system_ones(save);
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ScientiFig_ fa = ScientiFig_.getInstance();
                    CommonClassesLight.GUI = fa;
                    /*
                     * We reload important parameters
                     */
                    panels = fa.panels;
                    tableList = fa.tableList;
                    tableListModel = fa.tableListModel;
                    PanelCounter = fa.PanelCounter;
                    figureListModel = fa.figureListModel;
                    figureList = fa.figureList;
                    journalCombo = fa.journalCombo;
                    myList1 = fa.myList1;
                    updateTitle(lastSaveName);
                    imported_from_pixel_size = fa.imported_from_pixel_size;
                    Rstatus = fa.Rstatus;
                    checkStatus();
                    fa.setVisible(true);
                    Dimension screen = getScreenSize();
                    Dimension size = fa.getSize();

                    if (!alreadyExists) {
                        fa.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
                    }
                    /*
                     * here we interprete IJ/FIJI command line args
                     */
                    parseIJCommands(fa, args);
                    if (macro != null) {
                        if (macro.toLowerCase().contains("/figure")) {
                            Figure rh = new Figure(macro);
                            ArrayList<Object> macro_rows = rh.getRows();
                            rows.addAll(macro_rows);
                            for (Object object : macro_rows) {
                                Row r = (Row) object;
                                ArrayList<Object> montages = r.blocks;
                                String formula = "";
                                boolean first = true;
                                for (Object object1 : montages) {
                                    panels.put(PanelCounter, (Montage) object1);
                                    if (first) {
                                        formula += "" + PanelCounter;
                                        first = false;
                                    } else {
                                        formula += "+" + PanelCounter;
                                    }
                                    PanelCounter++;
                                    fa.PanelCounter++;
                                }
                                r.setFormula(formula);
                                figureListModel.addElement("" + r.getFormula());
                                figureList.revalidate();
                            }
                            updateFigure();
                        } else {
                            Montage m = new Montage(macro);
                            if (m.isEmpty()) {
                                System.err.println("no valid image found --> panel not created");
                                return;
                            }
                            panels.put(PanelCounter, m);
                            tableListModel.addElement("" + PanelCounter);
                            tableList.revalidate();
                            tableList.setSelectedIndex(tableListModel.getSize() - 1);
                            PanelCounter++;
                            fa.PanelCounter++;
                        }
                    }
                    if (save != null) {
                        if (save.toLowerCase().endsWith(".yf5m")) {
                            String selectedStyle = null;
                            if (journalCombo.getSelectedIndex() != -1) {
                                selectedStyle = journalCombo.getSelectedItem().toString();
                            }
                            final String style = selectedStyle;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Exportable ex = new Exportable();
                                        ex.save(panels, rows, myList1.getFullList(), style, imported_from_J, imported_from_pixel_size, true, save);
                                    } catch (Exception e) {
                                        StringWriter sw = new StringWriter();
                                        e.printStackTrace(new PrintWriter(sw));
                                        String stacktrace = sw.toString();
                                        System.err.println(stacktrace);
                                    } catch (OutOfMemoryError E) {
                                        StringWriter sw = new StringWriter();
                                        E.printStackTrace(new PrintWriter(sw));
                                        String stacktrace = sw.toString();
                                        System.err.println(stacktrace);
                                        try {
                                            System.gc();
                                            CommonClassesLight.Warning((Component) CommonClassesLight.GUI, "Sorry, not enough memory!\nPlease restart the software with more memory.");
                                        } catch (Exception e) {
                                        }
                                    }
                                    warnForSave = false;
                                }
                            }).start();
                            lastSaveName = save;
                            updateTitle(lastSaveName);
                        } else if (save.toLowerCase().endsWith(".svg")) {
                            export(FORMAT_SVG, save);
                        } else if (save.toLowerCase().endsWith(".tif") || save.toLowerCase().endsWith(".tiff")) {
                            export(FORMAT_TIFF, save);
                        } else if (save.toLowerCase().endsWith(".jpeg") || save.toLowerCase().endsWith(".jpg")) {
                            export(FORMAT_JPEG, save);
                        } else if (save.toLowerCase().endsWith(".png")) {
                            export(FORMAT_PNG, save);
                        } else if (save.toLowerCase().equals("ij/fiji")) {
                            export(FORMAT_IJ, null);
                        } else {
                            CommonClassesLight.Warning("Unknown format \"" + save + "\"");
                        }
                    }
                }
            });
        }
    }

    private void parseIJCommands(ScientiFig_ fa, String args) {
        /*
         * here we interprete IJ/FIJI command line args
         */
        if (args != null) {
            if (args.startsWith("save")) {
                if (args.toLowerCase().contains("svg")) {
                    fa.export(FORMAT_SVG, null);
                }
            }
            if (args.toLowerCase().contains("import")) {
                fa.runAllnow("import");
            }
            if (args.toLowerCase().contains("options")) {
                fa.runAllnow("options");
            }
        }
    }

    /**
     * Creates macro code for the corresponding montages/figures
     */
    public void recordMacro() {
        String code = "code=[";
        if (jTabbedPane1.getSelectedIndex() == 1) {
            if (curPanel != null) {
                code += curPanel.produceMacroCode(0);
            } else {
                blinkAnything(tableList.getParent());
                CommonClassesLight.Warning(this, "Please Select At Least One Panel In The 'Panels' List");
                return;
            }
        } else if (jTabbedPane1.getSelectedIndex() == 2) {
            if (rows.isEmpty()) {
                CommonClassesLight.Warning(this, "Please Create A Figure First");
                return;
            } else {
                Figure rh = new Figure(rows, getSpaceBetweenRows());
                code += rh.produceMacroCode();
            }
        }
        code += "]";
        if (!Recorder.record) {
            ij.IJ.run("Record...");
            CommonClassesLight.openImageJ();
        }
        /*
         * dirty bug fix for old imageJ versions
         */
        initScientifigMacroName();
        String[] split_code = code.replace("\"", "\\\"").split("\n");
        split_code[0] = "run(\"" + ScientiFigMacroName + "\", \"" + split_code[0];
        split_code[split_code.length - 1] += "\");\n";
        for (int i = 0; i < split_code.length; i++) {
            String string = split_code[i];
            String tab = "";
            for (int j = 0; j < string.length(); j++) {
                char c = string.charAt(j);
                if (c == '\t') {
                    tab += "\t";
                }
            }
            if (tab.equals("")) {
                tab += "\t";
            }
            if (i > 0 && i < split_code.length - 1) {
                Recorder.recordString("+" + tab + "\"" + string.replace("\t", "") + "\"" + "\n");
            } else if (i == 0) {
                Recorder.recordString(string.replace("\t", "") + "\"" + "\n");
            } else if (i == split_code.length - 1) {
                Recorder.recordString("+\"" + string.replace("\t", "") + "\n");
            }
        }
    }

    /**
     *
     * @return the screen size (used to center the GUI on the screen)
     */
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * this function applies a look n feel to the GUI
     */
    public static void setLNF() {
        String operating_system = System.getProperty("os.name");
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        ImageEditorFrame.setDefaultLookAndFeelDecorated(true);
        try {
            boolean found = false;
            if (!found) {
                if (!operating_system.contains("inu")) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } else {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to load L&F:");
            System.err.println(ex);
        }
    }

    /**
     *
     * @return the current instance of scientifig if one exists otherwise
     * creates a new instance of scientifig
     */
    public static ScientiFig_ getInstance() {
        ScientiFig_ ls = isInstanceAlreadyExisting() ? getPreviousInstance() : new ScientiFig_();
        return ls;
    }

    private void recordSave(String name) {
        if (Recorder.record) {
            String pal = "save=[";
            pal += CommonClassesLight.change_path_separators_to_system_ones(name);
            pal += "]";
            initScientifigMacroName();
            Recorder.recordString("run(\"" + ScientiFigMacroName + "\", \"" + pal + "\");");
        }
    }

    private void initScientifigMacroName() {
        if (ScientiFigMacroName == null) {
            try {
                ScientiFigMacroName = "ScientiFig";
                ij.IJ.run(ScientiFigMacroName);
            } catch (Exception e) {
                ScientiFigMacroName = "ScientiFig ";
            }
        }
    }

    private void loadCurSelParameters(Object cur_sel) {
        if (cur_sel != null) {
            if (cur_sel instanceof MyImage2D) {
                loading = true;
                MyImage2D tmpImg = (MyImage2D) cur_sel;
                int min_left = tmpImg.getLeft_crop();
                int min_right = tmpImg.getRight_crop();
                int min_up = tmpImg.getUp_crop();
                int min_down = tmpImg.getDown_crop();
                int min_angle = (int) tmpImg.getTheta();
                cropLeftSpinner.setValue((int) min_left);
                cropRightSpinner.setValue((int) min_right);
                cropUpSpinner.setValue((int) min_up);
                cropDownSpinner.setValue((int) min_down);
                rotateSpinner.setValue((int) min_angle);
                loading = false;
            }
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        if (!CommonClassesLight.isMac()) {
            setLNF();
        }
        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ScientiFig_ fa = ScientiFig_.getInstance();
                Dimension screen = getScreenSize();
                Dimension size = fa.getSize();
                fa.setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
                fa.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel AR;
    public static javax.swing.JButton AddTextLeftOfRow;
    public static javax.swing.JButton AddTextRightOfRow;
    private javax.swing.JButton AnnotateImageROIs;
    private javax.swing.JButton AnnotateImageText;
    private javax.swing.JMenuItem ChangeStrokeSize;
    private javax.swing.JButton DeleteSelectedBlock;
    private javax.swing.JButton Flip;
    public javax.swing.JMenuItem New;
    private javax.swing.JMenuItem OpenYF5M;
    public static javax.swing.JButton PIP;
    public javax.swing.JMenuItem Quit;
    public javax.swing.JList RowContentList;
    private javax.swing.JButton Rstatus;
    public javax.swing.JMenuItem Save;
    private javax.swing.JMenuItem SaveAs;
    private javax.swing.JButton addEmptyImageToCurrentBlock;
    public static javax.swing.JButton addLettersOutside;
    private javax.swing.JButton addSelectedImagesInCurPanel;
    private javax.swing.JButton addSelectedPanelToNewRow;
    private javax.swing.JButton addSelectedPanelToSelectedRow;
    private javax.swing.JButton addSelectionToCurrentPanel;
    public static javax.swing.JButton addTextAboveRow;
    public static javax.swing.JButton addTextBelowRow;
    private javax.swing.JButton bestFitZoom;
    private javax.swing.JButton buttonHelp;
    private javax.swing.JMenuItem capitalizeFirstLetter;
    public static javax.swing.JSpinner changeSpaceBetweenImageInPanel;
    public static javax.swing.JSpinner changeSpaceBetweenRows;
    private javax.swing.JButton checkFont;
    private javax.swing.JButton checkGraph;
    private javax.swing.JButton checkLineArts;
    private javax.swing.JButton checkSize;
    private javax.swing.JButton checkStyle;
    private javax.swing.JButton checkText;
    private javax.swing.JMenuItem citations;
    public javax.swing.JButton createPanelAutomatically;
    private javax.swing.JButton createPanelFromSelection;
    private javax.swing.JSpinner cropDownSpinner;
    private javax.swing.JSpinner cropLeftSpinner;
    private javax.swing.JSpinner cropRightSpinner;
    private javax.swing.JSpinner cropUpSpinner;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteImagesFromTheList;
    private javax.swing.JMenuItem deleteJournalStyle;
    private javax.swing.JButton deleteSelectColumn;
    private javax.swing.JButton deleteSelectedImageFromCurrentBlock;
    private static Dialogs.DoubleLayerPane doubleLayerPane1;
    private static Dialogs.DoubleLayerPane doubleLayerPane2;
    private Dialogs.DoubleLayerPane doubleLayerPane3;
    private javax.swing.JMenuItem exportAsJpeg;
    private javax.swing.JMenuItem exportAsPNG;
    private javax.swing.JMenuItem exportAsSVG;
    private javax.swing.JMenuItem exportAsTiff;
    private javax.swing.JMenuItem exportToIJ;
    private javax.swing.JMenuItem exportToIJMacro;
    private javax.swing.JMenuItem extract_images;
    public javax.swing.JList figureList;
    public static javax.swing.JLabel imageHeight;
    private javax.swing.JPanel imageSizePanel;
    public static javax.swing.JLabel imageWidth;
    public javax.swing.JList imagesInFigureList;
    private javax.swing.JButton importFromIJ;
    public static javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    public javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private static javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private static javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel1666;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane8;
    public static javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JComboBox journalColumnsCombo;
    private javax.swing.JComboBox journalCombo;
    private javax.swing.JMenuItem launchFigur;
    public static javax.swing.JButton loadAllyf5m;
    private javax.swing.JButton moveColLeft;
    private javax.swing.JButton moveColRight;
    private javax.swing.JButton moveDownInList;
    private javax.swing.JButton moveImageInCurrentPanelLeft;
    private javax.swing.JButton moveImageInCurrentPanelRight;
    private javax.swing.JButton moveLeft;
    private javax.swing.JButton moveRight;
    private javax.swing.JButton moveRowLeft;
    private javax.swing.JButton moveRowRight;
    private javax.swing.JButton moveUpInList;
    public Dialogs.MyListLight myList1;
    private javax.swing.JMenuItem newJournalStyle;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JButton realSizeZoom;
    private static javax.swing.JButton redo;
    private javax.swing.JButton reformatTable;
    private javax.swing.JMenuItem removeAllAnnotations;
    private javax.swing.JMenuItem removeAllScaleBars;
    private javax.swing.JMenuItem removeAllText;
    private javax.swing.JButton removeAnnotations;
    public static javax.swing.JButton removePiP;
    private javax.swing.JButton removeSelectedRow;
    public static javax.swing.JButton replaceImage;
    private javax.swing.JButton rotateLeft;
    private javax.swing.JPanel rotateNFlip;
    private javax.swing.JButton rotateRight;
    private javax.swing.JSpinner rotateSpinner;
    private javax.swing.JMenuItem showShortcuts;
    private javax.swing.JSpinner sizeInCM;
    private javax.swing.JSpinner sizeInPx;
    private javax.swing.JButton splitColoredImagesToMagentaGreen;
    private javax.swing.JButton swapImagesFromCurrentPanel;
    public javax.swing.JList tableContentList;
    public javax.swing.JList tableList;
    private static javax.swing.JButton undo;
    private javax.swing.JButton updateLetters;
    private javax.swing.JButton zoomMinus;
    private javax.swing.JButton zoomPlus;
    // End of variables declaration//GEN-END:variables
}

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
package Debug;

import GUIs.ScientiFig_;
import MyShapes.ColoredTextPaneSerializable;
import MyShapes.LineStrokable;
import MyShapes.MyEllipse2D;
import MyShapes.MyImage2D;
import MyShapes.MyPoint2D;
import MyShapes.MyRectangle2D;
import MyShapes.Row;
import MyShapes.TopBar;
import Commons.CommonClassesLight;
import Commons.Point3D;
import MyShapes.TextBar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;

/**
 * WiseDebug allows to test every buttons as if it was being tested by a human (it
 * allows visual inspection of the result)
 *
 * @author Benoit Aigouy
 */
public class WiseDebug {

    ScientiFig_ SF;
    static HashMap<String, Object[]> textNCorrepondingInteractible = new HashMap<String, Object[]>();
    public static ArrayList<Component> interactibles = new ArrayList<Component>();
    public boolean blinkOnExecution = true;
    private Robot robot;

    public static void clear() {
        textNCorrepondingInteractible.clear();
    }

    public WiseDebug(ScientiFig_ SF) {
        this.SF = SF;
        SF.addAllPanelsToOptions();
        try {
            robot = new Robot();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            System.err.println("\nError:\n" + stacktrace);
        }
        SF.setVisible(true);
        getAllInteractibles();
    }

    public WiseDebug() {
        this(new ScientiFig_());
    }

    private void getAllInteractibles() {
        AutoDebug.SF = this.SF;
        AutoDebug.getAllInteractibleComponents(SF);
        AutoDebug.getAllInteractibleComponents(ScientiFig_.ief);
        interactibles = AutoDebug.interactibles;
        textNCorrepondingInteractible = getInteractibleText(interactibles);
    }

    public static HashMap<String, Object[]> getInteractibleText(ArrayList<Component> interactibles) {
        for (Component object : interactibles) {
            if (object instanceof JButton || object instanceof JMenuItem || object instanceof JMenu || object instanceof JComboBox) {
                String txt = AutoDebug.getText(object);
                if (txt != null) {
                    textNCorrepondingInteractible.put(txt, new Object[]{object});
                }
            }
            if (object instanceof JTabbedPane) {
                JTabbedPane tabbedPane = ((JTabbedPane) object);
                int count = tabbedPane.getTabCount();
                for (int i = 0; i < count; i++) {
                    //put the component and its index
                    textNCorrepondingInteractible.put(tabbedPane.getTitleAt(i), new Object[]{tabbedPane, tabbedPane.getTitleAt(i)});
                }
            }
        }
        return textNCorrepondingInteractible;
    }

    public void selectObjectsInAList(JList list, int... sel) {
        list.setSelectedIndices(sel);
        if (list instanceof Component) {
            SF.blinkAnything((Component) list.getParent());
        }
    }

    public void unselectList(JList list) {
        list.setSelectedIndex(-1);
    }

    public void pressButton(JButton button) {
        button.doClick();
    }

    public ArrayList<Object> getJSpinnerButtons(Object o) {
        if (o != null) {
            o = ((Object[]) o)[0];
        }
        ArrayList<Object> spinner_buttons = new ArrayList<Object>();
        if (o instanceof JSpinner) {
            JSpinner spin = ((JSpinner) o);
            /*
             * we now extract the buttons
             */
            Component[] contained = spin.getComponents();
            for (Component component : contained) {
                if (component instanceof JButton) {
                    spinner_buttons.add(component);
                }
            }
        }
        return spinner_buttons;
    }

    public void pressSpinnerUp(ArrayList<Object> buttonsOfJspinner, int nb_of_clicks) {
        if (!buttonsOfJspinner.isEmpty()) {
            for (int i = 0; i < nb_of_clicks; i++) {
                ((JButton) buttonsOfJspinner.get(0)).doClick();
            }
            pause();
        }
    }

    public void pressSpinnerDown(ArrayList<Object> buttonsOfJspinner, int nb_of_clicks) {
        if (buttonsOfJspinner.size() == 2) {
            for (int i = 0; i < nb_of_clicks; i++) {
                ((JButton) buttonsOfJspinner.get(1)).doClick();
            }
            pause();
        }
    }

    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }

    public void pause() {
        pause(1000);
    }

    public void smallPause() {
        pause(500);
    }

    public void loadFile(String fileName) {
        ArrayList<String> yf5ms2load = new ArrayList<String>();
        yf5ms2load.add(fileName);
        ScientiFig_.yf5m_files = yf5ms2load;
        ScientiFig_.loadAllyf5m.doClick();
    }

    public void loadImagesToTheList(ArrayList<String> images) {
        SF.myList1.addAll(images);
    }

    public Object findByText(String buttonText) {
        return textNCorrepondingInteractible.get(buttonText);
    }

    public boolean click(Object clickable) {
        if (clickable instanceof JButton) {
            if (clickable instanceof Component) {
                SF.blinkAnything((Component) clickable);
            }
            ((JButton) clickable).doClick();
            return true;
        }
        if (clickable instanceof JMenuItem) {
            if (clickable instanceof Component) {
                SF.blinkAnything((Component) clickable);
            }
            ((JMenuItem) clickable).doClick();
            return true;
        }
        if (clickable instanceof JMenu) {
            if (clickable instanceof Component) {
                SF.blinkAnything((Component) clickable);
            }
            ((JMenu) clickable).doClick();
            return true;
        }
        System.err.println("button not found " + clickable);
        return false;
    }

    public void findAndClickButton(String txt) {
        Object button = findByText(txt);
        if (button != null) {
            button = ((Object[]) button)[0];
        }
        if (button instanceof JButton || button instanceof JMenu || button instanceof JMenuItem) {
            if (blinkOnExecution) {
                if (button instanceof Component) {
                    SF.blinkAnything((Component) button);
                }
            }
            if (!click(button)) {
                System.out.println("error -->" + txt);
            }
            pause(1500);
        }
    }

    public void findAndSelectTab(String txt) {
        Object button = findByText(txt);
        if (button != null) {
            String tab_tite = (String) ((Object[]) button)[1];
            JTabbedPane tab = (JTabbedPane) ((Object[]) button)[0];
            int count = tab.getTabCount();
            for (int i = 0; i < count; i++) {
                String title = tab.getTitleAt(i);
                if (title.equals(tab_tite)) {
                    tab.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    public void clearGUI() {
        SF.New.doClick();
    }

    private void simulateTab() {
        if (robot != null) {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }
    }

    private void simulateSupr() {
        if (robot != null) {
            robot.keyPress(KeyEvent.VK_DELETE);
            robot.keyRelease(KeyEvent.VK_DELETE);
        }
    }

    private void simulateNumber(int nb) {
        robot.keyPress(KeyEvent.VK_SHIFT);
        if (robot != null) {
            switch (nb) {
                case 0:
                    robot.keyPress(KeyEvent.VK_0);
                    robot.keyRelease(KeyEvent.VK_0);
                    break;
                case 1:
                    robot.keyPress(KeyEvent.VK_1);
                    robot.keyRelease(KeyEvent.VK_1);
                    break;
                case 2:
                    robot.keyPress(KeyEvent.VK_2);
                    robot.keyRelease(KeyEvent.VK_2);
                    break;
                case 3:
                    robot.keyPress(KeyEvent.VK_3);
                    robot.keyRelease(KeyEvent.VK_3);
                    break;
                case 4:
                    robot.keyPress(KeyEvent.VK_4);
                    robot.keyRelease(KeyEvent.VK_4);
                    break;
                case 5:
                    robot.keyPress(KeyEvent.VK_5);
                    robot.keyRelease(KeyEvent.VK_5);
                    break;
                case 6:
                    robot.keyPress(KeyEvent.VK_6);
                    robot.keyRelease(KeyEvent.VK_6);
                    break;
                case 7:
                    robot.keyPress(KeyEvent.VK_7);
                    robot.keyRelease(KeyEvent.VK_7);
                    break;
                case 8:
                    robot.keyPress(KeyEvent.VK_8);
                    robot.keyRelease(KeyEvent.VK_8);
                    break;
                case 9:
                    robot.keyPress(KeyEvent.VK_9);
                    robot.keyRelease(KeyEvent.VK_9);
                    break;
            }

        }
        robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    public void simulateEnter() {
        if (robot != null) {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
    }

    public void simulateAnyKeyInSomeMs(int ms, final String key, final int option) {
        final Timer t;
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (key.equals("Enter")) {
                    simulateEnter();
                }
                if (key.equals("Tab")) {
                    simulateTab();
                }
                if (key.equals("Supr")) {
                    simulateSupr();
                }
                if (key.equals("Number")) {
                    simulateNumber(option);
                }
                /*
                 * stop at first run
                 */
                t.cancel();
            }
        }, ms, 500);
    }

    public void simulateSpinnerEvent(Object o, int value) {
        if (o != null) {
            o = ((Object[]) o)[0];
        }
        if (o instanceof JSpinner) {
            JSpinner spinner = (JSpinner) o;
            if (spinner instanceof Component) {
                SF.blinkAnything((Component) spinner);
            }
            spinner.setValue(value);
            try {
                spinner.commitEdit();
            } catch (ParseException ex) {
            }
        } else {
            System.err.println("not a spinner " + o);
        }
    }

    public void selectAComboByValue(Object o, String text_to_select) {
        if (o != null) {
            o = ((Object[]) o)[0];
        }
        if (o instanceof JComboBox) {
            JComboBox combo = ((JComboBox) o);
            if (combo instanceof Component) {
                SF.blinkAnything((Component) combo);
            }
            combo.setSelectedIndex(-1);
            combo.setSelectedItem(text_to_select);
            if (combo.getSelectedIndex() == -1) {
                System.err.println(text_to_select + " could not be selected in the combo");
            }
        } else {
            System.err.println("not a combo " + o);
        }
    }

    public void swap2ImagesInAPanel(int pos1, int pos2) {
        selectObjectsInAList(SF.tableContentList, pos1, pos2);
        findAndClickButton("Best Fit");
        findAndClickButton("Swap 2 Images");
    }

    private void delteImageInAPanel(int... pos) {
        SF.showAppropriateOptions(SF.tableContentList);
        selectObjectsInAList(SF.tableContentList, pos);
        findAndClickButton("Delete Selected Image From Panel (send them back to the image List)");
    }

    private void rotateImage(int angleInDegrees) {
        simulateSpinnerEvent(findByText("Rotate images"), angleInDegrees);
        pause(800);
    }

    public void cropTop(int cropSize) {
        simulateSpinnerEvent(findByText("Crop images from the top"), cropSize);
        pause(800);
    }

    public void cropLeft(int cropSize) {
        simulateSpinnerEvent(findByText("Crop images from the left"), cropSize);
        pause(800);
    }

    public void cropRight(int cropSize) {
        simulateSpinnerEvent(findByText("Crop images from the right"), cropSize);
        pause(800);
    }

    public void cropDown(int cropSize) {
        simulateSpinnerEvent(findByText("Crop images from the bottom"), cropSize);
        pause(800);
    }

    public void reformatTable(int rows, int cols) {
        simulateAnyKeyInSomeMs(3200, "Enter", -1);
        simulateAnyKeyInSomeMs(500, "Tab", -1);
        simulateAnyKeyInSomeMs(800, "Tab", -1);
        simulateAnyKeyInSomeMs(1000, "Supr", -1);
        simulateAnyKeyInSomeMs(1300, "Number", cols);
        simulateAnyKeyInSomeMs(1600, "Enter", -1);
        simulateAnyKeyInSomeMs(1900, "Tab", -1);
        simulateAnyKeyInSomeMs(2300, "Supr", -1);
        simulateAnyKeyInSomeMs(2600, "Number", rows);
        /*
         * we reselect the enter button
         */
        simulateAnyKeyInSomeMs(2800, "Tab", -1);
        simulateAnyKeyInSomeMs(2900, "Tab", -1);
        simulateAnyKeyInSomeMs(3000, "Tab", -1);
        findAndClickButton("<html>Reformat<br>Table</html>");
    }

    public void changeSpaceBetwenImagesInAPanel(int space) {
        simulateSpinnerEvent(findByText("Empty space between images of the same panel"), space);
        pause();
    }

    public void changeSpaceBetwenPanelsInAFigure(int space) {
        simulateSpinnerEvent(findByText("Empty space between rows and columns within a row"), space);
        pause();
    }

    public void selectAJournalStyle(String name) {
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        selectAComboByValue(findByText("Select a journal style"), name);
        pause(2000);
    }

    public void changeJounralColSize(String nbCols) {
        selectAComboByValue(findByText("Width of the panel/row in journal columns"), nbCols);
        pause(1500);
    }

    public void bestFit() {
        findAndClickButton("Best Fit");
    }

    public void crawlList(JList list) {
        int size = list.getModel().getSize();
        for (int i = 0; i < size; i++) {
            selectObjectsInAList(list, i);
            pause(400);
        }
    }

    public void addPiP(int pos) {
        selectObjectsInAList(SF.tableContentList, pos);
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Add/Replace Inset(s)");
    }

    private void replaceImage(int pos) {
        selectObjectsInAList(SF.tableContentList, pos);
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Replace");
    }

    public void rotateLeft(int... pos) {
        selectObjectsInAList(SF.tableContentList, pos);
        findAndClickButton("Rotate Left");
    }

    public void rotateRight(int... pos) {
        selectObjectsInAList(SF.tableContentList, pos);
        findAndClickButton("Rotate Right");
    }

    public void flip(int... pos) {
        selectObjectsInAList(SF.tableContentList, pos);
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Flip");
    }

    public void fakeChangeImageText(int pos, String letter, String UL, String UR, String LL, String LR, String scale, int scaleBarSize, int strokeSizeScaleBar, int scaleBarColor) {
        selectObjectsInAList(SF.tableContentList, pos);
        if (ScientiFig_.cur_sel_image1 instanceof MyImage2D) {
            MyImage2D.Double tmp = ((MyImage2D.Double) ScientiFig_.cur_sel_image1);
            tmp.setLetter(letter);
            tmp.setUpper_left_text(UL);
            tmp.setUpper_right_text(UR);
            tmp.setLower_left_text(LL);
            tmp.setLower_right_text(LR);
            tmp.setScale_bar_size_in_px_of_the_real_image(scaleBarSize);
            tmp.setScale_bar_txt(scale);
            tmp.setSCALE_BAR_STROKE_SIZE(strokeSizeScaleBar);
            tmp.setScalebarColor(scaleBarColor);
            ScientiFig_.updateTable();
            selectObjectsInAList(SF.tableContentList, pos);
        }
        pause(1500);
    }

    public void fakeAnnotateAnImage(int pos, ArrayList<Object> annotations) {
        selectObjectsInAList(SF.tableContentList, pos);
        if (ScientiFig_.cur_sel_image1 instanceof MyImage2D) {
            MyImage2D.Double tmp = ((MyImage2D.Double) ScientiFig_.cur_sel_image1);
            tmp.setAssociatedObjects(annotations);
            ScientiFig_.updateTable();
        }
        pause(1500);
    }

    public ArrayList<Object> generateRandomShapes() {
        ArrayList<Object> Rois = new ArrayList<Object>();
        /*
         * rect
         */
        MyRectangle2D.Double rect = new MyRectangle2D.Double(120, 120, 256, 200);
        rect.setDrawColor(0xFF0000);
        rect.rotate(30);
        rect.setStrokeSize(3);
        rect.setLineStrokeType(LineStrokable.DASH_DOT);
        Rois.add(rect);
        /*
         * ellipse
         */
        MyEllipse2D.Double elli = new MyEllipse2D.Double(200, 250, 256, 180);
        elli.setDrawColor(0xFFFFFF);
        elli.rotate(90);
        elli.setLineStrokeType(LineStrokable.DASHED);
        Rois.add(elli);
        /*
         * text
         */
        MyPoint2D.Double text = new MyPoint2D.Double(256, 256);
        text.setText(new ColoredTextPaneSerializable("test Text", Color.ORANGE, new Font("Arial", Font.PLAIN, 12)));
        Rois.add(text);
        return Rois;
    }

    /*
     * there is a bug here because the function works and it doesn't seem to be the case here
     */
    public void fakeAddTextBarsToRow(int pos) {
        selectObjectsInAList(SF.figureList, pos);
        if (ScientiFig_.current_row instanceof Row) {
            Row row = (Row) ScientiFig_.current_row;
            if (true) {
                 HashMap<Point3D.Integer, ColoredTextPaneSerializable> posNText = new HashMap<Point3D.Integer, ColoredTextPaneSerializable>();
                ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable("pos 1:1 top", Color.blue, new Font("Arial", Font.PLAIN, 12));
                ctps.setFrame(true);
                ctps.setTextBgColor(Color.ORANGE);
                posNText.put(new  Point3D.Integer(1, 1,0), ctps);
                row.setTopTextBar(new TopBar(row, posNText, TopBar.HORIZONTAL));
            }
            if (true) {
           HashMap<Point3D.Integer, ColoredTextPaneSerializable> posNText = new HashMap<Point3D.Integer, ColoredTextPaneSerializable>();
                ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable("pos 1:1 bottom", Color.blue, new Font("Arial", Font.PLAIN, 12));
                ctps.setFrame(true);
                ctps.setTextBgColor(Color.ORANGE);
                ColoredTextPaneSerializable ctps2 = new ColoredTextPaneSerializable("pos 2:4 bottom", Color.black, new Font("Arial", Font.PLAIN, 12));
                ctps2.setTextBgColor(Color.black);
                posNText.put(new  Point3D.Integer(1, 1,0), ctps);
                posNText.put(new  Point3D.Integer(2, 4,0), ctps2);
                row.setBottomTextBar(new TopBar(row, posNText, TopBar.HORIZONTAL));
            }
            if (true) {
                 HashMap<Point3D.Integer, ColoredTextPaneSerializable> posNText = new HashMap<Point3D.Integer, ColoredTextPaneSerializable>();
                ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable("pos 1:1 left", Color.blue, new Font("Arial", Font.PLAIN, 12));
                ctps.setFrame(true);
                ctps.setTextBgColor(Color.ORANGE);
                posNText.put(new  Point3D.Integer(1, 1,0), ctps);
                row.setLeftTextBar(new TopBar(row, posNText, TopBar.LEFT));
            }
            if (true) {
                  HashMap<Point3D.Integer, ColoredTextPaneSerializable> posNText = new HashMap<Point3D.Integer, ColoredTextPaneSerializable>();
                ColoredTextPaneSerializable ctps2 = new ColoredTextPaneSerializable("pos 2:4 right", Color.black, new Font("Arial", Font.PLAIN, 12));
                ColoredTextPaneSerializable ctps = new ColoredTextPaneSerializable("pos 1:1 right", Color.blue, new Font("Arial", Font.PLAIN, 12));
                ctps.setFrame(true);
                ctps.setTextBgColor(Color.ORANGE);
                ctps2.setTextBgColor(Color.black);
                posNText.put(new Point3D.Integer(1, 1,0), ctps);
                posNText.put(new Point3D.Integer(2, 4,0), ctps2);
                row.setRightTextBar(new TopBar(row, posNText, TopBar.RIGHT));
            }
            row.arrangeRow();
            SF.reforceSameHeight(row);
            ScientiFig_.updateFigure();
            pause(2500);
        }
    }

    /**
     * this run takes about 3-4 minutes and should execute most if not all
     * functionalities of the soft --> enough to see if there is any defect
     * (human visual inspection)
     */
    public void createAMontageManually() {
        /**
         * test of Panel functionalities
         */
        /**
         * you can put any .yf5m file here with at least 15 images in the 'image list' and it should work
         */
        String fileName = "C:/Users/benoit/Desktop/test_create_a_montage.yf5m";
        if (!new File(fileName).exists()) {
            fileName = CommonClassesLight.getFolder(this.getClass(), "testing_samples_scientifig_various_versions/debug_sample/").replace("/dist", "") + new File(fileName).getName();
        }
        loadFile(fileName);

        pause(2000);

        selectObjectsInAList(SF.tableList, 0);

        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Delete Panel (send back Panel content to the image list)");

        crawlList(SF.myList1.list);

        /**
         * Dialog is modal so we have to press enter
         */
        selectObjectsInAList(SF.myList1.list, 0, 1, 2);
        simulateAnyKeyInSomeMs(1400, "Enter", -1);
        //au cas ou les image n'aient pas la meme taille ou le meme AR
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Add Selected Images To New Panel");

        selectObjectsInAList(SF.tableList, 0);
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Empty");
        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.tableContentList, 3);
        replaceImage(4);

        selectObjectsInAList(SF.myList1.list, 0);
        selectObjectsInAList(SF.tableList, 0);
        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Append Image To Current Panel");
        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.myList1.list, 0);
        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Append Image To Current Panel");

        findAndClickButton("Update Letters");

        fakeChangeImageText(1, "Letter", "upper left", "upper right", "lower left", "lower right", "60 µm", 120, 10, 0xFF00FF);

        /*
         * now we test the menus
         */
        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Export to ImageJ");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Select And Apply A Font or Text Color or Bg Color To All Components");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Select And Apply A Specific Line Width/Point Size To Graphs or a Stroke Size to ROI or the Stroke Size of SVG objects");

        /*
         simulateAnyKeyInSomeMs(1200, "Enter", -1);
         findAndClickButton("Apply The Selected Journal Style To All Components");
         */

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Apply Selected Width (in cm/px) To All Components");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Remove All Text");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Remove All Scale Bars");

        findAndClickButton("Update Letters");

        if (Math.random() <= 0.5) {
            reformatTable(3, 2);
        }

        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.tableContentList, 3);
        SF.showAppropriateOptions(SF.tableContentList);
        addPiP(1);

        fakeChangeImageText(1, "Letter", "upper left", "upper right", "lower left", "lower right", "60 µm", 120, 10, 0xFF00FF);
        fakeAnnotateAnImage(1, generateRandomShapes());

        simulateSpinnerEvent(findByText("Size of the panel/row in px"), 300);
        pause();

        changeSpaceBetwenImagesInAPanel(30);

        pressSpinnerUp(getJSpinnerButtons(findByText("Empty space between images of the same panel")), 15);
        pressSpinnerDown(getJSpinnerButtons(findByText("Empty space between images of the same panel")), 42);

        findAndClickButton("Update Letters");

        selectAJournalStyle("Nature Methods");
        selectAJournalStyle("PLOS ONE");

        changeJounralColSize("1 Col");
        changeJounralColSize("2 Cols");

        bestFit();

        SF.showAppropriateOptions(SF.tableContentList);
        selectObjectsInAList(SF.tableContentList, 1);
        pause();
        findAndClickButton("Change image order (swap with next image)");
        findAndClickButton("Change image order (swap with previous image)");
        swap2ImagesInAPanel(1, 3);
        selectObjectsInAList(SF.tableContentList, 3);

        bestFit();

        pause();
        cropDown(128);
        pressSpinnerUp(getJSpinnerButtons(findByText("Empty space between images of the same panel")), 15);
        pressSpinnerDown(getJSpinnerButtons(findByText("Empty space between images of the same panel")), 15);

        cropLeft(128);
        cropRight(128);
        cropTop(128);
        rotateImage(45);
        cropDown(0);
        cropLeft(0);
        cropRight(0);
        cropTop(0);
        rotateImage(90);
        pause();
        swap2ImagesInAPanel(1, 3);
        selectObjectsInAList(SF.tableContentList, 2);
        pause();
        simulateAnyKeyInSomeMs(900, "Enter", -1);
        findAndClickButton("Make The Selected Images Color Blind Friendly/Split Channels");

        if (Math.random() <= 0.5) {
            reformatTable(4, 3);
        } else {
            reformatTable(3, 4);
        }

        /*
         * delete all images created by the split
         */

        delteImageInAPanel(3);
        delteImageInAPanel(3, 4, 5, 6, 7);

        SF.showAppropriateOptions(SF.tableList);
        if (Math.random() <= 0.5) {
            reformatTable(2, 3);
        } else {
            reformatTable(3, 2);
        }

        /*
         * test replace image in a Panel
         */
        SF.showAppropriateOptions(SF.tableContentList);
        replaceImage(1);

        SF.showAppropriateOptions(SF.tableContentList);
        rotateLeft(1, 2);
        rotateLeft(3);
        rotateRight(1, 2);
        rotateRight(3);
        flip(1, 2, 3);
        flip(1);
        flip(2, 3);

        findAndClickButton("Auto");
        crawlList(SF.tableList);

        selectObjectsInAList(SF.tableList, 0);
        /*
         * from now on we are going to test all the Figure functionalities
         */
        SF.showAppropriateOptions(SF.figureList);

        /*
         * on teste move row left, right et various text bars et update letters + add row + add col + space
         */
        findAndClickButton("Add Selected Block As A New Row");
        selectObjectsInAList(SF.tableList, 0);
        findAndClickButton("Add Selected Block As A New Row");
        /*
         * we add two rows to the first Panel
         */
        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Add Panel To Current Row");
        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Add Panel To Current Row");

        fakeAddTextBarsToRow(0);

        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Change row order (swap selected row with the previous one)");

        findAndClickButton("Update Letters");

        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Change row order (swap selected row with the previous one)");

        changeSpaceBetwenPanelsInAFigure(30);
        pressSpinnerUp(getJSpinnerButtons(findByText("Empty space between rows and columns within a row")), 15);
        pressSpinnerDown(getJSpinnerButtons(findByText("Empty space between rows and columns within a row")), 42);

        /**
         * test du swapping et du delete dans les Panels
         */
        selectObjectsInAList(SF.figureList, 0);
        selectObjectsInAList(SF.RowContentList, 2);
        findAndClickButton("Move Panel to the left");
        pause();
        findAndClickButton("Move Panel to the left");
        pause();
        findAndClickButton("Move Panel to the right");
        pause();
        findAndClickButton("Move Panel to the right");

        /*
         * on delete un Panel d'une row
         */
        selectObjectsInAList(SF.figureList, 0);
        selectObjectsInAList(SF.RowContentList, 0);
        findAndClickButton("Remove selected Panel");
        pause();

        /*
         * on ajoute a nouveau un Panel
         */
        selectObjectsInAList(SF.tableList, 0);
        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Add Panel To Current Row");
        findAndClickButton("Update Letters");

        selectAJournalStyle("Nature Methods");
        changeJounralColSize("1 Col");
        changeJounralColSize("2 Cols");

        /*
         * now we test cropping rotation, ... inside a row
         */
        selectObjectsInAList(SF.figureList, 0);
        selectObjectsInAList(SF.RowContentList, 0);
        selectObjectsInAList(SF.imagesInFigureList, 1);

        cropDown(128);
        pressSpinnerUp(getJSpinnerButtons(findByText("Empty space between rows and columns within a row")), 15);
        pressSpinnerDown(getJSpinnerButtons(findByText("Empty space between rows and columns within a row")), 15);

        cropLeft(128);
        cropRight(128);
        cropTop(128);
        rotateImage(45);
        cropDown(0);
        cropLeft(0);
        cropRight(0);
        cropTop(0);
        pause(1500);

        /*
         * now we test the menus
         */
        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Export to ImageJ");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Select And Apply A Font or Text Color or Bg Color To All Components");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Select And Apply A Specific Line Width/Point Size To Graphs or a Stroke Size to ROI or the Stroke Size of SVG objects");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Apply The Selected Journal Style To All Components");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Apply Selected Width (in cm/px) To All Components");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Remove All Text");

        simulateAnyKeyInSomeMs(1200, "Enter", -1);
        findAndClickButton("Remove All Scale Bars");

        findAndClickButton("Update Letters");

        /*
         * on remove une row
         */
        selectObjectsInAList(SF.figureList, 0);
        findAndClickButton("Remove the current block from the selected row");

        pause(3500);

        /*
         * force quit the soft
         */
        ScientiFig_.mustWarnOnQuit = false;
        System.exit(0);

    }

    //faire un match entre ce qui est attendu et ce qui est genere par les macros --> permet d'automatiser les tests
    public static void main(String[] args) {
        WiseDebug wd = new WiseDebug();
        wd.createAMontageManually();

    }
}


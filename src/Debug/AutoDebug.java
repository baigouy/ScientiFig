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
package Debug;

import GUIs.ScientiFig;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;

/**
 * AutoDebug lists all buttons, combos, menuItems and spinners in SF and then
 * fakes their execution to find bugs. Run it once without images loaded and
 * once with (make sure that the path to a .yf5m exists)
 *
 * @since <B>SF v2.1</B>
 * @author Benoit Aigouy
 */
public class AutoDebug {

    /**
     * buttons that should be excluded from the test
     */
    static ArrayList<Component> forbidden_buttons = new ArrayList<Component>();
    public static ArrayList<Component> interactibles = new ArrayList<Component>();
    static ScientiFig SF;

    public static void clear() {
        forbidden_buttons = new ArrayList<Component>();
        interactibles = new ArrayList<Component>();
    }

    public AutoDebug(ScientiFig SF) {
        AutoDebug.SF = SF;
        SF.setVisible(true);
        SF.addAllPanelsToOptions();
        forbidden_buttons.add(SF.New);
        forbidden_buttons.add(SF.Quit);
        forbidden_buttons.add(SF.jButton7);
        forbidden_buttons.add(SF.Save);
        forbidden_buttons.add(SF.createPanelAutomatically);//AUTO
    }

    /*
     * this function presses all the buttons contained in the soft
     * (even tests the up and down buttons of spinners)
     */
    public static void populateInteractibleFromSF() {
        interactibles.clear();
        getAllInteractibleComponents(SF);
        getAllInteractibleComponents(ScientiFig.ief);
        for (Component object : interactibles) {

            /*
             * here we simulate a click on all buttons
             */
            if (object instanceof JButton) {
                if (!forbidden_buttons.contains(object)) {
                    String txt = getText(object);
                    if (txt == null) {
                        continue;
                    }
                    System.out.println("executing \"" + txt + "\"");
                    ((JButton) object).doClick();
                }
            }
            /*
             * here we simulate a click on all menuItems
             */
            if (object instanceof JMenuItem) {
                if (!forbidden_buttons.contains(object)) {
                    System.out.println("executing \"" + ((JMenuItem) object).getText() + "\"");
                    ((JMenuItem) object).doClick();
                }
            }
            if (object instanceof JComboBox) {
                /*
                 * here we simulate a selection change for the combos
                 */
                ((JComboBox) object).setSelectedIndex(0);
                if (((JComboBox) object).getSelectedItem() != null) {
                    System.out.println("executing \"" + ((JComboBox) object).getSelectedItem().toString() + "\"");
                }
            }
        }
    }

    /**
     *
     * @param o
     * @return button text or null
     */
    public static String getText(Object o) {
        String txt;
        if (o instanceof JButton) {
            txt = ((JButton) o).getText();
            if (txt == null || txt.equals("")) {
                txt = ((JButton) o).getToolTipText();
                if (txt == null || txt.equals("")) {
                    txt = ((JButton) o).getName();
                }
                /*
                 * we skip scrollbar buttons
                 */
                if (txt != null && txt.contains("ScrollBar.button")) {
                    return null;
                }
            }
            return txt;
        }
        if (o instanceof JMenu) {
            return ((JMenu) o).getText();
        }
        if (o instanceof JMenuItem) {
            return ((JMenuItem) o).getText();
        }
        if (o instanceof JComboBox) {
            return ((JComboBox) o).getToolTipText();
        }
        if (o instanceof JSpinner) {
            return ((JSpinner) o).getToolTipText();
        }
        /*
         * for tab pane components
         */
        if (o instanceof Component) {
        }
        return null;
    }

    /**
     * Lists all the subcomponents of a component
     *
     * @param component
     */
    public static void getAllInteractibleComponents(Component component) {
        if (component instanceof Container) {
            Container contain = (Container) component;
            for (int i = 0; i < contain.getComponentCount(); i++) {
                Component c = contain.getComponent(i);
                if (c instanceof JButton) {
                    interactibles.add(c);
                }
                if (c instanceof JSpinner) {
                    interactibles.add(c);
                }
                if (c instanceof JComboBox) {
                    interactibles.add(c);
                }
                if (c instanceof JMenu) {
                    if (!interactibles.contains(c)) {
                        interactibles.add(c);
                    }
                    for (Component object : ((JMenu) c).getMenuComponents()) {
                        if (!interactibles.contains(object)) {
                            interactibles.add(object);
                        }
                    }
                }
                if (c instanceof JTabbedPane) {
                    interactibles.add(c);
                }
                if (c instanceof Container) {
                    getAllInteractibleComponents((Container) c);
                }
            }
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        boolean execute_empty_test = true;
        boolean execute_test_with_images = true;
        boolean autoClose = false;
        if (execute_empty_test) {
            /*
             * empty soft test of all buttons
             */
            AutoDebug test = new AutoDebug(ScientiFig.getInstance());
            test.populateInteractibleFromSF();
        }
        if (execute_test_with_images) {
            /*
             * test of all buttons with an image loaded
             */
            String name_of_the_file_2_load = "/home/benoit/Bureau/testSF/perfect_test_sample_for_automation.yf5m";
            ScientiFig SF_with_image = ScientiFig.getInstance();
            SF_with_image.loadFile(name_of_the_file_2_load, false, true);
            SF_with_image.setVisible(true);
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            AutoDebug test2 = new AutoDebug(SF_with_image);
            test2.populateInteractibleFromSF();
        }
        if (autoClose) {
            System.exit(0);
        }
    }
}



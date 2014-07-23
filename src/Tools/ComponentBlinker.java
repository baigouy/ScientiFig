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
package Tools;

import Debug.AutoDebug;
import Debug.WiseDebug;
import Dialogs.GlassPane;
import MyShapes.MyRectangle2D;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;

/**
 * ComponentBlinker does...
 *
 * @author Benoit Aigouy
 */
public class ComponentBlinker {

    ArrayList<Component> interactibles = new ArrayList<Component>();
    HashMap<String, Object[]> nameAndComponent = new HashMap<String, Object[]>();
    HashMap<String, String> componentNameAndParentComponentName = new HashMap<String, String>();
    GlassPane gp;
    Timer t;

    
//    public ComponentBlinker()
//    {
//        
//    }
    
    public ComponentBlinker(Component master, GlassPane gp) {
        this.gp = gp;
        getAllInteractibleComponents(master);
        listAllObjectsByName();
        listParentTabForButtons();
    }

    /**
     * make a component blink only knowing its name
     *
     * @param name
     */
    public void blinkAnything(String name) {
        if (nameAndComponent == null || nameAndComponent.isEmpty()) {
            System.err.println("Component unknown:" + name + "\nPlease initialize blinker components");
            return;
        }
        Object[] stuffToBlink = nameAndComponent.get(name);
        if (stuffToBlink == null) {
            System.err.println("Cannot blink: " + name + " --> Component not found");
            return;
        }
        if (stuffToBlink.length > 1) {
            /**
             * we select the right tab before we blink
             */
            JTabbedPane tab = ((JTabbedPane) stuffToBlink[0]);
            String tab_tite = (String) stuffToBlink[1];
            int count = tab.getTabCount();
            for (int i = 0; i < count; i++) {
                String title = tab.getTitleAt(i);
                if (title.equals(tab_tite)) {
                    tab.setSelectedIndex(i);
                    blinkAnything(tab.getComponentAt(i));
                    break;
                }
            }

        } else {
            String parentTab = componentNameAndParentComponentName.get(name);
            if (parentTab != null) {
                findAndSelectTab(parentTab);
            }
            blinkAnything((Component) stuffToBlink[0]);
        }
    }

    public void findAndSelectTab(String txt) {
        Object button = nameAndComponent.get(txt);
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

    /**
     * a small method to allow components to blink in the gp
     *
     * @param c the composant that must be blinking
     */
    public void blinkAnything(final Component c) {
        if (c == null || !c.isShowing()) {
            return;
        }
        if (t != null) {
            t.cancel();
            t = null;
        }
        t = new Timer();
        t.schedule(new TimerTask() {
            int nb_of_blinks = 10;
            int count = 0;

            @Override
            public void run() {
                try {
                    MyRectangle2D.Double pos;
                    /*
                     * choose blink option (even counter --> show something otherwise hide)
                     */
                    if (count % 2 == 0) {
                        /*
                         * we force pos to update in case the window has been resized while blinking for example
                         */
                        pos = new MyRectangle2D.Double(getComponentPosition(c));
                        pos.setColor(0x00FF00);
                        pos.setTransparency(0.3f);
                    } else {
                        pos = null;
                    }
                    gp.setBlink(pos);
                    gp.repaint();
                    count++;
                    if (count >= nb_of_blinks) {
                        gp.clearBlink();
                        t.cancel();
                    }
                } catch (Exception e) {
                }
            }
        }, 0, 500);
    }

    /**
     * Lists all the subcomponents of a component
     *
     * @param component
     */
    private void getAllInteractibleComponents(Component component) {
        AutoDebug.getAllInteractibleComponents(component);
        interactibles = new ArrayList<Component>(AutoDebug.interactibles);
        AutoDebug.clear();
    }

    private void listAllObjectsByName() {
        nameAndComponent = new HashMap<String, Object[]>(WiseDebug.getInteractibleText(interactibles));
        WiseDebug.clear();
    }

    /**
     * if interactible objects are in a tab associate it to the right tab
     */
    private void listParentTabForButtons() {
        for (Component component : interactibles) {
            if (component instanceof JTabbedPane) {
                JTabbedPane tmp = ((JTabbedPane) component);
                int count = tmp.getTabCount();
                for (int i = 0; i < count; i++) {
                    Component c = tmp.getComponentAt(i);
                    String title = tmp.getTitleAt(i);
                    AutoDebug.clear();
                    AutoDebug.getAllInteractibleComponents(c);
                    ArrayList<Component> interactibleBackup = AutoDebug.interactibles;
                    WiseDebug.clear();
                    HashMap<String, Object[]> tmpTxt = WiseDebug.getInteractibleText(interactibleBackup);
                    ArrayList<String> text = new ArrayList<String>(tmpTxt.keySet());
                    for (String string : text) {
                        componentNameAndParentComponentName.put(string, title);
                    }
                }
            }
        }
    }

    /**
     *
     * @param c component
     * @return the bounds of a component (can be used for blinking the
     * component)
     */
    
    //the fucking bug is there --> why does it prevent FIJI from closing
    public static Rectangle2D getComponentPosition(Component c) {
        Rectangle bounds = c.getBounds();
        double transX = 0;
        double transY = 0;
        Component o = c.getParent();
        while (o.getParent() != null) {
            /*
             * we have to stop at rootpane if we wanna avoid position pbs
             */
            if (o instanceof JRootPane) {
                break;
            }
            transX += o.getBounds().x;
            transY += o.getBounds().y;
            o = o.getParent();
        }
        bounds.x += transX;
        bounds.y += transY;
        return bounds;
//        return null;
    }

    public void stop() {
        if (t != null) {
            try {
                t.cancel();
                t = null;
            } catch (Exception e) {
            }
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        /**
         * NB to test it just run scientifg and click on any button without
         * images opened
         */
//           CommonClasses.setMaxNbOfProcessorsToMaxPossible();
//           ArrayList<String> list = new LoadListeToArrayList().apply("/list.lst");
        long start_time = System.currentTimeMillis();
//        ComponentBlinker test = new ComponentBlinker();
//        test.apply(list); 
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


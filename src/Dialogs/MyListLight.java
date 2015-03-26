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
package Dialogs;

/*
 * MyList2.java
 *
 * Created on 27-Aug-2008, 12:19:33
 */
import GUIs.ScientiFig;
import Commons.IconLabel;
import Commons.CommonClassesLight;
import Commons.MyWriter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.*;

/**
 * MyList2 is just a list that can display icons and supports DND of images
 *
 * @author Benoit Aigouy
 */
public class MyListLight extends javax.swing.JPanel {

    public DefaultListModel listModel;
    public IconListCellRenderer iconListCellRenderer = new IconListCellRenderer();
    public ArrayList<String> list_of_elements_to_add = new ArrayList<String>();
    private String[] supportedExtensions = {".figur", ".svg", ".jpg", ".yf5m", ".jpeg", ".png", ".bmp", ".gif", ".tga", ".tif", ".tiff"};
    private static final int ALPHABETICAL_ORDER = 0;
    private static final int REVERSE = 1;

    /**
     * Creates new form MyList2
     */
    public MyListLight() {
        initComponents();
        listModel = new DefaultListModel();
        list.setCellRenderer(iconListCellRenderer);
        list.setModel(listModel);
        list.setSelectedIndex(0);
    }

    public MyListLight(DefaultListModel listModel) {
        this();
        this.listModel = listModel;
        list.setModel(listModel);
    }

    /**
     * change list conent
     *
     * @param absolute_path
     */
    public void setContent(ArrayList<String> absolute_path) {
        loadList(absolute_path);
    }

    /**
     * select the next entry in the list
     */
    public void selectNext() {
        int current_selection = getFullIndex();
        if (current_selection < getFullList().size() - 1) {
            list.setSelectedIndex(current_selection + 1);
        }
    }

    /**
     * select the previous entry in the list
     */
    public void selectPrev() {
        int current_selection = getFullIndex();
        if (current_selection > 0) {
            list.setSelectedIndex(current_selection - 1);
        }
    }

    /**
     * sets the supported drop extensions
     *
     * @param supportedExtensions
     */
    public void setSupportedExtensions(String[] supportedExtensions) {
        this.supportedExtensions = supportedExtensions;
    }

    public void removeShortcuts() {
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
    }

    /**
     * reverses list order
     */
    public void reverseList() {
        changeListOrder(REVERSE);
    }

    /**
     * Sorts the list by alphabetical order
     */
    public void sortAlpahbetically() {
        changeListOrder(ALPHABETICAL_ORDER);
    }

    private void changeListOrder(int MODE) {
        ArrayList<String> lst = getFullList();
        switch (MODE) {
            case ALPHABETICAL_ORDER:
                Collections.sort(lst);
                break;
            case REVERSE:
                Collections.reverse(lst);
                break;
        }
        listModel.clear();
        list.clearSelection();
        loadList(lst);
    }

    /**
     *
     * @return trur if the list is empty
     */
    public boolean isEmpty() {
        return listModel == null || listModel.isEmpty() ? true : false;
    }

    public void loadList(ArrayList<String> lst) {
        LoadToList test = new LoadToList(lst);
        test.start();
    }

    public void acceptdata(String data) {
        if (accept(data)) {
            data = CommonClassesLight.change_path_separators_to_system_ones(data);
            addToList(data);
        }
    }

    public void addAll(File[] list) {
        if (list != null) {
            for (File name : list) {
                String string = CommonClassesLight.change_path_separators_to_system_ones(name.toString());
                if (name.exists()) {
                    acceptdata(string);
                }
            }
            addAllToList();
        }
    }

    public void addAllNoCheck(ArrayList<String> list) {
        if (list != null) {
            for (String string : list) {
                if (string == null) {
                    continue;
                }
                if (string.contains("importJ:")) {
                    if (ScientiFig.imported_from_J.containsKey(string)) {//ca marche
                        addToListNoCheckIfNotExists(string);
                    }
                } else {
                    addToList(string);
                }
            }
            addAllToList();
        }
    }

    public void addAll(ArrayList<String> list) {
        if (list != null) {
            for (String string : list) {
                string = CommonClassesLight.change_path_separators_to_system_ones(string);
                acceptdata(string);
            }
            addAllToList();
        }
    }

    public void addAllIfNotAlreadyContained(ArrayList<String> list) {
        ArrayList<String> added = new ArrayList<String>();
        if (list != null) {
            for (String string : list) {
                string = CommonClassesLight.change_path_separators_to_system_ones(string);
                if (!getFullList().contains(string) && !added.contains(string)) {
                    added.add(string);
                    acceptdata(string);
                }
            }
            addAllToList();
        }
        updateList();
    }

    /**
     *
     * @param f a file name
     * @return true if the passed file is valid
     */
    public boolean acceptFile(String f) {
        if (f == null) {
            return false;
        }
        return accept(f);
    }

    /**
     *
     * @param f a file
     * @return true if the passed file is valid
     */
    public boolean accept(String f) {
        String fichier = f.toLowerCase();
        for (String string : supportedExtensions) {
            if (fichier.endsWith(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Swaps two elements from the list
     *
     * @param first_pos
     * @param second_pos
     */
    public void swap(int first_pos, int second_pos) {
        if (listModel != null && !listModel.isEmpty() && first_pos >= 0 && second_pos >= 0 && first_pos < listModel.size() && second_pos < listModel.size()) {
            Object tmp = listModel.get(first_pos);
            listModel.setElementAt(listModel.get(second_pos), first_pos);
            listModel.setElementAt(tmp, second_pos);
        }
    }

    /**
     * create an iconLabel
     *
     * @param pathToTheFile
     * @return an IconLabel that we can add to the list
     */
    public IconLabel createLabel(String pathToTheFile) {
        return new IconLabel(pathToTheFile);
    }

    /**
     * create an iconLabel
     *
     * @param text label text
     * @param label image
     * @return an IconLabel that we can add to the list
     */
    public IconLabel createLabel(String text, BufferedImage img) {
        return new IconLabel(text, img);
    }

    public int Size() {
        return listModel.size();
    }

    /**
     * Select the object at index idx
     *
     * @param idx
     */
    public void select(int idx) {
        list.setSelectedIndex(idx);
    }

    /**
     *
     * @return all the objects contained in the list
     */
    public ArrayList<String> getFullList() {
        ArrayList<String> list2 = new ArrayList<String>();//pb of list model is that
        for (int i = 0; i < listModel.size(); i++) {
            list2.add(listModel.get(i).toString());
        }
        return list2;
    }

    /**
     * select passed indices in the list
     *
     * @param indices
     */
    public void setSelectedIndices(int[] indices) {
        list.setSelectedIndices(indices);
    }

    /**
     * select the entry n in the list
     *
     * @param n
     */
    public void setSelectedIndex(int n) {
        list.setSelectedIndex(n);
    }

    /**
     * The value of the selected object
     *
     * @return
     */
    public String getSelectedValue() {
        if (list.getSelectedIndex() != -1) {
            return list.getSelectedValue().toString();
        } else {
            return null;
        }
    }

    public String getFirstImage() {
        if (listModel.size() > 0) {
            return listModel.getElementAt(0).toString();
        } else {
            return null;
        }
    }

    public String getSelectedOrFirstImage() {
        String name = getSelectedValue();
        return name == null ? getFirstImage() : name;
    }

    public ArrayList<String> getSelectedValues() {
        int[] indices = list.getSelectedIndices();
        ArrayList<String> selected_indices = new ArrayList<String>();
        if (indices != null) {
            for (int i : indices) {
                selected_indices.add(listModel.elementAt(i).toString());
            }
            return selected_indices;
        } else {
            return null;
        }
    }

    public int getCurrentSelection() {
        return list.getSelectedIndex();
    }

    /**
     *
     * @return the real list selection index irrespective of selection mode
     */
    public int getFullIndex() {
        return list.getSelectedIndex();
    }

    public int getSelectedIndex() {
        return getCurrentSelection();
    }

    /**
     *
     * @return true if the list selection is empty
     */
    public boolean isSelectionEmpty() {
        return list.isSelectionEmpty();
    }

    /**
     *
     * @return a list of selected files
     */
    public ArrayList<String> getSelection() {
        ArrayList<String> sel = new ArrayList<String>();
        int[] current_selection = list.getSelectedIndices();
        for (int i = 0; i < current_selection.length; i++) {
            sel.add(listModel.get(current_selection[i]).toString());
        }
        return sel;
    }

    public void removeSelection() {
        int[] current_selection = list.getSelectedIndices();
        Arrays.sort(current_selection);//,1,4);
        for (int i = 0; i < current_selection.length / 2; i++) {
            int tmp = current_selection[i];
            current_selection[i] = current_selection[current_selection.length - (i + 1)];
            current_selection[current_selection.length - (i + 1)] = tmp;
        }
        for (int i : current_selection) {
            listModel.remove(i);
        }
    }

    public void addDirectlyToList(String data, BufferedImage img) {
        IconLabel il = createLabel(data, img);
        listModel.addElement(il);
    }

    /**
     *
     * @return the selected list indices
     */
    public int[] getSelectedIndices() {
        return list.getSelectedIndices();
    }

    /**
     * Empties the list
     */
    public void clearList() {
//        list.clearSelection();
        listModel.clear();
//        list.updateUI();
        updateList();
    }

    /**
     * removes the selection (but not the content of the list)
     */
    public void clearSelection() {
        list.clearSelection();
    }

    /**
     * Removes the specified element from the list
     *
     * @param nb
     */
    public void removeElement(int nb) {
        if (nb >= 0 && nb < listModel.size()) {
            listModel.remove(nb);
        }
    }

    /**
     * removes all the selected objects
     */
    public void removeSelectedObjects() {
        int[] current_selection = list.getSelectedIndices();
        list.clearSelection();
        Arrays.sort(current_selection);
        for (int i = 0; i < current_selection.length / 2; i++) {
            int tmp = current_selection[i];
            current_selection[i] = current_selection[current_selection.length - (i + 1)];
            current_selection[current_selection.length - (i + 1)] = tmp;
        }
        for (int i : current_selection) {
            listModel.remove(i);
        }
    }

    /**
     * removes all unselected objects from the list
     */
    public void DeleteNotSelected() {
        ArrayList<Integer> neo_selection = new ArrayList<Integer>();
        int[] current_selection = list.getSelectedIndices();
        list.clearSelection();
        ArrayList<Integer> selected = new ArrayList<Integer>();
        for (int i : current_selection) {
            selected.add(i);
        }
        for (int i = 0; i < listModel.size(); i++) {
            neo_selection.add(i);
        }
        neo_selection.removeAll(selected);
        Collections.reverse(neo_selection);
        for (int pos : neo_selection) {
            removeElement(pos);
        }
    }

    /**
     * move selection up in the list
     *
     * @param evt
     */
    public void MoveUp(java.awt.event.ActionEvent evt) {
        int[] current_selection = getSelectedIndices();
        if (current_selection.length != 0) {
            if (current_selection[0] > 0) {
                for (int i = 0; i < current_selection.length; i++) {
                    if (current_selection[0] > 0) {
                        Object o4 = listModel.get(current_selection[i]);
                        Object o5 = listModel.get(current_selection[i] - 1);
                        listModel.set(current_selection[i], o5);
                        listModel.set(current_selection[i] - 1, o4);
                    }
                }
                for (int i = 0; i < current_selection.length; i++) {
                    current_selection[i]--;
                }
                list.setSelectedIndices(current_selection);
            }
        }
    }

    /**
     * move selection down
     *
     * @param evt
     */
    public void MoveDown(java.awt.event.ActionEvent evt) {
        int[] current_selection = getSelectedIndices();
        if (current_selection.length != 0) {
            if (current_selection[current_selection.length - 1] < Size() - 1) {
                for (int i = current_selection.length - 1; i >= 0; i--) {
                    if (current_selection[current_selection.length - 1] < Size() - 1) {
                        Object o4 = listModel.get(current_selection[i]);
                        Object o5 = listModel.get(current_selection[i] + 1);
                        listModel.set(current_selection[i], o5);
                        listModel.set(current_selection[i] + 1, o4);
                    }
                }
                for (int i = 0; i < current_selection.length; i++) {
                    current_selection[i]++;
                }
                list.setSelectedIndices(current_selection);
            }
        }
    }

    /**
     * Saves the name of the files contained in the list
     *
     * @param name
     */
    public void saveListAs(String name) {
        if (!listModel.isEmpty()) {
            new MyWriter().apply(name, getFullList());
        }
    }

    /**
     * refresh list UI
     */
    public void updateList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                list.updateUI();
            }
        });
    }

    public void addToListNoCheckIfNotExists(String elt) {
        if (!getFullList().contains(elt)) {
            list_of_elements_to_add.add(elt);
        }
    }

    public void addToListNoCheck(String elt) {
        list_of_elements_to_add.add(elt);
    }

    /**
     * Adds an element to the queue
     *
     * @param elt
     */
    public void addToList(String elt) {
        list_of_elements_to_add.add(elt);
    }

    public void addToListIfExists(String elt) {
        if (new File(elt).exists()) {
            addToList(elt);
        }
    }

    /**
     * Updates list content
     */
    public void addAllToList() {
        LoadToList test = new LoadToList(list_of_elements_to_add);
        test.start();
    }

    public JList getList() {
        return list;
    }

    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        list.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
    }

    /**
     * just override this to handle drop
     *
     * @param files2add
     */
    public void processDrop(ArrayList<String> files2add) {
        /*
         * we avoid concurrent access when loading several .yf5m
         */

        if (files2add != null) {
            ArrayList<String> yf5mFiles = new ArrayList<String>();
            for (String string : files2add) {
                if (string.toLowerCase().endsWith(".yf5m")) {
                    yf5mFiles.add(string);
                }
            }
            files2add.removeAll(yf5mFiles);
            ScientiFig.yf5m_files = yf5mFiles;
            ScientiFig.loadAllyf5m.doClick();
        }
        if (files2add != null) {
            //ce truc pourrait etre MT
            while (!files2add.isEmpty()) {
                String elt = files2add.get(0);
                files2add.remove(0);
                if (elt == null) {
                    continue;
                }
                IconLabel l = createLabel(elt);
                if (l == null) {
                    continue;
                }
                listModel.addElement(l);
                updateList();
            }
        }
    }

    class LoadToList extends Thread {

        ArrayList<String> files2add = null;

        LoadToList() {
        }

        LoadToList(ArrayList<String> files) {
            this.files2add = files;
        }

        LoadToList(String name) {
            files2add = new ArrayList<String>();
            files2add.add(name);
        }

        @Override
        public void run() {
            processDrop(files2add);
        }
    }

    public static void main(String[] args) {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> fake = new ArrayList<String>();
                fake.add("toto.png");
                fake.add("toto.png");
                fake.add("toto.png");
                fake.add("toto.png");
                fake.add("toto.png");
                MyListLight test = new MyListLight();
                test.loadList(fake);
                f.add(test).setVisible(true);
                f.pack();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        list.setBorder(javax.swing.BorderFactory.createTitledBorder("Image list (Drop your files here)"));
        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JList list;
    // End of variables declaration//GEN-END:variables
}

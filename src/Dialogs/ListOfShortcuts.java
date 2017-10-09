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
 /*
 * ListOfShortcuts.java
 *
 * Created on 24 octobre 2008, 11:43
 */
package Dialogs;

import Commons.CommonClassesLight;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Benoit Aigouy
 */
public class ListOfShortcuts extends javax.swing.JPanel {

    public static final long serialVersionUID = 2718089373730568781L;

    LinkedHashMap<Object, String> defaultShortcuts;
//    LinkedHashMap<Object, String> shortcuts;
    public TableModel model;
    public JTable table;

    //if the shortcut is gonna duplicate another shortcut just cancel
    public ListOfShortcuts(LinkedHashMap<Object, String> shortcuts) {
        this(shortcuts, null);
    }

    /**
     * Creates new form ListOfShortcuts
     *
     * @param shortcuts list of shortcuts and their associated functions
     */
    public ListOfShortcuts(LinkedHashMap<Object, String> shortcuts, LinkedHashMap<Object, String> defaultShortcuts) {
        initComponents();
        if (defaultShortcuts == null) {
            jButton1.setVisible(false);
        }
        this.defaultShortcuts = defaultShortcuts;
//        this.shortcuts = shortcuts;
        CommonClassesLight.speedUpJScrollpane(jScrollPane1);

        table = new JTable();
        initTable(shortcuts);

        jPanel2.add((table));
        jPanel2.revalidate();
    }

    public class ButtonKeyStrokeRenderer extends JButton implements TableCellRenderer {

        public ButtonKeyStrokeRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                /* only keystrokes are editable and should acquire a button shape */
                if (value instanceof KeyStroke) {
                    setForeground(table.getForeground());
                    setBackground(UIManager.getColor("Button.background"));
                } else {
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }
            }
            if (value instanceof KeyStroke) {
                setText(CustomShortcutDefinition.getFormattedKeys((KeyStroke) value));
            } else {
                setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }

    private boolean shortcutAlreadyExists(LinkedHashMap<Object, String> shortcuts, Object key) {
        return shortcuts.containsKey(key);
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean pressed;
        private Object sel;
        private Component parent ;

        public ButtonEditor(JTextField textField, Component parent) {
            super(textField);
            this.parent = parent;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            if (value instanceof KeyStroke) {
                label = CustomShortcutDefinition.getFormattedKeys((KeyStroke) value);
            } else {
                label = (value == null) ? "" : value.toString();
            }
            button.setText(label);
            sel = value;
            pressed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (pressed) {
                if (sel instanceof KeyStroke) {
                    CustomShortcutDefinition iopane = new CustomShortcutDefinition();
                    int result = JOptionPane.showOptionDialog(parent, new Object[]{iopane}, "Custom shortcuts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (result == JOptionPane.OK_OPTION) {
                        if (!iopane.KeySet) {
                            CommonClassesLight.Warning("<html><center>please enter a valid shortcut, e.g. a letter and possibly a modifier (Ctrl,Alt,Command,Shift,...), modifiers alone are not allowed");
                        } else {
                            /* do smthg with the new shortcut*/
                            Object value = iopane.getKeyStroke();
                            if (value instanceof KeyStroke) {
                                /* if the specified shortcut is unique, then we accept it */
                                if (!shortcutAlreadyExists(getShortcuts(), value)) {
                                    sel = value;
                                    pressed = false;
                                    return value;
                                } else {
                                    /* if the shortcut already exists then print a warning  */
                                    CommonClassesLight.Warning("Shortcut already exists, please use another shortcut");
                                }
                            }
                        }
                    }
                }
            }
            pressed = false;
            return sel;
        }

        @Override
        public boolean stopCellEditing() {
            pressed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public LinkedHashMap<Object, String> getShortcuts() {
        LinkedHashMap<Object, String> shortcuts = new LinkedHashMap<Object, String>();
        int nbRows = table.getRowCount();
        for (int i = 0; i < nbRows; i++) {
            Object key = model.getValueAt(i, 0);
            String value = model.getValueAt(i, 1).toString();
            shortcuts.put(key, value);
        }
        return shortcuts;
    }

    /* demo list of shortcuts */
    static LinkedHashMap<Object, String> dynamicShortcuts = new LinkedHashMap<Object, String>();

    static {
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Quit");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "Preview below the mask for a few seconds. Please prefer pressing M several times!");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "Show/hide mask");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply correction");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Zoom +");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Zoom -");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Open ImageJ");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Move to the next image in the list");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Move to the previous image in the list");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK), "altR");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Rerun watershed using local seeds");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "Remove small cells, apply correction and save");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "F2");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Open files");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Quit");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "About");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Undo");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Save");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "Remove small cells and apply correction");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectNextRow");
        dynamicShortcuts.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectPreviousRow");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("ShortCuts (double click on a shortcut to edit it)"));

        jPanel2.setLayout(new java.awt.GridLayout(1, 2));
        jScrollPane1.setViewportView(jPanel2);

        jButton1.setText("reset shortcuts");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetShortcutsToDefault(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 442, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void resetShortcutsToDefault(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetShortcutsToDefault
        initTable(defaultShortcuts);
    }//GEN-LAST:event_resetShortcutsToDefault

    private void initTable(LinkedHashMap<Object, String> shortcuts) {
        Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
        Vector<String> header = new Vector<String>();
        header.add("Shortcuts");
        header.add("Description");
        if (shortcuts != null) {
            for (Map.Entry<Object, String> entry : shortcuts.entrySet()) {
                Vector<Object> data = new Vector<Object>();
                Object key = entry.getKey();
                String value = entry.getValue();
                data.add(key);
                data.add(value);
                dataVector.add(data);
            }
        }
        model = new DefaultTableModel(dataVector, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                /* only the first column can be edited */
                return column == 0;
            }
        };
        table.setModel(model);
        table.getColumn(header.get(0)).setCellRenderer(new ButtonKeyStrokeRenderer());
        table.setRowHeight(30);
        table.getColumn(header.get(0)).setCellEditor(new ButtonEditor(new JTextField(),this));
        table.repaint();
    }

    public static void main(String[] args) {
        LinkedHashMap<Object, String> dup = new LinkedHashMap<Object, String>(dynamicShortcuts);
        ListOfShortcuts spane = new ListOfShortcuts(dynamicShortcuts, dup);
        Object[] pane = new Object[2];
        pane[0] = spane;
        JOptionPane.showMessageDialog(null, pane, "About...", JOptionPane.PLAIN_MESSAGE);
        System.out.println(spane.getShortcuts());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

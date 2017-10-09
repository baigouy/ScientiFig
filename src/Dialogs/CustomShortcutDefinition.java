package Dialogs;

import Commons.CommonClassesLight;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Records user defined shortcuts
 *
 * @author benoit aigouy
 */
public class CustomShortcutDefinition extends javax.swing.JPanel implements KeyListener {

    String initialText;
    int modifier = 0;
    int keyTyped = 0;
    boolean modifierSet = false;
    boolean KeySet = false;

    /**
     * Creates new form CustomShortcutDefinition
     */
    public CustomShortcutDefinition() {
        initComponents();
        jToggleButton1.addKeyListener(this);
        initialText = jToggleButton1.getText();
    }

//    public static ArrayList<String> modifier
    @Override
    public void keyPressed(KeyEvent e) {
        if (jToggleButton1.isSelected()) {
            if (e.getModifiers() != 0) {
                modifier = e.getModifiersEx();
                jLabel1.setText(InputEvent.getModifiersExText(e.getModifiersEx()));
                modifierSet = true;
            }

            String test = KeyEvent.getKeyText(e.getKeyCode());
            String labelText = jLabel1.getText();

            ///           System.out.println(test +" "+e.getKeyCode()+ " "+e.getModifiers());
            //bug fix to handle more user defined shortcuts
            if (!labelText.equals(test) && (/*test.length() < 3 &&*/!labelText.contains(test))) {
                jLabel3.setText(test);
                keyTyped = e.getKeyCode();
                KeySet = true;
                if (e.getModifiersEx() == 0) {
                    jLabel1.setText("");
                    modifierSet = false;
                    modifier = 0;
                }
            }

            if (KeySet && modifierSet) {
                jToggleButton1.setSelected(false);
                jToggleButton1.setText(initialText);
                modifierSet = false;
            } else if (e.getModifiersEx() == 0 && KeySet) {
                jToggleButton1.doClick();
                jToggleButton1.setSelected(false);
                jToggleButton1.setText(initialText);
                modifierSet = false;
                modifier = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jToggleButton1.setText("Toggle me when ready to enter your keyboard inputs");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                run(evt);
            }
        });

        jLabel1.setText("modifiers");

        jLabel2.setText("Keys pressed:");

        jLabel3.setText("Letters/keys");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jToggleButton1});

    }// </editor-fold>//GEN-END:initComponents

    private void run(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_run
        if (jToggleButton1.isSelected()) {
            jToggleButton1.setText("<html><font color='red'>Recording...");
            jLabel1.setText("");
            jLabel3.setText("");
            modifierSet = false;
            KeySet = false;
        } else {
            jToggleButton1.setText(initialText);
        }
    }//GEN-LAST:event_run

    public String getFormattedKeys() {
        String modifier = jLabel1.getText();
        String key = jLabel3.getText();
        return getFormattedKeys(modifier, key);
    }

    public static String getFormattedKeys(int modifier, int key) {
        String modifierS = InputEvent.getModifiersExText(modifier);
        String keyS = KeyEvent.getKeyText(key);
        return getFormattedKeys(modifierS, keyS);
    }

    public static String getFormattedKeys(KeyStroke key) {
        return getFormattedKeys(key.getModifiers(), key.getKeyCode());
    }

    public static String getFormattedKeys(String modifier, String key) {
        return modifier.trim().equals("") ? key : modifier + " + " + key;
    }

    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(keyTyped, modifier);
    }

    public static void main(String[] args) {
        CustomShortcutDefinition iopane = new CustomShortcutDefinition();
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "Custom shortcuts", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            if (!iopane.KeySet) {
                CommonClassesLight.Warning("<html><center>please enter a valid shortcut, e.g. a letter and possibly a modifier (Ctrl,Alt,Command,Shift,...), modifiers alone are not allowed");
            } else {
                /* do smthg with the new shortcut*/
                System.out.println(iopane.modifier + " " + iopane.keyTyped + " '" + iopane.getFormattedKeys() + "'");
            }
        }
        System.exit(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}

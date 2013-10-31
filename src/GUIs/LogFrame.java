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
/*
 * ErrorFrame.java
 *
 * Created on 22 juin 2011, 11:58:01
 */
package GUIs;

import Commons.CommonClassesLight;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.SwingUtilities;

/**
 * ErrorFrame is a system.err logging dialog
 *
 * @author Benoit Aigouy
 */
public class LogFrame extends javax.swing.JFrame {

    public static final int ERROR_LOGGER = 0;
    public static final int SOUT_LOGGER = 1;
    /**
     * Sets the type of the logger window
     */
    int MODE = SOUT_LOGGER;
    /**
     * Variables
     */
    public static final long serialVersionUID = -4943851090318045466L;
    /**
     *
     */
    public boolean also_write_to_command_line = true;
    int counter = 0;
    int max_size = -1;

    /**
     * Constructor
     *
     * @param also_write_to_command_line if true then error is also written to
     * system.in
     * @param max_size the max number of characters before erase (-1 --> never
     * erase)
     * @param txtColor the color of the text
     */
    public LogFrame(int MODE, boolean also_write_to_command_line, int max_size, int txtColor) {
        this.MODE = MODE;
        initComponents();
        this.setSize(860, 512);
        redirectStream();
        this.setFocusable(false);
        jTextArea1.setForeground(new Color(txtColor));

    }

    /**
     * Ads a string to the textarea
     *
     * @param error the error string
     */
    public void append(String text) {
        counter++;
        if (max_size > 0) {
            if (counter > max_size) {
                jTextArea1.setText("");
                counter = 0;
            }
        }
        if (also_write_to_command_line && MODE == ERROR_LOGGER) {
            System.out.print(text);
        }
        if (CommonClassesLight.isErrorLoggerActivated() && !isVisible()) {
            this.setVisible(true);
        }
        jTextArea1.append(text);
        jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    public static void fakeLog() {
        System.out.println("fake log");
    }

    /**
     * creates a fake error to test the function
     */
    public static void fakeError() {
        System.err.println("fake error");
    }

    private void redirectStream() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                append(String.valueOf((char) b));
            }
        };
        switch (MODE) {
            case ERROR_LOGGER:
                setTitle("Error Logger");
                System.setErr(new PrintStream(out, true));
                break;
            case SOUT_LOGGER:
                setTitle("Logger");
                System.setOut(new PrintStream(out, true));
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setTitle("Error Logger");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onQuit(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Empty");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                erase(evt);
            }
        });

        jButton1.setText("Copy to ClipBoard");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toClipBoard(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * the button empty has been pressed so we clear the textArea
     *
     * @param evt
     */
    private void erase(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_erase
        jTextArea1.setText("");
    }//GEN-LAST:event_erase

    /**
     * The user closed the window so we empty it
     *
     * @param evt
     */
    private void onQuit(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onQuit
        /*
         * We erase all previous errors because they did not interest the user
         */
        jTextArea1.setText("");
    }//GEN-LAST:event_onQuit

    private void toClipBoard(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toClipBoard
        CommonClassesLight.sendTextToClipboard(jTextArea1.getText());
    }//GEN-LAST:event_toClipBoard

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
                new LogFrame(ERROR_LOGGER, false, 10000, 0xFF0000).setVisible(true);
                LogFrame.fakeError();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}



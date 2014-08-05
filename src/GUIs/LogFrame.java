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
/*
 * ErrorFrame.java
 *
 * Created on 22 juin 2011, 11:58:01
 */
package GUIs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import javax.swing.SwingUtilities;

/**
 * ErrorFrame is a system.err logging dialog
 *
 * @author Benoit Aigouy
 */
public class LogFrame extends javax.swing.JFrame {

    /**
     * we backup serr and sout and we'll restore it in the end
     */
    PrintStream soutBckup = System.out;
    PrintStream serrBckup = System.err;

    OutputStream log;
    OutputStream out;
    static HashSet<String> lockers = new HashSet<String>();

    /**
     * Variables
     */
    public static final long serialVersionUID = -4943851090318045466L;
    /**
     *
     */
    int counter_err = 0;
    int counter_sout = 0;
    int max_size = -1;

    /**
     * Constructor
     *
     * @param max_size the max number of characters before erase (-1 --> never
     * erase)
     */
    public LogFrame(int max_size) {
        initComponents();
        //this.setSize(860, 512);
        redirectStream();
        this.setFocusable(false);
//        jTextArea1.setForeground(new Color(txtColor));
        setVisible(true);

    }

    public boolean isLocked() {
        return !lockers.isEmpty();
    }

    public void addLocker(String name) {
        lockers.add(name);
    }

    public void removeLocker(String name) {
        lockers.remove(name);
    }

    /**
     * Ads a string to the textarea
     *
     * @param text the error string
     */
    public void appendErr(String text) {
        counter_err++;
        if (max_size > 0) {
            if (counter_err > max_size) {
                jTextArea1.setText("");
                counter_err = 0;
            }
        }
        if (!isVisible()) {
            jTextArea1.setText("");
            jTextArea2.setText("");
            setVisible(true);
            toFront();
        }
        jTextArea1.append(text);
        jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }

    /**
     * Ads a string to the textarea
     *
     * @param text the error string
     */
    public void appendLog(String text) {
        counter_sout++;
        if (max_size > 0) {
            if (counter_sout > max_size) {
                jTextArea2.setText("");
                counter_sout = 0;
            }
        }
        if (!isVisible()) {
            jTextArea2.setText("");
            jTextArea1.setText("");
            setVisible(true);
            toFront();
        }
        jTextArea2.append(text);
        jTextArea2.setCaretPosition(jTextArea2.getText().length());
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
        log = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendLog(String.valueOf((char) b));
            }
        };
        out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendErr(String.valueOf((char) b));
            }
        };

//        switch (MODE) {
//            case DUAL_LOGGER:
        setTitle("Logger");
        System.setErr(new PrintStream(out, true));
        System.setOut(new PrintStream(log, true));
//                break;
//            case ERROR_LOGGER:
//                setTitle("Error Logger");
//                System.setErr(new PrintStream(out, true));
//                break;
//            case SOUT_LOGGER:
//                setTitle("Logger");
//                System.setOut(new PrintStream(out, true));
//                break;
//        }
    }

    public void restoreSystem() {
        System.setErr(serrBckup);
        System.setOut(soutBckup);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setTitle("Logger");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onQuit(evt);
            }
        });

        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setName("jTextArea2"); // NOI18N
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Error"));
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(255, 0, 0));
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        jTextArea2.setText("");
    }//GEN-LAST:event_onQuit

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
                new LogFrame(10000).setVisible(true);
                LogFrame.fakeError();
                LogFrame.fakeLog();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}

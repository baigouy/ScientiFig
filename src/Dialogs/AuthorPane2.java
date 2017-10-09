package Dialogs;

import javax.swing.JOptionPane;

/**
 * A panel that contains the licence and informations about the author and about
 * the software
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class AuthorPane2 extends javax.swing.JPanel {

    public static final long serialVersionUID = -159439366066913338L;

    /**
     * Creates new Author form that contains licenses, copyrights, mails, ...
     */
    public AuthorPane2(String Authr, String Copyrght, String SoftName, String Eml) {
        initComponents();
        Author.setText(Authr);
        Copyright.setText(Copyrght);
        Software_name.setText(SoftName);
        Email.setText(Eml);
        jTextArea1.setCaretPosition(0);
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @param args
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        AuthorPane2 iopane = new AuthorPane2("sdfsdf", "sdfsdf", "sdfsdf", "sfsdffsd");
        int result = JOptionPane.showOptionDialog(null, new Object[]{iopane}, "AuthorPane2", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Software_name = new javax.swing.JLabel();
        Copyright = new javax.swing.JLabel();
        Author = new javax.swing.JLabel();
        Email = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("About"));

        Software_name.setText("sqjdhqskdhqksjdhkqshdkqshdkqs");

        Copyright.setText("sqjdhqskdhqksjdhkqshdkqshdkqs");

        Author.setText("sqjdhqskdhqksjdhkqshdkqshdkqs");

        Email.setText("sqjdhqskdhqksjdhkqshdkqshdkqs");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Software_name)
                    .addComponent(Copyright)
                    .addComponent(Author)
                    .addComponent(Email))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(Software_name)
                .addGap(18, 18, 18)
                .addComponent(Copyright)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Author)
                .addGap(18, 18, 18)
                .addComponent(Email)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Licenses"));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("PLEASE READ CAREFULLY THE LICENSES BELOW, if you do not agree to the terms of the licenses, just delete ScientiFig from your computer.\n\nLicense ScientiFig (new BSD license)\n\nCopyright (C) 2012-2015 Benoit Aigouy \n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions are\nmet:\n\n(1) Redistributions of source code must retain the above copyright\nnotice, this list of conditions and the following disclaimer. \n\n(2) Redistributions in binary form must reproduce the above copyright\nnotice, this list of conditions and the following disclaimer in\nthe documentation and/or other materials provided with the\ndistribution.  \n    \n(3)The name of the author may not be used to\nendorse or promote products derived from this software without\nspecific prior written permission.\n\nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR \"AS IS\" AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\nWARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\nDISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,\nINDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\nSERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)\nHOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,\nSTRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING\nIN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE\nPOSSIBILITY OF SUCH DAMAGE.\n\nLicenses Third party libraries:\n\nSF is using the public domain software ImageJ for reading tiffs and can exchange data with ImageJ. ImageJ is a great software\nmany thanks to all the ImageJ authors and contributors.\n\nlicense BATIK  and XML-apis\n/*\n * The Apache Software License, Version 1.1\n *\n * Copyright (c) 1999 The Apache Software Foundation.  All rights \n * reserved.\n *\n * Redistribution and use in source and binary forms, with or without\n * modification, are permitted provided that the following conditions\n * are met:\n *\n * 1. Redistributions of source code must retain the above copyright\n *    notice, this list of conditions and the following disclaimer. \n *\n * 2. Redistributions in binary form must reproduce the above copyright\n *    notice, this list of conditions and the following disclaimer in\n *    the documentation and/or other materials provided with the\n *    distribution.\n *\n * 3. The end-user documentation included with the redistribution, if\n *    any, must include the following acknowlegement:  \n *       \"This product includes software developed by the \n *        Apache Software Foundation (http://www.apache.org/).\"\n *    Alternately, this acknowlegement may appear in the software itself,\n *    if and wherever such third-party acknowlegements normally appear.\n *\n * 4. The names \"The Jakarta Project\", \"Jakarta-Regexp\", and \"Apache Software\n *    Foundation\" must not be used to endorse or promote products derived\n *    from this software without prior written permission. For written \n *    permission, please contact apache@apache.org.\n *\n * 5. Products derived from this software may not be called \"Apache\"\n *    nor may \"Apache\" appear in their names without prior written\n *    permission of the Apache Group.\n *\n * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED\n * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR\n * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,\n * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT\n * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF\n * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND\n * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT\n * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF\n * SUCH DAMAGE.\n * ====================================================================\n *\n * This software consists of voluntary contributions made by many\n * individuals on behalf of the Apache Software Foundation.  For more\n * information on the Apache Software Foundation, please see\n * <http://www.apache.org/>.\n *\n */ \n \nLicense Rsession\nhttp://code.google.com/p/rsession/\nhttps://sites.google.com/site/mulabsltd/products/rsession\nBSD License\nCopyright (c) 2012, µ-Labs\nAll rights reserved.\n\nRedistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n\nRedistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\nRedistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\nNeither the name of the µ-Labs nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.\nTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n\n\nLicense icons\n\nI would really like to thank:\n\nSekkyumu:\nhttp://sekkyumu.deviantart.com/art/Developpers-Icons-63052312\nYou are free to use these icons on personal and/or commercial projects \nwithout any attribution or credit. You are not permitted to sell any of these \nicons individually or as an icon pack. However, you are permitted to sell a \nproduct which uses any of these icons as part of the product. Do not make the \nicon set available for download on any other icon/design site - always link to \nthis DeviantArt page.\n\nicojoy (onebit icons)\nhttp://www.icojoy.com\n\nJ O N A S R A S K D E S I G N\nThese icons are free to use in both commercial products as well as personal use.\nhttp://jonasraskdesign.com\njonasrask@gmail.com\n\niTweek\nhttp://itweek.deviantart.com/art/Knob-Buttons-Toolbar-icons-73463960\nYou are free to use these icons on your software application, website, etc.\nYou're welcome to give credits for the graphics, when used.\n\nlogo SF:\nhttp://icon-generator.net/faq.html\nIs it free?\nYes, you can use free on personal and commercial perpose.\n\nfor their marvelous free icon sets."); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Author;
    private javax.swing.JLabel Copyright;
    private javax.swing.JLabel Email;
    private javax.swing.JLabel Software_name;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}


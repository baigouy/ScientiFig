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
package R.RSession;

import Commons.CommonClassesLight;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import org.rosuda.REngine.REXPMismatchException;

/**
 *
 * @author benoit
 */
public class MyRsessionLogger extends MyRsession {

    private int counter = 0;
    JTextArea textArea;

    /**
     * Creates new form myRsessionLogger
     */
    public MyRsessionLogger() {
        super();
        CommonClassesLight.r = this;
    }

    public MyRsessionLogger(JTextArea textArea) throws REXPMismatchException {
        this();
        this.textArea = textArea;
        reopenConnection();
    }
    private OutputStream out = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            appendSystem(String.valueOf((char) b));
        }
    };

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void setLogger(JTextArea textArea) {
        setTextArea(textArea);
    }

    /**
     * we redirect system stream
     */
    public void appendSystem(String error) {
        if (textArea == null || !textArea.isShowing()) {
            System.out.print(error);
        } else {
            counter++;
            if (counter % 10000 == 0) {
                textArea.setText("");
                counter = 0;
            }
            textArea.append(error);
            textArea.setCaretPosition(textArea.getText().length());
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            out.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void reopenConnection() throws REXPMismatchException {
        in = new PrintStream(out);
        super.reopenConnection();
    }
}


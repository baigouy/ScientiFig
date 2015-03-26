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
package Commons;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * LimitedTextField is a mutant PlainDocument that only allows a limited number
 * of characters to be written to it
 *
 * @since <B>FiguR 0.5</B>
 * @author Benoit Aigouy
 */
public class LimitedTextField extends PlainDocument {

    /**
     * Variables
     */
    private int MAX_SIZE = ONE_LETTER;
    /**
     * set MAX_SIZE to ONE_LETTER to have a single letter TextField
     */
    public static final int ONE_LETTER = 1;

    /**
     * Constructor
     *
     * @param max_size max number of characters allowed
     */
    public LimitedTextField(int max_size) {
        super();
        this.MAX_SIZE = max_size;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= MAX_SIZE) {
            /*
             * while text size < limit --> we append the text
             */
            super.insertString(offset, str, attr);
        } else {
            /*
             * we erase the text
             */
            super.remove(0, super.getLength());
            /*
             * we only keep text of the desired size
             */
            super.insertString(0, str.substring(str.length() - MAX_SIZE), attr);
        }
    }
}



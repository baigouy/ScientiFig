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
package R.Maths;

import java.io.Serializable;

/**
 * RMathElement does...
 *
 * @author Benoit Aigouy
 */
public class RMathElement implements Serializable {

    public static final long serialVersionUID = -1602592266906767617L;
    String[] operationAndParameters;
    String finalFormula;

    public RMathElement(String... operationAndParameters) {
        this.operationAndParameters = operationAndParameters;
        update();
    }

    public RMathElement() {
    }

    public void reset() {
        operationAndParameters = null;
        finalFormula = null;//should I put it to null or to "" instead ???
    }

    public void create(String... operationAndParameters) {
        reset();
        this.operationAndParameters = operationAndParameters;
        update();
    }

    public boolean isNull() {
        return isEmpty();
    }

    public boolean isEmpty() {
        if (operationAndParameters == null || operationAndParameters.length == 0) {
            return true;
        }
        return false;
    }

    public void update() {
        if (!isEmpty()) {
            finalFormula = RMaths.interprete(operationAndParameters);
        }
    }

    public int size() {
        return operationAndParameters == null ? 0 : operationAndParameters.length;
    }

    @Override
    public String toString() {
        if (finalFormula == null) {
            update();
        }
        return finalFormula;
    }

    public boolean isItalic() {
        String txt = toString();
        if (txt == null) {
            return false;
        } else {
            /*
             * also works for bold italic
             */
            if (txt.contains("italic(")) {
                return true;
            }
        }
        return false;
    }

    public boolean isBold() {
        String txt = toString();
        if (txt == null) {
            return false;
        } else {
            if (txt.contains("bold(") || txt.contains("bolditalic(")) {
                return true;
            }
        }
        return false;
    }

    public boolean isRawR() {
        if (isEmpty()) {
            return false;
        }
        if (operationAndParameters[0].contains("raw")) {
            return true;
        }
        return false;
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        RMathElement test = new RMathElement();
        test.create("greek", "y=alphax+beta");
        System.out.println(test);
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


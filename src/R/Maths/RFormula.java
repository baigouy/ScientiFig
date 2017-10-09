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
package R.Maths;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RMathsCombinationOfElements contains several R Maths elements
 *
 * @author Benoit Aigouy
 */
public class RFormula extends RMathElement implements Serializable {

    public static final long serialVersionUID = -1602533266906763316L;
    ArrayList<RMathElement> formulaElements = new ArrayList<RMathElement>();
    String RFormattedFormula; //think about null or ""

    public RFormula() {
    }

    public RFormula(Object... elmts) {
        for (Object rMathElement : elmts) {
            if (rMathElement instanceof RMathElement) {
                add((RMathElement) rMathElement);
            }
        }
        update();
    }

    public RFormula(RMathElement... elmts) {
        for (RMathElement rMathElement : elmts) {
            add(rMathElement);
        }
        update();
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    public final void add(RMathElement... elmts) {
        for (RMathElement rMathElement : elmts) {
            formulaElements.add(rMathElement);
        }
        update();
    }

    public final void add(RMathElement rme) {
        if (rme != null && !rme.isNull()) {
            formulaElements.add(rme);
            update();
        }
    }

    public void remove(RMathElement rme) {
        formulaElements.remove(rme);
        update();
    }

    @Override
    public int size() {
        return formulaElements.size();
    }

    @Override
    public boolean isEmpty() {
        return formulaElements.isEmpty();
    }

    public void clear() {
        formulaElements.clear();
    }

    public ArrayList<RMathElement> getElements() {
        return formulaElements;
    }

    public void setElements(ArrayList<RMathElement> formulaElements) {
        this.formulaElements = formulaElements;
        update();
    }

    @Override
    public void update() {
        RFormattedFormula = "paste(";
        for (RMathElement rme : formulaElements) {
            RFormattedFormula += rme.toString() + ", ";
        }
        if (RFormattedFormula.endsWith(", ")) {
            RFormattedFormula = RFormattedFormula.substring(0, RFormattedFormula.length() - 2);
        }
        RFormattedFormula += ")";
    }

    //combine all the elements to create an R executable string
    @Override
    public String toString() {
        return RFormattedFormula;// == null ? "" : RFormattedFormula
    }

    public String toExpression() {
        return toRExpression();
    }

    //do I need to add controls ????
    public String toRExpression() {
        return "expression(" + toString() + ")";
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        /*
         * we create some math elements
         */
        RMathElement rme = new RMathElement("greek", "y=alphax+beta");
        RMathElement rme2 = new RMathElement("greek", "this is a test");
        RMathElement rme3 = new RMathElement("greek", "this is a test2");
        RMathElement rme4 = new RMathElement();//test with a null maths

        RFormula test = new RFormula(rme, rme2);
        test.add(rme3);
        test.add(rme4);
        System.out.println(test);

        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


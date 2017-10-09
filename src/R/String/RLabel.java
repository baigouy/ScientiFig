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
package R.String;

import MyShapes.ColoredTextPaneSerializable;
import R.Maths.RFormula;
import R.Maths.RMaths;
import R.StyledDoc2R;
import java.io.Serializable;

/**
 * RLabel is a x or y axis label for a R graph
 *
 * @author Benoit Aigouy
 */
public class RLabel implements Serializable {

    public static final long serialVersionUID = 5208062996960338016L;
    ColoredTextPaneSerializable mainText;
    ColoredTextPaneSerializable unit;
    RFormula formula;
    boolean autoBracket = true;
    String openingBracket = "(";
    String closingBracket = ")";

    public RLabel() {
    }

    public RLabel(String mainText) {
        this.mainText = new ColoredTextPaneSerializable(mainText);
    }

    public RLabel(ColoredTextPaneSerializable mainText, ColoredTextPaneSerializable unit, RFormula formula) {
        this.formula = formula;
        this.mainText = mainText;
        this.unit = unit;
    }

    public void setBrackets(String openingBracket, String closingBracket) {
        this.openingBracket = openingBracket;
        this.closingBracket = closingBracket;
        if (this.openingBracket == null) {
            this.openingBracket = "";
        }
        if (this.closingBracket == null) {
            this.closingBracket = "";
        }
    }

    public String getOpeningBracket() {
        return openingBracket;
    }

    public void setOpeningBracket(String openingBracket) {
        this.openingBracket = openingBracket;
    }

    public String getClosingBracket() {
        return closingBracket;
    }

    public void setClosingBracket(String closingBracket) {
        this.closingBracket = closingBracket;
    }

    public boolean isAutoBracket() {
        return autoBracket;
    }

    public void setAutoBracket(boolean autoBracket) {
        this.autoBracket = autoBracket;
    }

    public boolean hasUnit() {
        if (unit == null) {
            return false;
        }
        if (getUnitAsString().equals("")) {
            return false;
        }
        return true;
    }

    public boolean hasFormula() {
        if (formula == null) {
            return false;
        }
        if (formula.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean hasMainText() {
        if (mainText == null) {
            return false;
        }
        if (getMainTextAsString().equals("")) {
            return false;
        }
        return true;
    }

    public String getMainTextAsString() {
        if (mainText == null) {
            return "";
        }
        return new StyledDoc2R().getText(mainText.getDoc());
    }

    public String getUnitAsString() {
        if (unit == null) {
            return "";
        }
        return new StyledDoc2R().getText(unit.getDoc());
    }

    public String getFormulaAsString() {
        if (formula == null) {
            return "";
        }
        return formula.toString();
    }

    public ColoredTextPaneSerializable getMainText() {
        return mainText;
    }

    public void setMainText(ColoredTextPaneSerializable mainText) {
        this.mainText = mainText;
    }

    public ColoredTextPaneSerializable getUnit() {
        return unit;
    }

    public void setUnit(ColoredTextPaneSerializable unit) {
        this.unit = unit;
    }

    public RFormula getFormula() {
        return formula;
    }

    public void setFormula(RFormula formula) {
        this.formula = formula;
    }

    @Override
    public String toString() {
        String text = mainText != null ? new StyledDoc2R().convertStyledDocToR(mainText.getDoc()) : null;
        String formulaTxt = formula != null ? formula.toString() : null;
        String unitTxt = unit != null ? new StyledDoc2R().convertStyledDocToR(unit.getDoc()) : null;
        String openBracket = (unitTxt == null || unitTxt.equals("")) || !autoBracket ? null : openingBracket;
        String closeBracket = (unitTxt == null || unitTxt.equals("")) || !autoBracket ? null : closingBracket;
        String out = RMaths.CONCATENATE(false, text, formulaTxt != null && !formulaTxt.equals("") ? "' '" : null, formulaTxt, unitTxt != null && !unitTxt.equals("") ? "' '" : null, openBracket, unitTxt, closeBracket);
        return out;
    }

    public String toExpression() {
        return toRExpression();
    }

    public String toRExpression() {
        String txt = toString();
        if (txt.equals("")) {
            return "";
        }
        return "expression(" + txt + ")";
    }

    public void recreateStyledDoc() {
        if (mainText != null) {
            if (mainText.doc == null) {
                mainText.recreateStyledDoc();
            }
        }
        if (unit != null) {
            if (unit.doc == null) {
                unit.recreateStyledDoc();
            }
        }
    }

    public void getTextreadyForSerialization() {
        if (mainText != null) {
            mainText.getReadyForSerialization();
        }
        if (unit != null) {
            unit.getReadyForSerialization();
        }
    }

    /**
     * The main function is used to test the class and its methods
     *
     * @author Benoit Aigouy
     */
    public static void main(String args[]) {
        long start_time = System.currentTimeMillis();
        
        RLabel test = new RLabel(new ColoredTextPaneSerializable("test"), new ColoredTextPaneSerializable("a.u."), null);
        //test.setAutoBracket(false);
        System.out.println(test);
        
        System.out.println("ellapsed time --> " + (System.currentTimeMillis() - start_time) / 1000.0 + "s");
        System.exit(0);
    }
}


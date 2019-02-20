package view.util;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumericTextField extends JTextField {
    //Add other constructors as required. If you do,
    //be sure to call the "addFilter" method

    private boolean aceptaPunto;
    private boolean aceptaNegativo;
    private int cantMax;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public NumericTextField(String text, int columns) {
        super(text, columns);
        addFilter();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public NumericTextField(boolean punto) {
        addFilter();
        this.aceptaPunto = punto;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public NumericTextField(boolean punto, boolean negativo) {
        addFilter();
        this.aceptaPunto = punto;
        this.aceptaNegativo = negativo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public NumericTextField(boolean punto, boolean negativo, int cantMax) {
        addFilter();
        this.aceptaPunto = punto;
        this.aceptaNegativo = negativo;
        this.cantMax = cantMax;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public NumericTextField() {
        addFilter();
        this.aceptaPunto = false;
        this.aceptaNegativo = false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setAceptaPunto(boolean punto) {
        this.aceptaPunto = punto;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setAceptaNegativo(boolean negativo) {
        this.aceptaNegativo = negativo;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Add an instance of NumericDocumentFilter as a
    //document filter to the current text field
    private void addFilter() {
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    class NumericDocumentFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb,
                                 int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) {
                return;
            }
            if (isStringNumeric(string, offset)) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void replace(FilterBypass fb, int offset,
                            int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                return;
            }
            if (isStringNumeric(text, offset)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private boolean isStringNumeric(String string, int cantCaracteresIngresados) {
            char[] characters = string.toCharArray();
            for (char c : characters) {
                if (!Character.isDigit(c)) {
                    if ((aceptaPunto && c == '.') || (aceptaNegativo && c == '-')) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    //que sea distinto de cero implaca que se ha seteado un valor para la cantidad maxima de caracteres
                    if (cantMax > 0) {
                        if ((cantCaracteresIngresados + 1 > cantMax)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
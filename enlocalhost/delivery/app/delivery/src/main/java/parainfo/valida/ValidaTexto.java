package parainfo.valida;

import java.util.regex.PatternSyntaxException;

public class ValidaTexto {

    public static boolean conSpace(String texto) { // carateres, espacio y acentos
        boolean ok = false;
        
        String expreg = "^([a-zA-Z\\sÁÉÍÓÚáéíóúÑñÜü]+)$";
        
        try {
            ok = texto.matches(expreg);

        } catch (PatternSyntaxException e) {
        }
        
        return ok; // true
    }
    
    public static boolean conDigitos(String texto) { // carateres, acentos y dígitos
        boolean ok = false;
        
        String expreg = "^([a-zA-Z0-9ÁÉÍÓÚáéíóúÑñÜü]+)$";
        
        try {
            ok = texto.matches(expreg);

        } catch (PatternSyntaxException e) {
        }
        
        return ok; // true
    }
    
    public static boolean conSpaceDigitos(String texto) { // carateres, acentos y dígitos
        boolean ok = false;
        
        String expreg = "^([a-zA-Z0-9\\sÁÉÍÓÚáéíóúÑñÜü]+)$";
        
        try {
            ok = texto.matches(expreg);

        } catch (PatternSyntaxException e) {
        }
        
        return ok; // true
    }
    
    public static boolean conSpaceDigitosMas(String texto) { // carateres, acentos, dígitos y .%-_/
        boolean ok = false;
        
        String expreg = "^([a-zA-Z0-9\\sÁÉÍÓÚáéíóúÑñÜü.\\%\\-\\_\\/]+)$";
        
        try {
            ok = texto.matches(expreg);

        } catch (PatternSyntaxException e) {
        }
        
        return ok; // true
    }
}

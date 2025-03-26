import java.util.*;

public class ReemplazoPaginas {
    public static int seleccionarPagina(Map<Integer, Boolean> referenciado, Map<Integer, Boolean> modificado) {
        for (var entrada : referenciado.entrySet()) {
            if (entrada.getValue() == false && modificado.getOrDefault(entrada.getKey(), false) == false) {
                return entrada.getKey();
            }
        }
        for (var entrada : referenciado.entrySet()) {
            if (entrada.getValue() == false) {
                return entrada.getKey();
            }
        }
        return referenciado.keySet().iterator().next();
    }
}

import java.util.*;

public class TablaPaginas {
    private final int numMarcos;
    private final Map<Integer, Integer> mapaPaginas;
    private final Queue<Integer> colaMarcos;
    private final Map<Integer, Boolean> referenciado;
    private final Map<Integer, Boolean> modificado;

    public TablaPaginas(int numMarcos) {
        this.numMarcos = numMarcos;
        this.mapaPaginas = new HashMap<>();
        this.colaMarcos = new LinkedList<>();
        this.referenciado = new HashMap<>();
        this.modificado = new HashMap<>();
    }

    public synchronized boolean accederPagina(int pagina, boolean escritura) {
        if (mapaPaginas.containsKey(pagina)) {
            referenciado.put(pagina, true);
            if (escritura) modificado.put(pagina, true);
            return true;
        }
        return false;
    }

    public synchronized void reemplazarPagina(int pagina) {
        if (mapaPaginas.size() >= numMarcos) {
            int paginaVictima = ReemplazoPaginas.seleccionarPagina(referenciado, modificado);
            mapaPaginas.remove(paginaVictima);
            referenciado.remove(paginaVictima);
            modificado.remove(paginaVictima);
        }
        colaMarcos.add(pagina);
        mapaPaginas.put(pagina, colaMarcos.size() - 1);
        referenciado.put(pagina, false);
        modificado.put(pagina, false);
    }
}

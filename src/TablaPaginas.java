import java.util.HashMap;
import java.util.Map;

public class TablaPaginas {

    Map<Integer, EntradaPagina> tablaPaginas;

    public TablaPaginas() {
        tablaPaginas = new HashMap<>();
    }

    //Añadir las paginas con sus entradas a la tabla de páginas
    public void agregarEntrada(int numPag) {
        EntradaPagina entrada = new EntradaPagina();
        tablaPaginas.put(numPag, entrada);
    }

    //Saber si una página está en memoria RAM
    public boolean getEntradaPresente(int numPag) {
        return tablaPaginas.get(numPag).presente;
    }

    //Eliminar marco de página
    public synchronized int eliminarPágina() {
        int numeroMarco = 0;
        return numeroMarco;
    }

    //Asociar página a marco de página
    public synchronized void asociarMarco(int numPag, int numMarco) {
        tablaPaginas.get(numPag).numeroMarco = numMarco;
        tablaPaginas.get(numPag).presente = true;
    }

}

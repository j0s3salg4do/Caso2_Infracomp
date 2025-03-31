import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TablaPaginas {
    Map<Integer, EntradaPagina> tablaPaginas;

    public TablaPaginas() {
        tablaPaginas = new HashMap<>();
    }

    // Añadir las páginas con sus entradas a la tabla de páginas
    public synchronized void agregarEntrada(int numPag) {
        EntradaPagina entrada = new EntradaPagina();
        tablaPaginas.put(numPag, entrada);
    }

    // Saber si una página está en memoria RAM
    public boolean getEntradaPresente(int numPag) {
        return tablaPaginas.get(numPag).presente;
    }

    // Actualizar los bits de referencia periódicamente
    public synchronized void actualizarBitsReferencia() {
        for (EntradaPagina entrada : tablaPaginas.values()) {
            if (entrada.presente) {
                entrada.referencia = false; // Resetear el bit de referencia
            }
        }
    }

    // Eliminar una página usando el algoritmo NRU
    public synchronized int eliminarPágina() {
        List<Integer> clase0 = new ArrayList<>(); // R=0, M=0
        List<Integer> clase1 = new ArrayList<>(); // R=0, M=1
        List<Integer> clase2 = new ArrayList<>(); // R=1, M=0
        List<Integer> clase3 = new ArrayList<>(); // R=1, M=1

        // Clasificar las páginas
        for (Map.Entry<Integer, EntradaPagina> entry : tablaPaginas.entrySet()) {
            if (entry.getValue().presente) {
                int numPag = entry.getKey();
                boolean R = entry.getValue().referencia;
                boolean M = entry.getValue().modificada; // Asumimos que hay un bit de modificación en EntradaPagina

                if (!R && !M) clase0.add(numPag);
                else if (!R && M) clase1.add(numPag);
                else if (R && !M) clase2.add(numPag);
                else if (R && M) clase3.add(numPag);
            }
        }

        // Seleccionar una página de la clase más baja no vacía
        List<Integer> candidatas = new ArrayList<>();
        if (!clase0.isEmpty()) candidatas = clase0;
        else if (!clase1.isEmpty()) candidatas = clase1;
        else if (!clase2.isEmpty()) candidatas = clase2;
        else if (!clase3.isEmpty()) candidatas = clase3;

        if (candidatas.isEmpty()) {
            return -1; // No hay páginas para reemplazar (no debería ocurrir)
        }

        // Seleccionar una página aleatoria de la clase candidata
        Random rand = new Random();
        int numPag = candidatas.get(rand.nextInt(candidatas.size()));
        tablaPaginas.get(numPag).presente = false;
        return tablaPaginas.get(numPag).numeroMarco;
    }

    // Asociar página a marco de página
    public synchronized void asociarMarco(int numPag, int numMarco) {
        tablaPaginas.get(numPag).numeroMarco = numMarco;
        tablaPaginas.get(numPag).presente = true;
        tablaPaginas.get(numPag).referencia = true; // Marcar como referenciada
    }
}
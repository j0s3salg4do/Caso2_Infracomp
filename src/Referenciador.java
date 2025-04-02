import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Referenciador extends Thread {

    private int numMarcos;
    private String archivoReferencias;

      // Lista completa de referencias
    private List<Referencia> referencias = new ArrayList<>();

    // Estructuras de paginación
    private TablaPaginas tablaPaginas; // tabla de páginas
    private Queue<Integer> marcosLibres = new LinkedList<>();           // marcos disponibles
    private Map<Integer, Integer> paginaEnMarco = new HashMap<>();      // marco -> numPag

    // Estadísticas
    private int hits = 0;
    private int fallas = 0;

    // Tiempos (en nanosegundos, para una medición unificada)
    private long tiempoTotalNs = 0; 

    public Referenciador(int numMarcos, String archivoReferencias, TablaPaginas tablaPaginas) {
        this.tablaPaginas = tablaPaginas;
        this.numMarcos = numMarcos;
        this.archivoReferencias = archivoReferencias;

        // Inicializamos la lista de marcos libres
        for (int i = 0; i < numMarcos; i++) {
            marcosLibres.add(i);
        }
    }

    private void leerArchivoReferencias() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoReferencias))) {
            String linea;
            boolean saltarCabecera = true;
            while ((linea = br.readLine()) != null) {
                // Saltamos cabecera hasta encontrar "REFERENCIAS:" 
                if (saltarCabecera) {
                    if (linea.startsWith("REFERENCIAS")) {
                        saltarCabecera = false;
                    }
                    continue;
                }
                // Ya leyendo las referencias
                Referencia referencia = new Referencia(linea);
                referencias.add(referencia);
                tablaPaginas.agregarEntrada(referencia.pagina); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Leer el archivo de referencias
        leerArchivoReferencias();

        int procesadas = 0;
        for (Referencia ref : referencias) {
            if (tablaPaginas.getEntradaPresente(ref.pagina)) {
                // Si la página ya está en memoria, es un hit
                hits++;
                tiempoTotalNs += 50;
            } else {
                // Si no está en memoria, es un fallo
                fallas++;
                tiempoTotalNs += 10_000_000;
                // Verificar si hay marcos libres
                if (marcosLibres.isEmpty()) {
                    // Si no hay marcos libres, eliminar una página 
                    int numMarco = tablaPaginas.eliminarPágina();
                    tablaPaginas.asociarMarco(ref.pagina, numMarco);
                    paginaEnMarco.remove(numMarco); 
                    paginaEnMarco.put(numMarco, ref.pagina);

                } else {
                    // Si hay marcos libres, asignar uno nuevo
                    int numMarco = marcosLibres.poll();
                    tablaPaginas.asociarMarco(ref.pagina, numMarco);
                    paginaEnMarco.put(numMarco, ref.pagina);
    
                }
            }

            procesadas++;

            // Mostrar progreso cada 1000 referencias procesadas
            if (procesadas % 10000 == 0) {
                try {
                    Thread.sleep(1); 
                } catch (InterruptedException e) {
                    // si el hilo es interrumpido
                    break;
                }
            }

        }
        

    }
    public int getHits() {
        return hits;
    }

    public int getFallas() {
        return fallas;
    }

    public long getTiempoTotalNs() {
        return tiempoTotalNs;
    }

}

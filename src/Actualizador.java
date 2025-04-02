import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Actualizador extends Thread {
    private TablaPaginas tablaPaginas;
    private volatile boolean running = true;

    public Actualizador(TablaPaginas tablaPaginas) {
        this.tablaPaginas = tablaPaginas;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1); // Esperar 1 ms
                tablaPaginas.actualizarBitsReferencia(); // Actualizar bits de referencia
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    public void detener() {
        running = false;
    }
}
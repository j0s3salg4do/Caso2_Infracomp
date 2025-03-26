import java.util.*;

public class SimuladorMemoria {
    private final TablaPaginas tablaPaginas;
    private int aciertos = 0;
    private int fallos = 0;

    public SimuladorMemoria(int numMarcos) {
        this.tablaPaginas = new TablaPaginas(numMarcos);
    }

    public void procesarReferencias(List<int[]> referencias) {
        for (int[] ref : referencias) {
            int pagina = ref[0];
            boolean escritura = ref[1] == 1;

            if (tablaPaginas.accederPagina(pagina, escritura)) {
                aciertos++;
            } else {
                fallos++;
                tablaPaginas.reemplazarPagina(pagina);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void imprimirResultados() {
        System.out.println("Total de aciertos: " + aciertos);
        System.out.println("Total de fallos: " + fallos);
        long tiempoTotalMs = (aciertos * 50) / 1000000 + (fallos * 10);
        System.out.println("Tiempo total de acceso (ms): " + tiempoTotalMs);
    }
}

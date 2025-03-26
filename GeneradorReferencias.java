import java.io.*;
import java.util.*;

public class GeneradorReferencias {

    public static void generarReferencias(String archivoImagen, int tamPagina, String archivoSalida) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoSalida))) {
            int filas = 79, columnas = 119;
            int totalReferencias = filas * columnas * 6;
            int totalPaginas = (filas * columnas * 3) / tamPagina;

            escritor.write("TP=" + tamPagina + "\n");
            escritor.write("NF=" + filas + "\n");
            escritor.write("NC=" + columnas + "\n");
            escritor.write("NR=" + totalReferencias + "\n");
            escritor.write("NP=" + totalPaginas + "\n");

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    int pagina = (i * columnas + j) / tamPagina;
                    int desplazamiento = (i * columnas + j) % tamPagina;

                    escritor.write("Imagen[" + i + "][" + j + "].r," + pagina + "," + desplazamiento + ",R\n");
                    escritor.write("Imagen[" + i + "][" + j + "].g," + pagina + "," + (desplazamiento + 1) + ",R\n");
                    escritor.write("Imagen[" + i + "][" + j + "].b," + pagina + "," + (desplazamiento + 2) + ",R\n");
                }
            }
            System.out.println("Archivo de referencias generado: " + archivoSalida);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public static List<int[]> leerReferencias(String archivoNombre) {
        List<int[]> referencias = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoNombre))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith("TP=") || linea.startsWith("NF=") || linea.startsWith("NC=") ||
                    linea.startsWith("NR=") || linea.startsWith("NP=")) {
                    continue;
                }

                String[] partes = linea.split(",");
                if (partes.length < 4) continue;

                int pagina = Integer.parseInt(partes[1]);
                boolean escritura = partes[3].equals("W");
                referencias.add(new int[]{pagina, escritura ? 1 : 0});
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return referencias;
    }
}


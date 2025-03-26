import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Generar archivo de referencias");
            System.out.println("2. Simular paginación");
            System.out.println("3. Salir");
            System.out.print("Indica tu opción: ");
            int opcion = scanner.nextInt();

            if (opcion == 1) {
                System.out.print("Ingrese el nombre del archivo BMP: ");
                String archivoImagen = scanner.next();
                System.out.print("Ingrese el tamaño de página (bytes): ");
                int tamPagina = scanner.nextInt();
                String archivoSalida = "referencias.txt";
                GeneradorReferencias.generarReferencias(archivoImagen, tamPagina, archivoSalida);
            } 
            else if (opcion == 2) {
                System.out.print("Ingrese el número de marcos de página: ");
                int numMarcos = scanner.nextInt();
                String archivoReferencias = "referencias.txt";
                List<int[]> referencias = GeneradorReferencias.leerReferencias(archivoReferencias);
                SimuladorMemoria simulador = new SimuladorMemoria(numMarcos);
                simulador.procesarReferencias(referencias);
                simulador.imprimirResultados();
            } 
            else if (opcion == 3) {
                System.out.println("Saliendo del programa");
                break;
            } 
            else {
                System.out.println("La opcion deseada no existe.");
            }
        }

        scanner.close();
    }
}

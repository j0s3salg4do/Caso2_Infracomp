import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Menú:");
        System.out.println("1. Generar archivo de referencias (Opción 1)");
        System.out.println("2. Simular sistema de paginación (Opción 2)");
        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();
        sc.nextLine(); // consumir salto

        if (opcion == 1) {
            System.out.print("Ingrese tamaño de página en bytes: ");
            int tamPagina = sc.nextInt();
            sc.nextLine(); // consumir salto
            
            String ruta = "Datos/imagen.bmp";
            String nombreImagen = ruta;

            Imagen imagen = new Imagen(nombreImagen);
            Imagen imagenSalida = new Imagen(nombreImagen); 
            FiltroSobel filtro = new FiltroSobel(imagen, imagenSalida);
            filtro.generarReferencias(tamPagina, "referencias.txt");

        } else if (opcion == 2) {
            System.out.print("Ingrese número de marcos de página: ");
            int numMarcos = sc.nextInt();
            sc.nextLine(); // consumir salto
            String archivoReferencias = "referencias.txt";

            TablaPaginas tablaPaginas = new TablaPaginas();
            Referenciador referenciador = new Referenciador(numMarcos, archivoReferencias, tablaPaginas);
            Actualizador actualizador = new Actualizador(tablaPaginas);

            referenciador.start();
            actualizador.start();

            try {
                referenciador.join(); // Esperar a que termine el referenciador
                actualizador.detener(); // Detener el actualizador
                actualizador.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Mostrar resultados
            System.out.println("Resultados:");
            System.out.println("Hits: " + referenciador.getHits());
            System.out.println("Fallas: " + referenciador.getFallas());
            System.out.println("Tiempo total (ns): " + referenciador.getTiempoTotalNs());
            System.out.println("Porcentaje de hits: " + 
                (referenciador.getHits() * 100.0 / (referenciador.getHits() + referenciador.getFallas())) + "%");

        } else {
            System.out.println("Opción no válida.");
        }

    }
}
// Clase principal con menú de opciones 1 y 2
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
            
            String ruta = "C:/Users/Daniela/OneDrive - Universidad de los Andes/Universidad/Noveno Semestre/Infraestructura Computacional/Caso 2/Caso2/Datos/imagen.bmp";
            String nombreImagen = ruta;

            Imagen imagen = new Imagen(nombreImagen);
            Imagen imagenSalida = new Imagen(nombreImagen); // salida dummy
            FiltroSobel filtro = new FiltroSobel(imagen, imagenSalida);
            filtro.generarReferencias(tamPagina, "referencias.txt");

        } else if (opcion == 2) {
            System.out.print("Ingrese número de marcos de página: ");
            int numMarcos = sc.nextInt();
            sc.nextLine(); // consumir salto

            System.out.print("Ingrese ruta del archivo de referencias: ");
            String archivoReferencias = sc.nextLine();

        } else {
            System.out.println("Opción no válida.");
        }
    }
}



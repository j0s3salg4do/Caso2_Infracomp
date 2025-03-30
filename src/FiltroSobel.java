import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FiltroSobel { 
    Imagen imagenIn; 
    Imagen imagenOut; 
    
    FiltroSobel(Imagen imagenEntrada, Imagen imagenSalida){ 
     imagenIn = imagenEntrada; 
     imagenOut = imagenSalida; 
    } 
     
     // Sobel Kernels para detección de bordes 
       static final int[][] SOBEL_X = { 
           {-1, 0, 1}, 
           {-2, 0, 2}, 
           {-1, 0, 1} 
       }; 
    
       static final int[][] SOBEL_Y = { 
           {-1, -2, -1}, 
           { 0,  0,  0}, 
           { 1,  2,  1} 
            
       }; 
        
   /** 
    * Método para para aplicar el filtro de Sobel a una imagen BMP 
     * @pre la matriz imagenIn debe haber sido inicializada con una imagen 
     * @pos la mstriz imagenOut fue modificada aplicando el filtro Sobel  
     */ 
       public  void applySobel() { 
           // Recorrer la imagen aplicando los dos filtros de Sobel 
           for (int i = 1; i < imagenIn.alto - 1; i++) {   
               for (int j = 1; j < imagenIn.ancho - 1; j++) {   
                   int gradXRed = 0, gradXGreen = 0, gradXBlue = 0; 
                   int gradYRed = 0, gradYGreen = 0, gradYBlue = 0; 
    
                   // Aplicar las máscaras Sobel X y Y 
                   for (int ki = -1; ki <= 1; ki++) {  
                       for (int kj = -1; kj <= 1; kj++) {  
                           int red = imagenIn.imagen[i+ki][j+kj][0]; 
                           int green = imagenIn.imagen[i+ki][j+kj][1]; 
                           int blue = imagenIn.imagen[i+ki][j+kj][2]; 
    
                           gradXRed += red * SOBEL_X[ki + 1][kj + 1]; 
                           gradXGreen += green * SOBEL_X[ki + 1][kj + 1]; 
                           gradXBlue += blue * SOBEL_X[ki + 1][kj + 1]; 
    
                           gradYRed += red * SOBEL_Y[ki + 1][kj + 1]; 
                           gradYGreen += green * SOBEL_Y[ki + 1][kj + 1]; 
                           gradYBlue += blue * SOBEL_Y[ki + 1][kj + 1]; 
                       } 
                   } 
    
                   // Calcular la magnitud del gradiente 
                   int red = Math.min(Math.max((int)Math.sqrt(gradXRed * gradXRed+ 
                                                              gradYRed * gradYRed),0),255); 
                   int green = Math.min(Math.max((int)Math.sqrt(gradXGreen * gradXGreen +  
                                                              gradYGreen* gradYGreen),0),255); 
                   int blue = Math.min(Math.max((int) Math.sqrt(gradXBlue * gradXBlue +  
                                                                gradYBlue * gradYBlue),0),255); 
    
                   // Crear el nuevo valor RGB 
                   imagenOut.imagen[i][j][0] = (byte)red; 
                   imagenOut.imagen[i][j][1] = (byte)green; 
                   imagenOut.imagen[i][j][2] = (byte)blue;          
               } 
           } 
       }
       
       public void generarReferencias(int tamPagina, String archivoSalida) {
        List<String> referenciasTexto = new ArrayList<>();
    
        // Posiciones base en memoria virtual
        int base_imagen = 0;
        int base_filtroX = base_imagen + imagenIn.alto * imagenIn.ancho * 3;
        int base_filtroY = base_filtroX + 3 * 3 * 4;
        int base_respuesta = base_filtroY + 3 * 3 * 4;
    
        for (int i = 1; i < imagenIn.alto - 1; i++) {
            for (int j = 1; j < imagenIn.ancho - 1; j++) {
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        // Primero los accesos a imagen por canal
                        for (int c = 0; c < 3; c++) {
                            int offset = ((i + ki) * imagenIn.ancho + (j + kj)) * 3 + c;
                            int dirVirtual = base_imagen + offset;
                            int pagina = dirVirtual / tamPagina;
                            int despl = dirVirtual % tamPagina;
                            String canal = (c == 0) ? "r" : (c == 1) ? "g" : "b";
                            String etiqueta = "Imagen[" + (i + ki) + "][" + (j + kj) + "]." + canal;
                            referenciasTexto.add(etiqueta + "," + pagina + "," + despl + ",R");
                        }
    
                        // Luego 3 veces SOBEL_X[...] (una por canal)
                        int offsetFiltro = (ki + 1) * 3 + (kj + 1);
                        int dirFiltroX = base_filtroX + offsetFiltro * 4;
                        int paginaFX = dirFiltroX / tamPagina;
                        int desplFX = dirFiltroX % tamPagina;
                        String etiquetaFX = "SOBEL_X[" + (ki + 1) + "][" + (kj + 1) + "]";
                        for (int r = 0; r < 3; r++) {
                            referenciasTexto.add(etiquetaFX + "," + paginaFX + "," + desplFX + ",R");
                        }
    
                        // Luego 3 veces SOBEL_Y[...] (una por canal)
                        int dirFiltroY = base_filtroY + offsetFiltro * 4;
                        int paginaFY = dirFiltroY / tamPagina;
                        int desplFY = dirFiltroY % tamPagina;
                        String etiquetaFY = "SOBEL_Y[" + (ki + 1) + "][" + (kj + 1) + "]";
                        for (int r = 0; r < 3; r++) {
                            referenciasTexto.add(etiquetaFY + "," + paginaFY + "," + desplFY + ",R");
                        }
                    }
                }
    
                // Finalmente las escrituras a la imagen de salida (una por canal)
                for (int c = 0; c < 3; c++) {
                    int offset = (i * imagenIn.ancho + j) * 3 + c;
                    int dirVirtual = base_respuesta + offset;
                    int pagina = dirVirtual / tamPagina;
                    int despl = dirVirtual % tamPagina;
                    String canal = (c == 0) ? "r" : (c == 1) ? "g" : "b";
                    String etiqueta = "Rta[" + i + "][" + j + "]." + canal;
                    referenciasTexto.add(etiqueta + "," + pagina + "," + despl + ",W");
                }
            }
        }
    
        // Escribir el archivo de salida
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoSalida))) {
            pw.println("TP:" + tamPagina);
            pw.println("NF:" + imagenIn.alto);
            pw.println("NC:" + imagenIn.ancho);
            pw.println("NR:" + referenciasTexto.size());
    
            int totalBytes = base_respuesta + imagenIn.alto * imagenIn.ancho * 3;
            int np = (int) Math.ceil((double) totalBytes / tamPagina);
            pw.println("NP:" + np);
            pw.println("REFERENCIAS:");
    
            for (String linea : referenciasTexto) {
                pw.println(linea);
            }
    
            System.out.println("Archivo de referencias generado en " + archivoSalida);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     

   }

import java.io.*;

public class Imagen {
    byte[] cabecera = new byte[54];
    byte[][][] pixeles;
    int alto, ancho;
    int noContar;

    public Imagen(String nombre) {
        try {
            FileInputStream fis = new FileInputStream(nombre);
            fis.read(cabecera);
            
            ancho = ((cabecera[21] & 0xFF) << 24) | ((cabecera[20] & 0xFF) << 16) | 
                    ((cabecera[19] & 0xFF) << 8) | (cabecera[18] & 0xFF);
            alto = ((cabecera[25] & 0xFF) << 24) | ((cabecera[24] & 0xFF) << 16) | 
            ((cabecera[23] & 0xFF) << 8) | (cabecera[22] & 0xFF);
            
            System.out.println("Ancho: " + ancho + " px, Alto: " + alto + " px");
            pixeles = new byte[alto][ancho][3];
            
            int filaSinNoContar = ancho * 3;
            noContar = (4 - (filaSinNoContar % 4)) % 4;
            
            byte[] pixel = new byte[3];
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    fis.read(pixel);
                    pixeles[i][j][0] = pixel[0];
                    pixeles[i][j][1] = pixel[1];
                    pixeles[i][j][2] = pixel[2];
                }
                fis.skip(noContar);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribirImagen(String salida) {
        try {
            FileOutputStream fos = new FileOutputStream(salida);
            fos.write(cabecera);
            byte[] pixel = new byte[3];
            
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    pixel[0] = pixeles[i][j][0];
                    pixel[1] = pixeles[i][j][1];
                    pixel[2] = pixeles[i][j][2];
                    fos.write(pixel);
                }
                for (int k = 0; k < noContar; k++) fos.write(0);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
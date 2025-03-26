public class FiltroSobel {
    Imagen imagenIn;
    Imagen imagenOut;

    FiltroSobel(Imagen entrada, Imagen salida) {
        imagenIn = entrada;
        imagenOut = salida;
    }

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

    public void aplicarSobel() {
        for (int i = 1; i < imagenIn.alto - 1; i++) {
            for (int j = 1; j < imagenIn.ancho - 1; j++) {
                int gradXRojo = 0, gradXVerde = 0, gradXAzul = 0;
                int gradYRojo = 0, gradYVerde = 0, gradYAzul = 0;

                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        int rojo = imagenIn.pixeles[i+ki][j+kj][0];
                        int verde = imagenIn.pixeles[i+ki][j+kj][1];
                        int azul = imagenIn.pixeles[i+ki][j+kj][2];

                        gradXRojo += rojo * SOBEL_X[ki + 1][kj + 1];
                        gradXVerde += verde * SOBEL_X[ki + 1][kj + 1];
                        gradXAzul += azul * SOBEL_X[ki + 1][kj + 1];

                        gradYRojo += rojo * SOBEL_Y[ki + 1][kj + 1];
                        gradYVerde += verde * SOBEL_Y[ki + 1][kj + 1];
                        gradYAzul += azul * SOBEL_Y[ki + 1][kj + 1];
                    }
                }
                
                int rojo = Math.min(Math.max((int) Math.sqrt(gradXRojo * gradXRojo + gradYRojo * gradYRojo), 0), 255);
                int verde = Math.min(Math.max((int) Math.sqrt(gradXVerde * gradXVerde + gradYVerde * gradYVerde), 0), 255);
                int azul = Math.min(Math.max((int) Math.sqrt(gradXAzul * gradXAzul + gradYAzul * gradYAzul), 0), 255);

                imagenOut.pixeles[i][j][0] = (byte) rojo;
                imagenOut.pixeles[i][j][1] = (byte) verde;
                imagenOut.pixeles[i][j][2] = (byte) azul;
            }
        }
    }
}
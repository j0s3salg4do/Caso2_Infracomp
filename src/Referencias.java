class Referencia {
    String etiqueta;  
    int pagina;       
    int desplazamiento;
    char accion;      

    public Referencia(String linea) {

        String[] partes = linea.split(",");
        etiqueta = partes[0];
        pagina = Integer.parseInt(partes[1]);
        desplazamiento = Integer.parseInt(partes[2]);
        accion = partes[3].charAt(0);
    }
}

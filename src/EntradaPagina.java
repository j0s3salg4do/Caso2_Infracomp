class EntradaPagina {
    int numeroMarco;    
    boolean presente; 
    int contadorAging;  
    boolean referencia; 
    boolean modificada; // Nuevo campo para el bit de modificación

    public EntradaPagina() {
        this.numeroMarco = -1;
        this.presente = false;
        this.contadorAging = 0;
        this.referencia = false;
        this.modificada = false;
    }
}
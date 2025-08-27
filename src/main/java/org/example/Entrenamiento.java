package org.example;

public class Entrenamiento {
    private String fecha;
    private String tipo;
    private double marca;

    public Entrenamiento(String fecha, String tipo, double marca) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.marca = marca;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMarca() { return marca; }
    public void setMarca(double marca) { this.marca = marca; }

    @Override
    public String toString() {
        return String.format("%s | %s: %.2f", fecha, tipo, marca);
    }
}
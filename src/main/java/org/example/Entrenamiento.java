package org.example;

public class Entrenamiento {
    private String fecha;
    private String tipo;
    private double marca;
    private String ubicacion;
    private String pais;

    public Entrenamiento(String fecha, String tipo, double marca) {
        this(fecha, tipo, marca, "NACIONAL", "");
    }

    public Entrenamiento(String fecha, String tipo, double marca, String ubicacion, String pais) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.marca = marca;
        this.ubicacion = ubicacion != null ? ubicacion : "NACIONAL";
        this.pais = pais != null ? pais : "";
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMarca() { return marca; }
    public void setMarca(double marca) { this.marca = marca; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() {
        String lugar = "INTERNACIONAL".equalsIgnoreCase(ubicacion) ? ("Internacional - " + (pais.isBlank() ? "N/D" : pais)) : "Nacional";
        return String.format("%s | %s: %.2f | %s", fecha, tipo, marca, lugar);
    }
}
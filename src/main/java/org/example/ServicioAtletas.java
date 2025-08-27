package org.example;

import java.util.List;

public class ServicioAtletas {
    private List<Atleta> atletas;
    private RepositorioAtletas repositorio;

    public ServicioAtletas() {
        repositorio = new RepositorioAtletas();
        atletas = repositorio.cargar();
    }

    public void registrarAtleta(String nombre, int edad, String disciplina, String departamento) {
        Atleta atleta = new Atleta(nombre, edad, disciplina, departamento);
        atletas.add(atleta);
        System.out.println(" Atleta registrado: " + atleta);
    }

    public boolean registrarSesion(String nombreAtleta, String fecha, String tipo, double marca) {
        Atleta atleta = buscarAtleta(nombreAtleta);
        if (atleta != null) {
            Entrenamiento entrenamiento = new Entrenamiento(fecha, tipo, marca);
            atleta.agregarEntrenamiento(entrenamiento);
            System.out.println(" Sesión registrada para " + nombreAtleta);
            return true;
        }
        System.out.println(" Atleta no encontrado: " + nombreAtleta);
        return false;
    }

    public Atleta buscarAtleta(String nombre) {
        return atletas.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void mostrarHistorial(String nombreAtleta) {
        Atleta atleta = buscarAtleta(nombreAtleta);
        if (atleta != null) {
            EstadisticasAtleta stats = new EstadisticasAtleta(atleta);
            stats.mostrarEvolucion();
        } else {
            System.out.println(" Atleta no encontrado: " + nombreAtleta);
        }
    }

    public void listarAtletas() {
        if (atletas.isEmpty()) {
            System.out.println(" No hay atletas registrados");
            return;
        }
        System.out.println("\n=== ATLETAS REGISTRADOS ===");
        atletas.forEach(a -> System.out.println(" " + a));
    }

    public void guardarDatos() {
        repositorio.guardar(atletas);
    }

    public List<Atleta> getAtletas() {
        return atletas;
    }
}
package org.example;

import java.util.List;
import java.util.Comparator;

public class EstadisticasAtleta {
    private Atleta atleta;

    public EstadisticasAtleta(Atleta atleta) {
        this.atleta = atleta;
    }

    public double calcularPromedio() {
        List<Entrenamiento> entrenamientos = atleta.getEntrenamientos();
        if (entrenamientos.isEmpty()) return 0.0;

        return entrenamientos.stream()
                .mapToDouble(Entrenamiento::getMarca)
                .average()
                .orElse(0.0);
    }

    public double obtenerMejorMarca() {
        List<Entrenamiento> entrenamientos = atleta.getEntrenamientos();
        if (entrenamientos.isEmpty()) return 0.0;

        // Para disciplinas de tiempo (menor es mejor), cambiar a min()
        return entrenamientos.stream()
                .mapToDouble(Entrenamiento::getMarca)
                .max()
                .orElse(0.0);
    }

    public void mostrarEvolucion() {
        List<Entrenamiento> entrenamientos = atleta.getEntrenamientos();
        if (entrenamientos.isEmpty()) {
            System.out.println("No hay entrenamientos registrados.");
            return;
        }

        System.out.println("\n=== EVOLUCIÓN DE " + atleta.getNombre().toUpperCase() + " ===");
        entrenamientos.stream()
                .sorted(Comparator.comparing(Entrenamiento::getFecha))
                .forEach(e -> System.out.println(" " + e));

        System.out.printf("\n Promedio: %.2f\n", calcularPromedio());
        System.out.printf(" Mejor marca: %.2f\n", obtenerMejorMarca());
    }
}
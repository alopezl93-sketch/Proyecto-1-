package org.example;

import java.util.Comparator;
import java.util.List;

public class EstadisticasAtleta {
    private final Atleta atleta;

    public EstadisticasAtleta(Atleta atleta) { this.atleta = atleta; }

    public double calcularPromedio() {
        List<Entrenamiento> e = atleta.getEntrenamientos();
        if (e.isEmpty()) return 0.0;
        return e.stream().mapToDouble(Entrenamiento::getMarca).average().orElse(0.0);
    }

    public double obtenerMejorMarca() {
        List<Entrenamiento> e = atleta.getEntrenamientos();
        if (e.isEmpty()) return 0.0;
        return e.stream().mapToDouble(Entrenamiento::getMarca).max().orElse(0.0);
    }

    public double promedioNacional() {
        return atleta.getEntrenamientos().stream()
                .filter(en -> "NACIONAL".equalsIgnoreCase(en.getUbicacion()))
                .mapToDouble(Entrenamiento::getMarca).average().orElse(0.0);
    }

    public double promedioInternacional() {
        return atleta.getEntrenamientos().stream()
                .filter(en -> "INTERNACIONAL".equalsIgnoreCase(en.getUbicacion()))
                .mapToDouble(Entrenamiento::getMarca).average().orElse(0.0);
    }

    public void mostrarEvolucion() {
        List<Entrenamiento> e = atleta.getEntrenamientos();
        if (e.isEmpty()) {
            System.out.println("No hay entrenamientos registrados.");
            return;
        }
        System.out.println("\n=== EVOLUCIÓN DE " + atleta.getNombre().toUpperCase() + " ===");
        e.stream().sorted(Comparator.comparing(Entrenamiento::getFecha))
                .forEach(x -> System.out.println(" " + x));
        System.out.printf("\n Promedio general: %.2f\n", calcularPromedio());
        System.out.printf(" Mejor marca: %.2f\n", obtenerMejorMarca());
        System.out.printf(" Promedio nacional: %.2f | Promedio internacional: %.2f\n",
                promedioNacional(), promedioInternacional());
    }
}
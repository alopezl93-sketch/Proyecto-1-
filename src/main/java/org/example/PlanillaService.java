package org.example;

import java.util.List;

public class PlanillaService {

    // Procesa la planilla de todos los atletas
    public void procesar(List<Atleta> atletas) {
        if (atletas == null || atletas.isEmpty()) {
            System.out.println(" No hay atletas para procesar en la planilla.");
            return;
        }

        System.out.println(" Procesando planilla de " + atletas.size() + " atletas...");
        for (Atleta atleta : atletas) {
            double pago = calcularPago(atleta);
            System.out.println(" - Atleta: " + atleta.getNombre() + " | Pago: Q" + pago);
        }
        System.out.println(" Planilla procesada correctamente.");
    }

    // Ejemplo de cálculo de pago (ajústalo a tu lógica real)
    private double calcularPago(Atleta atleta) {
        // Por ejemplo: Q100 por cada entrenamiento registrado
        return atleta.getEntrenamientos().size() * 100.0;
    }
}
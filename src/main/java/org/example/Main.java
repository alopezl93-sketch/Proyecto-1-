package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("🇬🇹 Iniciando Sistema de Atletas de Guatemala...");

        PresentadorAtletas presentador = new PresentadorAtletas();

        // Hook para guardar datos al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            presentador.cerrar();
            System.out.println("\n Datos guardados automáticamente");
        }));

        presentador.mostrarMenu();
    }
}
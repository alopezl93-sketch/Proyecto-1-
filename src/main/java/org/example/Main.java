package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println(" Iniciando Sistema de Atletas de Guatemala...");
        System.out.println(" Versión con soporte MariaDB y JSON");

        // Crear presentador (por defecto usa MariaDB)
        PresentadorAtletas presentador = new PresentadorAtletas();

        // Hook para cerrar limpiamente al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n Cerrando sistema...");
            presentador.cerrar();
        }));

        try {
            // Mostrar menú principal
            presentador.mostrarMenu();
        } catch (Exception e) {
            System.err.println(" Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Asegurar cierre limpio
            presentador.cerrar();
        }
    }
}
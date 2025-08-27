package org.example;

import java.util.Scanner;

public class PresentadorAtletas {
    private Scanner scanner;
    private ServicioAtletas servicio;

    public PresentadorAtletas() {
        scanner = new Scanner(System.in);
        servicio = new ServicioAtletas();
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n === COMITÉ OLÍMPICO GUATEMALTECO ===");
            System.out.println("1. Registrar atleta");
            System.out.println("2. Registrar sesión de entrenamiento");
            System.out.println("3. Ver historial de atleta");
            System.out.println("4. Listar todos los atletas");
            System.out.println("5. Guardar datos");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    registrarAtleta();
                    break;
                case 2:
                    registrarSesion();
                    break;
                case 3:
                    verHistorial();
                    break;
                case 4:
                    servicio.listarAtletas();
                    break;
                case 5:
                    servicio.guardarDatos();
                    break;
                case 0:
                    System.out.println("🇬🇹 ¡Hasta luego!");
                    break;
                default:
                    System.out.println(" Opción inválida");
                    break;
            }
        } while (opcion != 0);
    }

    private void registrarAtleta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Disciplina: ");
        String disciplina = scanner.nextLine();
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();

        servicio.registrarAtleta(nombre, edad, disciplina, departamento);
    }

    private void registrarSesion() {
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        System.out.print("Tipo de entrenamiento: ");
        String tipo = scanner.nextLine();
        System.out.print("Marca/tiempo: ");
        double marca = scanner.nextDouble();
        scanner.nextLine();

        servicio.registrarSesion(nombre, fecha, tipo, marca);
    }

    private void verHistorial() {
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        servicio.mostrarHistorial(nombre);
    }

    public void cerrar() {
        servicio.guardarDatos();
        scanner.close();
    }
}
package org.example;

import java.time.LocalDate;
import java.util.Scanner;

public class PresentadorAtletas {
    private final Scanner scanner = new Scanner(System.in);
    private final ServicioAtletas servicio = new ServicioAtletas();

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n === COMITÉ OLÍMPICO GUATEMALTECO ===");
            System.out.println("1. Registrar atleta");
            System.out.println("2. Registrar sesión de entrenamiento");
            System.out.println("3. Ver historial de atleta");
            System.out.println("4. Listar todos los atletas");
            System.out.println("5. Ver estadísticas generales");
            System.out.println("6. Guardar datos");
            System.out.println("7. Configuración de base de datos");
            System.out.println("8. Sincronizar datos JSON ↔ MariaDB");
            System.out.println("9. Generar reportes CSV");
            System.out.println("10. Procesar pagos de planilla");
            System.out.println("11. Eliminar atleta");
            System.out.println("0. Salir");
            String modo = servicio.isUsandoBaseDatos() ? "MariaDB" : "JSON";
            System.out.println("   [Modo actual: " + modo + "]");

            System.out.print("Seleccione opción: ");
            String input = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> registrarAtleta();
                case 2 -> registrarSesion();
                case 3 -> verHistorial();
                case 4 -> servicio.listarAtletas();
                case 5 -> servicio.mostrarEstadisticas();
                case 6 -> servicio.guardarDatos();
                case 7 -> menuConfiguracion();
                case 8 -> sincronizarDatos();
                case 9 -> generarReportesCSV();
                case 10 -> servicio.procesarPlanilla();
                case 11 -> eliminarAtleta();
                case 0 -> System.out.println("🇬🇹 ¡Hasta luego!");
                default -> System.out.println("❌ Opción inválida");
            }
        } while (opcion != 0);

        cerrar(); // al salir del menú también se cierra
    }

    private void registrarAtleta() {
        System.out.println("\n=== REGISTRAR NUEVO ATLETA ===");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Disciplina: ");
        String disciplina = scanner.nextLine();
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine();
        System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
        String fiStr = scanner.nextLine().trim();
        LocalDate fechaIngreso = fiStr.isBlank() ? null : LocalDate.parse(fiStr);

        servicio.registrarAtleta(nombre, edad, disciplina, departamento, nacionalidad, fechaIngreso);
        System.out.println("✅ Atleta registrado correctamente.");
    }

    private void registrarSesion() {
        System.out.println("\n=== REGISTRAR SESIÓN DE ENTRENAMIENTO ===");
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        System.out.print("Tipo de entrenamiento: ");
        String tipo = scanner.nextLine();
        System.out.print("Valor de rendimiento: ");
        double marca = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Ubicación (NACIONAL/INTERNACIONAL): ");
        String ubicacion = scanner.nextLine().trim().toUpperCase();
        String pais = "";
        if ("INTERNACIONAL".equalsIgnoreCase(ubicacion)) {
            System.out.print("País: ");
            pais = scanner.nextLine();
        }

        servicio.registrarSesion(nombre, fecha, tipo, marca, ubicacion, pais);
    }

    private void verHistorial() {
        System.out.println("\n=== VER HISTORIAL DE ATLETA ===");
        System.out.print("Nombre del atleta: ");
        String nombre = scanner.nextLine();
        servicio.mostrarHistorial(nombre);
    }

    private void menuConfiguracion() {
        System.out.println("\n=== CONFIGURACIÓN DE BASE DE DATOS ===");
        System.out.println("1. Cambiar a MariaDB");
        System.out.println("2. Cambiar a archivos JSON");
        System.out.println("3. Probar conexión MariaDB");
        System.out.println("4. Volver");
        System.out.print("Seleccione opción: ");
        String input = scanner.nextLine().trim();
        int op;
        try {
            op = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Opción inválida");
            return;
        }
        switch (op) {
            case 1 -> {
                servicio.cambiarModoAlmacenamiento(true);
                System.out.println("✅ Cambiado a MariaDB");
            }
            case 2 -> {
                servicio.cambiarModoAlmacenamiento(false);
                System.out.println("✅ Cambiado a archivos JSON");
            }
            case 3 -> {
                if (ConexionBD.probarConexion()) System.out.println("✅ Conexión exitosa a MariaDB");
                else System.out.println("❌ No se pudo conectar a MariaDB");
            }
            case 4 -> { return; }
            default -> System.out.println("❌ Opción inválida");
        }
    }

    private void sincronizarDatos() {
        System.out.println("\n=== SINCRONIZACIÓN DE DATOS ===");
        if (!servicio.isUsandoBaseDatos()) {
            System.out.println("⚠️ Debe estar en modo MariaDB para sincronizar");
            return;
        }
        servicio.sincronizarDatos();
        System.out.println("✅ Sincronización completada");
    }

    private void generarReportesCSV() {
        System.out.println("\n=== REPORTES CSV ===");
        System.out.print("Ruta archivo atletas (ej. reportes/atletas.csv): ");
        String rutaA = scanner.nextLine();
        System.out.print("Nombre de atleta para entrenamientos (o vacío para saltar): ");
        String nombre = scanner.nextLine();
        servicio.generarCSV(rutaA, nombre);
        System.out.println("✅ Reportes generados");
    }

    private void eliminarAtleta() {
        System.out.println("\n=== ELIMINAR ATLETA ===");
        System.out.print("Nombre del atleta a eliminar: ");
        String nombre = scanner.nextLine();
        servicio.eliminarAtleta(nombre);
    }

    // ✅ Ahora es público para que Main pueda llamarlo
    public void cerrar() {
        System.out.println("💾 Guardando datos...");
        servicio.guardarDatos();
        servicio.cerrarConexiones();
        scanner.close();
        System.out.println("✅ Sistema cerrado correctamente");
    }
}
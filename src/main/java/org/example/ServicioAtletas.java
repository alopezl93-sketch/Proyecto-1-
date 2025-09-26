package org.example;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ServicioAtletas {
    private List<Atleta> atletas;
    private RepositorioAtletas repositorio;
    private final PlanillaService planillaService = new PlanillaService();

    public ServicioAtletas() { this(true); }

    public ServicioAtletas(boolean usarBaseDatos) {
        repositorio = new RepositorioAtletas(usarBaseDatos);
        atletas = repositorio.cargar();
    }

    // Registrar atleta
    public void registrarAtleta(String nombre, int edad, String disciplina, String departamento,
                                String nacionalidad, LocalDate fechaIngreso) {
        Atleta atleta = new Atleta(nombre, edad, disciplina, departamento, nacionalidad, fechaIngreso);
        atletas.add(atleta);
        if (repositorio.isUsandoBaseDatos()) {
            int id = repositorio.guardarAtleta(atleta);
            atleta.setId(id);
        }
    }

    // Registrar sesión de entrenamiento
    public void registrarSesion(String nombreAtleta, String fecha, String tipo, double marca,
                                String ubicacion, String pais) {
        Atleta atleta = buscarAtleta(nombreAtleta);
        if (atleta == null) {
            System.out.println(" No se encontró el atleta: " + nombreAtleta);
            return;
        }
        Entrenamiento e = new Entrenamiento(fecha, tipo, marca, ubicacion, pais);
        repositorio.registrarEntrenamiento(atleta, e);
        atleta.agregarEntrenamiento(e);
    }

    // Buscar atleta
    public Atleta buscarAtleta(String nombre) {
        return atletas.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    // Listar atletas
    public void listarAtletas() {
        if (atletas.isEmpty()) {
            System.out.println(" No hay atletas registrados.");
            return;
        }
        atletas.forEach(System.out::println);
    }

    // Estadísticas
    public void mostrarEstadisticas() {
        System.out.println(" Total atletas: " + atletas.size());
        long conEntrenamientos = atletas.stream().filter(a -> !a.getEntrenamientos().isEmpty()).count();
        System.out.println(" Atletas con entrenamientos registrados: " + conEntrenamientos);
    }

    // Guardar datos
    public void guardarDatos() {
        repositorio.guardar(atletas);
    }

    //  Eliminar atleta con borrado de CSV
    public void eliminarAtleta(String nombre) {
        boolean eliminado = repositorio.eliminarAtleta(nombre, atletas);
        if (eliminado) {
            // Borrar también el CSV de entrenamientos de ese atleta
            String rutaCSV = "entrenamientos_" + nombre + ".csv";
            borrarCSV(rutaCSV);

            try {
                CsvUtil.exportarAtletas("reportes/atletas.csv", atletas);
                System.out.println(" CSV actualizado sin el atleta eliminado.");
            } catch (Exception e) {
                System.err.println(" Error al actualizar CSV: " + e.getMessage());
            }
        }
    }

    //  Método auxiliar para borrar CSV
    private void borrarCSV(String ruta) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println(" Archivo eliminado: " + ruta);
            } else {
                System.out.println("️ No se pudo eliminar el archivo: " + ruta);
            }
        }
    }

    // Generar CSV
    public void generarCSV(String ruta, String nombreAtleta) {
        try {
            CsvUtil.exportarAtletas(ruta, atletas);
            if (nombreAtleta != null && !nombreAtleta.isBlank()) {
                Atleta a = buscarAtleta(nombreAtleta);
                if (a != null) {
                    CsvUtil.exportarEntrenamientos("entrenamientos_" + a.getNombre() + ".csv", a);
                }
            }
        } catch (Exception e) {
            System.err.println(" Error al exportar CSV: " + e.getMessage());
        }
    }

    // Saber si está en BD
    public boolean isUsandoBaseDatos() {
        return repositorio.isUsandoBaseDatos();
    }

    // Cambiar modo de almacenamiento
    public void cambiarModoAlmacenamiento(boolean usarBD) {
        repositorio.cambiarModo(usarBD);
        atletas = repositorio.cargar();
    }

    // Cerrar conexiones
    public void cerrarConexiones() {
        if (repositorio.isUsandoBaseDatos()) ConexionBD.cerrarDataSource();
    }

    //  Mostrar historial
    public void mostrarHistorial(String nombre) {
        Atleta atleta = buscarAtleta(nombre);
        if (atleta == null) {
            System.out.println(" No se encontró el atleta: " + nombre);
            return;
        }
        System.out.println("\n Historial de entrenamientos de " + nombre + ":");
        if (atleta.getEntrenamientos().isEmpty()) {
            System.out.println("️ No tiene entrenamientos registrados.");
        } else {
            atleta.getEntrenamientos().forEach(System.out::println);
        }
    }

    //  Sincronizar datos
    public void sincronizarDatos() {
        repositorio.sincronizarDatos();
    }

    //  Procesar planilla
    public void procesarPlanilla() {
        System.out.println("\n=== PROCESANDO PLANILLA ===");
        planillaService.procesar(atletas);
    }
}
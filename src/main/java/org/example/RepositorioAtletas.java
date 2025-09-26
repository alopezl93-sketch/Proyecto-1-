package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAtletas {
    private static final String ARCHIVO_JSON = "atletas.json";
    private Gson gson;
    private AtletaDAO atletaDAO;
    private EntrenamientoDAO entrenamientoDAO;
    private boolean usarBaseDatos;

    public RepositorioAtletas() { this(true); }

    public RepositorioAtletas(boolean usarBaseDatos) {
        this.usarBaseDatos = usarBaseDatos;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        if (usarBaseDatos) {
            atletaDAO = new AtletaDAO();
            entrenamientoDAO = new EntrenamientoDAO();
            if (!ConexionBD.probarConexion()) {
                System.out.println(" No se pudo conectar a MariaDB, usando JSON como respaldo");
                this.usarBaseDatos = false;
            }
        }
    }

    //  Guardar en ambos formatos
    public void guardar(List<Atleta> atletas) {
        guardarEnBaseDatos(atletas);
        guardarEnJSON(atletas);
    }

    //  Cargar según modo
    public List<Atleta> cargar() {
        return usarBaseDatos ? cargarDeBaseDatos() : cargarDeJSON();
    }

    //  Guardar un atleta individual en BD
    public int guardarAtleta(Atleta atleta) {
        if (usarBaseDatos) {
            return atletaDAO.crearAtleta(atleta);
        } else {
            System.out.println(" Guardado individual no disponible en modo JSON");
            return 0;
        }
    }

    //  Registrar entrenamiento
    public boolean registrarEntrenamiento(Atleta atleta, Entrenamiento entrenamiento) {
        if (usarBaseDatos) {
            return entrenamientoDAO.registrarEntrenamiento(atleta.getId(), entrenamiento);
        } else {
            atleta.agregarEntrenamiento(entrenamiento);
            return true;
        }
    }

    //  Guardar en BD
    public void guardarEnBaseDatos(List<Atleta> atletas) {
        if (!usarBaseDatos) return;
        try {
            for (Atleta atleta : atletas) {
                if (atleta.getId() == 0) {
                    int id = atletaDAO.crearAtleta(atleta);
                    atleta.setId(id);
                    for (Entrenamiento entrenamiento : atleta.getEntrenamientos()) {
                        entrenamientoDAO.registrarEntrenamiento(id, entrenamiento);
                    }
                } else {
                    atletaDAO.actualizarAtleta(atleta);
                }
            }
            System.out.println(" Datos guardados en MariaDB");
        } catch (Exception e) {
            System.err.println(" Error al guardar en BD: " + e.getMessage());
        }
    }

    //  Cargar desde BD
    private List<Atleta> cargarDeBaseDatos() {
        try {
            List<Atleta> atletas = atletaDAO.obtenerTodosLosAtletas();
            System.out.println(" Datos cargados desde MariaDB (" + atletas.size() + " atletas)");
            return atletas;
        } catch (Exception e) {
            System.err.println(" Error al cargar de BD: " + e.getMessage());
            return cargarDeJSON();
        }
    }

    // Guardar en JSON
    public void guardarEnJSON(List<Atleta> atletas) {
        try (FileWriter writer = new FileWriter(ARCHIVO_JSON)) {
            gson.toJson(atletas, writer);
            System.out.println(" Datos guardados en JSON (" + atletas.size() + " atletas)");
        } catch (IOException e) {
            System.err.println(" Error al guardar en JSON: " + e.getMessage());
        }
    }

    // Cargar desde JSON
    private List<Atleta> cargarDeJSON() {
        File archivo = new File(ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(ARCHIVO_JSON)) {
            Type listType = new TypeToken<List<Atleta>>() {}.getType();
            List<Atleta> atletas = gson.fromJson(reader, listType);
            return atletas != null ? atletas : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    //  Eliminar atleta
    public boolean eliminarAtleta(String nombre, List<Atleta> atletas) {
        Atleta atleta = atletas.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);

        if (atleta == null) {
            System.out.println(" No se encontró el atleta: " + nombre);
            return false;
        }

        atletas.remove(atleta);

        if (usarBaseDatos) {
            atletaDAO.eliminarAtleta(atleta.getId());
            entrenamientoDAO.eliminarEntrenamientosPorAtleta(atleta.getId());
        }

        guardarEnJSON(atletas); // respaldo
        System.out.println(" Atleta eliminado: " + nombre);
        return true;
    }

    //  Sincronizar datos JSON ↔ MariaDB
    public void sincronizarDatos() {
        if (!usarBaseDatos) {
            System.out.println(" Sincronización solo disponible en modo MariaDB");
            return;
        }
        List<Atleta> atletasJSON = cargarDeJSON();
        if (!atletasJSON.isEmpty()) {
            System.out.println(" Sincronizando " + atletasJSON.size() + " atletas desde JSON a MariaDB...");
            guardarEnBaseDatos(atletasJSON);
        } else {
            System.out.println(" No hay atletas en JSON para sincronizar.");
        }
    }

    //  Cambiar modo
    public void cambiarModo(boolean usarBaseDatos) {
        this.usarBaseDatos = usarBaseDatos;
        if (usarBaseDatos && (atletaDAO == null || entrenamientoDAO == null)) {
            atletaDAO = new AtletaDAO();
            entrenamientoDAO = new EntrenamientoDAO();
        }
        System.out.println(" Modo cambiado a: " + (usarBaseDatos ? "MariaDB" : "JSON"));
    }

    public boolean isUsandoBaseDatos() {
        return usarBaseDatos;
    }
}
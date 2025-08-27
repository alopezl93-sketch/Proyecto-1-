package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAtletas {
    private static final String ARCHIVO = "atletas.json";
    private Gson gson;

    public RepositorioAtletas() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void guardar(List<Atleta> atletas) {
        try (FileWriter writer = new FileWriter(ARCHIVO)) {
            gson.toJson(atletas, writer);
            System.out.println(" Datos guardados exitosamente");
        } catch (IOException e) {
            System.err.println(" Error al guardar: " + e.getMessage());
        }
    }

    public List<Atleta> cargar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(ARCHIVO)) {
            Type listType = new TypeToken<List<Atleta>>(){}.getType();
            List<Atleta> atletas = gson.fromJson(reader, listType);
            return atletas != null ? atletas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println(" Error al cargar: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
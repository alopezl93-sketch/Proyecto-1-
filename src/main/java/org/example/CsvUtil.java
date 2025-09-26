package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvUtil {

    public static void exportarAtletas(String ruta, List<Atleta> atletas) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) {
            // ✅ Encabezados más claros y con separador ;
            fw.write("ID;Nombre;Edad;Disciplina;Departamento;Nacionalidad;Fecha de Ingreso\n");

            for (Atleta a : atletas) {
                fw.write(String.format("%d;%s;%d;%s;%s;%s;%s\n",
                        a.getId(),
                        esc(a.getNombre()),
                        a.getEdad(),
                        esc(a.getDisciplina()),
                        esc(a.getDepartamento()),
                        esc(a.getNacionalidad()),
                        a.getFechaIngreso() != null ? a.getFechaIngreso() : ""));
            }
        }
    }

    public static void exportarEntrenamientos(String ruta, Atleta atleta) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) {
            // ✅ Encabezados más claros y con separador ;
            fw.write("Atleta_ID;Fecha;Tipo;Marca;Ubicación;País\n");

            for (Entrenamiento e : atleta.getEntrenamientos()) {
                fw.write(String.format("%d;%s;%s;%.2f;%s;%s\n",
                        atleta.getId(),
                        esc(e.getFecha()),
                        esc(e.getTipo()),
                        e.getMarca(),
                        esc(e.getUbicacion()),
                        esc(e.getPais())));
            }
        }
    }

    // ✅ Escapado mejorado para valores con ; o comillas
    private static String esc(String s) {
        if (s == null) return "";
        String t = s.replace("\"", "\"\"");
        if (t.contains(";") || t.contains("\"")) return "\"" + t + "\"";
        return t;
    }
}
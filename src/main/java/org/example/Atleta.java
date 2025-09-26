package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Atleta {
    private int id;
    private String nombre;
    private int edad;
    private String disciplina;
    private String departamento;
    private String nacionalidad;       // NUEVO
    private LocalDate fechaIngreso;    // NUEVO
    private List<Entrenamiento> entrenamientos;

    public Atleta(String nombre, int edad, String disciplina, String departamento,
                  String nacionalidad, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.edad = edad;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.nacionalidad = nacionalidad;
        this.fechaIngreso = fechaIngreso;
        this.entrenamientos = new ArrayList<>();
    }

    public Atleta() {
        this.entrenamientos = new ArrayList<>();
    }

    public Atleta(int id, String nombre, int edad, String disciplina, String departamento,
                  String nacionalidad, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.nacionalidad = nacionalidad;
        this.fechaIngreso = fechaIngreso;
        this.entrenamientos = new ArrayList<>();
    }

    public void agregarEntrenamiento(Entrenamiento entrenamiento) {
        entrenamientos.add(entrenamiento);
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public List<Entrenamiento> getEntrenamientos() { return entrenamientos; }
    public void setEntrenamientos(List<Entrenamiento> entrenamientos) { this.entrenamientos = entrenamientos; }

    // Compatibilidad antigua
    public double getPeso() { return 0.0; }
    public void setPeso(double peso) {}

    public double getAltura() { return 0.0; }
    public void setAltura(double altura) {}

    @Override
    public String toString() {
        return String.format("%s (%d años) - %s, %s | %s | ingreso: %s",
                nombre, edad, disciplina, departamento, nacionalidad,
                fechaIngreso != null ? fechaIngreso : "N/D");
    }
}
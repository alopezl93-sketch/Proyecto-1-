package org.example;

import java.util.ArrayList;
import java.util.List;

public class Atleta {
    private String nombre;
    private int edad;
    private String disciplina;
    private String departamento;
    private List<Entrenamiento> entrenamientos;

    public Atleta(String nombre, int edad, String disciplina, String departamento) {
        this.nombre = nombre;
        this.edad = edad;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.entrenamientos = new ArrayList<>();
    }

    public void agregarEntrenamiento(Entrenamiento entrenamiento) {
        entrenamientos.add(entrenamiento);
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public List<Entrenamiento> getEntrenamientos() { return entrenamientos; }
    public void setEntrenamientos(List<Entrenamiento> entrenamientos) { this.entrenamientos = entrenamientos; }

    @Override
    public String toString() {
        return String.format("%s (%d años) - %s, %s", nombre, edad, disciplina, departamento);
    }
}
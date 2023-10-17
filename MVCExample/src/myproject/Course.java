/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myproject;

import java.util.ArrayList;

/**
 *
 * @author hashemim
 */
public class Course {

    private String name;
    private ArrayList<Student> studentsRegistered = new ArrayList<>();

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Student> getStudentsRegistered() {
        return this.studentsRegistered;
    }

    public void setStudentsRegistered(ArrayList<Student> students) {
        this.studentsRegistered = students;
    }
}

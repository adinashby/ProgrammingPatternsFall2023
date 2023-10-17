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
public class CourseController {

    private Course courseModel;

    public CourseController(Course courseModel) {
        this.courseModel = courseModel;
    }

    public void setStudentName(String name, int index) {
        courseModel.getStudentsRegistered().get(index).setName(name);
    }

    public String getStudentName(int index) {
        return courseModel.getStudentsRegistered().get(index).getName();
    }

    public void setStudentGrade(double grade, int index) {
        courseModel.getStudentsRegistered().get(index).setGrade(grade);
    }

    public double getStudentGrade(int index) {
        return courseModel.getStudentsRegistered().get(index).getGrade();
    }

    public void setCourseName(String name) {
        courseModel.setName(name);
    }

    public String getCourseName() {
        return courseModel.getName();
    }

    public void setCourseStudents(ArrayList<Student> students) {
        courseModel.setStudentsRegistered(students);
    }

    public ArrayList<Student> getCourseStudents() {
        return courseModel.getStudentsRegistered();
    }
}

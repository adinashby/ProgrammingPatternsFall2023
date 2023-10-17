/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myproject;

import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author hashemim
 */
public class MyProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        
        
        Course programmingPatterns = new Course("Programming Patterns");
        ArrayList<Student> programmingPatternsStudents = new ArrayList<>();
        
        programmingPatternsStudents.add(new Student("Mubeen", 50.0));
        programmingPatternsStudents.add(new Student("Rachelle", 70.0));
        programmingPatternsStudents.add(new Student("Mert", 20.0));
        
        programmingPatterns.setStudentsRegistered(programmingPatternsStudents);
        
        Course ecommerce = new Course("E-Commerce");
        ArrayList<Student> ecommerceStudents = new ArrayList<>();
        
        ecommerceStudents.add(new Student("Smaie", 80.0));
        ecommerceStudents.add(new Student("James", 70.0));
        ecommerceStudents.add(new Student("Sonia", 60.0));
        
        ecommerce.setStudentsRegistered(ecommerceStudents);
        
        ArrayList<CourseController> allCourseControllers = new ArrayList<>(); 
        
        allCourseControllers.add(new CourseController(programmingPatterns));
        allCourseControllers.add(new CourseController(ecommerce));
        
        MainForm myFrame = new MainForm();
        
        Controller controller = Controller.getInstance(allCourseControllers, myFrame);
   
        controller.showForm();
    }
    
}

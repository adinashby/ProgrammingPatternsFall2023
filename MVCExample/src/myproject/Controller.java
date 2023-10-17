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
public class Controller {

    private ArrayList<CourseController> courseControllers;
    private MainForm mainFormView;
    private static Controller controllerObject;
    
    private Controller() {
        
    }

    private Controller(ArrayList<CourseController> courseControllers, MainForm mainFormView) {
        this.courseControllers = courseControllers;
        this.mainFormView = mainFormView;
        
        
    }
    
    public static Controller getInstance() {
        // create object if it's not already created
        if(controllerObject == null) {
           controllerObject = new Controller();
        }

         // returns the singleton object
         return controllerObject;
    }
    
    public static Controller getInstance(ArrayList<CourseController> courseControllers, MainForm mainFormView) {
        // create object if it's not already created
        if(controllerObject == null) {
           controllerObject = new Controller(courseControllers, mainFormView);
        }

         // returns the singleton object
         return controllerObject;
    }

    public void setCoursesControllers(ArrayList<CourseController> courseControllers) {
        this.courseControllers = courseControllers;
    }

    public ArrayList<CourseController> getCoursesControllers() {
        return courseControllers;
    }
    
    public void showForm() {
        mainFormView.show();
    }
    
    public void processInputTextField() {
        String selectedCourseName = mainFormView.inputTextField.getText();
        CourseController selectedCourseController = null;

        for (int i = 0; i < courseControllers.size(); i++) {
            if (courseControllers.get(i).getCourseName().equalsIgnoreCase(selectedCourseName)) {
                selectedCourseController = courseControllers.get(i);
            }
        }
        
        mainFormView.setCourseController(selectedCourseController);
    }

    public void updateStudentTextArea(CourseController selectedCourseController) {
        for (int i = 0; i < selectedCourseController.getCourseStudents().size(); i++) {
            mainFormView.resultTextArea.append(selectedCourseController.getCourseStudents().get(i).getName() + "  -  " + 
                    selectedCourseController.getCourseStudents().get(i).getGrade() + "\n");
        }
    }
}
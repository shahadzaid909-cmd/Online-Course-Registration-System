package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import online_course_registration_system.Entity.Student;
import online_course_registration_system.Tools.SessionManager;

public class StudentDashboardController {

    @FXML
    private javafx.scene.control.Label studentIdLabel;

    @FXML
    private javafx.scene.control.Label nameLabel;

    @FXML
    private javafx.scene.control.Label emailLabel;

    @FXML
    private javafx.scene.control.Label majorLabel;

    private SessionManager sessionManager;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();
        loadStudentInfo();
    }

    private void loadStudentInfo() {
        if (sessionManager.isLoggedIn()) {
            Student student = (Student) sessionManager.getCurrentUser();
            studentIdLabel.setText(student.getStudentId());
            nameLabel.setText(student.getFullName());
            emailLabel.setText(student.getEmail());
            majorLabel.setText(student.getMajor() != null ? student.getMajor() : "Undeclared");
        }
    }

    @FXML
    private void handleBrowseCourses() {
        navigateTo("/online_course_registration_system/UI/BrowseCourses.fxml", "Browse Courses");
    }

    @FXML
    private void handleMyCourses() {
        navigateTo("/online_course_registration_system/UI/MyCourses.fxml", "My Courses");
    }

    @FXML
    private void handleLogout() {
        try {
            sessionManager.logout();
            navigateTo("/online_course_registration_system/UI/Login.fxml", "Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) studentIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
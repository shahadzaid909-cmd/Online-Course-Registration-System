package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import online_course_registration_system.Entity.Professor;
import online_course_registration_system.Tools.SessionManager;

public class ProfessorDashboardController {

    @FXML
    private javafx.scene.control.Label professorIdLabel;

    @FXML
    private javafx.scene.control.Label nameLabel;

    @FXML
    private javafx.scene.control.Label emailLabel;

    private SessionManager sessionManager;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();
        loadProfessorInfo();
    }

    private void loadProfessorInfo() {
        if (sessionManager.isLoggedIn()) {
            Professor professor = (Professor) sessionManager.getCurrentUser();
            professorIdLabel.setText(professor.getProfessorId());
            nameLabel.setText(professor.getFullName());
            emailLabel.setText(professor.getEmail());
        }
    }

    @FXML
    private void handleManageCourses() {
        navigateTo("/online_course_registration_system/UI/ManageCourses.fxml", "Manage Courses");
    }

    @FXML
    private void handleManageGrades() {
        navigateTo("/online_course_registration_system/UI/ManageGrades.fxml", "Manage Grades");
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

            Stage stage = (Stage) professorIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
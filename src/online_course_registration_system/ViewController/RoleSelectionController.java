package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import online_course_registration_system.Tools.SessionManager;

public class RoleSelectionController {

    private SessionManager sessionManager = SessionManager.getInstance();

    @FXML
    private void handleStudentRole() {
        sessionManager.setExpectedUserType("STUDENT");
        navigateTo("/online_course_registration_system/UI/StudentLoginRegister.fxml", "Student Login / Register");
    }

    @FXML
    private void handleProfessorRole() {
        sessionManager.setExpectedUserType("PROFESSOR");
        navigateTo("/online_course_registration_system/UI/Login.fxml", "Professor Login");
    }

    @FXML
    private void handleAdminRole() {
        sessionManager.setExpectedUserType("ADMINISTRATOR");
        navigateTo("/online_course_registration_system/UI/Login.fxml", "Administrator Login");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(javafx.stage.Window::isShowing)
                    .findFirst()
                    .orElse(null);

            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle(title);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
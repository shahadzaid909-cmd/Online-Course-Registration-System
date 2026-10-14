package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import online_course_registration_system.Tools.SessionManager;

public class StudentLoginRegisterController {

    @FXML
    private void handleLogin() {
        try {
            SessionManager.getInstance().setExpectedUserType("STUDENT");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/Login.fxml"));
            Parent root = loader.load();

            Stage stage = getCurrentStage();
            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle("Student Login");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/SignUp.fxml"));
            Parent root = loader.load();

            Stage stage = getCurrentStage();
            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle("Student Registration");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/RoleSelection.fxml"));
            Parent root = loader.load();

            Stage stage = getCurrentStage();
            if (stage != null) {
                stage.setScene(new Scene(root));
                stage.setTitle("Student Login / Register");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage getCurrentStage() {
        return (Stage) javafx.stage.Window.getWindows().stream()
                .filter(javafx.stage.Window::isShowing)
                .findFirst()
                .orElse(null);
    }
}

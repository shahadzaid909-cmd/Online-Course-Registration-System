package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import online_course_registration_system.Entity.Administrator;
import online_course_registration_system.Tools.SessionManager;

public class AdminDashboardController {

    @FXML
    private Label adminIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    private SessionManager sessionManager;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();
        loadAdminInfo();
    }

    private void loadAdminInfo() {
        if (sessionManager.isLoggedIn()) {
            Administrator admin = (Administrator) sessionManager.getCurrentUser();
            adminIdLabel.setText(admin.getAdminId());
            nameLabel.setText(admin.getFullName());
            emailLabel.setText(admin.getEmail());
        }
    }

    @FXML
    private void handleManageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/ManageUsers.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) adminIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Manage Users");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleManageCourses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/ManageCoursesAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) adminIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Manage Courses");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            sessionManager.logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) adminIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

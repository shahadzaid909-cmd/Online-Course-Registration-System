package online_course_registration_system.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import online_course_registration_system.database.DatabaseConnection;
import java.sql.*;

public class SignUpController {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField majorField;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        messageLabel.setText("");
    }

    @FXML
    private void handleSignUp() {
        messageLabel.setText("");
        String studentId = studentIdField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String major = majorField.getText();

    
        if (studentId == null || studentId.trim().isEmpty()) {
            showError("Student ID is required");
            return;
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            showError("Full name is required");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            showError("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            showError("Password is required");
            return;
        }

        if (password.length() < 4) {
            showError("Password must be at least 4 characters long");
            return;
        }
        
        try {
            createStudent(studentId.trim(), fullName.trim(), email.trim(), password, major);
            showSuccess("Account created successfully!");

        } catch (SQLException e) {
            if (e.getMessage().contains("unique constraint")) {
                showError("Student ID or Email already exists");
            } else {
                showError("Failed to create account. Please try again.");
            }
            e.printStackTrace();
        } catch (Exception e) {
            showError("An error occurred. Please check your database connection.");
            e.printStackTrace();
        }
    }

    private void createStudent(String studentId, String fullName, String email, String password, String major) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Student (student_id, password, full_name, email, major) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, studentId);
        pstmt.setString(2, password);
        pstmt.setString(3, fullName);
        pstmt.setString(4, email);
        pstmt.setString(5, major != null && !major.trim().isEmpty() ? major.trim() : "Undeclared");
        pstmt.executeUpdate();
        pstmt.close();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/StudentLoginRegister.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) studentIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Portal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
    }
}

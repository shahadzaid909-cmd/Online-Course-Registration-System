package online_course_registration_system.ViewController;

import online_course_registration_system.Entity.Student;
import online_course_registration_system.Entity.User;
import online_course_registration_system.Entity.Administrator;
import online_course_registration_system.Entity.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import online_course_registration_system.database.DatabaseConnection;
import online_course_registration_system.Tools.SessionManager;
import java.sql.*;
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button loginButton;

    private SessionManager sessionManager;

    @FXML
    public void initialize() {
        try {
            sessionManager = SessionManager.getInstance();

            String expectedUserType = sessionManager.getExpectedUserType();
            if ("STUDENT".equals(expectedUserType)) {
                usernameLabel.setText("Student ID:");
                usernameField.setPromptText("Enter your Student ID");
            } else if ("PROFESSOR".equals(expectedUserType)) {
                usernameLabel.setText("Professor ID:");
                usernameField.setPromptText("Enter your Professor ID");
            } else if ("ADMINISTRATOR".equals(expectedUserType)) {
                usernameLabel.setText("Admin ID:");
                usernameField.setPromptText("Enter your Admin ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("Please enter username");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            errorLabel.setText("Please enter password");
            return;
        }

        try {
            User user = authenticateUser(username.trim(), password);

            if (user != null) {
                SessionManager.getInstance().login(user);

        
                navigateToDashboard(user);
            } else {
                errorLabel.setText("Invalid username or password");
                passwordField.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error during login");
        }
    }

    private User authenticateUser(String username, String password) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        String sql = "SELECT user_id, student_id, password, full_name, email, major FROM Student WHERE student_id = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Student student = new Student();
            student.setUserId(rs.getInt("user_id"));
            student.setStudentId(rs.getString("student_id"));
            student.setPassword(rs.getString("password"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            rs.close();
            pstmt.close();
            return student;
        }
        rs.close();
        pstmt.close();

        sql = "SELECT user_id, professor_id, password, full_name, email FROM Professor WHERE professor_id = ? AND password = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            Professor professor = new Professor();
            professor.setUserId(rs.getInt("user_id"));
            professor.setProfessorId(rs.getString("professor_id"));
            professor.setPassword(rs.getString("password"));
            professor.setFullName(rs.getString("full_name"));
            professor.setEmail(rs.getString("email"));
            rs.close();
            pstmt.close();
            return professor;
        }
        rs.close();
        pstmt.close();
   
        sql = "SELECT user_id, admin_id, password, full_name, email FROM Administrator WHERE admin_id = ? AND password = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            Administrator admin = new Administrator();
            admin.setUserId(rs.getInt("user_id"));
            admin.setAdminId(rs.getString("admin_id"));
            admin.setPassword(rs.getString("password"));
            admin.setFullName(rs.getString("full_name"));
            admin.setEmail(rs.getString("email"));
            rs.close();
            pstmt.close();
            return admin;
        }
        rs.close();
        pstmt.close();

        return null;
    }

    @FXML
    private void handleBack() {
        try {
            SessionManager sessionManager = SessionManager.getInstance();
            String expectedUserType = sessionManager.getExpectedUserType();

            String fxmlFile = "/online_course_registration_system/UI/RoleSelection.fxml";
            String title = "Select Your Role";

            if ("STUDENT".equals(expectedUserType)) {
                fxmlFile = "/online_course_registration_system/UI/StudentLoginRegister.fxml";
                title = "Student";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToDashboard(User user) {
        try {
            String fxmlFile = "";
            String title = "";

            if (user instanceof Student) {
                fxmlFile = "/online_course_registration_system/UI/StudentDashboard.fxml";
                title = "Student Dashboard";
            } else if (user instanceof Professor) {
                fxmlFile = "/online_course_registration_system/UI/ProfessorDashboard.fxml";
                title = "Professor Dashboard";
            } else if (user instanceof Administrator) {
                fxmlFile = "/online_course_registration_system/UI/AdminDashboard.fxml";
                title = "Administrator Dashboard";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load dashboard");
        }
    }
}

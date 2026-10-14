package online_course_registration_system.ViewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import online_course_registration_system.database.DatabaseConnection;
import online_course_registration_system.Entity.Administrator;
import online_course_registration_system.Entity.Professor;
import online_course_registration_system.Entity.Student;
import online_course_registration_system.Tools.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private TableView<Object> usersTable;

    @FXML
    private TableColumn<Object, String> userTypeColumn;

    @FXML
    private TableColumn<Object, String> idColumn;

    @FXML
    private TableColumn<Object, String> nameColumn;

    @FXML
    private TableColumn<Object, String> emailColumn;

    @FXML
    private Label messageLabel;

    private SessionManager sessionManager;

    @FXML
    public void initialize() {
        try {
            sessionManager = SessionManager.getInstance();

            if (sessionManager.isLoggedIn()) {
                Administrator admin = (Administrator) sessionManager.getCurrentUser();
            }
            userTypeComboBox.setItems(FXCollections.observableArrayList("Student", "Professor"));

            setupTableColumns();
            loadAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        userTypeColumn.setCellValueFactory(cellData -> {
            Object user = cellData.getValue();
            if (user instanceof Student) {
                return new SimpleStringProperty("Student");
            } else if (user instanceof Professor) {
                return new SimpleStringProperty("Professor");
            }
            return new SimpleStringProperty("");
        });

        idColumn.setCellValueFactory(cellData -> {
            Object user = cellData.getValue();
            if (user instanceof Student) {
                return new SimpleStringProperty(((Student) user).getStudentId());
            } else if (user instanceof Professor) {
                return new SimpleStringProperty(((Professor) user).getProfessorId());
            }
            return new SimpleStringProperty("");
        });

        nameColumn.setCellValueFactory(cellData -> {
            Object user = cellData.getValue();
            if (user instanceof Student) {
                return new SimpleStringProperty(((Student) user).getFullName());
            } else if (user instanceof Professor) {
                return new SimpleStringProperty(((Professor) user).getFullName());
            }
            return new SimpleStringProperty("");
        });

        emailColumn.setCellValueFactory(cellData -> {
            Object user = cellData.getValue();
            if (user instanceof Student) {
                return new SimpleStringProperty(((Student) user).getEmail());
            } else if (user instanceof Professor) {
                return new SimpleStringProperty(((Professor) user).getEmail());
            }
            return new SimpleStringProperty("");
        });
    }

    private void loadAllUsers() {
        try {
            List<Object> allUsers = new ArrayList<>();

            List<Student> students = getAllStudents();
            allUsers.addAll(students);

            List<Professor> professors = getAllProfessors();
            allUsers.addAll(professors);

            ObservableList<Object> userList = FXCollections.observableArrayList(allUsers);
            usersTable.setItems(userList);
            messageLabel.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        messageLabel.setText("");
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String userType = userTypeComboBox.getValue();

        if (id == null || id.trim().isEmpty()) {
            showError("ID is required");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            showError("Name is required");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            showError("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email");
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

        if (userType == null || userType.trim().isEmpty()) {
            showError("Please select a user type");
            return;
        }

        try {
            if (userType.equals("Student")) {
                createStudent(id.trim(), name.trim(), email.trim(), password);
       
            } else if (userType.equals("Professor")) {
                createProfessor(id.trim(), name.trim(), email.trim(), password);
              
            }
            clearForm();
            loadAllUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        userTypeComboBox.setValue(null);
        messageLabel.setText("");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    @FXML
    private void handleDeleteUser() {
        Object selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError("Please select a user to delete");
            return;
        }

        try {
            if (selectedUser instanceof Student) {
                deleteStudent(((Student) selectedUser).getUserId());
      
            } else if (selectedUser instanceof Professor) {
                deleteProfessor(((Professor) selectedUser).getUserId());
            }
            loadAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usersTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Administrator Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #F44336;");
    }

    private List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT user_id, student_id, full_name, email, major FROM Student ORDER BY full_name";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Student student = new Student();
            student.setUserId(rs.getInt("user_id"));
            student.setStudentId(rs.getString("student_id"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            students.add(student);
        }
        rs.close();
        pstmt.close();
        return students;
    }

    private List<Professor> getAllProfessors() throws SQLException {
        List<Professor> professors = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT user_id, professor_id, full_name, email FROM Professor ORDER BY full_name";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Professor professor = new Professor();
            professor.setUserId(rs.getInt("user_id"));
            professor.setProfessorId(rs.getString("professor_id"));
            professor.setFullName(rs.getString("full_name"));
            professor.setEmail(rs.getString("email"));
            professors.add(professor);
        }
        rs.close();
        pstmt.close();
        return professors;
    }

    private void createStudent(String studentId, String fullName, String email, String password) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Student (student_id, password, full_name, email, major) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, studentId);
        pstmt.setString(2, password);
        pstmt.setString(3, fullName);
        pstmt.setString(4, email);
        pstmt.setString(5, "Undeclared");
        pstmt.executeUpdate();
        pstmt.close();
    }

    private void createProfessor(String professorId, String fullName, String email, String password) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Professor (professor_id, password, full_name, email) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, professorId);
        pstmt.setString(2, password);
        pstmt.setString(3, fullName);
        pstmt.setString(4, email);
        pstmt.executeUpdate();
        pstmt.close();
    }

    private void deleteStudent(int userId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "DELETE FROM Student WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteProfessor(int userId) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "DELETE FROM Professor WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

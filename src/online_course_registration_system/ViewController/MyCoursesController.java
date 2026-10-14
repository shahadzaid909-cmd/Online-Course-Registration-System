package online_course_registration_system.ViewController;

import online_course_registration_system.Entity.Student;
import online_course_registration_system.Entity.Registration;
import online_course_registration_system.Entity.Course;
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
import online_course_registration_system.Tools.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyCoursesController {

    @FXML
    private TableView<Registration> coursesTable;

    @FXML
    private TableColumn<Registration, String> courseCodeColumn;

    @FXML
    private TableColumn<Registration, String> courseNameColumn;

    @FXML
    private TableColumn<Registration, String> gradeColumn;

    @FXML
    private Label messageLabel;

    private SessionManager sessionManager;
    private Student currentStudent;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();

        courseCodeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCourse().getCourseCode()));
        courseNameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCourse().getCourseName()));
        gradeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getGrade() != null ? cellData.getValue().getGrade() : "Not graded"));

        if (sessionManager.isLoggedIn()) {
            currentStudent = (Student) sessionManager.getCurrentUser();
        }

        loadCourses();
    }

    private void loadCourses() {
        try {
            List<Registration> registrations = getStudentRegistrations(currentStudent.getUserId());
            ObservableList<Registration> registrationList = FXCollections.observableArrayList(registrations);
            coursesTable.setItems(registrationList);
            messageLabel.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDropCourse() {
        Registration selectedRegistration = coursesTable.getSelectionModel().getSelectedItem();

        if (selectedRegistration == null) {
            showError("Please select a course to drop");
            return;
        }
        try {
            dropCourse(currentStudent.getUserId(), selectedRegistration.getCourse().getCourseId());
            loadCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Registration> getStudentRegistrations(int studentId) throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT r.registration_id, r.student_id, r.course_id, r.grade, " +
                     "c.course_code, c.course_name " +
                     "FROM Registration r " +
                     "JOIN Course c ON r.course_id = c.course_id " +
                     "WHERE r.student_id = ? ORDER BY c.course_code";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, studentId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Registration reg = new Registration();
            reg.setRegistrationId(rs.getInt("registration_id"));
            reg.setGrade(rs.getString("grade"));

            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseCode(rs.getString("course_code"));
            course.setCourseName(rs.getString("course_name"));
            reg.setCourse(course);

            registrations.add(reg);
        }
        rs.close();
        pstmt.close();
        return registrations;
    }

    private void dropCourse(int studentId, int courseId) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "DELETE FROM Registration WHERE student_id = ? AND course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, studentId);
        pstmt.setInt(2, courseId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/StudentDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) coursesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #F44336;");
    }
}

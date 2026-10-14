package online_course_registration_system.ViewController;

import online_course_registration_system.Entity.Student;
import online_course_registration_system.Entity.Registration;
import online_course_registration_system.Entity.Course;
import online_course_registration_system.Entity.RegistrationView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import online_course_registration_system.database.DatabaseConnection;
import online_course_registration_system.Tools.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewGradesController {

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label gradedCoursesLabel;

    @FXML
    private TableView<RegistrationView> gradesTable;

    @FXML
    private TableColumn<RegistrationView, String> courseCodeColumn;

    @FXML
    private TableColumn<RegistrationView, String> courseNameColumn;

    @FXML
    private TableColumn<RegistrationView, String> gradeColumn;

    @FXML
    private Label messageLabel;

    private SessionManager sessionManager;
    private Student currentStudent;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();

        if (sessionManager.isLoggedIn()) {
            currentStudent = (Student) sessionManager.getCurrentUser();
            studentNameLabel.setText(currentStudent.getFullName());
        }

        setupTableColumns();
        loadGrades();
    }

    private void setupTableColumns() {
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    private void loadGrades() {
        try {
            List<Registration> registrations = getStudentRegistrations(currentStudent.getUserId());
            List<RegistrationView> registrationViews = new ArrayList<>();

            int gradedCount = 0;
            for (Registration registration : registrations) {
                registrationViews.add(new RegistrationView(registration));
                if (registration.getGrade() != null && !registration.getGrade().isEmpty()) {
                    gradedCount++;
                }
            }

            ObservableList<RegistrationView> registrationList = FXCollections.observableArrayList(registrationViews);
            gradesTable.setItems(registrationList);

            totalCoursesLabel.setText(String.valueOf(registrations.size()));
            gradedCoursesLabel.setText(String.valueOf(gradedCount));

            messageLabel.setText("");
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

    @FXML
    private void handleRefresh() {
        loadGrades();
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/StudentDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) studentNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

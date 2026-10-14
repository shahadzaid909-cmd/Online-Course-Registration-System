package online_course_registration_system.ViewController;

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
import online_course_registration_system.Entity.Course;
import online_course_registration_system.Entity.Professor;
import online_course_registration_system.Tools.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageCoursesController {

    @FXML
    private Label professorNameLabel;

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    private SessionManager sessionManager;
    private Professor currentProfessor;

    @FXML
    public void initialize() {
        try {
            sessionManager = SessionManager.getInstance();

            if (sessionManager.isLoggedIn()) {
                currentProfessor = (Professor) sessionManager.getCurrentUser();
                professorNameLabel.setText(currentProfessor.getFullName());
            }

            courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

            loadCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        try {
            List<Course> courses = getProfessorCourses(currentProfessor.getUserId());
            ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
            coursesTable.setItems(courseList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Course> getProfessorCourses(int professorId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT course_id, course_code, course_name FROM Course WHERE professor_id = ? ORDER BY course_code";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, professorId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseCode(rs.getString("course_code"));
            course.setCourseName(rs.getString("course_name"));
            courses.add(course);
        }
        rs.close();
        pstmt.close();
        return courses;
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/ProfessorDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) professorNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Professor");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

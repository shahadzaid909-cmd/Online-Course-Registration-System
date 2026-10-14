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
import online_course_registration_system.Entity.Student;
import online_course_registration_system.Tools.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrowseCoursesController {

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;
    
    private SessionManager sessionManager;
    private Student currentStudent;

    @FXML
    public void initialize() {
        sessionManager = SessionManager.getInstance();

        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        if (sessionManager.isLoggedIn()) {
            currentStudent = (Student) sessionManager.getCurrentUser();
        }

        loadCourses();
    }

    private void loadCourses() {
        try {
            List<Course> allCourses = getAllAvailableCourses();
            List<Course> unregisteredCourses = new ArrayList<>();

            for (Course course : allCourses) {
                if (!isStudentRegisteredForCourse(currentStudent.getUserId(), course.getCourseId())) {
                    unregisteredCourses.add(course);
                }
            }

            ObservableList<Course> courseList = FXCollections.observableArrayList(unregisteredCourses);
            coursesTable.setItems(courseList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        try{
         registerStudentForCourse(currentStudent.getUserId(), selectedCourse.getCourseId());
            loadCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Course> getAllAvailableCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT course_id, course_code, course_name FROM Course ORDER BY course_code";
        PreparedStatement pstmt = conn.prepareStatement(sql);
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

    private boolean isStudentRegisteredForCourse(int userId, int courseId) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM Registration WHERE student_id = ? AND course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, courseId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        pstmt.close();
        return count > 0;
    }

    private void registerStudentForCourse(int userId, int courseId) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Registration (student_id, course_id) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
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
}

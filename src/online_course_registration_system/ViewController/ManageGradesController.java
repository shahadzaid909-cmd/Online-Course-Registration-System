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
import javafx.util.Callback;
import javafx.util.StringConverter;
import online_course_registration_system.database.DatabaseConnection;
import online_course_registration_system.Entity.Course;
import online_course_registration_system.Entity.Professor;
import online_course_registration_system.Entity.Registration;
import online_course_registration_system.Entity.Student;
import online_course_registration_system.Entity.StudentGrade;
import online_course_registration_system.Tools.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageGradesController {

    @FXML
    private Label professorNameLabel;

    @FXML
    private ComboBox<Course> courseComboBox;

    @FXML
    private TableView<StudentGrade> studentsTable;

    @FXML
    private TableColumn<StudentGrade, String> studentNameColumn;

    @FXML
    private TableColumn<StudentGrade, String> gradeColumn;

    private SessionManager sessionManager;
    private Professor currentProfessor;
    private ObservableList<StudentGrade> studentGrades;

    @FXML
    public void initialize() {
        try {
            sessionManager = SessionManager.getInstance();

            if (sessionManager.isLoggedIn()) {
                currentProfessor = (Professor) sessionManager.getCurrentUser();
                professorNameLabel.setText(currentProfessor.getFullName());
            }

            studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

          gradeColumn.setCellFactory(new Callback<TableColumn<StudentGrade, String>, TableCell<StudentGrade, String>>() {
    @Override
    public TableCell<StudentGrade, String> call(TableColumn<StudentGrade, String> param) {
        return new TableCell<StudentGrade, String>() {
            private TextField textField;
            private StudentGrade currentStudentGrade;

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (textField != null && currentStudentGrade != null) {
                    textField.textProperty().unbindBidirectional(currentStudentGrade.gradeProperty());
                }

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    textField = null;
                    currentStudentGrade = null;
                } else {
                    currentStudentGrade = getTableRow().getItem();
                    textField = new TextField();
                    textField.textProperty().bindBidirectional(currentStudentGrade.gradeProperty());
                    setGraphic(textField);
                }
            }
        };
    }
});

            courseComboBox.setConverter(new StringConverter<Course>() {
                @Override
                public String toString(Course course) {
                    return course != null ? course.getCourseCode() + " - " + course.getCourseName() : "";
                }

                @Override
                public Course fromString(String string) {
                    return null;
                }
            });

            courseComboBox.setOnAction(event -> loadStudentsForSelectedCourse());

            loadProfessorCourses();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfessorCourses() {
        try {
            List<Course> courses = getProfessorCourses(currentProfessor.getUserId());
            ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
            courseComboBox.setItems(courseList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsForSelectedCourse() {
        Course selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null) {
            studentsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            List<Registration> registrations = getRegistrationsByCourse(selectedCourse.getCourseId());

            studentGrades = FXCollections.observableArrayList(
                    registrations.stream()
                            .map(reg -> new StudentGrade(
                            reg.getRegistrationId(),
                            reg.getStudent().getUserId(),
                            reg.getStudent().getFullName(),
                            reg.getGrade()
                    ))
                            .collect(Collectors.toList())
            );

            studentsTable.setItems(studentGrades);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveGrades() {
        if (studentGrades == null || studentGrades.isEmpty()) {
            return;
        }

        try {
            for (StudentGrade studentGrade : studentGrades) {
                String grade = studentGrade.getGrade();
                if (grade != null && !grade.trim().isEmpty()) {
                    updateGrade(studentGrade.getRegistrationId(), grade.trim());
                }
            }
            loadStudentsForSelectedCourse();
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

    private List<Registration> getRegistrationsByCourse(int courseId) throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT r.registration_id, r.student_id, r.course_id, r.grade, "
                + "s.full_name, s.email, s.major "
                + "FROM Registration r "
                + "JOIN Student s ON r.student_id = s.user_id "
                + "WHERE r.course_id = ? ORDER BY s.full_name";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, courseId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Registration reg = new Registration();
            reg.setRegistrationId(rs.getInt("registration_id"));
            reg.setGrade(rs.getString("grade"));

            Student student = new Student();
            student.setUserId(rs.getInt("student_id"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            reg.setStudent(student);

            registrations.add(reg);
        }
        rs.close();
        pstmt.close();
        return registrations;
    }

    private void updateGrade(int registrationId, String grade) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE Registration SET grade = ? WHERE registration_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, grade);
        pstmt.setInt(2, registrationId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/ProfessorDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) professorNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Professor Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

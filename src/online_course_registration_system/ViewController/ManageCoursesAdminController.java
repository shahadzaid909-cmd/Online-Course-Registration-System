package online_course_registration_system.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import online_course_registration_system.database.DatabaseConnection;
import online_course_registration_system.Entity.Course;
import online_course_registration_system.Entity.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageCoursesAdminController {

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, String> courseIdColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> professorColumn;

    private ObservableList<Course> coursesList;

    @FXML
    public void initialize() {
        coursesList = FXCollections.observableArrayList();

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        professorColumn.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            if (course.getProfessor() != null) {
                return new javafx.beans.property.SimpleStringProperty(course.getProfessor().getFullName());
            } else {
                return new javafx.beans.property.SimpleStringProperty("Not Assigned");
            }
        });

        loadCourses();
    }

    private void loadCourses() {
        try {
            List<Course> courses = getAllCourses();
            coursesList.clear();
            coursesList.addAll(courses);
            coursesTable.setItems(coursesList);
        } catch (Exception e) {
              e.printStackTrace();
        }
    }


    @FXML
    private void handleAddCourse() {
        Course newCourse = new Course();
        Dialog<Course> dialog = createCourseDialog("Add New Course", newCourse);

        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(course -> {
            try {
                createCourse(course);
                loadCourses();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditCourse() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            return;
        }

        Dialog<Course> dialog = createCourseDialog("Edit Course", selectedCourse);

        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(course -> {
            try {
                updateCourse(course);
                loadCourses();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            return;
        }

        try {
            deleteCourse(selectedCourse.getCourseId());
            loadCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dialog<Course> createCourseDialog(String title, Course course) {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Enter course information");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField courseCodeField = new TextField(course.getCourseCode() != null ? course.getCourseCode() : "");
        TextField courseNameField = new TextField(course.getCourseName() != null ? course.getCourseName() : "");

        ComboBox<Professor> professorComboBox = new ComboBox<>();
        try {
            List<Professor> professors = getAllProfessors();
            professorComboBox.getItems().addAll(professors);
            if (course.getProfessor() != null) {
                professorComboBox.setValue(course.getProfessor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        professorComboBox.setConverter(new javafx.util.StringConverter<Professor>() {
            @Override
            public String toString(Professor professor) {
                return professor == null ? "" : professor.getFullName();
            }

            @Override
            public Professor fromString(String string) {
                return null;
            }
        });

        courseCodeField.setPromptText("e.g. CS313");
        courseNameField.setPromptText("Course Name");

        grid.add(new Label("Course Code:"), 0, 0);
        grid.add(courseCodeField, 1, 0);
        grid.add(new Label("Course Name:"), 0, 1);
        grid.add(courseNameField, 1, 1);
        grid.add(new Label("Professor:"), 0, 2);
        grid.add(professorComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                course.setCourseCode(courseCodeField.getText());
                course.setCourseName(courseNameField.getText());
                course.setProfessor(professorComboBox.getValue());
                return course;
            }
            return null;
        });

        return dialog;
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/online_course_registration_system/UI/AdminDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) coursesTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Administrator");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.professor_id, " +
                     "p.full_name as professor_name " +
                     "FROM Course c " +
                     "LEFT JOIN Professor p ON c.professor_id = p.user_id " +
                     "ORDER BY c.course_code";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseCode(rs.getString("course_code"));
            course.setCourseName(rs.getString("course_name"));

            int professorId = rs.getInt("professor_id");
            if (professorId > 0) {
                Professor professor = new Professor();
                professor.setUserId(professorId);
                professor.setFullName(rs.getString("professor_name"));
                course.setProfessor(professor);
            }

            courses.add(course);
        }
        rs.close();
        pstmt.close();
        return courses;
    }

    private void createCourse(Course course) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "INSERT INTO Course (course_code, course_name, professor_id) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, course.getCourseCode());
        pstmt.setString(2, course.getCourseName());
        if (course.getProfessor() != null) {
            pstmt.setInt(3, course.getProfessor().getUserId());
        } else {
            pstmt.setNull(3, Types.INTEGER);
        }
        pstmt.executeUpdate();
        pstmt.close();
    }

    private void updateCourse(Course course) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE Course SET course_code = ?, course_name = ?, professor_id = ? WHERE course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, course.getCourseCode());
        pstmt.setString(2, course.getCourseName());
        if (course.getProfessor() != null) {
            pstmt.setInt(3, course.getProfessor().getUserId());
        } else {
            pstmt.setNull(3, Types.INTEGER);
        }
        pstmt.setInt(4, course.getCourseId());
        pstmt.executeUpdate();
        pstmt.close();
    }

    private void deleteCourse(int courseId) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "DELETE FROM Course WHERE course_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, courseId);
        pstmt.executeUpdate();
        pstmt.close();
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
}

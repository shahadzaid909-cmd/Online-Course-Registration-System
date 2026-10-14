package online_course_registration_system.Entity;

public class Course implements Validatable {

    private int courseId;
    private String courseCode;
    private String courseName;
    private Professor professor;

    public Course() {
    }

    public Course(int courseId, String courseCode, String courseName, Professor professor) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.professor = professor;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public boolean validate() {
        if (courseCode == null || courseCode.isEmpty()) {
            return false;
        }
        if (courseName == null || courseName.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String getValidationError() {
        if (courseCode == null || courseCode.isEmpty()) {
            return "Course code is required";
        }
        if (courseName == null || courseName.isEmpty()) {
            return "Course name is required";
        }
        return "";
    }

    @Override
    public String toString() {
        return "Course{"
                + "courseId=" + courseId
                + ", courseCode='" + courseCode + '\''
                + ", courseName='" + courseName + '\''
                + ", professor=" + (professor != null ? professor.getFullName() : "None")
                + '}';
    }
}

package online_course_registration_system.Entity;


public class RegistrationView {
    private String courseCode;
    private String courseName;
    private String grade;

    public RegistrationView(Registration registration) {
        if (registration.getCourse() != null) {
            this.courseCode = registration.getCourse().getCourseCode();
            this.courseName = registration.getCourse().getCourseName();
        }

        this.grade = registration.getGrade() != null && !registration.getGrade().isEmpty()
                ? registration.getGrade()
                : "not assigned";
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

package online_course_registration_system.Entity;

public class Registration implements Validatable {

    private int registrationId;
    private Student student;
    private Course course;
    private String grade;

    public Registration() {
    }

    public Registration(int registrationId, Student student, Course course,
            String grade) {
        this.registrationId = registrationId;
        this.student = student;
        this.course = course;
        this.grade = grade;

    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public boolean validate() {
        if (student == null) {
            return false;
        }
        if (course == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getValidationError() {
        if (student == null) {
            return "Student is required";
        }
        if (course == null) {
            return "Course is required";
        }
        return "";
    }

    @Override
    public String toString() {
        return "Registration{"
                + "registrationId=" + registrationId
                + ", student=" + (student != null ? student.getFullName() : "null")
                + ", course=" + (course != null ? course.getCourseCode() : "null")
                + ", grade='" + grade + '\''
                + '}';
    }
}

package online_course_registration_system.Entity;

public class Student extends User implements Validatable {

    private String studentId;
    private String major;

    public Student() {
        super();
    }

    public Student(int userId, String studentId, String password, String fullName, String email, String major) {
        super(userId, password, fullName, email);
        this.studentId = studentId;
        this.major = major;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public boolean validate() {
        if (studentId == null || studentId.isEmpty()) {
            return false;
        }
        if (major == null || major.isEmpty()) {
            return false;
        }
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }
        if (email == null || email.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String getValidationError() {
        if (studentId == null || studentId.isEmpty()) {
            return "Student ID is required";
        }
        if (major == null || major.isEmpty()) {
            return "Major is required";
        }
        if (fullName == null || fullName.isEmpty()) {
            return "Full name is required";
        }
        if (email == null || email.isEmpty()) {
            return "Email is required";
        }
        return "";
    }

    @Override
    public String toString() {
        return "Student{"
                + "studentId='" + studentId + '\''
                + ", major='" + major + '\''
                + ", userId=" + userId
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + '}';
    }
}

package online_course_registration_system.Entity;

public class Professor extends User implements Validatable {

    private String professorId;

    public Professor() {
        super();
    }

    public Professor(int userId, String professorId, String password, String fullName, String email) {
        super(userId, password, fullName, email);
        this.professorId = professorId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    @Override
    public boolean validate() {
        if (professorId == null || professorId.isEmpty()) {
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
        if (professorId == null || professorId.isEmpty()) {
            return "Professor ID is required";
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
        return "Professor{"
                + "professorId='" + professorId + '\''
                + ", userId=" + userId
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + '}';
    }
}

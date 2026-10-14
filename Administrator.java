package online_course_registration_system.Entity;

public class Administrator extends User {
    private String adminId;

    public Administrator() {
        super();
    }

    public Administrator(int userId, String adminId, String password, String fullName, String email) {
        super(userId, password, fullName, email);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId='" + adminId + '\'' +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

package online_course_registration_system.Tools;

import online_course_registration_system.Entity.User;
import online_course_registration_system.Entity.Student;
import online_course_registration_system.Entity.Professor;
import online_course_registration_system.Entity.Administrator;

public class SessionManager {

    private static SessionManager instance;
    private User currentUser;
    private String currentUserType;
    private String expectedUserType;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        currentUser = user;
        if (user instanceof Student) {
            currentUserType = "STUDENT";
        } else if (user instanceof Professor) {
            currentUserType = "PROFESSOR";
        } else if (user instanceof Administrator) {
            currentUserType = "ADMINISTRATOR";
        }
    }

    public void logout() {
        currentUser = null;
        currentUserType = null;
        expectedUserType = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserType() {
        return currentUserType;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void setExpectedUserType(String type) {
        expectedUserType = type;
    }

    public String getExpectedUserType() {
        return expectedUserType;
    }
}

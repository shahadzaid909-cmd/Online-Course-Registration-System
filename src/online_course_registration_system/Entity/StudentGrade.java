package online_course_registration_system.Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentGrade {
    private int registrationId;
    private int studentPk;
    private String studentName;
    private StringProperty grade;

    public StudentGrade(int registrationId, int studentPk, String studentName, String grade) {
        this.registrationId = registrationId;
        this.studentPk = studentPk;
        this.studentName = studentName;
        this.grade = new SimpleStringProperty(grade != null ? grade : "");
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getStudentPk() {
        return studentPk;
    }

    public void setStudentPk(int studentPk) {
        this.studentPk = studentPk;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public StringProperty gradeProperty() {
        return grade;
    }
}

package online_course_registration_system.Entity;

public interface Validatable {
    boolean validate();
    String getValidationError();
}

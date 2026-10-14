# Online Course Registration System

A university course registration application built using Java, JavaFX, FXML, and Oracle Database.

## Features
- Student registration, course browsing, enrollment and grade viewing
- Professor course and grade management
- Administrator course and user management

## Setup
1. Open this NetBeans/Ant project in NetBeans with a compatible Java JDK and JavaFX SDK.
2. Configure the Oracle JDBC driver and JavaFX libraries for your local installation.
3. Create the database tables using `database_schema.sql` in a test Oracle database. **Review the DROP TABLE statements before executing**; they delete existing tables.
4. Set environment variables `DB_URL`, `DB_USER`, and **`DB_PASSWORD`** before launching. The URL defaults to `jdbc:oracle:thin:@localhost:1521:xe`, and the user defaults to `system`; use a dedicated least-privilege database account instead for real deployments.
5. Run `online_course_registration_system.MainApp` through NetBeans.

## Security
Database passwords are not stored in the prepared source package. The original local credentials were replaced by environment variables. If any previously committed password was reused elsewhere, change it.

## Notes
The project depends on external Oracle JDBC and JavaFX libraries, which are not bundled. Build and runtime functionality have not been verified.

## Upload status
The repository is being prepared. The complete source package has not yet been uploaded.

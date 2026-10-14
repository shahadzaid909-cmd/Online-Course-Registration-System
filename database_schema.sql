
DROP TABLE Registration CASCADE CONSTRAINTS;
DROP TABLE Course CASCADE CONSTRAINTS;
DROP TABLE Student CASCADE CONSTRAINTS;
DROP TABLE Professor CASCADE CONSTRAINTS;
DROP TABLE Administrator CASCADE CONSTRAINTS;

CREATE TABLE Administrator (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admin_id VARCHAR2(20) UNIQUE NOT NULL,
    password VARCHAR2(100) NOT NULL,
    full_name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL
);

CREATE TABLE Professor (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    professor_id VARCHAR2(20) UNIQUE NOT NULL,
    password VARCHAR2(100) NOT NULL,
    full_name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL
);

CREATE TABLE Student (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id VARCHAR2(20) UNIQUE NOT NULL,
    password VARCHAR2(100) NOT NULL,
    full_name VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    major VARCHAR2(100) DEFAULT 'Undeclared'
);

CREATE TABLE Course (
    course_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_code VARCHAR2(20) UNIQUE NOT NULL,
    course_name VARCHAR2(100) NOT NULL,
    professor_id NUMBER,
    CONSTRAINT fk_course_professor
        FOREIGN KEY (professor_id)
        REFERENCES Professor(user_id)
        ON DELETE SET NULL
);

CREATE TABLE Registration (
    registration_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id NUMBER NOT NULL,
    course_id NUMBER NOT NULL,
    grade VARCHAR2(2),
    CONSTRAINT fk_reg_student
        FOREIGN KEY (student_id)
        REFERENCES Student(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_reg_course
        FOREIGN KEY (course_id)
        REFERENCES Course(course_id)
        ON DELETE CASCADE,
    CONSTRAINT uk_student_course
        UNIQUE (student_id, course_id)
);

-- liquibase formatted sql

--changeset gabriel:1
CREATE INDEX student_name_index ON student(name);

--changeset gabriel:2
CREATE INDEX faculty_name_and_color ON faculty(name, color);
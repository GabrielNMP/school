package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int min, int max);

    List<Student> findByAge(int age);

    Student findStudentById(long id);

}

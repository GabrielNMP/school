package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT COUNT (*) FROM student", nativeQuery = true)
    int totalAmountOfStudent();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    double averageAgeOfStudent();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT :quantity ", nativeQuery = true)
    List<Student> lastOfStudent(@Param("quantity") int quantity);

}

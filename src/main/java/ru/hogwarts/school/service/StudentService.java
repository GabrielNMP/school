package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student renameStudent(Student student) {
        Optional<Student> findStudent = studentRepository.findById(student.getId());
        if (findStudent.isPresent()) {
            Student studentFromDb = findStudent.get();
            studentFromDb.setAge(student.getAge());
            studentFromDb.setName(student.getName());
            return studentRepository.save(studentFromDb);
        } else {
            return null;
        }
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> finedByAge(int min, int max){
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent (long id) {
        return studentRepository.findStudentById(id).getFaculty();
    }


    public int totalAmountOfStudent() {
        return studentRepository.totalAmountOfStudent();
    }

    public double averageAgeOfStudent() {
        return studentRepository.averageAgeOfStudent();
    }

    public List<Student> lastOfStudent(int quantity) {
        return studentRepository.lastOfStudent(quantity);
    }
}

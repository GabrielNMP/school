package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        logger.info("Method -createStudent- started");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Method -findStudent- started");
        return studentRepository.findById(id).orElseThrow();
    }

    public Student renameStudent(Student student) {
        Optional<Student> findStudent = studentRepository.findById(student.getId());
        if (findStudent.isPresent()) {
            Student studentFromDb = findStudent.get();
            studentFromDb.setAge(student.getAge());
            studentFromDb.setName(student.getName());
            logger.info("Method -renameStudent- started");
            return studentRepository.save(studentFromDb);
        } else {
            return null;
        }
    }

    public void deleteStudent(long id) {
        logger.info("Method -deleteStudent- was used");
        studentRepository.deleteById(id);
    }

    public Collection<Student> finedByAge(int min, int max){
        logger.info("Method -finedByAge(params min/max)- started");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent (long id) {
        logger.info("Method -getFacultyByStudent(pram - ID)- started");
        return studentRepository.findStudentById(id).getFaculty();
    }

    public int totalAmountOfStudent() {
        logger.info("Method -totalAmountOfStudent- started");
        return studentRepository.totalAmountOfStudent();
    }

    public double averageAgeOfStudent() {
        logger.info("Method -averageAgeOfStudent- started");
        return studentRepository.averageAgeOfStudent();
    }

    public List<Student> lastOfStudent(int quantity) {
        logger.info("Method -lastOfStudent(param quantity)- started");
        return studentRepository.lastOfStudent(quantity);
    }

    public List<String> studentNameStartA() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.charAt(0) == 'A')
                .sorted()
                .collect(Collectors.toList());
    }

    public OptionalDouble averageAgeOfStudents() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getAge)
                .mapToInt(value -> value)
                .average();
    }
}

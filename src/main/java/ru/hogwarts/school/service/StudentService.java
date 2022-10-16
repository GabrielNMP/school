package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public record StudentService(StudentRepository studentRepository) {

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElse(null);
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
        return studentRepository.getStudentById(id).getFaculty();
    }



}

package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentInfo (@PathVariable Long id){
        Student student = studentService.getStudent(id);
        if (student==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> finedStudentByParam (@RequestParam(required = false) int min,
                                                                   @RequestParam(required = false) int max) {
        if (min > 0 && max > 0) {
            return ResponseEntity.ok(studentService.finedByAge(min, max));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/faculty")
    public ResponseEntity<Faculty> finedFacultyByStudent (@RequestParam(required = false) long id) {
        if (id>0){
            return ResponseEntity.ok(studentService.getFacultyByStudent(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Student createStudent (@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> changeStudentInfo (@RequestBody Student student) {
        Student foundStudent = studentService.renameStudent(student);
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent (@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

}

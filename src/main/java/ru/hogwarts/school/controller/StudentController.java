package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentInfo (@PathVariable Long id){
        Student student = studentService.findStudent(id);
        if (student==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> finedStudentByParam (@RequestParam int min,
                                                                    @RequestParam int max) {
        if (min > 0 && max > 0) {
            return ResponseEntity.ok(studentService.finedByAge(min, max));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/faculty")
    public ResponseEntity<Faculty> finedFacultyByStudent (@RequestParam long id) {
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

    @GetMapping("/count")
    public  int totalAmountOfStudent(){
        return studentService.totalAmountOfStudent();
    }

    @GetMapping("/ageAverage")
    public  double averageAgeOfStudent(){
        return studentService.averageAgeOfStudent();
    }

    @GetMapping("/lastStudents")
    public List<Student> lastOfStudent(@RequestParam int quantity){
        return studentService.lastOfStudent(quantity);
    }
}

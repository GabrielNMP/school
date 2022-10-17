package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;


@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyInfo (@PathVariable Long id) {
         Faculty faculty = facultyService.findFaculty(id);
        if(faculty == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<Faculty> getFacultyByNameOrColor (@RequestParam String nameOrColor) {
        if (nameOrColor !=null && !nameOrColor.isBlank()) {
            return ResponseEntity.ok(facultyService.finedByNameColor(nameOrColor, nameOrColor));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> finedStudentByFaculty(@RequestParam long id) {

        if (id > 0){
            return ResponseEntity.ok(facultyService.getStudentByFaculty(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> changeFacultyInfo(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

}

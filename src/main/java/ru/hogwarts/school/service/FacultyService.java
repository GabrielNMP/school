package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        Optional<Faculty> findFaculty = facultyRepository.findById(faculty.getId());
        if (findFaculty.isPresent()) {
            Faculty facultyFromDb = findFaculty.get();
            facultyFromDb.setColor(faculty.getColor());
            facultyFromDb.setName(faculty.getName());
            return facultyRepository.save(facultyFromDb);
        } else {
            return null;
        }
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty finedByNameColor(String name, String color){
        return facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentByFaculty (long id) {
        return facultyRepository.getFacultyById(id).getStudents();
    }

}

package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Method -addFaculty- started");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Method -findFaculty- started");
        return facultyRepository.findById(id).orElseThrow();
    }

    public Faculty editFaculty(Faculty faculty) {
        Optional<Faculty> findFaculty = facultyRepository.findById(faculty.getId());
        if (findFaculty.isPresent()) {
            Faculty facultyFromDb = findFaculty.get();
            facultyFromDb.setColor(faculty.getColor());
            facultyFromDb.setName(faculty.getName());
            logger.info("Method -editFaculty- started ");
            return facultyRepository.save(facultyFromDb);
        } else {
            return null;
        }
    }

    public void deleteFaculty(long id) {
        logger.info("Method -addFaculty- was used");
        facultyRepository.deleteById(id);
    }

    public Faculty finedByNameColor(String name, String color){
        logger.info("Method -finedByNameColor- started");
        return facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentByFaculty (long id) {
        logger.info("Method -getStudentByFaculty- started");
        return facultyRepository.getFacultyById(id).getStudents();
    }

    public Optional<String> longestNameOfFaculty() {
        logger.info("Method -longestNameOfFaculty- started");
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length));
    }
}

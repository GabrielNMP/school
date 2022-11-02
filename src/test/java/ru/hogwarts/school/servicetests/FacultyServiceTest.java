package ru.hogwarts.school.servicetests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;


    @InjectMocks
    private FacultyService facultyService;


    @Test
    public void createFacultyTest() {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("Griffindor");
        testFaculty.setColor("yellow");
        testFaculty.setId(1L);
        when(facultyRepository.save(testFaculty)).thenReturn(testFaculty);
        assertThat(facultyRepository.save(testFaculty)).isEqualTo(facultyService.addFaculty(testFaculty));
    }

    @Test
    public void finedFacultyTest() {
        Faculty first = new Faculty();
        first.setName("Slyserin");
        first.setColor("green");
        first.setId(1L);

        Faculty second = new Faculty();
        second.setName("Puf");
        second.setColor("red");
        second.setId(2L);
        facultyService.addFaculty(first);
        facultyService.addFaculty(second);


        when(facultyRepository.findById(2L)).thenReturn(Optional.of(second));
        assertThat(facultyRepository.findById(2L)).contains(facultyService.findFaculty(2L));

    }

    @Test
    public void editFacultyTest() {
        Faculty puf = new Faculty();
        puf.setName("Puf");
        puf.setColor("red");
        puf.setId(2L);

        facultyService.addFaculty(puf);
        puf.setName("Puffenduy");

        assertThat(facultyRepository.save(puf)).isEqualTo(facultyService.editFaculty(puf));
    }

    @Test
    public void deleteFacultyTest() {
        Faculty puf = new Faculty();
        puf.setName("Puf");
        puf.setColor("red");
        puf.setId(2L);

        facultyService.addFaculty(puf);
        facultyService.deleteFaculty(2L);
        assertThat(facultyService).isNotNull();
    }
}

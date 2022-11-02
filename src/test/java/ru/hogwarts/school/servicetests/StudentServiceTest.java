package ru.hogwarts.school.servicetests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;


    @InjectMocks
    private StudentService studentService;


    @Test
    public void createStudentTest() {
        Student testStudent = new Student();
        testStudent.setName("Germi");
        testStudent.setAge(14);
        testStudent.setId(1L);
        when(studentRepository.save(testStudent)).thenReturn(testStudent);
        assertThat(studentRepository.save(testStudent)).isEqualTo(studentService.createStudent(testStudent));
    }

    @Test
    public void getStudentTest() {
        Student firstStudent = new Student();
        firstStudent.setName("Harry");
        firstStudent.setAge(16);
        firstStudent.setId(1L);

        Student secondStudent = new Student();
        secondStudent.setName("Germi");
        secondStudent.setAge(15);
        secondStudent.setId(2L);
        studentService.createStudent(firstStudent);
        studentService.createStudent(secondStudent);

        when(studentRepository.findById(2L)).thenReturn(Optional.of(secondStudent));
        assertThat(studentRepository.findById(2L)).contains(studentService.findStudent(2L));

    }

    @Test
    public void renameStudentTest() {
        Student ron = new Student();
        ron.setName("Ron");
        ron.setAge(15);
        ron.setId(3L);

        studentService.createStudent(ron);
        ron.setName("Harry");

        assertThat(studentRepository.save(ron)).isEqualTo(studentService.renameStudent(ron));
    }

    @Test
    public void deleteStudentTest() {
        Student testStudent = new Student();
        testStudent.setName("Sedric");
        testStudent.setAge(18);
        testStudent.setId(4L);

        studentService.createStudent(testStudent);
        studentService.deleteStudent(4L);
        assertThat(studentService).isNotNull();
    }








}

package ru.hogwarts.school.servicetests;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import static jdk.dynalink.linker.support.Guards.isNotNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createStudentTest(){
        Student student = given_student_with("studentName", 25);
        ResponseEntity<Student> response = when_sending_create_student_request(getUriBuilder().build().toUri(), student);
        then_student_has_been_created(response);
    }

    @Test
    public void getStudentById() {
        Student student = given_student_with("studentName", 25);
        ResponseEntity<Student> createResponse = when_sending_create_student_request(getUriBuilder().build().toUri(), student);
        then_student_has_been_created(createResponse);

        Student createdStudent = createResponse.getBody();
        then_student_with_id_has_been_found(createdStudent.getId(), createdStudent);
    }

    @Test
    public void finedStudentByAge () {
        Student student1 = given_student_with("studentN3", 18);
        Student student2 = given_student_with("studentN1", 20);
        Student student3 = given_student_with("studentN2", 19);
        Student student4 = given_student_with("studentN4", 21);

        when_sending_create_student_request(getUriBuilder().build().toUri(), student1);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student2);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student3);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student4);

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("age", "19");
        then_students_are_found_by_criteria(queryParam, student3);
    }

    @Test
    public void findByAgeBetween(){
        Student student1 = given_student_with("studentN3", 18);
        Student student2 = given_student_with("studentN1", 20);
        Student student3 = given_student_with("studentN2", 19);
        Student student4 = given_student_with("studentN4", 21);

        when_sending_create_student_request(getUriBuilder().build().toUri(), student1);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student2);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student3);
        when_sending_create_student_request(getUriBuilder().build().toUri(), student4);

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("min", "19");
        queryParam.add("max", "20");
        then_students_are_found_by_criteria(queryParam, student2, student3);
    }

    @Test
    public void putStudentTest() {
        Student student = given_student_with("studentName", 19);

        ResponseEntity<Student> responseEntity = when_sending_create_student_request(getUriBuilder().build().toUri(), student);
        then_student_has_been_created(responseEntity);
        Student createdStudent = responseEntity.getBody();
         when_updating_student(createdStudent, 20, "newName");
         then_student_has_been_updated(createdStudent, 20, "newName");

    }

    @Test
    public void deleteStudentTest() {
        Student student = given_student_with("studentName", 19);

        ResponseEntity<Student> responseEntity = when_sending_create_student_request(getUriBuilder().build().toUri(), student);
        then_student_has_been_created(responseEntity);
        Student createdStudent = responseEntity.getBody();

        when_deleting_student(createdStudent);
        then_student_not_found(createdStudent);
    }


    private void then_students_are_found_by_criteria(MultiValueMap<String, String> queryParam, Student... students) {
        URI uri = getUriBuilder().queryParams(queryParam).build().toUri();

        ResponseEntity<Set<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Set<Student>>() {
                });

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Set<Student> actualResult = response.getBody();
        resetIds(actualResult);
        assertThat(actualResult).isEqualTo(students);   }

    private void when_updating_student(Student createStudent, int newAge, String newName) {
        createStudent.setAge(newAge);
        createStudent.setName(newName);

        restTemplate.put(getUriBuilder().build().toUri(), createStudent);
    }


    private Student given_student_with(String name, int age){
        return  new Student(name, age);
    }

    private ResponseEntity<Student> when_sending_create_student_request(URI uri, Student student) {
        return restTemplate.postForEntity(uri,student, Student.class);

    }
    private void then_student_has_been_created(ResponseEntity<Student> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    private void  then_student_with_id_has_been_found(Long studentId, Student student) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(studentId).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);
        assertThat(response.getBody()).isEqualTo(student);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void  then_student_has_been_updated(Student createdStudent, int newAge, String newName) {
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> updatedStudentRs = restTemplate.getForEntity(getUri, Student.class);

        assertThat(updatedStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedStudentRs.getBody()).isNotNull();
        assertThat(updatedStudentRs.getBody().getAge()).isEqualTo(newAge);
        assertThat(updatedStudentRs.getBody().getName()).isEqualTo(newName);
    }

    private void resetIds(Collection<Student> students){
        students.forEach(it-> it.setId(null));
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/hogwarts/student");
    }

    private void when_deleting_student(Student createdStudent) {
        restTemplate.delete(getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri());

    }

    private void then_student_not_found(Student createdStudent) {
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> emptyRs = restTemplate.getForEntity(getUri, Student.class);

        assertThat(emptyRs.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}

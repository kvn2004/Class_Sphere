package lk.vihanganimsara.classsphere.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.service.StudentService;
import lk.vihanganimsara.classsphere.service.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    private final StudentService studentService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Student> registerStudent(
            @RequestPart("student") String studentJson,
            @RequestPart("photo") MultipartFile photo) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            StudentDto dto = mapper.readValue(studentJson, StudentDto.class);

            boolean saved = studentService.save(dto, photo);

            if (!saved) {
                log.error("Error while saving student");
            }
            log.info("Student successfully registered");
            return new ResponseEntity(new ApiResponse(200, "Student Saved", saved), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Student> updateStudent(
            @RequestPart("student") String studentJson,
            @RequestPart("photo") MultipartFile photo) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            StudentDto dto = mapper.readValue(studentJson, StudentDto.class);

            studentService.update(dto, photo);

            log.info("Student Updated");
            return new ResponseEntity(new ApiResponse(200, "Student Saved", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }
    @GetMapping(value = "/all")
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<StudentDto> all = studentService.getAll();
        if (all == null) {
            log.error("Error while getting all students");
        } else {
            log.info("All students successfully retrieved");
        }
        return ResponseEntity.ok(new ApiResponse(200, "success", all)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String id) {
        studentService.deleteById(id);
        return new ResponseEntity(new ApiResponse(
                200,
                "success",
                true),
                HttpStatus.OK);
    }


}

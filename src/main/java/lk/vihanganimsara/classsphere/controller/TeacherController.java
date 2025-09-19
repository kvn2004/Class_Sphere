package lk.vihanganimsara.classsphere.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.dto.TeacherDto;
import lk.vihanganimsara.classsphere.entity.Student;
import lk.vihanganimsara.classsphere.entity.Teacher;
import lk.vihanganimsara.classsphere.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Student> registerStudent(
            @RequestPart("teacher") String teacherJson,
            @RequestPart("photo") MultipartFile photo) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            TeacherDto dto = mapper.readValue(teacherJson, TeacherDto.class);

            boolean saved = teacherService.save(dto, photo);

            if (!saved) {
                log.error("Error while saving Teacher");
            }
            log.info("Teacher successfully registered");
            return new ResponseEntity(new ApiResponse(
                    200,
                    "Teacher Saved",
                    saved),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Student> updateStudent(
            @RequestPart("teacher") String teacherJson,
            @RequestPart(value = "photo",required = false) MultipartFile photo) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            TeacherDto dto = mapper.readValue(teacherJson, TeacherDto.class);

            teacherService.update(dto, photo);

            log.info("Teacher Updated");
            return new ResponseEntity(new ApiResponse(
                    200,
                    "Teacher Update",
                    null),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @GetMapping(value = "/all")
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<StudentDto> all = teacherService.getAll();
        if (all == null) {
            log.error("Error while getting all teachers");
        } else {
            log.info("All teachers successfully retrieved");
        }
        return ResponseEntity.ok(new ApiResponse(
                200,
                "success",
                all)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String id) {
        teacherService.deleteById(id);
        return new ResponseEntity(new ApiResponse(
                200,
                "success",
                true),
                HttpStatus.OK);
    }
}

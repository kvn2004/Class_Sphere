package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.dto.StudentDto;
import lk.vihanganimsara.classsphere.dto.SubjectDto;
import lk.vihanganimsara.classsphere.service.HallService;
import lk.vihanganimsara.classsphere.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("api/subjects")
@Slf4j
public class SubjectController {
    private final SubjectService subjectService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse> createSubject(@RequestBody SubjectDto dto) {
        subjectService.save(dto);
        return new ResponseEntity<>(
                new ApiResponse(
                        201,
                        "Subject created successfully",
                        true),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateSubject(@RequestBody SubjectDto dto) {

        subjectService.update(dto);
        return new ResponseEntity<>(
                new ApiResponse(
                        200,
                        "Subject updated successfully",
                        true),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteSubject(@PathVariable String id) {
        subjectService.delete(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        200,
                        "Subject deleted successfully",
                        true),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/all")
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<SubjectDto> all = subjectService.getAll();
        if (all == null) {
            log.error("error while getting all subjects");
        } else {
            log.info("successfully retrieved all subjects");
        }
        return ResponseEntity.ok(new ApiResponse(
                200,
                "success",
                all)
        );
    }


}

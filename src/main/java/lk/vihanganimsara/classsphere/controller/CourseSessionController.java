package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.CourseSessionDto;

import lk.vihanganimsara.classsphere.service.ClassSessionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class CourseSessionController {

    private final ClassSessionsService sessionService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody CourseSessionDto dto) {
        sessionService.save(dto);
        return new ResponseEntity<>(new ApiResponse(201, "Session created successfully", true), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody CourseSessionDto dto) {
        sessionService.update(dto);
        return new ResponseEntity<>(new ApiResponse(200, "Session updated successfully", true), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        sessionService.delete(id);
        return new ResponseEntity<>(new ApiResponse(200, "Session deleted successfully", true), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CourseSessionDto> findById(@PathVariable Integer id) {
//        return ResponseEntity.ok(sessionService.findById(id));
//    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseSessionDto>> findAllByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(sessionService.findAllByCourse(courseId));
    }
}

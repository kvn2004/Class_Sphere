package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.CourseDto;

import lk.vihanganimsara.classsphere.service.ClasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ClasesService courseService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody CourseDto dto) {
        courseService.save(dto);
        return new ResponseEntity<>(new ApiResponse(201, "Course created successfully", true), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody CourseDto dto) {
        courseService.update(dto);
        return new ResponseEntity<>(new ApiResponse(200, "Course updated successfully", true), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        courseService.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(200, "Course deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDto>> findAll() {
        return ResponseEntity.ok(courseService.getAll());
    }
}

package lk.vihanganimsara.classsphere.controller;

import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.HallDto;
import lk.vihanganimsara.classsphere.service.HallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class HallsController {
    private final HallService hallService;

    @PostMapping(value = "/save")
    public ResponseEntity<ApiResponse> createHall(@RequestBody HallDto hallDto) {
        hallService.save(hallDto);
        return new ResponseEntity<>(
                new ApiResponse(201, "Hall created successfully", true),
                HttpStatus.CREATED
        );

    }

    @PutMapping(value="/update")
    public ResponseEntity<ApiResponse> updateHall(@RequestBody HallDto hallDto) {
        hallService.update(hallDto);
        return new ResponseEntity<>(
                new ApiResponse(200, "Hall updated successfully", true),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllHalls() {
        List<HallDto> halls = hallService.getAll();
        return new ResponseEntity<>(
                new ApiResponse(200, "Halls retrieved successfully",  halls),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHall(@PathVariable String id) {
        hallService.deleteById(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Hall deleted successfully", true),
                HttpStatus.OK
        );
    }
}

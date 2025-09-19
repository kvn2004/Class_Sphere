package lk.vihanganimsara.classsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSessionDto {
    private String sessionId;
    private String courseId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String hallId;
    private String notes;
    private String courseTitle;


}
